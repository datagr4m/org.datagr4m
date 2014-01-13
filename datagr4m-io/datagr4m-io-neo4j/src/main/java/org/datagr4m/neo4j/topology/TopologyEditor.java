package org.datagr4m.neo4j.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.datagr4m.io.xml.generated.dataprism.Andby;
import org.datagr4m.io.xml.generated.dataprism.Attribute;
import org.datagr4m.io.xml.generated.dataprism.Dataprism;
import org.datagr4m.io.xml.generated.dataprism.Groupby;
import org.datagr4m.io.xml.generated.dataprism.Relation;
import org.datagr4m.io.xml.generated.dataprism.Remove;
import org.datagr4m.io.xml.generated.dataprism.Rule;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.NodeType;

import com.google.common.collect.Multimap;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

public class TopologyEditor {
    private static final String KEYWORD_TRIGGER_COLLAPSE = "Singles"; // @see HierarchicalTopologyModelFactory
    
	private static final String RULE_NAME_EMPTY_GROUPS = "empty_groups";
	private static final String RULE_NAME_EDGEMADEOF = "edgemadeof";
	private static final String RULE_NAME_BYTYPE = "type";
	private static final String RULE_NAME_NODE_IN_NO_GROUP = "node_in_no_group";
	private static final String RULE_NAME_ISOLATED = "isolated";
	private static final String RULE_SCOPE_TOPOLOGY = "topology";
	private static final String RULE_SCOPE_GRAPH = "graph";
	
	protected Neo4jTopology topology;
    protected List<Group<IPropertyNode>> recentGroups;

    public static class GroupByRelation {
        public GroupByRelation(String sourceType, String relationType, String targetType) {
            this.sourceType = sourceType;
            this.relationType = relationType;
            this.targetType = targetType;
        }

        String sourceType;
        String relationType;
        String targetType;

        @Override
		public String toString() {
            return sourceType + " " + relationType + " " + targetType;
        }
    }

    public static class GroupByAttribute {
        public GroupByAttribute(String source, String attribute) {
            this.source = source;
            this.attribute = attribute;
        }

        String source;
        String attribute;

        @Override
		public String toString() {
            return source + " " + attribute;
        }
    }
    
    public static class GroupByRule {
        public GroupByRule(String name) {
            this.name = name;
        }

        String name;

        @Override
		public String toString() {
            return name;
        }
    }

    public TopologyEditor(Neo4jTopology neo4jTopology) {
        this.topology = neo4jTopology;
    }
    
    
    /* APPLY PROJECTIONS */

    public TopologyEditor apply(Dataprism dp) {
        for (Groupby gb : dp.getGroupby()) {
            applyGroupBy(gb);
        }
        for (Remove remove : dp.getRemove()) {
            applyRemove(remove);
        }
        return this;
    }

    public void applyGroupBy(Groupby gb) {
        Attribute att = gb.getAttribute();
        Relation rel = gb.getRelation();
        Rule rule = gb.getRule();
        Object type = gb.getType();
        
        if (rel != null) {
            GroupByRelation groupBy = new GroupByRelation(rel.getSource(), rel.getType(), rel.getTarget());
            groupBy(groupBy);
            applyAndBy(gb.getAndby());
        }
        if(att != null){
            GroupByAttribute groupBy = new GroupByAttribute(att.getFor(), att.getName());
            groupBy(groupBy);
            applyAndBy(gb.getAndby());
        }
        if(type != null){
        	groupByType();
        	applyAndBy(gb.getAndby());
        }
        
        
        if (rule != null) {
            if (rule.getName().equals(RULE_NAME_NODE_IN_NO_GROUP)) {
                groupItemsWithNoGroup();
            }
        }
    }

    public void applyAndBy(Andby ab) {
        if (ab != null) {
        	Relation rel2 = ab.getRelation();
            Attribute att2 = ab.getAttribute();
            Rule rul2 = ab.getRule();
            
            if (rel2 != null){
                GroupByRelation groupBy2 = new GroupByRelation(rel2.getSource(), rel2.getType(), rel2.getTarget());
                andBy(groupBy2);
            }
            if(att2!=null){
                GroupByAttribute groupBy2 = new GroupByAttribute(att2.getFor(), att2.getName());
                andBy(groupBy2);
            }
            if(rul2!=null){
            	GroupByRule groupBy2 = new GroupByRule(rul2.getName());
            	andBy(groupBy2);
            }
        }
    }
    
    public void applyRemove(Remove remove) {
        Rule rule = remove.getRule();
        if (remove.getFrom().equals(RULE_SCOPE_GRAPH)) {
            if (rule.getName().equals(RULE_NAME_NODE_IN_NO_GROUP)) {
                removeUngroupedNodes();
            } else if (rule.getName().startsWith(RULE_NAME_BYTYPE + ":")) {
                String type = rule.getName().substring(5);
                removeNodesOfType(type);
            }
            // edgemadeof:Character:Character
            else if (rule.getName().startsWith(RULE_NAME_EDGEMADEOF+":")) {
                String infos = rule.getName().substring(11);
                String[] types = infos.split(":");
                removeEdgeMadeOf(types[0], types[1]);
            }
        } else if (remove.getFrom().equals(RULE_SCOPE_TOPOLOGY)) {
            if (rule.getName().equals(RULE_NAME_EMPTY_GROUPS)) {
                removeEmptyGroups();
            }
        }
    }
    
    /* */

    /**
     * Where relation is something like [A]-[COMES_FROM]->[B]
     * 
     * ou le noeud [B] peut contenir plusieur [any]
     * 
     * 
     * Supporter la version non orientée: détecter si un seul axe de groupement.
     * si deux possible, prévenir?
     * 
     * 
     * Attention cette implementation est inefficiace: utilise la mémoire en
     * plus créée par le INeo4jNode pour connaitre ses relations indexées
     * 
     * 
     * @param relation
     */
    public TopologyEditor groupBy(GroupByRelation groupBy) {
        Logger.getLogger(TopologyEditor.class).info("groupBy relation " + groupBy);
        Collection<IPropertyNode> scope = topology.getGraph().getVertices();

        groupBy(groupBy, scope);

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());
        return this;
    }

    protected TopologyEditor groupBy(GroupByRelation groupBy, Collection<IPropertyNode> scope) {
        Map<IPropertyNode, Group<IPropertyNode>> groupAvatars = new HashMap<IPropertyNode, Group<IPropertyNode>>();

        List<IPropertyNode> removeLater = new ArrayList<IPropertyNode>();

        for (IPropertyNode source : scope) {
            // source type match
            if (source.getType().getName().equals(groupBy.sourceType)) {
                Multimap<String, IPropertyNode> info = source.getInformationNodes();
                if (info != null) {
                    for (String t : info.keySet()) {
                        // relation match
                        if (t.equals(groupBy.relationType)) {
                            Collection<IPropertyNode> nodes = info.get(t);
                            for (IPropertyNode target : nodes) {
                                // target type match
                                if (target.getType().getName().equals(groupBy.targetType)) {
                                    Group<IPropertyNode> groupAvatar = groupAvatars.get(target);

                                    // init group if required
                                    if (groupAvatar == null) {
                                        groupAvatar = new Group<IPropertyNode>();
                                        groupAvatar.setName(target.getLabel());
                                        groupAvatar.setType("Group<" + target.getType().getName() + ">");
                                        groupAvatars.put(target, groupAvatar);
                                        topology.addGroup(groupAvatar);
                                    }

                                    groupAvatar.add(source);
                                    removeLater.add(target);
                                }
                            }
                        }
                    }
                }
            }
        }

        recentGroups = new ArrayList<Group<IPropertyNode>>(groupAvatars.values());

        // remove node than have changed to
        for (IPropertyNode remove : removeLater)
            topology.getGraph().removeVertex(remove);

        return this;
    }
    
    /* */

    public TopologyEditor andBy(GroupByRelation groupBy) {
        Logger.getLogger(TopologyEditor.class).info("andBy relation " + groupBy);
        for (Group<IPropertyNode> group : recentGroups) {
            andBy(groupBy, group);
        }

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());
        return this;
    }

    /**
     * @param groupBy
     * @param srcGroup
     * @return
     */
    protected TopologyEditor andBy(GroupByRelation groupBy, Group<IPropertyNode> srcGroup) {
        Map<IPropertyNode, Group<IPropertyNode>> groupAvatars = new HashMap<IPropertyNode, Group<IPropertyNode>>();

        List<IPropertyNode> removeFromGroupLater = new ArrayList<IPropertyNode>();
        List<IPropertyNode> removeFromTopologyLater = new ArrayList<IPropertyNode>();

        Iterator<IPropertyNode> it = srcGroup.iterator();

        while (it.hasNext()) {
            IPropertyNode source = it.next();

            // source type match
            if (source.getType().getName().equals(groupBy.sourceType)) {
                Multimap<String, IPropertyNode> info = source.getInformationNodes();
                if (info != null) {
                    for (String t : info.keySet()) {
                        // relation match
                        if (t.equals(groupBy.relationType)) {
                            Collection<IPropertyNode> nodes = info.get(t);
                            for (IPropertyNode target : nodes) {
                                // target type match
                                if (target.getType().getName().equals(groupBy.targetType)) {
                                    Group<IPropertyNode> groupAvatar = groupAvatars.get(target);

                                    // init group if required
                                    if (groupAvatar == null) {
                                        groupAvatar = new Group<IPropertyNode>();
                                        groupAvatar.setName(target.getLabel());
                                        groupAvatar.setType("Group<" + target.getType().getName() + ">");

                                        groupAvatars.put(target, groupAvatar);
                                        srcGroup.addSubGroup(groupAvatar);
                                        Logger.getLogger(TopologyEditor.class).info("adding " +
                                        groupAvatar + " to " + srcGroup);
                                        // topology.addGroup(groupAvatar);
                                    }
                                    groupAvatar.add(source);
                                    removeFromGroupLater.add(source);
                                    removeFromTopologyLater.add(target);
                                }
                            }
                        }
                    }
                }
            }

        }

        recentGroups = new ArrayList<Group<IPropertyNode>>(groupAvatars.values());

        // remove node than have changed to
        for (IPropertyNode remove : removeFromGroupLater) {
            srcGroup.remove(remove);
        }
        for (IPropertyNode remove : removeFromTopologyLater) {
            topology.getGraph().removeVertex(remove);
        }

        return this;
    }

    /* */

    public TopologyEditor groupBy(GroupByAttribute groupBy) {
        Logger.getLogger(TopologyEditor.class).info("groupBy attribute " + groupBy);
        Collection<IPropertyNode> scope = topology.getGraph().getVertices();

        groupBy(groupBy, scope);

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());
        return this;
    }

    protected TopologyEditor groupBy(GroupByAttribute groupBy, Collection<IPropertyNode> scope) {
        Map<String, Group<IPropertyNode>> attributeGroups = new HashMap<String, Group<IPropertyNode>>();
        
        for(IPropertyNode n: scope){
            if(n.getType().getName().equals(groupBy.source)){
                if(n.getType().getProperties().contains(groupBy.attribute)){
                    String value = n.getPropertyValue(groupBy.attribute);
                    if(value==null)
                        value="";

                    Group<IPropertyNode> group = attributeGroups.get(value);
                    if(group==null){
                        group = new Group<IPropertyNode>(value);
                        attributeGroups.put(value, group);
                    }
                    group.add(n);
                }
            }
        }
        
        recentGroups = new ArrayList<Group<IPropertyNode>>(attributeGroups.values());

        topology.getGroups().addAll(attributeGroups.values());
        topology.index();
        
        return this;
    }
    
    public TopologyEditor groupByType() {
        Logger.getLogger(TopologyEditor.class).info("groupBy node type");
        Collection<IPropertyNode> scope = topology.getGraph().getVertices();

        groupByType(scope);

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());
        return this;
    }

    protected TopologyEditor groupByType(Collection<IPropertyNode> scope) {
        Map<NodeType, Group<IPropertyNode>> typeGroups = new HashMap<NodeType, Group<IPropertyNode>>();
        
        for(IPropertyNode n: scope){
        	NodeType t = n.getType();
        	
        	Group<IPropertyNode> group = typeGroups.get(t);
        	if(group==null){
        		group = new Group<IPropertyNode>(t.getName());
        		typeGroups.put(t, group);
        	}
        	group.add(n);
        }
        
        recentGroups = new ArrayList<Group<IPropertyNode>>(typeGroups.values());

        topology.getGroups().addAll(typeGroups.values());
        topology.index();
        
        return this;
    }

    /* */

    public TopologyEditor andBy(GroupByAttribute groupBy) {
    	Logger.getLogger(TopologyEditor.class).info("andBy relation " + groupBy);
        for (Group<IPropertyNode> group : recentGroups) {
            andBy(groupBy, group);
        }

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());
        return this;
    }

    protected TopologyEditor andBy(GroupByAttribute groupBy, Group<IPropertyNode> srcGroup) {
        Map<String, Group<IPropertyNode>> attributeGroups = new HashMap<String, Group<IPropertyNode>>();
        List<IPropertyNode> removeFromGroupLater = new ArrayList<IPropertyNode>();
        
        for(IPropertyNode n: srcGroup){
            if(n.getType().getName().equals(groupBy.source)){
                if(n.getType().getProperties().contains(groupBy.attribute)){
                    String value = n.getPropertyValue(groupBy.attribute);
                    if(value==null)
                        value="";

                    Group<IPropertyNode> group = attributeGroups.get(value);
                    if(group==null){
                        group = new Group<IPropertyNode>(value);
                        attributeGroups.put(value, group);
                    }
                    group.add(n);
                    removeFromGroupLater.add(n);
                }
            }
        }
        
        for(IPropertyNode n: removeFromGroupLater)
            srcGroup.remove(n);
        
        recentGroups = new ArrayList<Group<IPropertyNode>>(attributeGroups.values());
        srcGroup.addSubGroups(new ArrayList<Group<IPropertyNode>>(attributeGroups.values()));
        topology.index();
        
        return this;
    }
    
    /* */

    public TopologyEditor andBy(GroupByRule groupBy) {
    	Logger.getLogger(TopologyEditor.class).info("andBy rule " + groupBy);
        for (Group<IPropertyNode> group : recentGroups) {
            andBy(groupBy, group);
        }

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());
        return this;
    }
    
    protected TopologyEditor andBy(GroupByRule groupBy, Group<IPropertyNode> srcGroup) {
    	Group<IPropertyNode> group = new Group<IPropertyNode>(srcGroup.getName()+" (no-neighbour)");
    	group.setType(srcGroup.getType() + "-" + KEYWORD_TRIGGER_COLLAPSE);
        List<IPropertyNode> removeFromGroupLater = new ArrayList<IPropertyNode>();
        
        if(RULE_NAME_ISOLATED.equals(groupBy.name)){
        	for(IPropertyNode n: srcGroup){
                if(topology.getGraph().getNeighborCount(n)==0){
                    group.add(n);
                    removeFromGroupLater.add(n);
                }
            }
        }
        
        for(IPropertyNode n: removeFromGroupLater)
            srcGroup.remove(n);
        
        //recentGroups = new ArrayList<Group<IPropertyNode>>(group);
        srcGroup.addSubGroup(group);
        topology.index();
        
        return this;
    }

    /* */

    public TopologyEditor removeUngroupedNodes() {

        // remove all non grouped item
        Group<IPropertyNode> singles = topology.getItemsWithoutGroup();

        Logger.getLogger(TopologyEditor.class).info("removeSingles " + singles.size());

        for (IPropertyNode single : singles) {
            topology.getGraph().removeVertex(single);
        }

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());

        return this;
    }

    public TopologyEditor groupItemsWithNoGroup() {
        Logger.getLogger(TopologyEditor.class).info("groupItemsWithNoGroup");

        // remove all non grouped item
        Group<IPropertyNode> singles = topology.getItemsWithoutGroup();
        singles.setType("groupSingles()");
        singles.setType("Group<Single>");
        topology.addGroup(singles);

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());

        return this;
    }

    public TopologyEditor removeEmptyGroups() {
        List<Group<IPropertyNode>> deleted = topology.deleteEmptyGroups();

        Logger.getLogger(TopologyEditor.class).info("deleteEmptyGroups " + deleted.size() + deleted);

        // update and exit
        topology.index();
        Logger.getLogger(TopologyEditor.class).info(topology.getStatistics());

        return this;
    }

    public TopologyEditor removeNodesOfType(String type) {
        Collection<IPropertyNode> nodes = topology.getGraph().getVertices();
        List<IPropertyNode> remove = new ArrayList<IPropertyNode>();
        for (IPropertyNode node : nodes) {
            NodeType t = node.getType();
            if (t.getName().equals(type)) {
                remove.add(node);
            }
        }
        for (IPropertyNode node : remove) {
            Logger.getLogger(TopologyEditor.class).info("ignore " + node + " type:" + node.getType().getName());
            topology.getGraph().removeVertex(node);
        }
        topology.index();

        return this;
    }

    public TopologyEditor removeEdgeMadeOf(String typeNode1, String typeNode2) {
        Graph<IPropertyNode, IPropertyEdge> graph = topology.getGraph();
        Collection<IPropertyEdge> edges = graph.getEdges();
        List<IPropertyEdge> remove = new ArrayList<IPropertyEdge>();
        for (IPropertyEdge edge : edges) {
            Pair<IPropertyNode> nodes = graph.getEndpoints(edge);
            String n1 = nodes.getFirst().getType().getName();
            boolean firstIsOk = n1.equals(typeNode1);
            if (!firstIsOk)
                continue;

            String n2 = nodes.getSecond().getType().getName();
            boolean secondIsOk = n2.equals(typeNode2);
            if (!secondIsOk)
                continue;
            remove.add(edge);
        }

        for (IPropertyEdge e : remove) {
            Logger.getLogger(TopologyEditor.class).info("ignore edge:" + e.getTypeName() + " | " + typeNode1 + ":" + graph.getEndpoints(e).getFirst() + " | " + typeNode2 + ":" + graph.getEndpoints(e).getSecond());

            graph.removeEdge(e);
        }

        topology.index();
        return this;
    }
}

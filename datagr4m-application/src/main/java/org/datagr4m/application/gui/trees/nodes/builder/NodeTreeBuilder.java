package org.datagr4m.application.gui.trees.nodes.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.datagr4m.topology.graph.IPropertyNode;
import org.neo4j.graphdb.RelationshipType;

import com.google.common.collect.Multimap;

public class NodeTreeBuilder {
    public int n = 0;
    public INodeVisitPredicate relationshipVisitor = new VisitAllWithMaxDepth(2);

    public DefaultTreeModel buildModel(String rootLabel, Collection<IPropertyNode> nodes) {
        n = 0;
        return new DefaultTreeModel(build(rootLabel, nodes));
    }
    
    public DefaultMutableTreeNode build(String rootLabel, Collection<IPropertyNode> nodes) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootLabel);
        Set<IPropertyNode> traversed = new HashSet<IPropertyNode>();
        
        List<IPropertyNode> rankedNodes = new ArrayList<IPropertyNode>(nodes);
        Collections.sort(rankedNodes, compareNode());

        for (IPropertyNode n : rankedNodes) {
            DefaultMutableTreeNode node = build(n, traversed, 0);
            if(node!=null &&! root.isNodeAncestor(node)){
                //System.out.println(Utils.blanks(0) + node);

                root.add(node);
            }
        }
        return root;
    }

    /**
     * 
     * @param node the node for which we need a GUI tree node with descendants
     * @param traversed the set of node already in the hierarchy (i.e. path to root)
     * @param depth number of time the function recursed
     * @return
     */
    public DefaultMutableTreeNode build(IPropertyNode node, Set<IPropertyNode> traversed, int depth) {
        if(isInt(node.getLabel())){
            return null;
        }
        
        Set<IPropertyNode> traversed2 = new HashSet<IPropertyNode>(traversed);
        traversed2.add(node);

        DefaultMutableTreeNode nodeTreeNode = new DefaultMutableTreeNode(node);
        
        if (relationshipVisitor.continueWith(traversed2, node, depth)) {
            Multimap<String, IPropertyNode> allChildren = node.getInformationNodes();
            
            if (allChildren != null){
                List<String> relationshipTypes = new ArrayList<String>(allChildren.keySet());
                Collections.sort(relationshipTypes);

                for (String type : relationshipTypes) {
                    boolean foundAnyRelevantChild = false; // don't want cycle,
                                                           // so avoid continue
                                                           // on traversed items

                    DefaultMutableTreeNode typeTreeNode = new DefaultMutableTreeNode(type.toString());
                    List<IPropertyNode> children = new ArrayList<IPropertyNode>(allChildren.get(type));
                    Collections.sort(children, compareNode());
                    
                    for (IPropertyNode child : children) {
                        if (!traversed2.contains(child)) {
                            DefaultMutableTreeNode childTreeNode = build(child, traversed2, depth+1);
                            // DefaultMutableTreeNode childNode = build(child);
                            if(childTreeNode!=null){
                                typeTreeNode.add(childTreeNode);
                                foundAnyRelevantChild = true;
                            }
                        }
                    }

                    if (foundAnyRelevantChild)
                        nodeTreeNode.add(typeTreeNode);
                }
            }
        }
        
        n++;
        return nodeTreeNode;
    }
    
    public Comparator<RelationshipType> compareRelationshipType(){
        return new Comparator<RelationshipType>(){
            @Override
            public int compare(RelationshipType arg0, RelationshipType arg1) {
                return arg0.name().compareTo(arg1.name());
            }
        };
    }
    
    public Comparator<IPropertyNode> compareNode(){
        return new Comparator<IPropertyNode>(){
            @Override
            public int compare(IPropertyNode n1, IPropertyNode n2) {
                if(n1.getType().equals(n2.getType())){
                    return n1.getLabel().compareTo(n2.getLabel());
                }
                else{
                    return n1.getType().getName().compareTo(n2.getType().getName());
                }
            }
        };
    }
    

    private boolean isInt(String label) {
        try{
            Integer.parseInt(label);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}

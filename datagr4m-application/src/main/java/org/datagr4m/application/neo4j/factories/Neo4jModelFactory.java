package org.datagr4m.application.neo4j.factories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.datagr4m.application.neo4j.model.Neo4jEdgeInfo;
import org.datagr4m.application.neo4j.model.Neo4jHierarchicalEdgeModel;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.items.DefaultBoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.DefaultEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ModelEdgesVisitor;
import org.datagr4m.neo4j.topology.graph.Neo4jGraphModel;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.graph.GenericGraphModel;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.neo4j.graphdb.RelationshipType;

public class Neo4jModelFactory extends HierarchicalTopologyModelFactory<IPropertyNode,IPropertyEdge>{
    protected Neo4jGraphModel graphModel;

    public Neo4jModelFactory(){
        this(null);
    }
    public Neo4jModelFactory(Neo4jGraphModel model){
        super();
        this.graphModel = model;
    }
    
    @Override
    public IBoundedItem getItemLayoutModel(IPropertyNode node) {
        return new DefaultBoundedItemIcon(node, GenericGraphModel.getIdentity(node));
    }
   
    @Override
    protected IHierarchicalEdgeModel getHierarchicalEdgeModel() {
        return new Neo4jHierarchicalEdgeModel();
    }

    @Override
    protected void createEdgeModel(Topology<IPropertyNode,IPropertyEdge> topology, IHierarchicalGraphModel model) {
        ModelEdgesVisitor edgeFinder = new ModelEdgesVisitor();
        edgeFinder.visit(model);
        IHierarchicalEdgeModel edgeModel = getHierarchicalEdgeModel();
        edgeModel.build(topology, model);
        createTubeLabels(edgeModel); // specific to this factory
        model.setEdgeModel(edgeModel);
    }
    
    /* */
    
    protected void createTubeLabels(IHierarchicalEdgeModel tubeModel) {
        for(Tube t: tubeModel.getRootTubes()){
            List<IEdgeInfo> iei = t.flattenInfos();
            Set<RelationshipType> rtypes = computeAvailableRelationshipTypes(iei);
            final String s = summarizeRelationships(rtypes);
            t.setEdgeInfo(new DefaultEdgeInfo(s));
        }
    }
    
    protected Set<RelationshipType> computeAvailableRelationshipTypes(List<IEdgeInfo> iei) {
        Set<RelationshipType> rtypes = new HashSet<RelationshipType>();
        for(IEdgeInfo ie: iei){
            Neo4jEdgeInfo nei = (Neo4jEdgeInfo)ie;
            rtypes.add(nei.getRelationship().getType());
        }
        return rtypes;
    }
    
    protected String summarizeRelationships(Set<RelationshipType> rtypes) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        int m = rtypes.size();
        for(RelationshipType rtype: rtypes){
            sb.append(rtype);
            if(k<m){
                sb.append(", ");
                k++;
            }
        }
        return sb.toString();
    }
}

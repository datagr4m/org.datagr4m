package org.datagr4m.application.neo4j.model;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.DefaultEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.HierarchicalEdgeModel;
import org.datagr4m.neo4j.topology.edges.Neo4jEdge;
import org.neo4j.graphdb.Relationship;

public class Neo4jHierarchicalEdgeModel extends HierarchicalEdgeModel{

    @Override
	protected <E> void handleEdge(E edge, IBoundedItem is, IBoundedItem it){
        CommutativePair<IBoundedItem> link = new CommutativePair<IBoundedItem>(is, it);
        if(edge instanceof Neo4jEdge)
            newNeo4jEdge((Neo4jEdge)edge, link);
        else
            newDefaultEdge(link);
    }

    public void newDefaultEdge(CommutativePair<IBoundedItem> link) {
        build(link, "interface", "interface", new DefaultEdgeInfo());
    }

    public <E> void newNeo4jEdge(Neo4jEdge edge, CommutativePair<IBoundedItem> link) {
        Relationship r = edge.getRelationship();
        String leftInterface =  null;//r.getType() + "->" +r.getEndNode();
        String rightInterface =  null;//r.getType() + "->" +r.getStartNode();
        build(link, leftInterface, rightInterface, new Neo4jEdgeInfo(r));
    	
    }

    //public class EdgeInfo
    
    private static final long serialVersionUID = 4290803078983193697L;
}

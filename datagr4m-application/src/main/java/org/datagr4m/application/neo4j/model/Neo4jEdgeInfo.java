package org.datagr4m.application.neo4j.model;

import java.util.List;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.neo4j.graphdb.Relationship;


public class Neo4jEdgeInfo implements IEdgeInfo{
    public Neo4jEdgeInfo(Relationship relationship) {
        this.relationship = relationship;
    }
    
    public Relationship getRelationship() {
        return relationship;
    }

    protected Relationship relationship;
    private static final long serialVersionUID = 8522263253591849081L;
    
	@Override
	public List<String> flattenInfoAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String flattenInfo() {
		
		return relationship.getType().name();
	}
	
	public String toString(){
	    return relationship.getType().name();
	}
}

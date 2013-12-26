package com.datagr4m.neo4j.topology.edges;

import java.util.Map;

import org.datagr4m.topology.graph.IPropertyEdge;
import org.neo4j.graphdb.Relationship;


public class Neo4jEdge implements IPropertyEdge{
    protected Relationship relationship;

    public Neo4jEdge(Relationship relationship) {
        super();
        this.relationship = relationship;
    }

    @Override
    public String getTypeName() {
        return relationship.getType().name();
    }
    
    public Relationship getRelationship(){
    	return relationship;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((relationship == null) ? 0 : relationship.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Neo4jEdge other = (Neo4jEdge) obj;
        if (relationship == null) {
            if (other.relationship != null)
                return false;
        } else if (!relationship.equals(other.relationship))
            return false;
        return true;
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new RuntimeException("not implemented");
    }  
}

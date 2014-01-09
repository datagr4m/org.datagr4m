package org.datagr4m.application.neo4j.navigation.plugins.louposcope;


import org.datagr4m.datastructures.triplets.Triplet;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;


/**
 * Holds the relationship summary:
 * <ul>
 * <li>edge</li>
 * <li>target node</li>
 * <li>outgoing interface object (if any) used to reach target node</li>
 * </ul>
 * 
 * A label is generated and made available.
 * 
 * @author Martin Pernollet
 */
public class Neo4jNodeRelationshipSummary {
    protected Triplet<IPropertyEdge, IPropertyNode, Object> relationship;
    protected String label;

    public Neo4jNodeRelationshipSummary(IPropertyEdge relationship, IPropertyNode target) {
        this(new Triplet<IPropertyEdge, IPropertyNode, Object>(relationship, target, null));
    }

    public Neo4jNodeRelationshipSummary(Triplet<IPropertyEdge, IPropertyNode, Object> relationships) {
        this.relationship = relationships;
        this.label = makeLabel();
    }

    public String makeLabel() {
        StringBuilder sb = new StringBuilder();
        makeLabel(sb);
        return sb.toString();
    }

    public void makeLabel(StringBuilder sb) {
        sb.append(relationship.a.getTypeName());
        sb.append(" ");
        sb.append(relationship.b.getLabel());
    }

    public Triplet<IPropertyEdge, IPropertyNode, Object> getRelationship() {
        return relationship;
    }

    public void setRelationship(Triplet<IPropertyEdge, IPropertyNode, Object> relationships) {
        this.relationship = relationships;
    }

    public String getLabel() {
        return label;
    }

    @Override
	public String toString(){
        return "summary " + getLabel();
    }
    
}

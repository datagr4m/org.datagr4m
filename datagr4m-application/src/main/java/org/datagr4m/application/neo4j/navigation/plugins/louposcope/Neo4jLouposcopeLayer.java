package org.datagr4m.application.neo4j.navigation.plugins.louposcope;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.application.neo4j.renderers.Neo4jRelationshipRendererSettings;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.navigation.plugin.louposcope.AbstractLouposcopeLayer;
import org.datagr4m.drawing.navigation.plugin.louposcope.ILouposcopeContent;
import org.datagr4m.drawing.navigation.plugin.louposcope.ILouposcopeLayer;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.viewer.IDisplay;

public class Neo4jLouposcopeLayer extends AbstractLouposcopeLayer<IPropertyNode,IPropertyEdge,Neo4jNodeRelationshipSummary> implements ILouposcopeLayer<IPropertyNode,IPropertyEdge,Neo4jNodeRelationshipSummary>{
    public Neo4jLouposcopeLayer(IHierarchicalNodeModel model, IDisplay display) {
        super(model, display);
    }

    @Override
    public ILouposcopeContent<Neo4jNodeRelationshipSummary> getOrCreateContent(IPropertyNode o) {
        List<Neo4jNodeRelationshipSummary> details = new ArrayList<Neo4jNodeRelationshipSummary>();
        for(IPropertyEdge edge: graph.getIncidentEdges(o)){
            IPropertyNode neighbour = graph.getOpposite(o, edge);
            Neo4jNodeRelationshipSummary s = new Neo4jNodeRelationshipSummary(edge, neighbour);
            //System.out.println(s);
            details.add(s);
        } 
        IBoundedItem item = model.getItem(o);
        Neo4jNodeLouposcopeContent c = new Neo4jNodeLouposcopeContent(item, details, 200, 10, 2, 2, edgeRendererSettings);
        return c;
    }
    
    public Neo4jRelationshipRendererSettings getEdgeRendererSettings() {
        return edgeRendererSettings;
    }

    public void setEdgeRendererSettings(Neo4jRelationshipRendererSettings edgeRendererSettings) {
        this.edgeRendererSettings = edgeRendererSettings;
    }
    
    protected Neo4jRelationshipRendererSettings edgeRendererSettings;
    
}

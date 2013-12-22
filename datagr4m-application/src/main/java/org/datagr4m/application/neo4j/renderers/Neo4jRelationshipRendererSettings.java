package org.datagr4m.application.neo4j.renderers;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.datagr4m.application.neo4j.model.Neo4jEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRendererSettings;
import org.datagr4m.drawing.renderer.policy.DefaultStyleSheet;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.viewer.colors.ColorMapRainbow;

public class Neo4jRelationshipRendererSettings extends TubeRendererSettings {
    @Override
	public Color getEdgeColor(IEdge edge) {
        if (edge == null)
            return DefaultStyleSheet.TUBE_COLOR;
        else if (edge.getEdgeInfo() != null && edge.getEdgeInfo() instanceof Neo4jEdgeInfo) {
            Neo4jEdgeInfo ne = (Neo4jEdgeInfo) edge.getEdgeInfo();
            Color color = getRelationshipColor(ne.getRelationship().getType().name());
            if(color!=null)
                return color;
            else
                return super.getEdgeColor(edge);
        } else
            return super.getEdgeColor(edge);
    }
    
    public Color getRelationshipColor(String type){
        if(type!=null)
            return getRelationshipTypeColor(type);
        else
            return Color.BLACK;
    }

    public Color getRelationshipTypeColor(String type) {
        return relationColor.get(type);
    }

    public void configureEdgeTypeColors(Collection<IPropertyEdge> edges) {
        Set<String> s = new HashSet<String>();
        for (IPropertyEdge r : edges){
            if(r.getTypeName()!=null)
                s.add(r.getTypeName());
        }
        configureWithTypes(s);
    }

    public void configureWithTypes(Collection<String> types) {
        int n = types.size();
        ColorMapRainbow cm = new ColorMapRainbow();

        int k = 0;
        for (String t : types) {
            Color c = cm.getColor(k, 0, n);
            relationColor.put(t, c);
            k++;
        }
    }

    protected Map<String, Color> relationColor = new HashMap<String, Color>();
}

package org.datagr4m.application.neo4j.navigation.plugins.louposcope;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.datagr4m.application.neo4j.renderers.Neo4jRelationshipRendererSettings;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.navigation.plugin.louposcope.AbstractLouposcopeContent;
import org.datagr4m.drawing.navigation.plugin.louposcope.ILouposcopeContent;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.viewer.renderer.annotations.items.ClickableRectangleAnnotation;

/** Build a left and right list of {@link ClickableRectangleAnnotation} according to the
 * given {@link Collection<Neo4jNodeRelationshipSummary>}
 * 
 * @author martin
 */
public class Neo4jNodeLouposcopeContent extends AbstractLouposcopeContent<Neo4jNodeRelationshipSummary> implements ILouposcopeContent<Neo4jNodeRelationshipSummary>{
    
    public Neo4jNodeLouposcopeContent(IBoundedItem item, Collection<Neo4jNodeRelationshipSummary> details, int width, int height, float margin, int boxMargin, Neo4jRelationshipRendererSettings s) {
        super(item, details, width, height, margin, boxMargin);
        edgeRendererSettings = s;
        built=false;
        update(); // TODO: effectue 2 fois l'operation a cause de l'héritage
    }

    @Override
    public void build(Collection<Neo4jNodeRelationshipSummary> details) {
        leftList.clear();
        rightList.clear();
        detailAnnotation.clear();
        
        List<Neo4jNodeRelationshipSummary> list = new ArrayList<Neo4jNodeRelationshipSummary>(details);
        Collections.sort(list, new Comparator<Neo4jNodeRelationshipSummary>(){
            @Override
            public int compare(Neo4jNodeRelationshipSummary arg0, Neo4jNodeRelationshipSummary arg1) {
                return arg0.getLabel().compareTo(arg1.getLabel());
            }
        });
        
        int ndt = details.size();
        int cut = ndt/2;
        buildLeftList(list, cut);
        buildRightList(list, ndt, cut);
        built = true;
    }

    protected void buildRightList(List<Neo4jNodeRelationshipSummary> list, int ndt, int cut) {
        for (int i = cut; i < ndt; i++) {
            Neo4jNodeRelationshipSummary summary = list.get(i);
            String label = summary.getLabel();
            Color c = getColor(summary);
            
            ClickableRectangleAnnotation rra = new ClickableRectangleAnnotation(label, boxMargin, width, height);
            if(c!=null){
                rra.setBackgroundColor(c);
            }
            rightList.add(rra);
            detailAnnotation.put(summary, rra);
        }
    }

    protected void buildLeftList(List<Neo4jNodeRelationshipSummary> list, int cut) {
        for (int i = 0; i < cut; i++) {
            Neo4jNodeRelationshipSummary summary = list.get(i);
            String label = summary.getLabel();
            Color c = getColor(summary);
            
            ClickableRectangleAnnotation lra = new ClickableRectangleAnnotation(label, boxMargin, width, height);
            if(c!=null)
                lra.setBackgroundColor(c);
            leftList.add(lra);
            detailAnnotation.put(summary, lra);
        }
    }
    
    public Color getColor(Neo4jNodeRelationshipSummary s){
        if(edgeRendererSettings!=null){
            IPropertyEdge e = s.getRelationship().a;
            String type = e.getTypeName();
            return edgeRendererSettings.getRelationshipTypeColor(type);
        }
        else
            return null;
    }

    public Neo4jRelationshipRendererSettings getEdgeRendererSettings() {
        return edgeRendererSettings;
    }

    public void setEdgeRendererSettings(Neo4jRelationshipRendererSettings edgeRendererSettings) {
        this.edgeRendererSettings = edgeRendererSettings;
    }
    
    protected Neo4jRelationshipRendererSettings edgeRendererSettings;
    
}

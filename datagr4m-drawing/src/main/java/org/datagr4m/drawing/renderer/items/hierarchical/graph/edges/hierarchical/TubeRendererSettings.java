package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical;

import java.awt.Color;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.renderer.policy.DefaultStyleSheet;


public class TubeRendererSettings {
    public Color getEdgeColor(IEdge edge) {
        if(edge==null)
            return DefaultStyleSheet.TUBE_COLOR;
        
        Color c = colors.get(edge);
        if(c!=null)
            return c;
        else{
            if(edge.getState().isSelected())
                return DefaultStyleSheet.TUBE_COLOR_SELECTED;
            return DefaultStyleSheet.TUBE_COLOR;
        }
    }
    
    public Stroke getEdgeStroke(IEdge edge){
        return strokes.get(edge);
    }
    
    /** If return false, then the renderer will try to guess if both end points of the edge
     * are visible. If they are'nt, the renderer won't display the edge.*/
    public boolean isEdgeWithoutVisibleNodesVisible(){
        return true;
    }
    
    /************************/

    public boolean isTubeSourceOpened(Tube tube){
        Boolean b = isSourceOpened.get(tube);
        if(b==null)
            return DEFAULT_TUBE_OPEN_STATUS;
        else
            return b;
    }
    
    public boolean isTubeTargetOpened(Tube tube){
        Boolean b = isTargetOpened.get(tube);
        if(b==null)
            return DEFAULT_TUBE_OPEN_STATUS;
        else
            return b;
    }
    
    public void setTubeSourceOpened(Tube tube, boolean value){
        isSourceOpened.put(tube, value);
    }
    
    public void setTubeTargetOpened(Tube tube, boolean value){
        isTargetOpened.put(tube, value);
    }
    
    /************************/
    
    protected Map<IEdge,Color> colors = new HashMap<IEdge,Color>();
    protected Map<IEdge,Stroke> strokes = new HashMap<IEdge,Stroke>();
    
    protected Map<Tube,Boolean> isSourceOpened = new HashMap<Tube,Boolean>();
    protected Map<Tube,Boolean> isTargetOpened = new HashMap<Tube,Boolean>();
    
    protected static boolean DEFAULT_TUBE_OPEN_STATUS = DefaultStyleSheet.DEFAULT_TUBE_IS_OPEN;
}

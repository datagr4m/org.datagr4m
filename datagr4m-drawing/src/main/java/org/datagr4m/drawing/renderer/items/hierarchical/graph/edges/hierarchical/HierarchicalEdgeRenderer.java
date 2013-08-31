package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.renderer.pathfinder.view.PathSegmentsRenderer;
import org.datagr4m.maths.geometry.LineUtils;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.DefaultRenderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class HierarchicalEdgeRenderer extends DefaultRenderer {
    public static boolean INCLUDING = true;
    
    public HierarchicalEdgeRenderer(IDisplay display){
        this.display = display;
        this.pathRenderer = new PathSegmentsRenderer();
        this.edgeRendererSettings = new TubeRendererSettings();
        
        this.annotationMap = new HashMap<Object,Pair<String,String>>();
    }
    
    /********** MAIN EDGE RENDERING & ANNOTATIONS *********/
    
    public void renderEdge(Graphics2D graphic, IEdge edge, Color color){
        if(!edgeRendererSettings.isEdgeWithoutVisibleNodesVisible()){
            if(canRender(edge)){
                pathRenderer.render(graphic, edge.getPathGeometry(), color, 1, false, false);
                renderRawEdgeInfo(graphic, edge, color);
            }
        }
        else{
            if(endpointParentsNotCollapsed(edge)){
                pathRenderer.render(graphic, edge.getPathGeometry(), color, 1, false, false);
                renderRawEdgeInfo(graphic, edge, color); 
            }
        }
    }

    public void renderEdge(Graphics2D graphic, IEdge edge, Color color, Stroke stroke){
        if(!edgeRendererSettings.isEdgeWithoutVisibleNodesVisible()){
            if(canRender(edge)){
                pathRenderer.render(graphic, edge.getPathGeometry(), color, false, false, stroke);
                renderRawEdgeInfo(graphic, edge, color);
            }
        }
        else{
            if(endpointParentsNotCollapsed(edge)){
                pathRenderer.render(graphic, edge.getPathGeometry(), color, false, false, stroke);
                renderRawEdgeInfo(graphic, edge, color); 
            }
        }
    }

    
    public void renderEdgeBefore(Graphics2D graphic, IEdge edge, Point2D point, Color color){
        if(!edgeRendererSettings.isEdgeWithoutVisibleNodesVisible()){
            if(canRender(edge)){
                pathRenderer.renderBefore(graphic, edge.getPathGeometry(), point, INCLUDING, color, 1, false, false, edge.getState().isSelected());
                renderRawEdgeInfo(graphic, edge, color);
            }
        }
        else{
            if(endpointParentsNotCollapsed(edge)){
                pathRenderer.renderBefore(graphic, edge.getPathGeometry(), point, INCLUDING, color, 1, false, false, edge.getState().isSelected());
                renderRawEdgeInfo(graphic, edge, color); 
            }
        }
    }

    public void renderEdgeAfter(Graphics2D graphic, IEdge edge, Point2D point, Color color){
        if(!edgeRendererSettings.isEdgeWithoutVisibleNodesVisible()){
            if(canRender(edge)){
                pathRenderer.renderAfter(graphic, edge.getPathGeometry(), point, INCLUDING, color, 1, false, false, edge.getState().isSelected());
                renderRawEdgeInfo(graphic, edge, color);
            }
        }
        else {
            if(endpointParentsNotCollapsed(edge)){
                pathRenderer.renderAfter(graphic, edge.getPathGeometry(), point, INCLUDING, color, 1, false, false, edge.getState().isSelected());
                renderRawEdgeInfo(graphic, edge, color);
            }
        }
    }

    protected boolean canRender(IEdge edge){
        boolean sourceD = edge.getSourceItem().isDisplayed(display);
        boolean targetD = edge.getTargetItem().isDisplayed(display);
        
        /*if(edge.getSourceItem().getParent().isCollapsed())
            sourceD = edge.getSourceItem().getParent().getCollapsedModel().isDisplayed(display);
        if(edge.getTargetItem().getParent().isCollapsed())
            targetD = edge.getTargetItem().getParent().getCollapsedModel().isDisplayed(display);
        */
        return sourceD||targetD;
    }
    
    protected boolean endpointParentsNotCollapsed(IEdge edge){
        boolean sourceD = edge.getSourceItem().getParent().isCollapsed();
        boolean targetD = edge.getTargetItem().getParent().isCollapsed();
        return true;//!(sourceD||targetD);
    }

    /********** EDGE INFO & ANNOTATIONS RENDERING *********/
    
    public void renderRawEdgeInfo(Graphics2D graphic, IEdge edge, Color color){
        graphic.setColor(Color.black);
        
        // Handle network informations
        IEdgeInfo info = edge.getEdgeInfo();
        if(info!=null && edge.getState().isSelected()){
            if(!hasAnnotation(edge)){ // maybe we already did it
                renderRawEdgeInfo(graphic, edge, info, color);
            }            
        }
        else{
            //if(edge.getState().isSelected())
            //    System.err.println("info is null:" + edge);
        }
    }
    
    public void renderRawEdgeInfo(Graphics2D graphic, IEdge edge, IEdgeInfo info, Color color){
    }

    
    public void clearAnnotationSquatting(){
        xSquat.clear();
        ySquat.clear();
        annotationMap.clear();
    }

    public boolean hasAnnotation(Object o){
        return annotationMap.containsKey(o);
    }
    
    public Pair<String,String> getAnnotation(Object o){
        return annotationMap.get(o);
    }
    
    public void storeAnnotation(Object o, Pair<String,String> str){
        annotationMap.put(o, str);
    }
    
    protected Map<Object, Pair<String,String>> annotationMap = new HashMap<Object,Pair<String,String>>();
    
    
    /*
    protected HtmlRenderer htmlRenderer = new HtmlRenderer();
     
    protected void renderRawEdgeInfo2(Graphics2D graphic, IEdge edge, IEdgeInfo info, Color color){
        if(info instanceof NetworkEdgeInfo){
            NetworkEdgeInfo edgeInfo = (NetworkEdgeInfo)info;
            int n = edgeInfo.getInfo().getNetworks().size();
            
            
            List<String[]> cells = new ArrayList<String[]>(n);
            
            for(IIPNetwork network: edgeInfo.getInfo().getNetworks()){
                String[] line = new String[2];
                line[0] = network.getBinaryIp().toString();
                line[1] = network.getBinaryMask().toString();
                cells.add(line);
            }
            
            Coord2d source = edge.getSourceItem().getAbsolutePosition();
            Coord2d target = edge.getTargetItem().getAbsolutePosition();
            
            HtmlObject o1 = new HtmlObject(Html.wrap(Html.table(cells)), source.cloneAsDoublePoint(), 1000, 1000);
            HtmlObject o2 = new HtmlObject(Html.wrap(Html.table(cells)), target.cloneAsDoublePoint(), 1000, 1000);
            
            htmlRenderer.render(graphic, o1);
            htmlRenderer.render(graphic, o2);
        }
    }*/
    
    /********** HIT PRIMITIVE ***********/
    
    // TODO: should synchronize reading of the path point list
    protected CommutativePair<Point2D> hitEdgePath(int x, int y, IEdge edge, float width){
        if(endpointParentsNotCollapsed(edge)){
            IPath p = edge.getPathGeometry();
            int np = p.getPointNumber();
            for (int i = 0; i < np-1; i++) {
                boolean cross = LineUtils.inTube(p.getPoint(i), p.getPoint(i+1), width, x, y);
                if(cross)
                    return new CommutativePair<Point2D>(p.getPoint(i), p.getPoint(i+1));
            }
        }
        return null;
    }

    /*********************/

    public TubeRendererSettings getEdgeRendererSettings() {
        return edgeRendererSettings;
    }

    public void setEdgeRendererSettings(TubeRendererSettings edgeRendererSettings) {
        this.edgeRendererSettings = edgeRendererSettings;
    }

    public IDisplay getDisplay() {
        return display;
    }
    
    /*********************/
    
    protected BiMap<Double,Point2D> ySquatMap = HashBiMap.create();
    protected List<Double> xSquat = new ArrayList<Double>();
    protected List<Double> ySquat = new ArrayList<Double>();
    
    protected TubeRendererSettings edgeRendererSettings;
    protected PathSegmentsRenderer pathRenderer;
    protected IDisplay display;
}

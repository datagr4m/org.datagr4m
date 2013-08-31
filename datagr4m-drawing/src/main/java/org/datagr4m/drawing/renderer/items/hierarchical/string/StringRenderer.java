package org.datagr4m.drawing.renderer.items.hierarchical.string;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.string.StringModel;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.IGraphRendererSettings;
import org.datagr4m.drawing.renderer.pathfinder.view.PathSegmentsRenderer;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.IDisplay;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.algorithms.interpolation.IInterpolator;
import org.jzy3d.maths.algorithms.interpolation.algorithms.BernsteinInterpolator;


public class StringRenderer extends HierarchicalGraphRenderer{
    public static boolean INTERPOLATION = false;
    public static boolean DRAW_MAGNETS = true;
    
    public StringRenderer(IDisplay display, IHierarchicalGraphModel model, IGraphRendererSettings settings) {
        super(display, model, settings);
    }

    public StringRenderer(IDisplay display, IHierarchicalGraphModel model) {
        super(display, model);
    }
    
    @Override
    public void render(Graphics2D graphic) {
        StringModel smodel = (StringModel)model;
        
        // Draw border
        if(settings.getBoundsSettings().isBoundDisplayed(model)){
            boundsRenderer.render(graphic, model);
        }
        
        // Draw all edges
        /*if(settings.isEmbeddedEdgeDisplayed())
            for(Pair<IBoundedItem, IBoundedItem> e: model.getRenderingEdges())
                edgeRenderer.render(graphic, e, settings.getEdgeSettings());
        */
        
        // Draw all nodes
        for(IBoundedItem item: smodel.getObstacles()){
            //if(item.isDisplayed(display)){
                //if(item instanceof IBoundedItemIcon)
                //    nodeIconRenderer.render(graphic, item, settings.getNodeSettings());
                //else
                    itemRenderer.render(graphic, item, settings.getNodeSettings());
            //}
        }
        
        for(List<IBoundedItem> string: smodel.getStrings()){
            if(string.size()>0){
                if(INTERPOLATION){
                    List<Coord3d> coords = new ArrayList<Coord3d>();
                    for(IBoundedItem node: string){
                        coords.add(new Coord3d(node.getPosition(), 0));
                    }
                    IInterpolator i = new BernsteinInterpolator();
                    List<Coord3d> interpo = i.interpolate(coords, 5);
                    
                    List<Point2D> points = new ArrayList<Point2D>();
                    for(Coord3d inter: interpo)
                        points.add(new Point2D.Float(inter.x, inter.y));
                    renderPath(graphic, points);
                }
                else{
                    List<Point2D> points = new ArrayList<Point2D>();
                    synchronized(smodel){
                        for(IBoundedItem node: string){
                            points.add(Pt.cloneAsDoublePoint(node.getPosition()));
                            
                            if(DRAW_MAGNETS)
                                itemRenderer.render(graphic, node, settings.getNodeSettings());
                        }
                    }
                    renderPath(graphic, points);
                }
                
                IBoundedItem first = string.get(0);
                IBoundedItem second = string.get(string.size()-1);
                
                itemRenderer.render(graphic, first, settings.getNodeSettings());
                itemRenderer.render(graphic, second, settings.getNodeSettings());
            }
        }
    }
    
    public void renderPath(Graphics2D graphic, List<Point2D> points){
        IPath path = new LockablePath(points);
        pathSegmentRenderer.render(graphic, path, Color.BLACK, false, false);
    }

    PathSegmentsRenderer pathSegmentRenderer = new PathSegmentsRenderer();
}

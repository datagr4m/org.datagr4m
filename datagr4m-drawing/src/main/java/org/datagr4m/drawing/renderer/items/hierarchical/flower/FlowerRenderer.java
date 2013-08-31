package org.datagr4m.drawing.renderer.items.hierarchical.flower;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.flower.IFlowerModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.IGraphRendererSettings;
import org.datagr4m.drawing.renderer.items.shaped.AbstractShapedItemIconRenderer;
import org.datagr4m.drawing.renderer.items.shaped.RectangleItemIconRenderer;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.Coord2d;


public class FlowerRenderer extends HierarchicalGraphRenderer{
    public static boolean INTERPOLATION = false;
    public static boolean DRAW_MAGNETS = true;
    public static int DEVICE_INFO_MARGIN = 6;
    
    public FlowerRenderer(IDisplay display, IHierarchicalGraphModel model, IGraphRendererSettings settings) {
        super(display, model, settings);
        itemRenderer = new RectangleItemIconRenderer(model, display);
    }

    public FlowerRenderer(IDisplay display, IHierarchicalGraphModel model) {
        super(display, model);
        itemRenderer = new RectangleItemIconRenderer(model, display);
    }
    
    @Override
    public void render(Graphics2D graphic) {
        if(model!=null){
            IFlowerModel<Object> fmodel = (IFlowerModel<Object>)model;
            
            List<IBoundedItem> selected = new ArrayList<IBoundedItem>();
            for(IBoundedItem i: fmodel.getChildren()){
                if(i.getState().equals(ItemState.SELECTED))
                    selected.add(i);
            }
            
            renderUnselectedItems(graphic, fmodel, selected);
            renderSelectedItems(graphic, fmodel, selected);
        }
    }
    
    protected void renderUnselectedItems(Graphics2D graphic, IFlowerModel<Object> fmodel, List<IBoundedItem> selected){
        renderItems(graphic, fmodel, selected, false);
    }
    
    protected void renderSelectedItems(Graphics2D graphic, IFlowerModel<Object> fmodel, List<IBoundedItem> selected){
        renderItems(graphic, fmodel, selected, true);
    }
    
    protected void renderItems(Graphics2D graphic, IFlowerModel<Object> fmodel, List<IBoundedItem> selected, boolean isSelected){
        // Draw border
        if(settings.getBoundsSettings().isBoundDisplayed(model)){
            boundsRenderer.render(graphic, model.getAbsoluteRectangleBounds());
        }
        
        // Draw all edges
        if(settings.getEdgeSettings().isEdgeDisplayed(null)){
            int oldSize = TextUtils.size;
            int size = settings.getEdgeSettings().getEdgeLabelSize(null);
            
            TextUtils.changeFontSize(graphic, size*2);
            for(Object o: fmodel.getEdges()){
                boolean renderIt = (isSelected==isEndPointInSelection(fmodel, o, selected));
                // draw all non selected
                if(renderIt)
                    computeAndRenderEdge(graphic, fmodel, o, isSelected?Color.RED:Color.BLACK, /*Color.WHITE*/null);
            }
            TextUtils.changeFontSize(graphic, oldSize);
        }
        
        // Draw all nodes
        for(IBoundedItem item: fmodel.getChildren()){
            if(item.isVisible()){
                boolean renderIt = (isSelected==selected.contains(item));
                if(renderIt)
                    itemRenderer.render(graphic, item, settings.getNodeSettings());
            }
        }
    }
    
    /*********************/

    protected boolean isEndPointInSelection(IFlowerModel<Object> fmodel, Object edge, List<IBoundedItem> selection){
        Pair<IBoundedItem,IBoundedItem> points = fmodel.getEndpoints(edge);
        
        if(selection.contains(points.a) || selection.contains(points.b))
            return true;
        else
            return false;
    }

    protected void computeAndRenderEdge(Graphics2D graphic, IFlowerModel<Object> fmodel, Object edge){
        computeAndRenderEdge(graphic, fmodel, edge, null, null);
    }
    
    protected void computeAndRenderEdge(Graphics2D graphic, IFlowerModel<Object> fmodel, Object edge, Color txt, Color back){
        Pair<IBoundedItem,IBoundedItem> points = fmodel.getEndpoints(edge);
        Pair<String,String> itemInfo = fmodel.getItemInfo(edge);

        Coord2d c1 = points.a.getPosition();
        Coord2d c2 = points.b.getPosition();
        Point2D p1 = Pt.cloneAsFloatPoint(c1);
        Point2D p2 = Pt.cloneAsFloatPoint(c2);
        Line2D line = new Line2D.Float(p1,p2);
        
        int itemInfoHeight = TextUtils.textHeight();
        int itemInfoAWidth = 0;
        int itemInfoBWidth = 0;

        if(itemInfo.a!=null)
            itemInfoAWidth = TextUtils.textWidth(itemInfo.a)+DEVICE_INFO_MARGIN;
        if(itemInfo.b!=null)
            itemInfoBWidth = TextUtils.textWidth(itemInfo.b)+DEVICE_INFO_MARGIN;
        
        // avoid having an edge that enters in the shape
        if(itemRenderer instanceof AbstractShapedItemIconRenderer){
            AbstractShapedItemIconRenderer nr = (AbstractShapedItemIconRenderer)itemRenderer;

            // update des coordonn�es pour text label
            // en mettant les coordonn�es sur la bordure de l'item
            Point2D pp1 = nr.getFirstIntersectionFor(points.a, line);
            Point2D pp2 = nr.getFirstIntersectionFor(points.b, line);
            
            if(pp1!=null)
                c1 = Pt.cloneAsCoord2d(pp1);
            if(pp2!=null)
                c2 = Pt.cloneAsCoord2d(pp2);

            // calcul de la ligne prenant en compte la taille du texte
            Point2D pt1 = nr.getFirstIntersectionFor(points.a, line, (itemInfoAWidth+DEVICE_INFO_MARGIN)*2, (itemInfoHeight+DEVICE_INFO_MARGIN)*2);
            Point2D pt2 = nr.getFirstIntersectionFor(points.b, line, (itemInfoBWidth+DEVICE_INFO_MARGIN)*2, (itemInfoHeight+DEVICE_INFO_MARGIN)*2);
            if(pt1!=null && pt2!=null)
                line.setLine(pt1, pt2);
            else if(pt1!=null)
                line.setLine(pt1, p2);
            else if(pt2!=null)
                line.setLine(p1, pt2);
        }
        
        // ----------------
        // draw edge text
        String edgeInfo = fmodel.getEdgeInfo(edge);
        if(edgeInfo!=null && !edgeInfo.equals("")){
            Coord2d centerP = fmodel.getCenter().getPosition();
            Coord2d neighbourPolarCoord = null;
            float neighbourRadial = 0;
            if(points.a==fmodel.getCenter()){
                neighbourPolarCoord = points.b.getPosition().sub(centerP).fullPolar();
                neighbourRadial = points.b.getRadialBounds();
            }
            else if(points.b==fmodel.getCenter()){
                neighbourPolarCoord = points.a.getPosition().sub(centerP).fullPolar();
                neighbourRadial = points.a.getRadialBounds();
            }
            else
                throw new RuntimeException("no polar!!");
            
            double edgeAngle = c2.sub(c1).fullPolar().x;
            float deviceInfoSize = 50;
    
            Coord2d txtPosition = null;
            if(Math.PI/2<edgeAngle && edgeAngle<(3*Math.PI/2)){
                neighbourPolarCoord.x+=Math.PI;
                neighbourPolarCoord.y-=(neighbourRadial+deviceInfoSize);
                txtPosition = centerP.sub(neighbourPolarCoord.cartesian()); 
                drawTextRotated(graphic, edgeInfo, txtPosition, edgeAngle+Math.PI, txt, back);
            }
            else{
                neighbourPolarCoord.y-=(neighbourRadial+deviceInfoSize);//fmodel.getCenter().getRadialBounds() + edgeInfoDistToCenterBounds;// + TextUtils.textWidth(edgeInfo);
                txtPosition = centerP.add(neighbourPolarCoord.cartesian()); //c2.add(c1).div(2); // default position 
    
                drawTextRotated(graphic, edgeInfo, txtPosition, edgeAngle, true, txt, back);
            }
        }
        
        
        // draw edge
        graphic.setColor(Color.BLACK);
        drawLine(graphic, line);

        // device network info

        // left
        float xoffset=0;
        float yoffset=-2;
        boolean rightAlign;
        
        if(itemInfo.a!=null && !itemInfo.a.equals("")){
            // d�callage du label pour coller au bord
            if(c1.x>points.a.getPosition().x) // si � gauche du point de l'item
                xoffset = +itemInfoAWidth;
            else
                xoffset = -itemInfoAWidth;
            if(c1.y>points.a.getPosition().y) // si au dessus du point de l'item
                yoffset = itemInfoHeight;
            Coord2d tc = c1.add(xoffset, yoffset);
            rightAlign = false;
            if(tc.x>c1.x)
                rightAlign = true;
            if(rightAlign)
                tc.x -= DEVICE_INFO_MARGIN;
            else
                tc.x += (DEVICE_INFO_MARGIN);
            //drawText(graphic, itemInfo.a, tc, rightAlign?-itemInfoAWidth:0, Color.WHITE);
            drawTextRotated(graphic, itemInfo.a, tc, 0, rightAlign, txt, back);
        }
        
        // right
        xoffset=0;
        yoffset=-2;
        if(itemInfo.b!=null && !itemInfo.b.equals("")){
            // d�callage du label pour coller au bord
            if(c2.x>points.b.getPosition().x) // si � gauche du point de l'item
                xoffset = +itemInfoBWidth;
            else
                xoffset = -itemInfoBWidth;
            if(c2.y>points.b.getPosition().y) // si au dessus du point de l'item
                yoffset = itemInfoHeight;
            Coord2d tc = c2.add(xoffset, yoffset);
            rightAlign = false;
            if(tc.x>c2.x)
                rightAlign = true;
            if(rightAlign)
                tc.x -= DEVICE_INFO_MARGIN;//DEVICE_INFO_MARGIN;
            else
                tc.x += (DEVICE_INFO_MARGIN);
            //drawText(graphic, itemInfo.b, tc, rightAlign?-itemInfoBWidth:0, Color.WHITE);
            drawTextRotated(graphic, itemInfo.b, tc, 0, rightAlign, txt, back);
        }
    }
}

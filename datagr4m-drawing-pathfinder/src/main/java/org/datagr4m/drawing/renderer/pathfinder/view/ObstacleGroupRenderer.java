package org.datagr4m.drawing.renderer.pathfinder.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleGroupModel;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.viewer.mouse.edges.ClickedPath;
import org.datagr4m.drawing.viewer.mouse.edges.ClickedSlot;
import org.datagr4m.maths.geometry.RectangleUtils;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.mouse.ILocalizedMouse;
import org.datagr4m.viewer.mouse.hit.IMouseHitModelListener;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;


public class ObstacleGroupRenderer extends AbstractRenderer implements IRenderer, IMouseHitModelListener {
    protected ObstacleGroupModel group;
    protected PathSegmentsRenderer pathRenderer = new PathSegmentsRenderer();
    protected ObstacleRenderer obstacleRenderer = new ObstacleRenderer();
    protected PopupRenderer popupRenderer = new PopupRenderer();
    
    protected boolean showBorder = false;
    protected boolean showEtremity = false;
    protected boolean showPathId = false;
    
    protected ILocalizedMouse controller;
    
    public ObstacleGroupRenderer(ObstacleGroupModel group){
        this.group = group;
    }
    
    public ObstacleGroupRenderer(ObstacleGroupModel group, boolean showBorder){
        this.group = group;
        this.showBorder = showBorder;
    }
    
    @Override
    public void render(Graphics2D graphic) {
        if(group!=null)
            render(graphic, group);
    }
    
    public void render(Graphics2D graphic, ObstacleGroupModel group) {
        for (ISlotableItem o : group.getItems()) {
            obstacleRenderer.render(graphic, (IPathObstacle)o, Color.gray, showBorder);
        }
        for (IPath p : group.getPathes()) {
            ILink<ISlotableItem> link = group.getExtremities(p);
            
            if(link!=null){
                if(isDisplayed((IPathObstacle)link.getSource()) && isDisplayed((IPathObstacle)link.getDestination()))
                    pathRenderer.render(graphic, p, Color.black, showEtremity, showPathId);
                else
                    pathRenderer.render(graphic, p, SHADED_EDGE, showEtremity, showPathId);
            }
            else
                pathRenderer.render(graphic, p, Color.red, showEtremity, showPathId);
        }
        
        popupRenderer.render(graphic);
    }
    
    Color SHADED_EDGE = new Color(190,190,190);
    
    protected boolean isDisplayed(IPathObstacle o){
        if(controller!=null){
            return RectangleUtils.contains(controller.getCurrentViewBounds(), o.getSlotableCenter());
        }
        else
            return true;
    }
    
    /** Allows the renderer to retrieve mouse and view informations to make a decision
     * on some rendering settings.*/
    public void setMouseController(ILocalizedMouse controller){
        this.controller = controller;
        this.controller.addMouseHitListener(this);
    }

    /***************** EDGES & SLOTS HITS ******************/
    
    @Override
    public List<IClickableItem> hit(int x, int y) {
        List<IClickableItem> clicked = new ArrayList<IClickableItem>();
        
        // check slot hit
        for(ISlotableItem o: group.getItems()){
            for(SlotGroup slots: o.getSlotGroups()){
                
                // TODO ajouter test pour verifier si la souris croise le slot group
                // pour �viter de tester toutes les intersections possibles
                int ns = slots.getSlotNumber();
                
                for (int i = 0; i < ns; i++) {
                    Point2D anchor = slots.getSlotAnchorPoint(i);
                    float width = slots.getSlotAnchorWidth();
                    
                    boolean cross = RectangleUtils.contains(anchor, width, width, x, y, false);
                    
                    if(cross){
                        clicked.add(new ClickedSlot(i, slots, o));
                    }
                }
            }
        }
        
        // check edge hit
        for(IPath p: group.getPathes()){
            int np = p.getPointNumber();
            for (int i = 0; i < np-1; i++) {
                // for orthogonal
                boolean cross = RectangleUtils.contains(p.getPoint(i), p.getPoint(i+1), CLICKABLE_SEGMENT_WIDTH, x, y, false);
                // for non orthogonal:
                //boolean cross = LineUtils.inTube(p.getPoint(i), p.getPoint(i+1), CLICKABLE_SEGMENT_WIDTH, x, y);
                if(cross){
                    clicked.add(new ClickedPath(p));
                }
            }
        }
        return clicked;
    }
    
    float CLICKABLE_SEGMENT_WIDTH = 10;

    /********** LISTENING MOUSE HITS AND POPUP INFO ********/
    
    @Override
    public void objectHit(Object object, Point2D screen, Point2D layout) {
    }

    @Override
    public void objectDragged(Object object, Point2D screen, Point2D layout) {
    }

    @Override
    public void objectReleased(Object object, Point2D screen, Point2D layout) {
    }

    @Override
    public void itemHit(IClickableItem item, List<IClickableItem> all, Point2D screen, Point2D layout) {
        for(IClickableItem it: all){
            if(it instanceof ClickedSlot){
                ClickedSlot slot = (ClickedSlot)item;
                int id = slot.getSlot();
                Point2D point = slot.getGroup().getSlotAnchorPoint(id);
                
                popupRenderer.clearPopups();
                popupRenderer.addPopup(new Popup("slot " + id, point));
            }
            else
                System.out.println("ITEM HIT" + item);
        }
        
    }

    @Override
    public void itemDragged(IClickableItem item, Point2D screen, Point2D layout) {
    }

    @Override
    public void itemReleased(IClickableItem item, Point2D screen, Point2D layout) {
    }

    protected void highlightSlot(){
        
    }
}

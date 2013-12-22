package org.datagr4m.drawing.renderer.pathfinder.view.slots;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.datagr4m.drawing.model.slots.SlotTarget.Direction;
import org.datagr4m.viewer.renderer.DefaultRenderer;


public class SlotGroupRenderer extends DefaultRenderer {
    public void render(Graphics2D graphic, SlotGroup group) {
        render(graphic, group, Color.BLACK);
    }

    public void render(Graphics2D graphic, SlotGroup group, Color color) {
        if (group != null) {
            graphic.setColor(color);

            int ns = group.getSlotNumber();

            double minx = +Double.MAX_VALUE;
            double maxx = -Double.MAX_VALUE;
            double miny = +Double.MAX_VALUE;
            double maxy = -Double.MAX_VALUE;

            for (int i = 0; i < ns; i++) {
                float ws = group.getSlotAnchorWidth();
                List<SlotTarget> targets = group.getSlotTarget(i);
                if (group.isFree(i))
                    renderSlotEmpty(graphic, group.getSlotAnchorPoint(i), ws, ws);
                else
                    renderSlotFilled(graphic, targets, group.getSlotAnchorPoint(i), ws, ws, group.getSide());

                Point2D anchor = group.getSlotAnchorPoint(i);
                Point2D path = group.getSlotPathPoint(i);
                drawLine(graphic, anchor, path);

                if (anchor.getX() < minx)
                    minx = anchor.getX();
                if (maxx < anchor.getX())
                    maxx = anchor.getX();

                if (anchor.getY() < miny)
                    miny = anchor.getY();
                if (maxy < anchor.getY())
                    maxy = anchor.getY();

                if (minx != Double.MAX_VALUE && maxx != -Double.MAX_VALUE && miny != Double.MAX_VALUE && maxy != -Double.MAX_VALUE)
                    drawLine(graphic, minx, miny, maxx, maxy);
            }
        }
    }

    public Direction getSlotLinkDirection(List<SlotTarget> targets) {
        Direction d = Direction.NONE;
        if (targets.size() > 0) {
            SlotTarget t = targets.get(0);
            return t.getDirection();
        }
        return d;
    }

    public void renderSlotFilled(Graphics2D graphic, List<SlotTarget> targets, Point2D pt, float width, float height, SlotSide side) {
        Direction d = getSlotLinkDirection(targets);

        if (Direction.NONE.equals(d)) {
            fillRectCentered(graphic, pt, width, height);
        } else {
            if (Direction.OUTGOING.equals(d)) {
                if(SlotSide.NORTH.equals(side)){
                    graphic.fillRect((int) (pt.getX()-width/2), (int) pt.getY(), (int) width, (int) height/2);
                }
                else if(SlotSide.EAST.equals(side)){
                    graphic.fillRect((int) (pt.getX()-width/2), (int) (pt.getY()-height/2), (int) width/2, (int) height);
                }
                else if(SlotSide.SOUTH.equals(side)){
                    graphic.fillRect((int) (pt.getX()-width/2), (int) (pt.getY()-height/2), (int) width, (int) height/2);

                }
                else if(SlotSide.WEST.equals(side)){
                    graphic.fillRect((int) (pt.getX()), (int) (pt.getY()-height/2), (int) width/2, (int) height);
                }
                else
                    throw new RuntimeException("unsupported side");
            } else if (Direction.INCOMING.equals(d)) {
//System.err.println("incoming");
                
                if(SlotSide.NORTH.equals(side)){
                    int[] x = {(int)(pt.getX()-width/2), (int)pt.getX(), (int)(pt.getX()+width/2)};
                    int[] y = {(int)(pt.getY()-height/2), (int)pt.getY(), (int)(pt.getY()-height/2)};
                    graphic.fillPolygon(x, y, 3);
                    graphic.drawRect((int) (pt.getX()-width/2), (int) pt.getY(), (int) width, (int) height/2);
                    
                }
                else if(SlotSide.EAST.equals(side)){
                    int[] x = {(int)(pt.getX()+width/2), (int)pt.getX(), (int)(pt.getX()+width/2)};
                    int[] y = {(int)(pt.getY()-height/2), (int)pt.getY(), (int)(pt.getY()+height/2)};
                    graphic.fillPolygon(x, y, 3);
                    graphic.drawRect((int) (pt.getX()-width/2), (int) (pt.getY()-height/2), (int) width/2, (int) height);
                }
                else if(SlotSide.SOUTH.equals(side)){
                    int[] x = {(int)(pt.getX()-width/2), (int)pt.getX(), (int)(pt.getX()+width/2)};
                    int[] y = {(int)(pt.getY()+height/2), (int)pt.getY(), (int)(pt.getY()+height/2)};
                    graphic.fillPolygon(x, y, 3);
                    graphic.drawRect((int) (pt.getX()-width/2), (int) (pt.getY()-height/2), (int) width, (int) height/2);

                }
                else if(SlotSide.WEST.equals(side)){
                    int[] x = {(int)(pt.getX()-width/2), (int)pt.getX(), (int)(pt.getX()-width/2)};
                    int[] y = {(int)(pt.getY()-height/2), (int)pt.getY(), (int)(pt.getY()+height/2)};
                    graphic.fillPolygon(x, y, 3);
                    graphic.drawRect((int) (pt.getX()), (int) (pt.getY()-height/2), (int) width/2, (int) height);
                    
                }
                else
                    throw new RuntimeException("unsupported side");
                
            }
        }
    }

    public void renderSlotFilled(Graphics2D graphic, Point2D pt, float width, float height) {
        fillRectCentered(graphic, pt, width, height);
    }

    public void renderSlotEmpty(Graphics2D graphic, Point2D pt, float width, float height) {
        drawRectCentered(graphic, pt, width, height);
    }
}

package org.datagr4m.drawing.model.pathfinder.obstacle;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.datagr4m.maths.geometry.RectangleUtils;
import org.jzy3d.maths.Coord2d;



public class PathObstacle implements IPathObstacle{
    private static final long serialVersionUID = -8909836078441852832L;
    public static float DEFAULT_MARGIN = 10;
    public static double STEP = 20;
    
    public PathObstacle(String name, double x, double y, double width, double height){
        this(name, x, y, width, height, DEFAULT_MARGIN);
    }
    
    public PathObstacle(String name, Rectangle2D rectangle){
        this(name, rectangle, DEFAULT_MARGIN);
    }
    
    public PathObstacle(String name, double x, double y, double width, double height, float slotMargin){
        this.name = name;
        this.center = new Coord2d(x,y);
        this.bounds = RectangleUtils.build(center.x, center.y, width, height);
        this.sideSlots = new HashMap<SlotSide,SlotGroup>();
        this.slotMargin = slotMargin;
    }
    
    public PathObstacle(String name, Rectangle2D rectangle, float slotMargin){
        this.name = name;
        this.center = new Coord2d(rectangle.getCenterX(), rectangle.getCenterY());
        this.bounds = rectangle;
        this.sideSlots = new HashMap<SlotSide,SlotGroup>();
        this.slotMargin = slotMargin;
    }
    
    /** Merge several obstacles as one*/
    /*public PathObstacle(List<IPathObstacle> obstacles){
        Rectangle2D r = obstacles.get(0).getBypassedBounds();
        name = obstacles.get(0).getName();
        for (int i = 1; i < obstacles.size(); i++) {
            name += "|" + obstacles.get(i).getName();
            Rectangle2D.intersect(r, obstacles.get(i).getBypassedBounds(), r);
        }
        this.center = new Coord2d(r.getCenterX(), r.getCenterY());
        this.bounds = r;
        this.sideSlots = new HashMap<SlotSide,SlotGroup>();
        this.slotMargin = slotMargin;
    }*/
    
    public PathObstacle(IPathObstacle o1, IPathObstacle o2){
        name = o1.getName() + "|" + o2.getName();
        this.bounds = new Rectangle2D.Double();
        Rectangle2D.union(o1.getBypassedBounds(), o2.getBypassedBounds(), bounds);
        this.center = new Coord2d(bounds.getCenterX(), bounds.getCenterY()); 
        this.sideSlots = new HashMap<SlotSide,SlotGroup>();
        this.slotMargin = Math.max(o1.getSlotMargin(), o2.getSlotMargin());
    }
    
    @Override
    public Rectangle2D getBounds() {
        return bounds;
    }
    
    @Override
    public Rectangle2D getBypassedBounds() {
        double bwidth = bounds.getWidth() + slotMargin*2 + STEP*nByPass;
        double bheight = bounds.getHeight() + slotMargin*2 + STEP*nByPass;
        return RectangleUtils.build(center.x, center.y, bwidth, bheight);
    }

    @Override
    public void addBypass() {
        nByPass++;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    /***** SLOTS *****/
    
    @Override
    public Coord2d getSlotableCenter() {
        return center;
    }
    
    @Override
    public Rectangle2D getSlotableBounds(){
        return getBounds();
    }
    
    @Override
    public float getSlotMargin() {
        return slotMargin;
    }
    
    @Override
    public Collection<SlotGroup> getSlotGroups(){
        return sideSlots.values();
    }
    
    @Override
    public SlotGroup getSlotGroup(SlotSide side){
        return sideSlots.get(side);
    }
    
    @Override
    public void addSlots(int nNorth, int nSouth, int nEast, int nWest){
        clearSlots();
        addNorthSlot(nNorth);
        addSouthSlot(nSouth);
        addEastSlot(nEast);
        addWestSlot(nWest);
    }

    @Override
    public void addNorthSlot(int number){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.NORTH, number, buildNorthSlotCenter(), (float)getSlotableBounds().getWidth(), getSlotMargin());
    }
    
    @Override
    public void addSouthSlot(int number){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.SOUTH, number, buildSouthSlotCenter(), (float)getSlotableBounds().getWidth(), getSlotMargin());
    }
    
    @Override
    public void addEastSlot(int number){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.EAST, number, buildEastSlotCenter(), getSlotMargin(), (float)getSlotableBounds().getHeight());
    }

    @Override
    public void addWestSlot(int number){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.WEST, number, buildWestSlotCenter(), getSlotMargin(), (float)getSlotableBounds().getHeight());
    }
    
    @Override
    public void addNorthSlot(Collection<SlotTarget> targets){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.NORTH, targets, buildNorthSlotCenter(), (float)getSlotableBounds().getWidth(), getSlotMargin());
    }
    
    @Override
    public void addSouthSlot(Collection<SlotTarget> targets){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.SOUTH, targets, buildSouthSlotCenter(), (float)getSlotableBounds().getWidth(), getSlotMargin());
    }
    
    @Override
    public void addEastSlot(Collection<SlotTarget> targets){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.EAST, targets, buildEastSlotCenter(), getSlotMargin(), (float)getSlotableBounds().getHeight());
    }
    
    @Override
    public void addWestSlot(Collection<SlotTarget> targets){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an obstacle having a maring="+slotMargin);
        addSlot(SlotSide.WEST, targets, buildWestSlotCenter(), getSlotMargin(), (float)getSlotableBounds().getHeight());
    }
    
    public void addSlot(SlotSide side, int number, Point2D center, float width, float height){
        sideSlots.put(side, new SlotGroup(this, number, side, center, width, height));
    }
    
    public void addSlot(SlotSide side, Collection<SlotTarget> targets, Point2D center, float width, float height){
        sideSlots.put(side, new SlotGroup(this, targets, side, center, width, height));
    }

    @Override
    public void clearSlots(){
        sideSlots.clear();
    }
    
    protected Point2D buildNorthSlotCenter(){
        return new Point2D.Double(getSlotableCenter().x, getSlotableCenter().y - (getSlotableBounds().getHeight()/2+getSlotMargin()/2));
    }
    protected Point2D buildSouthSlotCenter(){
        return new Point2D.Double(getSlotableCenter().x, getSlotableCenter().y + (getSlotableBounds().getHeight()/2+getSlotMargin()/2));
    }
    protected Point2D buildEastSlotCenter(){
        return new Point2D.Double(getSlotableCenter().x + (getSlotableBounds().getWidth()/2+getSlotMargin()/2), getSlotableCenter().y);
    }
    protected Point2D buildWestSlotCenter(){
        return new Point2D.Double(getSlotableCenter().x - (getSlotableBounds().getWidth()/2+getSlotMargin()/2), getSlotableCenter().y);
    }
    
    /**********/
    
    @Override
	public String toString(){
        Rectangle2D r = getBypassedBounds();
        return name + " c=" + center + " d=" + r.getWidth() + "*" + r.getHeight();
    }
    
    protected String name;
    protected Rectangle2D bounds;
    protected Coord2d center;
    protected int nByPass = 0;
    protected Map<SlotSide,SlotGroup> sideSlots;
    
    protected float slotMargin = 0;
}

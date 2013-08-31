package org.datagr4m.drawing.model.items;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.drawing.layout.slots.geometry.DefaultSlotGeometryBuilder;
import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryBuilder;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.Coord2d;


public class DefaultBoundedItem extends AbstractStatefullItem implements IBoundedItem{
    public static float RADIUS = 10;
    public static float DEFAULT_SLOT_HEIGHT = 20;
    
    /*public static DefaultBoundedItem fromRadius(String name, Object o, Coord2d position, float radius){
        
    }*/

    public static DefaultBoundedItem fromLabelSize(String name, Object o, Coord2d position){
        int width = TextUtils.textWidth(name);
        int height = TextUtils.textHeight();
        RectangleBounds textBounds = new RectangleBounds(width,height);
        float radius = textBounds.getRadius();        
        return new DefaultBoundedItem(name, position, textBounds, radius, DEFAULT_SLOT_HEIGHT, o);
    }

    /****************/
    
    public DefaultBoundedItem(Object o) {
        this(o!=null?o.toString():"", new Coord2d(), RADIUS, o);
    }
    
    public DefaultBoundedItem(Object o, String label) {
        this(label, new Coord2d(), RADIUS, o);
    }
    
    public DefaultBoundedItem(Object o, String label, float radius) {
        this(label, new Coord2d(), radius, o);
    }

    public DefaultBoundedItem(Object o, Coord2d location) {
        this(o.toString(), location, RADIUS, o);
    }

    public DefaultBoundedItem(Object o, Coord2d location, float radius) {
        this(o.toString(), location, radius, o);
    }

    public DefaultBoundedItem(List<String> labels) {
    	this(labels, new Coord2d(), listBounds(labels), RADIUS, DEFAULT_SLOT_HEIGHT, labels);
    }
    
    protected static RectangleBounds listBounds(List<String> labels){
    	int height = 0;
    	int width = 0;
    	for(String label: labels){
    		// enlarge width to largest text
    		int labelWidth = TextUtils.textWidth(label) + 10;
    		if(labelWidth>width){
    			width = labelWidth;
    		}
    		height += TextUtils.textHeight() + 5;
    	}
    	
    	return new RectangleBounds(width, height);
    }
    
    
    public DefaultBoundedItem(String label) {
        this(label, new Coord2d(), RADIUS);
    }
    
    public DefaultBoundedItem(String label, float radius) {
        this(label, new Coord2d(), radius);
    }
    
    public DefaultBoundedItem(String label, Coord2d position) {
        this(label, position, RADIUS);
    }
    
    public DefaultBoundedItem(String label, Coord2d position, float radius) {
        this(label, position, radius, label);
    }
    
    public DefaultBoundedItem(String label, Coord2d position, float radius, Object data) {
        this(label, position, new RectangleBounds(TextUtils.textWidth(label) + 10, TextUtils.textHeight() + 5), radius, DEFAULT_SLOT_HEIGHT, data);
        
    }
    
    /*protected DefaultBoundedItem(String label, Coord2d position, float radius, float margin, Object data) {
    }*/
    protected DefaultBoundedItem(String label, Coord2d position, RectangleBounds bounds, float radius, float margin, Object data) {
    	this(list(label), position, bounds, radius, margin, data);
    }    
    protected static List<String> list(String s){
    	List<String> list = new ArrayList(1);
    	list.add(s);
    	return list;
    }
    
    protected DefaultBoundedItem(List<String> labels, Coord2d position, RectangleBounds bounds, float radius, float margin, Object data) {
        this.shape = ItemShape.RECTANGLE;
        this.labels = labels;
        this.position = position;
        this.data = data;
        
        // bounds
        this.rectangleBounds = bounds;
        this.radialBounds = radius;

        this.slotMargin = margin; // !!
        this.margin = margin; // !!
        this.corridor = 0;
        this.sideSlots = new HashMap<SlotSide,SlotGroup>();
    }
    
    @Override
    public String getLabel() {
        return labels.get(0);
    }
    
    public List<String> getLabels(){
    	return labels;
    }
        
    public void setLabel(String label) {
        this.labels.set(0,label);
    }
    
    @Override
    public boolean isDisplayed(IDisplay display){
        if(!visible)
            return false;
        return isDisplayed(this, display);
    }
    
    public static boolean isDisplayed(IBoundedItem item, IDisplay display){
        Rectangle2D viewBounds = display.getView().getViewBounds();
        RectangleBounds itemBounds = item.getRawRectangleBounds().shiftCenterTo(item.getAbsolutePosition());
        return itemBounds.intersects(viewBounds);
    }
    
    

    /********* POSITION *********/
    
    /**
     * Change position by copying input coordinate values into self coordinate values.
     * Parent is set dirty
     */
    @Override
    public void changePosition(float x, float y){
        position.x = x;
        position.y = y;
        
        fireItemPositionChanged();
        if(parent!=null)
            parent.notifyChildrenPositionChanged();
    }

    @Override
    public void changePosition(Coord2d c){
        changePosition(c.x, c.y);
    }
    
    @Override
    public void changePosition(Point2D c){
        changePosition(c.getX(), c.getY());
    }
    
    @Override
    public void changePosition(double x, double y){
        changePosition((float)x, (float)y);
    }

    @Override
    public void shiftPosition(Coord2d c){
        shiftPosition(c.x, c.y);
    }
    
    /**
     * Change position by adding input coordinate values to self coordinate values.
     */
    @Override
    public void shiftPosition(float x, float y){
        position.addSelf(x, y);
        fireItemPositionChanged();
        if(parent!=null)
            parent.notifyChildrenPositionChanged();
    }
    
    /**
     * Return a reference to the item position. 
     * Changing the position values should be done through {@link changePosition()}
     * to ensure its parent get notified from change
     */
    @Override
    public Coord2d getPosition() {
        return position;
    }

    @Override
    /*public Coord2d getAbsolutePosition(){
        if(parent==null)
            return getPosition();
        else
            return getParent().getAbsolutePosition().add(getPosition());
    }*/
    
    public Coord2d getAbsolutePosition(){
        if(flags.absolutePositionDirty){
            computeAbsolutePosition(absolutePosition);
            //parentDirty = false;
        }
        return absolutePosition;
    }
    
    protected void computeAbsolutePosition(Coord2d abs){
        if(parent==null)
            abs.set(getPosition());
        else{
            //abs.set(getPosition());
            //abs.addSelf(getParent().getAbsolutePosition());
            abs.set(getParent().getAbsolutePosition().add(getPosition()));
        }
    }
    
 // getParent().getAbsolutePosition().add(getPosition());
    
    protected Coord2d absolutePosition = new Coord2d();
    
    /*********** BOUNDS ************/
    
    @Override
    public float getRadialBounds() {
        return radialBounds;
    }
    
    @Override
    public float getRadialBounds(double angle) {
        return getRadialBounds();
    }
    
    @Override
    public List<Coord2d> getPolygonBounds() {
        return null;
    }
    
    /** 
     * Returns a new rectangle instance centered at (0,0),
     * that includes the implicit circle returned by radial bounds.
     */
    @Override
    public RectangleBounds getRawRectangleBounds() {
        return rectangleBounds;
    }
        
    @Override
    public RectangleBounds getRawExternalRectangleBounds(){
        RectangleBounds rb = getRawRectangleBounds().clone();
        rb.enlargeSelfInPlace(margin, margin);
        return rb;
    }
    
    @Override
    public RectangleBounds getRawCorridorRectangleBounds(){
        return getRawExternalRectangleBounds();
    }
    
    
    /** 
     * Returns a new rectangle instance centered at the item position
     */
    @Override
    public RectangleBounds getRelativeRectangleBounds() {
        return getRawRectangleBounds().shiftCenterTo(getPosition());
    }
    
    @Override
    public RectangleBounds getAbsoluteRectangleBounds() {
        return getRawRectangleBounds().shiftCenterTo(getAbsolutePosition());
    }
    
    /**
     * @return getRelativeRectangleBounds()
     */
    @Override
    public RectangleBounds getInternalRectangleBounds() {
        return getRawRectangleBounds();
    }

    /**
     * @return getRelativeRectangleBounds()
     */
    @Override
    public RectangleBounds getExternalRectangleBounds() {
        return getRawRectangleBounds();
    }
    
    @Override
    public RectangleBounds getCorridorRectangleBounds() {
        return getExternalRectangleBounds();
    }
    
    @Override 
    public float getMargin(){
        return margin;
    }
    
    @Override 
    public float getCorridor(){
        return corridor;
    }

    
    /***** SLOTS *****/
    
    @Override
    public Coord2d getSlotableCenter() {
        return getAbsolutePosition();
    }
    
    @Override
    public Rectangle2D getSlotableBounds(){
        return getRawRectangleBounds().cloneAsRectangle2D(); //TODO: clone pas bon!!
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
        sideSlots.put(side, new SlotGroup(SLOT_GEOMETRY_BUILDER, this, number, side, center, width, height));
    }
    
    public void addSlot(SlotSide side, Collection<SlotTarget> targets, Point2D center, float width, float height){
        sideSlots.put(side, new SlotGroup(SLOT_GEOMETRY_BUILDER, this, targets, side, center, width, height));
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
    
    public static float getMaxRadius(List<IBoundedItem> items){
        float max = -Float.MAX_VALUE;
        for(IBoundedItem item: items){
            if(max<item.getRadialBounds())
                max = item.getRadialBounds();
        }
        return max;
    }
    
    /******************/

    @Override
	public String toString(){
        return this.getClass().getSimpleName() + " " + getLabel();
    }
    
    
    @Override
	public IBoundedItem clone(){
        return new DefaultBoundedItem(labels+"", position.clone(), rectangleBounds, radialBounds, margin, data);
    }

    @Override
    public String getDebugInfo(){
        return "";
    }

    
    /******************/

    protected List<String> labels = list("");
    protected Coord2d position;  
    protected float radialBounds = 0;
    protected float margin = 0;
    protected float corridor = 0;
    
    //protected float margin = 0;
    protected RectangleBounds rectangleBounds;
    protected List<Coord2d> polygonBounds;
    
    protected Map<SlotSide,SlotGroup> sideSlots;
    protected float slotMargin = 0;
    
    protected static ISlotGeometryBuilder SLOT_GEOMETRY_BUILDER = new DefaultSlotGeometryBuilder();
    
    private static final long serialVersionUID = -4679115231998026821L;
}

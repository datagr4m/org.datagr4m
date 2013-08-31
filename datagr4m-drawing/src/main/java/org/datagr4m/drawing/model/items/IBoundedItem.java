package org.datagr4m.drawing.model.items;

import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.listeners.IItemListener;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.jzy3d.maths.Coord2d;


/**
 * A bounded item describes the representation of the input data model.
 * It can be both used by layouts and renderers.
 * 
 * An item provides its bounds, either radial, rectangular, or polygonal. 
 * Bounds are relative to the item center.
 */
public interface IBoundedItem extends ISlotableItem, IClickableItem{
    /**
     * Return the label of this item, which must be unique, since it is used as a key.
     */
    public String getLabel();
    
    /**
     * Return the position of this item, relative to its parent.
     */
    public Coord2d getPosition();
    
    public void changePosition(Coord2d c);
    public void changePosition(Point2D c);
    public void changePosition(float x, float y);
    public void changePosition(double x, double y);
    public void shiftPosition(Coord2d c);
    public void shiftPosition(float x, float y);

    
    /**
     * Return the absolute position. 
     * @see {@link IHierarchicalModel.getAbsolutePosition(IBoundedItem)}
     */
    public Coord2d getAbsolutePosition();
    
    public void notifyParentPositionDirty();
    public void notifyParentPositionDirty(boolean parentDirty);
    
    /*public double getDistance(Coord2d c);
    public double getDistanceSq(Coord2d c);
    public double getDistanceX(Coord2d c);
    public double getDistanceY(Coord2d c);

    public double getDistance(Point2D p);
    public double getDistanceSq(Point2D p);
    public double getDistanceX(Point2D p);
    public double getDistanceY(Point2D p);*/

    
    /** 
     * Returns a uniform radial bound around the item. 
     * If the item has polygon bounds, the radius describes the smallest circle that 
     * completely embeds the item, assuming its center is given by {@link getPosition()}.
     */
    public float getRadialBounds();
    
    /** 
     * Returns the radius of the item at a given angle in [0;2PI].
     * If the item does not hold polygon bounds, then calling this method is equivalent
     * to calling  {@link getRadialBounds()}.
     */
    public float getRadialBounds(double angle);
    
    /** 
     * Returns a list of point describing a polygon boundary, or null if the item is considered as
     * a point item.
     */
    public List<Coord2d> getPolygonBounds();
    
    /**
     * Returns a rectangular region that embeds the item, with a center at (0,0).
     * It is therefore only relevant to call getRectangleBounds().width/height.
     */
    public RectangleBounds getRawRectangleBounds();
    //public RectangleBounds getRawInternalRectangleBounds();
    public RectangleBounds getRawExternalRectangleBounds();
    public RectangleBounds getRawCorridorRectangleBounds();
    
    public RectangleBounds getRelativeRectangleBounds();
    public RectangleBounds getAbsoluteRectangleBounds();
    public RectangleBounds getInternalRectangleBounds();
    public RectangleBounds getExternalRectangleBounds();
    public RectangleBounds getCorridorRectangleBounds();
    

    //public float getMargin();
    
    
    /** 
     * Indicates wether this item is locked or not, which is especially usefull to handle mouse control
     * and interactive layout computation.
     */
    public boolean locked();
    
    /**
     * Switch the item to locked.
     */
    public void lock();
    
    /** 
     * Switch the item to unlocked.
     */
    public void unlock();
    
    /** 
     * Indicates the current item state.
     */
    public ItemState getState();
    
    /**
     * Change the current state of the item.
     */
    public void setState(ItemState state);

    /**
     * Setup a new custom state based on a String. Calling {@link getState()} 
     * afterward will return a {@link ItemState} holding this String as
     * state name.
     */
    public void setState(String state);
    
    
    
    /**
     * Returns true if the item shape intersects the view bounds.
     * Will always return false if item is not set visible.
     * @see setVisible();
     */
    public boolean isDisplayed(IDisplay display);
    public boolean isVisible();
    public void setVisible(boolean visible);
    
    public boolean intersects(IBoundedItem item);
    
    // HIERARCHY
    public void setParent(IHierarchicalModel model);
    public IHierarchicalModel getParent();
    public int getDepth();
    /** 
     * Returns the root of this hierarchy, i.e. the first model having a null parent
     * starting from this one.
     */
    public IHierarchicalModel getRoot();
    public IHierarchicalModel computeRoot();
    
    /** Returns true if this item has no parent.*/
    public boolean isRoot();
    
    /** 
     * Returns the first ancestor of the current item that is ancestor
     * of the input sibbling item, or null if no ancestor are shared.
     */
    public IHierarchicalModel getFirstCommonAncestor(IBoundedItem sibbling);

    /**
     * Returns a parent of the current item that is a child of the input ancestor,
     * or null if the item has no parent, or no such ancestor.
     * 
     * Warning: if A is a child of B, B is root, and one calls A.getCHildrenOfAncestor(B),
     * result will be null, since there is no intermediate item between A and B.
     */
    public IHierarchicalModel getChildrenOfAncestor(IHierarchicalModel ancestor);

    
    
    public void addItemListener(IItemListener listener);
    public void removeItemListener(IItemListener listener);
    
    
    // SOURCE DATA
    public void setObject(Object data);
    public Object getObject();
    
    public ItemShape getShape();
    public void setShape(ItemShape shape);
    
    public float getMargin();
    public float getCorridor();
    
    
    /**
     * Return debug information.
     */
    public String getDebugInfo();
}

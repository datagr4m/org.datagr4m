package org.datagr4m.drawing.model.items;

import java.io.Serializable;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.jzy3d.maths.Coord2d;


public interface IGeometryProcessor extends Serializable{
    public void computeAbsolutePosition(IBoundedItem item, Coord2d position);

    
    public float computeRadialBounds(IBoundedItem item);

    /** Compute the raw bounds: the item itself, or the children. */
    public RectangleBounds computeRawRectangleBounds(IBoundedItem item);
    
    public RectangleBounds computeRawExternalBounds(IBoundedItem item);
    public RectangleBounds computeRawCorridorBounds(IBoundedItem item);
    
    /** Return bounds centered on the relative position of the item. */
    public RectangleBounds computeRelativeBounds(IBoundedItem item);
    
    /** Return internal bounds centered on the relative position of the item.
     * <ul>
     * <li>For an item, equivalent to the relative bounds.
     * <li>For a model, equivalent to the relative bounds.
     * </ul>
     */
    public RectangleBounds computeInternalBounds(IBoundedItem item);
    
    /** Return internal bounds centered on the relative position of the item.
     * <ul>
     * <li>For an item, equivalent to the relative bounds.
     * <li>For a model, equivalent to the relative bounds.
     * </ul>
     */
    public RectangleBounds computeExternalBounds(IBoundedItem item);
    
    /** Return the external bounds centered on the relative position of the item.
     * <ul>
     * <li>For an item, equivalent to the relative bounds.
     * <li>For a model, equivalent to the relative bounds.
     * </ul>
     */
    public RectangleBounds computeCorridorBounds(IBoundedItem item);
    
    /** Return the raw bounds at the absolute item's position.*/
    public RectangleBounds computeAbsoluteBounds(IBoundedItem item);
    
    /* DEBUG METHODS */
    
    public int getNumberOfAbsoluteCoordUpdate();
}

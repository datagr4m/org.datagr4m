package org.datagr4m.drawing.model.items;

import java.io.Serializable;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.jzy3d.maths.Coord2d;


public class GeometryCache  implements Serializable{
    private static final long serialVersionUID = 4397190101930381380L;

    public GeometryCache(){
        absolutePosition = new Coord2d();
    }
    
    public float getRadialBounds() {
        return radialBounds;
    }
    public void setRadialBounds(float radialBounds) {
        this.radialBounds = radialBounds;
    }
    public Coord2d getAbsolutePosition() {
        return absolutePosition;
    }
    public void setAbsolutePosition(Coord2d absolutePosition) {
        this.absolutePosition = absolutePosition;
    }
    
    
    public RectangleBounds getRawBounds() {
        return rawBounds;
    }
    public void setRawBounds(RectangleBounds rawBounds) {
        this.rawBounds = rawBounds;
    }
    
    public RectangleBounds getRawExternalBounds() {
        return rawExternalBounds;
    }

    public void setRawExternalBounds(RectangleBounds rawExternalBounds) {
        this.rawExternalBounds = rawExternalBounds;
    }
    
    public RectangleBounds getRawCorridorBounds() {
        return rawCorridorBounds;
    }

    public void setRawCorridorBounds(RectangleBounds rawCorridorBounds) {
        this.rawCorridorBounds = rawCorridorBounds;
    }

    public RectangleBounds getRelativeBounds() {
        return relativeBounds;
    }
    public void setRelativeBounds(RectangleBounds relativeBounds) {
        this.relativeBounds = relativeBounds;
    }
    public RectangleBounds getInternalBounds() {
        return internalBounds;
    }
    public void setInternalBounds(RectangleBounds internalBounds) {
        this.internalBounds = internalBounds;
    }
    public RectangleBounds getExternalBounds() {
        return externalBounds;
    }
    public void setExternalBounds(RectangleBounds externalBounds) {
        this.externalBounds = externalBounds;
    }
    public RectangleBounds getCorridorBounds() {
        return corridorBounds;
    }
    public void setCorridorBounds(RectangleBounds corridorBounds) {
        this.corridorBounds = corridorBounds;
    }
    public RectangleBounds getAbsoluteBounds() {
        return absoluteBounds;
    }
    public void setAbsoluteBounds(RectangleBounds absoluteBounds) {
        this.absoluteBounds = absoluteBounds;
    }
    public IHierarchicalNodeModel getRoot() {
        return cachedRoot;
    }
    public void setRoot(IHierarchicalNodeModel cachedRoot) {
        this.cachedRoot = cachedRoot;
    }
    public int getDepth() {
        return cachedDepth;
    }
    public void setDepth(int cachedDepth) {
        this.cachedDepth = cachedDepth;
    }
    
    

    protected float radialBounds;
    protected Coord2d absolutePosition;
    
    protected RectangleBounds rawBounds;
    protected RectangleBounds rawExternalBounds;
    protected RectangleBounds rawCorridorBounds;

    protected RectangleBounds relativeBounds;
    protected RectangleBounds internalBounds;
    protected RectangleBounds externalBounds;
    protected RectangleBounds corridorBounds;
    protected RectangleBounds absoluteBounds;
    
    protected IHierarchicalNodeModel cachedRoot;
    protected int cachedDepth = UNDEFINED_DEPTH;

    public static int UNDEFINED_DEPTH = -1;
}

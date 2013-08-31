package org.datagr4m.drawing.model.items;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.jzy3d.maths.Coord2d;


public class GeometryCacheHandler implements IGeometryCacheHandler{
    private static final long serialVersionUID = -7455034975626037049L;
    public GeometryCacheHandler(IBoundedItem item, GeometryCache cache, GeometryFlags flags, IGeometryProcessor processor) {
        this.item = item;
        this.cache = cache;
        this.flags = flags;
        this.processor = processor;
    }
    
    @Override
    public float getOrUpdateRadialBounds(){
        if(flags.radialBoundsDirty)
            updateRadialBounds();
        return cache.getRadialBounds();
    }
    
    @Override
    public void updateRadialBounds() {
        cache.setRadialBounds(processor.computeRadialBounds(item));
        flags.radialBoundsDirty = false;
    }
    
    @Override
    public RectangleBounds getOrUpdateRawRectangleBounds() {
        if(flags.rawBoundsDirty)
            updateRawRectangleBounds();            
        return cache.getRawBounds();
    }
    
    @Override
    public void updateRawRectangleBounds() {
        RectangleBounds bounds = processor.computeRawRectangleBounds(item);
        cache.setRawBounds(bounds);
        flags.rawBoundsDirty = false;
    }
    
    @Override
    public RectangleBounds getOrUpdateRawExternalBounds() {
        if(flags.rawExternalBoundsDirty)
            updateRawExternalBounds();            
        return cache.getRawExternalBounds();
    }
    
    @Override
    public void updateRawExternalBounds() {
        RectangleBounds bounds = processor.computeRawExternalBounds(item);
        cache.setRawExternalBounds(bounds);
        flags.rawExternalBoundsDirty = false;
    }

    @Override
    public RectangleBounds getOrUpdateRawCorridorBounds() {
        if(flags.rawCorridorBoundsDirty)
            updateRawCorridorBounds();            
        return cache.getRawCorridorBounds();
    }
    
    @Override
    public void updateRawCorridorBounds() {
        RectangleBounds bounds = processor.computeRawCorridorBounds(item);
        cache.setRawCorridorBounds(bounds);
        flags.rawCorridorBoundsDirty = false;
    }
    
    
    @Override
    public Coord2d getOrUpdateAbsolutePosition() {
        if(flags.absolutePositionDirty)
            updateAbsolutePosition();
        return cache.getAbsolutePosition();
    }

    @Override
    public void updateAbsolutePosition(){
        processor.computeAbsolutePosition(item, cache.getAbsolutePosition());
        flags.absolutePositionDirty = false;
    }

    @Override
    public RectangleBounds getOrUpdateRelativeBounds() {
        if(flags.relativeBoundsDirty)
            updateRelativeBounds();
        return cache.getRelativeBounds();
    }

    @Override
    public void updateRelativeBounds() {
        RectangleBounds bounds = processor.computeRelativeBounds(item);
        cache.setRelativeBounds(bounds);
        flags.relativeBoundsDirty = false;
    }

    @Override
    public RectangleBounds getOrUpdateInternalBounds() {
        if(flags.internalBoundsDirty)
            updateInternalBounds();
        return cache.getInternalBounds();
    }

    @Override
    public void updateInternalBounds() {
        RectangleBounds bounds = processor.computeInternalBounds(item);
        cache.setInternalBounds(bounds);
        flags.internalBoundsDirty = false;
    }

    @Override
    public RectangleBounds getOrUpdateExternalBounds() {
        if(flags.externalBoundsDirty)
            updateExternalBounds();
        return cache.getExternalBounds();
    }

    @Override
    public void updateExternalBounds() {
        RectangleBounds bounds = processor.computeExternalBounds(item);
        cache.setExternalBounds(bounds);
        flags.externalBoundsDirty = false;
    }

    @Override
    public RectangleBounds getOrUpdateAbsoluteBounds() {
        if(flags.absoluteBoundsDirty)
            updateAbsoluteBounds();
        return cache.getAbsoluteBounds();
    }

    @Override
    public void updateAbsoluteBounds() {
        RectangleBounds bounds = processor.computeAbsoluteBounds(item);
        cache.setAbsoluteBounds(bounds);
        flags.absoluteBoundsDirty = false;
    }
    
    @Override
    public RectangleBounds getOrUpdateCorridorBounds() {
        if(flags.corridorBoundsDirty)
            updateCorridorBounds();
        return cache.getCorridorBounds();
    }

    @Override
    public void updateCorridorBounds() {
        RectangleBounds bounds = processor.computeAbsoluteBounds(item);
        cache.setCorridorBounds(bounds);
        flags.corridorBoundsDirty = false;
    }
    
    protected GeometryCache cache;
    protected GeometryFlags flags;
    protected IGeometryProcessor processor;    
    protected IBoundedItem item;
}

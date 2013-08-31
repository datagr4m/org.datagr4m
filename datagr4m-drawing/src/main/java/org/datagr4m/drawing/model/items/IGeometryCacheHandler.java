package org.datagr4m.drawing.model.items;

import java.io.Serializable;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.jzy3d.maths.Coord2d;


public interface IGeometryCacheHandler extends Serializable{
    public Coord2d getOrUpdateAbsolutePosition();

    public float getOrUpdateRadialBounds();

    public RectangleBounds getOrUpdateRawRectangleBounds();
    public RectangleBounds getOrUpdateRawExternalBounds();
    public RectangleBounds getOrUpdateRawCorridorBounds();

    public RectangleBounds getOrUpdateRelativeBounds();
    public RectangleBounds getOrUpdateInternalBounds();
    public RectangleBounds getOrUpdateExternalBounds();
    public RectangleBounds getOrUpdateCorridorBounds();
    public RectangleBounds getOrUpdateAbsoluteBounds();
    
    public void updateAbsolutePosition();
    public void updateRadialBounds();
    
    public void updateRawRectangleBounds();
    public void updateRawExternalBounds();
    public void updateRawCorridorBounds();
    
    public void updateRelativeBounds();
    public void updateInternalBounds();
    public void updateExternalBounds();
    public void updateCorridorBounds();
    public void updateAbsoluteBounds();
}

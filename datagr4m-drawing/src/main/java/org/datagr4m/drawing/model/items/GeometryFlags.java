package org.datagr4m.drawing.model.items;

import java.io.Serializable;

public class GeometryFlags implements Serializable{
    private static final long serialVersionUID = -2903754655067216241L;
    public GeometryFlags(){
        dirtyAll();
    }
    
    public void onChildrenPositionChanged(){
        dirtyAllBounds();
    }
    
    public void onParentPositionChanged(){
        dirtyAllAbsoluteData();
    }
    
    public void onPositionChanged(){
        dirtyAllAbsoluteData();
        dirtyAllBounds(); // EXPERIMENTAL
    }
    
    /*************/
    
    public void dirtyAll(){
        dirtyAllStates();
        dirtyAllBounds();
    }
    
    public void dirtyAllBounds(){
        radialBoundsDirty = true;
        rawBoundsDirty = true;
        rawExternalBoundsDirty = true;
        rawCorridorBoundsDirty = true;
        relativeBoundsDirty = true;
        internalBoundsDirty = true;
        externalBoundsDirty = true;
        absoluteBoundsDirty = true;
        corridorBoundsDirty = true;
    }
    
    public void dirtyAllStates(){
        absolutePositionDirty = true;
        //parentPositionDirty = true;
        //childrenPositionDirty = true;        
    }
    
    public void dirtyAllAbsoluteData(){
        absolutePositionDirty = true;
        absoluteBoundsDirty = true;
    }
    
    //public boolean positionDirty;
    public boolean absolutePositionDirty;
    //public boolean parentPositionDirty;
    //public boolean childrenPositionDirty;
    
    public boolean radialBoundsDirty;
    
    public boolean rawBoundsDirty;
    public boolean rawExternalBoundsDirty;
    public boolean rawCorridorBoundsDirty;
    
    public boolean relativeBoundsDirty;
    public boolean internalBoundsDirty;
    public boolean externalBoundsDirty;
    public boolean absoluteBoundsDirty;
    public boolean corridorBoundsDirty;
}

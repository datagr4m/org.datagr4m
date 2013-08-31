package org.datagr4m.drawing.model.items.hierarchical.pair;

import java.awt.geom.CubicCurve2D;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;


/** 
 * Hold a pair of item, and the curve used to draw their relation.
 */
public interface IHierarchicalPairModel extends IHierarchicalModel{
    public IBoundedItem getFirst();
    public IBoundedItem getSecond();
    public float getSpacing();
    
    public CubicCurve2D getCurve();
    
    
    public void setCurve(CubicCurve2D curve);
}

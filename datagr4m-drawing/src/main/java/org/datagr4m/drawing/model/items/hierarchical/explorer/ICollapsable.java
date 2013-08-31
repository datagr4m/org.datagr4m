package org.datagr4m.drawing.model.items.hierarchical.explorer;

import org.datagr4m.drawing.model.bounds.RectangleBounds;

public interface ICollapsable {
    public boolean isCollapsed();
    public void setCollapsed(boolean status);
    public void toggleCollapsed();
    
    public boolean canCollapse();
    public void setCanCollapse(boolean value);

    /** Return true if the item can be seen as a group containing item, which is true by default.*/
    public boolean canExpand();
    public void setCanExpand(boolean value);
    
    /** Returns the radial bounds according to the collapsed status.*/
    public float getRadialBounds();
    /** Returns the radial bounds when the groups is expanded.*/
    public float getExpandedRadialBounds();    
    /** Returns the radial bounds when the group is collapsed.*/
    public float getCollapsedRadialBounds();


    /** Returns the rectangle bounds according to the collapsed status.*/
    public RectangleBounds getRawRectangleBounds();
    /** Returns the rectangle bounds when the groups is expanded.*/
    public RectangleBounds getExpandedRectangleBounds();    
    /** Returns the rectangle bounds when the group is collapsed.*/
    public RectangleBounds getCollapsedRectangleBounds();
    
    
    public CollapsedModelItem getCollapsedModel();
    public void setCollapsedModel(CollapsedModelItem model);
}

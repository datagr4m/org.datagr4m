package org.datagr4m.drawing.model.items.hierarchical.pair;

import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.AbstractHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.jzy3d.maths.Coord2d;


public class HierarchicalPairModel extends AbstractHierarchicalModel implements IHierarchicalPairModel{
    public HierarchicalPairModel(IHierarchicalNodeModel parent, IBoundedItem first, IBoundedItem second) {
        super(parent);
        
        // register first and second as children
        this.first = first;
        this.second = second;
        if(first!=null)
            addChild(first, false);
        if(second!=null)
            addChild(second, false);
        
        refreshBounds(true);
    }
        
    public HierarchicalPairModel(IBoundedItem first, IBoundedItem second) {
        this(null, first, second);
    }
    
    public HierarchicalPairModel(IHierarchicalNodeModel parent) {
        this(parent, null, null);
    }
    
    public HierarchicalPairModel() {
        this(null, null, null);
    }
    
    /***************/
    
    @Override
    public IBoundedItem getFirst() {
        return first;
    }

    @Override
    public IBoundedItem getSecond() {
        return second;
    }
    
    @Override
    public CubicCurve2D getCurve() {
        return curve;
    }
    
    /** Set a curve for this pair model, assuming it is computed by the layout
     */
    @Override
    public void setCurve(CubicCurve2D curve) {
        this.curve = curve;
    }
        
    @Override
	public boolean addChild(IBoundedItem item, boolean refreshBounds){
        boolean s = super.addChild(item, refreshBounds);
        
        List<IBoundedItem> list = new ArrayList<IBoundedItem>(children);
        if(list.size()==1)
            first = list.get(0);
        else if(list.size()==2){
            second = list.get(1);
        }
        else{
            throw new RuntimeException("no children was added!");
        }
            
        return s;
    }
    
    @Override
    public boolean addChildren(Collection<IBoundedItem> items){
        throw new RuntimeException("should call setFirst/setSecond to setup children of this model.");
    }
    
    /**
     * Spacing is not cached and simply computes the distance between first and second item.
     */
    @Override
    public float getSpacing(){
        if(first==null || second==null)
            return -1;
        else
            return (float)first.getPosition().distance(second.getPosition());
    }

    public void setFirst(IBoundedItem item) {
        this.first = item;
        refreshChildren();
    }

    public void setSecond(IBoundedItem item) {
        this.second = item;
        refreshChildren();
    }
    
    protected void refreshChildren(){
        clearChildren(false);
        addChild(first, false);
        addChild(second, false);
        refreshBounds(true);
    }
        
    /*******************/
    
    protected void computeRadialBounds(){
        double x1 = first.getPosition().distance(Coord2d.ORIGIN)+first.getRadialBounds();
        double x2 = second.getPosition().distance(Coord2d.ORIGIN)+second.getRadialBounds();
        cachedRadialBounds = (float)Math.max(x1,x2);
    }
    
    
    protected IBoundedItem first;
    protected IBoundedItem second;
    protected float spacing;
    
    protected CubicCurve2D curve;
    
    private static final long serialVersionUID = -2084464710276992579L;
}

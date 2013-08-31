package org.datagr4m.drawing.layout.slots.geometry.global;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryBuilder;
import org.datagr4m.drawing.model.slots.SlotGroup;


public abstract class AbstractGlobalSlotGeometryBuilder implements IGlobalSlotGeometryBuilder, Serializable{
    private static final long serialVersionUID = 938791251849104903L;

    public AbstractGlobalSlotGeometryBuilder(ISlotGeometryBuilder delegate){
        setDelegate(delegate);
    }
    
    @Override
    public ISlotGeometryBuilder getDelegate() {
        return delegate;
    }
    
    @Override
    public void setDelegate(ISlotGeometryBuilder builder) {
        if(builder instanceof IGlobalSlotGeometryBuilder)
            throw new IllegalArgumentException("delegate can't be a global builder");
        delegate = builder;
    }
    
    /*********/
    
    public boolean isFreeX(Double x){
        return !xBooking.containsKey(x);
    }

    public boolean isFreeY(Double y){
        return !yBooking.containsKey(y);
    }
    
    public void register(SlotGroup group, int id, Point2D point){
        Pair<SlotGroup,Integer> value = new Pair<SlotGroup,Integer>(group, id);
        xBooking.put(point.getX(), value);
        yBooking.put(point.getY(), value);
    }

    /*********/
    
    protected double getMinY(SlotGroup group){
        return group.getCenter().getY() - getAvailableHeight(group)/2;
    }
    
    protected double getMaxY(SlotGroup group){
        return group.getCenter().getY() + getAvailableHeight(group)/2;
    }
    
    protected double getMinX(SlotGroup group){
        return group.getCenter().getX() - getAvailableWidth(group)/2;
    }
    
    protected double getMaxX(SlotGroup group){
        return group.getCenter().getX() + getAvailableWidth(group)/2;
    }
    
    protected double getAvailableWidth(SlotGroup group){
        if(group.isHorizontal())
            return (group.getWidth()-group.getMargin()/100*group.getWidth()*2);
        else
            return group.getWidth();
    }
    
    protected double getAvailableHeight(SlotGroup group){
        if(group.isVertical())
            return (group.getHeight()-group.getMargin()/100*group.getHeight()*2);
        else
            return group.getHeight();
    }
    
    /*********/
    
    protected ISlotGeometryBuilder delegate;
    protected Map<Double,Pair<SlotGroup,Integer>> yBooking = new HashMap<Double,Pair<SlotGroup,Integer>>();
    protected Map<Double,Pair<SlotGroup,Integer>> xBooking = new HashMap<Double,Pair<SlotGroup,Integer>>();
}

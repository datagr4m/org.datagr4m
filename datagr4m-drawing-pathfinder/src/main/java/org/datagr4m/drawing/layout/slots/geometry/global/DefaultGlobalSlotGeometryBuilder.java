package org.datagr4m.drawing.layout.slots.geometry.global;

import java.awt.geom.Point2D;

import org.datagr4m.drawing.layout.slots.geometry.DefaultSlotGeometryBuilder;
import org.datagr4m.drawing.layout.slots.geometry.IPathStartValidator;
import org.datagr4m.drawing.model.slots.SlotGroup;


public class DefaultGlobalSlotGeometryBuilder extends AbstractGlobalSlotGeometryBuilder implements IPathStartValidator{
    private static final long serialVersionUID = 1269637812879802484L;
    public static double BASE_SEARCH_STEP = 1d;
    
    public DefaultGlobalSlotGeometryBuilder(){
        super(new DefaultSlotGeometryBuilder());
        getDelegate().setValidator(this);
    }
    
    /*****************/

    @Override
    public void build(SlotGroup group) {
        getDelegate().build(group); // will call validate, generate, and remember
    }
    
    @Override
    public boolean validate(Point2D path, SlotGroup group, int slotid){
        return isFreeX(path.getX()) && isFreeY(path.getY());
    }

    @Override
    public Point2D generate(Point2D base, SlotGroup group, int slotid) {
        double x = base.getX();
        double y = base.getY();
        if(!isFreeX(x)){
            //double minx = getMinX(group);
            //double maxx = getMaxX(group);
            x = searchX(base.getX(),getMinX(group), getMaxX(group), BASE_SEARCH_STEP);
        }
        if(!isFreeY(y)){
            //double miny = getMinY(group);
            //double maxy = getMaxY(group);
            y = searchY(base.getY(),getMinY(group), getMaxY(group), BASE_SEARCH_STEP);
        }
        else
            return base; // no problem actually!
        return new Point2D.Double(x, y);

    }
    
    protected double searchX(double start, double min, double max, double step){
        double value = start;
        double currentStep = step;
        while(true){
            value = start -currentStep;
            while(value>min){
                value-=currentStep;
                if(isFreeX(value))
                    return value;
            }
            value = start +currentStep;
            while(value<max){
                value+=currentStep;
                if(isFreeX(value))
                    return value;
            }
            
            if(currentStep<(step/16))
                throw new RuntimeException("can't find another X, even after reducing step to:"+currentStep+" (from " + step+") range was min:"+min+" max:"+max);
            currentStep/=2;
        }
    }
    
    protected double searchY(double start, double min, double max, double step){
        if(max<=min)
            throw new IllegalArgumentException("max " + max + " must be superior to min " + min);
        double value = start;
        double currentStep = step;
        while(true){
            value = start -currentStep;
            while(value>min){
                value-=currentStep;
                if(isFreeY(value))
                    return value;
            }
            value = start +currentStep;
            while(value<max){
                value+=currentStep;
                if(isFreeY(value))
                    return value;
            }
            if(currentStep<(step/16))
                throw new RuntimeException("can't find another Y, even after reducing step to:"+currentStep+" (from " + step+") range was min:"+min+" max:"+max);
            currentStep/=2;
        }
    }
    
    
    @Override
    public void remember(Point2D pathstart, SlotGroup group, int slotid){
        register(group, slotid, pathstart);
    }

    
    /********** IGNORE THESE METHODS *********/
    
    @Override
    public void makeSlot(SlotGroup group, int i, Point2D anchor, Point2D path) {
        throw new RuntimeException("not implemented");
    }
    
    @Override
    public IPathStartValidator getValidator() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setValidator(IPathStartValidator validator) {
        throw new RuntimeException("not implemented");
    }
}

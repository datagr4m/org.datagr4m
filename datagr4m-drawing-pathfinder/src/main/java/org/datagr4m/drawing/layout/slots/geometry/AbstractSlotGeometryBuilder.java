package org.datagr4m.drawing.layout.slots.geometry;

import java.awt.geom.Point2D;
import java.io.Serializable;

import org.datagr4m.drawing.model.slots.SlotGroup;


/**
 * Provide services for storing the slot coordinate.
 * 
 * If a validator is given, will use it to verify if the provided
 * path coordinate is valid, and will use it to generate another coordinate
 * in the contrary.
 */
public abstract class AbstractSlotGeometryBuilder implements ISlotGeometryBuilder, Serializable{

    @Override
	public void makeSlot(SlotGroup group, int i, Point2D anchor, Point2D path){
        // using a validation policy
        if(validator!=null){
            if(validator.validate(path, group, i)){
                group.setSlotAnchorPoint(i, anchor);
                group.setSlotPathPoint(i, path); 
                validator.remember(path, group, i);
            }
            // if did not validate: generating another point
            else{
                Point2D ok = validator.generate(path, group, i);
                
                if(group.isHorizontal()){
                    if(ok.getX()!=anchor.getX())
                        group.setSlotAnchorPoint(i, new Point2D.Double(ok.getX(), anchor.getY()));
                    else
                        group.setSlotAnchorPoint(i, anchor);
                }
                else{
                    if(ok.getY()!=anchor.getY())
                        group.setSlotAnchorPoint(i, new Point2D.Double(anchor.getX(), ok.getY()));
                    else
                        group.setSlotAnchorPoint(i, anchor);
                }
                group.setSlotPathPoint(i, ok);
                validator.remember(ok, group, i);
            }
        }
        // no validation policy: direct registration of anchor and path start points
        else{
            group.setSlotAnchorPoint(i, anchor);
            group.setSlotPathPoint(i, path); 
        }
    }
    
    @Override
	public IPathStartValidator getValidator() {
        return validator;
    }

    @Override
	public void setValidator(IPathStartValidator validator) {
        this.validator = validator;
    }

    protected IPathStartValidator validator;
    
    private static final long serialVersionUID = 2133862491221169284L;
}

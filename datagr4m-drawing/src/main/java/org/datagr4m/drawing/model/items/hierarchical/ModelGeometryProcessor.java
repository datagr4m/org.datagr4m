package org.datagr4m.drawing.model.items.hierarchical;

import org.datagr4m.drawing.model.bounds.BoundsMerger;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.IGeometryProcessor;
import org.datagr4m.drawing.model.items.ItemShape;
import org.jzy3d.maths.Coord2d;


/**
 * Makes actual computation of group models' geometry.
 * 
 * @author Martin Pernollet
 */
public class ModelGeometryProcessor implements IGeometryProcessor{
    private static final long serialVersionUID = -4059377354230866060L;


    @Override
    public float computeRadialBounds(IBoundedItem item) {
        float radial = 0;
        RectangleBounds b = shiftToAbsolute(item.getRawRectangleBounds(), item.getAbsolutePosition());
        Coord2d p = item.getAbsolutePosition();
        // les bounds ne sont peut �tre pas centr�es sur la position de l'item!!
        // il convient donc de conna�tre le bord le plus �loign� du centre
        double d1 = p.distanceSq(b.getTopLeftCorner());
        double d2 = p.distanceSq(b.getTopRightCorner());
        double d3 = p.distanceSq(b.getBottomLeftCorner());
        double d4 = p.distanceSq(b.getBottomRightCorner());
        double maxDist = Math.max(Math.max(d1,d2),Math.max(d3,d4));
        radial = (float)Math.sqrt( maxDist );

        radial += item.getMargin();
        return radial;
    }
    
    public static RectangleBounds shiftToAbsolute(RectangleBounds relativeBounds, Coord2d absoluteModelPosition){
        if(SHIFT_BY_TOP_LEFT){
            // shift bound so that they're rendered @ model absolute position
            // (top left corner is relative to the model position)
            Coord2d absoluteTopLeftCorner = absoluteModelPosition.add(relativeBounds.getTopLeftCorner());
            return relativeBounds.shiftTopLeftTo(absoluteTopLeftCorner);
        }
        else{
         // Another way of doing it: != result?!!
            return relativeBounds.shiftCenterTo(absoluteModelPosition);
        }
    }
    
    public static boolean SHIFT_BY_TOP_LEFT = true;
    

    /* RAW BOUNDS */

    @Override
    public RectangleBounds computeRawRectangleBounds(IBoundedItem input) {
        IHierarchicalModel model = subtype(input);
        
        BoundsMerger merger = new BoundsMerger();
        for (IBoundedItem item: model.getChildren()) {
            RectangleBounds b;
            if(item instanceof IHierarchicalModel){
                //b = item.getRelativeRectangleBounds();
                b = item.getExternalRectangleBounds();
            }
            else
                b = item.getRelativeRectangleBounds();
            merger.append(b);
        }
        
        // init bounds with top-left and bottom-right corners
        /*if(merger.hasNan())
            throw new RuntimeException("bounds merger has nan values!");
        else */if(merger.valid())
            return merger.build();
        else{
            if(model.getChildren().size()==0)
                return new RectangleBounds(10,10);
            else
                throw new RuntimeException("bounds merger invalid! Number of model child: " + model.getChildren().size() + " for " + model.getLabel());
        }
    }
    
    /* RAW EXTERNAL */
    
    @Override
    public RectangleBounds computeRawExternalBounds(IBoundedItem input) {
        if(input.getShape()==ItemShape.CIRCLE){
            float r = input.getRadialBounds()+input.getMargin();
            return new RectangleBounds(r*2, r*2);
        }
        else if(input.getShape()==ItemShape.RECTANGLE){
            RectangleBounds bounds = input.getRawRectangleBounds().clone();
            bounds.enlargeSelfInPlace(input.getMargin(), input.getMargin());
            return bounds;
        }
        else
            throw new RuntimeException();
    }
    
    @Override
    public RectangleBounds computeRawCorridorBounds(IBoundedItem input) {
        if(input.getShape()==ItemShape.CIRCLE){
            float r = input.getRadialBounds()+input.getMargin()+input.getCorridor();
            return new RectangleBounds(r*2, r*2);
        }
        else if(input.getShape()==ItemShape.RECTANGLE){
            float margin = input.getMargin() + input.getCorridor();
            RectangleBounds bounds = input.getRawRectangleBounds().clone();
            bounds.enlargeSelfInPlace(margin, margin);
            return bounds;
        }
        else
            throw new RuntimeException();
    }

    /* RELATIVE */

    @Override
    public RectangleBounds computeRelativeBounds(IBoundedItem input) {
        return input.getRawRectangleBounds().shiftCenterTo(input.getPosition());
    }

    /* INTERNAL */

    @Override
    public RectangleBounds computeInternalBounds(IBoundedItem input) {
        return input.getRawRectangleBounds().shiftCenterTo(input.getPosition());
    }

    /* EXTERNAL */
    
    @Override
    public RectangleBounds computeExternalBounds(IBoundedItem input) {
        if(input.getShape()==ItemShape.CIRCLE){
            float r = input.getRadialBounds()+input.getMargin();
            Coord2d c = input.getPosition();
            return new RectangleBounds(c.x-r, c.y-r, r*2, r*2);
        }
        else if(input.getShape()==ItemShape.RECTANGLE){
            RectangleBounds bounds = input.getRawRectangleBounds().clone();
            bounds.enlargeSelfInPlace(input.getMargin(), input.getMargin());
            bounds.shiftSelfCenterTo(input.getPosition());
            return bounds;
        }
        else
            throw new RuntimeException();
    }

    @Override
    public RectangleBounds computeCorridorBounds(IBoundedItem input) {
        if(input.getShape()==ItemShape.CIRCLE){
            RectangleBounds bounds = input.getExternalRectangleBounds();
            bounds.enlargeSelfInPlace(input.getCorridor(), input.getCorridor());
            return bounds;
        }
        else if(input.getShape()==ItemShape.RECTANGLE){
            float margin = input.getMargin()+input.getCorridor();
            RectangleBounds bounds = input.getRawRectangleBounds().clone();
            bounds.enlargeSelfInPlace(margin, margin);
            return bounds;
        }
        else
            throw new RuntimeException("not implemented");
    }

    @Override
    public RectangleBounds computeAbsoluteBounds(IBoundedItem input) {
        IHierarchicalModel model = subtype(input);
        
        if(COMPUTE_ABS_BOUNDS_USING_CORNER){
            RectangleBounds relativeBounds = model.getRawRectangleBounds();
            Coord2d absoluteModelPosition = model.getAbsolutePosition();
            // shift bound so that they're @ model absolute position
            // (top left corner is relative to the model position)
            Coord2d absoluteTopLeftCorner = absoluteModelPosition.add(relativeBounds.getTopLeftCorner());
            return relativeBounds.shiftTopLeftTo(absoluteTopLeftCorner);
        }
        else{//TODO why it does not work?
            return model.getRawRectangleBounds().shiftCenterTo(model.getAbsolutePosition());
        }
    }
    
    public static boolean COMPUTE_ABS_BOUNDS_USING_CORNER = true;

    /* ABSOLUTE POSITION */

    @Override
    public void computeAbsolutePosition(IBoundedItem input, Coord2d position) {
        if(input.getParent()==null)
            position.set(input.getPosition().clone());
        else{
            position.set(input.getParent().getAbsolutePosition().add(input.getPosition()));
        }
        nAbsCoordUpd++;
    }
    
    protected int nAbsCoordUpd = 0;
    
    @Override
	public int getNumberOfAbsoluteCoordUpdate(){
        return nAbsCoordUpd;
    }
    
    /************/

    protected IHierarchicalModel subtype(IBoundedItem item){
        return (IHierarchicalModel) item;
    }
}

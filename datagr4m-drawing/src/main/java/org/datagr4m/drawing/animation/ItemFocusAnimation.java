package org.datagr4m.drawing.animation;

import java.awt.geom.Point2D;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.maths.geometry.functions.LinearInterpolation;
import org.datagr4m.maths.geometry.functions.UpDownInterpolation;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.animation.AbstractAnimation;
import org.datagr4m.viewer.animation.IAnimation;
import org.jzy3d.maths.Array;
import org.jzy3d.maths.Coord2d;


public class ItemFocusAnimation extends AbstractAnimation implements IAnimation{
    public static int MIN_STEP = 3;
    
    public ItemFocusAnimation(IView view, IBoundedItem target) {
        this.view = view;
        this.target = target;
        
        Point2D start = view.getViewPoint(true);
        Point2D scale = view.getLastScaleSet();
        if(scale==null)
            scale = new Point2D.Double(1,1);
        float targetScale = 1.5f;
        
        
        
        Coord2d startC = Pt.cloneAsCoord2d(start);
        Coord2d targetC = target.getAbsolutePosition();
        double dist = targetC.distance(startC);
        double shiftStep = 200;
        nstep = (int)(dist/shiftStep);
        if(nstep<=MIN_STEP) // min step number
            nstep = MIN_STEP;
        
        viewpoints = LinearInterpolation.build(startC, targetC, nstep);        
        if(scale.getX()==targetScale){
            scales = UpDownInterpolation.build((float)scale.getX(), targetScale, nstep);
            //System.out.println("hop");
        }
        else{
            scales = LinearInterpolation.build((float)scale.getX(), targetScale, nstep);
        }
        Array.print(scales);
        Logger.getLogger(ItemFocusAnimation.class).info("dist="+dist+" nstep="+nstep);
        
        n = 0;
    }
    
    @Override
    public boolean next() {
        if(n<nstep){
            view.centerAt(viewpoints[n], 0, scales[n]);
            n++;
            return false;
        }
        else
            return true;
    }

    /****/
    
    public IView getView() {
        return view;
    }
    public IBoundedItem getTarget() {
        return target;
    }
    protected IView view;
    protected IBoundedItem target;
    
    protected float[] scales;
    protected Coord2d[] viewpoints;
    protected int n;
    protected int nstep;
}

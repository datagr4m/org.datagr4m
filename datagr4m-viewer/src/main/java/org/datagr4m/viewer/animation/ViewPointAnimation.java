package org.datagr4m.viewer.animation;

import org.datagr4m.maths.geometry.functions.LinearInterpolation;
import org.datagr4m.viewer.IView;
import org.jzy3d.maths.Coord2d;


public class ViewPointAnimation extends AbstractAnimation{
    public ViewPointAnimation(IView view, Coord2d from, Coord2d to, float duration) {
        this.view = view;
        if(from!=null && to!=null)
            this.keypoints = LinearInterpolation.build(from, to, (int)(duration/IAnimationStack.ANIMATION_FRAME_RATE));
        n = 0;
    }
    
    @Override
    public boolean next() {
        if(keypoints==null)
            return true;
        
        if(n<keypoints.length){
            view.centerAt(keypoints[n]);
            n++;
            return false;
        }
        else
            return true;
    }

    protected IView view;
    protected Coord2d[] keypoints;
    protected int n;
}

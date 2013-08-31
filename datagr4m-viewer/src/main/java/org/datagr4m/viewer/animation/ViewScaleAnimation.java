package org.datagr4m.viewer.animation;

import org.datagr4m.viewer.IView;
import org.jzy3d.maths.Coord2d;


public class ViewScaleAnimation extends AbstractAnimation{
    public ViewScaleAnimation(IView view, float duration) {
        this.view = view;
        steps = (int)(duration/IAnimationStack.ANIMATION_FRAME_RATE);
        n = 0;
        
        if(steps%2!=0)
            steps++;
        half = steps/2;
    }
    
    @Override
    public boolean next() {
        if(n<half)
            view.scale(0.95f, 0.95f);
        else if(n<=steps)
            view.scale(1.05f, 1.05f);
        else
            return true;
        n++;
        return false;
    }

    protected IView view;
    protected Coord2d[] keypoints;
    protected int n;
    protected int steps;
    protected int half;
}

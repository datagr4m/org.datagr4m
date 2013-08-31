package org.datagr4m.viewer.animation;

import java.awt.geom.Rectangle2D;

import org.datagr4m.maths.geometry.functions.LinearInterpolation;
import org.datagr4m.viewer.IView;
import org.jzy3d.maths.Coord2d;


public class ViewBoundsAnimation extends AbstractAnimation{
    public ViewBoundsAnimation(IView view, Rectangle2D from, Rectangle2D to, float duration) {
        this.view = view;
        
        Coord2d xyfrom = new Coord2d(from.getMinX(), from.getMinY());
        Coord2d xyto = new Coord2d(to.getMinX(), to.getMinY());
        Coord2d dimfrom = new Coord2d(from.getWidth(), from.getHeight());
        Coord2d dimto = new Coord2d(to.getWidth(), to.getHeight());
        
        this.xykeypoints = LinearInterpolation.build(xyfrom, xyto, (int)(duration/IAnimationStack.ANIMATION_FRAME_RATE));
        this.dimkeypoints = LinearInterpolation.build(dimfrom, dimto, (int)(duration/IAnimationStack.ANIMATION_FRAME_RATE));
        n = 0;
    }
    
    @Override
    public boolean next() {
        if(n<xykeypoints.length){
            view.fit(new Rectangle2D.Float(xykeypoints[n].x, xykeypoints[n].y, dimkeypoints[n].x, dimkeypoints[n].y), false);
            n++;
            return false;
        }
        else
            return true;
    }

    protected IView view;
    protected Coord2d[] xykeypoints;
    protected Coord2d[] dimkeypoints;
    protected int n;
}

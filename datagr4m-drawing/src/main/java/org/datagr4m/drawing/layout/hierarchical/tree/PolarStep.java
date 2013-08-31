package org.datagr4m.drawing.layout.hierarchical.tree;

import java.awt.geom.Point2D;

import org.datagr4m.maths.geometry.PointUtils;


/**
 * Store the angle ZCT angle with
 * C center
 * Z zero, a point right to C
 * T target, a point around C
 */
public class PolarStep {
    public PolarStep(Point2D center, Point2D target, float dist, PolarStepDirection direction){
        double angle = PointUtils.angle(center, target);
        this.direction = direction;
        if(direction==PolarStepDirection.RIGHT)
            cartesianStep = PointUtils.cartesian(angle-Math.PI/2, dist);
        else if(direction==PolarStepDirection.UP)
            cartesianStep = PointUtils.cartesian(angle, dist);
        else if(direction==PolarStepDirection.LEFT)
            cartesianStep = PointUtils.cartesian(angle+Math.PI/2, dist);
        else if(direction==PolarStepDirection.DOWN)
            cartesianStep = PointUtils.cartesian(angle+Math.PI, dist);        
    }
    
    public Point2D apply(Point2D point){
        return PointUtils.add(point, cartesianStep);        
    }
    
    protected Point2D cartesianStep;
    protected PolarStepDirection direction;
}

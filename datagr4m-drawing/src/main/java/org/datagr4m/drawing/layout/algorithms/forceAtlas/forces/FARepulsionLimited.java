package org.datagr4m.drawing.layout.algorithms.forceAtlas.forces;

import org.datagr4m.drawing.layout.algorithms.forces.ForceVectorUtils;
import org.datagr4m.drawing.layout.algorithms.forces.IReviewableForce;
import org.datagr4m.drawing.layout.algorithms.forces.ItemForceVector;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public class FARepulsionLimited extends FARepulsion implements IReviewableForce{
    private static final long serialVersionUID = 6573446452897390144L;
    
    protected double minDist = -1;
    
    public FARepulsionLimited(IBoundedItem owner, IBoundedItem source, double minDist) {
        super(owner, source, 1);
        this.minDist = minDist;
    }

    public FARepulsionLimited(IBoundedItem owner, IBoundedItem source) {
        super(owner, source);
    }
    
    @Override
	public void apply(double repulsion, boolean noCollide){
        if(compute()){
            double c = repulsion * (1 + ownerDegree) * (1 + sourceDegree) * factor;
           
            //double xDist = owner.getPosition().x - source.getPosition().x - (owner.getRadialBounds() + source.getRadialBounds());   // distance en x entre les deux noeuds
            //double yDist = owner.getPosition().y - source.getPosition().y - (owner.getRadialBounds() + source.getRadialBounds());
            //double dist = Math.sqrt(xDist * xDist + yDist * yDist);// - owner.getRadialBounds() - source.getRadialBounds();

            double xDist = owner.getPosition().x - source.getPosition().x;   // distance en x entre les deux noeuds
            double yDist = owner.getPosition().y - source.getPosition().y;
            
            if(xDist == 0 && yDist == 0){
                ownerForce.dx += Math.random();
                ownerForce.dy += Math.random();
                sourceForce.dx -= Math.random();
                sourceForce.dy -= Math.random();
            }
            else{
                double dist = Math.sqrt(xDist * xDist + yDist * yDist) - owner.getRadialBounds()*2 - source.getRadialBounds()*2;
                
                if (dist > 0) {
                    double f = ForceVectorUtils.repulsion(c, dist);

                    logLastMove(xDist / dist * f, yDist / dist * f);
                    
                    ownerForce.dx += xDist / dist * f; // car la dist peut �tre negative
                    ownerForce.dy += yDist / dist * f;

                    sourceForce.dx -= xDist / dist * f;
                    sourceForce.dy -= yDist / dist * f;
                } else if (dist != 0) {
                    double f = -c;  //flat repulsion

                    logLastMove(+xDist / dist * f, +yDist / dist * f);
                    
                    ownerForce.dx += xDist / dist * f;
                    ownerForce.dy += yDist / dist * f;

                    sourceForce.dx -= xDist / dist * f;
                    sourceForce.dy -= yDist / dist * f;
                } 
            }
        }
        else
            logLastMove(0, 0);
    }
    
    protected boolean compute(){
        double dist = owner.getPosition().distance(source.getPosition())-owner.getRadialBounds()-source.getRadialBounds();
        return (minDist==-1 || dist<minDist);
    }

    /************/
    
    protected Coord2d lastMove;
    
    @Override
    public Coord2d getLastMove() {
        return lastMove;
    }

    public void logLastMove(ItemForceVector v){
        this.lastMove = new Coord2d(v.dx, v.dy);
    }
    
    public void logLastMove(double x, double y){
        this.lastMove = new Coord2d(x, y);
    }
}

package org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone.polar;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FARepulsion;
import org.datagr4m.drawing.layout.algorithms.forces.ForceVectorUtils;
import org.datagr4m.drawing.layout.algorithms.forces.IReviewableForce;
import org.datagr4m.drawing.layout.algorithms.forces.ItemForceVector;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.maths.geometry.functions.LinearFunction;
import org.jzy3d.maths.Coord2d;


/** Apply a polar force, so that the source item is moved relative to the owner,
 * in order to have the line owner-source perpendicular to p1-p2
 */

//TODO: EXTRACT DIVISER POUR EVITER LA FORCE DE TOUT CEUX QUI POUSSE QUAND L ENVELOPPE RADIALE EST ETROITE
public class PolarDistanceRangeForce2 extends FARepulsion implements IReviewableForce{
    private static final long serialVersionUID = -3910344755243537646L;
    protected LinearFunction func;
    protected double minDist = 0;
    protected double maxDist = 0;
    
    public PolarDistanceRangeForce2(IBoundedItem owner, IBoundedItem source, double minDist, double maxDist) {
        super(owner, source);
        this.minDist = minDist;
        this.maxDist = maxDist;
    }
    
    @Override
	public void setAll(int ownerDegree, ItemForceVector ownerForce, int sourceDegree, ItemForceVector sourceForce){
        super.setAll(ownerDegree, ownerForce, sourceDegree, sourceForce);
    }

    @Override
	public void apply(double repulsion, boolean noCollide){
        Coord2d p = owner.getPosition().sub(source.getPosition()).fullPolar();
        
        double c = repulsion * (1 + ownerDegree) * (1 + sourceDegree) * factor;
        double dist = 0;
        if(p.y<minDist){
            dist = minDist-p.y;
            p.y += dist*ForceVectorUtils.repulsion(c, dist);
        }
        else if(p.y>maxDist){
            dist = p.y-maxDist;
            p.y -= dist*ForceVectorUtils.repulsion(c, dist)/1; 
        }
        Coord2d expect = source.getPosition().add(p.cartesian());
        
        ownerForce.dx += expect.x-owner.getPosition().x;
        ownerForce.dy += expect.y-owner.getPosition().y;
        
        logLastMove(expect.x-owner.getPosition().x, expect.y-owner.getPosition().y);
    }
    
/**********/
    
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

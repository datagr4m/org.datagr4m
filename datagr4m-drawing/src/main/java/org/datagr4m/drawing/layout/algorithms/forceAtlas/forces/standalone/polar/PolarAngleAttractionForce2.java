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
public class PolarAngleAttractionForce2 extends FARepulsion implements IReviewableForce{
    private static final long serialVersionUID = -3910344755243537646L;
    protected LinearFunction func;
    protected double expectedAngle = 0;
    
    public PolarAngleAttractionForce2(IBoundedItem owner, IBoundedItem source) {
        this(owner, source, 0);
    }
    public PolarAngleAttractionForce2(IBoundedItem owner, IBoundedItem source, double fixedAlpha) {
        super(owner, source);
        this.expectedAngle = fixedAlpha;
    }
    
    @Override
	public void setAll(int ownerDegree, ItemForceVector ownerForce, int sourceDegree, ItemForceVector sourceForce){
        super.setAll(ownerDegree, ownerForce, sourceDegree, sourceForce);
    }

    @Override
	public void apply(double repulsion, boolean noCollide){
        Coord2d p = owner.getPosition().sub(source.getPosition()).fullPolar();
        double ecart = angularDistance(p);
        double c = repulsion * (1 + ownerDegree) * (1 + sourceDegree) * factor;
        
        //double c = 1;
        double f = ForceVectorUtils.attraction(c, ecart);
        //System.out.println("ecart:"+ecart + " force:"+f);
        if(expectedAngle>p.x)
            p.x -= /*ecart */ f;
        else
            p.x += /*ecart */ f;
        Coord2d expect = source.getPosition().add(p.cartesian());
        
        ownerForce.dx += expect.x-owner.getPosition().x;
        ownerForce.dy += expect.y-owner.getPosition().y;
        
        logLastMove(expect.x-owner.getPosition().x, expect.y-owner.getPosition().y);
    }
    
    /**
     * Return distance in radius as p.x - expectedAngle.
     * 
     * If this is greater than PI, then performs: p.x - (expectedAngle+2PI)
     * 
     * negative value indicates to user that move vector should go the other way
     * 
     * @param p
     * @return
     */
    public double angularDistance(Coord2d p){
        double a = Math.abs(p.x - expectedAngle);
        if(a>Math.PI)
            return p.x - (expectedAngle+2*Math.PI);
        return a;
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

package org.datagr4m.drawing.layout.algorithms.forceAtlas;


import java.util.Collection;

import org.datagr4m.drawing.layout.IBoundedLayout;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FAAttraction;
import org.datagr4m.drawing.layout.algorithms.forces.IForce;
import org.datagr4m.drawing.layout.algorithms.forces.ItemForceVector;
import org.datagr4m.drawing.model.bounds.IBounds;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.jzy3d.maths.Coord2d;


/**
 * An implementation that maintains content into given bounds.
 * 
 * @author Martin Pernollet
 *
 */
public class BoundedForceAtlasLayout extends ForceAtlasLayout implements IBoundedLayout{
    private static final long serialVersionUID = 6269461478305063456L;

    protected IBounds bounds;
    
    public BoundedForceAtlasLayout(IHierarchicalGraphModel model) {
		super(model);
	}
    
	@Override
    public IBounds getBounds() {
		return bounds;
	}

	@Override
    public void setBounds(IBounds bounds) {
		this.bounds = bounds;
	}

	@Override
    public RectangleBounds getActualBounds(){
		RectangleBounds b = RectangleBounds.build(model.getChildren());
		return b;
	}
	
	/************/
	
    @Override
	protected void applyAttraction(Collection<IBoundedItem> items){
        for(IForce force: model.getAttractorForces()){
            ((FAAttraction)force).apply(getAttractionStrength(), isAdjustSizes(), isOutboundAttractionDistribution());
            /*if(force instanceof FAAttraction){
                ((FAAttraction)force).apply(getAttractionStrength(), isAdjustSizes(), isOutboundAttractionDistribution());
            }
            else{
                throw new RuntimeException("unexpected force object: " + force);
            }*/
        }
	}
    
	/** Applies bounds to the forces, so that node remains inside. */
	@Override
	protected void applyForces(Collection<IBoundedItem> items){
        for(IBoundedItem item: items) {
            ItemForceVector nLayout = itemForces.get(item);
            //System.out.println(item.getPosition() + " shift with " + nLayout.dx + " " + nLayout.dy);
            
            if (!item.locked()) {
                double d = 0.0001 + Math.sqrt(nLayout.dx * nLayout.dx + nLayout.dy * nLayout.dy);
                float ratio;
                if (isFreezeBalance()) {
                    nLayout.freeze = (float) (getFreezeInertia() * nLayout.freeze + (1 - getFreezeInertia()) * 0.1 * getFreezeStrength() * (Math.sqrt(Math.sqrt((nLayout.old_dx - nLayout.dx) * (nLayout.old_dx - nLayout.dx) + (nLayout.old_dy - nLayout.dy) * (nLayout.old_dy - nLayout.dy)))));
                    ratio = (float) Math.min((d / (d * (1f + nLayout.freeze))), getMaxDisplacement() / d);
                } else {
                    ratio = (float) Math.min(1, getMaxDisplacement() / d);
                }
                nLayout.dx *= ratio / getCooling();
                nLayout.dy *= ratio / getCooling();
                float x = item.getPosition().x + nLayout.dx;
                float y = item.getPosition().y + nLayout.dy;
                                
                //logForceMove(item, nLayout);

                applyStrictBounds(item, x, y);
            }
            else{
                //moveMemory.put(item, new Coord2d(0,0));
                //logForceMove(item, nLayout);
            }
        }
    }
	
	protected void applyStrictBounds(IBoundedItem item, float x, float y){
		if(bounds!=null){
			if(bounds.isIn(x, y)){
			    item.changePosition(x,y);
			}
			else{
				Coord2d p = bounds.correct(x, y);
                item.changePosition(p);
			}
		}
		else{
		    item.changePosition(x,y);
		}
	}
}

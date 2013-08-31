package org.datagr4m.drawing.layout.hierarchical.flower;

import java.util.List;

import org.datagr4m.drawing.layout.geometrical.flower.FlowerGeometry;
import org.datagr4m.drawing.layout.geometrical.flower.FlowerSolver;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.flower.AbstractFlowerModel;
import org.datagr4m.drawing.model.items.hierarchical.flower.IFlowerModel;
import org.jzy3d.maths.Coord2d;


public class StaticFlowerLayout extends ForceFlowerLayout{
    private static final long serialVersionUID = -633889608557248865L;

    public StaticFlowerLayout(IFlowerModel<?> model) {
        super(model);
    }

    public StaticFlowerLayout(IHierarchicalLayout parent, IFlowerModel<?> model) {
        super(parent, model);
    }

    @Override
	protected void setup(){
        super.setup();
        
        IBoundedItem center = fmodel.getCenter();
        Coord2d cp = center.getPosition();

        // --------------------------------
        // all device on external circle of FlowerGeometry
        List<IBoundedItem> externalCircleItems = fmodel.getExternalCircleItems();
        float m1 = 300;
        float r1 = center.getRadialBounds();
        float r2 = AbstractFlowerModel.getMaxRadius(externalCircleItems); // TODO: *2 est un hack, pb avec la formule?!
        //FlowerGeometry gfm = FlowerGeometry.fromR2(r2, hydreExtremities.size());
        
        FlowerGeometry gfm = FlowerSolver.getGeometry(externalCircleItems.size(), r1, r2, m1);
        
        List<Coord2d> coords = gfm.getIteration(cp);
        for (int i = 0; i < externalCircleItems.size(); i++) {
            IBoundedItem extremity = externalCircleItems.get(i);
            extremity.changePosition(coords.get(i));
            //System.out.println("fix:"+nonHydre + " to " + nonHydre.getPosition());
        }

        // --------------------------------
        // all networks on internal circle
        List<IBoundedItem> internalCircleItems = fmodel.getInternalCircleItems();
        
        for(IBoundedItem internalItem: internalCircleItems){
            List<IBoundedItem> extremities = fmodel.getExternalCircleItems(internalItem);
            
            double meanAngle = 0;
            double meanDist = 0;
            int k = 0;
            for(IBoundedItem extremity: extremities){
                if(extremity!=center){
                    Coord2d cart  = extremity.getPosition().sub(cp);
                    Coord2d polar = cart.fullPolar();
                    meanAngle += polar.x;
                    meanDist += polar.y;
                    //System.out.println("c:" + cart + "  |  p:" + polar);
                    k++;
                }
            }
            meanAngle /= k;
            meanDist /= k;
            Coord2d hP = new Coord2d(meanAngle,meanDist/2).cartesian().add(cp);
            internalItem.changePosition(hP);
        }
    }
    
    @Override
	public void goAlgo(){
        fmodel.getCenter().lock();
        for(IBoundedItem item: fmodel.getExternalCircleItems())
            item.lock();
        //super.goAlgo();
    }
}

package org.datagr4m.drawing.layout.algorithms.forceAtlas.forces;

import org.datagr4m.drawing.layout.algorithms.forces.ForceVectorUtils;
import org.datagr4m.drawing.model.items.IBoundedItem;


public class FAAttraction extends AbstractFAForce{
    private static final long serialVersionUID = 8903238202420121527L;

    public FAAttraction(IBoundedItem owner, IBoundedItem source) {
        super(owner, source);
    }
    
    public void apply(double attraction, boolean noCollide, boolean outboundAttraction){
        double bonus = (owner.locked() || source.locked()) ? (100) : (1);
        bonus *= weight;
        double c = bonus * attraction;
        
        if(outboundAttraction){
            if(ownerDegree<0)
                throw new RuntimeException("degree is " + ownerDegree + " for " + owner);
            c /= (1 + ownerDegree);
        }
        
        if(noCollide)
            ForceVectorUtils.fcBiAttractor_noCollide(owner, source, c, ownerForce, sourceForce);
        else
            ForceVectorUtils.fcBiAttractor(owner, source, c, ownerForce, sourceForce);
    }
}

package org.datagr4m.drawing.layout.algorithms.forceAtlas.forces;

import org.datagr4m.drawing.layout.algorithms.forces.ForceVectorUtils;
import org.datagr4m.drawing.model.items.IBoundedItem;


public class FARepulsion extends AbstractFAForce{
    private static final long serialVersionUID = 8903238202420121527L;

    public FARepulsion(IBoundedItem owner, IBoundedItem source) {
        this(owner, source,1);
    }

    public FARepulsion(IBoundedItem owner, IBoundedItem source, double factor) {
        super(owner, source);
        this.factor = factor;
    }
    
    public void apply(double repulsion, boolean noCollide){
        double c = repulsion * (1 + ownerDegree) * (1 + sourceDegree) * factor;
        if(noCollide)
            ForceVectorUtils.fcBiRepulsor_noCollide(owner, source, c, ownerForce, sourceForce);
        else
            ForceVectorUtils.fcBiRepulsor(owner, source, c, ownerForce, sourceForce);
    }
}

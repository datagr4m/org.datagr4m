package org.datagr4m.drawing.layout.algorithms.forceAtlas.forces;

import org.datagr4m.drawing.layout.algorithms.forces.Force;
import org.datagr4m.drawing.layout.algorithms.forces.ItemForceVector;
import org.datagr4m.drawing.model.items.IBoundedItem;


public class AbstractFAForce extends Force{
    private static final long serialVersionUID = -5917185878216431839L;

    public AbstractFAForce(IBoundedItem owner, IBoundedItem source) {
        super(owner, source);
    }
    
    public AbstractFAForce(IBoundedItem owner, IBoundedItem source, ItemForceVector ownerForce, ItemForceVector sourceForce, int ownerDegree, int sourceDegree) {
        super(owner, source);
        this.ownerForce = ownerForce;
        this.sourceForce = sourceForce;
        this.ownerDegree = ownerDegree;
        this.sourceDegree = sourceDegree;
    }
    
    public ItemForceVector getOwnerForce() {
        return ownerForce;
    }
    public void setOwnerForce(ItemForceVector ownerForce) {
        this.ownerForce = ownerForce;
    }
    public ItemForceVector getSourceForce() {
        return sourceForce;
    }
    public void setSourceForce(ItemForceVector sourceForce) {
        this.sourceForce = sourceForce;
    }
    public int getOwnerDegree() {
        return ownerDegree;
    }
    public void setOwnerDegree(int ownerDegree) {
        this.ownerDegree = ownerDegree;
    }
    public int getSourceDegree() {
        return sourceDegree;
    }
    public void setSourceDegree(int sourceDegree) {
        this.sourceDegree = sourceDegree;
    }
    
    public void setAll(int ownerDegree, ItemForceVector ownerForce, int sourceDegree, ItemForceVector sourceForce){
        if(sourceForce==null)
            throw new RuntimeException("trying to set a source force with a null value for " + source.getLabel());
        if(ownerForce==null)
            throw new RuntimeException("trying to set an owner force with a null value for " + owner.getLabel());

        this.sourceDegree = sourceDegree;
        this.sourceForce = sourceForce;
        this.ownerDegree = ownerDegree;
        this.ownerForce = ownerForce;
    }

    protected ItemForceVector ownerForce;
    protected ItemForceVector sourceForce;
    protected int ownerDegree;
    protected int sourceDegree;
}

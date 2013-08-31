package org.datagr4m.drawing.layout.algorithms.forces;

import org.datagr4m.drawing.model.items.IBoundedItem;

/**
 * If the force is working on item A, by considering influence of item B,
 * then A is considered as target (owner), and B as source.
 */
public class Force implements IForce{
    private static final long serialVersionUID = -1187187920206248464L;

    public Force(IBoundedItem owner, IBoundedItem source){
        this.owner = owner;
        this.source = source;
        
        if(owner==null)
            throw new RuntimeException("trying to initialize a force with a null owner");
        if(source==null)
            throw new RuntimeException("trying to initialize a force with a null source");
    }
    
    @Override
    public IBoundedItem getSource() {
        return source;
    }
    
    @Override
    public IBoundedItem getOwner() {
        return owner;
    }

    @Override
    public float getWeight() {
        return weight;
    }
    
    @Override
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Force other = (Force) obj;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }



    protected IBoundedItem owner; // the "target"
    protected IBoundedItem source;
    protected float weight = 1;
    protected double factor = 1;
}

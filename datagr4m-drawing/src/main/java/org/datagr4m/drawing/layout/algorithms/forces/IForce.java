package org.datagr4m.drawing.layout.algorithms.forces;

import java.io.Serializable;

import org.datagr4m.drawing.model.items.IBoundedItem;



public interface IForce extends Serializable{
    public IBoundedItem getOwner();
    public IBoundedItem getSource();
    public float getWeight();
    public void setWeight(float weight);
}

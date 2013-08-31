package org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.AbstractFAForce;
import org.datagr4m.drawing.model.items.IBoundedItem;


public abstract class StandaloneForce extends AbstractFAForce{
	private static final long serialVersionUID = -8837714540459309653L;

	public StandaloneForce(IBoundedItem owner, IBoundedItem source) {
        super(owner, source);
    }

    public abstract void apply();
}

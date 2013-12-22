package org.datagr4m.drawing.model.links;

import org.datagr4m.datastructures.pairs.UniquePair;
import org.datagr4m.drawing.model.slots.ISlotableItem;


/**
 * A edge model that inherit {@link UniquePair}, which means that 
 * two links are not considered equals even if their source and target
 * are identical.
 * In other words, each {@link UniquePair} instance is only equals()
 * to itself.
 * Thus, its directed or undirected status is undefined.
 * 
 * @see {@link org.datagr4m.datastructures.pairs.UniquePair}
 */
public class UniqueLink extends UniquePair<ISlotableItem> implements ILink<ISlotableItem>{
    private static final long serialVersionUID = -2975342927272554215L;

    public UniqueLink(ISlotableItem source, ISlotableItem destination) {
        super(source, destination);
    }
    
    
    protected Object modelEdge;

    @Override
	public Object getModelEdge() {
        return modelEdge;
    }

    @Override
	public void setModelEdge(Object modelEdge) {
        this.modelEdge = modelEdge;
    }
}

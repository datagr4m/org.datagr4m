package org.datagr4m.drawing.model.links;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.model.slots.ISlotableItem;


public class UndirectedLink extends CommutativePair<ISlotableItem> implements ILink<ISlotableItem>{
    private static final long serialVersionUID = -7062302682936057538L;

    public UndirectedLink(ISlotableItem a, ISlotableItem b) {
        super(a, b);
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

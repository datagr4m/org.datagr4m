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
    
    protected Object sourceInterface;
    protected Object targetInterface;
    
    @Override
    public Object getSourceInterface() {
        return sourceInterface;
    }

    public void setSourceInterface(Object sourceInterface) {
        this.sourceInterface = sourceInterface;
    }

    @Override
    public Object getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Object targetInterface) {
        this.targetInterface = targetInterface;
    }
}

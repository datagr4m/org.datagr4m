package org.datagr4m.drawing.model.links;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.datastructures.pairs.TypedPair;
import org.datagr4m.drawing.model.slots.ISlotableItem;


/**
 * A directed edge model that consider device+interface as source and destination identity
 */
public class DirectedLinkWithInterface extends TypedPair<Pair<ISlotableItem,Object>> implements ILink<Pair<ISlotableItem,Object>>{
    private static final long serialVersionUID = -6586367437405136994L;

    public DirectedLinkWithInterface(Pair<ISlotableItem,Object> a, Pair<ISlotableItem,Object> b) {
        super(a, b);
    }
    
    public DirectedLinkWithInterface(ISlotableItem a, Object ainf, ISlotableItem b, Object binf) {
        this(new Pair<ISlotableItem,Object>(a,ainf), new Pair<ISlotableItem,Object>(b,binf));
    }
    
    public DirectedLinkWithInterface revert(){
        return new DirectedLinkWithInterface(b,a);
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

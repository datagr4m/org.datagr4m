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
        sourceInterface = a.b;
        targetInterface = b.b;
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        result = prime * result + ((sourceInterface == null) ? 0 : sourceInterface.hashCode());
        result = prime * result + ((targetInterface == null) ? 0 : targetInterface.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DirectedLinkWithInterface other = (DirectedLinkWithInterface) obj;
        if (a == null) {
            if (other.a != null)
                return false;
        } else if (!a.equals(other.a))
            return false;
        if (b == null) {
            if (other.b != null)
                return false;
        } else if (!b.equals(other.b))
            return false;

        
        if (sourceInterface == null) {
            if (other.sourceInterface != null)
                return false;
        } else if (!sourceInterface.equals(other.sourceInterface))
            return false;
        if (targetInterface == null) {
            if (other.targetInterface != null)
                return false;
        } else if (!targetInterface.equals(other.targetInterface))
            return false;
        return true;
    }
}

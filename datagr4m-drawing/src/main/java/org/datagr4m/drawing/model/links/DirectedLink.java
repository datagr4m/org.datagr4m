package org.datagr4m.drawing.model.links;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.datastructures.pairs.TypedPair;
import org.datagr4m.drawing.model.slots.ISlotableItem;


/**
 * A directed edge model that inherit {@link TypedPair}, which means that 
 * its identity relies on its source and target. In other word,
 * two links are considered equals if they both have the same
 * source and the same target.
 * 
 * @see {@link UniqueLink}
 * @see {@link com.netlight.statistics.graph.NetworkEdge} 
 */
public class DirectedLink extends TypedPair<ISlotableItem> implements ILink<ISlotableItem>{
    public DirectedLink(ISlotableItem a, ISlotableItem b) {
        super(a, b);
    }
    
    public DirectedLink revert(){
        return new DirectedLink(b,a);
    }
    
    public static List<DirectedLink> revert(List<DirectedLink> links){
        List<DirectedLink> inverted = new ArrayList<DirectedLink>();
        for(DirectedLink link: links)
            inverted.add(link.revert());
        return inverted;
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
        DirectedLink other = (DirectedLink) obj;
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
    
    

    private static final long serialVersionUID = 4824062850825107364L;
}

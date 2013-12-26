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

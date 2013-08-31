package org.datagr4m.drawing.model.slots;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.layout.slots.ISlotLayout;
import org.datagr4m.drawing.model.links.DirectedLink;
import org.datagr4m.drawing.model.links.ILink;


/**
 * A SlotTarget describes how a target item (T) can be reached through 
 * a given output slot of an owner item (O).  
 * 
 * The owner can use the SlotTarget object to know
 * <ul>
 * <li>the target item (e.g. a computer network interface)
 * <li>its interface used to reach this item (e.g. a computer network interface)
 * <li>the edge, usually an {@link ILink} object (e.g. an IP network for computers)
 * </ul>
 * 
 * Two slot targets are considers equal if:
 * <ul>
 * <li>Both targets are equals according to their {@link equals} method.
 * <li>Both interfaces are equals according to their {@link equals} method.
 * <li>Both links are equals according to their {@link equals} method.
 * </ul>
 * 
 * The definition of equal(Object) is of a crucial importance to allow an {@link ISlotLayout}
 * to deal with various types of links. Indeed, the slot selection rely
 * on {@link Collection}s that make extensive use of equals method to retrieve content.
 * 
 * Thus, we can use various link implementations to deal with multiple links for
 * common source and target objects:
 * <ul>
 * <li>{@link org.datagr4m.drawing.model.links.DirectedLink}
 * <li>{@link org.datagr4m.drawing.model.links.UniqueLink}
 * <li>{@link com.netlight.statistics.graph.NetworkEdge}
 * </ul>
 * 
 * @author Martin Pernollet
 */
public class SlotTarget implements Serializable{
    private static final long serialVersionUID = 1393550386589082056L;
    
    public enum Direction{
        NONE,
        INCOMING,
        OUTGOING
    }
    
    public SlotTarget(ISlotableItem target, Object link) {
        this.target = target;
        this.link = link;
        updateDirection();
    }
    
    public SlotTarget(ISlotableItem target, Object intrface, Object link) {
        this.target = target;
        this.link = link;
        this.intrface = intrface;
        updateDirection();
    }
    
    public SlotTarget(ISlotableItem target, Object intrface, Object link, Direction d) {
        this.target = target;
        this.link = link;
        this.intrface = intrface;
        this.direction = d;
    }

    protected void updateDirection() {
        if(link instanceof DirectedLink){
            direction = Direction.OUTGOING;
        }
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public ISlotableItem getTarget() {
        return target;
    }
    
    public Object getLink() {
        return link;
    }
    
    public Object getInterface() {
        return intrface;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((intrface == null) ? 0 : intrface.hashCode());
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
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
        SlotTarget other = (SlotTarget) obj;
        if (intrface == null) {
            if (other.intrface != null)
                return false;
        } else if (!intrface.equals(other.intrface))
            return false;
        if (link == null) {
            if (other.link != null)
                return false;
        } else if (!link.equals(other.link))
            return false;
        if (target == null) {
            if (other.target != null)
                return false;
        } else if (!target.equals(other.target))
            return false;
        return true;
    }

    /**************/
    
    public static List<ISlotableItem> getObstacles(List<SlotTarget> targets){
        List<ISlotableItem> obstacles = new ArrayList<ISlotableItem>(targets.size());
        for(SlotTarget t: targets)
            obstacles.add(t.getTarget());
        return obstacles;
    }
    
    /**************/
    
    @Override
	public String toString(){
        return "(" + this.getClass().getSimpleName() + ") int:" + intrface + " target:"+target+" link:"+link;
    }

    protected Object intrface;
    protected ISlotableItem target;
    protected Object link;
    protected Direction direction = Direction.NONE;
}

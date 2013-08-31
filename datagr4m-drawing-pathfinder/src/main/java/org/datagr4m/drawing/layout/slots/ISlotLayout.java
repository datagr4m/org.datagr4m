package org.datagr4m.drawing.layout.slots;

import java.io.Serializable;
import java.util.List;

import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.ISlotableSetModel;


/** 
 * Supposed to initialize slot groups according to the links and to
 * the slotable items position in the map. In other word, determine if
 * a given edge should connect in the north, south, east or west of the item,
 * and to which slot the link is attached in the slot group.
 */
public interface ISlotLayout extends Serializable{
    /** 
     * Edit the items of the {@link ISlotableSetModel} so that their slot groups
     * are configured for the provided link list
     */
    public void initialize(ISlotableSetModel group, List<ILink<ISlotableItem>> links);

    /** Create an initialized slot group out of a list of links. */
    public ISlotableSetModel initialize(List<ILink<ISlotableItem>> links);
}
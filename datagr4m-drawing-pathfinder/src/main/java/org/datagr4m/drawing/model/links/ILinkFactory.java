package org.datagr4m.drawing.model.links;

import java.io.Serializable;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.slots.ISlotableItem;


/**
 * Able to inject link object generation in a component.
 *
 * @author Martin Pernollet
 *
 */
public interface ILinkFactory extends Serializable{
    public ILink<ISlotableItem> getLink(ISlotableItem source, ISlotableItem target);
    public ILink<Pair<ISlotableItem,Object>> getLink(ISlotableItem source, Object srcInterface, ISlotableItem target, Object trgInterface);
}

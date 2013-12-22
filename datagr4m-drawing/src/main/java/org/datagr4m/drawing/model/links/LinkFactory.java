package org.datagr4m.drawing.model.links;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.slots.ISlotableItem;


public class LinkFactory implements ILinkFactory{
    private static final long serialVersionUID = 1876767359480716152L;

    @Override
    public ILink<ISlotableItem> getLink(ISlotableItem source, ISlotableItem target) {
        return new DirectedLink(source, target);
    }

    @Override
    public ILink<Pair<ISlotableItem,Object>> getLink(ISlotableItem source, Object srcInterface, ISlotableItem target, Object trgInterface) {
        return new DirectedLinkWithInterface(source, srcInterface, target, trgInterface);
    }
}

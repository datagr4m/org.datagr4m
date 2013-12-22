package org.datagr4m.drawing.model.links;

import java.util.HashMap;
import java.util.Map;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.slots.ISlotableItem;


public class LinkPoolFactory implements ILinkFactory{
    private static final long serialVersionUID = 1876767359480716152L;

    protected Map<ILink<ISlotableItem>,ILink<ISlotableItem>> map = new HashMap<ILink<ISlotableItem>,ILink<ISlotableItem>>();
    protected Map<ILink<Pair<ISlotableItem,Object>>,ILink<Pair<ISlotableItem,Object>>> map2 = new HashMap<ILink<Pair<ISlotableItem,Object>>,ILink<Pair<ISlotableItem,Object>>>();
    //protected Set<ILink<ISlotableItem>> set = new HashSet<ILink<ISlotableItem>>();
    
    @Override
    public ILink<ISlotableItem> getLink(ISlotableItem source, ISlotableItem target) {
        final DirectedLink link = new DirectedLink(source, target);
        if(!map.keySet().contains(link)){
            map.put(link, link);
            return link;
        }
        else{
            return map.get(link);
        }
    }

    @Override
    public ILink<Pair<ISlotableItem,Object>> getLink(ISlotableItem source, Object srcInterface, ISlotableItem target, Object trgInterface) {
        
        final DirectedLinkWithInterface link = new DirectedLinkWithInterface(source, srcInterface, target, trgInterface);
        if(!map2.keySet().contains(link)){
            map2.put(link, link);
            return link;
        }
        else{
            return map2.get(link);
        }
    }
}

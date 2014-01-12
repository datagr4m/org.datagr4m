package org.datagr4m.drawing.renderer.items.hierarchical.hit;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.edges.ClickedSlot;
import org.datagr4m.maths.geometry.RectangleUtils;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.hit.DefaultHitProcessor;


public class HierarchicalHitPolicy extends DefaultHitProcessor{
    protected IHierarchicalRenderer renderer;
    
    public HierarchicalHitPolicy(IHierarchicalRenderer renderer){
        this.renderer = renderer;
    }
    
    @Override
    public List<IClickableItem> hit(int x, int y) {
        // processing clicks on all children model through SUB-RENDERERS
        List<IClickableItem> hitStack = new ArrayList<IClickableItem>();
        
        hitHierarchy(x, y, hitStack);
        hitPostRenderers(x, y, hitStack);
        hitSlots(x, y, hitStack);
        
        return hitStack;
    }
    
    @Override
    public <T> List<IClickableItem> hitOnly(int x, int y, Class<T> type){
        List<IClickableItem> hitStack = new ArrayList<IClickableItem>();
        if(type.isInstance(renderer)){
            hitHierarchy(x, y, hitStack);
            hitSlots(x, y, hitStack);
        }
        for(IRenderer r: renderer.getPostRenderers())
            if(type.isInstance(r)){
                List<IClickableItem> ci = r.hit(x, y);
                if(ci!=null)
                    hitStack.addAll(ci);
            }
        return hitStack;
    }
    
    @Override
    public <T> List<IClickableItem> hitExcluding(int x, int y, Class<T> type){
        List<IClickableItem> hitStack = new ArrayList<IClickableItem>();
        if(!type.isInstance(renderer)){
            hitHierarchy(x, y, hitStack);
            hitSlots(x, y, hitStack);
        }
        for(IRenderer r: renderer.getPostRenderers())
            if(!type.isInstance(r)){
                List<IClickableItem> ci = r.hit(x, y);
                if(ci!=null)
                    hitStack.addAll(ci);
            }
        return hitStack;
    }
    
    /***********/
    
    public void hitHierarchy(int x, int y, List<IClickableItem> hitStack){
        // processing bottom of hierarchy first, if groups are not collapsed
        for(IHierarchicalRenderer subrenderer: renderer.getChildren()){
            if(!subrenderer.getModel().isCollapsed())
                hitStack.addAll(subrenderer.hit(x,y));
        }
        
        // processing clicks on all children ITEMS
        Collection<IBoundedItem> items = renderer.getModel().getChildren(); 
        for(IBoundedItem item: items){
            // groupe: traite le cas du collaps�
            if(item instanceof IHierarchicalNodeModel){
                IHierarchicalNodeModel m = (IHierarchicalNodeModel)item;
                if(m.isCollapsed())
                    hitItem(m.getCollapsedModel(), x, y, hitStack);
                hitItem(m, x, y, hitStack);
            }
            // item normal
            else{
                hitItem(item, x, y, hitStack);
            }
        }
    }
    
    protected void hitItem(IBoundedItem item, int x, int y, List<IClickableItem> hitStack){
        boolean hit = renderer.getItemRenderer().hit(item, x, y);
        if(hit)
            hitStack.add(item);
    }
    
    public void hitSlots(int x, int y, List<IClickableItem> hitStack){
        //if(!isHitSlot())
        //    return;
     // processing clicks on all children ITEM SLOTS
        // TODO: remove this debugging code
        Collection<IBoundedItem> items = renderer.getModel().getChildren(); 

        for(ISlotableItem o: items){
            if(o instanceof IHierarchicalNodeModel)
                continue;
            
            synchronized(o.getSlotGroups()){
                for(SlotGroup slots: o.getSlotGroups()){
                    
                    // TODO ajouter test pour verifier si la souris croise le slot group
                    // pour �viter de tester toutes les intersections possibles
                    int ns = slots.getSlotNumber();
                    
                    for (int i = 0; i < ns; i++) {
                        Point2D anchor = slots.getSlotAnchorPoint(i);
                        float width = slots.getSlotAnchorWidth();
                        
                        boolean cross = RectangleUtils.contains(anchor, width, width, x, y, false);
                        
                        if(cross){
                            //System.out.println("click " + o + " group " + slots.getSide() + " slot " + i);
                            hitStack.add(new ClickedSlot(i, slots, o));
                        }
                    }
                }
            }
        }
    }
    
    public void hitPostRenderers(int x, int y, List<IClickableItem> hitStack){
        // processing clicks on all POST-RENDERERS
        for(IRenderer r: renderer.getPostRenderers()){
            List<IClickableItem> ci = r.hit(x, y);
            if(ci!=null)
                hitStack.addAll(ci);
        }
    }
    
    
    
    
    public boolean isHitSlot() {
        return hitSlot;
    }

    public void setHitSlot(boolean hitSlot) {
        this.hitSlot = hitSlot;
    }

    boolean hitSlot = true;
}

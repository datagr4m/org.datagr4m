package org.datagr4m.drawing.layout.algorithms.forces;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.renderer.bounds.BoundsRendererSettings;
import org.datagr4m.drawing.renderer.bounds.IBoundsRendererSettings;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.forceHighlight.NodeForceRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.forceHighlight.Selection;
import org.datagr4m.drawing.renderer.items.hierarchical.pair.IHierarchicalPairRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseHitModelAdapter;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;

/**
 * Enable debugging forces in a graph by showing attracting/repeling nodes of a selected node using colors.
 * 
 * @see {@link NodeForceRendererSettings}
 * @author martin
 */
public class ForceDebugger {
    public static void attach(MouseItemViewController mouse, IHierarchicalNodeModel model, IHierarchicalRenderer renderer){
        // Add tools to show force on selection
        final ForceIndex forceIndex = new ForceIndex();
        forceIndex.register(model);
        
        final Selection selection = new Selection();
        mouse.addMouseHitListener(new MouseHitModelAdapter(){
            @Override
            public void itemHit(IBoundedItem object) {
                selection.select(object);
                
                Collection<IBoundedItem> repulsors = forceIndex.getRepulsors(object);
                Collection<IBoundedItem> attractors = forceIndex.getAttractors(object);
                
                //log(repulsors, attractors);
            }
            @Override
            public void itemReleased(IBoundedItem object) {
                selection.clear();
            }
        });
        
        IItemRendererSettings nrs = new NodeForceRendererSettings(selection, forceIndex);
        IBoundsRendererSettings brs = new BoundsRendererSettings(false);

        setNodeRenderer(renderer, nrs);
        setBoundedRenderer(renderer, brs);
    }
    
    public static void setNodeRenderer(IHierarchicalRenderer renderer, IItemRendererSettings nrs){
        if(renderer instanceof HierarchicalGraphRenderer)
            ((HierarchicalGraphRenderer)renderer).getRendererSettings().setNodeSettings(nrs);
        
        for(IHierarchicalRenderer subr: renderer.getChildren())
            setNodeRenderer(subr, nrs);
    }
    
    public static void setBoundedRenderer(IHierarchicalRenderer renderer, IBoundsRendererSettings nrs){
        if(renderer instanceof IHierarchicalPairRenderer)
            ((IHierarchicalPairRenderer)renderer).getRendererSettings().setBoundsSettings(nrs);
        
        for(IHierarchicalRenderer subr: renderer.getChildren())
            setBoundedRenderer(subr, nrs);
    }
    
    public static void log(Collection<IBoundedItem> repulsors, Collection<IBoundedItem> attractors) {
        Logger.getLogger(ForceDebugger.class).info("--------------------------");
        Logger.getLogger(ForceDebugger.class).info(repulsors.size() + " repulsors: ");
        int k=0;
        for(IBoundedItem r: repulsors){
            Logger.getLogger(ForceDebugger.class).info(r.getObject());
            k++;
            if(k>20){
                Logger.getLogger(ForceDebugger.class).info("...");
                break;
            }
                
        }
        Logger.getLogger(ForceDebugger.class).info(attractors.size() + " attractors: ");
        for(IBoundedItem a: attractors)
            Logger.getLogger(ForceDebugger.class).info(a.getObject());
    }

}

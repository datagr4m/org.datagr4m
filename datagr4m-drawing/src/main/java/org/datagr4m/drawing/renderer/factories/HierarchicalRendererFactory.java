package org.datagr4m.drawing.renderer.factories;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.IHierarchicalPairModel;
import org.datagr4m.drawing.renderer.items.hierarchical.AbstractHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.pair.HierarchicalPairRenderer;
import org.datagr4m.viewer.IDisplay;


public class HierarchicalRendererFactory implements IHierarchicalRendererFactory{
    @Override
    public IHierarchicalRenderer getRenderer(IDisplay display, IHierarchicalModel model) {
        if(model instanceof IHierarchicalGraphModel){
            HierarchicalGraphRenderer renderer=new HierarchicalGraphRenderer(display, (IHierarchicalGraphModel)model);
            attachChildren(renderer, model);
            return renderer;
        }
        else if(model instanceof IHierarchicalPairModel){
            HierarchicalPairRenderer renderer = new HierarchicalPairRenderer(display, (IHierarchicalPairModel)model);
            attachChildren(renderer, model);
            return renderer;
        }
        return null;
    }
    
    /** recursively handle children renderers.*/
    protected void attachChildren(AbstractHierarchicalRenderer renderer, IHierarchicalModel model){
        for(IBoundedItem child: model.getChildren()){
            if(child instanceof IHierarchicalModel){
                IHierarchicalModel submodel = (IHierarchicalModel)child;
                IHierarchicalRenderer subrenderer = getRenderer(renderer.getDisplay(), submodel);
                if(subrenderer!=null)
                    renderer.addChild(subrenderer);
            }
        }
    }
}

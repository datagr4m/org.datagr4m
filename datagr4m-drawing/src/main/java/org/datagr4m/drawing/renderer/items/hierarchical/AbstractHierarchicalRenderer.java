package org.datagr4m.drawing.renderer.items.hierarchical;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.model.bounds.IBounds;
import org.datagr4m.drawing.renderer.bounds.IBoundsRenderer;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.hit.HierarchicalHitPolicy;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.DifferedRenderer;
import org.datagr4m.viewer.renderer.IRenderer;


public abstract class AbstractHierarchicalRenderer extends AbstractRenderer implements IHierarchicalRenderer{
    public AbstractHierarchicalRenderer(IDisplay display){
        this.parent = null;
        this.display = display;
        this.children = new ArrayList<IHierarchicalRenderer>();
        this.postRenderers = new ArrayList<IRenderer>();
        this.hitProcessor = new HierarchicalHitPolicy(this);
    }
    
    /********************/
    
    @Override
    public void render(Graphics2D graphic) {
        for(IHierarchicalRenderer child: getChildren())
            child.render(graphic);
        for(IRenderer renderer: postRenderers)
            renderer.render(graphic);
    }
    
    @Override
    public void clearDiffered() {
        differed.clear();
    }

    @Override
    public List<DifferedRenderer> getDiffered() {
        return differed;
    }
    
    @Override
    public void addDiffered(List<DifferedRenderer> differed){
        this.differed.addAll(differed);
    }
    
    @Override
    public void addDiffered(DifferedRenderer differed){
        this.differed.add(differed);
    }
    
    @Override
    public IBounds getBounds() {
        throw new RuntimeException("not implemented");
    }
    
    /*public RectangleBounds getBoundsForRendering(IHierarchicalModel model){
        //RectangleBounds bounds = model.getRectangleBounds().shiftCenterTo(model.getAbsolutePosition());
        //RectangleBounds bounds = AbstractHierarchicalModel.getAbsoluteBounds(model);
        //RectangleBounds bounds = model.getRelativeRectangleBounds();
        return model.getAbsoluteRectangleBounds();
    }*/
    
    /**************************/
    
    public void addChild(IHierarchicalRenderer renderer){
        children.add(renderer);
    }
    
    @Override
    public Collection<IHierarchicalRenderer> getChildren() {
        return children;
    }

    @Override
    public IHierarchicalRenderer getParent() {
        return parent;
    }
    
    @Override
    public IDisplay getDisplay(){
        return display;
    }
    
    @Override
    public void addPostRenderer(IRenderer renderer){
        postRenderers.add(renderer);
    }
    
    @Override
    public List<IRenderer> getPostRenderers(){
        return postRenderers;
    }
    
    @Override
    public IItemRenderer getItemRenderer() {
        return itemRenderer;
    }
    
    @Override
    public void setItemRenderer(IItemRenderer renderer) {
        this.itemRenderer = renderer;
    }
    
    /***********/
    
    protected IDisplay display;
    
    protected IHierarchicalRenderer parent;
    protected Collection<IHierarchicalRenderer> children;
    
    protected IBoundsRenderer boundsRenderer;
    protected IItemRenderer itemRenderer;
    //protected IItemRenderer nodeIconRenderer;
    
    protected List<IRenderer> postRenderers;
    
    protected List<DifferedRenderer> differed = new ArrayList<DifferedRenderer>();

}

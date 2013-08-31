package org.datagr4m.drawing.layout.hierarchical.graph;

import org.datagr4m.drawing.layout.IRunnableLayout;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.AbstractHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;


public class HierarchicalGraphLayout extends AbstractHierarchicalLayout implements IHierarchicalLayout, IRunnableLayout{
    private static final long serialVersionUID = -90450336667215378L;

    public HierarchicalGraphLayout(IHierarchicalGraphModel model){
        this(null, model);
    }
    
    public HierarchicalGraphLayout(IHierarchicalLayout parent, IHierarchicalGraphModel model) {
        super(parent);
        setModel(model);
    }

    @Override
    public void setModel(IHierarchicalModel model) {
        this.model = (HierarchicalGraphModel) model;        
        this.delegate = new BoundedForceAtlasLayout(this.model);
        this.delegate.resetPropertiesValues();
        this.delegate.setAdjustSizes(true);
        //this.delegate.setMaintainToCenter(false);
        //this.delegate.setBounds(new CircleBounds(300/(1+model.getRoot().getDepth(model))));
        //this.delegate.initAlgo();
    }

    @Override
    public HierarchicalGraphModel getModel() {
        return model;
    }
    
    @Override
	public BoundedForceAtlasLayout getDelegate() {
        return delegate;
    }
    
    /*******************/

    @Override
    public void initAlgo() {
        super.initAlgo();
        delegate.initAlgo();
        model.refreshBounds(true);
    }

    @Override
    public void goAlgo() {
        goAlgo(true);
    }

    /**
     * Return true if calling goAlgo once again is relevant, false otherwise.
     * @param handleChildren
     * @return
     */
    public boolean goAlgo(boolean handleChildren) {
        if(handleChildren)
            super.goAlgo();
        
        if(model.getChildren().size()==1){
            IBoundedItem item = model.getChildren().get(0);
            boolean changed = true;
            if(item.getPosition().x!=0 || item.getPosition().y!=0){
                item.changePosition(0, 0);
            }
            else{
                changed = false;
            }
            delegate.incCounter();
            onDoneAlgo();
            return changed;
        }
        else{
            delegate.goAlgo();
            onDoneAlgo();
            return true;
        }
    }
    
    @Override
    public void endAlgo() {
        super.endAlgo();
        delegate.endAlgo();
    }

    @Override
    public boolean canAlgo() {
        if(delegate.canAlgo()){ // parent has more chance to finish later than children
            return true;
        }
        else{
            return super.canAlgo();
        }
    }
    
    /*@Override
    public void onDoneAlgo(){
        
    }*/
    
    /*****************/
    
    protected HierarchicalGraphModel model;
    protected BoundedForceAtlasLayout delegate;
}

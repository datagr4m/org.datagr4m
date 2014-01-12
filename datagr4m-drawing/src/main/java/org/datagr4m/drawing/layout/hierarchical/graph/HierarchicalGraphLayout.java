package org.datagr4m.drawing.layout.hierarchical.graph;

import org.datagr4m.drawing.layout.IRunnableLayout;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.AbstractHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.monitors.ITimeMonitor;
import org.datagr4m.monitors.TimeMonitor;


public class HierarchicalGraphLayout extends AbstractHierarchicalLayout implements IHierarchicalNodeLayout, IRunnableLayout{
    private static final long serialVersionUID = -90450336667215378L;

    protected HierarchicalGraphModel model;
    protected BoundedForceAtlasLayout delegate;
    
    private ITimeMonitor timeMonitor;
    

    public HierarchicalGraphLayout(){
        this(null, null);
    }
    
    public HierarchicalGraphLayout(IHierarchicalGraphModel model){
        this(null, model);
    }
    
    public HierarchicalGraphLayout(IHierarchicalNodeLayout parent, IHierarchicalGraphModel model) {
        super(parent);
        initMonitor();
        setModel(model);
    }

    private void initMonitor() {
        timeMonitor = new TimeMonitor(this);
    }

    @Override
    public ITimeMonitor getTimeMonitor() {
        return timeMonitor;
    }
    
    @Override
    public void setModel(IHierarchicalNodeModel model) {
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
        timeMonitor.startMonitor();

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
            
            timeMonitor.stopMonitor();
            return changed;
        }
        else{
            delegate.goAlgo();
            onDoneAlgo();

            timeMonitor.stopMonitor();
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
}

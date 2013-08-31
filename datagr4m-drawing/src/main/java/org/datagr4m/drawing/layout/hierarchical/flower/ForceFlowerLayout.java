package org.datagr4m.drawing.layout.hierarchical.flower;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.model.items.hierarchical.flower.IFlowerModel;


public class ForceFlowerLayout extends HierarchicalGraphLayout{
    private static final long serialVersionUID = 5361324822065180275L;

    public ForceFlowerLayout(IFlowerModel<?> model) {
        super(model);
        this.fmodel = model;
        setup();
    }

    public ForceFlowerLayout(IHierarchicalLayout parent, IFlowerModel<?> model) {
        super(parent, model);
        this.fmodel = model;
        setup();
    }
    
    protected void setup(){
        getDelegate().setRepulsionStrength(100d);
        getDelegate().setAttractionStrength(1d);
        getDelegate().setAdjustSizes(true);
        getDelegate().setGravity(0d);
        getDelegate().setMaintainToCenter(false);
    }
    
    @Override
	public void goAlgo(){
        fmodel.getCenter().lock();
        super.goAlgo();
    }

    protected IFlowerModel<?> fmodel;
}

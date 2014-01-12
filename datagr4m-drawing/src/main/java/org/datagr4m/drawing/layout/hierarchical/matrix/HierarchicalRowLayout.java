package org.datagr4m.drawing.layout.hierarchical.matrix;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.monitors.ITimeMonitor;
import org.datagr4m.monitors.TimeMonitor;

/**
 * Unlike its parent class, the row layout is autonomous, since items are
 * arranged in the order they are found in the parent model.
 */
public class HierarchicalRowLayout extends HierarchicalMatrixLayout{
    private static final long serialVersionUID = -5755122765137168343L;

    private ITimeMonitor timeMonitor;

    
    public HierarchicalRowLayout(){
        super();
        initMonitor();
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
        super.setModel(model);
        int n = model.getChildren().size();
        setSize(1, n);
    }
    
    @Override
    protected void computeLayout(){
        timeMonitor.startMonitor();
        
        if(mapIndex.size()==0)
            autoLineGrid();
        autoColSize();
        super.computeLayout();
        
        timeMonitor.stopMonitor();
    }
}

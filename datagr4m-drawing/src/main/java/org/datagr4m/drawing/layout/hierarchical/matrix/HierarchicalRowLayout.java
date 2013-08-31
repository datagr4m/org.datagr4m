package org.datagr4m.drawing.layout.hierarchical.matrix;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;

/**
 * Unlike its parent class, the row layout is autonomous, since items are
 * arranged in the order they are found in the parent model.
 */
public class HierarchicalRowLayout extends HierarchicalMatrixLayout{
    @Override
    public void setModel(IHierarchicalModel model) {
        super.setModel(model);
        int n = model.getChildren().size();
        setSize(1, n);
    }
    
    @Override
    protected void computeLayout(){
        if(mapIndex.size()==0)
            autoLineGrid();
        autoColSize();
        super.computeLayout();
    }
      
    private static final long serialVersionUID = -5755122765137168343L;
}

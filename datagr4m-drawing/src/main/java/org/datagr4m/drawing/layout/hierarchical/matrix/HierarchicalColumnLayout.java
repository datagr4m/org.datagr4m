package org.datagr4m.drawing.layout.hierarchical.matrix;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;


/**
 * Unlike its parent class, the column layout is autonomous, since items are
 * arranged in the order they are found in the parent model.
 */
public class HierarchicalColumnLayout extends HierarchicalMatrixLayout{
    @Override
    public void setModel(IHierarchicalModel model) {
        super.setModel(model);
        int n = model.getChildren().size();
        setSize(n, 1);
    }
    
    @Override
    protected void computeLayout(){
        if(mapIndex.size()==0)
            autoColumnGrid();
        autoRowSize();
        super.computeLayout();
    }
      
    private static final long serialVersionUID = -5755122765137168343L;
}

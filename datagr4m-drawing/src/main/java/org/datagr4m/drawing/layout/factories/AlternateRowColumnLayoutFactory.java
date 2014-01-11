package org.datagr4m.drawing.layout.factories;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;

public class AlternateRowColumnLayoutFactory extends HierarchicalLayoutFactory{
    @Override 
    public IHierarchicalLayout getHierarchicalNodeLayout(IHierarchicalModel model){
        IHierarchicalLayout layout = getHierarchicalNodeLayout(model, HierarchicalLayoutFactory.LAYOUT_COLUMN_NAME);
        return layout;
    }
    
    public IHierarchicalLayout getHierarchicalNodeLayout(IHierarchicalModel model, String layout){
        IHierarchicalLayout layoutModel = getNodeLayoutByName(layout);
        layoutModel.setModel(model);
        attachChildren(layoutModel, model, layout);
        return layoutModel;
    }
    
    protected void attachChildren(IHierarchicalLayout layoutModel, IHierarchicalModel model, String layout){
        for(IBoundedItem child: model.getChildren()){
            if(child instanceof IHierarchicalModel){
                IHierarchicalModel submodel = (IHierarchicalModel)child;
                IHierarchicalLayout subLayoutModel = null;
                if(HierarchicalLayoutFactory.LAYOUT_COLUMN_NAME.equals(layout))
                    subLayoutModel = getHierarchicalNodeLayout(submodel, HierarchicalLayoutFactory.LAYOUT_ROW_NAME);
                else
                    subLayoutModel = getHierarchicalNodeLayout(submodel, HierarchicalLayoutFactory.LAYOUT_COLUMN_NAME);
                if(subLayoutModel!=null)
                    layoutModel.addChild(subLayoutModel);
            }
        }
    }
}

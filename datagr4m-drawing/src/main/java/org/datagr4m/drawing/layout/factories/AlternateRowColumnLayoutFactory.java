package org.datagr4m.drawing.layout.factories;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;

public class AlternateRowColumnLayoutFactory extends HierarchicalLayoutFactory{
    @Override 
    public IHierarchicalNodeLayout getHierarchicalNodeLayout(IHierarchicalNodeModel model){
        IHierarchicalNodeLayout layout = getHierarchicalNodeLayout(model, HierarchicalLayoutFactory.LAYOUT_COLUMN_NAME);
        return layout;
    }
    
    public IHierarchicalNodeLayout getHierarchicalNodeLayout(IHierarchicalNodeModel model, String layout){
        IHierarchicalNodeLayout layoutModel = getNodeLayoutByName(layout);
        layoutModel.setModel(model);
        attachChildren(layoutModel, model, layout);
        return layoutModel;
    }
    
    protected void attachChildren(IHierarchicalNodeLayout layoutModel, IHierarchicalNodeModel model, String layout){
        for(IBoundedItem child: model.getChildren()){
            if(child instanceof IHierarchicalNodeModel){
                IHierarchicalNodeModel submodel = (IHierarchicalNodeModel)child;
                IHierarchicalNodeLayout subLayoutModel = null;
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

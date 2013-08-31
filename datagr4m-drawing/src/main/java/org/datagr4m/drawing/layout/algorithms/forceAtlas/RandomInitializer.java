package org.datagr4m.drawing.layout.algorithms.forceAtlas;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;

public class RandomInitializer implements IInitializer {
    @Override
    public void apply(IHierarchicalModel model) {
        if(model.getChildren().size()==1){
            model.getChildren().get(0).changePosition(0,0);
        }
        else{
            for(IBoundedItem item: model.getChildren()){
                float x = ((float)Math.random()-.5f)*1000;
                float y = ((float)Math.random()-.5f)*1000;
                item.changePosition(x,y);
            }            
        }
    }
}

    
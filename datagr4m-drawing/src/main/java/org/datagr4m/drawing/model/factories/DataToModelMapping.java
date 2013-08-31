package org.datagr4m.drawing.model.factories;

import java.util.HashMap;
import java.util.Map;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;


/** Ce groupe de m�thode permet d'associer un item a son parent. 
 * En consid�rant que un item n'a qu'un parent.*/
public class DataToModelMapping<V> {
    /** Try to see if we registered this item parent, or if the item is part of provided model
     * (without diving in recursion). */
    public IBoundedItem getLocalItemOrParentModelOrFail(V v, IHierarchicalModel modelToSeekIn){
        IBoundedItem item = null;
        if(hasParentModel(v)){
            item = getParentModel(v);
        }
        else
            item = modelToSeekIn.getItem(v, false);
        
        if(item==null)
            throw new RuntimeException("could not retrieve bounded item for " + v);
        return item;
    }
    
    public void registerParentModel(V v, IHierarchicalModel parentModel){
        if(hasParentModel(v))
            throw new RuntimeException("this item is already used as key");
        dataToParentModel.put(v, parentModel);
    }

    public boolean hasParentModel(V v){
        return dataToParentModel.keySet().contains(v);
    }

    public IHierarchicalModel getParentModel(V v){
        return dataToParentModel.get(v);
    }
    
    protected Map<V,IHierarchicalModel> dataToParentModel = new HashMap<V,IHierarchicalModel>();
}

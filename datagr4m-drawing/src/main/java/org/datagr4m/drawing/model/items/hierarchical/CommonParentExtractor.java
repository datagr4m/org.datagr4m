package org.datagr4m.drawing.model.items.hierarchical;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.datagr4m.drawing.model.items.IBoundedItem;

import com.google.common.collect.ArrayListMultimap;

/**
 * A utility to map a parent item to its children items.
 * Ability to indicate if an item can collapse.
 * 
 * @author Martin Pernollet
 *
 */
public class CommonParentExtractor {
	public CommonParentExtractor() {
        this.items = new ArrayList<IBoundedItem>();
    }
	
    public CommonParentExtractor(List<IBoundedItem> items) {
        this.items = new ArrayList<IBoundedItem>();
        add(items);
    }
    
    public void add(IBoundedItem item){
        this.items.add(item);
        parentGroups.put(item.getParent(), item);
    }    
    
    public void add(List<IBoundedItem> items){
        this.items.addAll(items);
        for(IBoundedItem i: items){
            parentGroups.put(i.getParent(), i);
        }
    }
    
    public Set<IHierarchicalModel> getParents(){
        return parentGroups.keySet();
    }
    
    public List<IBoundedItem> getChildren(IHierarchicalModel item){
        return parentGroups.get(item);
    }
    
    public List<IBoundedItem> getChildren(){
        return items;
    }
    
    public IHierarchicalModel getParent(IBoundedItem item){
        for(IHierarchicalModel parent: getParents()){
            if(getChildren(parent).contains(item))
                return parent;
        }
        return null;
    }
    
    public boolean contains(IBoundedItem item){
        for(IHierarchicalModel parent: getParents()){
            if(getChildren(parent).contains(item))
                return true;
        }
        return false;
    }
    
    public boolean hasParent(IBoundedItem item){
        return getParent(item)!=null;
    }
    
    public boolean canCollapse(IBoundedItem item){
        if(item==null)
            return false;
        else if(item instanceof IHierarchicalModel)
            return ((IHierarchicalModel)item).canCollapse();
        else
            return false;
    }

    /********/
    
    protected List<IBoundedItem> items;
    protected ArrayListMultimap<IHierarchicalModel,IBoundedItem> parentGroups = ArrayListMultimap.create();
}

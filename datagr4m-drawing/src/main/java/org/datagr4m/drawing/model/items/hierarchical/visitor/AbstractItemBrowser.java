package org.datagr4m.drawing.model.items.hierarchical.visitor;

import java.util.Collection;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;



public class AbstractItemBrowser {
	public boolean isParent(IBoundedItem element){
		if(element instanceof IHierarchicalModel)
			return true;
		else
			return false;
	}
	
	public boolean hasChildren(IHierarchicalModel element){
		if(null!=getChildren(element))
			return true;
		else
			return false;
	}
	
	public Collection<IBoundedItem> getChildren(IHierarchicalModel element){
		return element.getChildren();
	}
}

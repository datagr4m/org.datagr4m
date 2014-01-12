package org.datagr4m.drawing.model.items.hierarchical.visitor;

import java.util.Collection;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;



public class AbstractItemBrowser {
	public boolean isParent(IBoundedItem element){
		if(element instanceof IHierarchicalNodeModel)
			return true;
		else
			return false;
	}
	
	public boolean hasChildren(IHierarchicalNodeModel element){
		if(null!=getChildren(element))
			return true;
		else
			return false;
	}
	
	public Collection<IBoundedItem> getChildren(IHierarchicalNodeModel element){
		return element.getChildren();
	}
}

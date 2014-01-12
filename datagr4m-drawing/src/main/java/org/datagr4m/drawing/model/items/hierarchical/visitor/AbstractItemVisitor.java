package org.datagr4m.drawing.model.items.hierarchical.visitor;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;



public abstract class AbstractItemVisitor extends AbstractItemBrowser{
	public AbstractItemVisitor(){
		forbidden = new Vector<Class<? extends IHierarchicalNodeModel>>();
	}
	
	public void visit(IHierarchicalNodeModel element){
		visit(null, element, 0);
	}
	
	public void visit(IHierarchicalNodeModel parent, IBoundedItem element, int depth){
		doVisitElement(parent, element, depth);
		
		if(isParent(element)){
		    IHierarchicalNodeModel p = ((IHierarchicalNodeModel)element);
			Collection<IBoundedItem> children = p.getChildren();

			if(!isForbidden(p) && children!=null)
				for(IBoundedItem child: children)
					visit(p, child, depth+1);
		}
		
		postVisit(parent, element, depth);
	}
	
	public void postVisit(IHierarchicalNodeModel parent, IBoundedItem element, int depth){
	    
	}
	
	public abstract void doVisitElement(IHierarchicalNodeModel parent, IBoundedItem element, int depth);

	/******************************************************************************/
	
	public void doNotVisitChildrenOf(Class<? extends IHierarchicalNodeModel> parent){
		forbidden.add(parent);
	}
	
	public boolean isForbidden(IHierarchicalNodeModel parent){
		return isForbidden(parent.getClass());
	}
	
	public boolean isForbidden(Class<? extends IHierarchicalNodeModel> parent){
		for (Class<? extends IHierarchicalNodeModel> forbid: forbidden) {
			if(forbid.equals(parent))
				return true;
		}
		return false;
	}
	
	protected List<Class<? extends IHierarchicalNodeModel>> forbidden;
}

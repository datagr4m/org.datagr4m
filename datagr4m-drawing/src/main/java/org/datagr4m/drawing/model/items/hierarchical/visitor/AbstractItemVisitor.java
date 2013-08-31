package org.datagr4m.drawing.model.items.hierarchical.visitor;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;



public abstract class AbstractItemVisitor extends AbstractItemBrowser{
	public AbstractItemVisitor(){
		forbidden = new Vector<Class<? extends IHierarchicalModel>>();
	}
	
	public void visit(IHierarchicalModel element){
		visit(null, element, 0);
	}
	
	public void visit(IHierarchicalModel parent, IBoundedItem element, int depth){
		doVisitElement(parent, element, depth);
		
		if(isParent(element)){
		    IHierarchicalModel p = ((IHierarchicalModel)element);
			Collection<IBoundedItem> children = p.getChildren();

			if(!isForbidden(p) && children!=null)
				for(IBoundedItem child: children)
					visit(p, child, depth+1);
		}
		
		postVisit(parent, element, depth);
	}
	
	public void postVisit(IHierarchicalModel parent, IBoundedItem element, int depth){
	    
	}
	
	public abstract void doVisitElement(IHierarchicalModel parent, IBoundedItem element, int depth);

	/******************************************************************************/
	
	public void doNotVisitChildrenOf(Class<? extends IHierarchicalModel> parent){
		forbidden.add(parent);
	}
	
	public boolean isForbidden(IHierarchicalModel parent){
		return isForbidden(parent.getClass());
	}
	
	public boolean isForbidden(Class<? extends IHierarchicalModel> parent){
		for (Class<? extends IHierarchicalModel> forbid: forbidden) {
			if(forbid.equals(parent))
				return true;
		}
		return false;
	}
	
	protected List<Class<? extends IHierarchicalModel>> forbidden;
}

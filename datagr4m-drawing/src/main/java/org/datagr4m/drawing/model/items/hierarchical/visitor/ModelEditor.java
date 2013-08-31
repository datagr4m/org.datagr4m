package org.datagr4m.drawing.model.items.hierarchical.visitor;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;

public class ModelEditor extends AbstractItemVisitor{
	public ModelEditor(){
	}
	
	public void applyGroupShape(IHierarchicalModel root, ItemShape shape){
	    this.shape = shape;
	    visit(root);
	    this.shape = null;
	}
	
	@Override
	public void visit(IHierarchicalModel element){
        super.visit(element);
    }
	
	@Override
	public void doVisitElement(IHierarchicalModel parent, IBoundedItem element, int depth){
	    if(element instanceof IHierarchicalModel && shape!=null)
	        element.setShape(shape);
    }
	
	protected ItemShape shape = null;
	
}

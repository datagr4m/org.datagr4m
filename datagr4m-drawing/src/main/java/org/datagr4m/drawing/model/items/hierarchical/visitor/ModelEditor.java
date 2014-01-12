package org.datagr4m.drawing.model.items.hierarchical.visitor;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;

public class ModelEditor extends AbstractItemVisitor{
	public ModelEditor(){
	}
	
	public void applyGroupShape(IHierarchicalNodeModel root, ItemShape shape){
	    this.shape = shape;
	    visit(root);
	    this.shape = null;
	}
	
	@Override
	public void visit(IHierarchicalNodeModel element){
        super.visit(element);
    }
	
	@Override
	public void doVisitElement(IHierarchicalNodeModel parent, IBoundedItem element, int depth){
	    if(element instanceof IHierarchicalNodeModel && shape!=null)
	        element.setShape(shape);
    }
	
	protected ItemShape shape = null;
	
}

package org.datagr4m.drawing.model.items.hierarchical.visitor;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;

public class ItemPrinterVisitor extends AbstractItemVisitor{
	public ItemPrinterVisitor(){
	    this(new ToString());
	}
	
	public ItemPrinterVisitor(ToString toString){
	    this.toString = toString;
    }
	
	@Override
	public void doVisitElement(IHierarchicalModel parent, IBoundedItem element, int depth){
        //an opened node
        if(isParent(element) && !isForbidden((IHierarchicalModel)element))
            System.out.println( blanks(depth) + "|- " + toString.apply(element) );
        
        //a closed node
        else if(isParent(element) && isForbidden((IHierarchicalModel)element))
            System.out.println( blanks(depth) + "|+ " + toString.apply(element) );
        
        //a leaf
        else 
            System.out.println( blanks(depth) + "|o " + toString.apply(element) );
    }
	
	public String blanks(int n){
	    String out = "";
	    for (int i = 0; i < n; i++)
            out += " ";
        return out;
	}
	
	protected ToString toString;
	
	public static class ToString{
	    public String apply(Object o){
	        if(o==null)
	            return "null object";
	        /*else if(o instanceof IHierarchicalModel){
	            return ((IHierarchicalModel)o).getChildren().toString();
	        }*/
	        return o.toString();
	    }
	}
}

package org.datagr4m.drawing.model.items.hierarchical.visitor;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;


public class ItemNameFinderVisitor extends AbstractItemVisitor{
	public ItemNameFinderVisitor(){
	    results = new ArrayList<IBoundedItem>();
	    searchString = "";
	}
	
	@Override
	public void visit(IHierarchicalModel element){
        clearResults();
        super.visit(element);
    }
	
	@Override
	public void doVisitElement(IHierarchicalModel parent, IBoundedItem element, int depth){
	    String label = element.getLabel();
        if(label!=null && searchString!=null && label.equals(searchString)){
            results.add(element);
        }
    }
	
    /******************/
	
	public List<IBoundedItem> find(String query, IHierarchicalModel model){
	    searchString = query;
	    visit(model);
	    return new ArrayList<IBoundedItem>(results);
	}

	public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<IBoundedItem> getResults() {
        return results;
    }

    public void clearResults(){
        if(results!=null)
            results.clear();
    }

    /******************/

    protected String searchString;
	protected List<IBoundedItem> results;
}

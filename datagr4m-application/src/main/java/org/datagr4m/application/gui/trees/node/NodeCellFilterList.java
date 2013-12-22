package org.datagr4m.application.gui.trees.node;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.datagr4m.application.gui.trees.topology.TreeNodeFilterByLabel;

public class NodeCellFilterList {
	public NodeCellFilterList() {
		this.filters = new ArrayList<TreeNodeFilterByLabel>();
	}
	
	public NodeCellFilterList(List<TreeNodeFilterByLabel> filters) {
		this.filters = filters;
	}
	
	public List<TreeNodeFilterByLabel> getFilters() {
		return filters;
	}

	public void setFilters(List<TreeNodeFilterByLabel> filters) {
		this.filters = filters;
	}
	
	public void addFilter(TreeNodeFilterByLabel filter){
		filters.add(filter);
	}
	
	public void clear(){
		filters.clear();
	}
	
	public boolean accepts(Object o) {
		if(o instanceof DefaultMutableTreeNode){
			for(TreeNodeFilterByLabel filter: filters)
				if( ! filter.accepts((DefaultMutableTreeNode)o) )
					return false;
			
		}
		return true;
	}
	
	@Override
	public String toString(){
		StringBuffer b = new StringBuffer();
        for(TreeNodeFilterByLabel filter: filters)
            b.append( filter.toString() + "\n" );
		return b.toString();
	}

	protected List<TreeNodeFilterByLabel> filters;
}

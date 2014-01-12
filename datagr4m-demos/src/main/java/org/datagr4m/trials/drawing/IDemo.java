package org.datagr4m.trials.drawing;

import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.workspace.factories.IDatagr4mFactories;

public interface IDemo {
	public void show();
	public ILayoutRunner run(IBreakCriteria criteria);
	
	public void makeModel();
	public void makeLayout(IHierarchicalNodeModel model);
	public void makeDisplay(IHierarchicalNodeModel model);
	
	public IDatagr4mFactories getFactories();
}
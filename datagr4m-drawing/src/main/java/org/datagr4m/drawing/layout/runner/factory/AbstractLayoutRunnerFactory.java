package org.datagr4m.drawing.layout.runner.factory;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutRunnerConfiguration;
import org.datagr4m.viewer.IView;

public abstract class AbstractLayoutRunnerFactory  implements ILayoutRunnerFactory{

	public AbstractLayoutRunnerFactory() {
		super();
	}

	@Override
	public ILayoutRunner newLayoutRunner(IHierarchicalLayout root) {
	    return newLayoutRunner(root, null);
	}

	@Override
	public ILayoutRunner newLayoutRunner(IHierarchicalLayout root, IView view) {
	    return newLayoutRunner(root, view, LayoutRunnerConfiguration.getDefault());
	}

	@Override
	public ILayoutRunner newLayoutRunner(IHierarchicalLayout root, IView view, LayoutRunnerConfiguration settings) {
		ILayoutRunner runner = newLayoutRunner();
		runner.configure(root, view, settings);
		return runner;
	}

}
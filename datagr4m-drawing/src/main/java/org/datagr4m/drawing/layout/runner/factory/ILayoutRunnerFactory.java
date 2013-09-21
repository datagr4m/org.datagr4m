package org.datagr4m.drawing.layout.runner.factory;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutRunnerConfiguration;
import org.datagr4m.viewer.IView;

public interface ILayoutRunnerFactory {
	public ILayoutRunner newLayoutRunner();
    public ILayoutRunner newLayoutRunner(IHierarchicalLayout root) ;
    public ILayoutRunner newLayoutRunner(IHierarchicalLayout root, IView view);
    public ILayoutRunner newLayoutRunner(IHierarchicalLayout root, IView view, LayoutRunnerConfiguration settings);
}

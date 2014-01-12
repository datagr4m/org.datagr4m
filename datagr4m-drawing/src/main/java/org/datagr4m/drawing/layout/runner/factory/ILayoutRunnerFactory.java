package org.datagr4m.drawing.layout.runner.factory;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutRunnerConfiguration;
import org.datagr4m.viewer.IView;

public interface ILayoutRunnerFactory {
	public ILayoutRunner newLayoutRunner();
    public ILayoutRunner newLayoutRunner(IHierarchicalNodeLayout root) ;
    public ILayoutRunner newLayoutRunner(IHierarchicalNodeLayout root, IView view);
    public ILayoutRunner newLayoutRunner(IHierarchicalNodeLayout root, IView view, LayoutRunnerConfiguration settings);
}

package org.datagr4m.trials.drawing;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.View;
import org.datagr4m.workspace.factories.Datagr4mFactories;
import org.datagr4m.workspace.factories.IDatagr4mFactories;

public abstract class AbstractDemo implements IDemo {
	protected IDatagr4mFactories factories = new Datagr4mFactories();
	protected IDisplay display = null;
	protected IHierarchicalNodeModel model = null;
	protected IHierarchicalNodeLayout layout = null;
	protected IHierarchicalRenderer renderer;
	protected ILayoutRunner runner;

	@Override
	public abstract void makeModel();

	@Override
	public void show() {
		makeModel();
		makeLayout(model);
		makeDisplay(model);
		display.openFrame();
	}

	@Override
	public void makeLayout(IHierarchicalNodeModel model) {
		IHierarchicalNodeLayout layout = factories.getLayoutFactory().getLayout(
				model);
		layout.initAlgo();
	}

	@Override
	public void makeDisplay(IHierarchicalNodeModel model) {
		display = new Display(true, factories.getMouseFactory());
		makeRenderer(model, display);
		display.setView(new View(renderer, display));
	}

	public void makeRenderer(IHierarchicalNodeModel model, IDisplay display) {
		renderer = factories.getRendererFactory().getRenderer(display, model);
	}

	@Override
	public ILayoutRunner run(IBreakCriteria criteria) {
		makeRunner(criteria);
		runner.start();
		return runner;
	}

	protected void makeRunner(IBreakCriteria criteria) {
		runner = new LayoutRunner(layout);
		runner.getConfiguration().getSequence()
				.setFirstPhaseBreakCriteria(criteria);
	}

	@Override
	public IDatagr4mFactories getFactories() {
		return factories;
	}
}

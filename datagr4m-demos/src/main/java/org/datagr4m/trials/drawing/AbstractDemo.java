package org.datagr4m.trials.drawing;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.View;
import org.datagr4m.workspace.factories.Datagr4mFactories;
import org.datagr4m.workspace.factories.IDatagr4mFactories;

public abstract class AbstractDemo implements IDemo {
	protected IDatagr4mFactories factories = new Datagr4mFactories();
	protected IDisplay display = null;
	protected IHierarchicalModel model = null;
	protected IHierarchicalLayout layout = null;
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
	public void makeLayout(IHierarchicalModel model) {
		IHierarchicalLayout layout = factories.getLayoutFactory().getLayout(
				model);
		layout.initAlgo();
	}

	@Override
	public void makeDisplay(IHierarchicalModel model) {
		display = new Display(true, factories.getMouseFactory());
		makeRenderer(model, display);
		display.setView(new View(renderer, display));
	}

	public void makeRenderer(IHierarchicalModel model, IDisplay display) {
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

package org.datagr4m.trials.drawing;

import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.View;

public class DisplayLauncher {
	public static IDisplay display(IHierarchicalModel model) {
		// display
		final Display display = new Display(true,
				new MouseItemControllerFactory());
		IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
		IHierarchicalRenderer renderer = rendererFactory.getRenderer(display,
				model);
		display.setView(new View(renderer, display));
		display.openFrame();

		ForceDebugger.attach((MouseItemViewController) display.getMouse(),
				model, renderer);

		// TODO enable rendering policy
		/*
		 * AbstractRenderingPolicy p = new AbstractRenderingPolicy(true);
		 * p.setup(model); p.apply(renderer);
		 */

		return display;
	}
}

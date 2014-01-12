package org.datagr4m.trials.drawing.demo04.hierarchies;

import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.topology.generator.TopologyGenerator;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;

public class DemoNestedGraphGeneratedWithMultiLayerEdgeBundling {
    public static void main(String[] args){
        final IHierarchicalNodeModel model = model(2, 3, 100);
        final HierarchicalGraphLayout layout = layout(model);
        
        // display
        final Display display = new Display(true, new MouseItemControllerFactory());
        IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        renderer.addPostRenderer(new TubeRenderer(display, model.getEdgeModel()));
        display.setView(new View(renderer, display));
        display.openFrame();
        
        ForceDebugger.attach((MouseItemViewController)display.getMouse(), model, renderer);

        // Run
        final LayoutRunner runner = new LayoutRunner(layout, display.getView());
        runner.getConfiguration().setAllowAutoFitAtStepEnd(true);
        runner.start();
    }

	public static HierarchicalGraphLayout layout(IHierarchicalNodeModel model) {
		//ForceListing listing = new ForceListing();
        //listing.exportForcesCsv(model, "forces-" + DemoGraphNestedGenerated.class.getSimpleName().toLowerCase() + ".csv");
        IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        HierarchicalGraphLayout layout = (HierarchicalGraphLayout)layoutFactory.getLayout(model);//getNodeLayoutByModelType(model);
        layout.initAlgo();
		return layout;
	}

    /**
     * Build a hierarchical graph according to settings
     * @param depth depth of the hierarchy
     * @param width number of subgroups per groups
     * @param nedge number of edges
     * @return
     */
    public static IHierarchicalNodeModel model(int depth, int width, int nedge) {
        IHierarchicalModelFactory factory = new HierarchicalTopologyModelFactory<String, String>();
        return (IHierarchicalNodeModel)factory.getLayoutModel(TopologyGenerator.buildGraphNested(depth, width, nedge));
    }   
}

package org.datagr4m.trials.drawing.demo04.hierarchies;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.model.factories.HierarchicalModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.hierarchical.AbstractHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class DemoGraphFlatSimple {
    public static boolean DISPLAY_BOUNDS = true;
    public static boolean DISPLAY_EDGE_EMBEDDED = true;
    
    public static void main(String[] args){
        final IHierarchicalModel model = model();
        final IHierarchicalLayout layout = layout(model);
        
        // display
        final Display display = new Display(true, new MouseItemControllerFactory());
        IHierarchicalRenderer renderer = renderer(model, display);
        display.openFrame();
        ForceDebugger.attach((MouseItemViewController)display.getMouse(), model, renderer);
        
        // run
        ((AbstractHierarchicalModel)model).toConsole();
        for (int i = 0; i < 1000; i++) {
            layout.goAlgo();
        }
        model.fit(display.getView());

    }

	public static IHierarchicalRenderer renderer(IHierarchicalModel model,
			final Display display) {
		// renderer
        IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        ((HierarchicalGraphRenderer)renderer).getRendererSettings().setLocalEdgeDisplayed(DISPLAY_EDGE_EMBEDDED);
        ((HierarchicalGraphRenderer)renderer).getRendererSettings().getBoundsSettings().setBoundDisplayed(null, DISPLAY_BOUNDS);
        display.setView(new View(renderer, display));
		return renderer;
	}

	public static IHierarchicalLayout layout(IHierarchicalModel model) {
		IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        IHierarchicalLayout layout = layoutFactory.getNodeLayoutByModelType(model);
        
        layout.initAlgo();
        
        BoundedForceAtlasLayout bfao = ((HierarchicalGraphLayout)layout).getDelegate();
        /*bfao.setAttractionStrength(1d);
        bfao.setRepulsionStrength(10d);
        bfao.setMaintainToCenter(false);
        bfao.setGravity(0d);*/
		return layout;
	}
    
    public static IHierarchicalModel model(){
        String d1 = new String("d1");
        String d2 = new String("d2");
        String d3 = new String("d3");
        String d4 = new String("d4");
        String d5 = new String("d5");
        String d6 = new String("d6");
        String d7 = new String("d7");
        
        // make a graph
        Graph<String,String> graph = new DirectedSparseGraph<String, String>();
        graph.addVertex(d1);
        graph.addVertex(d2);
        graph.addVertex(d3);
        graph.addVertex(d4);
        graph.addVertex(d5);
        graph.addVertex(d6);
        graph.addVertex(d7);
        graph.addEdge("hsrpA", d1, d2); // hsrp
        graph.addEdge("hsrpB", d6, d7); // hsrp

        graph.addEdge("hsrpA.1>3", d1, d3);
        graph.addEdge("hsrpA.2>3", d2, d3);
        graph.addEdge("hsrpA.1>4", d1, d4);
        graph.addEdge("hsrpA.2>4", d2, d4);
        graph.addEdge("hsrpA.1>5", d1, d5);
        graph.addEdge("hsrpA.2>5", d2, d5);
        graph.addEdge("hsrpB.1>5", d6, d5);
        graph.addEdge("hsrpB.2>5", d7, d5);

        IHierarchicalModelFactory f = new HierarchicalModelFactory<String, String>();
        return new HierarchicalGraphModel(graph, f);
    }

}

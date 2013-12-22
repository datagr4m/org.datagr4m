package org.datagr4m.trials.drawing.demo04.hierarchies;

import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MaxStepCriteria;
import org.datagr4m.drawing.model.factories.HierarchicalModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class DemoNestedGraphWithSingleLayerEdges {
    public static void main(String[] args){
        final IHierarchicalModel model = model();
        final HierarchicalGraphLayout layout = layout(model);
        final Display display = display(model);
        
        // Run
        final LayoutRunner runner = new LayoutRunner(layout);
        IBreakCriteria criteria = new MaxStepCriteria(1000){
            @Override
			public void onBreak(){  
                System.out.println(getMaxSteps() + " steps with: \n" + runner.getConfiguration());
                ((View)display.getView()).fit(model);

            }
        };
        runner.getConfiguration().getSequence().setFirstPhaseBreakCriteria(criteria);
        runner.start();
    }

	public static Display display(final IHierarchicalModel model) {
		// display
        final Display display = new Display(true, new MouseItemControllerFactory());
        IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        display.setView(new View(renderer, display));
        display.openFrame();
        
        ForceDebugger.attach((MouseItemViewController)display.getMouse(), model, renderer);
		return display;
	}

	public static HierarchicalGraphLayout layout(final IHierarchicalModel model) {
		IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        HierarchicalGraphLayout layout = (HierarchicalGraphLayout)layoutFactory.getLayout(model);//getNodeLayoutByModelType(model);
        layout.getDelegate().setRepulsionStrength(1d);
        
        layout.initAlgo();
		return layout;
	}
    
    public static IHierarchicalModel model(){
        IHierarchicalModelFactory factory = new HierarchicalModelFactory<String, String>();

        
        // ----------------
        String d1 = new String("dddddd1");
        String d2 = new String("dddddd2");
        String d3 = new String("dddddd3");

        Graph<String,String> graph1 = new DirectedSparseGraph<String, String>();
        graph1.addVertex(d1);
        graph1.addVertex(d2);
        graph1.addVertex(d3);
        graph1.addEdge("1>3", d1, d3);
        graph1.addEdge("1>2", d1, d2);
        
        HierarchicalGraphModel g1 = new HierarchicalGraphModel(graph1, factory);
        g1.setLabel("g1");
        g1.setShape(ItemShape.RECTANGLE);
        
        // ----------------
        String d4 = new String("d4");
        String d5 = new String("d5");
        String d6 = new String("d6");
        String d7 = new String("d7");
        

        Graph<String,String> graph2 = new DirectedSparseGraph<String, String>();
        graph2.addVertex(d4);
        graph2.addVertex(d5);
        graph2.addVertex(d6);
        graph2.addVertex(d7);
        graph2.addEdge("4>5", d4, d5);
        graph2.addEdge("4>6", d4, d6);
        graph2.addEdge("4>7", d4, d7);
        graph2.addEdge("5>7", d5, d7);
        HierarchicalGraphModel g2 = new HierarchicalGraphModel(graph2, factory);
        g2.setLabel("g2");
        // ----------------
        String d8 = new String("d8");
        String d9 = new String("d9");
        String d10 = new String("d10");
        String d11 = new String("d11");
        
        Graph<String,String> graph3 = new DirectedSparseGraph<String, String>();
        graph3.addVertex(d8);
        graph3.addVertex(d9);
        graph3.addVertex(d10);
        graph3.addVertex(d11);
        graph3.addEdge("8>9", d8, d9);
        graph3.addEdge("8>10", d8, d10);
        graph3.addEdge("8>11", d8, d11);
        graph3.addEdge("10>11", d10, d11);
        HierarchicalGraphModel g3 = new HierarchicalGraphModel(graph3, factory);
        g3.setLabel("g3");
        // ----------------
        HierarchicalGraphModel root = new HierarchicalGraphModel();
        root.registerChild(g1.getObject(), g1);
        root.registerChild(g2.getObject(), g2);
        root.registerChild(g3.getObject(), g3);
        root.addLocalEdge(g1, g2);
        root.addLocalEdge(g2, g3);
        root.addLocalEdge(g3, g1);
        root.addAttractionEdgeForce(g1,g2);
        root.addAttractionEdgeForce(g2,g3);
        root.addAttractionEdgeForce(g3,g1);
        root.setNodeDegree(g1, 2);
        root.setNodeDegree(g2, 2);
        root.setNodeDegree(g3, 2);
        g1.setParent(root);
        g2.setParent(root);
        g3.setParent(root);
        root.createAllMutualRepulsors();
        //root.createAllMutualAttractors();
        root.setLabel("root");
        return root;
    }
}

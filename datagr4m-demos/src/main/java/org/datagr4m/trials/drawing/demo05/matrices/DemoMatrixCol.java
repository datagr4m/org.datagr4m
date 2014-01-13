package org.datagr4m.trials.drawing.demo05.matrices;

import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalColumnLayout;
import org.datagr4m.drawing.model.items.BoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseHitModelAdapter;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;

public class DemoMatrixCol {
    public static void main(String[] args){
        int n = 10;
        
        // model
        HierarchicalGraphModel model = new HierarchicalGraphModel();
        for (int i = 0; i < n; i++) 
            model.addChild(new BoundedItem("i"+i));
        
        // layout
        HierarchicalColumnLayout layout = new HierarchicalColumnLayout();
        layout.setModel(model);
        layout.initAlgo();
        
        // display
        final Display display = new Display(true, new MouseItemControllerFactory());
        IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        display.setView(new View(renderer, display));
        display.openFrame();
        ((MouseItemViewController)display.getMouse()).addMouseHitListener(new MouseHitModelAdapter());
        model.fit(display.getView());
    }
}

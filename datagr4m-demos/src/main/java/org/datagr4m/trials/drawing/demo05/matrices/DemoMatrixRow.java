package org.datagr4m.trials.drawing.demo05.matrices;

import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalRowLayout;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseHitModelAdapter;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;

public class DemoMatrixRow {
    public static void main(String[] args){
        int n = 14;
        
        // model
        HierarchicalGraphModel model = new HierarchicalGraphModel();
        for (int i = 0; i < n; i++) 
            model.addChild(new DefaultBoundedItem("i"+i));
        
        // layout
        HierarchicalRowLayout layout = new HierarchicalRowLayout();
        layout.setModel(model);
        layout.initAlgo();        
        
        // display
        final Display display = new Display(true, new MouseItemControllerFactory());
        IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        display.setView(new View(renderer, display));
        display.openFrame();
        ((MouseItemViewController)display.getMouse()).addMouseHitListener(new MouseHitModelAdapter());
        
        ((View)display.getView()).fit(model);
    }
}

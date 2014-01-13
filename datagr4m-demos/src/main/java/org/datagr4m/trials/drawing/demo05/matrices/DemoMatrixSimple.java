package org.datagr4m.trials.drawing.demo05.matrices;

import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalMatrixLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.IHierarchicalMatrixLayout;
import org.datagr4m.drawing.model.items.BoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseHitModelAdapter;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;

public class DemoMatrixSimple {
    public static void main(String[] args){
        // model
        IBoundedItem i1 = new BoundedItem("i1", 30);
        IBoundedItem i2 = new BoundedItem("i2", 30);
        IBoundedItem i3 = new BoundedItem("i3", 30);

        HierarchicalGraphModel i4 = new HierarchicalGraphModel();
        i4.addChild(new BoundedItem("i4.child1"));
        i4.addChild(new BoundedItem("i4.child2"));

        HierarchicalGraphModel i5 = new HierarchicalGraphModel();
        i5.addChild(new BoundedItem("i5.child1"));
        
        HierarchicalGraphModel i6 = new HierarchicalGraphModel();
        i6.addChild(new BoundedItem("i6.child1"));
        i6.addChild(new BoundedItem("i6.child2"));
        i6.addChild(new BoundedItem("i6.child3"));
        i6.addChild(new BoundedItem("i6.child4"));
        
        HierarchicalGraphModel model = new HierarchicalGraphModel();
        model.addChild(i1);
        model.addChild(i2);
        model.addChild(i3);
        model.addChild(i4);
        model.addChild(i5);
        model.addChild(i6);
        
        // sub layout 2
        IHierarchicalMatrixLayout sublayout4 = new HierarchicalMatrixLayout();
        sublayout4.setSize(1, 2);
        sublayout4.setLineHeight(50);
        sublayout4.setColumnWidth(50);
        sublayout4.setModel(i4);
        sublayout4.setItemCell(i4.getChildren().get(0), 0, 0);
        sublayout4.setItemCell(i4.getChildren().get(1), 0, 1);
        //sublayout4.setItemCell(i4.getChildren().get(2), 0, 2);

        // sub layout 2
        IHierarchicalMatrixLayout sublayout5 = new HierarchicalMatrixLayout();
        sublayout5.setSize(1, 1);
        sublayout5.setLineHeight(50);
        sublayout5.setColumnWidth(50);
        sublayout5.setModel(i5);
        sublayout5.setItemCell(i5.getChildren().get(0), 0, 0);

        // sub layout 2
        IHierarchicalMatrixLayout sublayout6 = new HierarchicalMatrixLayout();
        sublayout6.setSize(4, 1);
        sublayout6.setLineHeight(50);
        sublayout6.setColumnWidth(50);
        sublayout6.setModel(i6);
        sublayout6.setItemCell(i6.getChildren().get(0), 0, 0);
        sublayout6.setItemCell(i6.getChildren().get(1), 1, 0);
        sublayout6.setItemCell(i6.getChildren().get(2), 2, 0);
        sublayout6.setItemCell(i6.getChildren().get(3), 4, 0);
        
        // main layout
        HierarchicalMatrixLayout layout = new HierarchicalMatrixLayout();
        layout.setSize(2, 3);
        layout.setLineHeight(100);
        layout.setColumnWidth(100);
        layout.setModel(model);
        layout.setItemCell(i1, 0, 0);
        layout.setItemCell(i2, 0, 1);
        layout.setItemCell(i3, 0, 2);
        layout.setItemCell(i4, 1, 0);
        layout.setItemCell(i5, 1, 1);
        layout.setItemCell(i6, 1, 2);
        
        layout.addChild(sublayout4);
        layout.addChild(sublayout5);
        layout.addChild(sublayout6);
        layout.initAlgo();
        
        
        // display
        final Display display = new Display(true, new MouseItemControllerFactory());
        IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        display.setView(new View(renderer, display));
        display.openFrame();
        ((MouseItemViewController)display.getMouse()).addMouseHitListener(new MouseHitModelAdapter());
        //display.getMouse().setRootLayout(layout);

        //BulkRendererSettings.debug(renderer, false);
        
        model.fit(display.getView());
    }
}

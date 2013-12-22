package org.datagr4m.trials.drawing.demo02.items;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.View;
import org.jzy3d.maths.Coord2d;

public class DemoMultilineItemLabel {
    public static void main(String[] args) throws Exception{
        IHierarchicalModel model = pairGraph();
        
        // layout
        IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        IHierarchicalLayout layout = layoutFactory.getNodeLayoutByModelType(model);
        layout.initAlgo();
         
        // display
        IDisplay display = new Display(true, new MouseItemControllerFactory());
        IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        display.setView(new View(renderer, display));
        display.openFrame();
        ((View)display.getView()).fit(model);
    }
    
    public static IHierarchicalModel pairGraph() throws Exception{
    	List<String> labels = new ArrayList<String>();
    	labels.add("left1.line1");
    	labels.add("left1.line2");
    	labels.add("left1.line3");
    	IBoundedItem left1 = new DefaultBoundedItem(labels);
        IBoundedItem right1 = new DefaultBoundedItem("right1", 30);

    	HierarchicalPairModel child1 = new HierarchicalPairModel(left1, right1);
        
        HierarchicalGraphModel root = new HierarchicalGraphModel();
        root.addChild(child1);
        
        root.setNodeDegree(child1, 0);

        List<IBoundedItem> list = new ArrayList<IBoundedItem>();
        list.add(child1);

        left1.changePosition(new Coord2d(-200,0));
        right1.changePosition(new Coord2d(200,0));
        
        root.createAllMutualRepulsors();
        root.createAllMutualAttractors();
        root.verify();
        return root;
    }
}

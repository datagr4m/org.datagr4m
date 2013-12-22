package org.datagr4m.trials.drawing.demo04.hierarchies;

import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunnerFactory;
import org.datagr4m.drawing.layout.runner.sequence.LayoutRunnerSequenceSinglePhase;
import org.datagr4m.drawing.layout.runner.stop.MaxStepCriteria;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.generator.TopologyGenerator;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.View;
import org.datagr4m.workspace.Workspace;


public class DemoRunHierarchicalLayout {
    public static void main(String[] args) throws Exception{
        Topology<String,String> topology = TopologyGenerator.buildGraphNested(2, 3, 100);

        IHierarchicalModelFactory factory = new HierarchicalTopologyModelFactory<String, String>();
        HierarchicalGraphModel model = (HierarchicalGraphModel) factory.getLayoutModel(topology);
        HierarchicalGraphLayout layout = layout(model);
        
        IDisplay d = display(model);
        
        Workspace.defaultRunnerFactory = new LayoutRunnerFactory();
        ILayoutRunner runner = Workspace.defaultRunnerFactory.newLayoutRunner(layout, d.getView());
        runner.getConfiguration().setAllowAutoFitAtStepEnd(true);
        LayoutRunnerSequenceSinglePhase seq = (LayoutRunnerSequenceSinglePhase)runner.getConfiguration().getSequence();
        seq.setFirstPhaseBreakCriteria(new MaxStepCriteria(10000));
        runner.start();
        
        Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(model);
        
        if(overlapping.size()>0)
            System.out.println(overlapping);
    }
    
    protected static HierarchicalGraphLayout layout(IHierarchicalModel model) {
        IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        HierarchicalGraphLayout layout = (HierarchicalGraphLayout)layoutFactory.getLayout(model);
        layout.initAlgo();
        return layout;
    }
    
    public static IDisplay display(IHierarchicalModel model){
     // display
        final Display display = new Display(true, new MouseItemControllerFactory());
        IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        display.setView(new View(renderer, display));
        display.openFrame();
        
        ForceDebugger.attach((MouseItemViewController)display.getMouse(), model, renderer);

        /*IRenderingPolicy p = new DeviceRenderingPolicy();
        p.setup(model);
        p.apply(renderer);*/
        
        return display;
    }
}

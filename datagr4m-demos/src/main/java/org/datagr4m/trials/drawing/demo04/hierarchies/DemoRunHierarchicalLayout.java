package org.datagr4m.trials.drawing.demo04.hierarchies;

import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunnerFactory;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MeanMoveCriteria;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.generator.TopologyGenerator;
import org.datagr4m.trials.drawing.DisplayInitilizer;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;

public class DemoRunHierarchicalLayout {
    public static void main(String[] args) throws Exception {
        Topology<String, String> topology = TopologyGenerator.buildGraphNested(2, 3, 100);
        //graph.setShape(ItemShape.RECTANGLE);

        Workspace.defaultRunnerFactory = new LayoutRunnerFactory();
        Workspace w = new Workspace(topology);
        //show(w, new MaxStepCriteria(10000));
        show(w, new MeanMoveCriteria(1000));
        Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(w.getModel());
        if (overlapping.size() > 0)
            System.out.println(overlapping);
    }

    public static void show(Workspace w, IBreakCriteria criteria) {
        DisplayInitilizer di = new DisplayInitilizer(EdgeComputationPolicy.COMPUTE_AT_END, EdgeRenderingPolicy.ALWAYS, ViewPolicy.AUTOFIT_AT_RUN);
        di.init(w).openFrame();
        ILayoutRunner runner = w.getRunner();
        runner.getConfiguration().getSequence().setFirstPhaseBreakCriteria(criteria);
        runner.start();
    }
}

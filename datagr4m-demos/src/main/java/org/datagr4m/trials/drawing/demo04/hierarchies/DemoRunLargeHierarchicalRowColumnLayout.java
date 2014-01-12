package org.datagr4m.trials.drawing.demo04.hierarchies;

import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.factories.AlternateRowColumnLayoutFactory;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunnerFactory;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MaxStepCriteria;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.drawing.monitors.ConsoleMonitorReport;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.generator.TopologyGenerator;
import org.datagr4m.trials.drawing.DisplayInitilizer;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.WorkspaceTimeMonitor;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;

public class DemoRunLargeHierarchicalRowColumnLayout {
    public static void main(String[] args) throws Exception {
        Topology<String, String> topology = TopologyGenerator.buildGraphNested(3, 7, 100);

        //-------------
        Workspace.defaultRunnerFactory = new LayoutRunnerFactory();
        Workspace.defaultLayoutFactory = new AlternateRowColumnLayoutFactory();
        Workspace w = new Workspace(topology);
        w.getModel().setShape(ItemShape.RECTANGLE, true);
        
        WorkspaceTimeMonitor monitor = new WorkspaceTimeMonitor(w);

        //-------------
        //show(w);
        start(w, new MaxStepCriteria(100));
        //show(w, new MeanMoveCriteria(1000));
        
        //-------------
        
        report(w, monitor);
    }

    public static void report(Workspace w, WorkspaceTimeMonitor monitor) {
        ConsoleMonitorReport report = new ConsoleMonitorReport();
        report.report(monitor);
        
        Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(w.getModel());
        if (overlapping.size() > 0)
            System.out.println(overlapping);
    }

    public static void show(Workspace w) {
        DisplayInitilizer di = new DisplayInitilizer(EdgeComputationPolicy.COMPUTE_AT_END, EdgeRenderingPolicy.ALWAYS, ViewPolicy.AUTOFIT_AT_RUN);
        di.init(w).openFrame();
    }

    public static void start(Workspace w, IBreakCriteria criteria) {
        ILayoutRunner runner = w.getRunner();
        runner.getConfiguration().getSequence().setFirstPhaseBreakCriteria(criteria);
        runner.start();
    }
}

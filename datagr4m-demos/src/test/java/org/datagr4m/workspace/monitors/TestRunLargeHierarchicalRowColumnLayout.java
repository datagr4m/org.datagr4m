package org.datagr4m.workspace.monitors;

import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.factories.AlternateRowColumnLayoutFactory;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutRunnerListenerAdapter;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MaxStepCriteria;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.monitors.FileMonitorReport;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.generator.TopologyGenerator;
import org.datagr4m.trials.drawing.DisplayInitilizer;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.WorkspaceTimeMonitor;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;

public class TestRunLargeHierarchicalRowColumnLayout {
    public static void main(String[] args) throws Exception {
        int depth = 2;
        int width = 3;
        int nchild = 10;
        int edges = 100;
        
        Topology<String, String> topology = TopologyGenerator.buildGraphNested(depth, width, nchild, edges);
        
        Workspace w = createWorkspace(topology);
        
        // configure force
        //SimpleCsv.writeLines(lines, file, separator);
        
        show(w); // before start
        startAndReport(w, new MaxStepCriteria(10000));
    }

    public static Workspace createWorkspace(Topology<String, String> topology) {
        Workspace w = new Workspace(topology);
        return w;
    }
    
    public static Workspace createRowColumnLayoutWorkspace(Topology<String, String> topology) {
        Workspace.defaultModelFactory = new HierarchicalTopologyModelFactory<String,String>(){
            // avoid pair model generation
            protected void createChildrenGroup(Topology<String, String> topology,
                    HierarchicalGraphModel parent, Group<String> group, int depth)
                    throws RuntimeException {
                    createChildrenGraph(topology, parent, group, depth);
            }
        };
        Workspace.defaultLayoutFactory = new AlternateRowColumnLayoutFactory(){
            /*@Override
            public IHierarchicalEdgeLayout getHierarchicalEdgeLayout(IHierarchicalNodeModel model){
                IHierarchicalEdgeLayout edgeLayout = new SlotedTubeLayout(pathFactory);
                edgeLayout.setEdgePostProcess(new StratumEdgePostProcess());
                return edgeLayout;
            }*/
        };
        Workspace w = new Workspace(topology);
        w.getModel().setShape(ItemShape.RECTANGLE, true);
        return w;
    }


    public static void show(Workspace w) {
        DisplayInitilizer di = new DisplayInitilizer(EdgeComputationPolicy.COMPUTE_AT_END, EdgeRenderingPolicy.ALWAYS, ViewPolicy.AUTOFIT_AT_RUN);
        di.init(w).openFrame();
    }

    public static void startAndReport(final Workspace w, IBreakCriteria criteria) {
        ILayoutRunner runner = w.getRunner();
        runner.getConfiguration().getSequence().setFirstPhaseBreakCriteria(criteria);
        runner.start();

        final WorkspaceTimeMonitor monitor = new WorkspaceTimeMonitor(w);

        runner.addListener(new LayoutRunnerListenerAdapter() {
            @Override
            public void runnerStopped() {
                report(w, monitor);
            }

            @Override
            public void runnerFinished() {
                report(w, monitor);
            }

            @Override
            public void runnerFailed(String message, Exception e) {
                report(w, monitor);
            }
            
            public void report(Workspace w, WorkspaceTimeMonitor monitor) {
                FileMonitorReport report = new FileMonitorReport("data/monitors/monitors.csv");
                report.report(monitor);
                
                Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(w.getModel());
                if (overlapping.size() > 0)
                    System.out.println(overlapping);
            }

        });
    }
}

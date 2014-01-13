package org.datagr4m.workspace;

import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutLevelSettings;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunnerFactory;
import org.datagr4m.drawing.layout.runner.sequence.LayoutRunnerSequenceSinglePhase;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MaxStepCriteria;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.io.TopologyIOXML;
import org.datagr4m.trials.drawing.DisplayInitilizer;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;
import org.junit.Test;

/**
 * Integration test: load an XML topology and verify that the workspace has the
 * appropriate data model.
 * 
 * To ease debugging, one may use: w.getTopology().toConsole();
 * w.getEdgeModel().toConsole();
 * 
 */
public class TestWorkspaceInitFromXMLTopologyFlat{
    public static void main(String[] str) throws Exception {
        TestWorkspaceInitFromXMLTopologyFlat t = new TestWorkspaceInitFromXMLTopologyFlat();
        t.loadAndTest(false, -1);
    }

    @Test
    public void testLoad() throws Exception {
        loadAndTest(true, 10);
    }

    
    //SlotLayoutCOnfiguration.isConsiderInterfaceForPath()
    
    
    public void loadAndTest(boolean test, int waitFor) throws Exception {
        TopologyIOXML xmlt = new TopologyIOXML();
        Topology<IPropertyNode, IPropertyEdge> topology = xmlt.loadTopology("src/test/resources/topology-flat.xml");
        // topology.toConsole();

        Workspace w = new Workspace(topology);
        // w.getEdgeModel().toConsole();
        display(w);
        
        //new MeanMoveCriteria(1, 10));
        IBreakCriteria criteria = new MaxStepCriteria(100);
        runAndTest(test, waitFor, w, criteria);
        
    }

    private void runAndTest(boolean test, int waitFor, Workspace w, IBreakCriteria criteria) throws Exception {
        ILayoutRunner runner = w.getRunner();
        
        runner.getConfiguration().setAllowAutoFitAtStepEnd(true);
        LayoutRunnerSequenceSinglePhase seq = (LayoutRunnerSequenceSinglePhase)runner.getConfiguration().getSequence();
        seq.setFirstPhaseBreakCriteria(criteria);
        
        LayoutLevelSettings settings = runner.getLayoutSettings(w.getNodeLayout().getChildren().get(0));
        System.out.println(settings.getRepulsion());
        settings.setRepulsion(500);
        settings.setAttraction(1);

        if (test && waitFor > 0)
            ((LayoutRunner) runner).startAndAwaitAtMost(waitFor);
        else
            runner.start();
    }
    
    public void display(Workspace w) {
        DisplayInitilizer di = new DisplayInitilizer(EdgeComputationPolicy.ALWAYS, EdgeRenderingPolicy.ALWAYS, ViewPolicy.AUTOFIT_AT_RUN);
        di.init(w).openFrame();
    }

    {
        Workspace.defaultRunnerFactory = new LayoutRunnerFactory();
    }

}

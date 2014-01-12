package org.datagr4m.trials.drawing.demo01.topologies;

import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunnerFactory;
import org.datagr4m.drawing.layout.runner.stop.MaxStepCriteria;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.io.TopologyIOXML;
import org.datagr4m.trials.drawing.DisplayInitilizer;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.keyboard.PrintScreenObfuscator;
import org.datagr4m.workspace.IWorkspaceController;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;
import org.datagr4m.workspace.factories.IDatagr4mFactories;

public class DemoTopologyXML {
	protected INavigationController navigationController;
	protected IWorkspaceController dataController;
	protected Display display;
	protected PrintScreenObfuscator pso;
	
    protected IDatagr4mFactories factories;

    public static void main(String[] args) throws Exception{
		TopologyIOXML xmlt = new TopologyIOXML();
		Topology<IPropertyNode, IPropertyEdge> topology  = xmlt.loadTopology("data/workspaces/xmltopo/topology.xml");

		topology.toConsole();

		
        Workspace.defaultRunnerFactory = new LayoutRunnerFactory();
        Workspace w = new Workspace(topology);
		
        // TODO test: assert there are as many edge in geometry as in topology
        w.getEdgeModel().toConsole();
        
        // TODO test: assert root layout has tubeModel and tubeLayout defined
        // otherwise no path computed
        System.out.println("edge model:"+w.getNodeLayout().getTubeModel());
        System.out.println("edge layout:"+w.getNodeLayout().getEdgeLayout());
        // TODO test: verifier couverture des tests sur tube in datagr4m-drawing/org.datagr4m.tests.drawing.tubes.data
        
		show(w);
		
        // TODO test: assert that path are not empty (have been computed)

    }

    public static void show(Workspace w) {
        DisplayInitilizer di = new DisplayInitilizer(EdgeComputationPolicy.ALWAYS, EdgeRenderingPolicy.ALWAYS, ViewPolicy.AUTOFIT_AT_RUN);
		di.init(w).openFrame();
		
		// TODO test: add test on mean move criteria (Here we never stop!!)
		ILayoutRunner runner = w.getRunner();
		//runner.getConfiguration().getSequence()runner.getConfiguration().getSequence().setFirstPhaseBreakCriteria(new MeanMoveCriteria(1000));
		runner.getConfiguration().getSequence().setFirstPhaseBreakCriteria(new MaxStepCriteria(100));
        
		runner.start();
    }
}

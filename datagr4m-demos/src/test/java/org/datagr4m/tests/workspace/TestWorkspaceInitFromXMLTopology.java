package org.datagr4m.tests.workspace;

import java.util.List;

import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunnerFactory;
import org.datagr4m.drawing.layout.runner.sequence.LayoutRunnerSequenceSinglePhase;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MaxStepCriteria;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemLabelFinder;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.io.TopologyIOXML;
import org.datagr4m.trials.drawing.DisplayInitilizer;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;
import org.junit.Assert;
import org.junit.Test;

/**
 * Integration test: load an XML topology and verify that the workspace has the
 * appropriate data model.
 * 
 * To ease debugging, one may use: w.getTopology().toConsole();
 * w.getEdgeModel().toConsole();
 * 
 */
public class TestWorkspaceInitFromXMLTopology{
    public static void main(String[] str) throws Exception {
        TestWorkspaceInitFromXMLTopology t = new TestWorkspaceInitFromXMLTopology();
        t.loadAndTest(false, -1);
    }

    @Test
    public void testLoad() throws Exception {
        loadAndTest(true, 10);
    }

    public void loadAndTest(boolean test, int waitFor) throws Exception {
        TopologyIOXML xmlt = new TopologyIOXML();
        Topology<IPropertyNode, IPropertyEdge> topology = xmlt.loadTopology("src/test/resources/topology.xml");
        // topology.toConsole();
        if (test)
            assertTopology(topology, 14, 19, 3, 2);

        Workspace w = new Workspace(topology);
        // w.getEdgeModel().toConsole();
        if (test) {
            assertWorkspaceNotNull(w);
            assertDrawingModel(w);
            assertEdgeModel(w);
        }

        display(w);
        
        //new MeanMoveCriteria(1, 10));
        IBreakCriteria criteria = new MaxStepCriteria(300);
        runAndTest(test, waitFor, w, criteria);
        
        
        // final tests
        if (test) {
            assertPathes(w);
        }
    }

    private void runAndTest(boolean test, int waitFor, Workspace w, IBreakCriteria criteria) throws Exception {
        ILayoutRunner runner = w.getRunner();
        
        runner.getConfiguration().setAllowAutoFitAtStepEnd(true);
        LayoutRunnerSequenceSinglePhase seq = (LayoutRunnerSequenceSinglePhase)runner.getConfiguration().getSequence();
        seq.setFirstPhaseBreakCriteria(criteria);
        
        if (test && waitFor > 0)
            ((LayoutRunner) runner).startAndAwaitAtMost(waitFor);
        else
            runner.start();
    }
    public void assertInterface(Workspace w) {
        Assert.assertFalse(checkItemHasInterface(w.getModel(), "rt1", "InterfaceNotDeclaredInXml"));

        Assert.assertTrue(checkItemHasInterface(w.getModel(), "fw1", "Interface1"));
        Assert.assertTrue(checkItemHasInterface(w.getModel(), "fw1", "Interface2"));
        Assert.assertTrue(checkItemHasInterface(w.getModel(), "fw2", "Interface1"));
        Assert.assertTrue(checkItemHasInterface(w.getModel(), "fw2", "Interface2"));
        
        Assert.assertTrue(checkItemHasInterface(w.getModel(), "rt1", "Interface1"));
        Assert.assertTrue(checkItemHasInterface(w.getModel(), "rt3", "Interface1"));
        
        Assert.assertTrue(checkItemHasInterface(w.getModel(), "rt2", "Interface1"));
        Assert.assertTrue(checkItemHasInterface(w.getModel(), "rt2", "Interface2"));
    }
    
    public void assertPathes(Workspace w) {
        Tube tube1 = w.getEdgeModel().getRootTubes().get(0);
        Assert.assertTrue(tube1.getPathGeometry().getPointNumber() > 0);
        
        Tube tube2 = (Tube) tube1.getChildren().get(0);
        Assert.assertEquals("tube at level 1 has N children edge", 12, tube2.getChildren().size());
        for(IEdge edge: tube2.getChildren())
            Assert.assertTrue(edge.getPathGeometry().getPointNumber() > 0);
    }

    public void assertEdgeModel(Workspace w) {
        Assert.assertEquals("model contains two tubes at level 0", w.getEdgeModel().getRootTubes().size(), 2);

        Tube tube1 = w.getEdgeModel().getRootTubes().get(0);
        Assert.assertEquals("tube at level 0 has N children tube", 1, tube1.getChildren().size());

        Tube tube2 = (Tube) tube1.getChildren().get(0);
        Assert.assertEquals("tube at level 1 has N children edge", 12, tube2.getChildren().size());
    }

    public void assertTopology(Topology<IPropertyNode, IPropertyEdge> topology, int vertexCount, int edgeCount, int groupCount, int depth) {
        Assert.assertEquals(vertexCount, topology.getGraph().getVertexCount());
        Assert.assertEquals(edgeCount, topology.getGraph().getEdgeCount());
        Assert.assertEquals(groupCount, topology.getGroups().size());
        Assert.assertEquals(depth, topology.getDepth());
    }

    public void assertWorkspaceNotNull(Workspace w) {
        Assert.assertNotNull("has item model", w.getModel());
        Assert.assertNotNull("has edge model", w.getEdgeModel());
        Assert.assertNotNull("has layout", w.getLayout());
        Assert.assertNotNull("has layout with reference on edge model", w.getLayout().getTubeModel());
        Assert.assertNotNull("has layout with an edge layout", w.getLayout().getTubeLayout());
    }

    private void assertDrawingModel(Workspace w) {
        HierarchicalGraphModel root = (HierarchicalGraphModel)w.getModel();
        Assert.assertEquals("root", w.getModel().getLabel());
        Assert.assertEquals(3, w.getModel().getChildren().size());

        Assert.assertTrue(root.getChildren().get(0) instanceof HierarchicalPairModel);

        HierarchicalGraphModel graph = (HierarchicalGraphModel)w.getModel().getChildren().get(1);
        Assert.assertEquals("routers", graph.getChildren().get(0).getLabel());
        Assert.assertEquals("servers", graph.getChildren().get(1).getLabel());
    }
    
    public boolean checkItemHasInterface(IHierarchicalModel model, String itemToFind, Object interfaceToFind) {
        ItemLabelFinder finder = new ItemLabelFinder();
        List<IBoundedItem> results = finder.find(itemToFind, model);
        if(results.size()>0){
            IBoundedItem i = results.get(0);
            for(SlotGroup slotGroup: i.getSlotGroups()){
                for(SlotTarget slotTarget: slotGroup.getAllSlotTargets().values()){
                    if(slotTarget.getInterface().equals(interfaceToFind))
                        return true;
                }
            }
        }
        return false;
    }

    public void display(Workspace w) {
        DisplayInitilizer di = new DisplayInitilizer(EdgeComputationPolicy.ALWAYS, EdgeRenderingPolicy.ALWAYS, ViewPolicy.AUTOFIT_AT_RUN);
        di.init(w).openFrame();
    }

    {
        Workspace.defaultRunnerFactory = new LayoutRunnerFactory();
    }

}

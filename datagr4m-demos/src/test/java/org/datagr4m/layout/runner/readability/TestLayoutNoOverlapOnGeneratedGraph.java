package org.datagr4m.layout.runner.readability;

import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;
import org.datagr4m.trials.drawing.DisplayLauncher;
import org.datagr4m.viewer.IDisplay;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;
/**
 * 
 * TODO : fix bug : never stops
 */
public class TestLayoutNoOverlapOnGeneratedGraph extends AbstractLayoutRunnerTest {
@Ignore
    @Test
    public void testNoOverlap() throws Exception{
        Topology<String, String> topo = buildTopology();
        HierarchicalGraphModel model = buildModel(topo);
        HierarchicalGraphLayout layout = buildLayout(model);
        display(model, layout);
        Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(model);
        
        Assert.assertTrue("no item overlap any other", overlapping.size()==0);
    }

    private void display(HierarchicalGraphModel model, HierarchicalGraphLayout layout) throws Exception {
        IDisplay d = DisplayLauncher.display(model);
        final LayoutRunner runner = new LayoutRunner(layout, d.getView());
        runner.startAndAwaitAtMost(1);
    }

    private HierarchicalGraphModel buildModel(Topology<String, String> topo) {
        IHierarchicalModelFactory factory = new HierarchicalTopologyModelFactory<String, String>();
        HierarchicalGraphModel model = (HierarchicalGraphModel) factory.getLayoutModel(topo);
        return model;
    }

    private Topology<String, String> buildTopology() {
        Topology<String, String> topo = new Topology<String, String>();
        Graph<String, String> graph = topo.getGraph();
        graph.addVertex("d1");
        graph.addVertex("d2");
        graph.addVertex("d3");
        graph.addVertex("d4");
        graph.addEdge("1>3", "d1", "d3");
        graph.addEdge("1>2", "d1", "d2");
        Group<String> g1 = new Group<String>("g1");
        g1.add("d1");
        g1.add("d2");
        Group<String> g2 = new Group<String>("g2");
        g2.add("d3");
        g2.add("d4");
        topo.getGroups().add(g1);
        topo.getGroups().add(g2);
        topo.index();
        return topo;
    }
}

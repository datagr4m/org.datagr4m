package org.datagr4m.tests.layout.runner.readability;

import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.tests.drawing.AbstractLayoutRunnerTest;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;
import org.datagr4m.trials.drawing.DisplayLauncher;
import org.datagr4m.viewer.IDisplay;

import edu.uci.ics.jung.graph.Graph;

public class TestLayoutNoOverlapOnGeneratedGraph extends AbstractLayoutRunnerTest {
    public void testNoOverlap() throws Exception{
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

        IHierarchicalModelFactory factory = new HierarchicalTopologyModelFactory<String, String>();
        HierarchicalGraphModel model = (HierarchicalGraphModel) factory.getLayoutModel(topo);
        HierarchicalGraphLayout layout = getInitializedLayout(model);
        
        IDisplay d = DisplayLauncher.display(model);
        final LayoutRunner runner = new LayoutRunner(layout, d.getView());
        runner.startAndAwaitAtMost(10);

        
        Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(model);
        
        
        
        assertTrue("no item overlap any other", overlapping.size()==0);
    }

    

    
}

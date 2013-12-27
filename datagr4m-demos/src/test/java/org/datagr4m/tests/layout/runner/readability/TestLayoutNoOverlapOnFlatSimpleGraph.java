package org.datagr4m.tests.layout.runner.readability;

import java.util.List;
import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.topology.Topology;

import edu.uci.ics.jung.graph.Graph;

public class TestLayoutNoOverlapOnFlatSimpleGraph extends AbstractLayoutRunnerTest {
    public void testNoOverlap() throws Exception{
        HierarchicalGraphModel model = buildModel();
        HierarchicalGraphLayout layout = buildLayout(model);

        final LayoutRunner runner = new LayoutRunner(layout);

        runner.startAndAwaitAtMost(10);
        
        List<IBoundedItem> candidates = model.getChildren();
        Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(candidates);
        
        assertTrue("no item overlap any other", overlapping.size()==0);
    }
    
    public HierarchicalGraphModel buildModel() {
        Topology<String, String> topo = new Topology<String, String>();
        Graph<String, String> graph = topo.getGraph();

        graph.addVertex("d1");
        graph.addVertex("d2");
        graph.addVertex("d3");
        graph.addVertex("d4");
        graph.addEdge("1>3", "d1", "d3");
        graph.addEdge("1>2", "d1", "d2");

        //System.out.println(graph.getEdges());
        
        topo.index();
topo.toConsole();
        // make a model with forces
        IHierarchicalModelFactory factory = new HierarchicalTopologyModelFactory<String, String>();
        return (HierarchicalGraphModel) factory.getLayoutModel(topo);
    }
}

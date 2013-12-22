package org.datagr4m.tests.layout.runner.readability;

import java.util.List;
import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.QualityScores;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.tests.drawing.AbstractLayoutRunnerTest;
import org.datagr4m.tests.drawing.forces.TestForcesOnAFlatSimpleGraph;

public class TestLayoutNoOverlapOnFlatSimpleGraph extends AbstractLayoutRunnerTest {
    public void testNoOverlap() throws Exception{
        HierarchicalGraphModel model = TestForcesOnAFlatSimpleGraph.buildGraphNested();
        HierarchicalGraphLayout layout = getInitializedLayout(model);

        final LayoutRunner runner = new LayoutRunner(layout);

        runner.startAndAwaitAtMost(10);
        
        List<IBoundedItem> candidates = model.getChildren();
        Set<CommutativePair<IBoundedItem>> overlapping = QualityScores.countOverlappingItems(candidates);
        
        assertTrue("no item overlap any other", overlapping.size()==0);
    }
}

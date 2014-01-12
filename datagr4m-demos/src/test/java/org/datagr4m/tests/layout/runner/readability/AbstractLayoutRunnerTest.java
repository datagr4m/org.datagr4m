package org.datagr4m.tests.layout.runner.readability;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;


public class AbstractLayoutRunnerTest {
    protected HierarchicalGraphLayout buildLayout(IHierarchicalNodeModel model) {
        IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        HierarchicalGraphLayout layout = (HierarchicalGraphLayout)layoutFactory.getLayout(model);
        layout.initAlgo();
        return layout;
    }
}

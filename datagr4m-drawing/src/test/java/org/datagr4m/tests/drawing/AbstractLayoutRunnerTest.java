package org.datagr4m.tests.drawing;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;

import junit.framework.TestCase;


public class AbstractLayoutRunnerTest extends TestCase{
    protected static HierarchicalGraphLayout getInitializedLayout(IHierarchicalModel model) {
        IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        HierarchicalGraphLayout layout = (HierarchicalGraphLayout)layoutFactory.getLayout(model);
        layout.initAlgo();
        return layout;
    }
}

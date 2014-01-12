package org.datagr4m.tests.drawing;

import junit.framework.TestCase;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;


public class AbstractLayoutRunnerTest extends TestCase{
    protected static HierarchicalGraphLayout getInitializedLayout(IHierarchicalNodeModel model) {
        IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        HierarchicalGraphLayout layout = (HierarchicalGraphLayout)layoutFactory.getLayout(model);
        layout.initAlgo();
        return layout;
    }
    
    public void testNothing(){}
}

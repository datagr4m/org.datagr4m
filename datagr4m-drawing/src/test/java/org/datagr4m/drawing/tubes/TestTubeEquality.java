package org.datagr4m.drawing.tubes;

import java.io.IOException;

import junit.framework.TestCase;

import org.datagr4m.drawing.model.items.BoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;


public class TestTubeEquality extends TestCase{
    public void testIt() throws IOException, ClassNotFoundException{
        
        HierarchicalGraphModel i1 = new HierarchicalGraphModel();
        i1.addChild(new BoundedItem("item1"));
        HierarchicalGraphModel i2 = new HierarchicalGraphModel();
        i2.addChild(new BoundedItem("item2"));
        
        Tube t1 = new Tube(i1, i2, new PathFactory());
        Tube t2 = new Tube(i2, i1, new PathFactory());
        
        assertTrue(t1.equals(t2));

    }
}

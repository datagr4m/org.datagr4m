package org.datagr4m.drawing.tubes;

import java.io.IOException;

import junit.framework.TestCase;

import org.datagr4m.drawing.model.items.BoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.Edge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.TubeUtils;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;


public class TestTubeFlipping extends TestCase{
    public void testIt() throws IOException, ClassNotFoundException{
        IBoundedItem i1 = new BoundedItem("item1");
        IBoundedItem i2 = new BoundedItem("item2");
        
        HierarchicalGraphModel g1 = new HierarchicalGraphModel();
        g1.addChild(i1);
        HierarchicalGraphModel g2 = new HierarchicalGraphModel();
        g2.addChild(i2);
        
        //IEdge
        
        Tube t1 = new Tube(g1, g2, new PathFactory());
        t1.setFlipped(true);
        Edge e1 = new Edge(i2, i1, TubeUtils.buildObstacle(i2), TubeUtils.buildObstacle(i1));
        t1.addChild(e1);
        
        assertTrue(e1.getParent()==t1);
        
        assertTrue(t1.isFlipped());
        assertFalse(e1.isFlipped());
        assertFalse(t1.isParentFlipped());
        assertTrue(e1.isParentFlipped());
    }
}

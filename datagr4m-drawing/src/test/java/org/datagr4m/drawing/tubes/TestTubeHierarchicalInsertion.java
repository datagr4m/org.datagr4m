package org.datagr4m.drawing.tubes;

import junit.framework.TestCase;

import org.datagr4m.drawing.layout.hierarchical.graph.edges.HierarchicalEdgeLayout;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.tubes.data.HsrpFullMeshDataTestWithInsertion;


public class TestTubeHierarchicalInsertion extends TestCase{
    public void testHSRPFullMeshTubeBuild() throws Exception{
        HsrpFullMeshDataTestWithInsertion testData = new HsrpFullMeshDataTestWithInsertion();
        testData.makeTestLayout();
        
        
        Tube t = testData.getFirstTube();
        
        // FIXME!
        //t.insertPointHierarchicallyAfter(0, new Point2D.Double(0,0));
        
        //testData.hierarchicalInsertPointOnTube(t, new Point2D.Double(0,0));
        HierarchicalEdgeLayout layout = new HierarchicalEdgeLayout();
        layout.build(testData.edges);
    }
    
    
}

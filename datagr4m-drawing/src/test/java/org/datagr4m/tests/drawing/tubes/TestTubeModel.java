package org.datagr4m.tests.drawing.tubes;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.tests.drawing.tubes.data.HsrpFullMeshDataTest;

import junit.framework.TestCase;


public class TestTubeModel extends TestCase{
    public void testHSRPFullMeshTubeBuild(){
        HsrpFullMeshDataTest testData = new HsrpFullMeshDataTest();
        IHierarchicalEdgeModel model = testData.getEdgeModel();
        
        // ---------------------
        // verify internal
        IEdge p = model.getInternalTubesAndEdges().get(0);
        assertTrue(p.getSourceItem()==testData.i11);
        assertTrue(p.getTargetItem()==testData.i12);
        
        p = model.getInternalTubesAndEdges().get(1);
        assertTrue(p.getSourceItem()==testData.i21);
        assertTrue(p.getTargetItem()==testData.i22);
        
        // this tube is internal since the two pairs are in a group
        Tube t = (Tube)model.getInternalTubesAndEdges().get(2);
        assertTrue(t.getChildren().get(0).getSourceItem()==testData.i11);
        assertTrue(t.getChildren().get(0).getTargetItem()==testData.i21);
        
        assertTrue(t.getChildren().get(3).getSourceItem()==testData.i12);
        assertTrue(t.getChildren().get(3).getTargetItem()==testData.i22);
        
        //model.toConsole();
        //System.out.println("----------");
    }
}

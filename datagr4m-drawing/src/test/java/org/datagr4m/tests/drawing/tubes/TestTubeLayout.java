package org.datagr4m.tests.drawing.tubes;

import org.datagr4m.drawing.layout.hierarchical.graph.edges.HierarchicalEdgeLayout;
import org.datagr4m.tests.drawing.tubes.data.HsrpFullMeshDataTest;

import junit.framework.TestCase;


public class TestTubeLayout extends TestCase{
    public void testHSRPFullMeshTubeBuild(){
        HsrpFullMeshDataTest testData = new HsrpFullMeshDataTest();
        
        HierarchicalEdgeLayout layout = new HierarchicalEdgeLayout();
        layout.build(testData.edges);
        
        // garantir qu'il n'y a une seule direction
        
        //testData.edges.toConsole();
        //System.out.println("----------");
    }
}

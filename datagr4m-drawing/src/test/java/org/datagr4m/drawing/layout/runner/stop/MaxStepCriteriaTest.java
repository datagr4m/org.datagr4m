package org.datagr4m.drawing.layout.runner.stop;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;


public class MaxStepCriteriaTest {
    @Test
    public void testMaxSteps(){
        IHierarchicalNodeLayout mockLayout = mockLayoutCounter(3);
        
        MaxStepCriteria c = new MaxStepCriteria(2);
        Assert.assertFalse(c.shouldBreak(mockLayout)); // non : 0 > 2
        Assert.assertFalse(c.shouldBreak(mockLayout)); // non : 1 > 2
        Assert.assertFalse(c.shouldBreak(mockLayout)); // non : 2 > 2
        Assert.assertTrue(c.shouldBreak(mockLayout));  // oui : 3 > 2
    }

    private IHierarchicalNodeLayout mockLayoutCounter(int count) {
        BoundedForceAtlasLayout mockDelegate = EasyMock.createMock(BoundedForceAtlasLayout.class);
        
        for (int i = 0; i <= count; i++) {
            EasyMock.expect(mockDelegate.getCounter()).andReturn(i).once();
        }
        
        IHierarchicalNodeLayout mockLayout = EasyMock.createMock(IHierarchicalNodeLayout.class);
        EasyMock.expect(mockLayout.getDelegate()).andReturn(mockDelegate).anyTimes();
        EasyMock.replay(mockLayout, mockDelegate);
        return mockLayout;
    }
}

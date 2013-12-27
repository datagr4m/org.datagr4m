package org.datagr4m.drawing.layout.runner.stop;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;


public class MaxStepCriteriaTest {
    @Test
    public void testMaxSteps(){
        IHierarchicalLayout mockLayout = EasyMock.createMock(IHierarchicalLayout.class);
        BoundedForceAtlasLayout mockDelegate = EasyMock.createMock(BoundedForceAtlasLayout.class);
        EasyMock.expect(mockLayout.getDelegate()).andReturn(mockDelegate).anyTimes();
        EasyMock.expect(mockDelegate.getCounter()).andReturn(0).once();
        EasyMock.expect(mockDelegate.getCounter()).andReturn(1).once();
        EasyMock.expect(mockDelegate.getCounter()).andReturn(2).once();
        EasyMock.expect(mockDelegate.getCounter()).andReturn(3).once();
        EasyMock.replay(mockLayout, mockDelegate);
        
        MaxStepCriteria c = new MaxStepCriteria(2);
        Assert.assertFalse(c.shouldBreak(mockLayout)); // non : 0 > 2
        Assert.assertFalse(c.shouldBreak(mockLayout)); // non : 1 > 2
        Assert.assertFalse(c.shouldBreak(mockLayout)); // non : 2 > 2
        Assert.assertTrue(c.shouldBreak(mockLayout));  // oui : 3 > 2
    }
}

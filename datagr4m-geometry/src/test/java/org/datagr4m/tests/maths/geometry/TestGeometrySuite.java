package org.datagr4m.tests.maths.geometry;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestGeometrySuite {
    public static Test suite() {
        System.err.println("test incomplets!!");
        
        TestSuite suite = new TestSuite("Test com.netlight.geometry");
        
        // path finder retest!
        suite.addTestSuite(TestIntersectionUtils.class);
        suite.addTestSuite(TestLinearFunction.class);
        suite.addTestSuite(TestRectangleUtils.class);
        
        
        //suite.addTestSuite(TestPointUtils.class);
        
        return suite;
    }
}

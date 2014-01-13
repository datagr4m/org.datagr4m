package org.datagr4m.drawing.model.pathfinder;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.datagr4m.drawing.model.pathfinder.impl.TestPathFinderPrimitives;
import org.datagr4m.drawing.model.pathfinder.slots.TestSlotTarget;


public class TestPathfinderSuite {
    public static Test suite() {
        System.err.println("missing tests on links equality");
        System.err.println("missing tests on pathfinder results");
        System.err.println("missing tests slot layout results");
        
        TestSuite suite = new TestSuite("Test com.netlight.pathfinder");
        suite.addTestSuite(TestPath.class);
        suite.addTestSuite(TestPathHistory.class);
        suite.addTestSuite(TestPathFinderPrimitives.class);
        suite.addTestSuite(TestSlotTarget.class);
        return suite;
    }
}

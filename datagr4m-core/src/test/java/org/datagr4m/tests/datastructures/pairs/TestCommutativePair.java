package org.datagr4m.tests.datastructures.pairs;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.datastructures.pairs.CommutativePair;

import junit.framework.TestCase;


public class TestCommutativePair extends TestCase {
    public void testPair(){
        CommutativePair<Integer> cp1 = new CommutativePair<Integer>(1, 3);
        CommutativePair<Integer> cp2 = new CommutativePair<Integer>(3, 1);
        CommutativePair<Integer> cp3 = new CommutativePair<Integer>(1, 4);
        CommutativePair<Integer> cp4 = new CommutativePair<Integer>(2, 3);
        CommutativePair<Integer> cp5 = new CommutativePair<Integer>(1, 3);
        
        assertTrue(cp1.equals(cp2));
        assertTrue(cp2.equals(cp1));
        assertTrue(cp1.equals(cp5));

        assertFalse(cp1.equals(cp3));
        assertFalse(cp1.equals(cp4));
        assertFalse(cp3.equals(cp1));
        assertFalse(cp4.equals(cp1));

        List<CommutativePair<Integer>> pairs = new ArrayList<CommutativePair<Integer>>();
        
        pairs.add(cp1);
        assertTrue(pairs.contains(cp1));
        assertTrue(pairs.contains(cp2));
        assertFalse(pairs.contains(cp3));
    }
}

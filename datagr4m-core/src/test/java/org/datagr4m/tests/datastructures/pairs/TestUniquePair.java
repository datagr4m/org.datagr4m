package org.datagr4m.tests.datastructures.pairs;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.datastructures.pairs.UniquePair;

import junit.framework.TestCase;


public class TestUniquePair extends TestCase{
    public void testUniquePair(){
        UniquePair<Integer> cp1 = new UniquePair<Integer>(1, 3);
        UniquePair<Integer> cp2 = new UniquePair<Integer>(3, 1);
        UniquePair<Integer> cp5 = new UniquePair<Integer>(1, 3);
        
        // ensure that common value do not imply object equality
        assertFalse(cp1.equals(cp2));
        assertFalse(cp2.equals(cp1));
        assertFalse(cp1.equals(cp5));

        List<UniquePair<Integer>> pairs = new ArrayList<UniquePair<Integer>>();
        
        pairs.add(cp1);
        assertTrue(pairs.contains(cp1));
        assertFalse(pairs.contains(cp5));
    }
}

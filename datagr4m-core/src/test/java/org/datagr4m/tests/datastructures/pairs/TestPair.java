package org.datagr4m.tests.datastructures.pairs;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.datagr4m.datastructures.pairs.Pair;


public class TestPair extends TestCase {
    public void testPair(){
        Pair<Integer,Integer> cp1 = new Pair<Integer,Integer>(1, 3);
        Pair<Integer,Integer> cp2 = new Pair<Integer,Integer>(3, 1);
        Pair<Integer,Integer> cp3 = new Pair<Integer,Integer>(1, 4);
        Pair<Integer,Integer> cp4 = new Pair<Integer,Integer>(2, 3);
        Pair<Integer,Integer> cp5 = new Pair<Integer,Integer>(1, 3);
        
        assertTrue(cp1.equals(cp5));

        assertFalse(cp1.equals(cp2));
        assertFalse(cp2.equals(cp1));
        assertFalse(cp1.equals(cp3));
        assertFalse(cp1.equals(cp4));
        assertFalse(cp3.equals(cp1));
        assertFalse(cp4.equals(cp1));

        List<Pair<Integer,Integer>> pairs = new ArrayList<Pair<Integer,Integer>>();
        
        pairs.add(cp1);
        assertTrue(pairs.contains(cp1));
        assertTrue(pairs.contains(cp5));
    }
}

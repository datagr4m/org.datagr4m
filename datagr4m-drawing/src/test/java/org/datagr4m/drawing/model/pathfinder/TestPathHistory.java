package org.datagr4m.drawing.model.pathfinder;

import junit.framework.TestCase;

import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;
import org.datagr4m.drawing.model.pathfinder.path.PathEditHistory;
import org.datagr4m.drawing.model.pathfinder.path.PathEditLog;


public class TestPathHistory extends TestCase{
    public void testEquality(){
        IPath path = new LockablePath();
        path.add(0, 0, true);
        path.add(1, 0, true);
        path.add(1, 5, true);
        path.add(3, 5, true);
        path.add(3, 0, true);
        
        IPath identical = new LockablePath(); // the same
        identical.add(0, 0, true);
        identical.add(1, 0, true);
        identical.add(1, 5, true);
        identical.add(3, 5, true);
        identical.add(3, 0, true);

        IPath lockdiff = new LockablePath();
        lockdiff.add(0, 0, true);
        lockdiff.add(1, 0, true);
        lockdiff.add(1, 5, false); // lock difference
        lockdiff.add(3, 5, true);
        lockdiff.add(3, 0, true);
        
        IPath valuediff = new LockablePath(); 
        valuediff.add(0, -1, true); // value difference
        valuediff.add(1, 0, true);
        valuediff.add(1, 5, true);
        valuediff.add(3, 5, true);
        valuediff.add(3, 0, true);
        
        PathEditLog log1 = new PathEditLog(9, 10, path);
        PathEditLog log2 = new PathEditLog(9, 11, path);
        PathEditLog log3 = new PathEditLog(9, 10, lockdiff);
        PathEditLog log4 = new PathEditLog(9, 10, identical);
        PathEditLog log5 = new PathEditLog(9, 10, path);
        
        PathEditHistory history = new PathEditHistory();
        assertTrue(history.getMaximumSize()==PathEditHistory.DEFAULT_MAX_SIZE);
        history.add(log1);
        assertTrue(history.exists(log1));
        assertTrue(history.exists(log4));
        assertTrue(history.exists(log5));
        assertFalse(history.exists(log2));
        assertFalse(history.exists(log3));
        
        history.add(log1);
        history.add(log2);
        history.add(log3);
        history.add(log4);
        history.add(log5);
        history.add(log5);
        history.add(log5);
        
        assertTrue(history.getCurrentSize()<=history.getMaximumSize());
    }
}

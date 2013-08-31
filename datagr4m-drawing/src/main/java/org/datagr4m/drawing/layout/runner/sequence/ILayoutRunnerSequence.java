package org.datagr4m.drawing.layout.runner.sequence;

import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;


public interface ILayoutRunnerSequence extends Runnable{
    public ILayoutRunner getRunner();
    public void setRunner(ILayoutRunner runner);
    
    public IBreakCriteria getFirstPhaseBreakCriteria();
    public void setFirstPhaseBreakCriteria(IBreakCriteria criteria);
}

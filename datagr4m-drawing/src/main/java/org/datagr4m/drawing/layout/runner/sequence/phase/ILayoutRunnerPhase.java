package org.datagr4m.drawing.layout.runner.sequence.phase;

import org.datagr4m.drawing.layout.runner.ILayoutRunner;

public interface ILayoutRunnerPhase {
    public void execute(ILayoutRunner runner);
    public void execute(ILayoutRunner runner, boolean stopThreadWhenDone);
}
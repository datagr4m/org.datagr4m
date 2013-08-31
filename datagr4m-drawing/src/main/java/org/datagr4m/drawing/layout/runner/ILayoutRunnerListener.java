package org.datagr4m.drawing.layout.runner;

import org.datagr4m.drawing.layout.runner.sequence.ILayoutRunnerSequence;

public interface ILayoutRunnerListener {
    /** Called when the runner has its {@link start()} method called, or {@link setRunning(true, true)}.*/
    public void runnerStarted();
    /** Called when the runner has its {@link stop()} method called, or {@link setRunning(false, true)}.*/
    public void runnerStopped();
    /** Called when the runner thread finishes a job (basically an {@link ILayoutRunnerSequence}.*/
    public void runnerFinished();
    /** Called when the runner thread fails to complete a job.*/
    public void runnerFailed(String message, Exception e);
}

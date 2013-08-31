package org.datagr4m.drawing.layout.runner.sequence.phase;

import org.datagr4m.drawing.layout.runner.ILayoutRunner;

public class LayoutRunnerPhaseShrink extends LayoutRunnerPhaseDefault{
    protected double shrinkRatio = 1;
    
    public LayoutRunnerPhaseShrink() {
        this(0.1);
    }
    
    
    public LayoutRunnerPhaseShrink(double shrinkRatio) {
        this.shrinkRatio = shrinkRatio;
    }

    @Override
    public void execute(ILayoutRunner runner, boolean stopThreadWhenDone) {
        //LayoutRunnerConfiguration settings = runner.getConfiguration();
        runner.repulsionMultiplyAllBy(shrinkRatio);
        super.execute(runner, stopThreadWhenDone);
    }
}

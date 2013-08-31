package org.datagr4m.drawing.layout.runner.sequence;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.sequence.phase.LayoutRunnerPhaseShrink;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MeanMoveCriteria;
import org.jzy3d.maths.TicToc;


public class LayoutRunnerSequenceTwoPhase extends LayoutRunnerSequenceSinglePhase{
    protected LayoutRunnerPhaseShrink shrinkPhase;
    protected boolean applyFinalShrink = false;
    
    protected IBreakCriteria secondPhaseBreakCriteria;

    public LayoutRunnerSequenceTwoPhase() {
        this(false);
    }

    public LayoutRunnerSequenceTwoPhase(boolean applyFinalShrink) {
        super();
        this.applyFinalShrink = applyFinalShrink;
        this.shrinkPhase = new LayoutRunnerPhaseShrink(0.0005);
    }

    
    public LayoutRunnerSequenceTwoPhase(ILayoutRunner runner) {
        super(runner);
        this.shrinkPhase = new LayoutRunnerPhaseShrink(0.0005);
        this.firstPhaseBreakCriteria = new MeanMoveCriteria(50);
        this.secondPhaseBreakCriteria = new MeanMoveCriteria(50, 100);
    }

	@Override
	public void run(){
        //throw new RuntimeException();

        
        TicToc clock = new TicToc();
        
        boolean stopThreadAfterDefault = false;
        if(!applyFinalShrink)
            stopThreadAfterDefault = true;
        
        // expand
        clock.tic();
        defaultPhase.init(runner, firstPhaseBreakCriteria);
        defaultPhase.execute(runner, stopThreadAfterDefault);
        Logger.getLogger(this.getClass()).info("done default " + clock.toc() + " s");
        
        // shrink
        if(applyFinalShrink){
	        clock.tic();
	        shrinkPhase.init(runner, secondPhaseBreakCriteria);
	        shrinkPhase.execute(runner, true);
	        Logger.getLogger(this.getClass()).info("done shrink " + clock.toc() + " s");
        }
        
        // edge
        if(!runner.getConfiguration().isDoRunEdge()){
            clock.tic();
            runner.oneEdgeStep();
            
            Logger.getLogger(this.getClass()).info("done edge " + clock.toc() + " s");            
        }
        
        // notify job finished
        runner.fireFinished();
    }

    public IBreakCriteria getSecondPhaseBreakCriteria() {
        return secondPhaseBreakCriteria;
    }

    public void setSecondPhaseBreakCriteria(IBreakCriteria secondPhaseBreakCriteria) {
        this.secondPhaseBreakCriteria = secondPhaseBreakCriteria;
    }
}

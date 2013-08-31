package org.datagr4m.drawing.layout.runner.sequence;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.sequence.phase.LayoutRunnerPhaseDefault;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MeanMoveCriteria;
import org.jzy3d.maths.TicToc;


/**
 * Only one phase that process a hierarchical model layout
 * @author Martin Pernollet
 */
public class LayoutRunnerSequenceSinglePhase implements ILayoutRunnerSequence{
    protected ILayoutRunner runner;
    protected LayoutRunnerPhaseDefault defaultPhase;
    protected IBreakCriteria firstPhaseBreakCriteria;
    
    public LayoutRunnerSequenceSinglePhase() {
        this.defaultPhase = new LayoutRunnerPhaseDefault();
        this.firstPhaseBreakCriteria = new MeanMoveCriteria(50);
    }
    
    public LayoutRunnerSequenceSinglePhase(ILayoutRunner runner) {
        this.runner = runner;
        this.defaultPhase = new LayoutRunnerPhaseDefault();
        this.firstPhaseBreakCriteria = new MeanMoveCriteria(50);
    }

    @Override
	public void run(){
        TicToc clock = new TicToc();

        // expand
        clock.tic();
        defaultPhase.init(runner, firstPhaseBreakCriteria);
        defaultPhase.execute(runner);
        Logger.getLogger(this.getClass()).info("done default " + clock.toc() + " s");
     
        // edge
        if(!runner.getConfiguration().isDoRunEdge()){
            clock.tic();
            runner.oneEdgeStep();
            
            Logger.getLogger(this.getClass()).info("done edge " + clock.toc() + " s");            
        }
        
        // notify job finished
        runner.fireFinished();
    }
    
    @Override
	public IBreakCriteria getFirstPhaseBreakCriteria(){
        return firstPhaseBreakCriteria;
    }

    @Override
	public void setFirstPhaseBreakCriteria(IBreakCriteria criteria){
        this.firstPhaseBreakCriteria = criteria;
    }

    
    @Override
    public ILayoutRunner getRunner() {
        return runner;
    }

    @Override
    public void setRunner(ILayoutRunner runner) {
        this.runner = runner;
    }
}

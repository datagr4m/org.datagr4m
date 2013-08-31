package org.datagr4m.drawing.animation;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutRunnerLookup;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.layout.runner.stop.MeanMoveCriteria;
import org.datagr4m.viewer.animation.AbstractAnimation;
import org.datagr4m.viewer.animation.IAnimation;


public class ForceLayoutAnimation extends AbstractAnimation implements IAnimation{
    public ForceLayoutAnimation(IHierarchicalLayout root){
        this(root, 10);
    }
    
    public ForceLayoutAnimation(IHierarchicalLayout root, double meanMoveThreshold){
        this(root, null);
        runner.getConfiguration().getSequence().setFirstPhaseBreakCriteria(new MeanMoveCriteria(meanMoveThreshold) {
            @Override
            public void onBreak() {
                finished = true;
                notifyAnimationFinished();
            }
        });
    }
    
    public ForceLayoutAnimation(IHierarchicalLayout root, IBreakCriteria criteria){
        super();
        runner = LayoutRunnerLookup.get(root);
        if(criteria!=null)
        	runner.getConfiguration().getSequence().setFirstPhaseBreakCriteria(criteria);
        started = false;
        finished = false;
    }
    
    @Override
    public boolean next() {
        if(!started){
            started = true;
            runner.start();
            finished = false;
        }
        return finished;
    }
    
    public void interrupt(){
        runner.stop();
        finished = true;
        notifyAnimationFinished();
    }
    
    public boolean finished(){
        return finished;
    }
    
    public ILayoutRunner getRunner() {
        return runner;
    }

    protected boolean started;
    protected boolean finished;
    protected ILayoutRunner runner;
}

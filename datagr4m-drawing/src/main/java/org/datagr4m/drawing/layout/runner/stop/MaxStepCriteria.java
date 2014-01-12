package org.datagr4m.drawing.layout.runner.stop;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;

/**
 * Allow to break when counter reach a given
 */
public class MaxStepCriteria implements IBreakCriteria{
    public MaxStepCriteria() {
        this(10);
    }
    
    public MaxStepCriteria(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    @Override
    public boolean shouldBreak(IHierarchicalNodeLayout layout) {
        if(layout.getDelegate()==null) // TODO : do not point on delegate but on MultiStepLayout.getCounter
            return false;
        
        if(layout.getDelegate().getCounter()>maxSteps)
            return true;
        else
            return false;
    }
    
    @Override 
    public void onBreak(){  
    }
        
    public int getMaxSteps() {
        return maxSteps;
    }



    protected int maxSteps = 10;
}

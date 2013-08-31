package org.datagr4m.drawing.layout.runner.stop;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;

public class MaxStepCriteria implements IBreakCriteria{
    public MaxStepCriteria() {
        this(10);
    }
    
    public MaxStepCriteria(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    @Override
    public boolean shouldBreak(IHierarchicalLayout layout) {
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

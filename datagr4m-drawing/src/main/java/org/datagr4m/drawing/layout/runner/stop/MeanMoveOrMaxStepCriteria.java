package org.datagr4m.drawing.layout.runner.stop;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;


public class MeanMoveOrMaxStepCriteria implements IBreakCriteria{
    public MeanMoveOrMaxStepCriteria(int maxSteps, double meanMoveThreshold) {
        this.meanMoveThreshold = meanMoveThreshold;
        this.maxSteps = maxSteps;
    }

    @Override
    public boolean shouldBreak(IHierarchicalLayout layout) {
        if(layout.getDelegate().getCounter()>maxSteps)
            return true;
        
        BoundedForceAtlasLayout f = layout.getDelegate();
        
        if(!f.isEnableMeanMoveLogging()){
            f.setEnableMeanMoveLogging(true);
        }
        
        double lastMeanPosition = f.getMeanMoveAnalysis().getLastMeanPositionChange();
        if(lastMeanPosition!=-1 && lastMeanPosition<meanMoveThreshold)
            return true;
        else
            return false;
    }
    
    @Override 
    public void onBreak(){  
    }

    
    protected double meanMoveThreshold = 10;
    protected int maxSteps = 10;
}

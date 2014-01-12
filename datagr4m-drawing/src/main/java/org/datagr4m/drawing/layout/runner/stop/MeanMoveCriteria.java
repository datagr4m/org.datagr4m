package org.datagr4m.drawing.layout.runner.stop;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.BoundedForceAtlasLayout;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;


/**
 * Requires a minimal mean move to continue processing.
 * 
 * Search the highest possible layout containing more than one item
 * (@see findHighestRelevantLayout}.
 * 
 * Support a number of starting inactive steps.
 * 
 * @author Martin Pernollet
 */
public class MeanMoveCriteria implements IBreakCriteria{
    public MeanMoveCriteria() {
        this(10);
    }
    
    public MeanMoveCriteria(double meanMoveThreshold) {
        this(meanMoveThreshold, 0);
    }
    
    public MeanMoveCriteria(double meanMoveThreshold, int nInactiveSteps) {
        this.nInactiveSteps = nInactiveSteps;
    }

    @Override
    public boolean shouldBreak(IHierarchicalNodeLayout layout) {
        if(currentInactiveSteps<nInactiveSteps){
            currentInactiveSteps++;
            return false;
        }
        else{
            BoundedForceAtlasLayout f = findHighestRelevantLayout(layout);
            if(f!=null){
                if(!f.isEnableMeanMoveLogging()){
                    f.setEnableMeanMoveLogging(true);
                    return false;
                }
                else{
                    double lastMeanMove = f.getMeanMoveAnalysis().getLastMeanPositionChange();
                    if(console)
                        Logger.getLogger(MeanMoveCriteria.class).info("lastMeanMove for " + f.getModel().getLabel() + " : " + lastMeanMove);
                    if(lastMeanMove!=-1 && lastMeanMove<meanMoveThreshold)
                        return true;
                    else
                        return false;
                }
            }
            return false;
        }
    }
    
    /**
     * To avoid inspecting a One-Element model (e.g. root) that is not modified
     * by its layout, we search the first model containing more than one element for inspection
     * @param layout
     * @return
     */
    protected BoundedForceAtlasLayout findHighestRelevantLayout(IHierarchicalNodeLayout layout){
        IHierarchicalNodeLayout first = findHighestRelevantLayoutInHierarchy(layout);
        return first.getDelegate();
    }
    
    protected IHierarchicalNodeLayout findHighestRelevantLayoutInHierarchy(IHierarchicalNodeLayout layout){
        if(layout./*getModel().*/getChildren().size()>0){
            try{
                IHierarchicalNodeLayout sublayout = layout.getChildren().get(0);
                return findHighestRelevantLayoutInHierarchy(sublayout);
            }
            catch(Exception e){
                System.err.println("Exception on " + layout);
                throw new RuntimeException(e);
            }
        }
        else
            return layout;
    }
    
    @Override 
    public void onBreak(){  
    	int a = 1;
    }

    
    protected boolean console = false;
    protected double meanMoveThreshold = 10;
    protected int nInactiveSteps = 0;
    protected int currentInactiveSteps = 0;
}

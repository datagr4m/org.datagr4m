package org.datagr4m.drawing.layout.runner.sequence.phase;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutRunnerConfiguration;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;


public class LayoutRunnerPhaseDefault implements ILayoutRunnerPhase {
    public void init(ILayoutRunner runner, IBreakCriteria criteria){
        runner.setBreakCriteria(criteria);
    }

    @Override
	public void execute(ILayoutRunner runner) {
        execute(runner, true);
    }
    
    /* (non-Javadoc)
     * @see com.netlight.layout.runner.ILayoutRunnerPhase#execute(com.netlight.layout.runner.LayoutRunner)
     */
    @Override
    public void execute(ILayoutRunner runner, boolean stopThreadWhenDone) {
        IHierarchicalLayout layout = runner.getLayout();
        IHierarchicalModel model = layout.getModel();
        IBreakCriteria criteria = runner.getBreakCriteria();
        LayoutRunnerConfiguration settings = runner.getConfiguration();
        
        //int n = 0;
        
        if(layout!=null){
            while (runner.isRunning()) {
                runner.visit(layout);
                // fit view
                if(settings.isAllowAutoFitAtStepEnd())
                    runner.autoFit(model);
                
                // break using criteria?
                if(criteria!=null)
                    if(criteria.shouldBreak(layout)){
                        criteria.onBreak();
                        
                        if(stopThreadWhenDone)
                           runner.setRunning(false, true);
                            // no using runner.stop because this destroy our thread :)
                        
                        break;
                    }
                
                // break because of ended algorithm
                if(!layout.canAlgo()){
                	if(stopThreadWhenDone)
                        runner.setRunning(false, true);	
                	break;
                }
            }
        }
    }
}

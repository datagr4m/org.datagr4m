package org.datagr4m.drawing.layout.runner;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.stop.IBreakCriteria;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.io.xml.generated.layout.Layout;
import org.datagr4m.viewer.IView;


public interface ILayoutRunner {
	public void configure(IHierarchicalLayout root);
	public void configure(IHierarchicalLayout root, IView view);
	public void configure(IHierarchicalLayout root, IView view,	LayoutRunnerConfiguration settings);
	
	public IHierarchicalLayout getLayout();
	
	public IView getView();
	
    public void start();
    public void stop();
    public void setRunning(boolean isRunning);
    public void setRunning(boolean isRunning, boolean notifyListeners);
    public boolean isRunning();
    
    public void oneEdgeStep();
    public void setDoRunEdge(boolean doRunEdge); 
    public boolean isDoRunEdge();    
    
    public LayoutRunnerConfiguration getConfiguration();
    
    public void addListener(ILayoutRunnerListener listener);
    public void removeListener(ILayoutRunnerListener listener);
    
    public void visit(IHierarchicalLayout root);
    public void autoFit(IHierarchicalModel model);
        
    public void repulsionMultiplyAllBy(double i);
    public void repulsionSetAllTo(double i);
    public void attractionMultiplyAllBy(double i);
    public void attractionSetAllTo(double i);
    
    public Layout getLayoutML();
    public void setLayoutML(Layout layoutML);
    
    // TODO: should not appear in interface
    public void fireFinished();
    
    @Deprecated
    public IBreakCriteria getBreakCriteria();
    @Deprecated
    public void setBreakCriteria(IBreakCriteria breakCriteria);
    

}

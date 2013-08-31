package org.datagr4m.workspace.configuration;

import org.datagr4m.drawing.layout.runner.LayoutRunnerConfiguration;
import org.datagr4m.drawing.layout.slots.SlotLayoutConfiguration;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRendererConfiguration;
import org.datagr4m.drawing.viewer.mouse.edges.MouseConfiguration;
import org.datagr4m.drawing.viewer.mouse.edges.MouseEdgeViewController;
import org.datagr4m.viewer.mouse.ILocalizedMouse;
import org.datagr4m.workspace.IWorkspace;


/**
 * Gather most of the API component configurations.
 * @author Martin Pernollet
 *
 */
public class ConfigurationFacade {
    public enum EdgeComputationPolicy{
        ALWAYS,
        COMPUTE_AT_END,
    }
    public enum EdgeRenderingPolicy{
        ALWAYS,
        ON_ROLL_OVER,
    }
    public enum ViewPolicy{
        AUTOFIT_AT_RUN,
        MANUAL_FIT
    }
    
    protected IWorkspace workspace;
    
    public ConfigurationFacade(IWorkspace workspace) {
        this.workspace = workspace;
    }
    
    public void apply(EdgeComputationPolicy policy){
        if(policy==EdgeComputationPolicy.ALWAYS){
            getLayoutRunnerConfiguration().setDoRunEdge(true);
            getMouseConfiguration().setEdgeRefreshAtDragEvent(true);
        }
        else if(policy==EdgeComputationPolicy.COMPUTE_AT_END){
            getLayoutRunnerConfiguration().setDoRunEdge(false);       
            getMouseConfiguration().setEdgeRefreshAtDragEvent(false);            
        }
    }
    
    public void apply(EdgeRenderingPolicy policy){
        if(policy==EdgeRenderingPolicy.ALWAYS){
            getTubeRendererConfiguration().setRenderEdgeOnlyOnMouseOver(false);
        }
        else if(policy==EdgeRenderingPolicy.ON_ROLL_OVER){
            getTubeRendererConfiguration().setRenderEdgeOnlyOnMouseOver(true);
        }
    }
    
    public void apply(ViewPolicy policy){
        if(policy==ViewPolicy.AUTOFIT_AT_RUN){
            getLayoutRunnerConfiguration().setAllowAutoFitAtStepEnd(true);
        }
        else if(policy==ViewPolicy.MANUAL_FIT){
            getLayoutRunnerConfiguration().setAllowAutoFitAtStepEnd(false);
        }
    }
    
    /* FACADE GETTERS */
    
    public LayoutRunnerConfiguration getLayoutRunnerConfiguration(){
        return workspace.getRunner().getConfiguration();
    }
    
    public TubeRendererConfiguration getTubeRendererConfiguration(){
        return workspace.getRenderer().getTubeRenderer().getConfiguration();
    }
    
    public MouseConfiguration getMouseConfiguration(){
        ILocalizedMouse mouse = workspace.getRunner().getView().getDisplay().getMouse();
        return ((MouseEdgeViewController)mouse).getConfiguration();
    }

    /** 
     * Apply the current configuration to all possible component
     * used to handle this workspace.
     */
    public void apply(IWorkspace workspace){
        
        
        
        // LayoutToolbar.plugWorkspace(...)
        // LayoutRunner constructor setDoRunEdge(true);
    }
    
    public void setEdgeAlways(){
        
    }
    
    // LayoutRunnerSequenceFactory
    // Run edge during default phase
    // Run edge during following phase

    LayoutRunnerConfiguration runner;
    MouseConfiguration mouse;
    // no update on drag
    
    
    SlotLayoutConfiguration slotLayout;
    // show unused interface (n2: no, n3: yes?)
    
    // factory configurations
    
    TubeRendererConfiguration tubeRenderer;
    // allow edge/tube on roll over only
}

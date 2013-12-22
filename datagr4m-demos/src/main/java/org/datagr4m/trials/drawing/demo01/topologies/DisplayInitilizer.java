package org.datagr4m.trials.drawing.demo01.topologies;

import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.layout.runner.sequence.LayoutRunnerSequenceTwoPhase;
import org.datagr4m.drawing.model.items.hierarchical.AbstractHierarchicalModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.NavigationController;
import org.datagr4m.drawing.viewer.mouse.edges.MouseEdgeViewController;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.mouse.AbstractMouseViewController;
import org.datagr4m.workspace.IWorkspace;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;

/**
 * Configure workspace, display, mouse, etc.
 * 
 * Constructor allow to setup parameters defining behaviour 
 * for edge management (when to compute/when to display)
 * 
 * @author Martin Pernollet
 */
public class DisplayInitilizer {
    EdgeComputationPolicy edgeComputationPolicy = EdgeComputationPolicy.COMPUTE_AT_END; // ALWAYS
    EdgeRenderingPolicy edgeRenderingPolicy = EdgeRenderingPolicy.ALWAYS; // ON_ROLL_OVER
    ViewPolicy viewFitPolicy = ViewPolicy.AUTOFIT_AT_RUN; // MANUAL_FIT
    
    public DisplayInitilizer() {
    }
    
    public DisplayInitilizer(EdgeComputationPolicy edgeComputationPolicy) {
        super();
        this.edgeComputationPolicy = edgeComputationPolicy;
    }

    public DisplayInitilizer(EdgeComputationPolicy edgeComputationPolicy, EdgeRenderingPolicy edgeRenderingPolicy, ViewPolicy viewFitPolicy) {
        this.edgeComputationPolicy = edgeComputationPolicy;
        this.edgeRenderingPolicy = edgeRenderingPolicy;
        this.viewFitPolicy = viewFitPolicy;
    }

    public IDisplay init(IWorkspace workspace){
    	IDisplay display  = initializeDisplay(workspace);
    	
    	configureDisplayRenderers(display, workspace);
    	//configureNavigation(display, workspace, (MouseEdgeViewController)display.getMouse());
    	configureMouse(display, workspace);
    	configureComputationPolicies(workspace);
    	return display;
    }
    
    public IDisplay initializeDisplay(final IWorkspace workspace){
        final Display display = new Display(true, new MouseItemControllerFactory(){
        	@Override
            public AbstractMouseViewController getController(IDisplay display, IView view){
        		// TODO: have single mouse implementation (merge with MouseItemViewController)
                return new MouseEdgeViewController(display, view);
            }
        });
        /*IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        display.setView(new View(renderer, display));*/
        workspace.initializeRenderer(display);
        workspace.createView(display);

        display.openFrame();
        
        ForceDebugger.attach((MouseItemViewController)display.getMouse(), workspace.getModel(), workspace.getRenderer().getMainHierarchicalRenderer());
		return display;
    }
    
    public void initBoundsCache(final IWorkspace workspace) {
        ((AbstractHierarchicalModel) workspace.getModel()).updateBoundsCache();
    }


	public void configureDisplayRenderers(IDisplay display, IWorkspace workspace) {
        display.getView().getGridRendererSettings().setDisplayed(false);
		display.getView().setShowCenterCross(false);
		// display.getView().setBackgroundColor(TopologyStyleSheet.BACKGROUND);
		// loadBackground(workspace);
	}

	public INavigationController configureNavigation(IDisplay display, IWorkspace workspace,
			MouseItemViewController mouse) {
		INavigationController navigationController = new NavigationController(display, workspace.getRenderer(),
				display.getAnimator(), mouse, workspace.getModel(), null, workspace.getRenderingPolicy());
		mouse.setNavigation(navigationController);
		return navigationController;
	}

	public MouseEdgeViewController configureMouse(IDisplay display, final IWorkspace workspace) {
		MouseEdgeViewController mouse = ((MouseEdgeViewController) display
				.getMouse());
		mouse.setLayout(workspace.getLayout());
		mouse.setTubeRenderer(workspace.getRenderer().getTubeRenderer());
		mouse.setRootModel(workspace.getModel());
		mouse.setRunner(workspace.getRunner(workspace.getLayout(),
				display.getView()));
		return mouse;
	}

    public void configureComputationPolicies(final IWorkspace workspace) {
        workspace.getConfiguration().apply(edgeComputationPolicy);
        workspace.getConfiguration().apply(edgeRenderingPolicy);
        workspace.getConfiguration().apply(viewFitPolicy);
        workspace.getConfiguration().getLayoutRunnerConfiguration().setSequence(new LayoutRunnerSequenceTwoPhase());
    }
}

package org.datagr4m.application.designer;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.runner.LayoutLevelSettings;
import org.datagr4m.drawing.layout.runner.LayoutRunnerConfiguration;
import org.datagr4m.drawing.layout.runner.sequence.LayoutRunnerSequenceTwoPhase;
import org.datagr4m.drawing.model.items.hierarchical.AbstractHierarchicalModel;
import org.datagr4m.drawing.viewer.mouse.edges.MouseEdgeViewController;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.keyboard.PrintScreenObfuscator;
import org.datagr4m.viewer.layered.IPopupLayer;
import org.datagr4m.viewer.layered.LayeredDisplay;
import org.datagr4m.workspace.IWorkspace;
import org.datagr4m.workspace.WorkspaceController;
import org.datagr4m.workspace.WorkspaceSettings;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;
import org.datagr4m.workspace.factories.Datagr4mFactories;
import org.datagr4m.workspace.factories.IDatagr4mFactories;


public class Designer extends AbstractDatagr4mViewer implements IDesigner {

	protected Designer() {
		this("", (WorkspaceSettings) null);
	}

	protected Designer(String root) {
		this(root, (WorkspaceSettings) null);
	}

	protected Designer(String root, String workspaceName) {
		this(root, new WorkspaceSettings(workspaceName));
	}

	protected Designer(String root, WorkspaceSettings settings) {
		init(root, settings);
	}
	
	@Override
	protected IDatagr4mFactories initFactories() {
		return new Datagr4mFactories();
	}

	@Override
	protected void initControllers(WorkspaceSettings settings) {
		this.dataController = new WorkspaceController(settings);
		this.dataController.addListener(this);
	}

	@Override
	protected void initDisplay() {
		this.display = new Display(true, factories.getMouseFactory());
		this.layered = new LayeredDisplay(display);
		this.display.setLayeredDisplay(layered);
		this.pso = new PrintScreenObfuscator(display);
	}

	@Override
	protected void buildGUI() {
	}

	/* */

	/* SET UP A WORKSPACE */

	@Override
	public void plugWorkspace(IWorkspace workspace) {
		if (workspace.getFiles() != null && workspace.getFiles().isXmlFormat()) {
			// if(!verifyTokens(workspace))
			// return;
		}

		// initialize window
		setTitle(DesignerConsts.WINDOW_TITLE + " - "
				+ dataController.getSettings().getName());
		configureStaticLayoutSettings(workspace);
		updateBoundsCache(workspace);

		// plug rendering to display
		workspace.initializeRenderer(display);
		workspace.createView(display);
		configureRenderers(workspace);

		// configure GUI
		// guiBuilder.configureOverviewWidget(workspace, this);
		// guiBuilder.plugWidgets(workspace, this); // plug each UI controller

		// showRunnerCounter(); // show the runner counter

		// now we can rebuild edge model
		// workspace.getRunner().oneEdgeStep();

		// configure mouse and navigation
		MouseEdgeViewController mouse = configureMouse(workspace);
		configureNavigation(workspace, mouse);
		// makeEdgeTableMouse();
		// ForceDebugger.attach((MouseItemViewController)display.getMouse(),
		// workspace.getModel(),
		// (IHierarchicalRenderer)workspace.getRenderer().getMainLayer());
		autoFit();

		// ENABLE SPECIFIC CONFIGURATION
		Logger.getLogger(this.getClass()).info(
				"Workspace info: "
						+ workspace.getTopology().getGraph()
								.getEdgeCount() + " edges");
		configureComputationPolicies(workspace);
		start(workspace);
	}

	@Override
	public void configureComputationPolicies(IWorkspace workspace) {
		if (workspace.getTopology().getGraph().getEdgeCount() > 100) {
			workspace.getConfiguration().apply(
					EdgeComputationPolicy.COMPUTE_AT_END);
			workspace.getConfiguration()
					.apply(EdgeRenderingPolicy.ON_ROLL_OVER);
		} else {
			workspace.getConfiguration().apply(EdgeComputationPolicy.ALWAYS);
			workspace.getConfiguration().apply(EdgeRenderingPolicy.ALWAYS);
		}

		// workspace.getConfiguration().apply(ViewPolicy.MANUAL_FIT);
		workspace.getConfiguration().apply(ViewPolicy.AUTOFIT_AT_RUN);
		workspace.getConfiguration().getLayoutRunnerConfiguration()
				.setSequence(new LayoutRunnerSequenceTwoPhase());
	}

	@Override
	public void configureStaticLayoutSettings(IWorkspace workspace) {
		if (LayoutRunnerConfiguration.command == null) {
			LayoutLevelSettings.DEFAULT_REPULSION = 30; // 200 ok achere
			LayoutLevelSettings.DEFAULT_LOOPS = 10;
		} else {
			LayoutLevelSettings.DEFAULT_REPULSION = LayoutRunnerConfiguration.command.repulse;
			LayoutLevelSettings.DEFAULT_ATTRACTION = LayoutRunnerConfiguration.command.attract;
		}
	}

	/* POPUPS */

	@Override
	public IPopupLayer getPopupLayer() {
		return layered;
	}

	protected static boolean DO_NOT_REBUILD_FRESH_WORK = true;

	/*  */

	public void updateBoundsCache(final IWorkspace workspace) {
		((AbstractHierarchicalModel) workspace.getModel())
				.updateBoundsCache();
	}

	/** Access the layered display, allowing to create toolbars and popups. */
	public LayeredDisplay getLayered() {
		return layered;
	}

	/* */

	protected LayeredDisplay layered;

	private static final long serialVersionUID = 2153677403730418312L;
}

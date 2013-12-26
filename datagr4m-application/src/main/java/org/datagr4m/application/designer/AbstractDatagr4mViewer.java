package org.datagr4m.application.designer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.animation.ItemFocusAnimation;
import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutUtils;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemLabelFinder;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.NavigationController;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.edges.MouseEdgeViewController;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.view2d.icons.IconLibrary;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.AnimationStack;
import org.datagr4m.viewer.keyboard.PrintScreenObfuscator;
import org.datagr4m.viewer.layered.IPopupLayer;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.workspace.IWorkspace;
import org.datagr4m.workspace.IWorkspaceController;
import org.datagr4m.workspace.WorkspaceSettings;
import org.datagr4m.workspace.factories.IDatagr4mFactories;
import org.jzy3d.io.FileImage;
import org.jzy3d.maths.Coord2d;

public abstract class AbstractDatagr4mViewer extends JFrame implements
		IDesktopDesigner {
	protected DesignerConfiguration configuration = new DesignerConfiguration();
	protected INavigationController navigationController;
	protected IWorkspaceController dataController;
	protected Display display;
	protected PrintScreenObfuscator pso;
	
    protected IDatagr4mFactories factories;


	/* */

	protected AbstractDatagr4mViewer() {
		this("", (WorkspaceSettings) null);
	}

	protected AbstractDatagr4mViewer(String root) {
		this(root, (WorkspaceSettings) null);
	}

	protected AbstractDatagr4mViewer(String root, String workspaceName) {
		this(root, new WorkspaceSettings(workspaceName));
	}

	protected AbstractDatagr4mViewer(String root, WorkspaceSettings settings) {
		init(root, settings);
	}

	@Override
	public void init(String root, WorkspaceSettings settings) {
		factories = initFactories();
		initControllers(settings);
		initUI(root);
		initDisplay();
		buildGUI();

		maximize();
	}
	

	protected void initUI(String root) {
		if (root != null)
			IconLibrary.setRoot(root);
		setIconImage(IconLibrary.getIcon(DesignerConsts.WINDOW_ICON).getImage());
		setTitle(DesignerConsts.WINDOW_TITLE);
		LookAndFeel.apply();
	}

	protected abstract IDatagr4mFactories initFactories();
	protected abstract void initControllers(WorkspaceSettings settings);
	protected abstract void initDisplay();
	protected abstract void buildGUI();

	@Override
	public void start(final IWorkspace workspace) {
		if (!workspace.isCoordinatesAvailable()) {
			workspace.getRunner().start();
		}
		// or simply compute edge layout
		else {
			workspace.getRunner().oneEdgeStep();
			LayoutUtils.updatePairSplines(workspace.getLayout());
		}
	}

	@Override
	public void maximize() {
		setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
	}

	@Override
	public JFrame toJFrame() {
		return this;
	}

	@Override
	public void configureRenderers(IWorkspace workspace) {
		display.getView().getGridRendererSettings().setDisplayed(false);
		display.getView().setShowCenterCross(false);
		// display.getView().setBackgroundColor(TopologyStyleSheet.BACKGROUND);
		// loadBackground(workspace);
	}

	@Override
	public void configureNavigation(IWorkspace workspace,
			MouseEdgeViewController mouse) {
		navigationController = new NavigationController(display, workspace.getRenderer(),
				display.getAnimator(), mouse, workspace.getModel(), null, workspace.getRenderingPolicy());
		mouse.setNavigation(navigationController);
	}

	protected MouseEdgeViewController configureMouse(final IWorkspace workspace) {
		MouseEdgeViewController mouse = ((MouseEdgeViewController) display
				.getMouse());
		mouse.setLayout(workspace.getLayout());
		mouse.setTubeRenderer(workspace.getRenderer().getTubeRenderer());
		mouse.setRootModel(workspace.getModel());
		mouse.setRunner(workspace.getRunner(workspace.getLayout(),
				display.getView()));
		return mouse;
	}

	@Override
	public void loadBackground(final IWorkspace workspace, String file) {
		try {
			Logger.getLogger(AbstractDatagr4mViewer.class).info("loading bg image: " + file);
			final Image image = FileImage.load(file);
			double w = 1000;
			double h = 1000;
			// final Rectangle2D rect = new Rectangle2D.Double(0,0,w,h);
			display.getUnderlay().clearRenderers();
			display.getUnderlay().addRenderer(new AbstractRenderer() {
				@Override
				public void render(Graphics2D graphic) {
					IHierarchicalModel model = workspace.getModel();
					Coord2d topLeft = model.getRawRectangleBounds()
							.getTopLeftCorner();
					Coord2d bottomRight = model.getRawRectangleBounds()
							.getBottomRightCorner();
					Point2D pt = Pt.cloneAsDoublePoint(topLeft);
					Point2D pt2 = Pt.cloneAsDoublePoint(bottomRight);
					display.getView().modelToScreen(pt);
					display.getView().modelToScreen(pt2);
					drawImage(
							graphic,
							image,
							new Rectangle2D.Double(pt.getX(), pt.getY(), pt2
									.getX() - pt.getX(), pt2.getY() - pt.getY()));
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ILayoutRunner getLayoutRunner() {
		return getDataController().getCurrentWorkspace().getRunner();
	}

	@Override
	public JInternalFrame popup(String title, Exception e) {
		return popup(title, e, 50, 50, 500, 200);
	}

	@Override
	public JInternalFrame popup(String title, String message) {
		return popup(title, message, 50, 50, 500, 200);
	}

	@Override
	public JInternalFrame popup(String title, Exception e, int x, int y,
			int width, int height) {
		return popup(title, getStackTrace(e), x, y, width, height);
	}

	protected static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	@Override
	public JInternalFrame popup(String title, String message, int x, int y,
			int width, int height) {
		JLabel label = new JLabel("<html>" + message + "</html>");
		label.setPreferredSize(new Dimension(1, 1));
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setBackground(new Color(1, 1, 1));
		IPopupLayer pl = getPopupLayer();
		if (pl != null) {
			JInternalFrame jif = pl.addPopupLayer(label, title, x, y, width,
					height);
			if (jif != null)
				jif.setBackground(new Color(255, 255, 255));
			return jif;
		} else
			return null;
	}

	@Override
	public JInternalFrame popup(String title, JComponent component, int x,
			int y, int width, int height) {
		IPopupLayer pl = getPopupLayer();
		if (pl != null) {
			return pl.addPopupLayer(component, title, x, y, width, height);
		} else
			return null;
	}

	protected boolean ioException(Exception e) {
		popup("IO Error", e);
		return false;
	}

	protected boolean licenseException(String title, Exception e) {
		popup("License Error", e);
		return false;
	}

	@Override
	public void workspaceLoaded(IWorkspace w) {
		plugWorkspace(w);
	}

	@Override
	public void workspaceCreated(IWorkspace w) {
		plugWorkspace(w);
	}

	@Override
	public void workspaceSaved(IWorkspace w) {
		System.out.println("saved");
	}

	@Override
	public void workspaceException(IWorkspace w, Exception e) {
		System.err.println("Workspace error:" + w);
		e.printStackTrace();
		if (w != null)
			popup("Workspace error - " + w, e);
		else
			popup("Workspace error", e);
	}

	@Override
	public void searchFinished(List<IBoundedItem> results) {
		if (results.size() > 0) {
			IBoundedItem b = results.get(0);
			panTo(b);
		}
	}

	@Override
	public void autoFit() {
		IWorkspace workspace = dataController.getCurrentWorkspace();
		if (workspace != null) {
			IHierarchicalModel model = workspace.getModel();
			if (model != null) {
				Rectangle2D bounds = model.getRawRectangleBounds()
						.cloneAsRectangle2D();
				// if(bounds!=null)
				display.getView().fit(bounds);
			}
		}
	}

	@Override
	public void moveToNode(String device) {
		ItemLabelFinder visitor = new ItemLabelFinder();
		visitor.setSearchString(device);
		visitor.visit(dataController.getCurrentWorkspace().getModel());

		if (visitor.getResults().size() > 0) {
			IBoundedItem b = visitor.getResults().get(0);
			panTo(b);
		}
	}

	protected void panTo(IBoundedItem b) {
		if (getConfiguration().isAnimateSearchResults()) {
			ItemFocusAnimation ifa = new ItemFocusAnimation(display.getView(),
					b);
			final AnimationStack animator = display.getAnimator();
			animator.push(ifa);
		} else
			display.getView().centerAt(b.getAbsolutePosition(), /*
																 * b.getRadialBounds
																 * ()
																 */0, 1.5f);
	}

	@Override
	public void addForceDebuggerRenderer(final IWorkspace workspace) {
		ForceDebugger.attach((MouseItemViewController) display.getMouse(),
				workspace.getModel(), (IHierarchicalRenderer) workspace
						.getRenderer().getMainLayer());
	}

	@Override
	public IDisplay getDisplay() {
		return display;
	}

	@Override
	public IWorkspaceController getDataController() {
		return dataController;
	}

	@Override
	public INavigationController getNavigationController() {
		return navigationController;
	}

	@Override
	public DesignerConfiguration getConfiguration() {
		return configuration;
	}


	private static final long serialVersionUID = 3347552405232226985L;
}
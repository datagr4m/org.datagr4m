package org.datagr4m.application.designer;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import org.datagr4m.application.designer.toolbars.search.ISearchEngineListener;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.viewer.mouse.edges.MouseEdgeViewController;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.layered.IPopupLayer;
import org.datagr4m.workspace.IControllerListener;
import org.datagr4m.workspace.IWorkspace;
import org.datagr4m.workspace.IWorkspaceController;
import org.datagr4m.workspace.WorkspaceSettings;


public interface IDesigner extends IControllerListener, ISearchEngineListener{
    /**
     * The GUI initialization method that every designer constructor must call.
     * Build menu, widgets, etc.
     * 
     * @param root
     * @param settings
     */
    public void init(String root, WorkspaceSettings settings);
    

    /**
     * Method called once a workspace loading process as finished. Can also be used to
     * load a workspace in the designer manually.
     * @param workspace
     */
    public void plugWorkspace(IWorkspace workspace);

    /**
     * Method called once {@link plugWorkspace} finishes.
     * Usually triggers layout computation.
     * @param workspace
     */
    public void start(final IWorkspace workspace);

    public void maximize();

    public DesignerConfiguration getConfiguration();

    public IWorkspaceController getDataController();
    public INavigationController getNavigationController();
    public IPopupLayer getPopupLayer();
    public IDisplay getDisplay();

    /**
     * The layout runner instance used to process the graph layout.
     * @return
     */
    public ILayoutRunner getLayoutRunner();

    public void autoFit();
    public void moveToNode(String node);
    public void addForceDebuggerRenderer(IWorkspace workspace);
    public void loadBackground(IWorkspace workspace, String file);
    //public void openLoadWorkspaceWizard();

    /* Swing specific*/
    public JInternalFrame popup(String title, Exception e);
    public JInternalFrame popup(String title, String message);
    public JInternalFrame popup(String title, Exception e, int x, int y, int width, int height);
    public JInternalFrame popup(String title, String message, int x, int y, int width, int height);
    public JInternalFrame popup(String title, JComponent component, int x, int y, int width, int height);
    
    /** Utility method used by plugWorkspace made public in interface to ensure a consistent application pattern */
    public void configureStaticLayoutSettings(final IWorkspace workspace);
    public void configureComputationPolicies(final IWorkspace workspace);
    public void configureRenderers(final IWorkspace workspace);
    public void configureNavigation(final IWorkspace workspace, MouseEdgeViewController mouse);
}
package org.datagr4m.application.applet;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import org.datagr4m.application.designer.DesignerConfiguration;
import org.datagr4m.application.designer.IDesigner;
import org.datagr4m.application.gui.Datagr4mViewer;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.viewer.mouse.edges.MouseEdgeViewController;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.layered.IPopupLayer;
import org.datagr4m.workspace.IWorkspace;
import org.datagr4m.workspace.IWorkspaceController;
import org.datagr4m.workspace.WorkspaceSettings;

public class Datagr4mAppletViewer extends JApplet implements IDesigner{
	private static final long serialVersionUID = -7634178083957216151L;
	protected Datagr4mViewer designer = new Datagr4mViewer();

    public Datagr4mAppletViewer() throws HeadlessException {
        super();
        //getContentPane().add(designer.getMainPanel(), BorderLayout.CENTER);
        /*
        String columns = "[1000px, grow]";
        String lines = "[700px, grow]";
        setLayout(new MigLayout("", columns, lines));
        */
        //setLayout (new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(designer.getToolbar(), BorderLayout.PAGE_START);
        add(designer.getSplitPane());
        setVisible(true);
    }

    @Override
    public void workspaceLoaded(IWorkspace w) {
        designer.workspaceLoaded(w);
    }

    @Override
    public void workspaceCreated(IWorkspace w) {
        designer.workspaceCreated(w);
    }

    @Override
    public void workspaceSaved(IWorkspace w) {
        designer.workspaceSaved(w);
    }

    @Override
    public void workspaceException(IWorkspace w, Exception e) {
        designer.workspaceException(w, e);
    }

    @Override
    public void searchFinished(List<IBoundedItem> results) {
        designer.searchFinished(results);
    }

    @Override
    public void init(String root, WorkspaceSettings settings) {
        designer.init(root, settings);
    }

    @Override
    public void plugWorkspace(IWorkspace workspace) {
        designer.plugWorkspace(workspace);
    }

    @Override
    public void start(IWorkspace workspace) {
        designer.start(workspace);
    }

    @Override
    public void maximize() {
        designer.maximize();
    }

    @Override
    public DesignerConfiguration getConfiguration() {
        return designer.getConfiguration();
    }

    @Override
    public IWorkspaceController getDataController() {
        return designer.getDataController();
    }

    @Override
    public INavigationController getNavigationController() {
        return designer.getNavigationController();
    }

    @Override
    public IPopupLayer getPopupLayer() {
        return designer.getPopupLayer();
    }

    @Override
    public IDisplay getDisplay() {
        return designer.getDisplay();
    }

    @Override
    public ILayoutRunner getLayoutRunner() {
        return designer.getLayoutRunner();
    }

    @Override
    public void autoFit() {
        designer.autoFit();
    }

    @Override
    public void moveToNode(String device) {
        designer.moveToNode(device);
    }

    @Override
    public void addForceDebuggerRenderer(IWorkspace workspace) {
        designer.addForceDebuggerRenderer(workspace);
    }

    @Override
    public void loadBackground(IWorkspace workspace, String file) {
        designer.loadBackground(workspace, file);
    }

    @Override
    public JInternalFrame popup(String title, Exception e) {
        return designer.popup(title, e);
    }

    @Override
    public JInternalFrame popup(String title, String message) {
        return designer.popup(title, message);
    }

    @Override
    public JInternalFrame popup(String title, Exception e, int x, int y, int width, int height) {
        return designer.popup(title, e, x, y, width, height);
    }

    @Override
    public JInternalFrame popup(String title, String message, int x, int y, int width, int height) {
        return designer.popup(title, message, x, y, width, height);
    }

    @Override
    public JInternalFrame popup(String title, JComponent component, int x, int y, int width, int height) {
        return designer.popup(title, component, x, y, width, height);
    }

    @Override
    public void configureStaticLayoutSettings(IWorkspace workspace) {
        designer.configureStaticLayoutSettings(workspace);
    }

    @Override
    public void configureComputationPolicies(IWorkspace workspace) {
        designer.configureComputationPolicies(workspace);
    }

    @Override
    public void configureRenderers(IWorkspace workspace) {
        designer.configureRenderers(workspace);
    }

    @Override
    public void configureNavigation(IWorkspace workspace, MouseEdgeViewController mouse) {
        designer.configureNavigation(workspace, mouse);
    }
}

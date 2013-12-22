package org.datagr4m.application.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.datagr4m.application.Datagr4mWorkspaceController;
import org.datagr4m.application.designer.AbstractDatagr4mViewer;
import org.datagr4m.application.designer.DesignerConsts;
import org.datagr4m.application.designer.IDesktopDesigner;
import org.datagr4m.application.designer.PopupHelper;
import org.datagr4m.application.designer.toolbars.runner.LayoutToolbar;
import org.datagr4m.application.gui.toolbar.Datagr4mToolbar;
import org.datagr4m.application.gui.toolbar.ToolbarConfiguration;
import org.datagr4m.application.gui.trees.listeners.ITreeNodeSelectedListener;
import org.datagr4m.application.gui.trees.topology.TopologyTree;
import org.datagr4m.application.neo4j.navigation.plugins.bringandgo.Neo4jBringAndGoPlugin;
import org.datagr4m.application.neo4j.renderers.Neo4jRelationshipRendererSettings;
import org.datagr4m.application.neo4j.workspace.Neo4jWorkspace;
import org.datagr4m.application.neo4j.workspace.Neo4jWorkspaceSettings;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.LayoutRunnerListenerAdapter;
import org.datagr4m.drawing.layout.runner.sequence.LayoutRunnerSequenceTwoPhase;
import org.datagr4m.drawing.model.items.hierarchical.AbstractHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.navigation.NavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.bringandgo.IBringAndGoPlugin;
import org.datagr4m.drawing.renderer.policy.RenderingPolicy;
import org.datagr4m.drawing.viewer.mouse.edges.MouseEdgeViewController;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.keyboard.PrintScreenObfuscator;
import org.datagr4m.viewer.layered.IPopupLayer;
import org.datagr4m.workspace.IWorkspace;
import org.datagr4m.workspace.WorkspaceSettings;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeComputationPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.EdgeRenderingPolicy;
import org.datagr4m.workspace.configuration.ConfigurationFacade.ViewPolicy;
import org.datagr4m.workspace.factories.Datagr4mFactories;
import org.datagr4m.workspace.factories.IDatagr4mFactories;

import com.datagr4m.neo4j.topology.Neo4jTopology;

public class Datagr4mViewer extends AbstractDatagr4mViewer implements IDesktopDesigner {
    protected JPanel mainPanel = new JPanel();
    protected JSplitPane splitPane;
    protected TopologyTree topologyTree;
    protected LayoutToolbar layoutToolbar;
    protected Datagr4mToolbar toolbar;
    protected PrintScreenObfuscator pso;
    

    public Datagr4mViewer() {
        super();
    }

    public Datagr4mViewer(String root, String workspaceName) {
        super(root, workspaceName);
    }

    public Datagr4mViewer(String root, WorkspaceSettings settings) {
        super(root, settings);
    }

    public Datagr4mViewer(String root) {
        super(root);
    }

    @Override
    public void init(String root, WorkspaceSettings settings) {
        super.init(root, settings);
    }
    
    @Override
	protected IDatagr4mFactories initFactories() {
		return new Datagr4mFactories();
	}

    @Override
    protected void initDisplay() {
        this.display = new Display(true, factories.getMouseFactory());
        // pso = new PrintScreenObfuscator(display);

        setF5Behaviour();
    }

    private void setF5Behaviour() {
		JRootPane p = getRootPane();
        p.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh");
        p.getActionMap().put("refresh", new AbstractAction() {
			private static final long serialVersionUID = 3410339800432895381L;

			@Override
            public void actionPerformed(ActionEvent e) {
                reloadWorkspace();
            }
        });
	}

    @Override
    protected void initControllers(WorkspaceSettings settings) {
        this.dataController = new Datagr4mWorkspaceController(this, settings != null ? new Neo4jWorkspaceSettings(settings) : null);
        this.dataController.addListener(this);
    }

    @Override
    protected void buildGUI() {
        // GUI components
        topologyTree = new TopologyTree();

        ITreeNodeSelectedListener listener = new ITreeNodeSelectedListener() {
            @Override
            public void nodeSelected(DefaultMutableTreeNode node) {
                Object o = node.getUserObject();
                if (o instanceof Group<?>) {
                    Group<?> group = (Group<?>) o;
                } else if (o instanceof IPropertyNode) {
                    IPropertyNode item = (IPropertyNode) o;
                    moveToNode(item.getLabel());
                }
                // System.out.println(.getClass());
            }
        };
        topologyTree.addItemSelectionListener(listener);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, topologyTree, display);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);
        // splitPane.setMinimumSize(new Dimension(800,600));
        splitPane.setBorder(BorderFactory.createBevelBorder(1));

        toolbar = buildGUIToolbar();

        // Complete application panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("", "[1000px, grow]", "[50px][700px, grow]"));
        mainPanel.add(toolbar, "cell 0 0, growx");
        mainPanel.add(splitPane, "cell 0 1, grow");

        // General frame layout
        String columns = "[1000px, grow]";
        String lines = "[700px, grow]";
        JFrame frame = toJFrame();
        frame.setLayout(new MigLayout("", columns, lines));
        frame.setContentPane(mainPanel);
        // designer.setContentPane(new G2DGLPanel(designer.getLayered()));

        // Application menu
        buildGUIMenu();
    }

    public void buildGUIMenu() {
        new MenuBuilder(this);
    }

    public Datagr4mToolbar buildGUIToolbar() {
        ToolbarConfiguration conf = new ToolbarConfiguration();
        conf.setSearch(false);
        Datagr4mToolbar toolbar = Datagr4mToolbar.newToolbar(this, conf);
        return toolbar;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    @Override
    public void plugWorkspace(final IWorkspace workspace) {
        setTitle(DesignerConsts.WINDOW_TITLE + " - " + dataController.getSettings().getName());

        if (workspace instanceof Neo4jWorkspace) {
            // layout settings
            configureStaticLayoutSettings(workspace);

            // plug rendering to display
            workspace.initializeRenderer(display);
            workspace.createView(display);
            configureRenderers(workspace);

            // configure GUI
            topologyTree.setEdgeCellRenderer(EDGE_COLORMAP);
            topologyTree.setModel(workspace.getName(), (Neo4jTopology) workspace.getTopology());
            toolbar.getButtonRun().plugWorkspace(workspace, display);
            workspace.getRunner().addListener(new LayoutRunnerListenerAdapter() {
                @Override
                public void runnerFailed(String message, Exception e) {
                    PopupHelper.error("Error", e); // print stack
                    //popup(message, e);
                }
            });
            // layoutToolbar.plugWorkspace(workspace, getDisplay());

            // now we can rebuild edge model
            // workspace.getRunner().oneEdgeStep();

            // configure mouse and navigation
            MouseEdgeViewController mouse = configureMouse(workspace);
            configureNavigation(workspace, mouse);
            addForceDebuggerRenderer(workspace);
            autoFit();

            ILayoutRunner r = getLayoutRunner();
            //r.repulsionSetAllTo(150000);

            // ENABLE SPECIFIC CONFIGURATION
            Logger.getLogger(this.getClass()).info("Workspace info: " + workspace.getTopology().getGraph().getEdgeCount() + " edges");
            configureComputationPolicies(workspace);

            // Either run layout from scratch,

            start(workspace);
        } else {
            popup("Error", "unknown workspace type: " + workspace.getClass());
        }
    }
    
    @Override
	public JInternalFrame popup(String title, String message, int x, int y, int width, int height) {
        PopupHelper.popup(this, message);
        return null;
        /*JLabel label = new JLabel("<html>" + message + "</html>");
        label.setPreferredSize(new Dimension(1, 1));
        label.setVerticalAlignment(JLabel.TOP);
        label.setBackground(new Color(1, 1, 1));
        IPopupLayer pl = getPopupLayer();
        if (pl != null) {
            JInternalFrame jif = pl.addPopupLayer(label, title, x, y, width, height);
            if(jif!=null)
                jif.setBackground(new Color(255, 255, 255));
            return jif;
        } else
            return null;*/
    }

    public void reloadWorkspace() {
        IWorkspace w = getDataController().getCurrentWorkspace();
        if (w != null) {
            String name = w.getName();
            w.shutdown();

            Neo4jWorkspaceSettings s = new Neo4jWorkspaceSettings(name);
            getDataController().setSettings(s);
            getDataController().loadWorkpaceFiles(this);
        }
    }

    /* ALL POST LOADING CONFIGURATION STEPS */

    @Override
    public void configureStaticLayoutSettings(final IWorkspace workspace) {
        ((AbstractHierarchicalModel) workspace.getModel()).updateBoundsCache();
    }

    @Override
    public void configureComputationPolicies(final IWorkspace workspace) {
        /*
         * if (workspace.getTopology().getGlobalGraph().getEdgeCount() > 1000) {
         * workspace
         * .getConfiguration().apply(EdgeComputationPolicy.COMPUTE_AT_END);
         * workspace.getConfiguration().apply(EdgeRenderingPolicy.ON_ROLL_OVER);
         * } else {
         * workspace.getConfiguration().apply(EdgeComputationPolicy.ALWAYS);
         * workspace.getConfiguration().apply(EdgeRenderingPolicy.ALWAYS); }
         */

        workspace.getConfiguration().apply(EdgeComputationPolicy.COMPUTE_AT_END);
        workspace.getConfiguration().apply(EdgeRenderingPolicy.ALWAYS);

        // workspace.getConfiguration().apply(ViewPolicy.MANUAL_FIT);
        workspace.getConfiguration().apply(ViewPolicy.AUTOFIT_AT_RUN);
        workspace.getConfiguration().getLayoutRunnerConfiguration().setSequence(new LayoutRunnerSequenceTwoPhase());
    }

    @Override
    public void configureRenderers(final IWorkspace workspace) {
        if (workspace instanceof Neo4jWorkspace) {
            display.getView().getGridRendererSettings().setDisplayed(false);
            display.getView().setShowCenterCross(false);
            // display.getM
            // display.getView().g
        }
        // else
        // super.configureRenderers(workspace);
    }

    @Override
    public void configureNavigation(final IWorkspace workspace, MouseEdgeViewController mouse) {
        if (workspace instanceof Neo4jWorkspace) {
            final Neo4jWorkspace nw = (Neo4jWorkspace) workspace;
            
            // Rendering policy
            RenderingPolicy policy = new RenderingPolicy() {
                @Override
				public void setup(IHierarchicalModel model) {
                }

                @Override
                public void init(boolean debugBoundsShown) {
                    super.init(debugBoundsShown);
                    tubeSettings = EDGE_COLORMAP;
                    Neo4jRelationshipRendererSettings nt = (Neo4jRelationshipRendererSettings) tubeSettings;
                    Collection<IPropertyEdge> rs = nw.getNeo4jTopology().getGraph().getEdges();
                    nt.configureEdgeTypeColors(rs);
                }

				@Override
				protected void setupEdgeColorPolicy(IHierarchicalModel model) {
					// TODO Auto-generated method stub
					
				}

				@Override
				protected void setupNodeColorPolicy(IHierarchicalModel model,
						Color color) {
					// TODO Auto-generated method stub
					
				}
            };
            
            // Navigation controller
            PluginLayeredRenderer plr = workspace.getRenderer();
            navigationController = new NavigationController(display, plr, display.getAnimator(), mouse, workspace.getModel(), null, policy);
            mouse.setNavigation(navigationController);
            
            // Custom plugins
            IBringAndGoPlugin bringAndGoPlugin = new Neo4jBringAndGoPlugin(navigationController, display, workspace.getRenderer(), display.getAnimator(), mouse, workspace.getModel());
            navigationController.setBringAndGoPlugin(bringAndGoPlugin);
            
        } else
            super.configureNavigation(workspace, mouse);
    }

    private static final long serialVersionUID = -9161434536060235077L;

    public static final Neo4jRelationshipRendererSettings EDGE_COLORMAP = new Neo4jRelationshipRendererSettings();
    
    @Override
    public IPopupLayer getPopupLayer() {
        // TODO Auto-generated method stub
        return null;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public TopologyTree getTopologyTree() {
        return topologyTree;
    }

    public LayoutToolbar getLayoutToolbar() {
        return layoutToolbar;
    }

    public Datagr4mToolbar getToolbar() {
        return toolbar;
    }

}

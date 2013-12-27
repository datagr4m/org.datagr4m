package org.datagr4m.workspace;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.hierarchical.stratum.HierarchicalStratumLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.factory.ILayoutRunnerFactory;
import org.datagr4m.drawing.layout.runner.factory.LookupLayoutRunnerFactory;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.factories.filters.GroupFilter;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.LabelMode;
import org.datagr4m.drawing.model.items.hierarchical.visitor.AbstractItemVisitor;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemLabelFinder;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ModelEditor;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.hit.HierarchicalHitPolicy;
import org.datagr4m.drawing.renderer.policy.IRenderingPolicy;
import org.datagr4m.io.xml.generated.layout.Edges;
import org.datagr4m.io.xml.generated.layout.Grouplayout;
import org.datagr4m.io.xml.generated.layout.Layout;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;
import org.datagr4m.utils.BinaryFiles;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.layered.LayeredRenderer;
import org.datagr4m.viewer.model.annotations.AnnotationModel;
import org.datagr4m.workspace.configuration.ConfigurationFacade;
import org.jzy3d.maths.Coord2d;

public class Workspace implements Serializable, IWorkspace {
    private static final long serialVersionUID = -3491251741544103630L;

    public static IHierarchicalModelFactory defaultModelFactory = new HierarchicalTopologyModelFactory<Object, Object>();
    public static IHierarchicalLayoutFactory defaultLayoutFactory = new HierarchicalLayoutFactory();
    public static ILayoutRunnerFactory defaultRunnerFactory = new LookupLayoutRunnerFactory();

    public static IRenderingPolicy defaultRenderingPolicy = null;

    public static IHierarchicalModelFactory getDefaultHierarchicalModelFactory() {
        return defaultModelFactory;
    }

    public static IHierarchicalLayoutFactory getDefaultHierarchicalLayoutFactory() {
        return defaultLayoutFactory;
    }

    public static IRenderingPolicy getDefaultRenderingPolicy() {
        return defaultRenderingPolicy;
    }

    public static void setDefaultHierarchicalModelFactory(IHierarchicalModelFactory f) {
        defaultModelFactory = f;
    }

    public static void setDefaultHierarchicalLayoutFactory(HierarchicalLayoutFactory f) {
        defaultLayoutFactory = f;
    }

    public static void setDefaultRenderingPolicy(IRenderingPolicy p) {
        defaultRenderingPolicy = p;
    }

    /* */

    public Workspace() {
        modelFactory = getDefaultHierarchicalModelFactory();
        layoutFactory = getDefaultHierarchicalLayoutFactory();
        renderingPolicy = getDefaultRenderingPolicy();
    }

    public Workspace(Topology<?, ?> topology, IHierarchicalModel model, IHierarchicalEdgeModel edgeModel, AnnotationModel amodel, IHierarchicalLayout layout,
            Map<String, Object> metadata) {
        this();

        this.topology = topology;
        this.model = model;
        this.annotationModel = amodel;
        this.layout = layout;
        this.edgeModel = edgeModel;
        this.metadata = metadata;
        this.configuration = new ConfigurationFacade(this);
    }

    public Workspace(Topology<?, ?> topology) {
        this();

        setName("#no workspace");

        this.topology = topology;
        this.model = (IHierarchicalModel) modelFactory.getLayoutModel(topology);
        this.edgeModel = model.getEdgeModel();
        this.layout = layoutFactory.getLayout(model);
        this.layout.getTubeLayout().setEdgePostProcess(null);
        this.annotationModel = new AnnotationModel();
        this.configuration = new ConfigurationFacade(this);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void initializeRenderer(IDisplay display) {
        pluginRenderer = new PluginLayeredRenderer(display, model, edgeModel, annotationModel);
        setupHitPolicy();
        setupEdgeRenderer();
    }

    public HierarchicalHitPolicy setupHitPolicy() {
        HierarchicalHitPolicy policy = (HierarchicalHitPolicy) pluginRenderer.getMainLayer().getHitProcessor();
        policy.setHitSlot(false);
        return policy;
    }

    protected TubeRendererSettings setupEdgeRenderer() {
        TubeRendererSettings edgeRendererSettings = new TubeRendererSettings();
        return setupEdgeRenderer(edgeRendererSettings);
    }

    public TubeRendererSettings setupEdgeRenderer(TubeRendererSettings edgeRendererSettings) {
        PluginLayeredRenderer plr = (PluginLayeredRenderer) pluginRenderer;
        IHierarchicalRenderer r = plr.getMainHierarchicalRenderer();
        if (r instanceof HierarchicalGraphRenderer) {
            HierarchicalGraphRenderer gr = (HierarchicalGraphRenderer) r;

            // -- Tube renderer --
            TubeRenderer tr = (TubeRenderer) gr.getPostRenderers().get(0);

            // settings
            tr.setEdgeRendererSettings(edgeRendererSettings);

            // configuration
            tr.getConfiguration().setRenderEdgeOnlyOnMouseOver(false);
        }
        TubeRenderer tr = (TubeRenderer) r.getPostRenderers().get(0);
        tr.setEdgeRendererSettings(edgeRendererSettings);
        tr.getConfiguration().setRenderEdgeOnlyOnMouseOver(false);

        return edgeRendererSettings;
    }

    @Override
    public void createView(IDisplay display) {
        display.createViewFor(pluginRenderer);
    }

    /************************************/

    /**
     * Build a workspace using the factories, filters, layout settings.
     */
    @SuppressWarnings("unchecked")
    public static <V, E> IWorkspace build(Topology<V, E> topology, IHierarchicalModelFactory factory, HierarchicalLayoutFactory layoutFactory, GroupFilter<V> filter,
            Layout layoutML, Map<String, Object> metadata) {
        // main model (including tubes)
        IHierarchicalGraphModel model = (IHierarchicalGraphModel) factory.getLayoutModel(topology); // with
                                                                                                    // tubes
        model.getLocalEdges().clear();

        List<IBoundedItem> items = model.getDescendants();
        for (IBoundedItem item : items) {
            if (!model.getEdgeModel().getItems().contains(item))
                model.getEdgeModel().addItem(item);
        }
        applyLayoutMLConfiguration(model, layoutML);
        // model.getTubeModel().addItem(o);

        // other models
        AnnotationModel amodel = new AnnotationModel();

        // layout
        if (layoutML != null) {
            Map<String, String> mapping = extractModelLayoutMapping(layoutML);
            layoutFactory.setModelLayoutMapping(mapping);
        }
        IHierarchicalLayout layout = layoutFactory.getLayout(model, model.getEdgeModel()); // with
                                                                                           // tube
                                                                                           // layout
        // LayoutPrinter p = new LayoutPrinter();
        // p.visit(layout);

        applySpecificLayoutSettings(layout, layoutML);

        layout.initAlgo();

        // go!
        return new Workspace(topology, model, model.getEdgeModel(), amodel, layout, metadata);
    }

    /* READING LAYOUT XML */

    /**
     * Read the default group shape, and apply it to ALL elements in the
     * hierarchy.
     */
    protected static void applyLayoutMLConfiguration(IHierarchicalModel model, Layout layoutML) {
        if (layoutML != null) {
            if (layoutML.getDefaults() == null)
                return;
            if (layoutML.getDefaults().getGroup() == null)
                return;

            // applique la forme par defaut a chacun des groupe
            String xmlShape = layoutML.getDefaults().getGroup().getShape();
            ModelEditor me = new ModelEditor();
            if ("rectangle".equals(xmlShape)) {
                me.applyGroupShape(model, ItemShape.RECTANGLE);
            } else if ("circle".equals(xmlShape)) {
                me.applyGroupShape(model, ItemShape.CIRCLE);
            } else
                throw new RuntimeException("unknown layout default shape: " + xmlShape);

            // active ou non les label de lien
            Edges xmlEdge = layoutML.getEdges();
            if (xmlEdge != null) {
                if (xmlEdge.getLabel() != null) {
                    String type = xmlEdge.getLabel().getType();
                    if ("none".equals(type))
                        model.getEdgeModel().setLabelMode(LabelMode.NONE);
                    else if ("dynamic".equals(type))
                        model.getEdgeModel().setLabelMode(LabelMode.DYNAMIC);
                    else
                        throw new RuntimeException("unknown label mode '" + type + "'. Expects none|dynamic");
                }
            } else {
                model.getEdgeModel().setLabelMode(LabelMode.DYNAMIC);
            }

        }
    }

    /**
     * Extract from XML layout settings the type of layout required for each
     * model.
     * 
     * @return a name mapping model name <-> layout type name
     */
    protected static Map<String, String> extractModelLayoutMapping(Layout layoutML) {
        Map<String, String> modelLayoutMapping = new HashMap<String, String>();

        for (org.datagr4m.io.xml.generated.layout.Group g : layoutML.getGroup()) {
            String gname = g.getName();
            Grouplayout glayout = g.getGrouplayout();

            if (glayout != null) {
                if (glayout.getColumn() != null)
                    modelLayoutMapping.put(gname, HierarchicalLayoutFactory.LAYOUT_COLUMN_NAME);
                else if (glayout.getRow() != null)
                    modelLayoutMapping.put(gname, HierarchicalLayoutFactory.LAYOUT_ROW_NAME);
                else if (glayout.getMatrix() != null)
                    modelLayoutMapping.put(gname, HierarchicalLayoutFactory.LAYOUT_MATRIX_NAME);
                else if (glayout.getStratums() != null)
                    modelLayoutMapping.put(gname, HierarchicalLayoutFactory.LAYOUT_STRATUMS_NAME);
            }
        }
        return modelLayoutMapping;
    }

    /**
     * Read parameters that are specific to a given layout, find this layout,
     * and setup its parameters.
     */
    protected static void applySpecificLayoutSettings(IHierarchicalLayout layout, Layout layoutML) {
        if (layoutML == null)
            return;
        IHierarchicalModel model = layout.getModel();

        for (org.datagr4m.io.xml.generated.layout.Group g : layoutML.getGroup()) {
            String gname = g.getName();
            Grouplayout glayout = g.getGrouplayout();

            // search for the king of layout
            if (glayout != null) {
                // applique les settings de stratum layout
                if (glayout.getStratums() != null) {
                    IHierarchicalLayout found = null;
                    if (gname.equals(layout.getModel().getLabel()))
                        found = layout;
                    else
                        found = layout.findLayoutHoldingModel(gname);

                    if (found != null) {
                        applyStratumParameters(model, glayout, found);
                    } else
                        throw new RuntimeException("could not find a layout holding model named '" + gname + "'");
                }
            }
        }
    }

    /**
     * Extract stratum sequence and configure stratum layout.
     */
    protected static void applyStratumParameters(IHierarchicalModel model, Grouplayout glayout, IHierarchicalLayout found) throws RuntimeException {
        HierarchicalStratumLayout stratumLayout = (HierarchicalStratumLayout) found;

        // build stratum sequence
        List<IBoundedItem> sequence = new ArrayList<IBoundedItem>();
        ItemLabelFinder itemFinder = new ItemLabelFinder();
        for (org.datagr4m.io.xml.generated.layout.Stratum stratum : glayout.getStratums().getStratum()) {
            String stratumGroupName = stratum.getGroup();

            List<IBoundedItem> results = itemFinder.find(stratumGroupName, model);

            if (results.size() == 1) {
                sequence.add(results.get(0));
            } else
                throw new RuntimeException("found irrelevant number of item named '" + stratumGroupName + "' -> " + results);
        }

        stratumLayout.setSequence(sequence);
    }

    // topology is just given to give the ability to the caller to indicate the
    // actual V/E types
    protected static <V, E> GroupFilter<V> newPassAllFilter(Topology<V, E> t) {
        return new GroupFilter<V>() {
            @Override
            public boolean accepts(Group<V> group) {
                return true;
            }
        };
    }

    /*************/

    @Override
    public Topology<?, ?> getTopology() {
        return topology;
    }

    @Override
    public IHierarchicalModel getModel() {
        return model;
    }

    @Override
    public IHierarchicalEdgeModel getEdgeModel() {
        return edgeModel;
    }

    @Override
    public AnnotationModel getAnnotationModel() {
        return annotationModel;
    }

    @Override
    public IHierarchicalLayout getLayout() {
        return layout;
    }

    @Override
    public PluginLayeredRenderer getRenderer() {
        return (PluginLayeredRenderer) pluginRenderer;
    }

    /** create or get a runner able to notify the given view for refresh */
    @Override
    public ILayoutRunner getRunner(IView view) {
        return getOrCreateRunner(getLayout(), view);
    }

    /** create or get a runner. */
    @Override
    public ILayoutRunner getRunner() {
        return getOrCreateRunner(getLayout(), null);
    }

    @Override
    public ILayoutRunner getRunner(IHierarchicalLayout root, IView view) {
        return getOrCreateRunner(root, view);
    }

    protected ILayoutRunner getOrCreateRunner(IHierarchicalLayout root, IView view) {
        if (runner == null) {
            runner = defaultRunnerFactory.newLayoutRunner(root, view);
        }
        return runner;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public ConfigurationFacade getConfiguration() {
        return configuration;
    }

    @Override
    public WorkspaceFiles getFiles() {
        return files;
    }

    public void setFiles(WorkspaceFiles files) {
        this.files = files;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IHierarchicalModelFactory getHierarchicalLayoutModelFactory() {
        return modelFactory;
    }

    @Override
    public IHierarchicalLayoutFactory getHierarchicalLayoutFactory() {
        return layoutFactory;
    }

    @Override
    public IRenderingPolicy getRenderingPolicy() {
        return renderingPolicy;
    }

    @Override
    public void setModelFactory(IHierarchicalModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    @Override
    public void setLayoutFactory(IHierarchicalLayoutFactory layoutFactory) {
        this.layoutFactory = layoutFactory;
    }

    @Override
    public void setRenderingPolicy(IRenderingPolicy renderingPolicy) {
        this.renderingPolicy = renderingPolicy;
    }

    /** A convenient class to remember which file were loaded. */
    public static class WorkspaceFiles {
        public WorkspaceFiles(String repositoryFile, String topologyFile, String layoutFile, boolean isXml) {
            this.repositoryFile = repositoryFile;
            this.topologyFile = topologyFile;
            this.layoutFile = layoutFile;
            this.isXmlFormat = isXml;
        }

        public String getRepositoryFile() {
            return repositoryFile;
        }

        public String getTopologyFile() {
            return topologyFile;
        }

        public String getLayoutFile() {
            return layoutFile;
        }

        public boolean isXmlFormat() {
            return isXmlFormat;
        }

        protected String repositoryFile;
        protected String topologyFile;
        protected String layoutFile;
        protected boolean isXmlFormat;
    }

    public static IWorkspace load(String name) throws Exception {
        return load(null, name);
    }

    public static IWorkspace load(String folder, String name) throws Exception {
        return null;
    }

    /**
     * A simple flag indicating if this workspace has some already defined
     * coordinates, in other words if the workspace contained a file storing a
     * position for each item.
     */
    @Override
    public boolean isCoordinatesAvailable() {
        return coordinatesAvailable;
    }

    @Override
    public void setCoordinatesAvailable(boolean coordinatesAvailable) {
        this.coordinatesAvailable = coordinatesAvailable;
    }

    public void saveMap(String file) throws Exception {
        final Map<String, Coord2d> map = new HashMap<String, Coord2d>();

        AbstractItemVisitor v = new AbstractItemVisitor() {
            @Override
            public void doVisitElement(IHierarchicalModel parent, IBoundedItem element, int depth) {
                if (element.getLabel() != null) // root
                    map.put(element.getLabel(), element.getPosition());
            }
        };
        v.visit(getModel());

        BinaryFiles.save(file, map);
    }

    public void loadMapIfExist() throws Exception {
        File f = new File("data/workspaces/" + name + "/map.bin");
        if (f.exists()) {
            loadMap(f.getAbsolutePath());
        }
    }

    public void loadMap(String file) throws Exception {
        final Map<String, Coord2d> map = BinaryFiles.load(file);
        if (map != null)
            applyMap(map);
    }

    public void applyMap(Map<String, Coord2d> map) {
        IHierarchicalModel model = getModel();
        if (model != null) {
            for (Map.Entry<String, Coord2d> info : map.entrySet()) {
                IBoundedItem i = null;
                if (model.getLabel().equals(info.getKey()))
                    i = model;
                else
                    i = model.getDescendantWithLabel(info.getKey());

                if (i != null) {
                    i.changePosition(info.getValue());
                    // done.add(i);
                } else
                    throw new RuntimeException("model does not hold any item with name '" + info.getKey() + "'");
            }
        } else
            throw new RuntimeException("no layout model in this workspace");
    }

    /***********/

    protected String name;

    protected Topology<?, ?> topology;
    protected IHierarchicalModel model;
    protected IHierarchicalLayout layout;
    protected IHierarchicalEdgeModel edgeModel;
    protected AnnotationModel annotationModel;
    protected transient LayeredRenderer pluginRenderer;
    protected transient ILayoutRunner runner;

    protected Map<String, Object> metadata;

    protected IHierarchicalModelFactory modelFactory;
    protected IHierarchicalLayoutFactory layoutFactory;
    protected IRenderingPolicy renderingPolicy;// = new
                                               // AbstractRenderingPolicy();
    protected ConfigurationFacade configuration;

    protected WorkspaceFiles files;
    protected boolean coordinatesAvailable = false;
}

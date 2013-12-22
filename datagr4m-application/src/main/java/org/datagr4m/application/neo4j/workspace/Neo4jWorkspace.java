package org.datagr4m.application.neo4j.workspace;

import java.util.Map;

import org.apache.log4j.Logger;
import org.datagr4m.application.neo4j.factories.Neo4jLayoutFactory;
import org.datagr4m.application.neo4j.factories.Neo4jModelFactory;
import org.datagr4m.application.neo4j.factories.Neo4jTopologyFactory;
import org.datagr4m.application.neo4j.renderers.Neo4jRelationshipRendererSettings;
import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.hit.HierarchicalHitPolicy;
import org.datagr4m.io.xml.JAXBHandler;
import org.datagr4m.io.xml.generated.dataprism.Dataprism;
import org.datagr4m.io.xml.generated.layout.Layout;
import org.datagr4m.neo4j.navigation.plugins.louposcope.Neo4jLouposcopeLayer;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.model.annotations.AnnotationModel;
import org.datagr4m.views.ViewsManager;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.configuration.ConfigurationFacade;
import org.jzy3d.io.SimpleFile;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import com.datagr4m.neo4j.topology.Neo4jTopology;
import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;
import com.datagr4m.neo4j.topology.graph.Neo4jGraphModelIO;
import com.datagr4m.neo4j.topology.graph.readers.INeo4jGraphReader;

import edu.uci.ics.jung.graph.Graph;

public class Neo4jWorkspace extends Workspace {

    
    /* DATA STRUCTURE */
    
    /** Minimal workspace holding default factories*/
    public Neo4jWorkspace() {
    	super();
    }
    

    /**
     * Build a lightweight workspace with no setting and configuration.
     */
    public Neo4jWorkspace(Neo4jTopology topology){
    	this();
    	
    	this.topology = topology;
    	
        // Build layout model
    	modelFactory = new Neo4jModelFactory();
        model = (IHierarchicalModel)modelFactory.getLayoutModel(topology);
        edgeModel = model.getEdgeModel();

        // Build layout
        Neo4jGraphModel typeModel = new Neo4jGraphModel();
        HierarchicalLayoutFactory lfact = new Neo4jLayoutFactory(typeModel);
        layout = lfact.getLayout(model);
        layout.getTubeLayout().setEdgePostProcess(null);

        // finalize workspace
        annotationModel = new AnnotationModel();
        setName("#no workspace");
        
        this.configuration = new ConfigurationFacade(this);
    }

    /**
     * Build full Neo4jWorkspace involving reading files given by settings:
     * <ul>
     * <li>neo4j database
     * <li>view
     * <li>graph model
     * <li>projection
     * </ul>
     * and build topology & geometrical model according to Neo4j factories
     * 
     * @param settings
     * @throws Exception
     */
    public Neo4jWorkspace(Neo4jWorkspaceSettings settings) throws Exception {
    	String db = DatabaseConfiguration.readDatabase(settings);
        Logger.getLogger(Neo4jWorkspace.class).info("using db " + db);

        // Get neo4j stuffs
        GraphDatabaseFactory factory = new GraphDatabaseFactory();
        database = factory.newEmbeddedDatabase(db);
        operation = GlobalGraphOperations.at(database);

        views = new ViewsManager();
        views.load(settings);
        layoutML = views.getFirst().getLayout();

        // Setup graph model
        Neo4jGraphModelIO gio = new Neo4jGraphModelIO();
        gio.read(SimpleFile.read(settings.getModelPath()));
        Neo4jGraphModel typeModel = gio.getGraphModel();

        // Build graph
        INeo4jGraphReader r = views.getFirst().getFilter();
        Graph<IPropertyNode, IPropertyEdge> graph = r.read(operation, typeModel);
        Logger.getLogger(Neo4jWorkspace.class).info("filtered graph has " + graph.getVertexCount() + " nodes using db " + db);

        // Build topology using graph typing model
        String topologyName = views.getFirst().getTopologyName();
        Neo4jTopologyFactory tfact = new Neo4jTopologyFactory();
        topology = tfact.newTopology(topologyName, graph, typeModel);

        // Edit topology according to dataprism
        Dataprism dp = loadDataprism(settings);
        if (dp != null)
            ((Neo4jTopology)topology).edit().apply(dp);

        // Build layout model
        modelFactory = new Neo4jModelFactory(gio.getGraphModel());
        model = (IHierarchicalModel)modelFactory.getLayoutModel(topology);
        edgeModel = model.getEdgeModel();
        applyLayoutMLConfiguration(model, layoutML);
       
        // Build layout
        layoutFactory = new Neo4jLayoutFactory(typeModel);
        if (layoutML != null) {
            Map<String, String> mapping = extractModelLayoutMapping(layoutML);
            layoutFactory.setModelLayoutMapping(mapping);
        }

        layout = layoutFactory.getLayout(model);
        layout.getTubeLayout().setEdgePostProcess(null);

        // finalize workspace
        annotationModel = new AnnotationModel();
        metadata = null;
        setName(settings.getName());
        loadMapIfExist();
        
        this.configuration = new ConfigurationFacade(this);
    }
    
    
    public Neo4jWorkspace(GraphDatabaseService database, GlobalGraphOperations operation, Neo4jTopology topology, IHierarchicalModel model, IHierarchicalEdgeModel tubeModel, AnnotationModel amodel, IHierarchicalLayout layout, ViewsManager views, Layout layoutML, Map<String, Object> metadata) {
        this.database = database;
        this.operation = operation;
        this.topology = topology;
        this.model = model;
        this.annotationModel = amodel;
        this.layout = layout;
        this.edgeModel = tubeModel;
        this.metadata = metadata;
        this.layoutML = layoutML;
        this.configuration = new ConfigurationFacade(this);
    }

    @Override
	public void shutdown() {
        if(database!=null)
            database.shutdown();
    }

    public Neo4jTopology getNeo4jTopology() {
        return (Neo4jTopology) topology;
    }

    public GraphDatabaseService getDatabase() {
        return database;
    }

    public GlobalGraphOperations getOperation() {
        return operation;
    }

    public ViewsManager getViews() {
        return views;
    }
    
    /* */
    

    /**
     * Initialize a renderer with edge coloring policy
     * 
     * @see IDesigner.configureComputationPolicies(final Workspace workspace)
     */
    @Override
	public void initializeRenderer(IDisplay display) {
        final Neo4jLouposcopeLayer nll = new Neo4jLouposcopeLayer(model, display);
        pluginRenderer = new PluginLayeredRenderer(display, model, edgeModel, annotationModel){
            @Override
			public void initLayerLouposcope(IDisplay display, IHierarchicalModel model) {
                Neo4jRelationshipRendererSettings s;
                // TODO: should not configure louposcope through renderers
                louposcopeLayer = nll;
                addLayer(louposcopeLayer);
            }
        };
        setupHitPolicy();
        Neo4jRelationshipRendererSettings s = setupEdgeRenderer();
        nll.setEdgeRendererSettings(s);
    }

    public HierarchicalHitPolicy setupHitPolicy() {
        HierarchicalHitPolicy policy = (HierarchicalHitPolicy) pluginRenderer.getMainLayer().getHitProcessor();
        policy.setHitSlot(false);
        return policy;
    }

    protected Neo4jRelationshipRendererSettings setupEdgeRenderer() {
        Neo4jRelationshipRendererSettings edgeRendererSettings = new Neo4jRelationshipRendererSettings();
        edgeRendererSettings.configureEdgeTypeColors(getNeo4jTopology().getGraph().getEdges());

        setupEdgeRenderer(edgeRendererSettings);
        return edgeRendererSettings;
    }

    /**
     * Initialize a layout runner configured with the available layout ML
     * configuration
     */
    @Override
	protected ILayoutRunner getOrCreateRunner(IHierarchicalLayout root, IView view) {
        if (runner == null) {
            //runner = LayoutRunnerLookup.get(root, view);
            runner = new LayoutRunner(root, view);
            runner.setLayoutML(layoutML);
        }
        return runner;
    }
    
    /* */

    public static Dataprism loadDataprism(Neo4jWorkspaceSettings settings) throws Exception {
        Object o = handler.unmarshall(settings.getDataprismFile());
        if (o != null)
            return (Dataprism) o;
        else
            return null;
    }

    public static <V, E> Neo4jTopology loadTopology(Neo4jWorkspaceSettings settings) throws Exception {
        String db = DatabaseConfiguration.readDatabase(settings);
        Logger.getLogger(Neo4jWorkspace.class).info("using db " + db);

        // Get neo4j stuffs
        GraphDatabaseFactory factory = new GraphDatabaseFactory();
        GraphDatabaseService database = factory.newEmbeddedDatabase(db);
        GlobalGraphOperations operation = GlobalGraphOperations.at(database);

        ViewsManager views = new ViewsManager();
        views.load(settings);

        // Setup graph model
        Neo4jGraphModelIO gio = new Neo4jGraphModelIO();
        gio.read(SimpleFile.read(settings.getModelPath()));
        Neo4jGraphModel typeModel = gio.getGraphModel();

        // Build graph
        INeo4jGraphReader r = views.getFirst().getFilter();
        Graph<IPropertyNode, IPropertyEdge> graph = r.read(operation, typeModel);
        Logger.getLogger(Neo4jWorkspace.class).info("filtered graph has " + graph.getVertexCount() + " nodes using db " + db);

        // Build topology using graph typing model
        String topologyName = views.getFirst().getTopologyName();
        Neo4jTopologyFactory tfact = new Neo4jTopologyFactory();
        Neo4jTopology topology = tfact.newTopology(topologyName, graph, typeModel);

        return topology;
    }
    
    /* */
    protected GraphDatabaseService database;
    protected GlobalGraphOperations operation;
    protected ViewsManager views;
    protected Layout layoutML;

    static JAXBHandler handler = new JAXBHandler(Dataprism.class.getPackage().getName());

    private static final long serialVersionUID = 8452790967698974359L;
}

package org.datagr4m.workspace;

import java.util.Map;

import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.renderer.policy.IRenderingPolicy;
import org.datagr4m.topology.Topology;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.model.annotations.AnnotationModel;
import org.datagr4m.workspace.Workspace.WorkspaceFiles;
import org.datagr4m.workspace.configuration.ConfigurationFacade;


public interface IWorkspace {
	public void shutdown();
	public void initializeRenderer(IDisplay display);
	public void createView(IDisplay display);

	public Topology<?, ?> getTopology();
	public IHierarchicalModel getModel();
	public IHierarchicalEdgeModel getEdgeModel();
	public AnnotationModel getAnnotationModel();
	public IHierarchicalLayout getLayout();
	public PluginLayeredRenderer getRenderer();

	/** create or get a runner able to notify the given view for refresh*/
	public ILayoutRunner getRunner(IView view);
	/** create or get a runner.*/
	public ILayoutRunner getRunner();
	public ILayoutRunner getRunner(IHierarchicalLayout root, IView view);

	public Map<String, Object> getMetadata();

	public void setMetadata(Map<String, Object> metadata);

	public ConfigurationFacade getConfiguration();
	public WorkspaceFiles getFiles();

	public String getName();
	public void setName(String name);

	/** A simple flag indicating if this workspace has some already defined coordinates,
	 * in other words if the workspace contained a file storing a position for each item.
	 */
	public boolean isCoordinatesAvailable();
	public void setCoordinatesAvailable(boolean coordinatesAvailable);
	
	public IHierarchicalModelFactory getHierarchicalLayoutModelFactory();    
    public IHierarchicalLayoutFactory getHierarchicalLayoutFactory();    
    public IRenderingPolicy getRenderingPolicy();    
    public void setModelFactory(IHierarchicalModelFactory modelFactory);
	public void setLayoutFactory(IHierarchicalLayoutFactory layoutFactory);
	public void setRenderingPolicy(IRenderingPolicy renderingPolicy);

}
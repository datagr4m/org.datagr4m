package org.datagr4m.drawing.layout.hierarchical.graph.edges;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.ILayoutListener;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.bundling.DefaultEdgeBundling;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.bundling.IEdgeBundling;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.post.IEdgePostProcessor;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.post.StratumEdgePostProcess;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalRowLayout;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.monitors.ITimeMonitor;
import org.datagr4m.monitors.TimeMonitor;


/**
 * A hierarchical edge layout allows to compute a path for each edge given by
 * a hierarchical edge model. It is made of three major steps:
 * <ul>
 * <li>The {@link IItemSlotLayout} computes the start/stop point of an edge by choosing a slot position on an {@link ISlotableItem}.
 * <li>The {@link IEdgeBundling} computes the tube (i.e. edge bundle) layout to reduce the visual complexity of large amount of edges.
 * <li>The {@link IEdgePostProcessor} may modify the path of some of the edges/tubes.
 * </ul>
 * 
 * @author Martin Pernollet
 */
public class HierarchicalEdgeLayout implements Serializable, IHierarchicalEdgeLayout{
    private static final long serialVersionUID = 5206985950656775071L;

    protected IPathFactory pathFactory = new PathFactory();
    protected IItemSlotLayout itemSlotLayout = new DefaultItemSlotLayout(pathFactory);
    protected IEdgeBundling edgeBundling = new DefaultEdgeBundling();
    protected IEdgePostProcessor edgePostProcess = new StratumEdgePostProcess();
    protected List<ILayoutListener> listeners = new ArrayList<ILayoutListener>();
    
    private ITimeMonitor timeMonitor;
    
    public HierarchicalEdgeLayout(){
        initMonitor();
    }
    
    private void initMonitor() {
        timeMonitor = new TimeMonitor(this);
    }
    
    @Override
    public ITimeMonitor getTimeMonitor() {
        return timeMonitor;
    }
    
    /** Build the complete edge model:
     * <ul>
     * <li> Item slot layout
     * <li> Edge and tube layout
     * <li> Group slot layout
     * </ul>
     */
    @Override
    public void build(IHierarchicalEdgeModel model){
        timeMonitor.startMonitor();
        
        doBuildItemSlotLayout(model);
        doBuildEdgeAndTubeLayout(model);
        doPostProcessTubeAndEdgeLayout(model);
        
        timeMonitor.stopMonitor();
    }
    
    protected void doBuildItemSlotLayout(IHierarchicalEdgeModel model) {
    	if(itemSlotLayout!=null)
    		itemSlotLayout.build(model);
    }
    
    /** 
     * Clear existing tube geometries (path) and rebuild:
     * <ul>
     * <li>tube layout according to their children tube & edge position.
     * <li>edge layout according to their current end points (vertices) position.
     * </ul>
     */
    protected void doBuildEdgeAndTubeLayout(IHierarchicalEdgeModel model){
        if(edgeBundling!=null)
        	edgeBundling.bundle(model);
    }
    
    protected void doPostProcessTubeAndEdgeLayout(IHierarchicalEdgeModel model) throws RuntimeException {
        if(edgePostProcess!=null)
            edgePostProcess.postProcess(model);
    }
    
    /* */

    @Override
    public List<ILayoutListener> getListeners() {
        return listeners;
    }

    @Override
    public void addListener(ILayoutListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ILayoutListener listener) {
        this.listeners.remove(listener);
    }

    protected void fireOnStepDone(int n, int total){
        for(ILayoutListener listener: listeners)
            listener.onStepDone(n, total);
    }
    
	@Override
	public IItemSlotLayout getItemSlotLayout() {
		return itemSlotLayout;
	}

	@Override
	public void setItemSlotLayout(IItemSlotLayout itemSlotLayout) {
		this.itemSlotLayout = itemSlotLayout;
	}

	@Override
	public IEdgeBundling getEdgeBundler() {
		return edgeBundling;
	}

	@Override
	public void setEdgeBundler(IEdgeBundling edgeBundler) {
		this.edgeBundling = edgeBundler;
	}

	@Override
	public IEdgePostProcessor getEdgePostProcess() {
		return edgePostProcess;
	}

	@Override
	public void setEdgePostProcess(IEdgePostProcessor edgePostProcess) {
		this.edgePostProcess = edgePostProcess;
	}
}

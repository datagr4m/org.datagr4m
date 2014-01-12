package org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.datagr4m.drawing.model.slots.ISlotableSetModel;
import org.datagr4m.topology.Topology;


public interface IHierarchicalEdgeModel extends ISlotableSetModel{
    /**
     * Forbids to create a tube between two items.
     */
    public void addNoTube(String from, String to);
    
    /** 
     * Supports a list of pairs as input to setup an actual list of tubeable edges.
     */
    public void build(Collection<Pair<IBoundedItem, IBoundedItem>> links);

    /**
     * Read the list of edges found in the topology global graph,
     * in order to extract actual network informations
     */
    public <V,E> void build(Topology<V, E> topology, IHierarchicalNodeModel model);

    /**
     * Verify the existence of a parent tube, and create one if it does not exists yet.
     * Recursively creates parent tubes for each created tube.
     */
    public IEdge build(Pair<IBoundedItem, IBoundedItem> link, Object srcInterface, Object trgInterface, IEdgeInfo info);

    /****/

    public boolean isFullRoot(IHierarchicalEdge tube);
    public boolean isNoRoot(IHierarchicalEdge tube);
    public boolean isPartialRoot(IHierarchicalEdge tube);
    public boolean isSourceRoot(IHierarchicalEdge tube);
    public boolean isTargetRoot(IHierarchicalEdge tube);
    public Tube findRoot(IEdge tube);

    /** 
     * Return tubes that link a group just below the root model, to another root group
     * just below the root model.
     */
    public List<Tube> getRootTubes();

    /** 
     * Tubes and edges that are not held by a root tube, i.e. some tube and edge
     * that are internal to a group.
     */
    public List<IEdge> getInternalTubesAndEdges();

    public List<IEdge> getRawEdges();

    public void changeLayoutModels(Map<IBoundedItem, IBoundedItem> todo);
    public List<IEdge> getFlattenList();
    public void toConsole();

    
    public List<IBoundedItem> findItemsBetween(IBoundedItem in, IBoundedItem out);

    /**
     * Returns true if at least one link exists between the two input items
     */
    public boolean hasLink(IBoundedItem in, IBoundedItem out);
    
    public void restore();
    public boolean backupedOnce();
    public void backup();
    
    /** 
     * Set all edge state to NONE
     */
    public void unselectAll();

    public LabelMode getLabelMode();
    public void setLabelMode(LabelMode mode);
    
    
}
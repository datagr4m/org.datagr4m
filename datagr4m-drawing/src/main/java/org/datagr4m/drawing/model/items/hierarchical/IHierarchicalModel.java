package org.datagr4m.drawing.model.items.hierarchical;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone.StandaloneForce;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.explorer.ICollapsable;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.zones.ZoningModel;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.jzy3d.maths.Coord2d;


public interface IHierarchicalModel extends IBoundedItem, ICollapsable{
    /** 
     * Returns the parent of the current model, or null if the model is a root.
     */
    @Override
	public IHierarchicalModel getParent();
    
    /** 
     * Returns the parent of the given item, i.e.:
     * <ul>
     * <li>the current model if item is part of the children
     * <li>a sub-model if this item stand in the current model hierarchy
     * <li>null if the item does not stand in the current model hierarchy
     * <li>null if the current model is a root
     * </ul>
     */
    public IHierarchicalModel getParent(IBoundedItem item);

    /**
     * Return the depth of the item, relative to the caller model, or -1 if the
     * item does not stand in this model hierarchy.
     */
    public int getDepth(IBoundedItem item);
    
    /**
     * Return the depth of the current model, that is the number of model before finding
     * the one without parent.
     */
    @Override
	public int getDepth();
    
    /**
     * Return the number of level between this model and its farest leaf in the hierarchy.
     */
    public int getMaxLeafLevel();
    
    /** 
     * Returns the children {@link IBoundedItem}, possibly instance of {@link IHierarchicalModel} or
     * sub interfaces.
     */
    public List<IBoundedItem> getChildren();
    public IBoundedItem getChildWithLabel(String label);
    public IBoundedItem getDescendantWithLabel(String label);
    
    /** 
     * Returns true if the item is an immediate child of this model.
     */
    public boolean hasChild(IBoundedItem child);
    public boolean hasChildWithLabel(String label);
    
    public boolean hasDescendant(IBoundedItem child);
    
    
    /**
     * Clear the children list
     */
    public void clearChildren();
    
    
    /**
     * Computes the barycenter of all children
     */
    public Coord2d getChildrenBarycentre();
    
    /** 
     * Returns all descendants of the hierarchy: items and models.
     */
    public List<IBoundedItem> getDescendants();
    
    /** 
     * Returns all descendants of the hierarchy: items and models if required.
     */
    public List<IBoundedItem> getDescendants(boolean addModels);

    /** 
     * Returns all descendant models of the hierarchy.
     */
    public List<IHierarchicalModel> getDescendantModels();
    
    /**
     * Returns the neighbours of the model, i.e. all other children of the parent
     * model, assuming they have been properly initialized by the parent model that
     * built this current model.
     */
    public Collection<IBoundedItem> getNeighbours();
    
    /** 
     * Returns true if the item is a neighbour of this model.
     */
    public boolean hasNeighbour(IBoundedItem child);
    
    /**
     * Return the input data binded to the given item. Result is null if the desired
     * item does not stand in this model hierarchy.
     * 
     * This method is a convenient shortcut for getData(data, true);
     */
    public Object getData(IBoundedItem item);
    
    /**
     * Return the input data binded to the given item. If the representation
     * appears in a submodel, a recursive search is performed if one calls this method
     * with allowRecursiveSearch=true.
     * 
     * Result is null if the desired item does not have a registered representation 
     * in this model hierarchy.
     * 
     * @see getItem() for the reverse mapping, i.e. getting the IBoundedItem representation
     *      for a given object
     */
    public Object getData(IBoundedItem item, boolean allowRecursiveSearch);
    
    
    /**
     * Return the representation for the given input data. Result is null if the desired
     * data does not have a representation standing in this model hierarchy.
     * 
     * This method is a convenient shortcut for getItem(data, true);
     */
    public IBoundedItem getItem(Object data);
    
    /**
     * Return the representation for the given input data. If the representation
     * appears in a submodel, a recursive search is performed if one calls this method
     * with allowRecursiveSearch=true.
     * 
     * Result is null if the desired data does not have a registered representation
     * in this model hierarchy.
     */
    public IBoundedItem getItem(Object data, boolean allowRecursiveSearch);
    
    /**
     * Returns true if the data has been registered and represented by a bounded item
     * in this model hierarchy.
     */
    public boolean isRegistered(Object data);
    
    /**
     * Returns true if the data has been registered and represented by a bounded item
     * in this model.
     * 
     * Performs recursive search in the model hierarchy if the data can't be found
     * among this model children.
     */
    public boolean isRegistered(Object data, boolean allowRecursiveSearch);
    
    /** 
     * Return the absolute position of an item, i.e.:
     * <ul>
     * <li>its raw position in this model, if the item is a child of this model
     * <li>its position relative to the current and all descendant models, if the item is a non immediate child of this model
     * <li>null if the item is not in this model hierarchy
     * </ul>
     */
    public Coord2d getAbsolutePosition(IBoundedItem item);
    

    /**
     * Calling this method on an item will let him consider its geometry is
     * outdated.
     * The implementation will most probably notify the ancestors of the called item
     * which will invalidate all ancestors geometry.
     */
    public void notifyChildrenPositionChanged();
       
    
    /**
     * Update the bounds. If updateChildren is to true, all children
     * model bounds are updated prior to their parent recursively.
     */
    public void refreshBounds(boolean updateChildren);
    
    // IBoundedItem methods
    @Override
	public String getLabel();
    public void setLabel(String label);
    /**
     * The position of an item is relative to its parent position.
     * @see {@link getAbsolutePosition(item)}
     */
    @Override
	public Coord2d getPosition();
    @Override
	public void changePosition(Coord2d c);
    @Override
	public void changePosition(float x, float y);
    @Override
	public void shiftPosition(Coord2d c);

    @Override
	public float getRadialBounds();
    @Override
	public float getRadialBounds(double angle);
    @Override
	public boolean locked();
    @Override
	public void lock();
    @Override
	public void unlock();

    // bounds of a model
    @Override
	public RectangleBounds getRawRectangleBounds();
    
    public void setShape(ItemShape shape, boolean recursive);
    
    public void compact(boolean callParents);
    public void compact();
    
    /**
     * Dumps the structure to console.
     */
    public void toConsole(); 
    
    /** Returns true if a popup menu should be open on item right click.*/
    public boolean hasPopupMenuController();
    /** Show the item's popup menu at the given coordinates. */
    public void showPopupMenuController(IDisplay invoker, int x, int y);
    
    
    public boolean addModelChangeListener(IHierarchicalModelChangeListener listener);
    public boolean removeModelChangeListener(IHierarchicalModelChangeListener listener);
    
    public void setEdgeModel(IHierarchicalEdgeModel model);
    public IHierarchicalEdgeModel getEdgeModel();
    
    public List<StandaloneForce> getForces();
    public void addForce(StandaloneForce force);
    
    public ZoningModel getZoningModel();
    public void setZoningModel(ZoningModel model);
    
    
    public Map<IBoundedItem,IBoundedItem> changeChildrenLayoutModels(IHierarchicalModelFactory factory);

    public void fit(IView view);
}

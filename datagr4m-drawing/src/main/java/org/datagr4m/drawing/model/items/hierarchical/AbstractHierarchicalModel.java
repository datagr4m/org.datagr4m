package org.datagr4m.drawing.model.items.hierarchical;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone.StandaloneForce;
import org.datagr4m.drawing.layout.slots.geometry.DefaultSlotGeometryBuilder;
import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryBuilder;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.AbstractStatefullItem;
import org.datagr4m.drawing.model.items.GeometryCache;
import org.datagr4m.drawing.model.items.GeometryCacheHandler;
import org.datagr4m.drawing.model.items.GeometryFlags;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.IGeometryProcessor;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.explorer.CollapsedModelItem;
import org.datagr4m.drawing.model.items.hierarchical.explorer.IModelExplorer;
import org.datagr4m.drawing.model.items.hierarchical.explorer.ModelTableBrowser;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemPrinterVisitor;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemPrinterVisitor.ToString;
import org.datagr4m.drawing.model.items.zones.ZoningModel;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.datagr4m.viewer.IDisplay;
import org.jzy3d.maths.Coord2d;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public abstract class AbstractHierarchicalModel extends AbstractStatefullItem implements IHierarchicalModel{
    public static float DEFAULT_MARGIN = 40;
    public static float DEFAULT_SLOT_MARGIN = 0;
    public static float DEFAULT_CORRIDOR = 100;
    
    public AbstractHierarchicalModel(){
        this(null);
    }
    
    public AbstractHierarchicalModel(IHierarchicalModel parent){
        this(parent, new ArrayList<IBoundedItem>());
    }
    
    public AbstractHierarchicalModel(IHierarchicalModel parent, List<IBoundedItem> children){
        this(parent, children, new ArrayList<IBoundedItem>());
    }
    
    public AbstractHierarchicalModel(IHierarchicalModel parent, List<IBoundedItem> children, Collection<IBoundedItem> neighbours){
        super();
        this.parent = parent;
        this.children = children;
        this.neighbours = neighbours;
        this.dataToModel = HashBiMap.create();
        this.position = new Coord2d();
        //this.visible = true;
        this.cachedDepth = UNDEFINED_DEPTH;
        this.changeListeners = new ArrayList<IHierarchicalModelChangeListener>();
        this.forces = new ArrayList<StandaloneForce>();
        
        // geometry settings
        this.shape = ItemShape.CIRCLE;
        this.margin = DEFAULT_MARGIN;
        this.corridor = DEFAULT_CORRIDOR;
        this.sideSlots = new HashMap<SlotSide,SlotGroup>();
        this.slotMargin = DEFAULT_SLOT_MARGIN;
        
        // geometry management
        this.cache = new GeometryCache();
        this.flags = new GeometryFlags();
        this.processor = new ModelGeometryProcessor();
        this.handler = new GeometryCacheHandler(this, cache, flags, processor);
    }

    @Override
    public String getDebugInfo(){
        return processor.getNumberOfAbsoluteCoordUpdate()+"";
    }

    
    @Override
    public String getLabel() {
        return label;
    }
    
    @Override
    public void setLabel(String label) {
        this.label = label;
    }
    
    
    @Override
    public boolean isDisplayed(IDisplay display){
        if(!isVisible())
            return false;
        Rectangle2D viewBounds = display.getView().getViewBounds();
        float r = getRadialBounds();
        Ellipse2D e = new Ellipse2D.Double(position.x-r, position.y-r, r*2, r*2);
        return e.intersects(viewBounds);

        //return DefaultBoundedItem.isDisplayed(this, display);
    }

    /************* HIERARCHY *************/
    
    @Override
    public boolean hasChild(IBoundedItem item){
        return children.contains(item);
    }
    
    @Override
    public boolean hasChildWithLabel(String label){
        for(IBoundedItem item: children)
            if(item.getLabel().equals(label))
                return true;
        return false;
    }
    
    @Override
    public IBoundedItem getChildWithLabel(String label){
        for(IBoundedItem item: children)
            if(item.getLabel()!=null && item.getLabel().equals(label))
                return item;
        return null;
    }
    
    @Override
    public IBoundedItem getDescendantWithLabel(String label){
        IBoundedItem child = getChildWithLabel(label);
        if(child!=null)
            return child;
        else{
            for(IBoundedItem i: children){
                if(i instanceof IHierarchicalModel){
                    IHierarchicalModel cm = (IHierarchicalModel)i;
                    child = cm.getDescendantWithLabel(label);
                    if(child!=null)
                        return child;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<IBoundedItem> getChildren() {
        return children;
    }
    
    @Override
    public boolean hasDescendant(IBoundedItem item){
        if(hasChild(item))
            return true;
        else{
            for(IBoundedItem child: getChildren()){
                if(child instanceof IHierarchicalModel){
                    if(((IHierarchicalModel)child).hasDescendant(item))
                        return true;
                }
            }
            return false;
        }
    }
    
    @Override
    public List<IBoundedItem> getDescendants(boolean addModels){
        List<IBoundedItem> descendants = new ArrayList<IBoundedItem>();
        for(IBoundedItem child: getChildren()){
            if(child instanceof IHierarchicalModel){
                IHierarchicalModel childModel = (IHierarchicalModel)child;
                descendants.addAll( childModel.getDescendants(addModels) );
            }
            else
                descendants.add(child);
        }
        if(addModels)
            descendants.add(this);
        
        return descendants;
    }
    
    @Override
    public List<IHierarchicalModel> getDescendantModels(){
        List<IHierarchicalModel> descendants = new ArrayList<IHierarchicalModel>();
        for(IBoundedItem child: getChildren()){
            if(child instanceof IHierarchicalModel){
                IHierarchicalModel childModel = (IHierarchicalModel)child;
                descendants.addAll( childModel.getDescendantModels() );
            }
        }
        descendants.add(this);
        return descendants;
    }
    
    @Override
    public List<IBoundedItem> getDescendants(){
        return getDescendants(false);
    }
    
    
    @Override
    public void clearChildren(){
        clearChildren(true);
    }
    
    public void clearChildren(boolean refreshBounds){
        children.clear();
        if(refreshBounds)
            refreshBounds(true);
    }

    @Override
    public boolean hasNeighbour(IBoundedItem neighbour){
        return neighbours.contains(neighbour);
    }
    
    @Override
    public Collection<IBoundedItem> getNeighbours() {
        return neighbours;
    }
    
    @Override
    public IHierarchicalModel getParent(IBoundedItem item) {
        if(hasChild(item))
            return this;
        else{
            for(IBoundedItem i: children){
                if(i instanceof AbstractHierarchicalModel){
                    IHierarchicalModel submodel = ((AbstractHierarchicalModel)i).getParent(item);
                    if(submodel!=null)
                        return submodel;
                }
            }
        }
        return null;
    }
    
    /** Warning: calling getRoot() for the first time will cache the root.
     * If needing to have
     */
    @Override
    public IHierarchicalModel getRoot(){
        if(cache.getRoot()==null){
            if(parent==null)
                cache.setRoot(this);
            else
                cache.setRoot(computeRoot());
        }
        return cache.getRoot();
    }
    
    @Override
    public IHierarchicalModel computeRoot(){
        if(parent==null)
            return this;
        else
            return parent.computeRoot();
    }
    
    @Override
    public int getDepth(){
        if(cache.getDepth() == UNDEFINED_DEPTH){
            if(parent==null)
                cache.setDepth(0);
            else
                cache.setDepth(parent.getDepth()+1);
        }
        return cache.getDepth();
    }
    
    @Override
    public int getDepth(IBoundedItem item) {
        return getDepth(item, 0);
    }
    
    protected int getDepth(IBoundedItem item, int depth){
        if(this==item)
            return 0;
        if(hasChild(item))
            return depth+1;
        else{
            for(IBoundedItem i: children){
                if(i instanceof AbstractHierarchicalModel){
                    int d = ((AbstractHierarchicalModel)i).getDepth(item, depth+1);
                    if(d!=-1)
                        return d;
                }
            }
        }
        return -1;
    }
    
    /**
     * Return the number of level between this model and its farest leaf in the hierarchy.
     * If no other child than raw IBoundedItem, then leaf level is 0.
     */
    @Override
    public int getMaxLeafLevel(){
        int max = 0;
        for(IBoundedItem i: children){
            if(i instanceof IHierarchicalModel){
                int level = 1 + ((IHierarchicalModel)i).getMaxLeafLevel();
                if(level>max)
                    max = level;
            }
        }
        return max;
    }
    
    /*********/
    
    /**
     * A shortcut for {@link addChild(item, true)}
     */
    public boolean addChild(IBoundedItem item){
        return addChild(item, true);
    }
    
    /**
     * Add an item to the child list, and set the input item parent to this.
     * If set to true, bounds of this model are updated recursively.
     */
    public boolean addChild(IBoundedItem item, boolean refreshBounds){
        boolean s = children.add(item);
        if(s){
            item.setParent(this);
            if(refreshBounds)
                refreshBounds(true);
        }
        return s;
    }

    public boolean addChildren(Collection<IBoundedItem> items){
        return addChildren(items, true);
    }
    
    public boolean removeChild(IBoundedItem item, boolean refreshBounds){
        boolean s = children.remove(item);
        if(s){
            if(refreshBounds)
                refreshBounds(true);
        }
        return s;
    }
    
    public void removeChildren(List<IBoundedItem> items, boolean refreshBounds){
        for(IBoundedItem item: items){
        	removeChild(item, false);
        }
        if(refreshBounds)
            refreshBounds(true);
    }
    
    /** 
     * Returns false if at least one item wasn't added properly to the children list.
     */
    public boolean addChildren(Collection<IBoundedItem> items, boolean refreshBounds){
        boolean s = true;
        for(IBoundedItem item: items)
            s = s && addChild(item, false);        
        if(refreshBounds)
            refreshBounds(true);
        return s;
    }

    public boolean addNeighbour(IBoundedItem item){
        return neighbours.add(item);
    }
    
    public boolean addNeighbours(Collection<IBoundedItem> item){
        return neighbours.addAll(item);
    }
    
    /** 
     * Map an input data to its representation. If the child is not referenced yet, it is
     * added to the children list through {@link addChild()}.
     */
    public void registerChild(Object data, IBoundedItem representation){
        if(!hasChild(representation))
            addChild(representation);
        this.dataToModel.put(data, representation);
    }
    
    /** 
     * Map a representation to the object that it is currently holding.
     */
    public void registerChild(IBoundedItem representation){
        registerChild(representation.getObject(), representation);
    }
    
    @Override
    public Map<IBoundedItem,IBoundedItem> changeChildrenLayoutModels(IHierarchicalModelFactory factory){
        Map<IBoundedItem,IBoundedItem> changeMap = new HashMap<IBoundedItem,IBoundedItem>(children.size());
        for(IBoundedItem child: children){
            IBoundedItem i = factory.getLayoutModel(child.getObject());
            if(i!=null){
                i.setParent(this);
                i.changePosition(child.getPosition());
                changeMap.put(child,i);
            }
        }
        for(IBoundedItem replaced: changeMap.keySet()){
            IBoundedItem newItem = changeMap.get(replaced);
            children.remove(replaced);
            children.add(newItem);
            registerChild(newItem.getObject(), newItem);
        }
        if(changeMap.size()>0){
            fireBoundsDirty();
            //fireParentPositionChanged();
        }
        return changeMap;
    }

    /************* DATA MODEL *************/
    
    @Override
    public Object getData(IBoundedItem item) {
        return getData(item, true);
    }
    
    @Override
    public Object getData(IBoundedItem item, boolean allowRecursiveSearch) {
        if(dataToModel.containsValue(item))
            return dataToModel.inverse().get(item);
        else{
            if(allowRecursiveSearch)
                for(IBoundedItem i: children){
                    if(i instanceof AbstractHierarchicalModel){
                        Object o = ((AbstractHierarchicalModel)i).getData(item);
                        if(o!=null)
                            return o;
                    }
                }
        }
        return null;
    }

    @Override
    public IBoundedItem getItem(Object data) {
        return getItem(data, true);
    }
    
    @Override
    public IBoundedItem getItem(Object data, boolean allowRecursiveSearch) {
        if(dataToModel.containsKey(data))
            return dataToModel.get(data);
        else{
            if(allowRecursiveSearch)
                for(IBoundedItem i: children){
                    if(i instanceof AbstractHierarchicalModel){
                        IBoundedItem o = ((AbstractHierarchicalModel)i).getItem(data, allowRecursiveSearch);
                        if(o!=null)
                            return o;
                    }
                }
        }
        return null;
    }
    
    @Override
    public boolean isRegistered(Object data) {
        return isRegistered(data, true);
    }

    @Override
    public boolean isRegistered(Object data, boolean allowRecursiveSearch) {
        if(dataToModel.containsKey(data))
            return true;
        else{
            if(allowRecursiveSearch)
                for(IBoundedItem i: children){
                    if(i instanceof AbstractHierarchicalModel){
                        boolean found = ((AbstractHierarchicalModel)i).isRegistered(data);
                        if(found)
                            return true;
                    }
                }
        }
        return false;
    }

    /************** POSITION ***********/
    
    @Override
    @Deprecated
    public Coord2d getAbsolutePosition(IBoundedItem item){
        return computeAbsolutePosition(item);
    }
    
    // TODO: cette m�thode est bugg�e si 2 niveau
    protected Coord2d computeAbsolutePosition(IBoundedItem item){
        if(item==this)
            return getPosition();
        else if(hasChild(item))
            return item.getAbsolutePosition();//item.getPosition().add(getPosition());
        else{
            for(IBoundedItem i: getChildren()){
                if(i instanceof AbstractHierarchicalModel){
                    AbstractHierarchicalModel childModel = ((AbstractHierarchicalModel)i);
                    Coord2d relative = childModel.getAbsolutePosition(item);
                    if(relative!=null)
                        return relative.add(getPosition());
                }
            }
        }
        return null;
    }
    
    
    @Override
	public Coord2d getAbsolutePosition(){
        /*if(parentDirty){
            computeAbsolutePosition(absolutePosition);
            //parentDirty = false;
        }
        return absolutePosition;*/
        return handler.getOrUpdateAbsolutePosition();

    }
    
    /*protected void computeAbsolutePosition(Coord2d abs){
        if(parent==null)
            abs.set(getPosition());
        else{
            //abs.set(getPosition());
            //abs.addSelf(getParent().getAbsolutePosition());
            abs.set(getParent().getAbsolutePosition().add(getPosition()));
        }
    }
    protected Coord2d absolutePosition = new Coord2d();*/
    
    
    
    @Override
    public Coord2d getPosition() {
        return position;
    }
    
    @Override
    public void changePosition(Coord2d c){
        changePosition(c.x, c.y);
    }
    
    @Override
    public void changePosition(Point2D c){
        changePosition(c.getX(), c.getY());
    }
    
    @Override
    public void changePosition(double x, double y){
        changePosition((float)x, (float)y);
    }
    
    /**
     * Change position by copying input coordinate values into self coordinate values.
     * Parent is set dirty
     */
    @Override
    public void changePosition(float x, float y){
        position.x = x;
        position.y = y;        
        notifyPositionChanged();
    }
    
    
    @Override
    public void shiftPosition(Coord2d c){
        shiftPosition(c.x, c.y);
    }
    
    /**
     * Change position by adding input coordinate values to self coordinate values.
     */
    @Override
    public void shiftPosition(float x, float y){
        position.addSelf(x, y);
        notifyPositionChanged();
    }
    
    // a shorcut to notify all parents and children
    protected void notifyPositionChanged(){
        flags.onPositionChanged();
        for(IBoundedItem child: children)
            child.notifyParentPositionDirty();
        if(parent!=null){
            parent.notifyChildrenPositionChanged();
            updateBoundsCache();
        }
        fireItemPositionChanged();
    }
    
    @Override
	public void notifyChildrenPositionChanged(){
        //dirtyChildrenPosition = true;
        flags.onChildrenPositionChanged();
        
        if(parent!=null)
            parent.notifyChildrenPositionChanged();
        
        fireBoundsDirty();
    }
    
    @Override
	public void notifyParentPositionDirty(boolean parentDirty){
        this.flags.onParentPositionChanged();
        for(IBoundedItem item: children)
            item.notifyParentPositionDirty(parentDirty);
    }
    
    /** Tells children that parent position changed and that there absolute position
     * is not accurate anymore*/
    public void fireParentPositionChanged(){
        for(IBoundedItem item: children)
            item.notifyParentPositionDirty();
    }
        
    /******** POPUP CONTROLLER **********/
    
    @Override
    public boolean hasPopupMenuController(){
        return canCollapse();
    }
    
    @Override
    public void showPopupMenuController(IDisplay display, int x, int y) {
        if(explorer==null){
            explorer = new ModelTableBrowser(this);
            //explorer = new ModelCollapser(this);
        }
        explorer.showPopupMenuController(display, x, y);
    }
    
    protected IModelExplorer explorer;
    
    
    /******** BOUNDS *********/
    
    @Override 
    public float getMargin(){
        return margin;
    }
    
    @Override 
    public float getCorridor(){
        return corridor;
    }
    
    @Override
    public float getRadialBounds(double angle) {
        return getRadialBounds();
    }
    
    @Override
    public float getRadialBounds() {
        if(isCollapsed())
            return getCollapsedRadialBounds();
        else
            return getExpandedRadialBounds();
    }
    
    @Override
    public RectangleBounds getRawRectangleBounds() {
        if(isCollapsed())
            return getCollapsedRectangleBounds();
        else
            return getExpandedRectangleBounds();
    }
    
    /* TODO EDIT ME TO SUPPORT COLLAPSE/EXPANDED*/
    
    @Override
    public RectangleBounds getRawExternalRectangleBounds() {
        return handler.getOrUpdateRawExternalBounds();
    }
    
    @Override
    public RectangleBounds getRawCorridorRectangleBounds() {
        return handler.getOrUpdateRawCorridorBounds();
    }
    
    @Override
    public RectangleBounds getRelativeRectangleBounds() {  
        return handler.getOrUpdateRelativeBounds();
    }
    
    @Override
    public RectangleBounds getAbsoluteRectangleBounds(){
        return handler.getOrUpdateAbsoluteBounds();
    }

    @Override
    public RectangleBounds getInternalRectangleBounds() {
        return handler.getOrUpdateInternalBounds();
    }

    @Override
    public RectangleBounds getExternalRectangleBounds() {
        return handler.getOrUpdateExternalBounds();//computeExternalRectangleBounds();
    }
    
    @Override
    public RectangleBounds getCorridorRectangleBounds(){
        return handler.getOrUpdateCorridorBounds();
    }
    
    @Override
    public List<Coord2d> getPolygonBounds() {
        throw new RuntimeException("not implemented");
    }
    
    /*****/
    
    @Override
    public float getExpandedRadialBounds() {
        return handler.getOrUpdateRadialBounds();
    }
    
    @Override
    public RectangleBounds getExpandedRectangleBounds(){
        return handler.getOrUpdateRawRectangleBounds();
    }
    
    /**************/
    
    @Override
    public void refreshBounds(boolean updateChildren){
        //updateBoundsCache();
    }
    
    public void updateBoundsCache(){
        /*for(IBoundedItem i: getChildren()){
            if(i instanceof AbstractHierarchicalModel){
                ((AbstractHierarchicalModel)i).updateBoundsCache();
            }
        }*/
        handler.updateRawRectangleBounds();
        handler.updateRadialBounds();
        //flags.dirtyChildrenPosition = false;        
        fireItemBoundsChanged();
    }
    
    /******** MULTI REPRESENTATION (COLLAPSED) *********/
    
    @Override
    public CollapsedModelItem getCollapsedModel(){
        return collapsedModel;
    }
    
    @Override
    public void setCollapsedModel(CollapsedModelItem model){
        this.collapsedModel = model;
    }
    
    @Override
    public boolean isCollapsed() {
        return collapsed;
    }

    @Override
    public void setCollapsed(boolean status) {
        this.collapsed = status;
    }
    
    @Override
    public void toggleCollapsed(){
        this.collapsed = !this.collapsed;
    }
    
    @Override
    public float getCollapsedRadialBounds() {
        return collapsedModel.getRadialBounds();//collapsedBounds;
    }

    @Override
    public RectangleBounds getCollapsedRectangleBounds(){
        return collapsedModel.getRawRectangleBounds();
    }
    
    @Override
    public boolean canCollapse(){
        return canCollapse;
    }
    
    @Override
    public void setCanCollapse(boolean value){
        this.canCollapse = value;
    }
    
    @Override
    public boolean canExpand(){
        return canExpand;
    }
    
    @Override
    public void setCanExpand(boolean value){
        this.canExpand = value;
    }
    
    protected CollapsedModelItem collapsedModel = null;
    protected boolean canCollapse = false;
    protected boolean collapsed = false;
    protected float collapsedBounds = 30;
    protected RectangleBounds collapsedRectBounds;
    protected boolean canExpand = true;
    
    /*** COMPACTION ***/

    @Override
	public void compact(){
        compact(true);
    }
    
    @Override
	public void compact(boolean callParents){
        offsetToCenter(getChildren());
        
        if(callParents && parent!=null)
            parent.compact(callParents);
    }
    public void offsetToCenter(Collection<IBoundedItem> items){
        Coord2d bary = getBarycentre(items).mul(-1);
        
        for(IBoundedItem item: items)
            item.shiftPosition(bary);
    }
    
    public Coord2d getBarycentre(Collection<IBoundedItem> items){
        Coord2d mean = new Coord2d();
        for(IBoundedItem item: items)
            mean.addSelf(item.getPosition());
        mean.divSelf(items.size());
        return mean;
    }
        
    /******** LISTEN TO CHANGES *********/

    @Override
	public boolean addModelChangeListener(IHierarchicalModelChangeListener listener){
        return changeListeners.add(listener);
    }

    @Override
	public boolean removeModelChangeListener(IHierarchicalModelChangeListener listener){
        return changeListeners.remove(listener);
    }

    protected void fireBoundsDirty(){
        for(IHierarchicalModelChangeListener listener: changeListeners)
            listener.boundsDirty(this);
    }
    
    /*****************/
    
    @Override
	public void toConsole(){
        ItemPrinterVisitor v = new ItemPrinterVisitor(new ToString(){
            @Override
			public String apply(Object o){
                if(o instanceof IBoundedItem){
                    IBoundedItem i = (IBoundedItem)o;
                    return i.getLabel() + " | " + i.getPosition() + " | " + i.getRawRectangleBounds();
                }
                return o.toString();
            }
        });
        v.visit(this);
    }
    
    @Override
    public Coord2d getChildrenBarycentre(){
        Coord2d mean = new Coord2d();
        for(IBoundedItem item: getChildren())
            mean.addSelf(item.getPosition());
        mean.divSelf(getChildren().size());
        return mean;
    }
    
    /**********************/
    
    @Override
    public List<StandaloneForce> getForces(){
        return forces;
    }
    
    @Override
    public void addForce(StandaloneForce force){
        forces.add(force);
    }

    /*public boolean equals(Object o){
        if(o instanceof IBoundedItem){
            Object data = getObject();
            Object data2 = ((IBoundedItem)o).getObject();
            if(data!=null && data2!=null){
                return data.equals(data2);
            }
            else
                return super.equals(o);
        }
        else
            return super.equals(o);
    }
    
    public int hashCode(){
        Object data = getObject();
        if(data!=null)
            return data.hashCode();
        return super.hashCode();
    }*/
    
    /********* SLOTS *********/
    
    @Override
    public Coord2d getSlotableCenter() {
        return getAbsolutePosition();
    }
    
    @Override
    public Rectangle2D getSlotableBounds(){
        return getRawRectangleBounds().cloneAsRectangle2D(); //TODO: clone pas bon!!
    }
    
    @Override
    public float getSlotMargin() {
        return slotMargin;
    }
    
    @Override
    public Collection<SlotGroup> getSlotGroups(){
        return sideSlots.values();
    }
    
    @Override
    public SlotGroup getSlotGroup(SlotSide side){
        return sideSlots.get(side);
    }
    
    @Override
    public void addSlots(int nNorth, int nSouth, int nEast, int nWest){
        clearSlots();
        addNorthSlot(nNorth);
        addSouthSlot(nSouth);
        addEastSlot(nEast);
        addWestSlot(nWest);
    }

    @Override
    public void addNorthSlot(int number){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.NORTH, number, buildNorthSlotCenter(), (float)getSlotableBounds().getWidth(), getSlotMargin());
    }
    
    @Override
    public void addSouthSlot(int number){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.SOUTH, number, buildSouthSlotCenter(), (float)getSlotableBounds().getWidth(), getSlotMargin());
    }
    
    @Override
    public void addEastSlot(int number){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.EAST, number, buildEastSlotCenter(), getSlotMargin(), (float)getSlotableBounds().getHeight());
    }

    @Override
    public void addWestSlot(int number){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.WEST, number, buildWestSlotCenter(), getSlotMargin(), (float)getSlotableBounds().getHeight());
    }
    
    @Override
    public void addNorthSlot(Collection<SlotTarget> targets){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.NORTH, targets, buildNorthSlotCenter(), (float)getSlotableBounds().getWidth(), getSlotMargin());
    }
    
    @Override
    public void addSouthSlot(Collection<SlotTarget> targets){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.SOUTH, targets, buildSouthSlotCenter(), (float)getSlotableBounds().getWidth(), getSlotMargin());
    }
    
    @Override
    public void addEastSlot(Collection<SlotTarget> targets){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an item having a maring="+getSlotMargin());
        addSlot(SlotSide.EAST, targets, buildEastSlotCenter(), getSlotMargin(), (float)getSlotableBounds().getHeight());
    }
    
    @Override
    public void addWestSlot(Collection<SlotTarget> targets){
        if(getSlotMargin() == 0)
            throw new IllegalArgumentException("trying to add a slot for an obstacle having a maring="+slotMargin);
        addSlot(SlotSide.WEST, targets, buildWestSlotCenter(), getSlotMargin(), (float)getSlotableBounds().getHeight());
    }
    
    public void addSlot(SlotSide side, int number, Point2D center, float width, float height){
        sideSlots.put(side, new SlotGroup(SLOT_GEOMETRY_BUILDER, this, number, side, center, width, height));
    }
    
    public void addSlot(SlotSide side, Collection<SlotTarget> targets, Point2D center, float width, float height){
        sideSlots.put(side, new SlotGroup(SLOT_GEOMETRY_BUILDER, this, targets, side, center, width, height));
    }
    
    @Override
	public void clearSlots(){
        sideSlots.clear();
    }
    
    protected Point2D buildNorthSlotCenter(){
        return new Point2D.Double(getSlotableCenter().x, getSlotableCenter().y - (getSlotableBounds().getHeight()/2+getSlotMargin()/2));
    }
    protected Point2D buildSouthSlotCenter(){
        return new Point2D.Double(getSlotableCenter().x, getSlotableCenter().y + (getSlotableBounds().getHeight()/2+getSlotMargin()/2));
    }
    protected Point2D buildEastSlotCenter(){
        return new Point2D.Double(getSlotableCenter().x + (getSlotableBounds().getWidth()/2+getSlotMargin()/2), getSlotableCenter().y);
    }
    protected Point2D buildWestSlotCenter(){
        return new Point2D.Double(getSlotableCenter().x - (getSlotableBounds().getWidth()/2+getSlotMargin()/2), getSlotableCenter().y);
    }
    
    
    /* */

    @Override
    public IHierarchicalEdgeModel getEdgeModel() {
        return tubeModel;
    }

    @Override
    public void setEdgeModel(IHierarchicalEdgeModel tubeModel) {
        this.tubeModel = tubeModel;
    }
    
    @Override
    public ZoningModel getZoningModel(){
        return model;
    }
    
    @Override
    public void setZoningModel(ZoningModel model){
        this.model = model;
    }

    /* */

    protected List<IBoundedItem> children;
    
    protected ZoningModel model;

    protected Collection<IBoundedItem> neighbours;
    protected BiMap<Object,IBoundedItem> dataToModel;
    
    protected IHierarchicalEdgeModel tubeModel;
    
    protected String label;
    protected Coord2d position;
    protected float margin;
    protected float corridor;
    
    protected static int UNDEFINED_DEPTH = -1;
    protected int cachedDepth = UNDEFINED_DEPTH;
    protected IHierarchicalModel cachedRoot;
    
    protected RectangleBounds cachedRectangleBounds = new RectangleBounds(0,0);
    protected float cachedRadialBounds = 0;
    
    //protected boolean dirtyChildrenPosition = true;
    
    protected List<IHierarchicalModelChangeListener> changeListeners;
    
    private static final long serialVersionUID = 7722574121824752558L;
    
    protected List<StandaloneForce> forces;
    
    
    protected Map<SlotSide,SlotGroup> sideSlots;
    protected float slotMargin;    
    protected static ISlotGeometryBuilder SLOT_GEOMETRY_BUILDER = new DefaultSlotGeometryBuilder();


    protected GeometryCache cache;
    protected GeometryFlags flags;
    protected GeometryCacheHandler handler;
    protected IGeometryProcessor processor;
    
}

package org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.model.items.BoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.annotations.ClickableRectangleAnnotationItem;
import org.datagr4m.drawing.model.items.annotations.IClickableItemAnnotation;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.IIconHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.IconHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.Edge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.tree.TreeModel;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;
import org.datagr4m.utils.StringUtils;


public class Tube implements IHierarchicalEdge {
    
/************/
    
    protected IPath backup = null;
    
    public void backupPath(){
        backup = (IPath)path.clone();
    }
    
    public boolean hasBackup(){
        return (backup != null);
    }
    
    public void restorePath(){
        path = (LockablePath)backup.clone();
    }
    
    /************/
    
    public Tube(IBoundedItem source, IBoundedItem target, IPathFactory factory) {
        this.sourceItem = source;
        this.targetItem = target;
        this.sourceObstacle = TubeUtils.buildObstacle(source);
        this.targetObstacle = TubeUtils.buildObstacle(target);
        this.children = new ArrayList<IEdge>();
        this.path = factory.newPath();
        this.path.setForbidDuplicates(false);

        this.state = ItemState.none(); // TODO: use static instance

        // for computing a commutative equality easily
        buildPairIdentifier();
    }
    
    protected void buildPairIdentifier(){
        this.vertexPair = new CommutativePair<IBoundedItem>(sourceItem, targetItem);
    }
    
    @Override
	public void editSource(IBoundedItem sourceItem, IPathObstacle sourceObstacle){
        this.sourceItem = sourceItem;
        this.sourceObstacle = sourceObstacle;
        buildPairIdentifier();
    }
    
    @Override
	public void editTarget(IBoundedItem targetItem, IPathObstacle targetObstacle){
        this.targetItem = targetItem;
        this.targetObstacle = targetObstacle;
        buildPairIdentifier();
    }
    
    

    // TODO: cache
    public List<IEdge> flatten() {
        List<IEdge> flattenList = new ArrayList<IEdge>();
        for (IEdge e : children) {
            if (e instanceof Tube) {
                flattenList.addAll(((Tube) e).flatten());
            } else
                flattenList.add(e);
        }
        flattenList.add(this);
        return flattenList;
    }

    /******* HIERARCHICAL TUBE METHODS *******/

    /**
     * Add a child, with a priliminary check, ensuring the child can really be a
     * child of this tube.
     */
    @Override
	public void addChild(IEdge edge) {
        if (!(acceptChild(edge)))
            throw new IllegalArgumentException("Incompatible parent groups: wish to add " + "\nsource:" + edge.getSourceItem().getObject() + "\ntarget:" + edge.getSourceItem().getObject() + "\nto existing tube:" + this);

        children.add(edge);
        edge.setParent(this);

        if (HierarchicalEdgeModel.DEBUG)
            Logger.getLogger(Tube.class).info("TUBE.ADDCHILD:" + edge);
    }

    @Override
    public boolean acceptChild(IEdge edge) {
        boolean ok1 = edge.getSourceItem().getParent() == sourceItem && edge.getTargetItem().getParent() == targetItem;
        boolean ok2 = edge.getSourceItem().getParent() == targetItem && edge.getTargetItem().getParent() == sourceItem;

        boolean ok3 = edge.getSourceItem() == sourceItem && edge.getTargetItem().getParent() == targetItem;
        boolean ok4 = edge.getSourceItem().getParent() == sourceItem && edge.getTargetItem() == targetItem;

        boolean ok5 = edge.getSourceItem() == targetItem && edge.getTargetItem().getParent() == sourceItem;
        boolean ok6 = edge.getSourceItem().getParent() == targetItem && edge.getTargetItem() == sourceItem;

        // TODO: optimiser, pas la peine de tout calculer
        return (ok1 || ok2 || ok3 || ok4 || ok5 || ok6);
    }

    @Override
    public List<IEdge> getChildren() {
        return children;
    }

    @Override
    public boolean hasChildren(IEdge child) {
        return children.contains(child);
    }

    @Override
    public boolean hasDescendant(IEdge child) {
        for (IEdge edge : children) {
            if (edge instanceof Tube) {
                if (((Tube) edge).hasDescendant(child))
                    return true;
            } else {
                if (edge == child)
                    return true;
            }
        }
        return false;
    }

    @Override
    public Tube getParent() {
        return this.parent;
    }

    @Override
    public void setParent(Tube parent) {
        this.parent = parent;
    }
    
    @Override
    public boolean isOriented() {
        return false;
    }

    @Override
    public boolean isFlipped() {
        return flipped;
    }

    @Override
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    
    @Override
    public void toggleFlipped() {
        this.flipped = !flipped;
    }
    
    @Override
    public boolean isParentFlipped(){
        /*Tube parent = getParent();
        int nflip = 0;
        while(parent!=null){
            if(parent.isFlipped())
                nflip++;
            parent = parent.getParent();
            //break;
        }
        //return nflip%2!=0;*/
        return getParent()==null?false:getParent().isFlipped();
    }
    
    /******* TUBE INFO ********/

    /**
     * Return the total number of raw edges held by this tube and its subtubes.
     */
    public int getWidth() {
        int width = 0; // TODO: cache tube width
        for (IEdge child : children) {
            if (child instanceof Tube) {
                width += ((Tube) child).getWidth();
            } else
                width += 1;
        }
        return width;
    }

    public List<IEdgeInfo> flattenInfos() {
        List<IEdgeInfo> infos = new ArrayList<IEdgeInfo>();

        for (IEdge child : children) {
            if (child instanceof Tube) {
                infos.addAll(((Tube) child).flattenInfos());
            } else if (child instanceof Edge) {
                infos.add(child.getEdgeInfo());
            }
        }
        return infos;
    }

    /******* TUBE DATA ********/

    public TubeSlot getSourceSlot() {
        return sourceSlot;
    }

    public TubeSlot getTargetSlot() {
        return targetSlot;
    }

    /**** TUBE OUTPUT HIERARCHY ****/

    /**
     * Return a {@link TreeModel} to explore the hierarchy of pathes
     * from the tube source side.
     */
    public TreeModel getSourceItemHierarchy(){
        //if(sourceItemTree==null)
        //    sourceItemTree = hierarchy(sourceItem);
        return hierarchy(sourceItem);
    }

    /**
     * Return a {@link TreeModel} to explore the hierarchy of pathes
     * from the tube target side.
     */
    public TreeModel getTargetItemHierarchy(){
        //if(targetItemTree==null)
        //    targetItemTree = hierarchy(targetItem);
        return hierarchy(targetItem);
    }
    
    protected TreeModel hierarchy(IBoundedItem item){
        TreeModel model = new TreeModel(); // will be updated to parent model by caller
        model.setObject(item.getObject());
                
        // -------------------------------------
        // Cas o� l'item de d�part est un modele
        if(item instanceof IHierarchicalNodeModel){
            
            for(IEdge tubeChild: children){                
                // --------------------
                // creation d'un niveau en plus
                if(tubeChild instanceof Tube){
                    Tube childTube = (Tube)tubeChild;
                    
                    // un tube enfant comporte forc�ment des cibles ayant l'item source comme parent
                    IBoundedItem childSourceParent = tubeChild.getSourceItem().getParent();
                    if(/*childSourceParent == item*/isSameOrDescendant(childSourceParent, item)){
                        IHierarchicalNodeModel childSourceTree = childTube.getSourceItemHierarchy();
                        model.addChild(childSourceTree, false);
                    }
                    IBoundedItem childTargetParent = tubeChild.getTargetItem().getParent();
                    if(/*childTargetParent == item*/isSameOrDescendant(childTargetParent, item)){
                        IHierarchicalNodeModel childTargetTree = childTube.getTargetItemHierarchy();
                        model.addChild(childTargetTree, false);
                    }

                }
                
                // --------------------
                // on a des arc bruts, avec des device au bout, donc on fabrique un node device
                // creation d'une icone de device
                else{
                    // --------------------------------
                    // On r�cup�re toute l'arborescence situ� sous l'item courant
                    IBoundedItem childSourceParent = tubeChild.getSourceItem().getParent();
                    if(/*childSourceParent == item*/isSameOrDescendant(item, childSourceParent)){
                        BoundedItemIcon bii = (BoundedItemIcon)tubeChild.getSourceItem();
                        if(!model.hasChildWithLabel(bii.getLabel())){
                            IIconHierarchicalModel node = IconHierarchicalModel.clone(bii);
                            model.addChild(node);
                        }
                        
                        // add ip
                        if(tubeChild.getEdgeInfo()!=null){
                            if(tubeChild.getState().isSelected()){
                                IconHierarchicalModel node = (IconHierarchicalModel)model.getChildWithLabel(tubeChild.getSourceItem().getLabel());
                                String txt = tubeChild.getEdgeInfo().flattenInfo();
                                IClickableItemAnnotation ipNode = new ClickableRectangleAnnotationItem(this, txt);
                                node.addChild(ipNode);   
                            }
                        }

                    }
                    
                    // --------------------------------
                    // l'item courant pouvant �tre une target et non une source
                    IBoundedItem childTargetParent = tubeChild.getTargetItem().getParent();
                    if(/*childTargetParent == item*/isSameOrDescendant(item, childTargetParent)){
                        BoundedItemIcon bii = (BoundedItemIcon)tubeChild.getTargetItem();
                        if(!model.hasChildWithLabel(bii.getLabel())){
                            IIconHierarchicalModel node = IconHierarchicalModel.clone(bii);
                            
                            model.addChild(node);
                        }
                        
                        // add ip
                        if(tubeChild.getEdgeInfo()!=null){
                            if(tubeChild.getState().isSelected()){
                               // Logger.getLogger(Tube.class).info("building annot");
                                
                            //Tube cTube = (Tube)child;
                                IconHierarchicalModel node = (IconHierarchicalModel)model.getChildWithLabel(tubeChild.getTargetItem().getLabel());
                                String txt = tubeChild.getEdgeInfo().flattenInfo();
                                IClickableItemAnnotation ipNode = new ClickableRectangleAnnotationItem(this, txt);
                                node.addChild(ipNode);   
                            }
                        }
                        
                    }
                    
                    
                }
            }
            return model;
        }
        
        // -------------------------------------
        // si la cible de ce tube est un device directement
        else{
            // adding a node with an item
            BoundedItemIcon bii = (BoundedItemIcon)item;
            if(!model.hasChildWithLabel(bii.getLabel())){
                IIconHierarchicalModel node = IconHierarchicalModel.clone(bii);
                model.addChild(node);
            }
            
            // adding sub nodes with IP
            IconHierarchicalModel node = (IconHierarchicalModel)model.getChildWithLabel(item.getLabel());
            for(IEdge e: getChildren()){
                if(e.getState().isSelected()){
                    if(e.getEdgeInfo()!=null){
                        String txt = e.getEdgeInfo().flattenInfo();
                        IClickableItemAnnotation ipNode = new ClickableRectangleAnnotationItem(this, "txt!"+txt);
                        node.addChild(ipNode);   
                    }
                }
            }
            return model;
        }
    }
    
    protected boolean isSameOrDescendant(IBoundedItem model, IBoundedItem child){
        if(model==child)
            return true;
        else if(model instanceof IHierarchicalNodeModel){
            if(((IHierarchicalNodeModel)model).hasDescendant(child))            
                return true;
            else
                return false;
        }
        else
            return false;
    }
   

    /******* EDGE PATH METHODS *******/

    @Override
    public IBoundedItem getSourceItem() {
        return sourceItem;
    }

    @Override
    public IBoundedItem getTargetItem() {
        return targetItem;
    }

    @Override
    public IPathObstacle getSourceObstacle() {
        return sourceObstacle;
    }

    @Override
    public IPathObstacle getTargetObstacle() {
        return targetObstacle;
    }

    @Override
    public IEdgeInfo getEdgeInfo() {
        return tubeInfo;
    }

    @Override
    public void setEdgeInfo(IEdgeInfo info) {
        tubeInfo = info;
    }
    
    @Override
    public IPath getPathGeometry() {
        return path;
    }

    @Override
    public ItemState getState() {
        return state;
    }

    @Override
    public void setState(ItemState state) {
        this.state = state;
    }

    @Override
    public void setHierarchyState(ItemState state) {
        setState(state);
        for (IEdge child : getChildren()) {
            if (child instanceof Tube)
                ((Tube) child).setHierarchyState(state);
            else
                child.setState(state);
        }
    }
    
    /* POINT INSERTION */
    
    @Override
    public void insertPointHierarchicallyAfter(int pathPointId, Point2D point) throws Exception{
        IPath p = getPathGeometry();
        
        if( (pathPointId < (p.getPointNumber()-1)) && (pathPointId>=0) ){
            int insertionPoint = pathPointId+1;
            Point2D start = p.getPoint(pathPointId);
            Point2D stop  = p.getPoint(insertionPoint);

            insertPointBetween(point, start, stop);
        }
        else{
            String m = "illegal point id, out of path point sequence:" + pathPointId
                     + "\nin " + p;
            throw new Exception(m);
        }
        
    }
    
    @Override
    public void insertPointBetween(Point2D point, Point2D start, Point2D stop) throws Exception{
        IPath p = getPathGeometry();
        int startId = p.getIndex(start);
        int stopId = p.getIndex(stop);
        
        if(startId!=-1 && stopId!=-1){
            p.insertAt(Math.max(startId, stopId), point, POINT_IS_LOCKED);
            
            // insert in children edges or tubes
            for(IEdge edge: getChildren()){
                if(edge instanceof IHierarchicalEdge){
                    if(!isFlipped())
                        ((IHierarchicalEdge)edge).insertPointBetween(point, start, stop);
                    else
                        ((IHierarchicalEdge)edge).insertPointBetween(point, stop, start);
                }
                else{
                    // add point to this child
                    insertBetween(edge, point, start, stop);
                }
            }
        }
        else{
            System.err.println("Tube.insertPointBetween: insertion failed!");
            //throw new RuntimeException("insertion failed");
        }
    }

    protected void insertBetween(IEdge edge, Point2D point, Point2D start, Point2D stop) throws Exception {
        IPath p = edge.getPathGeometry();
        int startId = p.getIndex(start);
        int stopId = p.getIndex(stop);
        
        if(startId!=-1 && stopId!=-1)
            p.insertAt(Math.max(startId, stopId), point, POINT_IS_LOCKED);
        else{
            StringBuffer sb = new StringBuffer();
            sb.append("Could not find at least one point in a child edge\n");
            sb.append("start: " + start.getX() + "," + start.getY() + "\n");
            sb.append("stop: " + stop.getX() + "," + stop.getY() + "\n");
            sb.append("path: " + p + "\n");
            throw new Exception(sb.toString());
        }
    }
    
    protected static boolean POINT_IS_LOCKED = false;

    /******* IDENTITY *******/

    @Override
    public int hashCode() {
        return vertexPair.hashCode();
    }

    /**
     * Two tubes t1 and t2 are considered equals if t1.source=t2.source AND
     * t1.target = t2.target OR t1.source=t2.target AND t1.target = t2.source
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tube)
            return vertexPair.equals(((Tube) obj).vertexPair);
        else
            return false;
    }

    /**************/

    @Override
	public String toString() {
        return toString(0);
    }

    public String toString(int depth) {
        String msg = StringUtils.blanks(depth) + this.getClass().getSimpleName() + ": {" + sourceItem.getObject() + "," + targetItem.getObject() + "}"
        + " (flipped:"+isFlipped()+")" + " (ancestry_flipped:"+isParentFlipped()+")";

        for (IEdge child : children) {
            if (child instanceof Tube) {
                msg += "\n" + ((Tube) child).toString(depth + 2);
            } else if (child instanceof Edge) {
                Edge edge = ((Edge) child);
                msg += "\n" + StringUtils.blanks(depth + 2) + edge + " (flipped:"+child.isFlipped()+")" + " (ancestry_flipped:"+child.isParentFlipped()+")";
            }
        }
        return msg;
    }

    /**************/

    protected CommutativePair<IBoundedItem> vertexPair; // for commutative
                                                        // equality

    protected List<IEdge> children;

    protected IBoundedItem sourceItem;
    protected IBoundedItem targetItem;
    protected IPathObstacle sourceObstacle;
    protected IPathObstacle targetObstacle;
    protected TubeSlot sourceSlot;
    protected TubeSlot targetSlot;
    protected IPath path;
    
    protected IEdgeInfo tubeInfo;
    
    protected TreeModel sourceItemTree;
    protected TreeModel targetItemTree;

    protected ItemState state;

    protected Tube parent;
    protected boolean flipped = false;
    
    private static final long serialVersionUID = -2748553962424834499L;

    @Override
    public Object getSourceInterface() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Object getTargetInterface() {
        throw new RuntimeException("not implemented");
    }
}

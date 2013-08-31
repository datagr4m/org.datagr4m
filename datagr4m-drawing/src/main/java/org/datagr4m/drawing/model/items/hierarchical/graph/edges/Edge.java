package org.datagr4m.drawing.model.items.hierarchical.graph.edges;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.LockablePath;


public class Edge implements IEdge{
    private static final long serialVersionUID = 4092485649525588936L;
    
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
    
    public Edge(IBoundedItem sourceItem, IBoundedItem targetItem, IPathObstacle sourceObstacle, IPathObstacle targetObstacle) {
        this(sourceItem, targetItem, sourceObstacle, targetObstacle, null);
    }
    
    public Edge(IBoundedItem sourceItem, IBoundedItem targetItem, IPathObstacle sourceObstacle, IPathObstacle targetObstacle, LockablePath pathGeometry) {
        this(sourceItem, targetItem, sourceObstacle, targetObstacle, pathGeometry, null);
    }
    
    public Edge(IBoundedItem sourceItem, IBoundedItem targetItem, IPathObstacle sourceObstacle, IPathObstacle targetObstacle, LockablePath pathGeometry, IEdgeInfo info) {
        this(sourceItem, null, targetItem, null, sourceObstacle, targetObstacle, pathGeometry, info);
    }

    public Edge(IBoundedItem sourceItem, Object sourceInterface, IBoundedItem targetItem, Object targetInterface, IPathObstacle sourceObstacle, IPathObstacle targetObstacle, IPath pathGeometry, IEdgeInfo info) {
        this(sourceItem, sourceInterface, targetItem, targetInterface, sourceObstacle, targetObstacle, pathGeometry, info, false);
    }
    
    public Edge(IBoundedItem sourceItem, Object sourceInterface, IBoundedItem targetItem, Object targetInterface, IPathObstacle sourceObstacle, IPathObstacle targetObstacle, IPath pathGeometry, IEdgeInfo info, boolean oriented) {
        editSource(sourceItem, sourceObstacle);
        editTarget(targetItem, targetObstacle);
        this.sourceInterface = sourceInterface;
        this.targetInterface = targetInterface;
        this.oriented = oriented;
        this.path = pathGeometry;
        this.info = info;
        this.state = ItemState.none(); // TODO: use static instance
    }
    
    @Override
	public void editSource(IBoundedItem sourceItem, IPathObstacle sourceObstacle){
        this.sourceItem = sourceItem;
        this.sourceObstacle = sourceObstacle;
    }
    
    @Override
	public void editTarget(IBoundedItem targetItem, IPathObstacle targetObstacle){
        this.targetItem = targetItem;
        this.targetObstacle = targetObstacle;
    }
    
    @Override
    public IBoundedItem getSourceItem() {
        return sourceItem;
    }

    @Override
    public IBoundedItem getTargetItem() {
        return targetItem;
    }
    
    @Override
    public Object getSourceInterface(){
        return sourceInterface;
    }
    
    @Override
    public Object getTargetInterface(){
        return targetInterface;
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
        return info;
    }
    
    @Override
    public void setEdgeInfo(IEdgeInfo info) {
        this.info = info;
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
        this.state= state;
    }
    
    @Override
    public Tube getParent(){
        return this.parent;
    }
    
    @Override
    public void setParent(Tube parent){
        this.parent = parent;
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
    public boolean isOriented() {
        return oriented;
    }
    
    /** Return true if parent is flipped. */
    @Override
    public boolean isParentFlipped(){
        return getParent()==null?false:getParent().isFlipped();
    }
    
    @Override
	public String toString(){
        return this.getClass().getSimpleName() + ": " + getSourceItem().getLabel() + " > " + getTargetItem().getLabel() + "(inf: " + sourceInterface + " > " + targetInterface+")";
    }

    protected IPathObstacle sourceObstacle;
    protected IPathObstacle targetObstacle;
    protected IEdgeInfo info;
    protected IPath path;

    protected IBoundedItem sourceItem;
    protected IBoundedItem targetItem;
    
    protected Object sourceInterface;
    protected Object targetInterface;
    
    protected ItemState state;
    
    protected Tube parent;
    
    protected boolean flipped = false;
    protected boolean oriented = false;

    
}

package org.datagr4m.drawing.model.items;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.listeners.IItemListener;


public abstract class AbstractStatefullItem implements IBoundedItem{
    private static final long serialVersionUID = 8075639255184564793L;
        
    @Override
    public void setParent(IHierarchicalNodeModel parent) {
        this.parent = parent;
    }

    @Override
    public IHierarchicalNodeModel getParent() {
        return parent;
    }
    
    @Override
    public int getDepth(){
        if(parent==null)
            return 0;
        else
            return parent.getDepth()+1;
    }
    
    @Override
    public IHierarchicalNodeModel getRoot(){
        if(parent==null)
            return null;
        else
            return parent.getRoot();
    }
    
    @Override
    public IHierarchicalNodeModel computeRoot(){
        if(parent==null)
            return null;
        else
            return parent.computeRoot();
    }
    
    @Override
    public boolean isRoot(){
        return (parent==null);
    }
    
    /***********/
    
    @Override
    public IHierarchicalNodeModel getFirstCommonAncestor(IBoundedItem sibbling){
        IHierarchicalNodeModel p1 = getParent();
        
        while(p1!=null){
            IHierarchicalNodeModel p2 = sibbling.getParent();
            while(p2!=null){
                if(p1==p2)
                    return p1;
                p2 = p2.getParent();
            }
            p1 = p1.getParent();
        }
        
        return null;
    }

    @Override
    public IHierarchicalNodeModel getChildrenOfAncestor(IHierarchicalNodeModel ancestor){
        IHierarchicalNodeModel p1 = getParent();
        
        while(p1!=null){
            if(ancestor.hasChild(p1))
                return p1;
            else
                p1 = p1.getParent();
        }
        return null;
    }

    /***********/

    @Override
    public void setObject(Object data) {
        this.data = data;
    }

    @Override
    public Object getObject() {
        return data;
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
    public void setState(String state) {
        this.state = new ItemState(state);
    }

    @Override
    public ItemShape getShape(){
        return this.shape;
    }
    
    @Override
    public void setShape(ItemShape shape){
        this.shape = shape;
    }
    
    @Override
    public boolean locked() {
        return locked;
    }

    @Override
    public void lock() {
        locked = true;
    }

    @Override
    public void unlock() {
        locked = false;
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
	public void notifyParentPositionDirty(){
        notifyParentPositionDirty(true);
    }

    @Override
	public void notifyParentPositionDirty(boolean parentDirty){
        flags.onParentPositionChanged();
    }
    
    
    /* */

    @Override
	public void addItemListener(IItemListener listener){
        listeners.add(listener);
    }
    
    @Override
	public void removeItemListener(IItemListener listener){
        listeners.remove(listener);
    }

    protected void fireItemBoundsChanged(){
        for(IItemListener listener: listeners)
            listener.itemBoundsChanged(this);
    }
    
    protected void fireItemPositionChanged(){
        for(IItemListener listener: listeners)
            listener.itemPositionChanged(this);
    }

    /******************/
    
    @Override
	public boolean intersects(IBoundedItem item){
        if(!visible || !item.isVisible())
            return false;
        
        
        if(getShape()==ItemShape.RECTANGLE && item.getShape()==ItemShape.RECTANGLE)
            return intersectsRectangle(this, item);
        else if(getShape()==ItemShape.CIRCLE && item.getShape()==ItemShape.CIRCLE)
            return intersectsCircle(this, item);
        else
            return intersectsRectangle(this, item);
    }
    
    public static boolean intersectsRectangle(IBoundedItem item, IBoundedItem item2){
        RectangleBounds itemBounds = item.getRawRectangleBounds().shiftCenterTo(item.getAbsolutePosition());
        RectangleBounds item2Bounds = item2.getRawRectangleBounds().shiftCenterTo(item2.getAbsolutePosition());
        return itemBounds.intersects(item2Bounds.cloneAsRectangle2D());
    }

    public static boolean intersectsCircle(IBoundedItem item, IBoundedItem item2){
        double distSq = item.getAbsolutePosition().distanceSq(item2.getAbsolutePosition());
        double rads = item.getRadialBounds()+item2.getRadialBounds();
        
        return distSq < (rads*rads);
    }

    
    /******************/

    
    protected boolean locked = false;
    protected ItemState state = new ItemState();
    protected ItemShape shape = ItemShape.CIRCLE;
    protected IHierarchicalNodeModel parent;
    protected Object data;
    protected boolean visible = true;
    //protected boolean parentDirty = true;
    protected GeometryFlags flags = new GeometryFlags();
    protected List<IItemListener> listeners = new ArrayList<IItemListener>(0);
}
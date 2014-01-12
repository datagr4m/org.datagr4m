package org.datagr4m.drawing.viewer.mouse.items;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalNodeLayout;
import org.datagr4m.drawing.layout.runner.ILayoutRunner;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.viewer.mouse.edges.slothit.SlotHitPolicy;
import org.datagr4m.drawing.viewer.mouse.edges.slothit.SlotHitPolicy2;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.mouse.AbstractMouseViewController;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.mouse.hit.IMouseHitModelListener;
import org.datagr4m.viewer.renderer.IRenderer;
import org.jzy3d.maths.Coord2d;


public class MouseItemViewController extends AbstractMouseViewController {
	public MouseItemViewController(IDisplay display, IView view){
		super(display, view);
	}	
	
	@Override
	public void onKeyPressed(char c, int code){
        
    }
	
	@Override
    public boolean onDoubleClick(IRenderer renderer, Point2D screen, Point2D layout) {
        List<IClickableItem> mouseHits = renderer.hit((int)layout.getX(), (int)layout.getY());
        if(mouseHits!=null && mouseHits.size()>0)
            grabObject = (IBoundedItem)mouseHits.get(0);
        else
            grabObject = null;
        
        // Do something with object model
        if(grabObject!=null){
            if(grabObject.locked())
                grabObject.unlock();
            else
                grabObject.lock();
            return true;
        }
        return false;
    }
	
	@Override
	public boolean onLeftClick(IRenderer renderer, Point2D screen, Point2D layout){
	    List<IClickableItem> mouseHits = renderer.hit((int)layout.getX(), (int)layout.getY());
        if(mouseHits!=null && mouseHits.size()>0)
            grabObject = (IBoundedItem)mouseHits.get(0);
        else
            grabObject = null;
        
        // Do something with object model
        if(grabObject!=null){
            fireObjectHit(grabObject.getObject(), screen, layout);
            fireItemHit(grabObject);
            grabObject.lock();
            return true;
        }
        return false;
	}
	
	@Override
    public boolean onRightClick(IRenderer renderer, Point2D screen, Point2D layout) {
	    return false;
    }
	
    @Override
    public boolean onPopupQueryClick(IRenderer renderer, Point2D screen, Point2D layout) {
        return false;
    }

	
	@Override
	public boolean onMouseDragged(Point pt2, Point pt3){
	    if(grabObject!=null){
            final Coord2d mouseShift = new Coord2d(pt2.getX()-pt3.getX(), pt2.getY()-pt3.getY());
            
            if(grabObject.getParent()!=null && grabObject.getParent().getChildren().size()==1){
                fireObjectDragged(grabObject.getObject(), null, pt3);
                fireItemDragged(grabObject);

                doDrag(grabObject.getParent(), mouseShift);
            }
            else{
                fireObjectDragged(grabObject.getObject(), null, pt3);
                fireItemDragged(grabObject);

                doDrag(grabObject, mouseShift);
            }
            return true;
        }
	    return false;
	}
	
	@Override
	public boolean onMouseRelease(){
	    if(grabObject!=null){
            //updateParentPosition(grabObject);
            fireObjectReleased(grabObject.getObject(), null, null);
            fireItemReleased(grabObject);
            grabObject.unlock();
            grabObject = null;
            return true;
        }
	    return false;
	}
	
	@Override
	public boolean onMouseMoved(IRenderer renderer, Point2D mousePoint){
	    /*if(rootModel!=null){
            List<IClickableItem> mouseHits = renderer.hit((int)mousePoint.getX(), (int)mousePoint.getY());

            // TODO: ultra inefficace!
            Collection<IBoundedItem> all = rootModel.getDescendants(true);
            for(IBoundedItem i: all){
                i.setState(ItemState.NONE);
            }
            
            if(mouseHits!=null){
                for(IClickableItem item: mouseHits){
                    //System.out.println("over " + item);
                    //if(!(item instanceof IHierarchicalModel))
                        ((IBoundedItem)item).setState(ItemState.MOUSE_OVER);
                }
            }
            
            prevMouseHits = mouseHits;
            return true;
        }*/
	    return false;
	}
	
	/**********************************/
    
    protected void doDrag(final IBoundedItem item, final Coord2d mouseShift){
        // methode 1: bete et m�chante
        item.shiftPosition(mouseShift);
        
        // methode 2: conservation du barycentre
        /*applyDynamicBarycenterShift(new IModelChange() {
            public IBoundedItem who() {
                return item;
            }
            public void apply() {
                item.getPosition().addSelf(mouseShift);
            }
        });*/
    }
    
    protected void applyDynamicBarycenterShift(IModelChange change){
        // apply parent change and brothers change
        final IHierarchicalNodeModel parent = change.who().getParent();
        if(parent==null){
            change.apply();
        }
        else{
            Coord2d baryBefore = change.who().getParent().getChildrenBarycentre();
            change.apply(); // <<< DO IT!
            Coord2d baryAfter = change.who().getParent().getChildrenBarycentre();
            final Coord2d baryMove = baryAfter.sub(baryBefore);
        
            // We later move the parent, so we apply the inverse move
            // to all children to ensure everybody remains at its position
            Collection<IBoundedItem> children = parent.getChildren();//getDescendants();
            for(IBoundedItem item: children)
                item.shiftPosition(baryMove.mul(-1));
            
            // Apply barycenter shift to the parent, with recursion going toward root
            applyDynamicBarycenterShift(new IModelChange() {
                @Override
				public IBoundedItem who() {
                    return parent;
                }
                @Override
				public void apply() {
                    parent.shiftPosition(baryMove);
                }
            });
        }
    }
    
    public interface IModelChange{
        public void apply();
        public IBoundedItem who();
    }
    
    protected void updateParentPosition(IBoundedItem movedObject){
        IHierarchicalNodeModel parent = movedObject.getParent();
        
        if(parent!=null){
        
            Coord2d mean = parent.getChildrenBarycentre();
            Logger.getLogger(MouseItemViewController.class).info(mean);
            // si les bounds �taient valide avant, le barycentre �tait (0,0), 
            // donc on peut utiliser mean comme vecteur de d�placement
            parent.getPosition().addSelf(mean);
            
            updateParentPosition(parent);
        }
    }
    
    /*protected Coord2d getChildrenBarycentre(IHierarchicalModel parent){
        Coord2d mean = new Coord2d();
        for(IBoundedItem item: parent.getChildren()){
            mean.addSelf(item.getPosition());
        }
        mean.divSelf(parent.getChildren().size());
        return mean;
    }*/
    
    /************************/
	
    protected void fireItemHit(IBoundedItem o){
        for(IMouseHitModelListener listener: objectHitListeners){
            if(listener instanceof org.datagr4m.drawing.viewer.mouse.items.IMouseHitModelListener){
                ((org.datagr4m.drawing.viewer.mouse.items.IMouseHitModelListener)listener).itemHit(o);
            }
        }
    }
    
    protected void fireItemDragged(IBoundedItem o){
        for(IMouseHitModelListener listener: objectHitListeners)
            if(listener instanceof org.datagr4m.drawing.viewer.mouse.items.IMouseHitModelListener){
                ((org.datagr4m.drawing.viewer.mouse.items.IMouseHitModelListener)listener).itemDragged(o);
            }
    }
    
    protected void fireItemReleased(IBoundedItem o){
        for(IMouseHitModelListener listener: objectHitListeners)
            if(listener instanceof org.datagr4m.drawing.viewer.mouse.items.IMouseHitModelListener){
                ((org.datagr4m.drawing.viewer.mouse.items.IMouseHitModelListener)listener).itemReleased(o);
            }
    }
    
    /************************/
    
    public IHierarchicalNodeLayout getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(IHierarchicalNodeLayout rootLayout) {
        this.rootLayout = rootLayout;
    }
    
    public IHierarchicalNodeModel getRootModel() {
        return rootModel;
    }

    public void setRootModel(IHierarchicalNodeModel rootModel) {
        this.rootModel = rootModel;        
        this.allItems = rootModel.getDescendants(true);
    }
    
    public ILayoutRunner getRunner() {
        return runner;
    }

    public void setRunner(ILayoutRunner runner) {
        this.runner = runner;
    }

    public INavigationController getNavigation() {
        return navigation;
    }

    public void setNavigation(INavigationController navigation) {
        this.navigation = navigation;
        slotHitPolicy.setTooltipPlugin(navigation.getTooltipsPlugin());
    }

    protected IBoundedItem grabObject;
	protected IHierarchicalNodeLayout rootLayout;
    protected IHierarchicalNodeModel rootModel;
    protected Collection<IBoundedItem> allItems;

    protected ILayoutRunner runner;
    
    protected INavigationController navigation;
    protected SlotHitPolicy slotHitPolicy = new SlotHitPolicy2();

}

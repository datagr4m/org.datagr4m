package org.datagr4m.drawing.viewer.mouse.edges;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.layout.runner.LayoutUtils;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.context.ContextType;
import org.datagr4m.drawing.navigation.context.NavigationContext;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRenderer;
import org.datagr4m.drawing.renderer.items.shaped.AbstractShapedItemRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.IRenderer;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.TicToc;


public class MouseEdgeViewController extends MouseItemViewController {

    public MouseEdgeViewController(IDisplay display, IView view) {
        super(display, view);
    }

    /* KEYBOARD */

    @Override
    public void onKeyPressed(char c, int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            navigation.apply(new NavigationContext(ContextType.DEFAULT));
            navigation.unselectAll();
        }
    }
    
    /*************** PRESS *******************/

    @Override
    public boolean onLeftClick(IRenderer renderer, Point2D screen, Point2D layout) {
        //boolean isBring = navigation.getContext().is(ContextType.BRING);
        ((JComponent)display).requestFocus();

        if (tubeRenderer != null) {
            tubeRenderer.clearTreeModelCache();
            tubeRenderer.clearTreeLayoutCache();
        }
        
        // ------------------------------
        // compute who's under the click
        List<IClickableItem> mouseHits = renderer.hit((int) layout.getX(), (int) layout.getY());
        Collections.sort(mouseHits, clickComparator);
        
        // ------------------------------------------------
        // analyse click, and select an item to grab if any
        display.refresh();

        if (mouseHits != null && mouseHits.size() > 0) {
            rawClick = mouseHits.get(0);
            fireItemHit(rawClick, mouseHits, screen, layout);

            if (rawClick instanceof IBoundedItem)
                grabObject = (IBoundedItem) rawClick;
            else if(rawClick instanceof ClickedEdge){
                Logger.getLogger(MouseEdgeViewController.class).info("clicked edge: " + rawClick);
                navigation.apply(new NavigationContext(ContextType.EDGE_DETAILS, rawClick));
                grabObject = null;
            }
            else
                grabObject = null;
        } else
            grabObject = null;

        // ------------------------------------------------
        // Do something with the selected item
        if (grabObject != null) {
            if(navigation!=null && keyMemory!=null){
                if (keyMemory.isHold('b')) {
                    if(!(grabObject instanceof IHierarchicalModel)){
                        if(!navigation.getContext().is(ContextType.BRING))
                            doBring(grabObject);
                        else
                            doGo(grabObject, screen, layout);
                    }
                    else
                        Logger.getLogger(this.getClass()).warn("grab was a model: " + grabObject);
                } 
                else if (keyMemory.isHold('l')) {
                    doLouposcope(grabObject);
                }
                /*else if (keyMemory.isHold('f')){
                    doFileOpen(grabObject);
                }*/
                else if (keyMemory.isHold('m')){
                    doBoardOpenBring(grabObject);
                }
                // default mouse press
                else{
                    doObjectClick(grabObject, screen, layout);
                    return true;
                }
            }
            else{
                doObjectClick(grabObject, screen, layout);
                return true;
            }
        }

        return false;
    }
    
    @Override
    public boolean onRightClick(IRenderer renderer, Point2D screen, Point2D layout) {
        return false;
    }
    
    /** Open a popup for each item returned by the renderer hit policy.*/
    @Override
    public boolean onPopupQueryClick(IRenderer renderer, Point2D screen, Point2D layout) {
        List<IClickableItem> mouseHits = renderer.hit((int) layout.getX(), (int) layout.getY());
        Collections.sort(mouseHits, clickComparator);
        
        if (mouseHits != null && mouseHits.size() > 0) {
            
            for(IClickableItem item: mouseHits){
                if(item instanceof IHierarchicalModel){
                    IHierarchicalModel m = (IHierarchicalModel)item;
                    if(m.hasPopupMenuController()) // si cette fonctionnalit� est support�e
                        m.showPopupMenuController(display, (int)screen.getX(), (int)screen.getY());
                }
            }
        }        
        return false;
    }

    /* DRAGGED */

    @Override
    public boolean onMouseDragged(Point pt2, Point pt3) {
        if (rawClick != null)
            fireItemDragged(rawClick, null, null);

        if (grabObject != null) {
            final Coord2d mouseShift = new Coord2d(pt2.getX() - pt3.getX(), pt2.getY() - pt3.getY());

            //Logger.getLogger(this.getClass()).info("onMouseDragged: current"+new Coord2d(pt2)+" prev: " + new Coord2d(pt3));
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
    
    /************* RELEASE ***************/

    @Override
    public boolean onMouseRelease() {
        boolean didClicked = false;

        if (rawClick != null) {
            fireItemReleased(rawClick, null, null);
            rawClick = null;
            didClicked = true;
        }

        if (grabObject != null) {
            // updateParentPosition(grabObject);
            doObjectRelease(grabObject);
            grabObject = null;
            didClicked = true;
        }
        return didClicked;
    }
    
    /************** OVER **************/
    

    @Override
    public boolean onMouseMoved(IRenderer renderer, Point2D mousePoint) {
        if (configuration.isMouseOverOn() && rootModel != null) {
            float manhattan = configuration.getMouseMovedAroundManhattanDistance();
            float around = configuration.getMouseMovedAroundDistanceSensitivity();
            
            // hit appropriate layers, and set to MOUSE_OVER items that intersect mouse click
            // slot are highlighted according to the slot hit policy that defines a strategy
            // to select annotations to be displayed by the tooltip plugin
            boolean foundMouseOver = mouseOverProcessor.process(renderer, mousePoint, slotHitPolicy, manhattan, navigation);
            
            // also trigger slot hit policy if an item is "around", but mouse over, to
            // allow slot to be "near" mouse when mouse pointer is out of the device.
            if(descendants==null)
                descendants = rootModel.getDescendants();
            if(maxradius==-1)
                maxradius = DefaultBoundedItem.getMaxRadius(descendants);
            
            List<IBoundedItem> foundMouseAround = mouseAroundProcessor.hitSlotAround(descendants, Pt.cloneAsCoord2d(mousePoint), maxradius+around, mousePoint, slotHitPolicy, manhattan, navigation);
            
            return foundMouseOver;
        }
        return false;
    }
        
    List<IBoundedItem> descendants;
    protected float maxradius = -1;
    
    /* */

    protected void doBring(IBoundedItem source) {
        navigation.apply(new NavigationContext(ContextType.BRING, source));
    }
    
    protected boolean doGo(IBoundedItem grabObject, Point2D screen, Point2D layout) {
        if (navigation.getContext().is(ContextType.BRING)) {
            navigation.apply(new NavigationContext(ContextType.GO, grabObject));
            return true;
        } else {
            return false;
        }
    }
    
    protected void doLouposcope(IBoundedItem grabObject) {
        navigation.apply(new NavigationContext(ContextType.LOUPOSCOPE, grabObject));
    }
    
    protected void doFileOpen(IBoundedItem grabObject) {
        navigation.apply(new NavigationContext(ContextType.FILE_OPEN, grabObject));
    }
    
    protected void doBoardOpenBring(IBoundedItem source) {
        navigation.apply(new NavigationContext(ContextType.SHOW_DEVICE_BOARD, source));
    }

    protected void doObjectClick(IBoundedItem grabObject, Point2D screen, Point2D layout){
        fireObjectHit(grabObject.getObject(), screen, layout);
        fireItemHit(grabObject);
        //grabObject.setState(ItemState.SELECTED);
        grabObject.setState(ItemState.MOUSE_OVER);
        grabObject.lock();
    }

    protected void doObjectRelease(IBoundedItem grabObject){
        fireObjectReleased(grabObject.getObject(), null, null);
        fireItemReleased(grabObject);
        grabObject.setState(ItemState.NONE);
        grabObject.unlock();
        
        if(!configuration.isEdgeRefreshAtDragEvent()){
            TicToc clock = new TicToc();
            clock.tic();
            layout.goAlgoEdge();
            Logger.getLogger(this.getClass()).info("done edge @ mouse release " + clock.toc() + " s");
            tubeRenderer.setEnable(true);
            AbstractShapedItemRenderer.ALLOW_SLOT_RENDERING = true;
        }
    }
    
    
    
    @Override
    protected void doDrag(final IBoundedItem item, final Coord2d mouseShift) {
        // methode 1: bete et m�chante
        if(!configuration.isDragWithBarycentreUpdate()){
            item.setState(ItemState.MOUSE_OVER);
            
            item.shiftPosition(mouseShift);
            //Logger.getLogger(this.getClass()).info("drag:"+mouseShift+" imply position: " + item.getPosition());
            
            if(configuration.isApplyMouseDragTrick())
                if(item.getParent()!=null){
                    item.shiftPosition(mouseShift);
                    item.getParent().compact(false);
                }
            
            if (layout != null){
                // update layout if no runner is handling layout update
                if(runner == null || (runner!=null && !runner.isRunning()) ){
                    LayoutUtils.updatePairSplines(layout);
                    
                    // will update complete edge layout later (see doObjectRelease)!
                    try{
                        if(configuration.isEdgeRefreshAtDragEvent()){
                            TicToc clock = new TicToc();
                            clock.tic();
                            layout.goAlgoEdge();
                            //Logger.getLogger(this.getClass()).info("done edge @ mouse drag " + clock.toc() + " s");
                            tubeRenderer.setEnable(true);
                            AbstractShapedItemRenderer.ALLOW_SLOT_RENDERING = true;
                        }
                        else{
                            AbstractShapedItemRenderer.ALLOW_SLOT_RENDERING = false;
                            tubeRenderer.setEnable(false);
                        }
                    }
                    catch(RuntimeException e){
                        e.printStackTrace();
                        Logger.getLogger(this.getClass()).error(e);
                    }
                }
            }
            
            if (tubeRenderer != null)
                tubeRenderer.clearTreeLayoutCache();
        }
        
        // methode 2: conservation du barycentre
        else{
            applyDynamicBarycenterShift(new IModelChange() {
                @Override
				public IBoundedItem who() {
                    return item;
                }
                @Override
				public void apply() {
                    item.getPosition().addSelf(mouseShift);
                }
            });
        }
    }
    
    public PluginLayeredRenderer getPluginLayeredRenderer(){
        IRenderer viewRenderer = view.getRenderer();
        if(viewRenderer instanceof PluginLayeredRenderer)
            return (PluginLayeredRenderer)viewRenderer;
        else
            return null;
    }
    
    IHierarchicalLayout layoutToUpdate;

    
    /**********************************/

    protected IHierarchicalLayout layout;
    protected TubeRenderer tubeRenderer;

    public IHierarchicalLayout getLayout() {
        return layout;
    }

    public void setLayout(IHierarchicalLayout tgLayout) {
        this.layout = tgLayout;
    }

    public TubeRenderer getTubeRenderer() {
        return tubeRenderer;
    }

    public void setTubeRenderer(TubeRenderer tubeRenderer) {
        this.tubeRenderer = tubeRenderer;
    }

    protected void applyDynamicBarycenterShift(IModelChange change) {
        // apply parent change and brothers change
        final IHierarchicalModel parent = change.who().getParent();
        if (parent == null) {
            change.apply();
        } else {
            Coord2d baryBefore = change.who().getParent().getChildrenBarycentre();
            change.apply(); // <<< DO IT!
            Coord2d baryAfter = change.who().getParent().getChildrenBarycentre();
            final Coord2d baryMove = baryAfter.sub(baryBefore);

            // We later move the parent, so we apply the inverse move
            // to all children to ensure everybody remains at its position
            Collection<IBoundedItem> children = parent.getChildren();// getDescendants();
            for (IBoundedItem item : children)
                item.shiftPosition(baryMove.mul(-1));

            // Apply barycenter shift to the parent, with recursion going toward
            // root
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

    public interface IModelChange {
        public void apply();

        public IBoundedItem who();
    }

    @Override
	protected void updateParentPosition(IBoundedItem movedObject) {
        IHierarchicalModel parent = movedObject.getParent();

        if (parent != null) {

            Coord2d mean = parent.getChildrenBarycentre();
            //System.out.println(mean);
            // si les bounds �taient valide avant, le barycentre �tait (0,0),
            // donc on peut utiliser mean comme vecteur de d�placement
            parent.getPosition().addSelf(mean);

            updateParentPosition(parent);
        }
    }
    
    public MouseConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(MouseConfiguration configuration) {
        this.configuration = configuration;
    }
    
    /* */

    protected IClickableItem rawClick;    
    
    protected ClickPriorityComparator clickComparator = new ClickPriorityComparator();
    protected MouseOverProcessor mouseOverProcessor = new MouseOverProcessor();
    protected MouseAroundProcessor mouseAroundProcessor = new MouseAroundProcessor();
    protected MouseConfiguration configuration = new MouseConfiguration();
}

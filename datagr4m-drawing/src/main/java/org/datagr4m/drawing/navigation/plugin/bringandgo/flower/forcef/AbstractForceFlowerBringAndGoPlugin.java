package org.datagr4m.drawing.navigation.plugin.bringandgo.flower.forcef;

import java.awt.geom.Rectangle2D;
import java.util.List;

import org.datagr4m.drawing.layout.ItemPositionMap;
import org.datagr4m.drawing.layout.PositionMapTransition;
import org.datagr4m.drawing.layout.hierarchical.flower.StaticFlowerLayout;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.AvatarManager;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.flower.ForceFlowerModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.bringandgo.AbstractBringAndGoPlugin;
import org.datagr4m.drawing.navigation.plugin.bringandgo.IBringAndGoPlugin;
import org.datagr4m.drawing.navigation.plugin.bringandgo.force.ForceBringAndGoLayer;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.animation.IAnimation;
import org.datagr4m.viewer.animation.IAnimationMonitor;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.animation.ViewBoundsAnimation;
import org.datagr4m.viewer.animation.ViewPointAnimation;
import org.datagr4m.viewer.animation.ViewScaleAnimation;
import org.datagr4m.viewer.mouse.ILocalizedMouse;
import org.jzy3d.maths.Coord2d;



public abstract class AbstractForceFlowerBringAndGoPlugin<V, E> extends AbstractBringAndGoPlugin<V,E> implements IBringAndGoPlugin{
    public AbstractForceFlowerBringAndGoPlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalNodeModel model) {
        super(controller, display, layered, animator, mouse, model);
        buildFlower();
    }
    
	protected abstract void buildFlower();
    
    /** Create a FlowerModel, and performs the bring animation */
    @Override
    public void bring(IBoundedItem item) {
        setupModel(item);
        flowerLayout.goAlgo();

        IView view = display.getView();

        RectangleBounds flowerBounds = flowerLayout.getModel().getRawRectangleBounds();
        Rectangle2D targetBounds = flowerBounds.cloneAsRectangle2D();
        Rectangle2D currentBounds = view.getViewBounds();
        // center view to item
        ViewBoundsAnimation va = new ViewBoundsAnimation(view, currentBounds, targetBounds, 300);
        //ViewPointAnimation va = new ViewPointAnimation(view, new Coord2d(view.getCenter()), item.getAbsolutePosition(), 300);
        animator.push(va);
    }

    /** Performs the go animation */
    @Override
    public void go(IBoundedItem item, IAnimationMonitor monitor) {
        // animate inverse transition
        PositionMapTransition t = new PositionMapTransition();
        ItemPositionMap currentPositionMap = new ItemPositionMap();
        currentPositionMap.copyAbsolutePosition(flowerModel.getChildren());
        IAnimation anim = t.transition(currentPositionMap, originalPositionMap, DURATION);

        // switch to default context when animation finishes
        anim.addAnimationMonitor(monitor);

        Coord2d from = currentPositionMap.get(item);
        
        if(item.getLabel().contains("#")){
            int i = item.getLabel().indexOf("#");
            if(i>0){
                String name = item.getLabel().substring(0, i);
                item = originalPositionMap.findByLabel(name);                
            }
        }
        
        Coord2d to = originalPositionMap.get(item);

        // find target, and set it as mouseover for easy animation understanding
        //setState(item.getLabel(), ItemState.MOUSE_OVER);
        ViewPointAnimation va = new ViewPointAnimation(display.getView(), from, to, DURATION);
        ViewScaleAnimation vs = new ViewScaleAnimation(display.getView(), DURATION);
        animator.push(anim);
        animator.push(va);
        animator.push(vs);
    }

    /* BUILD */
    
    /** Setup a flower model for this item's bring and go*/
    @Override
	protected void setupModel(IBoundedItem item){
        // --------------
        // model
        flowerBuilder.build(item);
        flowerModel = flowerBuilder.getFlowerModel();
        
        setupOriginalPositions(flowerModel.getNeighbours());
        setupRendering(flowerModel.getCenter(), flowerModel.getNeighbours());

        
        // --------------
        // layout
        flowerLayout = new StaticFlowerLayout(flowerModel);
        
        // --------------
        // renderer
        ForceBringAndGoLayer layer = new ForceBringAndGoLayer(display, flowerModel);
        layer.setCurrentItem(item);
        layer.setNeighbours(flowerModel.getNeighbours());
        layer.getRendererSettings().getBoundsSettings().setBoundDisplayed(null, false);
        layered.setBringAndGoLayer(layer);        
    }
    
    @Override
	protected void setupOriginalPositions(List<IBoundedItem> neighbours) {
        originalPositionMap = new ItemPositionMap();
        originalPositionMap.copyAbsolutePosition(neighbours);
    }
    
    /* */
    
    protected IForceFlowerBuilder<V,E> flowerBuilder;
    protected ForceFlowerModel<E> flowerModel;
    protected StaticFlowerLayout flowerLayout;
    
    protected ItemPositionMap originalPositionMap;

    protected AvatarManager avatarManager = new AvatarManager();
}

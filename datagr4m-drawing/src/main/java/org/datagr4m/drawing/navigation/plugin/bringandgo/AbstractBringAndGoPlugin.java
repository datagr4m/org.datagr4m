package org.datagr4m.drawing.navigation.plugin.bringandgo;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.drawing.layout.ItemPositionMap;
import org.datagr4m.drawing.layout.PositionMapTransition;
import org.datagr4m.drawing.layout.hierarchical.flower.StaticFlowerLayout;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.AvatarManager;
import org.datagr4m.drawing.model.items.DefaultBoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.flower.StaticFlowerModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.AbstractNavigationPlugin;
import org.datagr4m.drawing.navigation.plugin.bringandgo.flower.staticf.IStaticFlowerBuilder;
import org.datagr4m.drawing.navigation.plugin.bringandgo.force.ForceBringAndGoLayer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.IGraphRendererSettings;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.animation.IAnimation;
import org.datagr4m.viewer.animation.IAnimationMonitor;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.animation.ViewBoundsAnimation;
import org.datagr4m.viewer.animation.ViewPointAnimation;
import org.datagr4m.viewer.animation.ViewScaleAnimation;
import org.datagr4m.viewer.colors.ColorMapRainbow;
import org.datagr4m.viewer.mouse.ILocalizedMouse;
import org.jzy3d.maths.Coord2d;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import edu.uci.ics.jung.graph.Graph;

public abstract class AbstractBringAndGoPlugin<V,E> extends AbstractNavigationPlugin<V,E> implements IBringAndGoPlugin{
    public static int DURATION = 1000;

    public AbstractBringAndGoPlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalModel model) {
        super(controller, display, layered, animator, mouse, model);
        
        
        this.deviceNeighbours = new HashMap<V, List<IBoundedItem>>();
        
        Graph<V,E> graph = getDataModel().getTopology().getGlobalGraph();
        this.deviceNetworks = lookupNetworks(graph);
    }
    
    protected abstract Multimap<V, E> lookupNetworks(Graph<V, E> graph);

	public boolean isEnabled(){
    	/*if(getWorkspace()==null)
    		return false;
    	else if(getWorkspace().isLayer2())
    		return false;
    	else*/
    		return true;
    }
    
    protected void setupRendering(IBoundedItem item, List<IBoundedItem> neighbours) {
        layered.getBringAndGoLayer().setCurrentItem(item);
        layered.getBringAndGoLayer().setNeighbours(neighbours);

        grayPolicy.apply(layered.getMainHierarchicalRenderer());
        
        layered.setVisible(layered.getMainHierarchicalRenderer(), false);
        layered.setHittable(layered.getMainHierarchicalRenderer(), false);
        layered.setOperational(layered.getLouposcopeLayer(), false);
        layered.setOperational(layered.getBringAndGoLayer(), true);
    }
    
 // TODO: use cache on neighbour avatars
    protected List<IBoundedItem> getItemNeighbourAvatars(IBoundedItem item, boolean copyAbsolutePositionToPosition) {
        if (item.getObject() instanceof org.datagr4m.topology.Group<?>)
            return new ArrayList<IBoundedItem>();

        @SuppressWarnings("unchecked")
		V device = (V) item.getObject();
        List<IBoundedItem> neighbours = null;// deviceNeighbours.get(device);
        if (neighbours == null) {
            neighbours = new ArrayList<IBoundedItem>();
            Collection<V> devices = getDataModel().getTopology().getGlobalGraph().getNeighbors(device);
            for (V dd : devices) {
                IBoundedItem it = model.getItem(dd);
                if (it == null)
                    System.err.println("can't find:" + dd);
                else {
                    IBoundedItem avatar = ((DefaultBoundedItemIcon) it).clone();

                    if (copyAbsolutePositionToPosition)
                        avatar.changePosition(it.getAbsolutePosition());

                    neighbours.add(avatar);
                }
            }
            deviceNeighbours.put(device, neighbours);
        }
        return neighbours;
    }
    

    /** Create a FlowerModel, and performs the bring animation */
    @Override
    public void bring(IBoundedItem item) {
    	if(!isEnabled())
    		return;
    	
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
    	if(!isEnabled())
    		return;
    	
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
        ForceBringAndGoLayer renderer = new ForceBringAndGoLayer(display, flowerModel);
        renderer.setCurrentItem(item);
        renderer.setNeighbours(flowerModel.getNeighbours());
        renderer.getRendererSettings().getBoundsSettings().setBoundDisplayed(null, false);
        
        IGraphRendererSettings settings = renderer.getRendererSettings();
        //settings.getNodeSettings().setNodeBodyColor(flowerModel.getCenter(), Color.CYAN);
        
        
        ArrayListMultimap<String,IBoundedItem> occs = flowerModel.getNameToOccurenceMapping();

        // count number of dupplicates
        int n = 0;
        for(String name: occs.keySet()){
        	if(occs.get(name).size()>1){
        		n++;
        	}
        }
        ColorMapRainbow colormap = new ColorMapRainbow();
        int k = 0;
        for(String name: occs.keySet()){
        	List<IBoundedItem> items = occs.get(name);
        	if(items.size()>1){
                Color c = colormap.getColor(k, 0, n);
        		for(IBoundedItem occurence: items)
        			settings.getNodeSettings().setNodeBorderColor(occurence, c.brighter());
        		k++;
        	}
        }
        
        
        layered.setBringAndGoLayer(renderer);        
    }
    
    protected void setupOriginalPositions(List<IBoundedItem> neighbours) {
        originalPositionMap = new ItemPositionMap();
        originalPositionMap.copyAbsolutePosition(neighbours);
    }
    
    /* */
    
    protected IStaticFlowerBuilder<?,?> flowerBuilder;
    protected StaticFlowerModel<?> flowerModel;
    protected StaticFlowerLayout flowerLayout;
    
    protected ItemPositionMap originalPositionMap;

    protected AvatarManager avatarManager = new AvatarManager();

    protected Multimap<V, E> deviceNetworks;
    protected Map<V, List<IBoundedItem>> deviceNeighbours;
}
package org.datagr4m.drawing.navigation.plugin.bringandgo.force;

import java.util.List;

import org.datagr4m.drawing.animation.ForceLayoutAnimation;
import org.datagr4m.drawing.layout.ItemPositionMap;
import org.datagr4m.drawing.layout.PositionMapTransition;
import org.datagr4m.drawing.layout.hierarchical.flower.ForceFlowerLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.runner.stop.MeanMoveOrMaxStepCriteria;
import org.datagr4m.drawing.model.items.BoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.flower.ForceFlowerModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemLabelFinder;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.bringandgo.simple.SimpleBringAndGoPlugin;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.animation.IAnimation;
import org.datagr4m.viewer.animation.IAnimationMonitor;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.animation.ViewPointAnimation;
import org.datagr4m.viewer.animation.ViewScaleAnimation;
import org.datagr4m.viewer.mouse.ILocalizedMouse;
import org.jzy3d.maths.Coord2d;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

public class ForceBringAndGoPlugin<V,E> extends SimpleBringAndGoPlugin<V,E>{
    public static double MIN_RADIUS = 300;
    public static double MAX_RADIUS = 500;
    public static double MOVE_THRESHOLD = 10;
    public static int MAX_STEPS = 10000;
    
    public ForceBringAndGoPlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalNodeModel model) {
        super(controller, display, layered, animator, mouse, model);
    }

    @Override
    public void bring(IBoundedItem item) {
        setup(item);
        
        // center view to item
        IView view = display.getView();
        ViewPointAnimation va = new ViewPointAnimation(view, Pt.cloneAsCoord2d(view.getCenter()), item.getAbsolutePosition(), 300);

        // trigger force based bring
        final ForceLayoutAnimation fla = new ForceLayoutAnimation(layout);
        fla.getRunner().getConfiguration().getSequence().setFirstPhaseBreakCriteria(new MeanMoveOrMaxStepCriteria(MAX_STEPS, MOVE_THRESHOLD){
            @Override
            public void onBreak() {
                fla.interrupt();
            }
        });
        this.fla = fla;
        
        animator.push(va);
        animator.push(fla);
    }

    @Override
    public void go(IBoundedItem item, IAnimationMonitor monitor) {
        if(!fla.finished())
            fla.interrupt();
        // animate inverse transition
        
        ItemPositionMap currentPositionMap = new ItemPositionMap();
        currentPositionMap.copyAbsolutePosition(flower.getChildren());
        
        PositionMapTransition t = new PositionMapTransition();
        IAnimation anim = t.transition(currentPositionMap, originalPositionMap, DURATION);

        // switch to default context when animation finishes
        anim.addAnimationMonitor(monitor);

        Coord2d from = currentPositionMap.get(item);
        Coord2d to = originalPositionMap.get(item);

        // find target, and set it as mouseover for easy animation understanding
        //setState(item.getLabel(), ItemState.MOUSE_OVER);

        ViewPointAnimation va = new ViewPointAnimation(display.getView(), from, to, DURATION);
        ViewScaleAnimation vs = new ViewScaleAnimation(display.getView(), DURATION);
        animator.push(anim);
        animator.push(va);
        animator.push(vs);
    }
    
    /********************/
    
    @Override
	protected void setup(final IBoundedItem item) {
        IBoundedItem center = ((BoundedItemIcon) item).clone();
        center.changePosition(item.getAbsolutePosition());
        
        List<IBoundedItem> neighbours = getItemNeighbourAvatars(item, true);
        
        setupRendering(center, neighbours);
        
        // --------------
        // model
        flower = new ForceFlowerModel<E>(center, neighbours, MIN_RADIUS, MAX_RADIUS);
        buildEdges(flower, item, center);
        
        
        setupOriginalPositions(flower.getChildren());
        
        // --------------
        // layout
        layout = new ForceFlowerLayout(flower);
        
        // --------------
        // renderer
        ForceBringAndGoLayer layer = new ForceBringAndGoLayer(display, flower);
        layer.getRendererSettings().getBoundsSettings().setBoundDisplayed(null, false);
        layered.setBringAndGoLayer(layer);
    }
    
    /*** ONLY FOR THOSE USING FLOWER MODEL ***/
    
    protected void buildEdges(ForceFlowerModel<E> flower, IBoundedItem centerOriginal, IBoundedItem centerAvatar){
        ItemLabelFinder finder = new ItemLabelFinder();
        
        Graph<V,E> graph = getDataModel().getTopology().getGraph();
        
        for(E edge: graph.getEdges()){
            Pair<V> pair = graph.getEndpoints(edge);
            
            IBoundedItem leftO = model.getItem(pair.getFirst());
            IBoundedItem rightO = model.getItem(pair.getSecond());
            
            if(leftO!=centerOriginal && rightO!=centerOriginal)
                continue; // ignore edge where the center is not an end point
            
            if(leftO==null||rightO==null)
                throw new RuntimeException("left: " + leftO + " right: " + rightO);
            else{
                List<IBoundedItem> resLeft = finder.find(leftO.getLabel(), flower);
                List<IBoundedItem> resRight = finder.find(rightO.getLabel(), flower);
                
                if(resLeft.size()>0 && resRight.size()>0){
                    IBoundedItem left = resLeft.get(0);
                    IBoundedItem right = resRight.get(0);
                    
                    String edgeInfo = edge.toString();
                    String leftInfo = "xxx";
                    String rightInfo = "xxx";
                    
                    if(left==centerAvatar)
                        flower.setEdge(edge, edgeInfo, left, leftInfo, right, rightInfo);
                    else
                        flower.setEdge(edge, edgeInfo, right, rightInfo, left, leftInfo);
                }
                else
                    System.err.println("not found either " + leftO.getLabel() + " or " + rightO.getLabel());
            }
        }
    }
    
    /*******/
    
    protected ForceFlowerModel<E> flower;
    protected HierarchicalGraphLayout layout;
    protected ForceLayoutAnimation fla;
}

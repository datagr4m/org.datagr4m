package org.datagr4m.drawing.navigation.plugin.bringandgo.simple;

import java.util.List;

import org.datagr4m.drawing.layout.ItemPositionMap;
import org.datagr4m.drawing.layout.PositionMapTransition;
import org.datagr4m.drawing.layout.geometrical.CircleLayoutTable;
import org.datagr4m.drawing.layout.geometrical.RectangleLayoutTable;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.ItemLabelFinder;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.bringandgo.AbstractBringAndGoPlugin;
import org.datagr4m.drawing.navigation.plugin.bringandgo.IBringAndGoPlugin;
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

import com.google.common.collect.Multimap;

import edu.uci.ics.jung.graph.Graph;

public class SimpleBringAndGoPlugin<V,E> extends AbstractBringAndGoPlugin<V,E>  implements IBringAndGoPlugin {
    public SimpleBringAndGoPlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalNodeModel model){
        super(controller, display, layered, animator, mouse, model);
    }

    @Override
	protected Multimap<V, E> lookupNetworks(Graph<V, E> graph) {
		return null;
	}

    @Override
    public void bring(IBoundedItem item) {
        setup(item);
        
        // center view to item
        IView view = display.getView();
        ViewPointAnimation va = new ViewPointAnimation(view, Pt.cloneAsCoord2d(view.getCenter()), item.getAbsolutePosition(), 300);

        // Rectangle2D layoutBounds =
        // model.getRectangleBounds().cloneAsRectangle2D();
        // System.out.println(view.getLayoutScale(layoutBounds));

        // animate transition
        PositionMapTransition t = new PositionMapTransition();
        IAnimation anim = t.transition(originalPositionMap, circularPositionMap, DURATION / 2);
        animator.push(anim);
        animator.push(va);
    }

    @Override
    public void go(IBoundedItem item, IAnimationMonitor monitor) {
        // animate inverse transition
        PositionMapTransition t = new PositionMapTransition();
        IAnimation anim = t.transition(circularPositionMap, originalPositionMap, DURATION);

        // switch to default context when animation finishes
        anim.addAnimationMonitor(monitor);

        Coord2d from = circularPositionMap.get(item);
        Coord2d to = originalPositionMap.get(item);

        // find target, and set it as mouseover for easy animation understanding
        setState(item.getLabel(), ItemState.MOUSE_OVER);

        ViewPointAnimation va = new ViewPointAnimation(display.getView(), from, to, DURATION);
        ViewScaleAnimation vs = new ViewScaleAnimation(display.getView(), DURATION);
        animator.push(anim);
        animator.push(va);
        animator.push(vs);
    }
    
    protected void setState(String label, String state){
        ItemLabelFinder f = new ItemLabelFinder();
        f.setSearchString(label);
        f.visit(model);
        List<IBoundedItem> is = f.getResults();
        if (is.size() > 0)
            is.get(0).getState().set(state);
    }
        
    protected void setup(IBoundedItem item) {
        List<IBoundedItem> neighbours = getItemNeighbourAvatars(item, true);
        setupOriginalPositions(neighbours);
        setupRendering(item, neighbours);
        
        circularPositionMap = makeCircularLayout(item, neighbours);
    }

    @Override
	protected void setupOriginalPositions(List<IBoundedItem> neighbours) {
        originalPositionMap = new ItemPositionMap();
        originalPositionMap.copyAbsolutePosition(neighbours);
    }
     
    protected void bringTransition(IBoundedItem currentItem, List<IBoundedItem> neighbours) {
        final ItemPositionMap rect = makeRectangleLayout(currentItem, neighbours);
        final ItemPositionMap circ = makeCircularLayout(currentItem, neighbours);
        PositionMapTransition t = new PositionMapTransition();
        IAnimation anim = t.transition(rect, circ, 300);
        animator.push(anim);
    }

    protected ItemPositionMap makeRectangleLayout(IBoundedItem currentItem, List<IBoundedItem> neighbours) {
        Coord2d center = currentItem.getAbsolutePosition();
        RectangleLayoutTable layout = new RectangleLayoutTable(neighbours, center, 4000, 4000);
        layout.goAlgo();
        return layout.getPositions();
    }

    protected ItemPositionMap makeCircularLayout(IBoundedItem currentItem, List<IBoundedItem> neighbours) {
        Coord2d center = currentItem.getAbsolutePosition();
        CircleLayoutTable layout = new CircleLayoutTable(neighbours, center, false, 200);
        layout.goAlgo();
        return layout.getPositions();
    }
    
    protected ItemPositionMap originalPositionMap;
    protected ItemPositionMap circularPositionMap;
    
}

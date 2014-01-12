package org.datagr4m.drawing.viewer.mouse.edges;

import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.pair.IHierarchicalPairModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.context.ContextType;
import org.datagr4m.drawing.navigation.plugin.bringandgo.BringAndGoLayerHitProcessor;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.viewer.mouse.edges.slothit.ISlotHitPolicy;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.IRenderer;


public class MouseOverProcessor extends AbstractSlotHitHandler {
    protected List<IClickableItem> prevMouseRollovers;
    
    public boolean process(IRenderer renderer, Point2D mousePoint, ISlotHitPolicy slotHitPolicy, double manhattanDistance, INavigationController navigation) {
            boolean isBring = false;
            if (navigation != null && navigation.getContext() != null)
                isBring = navigation.getContext().is(ContextType.BRING);

            List<IClickableItem> mouseHits = null;
            if (isBring)
                mouseHits = renderer.hitOnly((int) mousePoint.getX(), (int) mousePoint.getY(), BringAndGoLayerHitProcessor.class);
            else
                mouseHits = renderer.hitOnly((int) mousePoint.getX(), (int) mousePoint.getY(), IHierarchicalRenderer.class);

            // reset mouse over
            if (prevMouseRollovers != null) {
                for (IClickableItem clickable : prevMouseRollovers) {
                    if (clickable instanceof IBoundedItem) {
                        ((IBoundedItem) clickable).setState(ItemState.NONE);
                    }
                }
            }

            boolean hasOneOrMoreOver = false;

            // set mouse over status
            if (mouseHits != null) {
                for (IClickableItem item : mouseHits) {
                    if (item instanceof IBoundedItem) {
                        setMouseOverStatus(mousePoint, slotHitPolicy, manhattanDistance, navigation, item);
                    }
                }
            }

            prevMouseRollovers = mouseHits;
            return true;
    }

    protected void setMouseOverStatus(Point2D mousePoint, ISlotHitPolicy slotHitPolicy, double manhattanDistance, INavigationController navigation, IClickableItem item) {
        boolean hasOneOrMoreOver;
        if (!(item instanceof IHierarchicalNodeModel) && !(item instanceof IEdge)) {
            IBoundedItem bi = ((IBoundedItem) item);
            bi.setState(ItemState.MOUSE_OVER);
            processItemRollOver(bi, mousePoint, slotHitPolicy, navigation, manhattanDistance);
            //System.out.println("over:" + item);
            hasOneOrMoreOver = true;
        }
        else if(item instanceof IHierarchicalPairModel){
            IBoundedItem bi = ((IBoundedItem) item);
            bi.setState(ItemState.MOUSE_OVER);
        }
        else if(item instanceof HierarchicalGraphModel){
            IBoundedItem bi = ((IBoundedItem) item);
            bi.setState(ItemState.MOUSE_OVER);
        }
    }
    
}

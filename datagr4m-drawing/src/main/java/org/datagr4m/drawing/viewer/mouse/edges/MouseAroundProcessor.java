package org.datagr4m.drawing.viewer.mouse.edges;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.viewer.mouse.edges.slothit.ISlotHitPolicy;
import org.jzy3d.maths.Coord2d;


public class MouseAroundProcessor extends AbstractSlotHitHandler{
    public List<IBoundedItem> hitSlotAround(List<IBoundedItem> items, Coord2d mouse, double itemAroundDistance, Point2D mousePoint, ISlotHitPolicy slotHitPolicy, double manhattanDistance, INavigationController navigation){
        List<IBoundedItem> around = new ArrayList<IBoundedItem>();
        
        //hitThoseBelowADistance(items, mouse, itemAroundDistance, mousePoint, slotHitPolicy, manhattanDistance, navigation, around);
        hitClosest(items, mouse, mousePoint, slotHitPolicy, manhattanDistance, navigation, around);
        return around;
    }

    protected void hitClosest(List<IBoundedItem> items, Coord2d mouse, Point2D mousePoint, ISlotHitPolicy slotHitPolicy, double manhattanDistance, INavigationController navigation, List<IBoundedItem> around) {
        IBoundedItem closest = null;
        double minDist = Double.MAX_VALUE;
        for(IBoundedItem item: items){
            if(!(item instanceof IHierarchicalModel)){
                double iSq = item.getAbsolutePosition().distanceSq(mouse);
                if(iSq<=minDist){
                    minDist = iSq;
                    closest = item;
                }
            }
        }
        if(closest!=null){
            around.add(closest);
            processItemRollOver(closest, mousePoint, slotHitPolicy, navigation, manhattanDistance);
        }
    }

    /** Pour tout item situ� � une distance inf�rieure au carr� de itemAroundDistance de la souris */
    protected void hitThoseBelowADistance(List<IBoundedItem> items, Coord2d mouse, double itemAroundDistance, Point2D mousePoint, ISlotHitPolicy slotHitPolicy, double manhattanDistance, INavigationController navigation, List<IBoundedItem> around) {
        double distSq = itemAroundDistance*itemAroundDistance;
        for(IBoundedItem item: items){
            double iSq = item.getAbsolutePosition().distanceSq(mouse);
            if(iSq<=distSq){
                around.add(item);
                processItemRollOver(item, mousePoint, slotHitPolicy, navigation, manhattanDistance);
            }
        }
    }
}

package org.datagr4m.trials.drawing.demo08.slots;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.slots.ISlotLayout;
import org.datagr4m.drawing.layout.slots.SlotLayout;
import org.datagr4m.drawing.model.links.DirectedLink;
import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleGroupModel;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.trials.drawing.demo07.pathfinder.AbstractDemo;

public class DemoSlots2 extends AbstractDemo{
    public static void main(String[] args) {
        PathObstacle o1 = new PathObstacle("obs1", 0500,0500,100,100);
        PathObstacle o2 = new PathObstacle("obs2", 0000,0000,100,100);
        PathObstacle o3 = new PathObstacle("obs3", 1000,0000,100,100);
        PathObstacle o4 = new PathObstacle("obs4", 1000,1000,100,100);
        PathObstacle o5 = new PathObstacle("obs5", 0000,1000,100,100);
        
        List<ILink<ISlotableItem>> links = new ArrayList<ILink<ISlotableItem>>();
        links.add(new DirectedLink(o1,o2));
        links.add(new DirectedLink(o1,o3));
        links.add(new DirectedLink(o1,o4));
        links.add(new DirectedLink(o1,o5));
        
        ISlotLayout si = new SlotLayout();
        ObstacleGroupModel g = (ObstacleGroupModel)si.initialize(links);
       
       
        show(g);
    }
}

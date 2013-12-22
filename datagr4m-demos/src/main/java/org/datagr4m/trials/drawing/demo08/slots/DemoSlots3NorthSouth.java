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

public class DemoSlots3NorthSouth extends AbstractDemo{
    public static void main(String[] args) {
        PathObstacle oLeft1 = new PathObstacle("obsLeft1", 0, 0, 100,100);
        PathObstacle oLeft2 = new PathObstacle("obsLeft2", 500, 0, 100,100);
        PathObstacle oLeft3 = new PathObstacle("obsLeft3", 1000, 0, 100,100);
        
        PathObstacle oRight1 = new PathObstacle("oRight1", 0,1000,100,100);
        PathObstacle oRight2 = new PathObstacle("oRight2", 250,1000,100,100);
        PathObstacle oRight3 = new PathObstacle("oRight3", 500,1000,100,100);
        PathObstacle oRight4 = new PathObstacle("oRight4", 750,1000,100,100);
        PathObstacle oRight5 = new PathObstacle("oRight5", 1000,1000,100,100);
        
        List<ILink<ISlotableItem>> links = new ArrayList<ILink<ISlotableItem>>();
        links.add(new DirectedLink(oLeft1,oRight1));
        links.add(new DirectedLink(oLeft1,oRight2));
        links.add(new DirectedLink(oLeft1,oRight3));
        links.add(new DirectedLink(oLeft1,oRight4));
        links.add(new DirectedLink(oLeft1,oRight5));
        
        links.add(new DirectedLink(oLeft2,oRight1));
        links.add(new DirectedLink(oLeft2,oRight2));
        links.add(new DirectedLink(oLeft2,oRight3));
        links.add(new DirectedLink(oLeft2,oRight4));
        links.add(new DirectedLink(oLeft2,oRight5));
        
        links.add(new DirectedLink(oLeft3,oRight1));
        links.add(new DirectedLink(oLeft3,oRight2));
        links.add(new DirectedLink(oLeft3,oRight3));
        links.add(new DirectedLink(oLeft3,oRight4));
        links.add(new DirectedLink(oLeft3,oRight5));

        // the same in the other direction
        links.add(new DirectedLink(oRight5,oLeft3));
        
        
        ISlotLayout si = new SlotLayout();
        ObstacleGroupModel g = (ObstacleGroupModel)si.initialize(links);
       
        show(g);
    }
        
}

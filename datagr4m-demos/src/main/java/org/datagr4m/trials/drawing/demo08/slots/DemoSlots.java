package org.datagr4m.trials.drawing.demo08.slots;

import org.datagr4m.drawing.layout.pathfinder.impl.PathFinder3;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleStructure;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.trials.drawing.demo07.pathfinder.AbstractDemo;

/**
 * This demo shows items having slots, i.e. incoming/outgoing edge symbols that are layed out
 * according to neighbourhood geometry.
 * 
 * The demos use an obstacle model initially introduced for rapid prototyping.
 * This model does not allow selecting nodes with mouse.
 */
public class DemoSlots extends AbstractDemo{
    public static void main(String[] args) {
        PathObstacle o1 = new PathObstacle("obs1", 500,500,100,100);
        o1.addSlots(3, 12, 4, 6);
        ObstacleStructure struct = new ObstacleStructure(new PathFinder3());
        struct.addObstacle(o1); // source
     
        show(struct);
    }
}

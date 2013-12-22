package org.datagr4m.trials.drawing.demo08.slots;

import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleGroupModel;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.drawing.renderer.pathfinder.view.ObstacleGroupRenderer;
import org.datagr4m.trials.drawing.demo07.pathfinder.AbstractDemo;

public class DemoSlotsUniquelyGlobal extends AbstractDemo{
    public static void main(String[] args) {
        PathObstacle o1 = new PathObstacle("obs1", 500,500,100,100);
        o1.addSlots(3, 12, 4, 6);
        ObstacleGroupModel struct = new ObstacleGroupModel();
        struct.addItem(o1); // source
     
        show(new ObstacleGroupRenderer(struct, true));
    }
}

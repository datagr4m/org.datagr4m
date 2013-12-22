package org.datagr4m.trials.drawing.demo08.slots;

import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleGroupModel;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.drawing.renderer.pathfinder.view.ObstacleGroupRenderer;
import org.datagr4m.trials.drawing.demo07.pathfinder.AbstractDemo;

public class DemoSlotsUnique extends AbstractDemo{
    public static void main(String[] args) {
        ObstacleGroupModel group = new ObstacleGroupModel();
        
        int n = 10;
        int s = 100;
        int o = 200;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                PathObstacle o1 = new PathObstacle(i+","+j, i*o, j*o, s, s);
                o1.addSlots(10,10,10,10);
                group.addItem(o1); // source
                
            }
        }
        
        
        show(new ObstacleGroupRenderer(group, true));
    }
}

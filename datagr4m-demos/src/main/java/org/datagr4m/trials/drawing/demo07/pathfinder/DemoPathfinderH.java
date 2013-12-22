package org.datagr4m.trials.drawing.demo07.pathfinder;

import org.datagr4m.drawing.layout.pathfinder.IPathFinder;
import org.datagr4m.drawing.layout.pathfinder.impl.PathFinder3;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleStructure;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.drawing.renderer.pathfinder.view.struct.StructRenderer;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;
import org.datagr4m.viewer.renderer.IRenderer;

public class DemoPathfinderH {
    public static void main(String[] args) {
        PathObstacle o1 = new PathObstacle("obs1", 0,0,100,100);
        PathObstacle o2 = new PathObstacle("obs2", 300,400,100,100);
        
        
        // horizontal obstacles
        PathObstacle o4 = new PathObstacle("obs4", 500,0,100,100);
        PathObstacle o5 = new PathObstacle("obs5", 1000,0,100,100);
        PathObstacle o6 = new PathObstacle("obs6", 750,0,100,100);
        
        IPathFinder finder = new PathFinder3();
        
        ObstacleStructure struct = new ObstacleStructure(finder);
        struct.addObstacle(o1); // source
        struct.addObstacle(o2); // source
        struct.addObstacle(o4);
        struct.addObstacle(o5);
        struct.addObstacle(o6);
        
        //horizontal edges
        struct.addLink(o1, o5); 
        struct.addLink(o5, o1);
        struct.addLink(o2, o1);
        struct.addLink(o6, o1);
        struct.addLink(o1, o6);
        
        show(struct);
    }
    
    public static void show(ObstacleStructure struct){
        final Display display = new Display(true);
        IRenderer renderer = new StructRenderer(struct);
        display.setView(new View(renderer, display));
        display.openFrame();
    }
}

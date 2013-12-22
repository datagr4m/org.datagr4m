package org.datagr4m.trials.drawing.demo07.pathfinder;

import org.datagr4m.drawing.layout.pathfinder.impl.PathFinder3;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleStructure;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.drawing.renderer.pathfinder.view.struct.StructRenderer;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;
import org.datagr4m.viewer.renderer.IRenderer;

public class DemoPathfinder2V {
    public static void main(String[] args) {
        PathObstacle o1 = new PathObstacle("obs1", 0,0,100,100);
        PathObstacle o2 = new PathObstacle("obs3", 0,1000,100,100);
        
        // vertical obstacles
        PathObstacle o3 = new PathObstacle("obs2", 0,500,100,100);
        PathObstacle o8 = new PathObstacle("obs8", 0,750,100,100);
        PathObstacle o9 = new PathObstacle("obs9", 20,820,100,100);
        
        
        ObstacleStructure struct = new ObstacleStructure(new PathFinder3());
        struct.addObstacle(o1); // source
        struct.addObstacle(o2); // dest
        struct.addObstacle(o3);
        struct.addObstacle(o8);
        struct.addObstacle(o9);
        
        // vertical edges
        struct.addLink(o1, o2);
        struct.addLink(o2, o1);
        //struct.addLink(o2, o1);
        

        show(struct);
    }
    
    public static void show(ObstacleStructure struct){
        final Display display = new Display(true);
        IRenderer renderer = new StructRenderer(struct);
        display.setView(new View(renderer, display));
        display.openFrame();
    }
}

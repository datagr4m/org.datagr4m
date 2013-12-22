package org.datagr4m.trials.drawing.demo07.pathfinder;

import org.datagr4m.drawing.layout.pathfinder.impl.PathFinder3;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleStructure;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.drawing.renderer.pathfinder.view.struct.StructRenderer;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;
import org.datagr4m.viewer.renderer.IRenderer;

/**
 * This demo shows a simple pathfinding able to avoid obstacle with orthogonal pathes.
 * 
 * Pathfinder demos use an obstacle model initially introduced for rapid prototyping.
 * This model does not allow selecting nodes with mouse.
 *  
 * Although not yet available as demo, pathfinder can be applied to hierarchical graphs, and be
 * scheduled to recompute pathes once a node moves.
 * 
 * 
 * Future works on Pathfinder:
 * <ul>
 * <li>Integrate to drawing api</li>
 * <li>Remove the ILink datamodel and simply rely on IEdge model</li>
 * <li>Let pathes along non obstructed ways stand next to each other, as they do when they avoid an obstacle</li>
 * <li>Provide a documentation for this algorithm</li>
 * </ul>
 */
public class DemoPathfinder {
    public static void main(String[] args) {
        PathObstacle o1 = new PathObstacle("obs1", 0,0,100,100);
        PathObstacle o2 = new PathObstacle("obs3", 0,1000,100,100);
        
        // vertical obstacles
        PathObstacle o3 = new PathObstacle("obs2", 0,500,100,100);
        PathObstacle o8 = new PathObstacle("obs8", 0,750,100,100);
        PathObstacle o9 = new PathObstacle("obs9", 20,820,100,100);
        
        // horizontal obstacles
        PathObstacle o4 = new PathObstacle("obs4", 500,0,100,100);
        PathObstacle o5 = new PathObstacle("obs5", 1000,0,100,100);
        PathObstacle o6 = new PathObstacle("obs6", 750,0,100,100);
        PathObstacle o7 = new PathObstacle("obs7", 820,20,100,100);
        
        ObstacleStructure struct = new ObstacleStructure(new PathFinder3());
        struct.addObstacle(o1); // source
        struct.addObstacle(o2); // dest
        struct.addObstacle(o3);
        struct.addObstacle(o4);
        struct.addObstacle(o5);
        struct.addObstacle(o6);
        struct.addObstacle(o7);
        struct.addObstacle(o8);
        struct.addObstacle(o9);
        
        // vertical edges
        struct.addLink(o1, o2);
        struct.addLink(o2, o1);
        
        //horizontal edges
        struct.addLink(o1, o5); 
        struct.addLink(o5, o1);
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

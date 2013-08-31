package org.datagr4m.drawing.model.pathfinder.obstacle;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.maths.geometry.RectangleUtils;


public class ObstacleUtils {
    public static List<IPathObstacle> merge(List<IPathObstacle> obstacles){
        List<IPathObstacle> copy = new ArrayList<IPathObstacle>(obstacles);
        
        for (int i = 0; i < copy.size(); i++) {
            IPathObstacle o1 = copy.get(i);
            for (int j = i+1; j < copy.size(); j++) { //triangle
                IPathObstacle o2 = copy.get(j);
                
                if(RectangleUtils.intersects(o1.getBypassedBounds(), o2.getBypassedBounds())){
                    copy.remove(o1);
                    copy.remove(o2);
                    IPathObstacle on = new PathObstacle(o1,o2);
                    System.out.println("merged " + o1.getName() + " and " + o2.getName() + " to " + on.getName());
                    copy.add(on);
                    i=-1;
                    j=-1;
                    break;
                }
            } 
        }
        return copy;
    }
    
    public static List<IPathObstacle> list(IPathObstacle o){
        List<IPathObstacle> obstacles = new ArrayList<IPathObstacle>(1);
        obstacles.add(o);
        return obstacles;
    }
}

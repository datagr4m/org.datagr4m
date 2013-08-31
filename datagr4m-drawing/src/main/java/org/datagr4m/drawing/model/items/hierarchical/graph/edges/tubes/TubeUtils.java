package org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.BoundsType;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.obstacle.PathObstacle;
import org.datagr4m.maths.geometry.RectangleUtils;
import org.jzy3d.maths.Coord2d;


public class TubeUtils {
	public static IPathObstacle buildObstacle(IBoundedItem item){
        return buildObstacle(item, BoundsType.RAW);
    }
	
    public static IPathObstacle buildObstacle(IBoundedItem item, BoundsType type){
        Coord2d p = item.getAbsolutePosition();
        float width = 0;
        float height = 0;
        
        if(type==BoundsType.RAW){
	        width = item.getRawRectangleBounds().width;
	        height = item.getRawRectangleBounds().height;
        }
        else if(type==BoundsType.EXTERNAL){
	        width = item.getRawExternalRectangleBounds().width;
	        height = item.getRawExternalRectangleBounds().height;
        }
        else if(type==BoundsType.CORRIDOR){
	        width = item.getRawCorridorRectangleBounds().width;
	        height = item.getRawCorridorRectangleBounds().height;
        }
        return new PathObstacle(item.getLabel(), RectangleUtils.build(p, width, height));
    }
    
    public static List<IPathObstacle> buildObstacles(List<IBoundedItem> items){
    	return buildObstacles(items, BoundsType.RAW);
    }
    
    public static List<IPathObstacle> buildObstacles(List<IBoundedItem> items, BoundsType type){
        List<IPathObstacle> obs = new ArrayList<IPathObstacle>();
        for(IBoundedItem i: items){
        	obs.add(buildObstacle(i, type));
        }
        return obs;
    }
}

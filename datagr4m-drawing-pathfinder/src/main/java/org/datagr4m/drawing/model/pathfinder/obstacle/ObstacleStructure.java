package org.datagr4m.drawing.model.pathfinder.obstacle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.pathfinder.IPathFinder;
import org.jzy3d.maths.Coord2d;


/** 
 * Store obstacles, and uses an IPathFinder to compute an actual path between two
 * obstacles.
 */
public class ObstacleStructure implements Serializable{
    private static final long serialVersionUID = -4507602862430576357L;

    public ObstacleStructure(IPathFinder finder){
        this.finder = finder;
    }
    
    public IPathFinder getFinder() {
        return finder;
    }

    public void setFinder(IPathFinder finder) {
        this.finder = finder;
    }



    public void addObstacle(IPathObstacle obstacle){
        obstacles.add(obstacle);
    }
    
    public void addLink(Pair<IPathObstacle, IPathObstacle> link){
        links.add(link);
    }
    
    public void addLink(IPathObstacle o1, IPathObstacle o2){
        addLink(new Pair<IPathObstacle, IPathObstacle>(o1, o2));
    }
    
    /******************/
    
    public List<IPathObstacle> getObstacles(){
        return obstacles;
    }
    
    public List<Pair<IPathObstacle, IPathObstacle>> getLinks(){
        return links;
    }
    
    /******************/

    public List<Coord2d> getPath(Pair<IPathObstacle, IPathObstacle> link){
        List<Coord2d> path = map.get(link);
        
        // compute path if not available
        if(path==null){
            try{
                path = finder.find(link.a, link.b, obstacles);
            }
            catch(Exception e){
                //e.printStackTrace();
                System.out.println(e);
                path = new ArrayList<Coord2d>();
                path.add(link.a.getSlotableCenter());
                path.add(link.b.getSlotableCenter());
            }
            map.put(link, path);
        }
        return path;
    }

    public void setPath(CommutativePair<IPathObstacle> link, List<Coord2d> path){
        map.put(link, path);
    }

    /******************/
    
    protected IPathFinder finder;
    
    protected List<IPathObstacle> obstacles = new ArrayList<IPathObstacle>();
    protected List<Pair<IPathObstacle, IPathObstacle>> links = new ArrayList<Pair<IPathObstacle, IPathObstacle>>();
    protected Map<Pair<IPathObstacle, IPathObstacle>, List<Coord2d>> map = new HashMap<Pair<IPathObstacle, IPathObstacle>,List<Coord2d>>();
}

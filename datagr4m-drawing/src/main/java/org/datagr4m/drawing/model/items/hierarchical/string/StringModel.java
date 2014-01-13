package org.datagr4m.drawing.model.items.hierarchical.string;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FARepulsion;
import org.datagr4m.drawing.layout.algorithms.forces.IForce;
import org.datagr4m.drawing.model.items.BoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.maths.geometry.Pt;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.algorithms.interpolation.IInterpolator;
import org.jzy3d.maths.algorithms.interpolation.algorithms.BernsteinInterpolator;


public class StringModel extends HierarchicalGraphModel{
    private static final long serialVersionUID = -281875289833790663L;
    //protected int stringId = 0;
    protected int magnetId = 0;

    public StringModel(){
        strings = new ArrayList<List<IBoundedItem>>();
        obstacles = new ArrayList<IBoundedItem>();
    }
    
    /******** ADD STRINGS TO THE MODEL ********/
    
    public void addStrings(List<List<Coord2d>> strings){
        for(List<Coord2d> string: strings)
            addString(string);
    }
    
    public void addString(List<? extends Coord2d> string){
        int k = 0;
        
        List<IBoundedItem> istring = new Vector<IBoundedItem>(string.size());
        for(Coord2d c: string){
            String label = ""+(k);
            IBoundedItem item = new BoundedItem(label, c.clone());
            k++;
            istring.add(item);
        }
        registerString(istring);
        strings.add(istring);
    }
    
    public void addPointString(List<Point2D> string){
        List<Coord2d> pts = new ArrayList<Coord2d>();
        for(Point2D c: string){
            pts.add(Pt.cloneAsCoord2d(c));
        }
        addString(pts);
    }
    
    protected void registerString(List<IBoundedItem> items){
        int k = 0;
        int n = items.size();
        
        IBoundedItem prev = null;

        for(IBoundedItem item: items){
            boolean isExtremity = (k==0 || k==n-1);
            registerChild(item, isExtremity);
            
            // inter magnet attraction
            if(prev!=null){
                addAttractionEdgeForce(prev, item);
            }
            prev = item;
            
            k++;
        }
    }
    
    protected void registerChild(IBoundedItem item, boolean isExtremity){
        int id = magnetId++;
        
        registerChild(id, item);
        
        if(!isExtremity){
            setNodeDegree(item, 2);
        }else{
            item.lock(); // locked!
            setNodeDegree(item, 1);
        }
    }
    
    /** Modify a string by adding a new magnet between the start and stop points. 
     * All forces in the model are updated afterward. */
    public synchronized void addLiveMagnet(List<IBoundedItem> string, Coord2d c){
        IBoundedItem obstacle = closestObstacleFrom(getObstacles(), c);

        Pair<IBoundedItem, List<IBoundedItem>> p = new Pair<IBoundedItem, List<IBoundedItem>>(obstacle, string);
        if(!list.contains(p)){
            // find the new magnet position
            double min = Double.MAX_VALUE;
            int mi = 0;
            for (int i = 0; i < string.size()-1; i++) {
                IBoundedItem i1 = string.get(i);
                IBoundedItem i2 = string.get(i+1);            
                double d1 = i1.getPosition().distance(c);
                double d2 = i2.getPosition().distance(c);            
                if(d1+d2<min){
                    min = d1+d2;
                    mi = i;
                }
            }
            if(mi==0)
                mi++;        
            if(mi==string.size()-1)
                mi-=2;        
            int prevI = mi;
            int nextI = prevI+1;
            
            if(string.size()==2){
                prevI = 0;
                nextI = 1;
            }
            
            IBoundedItem prev = string.get(prevI);
            IBoundedItem next = string.get(nextI);
            
            // remove existing attraction
            attractionEdges.remove(new Pair<IBoundedItem,IBoundedItem>(prev, next));
            
            // add item
            IBoundedItem item = new BoundedItem("livemagnet", c.clone());
            registerChild(item, false);
            string.add(nextI, item);
            
            // add attraction
            addAttractionEdgeForce(prev, item);
            addAttractionEdgeForce(item, next);
            
            // repulsion with all obstacles
            IBoundedItem leftI = string.get(0);
            IBoundedItem rightI = string.get(string.size()-1);
            
            //////////////////////////addForce(new PolarRepulsionForce(obstacle, item, leftI.getPosition(), rightI.getPosition(), true));
            
            
            
            list.add(p);
        }
        //for(IBoundedItem obstacle: getObstacles()){
            //addRepulsor(obstacle, item);
    }
    
    protected List<Pair<IBoundedItem,List<IBoundedItem>>> list = new ArrayList<Pair<IBoundedItem,List<IBoundedItem>>>();
    
    
    protected IBoundedItem closestObstacleFrom(List<IBoundedItem> obstacles, Coord2d c){
        IBoundedItem closest = null;
        double minD = Double.MAX_VALUE;
        for(IBoundedItem obstacle: obstacles){
            //addRepulsor(obstacle, item);
            double d = c.distance(obstacle.getPosition());
            if(d<minD){
                minD = d;
                closest = obstacle;
            }
        }
        return closest;
    }
    
    public synchronized void removeLiveMagnet(List<IBoundedItem> string, IBoundedItem magnet){
        int id = string.indexOf(magnet);
        
        //if(id>0)
        
        
        clearAttractingEdgeWith(magnet); // clean attraction
        clearRepulsorsWith(magnet);
        string.remove(magnet); // remove from string
        
        IBoundedItem i1 = string.get(id-1);
        IBoundedItem i2 = string.get(id);
        
        //update attraction
        addAttractionEdgeForce(i1, i2);
    }
    
    protected void clearAttractingEdgeWith(IBoundedItem magnet){
        synchronized(attractionEdges){
            List<Pair<IBoundedItem,IBoundedItem>> toBeRemoved = new ArrayList<Pair<IBoundedItem,IBoundedItem>>();
            for(Pair<IBoundedItem,IBoundedItem> i: attractionEdges){
                if(i.a == magnet || i.b == magnet)
                    toBeRemoved.add(i);
            }
            attractionEdges.removeAll(toBeRemoved);
        }
    }

    protected void clearRepulsorsWith(IBoundedItem magnet){
        synchronized(repulsors){
            // remove self force
            List<IBoundedItem> toBeRemoved = new ArrayList<IBoundedItem>();
            for(IBoundedItem i: repulsors.keySet()){
                if(i == magnet)
                    toBeRemoved.add(i);
            }
            repulsors.removeAll(toBeRemoved);
            
            // remove force to others
            for(IBoundedItem i: repulsors.keySet()){
                List<IForce> toBeDeleted = new ArrayList<IForce>();
                
                Collection<IForce> forces = repulsors.get(i);
                for(IForce force: forces){
                    if(force.getSource()==magnet)
                        toBeDeleted.add(force);
                }
                forces.removeAll(toBeDeleted);
            }
        }
    }

    
    /******** ADD OBSTACLES TO THE MODEL ********/

    public void addObstacle(String name, Coord2d position, float radius){
        addObstacle(name, position, radius, null);
    }
    
    public void addObstacle(String name, Coord2d position, float radius, Object o){
        IBoundedItem item = new BoundedItem(name, position, radius);
        item.setObject(o);
        registerChild(name, item);
        setNodeDegree(item, 0);
        item.lock();
        obstacles.add(item);
    }
    
    public void addModelChildrenAsObstacles(IHierarchicalNodeModel model){
        addModelChildrenAsObstacles(model, true);
    }
    
    public void addModelChildrenAsObstacles(IHierarchicalNodeModel model, boolean forbidDuplicates){
        for(IBoundedItem i: model.getChildren()){
            if(!forbidDuplicates)
                addObstacle(i.getLabel(), i.getAbsolutePosition(), i.getRadialBounds(), i);
            else{
                boolean exists = false;
                for(IBoundedItem o: obstacles){
                    if(o.getObject() == i){
                        exists = true;
                        break;
                    }
                }
                if(!exists){
                    //System.out.println("obstacle: " + i.getObject().toString());
                    addObstacle(i.getLabel(), i.getAbsolutePosition(), i.getRadialBounds(), i);
                }
            }
        }
    }

    /******** TOOLS TO SETUP MAGNETS *********/

    @Override
	public void addRepulsor(IBoundedItem target, IBoundedItem source){
        addRepulsor(target, new FARepulsion(target, source));
    }
    
    @Override
	public void createAllMutualRepulsors(){
        for(IBoundedItem obstacle: getObstacles())
            for(List<IBoundedItem> string: getStrings())
                for(IBoundedItem item: string)
                    addRepulsor(obstacle, item);
    }

    /********* BERNSTEIN INTERPOLATION ********/

    public static List<Point2D> processBernsteinInterpo(List<IBoundedItem> string, int steps){
        List<Coord3d> coords = new ArrayList<Coord3d>();
        for(IBoundedItem node: string){
            coords.add(new Coord3d(node.getPosition(), 0));
        }
        IInterpolator i = new BernsteinInterpolator();
        List<Coord3d> interpo = i.interpolate(coords, steps);
        
        List<Point2D> points = new ArrayList<Point2D>();
        for(Coord3d inter: interpo)
            points.add(new Point2D.Float(inter.x, inter.y));
        return points;
    }
    
    /*****************/
        
    public List<List<IBoundedItem>> getStrings() {
        return strings;
    }
    
    public List<Coord2d> getStringPath(List<IBoundedItem> string) {
        List<Coord2d> path = new ArrayList<Coord2d>();
        for(IBoundedItem item: string)
            path.add(item.getPosition());
        return path;
    }
    
    public List<IBoundedItem> getObstacles() {
        return obstacles;
    }

    protected List<List<IBoundedItem>> strings;
    protected List<IBoundedItem> obstacles;
}

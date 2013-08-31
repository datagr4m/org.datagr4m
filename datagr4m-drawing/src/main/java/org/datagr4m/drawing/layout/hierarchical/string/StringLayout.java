package org.datagr4m.drawing.layout.hierarchical.string;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.utils.EdgeHit;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.string.StringModel;
import org.jzy3d.maths.Coord2d;


/** The StringLayout is based on a force directed algorithm.
 * 
 * It modified a StringModel by adding and removing relevant magnetic anchors
 * along each string, to ensure the string does not pass through any obstacle.
 * 
 */
public class StringLayout extends HierarchicalGraphLayout{
    private static final long serialVersionUID = -4018523395852420171L;
    protected static int MIN_HITS_TO_ADD = 2000;
    protected static int MIN_HITS_TO_REMOVE = MIN_HITS_TO_ADD*50;
    
    public StringLayout(StringModel model) {
        super(model);
        smodel = model;
        hitMemory = new HitMemory<List<IBoundedItem>>();
        noHitMemory = new HitMemory<IBoundedItem>();
    }

    int k=0;
    @Override
	public boolean goAlgo(boolean handleChildren){
        
        List<IBoundedItem> obstacles = smodel.getObstacles();
        List<List<IBoundedItem>> strings = smodel.getStrings();
        
        // make standard gravity algo
        boolean s = super.goAlgo(handleChildren);
        
        //removeMagnets(strings, obstacles);
        addMagnets(strings, obstacles);
        return s;
    }
    
    /************ DYNAMIC MAGNET ADD/REMOVE ***************/
    
    protected void removeMagnets(List<List<IBoundedItem>> strings, List<IBoundedItem> obstacles){
        for(List<IBoundedItem> string: strings){
            //List<IBoundedItem> toBeRemoved = new ArrayList<IBoundedItem>();
            
            // count segments of this string that hit an obstacle
            overString:
            for (int i = 0; i < string.size()-2; i++) {
                Coord2d p1 = string.get(i).getPosition();
                Coord2d p2 = string.get(i+1).getPosition();
                Coord2d p3 = string.get(i+2).getPosition();
                
                overObstacles:
                for(IBoundedItem obstacle: obstacles){
                    
                    if(!EdgeHit.intersects(p1, p2, obstacle) && !EdgeHit.intersects(p2, p3, obstacle)){
                        if(!EdgeHit.intersects(p1, p3, obstacle)){
                            IBoundedItem item = string.get(i+1);
                            noHitMemory.increment(item, null);
                            break overString;
                        }
                    }
                }
            }
            
            // really decide which magnet can be removed in this string
            List<IBoundedItem> toBeRemoved = new ArrayList<IBoundedItem>();
        
            for(IBoundedItem i: string){
                if(noHitMemory.getHits(i, null)>MIN_HITS_TO_REMOVE){
                    toBeRemoved.add(i);
                }
            }

            if(toBeRemoved.size()>0)
                Logger.getLogger(StringLayout.class).info("before cleaning: " + string.size());

            for(IBoundedItem i: toBeRemoved){
                smodel.removeLiveMagnet(string, i);
                noHitMemory.clear(i, null);
                
                //Logger.getLogger(StringLayout.class).info("removed " + i);
                
                //Logger.getLogger(StringLayout.class).info("clearing");
            }
            if(toBeRemoved.size()>0)
                Logger.getLogger(StringLayout.class).info("after cleaning: " + string.size());
        }
    }
    
    protected void addMagnets(List<List<IBoundedItem>> strings, List<IBoundedItem> obstacles){
        for(List<IBoundedItem> string: strings){
            // recompute intersection
            List<Coord2d> path = smodel.getStringPath(string);
            List<IBoundedItem> hits = EdgeHit.intersectionsCoords(path, obstacles);
            
            // update age
            for(IBoundedItem hit: hits)
                hitMemory.increment(string, hit);
            
            // create a new magnet where required
            boolean changed = false;
            for(IBoundedItem hit: hits){
                int ni = 0+hitMemory.getHits(string, hit);
                if(ni>MIN_HITS_TO_ADD){
                    changed = true;
                    smodel.addLiveMagnet(string, hit.getPosition());
                    //Logger.getLogger(StringLayout.class).info("added " + hit.getPosition());
                }
            }
            if(changed)
                hitMemory.clear(string);
        }
    }

    protected HitMemory<IBoundedItem> noHitMemory;
    protected HitMemory<List<IBoundedItem>> hitMemory;
    protected StringModel smodel;
}

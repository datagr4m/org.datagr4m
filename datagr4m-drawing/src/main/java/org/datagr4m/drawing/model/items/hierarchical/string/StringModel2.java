package org.datagr4m.drawing.model.items.hierarchical.string;

import java.util.List;

import org.apache.log4j.Logger;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public class StringModel2 extends StringModel{
    
    public StringModel2(){
        super();
    }
    
    /** Modify a string by adding a new magnet between the start and stop points. 
     * All forces in the model are updated afterward. */
    @Override
	public synchronized void addLiveMagnet(List<IBoundedItem> string, Coord2d c){
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
        IBoundedItem item = new DefaultBoundedItem("livemagnet", c.clone());
        registerChild(item, false);
        string.add(nextI, item);
        
        // add attraction
        addAttractionEdgeForce(prev, item);
        addAttractionEdgeForce(item, next);
        
        // repulsion with all obstacles
        Coord2d c1 = string.get(0).getAbsolutePosition();
        Coord2d c2 = string.get(string.size()-1).getAbsolutePosition();
        Coord2d c3 = c2.sub(c1).fullPolar();
        final double angle = c3.x + Math.PI/2;
        
        Logger.getLogger(StringModel2.class).info("angle:"+angle);
        
        // TODO: ATTENTION ON PEUT SE CONCENTRER QUE SUR UN SOUS ENSEMBLE!!
        
        
        //addRepulsor(closest, new AlignementForce(closest, item, angle));
        //addRepulsor(closest, new PolarForce(closest, item, c1, c2));

    }
    
}

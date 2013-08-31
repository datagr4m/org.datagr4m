package org.datagr4m.drawing.layout.hierarchical.string;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;


/** Helper to guess how long time an object hitted something.*/
public class HitMemory<T> {
    public HitMemory(){
        hitAge = new HashMap<Pair<T,IBoundedItem>, Integer>();
    }
    
    public int getHits(T object, IBoundedItem obstacle){
        synchronized(hitAge){
            if(object==null)
                return 0;
            Pair<T,IBoundedItem> p = key(object, obstacle);
            if(p==null || hitAge==null)
                return 0;
            Integer i = hitAge.get(p);
            if(i==null)
                return 0;
            return i;
        }
    }
    
    public void increment(T object, IBoundedItem obstacle){
        synchronized(hitAge){
            Integer i = hitAge.get(key(object, obstacle));
            if(i==null)
                hitAge.put(key(object, obstacle), 1);
            else{
                hitAge.put(key(object, obstacle), i+1);
            }
        }
    }
    
    public void clear(T object, IBoundedItem obstacle){
        synchronized(hitAge){
            hitAge.put(key(object, obstacle), 0);
        }
    }
    
    public void clear(T object){
        synchronized(hitAge){
            Set<Pair<T,IBoundedItem>> pairs = hitAge.keySet();
            for(Pair<T,IBoundedItem> pair: pairs){
                if(pair.a == object){
                    Integer i = hitAge.get(pair);
                    i = 0;
                    //clear(object, pair.b);
                }
            }
        }
    }
    
    public Pair<T,IBoundedItem> key(T object, IBoundedItem obstacle){
        return new Pair<T,IBoundedItem>(object, obstacle);
    }
    
    protected Map<Pair<T,IBoundedItem>, Integer> hitAge;
}

package org.datagr4m.drawing.layout.algorithms.forceAtlas.monitoring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.ForceAtlasLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public class MeanMoveAnalysis {
    public void makeAnalysis(ForceAtlasLayout layout, Collection<IBoundedItem> items){
        if(layout.getCounter() % positionLogFrq==0){
            if(positionLog.size()>0)
                computeMeanPositionChange(items);
            logPosition(items);
        }
    }
    
    public void logPosition(Collection<IBoundedItem> items){
        for(IBoundedItem item: items){
            positionLog.put(item, item.getPosition().clone());
        }
    }
    
    public void computeMeanPositionChange(Collection<IBoundedItem> items){
        double totald = 0;
        int nok = 0;
        for(IBoundedItem item: items){
            Coord2d p = positionLog.get(item);
            if(p!=null){
                double d = item.getPosition().distance(p);
                totald+=d;
                nok++;
            }
            else
                throw new RuntimeException("missing log position");
        }
        lastMeanPositionChange = totald/nok;
    }
    
    public double getLastMeanPositionChange() {
        return lastMeanPositionChange;
    }
    
    public int getPositionLogFrq() {
        return positionLogFrq;
    }

    protected double lastMeanPositionChange = -1;
    protected int positionLogFrq = 100;
    protected Map<IBoundedItem,Coord2d> positionLog = new HashMap<IBoundedItem,Coord2d>();
}

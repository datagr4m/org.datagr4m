package org.datagr4m.monitors;

import java.util.Date;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;

public interface ITimeMonitor {
    public boolean isEnabled();
    public void enable(boolean status);
    
    public void startMonitor();
    public void stopMonitor();
    public List<Pair<Date,Double>> getMeasurements();
    public Double getMean();
    public int getSize();
    
    public Unit getUnit();
    public String getName();
    public Object getMonitored();
    
    public enum Unit{
        SECONDS
    }
}

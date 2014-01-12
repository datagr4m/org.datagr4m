package org.datagr4m.monitors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;

public class TimeMonitor implements ITimeMonitor{
    protected long start;
    protected List<Pair<Date,Double>> measurements = new ArrayList<Pair<Date,Double>>();
    protected static double TEN_POWER_9 = 1000000000.0; 
    protected String name;
    protected Object monitored;
    protected boolean enabled;

    public TimeMonitor(Object o) {
        this(o, o.getClass().getSimpleName(), false);
    }

    /*public TimeMonitor(String name) {
        this(name, false);
    }
    
    public TimeMonitor(String name, boolean enabled) {
        this(null, name, enabled);
    }*/

    public TimeMonitor(Object monitored, String name, boolean enabled) {
        this.monitored = monitored;
        this.name = name;
        this.enabled = enabled;
    }

    @Override
    public void startMonitor() {
        if(isEnabled())
            start = System.nanoTime();
    }

    @Override
    public void stopMonitor() {
        if(isEnabled()){
            long elapsedNano = System.nanoTime() - start;
            double elapsedSecons = elapsedNano / TEN_POWER_9; 
            measurements.add(new Pair<Date,Double>(new Date(), elapsedSecons));            
        }
    }

    @Override
    public List<Pair<Date,Double>> getMeasurements() {
        return measurements;
    }

    @Override
    public Unit getUnit() {
        return Unit.SECONDS;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void enable(boolean status) {
        this.enabled = status;
    }

    @Override
    public Object getMonitored() {
        return monitored;
    }

    @Override
    public Double getMean() {
        if(measurements.size()==0)
            return Double.NaN;
        else{
            double mean = 0;
            int k = 0;
            for(Pair<Date,Double> m: measurements){
                mean += m.b;
                k++;
            }
            return mean/k;
        }
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return 0;
    }
}

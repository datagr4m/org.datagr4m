package org.datagr4m.drawing.monitors;

import org.datagr4m.monitors.ITimeMonitorable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class TimeMonitorCollection {
    protected String name;
    protected Multimap<String, ITimeMonitorable> monitorables = ArrayListMultimap.create();

    public Multimap<String, ITimeMonitorable> getMonitorables() {
        return monitorables;
    }

    public void setMonitorables(Multimap<String, ITimeMonitorable> monitorables) {
        this.monitorables = monitorables;
    }

    /**
     * enable a monitor and attach it to a key
     */
    public void addMonitorable(String name, ITimeMonitorable monitorable) {
        addMonitorable(name, monitorable, true);
    }

    public void addMonitorable(String name, ITimeMonitorable monitorable, boolean enableMonitor) {
        monitorable.getTimeMonitor().enable(enableMonitor);
        this.monitorables.put(name, monitorable);
    }
}

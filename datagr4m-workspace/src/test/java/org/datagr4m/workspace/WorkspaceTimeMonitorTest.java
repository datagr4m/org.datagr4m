package org.datagr4m.workspace;

import org.datagr4m.monitors.ITimeMonitorable;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.generator.TopologyGenerator;
import org.junit.Assert;
import org.junit.Test;

public class WorkspaceTimeMonitorTest {
    @Test
    public void allMonitorsAreEnabled() {
        Topology<String, String> topology = TopologyGenerator.buildGraphNested(1, 1, 1);

        Workspace w = new Workspace(topology);
        
        WorkspaceTimeMonitor monitor = new WorkspaceTimeMonitor(w);
        
        int nExpectedMonitors = 3;
        Assert.assertTrue("Has at least N monitorables", monitor.getMonitorables().values().size() >= nExpectedMonitors);
        
        for (ITimeMonitorable monitorable : monitor.getMonitorables().values()) {
            Assert.assertTrue("Each monitor is enabled", monitorable.getTimeMonitor().isEnabled());
        }
    }
}

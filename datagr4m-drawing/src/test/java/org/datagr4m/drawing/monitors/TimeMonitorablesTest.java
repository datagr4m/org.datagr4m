package org.datagr4m.drawing.monitors;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.geometrical.category.CategoryLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.HierarchicalGraphLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.HierarchicalEdgeLayout;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.SlotedTubeLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalColumnLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalMatrixLayout;
import org.datagr4m.drawing.layout.hierarchical.matrix.HierarchicalRowLayout;
import org.datagr4m.drawing.layout.hierarchical.pair.HierarchicalPairLayout;
import org.datagr4m.drawing.layout.hierarchical.tree.TreeFlowerLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunner;
import org.datagr4m.monitors.ITimeMonitorable;
import org.junit.Assert;
import org.junit.Test;

public class TimeMonitorablesTest {
    public List<ITimeMonitorable> getMonitorables() {
        List<ITimeMonitorable> monitorables = new ArrayList<ITimeMonitorable>();
        
        // Node layouts
        monitorables.add(new HierarchicalColumnLayout());
        monitorables.add(new HierarchicalRowLayout());
        monitorables.add(new HierarchicalMatrixLayout());
        monitorables.add(new HierarchicalGraphLayout());
        monitorables.add(new HierarchicalPairLayout());
        monitorables.add(new CategoryLayout());
        monitorables.add(new TreeFlowerLayout());
        
        // Edge layouts
        monitorables.add(new HierarchicalEdgeLayout());
        monitorables.add(new SlotedTubeLayout()); // available by extension of hierarchicaledgelayout
        
        // Runners
        monitorables.add(new LayoutRunner());
        
        return monitorables;
    }
    
    @Test
    public void testAllLayoutMonitors(){
        for(ITimeMonitorable monitorable: getMonitorables())
            testMonitorAvailableAndDisabled(monitorable);

    }
    
    public void testMonitorAvailableAndDisabled(ITimeMonitorable layout){
        Assert.assertNotNull(layout.getTimeMonitor());
        Assert.assertFalse(layout.getTimeMonitor().isEnabled());
    }
}

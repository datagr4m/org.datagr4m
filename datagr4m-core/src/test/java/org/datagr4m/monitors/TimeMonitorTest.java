package org.datagr4m.monitors;

import org.junit.Assert;
import org.junit.Test;

public class TimeMonitorTest {
    @Test
    public void testTimeMonitor() throws InterruptedException {
        TimeMonitor m = new TimeMonitor("test-monitor");
        assertInitializedDisabledWithName(m);

        m.enable(true);

        // ensure did measure properly
        double allowedDifferenceBetweenPauseAndMeasure = 0.01; // s

        assertMonitorMeasureActualPause(m, 0, 1000, allowedDifferenceBetweenPauseAndMeasure);
        assertMonitorMeasureActualPause(m, 1, 500, allowedDifferenceBetweenPauseAndMeasure);
        assertMonitorMeasureActualPause(m, 2, 250, allowedDifferenceBetweenPauseAndMeasure);

        assertDontAddMeasureWhenDisabled(m);
        assertAddMeasureWhenEnabled(m);
    }

    public void assertInitializedDisabledWithName(TimeMonitor m) {
        Assert.assertFalse("time monitor is disabled by default", m.isEnabled());
        Assert.assertEquals(0, m.getMeasurements().size());
        Assert.assertEquals("test-monitor", m.getName());
    }

    public void assertAddMeasureWhenEnabled(TimeMonitor m) {
        int before = m.getMeasurements().size();

        m.enable(true);
        m.startMonitor();
        m.stopMonitor();
        Assert.assertEquals(before + 1, m.getMeasurements().size());
    }

    public void assertDontAddMeasureWhenDisabled(TimeMonitor m) {
        // disable & ensure stop measuring
        int before = m.getMeasurements().size();
        m.enable(false);
        m.startMonitor();
        m.stopMonitor();
        Assert.assertEquals(before, m.getMeasurements().size());
    }

    public void assertMonitorMeasureActualPause(TimeMonitor m, int id, int miliSecondBreak, double tolerance) throws InterruptedException {
        m.startMonitor();
        Thread.sleep(miliSecondBreak);
        m.stopMonitor();
        int nExpectedMeasures = id+1;
        Assert.assertEquals("There are " + nExpectedMeasures + " measures", nExpectedMeasures, m.getMeasurements().size());
        
        double measure = m.getMeasurements().get(id).b;
        double expected = miliSecondBreak / 1000.0;

        Assert.assertTrue("Measure is >= " + expected+" s", measure >= expected);
    
        double diff = Math.abs(measure-expected);

        Assert.assertTrue("Measure is in range [-" + tolerance+":"+tolerance+"] s of actual pause time. Diff=" + diff + " s", diff <= tolerance);
    }
}

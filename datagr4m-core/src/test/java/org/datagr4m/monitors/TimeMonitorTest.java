package org.datagr4m.monitors;

import org.junit.Assert;
import org.junit.Test;

public class TimeMonitorTest {
    @Test
    public void testTimeMonitor() throws InterruptedException {
        TimeMonitor m = new TimeMonitor("test-monitor");
        Assert.assertFalse("time monitor is disabled by default", m.isEnabled());
        Assert.assertEquals(0, m.getMeasurements().size());
        Assert.assertEquals("test-monitor", m.getName());

        m.enable(true);

        // ensure did measure properly
        int FIRST_BREAK = 1000;
        m.startMonitor();
        Thread.sleep(FIRST_BREAK);
        m.stopMonitor();
        Assert.assertEquals(1, m.getMeasurements().size());
        Assert.assertTrue(m.getMeasurements().get(0).b >= FIRST_BREAK / 1000);

        int SECOND_BREAK = 500;
        m.startMonitor();
        Thread.sleep(SECOND_BREAK);
        m.stopMonitor();
        Assert.assertEquals(2, m.getMeasurements().size());
        Assert.assertTrue(m.getMeasurements().get(1).b >= SECOND_BREAK / 1000);
        Assert.assertTrue(m.getMeasurements().get(1).b <= FIRST_BREAK / 1000);

        int THIRD_BREAK = 250;
        m.startMonitor();
        Thread.sleep(THIRD_BREAK);
        m.stopMonitor();
        Assert.assertEquals(3, m.getMeasurements().size());
        Assert.assertTrue(m.getMeasurements().get(2).b >= THIRD_BREAK / 1000);
        Assert.assertTrue(m.getMeasurements().get(2).b <= SECOND_BREAK / 1000);

        // disable & ensure stop measuring
        int before = m.getMeasurements().size();
        m.enable(false);
        m.startMonitor();
        m.stopMonitor();
        Assert.assertEquals(before, m.getMeasurements().size());

        // enable again
        m.enable(true);
        m.startMonitor();
        m.stopMonitor();
        Assert.assertEquals(before + 1, m.getMeasurements().size());
    }
}

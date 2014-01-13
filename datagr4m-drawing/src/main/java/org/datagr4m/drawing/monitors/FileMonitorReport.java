package org.datagr4m.drawing.monitors;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.datagr4m.monitors.ITimeMonitor;
import org.datagr4m.monitors.ITimeMonitorable;
import org.jzy3d.io.SimpleFile;
import org.jzy3d.maths.Utils;

public class FileMonitorReport implements IMonitorReport {
    protected String file;
    
    public FileMonitorReport(String file){
        this.file = file;
    }
    
    @Override
    public void report(TimeMonitorCollection monitors) {
        StringBuilder sb = new StringBuilder();
        for (String name : monitors.getMonitorables().keySet()) {
            append(sb, name);
            append(sb, " : ");
            Collection<ITimeMonitorable> monitorables = monitors.getMonitorables().get(name);
            for (ITimeMonitorable monitorable : monitorables) {
                ITimeMonitor timeMonitor = monitorable.getTimeMonitor();
                append(sb, Utils.num2str(timeMonitor.getMean()) + "");
                if(ITimeMonitor.Unit.SECONDS.equals(timeMonitor.getUnit())){
                    append(sb, " s");
                }
            }
            appendln(sb);
        }
        
        try {
            SimpleFile.write(sb.toString(), file);
        } catch (Exception e) {
            Logger.getLogger(FileMonitorReport.class).error(e);
        }
    }


    protected void appendln(StringBuilder sb) {
        sb.append("\n");
    }

    protected void appendln(StringBuilder sb, String value) {
        sb.append(value + "\n");
    }

    protected void append(StringBuilder sb, String value) {
        sb.append(value);
    }

}

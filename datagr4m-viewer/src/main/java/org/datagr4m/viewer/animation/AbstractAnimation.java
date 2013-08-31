package org.datagr4m.viewer.animation;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAnimation implements IAnimation{
    @Override
    public void addAnimationMonitor(IAnimationMonitor listener) {
        if(monitors == null)
            monitors = new ArrayList<IAnimationMonitor>(1);
        monitors.add(listener);
    }

    @Override
    public void removeAnimationMonitor(IAnimationMonitor listener) {
        if(monitors != null)
            monitors.remove(listener);
    }
    
    @Override
	public void notifyAnimationFinished(){
        if(monitors != null)
            for(IAnimationMonitor monitor: monitors)
                monitor.finished(this);
    }
    
    protected void clearMonitors(){
        if(monitors!=null)
            monitors.clear();
    }

    protected List<IAnimationMonitor> monitors;
}

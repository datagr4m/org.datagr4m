package org.datagr4m.viewer.animation;

public interface IAnimation {
    /** Returns true if the animation is terminated. */
    public boolean next();
    
    public void addAnimationMonitor(IAnimationMonitor listener);
    public void removeAnimationMonitor(IAnimationMonitor listener);
    public void notifyAnimationFinished();
}

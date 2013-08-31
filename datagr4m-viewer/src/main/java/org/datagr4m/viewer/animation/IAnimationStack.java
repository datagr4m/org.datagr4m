package org.datagr4m.viewer.animation;


public interface IAnimationStack {
    public boolean isRunning();
    public void push(IAnimation animation);
    
    public static long ANIMATION_FRAME_RATE = 30;//40>ms=25 img/s
}

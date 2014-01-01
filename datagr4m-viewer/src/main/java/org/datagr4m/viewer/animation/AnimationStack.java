package org.datagr4m.viewer.animation;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.viewer.IDisplay;


public class AnimationStack implements IAnimationStack{
    
    public AnimationStack(IDisplay display){
        this.animations = new ArrayList<IAnimation>();
        this.display = display; 
    }
    
    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void push(IAnimation animation) {
        animations.add(animation);
        //System.out.println("is running: " + isRunning);
        if(!isRunning)
            start();
    }
    
    @SuppressWarnings("deprecation")
	public void start(){
        isRunning = true;
        if(mainThread!=null){
            mainThread.stop();
            mainThread = null;
        }
        mainThread = getRunner();
        mainThread.start();
    }
    @SuppressWarnings("deprecation")
    public void stop(){
        isRunning = false;
        if(mainThread!=null){
            mainThread.stop();
            mainThread = null;
        }
    }
    
    protected Thread getRunner(){
        return new Thread(new Runnable(){
            @Override
			public void run(){
                isRunning = true;
                while(animations.size()>0){
                    next();
                    try {
                        Thread.sleep(ANIMATION_FRAME_RATE);
                    } catch (InterruptedException e) {
                        isRunning = false;
                        e.printStackTrace();
                    }
                }
                isRunning = false;
                //System.err.println("AnimationStack done");
            }
        });
    }
    
    protected void next(){
        List<IAnimation> done = new ArrayList<IAnimation>();
        for(IAnimation animation: animations){
            if(animation.next())
                done.add(animation);
        }
        
        display.refresh();
        
        for(IAnimation anim: done){
            anim.notifyAnimationFinished();
            animations.remove(anim);
        }
    }

    protected Thread mainThread;

    
    protected List<IAnimation> animations;
    protected boolean isRunning = false;
    protected IDisplay display;
}

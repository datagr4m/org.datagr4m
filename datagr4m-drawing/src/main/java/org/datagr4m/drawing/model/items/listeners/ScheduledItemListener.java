package org.datagr4m.drawing.model.items.listeners;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;



public class ScheduledItemListener implements IItemListener {
    public static int INTERVAL = 1000;
    
    public void listen(IHierarchicalModel model){
        model.addItemListener(this);
        thread = getThread();
        thread.start();
    }
    
    @Override
    public void itemBoundsChanged(IBoundedItem item) {
        lastBoundChanged = item;
    }
    
    @Override
    public void itemPositionChanged(IBoundedItem item) {
    }

    
    protected void fireItemBoundsChanged(IBoundedItem item){
        for(IItemListener listener: listeners)
            listener.itemBoundsChanged(item);
    }
        
    protected Thread getThread(){
        return new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    if(lastBoundChanged!=null)
                        fireItemBoundsChanged(lastBoundChanged);
                    lastBoundChanged = null;
                    
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    public void addItemListener(IItemListener listener){
        listeners.add(listener);
    }
    
    public void removeItemListener(IItemListener listener){
        listeners.remove(listener);
    }
    protected IBoundedItem lastBoundChanged;
    protected Thread thread;
    protected List<IItemListener> listeners = new ArrayList<IItemListener>(0);
}

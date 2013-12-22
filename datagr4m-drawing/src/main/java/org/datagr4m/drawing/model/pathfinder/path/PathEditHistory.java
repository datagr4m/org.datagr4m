package org.datagr4m.drawing.model.pathfinder.path;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PathEditHistory implements Serializable{
    private static final long serialVersionUID = 2037881975788334838L;
    
    public PathEditHistory(){
        this(DEFAULT_MAX_SIZE);
    }
    
    public PathEditHistory(int maxsize){
        this.maxsize = maxsize;
    }
    
    public void add(PathEditLog log){
        if(logs.size()>=maxsize){
            logs.remove(0);
        }
        logs.add(log);
    }
    
    public void clear(){
        logs.clear();
    }
    
    public boolean exists(PathEditLog log){
        return logs.contains(log);
    }
    
    public int getCurrentSize(){
        return logs.size();
    }
    
    public int getMaximumSize(){
        return maxsize;
    }
    
    @Override
	public String toString(){
        String out = "(PathEditHistory) \n";
        for(PathEditLog log: logs){
            out += log.toString() + "\n";
        }
        return out;
    }
    
    protected int maxsize;
    protected List<PathEditLog> logs = new ArrayList<PathEditLog>();
    public static int DEFAULT_MAX_SIZE = 5;
}

package org.datagr4m.drawing.model.pathfinder.path;

import java.io.Serializable;

public class PathEditLog implements Serializable{
    private static final long serialVersionUID = -7241385265669097574L;
    
    public PathEditLog(int from, int to, IPath path) {
        this.from = from;
        this.to = to;
        this.path = path;
    }
    
    public int getFrom() {
        return from;
    }
    public int getTo() {
        return to;
    }
    public IPath getPath() {
        return path;
    }
    
    @Override
	public String toString(){
        return "(PathEditLog) from " + from + " to " + to + ", path: " + path;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + from;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + to;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PathEditLog other = (PathEditLog) obj;
        if (from != other.from)
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (to != other.to)
            return false;
        return true;
    }
    
    protected int from;
    protected int to;
    protected IPath path;
}

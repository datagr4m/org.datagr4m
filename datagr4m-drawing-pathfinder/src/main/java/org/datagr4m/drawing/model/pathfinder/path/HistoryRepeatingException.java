package org.datagr4m.drawing.model.pathfinder.path;

import org.datagr4m.drawing.layout.pathfinder.PathFinderException;

public class HistoryRepeatingException extends PathFinderException{
    public HistoryRepeatingException(PathEditLog lastLog, PathEditHistory history) {
        super();
        this.lastLog = lastLog;
        this.history = history;
    }
    
    public PathEditLog getLastLog() {
        return lastLog;
    }
    public PathEditHistory getHistory() {
        return history;
    }
    
    @Override
	public String toString(){
        return "HistoryRepeatingException for " + lastLog + " on " + history;
    }
    
    protected PathEditLog lastLog;
    protected PathEditHistory history;
    private static final long serialVersionUID = 5121144815456004694L;
}

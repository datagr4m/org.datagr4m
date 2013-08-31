package org.datagr4m.drawing.viewer.mouse.edges;

import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.viewer.mouse.IClickableItem;


public class ClickedPath implements IClickableItem{
    private static final long serialVersionUID = 77013289085541373L;

    public ClickedPath(IPath path) {
        this.path = path;
    }

    public IPath getPath() {
        return path;
    }
    
    @Override
	public String toString(){
        return path.toString();
    }

    protected IPath path;
}

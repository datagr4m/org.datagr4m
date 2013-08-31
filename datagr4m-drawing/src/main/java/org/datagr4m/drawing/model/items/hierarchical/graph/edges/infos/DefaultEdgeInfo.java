package org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos;

import java.util.List;

public class DefaultEdgeInfo implements IEdgeInfo{
    private static final long serialVersionUID = -8328160105121828273L;
    protected String string;
    
    public DefaultEdgeInfo() {
        this.string = null;
    }
    
    public DefaultEdgeInfo(String string) {
        this.string = string;
    }
    public String getString() {
        return string;
    }
    public void setString(String string) {
        this.string = string;
    }
    
    @Override
    public String toString(){
        if(string!=null)
            return string;
        else
            return "";
    }

	@Override
	public List<String> flattenInfoAsString() {
		return null;
	}

	@Override
	public String flattenInfo() {
		return string;
	}
}

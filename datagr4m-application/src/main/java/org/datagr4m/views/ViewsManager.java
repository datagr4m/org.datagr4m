package org.datagr4m.views;

import java.util.LinkedHashMap;
import java.util.Map;

import org.datagr4m.application.neo4j.workspace.Neo4jWorkspaceSettings;

public class ViewsManager {
    LinkedHashMap<String,WorkspaceView> views = new LinkedHashMap<String,WorkspaceView>();
    
    public Map<String, WorkspaceView> getViews() {
        return views;
    }
    
    public WorkspaceView getFirst(){
        return views.values().iterator().next();
    }

    public void load(Neo4jWorkspaceSettings s) throws Exception{
        for(String v: s.getViews()){
            views.put(v, new WorkspaceView(s,v));
        }
    }
}

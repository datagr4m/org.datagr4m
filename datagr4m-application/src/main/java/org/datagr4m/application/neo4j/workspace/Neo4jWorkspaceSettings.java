package org.datagr4m.application.neo4j.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.workspace.WorkspaceSettings;
import org.jzy3d.io.SimpleDir;


public class Neo4jWorkspaceSettings extends WorkspaceSettings {
    public Neo4jWorkspaceSettings(WorkspaceSettings s) {
        this();
        setName(s.getName());
    }

    public Neo4jWorkspaceSettings() {
        super();
    }

    public Neo4jWorkspaceSettings(File data, String name) {
        super(data, name);
    }

    public Neo4jWorkspaceSettings(File data) {
        super(data);
    }

    public Neo4jWorkspaceSettings(String name) {
        super(name);
    }

    @Override
	public void setName(String name) {
        super.setName(name);
        localDatabasePath = getWorkspaceFolder() + "/db.neo4j";
        modelPath = getWorkspaceFolder() + "/model.ns";
        viewsPath = getWorkspaceFolder() + "/views";
    }

    public String getLocalDatabasePath() {
        return localDatabasePath;
    }
    
    public String getLocalDataPath() {
        return getWorkspaceFolder() + "/data.ns";
    }


    public String getViewsPath() {
        return viewsPath;
    }

    public String getViewPath(String view) {
        return viewsPath + "/" + view;
    }
    
    public String getViewDataPath(String view) {
        return viewsPath + "/" + view + "/data.ns";
    }

    public String getModelPath() {
        return modelPath;
    }

    public List<String> getViews() {
        File[] views = SimpleDir.listDir(new File(getViewsPath()));
        List<String> out = new ArrayList<String>();
        for (File f : views)
            out.add(f.getName());
        return out;

    }

    protected String localDatabasePath;
    protected String viewsPath;
    protected String modelPath;

}

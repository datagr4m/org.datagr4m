package org.datagr4m.workspace;

import java.awt.Component;
import java.io.IOException;
import java.util.List;


public interface IWorkspaceController {

    public IWorkspace getCurrentWorkspace();
    public void setCurrentWorkspace(IWorkspace workspace);
    public WorkspaceSettings getSettings();
    public void setSettings(WorkspaceSettings settings);
    public List<String> getWorkspaceNames() throws IOException;


    /************ IO *************/

    public void loadWorkpaceFiles();
    public void loadWorkpaceFiles(Component parent);

    public boolean createWorkspace(String workspace);
    public void createWorkspace(String name, IWorkspace workspace) throws IOException;

    /**** CACHE ****/

    public void saveWorkspaceToCache();

    public void reloadWorkspaceFromCache();

    /************ LISTENERS *************/

    public void addListener(IControllerListener listener);

    public void removeListener(IControllerListener listener);

}
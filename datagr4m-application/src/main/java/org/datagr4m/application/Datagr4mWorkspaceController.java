package org.datagr4m.application;

import java.awt.Component;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.datagr4m.application.designer.AbstractDatagr4mViewer;
import org.datagr4m.application.neo4j.workspace.Neo4jWorkspace;
import org.datagr4m.application.neo4j.workspace.Neo4jWorkspaceSettings;
import org.datagr4m.gui.editors.toolbars.ModalListSelector;
import org.datagr4m.workspace.IWorkspace;
import org.datagr4m.workspace.WorkspaceController;


public class Datagr4mWorkspaceController extends WorkspaceController {
	AbstractDatagr4mViewer app;

    public Datagr4mWorkspaceController(AbstractDatagr4mViewer app, Neo4jWorkspaceSettings settings) {
        super(settings);
        this.app = app;
    }
    
    @Override
    public void loadWorkpaceFiles() {
        loadWorkpaceFiles(null);
    }

    @Override
    public void loadWorkpaceFiles(Component parent) {
        try {
        	if(isNeo4jWorkspace())
        		workspace = newNeo4jWorkspace();
        	
        	if(workspace!=null)
        		fireWorkspaceLoaded(workspace);
        	else
                fireWorkspaceException(workspace, new Exception("could not initialize a workspace"));        		
            return;
        } catch (Exception e) {
            fireWorkspaceException(workspace, e);
        }
    }

	public boolean isNeo4jWorkspace() {
		return true;
	}

	public Neo4jWorkspace newNeo4jWorkspace() throws Exception {
		return new Neo4jWorkspace(newNeo4jWorkspaceSettings());
	}
    
    
    public Neo4jWorkspaceSettings newNeo4jWorkspaceSettings() {
        return new Neo4jWorkspaceSettings(getSettings());
    }

    

    /**** CACHE ****/

    @Override
    public void saveWorkspaceToCache() {
        throw new RuntimeException("not implemented");
        /*
         * if (workspace != null) { try {
         * BinaryFiles.save(settings.getWorkspaceCache(), workspace);
         * fireWorkspaceSaved(workspace); } catch (IOException e) {
         * fireWorkspaceException(workspace, e); } }
         */
    }

    @Override
    public void reloadWorkspaceFromCache() {
        throw new RuntimeException("not implemented");
        /*
         * try { workspace = (Workspace)
         * BinaryFiles.load(settings.getWorkspaceCache());
         * fireWorkspaceLoaded(workspace); } catch (IOException e) {
         * fireWorkspaceException(workspace, e); } catch (ClassNotFoundException
         * e) { fireWorkspaceException(workspace, e); }
         */
    }

    @Override
    public boolean createWorkspace(String workspace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createWorkspace(String name, IWorkspace workspace) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    /* */
    
    public void showWorkspaceSelectGUI() {
        // ArrayList<String>.(type[]) collection.toArray(new
        // type[collection.size()])
        List<String> workspaces;
        try {
            workspaces = getWorkspaceNames();
            Collections.sort(workspaces);
            Object[] choice = workspaces.toArray(new Object[workspaces.size()]);// {"default","conf.and.db"};
            String answer = ModalListSelector.ask("Open Workspace", "Please select a workspace", choice, "");

            // if did not cancel
            if (answer != null) {
                getSettings().setName(answer);
                loadWorkpaceFiles(app);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

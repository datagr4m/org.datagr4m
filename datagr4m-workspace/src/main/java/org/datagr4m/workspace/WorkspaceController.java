package org.datagr4m.workspace;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.utils.BinaryFiles;
import org.jzy3d.io.SimpleDir;



/**
 * <ul>
 * <li>hold a current workspace instance
 * <li>load workspace
 * <li>notify listeners that workspace has been loaded or failed to load
 * </ul>
 * @author Martin Pernollet
 *
 */
public class WorkspaceController implements IWorkspaceController {
    // TODO: remove me
    public WorkspaceController(){
        this(null);
    }
    
    public WorkspaceController(WorkspaceSettings settings){
        listeners = new ArrayList<IControllerListener>();
        if(settings!=null)
        	this.settings = settings;
        else
        	this.settings = new WorkspaceSettings(); // don't care "default" really exist
    }
    
    @Override
    public IWorkspace getCurrentWorkspace(){
        return workspace;
    }
    
    @Override
    public void setCurrentWorkspace(IWorkspace workspace){
        this.workspace = workspace;
        fireWorkspaceLoaded(workspace);
    }
    
    @Override
    public WorkspaceSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(WorkspaceSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public List<String> getWorkspaceNames() throws IOException{
        final File dir = new File(settings.getWorkspacesPath());
        final List<File> files = SimpleDir.getAllFolders(dir);
        
        List<String> output = new ArrayList<String>();
        for(File f: files){
            output.add(f.getName());
        }
        return output;
    }
    
    @Override
    public boolean createWorkspace(String workspace) {
        final File dir = new File(settings.getWorkspacesPath() + "/" + workspace);
        return dir.mkdirs();
    }
    
    @Override
    public void createWorkspace(String name, IWorkspace workspace) throws IOException {
    	final File dir = new File(settings.getWorkspacesPath() + "/" + workspace);
        dir.mkdirs();
        BinaryFiles.save(new File(dir.getPath() + "/workspace.bin").getPath(), workspace);
    }

    /************ IO *************/
    
    @Override
    public void loadWorkpaceFiles(){
        loadWorkpaceFiles(null);
    }
    
    @Override
    public void loadWorkpaceFiles(Component parent){
        try {
        	/*// try loading a binary workspace
            BinaryWorkspaceIO wbin = new BinaryWorkspaceIO();
            if(wbin.loadWorkspace(settings, parent)){
                workspace = wbin.getBuiltWorkspace();
                fireWorkspaceLoaded(workspace);
                return;
            }
        	
            // try loading an XML workspace
            XmlWorkspaceIO wxml = new XmlWorkspaceIO();
            if(wxml.loadWorkspace(settings, parent)){
                workspace = wxml.getBuiltWorkspace();
                fireWorkspaceLoaded(workspace);
                return;
            }*/
        	
            
            fireWorkspaceException(null, new Exception("Workspace '" + settings.getName() + "' could not be loaded properly, either as XML or binary file"));                
        } 
        catch (Exception e) {
            fireWorkspaceException(workspace, e);
        } 
    }
    
    /**** CACHE ****/
    
    @Override
    public void saveWorkspaceToCache(){
        if(workspace!=null){
            try {
                BinaryFiles.save(settings.getWorkspaceCache(), workspace);
                fireWorkspaceSaved(workspace);
            } catch (IOException e) {
                fireWorkspaceException(workspace, e);
            }
        }
    }
    
    @Override
    public void reloadWorkspaceFromCache(){
        try {
            workspace = (Workspace)BinaryFiles.load(settings.getWorkspaceCache());
            fireWorkspaceLoaded(workspace);
        } catch (IOException e) {
            fireWorkspaceException(workspace, e);
        } catch (ClassNotFoundException e) {
            fireWorkspaceException(workspace, e);
        }
    }
    
    
    /************ LISTENERS *************/
    
    @Override
    public void addListener(IControllerListener listener){
        listeners.add(listener);
    }

    @Override
    public void removeListener(IControllerListener listener){
        listeners.remove(listener);
    }

    protected void fireWorkspaceCreated(IWorkspace workspace){
        for(IControllerListener listener: listeners)
            listener.workspaceCreated(workspace);
    }

    protected void fireWorkspaceLoaded(IWorkspace workspace){
        for(IControllerListener listener: listeners)
            listener.workspaceLoaded(workspace);
    }

    protected void fireWorkspaceSaved(IWorkspace workspace){
        for(IControllerListener listener: listeners)
            listener.workspaceSaved(workspace);
    }

    protected void fireWorkspaceException(IWorkspace workspace, Exception e){
        for(IControllerListener listener: listeners)
            listener.workspaceException(workspace, e);
    }
    
    /*******************/
    
    protected IWorkspace workspace;
    protected WorkspaceSettings settings;
    protected List<IControllerListener> listeners;
}

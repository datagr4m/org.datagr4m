package org.datagr4m.application;

import java.awt.Dimension;

import org.datagr4m.application.designer.IDesktopDesigner;
import org.datagr4m.application.gui.Datagr4mViewer;
import org.datagr4m.viewer.Display;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.WorkspaceSettings;

public class Datagr4mLauncher {
	public static void main(String[] args) throws Exception{
        new Datagr4mLauncher().start();  
    }
    
    protected IDesktopDesigner startDesigner(String root, String workspace){
        IDesktopDesigner designer;
        
        if(workspace!=null){
            final WorkspaceSettings settings = new WorkspaceSettings(workspace);
            designer = new Datagr4mViewer(root, settings);
        }
        else
            designer = new Datagr4mViewer(root);
        return designer;
    }
    
    public final IDesktopDesigner start(){
        return start("", (Workspace)null);
    }    

    /** Starts designer with a predefined workspace.*/
    public final IDesktopDesigner start(Workspace workspace){
    	return start(null, workspace);
    }

    /** Starts designer with a file workspace.*/
    public final IDesktopDesigner start(String workspace){
    	return start(null, workspace);
    }

    /** Starts designer with a predefined workspace.*/
    public final IDesktopDesigner start(String root, Workspace workspace){
        IDesktopDesigner designer = startDesigner(root, (String)null);
        if(designer==null)
            return null;
        Display.show(designer.toJFrame(), new Dimension(1300, 700));
        
        if(workspace!=null){
            designer.getDataController().setCurrentWorkspace(workspace);
            designer.workspaceLoaded(workspace);
        }
        return designer;
    }
    
    /** Starts designer with a file workspace.*/
    public final IDesktopDesigner start(String root, String workspace){
        IDesktopDesigner designer = startDesigner(root, workspace);
        if(designer==null)
            return null;
        Display.show(designer.toJFrame(), new Dimension(1300, 700));
        
        if(workspace!=null){
            designer.getDataController().loadWorkpaceFiles();
        }
        return designer;
    }
}

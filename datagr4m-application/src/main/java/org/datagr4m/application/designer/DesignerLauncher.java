package org.datagr4m.application.designer;

import java.awt.Dimension;
import java.io.File;

import org.datagr4m.viewer.Display;
import org.datagr4m.workspace.Workspace;
import org.datagr4m.workspace.WorkspaceSettings;


//
// --attract : attraction force (default: Layer3(hierarchical)=10, Layer 2(flat)=4"
// --repulse : repulsion force (default: Layer3(hierarchical)=30, Layer 2(flat)=35000"
// --attract 10 --repulse 12500
// --attract 4 --repulse 100000

/**
 * Start a designer application according to command line arguments.
 * 
 * Performs, if flags set to true:
 * <ul>
 * <li>licence checking 
 * <li>token server communication
 * </ul>
 * 
 * @author Martin
 */
public class DesignerLauncher {
	public static void main(String[] args) throws Exception{
		IDesigner nd = start(new File(".").getAbsolutePath(), (String)null);  
    }
    
    public static IDesigner start(){
        return start("", (Workspace)null);
    }    
    
    public final static IDesigner start(Workspace workspace){
    	return start(null, workspace);
    }

    public final static IDesigner start(String workspace){
    	return start(null, workspace);
    }
    
    /** Starts designer with a hand built workspace.*/
    public final static IDesigner start(String root, Workspace workspace){
        Designer designer = startDesigner(root, (String)null);
        if(designer==null)
            return null;
        Display.show(designer, new Dimension(1300, 700));
        
        if(workspace!=null){
            designer.getDataController().setCurrentWorkspace(workspace);
            designer.workspaceLoaded(workspace);
        }
        return designer;
    }
    
    public final static IDesigner start(String root, String workspace){
        Designer designer = startDesigner(root, workspace);
        if(designer==null)
            return null;
        Display.show(designer, new Dimension(1300, 700));
        

        if(workspace!=null){
            designer.getDataController().loadWorkpaceFiles();
        }
        return designer;
    }

    /**
     * verify license if set to do so
     */
    private final static Designer startDesigner(String root, String workspace){
        Designer designer;
        
        if(workspace!=null){
            final WorkspaceSettings settings = new WorkspaceSettings(workspace);
            designer = new Designer(root, settings);
        }
        else
            designer = new Designer(root);
        return designer;
    }
}

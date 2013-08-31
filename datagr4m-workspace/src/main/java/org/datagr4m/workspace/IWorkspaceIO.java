package org.datagr4m.workspace;

import java.awt.Component;


/**
 * Load the content of a workspace, which may be a set of files, or a single file,
 * based on various formats (XML, binary, etc).
 * 
 * 
 * @author Martin Pernollet
 */
public interface IWorkspaceIO {

	public abstract boolean exists(WorkspaceSettings settings);


	public boolean loadWorkspace(WorkspaceSettings settings) throws Exception;
	/** 
     * Try loading required (file) to build a workspace.
     * Return true if at least repository and topology XML files can be found in this repository.
     * Once loaded, the repository should be retrieved by calling getBuiltWorkspace()
     */
    public boolean loadWorkspace(WorkspaceSettings settings, Component parent) throws Exception;

	/** Return a built workspace*/
	public IWorkspace getBuiltWorkspace();
    
    /**
     * Will save a workspace, maybe by splitting it into several files.
     */
    public void saveWorkspace(String file, IWorkspace workspace) throws Exception;
    
}
package org.datagr4m.workspace;

/**
 * Load the content of a workspace, which may be a set of files, or a single file,
 * based on various formats (XML, binary, etc).
 * 
 * 
 * @author Martin Pernollet
 */
public interface IWorkspaceLoader {

	public abstract boolean exists(WorkspaceSettings settings);

	/** 
	 * Try loading required (file) to build a workspace.
	 * Return true if at least repository and topology XML files can be found in this repository.
	 */
	public abstract boolean loadWorkspace(WorkspaceSettings settings) throws Exception;

	/** Return a built workspace*/
	public IWorkspace getBuiltWorkspace();
}
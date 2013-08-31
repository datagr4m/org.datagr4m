package org.datagr4m.workspace;

public interface IControllerListener {
    public void workspaceLoaded(IWorkspace w);
    public void workspaceCreated(IWorkspace w);
    public void workspaceSaved(IWorkspace w);
    public void workspaceException(IWorkspace w, Exception e);
}

package org.datagr4m.application.gui.trees.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


public class TreeMouseListener extends MouseAdapter {
    ITreeNodeSelectedListener listener;

    public TreeMouseListener(ITreeNodeSelectedListener listener) {
        this.listener = listener;
    }

    @Override
	public void mousePressed(MouseEvent e) {
        DefaultMutableTreeNode node = getSelectedItem(e);
        if (node == null)
            return;
        listener.nodeSelected(node);
    }

    protected DefaultMutableTreeNode getSelectedItem(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        JTree tree = (JTree) e.getSource();
        TreePath path = tree.getPathForLocation(x, y);
        if (path == null)
            return null;
        tree.setSelectionPath(path);
        return (DefaultMutableTreeNode) path.getLastPathComponent();
    }
}

package org.datagr4m.application.gui.trees.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;


public class TreeKeyListener implements KeyListener{
    ITreeNodeSelectedListener listener;
    public TreeKeyListener(ITreeNodeSelectedListener listener){
        this.listener = listener;
    }
    
    @Override
	public void keyPressed(KeyEvent e) {}
    @Override
	public void keyReleased(KeyEvent e) {
        listener.nodeSelected( (DefaultMutableTreeNode) ((JTree)e.getSource()).getSelectionPath().getLastPathComponent()  );
        if (e.getKeyCode() == KeyEvent.VK_A) {
            System.out.println("You pressed it!");
        }
    }
    @Override
	public void keyTyped(KeyEvent e) {}
}

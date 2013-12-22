package org.datagr4m.application.gui.trees.node;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.datagr4m.topology.graph.IPropertyNode;


public class NodeCellRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 7193877813518850922L;
    //protected DeviceCellMouseAdapter contextClick;

    public NodeCellRenderer() {
        //setTextNonSelectionColor(Color.cyan);
    }

    @Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
        if (isNode(value)) {
            setNodeStyle(value);
        } 

//        if( contextClick == null ){
//            contextClick = new DeviceCellMouseAdapter();
//            System.out.println("adding mouse listener");
//            this.addMouseListener( contextClick );
//        }
        return this;
    }
    
    public boolean isNode(Object value) {
        if(value==null)
            return false;
        else if(((DefaultMutableTreeNode)value).getUserObject() instanceof IPropertyNode)
            return true;
        else
            return false;
    }

    public IPropertyNode extractNode(Object value) {
        return (IPropertyNode)((DefaultMutableTreeNode)value).getUserObject();
    }
    
    public void setNodeStyle(Object value) {
    	IPropertyNode d = extractNode(value);
    	setIcon(d.getType().getImageSmall());
	}	
}

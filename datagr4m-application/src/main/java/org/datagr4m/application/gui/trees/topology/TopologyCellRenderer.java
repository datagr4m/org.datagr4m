package org.datagr4m.application.gui.trees.topology;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.datagr4m.application.gui.trees.node.NodeCellRenderer;
import org.datagr4m.application.neo4j.renderers.Neo4jRelationshipRendererSettings;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.NodeType;
import org.datagr4m.view2d.icons.IconLibrary;
import org.neo4j.graphdb.RelationshipType;

public class TopologyCellRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 7193877813518850922L;
    protected NodeCellRenderer r = new NodeCellRenderer();
    //protected DeviceCellMouseAdapter contextClick;
    
    protected Neo4jRelationshipRendererSettings edgeRendererSettings;

    public TopologyCellRenderer() {
        //setTextNonSelectionColor(Color.cyan);
    }
    
    public TopologyCellRenderer(Neo4jRelationshipRendererSettings edgeRendererSettings) {
        super();
        this.edgeRendererSettings = edgeRendererSettings;
    }

    /**actual object is defined in */
    @Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
        if (isNode(value)) {
            setNodeStyle(value);
        } 
        else if (isNodeType(value)) {
            setNodeTypeStyle(value);
        } 
        else if (isRelationshipType(value)) {
            setRelationshipTypeStyle(value);
        } 
        else if (isGroup(value)) {
            setGroupStyle(value);
        } 
        else
            ;//System.out.println(value);

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
    
    public boolean isNodeType(Object value) {
        if(value==null)
            return false;
        else if(((DefaultMutableTreeNode)value).getUserObject() instanceof NodeType)
            return true;
        else
            return false;
    }
    
    public boolean isRelationshipType(Object value) {
        if(value==null)
            return false;
        else if(((DefaultMutableTreeNode)value).getUserObject() instanceof RelationshipType)
            return true;
        else
            return false;
    }

    public IPropertyNode extractNode(Object value) {
        return (IPropertyNode)((DefaultMutableTreeNode)value).getUserObject();
    }

    public NodeType extractNodeType(Object value) {
        return (NodeType)((DefaultMutableTreeNode)value).getUserObject();
    }

    public String extractRelationshipType(Object value) {
        return (String)((DefaultMutableTreeNode)value).getUserObject();
    }
    
    public void setNodeStyle(Object value) {
        IPropertyNode d = extractNode(value);
        setIcon(d.getType().getImageSmall());
    } 

    public void setNodeTypeStyle(Object value) {
        NodeType d = extractNodeType(value);
        setIcon(d.getImageSmall());
        
        //Font f = getFont();
        //f.
        //setFont(f);
    } 

    public void setRelationshipTypeStyle(Object value) {
        if(edgeRendererSettings!=null){
            String d = extractRelationshipType(value);
            Color fg = edgeRendererSettings.getRelationshipTypeColor(d);
            if(fg==null)
                fg = Color.GRAY;
            setForeground(fg);
            //setBackground(fg);
            //setIcon(d.getType().getImageSmall());
        }
    }  
    
    public boolean isGroup(Object value) {
        if(value==null)
            return false;
        else if(((DefaultMutableTreeNode)value).getUserObject() instanceof Group<?>)
            return true;
        else
            return false;
    }

    public Group<?> extractGroup(Object value) {
        return (Group<?>)((DefaultMutableTreeNode)value).getUserObject();
    }
    
    public void setGroupStyle(Object value) {
    	Group<?> d = extractGroup(value);
    	//System.out.println(d.getType());
    	ImageIcon i = IconLibrary.createImageIconAsFile("data/images/icons/symbolic/n3.png");
    	if(i==null)
    	    throw new RuntimeException("missing icon");
    	setIcon(i);
    	setText(d.getName());
	}


    public Neo4jRelationshipRendererSettings getEdgeRendererSettings() {
        return edgeRendererSettings;
    }


    public void setEdgeRendererSettings(Neo4jRelationshipRendererSettings edgeRendererSettings) {
        this.edgeRendererSettings = edgeRendererSettings;
    }
}

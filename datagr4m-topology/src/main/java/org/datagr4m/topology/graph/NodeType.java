package org.datagr4m.topology.graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

public class NodeType {
    protected String name;
    protected String inheritance;
    protected String label;
    protected String icon;
    protected ImageIcon image;
    protected ImageIcon imageSmall;
    protected boolean propertyNode;
    
    protected Set<String> properties;
    protected Set<String> relations;
    protected NodeType superType;
    
    protected static int undefId;
    
    public static NodeType undefined(){
        return new NodeType("?type" + (undefId++), new HashSet<String>());
    }
    
    public NodeType(String name, Set<String> properties) {
        this.name = name;
        this.properties = properties;
        this.relations = new HashSet<String>();
    }
    public NodeType(Set<String> properties) {
        this("?type" + (undefId++), properties);
    }
    public NodeType(String name) {
        this(name, new HashSet<String>());
    }
    /*public NodeType(Node node) {
        this(Neo4jNode.properties(node));
    }*/
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<String> getProperties() {
        return properties;
    }
    
    public Set<String> getRelations() {
        return relations;
    }

    public void setRelations(Set<String> relations) {
        this.relations = relations;
    }

    public String getInheritance() {
        return inheritance;
    }

    public void setInheritance(String inheritance) {
        this.inheritance = inheritance;
    }
    
    public NodeType getSuperType() {
        return superType;
    }

    public void setSuperType(NodeType superType) {
        this.superType = superType;
    }
    
    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }
    
    public ImageIcon getImage() {
        return image;
    }
    public void setImage(ImageIcon image) {
        this.image = image;
    }
    public ImageIcon getImageSmall() {
        return imageSmall;
    }
    public void setImageSmall(ImageIcon image) {
        this.imageSmall = image;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setIcon(String icon) {
        this.icon = "data/images/" + icon;
        
        
        try{
            Logger.getLogger(NodeType.class).info("loading " + this.icon);
            setImage(new ImageIcon(this.icon));
        }
        catch(Exception e){
            Logger.getLogger(NodeType.class).error(e + " while loading " + this.icon);
        }
        
        try {
            List<String> str = otherVersion(this.icon);
            if(str.size()>0){
                String smallVersion = str.get(0);
                Logger.getLogger(NodeType.class).info("found small version " + smallVersion);
                setImageSmall(new ImageIcon(smallVersion));
            }
            else
                setImageSmall(getImage());
        } catch (IOException e) {
            Logger.getLogger(NodeType.class).error(e + " while loading smaller size icons for " + this.icon);
            e.printStackTrace();
        }
    }
    
    public List<String> otherVersion(String iconFilename) throws IOException{
        File iconFile = new File(iconFilename);
        File parentFolder = iconFile.getParentFile();
        //List<File> children = SimpleDir.getAllFiles(parentFolder);
        String name = iconFile.getName();
        
        int[] size = {16,32};
        
        String parentPath = parentFolder.getAbsolutePath();
        List<String> str = new ArrayList<String>();
        for (int i = 0; i < size.length; i++) {
            String candidate = name.replace(".png", "-" + size[i] + ".png");
            String candidatePath = parentPath + "/" + candidate;
            File candidateFile = new File(candidatePath);
            if(candidateFile.exists())
                str.add(candidatePath);
        }
        return str;
    }
    
    
    
    public void setProperties(Set<String> properties) {
        this.properties = properties;
    }
    
    public boolean isPropertyNode() {
        return propertyNode;
    }
    public void setPropertyNode(boolean propertyNode) {
        this.propertyNode = propertyNode;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeType other = (NodeType) obj;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!properties.equals(other.properties))
            return false;
        return true;
    }
    
    @Override
	public String toString(){
        return getName();
    }
}

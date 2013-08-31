package org.datagr4m.topology.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.datagr4m.datastructures.pairs.Pair;


public abstract class GenericGraphModel<T> {
    protected Map<T,NodeType> map = new HashMap<T,NodeType>();
    protected List<NodeType> types = new ArrayList<NodeType>();
    protected Map<T,IPropertyNode> modelMap = new HashMap<T,IPropertyNode>();
    
    public NodeType getType(IPropertyNode node){
        return getType(getSourceNode(node));
    }

    public NodeType newNodeType(IPropertyNode node){
        return newNodeType(getSourceNode(node));
    }
    
    public String getNodeLabel(IPropertyNode node, NodeType type) {
        return getNodeLabel(getSourceNode(node), type);
    }

    public abstract T getSourceNode(IPropertyNode node);
    public abstract NodeType newNodeType(T node);

    
    public String getNodeLabel(T node) {
        NodeType type = getType(node);
        return getNodeLabel(node, type);
    }

    public String getNodeLabel(T node, NodeType type) {
        String label = null;
        try {
            label = readLabel(node, type);//node.getProperty(type.getLabel()).toString();
        } catch (Exception e) {
            Logger.getLogger(GenericGraphModel.class).warn("null key for type label '" + type.getLabel() + "': " + node);
        }
        if (label == null)
            label = node.toString();
        return label;
    }
    
    public abstract String readLabel(T node, NodeType type) throws Exception;
    
    public void registerModel(T node, IPropertyNode model){
        modelMap.put(node, model);
    }
    
    public IPropertyNode getModel(T node){
        return modelMap.get(node);
    }
    
    public boolean containsNodeType(String name){
        return getNodeType(name) != null;
    }
    
    public NodeType getOrCreateNodeType(IPropertyNode node){
        return getOrCreateNodeType(getSourceNode(node));//.getNode()
    }

    public NodeType getOrCreateNodeType(T node) {
        NodeType type = newNodeType(node);
        if(getTypes().contains(type)){
            type = getNodeType(type);// use already define type according to name of properties
        }
        return type;
    }
    
    /** return node type that is equal to input, according to equals() method. O(N)*/
    public NodeType getNodeType(NodeType type){
        for(NodeType t: types){
            if(t.equals(type))
                return t;
        }
        return null;
    }
    
    public NodeType getNodeType(String name){
        for(NodeType t: types){
            if(t.getName().equals(name))
                return t;
        }
        return null;
    }
    
    public List<NodeType> getTypes() {
        return types;
    }

    public void setTypes(List<NodeType> types) {
        this.types = types;
    }

    public void reset(){
        types = new ArrayList<NodeType>();
    }
    
    public NodeType getType(T node){
        NodeType type = map.get(node);
        if(type==null){
            NodeType unknownType = newNodeType(node);
            type = getNodeType(unknownType);
            if(type==null)
                type= unknownType;
            map.put(node, type);
        }
        return type;
    }
    
    public static Pair<String, ImageIcon> getIdentity(IPropertyNode node) {
        return new Pair<String,ImageIcon>(node.getLabel(), node.getType().getImage());
    }
    
    public static Pair<String, ImageIcon> getIdentity(IPropertyNode node, GenericGraphModel<?> graphModel) {
        NodeType type = null;
        if(graphModel!=null)
            type = graphModel.getType(node);
        else
            type = graphModel.newNodeType(node);
        String label = graphModel.getNodeLabel(node, type);
        ImageIcon icon = type.getImage();
        return new Pair<String,ImageIcon>(label, icon);
    }
}

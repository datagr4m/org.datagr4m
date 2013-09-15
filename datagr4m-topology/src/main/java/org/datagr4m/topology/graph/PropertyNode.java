package org.datagr4m.topology.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class PropertyNode implements IPropertyNode{
    protected NodeType type;
    protected String label;
    protected SetMultimap<String, IPropertyNode> informationNodes = null;

    Map<String,String> properties = new HashMap<String,String>();
    
    public PropertyNode() {
    }

    public PropertyNode(String label) {
        this.label = label;
        this.type = NodeType.undefined();
    }

    public PropertyNode(String label, NodeType type) {
        this.label = label;
        this.type = type;
    }

    
    public PropertyNode(Map<String, String> properties, GenericGraphModel<Map<String,String>> model) {
        configure(properties, model);
    }

    public PropertyNode(Map<String, String> properties, NodeType type, String label) {
        configure(properties, type, label);
    }


    public void configure(Map<String, String> properties, GenericGraphModel<Map<String,String>> model){
        configure(properties, model.getOrCreateNodeType(properties), model.getNodeLabel(properties));
    }

    public void configure(Map<String, String> properties, NodeType type, String label) {
        this.properties = properties;
        this.type = type;
        this.label = label;
    }
    
    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PropertyNode other = (PropertyNode) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }
    

    @Override
    public Set<String> getPropertyNames(){
        return properties.keySet();
    }

    @Override
    public String getPropertyValue(String property){
        return properties.get(property);
    }


    @Override
    public SetMultimap<String, IPropertyNode> getInformationNodes() {
        return informationNodes;
    }

    @Override
    public void setInformationNodes(SetMultimap<String, IPropertyNode> informationNodes) {
        this.informationNodes = informationNodes;
    }

    @Override
    public void addInformationNode(String type, IPropertyNode propertyNodes) {
        if (informationNodes == null)
            informationNodes = HashMultimap.create();//ArrayListMultimap.create();
        this.informationNodes.put(type, propertyNodes);
    }
    
    @Override
    public Object getNode() {
        return properties;
    }

    @Override
    public NodeType getType() {
        return type;
    }
    
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }
    
    @Override
    public boolean isPropertyNode(){
        return getType().isPropertyNode();
    }

    @Override
    public String toString(){
        return getLabel();
    }
}

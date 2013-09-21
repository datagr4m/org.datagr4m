package org.datagr4m.topology.graph;

import java.util.Set;

import com.google.common.collect.SetMultimap;

public interface IPropertyNode {
    public NodeType getType();
    public String getLabel();
    public void setLabel(String label);
    
    
    public Set<String> getPropertyNames();
    public String getPropertyValue(String property);
    
    public boolean isPropertyNode();
    
    public Object getNode();

    public SetMultimap<String, IPropertyNode> getInformationNodes();
    public void setInformationNodes(SetMultimap<String, IPropertyNode> informationNodes);
    public void addInformationNode(String type, IPropertyNode propertyNodes);
}

package com.datagr4m.graphml;

import java.util.Map;

import org.datagr4m.topology.graph.GenericGraphModel;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.NodeType;
import org.datagr4m.topology.graph.PropertyNode;


public class PropertyNodeGraphModel extends GenericGraphModel<Map<String,String>>{
    @Override
    public Map<String, String> getSourceNode(IPropertyNode node) {
        PropertyNode pn = (PropertyNode)node;
        return pn.getProperties();
    }

    @Override
    public NodeType newNodeType(Map<String, String> node) {
        return new NodeType(node.keySet());
    }

    @Override
    public String readLabel(Map<String, String> node, NodeType type) throws Exception {
        return node.get(type.getLabel());
    }
}

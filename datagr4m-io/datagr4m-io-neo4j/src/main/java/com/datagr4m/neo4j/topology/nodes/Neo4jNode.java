package com.datagr4m.neo4j.topology.nodes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.NodeType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class Neo4jNode implements IPropertyNode {
    protected Node node;
    protected NodeType type;
    protected String label;
    protected Set<String> propertyNames;
    
    protected SetMultimap<String, IPropertyNode> informationNodes = null;

    protected Neo4jNode() {}
    
    public static NodeType newNodeType(Node node) {
        return new NodeType(Neo4jNode.properties(node));
    }
    
    public Neo4jNode(Node node, Neo4jGraphModel model) {
        this(node, model.getOrCreateNodeType(node), model.getNodeLabel(node));
    }

    public Neo4jNode(Node node, NodeType type, String label) {
        this.node = node;
        this.type = type;
        this.label = label;
    }
    
    @Override
    public Set<String> getPropertyNames(){
        if(propertyNames==null)
            propertyNames = properties(node);
        return propertyNames;
    }

    @Override
    public String getPropertyValue(String property){
        return node.getProperty(property).toString();
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
    public Node getNode() {
        return node;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((node == null) ? 0 : node.hashCode());
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
        Neo4jNode other = (Neo4jNode) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return getLabel();
    }
    

    public StringBuilder getAllProperties() {
        StringBuilder labelBuilder = new StringBuilder();
        Iterator<String> keys = getNode().getPropertyKeys().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = getNode().getProperty(key).toString();
            labelBuilder.append(key + ":" + value + "\n");
        }
        return labelBuilder;
    } 
    
    
    public static Set<String> properties(Node node){
        Set<String> properties = new TreeSet<String>();
        Iterator<String> keys = node.getPropertyKeys().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            properties.add(key);
        }
        
        
        return properties;
    }
    
    public static Set<String> properties(Relationship relationship){
        Set<String> properties = new HashSet<String>();
        Iterator<String> keys = relationship.getPropertyKeys().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            System.err.println(key);
            properties.add(key);
        }
        return properties;
    }
    
    public static Set<String> relationTypes(Node node){
        Set<String> properties = new TreeSet<String>();
        Iterator<Relationship> keys = node.getRelationships().iterator();
        while(keys.hasNext()){
            Relationship key = keys.next();
            properties.add(key.getType().name());
        }
        return properties;
    }
}

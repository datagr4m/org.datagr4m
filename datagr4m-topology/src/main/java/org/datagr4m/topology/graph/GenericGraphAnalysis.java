package org.datagr4m.topology.graph;

import java.util.Collection;
import java.util.Set;

import org.datagr4m.datastructures.pairs.Pair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public abstract class GenericGraphAnalysis<T> {
    public abstract NodeType newNodeType(T node);
    public abstract Set<String> relationTypes(T node);
    public abstract Multimap<String,Pair<String,T>> collectNodeAttributes(Collection<T> nodes);

    
    public Multimap<NodeType, T> getNodeTypes(Collection<T> nodes){
        Multimap<NodeType, T> m = ArrayListMultimap.create();
        
        for(T node: nodes){
            NodeType type = newNodeType(node);
            if(m.keySet().contains(type)){
                m.get(type).add(node);
                
                // add relationships to this type
                for(NodeType original: m.keySet()){
                    if(original.equals(type)){
                        Set<String> relations = relationTypes(node);
                        original.getRelations().addAll(relations);
                        break;
                    }
                }
            }
            else
                m.put(type, node);
        }
        
        return m;
    }

    public Pair<String, T> valueNode(T node, String value) {
        return new Pair<String,T>(value, node);
    }
}

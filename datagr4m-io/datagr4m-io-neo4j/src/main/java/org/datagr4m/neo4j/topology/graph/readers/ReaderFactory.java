package org.datagr4m.neo4j.topology.graph.readers;

import java.util.HashMap;
import java.util.Map;


public class ReaderFactory {
    Map<String,INeo4jGraphReader> filters = new HashMap<String,INeo4jGraphReader>();
    
    public ReaderFactory(){
        register(FullGraphReader.FILTER_NAME, new FullGraphReader());
        register(FirstRelationshipGraphReader.FILTER_NAME, new FirstRelationshipGraphReader());
    }
    
    public INeo4jGraphReader getFilter(String name) {
        return filters.get(name);
    }

    public void register(String name, INeo4jGraphReader filter){
        filters.put(name, filter);
    }
}

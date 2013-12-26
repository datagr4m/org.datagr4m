package org.datagr4m.topology.graph;

import java.util.Map;


public interface IPropertyEdge {
    public static final String PROPERTY_SOURCE_INTERFACE = "sourceInterface";
    public static final String PROPERTY_TARGET_INTERFACE = "targetInterface";
    public String getTypeName();
    public Map<String,Object> getProperties();
}

package com.netscope.neo4j.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.apps.designer.IDesigner;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.PropertyEdge;
import org.datagr4m.topology.graph.PropertyNode;
import org.datagr4m.workspace.Workspace;

import com.datagr4m.neo4j.apps.gui.NetScope2Launcher;
import com.datagr4m.neo4j.topology.Neo4jTopology;
import com.datagr4m.neo4j.workspace.Neo4jWorkspace;

import edu.uci.ics.jung.graph.Graph;

public class DemoDirectedEdge2 {
    public static void main(String[] args){
        List<String> items = new ArrayList<String>();
        items.add("d1");
        items.add("d2");
        items.add("d3");
        items.add("d4");
        
        Map<String,PropertyNode> nodes = new HashMap<String,PropertyNode>();
        for(String item: items)
            nodes.put(item,  new PropertyNode(item));
        
        // cree une topologie manuellement
        Neo4jTopology topology = new Neo4jTopology();
        Graph<IPropertyNode,IPropertyEdge> graph = topology.getGlobalGraph();
        for(PropertyNode d: nodes.values()){
            System.out.println("add " + d);
            graph.addVertex(d);
        }

        // sous graphe de test 1
        graph.addEdge(new PropertyEdge("n1"), nodes.get("d1"), nodes.get("d2"));
        graph.addEdge(new PropertyEdge("n2"), nodes.get("d1"), nodes.get("d2"));
        graph.addEdge(new PropertyEdge("n3"), nodes.get("d1"), nodes.get("d2"));
        graph.addEdge(new PropertyEdge("n4"), nodes.get("d1"), nodes.get("d2"));
        graph.addEdge(new PropertyEdge("n5"), nodes.get("d1"), nodes.get("d3"));
        graph.addEdge(new PropertyEdge("n6"), nodes.get("d3"), nodes.get("d4"));
        topology.createDefaultRootGroup("root", "notype");
        topology.index();
        
        // open window
        Neo4jWorkspace w = Neo4jWorkspace.build(topology);
        IDesigner nd = new NetScope2Launcher().start(w);
    }
}

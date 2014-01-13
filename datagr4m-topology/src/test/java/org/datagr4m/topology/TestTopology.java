package org.datagr4m.topology;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;

import edu.uci.ics.jung.graph.Graph;

public class TestTopology extends TestCase{
    public void testSubgraph(){
        Topology<String, String> topo = new Topology<String, String>();
        Graph<String, String> graph = topo.getGraph();

        graph.addVertex("d1");
        graph.addVertex("d2");
        graph.addVertex("d3");
        graph.addVertex("d4");
        graph.addEdge("1>3", "d1", "d3");
        graph.addEdge("1>2", "d1", "d2");

        assertTrue(graph.getEdgeCount()==2);
        
        
        Group<String> group = topo.getItemsWithoutGroup();
        Graph<String, String> subgraph = topo.getSubGraph(group);
        
        assertTrue(subgraph.getVertexCount()==4);
        assertTrue(subgraph.getEdgeCount()==2);
        
        //System.out.println(graph.getEdges());

    }
    
    public void testTopoFilterNoGroup(){
        Topology<String, String> topo = new Topology<String, String>();
        Graph<String, String> graph = topo.getGraph();

        graph.addVertex("d1");
        graph.addVertex("d2");
        graph.addVertex("d3");
        graph.addVertex("d4");
        graph.addEdge("1>3", "d1", "d3");
        graph.addEdge("1>2", "d1", "d2");

        List<String> filter = new ArrayList<String>();
        filter.add("d1");
        filter.add("d2");
        
        Topology<String, String> topo2 = topo.filter(filter);
        assertTrue(topo2.getGraph().getVertices().contains("d1"));
        assertTrue(topo2.getGraph().getVertices().contains("d2"));
        assertFalse(topo2.getGraph().getVertices().contains("d3"));
        assertFalse(topo2.getGraph().getVertices().contains("d4"));

        assertTrue(topo2.getGraph().getEdges().contains("1>2"));
        assertFalse(topo2.getGraph().getEdges().contains("1>3"));

    }
    
    public void testTopoFilterGroup(){
        Topology<String, String> topo = new Topology<String, String>();
        Graph<String, String> graph = topo.getGraph();

        graph.addVertex("d1");
        graph.addVertex("d2");
        graph.addVertex("d3");
        graph.addVertex("d4");
        graph.addEdge("1>3", "d1", "d3");
        graph.addEdge("1>2", "d1", "d2");
        
        Group<String> g1 = new Group<String>();
        g1.setName("group1");
        g1.add("d1");
        g1.add("d2");
        
        Group<String> g2 = new Group<String>();
        g2.setName("group2");
        g2.add("d3");
        g2.add("d4");
        
        topo.addGroup(g1);
        topo.addGroup(g2);
        topo.index();
        
        List<String> filter = new ArrayList<String>();
        filter.add("d1");
        filter.add("d2");
        
        Topology<String, String> topo2 = topo.filter(filter);
        assertTrue(topo2.getGraph().getVertices().contains("d1"));
        assertTrue(topo2.getGraph().getVertices().contains("d2"));
        assertFalse(topo2.getGraph().getVertices().contains("d3"));
        assertFalse(topo2.getGraph().getVertices().contains("d4"));

        assertTrue(topo2.getGraph().getEdges().contains("1>2"));
        assertFalse(topo2.getGraph().getEdges().contains("1>3"));

        assertTrue("filtered topology should contain group 1", topo2.getGroups().contains(g1));
        assertFalse("filtered topology should NOT contain group 2", topo2.getGroups().contains(g2));
        
        //System.out.println("topo groups: "+topo2.getGroups());
    }
    
    public void testTopoFilterHierarchicalGroup(){
        Topology<String, String> topo = new Topology<String, String>();
        Graph<String, String> graph = topo.getGraph();

        graph.addVertex("d1");
        graph.addVertex("d2");
        graph.addVertex("d3");
        graph.addVertex("d4");
        graph.addEdge("1>3", "d1", "d3");
        graph.addEdge("1>2", "d1", "d2");
        
        Group<String> g1 = new Group<String>();
        g1.setName("group1");
        g1.add("d1");
        g1.add("d2");
        Group<String> superg1 = new Group<String>();
        superg1.setName("super.group1");
        superg1.addSubGroup(g1);
        
        Group<String> g2 = new Group<String>();
        g2.setName("group2");
        g2.add("d3");
        g2.add("d4");
        Group<String> superg2 = new Group<String>();
        superg2.setName("super.group2");
        superg2.addSubGroup(g2);
        
        topo.addGroup(superg1);
        topo.addGroup(superg2);
        topo.index();

        topo.toConsole();

        
        List<String> filter = new ArrayList<String>();
        filter.add("d1");
        filter.add("d2");
        
        Topology<String, String> topo2 = topo.filter(filter);
        assertTrue(topo2.getGraph().getVertices().contains("d1"));
        assertTrue(topo2.getGraph().getVertices().contains("d2"));
        assertFalse(topo2.getGraph().getVertices().contains("d3"));
        assertFalse(topo2.getGraph().getVertices().contains("d4"));

        assertTrue(topo2.getGraph().getEdges().contains("1>2"));
        assertFalse(topo2.getGraph().getEdges().contains("1>3"));

        topo2.toConsole();

        assertTrue("filtered topology should contain super group 1", topo2.getGroups().contains(superg1));
        assertFalse("filtered topology should NOT contain super group 2", topo2.getGroups().contains(superg2));

        Group<String> superg1copy = topo2.getGroups().get(0);
        //System.out.println("superg1copy:\n"+superg1copy.getSubGroups());
        assertTrue("filtered super group 1 contains g1", superg1copy.getSubGroups().contains(g1));
    }
}

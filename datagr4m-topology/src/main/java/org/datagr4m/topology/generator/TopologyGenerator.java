package org.datagr4m.topology.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;

import edu.uci.ics.jung.graph.Graph;

/**
 * Mainly used for test and demo purpose
 * @author Martin Pernollet
 */
public class TopologyGenerator {
    public static Topology<String,String> buildGraphNested(int depth, int width, int nedge){
        Topology<String,String> topo = new Topology<String,String>();
        topo.addGroups(generateGroups(depth, width, width, 0), true);
        generateGraph(topo, nedge);
        topo.index();
        return topo;
    }

    public static Topology<String,String> buildGraphNested(int depth, int width, int nChildren, int nedge){
        Topology<String,String> topo = new Topology<String,String>();
        
        if(depth>0)
            topo.addGroups(generateGroups(depth, width, nChildren, 0), true);
        else{
            for (int i = 0; i < nChildren; i++) {
                topo.getGraph().addVertex(genItem());
            }
        }
        generateGraph(topo, nedge);
        topo.index();
        return topo;
    }

    protected static List<Group<String>> generateGroups(int depth, int width, int nChildren, int currentDepth) {
        List<Group<String>> groups = new ArrayList<Group<String>>();
        
        for (int i = 0; i < width; i++) {
            Group<String> g = new Group<String>(genGroup());
            
            if(currentDepth<depth){
                g.addSubGroups(generateGroups(depth, width, nChildren, currentDepth+1));
            }
            else{
                for (int j = 0; j < nChildren; j++) {
                    g.add(genItem());                    
                }
            }
            
            groups.add(g);
        }
        return groups;
    }
    
    protected static void generateGraph(Topology<String,String> topo, int maxEdges){
        Graph<String,String> graph = topo.getGraph();
        List<String> vertices = new ArrayList<String>(graph.getVertices());
        Collections.shuffle(vertices);
        
        int n = 0;
        for(String s1: vertices){
            for(String s2: vertices){
                if(s1!=s2){
                    graph.addEdge(genEdge(), s1, s2);
                    n++;
                    if(n>=maxEdges)
                        return;
                }
            }            
        }
        System.err.println("created " + n + " edges");
    }
    
    protected static String genItem(){
        return "Item" + (itemId++);
    }

    protected static String genGroup(){
        return "Group" + (groupId++);
    }
    
    protected static String genEdge(){
        return "Edge" + (edgeId++);
    }

    protected static int itemId = 0;
    protected static int groupId = 0;
    protected static int edgeId = 0;
}

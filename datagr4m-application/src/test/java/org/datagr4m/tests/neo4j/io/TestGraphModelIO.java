package org.datagr4m.tests.neo4j.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.datagr4m.topology.graph.NodeType;
import org.junit.Test;
import org.jzy3d.io.SimpleFile;

import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;
import com.datagr4m.neo4j.topology.graph.Neo4jGraphModelIO;

public class TestGraphModelIO {

    @Test
    public void test() throws IOException {
        Neo4jGraphModelIO n = new Neo4jGraphModelIO();
        
        List<String> lines = SimpleFile.read("data/workspaces/doctorwho-flatgraph/model.ns");
        
        n.read(lines);
        Neo4jGraphModel model = n.getGraphModel();
        NodeType nAct1 = model.getNodeType("Actor");
        NodeType nAct2 = model.getNodeType("Actor2");
        NodeType nAct3 = model.getNodeType("Actor3");
        
        assertEquals(nAct1.getName(), nAct2.getInheritance());
        assertEquals(nAct3.getSuperType(), nAct1);
    }    
}

package org.datagr4m.trials.topology;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.datagr4m.application.neo4j.workspace.Neo4jWorkspace;
import org.datagr4m.io.xml.generated.dataprism.Andby;
import org.datagr4m.io.xml.generated.dataprism.Attribute;
import org.datagr4m.io.xml.generated.dataprism.Dataprism;
import org.datagr4m.io.xml.generated.dataprism.Groupby;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import com.datagr4m.neo4j.topology.Neo4jTopology;
import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;
import com.datagr4m.neo4j.topology.graph.Neo4jGraphModelIO;
import com.datagr4m.neo4j.topology.graph.readers.FullGraphReader;
import com.datagr4m.neo4j.topology.graph.readers.INeo4jGraphReader;

import edu.uci.ics.jung.graph.Graph;


public class TrialTestNeo4jTopologyEditorEmbeddedDb {
    //@Test
    public void testClusterByLob() throws Exception {
        Neo4jGraphModel typeModel = getGraphModel(getGraphModelConfig());
        Neo4jTopology topology = getTopology(operation, typeModel);
        
        assertTrue(topology.getGraph().getVertexCount()>1);
        
        // ----------------------------
        // Edit topology according to dataprism conf
        Dataprism projection = new Dataprism();
        Groupby gb = new Groupby();
        Attribute a1 = new Attribute();
        a1.setFor("Employee");
        a1.setName("lob");
        gb.setAttribute(a1);
        projection.getGroupby().add(gb);
        topology.edit().apply(projection);
        
        topology.toConsole();
        
        assertTrue(topology.hasGroup("Bresil"));
        assertTrue(topology.hasGroup("Services, Industrie, Distribution, Energie"));
        assertTrue(topology.hasGroup("LAB"));
        assertTrue(topology.hasGroup("Operations"));
        assertTrue(topology.hasGroup("Banque and Assurance"));
        assertTrue(topology.hasGroup("Maroc"));
        assertTrue(topology.hasGroup("Big Data and Analytics"));
        assertTrue(topology.hasGroup("Telecom, Internet, Media, Entertainment"));
        assertTrue(topology.hasGroup("Suisse"));
    }
    
    //@Test
    public void testClusterByLobAndByAge() throws Exception {
        Neo4jGraphModel typeModel = getGraphModel(getGraphModelConfig());
        Neo4jTopology topology = getTopology(operation, typeModel);

        assertTrue(topology.getGraph().getVertexCount()>1);

        // ----------------------------
        // Edit topology according to dataprism conf
        Dataprism projection = new Dataprism();
        
        Groupby groupBy = new Groupby();
        Attribute a1 = new Attribute();
        a1.setFor("Employee");
        a1.setName("lob");
        groupBy.setAttribute(a1);
        
        Andby andBy = new Andby();
        Attribute a2 = new Attribute();
        a2.setFor("Employee");
        a2.setName("job");
        andBy.setAttribute(a2);
        
        groupBy.setAndby(andBy);
        projection.getGroupby().add(groupBy);
        topology.edit().apply(projection);
        
        
        topology.toConsole();
        
        assertTrue(topology.hasGroup("Bresil"));
        assertTrue(topology.getGroup("Bresil").hasSubGroup("Consultant - level 3"));
        assertTrue(topology.getGroup("Bresil").hasSubGroup("Consultant"));
        assertTrue(topology.getGroup("Bresil").getSubGroups().size()==2);
        
        Group<IPropertyNode> g = topology.getGroup("Services, Industrie, Distribution, Energie");
        g.hasSubGroup("LOB Manager");
        g.hasSubGroup("Manager");
        g.hasSubGroup("Ingenieur d'Affaires");
        g.hasSubGroup("Consultant - level 2");
        g.hasSubGroup("Consultant - level 3");
        g.hasSubGroup("Consultant - level 4");
        assertTrue(g.getSubGroups().size()==7);
    }
    
    /* */
    
    protected static String TEST_DB = "data/databases/test-octo";
    protected GraphDatabaseFactory factory;
    protected GraphDatabaseService database;
    protected GlobalGraphOperations operation;

    @Before
    public void before(){
        Logger.getLogger(this.getClass()).info("using db " + TEST_DB);
        factory = new GraphDatabaseFactory();
        database = factory.newEmbeddedDatabase(TEST_DB);
        operation = GlobalGraphOperations.at(database);
    }
    
    @After
    public void after(){
        database.shutdown();
    }
    
    public List<String> getGraphModelConfig() {
        List<String> conf = new ArrayList<String>();
        conf.add("type Employee{");
        conf.add(" property: anciennete");
        conf.add(" property: id");
        conf.add(" property: job");
        conf.add(" property: label");
        conf.add(" property: lob");
        conf.add(" relation: unknown");
        conf.add(" label: id");
        conf.add(" icon: doctorwho/character.png");
        conf.add("}");
        return conf;
    }

    public Neo4jTopology getTopology(GlobalGraphOperations operation, Neo4jGraphModel typeModel) {
        INeo4jGraphReader r = new FullGraphReader();
        Graph<IPropertyNode, IPropertyEdge> graph = r.read(operation, typeModel);
        Logger.getLogger(Neo4jWorkspace.class).info("filtered graph has " + graph.getVertexCount() + " nodes using db " + TEST_DB);
        Neo4jTopology topology = new Neo4jTopology(graph, typeModel);
        return topology;
    }

    public Neo4jGraphModel getGraphModel(List<String> conf) {
        Neo4jGraphModelIO gio = new Neo4jGraphModelIO();
        gio.read(conf);
        Neo4jGraphModel typeModel = gio.getGraphModel();
        return typeModel;
    }
}

package com.datagr4m.graphml;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.Factory;
import org.xml.sax.SAXException;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;

public class PropertyGraphMLReader<V,E> {
    
    
    public void read(Graph<V,E> graph, String file, Factory<V> vf, Factory<E> ef) throws ParserConfigurationException, SAXException, IOException{
            
        //Step 1 we make a new GraphML Reader. We want an Undirected Graph of type node and edge.
          GraphMLReader<Graph<V,E>, V,E> gmlr = new GraphMLReader<Graph<V,E>, V,E>(vf, ef);
           
          //Here we read in our graph. filename is our .graphml file, and graph is where we will store our graph.
          gmlr.load(file, graph);
          
          BidiMap<V, String> vertex_ids = gmlr.getVertexIDs();  //The vertexIDs are stored in a BidiMap.
          Map<String, GraphMLMetadata<V>> vertex_color = gmlr.getVertexMetadata(); //Our vertex Metadata is stored in a map.
          Map<String, GraphMLMetadata<E>> edge_meta = gmlr.getEdgeMetadata(); // Our edge Metadata is stored in a map.
           
          // Here we iterate through our vertices, n, and we set the value and the color of our nodes from the data we have
          // in the vertex_ids map and vertex_color map.
          for (V n : graph.getVertices())
          {
              //n.setValue(vertex_ids.get(n)); //Set the value of the node to the vertex_id which was read in from the GraphML Reader.
              //n.setColor(vertex_color.get("d0").transformer.transform(n)); // Set the color, which we get from the Map vertex_color.            //Let's print out the data so we can get a good understanding of what we've got going on.
              //System.out.println("ID: "+n.getID()+", Value: "+n.getValue()+", Color: "+n.getColor());
          }
           // Just as we added the vertices to the graph, we add the edges as well.
          for (E e : graph.getEdges())
          {
              //e.setValue(edge_meta.get("d1").transformer.transform(e)); //Set the edge's value.
              //System.out.println("Edge ID: "+e.getID());
          }
      }
}

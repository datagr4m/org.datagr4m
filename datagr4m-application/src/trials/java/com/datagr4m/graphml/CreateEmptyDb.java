package com.datagr4m.graphml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.xml.sax.SAXException;

public class CreateEmptyDb {
    
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( "data/databases/untitled" );
    }
}

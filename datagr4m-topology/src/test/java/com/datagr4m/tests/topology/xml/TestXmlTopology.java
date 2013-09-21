package com.datagr4m.tests.topology.xml;

import org.datagr4m.topology.Topology;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.io.XmlTopology;
import org.junit.Test;

public class TestXmlTopology {

	
	
	@Test
	public void test() throws Exception {
		XmlTopology xmlt = new XmlTopology();
		//Topology<IPropertyNode, IPropertyEdge> topology  = xmlt.loadTopology("xml.datagr4m/topology.xml");
		//System.err.println(new File("./").getAbsolutePath());
		//Topology<IPropertyNode, IPropertyEdge> topology  = xmlt.loadTopology("data\\workspaces\\xmltopo\\topology.xml");
		Topology<IPropertyNode, IPropertyEdge> topology  = xmlt.loadTopology("data/workspaces/xmltopo/topology.xml");
		topology.toConsole();
		
		
		//topology.g
	}

}

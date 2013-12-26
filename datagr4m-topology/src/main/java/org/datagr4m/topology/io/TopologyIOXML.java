package org.datagr4m.topology.io;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.datagr4m.io.xml.JAXBHandler;
import org.datagr4m.io.xml.generated.topology.Edge;
import org.datagr4m.io.xml.generated.topology.Graph;
import org.datagr4m.io.xml.generated.topology.Group;
import org.datagr4m.io.xml.generated.topology.Groups;
import org.datagr4m.io.xml.generated.topology.Node;
import org.datagr4m.io.xml.generated.topology.Topology;
import org.datagr4m.topology.TopologyNodeLabelIndex;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.NodeType;
import org.datagr4m.topology.graph.PropertyEdge;
import org.datagr4m.topology.graph.PropertyNode;

public class TopologyIOXML {
	static JAXBHandler handler = JAXBHandler.xmlHandlerTopology();

	public org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge> loadTopology(
			String file) throws Exception {
		org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge> topology = new org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge>();
		Topology xmlTopo = readXmlObject(file);
		readTopology(topology, xmlTopo);
		return topology;
	}
	

	public Topology readXmlObject(String file) throws Exception {
		Logger.getLogger(TopologyIOXML.class).info("reading topology from " + file);
		return (Topology) handler.unmarshall(file);
	}
	
	/* XML READ */

	public void readTopology(
			org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge> topology,
			Topology xmlTopo) {
		readGroups(topology, xmlTopo);
		readGraph(topology, xmlTopo);
	}

	protected void readGraph(
			org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge> topology,
			Topology xmlTopo) {
		TopologyNodeLabelIndex labelIndex = new TopologyNodeLabelIndex(topology);
		edu.uci.ics.jung.graph.Graph<IPropertyNode,IPropertyEdge> graph = topology.getGraph();
		Graph xmlGraph = xmlTopo.getGraph();
		for(Edge xmlEdge: xmlGraph.getEdge()){
			readEdge(labelIndex, graph, xmlEdge);
		}
	}

	protected void readEdge(TopologyNodeLabelIndex labelIndex,
			edu.uci.ics.jung.graph.Graph<IPropertyNode, IPropertyEdge> graph,
			Edge xmlEdge) {
		IPropertyEdge edge = new PropertyEdge();
		
		String src = xmlEdge.getSource();
		String trg = xmlEdge.getTarget();
		String srcSlot = xmlEdge.getSourceSlot();
		String trgSlot = xmlEdge.getTargetSlot();
		
		edge.getProperties().put(IPropertyEdge.PROPERTY_SOURCE_INTERFACE, srcSlot);
        edge.getProperties().put(IPropertyEdge.PROPERTY_TARGET_INTERFACE, trgSlot);
        
		//edge.set
		Collection<IPropertyNode> srcs = labelIndex.find(src);
		Collection<IPropertyNode> trgs = labelIndex.find(trg);
		
		IPropertyNode srcNode = null;
		if(srcs.size()>0){
			srcNode = srcs.iterator().next();
		}
		else
			Logger.getLogger(TopologyIOXML.class).warn("no src node for label " + src);

		IPropertyNode trgNode = null;
		if(trgs.size()>0){
			trgNode = trgs.iterator().next();
		}
		else
			Logger.getLogger(TopologyIOXML.class).warn("no trg node for label " + trg);

		if(srcNode!=null && trgNode!=null)
			graph.addEdge(edge, srcNode, trgNode);
	}

	protected void readGroups(
			org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge> topology,
			Topology xmlTopo) {
		Groups xmlGroups = xmlTopo.getGroups();
		if (xmlGroups != null && xmlGroups.getGroup() != null) {
			for (Group xmlGroup : xmlGroups.getGroup()) {
				readGroup(topology, xmlGroup);
			}
		}
	}

	protected void readGroup(
			org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge> topology,
			Group xmlGroup) {
		String groupName = xmlGroup.getName();
		String groupType = xmlGroup.getType();

		org.datagr4m.topology.Group<IPropertyNode> group = new org.datagr4m.topology.Group<IPropertyNode>(
				groupName, groupType);
		if (xmlGroup.getNode() != null) {
			for (Node xmlNode : xmlGroup.getNode()) {
				readNode(topology, group, xmlNode);
			}
		}
		if (xmlGroup.getGroup() != null) {
			for (Group xmlGrp : xmlGroup.getGroup()) {
				readSubGroup(topology, group, xmlGrp);
			}
		}
		topology.addGroup(group);
	}

	protected void readSubGroup(org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge> topology,
			org.datagr4m.topology.Group<IPropertyNode> parent, Group xmlGroup) {
		String groupName = xmlGroup.getName();
		String groupType = xmlGroup.getType();

		org.datagr4m.topology.Group<IPropertyNode> group = new org.datagr4m.topology.Group<IPropertyNode>(
				groupName, groupType);
		if (xmlGroup.getNode() != null) {
			for (Node xmlNode : xmlGroup.getNode()) {
				readNode(topology, group, xmlNode);
			}
		}
		if (xmlGroup.getGroup() != null) {
			for (Group xmlGrp : xmlGroup.getGroup()) {
				readSubGroup(topology, group, xmlGrp);
			}
		}
		parent.addSubGroup(group);
	}

	protected void readNode(org.datagr4m.topology.Topology<IPropertyNode, IPropertyEdge> topology,
			org.datagr4m.topology.Group<IPropertyNode> group,
			Node xmlNode) {
		String name = xmlNode.getName();
		String type = xmlNode.getType();

		NodeType nodeType = new NodeType(type);
		IPropertyNode node = new PropertyNode(name, nodeType);
		
		topology.getGraph().addVertex(node);
		group.add(node);
	}
}

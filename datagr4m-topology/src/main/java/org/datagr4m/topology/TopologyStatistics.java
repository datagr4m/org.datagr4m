package org.datagr4m.topology;

public class TopologyStatistics {
    protected Topology<?,?> t;
    public int nNodes;
    public int nEdges;
    public int nGroups;
    public int depth;
    public int nZones;
    public int nSingles;

    public TopologyStatistics(Topology<?, ?> t) {
        super();
        this.t = t;
        
        nNodes = t.getGraph().getVertexCount();
        nEdges = t.getGraph().getEdgeCount();
        nGroups = t.getGroups().size();
        depth = t.getDepth();
        nZones = t.getZones().size();
        nSingles = t.getItemsWithoutGroup().size();
    }
    
    public String toCompactString(){
        return "N:" + nNodes + " E:" + nGroups + " G:" + nGroups + " (D:" +depth+") Z:" + nZones + " S:" + nSingles;
    }
    
    @Override
	public String toString(){
        return "nNodes " + nNodes + " nEdges " + nEdges + " nGroups " + nGroups + " nZones " + nZones + " nSingles " + nSingles;
    }
}

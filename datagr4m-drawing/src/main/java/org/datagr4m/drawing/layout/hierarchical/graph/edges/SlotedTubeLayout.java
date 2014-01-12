package org.datagr4m.drawing.layout.hierarchical.graph.edges;

import org.datagr4m.drawing.layout.hierarchical.graph.edges.bundling.SlotedEdgeBundling;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;



public class SlotedTubeLayout extends HierarchicalEdgeLayout {
    private static final long serialVersionUID = 3650843179953494660L;

    public SlotedTubeLayout(){
        this(new PathFactory());
    }
    
    public SlotedTubeLayout(IPathFactory factory){
    	super();
    	itemSlotLayout = new DefaultItemSlotLayout(factory);
    	edgeBundling = new SlotedEdgeBundling();
    }
}

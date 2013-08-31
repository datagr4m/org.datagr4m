package org.datagr4m.tests.drawing.tubes.data;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.HierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;
import org.datagr4m.tests.drawing.tubes.ITubeDataTest;
import org.jzy3d.maths.Coord2d;


//construit model hierarchique
// root
//  group
//   pair1 i11 i12
//   pair2 i21 i22
public class HsrpPartialMeshDataTest extends AbstractTubeDataTest implements ITubeDataTest{
    public HierarchicalGraphModel items;
    public HierarchicalEdgeModel edges;
    
    public HierarchicalPairModel p1;
    public HierarchicalPairModel p2;
    public HierarchicalGraphModel graph;
    
    public DefaultBoundedItem i11;
    public DefaultBoundedItem i12;
    public DefaultBoundedItem i21;
    public DefaultBoundedItem i22;
    
    public HsrpPartialMeshDataTest(){
        i11 = new DefaultBoundedItem("i11");
        i12 = new DefaultBoundedItem("i12");
        p1 = new HierarchicalPairModel();
        p1.setObject("pair1");
        p1.addChild(i11);
        p1.addChild(i12);
        
        i21 = new DefaultBoundedItem("i21");
        i21.changePosition(Coord2d.ORIGIN);
        
        graph = new HierarchicalGraphModel();
        graph.setObject("group");
        graph.addChild(p1);
        graph.addChild(i21);
        
        items = new HierarchicalGraphModel();
        items.setObject("root");
        items.addChild(graph);
        //m.addChild(p1);
        //m.addChild(i21);
        
        items.addLocalEdge(link(i11, i12)); // internal
        items.addLocalEdge(link(i11, i21)); // pair to device 21
        items.addLocalEdge(link(i12, i21)); // pair to device 21
        
        edges = new HierarchicalEdgeModel();
        edges.build(items.getLocalEdges());
    }
    
    @Override
    public IHierarchicalModel getItemModel() {
        return items;
    }

    @Override
    public IHierarchicalEdgeModel getEdgeModel() {
        return edges;
    }

    @Override
    public IHierarchicalLayout makeTestLayout(){
        HierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        IHierarchicalLayout layout = layoutFactory.getLayout(items, edges);
        layout.initAlgo();
        
        items.changePosition(0,0);
        graph.changePosition(0,0);
        p1.changePosition(-100, 0);
        
        return layout;
    }

}

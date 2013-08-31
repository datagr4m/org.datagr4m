package org.datagr4m.tests.drawing.tubes.data;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.HierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;
import org.datagr4m.tests.drawing.tubes.ITubeDataTest;


//construit model hierarchique
// root
//  group
//   pair1 i11 i12
//   pair2 i21 i22
public class HsrpFullMeshDataTest extends AbstractTubeDataTest implements ITubeDataTest{
    public HierarchicalGraphModel items;
    public HierarchicalEdgeModel edges;
    
    public HierarchicalGraphModel graph;
    
    public HierarchicalPairModel p1;
    public HierarchicalPairModel p2;
    
    public DefaultBoundedItem i11;
    public DefaultBoundedItem i12;
    public DefaultBoundedItem i21;
    public DefaultBoundedItem i22;
    
    public HsrpFullMeshDataTest(){
     
        i11 = new DefaultBoundedItem("i11");
        i12 = new DefaultBoundedItem("i12");
        p1 = new HierarchicalPairModel();
        p1.setObject("pair1");
        p1.setLabel("pair1");
        p1.addChild(i11);
        p1.addChild(i12);
        
        i21 = new DefaultBoundedItem("i21");
        i22 = new DefaultBoundedItem("i22");
        p2 = new HierarchicalPairModel();
        p2.setObject("pair2");
        p2.setLabel("pair2");
        p2.addChild(i21);
        p2.addChild(i22);
        
        graph = new HierarchicalGraphModel();
        graph.setObject("group");
        graph.setLabel("group");
        graph.addChild(p1);
        graph.addChild(p2);
        graph.createAllMutualRepulsorsNoSymetry();
        
        items = new HierarchicalGraphModel();
        items.setObject("root");
        items.setLabel("root");
        items.addChild(graph);
        
        // ---------------------
        // construit un full mesh
        List<Pair<IBoundedItem,IBoundedItem>> links = new ArrayList<Pair<IBoundedItem,IBoundedItem>>();
        links.add(link(i11, i12)); // full mesh
        links.add(link(i21, i22));
        links.add(link(i11, i21));
        links.add(link(i11, i22));
        links.add(link(i12, i21));
        links.add(link(i12, i22));
        
        //
        edges = new HierarchicalEdgeModel();
        edges.build(links);
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
        p2.changePosition(+100, 0);
        
        return layout;
    }
}

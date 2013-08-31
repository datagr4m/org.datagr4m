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
public class HierarchicalFlipDataTest3 extends AbstractTubeDataTest implements ITubeDataTest {
    public HierarchicalEdgeModel edges;

    public HierarchicalGraphModel items;
    public HierarchicalGraphModel groupGroup1;

    public HierarchicalGraphModel groupI;
    public HierarchicalPairModel pI1;
    public HierarchicalPairModel pI2;
    public DefaultBoundedItem i11;
    public DefaultBoundedItem i12;
    public DefaultBoundedItem i21;
    public DefaultBoundedItem i22;

    public HierarchicalGraphModel groupJ;
    public HierarchicalPairModel pJ1;
    public HierarchicalPairModel pJ2;
    public DefaultBoundedItem j11;
    public DefaultBoundedItem j12;
    public DefaultBoundedItem j21;
    public DefaultBoundedItem j22;
    
    public HierarchicalGraphModel groupGroup2;
    public HierarchicalPairModel pK1;
    public DefaultBoundedItem k11;
    public DefaultBoundedItem k12;
    public DefaultBoundedItem k21;
    public DefaultBoundedItem k22;

    public HierarchicalFlipDataTest3() {
        // I Part
        i11 = new DefaultBoundedItem("i11");
        i12 = new DefaultBoundedItem("i12");
        pI1 = new HierarchicalPairModel();
        pI1.setObject("pairI1");
        pI1.setLabel("pairI1");
        pI1.addChild(i11);
        pI1.addChild(i12);

        i21 = new DefaultBoundedItem("i21");
        i22 = new DefaultBoundedItem("i22");
        pI2 = new HierarchicalPairModel();
        pI2.setObject("pairI2");
        pI2.setLabel("pairI2");
        pI2.addChild(i21);
        pI2.addChild(i22);

        groupI = new HierarchicalGraphModel();
        groupI.setObject("groupI");
        groupI.setLabel("groupI");
        groupI.addChild(pI1);
        groupI.addChild(pI2);
        groupI.createAllMutualRepulsorsNoSymetry();

        // J Part
        j11 = new DefaultBoundedItem("j11");
        j12 = new DefaultBoundedItem("j12");
        pJ1 = new HierarchicalPairModel();
        pJ1.setObject("pairJ1");
        pJ1.setLabel("pairJ1");
        pJ1.addChild(j11);
        pJ1.addChild(j12);

        j21 = new DefaultBoundedItem("j21");
        j22 = new DefaultBoundedItem("j22");
        pJ2 = new HierarchicalPairModel();
        pJ2.setObject("pairI2");
        pJ2.setLabel("pairJ2");
        pJ2.addChild(j21);
        pJ2.addChild(j22);

        groupJ = new HierarchicalGraphModel();
        groupJ.setObject("groupJ");
        groupJ.setLabel("groupJ");
        groupJ.addChild(pJ1);
        groupJ.addChild(pJ2);
        groupJ.createAllMutualRepulsorsNoSymetry();

        // K Part
        k11 = new DefaultBoundedItem("k11");
        k12 = new DefaultBoundedItem("k12");
        pK1 = new HierarchicalPairModel();
        pK1.setObject("pairK1");
        pK1.setLabel("pairK1");
        pK1.addChild(k11);
        pK1.addChild(k12);

        // main groups
        groupGroup1 = new HierarchicalGraphModel();
        groupGroup1.setObject("groupGroup1");
        groupGroup1.setLabel("groupGroup1");
        groupGroup1.addChild(groupI);
        groupGroup1.addChild(groupJ);
        
        groupGroup2 = new HierarchicalGraphModel();
        groupGroup2.setObject("groupGroup2");
        groupGroup2.setLabel("groupGroup2");
        groupGroup2.addChild(pK1);
        
        
        // ROOT
        items = new HierarchicalGraphModel();
        items.setObject("root");
        items.setLabel("root");
        items.addChild(groupGroup1);
        items.addChild(groupGroup2);
        
        
        
        // ---------------------
        // construit un full mesh
        edges = new HierarchicalEdgeModel();
        edges.build(makeLinks());
    }

    public List<Pair<IBoundedItem, IBoundedItem>> makeLinks() {
        List<Pair<IBoundedItem, IBoundedItem>> links = new ArrayList<Pair<IBoundedItem, IBoundedItem>>();

        links.add(link(j11, k11));
        //links.add(link(i12, k11)); // si seulement iN, ca marche

        links.add(link(k11, i11)); // << problem
        //Tube: {groupGroup1,groupGroup2} (flipped:false)
        //    Tube: {groupJ,groupGroup2} (flipped:false)
        //      Tube: {pairJ1,pairK1} (flipped:false)
        //        EdgePath: j11 k11 (flipped:false)
        //    Tube: {groupGroup2,groupI} (flipped:true)
        //      Tube: {pairK1,pairI1} (flipped:false)
        //        EdgePath: k11 i11 (flipped:false)
        return links;
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
    public IHierarchicalLayout makeTestLayout() {
        HierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
        IHierarchicalLayout layout = layoutFactory.getLayout(items, edges);
        layout.initAlgo();

        items.changePosition(0, 0);

        groupGroup1.changePosition(-400, -200);
        
        groupI.changePosition(-200, -50);
        pI1.changePosition(-100, 0);
        pI2.changePosition(+100, 0);

        groupJ.changePosition(+200, +50);
        pJ1.changePosition(-100, 0);
        pJ2.changePosition(+100, 0);
        
        groupGroup2.changePosition(+400, +200);
        pK1.changePosition(0, 0);


        layout.goAlgoEdge();

        return layout;
    }
}

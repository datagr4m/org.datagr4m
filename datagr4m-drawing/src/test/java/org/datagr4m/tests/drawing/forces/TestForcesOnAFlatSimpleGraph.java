package org.datagr4m.tests.drawing.forces;

import java.util.Collection;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FAAttraction;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FARepulsion;
import org.datagr4m.drawing.layout.algorithms.forces.IForce;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.tests.drawing.AbstractLayoutRunnerTest;
import org.datagr4m.topology.Topology;


import edu.uci.ics.jung.graph.Graph;

public class TestForcesOnAFlatSimpleGraph extends AbstractLayoutRunnerTest {
    boolean console = true;
    
    public static HierarchicalGraphModel buildGraphNested() {
        Topology<String, String> topo = new Topology<String, String>();
        Graph<String, String> graph = topo.getGraph();

        graph.addVertex("d1");
        graph.addVertex("d2");
        graph.addVertex("d3");
        graph.addVertex("d4");
        graph.addEdge("1>3", "d1", "d3");
        graph.addEdge("1>2", "d1", "d2");

        //System.out.println(graph.getEdges());
        
        topo.index();

        // make a model with forces
        IHierarchicalModelFactory factory = new HierarchicalTopologyModelFactory<String, String>();
        return (HierarchicalGraphModel) factory.getLayoutModel(topo);
    }

    public void testForces() {
        // build a model with forces
        HierarchicalGraphModel model = buildGraphNested();

        // ---------------------------
        // assert we found all items
        IBoundedItem d1 = model.getItem("d1");
        IBoundedItem d2 = model.getItem("d2");
        IBoundedItem d3 = model.getItem("d3");
        IBoundedItem d4 = model.getItem("d4");

        assertTrue("we have exactly 4 items", model.getChildren().size() == 4);

        assertTrue("d1 found", d1 != null);
        assertTrue("d2 found", d2 != null);
        assertTrue("d3 found", d3 != null);
        assertTrue("d4 found", d4 != null);

        // -- ASSERT REPULSIONS --

        // d1 forces
        Collection<IForce> forcesD1 = model.getRepulsors(d1);
        if (console) {
            for (IForce force : forcesD1)
                System.out.println("repulsor:" + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D1 must have repulsion with D2, D3, D4", forcesD1.size() == 3);
        assertTrue("D1 repulsed by D2", forcesD1.contains(new FARepulsion(d1, d2)));
        assertTrue("D1 repulsed by D3", forcesD1.contains(new FARepulsion(d1, d3)));
        assertTrue("D1 repulsed by D4", forcesD1.contains(new FARepulsion(d1, d4)));

        // d2 forces
        Collection<IForce> forcesD2 = model.getRepulsors(d2);
        if (console) {
            for (IForce force : forcesD2)
                System.out.println("repulsor:" + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D2 must have repulsion with D3, D4", forcesD2.size() == 2);
        assertTrue("D2 repulsed by D3", forcesD2.contains(new FARepulsion(d2, d3)));
        assertTrue("D2 repulsed by D4", forcesD2.contains(new FARepulsion(d2, d4)));

        // d3 forces
        Collection<IForce> forcesD3 = model.getRepulsors(d3);
        if (console) {
            for (IForce force : forcesD3)
                System.out.println("repulsor:" + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D3 must have repulsion with D4", forcesD3.size() == 1);
        assertTrue("D3 repulsed by D4", forcesD3.contains(new FARepulsion(d3, d4)));

        // d4 forces
        Collection<IForce> forcesD4 = model.getRepulsors(d4);
        if (console) {
            for (IForce force : forcesD4)
                System.out.println("repulsor:" + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D4 must have repulsion with nobody, " + "as it repulsed by all other and since repulsion is symetric", forcesD4.size() == 0);

        // -- ASSERT ATTRACTIONS --

        Collection<IForce> attrD1 = model.getAttractionEdgeForces(d1);
        if (console) {
            for (IForce force : attrD1)
                System.out.println("attractor:" + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D1 has two attractors to D2 and D3", attrD1.size() == 2);
        assertTrue("D1 attract D2", attrD1.contains(new FAAttraction(d1, d2)));
        assertTrue("D1 attract D3", attrD1.contains(new FAAttraction(d1, d3)));
        
        // assert D2 and D3 don't have dupplicated force
        if (console) {
            for (IForce force : model.getAttractionEdgeForces(d2))
                System.out.println("attractor:" + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D2 has no attractor", model.getAttractionEdgeForces(d2).size() == 0);
        assertTrue("D3 has no attractor", model.getAttractionEdgeForces(d3).size() == 0);
        assertTrue("D4 has no attractor", model.getAttractionEdgeForces(d4).size() == 0);
    }    
}

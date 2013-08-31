package org.datagr4m.tests.drawing.forces;

import java.util.Collection;

import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FAAttraction;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FARepulsion;
import org.datagr4m.drawing.layout.algorithms.forces.IForce;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;
import org.datagr4m.tests.drawing.AbstractLayoutRunnerTest;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;


import edu.uci.ics.jung.graph.Graph;

public class TestForcesOnASimpleHierarchicalGraph extends AbstractLayoutRunnerTest {
    boolean console = true;

    public void testForces() {
        // build a model with forces
        Topology<String, String> topo = new Topology<String, String>();
        Graph<String, String> graph = topo.getGlobalGraph();
        graph.addVertex("d1");
        graph.addVertex("d2");
        graph.addVertex("d3");
        graph.addVertex("d4");
        graph.addEdge("1>3", "d1", "d3");
        graph.addEdge("1>2", "d1", "d2");
        Group<String> g1 = new Group<String>("g1");
        g1.add("d1");
        g1.add("d2");
        Group<String> g2 = new Group<String>("g2");
        g2.add("d3");
        g2.add("d4");
        topo.getGroups().add(g1);
        topo.getGroups().add(g2);
        topo.index();

        IHierarchicalModelFactory factory = new HierarchicalTopologyModelFactory<String, String>();
        HierarchicalGraphModel model = (HierarchicalGraphModel) factory.getLayoutModel(topo);

        // -------------------
        // assert we found all items
        HierarchicalPairModel gm1 = (HierarchicalPairModel) model.getItem(g1); 
        HierarchicalPairModel gm2 = (HierarchicalPairModel) model.getItem(g2);

        assertTrue("gm1 found", gm1 != null);
        assertTrue("gm2 found", gm2 != null);
        assertTrue("we have exactly 2 graphs", model.getChildren().size() == 2);

        IBoundedItem d1 = model.getItem("d1"); // search with data
        IBoundedItem d2 = model.getItem("d2");
        IBoundedItem d3 = model.getItem("d3");
        IBoundedItem d4 = model.getItem("d4");

        assertTrue("d1 found", d1 != null);
        assertTrue("d2 found", d2 != null);
        assertTrue("we have exactly 2 items in gm1", gm1.getChildren().size() == 2);

        assertTrue("d3 found", d3 != null);
        assertTrue("d4 found", d4 != null);
        assertTrue("we have exactly 2 items in gm2", gm2.getChildren().size() == 2);

        // -------------------
        // ASSERT REPULSION

        // d1 forces
        /*Collection<IForce> forcesD1 = gm1.getRepulsors(d1);
        if (console) {
            for (IForce force : forcesD1)
                System.out.println("repulsor: " + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D1 must have repulsion with D2", forcesD1.size() == 1);
        assertTrue("D1 repulsed by D2", forcesD1.contains(new FARepulsion(d1, d2)));

        // d2 forces
        Collection<IForce> forcesD2 = gm1.getRepulsors(d2);
        if (console) {
            for (IForce force : forcesD2)
                System.out.println("repulsor: " + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D2 must have no repulsion with others", forcesD2.size() == 0);

        // d3 forces
        Collection<IForce> forcesD3 = gm2.getRepulsors(d3);
        if (console) {
            for (IForce force : forcesD3)
                System.out.println("repulsor: " + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D3 must have repulsion with D4", forcesD3.size() == 1);
        assertTrue("D3 repulsed by D4", forcesD3.contains(new FARepulsion(d3, d4)));

        // d4 forces
        Collection<IForce> forcesD4 = gm2.getRepulsors(d4);
        if (console) {
            for (IForce force : forcesD4)
                System.out.println("repulsor: " + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D4 must have no repulsion with others", forcesD4.size() == 0);*/

        // gm1 forces
        Collection<IForce> forcesGM1 = model.getRepulsors(gm1);
        if (console) {
            for (IForce force : forcesGM1)
                System.out.println("repulsor: " + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("GM1 must have repulsion with GM2", forcesGM1.size() == 1);
        assertTrue("GM1 repulsed by GM2", forcesGM1.contains(new FARepulsion(gm1, gm2)));

        // gm2 force
        Collection<IForce> forcesGM2 = model.getRepulsors(gm2);
        if (console) {
            for (IForce force : forcesGM2)
                System.out.println("repulsor: " + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("GM2 must have no repulsion", forcesGM2.size() == 0);
                    
        // ---------------------
        // ATTRACTION
        
        // d1            
        /*Collection<IForce> attractD1 = gm1.getAttractionEdgeForces(d1);
        if (console) {
            for (IForce force : attractD1)
                System.out.println("attractor: " + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("D1 must have repulsion with D2", attractD1.size() == 1);
        assertTrue("D1 attract D2", attractD1.contains(new FAAttraction(d1, d2)));
        
        // d2
        assertTrue("D1 must have repulsion with D2", gm1.getAttractionEdgeForces(d2).size() == 0);*/
        
        // gm1
        Collection<IForce> attractGM1 = model.getAttractionEdgeForces(gm1);
        if (console) {
            for (IForce force : attractGM1)
                System.out.println("attractor: " + force.getOwner() + " > " + force.getSource());
        }
        assertTrue("gm1 must have attraction with gm2", attractGM1.size() == 1);
        assertTrue("gm1 attract gm2", attractGM1.contains(new FAAttraction(gm1, gm2)));
        
        // gm2
        Collection<IForce> attractGM2 = model.getAttractionEdgeForces(gm2);
        if (console) {
            for (IForce force : attractGM2)
                System.out.println("attractor: " + force.getOwner() + " > " + force.getSource());
        }
        //assertTrue("gm2 must have no repulsion with gm1", attractGM2.size() == 0);
    }
}

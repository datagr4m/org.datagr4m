package org.datagr4m.trials.drawing.demo06.flowers;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.layout.hierarchical.flower.ForceFlowerLayout;
import org.datagr4m.drawing.layout.hierarchical.flower.StaticFlowerLayout;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.flower.AbstractFlowerModel;
import org.datagr4m.drawing.model.items.hierarchical.flower.ForceFlowerModel;
import org.datagr4m.drawing.model.items.hierarchical.flower.HyperEdgeStructure;
import org.datagr4m.drawing.renderer.items.hierarchical.flower.FlowerRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;
import org.datagr4m.viewer.layered.LayeredRenderer;
import org.jzy3d.maths.Coord2d;

public class DemoFlowerCircle {
    protected static boolean FORCE_DISPLAYED = false;
    protected static double BREAK_DISTANCE = 20;
    
    public static void main(String[] args) throws InterruptedException {
        ForceFlowerModel<String> flower = buildFlower();
        ForceFlowerLayout layout = new StaticFlowerLayout(flower);
        show(flower);
    }

	public static Display show(ForceFlowerModel<String> flower) {
		final Display display = new Display(true, new MouseItemControllerFactory());

        FlowerRenderer renderer = new FlowerRenderer(display, flower);
        renderer.getRendererSettings().setLocalEdgeDisplayed(true);
        renderer.getRendererSettings().getBoundsSettings().setBoundDisplayed(null, false);
        
        ForceRenderer forceR = new ForceRenderer(flower);
        LayeredRenderer layered = new LayeredRenderer(renderer, forceR);
        layered.setVisible(forceR, FORCE_DISPLAYED);
        
        display.setView(new View(layered, display));
        display.openFrame();
        display.getView().centerAt(new Coord2d());
        display.getView().fit(flower.getRawRectangleBounds().cloneAsRectangle2D());
        display.getView().setShowCenterCross(true);
        
        
        ForceDebugger.attach((MouseItemViewController)display.getMouse(), flower, renderer);
		return display;
	}

	public static ForceFlowerModel<String> buildFlower() {
		double radius = 2000;
        int n = 10;
        int r1 = 50;
        int r2 = 50;
        int nagllomerats = 3;
        int n2 = 2; // n item par agglomerat
        int h = 2; // n hydre
        
        double min = 300;
        double max = 310;
        
        final IBoundedItem center = new DefaultBoundedItem("center", r1);
        
        // voisins r�guliers
        List<IBoundedItem> neighbours = new ArrayList<IBoundedItem>();
        double step = Math.PI*2/n;
        double angle = 0;
        for (int i = 0; i < n; i++) {
            IBoundedItem item = new DefaultBoundedItem(i+"", r2);
            item.changePosition(new Coord2d(/*angle+0.01*/Math.random()*2*Math.PI, radius).cartesian());
            neighbours.add(item);
            angle+=step;
        }
        
        // voisins agglom�r�s
        step = 2*Math.PI/nagllomerats;
        angle = 0;
        for (int i = 0; i < nagllomerats; i++) {
            for (int j = 0; j < n2; j++) {
                IBoundedItem item = new DefaultBoundedItem("add."+i+"."+j, r2);
                item.changePosition(new Coord2d(angle+0.01, radius).cartesian());
                neighbours.add(item);
            }
            angle+=step;
        }
        
        // hydres
        int hsize = 2;
        int from = 0;
        int to = from+hsize;
        List<HyperEdgeStructure> hydres = new ArrayList<HyperEdgeStructure>();
        for (int i = 0; i < h; i++) {
            IBoundedItem network = new DefaultBoundedItem("H:10.10.10."+i);
            HyperEdgeStructure hydre = new HyperEdgeStructure(network, center, neighbours.subList(from, to));
            //from = to;
            //to = from+hsize;
            hydres.add(hydre);
        }
        
        // -------------------
        // model
        ForceFlowerModel<String> flower = new ForceFlowerModel<String>(center, neighbours, hydres, min, max);
        String net = "192.168.0.";
        
        int k=0;
        for(IBoundedItem neighbour: neighbours){
            if(!AbstractFlowerModel.inStructure(hydres, neighbour))
                flower.setEdge(net+k, net+k, center, "." + k, neighbour, "." + k);
            k++;
        }
        
        for(HyperEdgeStructure hydre: hydres){
            IBoundedItem hydreNetItem = hydre.getEdge();
            flower.setEdge(net+k, null, center, "." + k, hydreNetItem, null);
            
            int hk = 0;
            for(IBoundedItem device: hydre.getNeighbours()){
                if(device!=center){
                    IBoundedItem copy = flower.getRepresentationInHydres(device, hydre);
                    if(copy==null)
                        System.err.println("null");
                    else
                        System.err.println("ok");
                    String edge = net+k+"("+hk+")";
                    flower.setEdge(edge, null, hydreNetItem, null, copy, "." + k);
                    hk++;
                    
                }
            }
            k++;
        }
        //System.exit(0);
        //flower.toConsole();
		return flower;
	}
}

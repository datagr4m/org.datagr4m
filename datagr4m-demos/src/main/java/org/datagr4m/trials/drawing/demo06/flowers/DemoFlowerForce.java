package org.datagr4m.trials.drawing.demo06.flowers;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.animation.ForceLayoutAnimation;
import org.datagr4m.drawing.layout.algorithms.forces.ForceDebugger;
import org.datagr4m.drawing.layout.hierarchical.flower.ForceFlowerLayout;
import org.datagr4m.drawing.layout.runner.impl.LayoutRunnerFactory;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.flower.ForceFlowerModel;
import org.datagr4m.drawing.renderer.items.hierarchical.flower.FlowerRenderer;
import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.drawing.viewer.mouse.items.factory.MouseItemControllerFactory;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.View;
import org.datagr4m.viewer.layered.LayeredRenderer;
import org.jzy3d.maths.Coord2d;

public class DemoFlowerForce {
    protected static boolean FORCE_DISPLAYED = false;
    protected static double BREAK_DISTANCE = 20;
    
    public static void main(String[] args) throws InterruptedException {
    	final ForceFlowerModel<String> flower = buildFlower();
        ForceFlowerLayout layout = new ForceFlowerLayout(flower);
        final Display display = show(flower);
        
        // -------------------
        // run layout
    	ForceLayoutAnimation.defaultRunnerFactory = new LayoutRunnerFactory();
        ForceLayoutAnimation a = new ForceLayoutAnimation(layout, BREAK_DISTANCE);
        Thread.sleep(1000); // TODO: push wait animation to animator instead
        display.getAnimator().push(a);
        /*display.getAnimator().push(new AbstractAnimation() {
			@Override
			public boolean next() {
				((View)display.getView()).fit(flower.getSlotableBounds());
				System.err.println("out");
				return true;
			}
		});*/
    }

	public static Display show(final ForceFlowerModel<String> flower) {
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
        //display.getView().setShowCenterCross(true);
        
        ForceDebugger.attach((MouseItemViewController)display.getMouse(), flower, renderer);
		return display;
	}

	public static ForceFlowerModel<String> buildFlower() {
		double radius = 2000;
        int n = 6;
        int r1 = 50;
        int r2 = 50;
        int n2 = 4;
        
        double min = 300;
        double max = 310;
        
        final IBoundedItem center = new DefaultBoundedItem("center", r1);
        
        List<IBoundedItem> neighbours = new ArrayList<IBoundedItem>();
        double step = Math.PI*2/n;
        double angle = 0;
        for (int i = 0; i < n; i++) {
            IBoundedItem item = new DefaultBoundedItem(i+"", r2);
            item.changePosition(new Coord2d(/*angle+0.01*/Math.random()*2*Math.PI, radius).cartesian());
            neighbours.add(item);
            angle+=step;
        }
        
        int nsteps = 3;
        step = 2*Math.PI/nsteps;
        angle = 0;
        for (int i = 0; i < nsteps; i++) {
            for (int j = 0; j < n2; j++) {
                IBoundedItem item = new DefaultBoundedItem("add."+i+"."+j, r2);
                item.changePosition(new Coord2d(angle+0.01, radius).cartesian());
                neighbours.add(item);
            }
            angle+=step;
        }
        
        // -------------------
        ForceFlowerModel<String> flower = new ForceFlowerModel<String>(center, neighbours, min, max);
        String net = "192.168.0.";
        
        int k=0;
        for(IBoundedItem neighbour: neighbours){
            flower.setEdge(net+k, net+k, center, "." + k, neighbour, "." + k);
            k++;
        }
		return flower;
	}
}

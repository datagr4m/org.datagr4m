package org.datagr4m.trials.drawing.demo02.items;

import java.awt.Color;
import java.awt.Graphics2D;

import org.datagr4m.drawing.model.items.BoundsType;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.DefaultBoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.policy.IRenderingPolicy;
import org.datagr4m.drawing.renderer.policy.RenderingPolicy;
import org.datagr4m.trials.drawing.AbstractDemo;
import org.datagr4m.trials.drawing.IDemo;
import org.datagr4m.trials.drawing.IconSet;
import org.datagr4m.viewer.View;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.jzy3d.maths.Coord2d;

public class DemoItemBounds extends AbstractDemo{
    public static void main(String[] args){
    	IDemo demo = new DemoItemBounds();
    	demo.show();        
    }
    
	@Override
	public void makeModel() {
        IBoundedItem item1 = new DefaultBoundedItem("item1");
        IBoundedItem item2 = new DefaultBoundedItem("item2");
        IBoundedItem item3 = new DefaultBoundedItemIcon("router", IconSet.ROUTER);
        IBoundedItem item4 = new DefaultBoundedItemIcon("firewall-with-longer-label", IconSet.FIREWALL);
        
        HierarchicalGraphModel graph = new HierarchicalGraphModel();
        graph.addChild(item1);
        graph.addChild(item2);
        graph.addChild(item3);
        graph.addChild(item4);
        
        item1.changePosition(-100, 0);
        item2.changePosition(100, 0);
        item3.changePosition(100, 300);
        item4.changePosition(-100, 300);
        
        model = graph;
	}
	
	@Override
	public void show(){
		makeModel();
		//makeLayout(model);
		makeDisplay(model);
		display.openFrame();
        display.getView().centerAt(Coord2d.ORIGIN);


		display.getOverlay().addRenderer(new AbstractRenderer(){
            @Override
            public void render(Graphics2D graphic){
                pushLayer0Transform(graphic);
                pullDefaultTransform(graphic);
                graphic.setColor(Color.gray);
                graphic.drawString("green: raw bounds", 10, 15);
                graphic.drawString("orange: internal bounds", 10, 30);
                graphic.drawString("red: external bounds", 10, 45);
                graphic.drawString("pink: relative bounds", 10, 60);
                pullLayer0Transform(graphic);
            }
        });
		// rendering policy
        IRenderingPolicy p = new RenderingPolicy();
        p.setup(model);
        p.getItemSettings().setNodeBoundsDisplayed(null, true); // activate node bounds display
        p.getItemSettings().setNodeBoundsTypeColor(BoundsType.RAW, Color.GREEN);
        p.getItemSettings().setNodeBoundsTypeColor(BoundsType.RELATIVE, Color.PINK);
        p.getItemSettings().setNodeBoundsTypeColor(BoundsType.INTERNAL, Color.ORANGE);
        p.getItemSettings().setNodeBoundsTypeColor(BoundsType.EXTERNAL, Color.RED);
        p.getBoundsSettings().setBoundDisplayed(null, false); // desactivate node group bounds
        p.apply(renderer);
        
        model.fit(display.getView());
	}
}

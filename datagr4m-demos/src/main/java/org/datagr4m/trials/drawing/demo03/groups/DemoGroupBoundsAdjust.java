package org.datagr4m.trials.drawing.demo03.groups;

import java.awt.Color;
import java.awt.Graphics2D;

import org.datagr4m.drawing.model.items.BoundsType;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.policy.IRenderingPolicy;
import org.datagr4m.drawing.renderer.policy.RenderingPolicy;
import org.datagr4m.trials.drawing.AbstractDemo;
import org.datagr4m.trials.drawing.IDemo;
import org.datagr4m.viewer.View;
import org.datagr4m.viewer.renderer.AbstractRenderer;

public class DemoGroupBoundsAdjust extends AbstractDemo{
    public static void main(String[] args){
    	IDemo demo = new DemoGroupBoundsAdjust();
    	demo.show();        
    }

	@Override
	public void makeModel() {
		IBoundedItem item1 = new DefaultBoundedItem("item1", 30);
        IBoundedItem item2 = new DefaultBoundedItem("item2", 30);
        IBoundedItem item3 = new DefaultBoundedItem("item3");
        IBoundedItem item4 = new DefaultBoundedItem("item4 with longer label");

        HierarchicalGraphModel graph = new HierarchicalGraphModel();
        graph.addChild(item1);
        graph.addChild(item2);
        graph.addChild(item3);
        graph.addChild(item4);
        graph.setShape(ItemShape.RECTANGLE);

        item1.changePosition(0, 0);
        item2.changePosition(100, 0);
        item3.changePosition(100, 100);
        item4.changePosition(0, 100);

        HierarchicalGraphModel root = new HierarchicalGraphModel();
        root.addChild(graph);
        
        model = root;
	}
	
	@Override
	public void show(){
		super.show();
		
		display.getOverlay().addRenderer(new AbstractRenderer(){
            @Override
            public void render(Graphics2D graphic){
                pushLayer0Transform(graphic);
                pullDefaultTransform(graphic);
                graphic.setColor(Color.gray);
                graphic.drawString("green: raw bounds", 10, 15);
                graphic.drawString("orange: internal bounds", 10, 30);
                graphic.drawString("red: external bounds (extent+margin)", 10, 45);
                graphic.drawString("blue: corridor bounds (extent+margin+corridor)", 10, 60);
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
        p.getItemSettings().setNodeBoundsTypeColor(BoundsType.CORRIDOR, Color.BLUE);
        p.getBoundsSettings().setBoundDisplayed(null, false); // desactivate node group bounds
        p.apply(renderer);
        
        model.fit(display.getView());
	}
}

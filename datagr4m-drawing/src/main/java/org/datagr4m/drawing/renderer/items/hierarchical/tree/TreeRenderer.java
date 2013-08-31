package org.datagr4m.drawing.renderer.items.hierarchical.tree;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.annotations.IClickableItemAnnotation;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.IIconHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.tree.TreeModel;
import org.datagr4m.drawing.renderer.items.hierarchical.AbstractHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.GraphRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.IGraphRendererSettings;
import org.datagr4m.drawing.renderer.policy.DefaultStyleSheet;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.annotations.items.IClickableAnnotation;


public class TreeRenderer extends AbstractHierarchicalRenderer implements IRenderer{
    public static float NODE_WIDTH = 30;
    
    public TreeRenderer(IDisplay display) {
        this(display, null);
    }
    
    public TreeRenderer(IDisplay display, TreeModel model) {
        super(display);
        setModel(model);
        itemRenderer = new TreeIconRenderer(model, display);
    }
    
    @Override
    public void render(Graphics2D graphic) {
        render(graphic, model);
    }
    
    public void render(Graphics2D graphic, TreeModel model) {
        if(model!=null){
            // show children nodes, or render leaf nodes
            for(IBoundedItem child: model.getChildren()){
                graphic.setColor(DefaultStyleSheet.TUBE_COLOR);
                drawLine(graphic, model.getPosition(), child.getPosition());

                if(child instanceof TreeModel){
                    render(graphic, (TreeModel)child);
                }
                else if(child instanceof IClickableItemAnnotation){
                    ((IClickableItemAnnotation)child).render(graphic);
                }
                else{
                    // render leafs (ip)
                    if(child instanceof IIconHierarchicalModel)
                        render(graphic, (IIconHierarchicalModel)child);
                    itemRenderer.render(graphic, child, settings.getNodeSettings());
                }
            }
            
            // show model node
            IClickableAnnotation annotation = model.getNodeRepresentations();
            if(annotation!=null)
                annotation.render(graphic);
            else{
                graphic.setColor(Color.RED);
                drawRectCentered(graphic, model.getPosition(), NODE_WIDTH, NODE_WIDTH);
            }
        }
    }
    
    public void render(Graphics2D graphic, IIconHierarchicalModel model) {
        for(IBoundedItem i: model.getChildren())
            if(i instanceof IClickableItemAnnotation){
                graphic.setColor(DefaultStyleSheet.TUBE_COLOR);
                drawLine(graphic, i.getParent().getPosition(), i.getPosition());

                ((IClickableItemAnnotation)i).render(graphic);
            }
    }

    @Override
    public List<IClickableItem> hit(int x, int y) {
        throw new RuntimeException("not implemented");
    }
    
    @Override
    public void setModel(IHierarchicalModel model){
        if(model!=null){
            this.model = (TreeModel)model; 
        }
        else
            this.model = null;
    }
    
    @Override
    public IHierarchicalModel getModel() {
        return model;
    }
    
    public IGraphRendererSettings getSettings() {
        return settings;
    }

    public void setSettings(IGraphRendererSettings settings) {
        this.settings = settings;
    }

    protected IGraphRendererSettings settings = new GraphRendererSettings();


    protected TreeModel model;
}

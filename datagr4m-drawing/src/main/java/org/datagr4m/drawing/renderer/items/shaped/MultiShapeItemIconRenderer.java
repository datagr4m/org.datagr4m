package org.datagr4m.drawing.renderer.items.shaped;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemShape;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.DifferedRenderer;


public class MultiShapeItemIconRenderer implements IItemRenderer{
    protected IItemRenderer circle;
    protected IItemRenderer rectangle;
    
    public MultiShapeItemIconRenderer(IHierarchicalNodeModel root, IDisplay display){
        circle = new CircleItemIconRenderer(root, display);
        rectangle = new RectangleItemIconRenderer(root, display);
    }
    
    /***********/
    
    @Override
    public void render(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings) {
        if(item instanceof IHierarchicalNodeModel)
            renderGroup(graphic, (IHierarchicalNodeModel)item, settings);
        else
            renderItem(graphic, item, settings);
    }
    
    protected void renderGroup(Graphics2D graphic, IHierarchicalNodeModel item, IItemRendererSettings settings){
        // show a collapsed group
        if(item.isCollapsed()){
            IBoundedItem collapsedRep = item.getCollapsedModel();
            if(collapsedRep!=null)
                rectangle.render(graphic, item.getCollapsedModel(), settings);
        }
        // show a non collapsed group
        else{
            //if(item.getD)
            //rectangle.render(graphic, item, settings);
            if(item.getShape()==ItemShape.CIRCLE)
                circle.render(graphic, item, settings);
            else if(item.getShape()==ItemShape.RECTANGLE)
                rectangle.render(graphic, item, settings);
            else
                throw new IllegalArgumentException("can't deal with such a shape: " + item.getShape());
        }
    }
    
    protected void renderItem(Graphics2D graphic, IBoundedItem item, IItemRendererSettings settings){
        if(item.getShape()==ItemShape.RECTANGLE)
            rectangle.render(graphic, item, settings);
        else if(item.getShape()==ItemShape.CIRCLE)
            circle.render(graphic, item, settings);
        else
            throw new IllegalArgumentException("can't deal with such a shape: " + item.getShape());
    }
    
    /***********/
    
    @Override
    public boolean hit(IBoundedItem item, int x, int y){
        if(item instanceof IHierarchicalNodeModel)
            return hitGroup((IHierarchicalNodeModel)item, x, y);
        else
            return hitItem(item, x, y);
    }
    
    protected boolean hitGroup(IHierarchicalNodeModel item, int x, int y){
        // show a collapsed group
        if(item.isCollapsed()){
            IBoundedItem collapsedRep = item.getCollapsedModel();
            if(collapsedRep!=null)
                return rectangle.hit(item.getCollapsedModel(), x, y);
            else
                return false;
        }
        // show a non collapsed group
        else{
            if(item.getShape()==ItemShape.CIRCLE)
                return circle.hit(item, x, y);
            else if(item.getShape()==ItemShape.RECTANGLE)
                return rectangle.hit(item, x, y);
            else
                return false;
        }
    }
    
    protected boolean hitItem(IBoundedItem item, int x, int y){
        if(item instanceof IHierarchicalNodeModel)
            return circle.hit(item, x, y);
        else
            return rectangle.hit(item, x, y);
    }

    @Override
    public void clearDiffered() {
        rectangle.clearDiffered();
        circle.clearDiffered();
    }

    @Override
    public List<DifferedRenderer> getDiffered() {
        List<DifferedRenderer> list = new ArrayList<DifferedRenderer>();
        list.addAll(rectangle.getDiffered());
        list.addAll(circle.getDiffered());
        return list;
    }
    
    @Override
    public void addDiffered(List<DifferedRenderer> differed){
        this.rectangle.addDiffered(differed);
    }
    
    @Override
    public void addDiffered(DifferedRenderer differed){
        this.rectangle.addDiffered(differed);
    }
}

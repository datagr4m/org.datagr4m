package org.datagr4m.drawing.navigation.plugin.bringandgo.simple;

import java.awt.Graphics2D;
import java.util.List;

import org.datagr4m.drawing.layout.geometrical.RectangleLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.navigation.plugin.bringandgo.BringAndGoLayerHitProcessor;
import org.datagr4m.drawing.navigation.plugin.bringandgo.IBringAndGoLayer;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.ItemRendererSettings;
import org.datagr4m.drawing.renderer.items.shaped.CircleItemIconRenderer;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.jzy3d.maths.Coord2d;


public class SimpleBringAndGoLayer extends AbstractRenderer implements IBringAndGoLayer{
    private static final long serialVersionUID = -7626982957490135258L;
        
    public SimpleBringAndGoLayer(IHierarchicalModel model, IDisplay display){
        this.model = model;
        this.display = display;

        itemRenderer = new CircleItemIconRenderer(model, display);
        itemSettings = new ItemRendererSettings();
        itemSettings.setNodeBorderDisplayed(null, true);
        
        hitProcessor = new BringAndGoLayerHitProcessor(this);
    }
    
    protected void makeLayout(){
        Coord2d center =  currentItem.getAbsolutePosition();
        //CircleLayout layout = new CircleLayout(neighbours, center, false, 200);
        RectangleLayout layout = new RectangleLayout(neighbours, center, 400, 400);
        layout.goAlgo();        
    }

    @Override
	public void render(Graphics2D graphic){
        if(neighbours!=null && neighbours.size()!=0){
            for(IBoundedItem neighbour: neighbours){
                itemRenderer.render(graphic, neighbour, itemSettings);
            }
        }
        else
            if(currentItem!=null)
                drawText(graphic, "no neighbour!", currentItem.getAbsolutePosition());
    }
    
    /******************/
    
    @Override
    public IBoundedItem getCurrentItem() {
        return currentItem;
    }

    @Override
    public void setCurrentItem(IBoundedItem currentItem) {
        this.currentItem = currentItem;
    }
    
    @Override
    public List<IBoundedItem> getNeighbours() {
        return neighbours;
    }

    @Override
    public void setNeighbours(List<IBoundedItem> neighbours) {
        this.neighbours = neighbours;
    }
    
    @Override
    public IItemRendererSettings getItemSettings() {
        return itemSettings;
    }

    @Override
    public void setItemSettings(IItemRendererSettings itemSettings) {
        this.itemSettings = itemSettings;
    }

    @Override
    public IItemRenderer getItemRenderer() {
        return itemRenderer;
    }

    /******************/
    
    protected IItemRenderer itemRenderer;
    protected IItemRendererSettings itemSettings;
    
    protected IHierarchicalModel model;
    protected IDisplay display;

    protected IBoundedItem currentItem;
    protected List<IBoundedItem> neighbours;
}

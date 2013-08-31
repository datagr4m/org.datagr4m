package org.datagr4m.drawing.navigation.plugin.bringandgo.force;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.flower.IFlowerModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.datagr4m.drawing.navigation.plugin.bringandgo.IBringAndGoLayer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.flower.FlowerRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.IGraphRendererSettings;
import org.datagr4m.viewer.IDisplay;
import org.jzy3d.maths.Coord2d;


public class ForceBringAndGoLayer extends FlowerRenderer implements IBringAndGoLayer{
    public ForceBringAndGoLayer(IDisplay display, IHierarchicalGraphModel model, IGraphRendererSettings settings) {
        super(display, model, settings);
    }

    public ForceBringAndGoLayer(IDisplay display, IHierarchicalGraphModel model) {
        super(display, model);
    }
    @Override
    public void render(Graphics2D graphic) {
        if(model!=null){
            @SuppressWarnings("unchecked")
            IFlowerModel<Object> fmodel = (IFlowerModel<Object>)model;
            Coord2d c = fmodel.getCenter().getPosition();
            drawCircle(graphic, c, (float)fmodel.getMaxDist(), null, new Color(250,250,250));
            drawCircle(graphic, c, (float)fmodel.getMinDist(), null, new Color(230,230,230));
        }
        super.render(graphic);
    }

    @Override
    public IBoundedItem getCurrentItem() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setCurrentItem(IBoundedItem currentItem) {
        //throw new RuntimeException("not implemented");
    }

    @Override
    public List<IBoundedItem> getNeighbours() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setNeighbours(List<IBoundedItem> neighbours) {
        //throw new RuntimeException("not implemented");
    }

    @Override
    public IItemRendererSettings getItemSettings() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setItemSettings(IItemRendererSettings itemSettings) {
        //throw new RuntimeException("not implemented");
    }

}

package org.datagr4m.drawing.navigation.plugin.louposcope;

import java.awt.Graphics2D;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.renderer.items.IItemRenderer;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.ItemRendererSettings;
import org.datagr4m.drawing.renderer.items.shaped.CircleItemIconRenderer;
import org.datagr4m.topology.Group;
import org.datagr4m.topology.Topology;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.AbstractRenderer;

import edu.uci.ics.jung.graph.Graph;

/**
 * Retrieve data from model, build louposcope content the first time it is required for
 * a given node
 * @author martin
 *
 * @param <V>
 * @param <E>
 * @param <C>
 */
public abstract class AbstractLouposcopeLayer<V,E,C> extends AbstractRenderer implements ILouposcopeLayer<V, E, C>{
    protected IHierarchicalModel model;
    protected List<IBoundedItem> items;
    
    protected Topology<V,E> topology;
    protected Graph<V,E> graph;
    
    protected IDisplay display;

    public AbstractLouposcopeLayer(IHierarchicalModel model, IDisplay display){
        this.model = model;
        this.items = model.getDescendants(true);
        this.display = display;
        this.itemRenderer = new CircleItemIconRenderer(model, display);
        this.itemSettings = new ItemRendererSettings();
        this.itemSettings.setNodeBorderDisplayed(null, true);
        
        extractTopology(model);
    }

    protected void extractTopology(IHierarchicalModel model) {
        if(model instanceof HierarchicalGraphModel){
            HierarchicalGraphModel graphModel = (HierarchicalGraphModel)model;
            topology = (Topology<V,E>)graphModel.getObject();
            graph = topology.getGlobalGraph();
        }
        else{
            throw new RuntimeException("model is not holding a Topology<V,E> instance. Can't process " + model.getObject().getClass().getCanonicalName());
        }
    }
    
    @Override
    public void render(Graphics2D graphic){
        for(IBoundedItem item: items){
            if(item.getState().isMouseOver()){
                Object o = item.getObject();
                renderContent(graphic, (V)o, item);
            }
        }
    }
    
    @Override
    public void renderContent(Graphics2D graphic, V o, IBoundedItem item){
        if(o instanceof Group<?>)
            return;
        ILouposcopeContent<C> content = getOrCreateContent(o);
        content.update();
        content.render(graphic);
    }
    


    @Override
    public LouposcopePlugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(LouposcopePlugin plugin) {
        this.plugin = plugin;
    }
    
    protected LouposcopePlugin plugin;
    protected IItemRenderer itemRenderer;
    protected IItemRendererSettings itemSettings;
}

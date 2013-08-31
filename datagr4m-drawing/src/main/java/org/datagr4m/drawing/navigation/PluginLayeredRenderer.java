package org.datagr4m.drawing.navigation;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.navigation.plugin.bringandgo.IBringAndGoLayer;
import org.datagr4m.drawing.navigation.plugin.bringandgo.simple.SimpleBringAndGoLayer;
import org.datagr4m.drawing.navigation.plugin.louposcope.ILouposcopeLayer;
import org.datagr4m.drawing.navigation.plugin.tooltips.SlotAnnotationRenderer;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRenderer;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.layered.LayeredRenderer;
import org.datagr4m.viewer.model.annotations.AnnotationModel;
import org.datagr4m.viewer.renderer.annotations.AnnotationRenderer;


public class PluginLayeredRenderer extends LayeredRenderer{
    //TODO: remove static factory from here > dupplicate with workspace
    public static IHierarchicalRendererFactory RENDERER_FACTORY = new HierarchicalRendererFactory();

    public PluginLayeredRenderer(IDisplay display, IHierarchicalModel model, IHierarchicalEdgeModel tubeModel, AnnotationModel amodel) {
        super(null);
        initLayers(display, model, tubeModel, amodel);
    }

    public void initLayers(IDisplay display, IHierarchicalModel model, IHierarchicalEdgeModel tubeModel, AnnotationModel amodel) {
        initLayerMain(display, model, tubeModel);
        // plugins
        initLayerLouposcope(display, model);
        initLayerBringAndGo(display, model);
        initLayerTooltip(amodel);
    }

    public void initLayerMain(IDisplay display, IHierarchicalModel model, IHierarchicalEdgeModel tubeModel) {
        // main layer
        tubeRenderer = new TubeRenderer(display, tubeModel);
        IHierarchicalRendererFactory rendererFactory = RENDERER_FACTORY;
        IHierarchicalRenderer renderer = rendererFactory.getRenderer(display, model);
        renderer.addRenderer(tubeRenderer); // <<< TUBES
        tubeRenderer.setParent(renderer);
        setMainLayer(renderer);
    }
    
    public void initLayerTooltip(AnnotationModel amodel) {
        tooltipLayer = new SlotAnnotationRenderer(amodel);
        addLayer(tooltipLayer);
    }

    public void initLayerBringAndGo(IDisplay display, IHierarchicalModel model) {
        bringAndGoLayer = new SimpleBringAndGoLayer(model, display);
        addLayer(bringAndGoLayer);
    }

    public void initLayerLouposcope(IDisplay display, IHierarchicalModel model) {
        //louposcopeLayer = new LouposcopeNetworksLayer(model, display);
        //louposcopeLayer = new LouposcopeInterfaceLayer(model, display);
        //addLayer(louposcopeLayer);
    	Logger.getLogger(PluginLayeredRenderer.class).warn("no louposcope impl");
    }

    public ILouposcopeLayer<?,?,?> getLouposcopeLayer(){
        return louposcopeLayer;
    }
    
    public IBringAndGoLayer getBringAndGoLayer(){
        return bringAndGoLayer;
    }
    
    public TubeRenderer getTubeRenderer(){
        return tubeRenderer;
    }
    
    public void setTubeRenderer(TubeRenderer tubeRenderer) {
        this.tubeRenderer = tubeRenderer;
    }

    public void setLouposcopeLayer(ILouposcopeLayer<?,?,?> louposcopeLayer) {
        this.louposcopeLayer = louposcopeLayer;
    }
    

    public AnnotationRenderer getTooltipLayer() {
        return tooltipLayer;
    }

    public void setTooltipLayer(AnnotationRenderer tooltipLayer) {
        isVisible.put(tooltipLayer, isVisible.get(this.tooltipLayer));
        isHittable.put(tooltipLayer, isVisible.get(this.tooltipLayer));
        this.secondaryLayers.remove(this.tooltipLayer);
        this.tooltipLayer = tooltipLayer;
        this.secondaryLayers.add(tooltipLayer);
    }


    public void setBringAndGoLayer(IBringAndGoLayer bringAndGoLayer) {
        isVisible.put(bringAndGoLayer, isVisible.get(this.bringAndGoLayer));
        isHittable.put(bringAndGoLayer, isVisible.get(this.bringAndGoLayer));
        this.secondaryLayers.remove(this.bringAndGoLayer);
        this.bringAndGoLayer = bringAndGoLayer;
        this.secondaryLayers.add(bringAndGoLayer);
    }

    public IHierarchicalRenderer getMainHierarchicalRenderer(){
        return (IHierarchicalRenderer)mainLayer;
    }
    
    protected transient TubeRenderer tubeRenderer;
    protected transient ILouposcopeLayer<?,?,?> louposcopeLayer;    
    protected transient IBringAndGoLayer bringAndGoLayer;
    protected transient AnnotationRenderer tooltipLayer;
}

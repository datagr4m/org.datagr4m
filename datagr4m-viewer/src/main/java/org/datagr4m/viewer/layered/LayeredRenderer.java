package org.datagr4m.viewer.layered;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.hit.IHitProcessor;


public class LayeredRenderer implements IRenderer{
    protected static boolean SECONDARY_LAYER_OPERATIONAL_INIT = true;
    public LayeredRenderer(IRenderer mainLayer) {
        this(mainLayer, new ArrayList<IRenderer>(5));
    }
    
    public LayeredRenderer(IRenderer mainLayer, IRenderer secondaryLayer) {
        this(mainLayer, AbstractRenderer.listRenderer(secondaryLayer));
    }
    
    public LayeredRenderer(IRenderer mainLayer, List<IRenderer> secondaryLayers) {
        this.mainLayer = mainLayer;
        this.secondaryLayers = secondaryLayers;
        this.isVisible = new HashMap<IRenderer,Boolean>();
        this.isHittable = new HashMap<IRenderer,Boolean>();
        if(mainLayer!=null)
            setOperational(mainLayer, true);
        setOperational(mainLayer, true);
        if(secondaryLayers!=null)
            setOperational(secondaryLayers, SECONDARY_LAYER_OPERATIONAL_INIT);
    }
    
    @Override
    public void render(Graphics2D graphic) {
        if(isVisible(mainLayer))
            mainLayer.render(graphic);
        for(IRenderer secondary: secondaryLayers)
            if(isVisible(secondary))
                secondary.render(graphic);
    }
    
    /**********/
    
    @Override
    public <T> List<IClickableItem> hitOnly(int x, int y, Class<T> type){
        List<IClickableItem> items = new ArrayList<IClickableItem>();
        List<IClickableItem> s;
        
        if(isHittable(mainLayer)){
            s = mainLayer.hitOnly(x, y, type);
            if(s!=null)
                items.addAll(s);
        }
        for(IRenderer secondary: secondaryLayers)
            if(isHittable(secondary)){
                s = secondary.hitOnly(x, y, type);
                if(s!=null)
                    items.addAll(s);
            }
        return items;//mainLayer.hitOnly(x, y, type);
    }
    
    @Override
    public <T> List<IClickableItem> hitExcluding(int x, int y, Class<T> type){
        List<IClickableItem> items = new ArrayList<IClickableItem>();
        List<IClickableItem> s;
        
        if(isHittable(mainLayer)){
            s = mainLayer.hitExcluding(x, y, type);
            if(s!=null)
                items.addAll(s);
        }
        for(IRenderer secondary: secondaryLayers)
            if(isHittable(secondary)){
                s = secondary.hitExcluding(x, y, type);
                if(s!=null)
                    items.addAll(s);
            }
        return items;//return mainLayer.hitExcluding(x, y, type);
    }
    
    @Override
    public List<IClickableItem> hit(int x, int y) {
        List<IClickableItem> items = new ArrayList<IClickableItem>();
        List<IClickableItem> s;
        
        if(isHittable(mainLayer)){
            s = mainLayer.hit(x, y);
            if(s!=null)
                items.addAll(s);
        }
        for(IRenderer secondary: secondaryLayers)
            if(isHittable(secondary)){
                s = secondary.hit(x, y);
                if(s!=null)
                    items.addAll(s);
            }
        return items;//return mainLayer.hit(x, y);
    }

    @Override
    public IHitProcessor getHitProcessor() {
        return mainLayer.getHitProcessor();
    }

    @Override
    public void setHitProcessor(IHitProcessor hitProcessor) {
        this.mainLayer.setHitProcessor(hitProcessor);
    }

    /**********/

    public boolean isVisible(IRenderer renderer){
        return isVisible.get(renderer);
    }
    
    public boolean isHittable(IRenderer renderer){
        return isHittable.get(renderer);
    }
    
    public void setVisible(IRenderer renderer, boolean status){
        isVisible.put(renderer, status);
    }
    
    public void setHittable(IRenderer renderer, boolean status){
        isHittable.put(renderer, status);
    }
    
    /**********/

    public void setOperational(IRenderer renderer, boolean status){
        setVisible(renderer, status);
        setHittable(renderer, status);
    }
    
    public void setOperational(List<IRenderer> renderers, boolean status){
        for(IRenderer renderer: renderers)
            setOperational(renderer, status);
    }
    
    public void setAllOperational(boolean status){
        setOperational(mainLayer, status);
        for(IRenderer renderer: secondaryLayers)
            setOperational(renderer, status);
    }
    
    /**********/
    
    public IRenderer getMainLayer() {
        return mainLayer;
    }
    public void setMainLayer(IRenderer mainLayer) {
        this.mainLayer = mainLayer;
        setOperational(mainLayer, true);
    }
    public List<IRenderer> getSecondaryLayers() {
        return secondaryLayers;
    }
    public void setSecondaryLayers(List<IRenderer> secondaryLayers) {
        this.secondaryLayers = secondaryLayers;
    }
    public void addLayer(IRenderer layer){
        secondaryLayers.add(layer);
        setOperational(layer, SECONDARY_LAYER_OPERATIONAL_INIT);
    }
    
    protected IRenderer mainLayer;
    protected List<IRenderer> secondaryLayers;
    
    protected Map<IRenderer,Boolean> isVisible;
    protected Map<IRenderer,Boolean> isHittable;
}

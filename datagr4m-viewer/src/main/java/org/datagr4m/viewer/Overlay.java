package org.datagr4m.viewer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.viewer.renderer.IRenderer;


public class Overlay {
    public void render(Graphics2D graphics){
        for(IRenderer renderer: renderers)
            renderer.render(graphics);
    }
    
    public void addRenderer(IRenderer renderer){
        renderers.add(renderer);
    }
    
    public void removeRenderer(IRenderer renderer){
        renderers.remove(renderer);
    }
    
    public void clearRenderers(){
        renderers.clear();
    }
    
    List<IRenderer> renderers = new ArrayList<IRenderer>();
}

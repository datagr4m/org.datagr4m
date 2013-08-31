package org.datagr4m.drawing.renderer.items.hierarchical.pair;

import org.datagr4m.drawing.renderer.bounds.BoundsRendererSettings;
import org.datagr4m.drawing.renderer.bounds.IBoundsRendererSettings;
import org.datagr4m.drawing.renderer.items.IItemRendererSettings;
import org.datagr4m.drawing.renderer.items.ItemRendererSettings;



public class PairRendererSettings implements IPairRendererSettings {
    public PairRendererSettings(){
        nodeSettings = new ItemRendererSettings();
        boundsSettings = new BoundsRendererSettings();
    }
    
    @Override
    public IItemRendererSettings getNodeSettings() {
        return nodeSettings;
    }
    @Override
    public void setNodeSettings(IItemRendererSettings nodeSettings) {
        this.nodeSettings = nodeSettings;
    }
    @Override
    public IBoundsRendererSettings getBoundsSettings() {
        return boundsSettings;
    }
    @Override
    public void setBoundsSettings(IBoundsRendererSettings boundsSettings) {
        this.boundsSettings = boundsSettings;
    }
    
    protected IItemRendererSettings nodeSettings;
    protected IBoundsRendererSettings boundsSettings;
}

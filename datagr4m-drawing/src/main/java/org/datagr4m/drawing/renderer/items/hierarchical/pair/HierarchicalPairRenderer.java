package org.datagr4m.drawing.renderer.items.hierarchical.pair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.IHierarchicalPairModel;
import org.datagr4m.drawing.renderer.bounds.BoundsRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.AbstractHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.shaped.MultiShapeItemIconRenderer;
import org.datagr4m.viewer.IDisplay;
import org.jzy3d.maths.Coord2d;


public class HierarchicalPairRenderer extends AbstractHierarchicalRenderer implements IHierarchicalPairRenderer{
    public HierarchicalPairRenderer(IDisplay display, IHierarchicalPairModel model){
        this(display, model, new PairRendererSettings());
    }
    
    public HierarchicalPairRenderer(IDisplay display, IHierarchicalPairModel model, IPairRendererSettings settings){
        super(display);
        setModel(model);
        this.settings = settings;
    }
    
    @Override
    public void render(Graphics2D graphic) {
        // Rendering bounds
        if(settings.getBoundsSettings().isBoundDisplayed(model))
            boundsRenderer.render(graphic, model.getAbsoluteRectangleBounds(), model.getLabel());
        
        // Rendering left part
        //if(model.getFirst() instanceof IBoundedItemIcon)
        //    nodeIconRenderer.render(graphic, model.getFirst(), settings.getNodeSettings());
        //else
        //settings.getNodeSettings().setNodeBorderDisplayed(null, true);
        if(model.getFirst()!=null)
            itemRenderer.render(graphic, model.getFirst(), settings.getNodeSettings());
    
        // Rendering right part
        //if(model.getSecond() instanceof IBoundedItemIcon)
        //    nodeIconRenderer.render(graphic, model.getSecond(), settings.getNodeSettings());
        //else
        if(model.getSecond()!=null)
            itemRenderer.render(graphic, model.getSecond(), settings.getNodeSettings());
            
        // render the pair
        if(model.getCurve()!=null){
            graphic.setColor(Color.BLACK);
            graphic.draw(model.getCurve());
            
            int nStandby = model.getChildren().size();
            
            if(nStandby>0){
                Rectangle2D splineBounds = model.getCurve().getBounds2D();
                Coord2d first = new Coord2d(splineBounds.getCenterX(), splineBounds.getMinY());
                
                drawTextCell(graphic, nStandby + " standby", first);
                
                /*for(Standby sb: model.getStandby()){
                    drawTextCell(graphic, sb.getIp().toString(), first);
                    first.addSelfY(-TextUtils.textHeight());
                }*/
            }
        }
        // handle children renderer if any, so that they
        // appear ON TOP of their parents
        super.render(graphic); 
    }
    
    /**
     * Set the model and build all renderer that require a reference to this model.
     */
    @Override
    public void setModel(IHierarchicalModel model) {
        this.model = (IHierarchicalPairModel)model;
        this.boundsRenderer = new BoundsRenderer();
        this.itemRenderer = new MultiShapeItemIconRenderer(model.getRoot(), display);
    }

    @Override
    public IHierarchicalModel getModel() {
        return model;
    }
    
    @Override
    public IPairRendererSettings getRendererSettings(){
        return settings;
    }
    
    @Override
    public void getRendererSettings(IPairRendererSettings settings){
        this.settings = settings;
    }
    
    protected IHierarchicalPairModel model;
    protected IPairRendererSettings settings;
}

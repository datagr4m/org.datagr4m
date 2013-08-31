package org.datagr4m.drawing.renderer.items.hierarchical.visitor;

import java.awt.Color;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.renderer.bounds.IBoundsRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.IHierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.IEdgeRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.pair.IHierarchicalPairRenderer;
import org.datagr4m.drawing.renderer.policy.DefaultStyleSheet;


public class BulkRendererSettings {
    public static void user(IHierarchicalRenderer renderer){
        RendererSettingVisitor rs = new RendererSettingVisitor(){
            @Override
            public void editGraphRenderer(HierarchicalGraphRenderer renderer) {
                hideModelNodes(renderer);
                hideNodesCenter(renderer);
                
                renderer.getRendererSettings().setBoundsSettings(boundsRendererSettings(false));
                renderer.getRendererSettings().setEdgeSettings(edgeRendererSettings(Color.GRAY, true));
            }
            @Override
            public void editPairRenderer(IHierarchicalPairRenderer renderer) {
                hideModelNodes(renderer);
                hideNodesCenter(renderer);
                renderer.getRendererSettings().setBoundsSettings(boundsRendererSettings(false));
            }
        };
        rs.visit(renderer);
    }
    
    public static void develop(IHierarchicalRenderer renderer, final boolean showEmbeddedEdge){
        RendererSettingVisitor rs = new RendererSettingVisitor(){
            @Override
            public void editGraphRenderer(HierarchicalGraphRenderer renderer) {
                hideNodesCenter(renderer);
                renderer.getRendererSettings().setBoundsSettings(boundsRendererSettings(false));
                renderer.getRendererSettings().setEdgeSettings(edgeRendererSettings(Color.GRAY, true));
                renderer.getRendererSettings().setLocalEdgeDisplayed(showEmbeddedEdge);
            }
            @Override
            public void editPairRenderer(IHierarchicalPairRenderer renderer) {
                hideNodesCenter(renderer);
                renderer.getRendererSettings().setBoundsSettings(boundsRendererSettings(false));
            }
        };
        rs.visit(renderer);
    }
    
    public static void debug(IHierarchicalRenderer renderer, final boolean doHideNodes){
        RendererSettingVisitor rs = new RendererSettingVisitor(){
            @Override
            public void editGraphRenderer(HierarchicalGraphRenderer renderer) {
                if(doHideNodes)
                    hideModelNodes(renderer);
                if(!doHideNodes)
                    showNodesCenter(renderer);
                renderer.getRendererSettings().getNodeSettings().setNodeBorderDisplayed(null, true);
                renderer.getRendererSettings().setBoundsSettings(boundsRendererSettings(true));
                renderer.getRendererSettings().setEdgeSettings(edgeRendererSettings(Color.GRAY, true));
            }
            @Override
            public void editPairRenderer(IHierarchicalPairRenderer renderer) {
                if(doHideNodes)
                    hideModelNodes(renderer);
                if(!doHideNodes)
                    showNodesCenter(renderer);                
                renderer.getRendererSettings().getNodeSettings().setNodeBorderDisplayed(null, true);
                renderer.getRendererSettings().setBoundsSettings(boundsRendererSettings(true));
            }
        };
        rs.visit(renderer);
    }
    
    public static void showDeviceBorder(IHierarchicalRenderer renderer){
        RendererSettingVisitor rs = new RendererSettingVisitor(){
            @Override
            public void editGraphRenderer(HierarchicalGraphRenderer renderer) {
                renderer.getRendererSettings().getNodeSettings().setNodeBorderDisplayed(null, true);
            }
            @Override
            public void editPairRenderer(IHierarchicalPairRenderer renderer) {
                renderer.getRendererSettings().getNodeSettings().setNodeBorderDisplayed(null, true);
            }
        };
        rs.visit(renderer);
    }
    
    /******* PRIMITIVES *******/
    
    public static void hideModelNodes(IHierarchicalRenderer renderer){
        if(renderer instanceof IHierarchicalGraphRenderer){
            IHierarchicalGraphRenderer gr = (IHierarchicalGraphRenderer)renderer;
            for(IBoundedItem item: renderer.getModel().getChildren())
                gr.getRendererSettings().getNodeSettings().setNodeDisplayed(item, false);
        }
        else if(renderer instanceof IHierarchicalPairRenderer){
            IHierarchicalPairRenderer pr = (IHierarchicalPairRenderer)renderer;
            for(IBoundedItem item: renderer.getModel().getChildren())
                pr.getRendererSettings().getNodeSettings().setNodeDisplayed(item, true);
        }
        /*for(IBoundedItem item: renderer.getModel().getChildren()){
            if(item instanceof IHierarchicalModel)
                renderer.getRendererSettings().getItemSettings().setNodeDisplayed(item, false);
            else
                renderer.getRendererSettings().getItemRendererSettings().setNodeDisplayed(item, true);
        }*/
    }
    
    public static void hideNodesCenter(IHierarchicalRenderer renderer){
        setNodesCenterDisplay(renderer, false);
    }
    
    public static void showNodesCenter(IHierarchicalRenderer renderer){
        setNodesCenterDisplay(renderer, true);
    }
    
    public static void hideNodesBounds(IHierarchicalRenderer renderer){
        setNodesBoundsDisplay(renderer, false);
    }

    public static void showNodesBounds(IHierarchicalRenderer renderer){
        setNodesBoundsDisplay(renderer, true);
    }

    
    public static void setNodesCenterDisplay(IHierarchicalRenderer renderer, boolean isDisplayed){
        if(renderer instanceof IHierarchicalGraphRenderer){
            IHierarchicalGraphRenderer gr = (IHierarchicalGraphRenderer)renderer;
            for(IBoundedItem item: renderer.getModel().getChildren())
                gr.getRendererSettings().getNodeSettings().setNodeCenterDisplayed(item, false);
        }
        else if(renderer instanceof IHierarchicalPairRenderer){
            IHierarchicalPairRenderer pr = (IHierarchicalPairRenderer)renderer;
            for(IBoundedItem item: renderer.getModel().getChildren())
                pr.getRendererSettings().getNodeSettings().setNodeCenterDisplayed(item, true);
        }
    }
    
    public static void setNodesBoundsDisplay(IHierarchicalRenderer renderer, boolean isDisplayed){
        if(renderer instanceof IHierarchicalGraphRenderer){
            IHierarchicalGraphRenderer gr = (IHierarchicalGraphRenderer)renderer;
            gr.getRendererSettings().getBoundsSettings().setBoundDisplayed(null, true);
                
        }
        else if(renderer instanceof IHierarchicalPairRenderer){
            IHierarchicalPairRenderer pr = (IHierarchicalPairRenderer)renderer;
            pr.getRendererSettings().getBoundsSettings().setBoundDisplayed(null, true);
        }
    }
    
    public static void showModelNodes(IHierarchicalRenderer renderer){
        setModelNodesDisplay(renderer, true);
    }
    
    public static void setModelNodesDisplay(IHierarchicalRenderer renderer, boolean value){
        if(renderer instanceof IHierarchicalGraphRenderer){
            IHierarchicalGraphRenderer gr = (IHierarchicalGraphRenderer)renderer;
            for(IBoundedItem item: renderer.getModel().getChildren())
                gr.getRendererSettings().getNodeSettings().setNodeDisplayed(item, value);
        }
        else if(renderer instanceof IHierarchicalPairRenderer){
            IHierarchicalPairRenderer pr = (IHierarchicalPairRenderer)renderer;
            for(IBoundedItem item: renderer.getModel().getChildren())
                pr.getRendererSettings().getNodeSettings().setNodeDisplayed(item, value);
        }
    }
    
    public static void colorGroupNodesWithGroupType(IHierarchicalRenderer renderer){
        for(IBoundedItem item: renderer.getModel().getChildren()){
            Object data = item.getObject();
            IHierarchicalGraphRenderer gr = (IHierarchicalGraphRenderer)renderer;
            
            gr.getRendererSettings().getNodeSettings().setNodeBodyColor(item, DefaultStyleSheet.getColor(data));

            
            /*if(data instanceof ManGroup<?>)
                gr.getRendererSettings().getNodeSettings().setNodeBodyColor(item, TopologyStyleSheet.MAN_COLOR);
            else if(data instanceof SecurityGroup<?>)
                gr.getRendererSettings().getNodeSettings().setNodeBodyColor(item, TopologyStyleSheet.SAN_COLOR);
            else if(data instanceof LanGroup<?>)
                gr.getRendererSettings().getNodeSettings().setNodeBodyColor(item, TopologyStyleSheet.LAN_COLOR);
            else if(data instanceof LanGroup<?>)
                gr.getRendererSettings().getNodeSettings().setNodeBodyColor(item, TopologyStyleSheet.LAN_COLOR);*/
        }
    }
    
    public static void colorDeviceNodes(IHierarchicalRenderer renderer, Color color){
        for(IHierarchicalRenderer rr: renderer.getChildren()){
            for(IBoundedItem item: rr.getModel().getChildren()){
                if(!(item instanceof IHierarchicalModel)){
                    if(rr instanceof IHierarchicalGraphRenderer)
                        ((IHierarchicalGraphRenderer)rr).getRendererSettings().getNodeSettings().setNodeBodyColor(item, color);
                    else if(rr instanceof IHierarchicalPairRenderer)
                        ((IHierarchicalPairRenderer)rr).getRendererSettings().getNodeSettings().setNodeBodyColor(item, color);
                }
                else{
                    colorDeviceNodes(rr, color);
                }
            }
        }
    }
    
    /**************/
    
    protected static IEdgeRendererSettings edgeRendererSettings(final Color color, final boolean isStraight){
        return new IEdgeRendererSettings() {
            @Override
            public boolean isEdgeLabelDisplayed(Pair<IBoundedItem, IBoundedItem> edge) {
                return true;
            }
            @Override
            public boolean isEdgeDisplayed(Pair<IBoundedItem, IBoundedItem> edge) {
                return true;
            }
            @Override
            public Color getEdgeLabelColor(Pair<IBoundedItem, IBoundedItem> edge) {
                return color;
            }
            @Override
            public Color getEdgeColor(Pair<IBoundedItem, IBoundedItem> edge) {
                return color;
            }
            @Override
            public boolean isEdgeStraight(Pair<IBoundedItem, IBoundedItem> edge) {
                return isStraight;
            }
            @Override
            public int getEdgeLabelSize(Pair<IBoundedItem, IBoundedItem> edge) {
                throw new RuntimeException("not implemented");
            }
        };
    }
    
    protected static IBoundsRendererSettings boundsRendererSettings(final boolean visible){
        return new IBoundsRendererSettings() {
            @Override
            public void setBoundDisplayed(IBoundedItem model, boolean displayed) {
            }
            @Override
            public boolean isBoundDisplayed(IBoundedItem model) {
                return visible;
            }
        };
    }
}

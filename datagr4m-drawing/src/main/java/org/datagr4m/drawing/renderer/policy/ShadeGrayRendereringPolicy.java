package org.datagr4m.drawing.renderer.policy;

import java.awt.Color;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.MonochromeTubeRendererSettings;
import org.datagr4m.topology.Group;


public class ShadeGrayRendereringPolicy extends RenderingPolicy implements IRenderingPolicy {
    public ShadeGrayRendereringPolicy(){
        super();
    }
    
    Color gray = new Color(250, 250, 250);
    
    @Override
    public void setup(IHierarchicalModel model){
        monocolorAllItems(model, gray);
        tubeSettings = new MonochromeTubeRendererSettings(gray);
        //colorGroupNodesWithGroupType(model);
        showNodeBorder();
        //colorDeviceNodes(model, java.awt.Color.WHITE);
        //oneEdgeColorPerIp(model);
        showDebugBounds(false);
        isLocalEdgeDisplayed = false;
    }

	@Override
	protected void setupEdgeColorPolicy(IHierarchicalModel model) {
	}

	@Override
	protected void setupNodeColorPolicy(IHierarchicalModel model, Color color) {
	}
}

package org.datagr4m.drawing.renderer.policy;

import java.awt.Color;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.renderer.bounds.BoundsRendererSettings;
import org.datagr4m.drawing.renderer.items.ItemRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.IHierarchicalRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.GraphRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.HierarchicalGraphRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.local.LocalEdgeRendererSettings;
import org.datagr4m.drawing.renderer.items.hierarchical.pair.IHierarchicalPairRenderer;
import org.datagr4m.drawing.renderer.items.hierarchical.visitor.RendererSettingVisitor;
import org.datagr4m.viewer.renderer.IRenderer;


public class RenderingPolicy implements IRenderingPolicy {
    public static boolean EDGE_COLOR = true;
    
    public RenderingPolicy(){
        this(false);
    }
    
    public RenderingPolicy(boolean debugBoundsShown){
        init(debugBoundsShown);
    }
    
    public void init(boolean debugBoundsShown) {
        itemSettings = new ItemRendererSettings();
        edgeSettings = new LocalEdgeRendererSettings();
        tubeSettings = new TubeRendererSettings();
        boundsSettings = new BoundsRendererSettings();
        debug = debugBoundsShown;
    }
    
    @Override
    public void setup(IHierarchicalModel model){
        showNodeBorder();
        setupNodeColorPolicy(model, java.awt.Color.WHITE);
        if(EDGE_COLOR)
            setupEdgeColorPolicy(model);
        else
            grayTube(model.getEdgeModel());
        
        showDebugBounds(debug);
        showNodeCenter(debug);
        isLocalEdgeDisplayed = LOCAL_EDGE_DISPLAY_DO_NOT_EDIT;
    }
    
    protected void setupEdgeColorPolicy(IHierarchicalModel model) {
		// tubeSettings = new
		// OneColorPerIpTubeRendererSettings(model.getEdgeModel());
	}

	protected void setupNodeColorPolicy(IHierarchicalModel model, Color color) {
		for (IBoundedItem item : model.getChildren()) {
			if (!(item instanceof IHierarchicalModel)) {
				itemSettings.setNodeBodyColor(item, color);
			} else {
				IBoundedItem delegate = model.getCollapsedModel();
				if (delegate != null) {
					itemSettings.setNodeBodyColor(item, color);
				}

				setupNodeColorPolicy((IHierarchicalModel) item, color);
			}
		}
	}

    protected void monocolorAllItems(IHierarchicalModel model, Color color){
        for(IBoundedItem item: model.getChildren()){
            itemSettings.setNodeBodyColor(item, Color.WHITE);
            itemSettings.setNodeBorderColor(item, color);
            itemSettings.setNodeLabelColor(item, color);
            itemSettings.setNodeIconFilterColor(item, color);
            if(item instanceof IHierarchicalModel){
                IHierarchicalModel group = (IHierarchicalModel)item;
                if(group.canCollapse())
                    itemSettings.setNodeIconFilterColor(group.getCollapsedModel(), color);
                monocolorAllItems(group, color);
            }
        }
    }
    protected void showNodeBorder(){
        itemSettings.setNodeBorderDisplayed(null, true);
    }

    protected void grayTube(IHierarchicalEdgeModel model){
        tubeSettings = new TubeRendererSettings();
        
        if(model!=null){
            for(Tube tube: model.getRootTubes()){
                tubeSettings.setTubeSourceOpened(tube, true);
                tubeSettings.setTubeTargetOpened(tube, true);
            }
        }
            
    }
    
    protected void showDebugBounds(boolean visible){
        boundsSettings.setBoundDisplayed(null, visible);
        itemSettings.setNodeBoundsDisplayed(null, visible);
    }
    
    protected void showNodeCenter(boolean visible){
        itemSettings.setNodeCenterDisplayed(null, visible);
    }
    
    /***********/
    
    @Override
    public void apply(IHierarchicalRenderer root){
        RendererSettingVisitor rs = new RendererSettingVisitor(){
            @Override
            public void editGraphRenderer(HierarchicalGraphRenderer renderer) {
                renderer.getRendererSettings().setNodeSettings(itemSettings);
                renderer.getRendererSettings().setEdgeSettings(edgeSettings);
                renderer.getRendererSettings().setBoundsSettings(boundsSettings);
                renderer.getRendererSettings().setLocalEdgeDisplayed(isLocalEdgeDisplayed);
            }
            @Override
            public void editPairRenderer(IHierarchicalPairRenderer renderer) {
                renderer.getRendererSettings().setNodeSettings(itemSettings);
                renderer.getRendererSettings().setBoundsSettings(boundsSettings);
            }
        };
        rs.visit(root);
        
        
        for (int i = 0; i < root.getPostRenderers().size(); i++) {
            IRenderer ir = root.getPostRenderers().get(i);
            if(ir instanceof TubeRenderer){
                TubeRenderer r = (TubeRenderer)ir;
                r.setEdgeRendererSettings(tubeSettings);
                r.getTreeRenderer().getSettings().setNodeSettings(itemSettings);
            }
        }
    }
    
    /***************/
    
    @Override
	public ItemRendererSettings getItemSettings() {
        return itemSettings;
    }

    @Override
	public LocalEdgeRendererSettings getEdgeSettings() {
        return edgeSettings;
    }

    @Override
	public TubeRendererSettings getTubeSettings() {
        return tubeSettings;
    }

    @Override
	public BoundsRendererSettings getBoundsSettings() {
        return boundsSettings;
    }
    
    @Override
	public GraphRendererSettings getGraphSettings() {
        return graphSettings;
    }



    /***************/

    protected ItemRendererSettings itemSettings;
    protected LocalEdgeRendererSettings edgeSettings;
    protected TubeRendererSettings tubeSettings;
    protected BoundsRendererSettings boundsSettings;
    protected GraphRendererSettings graphSettings;
    protected IHierarchicalRenderer root;
    
    protected boolean isLocalEdgeDisplayed = true;
    
    protected boolean debug;
    
    protected static boolean LOCAL_EDGE_DISPLAY_DO_NOT_EDIT = true;
    
}

package org.datagr4m.drawing.navigation;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.visitor.AbstractItemVisitor;
import org.datagr4m.drawing.navigation.context.ContextType;
import org.datagr4m.drawing.navigation.context.NavigationContext;
import org.datagr4m.drawing.navigation.plugin.bringandgo.IBringAndGoPlugin;
import org.datagr4m.drawing.navigation.plugin.edgetables.EdgeLabelsAndTablesPlugin;
import org.datagr4m.drawing.navigation.plugin.louposcope.LouposcopePlugin;
import org.datagr4m.drawing.navigation.plugin.tooltips.TooltipPlugin;
import org.datagr4m.drawing.renderer.policy.IRenderingPolicy;
import org.datagr4m.drawing.renderer.policy.RenderingPolicy;
import org.datagr4m.drawing.viewer.mouse.edges.ClickedEdge;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.IAnimation;
import org.datagr4m.viewer.animation.IAnimationMonitor;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.layered.IPopupLayer;
import org.datagr4m.viewer.mouse.ILocalizedMouse;


public class NavigationController implements INavigationController {
    IDisplay display;
    IPopupLayer layeredDisplay;
    
    IRenderingPolicy defaultPolicy;
    //ShadeGrayRendereringPolicy grayPolicy;
    
    int DURATION = 1000;
    
    protected IBringAndGoPlugin bringAndGoPlugin;
    protected LouposcopePlugin louposcopePlugin;
    protected TooltipPlugin tooltipsPlugin;
    protected EdgeLabelsAndTablesPlugin edgeTablesPlugin;
    
    public NavigationController(IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalModel model, IPopupLayer layeredDisplay, IRenderingPolicy policy) {
        this.display = display;
        this.pluginRenderer = layered;
        this.animator = animator;
        this.mouse = mouse;
        this.model = model;
        this.layeredDisplay = layeredDisplay;
        
        initRenderingPolicy(model, policy);
        initPlugins(display, layered, animator, mouse, model, layeredDisplay);
    }

    public void initRenderingPolicy(IHierarchicalModel model, IRenderingPolicy policy) {
        // rendering policies init
        defaultPolicy = policy;
        if(defaultPolicy==null)
            defaultPolicy = new RenderingPolicy();
        defaultPolicy.setup(model);
    }

    public void initPlugins(IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalModel model, IPopupLayer layeredDisplay) {
        // navigation plugins
        edgeTablesPlugin = new EdgeLabelsAndTablesPlugin(this, display, layered, animator, mouse, model, layeredDisplay);
        tooltipsPlugin = new TooltipPlugin(this, display, layered, animator, mouse, model);
        louposcopePlugin = new LouposcopePlugin(this, display, layered, animator, mouse, model);
        //bringAndGoPlugin = new StaticFlowerBringAndGoPlugin(workspace, this, display, layered, animator, mouse, model);
        //bringAndGoPlugin = new ForceFlowerBringAndGoPlugin(workspace, this, display, layered, animator, mouse, model);
        //bringAndGoPlugin = new ForceBringAndGoPlugin(workspace, this, display, layered, animator, mouse, model);
        
        if(pluginRenderer.getLouposcopeLayer()!=null)
            pluginRenderer.getLouposcopeLayer().setPlugin(louposcopePlugin);
        else
            Logger.getLogger(NavigationController.class).error("no rendering layer avaiable for louposcope plugin");
        apply(new NavigationContext());// default context
        tooltipsPlugin.setup();
    }

    @Override
    public void apply(final NavigationContext context) {
        // default navigation context
        if (context.is(ContextType.DEFAULT)) {
            setupDefaultRenderingPolicy();
        } 
        // query our dear louposcope
        else if (context.is(ContextType.LOUPOSCOPE)) {
          context.getBoundedItem().setState(ItemState.STATE_MOUSE_OVER);
            louposcopePlugin.setup();
        } 
        // query a bring
        else if (context.is(ContextType.BRING)) {
            bringAndGoPlugin.bring(context.getBoundedItem());
        } 
        // query a go
        else if (context.is(ContextType.GO)) {
            bringAndGoPlugin.go(context.getBoundedItem(), new IAnimationMonitor() {
                @Override
                public void finished(IAnimation animation) {
                    NavigationController.this.apply(new NavigationContext());
                    context.getBoundedItem().setState(ItemState.NONE);
                }
            });
        } 
        // select an edge or tube to query details
        else if (context.is(ContextType.EDGE_DETAILS)){
            edgeTablesPlugin.click((ClickedEdge)context.getClickableItem(), context.getScreen());
        }
        // no other possiblity
        else
            throw new RuntimeException("context can't be applied: " + context.getType());
        
        // store context
        this.context = context;
    }

    @Override
    public void setupDefaultRenderingPolicy() {
        defaultPolicy.apply(pluginRenderer.getMainHierarchicalRenderer());
        pluginRenderer.setOperational(pluginRenderer.getMainHierarchicalRenderer(), true);
        pluginRenderer.setOperational(pluginRenderer.getLouposcopeLayer(), false);
        pluginRenderer.setOperational(pluginRenderer.getBringAndGoLayer(), false);
        pluginRenderer.setOperational(pluginRenderer.getTooltipLayer(), true);
        display.refresh();
    }

    @Override
    public IRenderingPolicy getDefaultPolicy(){
        return defaultPolicy;
    }
    
    /*************/

    @Override
    public NavigationContext getContext() {
        return context;
    }

    @Override
    public boolean isContext(NavigationContext ctx) {
        if (context == null)
            return ctx == null;
        else
            return context.equals(ctx);
    }

    /******************/

    @Override
    public void unselectAll(){
        AbstractItemVisitor v = new AbstractItemVisitor(){
            @Override
            public void doVisitElement(IHierarchicalModel parent, IBoundedItem element, int depth) {
                if(element!=null)
                    element.setState(ItemState.NONE);
            }
        };
        v.visit(model);
        
        if(model.getEdgeModel()!=null)
            model.getEdgeModel().unselectAll();
    }
    
    /******************/

    @Override
    public IBringAndGoPlugin getBringAndGoPlugin() {
        return bringAndGoPlugin;
    }

    @Override
    public void setBringAndGoPlugin(IBringAndGoPlugin bringAndGoPlugin) {
        this.bringAndGoPlugin = bringAndGoPlugin;
    }

    @Override
    public LouposcopePlugin getLouposcopePlugin() {
        return louposcopePlugin;
    }

    @Override
    public void setLouposcopePlugin(LouposcopePlugin louposcopePlugin) {
        this.louposcopePlugin = louposcopePlugin;
    }

    @Override
    public TooltipPlugin getTooltipsPlugin() {
        return tooltipsPlugin;
    }

    @Override
    public void setTooltipsPlugin(TooltipPlugin tooltipsPlugin) {
        this.tooltipsPlugin = tooltipsPlugin;
    }
    
    @Override
    public IDisplay getDisplay() {
        return display;
    }

    @Override
    public void setDisplay(IDisplay display) {
        this.display = display;
    }

    
    @Override
    public EdgeLabelsAndTablesPlugin getEdgeTablesPlugin() {
        return edgeTablesPlugin;
    }

    @Override
    public void setEdgeTablesPlugin(EdgeLabelsAndTablesPlugin edgeTablesPlugin) {
        this.edgeTablesPlugin = edgeTablesPlugin;
    }

    @Override
    public PluginLayeredRenderer getPluginRenderer() {
        return pluginRenderer;
    }

    @Override
    public void setPluginRenderer(PluginLayeredRenderer pluginRenderer) {
        this.pluginRenderer = pluginRenderer;
    }

    @Override
    public IAnimationStack getAnimator() {
        return animator;
    }

    @Override
    public void setAnimator(IAnimationStack animator) {
        this.animator = animator;
    }

    @Override
    public ILocalizedMouse getMouse() {
        return mouse;
    }

    @Override
    public void setMouse(ILocalizedMouse mouse) {
        this.mouse = mouse;
    }

    @Override
    public IHierarchicalModel getModel() {
        return model;
    }

    @Override
    public void setModel(IHierarchicalModel model) {
        this.model = model;
    }

    /*@Override
    public IWorkspace getWorkspace() {
        return workspace;
    }

    @Override
    public void setWorkspace(IWorkspace workspace) {
        this.workspace = workspace;
    }

    @Override
    public void setDataController(IWorkspaceController dataController) {
        this.dataController = dataController;
    }*/

    @Override
    public void setContext(NavigationContext context) {
        this.context = context;
    }



    /**************/

    protected PluginLayeredRenderer pluginRenderer;

    protected IAnimationStack animator;
    protected ILocalizedMouse mouse;

    protected IHierarchicalModel model;
    //protected Topology<DeviceKey, NetworkEdge> topology;
    //protected Graph<DeviceKey, NetworkEdge> graph;

    protected NavigationContext context;
    
    
    
}

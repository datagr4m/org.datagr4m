package org.datagr4m.drawing.navigation;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.navigation.context.NavigationContext;
import org.datagr4m.drawing.navigation.plugin.bringandgo.IBringAndGoPlugin;
import org.datagr4m.drawing.navigation.plugin.edgetables.EdgeInfoTablePlugin;
import org.datagr4m.drawing.navigation.plugin.louposcope.LouposcopePlugin;
import org.datagr4m.drawing.navigation.plugin.tooltips.TooltipPlugin;
import org.datagr4m.drawing.renderer.policy.IRenderingPolicy;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.mouse.ILocalizedMouse;


public interface INavigationController {

    public void apply(final NavigationContext context);

    public void setupDefaultRenderingPolicy();

    public IRenderingPolicy getDefaultPolicy();

    /*************/

    public NavigationContext getContext();

    public boolean isContext(NavigationContext ctx);

    /******************/

    public void unselectAll();

    /******************/

    public IBringAndGoPlugin getBringAndGoPlugin();

    public void setBringAndGoPlugin(IBringAndGoPlugin bringAndGoPlugin);

    public LouposcopePlugin getLouposcopePlugin();

    public void setLouposcopePlugin(LouposcopePlugin louposcopePlugin);

    public TooltipPlugin getTooltipsPlugin();

    public void setTooltipsPlugin(TooltipPlugin tooltipsPlugin);


    public IDisplay getDisplay();

    public void setDisplay(IDisplay display);

//    public IWorkspaceController getDataController();

    //public IWorkspace getWorkspace();
    //public void setWorkspace(IWorkspace workspace);
    //public void setDataController(IWorkspaceController dataController);

    public EdgeInfoTablePlugin getEdgeTablesPlugin();

    public void setEdgeTablesPlugin(EdgeInfoTablePlugin edgeTablesPlugin);

    public PluginLayeredRenderer getPluginRenderer();

    public void setPluginRenderer(PluginLayeredRenderer pluginRenderer);

    public IAnimationStack getAnimator();

    public void setAnimator(IAnimationStack animator);

    public ILocalizedMouse getMouse();

    public void setMouse(ILocalizedMouse mouse);

    public IHierarchicalNodeModel getModel();

    public void setModel(IHierarchicalNodeModel model);


    public void setContext(NavigationContext context);

}
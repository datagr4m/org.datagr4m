package org.datagr4m.drawing.navigation.plugin;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.renderer.policy.ShadeGrayRendereringPolicy;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.mouse.ILocalizedMouse;


public class AbstractNavigationPlugin<V,E> implements INavigationPlugin<V,E> {
    //protected IWorkspace workspace;
    protected INavigationController navigationController;
    protected IDisplay display;
    protected ShadeGrayRendereringPolicy grayPolicy;
    protected PluginLayeredRenderer layered;
    protected IAnimationStack animator;
    protected ILocalizedMouse mouse;
    protected IHierarchicalNodeModel model;
    
    protected PluginDataModelHolder<V, E> data;
    
    public AbstractNavigationPlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalNodeModel model) {
        this.navigationController = controller;
        this.display = display;
        this.layered = layered;
        this.animator = animator;
        this.mouse = mouse;
        this.model = model;
        this.data = new PluginDataModelHolder<V, E>(model);
        
        // rendering policies init
        grayPolicy = new ShadeGrayRendereringPolicy();
        grayPolicy.setup(model);
    }

    @Override
	public INavigationController getNavigationController() {
        return navigationController;
    }

    @Override
	public IDisplay getDisplay() {
        return display;
    }

    @Override
	public PluginLayeredRenderer getLayeredPluginRenderer() {
        return layered;
    }

    @Override
	public IAnimationStack getAnimator() {
        return animator;
    }

    @Override
	public ILocalizedMouse getMouse() {
        return mouse;
    }

    @Override
	public IHierarchicalNodeModel getModel() {
        return model;
    }
    
    @Override
	public PluginDataModelHolder<V, E> getDataModel() {
        return data;
    }
}
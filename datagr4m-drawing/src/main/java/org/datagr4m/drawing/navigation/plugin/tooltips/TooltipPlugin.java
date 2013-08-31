package org.datagr4m.drawing.navigation.plugin.tooltips;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.AbstractNavigationPlugin;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.model.annotations.AnnotationModel;
import org.datagr4m.viewer.mouse.ILocalizedMouse;
import org.datagr4m.viewer.renderer.annotations.AnnotationRenderer;
import org.datagr4m.viewer.renderer.annotations.AnnotationRendererSettings;
import org.datagr4m.viewer.renderer.annotations.IAnnotationRendererSettings;


public class TooltipPlugin extends AbstractNavigationPlugin{
    public TooltipPlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalModel model) {
        super(controller, display, layered, animator, mouse, model);
        settings = new AnnotationRendererSettings();
        annotationModel = new AnnotationModel();
        renderer = new SlotAnnotationRenderer(annotationModel, settings);
    }
    
    public void setup(){
        layered.setTooltipLayer(renderer);
    }
    
    public AnnotationModel getTooltipModel(){
        return annotationModel;
    }
    
    public void clearTooltips(){
        annotationModel.clear();
    }
    
    public AnnotationRenderer getTooltipLayer(){
        return renderer;
    }
    
    public IAnnotationRendererSettings getSettings() {
        return settings;
    }

    AnnotationRenderer renderer;
    AnnotationModel annotationModel;
    IAnnotationRendererSettings settings;
}

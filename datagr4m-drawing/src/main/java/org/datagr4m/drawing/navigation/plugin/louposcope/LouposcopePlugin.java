package org.datagr4m.drawing.navigation.plugin.louposcope;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.AbstractNavigationPlugin;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.mouse.ILocalizedMouse;


/**
 * Louposcope uses following tools:
 * <ul>
 * <li>A {@link ILouposcopeContent} that gather details to display about the node (e.g. a set of {@link INetworkInterface})</li>
 * <li>A {@link AbstractLouposcopeLayer} that extract content to be displayed and render it</li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * The rendering layer is not linked to this plugin. Instead, The {@link PluginLayeredRenderer}
 * let the louposcope layer be available through {@link getLouposcopeLayer()} method.
 * 
 * @author Martin Pernollet
 *
 */
public class LouposcopePlugin extends AbstractNavigationPlugin{
    public LouposcopePlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalModel model) {
        super(controller, display, layered, animator, mouse, model);
    }

    public void setup() {
        // gray color model for main layer
        //grayPolicy.apply(layered.getMainHierarchicalRenderer());

        layered.setOperational(layered.getMainHierarchicalRenderer(), true);
        layered.setOperational(layered.getBringAndGoLayer(), false);

        layered.setVisible(layered.getLouposcopeLayer(), true);
        // layered.setHittable(layered.getDeviceDetailsLayer(), false);

        display.refresh();
    }
}

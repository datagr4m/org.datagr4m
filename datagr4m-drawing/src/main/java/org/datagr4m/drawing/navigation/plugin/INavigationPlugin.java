package org.datagr4m.drawing.navigation.plugin;

import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.mouse.ILocalizedMouse;


/**
 * Il n'est pas obligatoire de typer un plugin de navigation: 
 * d�pend de l'impl�mentation: si elle utilise le mod�le m�tier, il
 * est recommand� de l'indiquer dans la signature de la classe pour
 * �viter crash runtimes
 * 
 * 
 * @author Martin
 *
 * @param <V>
 * @param <E>
 */
public interface INavigationPlugin<V,E> {

	//public IWorkspace getWorkspace();

	public INavigationController getNavigationController();

	public IDisplay getDisplay();

	public PluginLayeredRenderer getLayeredPluginRenderer();

	public IAnimationStack getAnimator();

	public ILocalizedMouse getMouse();

	public IHierarchicalNodeModel getModel();

	public PluginDataModelHolder<V,E> getDataModel();
}
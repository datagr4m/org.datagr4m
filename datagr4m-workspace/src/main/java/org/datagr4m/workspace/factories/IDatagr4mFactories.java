package org.datagr4m.workspace.factories;

import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.viewer.mouse.factory.IMouseControllerFactory;

public interface IDatagr4mFactories {
	public IMouseControllerFactory getMouseFactory();
	
	public IHierarchicalLayoutFactory getLayoutFactory();
	public IHierarchicalModelFactory getModelFactory();
	public IHierarchicalRendererFactory getRendererFactory();
	
	public void setMouseFactory(IMouseControllerFactory mouseFactory);
	
	public void getLayoutFactory(IHierarchicalLayoutFactory layoutFactory);
	public void getModelFactory(IHierarchicalModelFactory modelFactory);
	public void getRendererFactory(IHierarchicalRendererFactory rendererFactory);
}

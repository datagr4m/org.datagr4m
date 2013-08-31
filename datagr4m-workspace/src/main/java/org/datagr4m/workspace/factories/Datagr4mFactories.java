package org.datagr4m.workspace.factories;

import org.datagr4m.drawing.layout.factories.HierarchicalLayoutFactory;
import org.datagr4m.drawing.layout.factories.IHierarchicalLayoutFactory;
import org.datagr4m.drawing.model.factories.HierarchicalTopologyModelFactory;
import org.datagr4m.drawing.model.factories.IHierarchicalModelFactory;
import org.datagr4m.drawing.renderer.factories.HierarchicalRendererFactory;
import org.datagr4m.drawing.renderer.factories.IHierarchicalRendererFactory;
import org.datagr4m.drawing.viewer.mouse.edges.factory.MouseEdgeControllerFactory;
import org.datagr4m.viewer.mouse.factory.IMouseControllerFactory;

public class Datagr4mFactories implements IDatagr4mFactories{
	IMouseControllerFactory mouseFactory = new MouseEdgeControllerFactory();
	IHierarchicalLayoutFactory layoutFactory = new HierarchicalLayoutFactory();
	IHierarchicalModelFactory modelFactory = new HierarchicalTopologyModelFactory();
	IHierarchicalRendererFactory rendererFactory = new HierarchicalRendererFactory();
	
	@Override
	public IMouseControllerFactory getMouseFactory() {
		return mouseFactory;
	}

	@Override
	public IHierarchicalLayoutFactory getLayoutFactory() {
		return layoutFactory;
	}

	@Override
	public IHierarchicalModelFactory getModelFactory() {
		return modelFactory;
	}

	@Override
	public IHierarchicalRendererFactory getRendererFactory() {
		return rendererFactory;
	}

	@Override
	public void setMouseFactory(IMouseControllerFactory mouseFactory) {
		this.mouseFactory = mouseFactory;
	}

	@Override
	public void getLayoutFactory(IHierarchicalLayoutFactory layoutFactory) {
		this.layoutFactory = layoutFactory;
	}

	@Override
	public void getModelFactory(IHierarchicalModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	@Override
	public void getRendererFactory(IHierarchicalRendererFactory rendererFactory) {
		this.rendererFactory = rendererFactory;
	}
}

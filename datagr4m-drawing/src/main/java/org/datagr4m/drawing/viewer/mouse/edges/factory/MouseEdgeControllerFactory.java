package org.datagr4m.drawing.viewer.mouse.edges.factory;

import org.datagr4m.drawing.viewer.mouse.edges.MouseEdgeViewController;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.mouse.AbstractMouseViewController;
import org.datagr4m.viewer.mouse.factory.DefaultMouseControllerFactory;


public class MouseEdgeControllerFactory extends DefaultMouseControllerFactory{
	@Override
    public AbstractMouseViewController getController(IDisplay display, IView view){
        return new MouseEdgeViewController(display, view);
    }
}

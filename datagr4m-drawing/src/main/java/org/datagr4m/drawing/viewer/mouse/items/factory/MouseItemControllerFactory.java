package org.datagr4m.drawing.viewer.mouse.items.factory;

import org.datagr4m.drawing.viewer.mouse.items.MouseItemViewController;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.mouse.AbstractMouseViewController;
import org.datagr4m.viewer.mouse.factory.DefaultMouseControllerFactory;


public class MouseItemControllerFactory extends DefaultMouseControllerFactory{
	@Override
    public AbstractMouseViewController getController(IDisplay display, IView view){
        return new MouseItemViewController(display, view);
    }
}

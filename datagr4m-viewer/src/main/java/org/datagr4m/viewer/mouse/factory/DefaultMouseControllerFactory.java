package org.datagr4m.viewer.mouse.factory;

import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.mouse.AbstractMouseViewController;
import org.datagr4m.viewer.mouse.DefaultMouseViewController;

public class DefaultMouseControllerFactory implements IMouseControllerFactory {
    @Override
    public AbstractMouseViewController getController(IDisplay display, IView view){
        return new DefaultMouseViewController(display, view);
    }
}

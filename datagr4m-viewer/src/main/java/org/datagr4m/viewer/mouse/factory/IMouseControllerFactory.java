package org.datagr4m.viewer.mouse.factory;

import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.mouse.AbstractMouseViewController;

public interface IMouseControllerFactory {

    public AbstractMouseViewController getController(IDisplay display, IView view);

}
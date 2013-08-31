package org.datagr4m.viewer.layered;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

public interface IPopupLayer {

    /** Add a component to {@link JInternalFrame}, add this frame to popup layer and return it.*/
    public abstract JInternalFrame addPopupLayer(JComponent component, String title, int x, int y, int width, int height);

    public abstract JInternalFrame addPopupLayer(JComponent component, String title, int x, int y, int width, int height, Object constraint);

}
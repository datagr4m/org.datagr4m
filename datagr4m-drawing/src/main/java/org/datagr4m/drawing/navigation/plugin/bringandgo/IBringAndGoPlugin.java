package org.datagr4m.drawing.navigation.plugin.bringandgo;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.viewer.animation.IAnimationMonitor;


public interface IBringAndGoPlugin {
    public void bring(IBoundedItem item);
    public void go(IBoundedItem item, IAnimationMonitor monitor);
}
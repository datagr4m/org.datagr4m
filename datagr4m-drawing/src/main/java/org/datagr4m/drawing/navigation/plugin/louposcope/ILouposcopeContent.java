package org.datagr4m.drawing.navigation.plugin.louposcope;

import java.awt.Graphics2D;
import java.util.Collection;

public interface ILouposcopeContent<T> {

    public void build(Collection<T> details);

    public void render(Graphics2D graphic);
    
    public void update();

}
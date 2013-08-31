package org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.buttons;

import java.awt.geom.Point2D;
import java.util.List;

import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.renderer.items.hierarchical.graph.edges.hierarchical.TubeRendererSettings;
import org.datagr4m.viewer.mouse.IClickableItem;


public class TubeSourceButton extends TubeOpenCloseButton{

    public TubeSourceButton(Tube tube, Point2D position, float radius, String name1, TubeRendererSettings settings) {
        super(tube, position, radius, name1);
        this.settings = settings;
        if(settings.isTubeSourceOpened(tube))
            open();
        else
            close();
    }
    
    @Override
	public List<IClickableItem> hit(int x, int y){
        if(circle.contains(x, y)){
            toggleOpenStatus();
            settings.setTubeSourceOpened(tube, isOpen());
            return listClick(this);
        }
        return null;
    }

    protected TubeRendererSettings settings;
    private static final long serialVersionUID = 1323675060523898481L;
}

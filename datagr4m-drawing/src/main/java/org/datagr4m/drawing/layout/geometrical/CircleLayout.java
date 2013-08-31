package org.datagr4m.drawing.layout.geometrical;

import java.util.List;

import org.datagr4m.drawing.layout.AbstractStaticLayout;
import org.datagr4m.drawing.layout.IStaticLayout;
import org.datagr4m.drawing.layout.geometrical.flower.FlowerGeometry;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public class CircleLayout extends AbstractStaticLayout implements IStaticLayout{
    private static final long serialVersionUID = -5124418768199003016L;
    
    public CircleLayout(List<IBoundedItem> items, Coord2d center, boolean autoRadius, float radius){
        setModel(items);
        this.center = center;
        this.autoRadius = autoRadius;
        this.radius = radius;
    }

    @Override
    public void initAlgo() {
        goAlgo();
    }
    
    public void goAlgo(){
        if(autoRadius){
            float max = 0;
            for(IBoundedItem item: model){
                float r = item.getRadialBounds();
                if(r>max)
                    max = r;
            }
            FlowerGeometry flower = FlowerGeometry.fromR2(max, model.size());
            radius = flower.getBodyRadius();
        }
        
        if(model!=null && center!=null){
            double angle = 0;
            double step = Math.PI*2/model.size();
            for(IBoundedItem item: model){
                fixPosition(item, center.add(new Coord2d(angle, radius).cartesian()));
                angle+=step;
            }            
        }
    }
    
    @Override
	public void fixPosition(IBoundedItem item, Coord2d position){
        item.changePosition(position);        
    }
    
    @Override
    public void resetPropertiesValues() {
    }


    public void setModel(List<IBoundedItem> items) {
        this.model = items;
    }

    public List<IBoundedItem> getModel() {
        return model;
    }
    
    /***********************/

    protected List<IBoundedItem> model;
    protected double radius;
    protected Coord2d center;
    protected boolean autoRadius;
}



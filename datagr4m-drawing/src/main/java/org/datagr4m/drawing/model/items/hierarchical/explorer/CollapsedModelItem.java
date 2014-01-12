package org.datagr4m.drawing.model.items.hierarchical.explorer;

import javax.swing.Icon;

import org.datagr4m.drawing.model.items.DefaultBoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItemIcon;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.jzy3d.maths.Coord2d;


public class CollapsedModelItem extends DefaultBoundedItemIcon implements IBoundedItemIcon{
    private static final long serialVersionUID = 6723996304245319449L;

    public CollapsedModelItem(String name, Icon icon, IHierarchicalNodeModel real){
        super(name, icon);
        this.real = real;
    }
    
    /*public CollapsedModelItem(IHierarchicalModel real, Icon icon){
        super(real.toString(), icon);
        this.real = real;
    }*/
    
    @Override
    public void changePosition(float x, float y){
        real.changePosition(x, y);
    }

    @Override
    public void shiftPosition(float x, float y){
        real.shiftPosition(x, y);
    }
    
    @Override
    public Coord2d getPosition() {
        return real.getPosition();
    }

    @Override
	public Coord2d getAbsolutePosition(){
        return real.getAbsolutePosition();
    }
    
    @Override
	public Object getObject(){
        return real.getObject();
    }
    
    /*public IBoundedItem clone(){
        CollapsedModelItem clon = new CollapsedModelItem(getLabel(), getIcon(), real);
        //clon.rectangleBounds = getRectangleBounds().clone();
        //clon.radialBounds = getRadialBounds();
        //clon.position = getPosition().clone();
        clon.parent = null;
        return clon;
    }*/
    
    protected IHierarchicalNodeModel real;
}

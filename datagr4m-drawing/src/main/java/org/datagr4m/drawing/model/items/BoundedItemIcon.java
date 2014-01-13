package org.datagr4m.drawing.model.items;

import javax.swing.Icon;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.Coord2d;


public class BoundedItemIcon extends BoundedItem implements IBoundedItemIcon{
    public BoundedItemIcon(String label, Coord2d position, Icon icon) {
        super(label, position);
        setIcon(icon);
    }

    public BoundedItemIcon(Pair<String,? extends Icon> labelIcon) {
        this(labelIcon.a, labelIcon.b);
    }
    
    public BoundedItemIcon(String label, Icon icon) {
        super(label);
        setIcon(icon);
    }
    
    public BoundedItemIcon(Object o, Icon icon) {
        this(o, icon, DEFAULT_SLOT_HEIGHT);
    }
    
    public BoundedItemIcon(Object o, Icon icon, float margin) {
        super(o);
        setIcon(icon, margin);
    }

    public BoundedItemIcon(Object o, Pair<String,? extends Icon> labelIcon) {
        this(o, labelIcon.a, labelIcon.b, DEFAULT_SLOT_HEIGHT);
    }

    public BoundedItemIcon(Object o, String label, Icon icon) {
        this(o, label, icon, DEFAULT_SLOT_HEIGHT);
    }
    
    public BoundedItemIcon(Object o, String label, Icon icon, float margin) {
        super(o, label);
        setIcon(icon, margin);
    }
    
    /**********/
        
    @Override
    public void setIcon(Icon icon) {
        setIcon(icon, 0);
    }
    
    @Override
    public void setIcon(Icon icon, float margin) {
        this.icon = icon;
        this.margin = margin;
        this.slotMargin = margin;
        
        // un rectangle qui contient à la fois le texte et l'icone
        float width = 0;
        float height = 0;
        
        if(icon!=null){
            width = Math.max(icon.getIconWidth(),TextUtils.textWidth(getLabel()));
            height = icon.getIconHeight()+TextUtils.textHeight();
        }
        
        this.rectangleBounds = new RectangleBounds(width+margin*2, height+margin*2);
        this.radialBounds = rectangleBounds.getRadius();
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public Coord2d getScale() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setScale(Coord2d scale) {
        throw new RuntimeException("not implemented");
    }
    
    @Override
	public IBoundedItem clone(){
        BoundedItemIcon clon = new BoundedItemIcon(getObject(), getLabel(), getIcon(), getSlotMargin());
        clon.rectangleBounds = getRawRectangleBounds().clone();
        clon.radialBounds = getRadialBounds();
        clon.position = getPosition().clone();
        clon.parent = null;
        return clon;
    }
    
    /**************/
    
    protected Icon icon;
    
    private static final long serialVersionUID = 5992337850972258691L;
}

package org.datagr4m.drawing.model.items.hierarchical;

import java.util.Collection;
import java.util.List;

import javax.swing.Icon;

import org.datagr4m.drawing.model.items.DefaultBoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItemIcon;
import org.jzy3d.maths.Coord2d;


public class IconHierarchicalModel extends AbstractHierarchicalModel implements IIconHierarchicalModel {
    public IconHierarchicalModel(IHierarchicalModel parent, List<IBoundedItem> children, Collection<IBoundedItem> neighbours, Icon icon) {
        super(parent, children, neighbours);
        setIcon(icon);
    }
    
    public IconHierarchicalModel(Icon icon) {
        super();
        setIcon(icon);
    }

    /*public IconHierarchicalModel(IHierarchicalModel parent, List<IBoundedItem> children, Collection<IBoundedItem> neighbours) {
        super(parent, children, neighbours);
    }
    public IconHierarchicalModel(IHierarchicalModel parent, List<IBoundedItem> children) {
        super(parent, children);
    }
    public IconHierarchicalModel(IHierarchicalModel parent) {
        super(parent);
    }
    public IconHierarchicalModel() {
        super();
    }*/

    /**********/
    
    @Override
    public void setIcon(Icon icon) {
        setIcon(icon, 0);
    }
    
    @Override
    public void setIcon(Icon icon, float margin) {
        this.icon = icon;
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
        return new DefaultBoundedItemIcon(data, icon, 0);
    }
    
    public static IIconHierarchicalModel clone(IBoundedItemIcon item){
        IconHierarchicalModel model = new IconHierarchicalModel(item.getIcon());
        model.label = item.getLabel();
        model.data = item.getObject();
        return model;
    }

    @Override
    public String getDebugInfo(){
        return "";
    }

    
    /**************/
    
    protected Icon icon;
    private static final long serialVersionUID = 7496119778481807684L;
}

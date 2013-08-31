package org.datagr4m.drawing.model.items.zones;

import java.awt.Polygon;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;


public interface IZoneModel {
    public String getLabel();
    public void setLabel(String label);
    public Polygon getGeometry();
    
    public List<IBoundedItem> getChildren();
    public void addChild(IBoundedItem child);
    public void addChild(IBoundedItem child, boolean updateGeometry);
    public void addChildren(List<IBoundedItem> children);

    public void dispose();
    
    public Object getData();
    public Object getData(IBoundedItem item);
    public IBoundedItem getItem(Object data);
    public IBoundedItem getChildWithLabel(String label);  
    
    public void updateGeometry();
}

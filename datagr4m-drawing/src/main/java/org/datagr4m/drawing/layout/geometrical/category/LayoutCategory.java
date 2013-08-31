package org.datagr4m.drawing.layout.geometrical.category;

import java.util.List;

import org.datagr4m.drawing.model.items.BarOrientation;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


public class LayoutCategory {
    public LayoutCategory(List<IBoundedItem> items, BarOrientation orientation){
        this(items, new Coord2d(), orientation);
    }
    public LayoutCategory(List<IBoundedItem> items, Coord2d position, BarOrientation orientation){
        this.items = items;
        this.itemNumber = items.size();
        this.itemRadius = CategoryLayout.getMaxRadius(items);
        this.itemWidth = CategoryLayout.getMaxWidth(items);
        this.itemHeight = CategoryLayout.getMaxHeight(items);
        this.orientation = orientation;
        if(orientation==BarOrientation.HORIZONTAL){
            this.width = itemNumber*itemWidth;//itemRadius*2;
            this.height = itemHeight;//itemRadius*2;
        }
        else{
            this.width = itemWidth;//itemRadius*2;
            this.height = itemNumber*itemHeight;//itemNumber*itemRadius*2;
        }
        this.position= position; 
    }
    
    public Coord2d getPosition() {
        return position;
    }

    public void setPosition(Coord2d position) {
        this.position = position;
    }

    public Coord2d getAbsoluteItemPosition(IBoundedItem item){
        return position.add(getItemPosition(item));
    }
    
    public Coord2d getItemPosition(IBoundedItem item){
        int id = items.indexOf(item);
        if(id==-1)
            throw new RuntimeException("item not in category");
        else{
            if(orientation==BarOrientation.HORIZONTAL){
                //float x = -width/2 + id*itemRadius*2 + itemRadius;
                float x = -width/2 + id*itemWidth + itemWidth/2;
                return new Coord2d(x, 0);
            }
            else{
                //float y = -height/2 + id*itemRadius*2 + itemRadius;
                float y = -height/2 + id*itemHeight + itemHeight/2;
                return new Coord2d(0, y);
            }
        }
    }
    
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public void update(){
        for(IBoundedItem i: items)
            i.changePosition(getAbsoluteItemPosition(i));
    }
    
    public List<IBoundedItem> getItems() {
        return items;
    }

    protected Coord2d position;
    
    protected List<IBoundedItem> items;
    protected float itemRadius;
    protected float itemHeight;
    protected float itemWidth;
    protected int itemNumber;
    
    protected float width;
    protected float height;
    
    protected BarOrientation orientation;
}

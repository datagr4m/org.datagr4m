package org.datagr4m.drawing.layout.geometrical.category;

import java.util.Collection;

import org.datagr4m.drawing.layout.IStaticLayout;
import org.datagr4m.drawing.layout.hierarchical.AbstractHierarchicalLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.jzy3d.maths.Coord2d;

import com.google.common.collect.ArrayListMultimap;

public class CategoryLayout extends AbstractHierarchicalLayout implements IStaticLayout{
    private static final long serialVersionUID = 8119255818627497594L;
    
    public CategoryLayout(IHierarchicalModel model, ICategoryProcessor processor){
        this.model = model;
        this.processor = processor;
    }
    
    @Override
    public void setModel(IHierarchicalModel model) {
        this.model = model;
    }
    
    @Override
    public IHierarchicalModel getModel() {
        return model;
    }

    @Override
    public void fixPosition(IBoundedItem item, Coord2d position) {
        item.changePosition(position);
    }
    
    public ICategoryProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(ICategoryProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void initAlgo() {
        this.categories = processor.getCategories(model.getChildren());
        
        for(String c: categories.keySet())
            System.out.println(c);
        System.err.println("CategoryLayout not implemented!");
    }
    
    public static float getMaxRadius(Collection<IBoundedItem> items){
        float max = 0;
        for(IBoundedItem i: items)
            if(i.getRadialBounds()>max)
                max = i.getRadialBounds();
        return max;
    }
    
    public static float getMaxWidth(Collection<IBoundedItem> items){
        float max = 0;
        for(IBoundedItem i: items)
            if(i.getRawRectangleBounds().getWidth()>max)
                max = i.getRawCorridorRectangleBounds().getWidth();
        return max;
    }
    
    public static float getMaxHeight(Collection<IBoundedItem> items){
        float max = 0;
        for(IBoundedItem i: items)
            if(i.getRawRectangleBounds().getHeight()>max)
                max = i.getRawCorridorRectangleBounds().getHeight();
        return max;
    }
    
    /***********************/

    protected IHierarchicalModel model;
    protected ICategoryProcessor processor;
    protected ArrayListMultimap<String,IBoundedItem> categories;

}

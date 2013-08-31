package org.datagr4m.drawing.layout.geometrical.category;

import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;

import com.google.common.collect.ArrayListMultimap;

public abstract class AbstractCategoryProcessor implements ICategoryProcessor {
    @Override
	public ArrayListMultimap<String,IBoundedItem> getCategories(List<IBoundedItem> items){
        ArrayListMultimap<String,IBoundedItem> categories = ArrayListMultimap.create();
        for(IBoundedItem i: items)
            categories.put(getCategory(i), i);
        return categories;
    }
}

package org.datagr4m.drawing.layout.geometrical.category;

import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;

import com.google.common.collect.ArrayListMultimap;

public interface ICategoryProcessor{
    public String getCategory(IBoundedItem item);
    public ArrayListMultimap<String,IBoundedItem> getCategories(List<IBoundedItem> items);
}

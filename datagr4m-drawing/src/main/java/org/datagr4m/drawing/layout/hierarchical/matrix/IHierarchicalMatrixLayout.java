package org.datagr4m.drawing.layout.hierarchical.matrix;

import org.datagr4m.drawing.layout.hierarchical.IHierarchicalLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;


public interface IHierarchicalMatrixLayout extends IHierarchicalLayout{
    public void setSize(int nLine, int nColumn) throws IllegalArgumentException;
    public void setItemCell(IBoundedItem item, int lineIndex, int columnIndex) throws IllegalArgumentException;
    
    public void setLineHeight(float height);
    public void setColumnWidth(float width);
    //public void setLineHeight(int line, float height);
    //public void setColumnWidth(int column, float width);

    public float getLineHeight(int line);
    public float getColumnWidth(int column);
}

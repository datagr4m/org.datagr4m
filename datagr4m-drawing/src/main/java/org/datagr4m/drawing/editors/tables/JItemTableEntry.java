package org.datagr4m.drawing.editors.tables;


public class JItemTableEntry<T> {
    public JItemTableEntry(T item){
        this.item = item;
    }
    public T getItem() {
        return item;
    }
    
    protected T item;
}

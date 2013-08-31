package org.datagr4m.topology;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

public class Zone<K> extends ArrayList<K> {
    protected String name;
    protected String type;
    protected Color color;
    
    public Zone() {
        super();
    }
    public Zone(Collection<? extends K> c) {
        super(c);
    }
    public Zone(int initialCapacity) {
        super(initialCapacity);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    
    @Override
	public String toString(){
        return name + " (" + type + ")";
    }
    
    private static final long serialVersionUID = 219164261797079626L;
}

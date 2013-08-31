package org.datagr4m.drawing.layout.algorithms.forces;

import java.io.Serializable;

public class ItemForceVector implements Serializable{
    private static final long serialVersionUID = 5476885541756487256L;
    
    public float dx = 0;
    public float dy = 0;
    public float old_dx = 0;
    public float old_dy = 0;
    public float freeze = 0f;
    
    @Override
	public String toString(){
        return "dx:"+dx+" dy:"+dy;
    }
}

package org.datagr4m.drawing.model.bounds;

public class BoundsMerger {
    public BoundsMerger(){
        reset();
    }
    
    public void append(RectangleBounds b){
     // compute the actual bounds
        float bxmax = b.x+b.width;
        float bymax = b.y+b.height;
        
        if(bxmax > xmax)
            xmax = bxmax;
        if(b.x < xmin)
            xmin = b.x;
        if(bymax > ymax)
            ymax = bymax;
        if(b.y < ymin)
            ymin = b.y;
    }
    
    public RectangleBounds build(){
        return new RectangleBounds(xmin, ymin, xmax-xmin, ymax-ymin);
    }
    
    public float width(){
        return xmax-xmin;
    }
    
    public float height(){
        return ymax-ymin;
    }
    
    public boolean valid(){
        return xmin!=DEFAULT_MIN
            && xmax!=DEFAULT_MAX
            && ymin!=DEFAULT_MIN
            && ymax!=DEFAULT_MAX;
    }
    
    public boolean hasNan(){
        return Float.isNaN(xmin) || Float.isNaN(xmax) || Float.isNaN(ymin) || Float.isNaN(ymax);
    }
    
    public void reset(){
        xmin = DEFAULT_MIN;
        xmax = DEFAULT_MAX;
        ymin = DEFAULT_MIN;
        ymax = DEFAULT_MAX;
    }
    
    public float xmin = DEFAULT_MIN;
    public float xmax = DEFAULT_MAX;
    public float ymin = DEFAULT_MIN;
    public float ymax = DEFAULT_MAX;
    
    protected static float DEFAULT_MIN = Float.MAX_VALUE;
    protected static float DEFAULT_MAX = -Float.MAX_VALUE;
}

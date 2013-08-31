package org.datagr4m.drawing.renderer.items.shaped;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.renderer.DifferedRenderer;
import org.datagr4m.viewer.renderer.TextUtils;
import org.jzy3d.maths.Coord2d;


public abstract class UnscaledTextDifferedRenderer extends DifferedRenderer{
    public UnscaledTextDifferedRenderer(String label, Coord2d position) {
        this(label, position, -1);
    }    
    public UnscaledTextDifferedRenderer(String label, Coord2d position, int size) {
        this(label, position, size, 0);
    }
    
    public UnscaledTextDifferedRenderer(String label, Coord2d position, int size, int priority) { 
        this.label = label;
        this.position = position;
        this.size = size;
        this.initialSize = size;
        this.priority = priority;
    }

    @Override
    public void render(Graphics2D graphic) {
        int oldsize = -1;
        if(size!=TextUtils.size){
            oldsize = TextUtils.size;
            TextUtils.changeFontSize(graphic, size);
        }
        
        renderText(graphic);
        
        if(oldsize!=-1){
            TextUtils.changeFontSize(graphic, oldsize);
        }
    }
    
    public abstract void renderText(Graphics2D graphic);
    
    public String getLabel() {
        return label;
    }

    public Coord2d getPosition() {
        return position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public int getInitialSize() {
        return initialSize;
    }
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    
    
    /***********/
    
    public Rectangle2D computeBounds(){
        int oldsize = -1;
        if(size!=TextUtils.size){
            TextUtils.changeFontSize(size);
        }
        Rectangle2D r =  new Rectangle2D.Float(position.x, position.y, TextUtils.textWidth(label)+10, TextUtils.textHeight()+5);
        if(oldsize!=-1){
            TextUtils.changeFontSize(oldsize);
        }
        return r;
    }

    static float factor = 1.5f;
    
    public Rectangle2D computeBounds(IView view){
        int oldsize = -1;
        if(size!=TextUtils.size){
            TextUtils.changeFontSize(size);
        }
        Point2D p = Pt.cloneAsFloatPoint(position);
        Point2D p2 = new Point2D.Double(p.getX()+TextUtils.textWidth(label), p.getY()+TextUtils.textHeight());
        view.modelToScreen(p);
        view.modelToScreen(p2);
        Rectangle2D r =  new Rectangle2D.Float((float)p.getX(), (float)p.getY(), TextUtils.textWidth(label)*factor, TextUtils.textHeight()*factor);
        //Rectangle2D r =  new Rectangle2D.Double(p.getX(), p.getY(), p2.getX()-p.getX(), p2.getY()-p.getY());
        if(oldsize!=-1){
            TextUtils.changeFontSize(oldsize);
        }
        return r;
    }
    
    /**********/
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UnscaledTextDifferedRenderer other = (UnscaledTextDifferedRenderer) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }
    
    /*********/

    protected String label;
    protected Coord2d position;
    protected int size = -1;
    protected int priority = 0;
    protected int initialSize;
    //protected boolean specificSize;
}

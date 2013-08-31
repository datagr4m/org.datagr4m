package org.datagr4m.drawing.model.bounds;

import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.jzy3d.maths.Coord2d;


/**
 * Create a rectangle bound, either from top left corner or from center (see constructor).
 * 
 * As an IBounds object, provides ways to check if a point is inside, and correct its
 * position so that the objects stand on the bounds border.
 * 
 * Provides method for shifting the bounds, intersecting with some other bounds, and enlarge
 * the bounds.
 * 
 * @author Martin Pernollet
 *
 */
public class RectangleBounds implements IBounds {
    /** Creates a RectangleBounds centered at 0, with x=-width/2 and -height/2.*/
	public RectangleBounds(float width, float height) {
		this(-width/2, -height/2, width, height);
	}
	
	/** Creates a RectangleBounds centered at x+width/2, y+height/2, 
	 * in other word initialize from top left corner
	 */
	public RectangleBounds(float x, float y, float width, float height) {
		this(x, y, width, height, computeCenter(x, y, width, height));
	}
	
	protected RectangleBounds(float x, float y, float width, float height, Coord2d center) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.center = center;
    }
	
	protected static Coord2d computeCenter(float x, float y, float width, float height){
	    return new Coord2d(x+width/2, y+height/2);
	}
	
	// TODO: eviter new
	public Coord2d getTopLeftCorner(){
        return new Coord2d(x, y);
    }
	
	public Coord2d getTopPoint(){
        return new Coord2d(x+width/2, y);
    }
	
    // TODO: �viter new
	public Coord2d getTopRightCorner(){
        return new Coord2d(x+width, y);
    }

    // TODO: �viter new
    public Coord2d getBottomLeftCorner(){
        return new Coord2d(x, y+height);
    }
    
    public Coord2d getBottomRightCorner(){
        return new Coord2d(x+width, y+height);
    }
	
	@Override
	public Coord2d getCenter() {
		return center;
	}
	
	/************/
	
	//TODO: ca cr�e une classe pour rien
	@Override
    public Dimension getDimension() {
        return new Dimension((int)width, (int)height);
    }
	
	public void setDimension(int width, int height){
	    this.width = width;
	    this.height = height;
	}
	
	public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    public void setWidth(float width) {
        this.width = width;
        this.center = computeCenter(x, y, width, height);
    }

    public void setHeight(float height) {
        this.height = height;
        this.center = computeCenter(x, y, width, height);
    }

    public void setCenter(Coord2d center) {
        this.center = center;
        this.x = center.x-width/2;
        this.y = center.y-height/2;
    }

    /***********/
		
	@Override
	public boolean isIn(float x, float y){
		if(x<this.x)
			return false;
		if(y<this.y)
			return false;
		if(x>this.x+this.width)
			return false;
		if(y>this.y+this.height)
			return false;
		return true;
	}
	
	@Override
	public Coord2d correct(float x, float y) {
		return new Coord2d(correctX(x), correctY(y));
	}
	
	protected float correctX(float x){
		if(x<this.x)
			return this.x;
		else if(x>this.x+this.width)
			return this.x+this.width;
		else
			return x;
	}

	protected float correctY(float y){
		if(y<this.y)
			return this.y;
		else if(y>this.y+this.height)
			return this.y+this.height;
		else
			return y;
	}
	
	//TODO: warning with new
	public RectangleBounds shiftTopLeftTo(Coord2d to){
        return new RectangleBounds(to.x, to.y, width, height);
	}
	
    public RectangleBounds shiftCenterTo(Coord2d to){
        return new RectangleBounds(to.x-width/2, to.y-height/2, width, height);
    }
    
    public void shiftSelfCenterTo(Coord2d to){
        x = to.x-width/2;
        y = to.y-height/2;
    }
    
    public RectangleBounds shift(Coord2d shift){
        return new RectangleBounds(x+shift.x, y+shift.y, width, height);
    }

    public boolean contains(RectangleBounds bounds){
        if(x<=bounds.x){
            if(width+x>=bounds.width+bounds.x){
            }
            else
                return false;
        }
        else
            return false;

        if(y<=bounds.y){// assume y is increasing from top to down
            if(height-y>=bounds.height-bounds.y){
            }
            else
                return false;
        }
        else
            return false;
        return true;
    }
    
    public boolean intersects(Rectangle2D r){
        return r.intersects(x, y, width, height);
    }
    
    public boolean intersects(Ellipse2D e){
        return e.intersects(x, y, width, height);
    }
    
    /** 
     * Returns the half diagonal of the rectangle.
     */
    public float getRadius(){
        return (float) Math.hypot(width, height)/2;
    }

    public RectangleBounds enlarge(float width, float height){
        return new RectangleBounds(x, y, this.width+width, this.height+height);
    }
    
    public RectangleBounds enlarge(double width, double height){
        return new RectangleBounds(x, y, (float)(this.width+width), (float)(this.height+height));
    }

    public void enlargeSelfInPlace(float width, float height){
        this.x -= width/2;
        this.y -= height/2;
        this.center.x -= width/2;
        this.center.y -= height/2;
        this.width += width;
        this.height += height;
    }
    
    
    public void enlargeSelf(float width, float height){
        this.width+=width;
        this.height+=height;
    }
    
    public void enlargeSelf(double width, double height){
        this.width+=width;
        this.height+=height;
    }

	
	@Override
	public String toString(){
		return "(" + this.getClass().getSimpleName() + ") x=" + x + " y=" + y + " width="+width + " height=" + height;
	}
	
	public Rectangle2D cloneAsRectangle2D(){
	    return new Rectangle2D.Double(x, y, width, height);
	}
	
	@Override
	public RectangleBounds clone(){
        return new RectangleBounds(x, y, width, height, center.clone());
    }
	
	
	
	public float x;
	public float y;
	public float width;
	public float height;
	public Coord2d center;
	private static final long serialVersionUID = 764558491732050452L;

	/****************/
	
	public static RectangleBounds build(Collection<IBoundedItem> items){
        float xmin =  Float.MAX_VALUE;
        float xmax = -Float.MAX_VALUE;
        float ymin =  Float.MAX_VALUE;
        float ymax = -Float.MAX_VALUE;
        
        for (IBoundedItem item: items) {
            Coord2d p = item.getPosition();
            RectangleBounds b = item.getRawRectangleBounds();
            if(p.x+b.width/2 > xmax)
                xmax = p.x + b.width/2;
            if(p.x-b.width/2 < xmin)
                xmin = p.x - b.width/2;
            if(p.y+b.height/2 > ymax)
                ymax = p.y + b.height/2;
            if(p.y-b.height/2 < ymin)
                ymin = p.y - b.height/2;
        }
		return new RectangleBounds(xmin, ymin, xmax-xmin, ymax-ymin);
	}
}

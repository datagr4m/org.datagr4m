package org.datagr4m.drawing.layout.geometrical;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.AbstractStaticLayout;
import org.datagr4m.drawing.layout.IStaticLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.maths.geometry.LineUtils;
import org.datagr4m.maths.geometry.Pt;
import org.jzy3d.maths.Coord2d;


public class RectangleLayout extends AbstractStaticLayout implements IStaticLayout{
    private static final long serialVersionUID = -5124418768199003016L;
    
    public RectangleLayout(List<IBoundedItem> items, Coord2d center, float width, float height){
        setModel(items);
        this.center = center;
        this.width = width;
        this.height = height;
        
    }

    @Override
    public void initAlgo() {
        goAlgo();
    }
    
    public void goAlgo(){
        float xmin = center.x - width/2;
        float xmax = center.x + width/2;
        float ymin = center.y - height/2;
        float ymax = center.y + height/2;
        
        topLine = new Line2D.Double(xmin, ymax, xmax, ymax);
        bottomLine = new Line2D.Double(xmin, ymin, xmax, ymin);
        leftLine = new Line2D.Double(xmin, ymin, xmin, ymax);
        rightLine = new Line2D.Double(xmax, ymin, xmax, ymax);
        
        segments = new ArrayList<Line2D>(model.size());
        
        radius = Math.max(width, height)/2;
        if(model!=null && center!=null){

            double angle = 0;
            double step = Math.PI*2/model.size();
            
            for(IBoundedItem item: model){
                Coord2d radialEnd = center.add(new Coord2d(angle, radius*2).cartesian());
                //System.out.println(center + " "+ radialEnd);
                Line2D segment = new Line2D.Double(center.x, center.y, radialEnd.x, radialEnd.y);
                
                segments.add(segment);
                
                Point2D top = LineUtils.getIntersectionPoint(topLine, segment, false);
                Point2D bottom = LineUtils.getIntersectionPoint(bottomLine, segment, false);
                Point2D left = LineUtils.getIntersectionPoint(leftLine, segment, false);
                Point2D right = LineUtils.getIntersectionPoint(rightLine, segment, false);
                
                if(top!=null)
                    fixPosition(item, Pt.cloneAsCoord2d(top));
                else if(bottom!=null)
                    fixPosition(item, Pt.cloneAsCoord2d(bottom));
                else if(left!=null)
                    fixPosition(item, Pt.cloneAsCoord2d(left));
                else if(right!=null)
                    fixPosition(item, Pt.cloneAsCoord2d(right));
                
                angle+=step;
            }            
        }
    }
    
    /*public void debugSegments(Graphics2D graphic){
        setLineWidth(graphic, 2);
        drawLine(graphic, topLine);
        drawLine(graphic, bottomLine);
        drawLine(graphic, leftLine);
        drawLine(graphic, rightLine);
        if(segments!=null)
            drawLines(graphic, segments);
        else {
            System.out.println("no segment");
        }
        setLineWidth(graphic, 1);
    }*/
    
    /*protected int nNonNull(){
        
    }*/
    
    @Override
    public void resetPropertiesValues() {
    }


    public void setModel(List<IBoundedItem> items) {
        this.model = items;
    }

    public List<IBoundedItem> getModel() {
        return model;
    }
    
    /***********************/

    protected List<IBoundedItem> model;
    protected double radius;
    protected Coord2d center;
    protected float width;
    protected float height;
    
    public Line2D topLine;
    public Line2D bottomLine;
    public Line2D leftLine;
    public Line2D rightLine;
    public List<Line2D> segments;
}



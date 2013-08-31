package org.datagr4m.drawing.layout.hierarchical.graph.edges.utils;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.EdgeSection;
import org.jzy3d.maths.Coord2d;


public class EdgeHit {
    /** Returns true if the tube intersects any obstacle.*/
    public static boolean intersectAny(EdgeSection section, List<IBoundedItem> items){
        for(IBoundedItem i: items){
            if(section.getEdge().getSourceItem()==i || section.getEdge().getTargetItem()==i)
                continue;
            else{
                Coord2d c = i.getAbsolutePosition();
                float r = i.getRadialBounds();
                Rectangle2D rect = new Rectangle2D.Double(c.x-r, c.y-r, r*2, r*2);
                
                //List<Point2D> pts = p.getPoints();
                List<Point2D> pts =  section.getSection();
                if(pts.size()==0)
                    System.err.println("a path without section: " + section);
                
                for (int j = 0; j < pts.size()-1; j++) {
                    Point2D p1 = pts.get(j);
                    Point2D p2 = pts.get(j+1);
                    if(rect.intersectsLine(p1.getX(), p1.getY(), p2.getX(), p2.getY()))
                        return true;
                }
            }
        }
        return false;
    }
    
    public static List<IBoundedItem> intersections(EdgeSection section, List<IBoundedItem> items){
        //Path p = tube.getPathGeometry();
        //List section.getSection();
        List<IBoundedItem> intersections = new ArrayList<IBoundedItem>();
        
        for(IBoundedItem i: items){
            if(section.getEdge().getSourceItem()==i || section.getEdge().getTargetItem()==i)
                continue;
            else{
                if(intersects(section.getSection(), i))
                    intersections.add(i);
            }
        }
        return intersections;
    }
    
    public static List<IBoundedItem> intersections(List<Point2D> pts, List<IBoundedItem> items){
        List<IBoundedItem> intersections = new ArrayList<IBoundedItem>();
        
        for(IBoundedItem i: items)
            if(intersects(pts, i))
                intersections.add(i);
        return intersections;
    }
    
    public static List<IBoundedItem> intersectionsCoords(List<Coord2d> pts, List<IBoundedItem> items){
        List<IBoundedItem> intersections = new ArrayList<IBoundedItem>();
        
        for(IBoundedItem i: items)
            if(intersectsCoords(pts, i))
                intersections.add(i);
        return intersections;
    }
    
    public static boolean intersects(List<Point2D> pts, IBoundedItem i){
        Coord2d c = i.getAbsolutePosition();
        float r = i.getRadialBounds();

        // TODO: donne moi en argument pour ne pas reconstruire � chaque fois
        Rectangle2D rect = new Rectangle2D.Double(c.x-r, c.y-r, r*2, r*2); 
        
        for (int j = 0; j < pts.size()-1; j++) {
            Point2D p1 = pts.get(j);
            Point2D p2 = pts.get(j+1);
            if(rect.intersectsLine(p1.getX(), p1.getY(), p2.getX(), p2.getY())){
                return true;
            }
        }
        return false;
    }
    
    public static boolean intersects(Point2D p1, Point2D p2, IBoundedItem i){
        Coord2d c = i.getAbsolutePosition();
        float r = i.getRadialBounds();
        Rectangle2D rect = new Rectangle2D.Double(c.x-r, c.y-r, r*2, r*2);
        
        if(rect.intersectsLine(p1.getX(), p1.getY(), p2.getX(), p2.getY()))
            return true;
        return false;
    }
    
    public static boolean intersectsCoords(List<Coord2d> pts, IBoundedItem i){
        Coord2d c = i.getAbsolutePosition();
        float r = i.getRadialBounds();
        Rectangle2D rect = new Rectangle2D.Double(c.x-r, c.y-r, r*2, r*2);
        
        for (int j = 0; j < pts.size()-1; j++) {
            Coord2d p1 = pts.get(j);
            Coord2d p2 = pts.get(j+1);
            if(rect.intersectsLine(p1.x, p1.y, p2.x, p2.y)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean intersects(Coord2d p1, Coord2d p2, IBoundedItem i){
        Coord2d c = i.getAbsolutePosition();
        float r = i.getRadialBounds();
        Rectangle2D rect = new Rectangle2D.Double(c.x-r, c.y-r, r*2, r*2);
        
        if(rect.intersectsLine(p1.x, p1.y, p2.x, p2.y))
            return true;
        return false;
    }
}

package org.datagr4m.drawing.renderer.pathfinder.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.viewer.renderer.DefaultRenderer;


public class PathSegmentsRenderer extends DefaultRenderer{
    protected static int NO_STROKE_SPEC = -1;

    /**
     * Render a complete path.
     * Lock path.getPoints() to ensure thread safety
     */
    public void render(Graphics2D graphic, IPath path, Color color, boolean showExtremity, boolean showPointId, Stroke stroke){
    	synchronized(path.getPoints()){
    		renderFromTo(graphic, path, 0, path.getPoints().size()-1, color, showExtremity, showPointId, stroke);   
    	}
    }
    
    /**
     * Render a complete path.
     * Lock path.getPoints() to ensure thread safety
     */
    public void render(Graphics2D graphic, IPath path, Color color, boolean showExtremity, boolean showPointId){
    	synchronized(path.getPoints()){
    		render(graphic, path, color, NO_STROKE_SPEC, showExtremity, showPointId, false);
    	}
    }
    
    /**
     * Render a complete path.
     * Lock path.getPoints() to ensure thread safety
     */
    public void render(Graphics2D graphic, IPath path, Color color, boolean showExtremity, boolean showPointId, boolean isDashed){
    	synchronized(path.getPoints()){
    		render(graphic, path, color, NO_STROKE_SPEC, showExtremity, showPointId, isDashed);
    	}
    }
    
    /**
     * Render a complete path.
     * Lock path.getPoints() to ensure thread safety
     */
    public void render(Graphics2D graphic, IPath path, Color color, float width, boolean showExtremity, boolean showPointId){
        synchronized(path.getPoints()){
        	renderFromTo(graphic, path, 0, path.getPoints().size()-1, color, width, showExtremity, showPointId, false); 
        }
    }


    public void render(Graphics2D graphic, IPath path, Color color, float width, boolean showExtremity, boolean showPointId, boolean isDashed){
    	synchronized(path.getPoints()){
    		renderFromTo(graphic, path, 0, path.getPoints().size()-1, color, width, showExtremity, showPointId, isDashed); 
    	}
    }
    
    /**
     * render a path segment.
     * 
     * Lock path.getPoints() to ensure thread safety
     */
    public void renderAfter(Graphics2D graphic, IPath path, Point2D from, boolean including, Color color, float width, boolean showExtremity, boolean showPointId, boolean isDashed){
    	synchronized(path.getPoints()){
	    	int max = path.getPointNumber()-1;
	        int min = 0;
	        
	        int id = path.getIndex(from);
	        if(id==-1)
	            id = min;
	        if(!including)
	            id++;
	        if(id>max)
	            id = max;
	        
	        renderFromTo(graphic, path, id, max, color, width, showExtremity, showPointId, isDashed);   
    	}
    }

    /**
     * render a path segment.
     * 
     * Lock path.getPoints() to ensure thread safety
     */
    public void renderBefore(Graphics2D graphic, IPath path, Point2D from, boolean including, Color color, float width, boolean showExtremity, boolean showPointId, boolean isDashed){
    	synchronized(path.getPoints()){
	    	int max = path.getPointNumber()-1;
	        int min = 0;
	        
	        int id = path.getIndex(from);
	        if(id==-1)
	            id = max;
	        if(!including)
	            id--;
	        if(id<min)
	            id = min;
	        
	        renderFromTo(graphic, path, min, id, color, width, showExtremity, showPointId, isDashed);  
    	}
    }

    /**
     * perform no synchronization
     */
    public void renderFromTo(Graphics2D graphic, IPath path, int from, int to, Color color, float width, boolean showExtremity, boolean showPointId, boolean isDashed){
        boolean didChangeLineStyle = false;
        
        if(path.getPoints()!=null){
            if(width!=NO_STROKE_SPEC){
                if(isDashed)
                    setLineStyleDashed(graphic, width);
                else
                    setLineStyleRound(graphic, width);
                didChangeLineStyle = true;
            }
            
            for (int i = from; i < to; i++) {
                Point2D c1 = path.getPoint(i);
                Point2D c2 = path.getPoint(i+1);
                
                if(c1!=null && c2!=null){ // TODO: should never be null?! a hack for a weird null pointer exception :)
                    // show extremity
                    if(showExtremity && path.isFirst(i)){
                        graphic.setColor(Color.GREEN);
                        drawRectCentered(graphic, c1, 20, 20);
                    }
                    if(showExtremity && path.isLast(i+1)){
                        graphic.setColor(Color.GREEN);
                        drawRectCentered(graphic, c2, 20, 20);
                    }
                    
                    // show segment
                    graphic.setColor(color);
                    graphic.drawLine((int)c1.getX(), (int)c1.getY(), (int)c2.getX(), (int)c2.getY());
                    
                    // show colored id
                    //showPointId = isDashed;
                    //showPointId = true;
                    if(showPointId){
                        if(path.getLock(i))
                            graphic.setColor(LOCKED_POINT_COLOR);
                        else
                            graphic.setColor(UNLOCKED_POINT_COLOR);
                        graphic.drawString(i+"", (int)c1.getX(), (int)c1.getY());
                    }
                }
            }
            
            // show last
            if(path.getPointNumber()>0){
                if(showPointId){
                    int id = to;
                    //if(from<=id && id<to){
                        if(path.getLock(id))
                            graphic.setColor(LOCKED_POINT_COLOR);
                        else
                            graphic.setColor(UNLOCKED_POINT_COLOR);
                        Point2D c = path.getPoint(id);
                        graphic.drawString(id+"", (int)c.getX(), (int)c.getY());
                    //}
                }
            }
            
            if(didChangeLineStyle)
                resetLineStroke(graphic);
        }
    }
        
    public void renderFromTo(Graphics2D graphic, IPath path, int from, int to, Color color, boolean showExtremity, boolean showPointId, Stroke stroke){
        boolean didChangeLineStyle = false;
        
        if(path.getPoints()!=null){
            if(stroke!=null){
                setStroke(graphic, stroke);
            }
                didChangeLineStyle = true;
            }
            
            synchronized(path.getPoints()){
                for (int i = from; i < to; i++) {
                    Point2D c1 = path.getPoint(i);
                    Point2D c2 = path.getPoint(i+1);
                    
                    if(c1!=null && c2!=null){ // TODO: should never be null?! a hack for a weird null pointer exception :)
                        // show extremity
                        if(showExtremity && path.isFirst(i)){
                            graphic.setColor(Color.GREEN);
                            drawRectCentered(graphic, c1, 20, 20);
                        }
                        if(showExtremity && path.isLast(i+1)){
                            graphic.setColor(Color.GREEN);
                            drawRectCentered(graphic, c2, 20, 20);
                        }
                        
                        // show segment
                        graphic.setColor(color);
                        graphic.drawLine((int)c1.getX(), (int)c1.getY(), (int)c2.getX(), (int)c2.getY());
                        
                        // show colored id
                        //showPointId = isDashed;
                        //showPointId = true;
                        if(showPointId){
                            if(path.getLock(i))
                                graphic.setColor(LOCKED_POINT_COLOR);
                            else
                                graphic.setColor(UNLOCKED_POINT_COLOR);
                            graphic.drawString(i+"", (int)c1.getX(), (int)c1.getY());
                        }
                    }
                }
                
                // show last
                if(path.getPointNumber()>0){
                    if(showPointId){
                        int id = to;
                        //if(from<=id && id<to){
                            if(path.getLock(id))
                                graphic.setColor(LOCKED_POINT_COLOR);
                            else
                                graphic.setColor(UNLOCKED_POINT_COLOR);
                            Point2D c = path.getPoint(id);
                            graphic.drawString(id+"", (int)c.getX(), (int)c.getY());
                        //}
                    }
                }
            }
            if(didChangeLineStyle)
                resetLineStroke(graphic);
        }
    /*    if(didChangeLineStyle)
            resetLineStroke(graphic);
    }*/
    
    public static Color LOCKED_POINT_COLOR = Color.PINK;
    public static Color UNLOCKED_POINT_COLOR = Color.BLACK;
}

package org.datagr4m.viewer.renderer.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.jzy3d.maths.Coord2d;

public class CrossRenderer{
    public static int DEFAULT_WIDTH = 10;
    
    protected float width = DEFAULT_WIDTH;
    
    public CrossRenderer(){
        this(DEFAULT_WIDTH);
    }
    
    public CrossRenderer(float width){
        this.width = width;
    }
    
    public void render(Graphics2D g2d, Color color, Coord2d position){
        render(g2d, color, position, width/2);
    }

    public void render(Graphics2D g2d, Color color, Point position){
        render(g2d, color, position, width/2);
    }

    public void render(Graphics2D g2d, Color color, Coord2d position, float width){
        g2d.setColor(color);
        g2d.drawLine((int)(position.x-width), (int)(position.y)      , (int)(position.x+width), (int)(position.y)      ); // xaxis
        g2d.drawLine((int)(position.x)      , (int)(position.y-width), (int)(position.x)      , (int)(position.y+width)); // yaxis      
    }
    
    public void render(Graphics2D g2d, Color color, Point position, float width){
        g2d.setColor(color);
        g2d.drawLine((int)(position.x-width), (position.y)      , (int)(position.x+width), (position.y)      ); // xaxis
        g2d.drawLine((position.x)      , (int)(position.y-width), (position.x)      , (int)(position.y+width)); // yaxis      
    }
}

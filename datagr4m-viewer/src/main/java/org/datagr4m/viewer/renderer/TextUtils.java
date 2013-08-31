package org.datagr4m.viewer.renderer;


import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.jzy3d.maths.IntegerCoord2d;

public class TextUtils {
	public static IntegerCoord2d centeredTextCoords(String text){
		int height = TextUtils.textHeight();
		int width = TextUtils.textWidth(text);
		return new IntegerCoord2d(-width/2, height/2);
	}
	
	public static int textHeight(){
	    if(!initialized)
	        init();
		return height;
	}
	
	public static int textWidth(String txt){
	    if(!initialized)
            init();
		return graphic.getFontMetrics().stringWidth(txt);
	}
	
	public static void changeFontSize(Graphics2D graphic, int size){
	    TextUtils.size = size;
	    TextUtils.font = new Font(name, style, size);
        graphic.setFont(font);
        
    }
	
	public static void changeFontSize(int size){
        TextUtils.size = size;
        TextUtils.font = new Font(name, style, size);
        graphic.setFont(font);
        
    }

   public static void changeFontStyle(int style){
        TextUtils.style = style;
        TextUtils.font = new Font(name, style, size);
        graphic.setFont(font);	        
    }
	
	protected static void init(){
        graphic = (Graphics2D)helperImg.getGraphics();
        graphic.setFont(font);	// there's one by default
        height = graphic.getFontMetrics().getHeight();
	}
	
	protected static boolean initialized = false;
	
	
	public static String name = "Arial";
	public static int style = Font.PLAIN;
	public static int size = 12;
	public static Font font = new Font(name, style, size);
	protected static int height;
	
	protected static BufferedImage helperImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    protected static Graphics2D graphic;
}

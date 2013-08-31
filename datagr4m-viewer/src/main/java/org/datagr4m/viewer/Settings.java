package org.datagr4m.viewer;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Settings {
    public Settings() {
        this(false, true);
    }    
    
    public void apply(Graphics2D g2d){
        if(isAntiAliasing())
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        else
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
    
        if(isAntiAliasingText()){
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        else{
            //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }
    
    public Settings(boolean antiAliasing, boolean antiAliasingText) {
        this.antiAliasing = antiAliasing;
        this.antiAliasingText = antiAliasingText;
    }
    public boolean isAntiAliasing() {
        return antiAliasing;
    }
    public void setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
    }
    public boolean isAntiAliasingText() {
        return antiAliasingText;
    }
    public void setAntiAliasingText(boolean antiAliasingText) {
        this.antiAliasingText = antiAliasingText;
    }
    protected boolean antiAliasing;
    protected boolean antiAliasingText;
}

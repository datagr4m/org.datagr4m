package org.datagr4m.viewer.renderer.annotations;

import java.awt.Color;

import org.datagr4m.viewer.model.annotations.Annotation;
import org.datagr4m.viewer.renderer.CellAnchor;


public interface IAnnotationRendererSettings {
    public Color getTextColor(Annotation annotation);
    public void setTextColor(Annotation annotation, Color textColor);
    public Color getBorderColor(Annotation annotation);
    public void setBorderColor(Annotation annotation, Color borderColor);
    public Color getBodyColor(Annotation annotation);
    public void setBodyColor(Annotation annotation, Color bodyColor);
    public CellAnchor getAnchor(Annotation annotation);
    public void setAnchor(Annotation annotation, CellAnchor anchor);
    public int getBoxMargin(Annotation annotation);
    public void setBoxMargin(Annotation annotation, int boxMargin);
    public int getWidth(Annotation annotation);
    public void setWidth(Annotation annotation, int width);
    public int getHeight(Annotation annotation);
    public void setHeight(Annotation annotation, int height);
    public boolean isDrawAnchor(Annotation annotation);
    public void setDrawAnchor(Annotation annotation, boolean drawAnchor);
}
package org.datagr4m.viewer.renderer.annotations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.datagr4m.viewer.model.annotations.Annotation;
import org.datagr4m.viewer.model.annotations.AnnotationModel;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.CellAnchor;
import org.datagr4m.viewer.renderer.TextUtils;


public class AnnotationRenderer extends AbstractRenderer{
    public AnnotationRenderer(AnnotationModel model) {
        this(model, new AnnotationRendererSettings());
    }
    
    public AnnotationRenderer(AnnotationModel model, IAnnotationRendererSettings settings) {
        this.model = model;
        this.settings = settings;
    }

    @Override
    public void render(Graphics2D graphic){
        //System.out.println("---");
        
        for(Annotation a: model.getAnnotations()){
            String text = a.getText();
            Point2D position = model.getPosition(a);
            //System.out.println("show tooltip: " + text + " " + position);
            
            Color textColor = settings.getTextColor(a);
            Color borderColor = settings.getBorderColor(a);
            Color bodyColor = settings.getBodyColor(a);
            CellAnchor anchor = settings.getAnchor(a);
            int boxMargin = settings.getBoxMargin(a);
            int width = settings.getWidth(a);
            int height = settings.getHeight(a);
            boolean drawAnchor = settings.isDrawAnchor(a);
            
            if(width==AnnotationRendererSettings.AUTO_SIZE)
                width = TextUtils.textWidth(text);
            if(height==AnnotationRendererSettings.AUTO_SIZE)
                height = TextUtils.textHeight();
            
            drawTextCell(graphic, text, position, textColor, borderColor, bodyColor, anchor, boxMargin, width, height, drawAnchor);
        }
    }

    public AnnotationModel getModel() {
        return model;
    }

    public void setModel(AnnotationModel model) {
        this.model = model;
    }
    
    protected AnnotationModel model;
    protected IAnnotationRendererSettings settings;
}

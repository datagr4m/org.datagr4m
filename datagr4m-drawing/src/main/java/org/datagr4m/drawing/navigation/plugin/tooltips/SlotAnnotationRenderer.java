package org.datagr4m.drawing.navigation.plugin.tooltips;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.datagr4m.drawing.model.items.annotations.SlotAnnotation;
import org.datagr4m.viewer.model.annotations.Annotation;
import org.datagr4m.viewer.model.annotations.AnnotationModel;
import org.datagr4m.viewer.renderer.CellAnchor;
import org.datagr4m.viewer.renderer.TextUtils;
import org.datagr4m.viewer.renderer.annotations.AnnotationRenderer;
import org.datagr4m.viewer.renderer.annotations.AnnotationRendererSettings;
import org.datagr4m.viewer.renderer.annotations.IAnnotationRendererSettings;


public class SlotAnnotationRenderer extends AnnotationRenderer{
    public SlotAnnotationRenderer(AnnotationModel model) {
        super(model);
    }
    
    public SlotAnnotationRenderer(AnnotationModel model, IAnnotationRendererSettings settings) {
        super(model, settings);
    }

    @Override
    public void render(Graphics2D graphic){
        if(model!=null)
            for(Annotation a: model.getAnnotations()){
                String text = a.getText();
                if(text==null)
                    text="SlotAnnotationRenderer: annotation text is null";
                Point2D position = model.getPosition(a);
                
                CellAnchor anchor;
                if(a instanceof SlotAnnotation){
                    anchor = ((SlotAnnotation)a).getGuessedAnchor();
                }
                else
                    anchor = settings.getAnchor(a);
                
                Color textColor = settings.getTextColor(a);
                Color borderColor = settings.getBorderColor(a);
                Color bodyColor = settings.getBodyColor(a);
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
    //getGuessedAnchor
}

package org.datagr4m.viewer.renderer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.maths.geometry.PointUtils;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.mouse.IClickableItem;
import org.datagr4m.viewer.renderer.hit.IHitProcessor;
import org.jzy3d.maths.Coord2d;


/** Provide primitives for drawing. */
public abstract class AbstractRenderer implements IRenderer{
    public static boolean ALLOW_ALPHA = false;
    public static int DEFAULT_TEXT_SIZE = 12;

    
    protected IHitProcessor hitProcessor;
    
    /*****/

    @Override
    public IHitProcessor getHitProcessor() {
        return hitProcessor;
    }

    @Override
    public void setHitProcessor(IHitProcessor hitProcessor) {
        this.hitProcessor = hitProcessor;
    }
    
    @Override
    public List<IClickableItem> hit(int x, int y){
        if(hitProcessor!=null)
            return hitProcessor.hit(x, y);
        else
            return null;
    }

    @Override
    public <T> List<IClickableItem> hitOnly(int x, int y, Class<T> type){
        if(hitProcessor!=null)
            return hitProcessor.hitOnly(x, y, type);
        else
            return null;
    }
    
    @Override
    public <T> List<IClickableItem> hitExcluding(int x, int y, Class<T> type){
        if(hitProcessor!=null)
            return hitProcessor.hitExcluding(x, y, type);
        else
            return null;
    }

    /******************/

    public void drawLines(Graphics2D graphic, List<Line2D> lines) {
        for(Line2D line: lines)
            drawLine(graphic, line);
    }

    public void drawLine(Graphics2D graphic, Line2D line) {
        graphic.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
    }
    
    public void drawLine(Graphics2D graphic, Point2D c1, Point2D c2) {
        graphic.drawLine((int) c1.getX(), (int) c1.getY(), (int) c2.getX(), (int) c2.getY());
    }
    
    public void drawLine(Graphics2D graphic, Coord2d c1, Coord2d c2) {
        graphic.drawLine((int) c1.x, (int) c1.y, (int) c2.x, (int) c2.y);
    }
    
    public void drawLine(Graphics2D graphic, Coord2d c1, Point2D c2) {
        graphic.drawLine((int) c1.x, (int) c1.y, (int) c2.getX(), (int) c2.getY());
    }
    
    public void drawLine(Graphics2D graphic, Coord2d c1, Coord2d c2, float xoffset, float yoffset) {
        graphic.drawLine((int) (xoffset + c1.x), (int) (yoffset + c1.y), (int) (xoffset + c2.x), (int) (yoffset + c2.y));
    }
    
    public void drawLine(Graphics2D graphic, Coord2d c1, Coord2d c2, float xoffset, float yoffset, Color color) {
        graphic.setColor(color);
        graphic.drawLine((int) (xoffset + c1.x), (int) (yoffset + c1.y), (int) (xoffset + c2.x), (int) (yoffset + c2.y));
    }

    public void drawLine(Graphics2D graphic, float x1, float y1, float x2, float y2, float xoffset, float yoffset) {
        graphic.drawLine((int) (xoffset + x1), (int) (yoffset + y1), (int) (xoffset + x2), (int) (yoffset + y2));
    }

    public void drawLine(Graphics2D graphic, float x1, float y1, float x2, float y2) {
        graphic.drawLine((int) (x1), (int) (y1), (int) (x2), (int) (y2));
    }
    
    public void drawLine(Graphics2D graphic, double x1, double y1, double x2, double y2) {
        graphic.drawLine((int) (x1), (int) (y1), (int) (x2), (int) (y2));
    }
    
    public void drawScaledLine(Graphics2D graphic, Coord2d c1, Coord2d c2, Coord2d scale, float xoffset, float yoffset) {
        Coord2d cc1 = c1.mul(scale);
        Coord2d cc2 = c2.mul(scale);
        cc1.addSelf(xoffset, yoffset);
        cc2.addSelf(xoffset, yoffset);        
        graphic.drawLine((int)cc1.x, (int)cc1.y, (int)cc2.x, (int)cc2.y);
    }

    public void drawScaledLine(Graphics2D graphic, float x1, float y1, float x2, float y2, Coord2d scale, float xoffset, float yoffset) {
        float xx1 = x1 * scale.x + xoffset;
        float xx2 = x2 * scale.x + xoffset;
        float yy1 = y1 * scale.y + yoffset;
        float yy2 = y2 * scale.y + yoffset;
        graphic.drawLine((int) xx1, (int) yy1, (int) xx2, (int) yy2);
    }
    
    /************* RECT **************/


    public void drawRect(Graphics2D graphic, float x, float y, float width, float height, float xoffset, float yoffset) {
        graphic.drawRect((int) (xoffset + x), (int) (yoffset + y), (int) width, (int) height);
    }
    
    public void drawRect(Graphics2D graphic, float x, float y, float width, float height) {
        graphic.drawRect((int) x, (int) y, (int) width, (int) height);
    }
    
    public void drawRect(Graphics2D graphic, Rectangle2D rect) {
        graphic.drawRect((int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getWidth(), (int)rect.getHeight());
    }
        
    public void drawRectCentered(Graphics2D graphic, float x, float y, float width, float height) {
        graphic.drawRect((int) (x-width/2), (int) (y-height/2), (int) width, (int) height);
    }
    
    public void drawRectCentered(Graphics2D graphic, Point2D center, float width, float height, Color color) {
        graphic.setColor(color);
        graphic.drawRect((int) (center.getX()-width/2), (int) (center.getY()-height/2), (int) width, (int) height);
    }
    
    public void drawRectCentered(Graphics2D graphic, List<Point2D> centers, float width, float height, Color color) {
        graphic.setColor(color);
        for(Point2D center: centers)
            graphic.drawRect((int) (center.getX()-width/2), (int) (center.getY()-height/2), (int) width, (int) height);
    }
    
    public void drawRectCentered(Graphics2D graphic, Point2D center, float width, float height) {
        graphic.drawRect((int) (center.getX()-width/2), (int) (center.getY()-height/2), (int) width, (int) height);
    }

    public void drawRectCentered(Graphics2D graphic, Coord2d center, float width, float height) {
        graphic.drawRect((int) (center.x-width/2), (int) (center.y-height/2), (int) width, (int) height);
    }
    
    public void fillRect(Graphics2D graphic, int x, int y, int width, int height) {
        graphic.fillRect(x, y, width, height);
    }
    
    public void fillRect(Graphics2D graphic, Rectangle2D rect) {
        graphic.fillRect((int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getWidth(), (int)rect.getHeight());
    }

    public void fillRectCentered(Graphics2D graphic, Point2D center, float width, float height, Color color) {
        graphic.setColor(color);
        graphic.fillRect((int) (center.getX()-width/2), (int) (center.getY()-height/2), (int) width, (int) height);
    }
    
    public void fillRectCentered(Graphics2D graphic, Point2D center, float width, float height) {
        graphic.fillRect((int) (center.getX()-width/2), (int) (center.getY()-height/2), (int) width, (int) height);
    }
    
    public void drawScaledRect(Graphics2D graphic, float x, float y, float width, float height, Coord2d scale, float xoffset, float yoffset) {
        float ww = scale.x * width;
        float hh = scale.y * height;
        float xx = scale.x * (x-width/2) + ww/2 + xoffset;
        float yy = scale.y * (y-height/2) + hh/2 + yoffset;
        graphic.drawRect((int)xx, (int)yy, (int)ww, (int)hh);
    }
    
    /*public void drawRect(Graphics2D graphic, Rectangle2D rectangle) {
        graphic.draw(rectangle);
    }

    public void fillRect(Graphics2D graphic, Rectangle2D rectangle) {
        graphic.fill(rectangle);
    }*/
    
    public void drawRectangle(Graphics2D graphic, Rectangle2D rectangle, Color border, Color body) {
        if(body!=null){
            graphic.setPaint(body);
            graphic.fill(rectangle);
        }
        if(border!=null){
            graphic.setPaint(border);
            graphic.draw(rectangle);
        }
    }
    
    public void drawRoundRectangle(Graphics2D graphic, Rectangle2D rectangle, Color border, Color body, int arc) {
        drawRoundRectangle(graphic, rectangle, border, body, arc, arc);
    }    
    
    public void drawRoundRectangle(Graphics2D graphic, Rectangle2D rectangle, Color border, Color body, int arcWidth, int arcHeight) {
        if(body!=null){
            graphic.setPaint(body);
            graphic.fillRoundRect((int)rectangle.getMinX(), (int)rectangle.getMinY(), (int)rectangle.getWidth(), (int)rectangle.getHeight(), arcWidth, arcHeight);
        }
        if(border!=null){
            graphic.setPaint(border);
            graphic.drawRoundRect((int)rectangle.getMinX(), (int)rectangle.getMinY(), (int)rectangle.getWidth(), (int)rectangle.getHeight(), arcWidth, arcHeight);
        }
    }
    
    public void drawRoundRectangleWithGradient(Graphics2D graphic, Rectangle2D rectangle, Color border, Color body, int arc) {
        drawRoundRectangleWithGradient(graphic, rectangle, border, body, arc, arc);
    }  
    
    public void drawRoundRectangleWithGradient(Graphics2D g, Rectangle2D rectangle, Color border, Color body, int arcWidth, int arcHeight) {
        final int x = (int)rectangle.getMinX();
        final int y = (int)rectangle.getMinY();
        final int w = (int)rectangle.getWidth();
        final int h = (int)rectangle.getHeight();
        
        if(body!=null) {
            GradientPaint paint = new GradientPaint(x + 2, y + 2, body, x + w * 2, y + h * 2, body.darker());
            g.setPaint(paint);
            g.fillRoundRect(x, y, w, h, arcWidth, arcHeight);
            g.setPaint(null);
        }
         if(border!=null){
            g.setPaint(border);
            g.drawRoundRect(x, y, w, h, arcWidth, arcHeight);
         }
     }
    
    public Rectangle2D initRectangle(){
        return new Rectangle2D.Double();
    }
    
    public void updateRectangle(Rectangle2D rectangle, Coord2d center, float radius) {
        rectangle.setFrame(center.x - radius, center.y - radius, radius * 2, radius * 2);
    }

    private static final Color COLOR_TRANSPARENT = new Color(255, 255, 255, 0);
    /**
     * Paints a "glow" around the inner edges of the given rectangle.
     * The glow effect is simply a gradient transition from a partially
     * translucent color to a fully translucent color - all around the inner
     * edges of the specified rectangle.
     *
     * @param graphics         the graphics object to paint on
     * @param bounds           the rectangle
     * @param baseGlowColor    the base color to base the glow off of (alpha channel ignored)
     * @param transitionForce  a floating point number between 0.0f and 1.0f reflecting the strength of the glow transition
     * @param glowSize         glow width, in pixels
     */
    public void paintInnerGlowBorder(Graphics2D graphics, Rectangle2D bounds, Color baseGlowColor, int arc) {
        final float transitionForce = 0.5f;
        int glowSize = 5;

        Color glowColor = new Color(
                baseGlowColor.getRed(),
                baseGlowColor.getGreen(),
                baseGlowColor.getBlue(),
                (int) (transitionForce * 255.0f));

        final int x = (int)bounds.getMinX();
        final int y = (int)bounds.getMinY();
        final int width = (int)bounds.getWidth();
        final int height = (int)bounds.getHeight();

        // top
        graphics.setPaint(new GradientPaint(x, y, glowColor, x, y + glowSize, COLOR_TRANSPARENT));
        graphics.fill(new Rectangle(x, y, x + width, glowSize));

        // bottom
        graphics.setPaint(new GradientPaint(x, y + height, glowColor, x, (y + height) - glowSize, COLOR_TRANSPARENT));
        graphics.fill(new Rectangle(x, (y + height) - glowSize, x + width, glowSize));

        // left
        graphics.setPaint(new GradientPaint(x, y, glowColor, x + glowSize, y, COLOR_TRANSPARENT));
        graphics.fill(new Rectangle(x, y, x + glowSize, y + height));

        // right
        graphics.setPaint(new GradientPaint(x + width, y, glowColor, (x + width) - glowSize, y, COLOR_TRANSPARENT));
        graphics.fill(new Rectangle((x + width) - glowSize, y, glowSize, y + height));
    }

    public void paintOutterGlowRectangle(Graphics2D graphic, Rectangle2D rectangle, Color border, int arc) {
        final Color[] focusArray = new Color[] {
            new Color(border.getRed(), border.getGreen(), border.getBlue(), 120),
            new Color(border.getRed(), border.getGreen(), border.getBlue(), 100),
            new Color(border.getRed(), border.getGreen(), border.getBlue(), 60),
            new Color(border.getRed(), border.getGreen(), border.getBlue(), 30),
            new Color(border.getRed(), border.getGreen(), border.getBlue(), 10)
            };
        final float[] strokeArray = new float[] {
            1.f, 2.f, 3.f, 4.f, 5.f
        };

        for (int i = 0; i < 5; ++i) {
            final float stroke = strokeArray[i];
            graphic.setColor(focusArray[i]);
            graphic.setStroke(new BasicStroke(stroke));
            graphic.drawRoundRect((int)rectangle.getMinX() - (int)stroke,
                                  (int)rectangle.getMinY() - (int)stroke,
                                  (int)rectangle.getWidth() + 2 * (int)stroke,
                                  (int)rectangle.getHeight() + 2 * (int)stroke, arc + i, arc + i);
        }
    }
    
    /************ CIRCLE ***************/
    
    public void drawCircles(Graphics2D graphic, List<Point2D> points, float radius, Color color){
        for (Point2D p: points) {
            drawCircle(graphic, p, radius, color, null);
        }
    }
    
    public void drawCircle(Graphics2D graphic, Coord2d center, float radius, float xoffset, float yoffset, Color border, Color body) {
        Ellipse2D circle = buildCircle(center, radius, xoffset, yoffset);
        drawCircle(graphic, circle, border, body);
    }
    
    public void drawCircle(Graphics2D graphic, Coord2d center, float radius, Color border, Color body) {
        Ellipse2D circle = buildCircle(center, radius);
        drawCircle(graphic, circle, border, body);
    }

    public void drawCircle(Graphics2D graphic, Point2D center, float radius, Color border, Color body) {
        Ellipse2D circle = buildCircle(center, radius);
        drawCircle(graphic, circle, border, body);
    }
    
    public void drawScaledCircle(Graphics2D graphic, Coord2d center, float radius, Coord2d scale, float xoffset, float yoffset, Color border, Color body) {
        Ellipse2D circle = buildScaledCircle(center, radius, scale, xoffset, yoffset);
        drawCircle(graphic, circle, border, body);
    }
    
    public void drawCircle(Graphics2D graphic, Ellipse2D circle, Color border, Color body) {
        if(body!=null){
            graphic.setPaint(body);
            graphic.fill(circle);
        }
        if(border!=null){
            graphic.setPaint(border);
            graphic.draw(circle);
        }
    }
    
    public void drawImage(Graphics2D graphic, Image image, Rectangle2D rect) {
        if(image!=null){
            int cellWidth = image.getWidth(null);
            int cellHeight = image.getHeight(null);
            graphic.drawImage(image, 
                              (int)rect.getMinX(), (int)rect.getMinY(), 
                              (int)rect.getMaxX(), (int)rect.getMaxY(), 
                               0, 0, cellWidth, cellHeight, null);
        }
    }

    public Ellipse2D buildCircle(Coord2d center, float radius, float xoffset, float yoffset) {
        return new Ellipse2D.Float(xoffset + center.x - radius, yoffset + center.y - radius, radius * 2, radius * 2);
    }
    
    public Ellipse2D buildCircle(Coord2d center, float radius) {
        return new Ellipse2D.Float(center.x - radius, center.y - radius, radius * 2, radius * 2);
    }
    
    public Ellipse2D buildCircle(Point2D center, float radius) {
        return new Ellipse2D.Float((float)center.getX() - radius, (float)center.getY() - radius, radius * 2, radius * 2);
    }
    
    public Ellipse2D buildScaledCircle(Coord2d center, float radius, Coord2d scale, float xoffset, float yoffset) {
        float x = scale.x * center.x - radius * scale.x;
        float y = scale.y * center.y - radius * scale.y;
        float r = scale.x * radius * 2;
        return new Ellipse2D.Float(xoffset + x, yoffset + y, r, r);
    }
    
    public void updateCircle(Ellipse2D ellipse, Coord2d center, float radius) {
        ellipse.setFrame(center.x - radius, center.y - radius, radius * 2, radius * 2);
    }
    
    public Ellipse2D initCircle(){
        return new Ellipse2D.Double();
    }

    /************* TEXT **************/
    
    public void drawText(Graphics2D graphic, String text) {
        graphic.drawString(text, 0, 0);
    }

    public void drawText(Graphics2D graphic, String text, Coord2d position) {
        graphic.drawString(text, (int)position.x, (int)position.y);
    }
    
    public void drawText(Graphics2D graphic, String text, Coord2d position, boolean rightAlign) {
        if(!rightAlign)
            drawText(graphic, text, position, 0);
        else
            drawText(graphic, text, position, -TextUtils.textWidth(text));
    }
    
    public void drawText(Graphics2D graphic, String text, Coord2d position, float xoffset) {
        graphic.drawString(text, (int)position.x+xoffset, (int)position.y);
    }
    
    public void drawText(Graphics2D graphic, String text, Coord2d position, float xoffset, Color back) {
        Color old = graphic.getColor();
        graphic.setColor(back);
        graphic.fillRect((int)(position.x+xoffset), (int)position.y, TextUtils.textWidth(text), -TextUtils.textHeight());
        graphic.setColor(old);
        graphic.drawString(text, (int)position.x+xoffset, (int)position.y);
    }

    public void drawText(Graphics2D graphic, String text, int x, int y) {
        graphic.drawString(text, x, y);
    }
    
    public void drawText(Graphics2D graphic, String text, Point2D position) {
        graphic.drawString(text, (int)position.getX(), (int)position.getY());
    }
    
    public void drawTextShifted(Graphics2D graphic, String text, Point2D position, float xShift) {
        graphic.drawString(text, (int)(position.getX()-xShift), (int)position.getY());
    }
    
    public void drawTextCentered(Graphics2D graphic, String text, Point2D position) {
        graphic.drawString(text, (int)position.getX()-TextUtils.textWidth(text)/2, (int)position.getY());
    }
    
    /** with cell */
    
    public static int DEFAULT_TXT_CELL_MARGIN = 10;

    public void drawTextCell(Graphics2D graphic, String text, Coord2d position) {
        drawTextCell(graphic, text, Pt.cloneAsDoublePoint(position), Color.BLACK, Color.BLACK, Color.WHITE, CellAnchor.CENTER, DEFAULT_TXT_CELL_MARGIN); 
    }

    public void drawTextCell(Graphics2D graphic, String text, Coord2d position, Color textColor, Color borderColor, Color bodyColor) {
        drawTextCell(graphic, text, Pt.cloneAsDoublePoint(position), textColor, borderColor, bodyColor, CellAnchor.CENTER, DEFAULT_TXT_CELL_MARGIN); 
    }
    
    public void drawTextCell(Graphics2D graphic, String text, Point2D position, Color textColor, Color borderColor, Color bodyColor) {
        drawTextCell(graphic, text, position, textColor, borderColor, bodyColor, CellAnchor.CENTER, DEFAULT_TXT_CELL_MARGIN); 
    }
    
    public void drawTextCell(Graphics2D graphic, String text, Point2D position, Color textColor, Color borderColor, Color bodyColor, CellAnchor anchor) {
        drawTextCell(graphic, text, position, textColor, borderColor, bodyColor, anchor, DEFAULT_TXT_CELL_MARGIN);
        
    }
    public void drawTextCell(Graphics2D graphic, String text, Point2D position, Color textColor, Color borderColor, Color bodyColor, CellAnchor anchor, int boxMargin) {
        drawTextCell(graphic, text, position, textColor, borderColor, bodyColor, anchor, boxMargin, true);
    }
    
   public void drawTextCell(Graphics2D graphic, String text, Point2D position, Color textColor, Color borderColor, Color bodyColor, CellAnchor anchor, int boxMargin, boolean drawAnchor) {
        int width = TextUtils.textWidth(text);
        int height = TextUtils.textHeight();
        drawTextCellMargin(graphic, text, position, textColor, borderColor, bodyColor, anchor, boxMargin, width, height, drawAnchor);
    } 
    
    public void drawTextCell(Graphics2D graphic, String text, Point2D position, Color textColor, Color borderColor, Color bodyColor, CellAnchor anchor, int boxMargin, int width, int height, boolean drawAnchor) {
        int x;
        int y;
        
        if(anchor.equals(CellAnchor.CENTER)){
            x = (int)position.getX()-width/2;
            y = (int)position.getY()+height/2;
        }
        else if(anchor.equals(CellAnchor.TOP_LEFT)){
            x = (int)position.getX();
            y = (int)position.getY()+height;
        }
        else if(anchor.equals(CellAnchor.TOP_RIGHT)){
            x = (int)position.getX()-width;
            y = (int)position.getY()+height;
        }
        else if(anchor.equals(CellAnchor.BOTTOM_LEFT)){
            x = (int)position.getX();
            y = (int)position.getY();
        }
        else if(anchor.equals(CellAnchor.BOTTOM_RIGHT)){
            x = (int)position.getX()-width;
            y = (int)position.getY();
        }
        else if(anchor.equals(CellAnchor.MIDDLE_LEFT)){
            x = (int)position.getX();
            y = (int)position.getY()+height/2;
        }
        else if(anchor.equals(CellAnchor.MIDDLE_RIGHT)){
            x = (int)position.getX()-width;
            y = (int)position.getY()+height/2;
        }
        else if(anchor.equals(CellAnchor.MIDDLE_TOP)){
            x = (int)position.getX()-width/2;
            y = (int)position.getY()+height;
        }
        else if(anchor.equals(CellAnchor.MIDDLE_BOTTOM)){
            x = (int)position.getX()-width/2;
            y = (int)position.getY();
        }
        else
            throw new RuntimeException("anchor not supported: " + anchor);
        
        int xbox = x-boxMargin;
        int ybox = y-boxMargin-height;
        
        int corner = 5;
        boolean rounded = true;
        
        //  border
                 
        GradientPaint paint = new GradientPaint(xbox + 2, ybox + 2, bodyColor, xbox + width, ybox + height, enlight(bodyColor, 0.8));
        graphic.setPaint(paint);
        
        //graphic.setPaint(bodyColor);
        if(rounded)
            graphic.fillRoundRect(xbox, ybox, width+boxMargin*2, height+boxMargin*2, corner, corner);
        else
            graphic.fillRect(xbox, ybox, width+boxMargin*2, height+boxMargin*2);
        
        graphic.setPaint(borderColor);
        if(rounded)
            graphic.drawRoundRect(xbox, ybox, width+boxMargin*2, height+boxMargin*2, corner, corner);
        else
            graphic.drawRect(xbox, ybox, width+boxMargin*2, height+boxMargin*2);
        
        // label
        graphic.setColor(textColor);
        graphic.drawString(text, x, y);
        
        // attach point
        if(drawAnchor)
            if(!anchor.equals(CellAnchor.CENTER)){
                graphic.setPaint(borderColor);
                float attachWidth = 6;
                graphic.fillOval((int)(position.getX()-attachWidth/2), (int)(position.getY()-attachWidth/2), (int)attachWidth, (int)attachWidth); // un point d'attache
            }
    }

    
    public void drawTextCellMargin(Graphics2D graphic, String text, Point2D position, Color textColor, Color borderColor, Color bodyColor, CellAnchor anchor, int boxMargin, int width, int height, boolean drawAnchor) {
        int x;
        int y;
        
        if(anchor.equals(CellAnchor.CENTER)){
            x = (int)position.getX()-width/2;
            y = (int)position.getY()+height/2;
        }
        else if(anchor.equals(CellAnchor.TOP_LEFT)){
            x = (int)position.getX();
            y = (int)position.getY()+height;
        }
        else if(anchor.equals(CellAnchor.TOP_RIGHT)){
            x = (int)position.getX()-width;
            y = (int)position.getY()+height;
        }
        else if(anchor.equals(CellAnchor.BOTTOM_LEFT)){
            x = (int)position.getX();
            y = (int)position.getY();
        }
        else if(anchor.equals(CellAnchor.BOTTOM_RIGHT)){
            x = (int)position.getX()-width;
            y = (int)position.getY();
        }
        else if(anchor.equals(CellAnchor.MIDDLE_LEFT)){
            x = (int)position.getX();
            y = (int)position.getY()+height/2;
        }
        else if(anchor.equals(CellAnchor.MIDDLE_RIGHT)){
            x = (int)position.getX()-width;
            y = (int)position.getY()+height/2;
        }
        else if(anchor.equals(CellAnchor.MIDDLE_TOP)){
            x = (int)position.getX()-width/2;
            y = (int)position.getY()+height;
        }
        else if(anchor.equals(CellAnchor.MIDDLE_BOTTOM)){
            x = (int)position.getX()-width/2;
            y = (int)position.getY();
        }
        else
            throw new RuntimeException("anchor not supported: " + anchor);
        
        final int corner = 10;
        final int xmargin = boxMargin;
        final int ymargin = boxMargin / 2;
        
        int xbox = x - xmargin / 2;
        int ybox = y - ymargin / 3 - height;

        boolean rounded = true;
        
        //  border
                 
        GradientPaint paint = makeGradientVertical(bodyColor, width, height, xmargin, ymargin, xbox, ybox);
        graphic.setPaint(paint);
        
        //graphic.setPaint(bodyColor);
        if(rounded)
            graphic.fillRoundRect(xbox, ybox, width+xmargin, height+ymargin, corner, corner);
        else
            graphic.fillRect(xbox, ybox, width+xmargin, height+ymargin);
        
        graphic.setPaint(borderColor);
        if(rounded)
            graphic.drawRoundRect(xbox, ybox, width+xmargin, height+ymargin, corner, corner);
        else
            graphic.drawRect(xbox, ybox, width+xmargin, height+ymargin);
        
        // label
        graphic.setColor(textColor);
        graphic.drawString(text, x, y);
        
        // attach point
        if(drawAnchor)
            if(!anchor.equals(CellAnchor.CENTER)){
                graphic.setPaint(borderColor);
                float attachWidth = 6;
                graphic.fillOval((int)(position.getX()-attachWidth/2), (int)(position.getY()-attachWidth/2), (int)attachWidth, (int)attachWidth); // un point d'attache
            }
    }

    public GradientPaint makeGradientDiagonal(Color bodyColor, int width, int height, final int xmargin, final int ymargin, int xbox, int ybox) {
        return new GradientPaint(xbox + 2, ybox + 2, bodyColor, xbox + width + xmargin, ybox + height + ymargin, enlight(bodyColor, 0.8));
    }
    
    public GradientPaint makeGradientVertical(Color bodyColor, int width, int height, final int xmargin, final int ymargin, int xbox, int ybox) {
        return new GradientPaint(xbox, ybox + 2, bodyColor, xbox, ybox + height + ymargin, enlight(bodyColor, 0.8));
    }

    
    /** rotated text */
    
    public void drawTextRotated(Graphics2D graphic, String str, Point2D position, double angle){
        drawTextRotated(graphic, str, position, angle, false);
    }
    public void drawTextRotated(Graphics2D graphic, String str, Coord2d position, double angle){
        drawTextRotated(graphic, str, Pt.cloneAsDoublePoint(position), angle, false);
    }

    public void drawTextRotated(Graphics2D graphic, String str, Point2D position, double angle, Color background){
        drawTextRotated(graphic, str, position, angle, false, Color.BLACK, background);
    }
    public void drawTextRotated(Graphics2D graphic, String str, Coord2d position, double angle, Color background){
        drawTextRotated(graphic, str, Pt.cloneAsDoublePoint(position), angle, false, Color.BLACK, background);
    }

    public void drawTextRotated(Graphics2D graphic, String str, Coord2d position, double angle, Color txtColor, Color background){
        drawTextRotated(graphic, str, Pt.cloneAsDoublePoint(position), angle, false, txtColor, background);
    }

    public void drawTextRotated(Graphics2D graphic, String str, Point2D position, double angle, boolean rightAlign){
        drawTextRotated(graphic, str, position, angle, rightAlign, Color.BLACK, null);
    }

    public void drawTextRotated(Graphics2D graphic, String str, Coord2d position, double angle, boolean rightAlign, Color txtColor, Color background){
        drawTextRotated(graphic, str, Pt.cloneAsDoublePoint(position), angle, rightAlign, txtColor, background);
    }

    
    public void drawTextRotated(Graphics2D graphic, String str, Point2D position, double angle, boolean rightAlign, Color txt, Color background){
        AffineTransform old = graphic.getTransform();
        
        /*if(!rightAlign){
            AffineTransform rot = AffineTransform.getRotateInstance(angle);
            AffineTransform pos = AffineTransform.getTranslateInstance(position.getX(), position.getY());
            pos.concatenate(rot); // will append to current graphic transform p'=pos(rot(p))
            graphic.transform(pos);
        }
        else{
            AffineTransform rot = AffineTransform.getRotateInstance(angle);
            AffineTransform pos = AffineTransform.getTranslateInstance(position.getX(), position.getY());
            rot.concatenate(AffineTransform.getTranslateInstance(-TextUtils.textWidth(str), 0));
            pos.concatenate(rot); // will append to current graphic transform pos(rot(p))
            graphic.transform(pos);
        } */       
        /*boolean unscaled = true;
        if(unscaled){
            pushLayer0Transform(graphic);
            pullDefaultTransform(graphic);
        }*/

        AffineTransform rot = AffineTransform.getRotateInstance(angle);
        AffineTransform pos = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        if(rightAlign)
            rot.concatenate(AffineTransform.getTranslateInstance(-TextUtils.textWidth(str), 0));
        pos.concatenate(rot); // will append to current graphic transform pos(rot(p))
        graphic.transform(pos);
        
        //Color bg = Color.WHITE;
        if(background!=null){
            graphic.setColor(background);
            int height = TextUtils.textHeight();
            fillRect(graphic, 0, -height, TextUtils.textWidth(str), height);
        }
        
        if(txt!=null)
            graphic.setColor(txt);
        drawText(graphic, str);
        
        //if(unscaled)
        //    pushLayer0Transform(graphic);
        
        
        //drawUnscaledText(graphic, str, new Point2D.Float());
        graphic.setTransform(old);
        
    }
    
    public void drawTextOnSegment(Graphics2D graphic, String str, Point2D ptAt, Point2D ptToward){
        boolean rightAlign = false;
        if(ptAt.getX()>ptToward.getX())
            rightAlign = true;
        double angle = PointUtils.angle(ptAt, ptToward);
        drawTextRotated(graphic, str, ptAt, angle, rightAlign); 
    }
    
    public void drawTextCentered(Graphics2D graphic, String text, Coord2d position) {
        graphic.drawString(text, (int)position.x-TextUtils.textWidth(text)/2, (int)position.y);
    }
    
    public void drawUnscaledText(Graphics2D graphic, String text, Coord2d position){
        drawUnscaledText(graphic, text, new Point2D.Float(position.x, position.y));
    }
    
    public void drawUnscaledText(Graphics2D graphic, String text, Point2D position){
        Point2D screen = new Point2D.Float();
        //try {
            graphic.getTransform().transform(position, screen);
        /*} catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }*/
        pushLayer0Transform(graphic);
        
        pullDefaultTransform(graphic);
        drawText(graphic, text, screen);
        pullLayer0Transform(graphic);
    }
    
    public void drawUnscaledTextCell(Graphics2D graphic, String text, Coord2d position, Color body, Color back){
        drawUnscaledTextCell(graphic, text, Pt.cloneAsDoublePoint(position), body, back);
    }
    
    public void drawUnscaledTextCell(Graphics2D graphic, String text, Point2D position, Color body, Color back){
        Point2D screen = new Point2D.Float();
        //try {
            graphic.getTransform().transform(position, screen);
        /*} catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }*/
        pushLayer0Transform(graphic);
        
        pullDefaultTransform(graphic);
        //pullNoTransform(graphic);
        drawTextCell(graphic, text, screen, body, body, back);
        pullLayer0Transform(graphic);
    }
    
    public void drawScaledText(Graphics2D graphic, String text, Coord2d c1, Coord2d scale, float xoffset, float yoffset) {
        Coord2d cc1 = c1.mul(scale);
        cc1.addSelf(xoffset, yoffset);
        graphic.drawString(text, (int)cc1.x, (int)cc1.y);
    }
    
    /************** ALPHA ****************/
    
    protected float lastAlphaSetting = 1;
    
    public void applyAlpha(Graphics2D graphic, float alpha){
        lastAlphaSetting = alpha;
        if(ALLOW_ALPHA)
            graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }
    
    public float getLastAlphaSetting() {
        return lastAlphaSetting;
    }

    public void resetAlpha(Graphics2D graphic){
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    public void backAlpha(Graphics2D graphic){
        if(ALLOW_ALPHA)
            graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, lastAlphaSetting));
    }

    public void applyFusion(Graphics2D graphic, float alpha){//SRC_ATOP
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
    }
    
    /*************** STROKE ***************/
    
    public void resetLineWidth(Graphics2D graphic){
        lastStroke = graphic.getStroke();
        graphic.setStroke(new BasicStroke(1));
    }
    
    public void setLineWidth(Graphics2D graphic, float width){
        lastStroke = graphic.getStroke();
        graphic.setStroke(new BasicStroke(width));
    }

    public void setStroke(Graphics2D graphic, Stroke stroke){
        lastStroke = graphic.getStroke();
        graphic.setStroke(stroke);
    }

    
    public void setLineStyleRound(Graphics2D graphic, float width){
        lastStroke = graphic.getStroke();
        graphic.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }
    
    public void setLineStyleDashed(Graphics2D graphic, float width){
        lastStroke = graphic.getStroke();
        graphic.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
    }
    
    float dash[] = { 10.0f };
    
    public void resetLineStroke(Graphics2D graphic){
        graphic.setStroke(lastStroke);
    }
    
    protected Stroke lastStroke;
    
    /*************** BACKUP TRANSFORM ***************/

    public static void pushDefaultTransform(Graphics2D g2d){
        defaultTransformBackup = g2d.getTransform();
    }
    
    public static void pullDefaultTransform(Graphics2D g2d){
        g2d.setTransform(defaultTransformBackup);
    }
    
    public static void pushLayer0Transform(Graphics2D g2d){
        layer0TransformBackup = g2d.getTransform();
    }
    
    public static void pullLayer0Transform(Graphics2D g2d){
        g2d.setTransform(layer0TransformBackup);
    }
    
    public static void pullNoTransform(Graphics2D g2d){
        g2d.setTransform(noTransformBackup);
    }
    
    protected static AffineTransform defaultTransformBackup;
    protected static AffineTransform layer0TransformBackup;
    protected static AffineTransform noTransformBackup = AffineTransform.getTranslateInstance(0, 0);
    /*****************/
    
    public static List<IClickableItem> listClick(IClickableItem item){
        List<IClickableItem> list = new ArrayList<IClickableItem>(1);
        list.add(item);
        return list;
    }
    
    public static List<IRenderer> listRenderer(IRenderer renderer){
        List<IRenderer> list = new ArrayList<IRenderer>(1);
        list.add(renderer);
        return list;
    }
    
    static private Color enlight(final Color color, final double factor) {
               return new Color(Math.max((int)(color.getRed() * factor), 0), 
                        Math.max((int)(color.getGreen() * factor), 0),
                        Math.max((int)(color.getBlue() * factor), 0));
               }

}
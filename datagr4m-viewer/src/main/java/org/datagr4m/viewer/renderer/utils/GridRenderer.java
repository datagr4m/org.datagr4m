package org.datagr4m.viewer.renderer.utils;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.renderer.DefaultRenderer;


public class GridRenderer extends DefaultRenderer{
    public static boolean USE_VIEW_BOUNDS = true;
    public GridRenderer(IView view) {
        this.view = view;
        this.n    = 100;
    }
    
    protected static int range = 100000;
    
    public void render(Graphics2D graphics, GridRendererSettings settings){
        if(settings.isDisplayed()){
            int xmin = -range;
            int xmax = +range;
            int ymin = -range;
            int ymax = +range;
            
            if(USE_VIEW_BOUNDS){
                Rectangle2D bounds = view.getViewBounds();
                
                xmin = (int)bounds.getMinX();
                xmax = (int)bounds.getMaxX();
                ymin = (int)bounds.getMinY();
                ymax = (int)bounds.getMaxY();
            }
            
            graphics.setColor(settings.getGridColor());
            
            /*for (int i=-n; i<=n; i++) {
                graphics.drawLine(xmin, i*settings.getSpacing(), xmax, i*settings.getSpacing());
                graphics.drawLine(i*settings.getSpacing(), ymin, i*settings.getSpacing(), ymax);
            }*/
            
            int step = settings.getSpacing();
            for (int i=xmin; i<=xmax; i+=step) {
                graphics.drawLine(i, ymin, i, ymax);
            }
            for (int i=ymin; i<=ymax; i+=step) {
                graphics.drawLine(xmin, i, xmax, i);
            }
            
            if(settings.isGridValuesDisplayed()){
                Rectangle2D bounds = view.getViewBounds();
                
                if(!USE_VIEW_BOUNDS){
                    xmin = (int)bounds.getMinX();
                    xmax = (int)bounds.getMaxX();
                    ymin = (int)bounds.getMinY();
                    ymax = (int)bounds.getMaxY();
                }
                
                graphics.setColor(settings.getGridValuesColor());
                
                for (int xi=-n; xi<=n; xi++){//=settings.getMajorTicks()) {
                    int x = xi*settings.getSpacing();
                    if(xmin<=x && x<=xmax)
                        drawText(graphics, ""+x, x, ymax);
                }
                
                for (int yi=-n; yi<=n; yi++){//=settings.getMajorTicks()) {
                    int y = yi*settings.getSpacing();
                    if(ymin<=y && y<=ymax)
                        drawText(graphics, ""+y, xmin, y);
                }
            }
        }
    }
    
    protected int n;
    protected int max;
    protected IView view;
}

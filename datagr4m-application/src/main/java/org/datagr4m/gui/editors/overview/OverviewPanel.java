package org.datagr4m.gui.editors.overview;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.listeners.IItemListener;
import org.datagr4m.drawing.model.items.listeners.ScheduledItemListener;
import org.datagr4m.viewer.Display;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.IView;
import org.datagr4m.viewer.IViewListener;
import org.datagr4m.viewer.ImagePanel;


public class OverviewPanel extends ImagePanel {
    private static final long serialVersionUID = -7250045769897925312L;

    public OverviewPanel(Image img) {
        super(img);
    }

    public OverviewPanel(String img) {
        super(img);
    }

    // TODO: FIXME <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    /*public void paintComponent(Graphics g) {
        if (simg != null)
            g.drawImage(simg, 0, 0, null);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        if (scaledView != null) {
            g.setColor(Color.RED);
            g.drawRect((int) scaledView.getMinX(), (int) scaledView.getMinY(), (int) scaledView.getWidth(), (int) scaledView.getHeight());
        }
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, (int)swidth-1, (int)sheight-1);
        // g.draw
    }*/

    public void register(final Display display) {
        this.display = display;
        display.addComponentListener(new ComponentAdapter(){
            @Override
			public void componentResized(ComponentEvent e){
                compute();
            }
        });
        /*
         * display.addListeners(new IRepaintListener() {
         * 
         * @Override public void repaint() { final BufferedImage i =
         * display.screenshot(); if(i!=null) setImg(i); } });
         */
    }

    public void register(IView view) {
        this.view = view;
        view.addListener(new IViewListener() {
            @Override
            public void boundChanged(Rectangle2D bounds) {
                //viewBounds = bounds;
                compute();
            }
        });
    }

    public void register(final IHierarchicalModel model) {
        listener = new ScheduledItemListener();
        listener.addItemListener(new IItemListener() {
            @Override
            public void itemBoundsChanged(final IBoundedItem item) {
                modelBounds = model.getRawRectangleBounds().cloneAsRectangle2D();
                
                // System.out.println("upd");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateImage(item);
                    }
                });
            }

            @Override
            public void itemPositionChanged(IBoundedItem item) {
                // TODO Auto-generated method stub
                
            }
        });
        listener.listen(model);
    }

    /*********/

    protected void compute(){
        viewBounds = view.getViewBounds();
        double xmin = viewBounds.getMinX();// *wscale; // TODO: fixme
        double ymin = viewBounds.getMinY();// *hscale;
//System.out.println(viewBounds);
        if(screenShotBounds!=null){
            Point2D left = project(xmin, ymin);
            Point2D right = project(xmin + viewBounds.getWidth(), ymin + viewBounds.getHeight());
            scaledView = new Rectangle2D.Double(left.getX(), left.getY(), right.getX() - left.getX(), right.getY() - left.getY());
        }
    }
    
    protected Point2D project(double x, double y) {
        if (screenShotBounds != null) {
            double v11x = screenShotBounds.getMinX();
            double v11y = screenShotBounds.getMinY();
            double v12x = v11x + screenShotBounds.getWidth();
            double v12y = v11y + screenShotBounds.getHeight();
            double v22x = 0;// was: swidth;// TODO: FIX ME <<<<<<<<<<<<<<<<<<<<
            double v22y = 0;// was: sheight;// // TODO: FIX ME <<<<<<<<<<<<<<<<<<<<

            // if(x<v11x||x>v12x||y<v11y||y>v12y)
            // throw new IllegalArgumentException("out of bounds");

            double px = v22x * (x - v11x) / (v12x - v11x);
            double py = v22y * (y - v11y) / (v12y - v11y);

            return new Point2D.Double(px, py);
        }
        return null;
    }

    public void updateOverview() {
        updateImage(prevItem);
    }
    
    protected void updateImage(final IBoundedItem model) {
        if (display != null && view != null && model != null) {
            view.fit(modelBounds);
            screenShotBounds = view.getViewBounds(true);
            final BufferedImage i = display.screenshot();
            if (i != null) {
                // was setImg(i)   TODO: FIXME       <<<<<<<<<<<<<<<<<<<<<<
                repaint();
            }
            view.fit(viewBounds);

            prevItem = model;
        }
    }
    
    /*****/

    public IDisplay getDisplay() {
        return display;
    }

    /*****/

    protected Rectangle2D scaledView;
    protected ScheduledItemListener listener;

    protected IDisplay display;
    protected IView view;
    protected Rectangle2D viewBounds;
    protected Rectangle2D modelBounds;
    protected Rectangle2D currentViewBounds;
    protected Rectangle2D screenShotBounds;
    protected IBoundedItem prevItem;
}

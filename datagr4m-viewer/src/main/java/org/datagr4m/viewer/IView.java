package org.datagr4m.viewer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.utils.GridRendererSettings;
import org.jzy3d.maths.Coord2d;

import edu.uci.ics.jung.visualization.transform.MutableAffineTransformer;

public interface IView {

	public Point2D getLastScaleShift();

	public Point2D getLastScaleSet();

	public void refresh();

	/******** VIEW BOUNDS IN LAYOUT *******/

	public Rectangle2D getViewBounds();

	/** A rectangle that indicates the viewable rectangle in the user space.*/
	public Rectangle2D getViewBounds(boolean update);

	public void updateViewBounds();

	public Point2D getViewPoint();

	/** A rectangle that indicates the viewable rectangle in the user space.*/
	public Point2D getViewPoint(boolean update);

	public void updateViewPoint();

	public boolean isDisplayed(Point2D point);

	public boolean isDisplayed(Rectangle2D point);

	/******** FIT A GIVEN LAYOUT BOUNDARY *******/

	public void fit(Rectangle2D rect);

	/** Make the view auto pan & scale so that the provided rectangle region is shown.*/
	public void fit(Rectangle2D rect, boolean refreshNow);

	public void centerAt(Coord2d position, float radius);

	public void centerAt(Coord2d position, float radius, float scale);

	public void centerAt(Coord2d position);

	public Point2D getCenter();

	public Coord2d getLayoutScale(Rectangle2D r);

	public void render(Graphics2D g2d);

	public void modelToScreen(Point2D p);

	/** Converts absolute mouse coordinates to coordinates relative
	 * to the rendered layout.*/
	public void screenToModel(Point2D p);

	public Shape screenToModel(Shape shape);

	public Coord2d screenToModel(Coord2d c);

	public void scale(float xRatio, float yRatio);

	public void scale(float xRatio, float yRatio, boolean refreshNow);

	public void scale(float xRatio, float yRatio, Point2D from);

	public void scale(float xRatio, float yRatio, Point2D from,
			boolean refreshNow);

	public void setScale(float xRatio, float yRatio);

	public void setScale(float xRatio, float yRatio, boolean refreshNow);

	public void setScale(float xRatio, float yRatio, Point2D from);

	public void setScale(float xRatio, float yRatio, Point2D from,
			boolean refreshNow);

	/*public void getScale(){
	    return viewTransformer.getScale();
	}*/
	public void pan(float dx, float dy);

	public void pan(float dx, float dy, boolean refreshNow);

	/*************/

	public IRenderer getRenderer();

	public void setRenderer(IRenderer renderer);

	public boolean isShowCenterCross();

	public void setShowCenterCross(boolean showCenterCross);

	public boolean isShowLastClickCross();

	public void setShowLastClickCross(boolean showLastClickCross);

	public GridRendererSettings getGridRendererSettings();

	public void setGridRendererSettings(
			GridRendererSettings gridRendererSettings);

	public Color getBackgroundColor();

	public void setBackgroundColor(Color bgColor);

	public IDisplay getDisplay();

	public MutableAffineTransformer getViewTransformer();

	public void setViewTransformer(MutableAffineTransformer viewTransformer);

	public void addListener(IViewListener listener);

	public void removeListener(IViewListener listener);

}
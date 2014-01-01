package org.datagr4m.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.datagr4m.maths.geometry.RectangleUtils;
import org.datagr4m.viewer.renderer.AbstractRenderer;
import org.datagr4m.viewer.renderer.IRenderer;
import org.datagr4m.viewer.renderer.utils.CrossRenderer;
import org.datagr4m.viewer.renderer.utils.GridRenderer;
import org.datagr4m.viewer.renderer.utils.GridRendererSettings;
import org.jzy3d.maths.Coord2d;

import edu.uci.ics.jung.visualization.transform.MutableAffineTransformer;

public class View implements IView {
	public View(IRenderer renderer, IDisplay display) {
		this.renderer = renderer;
		this.display = display;
		this.viewTransformer = new MutableAffineTransformer() {
			@Override
			public void scale(double scalex, double scaley, Point2D from) {
				AffineTransform xf = AffineTransform.getTranslateInstance(
						from.getX(), from.getY());
				xf.scale(scalex, scaley);
				xf.translate(-from.getX(), -from.getY());
				inverse = null;
				transform.preConcatenate(xf);
				fireStateChanged();

				lastScaleShift = new Point.Double(scalex, scaley);
				// System.out.println(lastScale);
			}

			@Override
			public void setScale(double scalex, double scaley, Point2D from) {
				super.setScale(scalex, scaley, from);
				lastScaleSet = new Point.Double(scalex, scaley);
			}
		};
		this.crossRenderer = new CrossRenderer(100);
		this.gridRenderer = new GridRenderer(this);
		this.gridRendererSettings = new GridRendererSettings();
		this.listeners = new ArrayList<IViewListener>(1);
	}

	@Override
	public Point2D getLastScaleShift() {
		return lastScaleShift;
	}

	@Override
	public Point2D getLastScaleSet() {
		return lastScaleSet;
	}

	@Override
	public void refresh() {
		if (isUpdateOnDemand)
			display.refresh();
		// display.repaint();
		updateViewBounds();
		updateViewPoint();
	}

	/******** VIEW BOUNDS IN LAYOUT *******/

	@Override
	public Rectangle2D getViewBounds() {
		if (viewBounds == null)
			getViewBounds(true);
		return getViewBounds(false);
	}

	/** A rectangle that indicates the viewable rectangle in the user space. */
	@Override
	public Rectangle2D getViewBounds(boolean update) { // TODO: should optimize:
														// cache this
														// computation
		if (update)
			updateViewBounds();
		return viewBounds;
	}

	@Override
	public void updateViewBounds() {
		// System.out.println(display.getSize());
		Point2D topLeft = new Point2D.Double(0, 0);
		Point2D bottomRight = new Point2D.Double(display.getSize().width,
				display.getSize().height);
		screenToModel(topLeft);
		screenToModel(bottomRight);
		viewBounds = RectangleUtils.build(topLeft, bottomRight);
	}

	@Override
	public Point2D getViewPoint() {
		if (viewPoint == null)
			getViewPoint(true);
		return getViewPoint(false);
	}

	/** A rectangle that indicates the viewable rectangle in the user space. */
	@Override
	public Point2D getViewPoint(boolean update) { // TODO: should optimize:
													// cache this computation
		if (update)
			updateViewPoint();
		return viewPoint;
	}

	@Override
	public void updateViewPoint() {
		Point2D viewPoint = new Point2D.Double(display.getSize().width / 2,
				display.getSize().height / 2);
		screenToModel(viewPoint);
		this.viewPoint = viewPoint;
	}

	@Override
	public boolean isDisplayed(Point2D point) {
		if (viewBounds == null)
			updateViewBounds();
		if (viewBounds.contains(point))
			return true;
		return false;
	}

	@Override
	public boolean isDisplayed(Rectangle2D point) {
		if (viewBounds == null)
			updateViewBounds();
		if (viewBounds.contains(point))
			return true;
		return false;
	}

	/******** FIT A GIVEN LAYOUT BOUNDARY *******/

	/*public void fit(IHierarchicalModel model) {
		display.getView().fit(
				model.getRawRectangleBounds().cloneAsRectangle2D());
	}
*/
	@Override
	public void fit(Rectangle2D rect) {
		fit(rect, true);
	}

	/**
	 * Make the view auto pan & scale so that the provided rectangle region is
	 * shown.
	 */
	@Override
	public void fit(Rectangle2D rect, boolean refreshNow) {
		Point2D center = RectangleUtils.center(rect);
		Point2D topLeft = RectangleUtils.topLeft(rect);
		Point2D bottomRight = RectangleUtils.bottomRight(rect);

		float modelWidth = (float) (bottomRight.getX() - topLeft.getX());
		float modelHeight = (float) (topLeft.getY() - bottomRight.getY());

		// 1]------------------------
		// Si les bounds du modele ne sont pas
		// parfaitement centree, on decalle les bounds
		// pour qu'elle forment un rectangle centre en 0,0
		float xpan = (float) (-center.getX());
		float ypan = (float) (-center.getY());

		// 2]------------------------
		// scale pour que le modele soit aligne sur la taille du display
		float xscale = (display.getSize().width / modelWidth);
		float yscale = (display.getSize().height / modelHeight);
		float scale = Math.min(xscale, yscale);
		scale *= 0.9;

		// 3]------------------------
		// shift de la moiti� de la taille du mod�le pour tout recentrer

		// 4]------------------------
		// apparait en haut � gauche, donc si width>height, on a une bande
		// libre en bas, et si width<height, on a une bande � droite
		// on fait une derni�re correction pour avoir une demi bande de chaque
		// c�t�
		float newWidth = modelWidth * scale;
		float newHeight = modelHeight * scale;

		float diffWidth = display.getSize().width - newWidth;
		float diffHeight = display.getSize().height - newHeight;

		// ---
		viewTransformer.getTransform().setToIdentity();
		// 1
		pan(xpan, ypan, false);
		// 2
		scale(scale, scale, false);
		// 3
		pan(modelWidth / 2, modelHeight / 2, false);
		// 4
		pan(diffWidth / 2 / scale, diffHeight / 2 / scale, false);

		if (refreshNow)
			refresh();
	}

	@Override
	public void centerAt(Coord2d position, float radius) {
		centerAt(position, radius, 1.75f);
	}

	@Override
	public void centerAt(Coord2d position, float radius, float scale) {
		setScale(scale, scale);
		centerAt(position);
	}

	@Override
	public void centerAt(Coord2d position) {
		Point2D screenCenter = new Point2D.Double(display.getSize().width / 2,
				display.getSize().height / 2);
		screenToModel(screenCenter);

		// System.out.println("screen center:"+screenCenter);
		// System.out.println("item center:"+position);
		float xpan = (float) (position.x - screenCenter.getX());
		float ypan = (float) (position.y - screenCenter.getY());
		// System.out.println("xpan:"+xpan);
		// System.out.println("ypan:"+ypan);
		pan(-xpan, -ypan);
		// screenCenter = new Point2D.Double(display.getSize().width/2,
		// display.getSize().height/2);
		// screenToModel(screenCenter);

		// System.out.println("screen center:"+screenCenter);
	}

	@Override
	public Point2D getCenter() {
		Point2D screenCenter = new Point2D.Double(display.getSize().width / 2,
				display.getSize().height / 2);
		screenToModel(screenCenter);
		return screenCenter;
	}

	@Override
	public Coord2d getLayoutScale(Rectangle2D r) {
		Rectangle2D view = getViewBounds(true);
		return new Coord2d(view.getWidth() / r.getWidth(), view.getHeight()
				/ r.getHeight());
	}

	@Override
	public void render(Graphics2D g2d) {
		// Apply zoom/offset
		AbstractRenderer.pushDefaultTransform(g2d); // backup current transform

		// AffineTransform t = viewTransformer.getTransform();
		g2d.transform(viewTransformer.getTransform());

		// render a grid
		gridRenderer.render(g2d, gridRendererSettings);

		// render content
		renderer.render(g2d);

		// center position
		if (showCenterCross)
			crossRenderer.render(g2d, Color.RED, new Coord2d());

		// mouse position
		if (showLastClickCross) {
			Point m = (Point) display.getMouse().getPrevMouse().clone();
			screenToModel(m);
			crossRenderer.render(g2d, Color.GREEN, m);
		}

		// render visible part of the layout
		if (showViewBounds) {
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(14));
			g2d.drawRect((int) viewBounds.getMinX(),
					(int) viewBounds.getMinY(), (int) viewBounds.getWidth(),
					(int) viewBounds.getHeight());
			g2d.setStroke(new BasicStroke(1));
		}

		// Reset transform to former one
		AbstractRenderer.pullDefaultTransform(g2d);
	}

	@Override
	public void modelToScreen(Point2D p) {
		viewTransformer.getTransform().transform(p, p);
	}

	/**
	 * Converts absolute mouse coordinates to coordinates relative to the
	 * rendered layout.
	 */
	@Override
	public void screenToModel(Point2D p) {
		AffineTransform t = viewTransformer.getInverse();
		if (t != null)
			t.transform(p, p);
	}

	@Override
	public Shape screenToModel(Shape shape) {
		AffineTransform t = viewTransformer.getInverse();
		if (t != null)
			return t.createTransformedShape(shape);
		return null;
	}

	@Override
	public Coord2d screenToModel(Coord2d c) {
		Point2D p = new Point2D.Float(c.x, c.y);
		viewTransformer.getInverse().transform(p, p);
		return new Coord2d(p.getX(), p.getY());
	}

	@Override
	public void scale(float xRatio, float yRatio) {
		scale(xRatio, yRatio, true);
	}

	@Override
	public void scale(float xRatio, float yRatio, boolean refreshNow) {
		viewTransformer.scale(xRatio, yRatio, new Point2D.Float(0, 0));
		if (refreshNow)
			refresh();
		fireViewBoundsChanged();
	}

	@Override
	public void scale(float xRatio, float yRatio, Point2D from) {
		scale(xRatio, yRatio, from, true);
	}

	@Override
	public void scale(float xRatio, float yRatio, Point2D from,
			boolean refreshNow) {
		viewTransformer.scale(xRatio, yRatio, from);
		// System.out.println("view.scale:"+viewTransformer.getScale());
		if (refreshNow)
			refresh();
		fireViewBoundsChanged();
	}

	@Override
	public void setScale(float xRatio, float yRatio) {
		setScale(xRatio, yRatio, true);
	}

	@Override
	public void setScale(float xRatio, float yRatio, boolean refreshNow) {
		viewTransformer.setScale(xRatio, yRatio, new Point2D.Float(0, 0));
		if (refreshNow)
			refresh();
		fireViewBoundsChanged();
	}

	@Override
	public void setScale(float xRatio, float yRatio, Point2D from) {
		setScale(xRatio, yRatio, from, true);
	}

	@Override
	public void setScale(float xRatio, float yRatio, Point2D from,
			boolean refreshNow) {
		viewTransformer.setScale(xRatio, yRatio, from);
		if (refreshNow)
			refresh();
		fireViewBoundsChanged();
	}

	/*
	 * public void getScale(){ return viewTransformer.getScale(); }
	 */
	@Override
	public void pan(float dx, float dy) {
		pan(dx, dy, true);
	}

	@Override
	public void pan(float dx, float dy, boolean refreshNow) {
		viewTransformer.translate(dx, dy);
		if (refreshNow)
			refresh();
		fireViewBoundsChanged();
	}

	/*************/

	@Override
	public IRenderer getRenderer() {
		return renderer;
	}

	@Override
	public void setRenderer(IRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public boolean isShowCenterCross() {
		return showCenterCross;
	}

	@Override
	public void setShowCenterCross(boolean showCenterCross) {
		this.showCenterCross = showCenterCross;
	}

	@Override
	public boolean isShowLastClickCross() {
		return showLastClickCross;
	}

	@Override
	public void setShowLastClickCross(boolean showLastClickCross) {
		this.showLastClickCross = showLastClickCross;
	}

	@Override
	public GridRendererSettings getGridRendererSettings() {
		return gridRendererSettings;
	}

	@Override
	public void setGridRendererSettings(
			GridRendererSettings gridRendererSettings) {
		this.gridRendererSettings = gridRendererSettings;
	}

	@Override
	public Color getBackgroundColor() {
		return bgColor;
	}

	@Override
	public void setBackgroundColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	@Override
	public IDisplay getDisplay() {
		return display;
	}

	@Override
	public MutableAffineTransformer getViewTransformer() {
		return viewTransformer;
	}

	@Override
	public void setViewTransformer(MutableAffineTransformer viewTransformer) {
		this.viewTransformer = viewTransformer;
	}

	@Override
	public void addListener(IViewListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IViewListener listener) {
		this.listeners.remove(listener);
	}

	protected void fireViewBoundsChanged() {
		for (IViewListener listener : listeners) {
			listener.boundChanged(getViewBounds());
		}
	}

	/**************/

	protected IRenderer renderer;
	protected IDisplay display;
	protected MutableAffineTransformer viewTransformer;
	protected CrossRenderer crossRenderer;
	protected GridRenderer gridRenderer;
	protected GridRendererSettings gridRendererSettings;
	protected boolean showCenterCross = false;
	protected boolean showLastClickCross = false;
	protected boolean isUpdateOnDemand = true;
	protected boolean showViewBounds = false;
	protected Rectangle2D viewBounds;
	protected Point2D viewPoint;
	protected Point2D lastScaleShift;
	protected Point2D lastScaleSet;
	protected List<IViewListener> listeners;

	protected Color bgColor = Color.WHITE;

}

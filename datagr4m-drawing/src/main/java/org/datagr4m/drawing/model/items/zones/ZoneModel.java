package org.datagr4m.drawing.model.items.zones;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.listeners.IItemListener;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.algorithms.convexhull.GrahamScan;
import org.jzy3d.maths.algorithms.interpolation.algorithms.BernsteinInterpolator;


public class ZoneModel implements IZoneModel, IItemListener {
    protected String label;
    protected Object data;
    protected Polygon geometry;
    protected List<IBoundedItem> children;
    protected boolean geometryDirty = false;

    public ZoneModel(String label, Object data) {
        this(label, data, new ArrayList<IBoundedItem>());
    }

    public ZoneModel(String label, Object data, List<IBoundedItem> children) {
        this.label = label;
        this.data = data;
        this.children = children;
    }

    @Override
    public void itemBoundsChanged(IBoundedItem item) {

    }

    @Override
    public void itemPositionChanged(IBoundedItem item) {
        Logger.getLogger(ZoneModel.class).info(item + " changed");
        if (children.contains(item))
            geometryDirty = true;
    }

    @Override
    public void dispose() {
        for (IBoundedItem item : children)
            unregister(item);
    }

    protected void register(IBoundedItem item) {
        item.addItemListener(this);
    }

    protected void unregister(IBoundedItem item) {
        item.removeItemListener(this);
    }

    /* */

    @Override
    public void addChild(IBoundedItem child) {
        addChild(child, true);
    }

    @Override
    public void addChild(IBoundedItem child, boolean updateGeometry) {
        children.add(child);
        register(child);
        geometryDirty = true;
        if (updateGeometry)
            updateGeometry();
    }

    @Override
    public void addChildren(List<IBoundedItem> children) {
        for (IBoundedItem item : children)
            addChild(item, false);
        updateGeometry();
    }

    @Override
    public List<IBoundedItem> getChildren() {
        return children;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Polygon getGeometry() {
        if (geometryDirty)
            updateGeometry();
        return geometry;
    }

    @Override
    public Object getData(IBoundedItem item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IBoundedItem getItem(Object data) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IBoundedItem getChildWithLabel(String label) {
        // TODO Auto-generated method stub
        return null;
    }

    /* */

    @Override
	public void updateGeometry() {
        Logger.getLogger(ZoneModel.class).info("update zone");
        geometry = computeGeometry();
        geometryDirty = false;
    }

    protected Polygon computeGeometry() {
        Polygon p = new Polygon();
        /*
         * if (children.size() == 0) { return p; } else if (children.size() ==
         * 1) { return p; // p.addPoint((int)children.get(0).getX(),
         * (int)pt.getY()); } else
         */{
            // GrahamScan
        	 Coord2d[] points = new Coord2d[children.size() * 4];

            int k = 0;
            for (IBoundedItem item : children) {

                RectangleBounds r = item.getAbsoluteRectangleBounds();// .enlarge(30,
                                                                      // 30);

                points[k++] = r.getBottomLeftCorner();
                points[k++] = r.getBottomRightCorner();
                points[k++] = r.getTopLeftCorner();
                points[k++] = r.getTopRightCorner();
            }

            // Compute convex hull
            GrahamScan gs = new GrahamScan();
            Deque<Coord2d> deque = gs.getConvexHull(points);

            Iterator<Coord2d> it = deque.iterator();

            if (interpolate) {
                List<Coord3d> in = new ArrayList<Coord3d>();
                while (it.hasNext()) {
                    Coord2d pt = it.next();
                    // Logger.getLogger(ZoneModel.class).info(pt);
                    in.add(new Coord3d(pt.getX(), pt.getY(), 0));
                }

                // Interpolation
                BernsteinInterpolator bi = new BernsteinInterpolator();
                List<Coord3d> pts = bi.interpolate(in, 10);
                for(Coord3d pt: pts){
                    p.addPoint((int) pt.x, (int) pt.y);
                }
            } else {

                // Logger.getLogger(ZoneModel.class).info(children.size() + " items in zone model");
                while (it.hasNext()) {
                    Coord2d pt = it.next();
                    // Logger.getLogger(ZoneModel.class).info(pt);
                    p.addPoint((int) pt.getX(), (int) pt.getY());
                }
            }
            return p;
        }
    }

    protected boolean interpolate = true;

    @Override
	public String toString() {
        return "ZoneModel " + label + " : " + children;
    }
}

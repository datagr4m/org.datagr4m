package org.datagr4m.drawing.layout.hierarchical.pair;


import java.awt.geom.CubicCurve2D;

import org.datagr4m.drawing.layout.hierarchical.AbstractHierarchicalLayout;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.IHierarchicalPairModel;
import org.datagr4m.monitors.ITimeMonitor;
import org.datagr4m.monitors.TimeMonitor;
import org.jzy3d.maths.Coord2d;


/**
 * Setup a pair of items either horizontally or vertically according to the desired
 * inter item margin, which defaults to {@link HierarchicalPairLayout.DEFAULT_INTER_ITEM_MARGIN}
 */
public class HierarchicalPairLayout extends AbstractHierarchicalLayout implements IHierarchicalPairLayout{
    private static final long serialVersionUID = -5124418768199003016L;
    public static int HORIZONTAL = 0;
    public static int VERTICAL = 1;
    public static int DEFAULT_INTER_ITEM_MARGIN = 30;
    
    protected boolean showSpline = false;
    
    private ITimeMonitor timeMonitor;

    public HierarchicalPairLayout(){
        this(null);
    }
    
    public HierarchicalPairLayout(IHierarchicalPairModel model){
        setModel(model);
        initMonitor();
    }
    
    private void initMonitor() {
        timeMonitor = new TimeMonitor(this);
    }
    
    @Override
    public ITimeMonitor getTimeMonitor() {
        return timeMonitor;
    }
    
    @Override
    public void initAlgo() {
        super.initAlgo(); // init child items first
        
        if(model.getFirst()==null)
            ;//throw new RuntimeException("First item of the pair is null in '" + model.getLabel() + "'");
        else
            model.getFirst().changePosition(0,0);
            
        if(model.getSecond()==null)
            ;//throw new RuntimeException("Second item of the pair is null in '" + model.getLabel() + "'");
        else
            model.getSecond().changePosition(0,0);
        
        compute();
    }
    
    

    
    @Override
	public void compute(){
        if(model.getFirst()!=null && model.getSecond()!=null){
            // get bounds relative to current model position
            RectangleBounds b1 = model.getFirst().getRawRectangleBounds();
            RectangleBounds b2 = model.getSecond().getRawRectangleBounds();
            Coord2d c1 = new Coord2d();
            Coord2d c2 = new Coord2d();
            Coord2d c  = new Coord2d();//c1.add(c2).div(2);
            
            if(orientation==HORIZONTAL){
                c1.x = c.x - b1.width/2 - interItemMargin/2;
                c1.y = c.y;
                c2.x = c.x + b2.width/2 + interItemMargin/2;
                c2.y = c.y;
            }
            else{
                c1.x = c.x;
                c1.y = c.y + b1.height/2 + interItemMargin/2;
                c2.x = c.x;
                c2.y = c.y - b2.height/2 - interItemMargin/2;
            }
            
            model.getFirst().changePosition(c1);
            model.getSecond().changePosition(c2);
            model.updateBoundsCache();
            
            computeSpline();
        }
        else{
            resetSpline();
            if(model.getFirst()!=null){
                model.getFirst().changePosition(new Coord2d());
            }
            if(model.getSecond()!=null){
                model.getSecond().changePosition(new Coord2d());
            }
        }
        counter++;
    }
    
    @Override
	public void computeSpline(){
        if(showSpline && model.getFirst()!=null && model.getSecond()!=null){
            doComputeSpline();
        }
        else
            resetSpline();
    }

    private void doComputeSpline() {
        Coord2d ac1 = model.getFirst().getAbsolutePosition();
        Coord2d ac2 = model.getSecond().getAbsolutePosition();
        ac1.addSelfY(-model.getFirst().getRadialBounds());
        ac2.addSelfY(-model.getSecond().getRadialBounds());
        
        Coord2d cc1 = ac1.interpolation(ac2, 0.75f);
        Coord2d cc2 = ac1.interpolation(ac2, 0.25f);
        
        float height = Math.max(model.getFirst().getRadialBounds(), model.getSecond().getRadialBounds());
        height /= 2;
        cc1.addSelfY(-height);
        cc2.addSelfY(-height);
        
        model.setCurve(new CubicCurve2D.Float(ac1.x, ac1.y, cc1.x, cc1.y, cc2.x, cc2.y, ac2.x, ac2.y));
    }
    
    public void resetSpline(){
        model.setCurve(null);
    }
    
    @Override
    public void goAlgo(){
        //super.goAlgo(); // goAlgo on children first
        compute();
        computeSpline();
    }
    
    @Override
    public int getInterItemMargin() {
        return interItemMargin;
    }

    @Override
    public void setInterItemMargin(int interItemMargin) {
        this.interItemMargin = interItemMargin;
    }

    @Override
    public int getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean isShowSpline() {
        return showSpline;
    }

    public void setShowSpline(boolean showSpline) {
        this.showSpline = showSpline;
    }

    /** Must be a {@link IHierarchicalPairModel}, otherwise crashes 
     * with a cast exception.
     */
    @Override
    public void setModel(IHierarchicalNodeModel model) {
        this.model = (HierarchicalPairModel)model;
    }

    @Override
    public IHierarchicalPairModel getModel() {
        return model;
    }
    
    /***********************/

    protected HierarchicalPairModel model;
    protected int orientation = HORIZONTAL;
    protected int interItemMargin = DEFAULT_INTER_ITEM_MARGIN;
}

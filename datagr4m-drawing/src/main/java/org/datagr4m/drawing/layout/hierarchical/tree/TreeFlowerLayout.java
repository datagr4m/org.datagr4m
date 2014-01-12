package org.datagr4m.drawing.layout.hierarchical.tree;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.drawing.layout.geometrical.flower.FlowerGeometry;
import org.datagr4m.drawing.layout.hierarchical.AbstractHierarchicalLayout;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.tree.TreeModel;
import org.datagr4m.drawing.renderer.policy.DefaultStyleSheet;
import org.datagr4m.maths.geometry.PointUtils;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.maths.geometry.functions.LinearFunction;
import org.datagr4m.monitors.ITimeMonitor;
import org.datagr4m.monitors.TimeMonitor;
import org.datagr4m.topology.Group;
import org.datagr4m.viewer.renderer.annotations.items.ClickableRectangleAnnotation;
import org.datagr4m.viewer.renderer.annotations.items.IClickableAnnotation;
import org.jzy3d.maths.Coord2d;


/**
 * min floor height=noderadius*3
 */
public class TreeFlowerLayout extends AbstractHierarchicalLayout{
    private ITimeMonitor timeMonitor;
    
    protected TreeModel model;
    protected Point2D sourcePoint;
    protected Point2D targetPoint;
    protected Point2D gardenCenter;
    protected float gardenRadius;
    protected float rootDistance;
    protected Point2D root;
    protected FlowerGeometry flower;
    protected double[] floors;
    protected float minFloor;
    protected double maxAngle;
    protected double minAngle;
    protected double preferedAngle;
    protected double treeHeight;//from center
    protected double treeAngle;
    protected int buildLevel;
    protected float nodeRadius;
    
    protected Map<IBoundedItem, Coord2d> polarCoord = new HashMap<IBoundedItem, Coord2d>();
    
    private static final long serialVersionUID = 2707510020001523214L;

    public TreeFlowerLayout() {
        this(0, new Point2D.Float(0,0), new Point2D.Float(0,0), new Point2D.Float(0,0), 0, 0);
    }
    public TreeFlowerLayout(TreeModel model, float radius, Point2D rotation, Point2D source, Point2D dest, float rootDistance, float nodeRadius) {
        this(radius, rotation, source, dest, rootDistance, nodeRadius);
        setModel(model);
    }

    public TreeFlowerLayout(float radius, Point2D rotation, Point2D source, Point2D target, float rootDistance, float nodeRadius) {
        this.gardenRadius = radius;
        this.gardenCenter = rotation;
        this.sourcePoint = source;
        this.targetPoint = target;
        this.rootDistance = rootDistance;
        this.nodeRadius = nodeRadius;
        
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
    public void setModel(IHierarchicalNodeModel model) {
        this.model = (TreeModel)model;
        initAlgo();
    }
    
    @Override
    public IHierarchicalNodeModel getModel() {
        return model;
    }
    
    @Override
    public void initAlgo() {
        if(model!=null)
            init(model);
    }
    
    @Override
	public void goAlgo(){
    }
    
    /***************/
    
    public void init(TreeModel model){
        timeMonitor.startMonitor();
        
        doInit(model);
        
        timeMonitor.stopMonitor();
    }

    public void doInit(TreeModel model) {
        // build list of tree leafs
        List<IBoundedItem> leafDevices = model.getDescendants(true);
        int maxDepth = 0;
        for(IBoundedItem item: leafDevices){
            if(item.getDepth()>maxDepth)
                maxDepth=item.getDepth();
        }
        
        int n = leafDevices.size()-1;
        
        minFloor = nodeRadius*2+nodeRadius;
        maxAngle = Math.PI/1.5;
        minAngle = 0;
        preferedAngle = Math.PI/6;
        
        // setup flower with a bound on node radius and distance
        flower = FlowerGeometry.fromR1AndR2(gardenRadius+rootDistance+minFloor*maxDepth, nodeRadius, n);
        treeAngle = flower.getTotalAngle();
        treeHeight = flower.getBodyRadius()+flower.getPetalRadius();
        //treeAngle = Float.MAX_VALUE;
        //treeHeight = Float.MAX_VALUE;
        buildLevel = 1;

        // for other attempts
        double tryAngle = 0;
        double tryStep = Math.PI/100;
        
        // setup flower with a bound on node radius and a fixed angle
        tryAngle = preferedAngle;
        while(!constraintOnAngle()){
            flower = FlowerGeometry.fromR2(nodeRadius, n, maxAngle);
            treeAngle = flower.getTotalAngle();
            treeHeight = flower.getBodyRadius()+flower.getPetalRadius();
            buildLevel = 2;
            
            if(tryAngle>=maxAngle)
                break;
            else
                tryAngle += tryStep;
        }
        
        // si trop proche
        tryAngle = preferedAngle;
        while(!constraintOnHeight(maxDepth)){
            flower = FlowerGeometry.fromR1(gardenRadius+rootDistance, n, tryAngle);
            treeAngle = flower.getTotalAngle();
            treeHeight = flower.getBodyRadius()+flower.getPetalRadius();
            buildLevel = 3;
            
            if(tryAngle>=maxAngle)
                break;
            else
                tryAngle += tryStep;
        }
        
        // setup alpha range
        double angle = PointUtils.angle(sourcePoint, targetPoint);
        double astep  = flower.getPetalSliceAngle();
        double astart = angle - flower.getTotalAngle()/2;
        
        if(astep==0)
            astep=0;
        
        // compute each tree depth floor expected distance
        double floorHeight = (treeHeight-(gardenRadius+rootDistance))/maxDepth;
        floorHeight = Math.max(floorHeight, minFloor); // a min floor height
        
        floors = new double[maxDepth+1];
        for (int i = 0; i < maxDepth+1; i++) {
            floors[i] = (gardenRadius+rootDistance) + floorHeight*i;
        }

        // make each leaf petal
        root = LinearFunction.getPointOnSegment(sourcePoint, targetPoint, rootDistance);
        Coord2d cRoot = Pt.cloneAsCoord2d(root);
        Coord2d cPivot = Pt.cloneAsCoord2d(gardenCenter);
        for(IBoundedItem item: leafDevices){
            Coord2d polar = new Coord2d(astart+astep/2, floors[item.getDepth()]);
            polarCoord.put(item, polar);
            item.changePosition(polar.cartesian().add(cPivot));
            
            astart+=astep;
        }
        // and make parent by averaging their children angle
        fixParents(model, cPivot, cRoot);
        
        model.setPivotPoint(cPivot);
    }
    
    /** Return true if tree angle stand between min and max values.*/
    protected boolean constraintOnAngle(){
        return (treeAngle<maxAngle && treeAngle>=minAngle);
    }
    
    /** Return true if tree height is hight than the default min height.*/
    protected boolean constraintOnHeight(int maxTreeDepth){
        return (treeHeight > (gardenRadius+rootDistance + minFloor*maxTreeDepth));
    }
        
    protected void fixParents(TreeModel model, Coord2d cPivot, Coord2d cRoot){
        List<Coord2d> polarList = new ArrayList<Coord2d>();
        for(IBoundedItem child: model.getChildren()){
            if(child instanceof TreeModel){
                fixParents((TreeModel)child, cPivot, cRoot);
                polarList.add(polarCoord.get(child));
            }
            else{
                polarList.add(polarCoord.get(child));
            }
        }

        //mean angle
        int k = 0;
        float a = 0;
        for(Coord2d p: polarList){
            a+=p.x; // mean alpha
            k++;
        }
        a /= k;
        Coord2d polar = new Coord2d(a, floors[model.getDepth()]);
        polarCoord.put(model, polar);
        Coord2d position;
        if(model.getDepth()==0)
            position = cRoot;//polar.cartesian().add(cPivot);//cRoot;
        else
            position = polar.cartesian().add(cPivot);
        model.changePosition(position);
        
        // model node representation
        String nodeName = getNodeName(model);
        Color color = getNodeColor(model);
        IClickableAnnotation annotation = new ClickableRectangleAnnotation(position, nodeName);
        annotation.setBackgroundColor(color);
        annotation.setTextColor(Color.WHITE);
        model.setNodeRepresentations(annotation);
    }
    
    protected String getNodeName(TreeModel model){
        if(model==null)
            return "null";
        else{
            if(model.getObject()!=null){
                Object o = model.getObject();
                if(o instanceof Group<?>)
                    return ((Group) o).getType();
                return model.getObject().toString();
            }
            else{
                String str = model.getChildren().size()+"";
                if(model.getParent()!=null)
                    str+= " (" + model.getParent().getChildren().indexOf(model) + ")";
                str+= " d=" + model.getDepth();
                return str;
            }
        }
    }
    
    protected Color getNodeColor(TreeModel model){
        if(model!=null){
            if(model.getObject()!=null){
                return DefaultStyleSheet.getColor(model.getObject());
            }
        }
        return Color.WHITE;
    }
            
    /*************/

    public Point2D getSourcePoint() {
        return sourcePoint;
    }

    public Point2D getTargetPoint() {
        return targetPoint;
    }

    public Point2D getGardenCenter() {
        return gardenCenter;
    }

    public float getGardenRadius() {
        return gardenRadius;
    }

    public float getRootDistance() {
        return rootDistance;
    }

    public Point2D getRoot() {
        return root;
    }

    public FlowerGeometry getFlower() {
        return flower;
    }

    public double[] getFloors() {
        return floors;
    }

    public float getMinFloor() {
        return minFloor;
    }

    public double getMaxAngle() {
        return maxAngle;
    }

    public double getMinAngle() {
        return minAngle;
    }

    public double getTreeHeight() {
        return treeHeight;
    }

    public double getTreeAngle() {
        return treeAngle;
    }

    public int getBuildLevel() {
        return buildLevel;
    }

    public float getNodeRadius() {
        return nodeRadius;
    }
}

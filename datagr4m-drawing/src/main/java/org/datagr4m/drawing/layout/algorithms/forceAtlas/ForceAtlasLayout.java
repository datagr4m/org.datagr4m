package org.datagr4m.drawing.layout.algorithms.forceAtlas;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.datagr4m.drawing.layout.IRunnableLayout;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FAAttraction;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FARepulsion;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone.StandaloneForce;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.monitoring.MeanMoveAnalysis;
import org.datagr4m.drawing.layout.algorithms.forces.IForce;
import org.datagr4m.drawing.layout.algorithms.forces.ItemForceVector;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;
import org.jzy3d.maths.Coord2d;



public class ForceAtlasLayout implements IRunnableLayout {
    public static double INERTIA_DEFAULT = 0.1d; // 0.1d;
    public static double REPULSION_DEFAULT = 200d;
    public static double ATTRACTION_DEFAULT = 10d;
    public static double MAX_DISPLACEMENT_DEFAULT = 10d;
    public static double FREEZE_STRENGTH_DEFAULT = 80d;
    public static double FREEZE_INERTIA_DEFAULT = 0.2d;
    public static boolean FREEZE_BALANCE_DEFAULT = true;
    public static double GRAVITY_DEFAULT = 30d;//30d;
    public static double SPEED_DEFAULT = 1d;
    public static double COOLING_DEFAULT = 1d;
    
    public static boolean OUTBOUND_ATTRACTION_DEFAULT = false;
    public static boolean ADJUST_SIZE_DEFAULT = true;
    public static boolean MAINTAIN_TO_CENTER_DEFAULT = true;
    
    private static final long serialVersionUID = 1526844201583879045L;
    protected IHierarchicalGraphModel model;
    protected Map<IBoundedItem, ItemForceVector> itemForces = new HashMap<IBoundedItem, ItemForceVector>();
    
    //Properties
    public double inertia;
    private double repulsionStrength;
    private double attractionStrength;
    private double maxDisplacement;
    private boolean freezeBalance;
    private double freezeStrength;
    private double freezeInertia;
    private double gravity;
    private double speed;
    private double cooling;
    private boolean outboundAttractionDistribution;
    private boolean adjustSizes;
    protected boolean maintainToCenter = true;
    
    protected IInitializer initializer = new RandomInitializer();
    
    protected boolean enableMeanMoveLogging = false;
    protected MeanMoveAnalysis meanMoveAnalysis = new MeanMoveAnalysis();
    
    
    protected int counter = 0;

    public ForceAtlasLayout(IHierarchicalGraphModel model) {
        this.model = model;
        resetPropertiesValues();
    }

    @Override
    public void resetPropertiesValues() {
        setInertia(INERTIA_DEFAULT);//0.1;
        setRepulsionStrength(REPULSION_DEFAULT);
        setAttractionStrength(ATTRACTION_DEFAULT);
        setMaxDisplacement(MAX_DISPLACEMENT_DEFAULT);
        setFreezeBalance(FREEZE_BALANCE_DEFAULT);
        setFreezeStrength(FREEZE_STRENGTH_DEFAULT);
        setFreezeInertia(FREEZE_INERTIA_DEFAULT);
        setGravity(GRAVITY_DEFAULT);//30d
        setOutboundAttractionDistribution(OUTBOUND_ATTRACTION_DEFAULT);
        setAdjustSizes(ADJUST_SIZE_DEFAULT);
        setSpeed(SPEED_DEFAULT);
        setCooling(COOLING_DEFAULT);
        setMaintainToCenter(MAINTAIN_TO_CENTER_DEFAULT);
    }

    @Override
    public void initAlgo() {
        if(initializer!=null){
            initializer.apply(model);
        }
    }
    
    @Override
    public void goAlgo() {        
        Collection<IBoundedItem> items = model.getChildren();
                
        // init at each step
        initItemVectors(items);
        initForces(items);
        
        // compute additive forces
        applyRepulsion(items);
        applyAttraction(items);
        applyStandaloneForces();
        applyGravity(items);
        applySpeed(items);
        
        // apply forces
        applyForces(items);
        
        if(enableMeanMoveLogging){
            meanMoveAnalysis.makeAnalysis(this, items);
        }
        incCounter();
        
        // beautifier
        if(maintainToCenter)
            offsetToCenter(items);
    }

    public void incCounter() {
        counter++;
    }
    
    protected void initItemVectors(Collection<IBoundedItem> items){
        for(IBoundedItem item: items){
            ItemForceVector itemForce = new ItemForceVector();
            itemForce.old_dx = itemForce.dx;
            itemForce.old_dy = itemForce.dy;
            itemForce.dx *= inertia;
            itemForce.dy *= inertia;            
            itemForces.put(item, itemForce);
        }
    }
    
    protected void initForces(Collection<IBoundedItem> items){
        // repulsion force
        for(IBoundedItem item: items){
            Collection<IForce> forces = model.getRepulsors(item);
            for(IForce force: forces){
                if(force instanceof FARepulsion){
                    FARepulsion far = (FARepulsion)force;
                    ItemForceVector v1 = itemForces.get(force.getOwner());
                    ItemForceVector v2 = itemForces.get(force.getSource());
                    int d1 = model.getNodeDegree(force.getOwner());
                    int d2 = model.getNodeDegree(force.getSource());
                    far.setAll(d1, v1, d2, v2);
                }
                else{
                    throw new RuntimeException("unexpected force object: " + force);
                }   
            }
        }
        
        //attraction force
        Collection<IForce> forces = model.getAttractorForces();
        for(IForce force: forces){
            if(force instanceof FAAttraction){
                FAAttraction faa = (FAAttraction)force;
                ItemForceVector v1 = itemForces.get(faa.getOwner());
                ItemForceVector v2 = itemForces.get(faa.getSource());
                int d1 = model.getNodeDegree(faa.getOwner());
                int d2 = model.getNodeDegree(faa.getSource());
                
                if(v2==null)
                    throw new RuntimeException("trying to set a source force with a null value for " + faa.getSource().getLabel() + " in " + model.getLabel() + " with " + faa.getOwner().getLabel());
                if(v1==null)
                    throw new RuntimeException("trying to set an owner force with a null value for " + faa.getOwner().getLabel() + " in " + model.getLabel() + " with " + faa.getSource().getLabel());

                
                faa.setAll(d1, v1, d2, v2);
            }
            else{
                throw new RuntimeException("unexpected force object: " + force);
            }   
        }
        
        // standalone force (no setting)
        Collection<StandaloneForce> dforces = model.getForces();
        for(StandaloneForce force: dforces){
            StandaloneForce sf = force;
            ItemForceVector v1 = itemForces.get(sf.getOwner());
            ItemForceVector v2 = itemForces.get(sf.getSource());
            int d1 = model.getNodeDegree(sf.getOwner());
            int d2 = model.getNodeDegree(sf.getSource());
            sf.setAll(d1, v1, d2, v2);
        }
    }
    
    /***********************/
    
    
    protected void applyRepulsion(Collection<IBoundedItem> items){
        for(IBoundedItem item: items){
            Collection<IForce> forces = model.getRepulsors(item);
            //Logger.getLogger(ForceAtlasLayout.class).info("forces:"+forces.size());
            for(IForce force: forces){
                if(force instanceof FARepulsion){
                    ((FARepulsion)force).apply(getRepulsionStrength(), isAdjustSizes());
                }
                else{
                    throw new RuntimeException("unexpected force object: " + force);
                }
            }
        }
    }
    
    protected void applyAttraction(Collection<IBoundedItem> items){
        for(IBoundedItem item: items){
            Collection<IForce> forces = model.getAttractors(item);
            for(IForce force: forces){
                if(force instanceof FAAttraction){
                    ((FAAttraction)force).apply(getAttractionStrength(), isAdjustSizes(), isOutboundAttractionDistribution());
                }
                else{
                    throw new RuntimeException("unexpected force object: " + force);
                }
            }
        }
    }
    
    protected void applyStandaloneForces(){
        for(IForce force: model.getForces()){
            if(force instanceof StandaloneForce){
                ((StandaloneForce)force).apply();
            }
            else{
                throw new RuntimeException("unexpected force object: " + force);
            }
        }
    }
    
    protected void applyGravity(Collection<IBoundedItem> items){
        for(IBoundedItem item: items){
            float nx = item.getPosition().x;
            float ny = item.getPosition().y;
            double d = 0.0001 + Math.sqrt(nx * nx + ny * ny);
            double gf = 0.0001 * getGravity() * d;
            ItemForceVector layoutData = itemForces.get(item);
            layoutData.dx -= gf * nx / d;
            layoutData.dy -= gf * ny / d;
        }
    }
    
    protected void applySpeed(Collection<IBoundedItem> items){
        if (isFreezeBalance()) {
            for(IBoundedItem item: items){
                ItemForceVector layoutData = itemForces.get(item);
                layoutData.dx *= getSpeed() * 10f;
                layoutData.dy *= getSpeed() * 10f;
            }
        } else {
            for(IBoundedItem item: items){
                ItemForceVector layoutData = itemForces.get(item);
                layoutData.dx *= getSpeed();
                layoutData.dy *= getSpeed();
            }
        } 
    }
    
    protected void applyForces(Collection<IBoundedItem> items){
        for(IBoundedItem item: items) {
            if (!item.locked()) {
                ItemForceVector nLayout = itemForces.get(item);

                double d = 0.0001 + Math.sqrt(nLayout.dx * nLayout.dx + nLayout.dy * nLayout.dy);
                float ratio;
                if (isFreezeBalance()) {
                    nLayout.freeze = (float) (getFreezeInertia() * nLayout.freeze + (1 - getFreezeInertia()) * 0.1 * getFreezeStrength() * (Math.sqrt(Math.sqrt((nLayout.old_dx - nLayout.dx) * (nLayout.old_dx - nLayout.dx) + (nLayout.old_dy - nLayout.dy) * (nLayout.old_dy - nLayout.dy)))));
                    ratio = (float) Math.min((d / (d * (1f + nLayout.freeze))), getMaxDisplacement() / d);
                } else {
                    ratio = (float) Math.min(1, getMaxDisplacement() / d);
                }
                nLayout.dx *= ratio / getCooling();
                nLayout.dy *= ratio / getCooling();
                item.shiftPosition(nLayout.dx, nLayout.dy);
                
                //logForceMove(item, nLayout);
                //Logger.getLogger(ForceAtlasLayout.class).info(item.getPosition());
            }
            else
                ;//logForceMove(item);

        }
    }
        
    public int getCounter() {
        return counter;
    }

    /*  */
    
    @Override
    public void endAlgo() {
        for (IBoundedItem i : itemForces.keySet()) {
            itemForces.put(i, null);
        }
    }

    @Override
    public boolean canAlgo() {
        return true;
    }
    
    public void offsetToCenter(Collection<IBoundedItem> items){
        Coord2d bary = getBarycentre(items).mul(-1);
        
        for(IBoundedItem item: items)
            item.shiftPosition(bary);
    }
    
    public Coord2d getBarycentre(Collection<IBoundedItem> items){
        Coord2d mean = new Coord2d();
        for(IBoundedItem item: items)
            mean.addSelf(item.getPosition());
        mean.divSelf(items.size());
        return mean;
    }

    /***********/

    public void setInertia(Double inertia) {
        this.inertia = inertia;
    }

    public Double getInertia() {
        return inertia;
    }

    public Double getRepulsionStrength() {
        return repulsionStrength;
    }

    public void setRepulsionStrength(Double repulsionStrength) {
        this.repulsionStrength = repulsionStrength;
    }

    public Double getAttractionStrength() {
        return attractionStrength;
    }

    public void setAttractionStrength(Double attractionStrength) {
        this.attractionStrength = attractionStrength;
    }

    public Double getMaxDisplacement() {
        return maxDisplacement;
    }

    public void setMaxDisplacement(Double maxDisplacement) {
        this.maxDisplacement = maxDisplacement;
    }

    public Boolean isFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(Boolean freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public Double getFreezeStrength() {
        return freezeStrength;
    }

    public void setFreezeStrength(Double freezeStrength) {
        this.freezeStrength = freezeStrength;
    }

    public Double getFreezeInertia() {
        return freezeInertia;
    }

    public void setFreezeInertia(Double freezeInertia) {
        this.freezeInertia = freezeInertia;
    }

    public Double getGravity() {
        return gravity;
    }

    public void setGravity(Double gravity) {
        this.gravity = gravity;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getCooling() {
        return cooling;
    }

    public void setCooling(Double cooling) {
        this.cooling = cooling;
    }

    public Boolean isOutboundAttractionDistribution() {
        return outboundAttractionDistribution;
    }

    public void setOutboundAttractionDistribution(Boolean outboundAttractionDistribution) {
        this.outboundAttractionDistribution = outboundAttractionDistribution;
    }

    public Boolean isAdjustSizes() {
        return adjustSizes;
    }

    public void setAdjustSizes(Boolean adjustSizes) {
        this.adjustSizes = adjustSizes;
    }
    
    public boolean isMaintainToCenter() {
        return maintainToCenter;
    }

    public void setMaintainToCenter(boolean maintainToCenter) {
        this.maintainToCenter = maintainToCenter;
    }
    
    public IInitializer getInitializer() {
        return initializer;
    }

    public void setInitializer(IInitializer initializer) {
        this.initializer = initializer;
    }

    public boolean isEnableMeanMoveLogging() {
        return enableMeanMoveLogging;
    }

    public void setEnableMeanMoveLogging(boolean enableMeanMoveLogging) {
        this.enableMeanMoveLogging = enableMeanMoveLogging;
        //Logger.getLogger(this.getClass()).warn("logging for " + model.getLabel() + " : " + this.enableMeanMoveLogging);
    }
    
    public MeanMoveAnalysis getMeanMoveAnalysis() {
        return meanMoveAnalysis;
    }
    
    

    public IHierarchicalGraphModel getModel() {
        return model;
    }

    /********************/
    
    public float getMaxItemRadius(){
        float maxRadius = 0; // we don't want a multiplier less than 1
        for(IBoundedItem item: model.getChildren()){
            if(item.getRadialBounds()>maxRadius)
                maxRadius = item.getRadialBounds();
            //Logger.getLogger(ForceAtlasLayout.class).info("item radius:" + item.getRadius());
        }
        return maxRadius;
    }
}

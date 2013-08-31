package org.datagr4m.drawing.model.slots;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagr4m.drawing.layout.slots.ISlotGroupLayout;
import org.datagr4m.drawing.layout.slots.SlotGroupLayout;
import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryBuilder;
import org.datagr4m.drawing.layout.slots.geometry.global.DefaultGlobalSlotGeometryBuilder;
import org.datagr4m.drawing.model.pathfinder.path.IPath;

import com.google.common.collect.ArrayListMultimap;

/**
 * A SlotGroup stores geometrical data and source datamodel required
 * to draw the relations of its parent item to neighbour items.
 *
 * A SlotGroup handles one side of a rectangle, either north, south, east
 * or west.
 *
 * Actual slots geometry (anchor position, path start segment size) is handled
 * by a {@link ISlotGeometryBuilder}.
 *
 * @author Martin Pernollet
 */
public class SlotGroup implements Serializable, ISlotGroup {
    private static final long serialVersionUID = 4982134834283588780L;
    public static float DEFAULT_WIDTH_MARGIN = 10;//%
    public static ISlotGeometryBuilder DEFAULT_GEOMETRY_BUILDER = new DefaultGlobalSlotGeometryBuilder();//new ProgressiveSlotGeometryBuilder();
    protected boolean flipped90 = false;

    protected ISlotGroupLayout slotGroupLayout = new SlotGroupLayout();

    public SlotGroup(ISlotableItem parent, int slotNumber, SlotSide side, Point2D center, float width, float height){
        this(DEFAULT_GEOMETRY_BUILDER, parent, slotNumber, side, center, width, height, DEFAULT_WIDTH_MARGIN, false);
    }
    public SlotGroup(ISlotableItem parent, Collection<SlotTarget> targets, SlotSide side, Point2D center, float width, float height){
        this(DEFAULT_GEOMETRY_BUILDER, parent, targets.size(), side, center, width, height, DEFAULT_WIDTH_MARGIN, false);
        addUnattachedTargets(targets);
    }
    public SlotGroup(ISlotGeometryBuilder builder, ISlotableItem parent, int slotNumber, SlotSide side, Point2D center, float width, float height){
        this(builder, parent, slotNumber, side, center, width, height, DEFAULT_WIDTH_MARGIN, false);
    }
    public SlotGroup(ISlotGeometryBuilder builder, ISlotableItem parent, Collection<SlotTarget> targets, SlotSide side, Point2D center, float width, float height){
        this(builder, parent, targets.size(), side, center, width, height, DEFAULT_WIDTH_MARGIN, false);
        addUnattachedTargets(targets);
    }
    public SlotGroup(ISlotGeometryBuilder builder, ISlotableItem parent, Collection<SlotTarget> targets, SlotSide side, Point2D center, float width, float height, boolean isFlipped90){
        this(builder, parent, targets.size(), side, center, width, height, DEFAULT_WIDTH_MARGIN, isFlipped90);
        addUnattachedTargets(targets);
    }
    public SlotGroup(ISlotGeometryBuilder builder, ISlotableItem parent, int slotNumber, SlotSide side, Point2D center, float width, float height, float margin, boolean isFlipped90){
        if(width==0 || height==0)
            throw new IllegalArgumentException("SlotGroup width is " + width + " and height is " + height + " which is forbidden");
        this.builder = builder;
        this.parent = parent;
        this.side = side;
        this.slotNumber = slotNumber;
        this.slotPathPoint = new HashMap<Integer,Point2D>();
        this.slotAnchorPoint = new HashMap<Integer,Point2D>();
        this.slotPath = ArrayListMultimap.create();//new HashMap<Integer,IPath>();//HashBiMap.create();
        this.slotTarget = ArrayListMultimap.create();
        this.flipped90 = isFlipped90;
        setGeometry(center, width, height, margin, true);
    }

    /* ACCESS SLOT MODEL */

    /** Return the first available slot, or -1 if none.*/
    public int getNextFreeSlot(){
        for (int i = 0; i < slotNumber; i++) {
            if(isFree(i))
                return i;
        }
        return -1;
    }

    public boolean contains(SlotTarget t) {
        return getSlotTargets().contains(t);
    }

    // TODO: Ce serait plus pratique d'avoir une liste indexable!!
    @Override
    public Collection<SlotTarget> getSlotTargets() {
        return slotTarget.values();
    }

    public List<SlotTarget> getSlotTarget(int slot) {
        return slotTarget.get(slot);
    }

    @Override
	public ArrayListMultimap<Integer,SlotTarget> getAllSlotTargets(){
        return slotTarget;
    }

    @Override
    public void setSlotTargetAt(int i, final SlotTarget slotTarget){
        this.slotTarget.put(i, slotTarget);
    }

    @Override
    public void clear(){
        this.slotTarget.clear();
    }

    /** Format slot target information for the given slot.
     * If several {@link SlotTarget} are held at this slot id, the label
     * will gather all slot target info unless they repeat the same information.
     *
     */
    public String getSlotTargetInterfaceLabels(int slot, String separator) {
        List<SlotTarget> targets = slotTarget.get(slot);

        if(targets.size()==1){
            return formatInterface(targets.get(0));
        }
        else if(targets.size()==0){
            return "SlotGroup.getSlotTargetInterfaceLabels: no slot target";
        }
        else{
            StringBuffer fullLabel = new StringBuffer();
            for(int i=0; i<targets.size(); i++){
                String thisTargetLabel = formatInterface(targets.get(i));

                if(canAddLabel(fullLabel, thisTargetLabel)){
                    if(i<(targets.size()-1))
                        fullLabel.append(thisTargetLabel+separator);
                    else
                        fullLabel.append(thisTargetLabel);
                }
                else
                    ;// do not repeat an existing string

            }
            return fullLabel.toString();
        }
    }
    protected boolean canAddLabel(StringBuffer b, String label) {
        return !b.toString().contains(label);
    }

    protected String formatInterface(SlotTarget target){
        //return ((target.getInterface()==null)?"null":target.getInterface().toString());
        if(target.getInterface()==null)
            return "SlotGroup.format: slot interface is null";
        // unknown kind of interface
        else
            return target.getInterface().toString();
    }

    /** -1 if no slot holds this target. */
    public int getTargetSlot(SlotTarget target) {
        for(Integer slotId: slotTarget.keySet()){
            List<SlotTarget> targets = slotTarget.get(slotId);
            if(targets.contains(target))
                return slotId;
        }
        return -1;
    }

    /** Returns the slot that hold the given item as a target.*/
    public List<Integer> findTargetSlotHolding(ISlotableItem target) {
        List<Integer> list = new ArrayList<Integer>();
        for(Integer i: slotTarget.keySet()){
            List<SlotTarget> targets = slotTarget.get(i);
            for(SlotTarget t: targets){
                if(t.getTarget()!=null && t.getTarget().equals(target))
                    list.add(i);
            }
        }
        return list;
    }

    public List<Integer> findTargetSlotHoldingWithLink(ISlotableItem target, Object link) {
        List<Integer> list = new ArrayList<Integer>();
        for(Integer i: slotTarget.keySet()){
            List<SlotTarget> targets = slotTarget.get(i);
            for(SlotTarget t: targets){
                if(t.getTarget()!=null && t.getTarget().equals(target) && t.getLink().equals(link))
                    list.add(i);
            }
        }
        return list;
    }

    /** Returns the slot that hold the given item as a target with the given interface.*/
    public List<Integer> findTargetSlotHolding(ISlotableItem target, Object intrface) {
        List<Integer> list = new ArrayList<Integer>();
        for(Integer i: slotTarget.keySet()){
            List<SlotTarget> targets = slotTarget.get(i);
            for(SlotTarget t: targets)
                if(t.getTarget().equals(target) && t.getInterface().equals(intrface))
                    list.add(i);
        }
        return list;
    }

    public SlotSide getSide() {
        return side;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isFree(int slot){
        return !slotPath.containsKey(slot);
    }

    public double distanceSq(Point2D mouse, int slotid){
        return mouse.distanceSq(getSlotAnchorPoint(slotid));
    }

    /* PATHES */

    /** Setup the path object attached to this slot.*/
    public void attach(int slot, IPath path) {
        //if(this.slotPath.containsKey(slot))
        //    throw new RuntimeException("Slot group already has a path attached to this slot:" + slot);
        this.slotPath.put(slot, path);
    }

    /*public IPath getSlotPath(int slot) {
        return slotPath.get(slot);
    }

    public int getPathSlot(IPath path) {
        return slotPath.inverse().get(path);
    }*/

    /* ACCESS SLOT GEOMETRY */

    public Point2D getSlotAnchorPoint(int slot){
        return slotAnchorPoint.get(slot);
    }

    public Point2D getSlotPathPoint(int slot){
        return slotPathPoint.get(slot);
    }

    public Point2D getCenter() {
        return center;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMargin() {
        return margin;
    }

    public float getSlotAnchorWidth(){
        return slotAnchorWidth;
    }

    /* EDIT SLOT GEOMETRY */

    public void setGeometry(Point2D center, float width, float height, float margin){
        setGeometry(center, width, height, margin, true);
    }

    public void setGeometry(Point2D center, float width, float height, float margin, boolean rebuild){
        this.center = center;
        this.width = width;
        this.height = height;
        this.margin = margin;

        if(rebuild)
            rebuild();
    }

    public void rebuild(){
        builder.build(this);
    }

    public void setSlotAnchorPoint(int slot, Point2D pt){
        slotAnchorPoint.put(slot, pt);
    }

    public void setSlotPathPoint(int slot, Point2D pt){
        slotPathPoint.put(slot, pt);
    }

    public void setSlotAnchorWidth(float width){
        slotAnchorWidth = width;
    }

    /* LAYOUT INFORMATIONS */
    @Override
    public boolean isNorth(){
        return isNorth(side);
    }

    @Override
    public boolean isSouth(){
        return isSouth(side);
    }

    @Override
    public boolean isEast(){
        return isEast(side);
    }

    @Override
    public boolean isWest(){
        return isWest(side);
    }

    public boolean isVertical(){
        return isVertical(side);
    }

    public boolean isHorizontal(){
        return isHorizontal(side);
    }

    public boolean isFlipped90() {
        return flipped90;
    }

    public void setFlipped90(boolean flipped90) {
        this.flipped90 = flipped90;
    }

    /* */

    protected boolean isVertical(SlotSide side){
        return isEast(side) || isWest(side);
    }

    protected boolean isHorizontal(SlotSide side){
        return isNorth(side) || isSouth(side);
    }

    protected boolean isNorth(SlotSide side){
        return SlotSide.NORTH.equals(side);
    }

    protected boolean isSouth(SlotSide side){
        return SlotSide.SOUTH.equals(side);
    }

    protected boolean isEast(SlotSide side){
        return SlotSide.EAST.equals(side);
    }

    protected boolean isWest(SlotSide side){
        return SlotSide.WEST.equals(side);
    }

    /* TEMPORARY SLOT MODEL */

    public void addUnattachedTarget(SlotTarget target){
        unattachedTargets.add(target);
    }

    public void addUnattachedTargets(Collection<SlotTarget> targets){
        unattachedTargets.addAll(targets);
    }

    public List<SlotTarget> getUnattachedTarget(){
        return unattachedTargets;
    }

    /****************/

    @Override
	public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("SlotGroup " + side + " " + parent.toString() + "\n");
        for(Integer i: slotTarget.keySet()){
            List<SlotTarget> st = slotTarget.get(i);
            for(SlotTarget s: st)
                sb.append("  " + i + ": target=" + s.getTarget() + " with link=" + s.getLink() + "\n");
        }
        return sb.toString();
    }

    /****************/

    protected ISlotableItem parent;

    protected List<SlotTarget> unattachedTargets = new ArrayList<SlotTarget>();

    protected Point2D center;
    protected float width;
    protected float height;
    protected float margin;
    protected SlotSide side;
    protected int slotNumber;
    protected float slotAnchorWidth;

    // slot content
    //protected Map<Integer,IPath> slotPath;
    protected ArrayListMultimap<Integer,IPath> slotPath;
    protected ArrayListMultimap<Integer,SlotTarget> slotTarget;
    //protected ArrayListMultimap<Integer,Slot<T>> slotItem;

    // slot geometry
    protected Map<Integer,Point2D> slotPathPoint;
    protected Map<Integer,Point2D> slotAnchorPoint;

    protected ISlotGeometryBuilder builder;
}

package org.datagr4m.drawing.layout.slots;

import java.awt.geom.Point2D;
import java.util.List;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryPostProcessor;
import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.pathfinder.obstacle.ObstacleGroupModel;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.ISlotableSetModel;
import org.datagr4m.drawing.model.slots.SlotGroup;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.model.slots.SlotTarget;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/** This algorithm initializes paths between the two extremity of a link in two steps:
 * <ul>
 * <li>First, analyzes relative position of each extremity to define an ideal slot group for source and target.
 * <li>Second, create a two points path at the appropriate slot coordinate.
 * </ul>
 * 
 * Warning: in this implementation, no two Link can be equals. 
 */
public class SlotLayout implements ISlotLayout{
    private static final long serialVersionUID = 4354618616262994431L;
    
    protected SlotLayoutConfiguration configuration = new SlotLayoutConfiguration();
    protected boolean DEFAULT_PATH_LOCK = false;

    protected boolean isAddMode = false;
    protected ISlotGroupLayout slotGroupLayout;
    protected ISlotGeometryPostProcessor slotGeometryPostProcessor;
    protected Multimap<ISlotableItem,SlotTarget> slotNorthList = ArrayListMultimap.create();
    protected Multimap<ISlotableItem,SlotTarget> slotSouthList = ArrayListMultimap.create();
    protected Multimap<ISlotableItem,SlotTarget> slotEastList = ArrayListMultimap.create();
    protected Multimap<ISlotableItem,SlotTarget> slotWestList = ArrayListMultimap.create();
    
    protected IPathFactory pathFactory;
    
    /* */
    
    /** 
     * Creates an initializer in add mode, meaning the path objects do not
     * exist yet in the {@link ISlotableSetModel} input
     */
    public SlotLayout(IPathFactory factory){
        this(true, factory);
    }
    
    public SlotLayout(){
        this(new PathFactory());
    }

    
    /** 
     * Call with false to have a ModifMode on.
     */
    public SlotLayout(boolean isAddMode, IPathFactory factory){
        this(isAddMode, null, null, factory);
    }

    public SlotLayout(boolean isAddMode, ISlotGroupLayout sgLayout, ISlotGeometryPostProcessor postProcessor, IPathFactory factory){
        this.isAddMode = isAddMode;
        if(sgLayout==null)
            this.slotGroupLayout = new SlotGroupLayout();
        else
            this.slotGroupLayout = sgLayout;
        this.slotGeometryPostProcessor = postProcessor;
        
        this.pathFactory = factory;
    }
    
    @Override
    public void initialize(ISlotableSetModel set, List<ILink<ISlotableItem>> links) {
        if(links!=null)
            set.fillMissingItems(links);
        clearSlots(set);
        initializeSlotTargets(links);
        initializeSlots();
        organizeSlotTargets(set);
        
        if(slotGeometryPostProcessor!=null){
            slotGeometryPostProcessor.postprocess(set);
        }
        
        initializePathes(set);
    }

    /** 
     * A convenient method that creates and fill an {@link ObstacleGroupModel} instance.
     * Should be called in AddMode On.
     */
    @Override
    public ISlotableSetModel initialize(List<ILink<ISlotableItem>> links) {
        ISlotableSetModel slotableGroup = new ObstacleGroupModel();
        initialize(slotableGroup, links);
        return slotableGroup;
    }
    
    /* INITIALIZE SLOTS */
    
    protected void clearSlots(ISlotableSetModel group){
        for(ISlotableItem source: group.getItems()){
            synchronized(source.getSlotGroups()){
                source.clearSlots();
            }
        }
    }
    
    /** 
     * 1) Choose a best slot group for the source & target of each link.
     * Once this method finishes, we have a map (source,side)->(target,link)
     * that is made for both source and target, but slot are not initialized yet.
     */
    protected void initializeSlotTargets(List<ILink<ISlotableItem>> links){
        slotNorthList.clear();
        slotSouthList.clear();
        slotWestList.clear();
        slotEastList.clear();
        
        for(ILink<ISlotableItem> link: links){
            ISlotableItem o1 = link.getSource();
            ISlotableItem o2 = link.getDestination();
            SlotSide o1Slot = slotGroupLayout.getTargetBestSlotSide(o1, o2);
            SlotSide o2Slot = slotGroupLayout.getTargetBestSlotSide(o2, o1);   
            
            incrementSlotList(o1, o1Slot, new SlotTarget(o2, link.getSourceInterface() /*"to " + o2*/, link));
            incrementSlotList(o2, o2Slot, new SlotTarget(o1, link.getTargetInterface()/*"to " + o1*/, link));
        }
    }
    
    protected void incrementSlotList(ISlotableItem source, SlotSide side, SlotTarget target){
        if(SlotSide.NORTH.equals(side))
            incrementSlotList(source, slotNorthList, target);
        else if(SlotSide.SOUTH.equals(side))
            incrementSlotList(source, slotSouthList, target);
        else if(SlotSide.EAST.equals(side))
            incrementSlotList(source, slotEastList, target);
        else if(SlotSide.WEST.equals(side))
            incrementSlotList(source, slotWestList, target);
        else
            throw new IllegalArgumentException("unhandled side:" + side);
    }
    
    protected void incrementSlotList(ISlotableItem source, Multimap<ISlotableItem,SlotTarget> list, SlotTarget target){
        if(list.get(source).contains(target))
            ;//System.err.println("SlotInitializer.incrementSlotList source contains target:" + source + " > " + target);
        else
            list.put(source, target);
    }
    
    /* */
    
    /**
     * 2) Create slot groups for each item, containing "unattached" targets,
     * meaning their slot id is not setup yet.
     */
    protected void initializeSlots(){
        for(ISlotableItem o: slotNorthList.keySet())
            o.addNorthSlot(slotNorthList.get(o));
        for(ISlotableItem o: slotSouthList.keySet())
            o.addSouthSlot(slotSouthList.get(o));
        for(ISlotableItem o: slotEastList.keySet())
            o.addEastSlot(slotEastList.get(o));
        for(ISlotableItem o: slotWestList.keySet())
            o.addWestSlot(slotWestList.get(o));
    }
    

    /* ORGANIZE SLOTS AND INITIALIZE PATHES */
    
    /**
     * 3) Choose the best slot id for each target of each source.
     * When this method completes, the slot group knows from which
     * slot we should start a path to reach the target
     */
    protected void organizeSlotTargets(ISlotableSetModel group){
        for(ISlotableItem source: group.getItems())
            for(SlotGroup sourceSlotGroup: source.getSlotGroups())
                organizeSlotTargets(source, sourceSlotGroup);
    }
    
    protected void organizeSlotTargets(ISlotableItem item, SlotGroup slotGroup){
        List<SlotTarget> unattachedTargets = slotGroup.getUnattachedTarget();
        int[] order = slotGroupLayout.getSlotTargetBestId(item, slotGroup, unattachedTargets);
        
        for (int i = 0; i < unattachedTargets.size(); i++) {
            slotGroup.setSlotTargetAt(order[i], unattachedTargets.get(i));
            //slotGroup.setSlotTargetAt(0, unattachedTargets.get(i));
        }
    }

    /* INITIALIZE OR EDIT PATHES */

    /** 
     * 4) Either create the actual path, by defining the two (x,y) end points,
     * 
     * or modifies an existing path!!! on peut avoir plusieurs link A->B entre deux
     * slot groups :(
     * 
     * After path initialization, the slotable group will have registered pathes,
     * in other word, for each input link, the following method is called:
     *  group.addPath(source, p, target);
     * @param group
     */
    protected void initializePathes(ISlotableSetModel group){
        //isAddMode = true;
        for(ISlotableItem source: group.getItems()){
            for(SlotGroup sourceSlotGroup: source.getSlotGroups()){
                for(SlotTarget slotTarget: sourceSlotGroup.getSlotTargets()){
                    Object sourceSlotlink = slotTarget.getLink();
                    
                    if(sourceSlotlink == null)
                        continue; // do not work with slot holding no link (no path)
                    
                    ISlotableItem target = slotTarget.getTarget();
                    
                    // ensure that the stored link really consider this target
                    // as the target and not as the source,
                    // otherwise, we ignore it, since it will be added later
                    boolean ok = false;
                    if(sourceSlotlink instanceof ILink){
                        if(((ILink<?>)sourceSlotlink).getDestination().equals(target))
                            ok = true;
                    }
                    else
                        throw new IllegalArgumentException("don't know how to deal with with link type: " + sourceSlotlink.getClass());
                    
                    if(ok){
                        addOrEditPath(group, source, sourceSlotGroup, slotTarget, sourceSlotlink, target);
                    }
                }
            }
        }
        //isAddMode = false;
    }

    protected void addOrEditPath(ISlotableSetModel group, ISlotableItem source, SlotGroup sourceSlotGroup, SlotTarget slotTarget, Object link, ISlotableItem target) throws RuntimeException {
        SlotGroup targetSlotGroup = findSlotGroupWithLink(target, source, link);
        // findSlotGroup(target, source);
        
        if(targetSlotGroup==null && configuration.isCrashOnMissingTargetGroup())
            throw new RuntimeException("source has a target that do not know source has a target! (asymetric)");
        else{
            SlotTarget sourceSlot = slotTarget;
            SlotTarget targetSlot = findTargetSlotWithLink(source, sourceSlot, targetSlotGroup, link);
            //SlotTarget targetSlot = findTargetSlot(source, sourceSlot, targetSlotGroup);
            
            
            // ----------------
            // we simply add a new path for the given link
            if(isAddMode){                                
                IPath p = createAndRegisterPath(source, target, link, sourceSlotGroup, targetSlotGroup, sourceSlot.getInterface(), targetSlot.getInterface());
                
                if(!configuration.isConsiderInterfaceForPath())
                    group.addPath(source, p, target);
                else
                    group.addPath(source, sourceSlot.getInterface(), p, target, targetSlot.getInterface());
            }
            
            // ----------------
            // modif mode
            else{
                // Retrieve path to be modified
                List<IPath> pathes = null;
                
                if(!configuration.isConsiderInterfaceForPath())
                    pathes = group.getPathes(source, target);
                else{
                    /*if(sourceSlot.getInterface()==null || targetSlot.getInterface()==null){
                        pathes = group.getPathes(source, target);
                        //throw new RuntimeException("missing interface in slot source or target, can't getPath(source,infS,target,infT). Should seek without interface?");
                    }
                    else*/
                    pathes = group.getPathes(source, sourceSlot.getInterface(), target, targetSlot.getInterface());
                }
                
                // -------------------------
                // found the right path
                if(pathes.size()==1){
                    IPath path = pathes.get(0);
                    editExistingPath(source, target, link, sourceSlotGroup, targetSlotGroup, path, sourceSlot.getInterface(), targetSlot.getInterface());
                }
                // -------------------------
                // found NO path
                else if(pathes==null || pathes.size()==0){
                    String error = "missing path for source/target in modif mode:\n" + slotsInfo(source, target, sourceSlot, targetSlot);
                    if(configuration.isCrashOnMissingPath()){
                        //group.logLinks();
                        throw new IllegalArgumentException(error);
                    }
                    else
                        Logger.getLogger(this.getClass()).error(this.getClass().getSimpleName() + ": " + error);
                }
                // -------------------------
                // found too many path
                else{
                    StringBuffer sb = new StringBuffer();
                    sb.append("found more than 1 path in modif mode for: " + slotsInfo(source, target, sourceSlot, targetSlot));
                    for(IPath p: pathes)
                        sb.append("\n" + p);
                    
                    if(configuration.isCrashOnDupplicatePath())
                        throw new IllegalArgumentException(this.getClass().getSimpleName() + ": " + sb.toString());
                    else
                        Logger.getLogger(this.getClass()).error(this.getClass().getSimpleName() + ": " + sb.toString());
                }
            }
        }
    }
    
    protected SlotTarget findTargetSlotWithLink(ISlotableItem source, SlotTarget sourceSlot, SlotGroup targetSlotGroup, Object link) {
        ArrayListMultimap<Integer,SlotTarget> tsg = targetSlotGroup.getAllSlotTargets();
        
        for(Integer graphicalSlot: tsg.keySet()){
            List<SlotTarget> targets = tsg.get(graphicalSlot);
            
            for(SlotTarget st : targets){
                if(st.getTarget().equals(source) && st.getLink().equals(link))
                    return st;
            }
        }
        throw new RuntimeException("No targets hold source item: " + source + "\nfor link " + link);
    }

    
    @Deprecated 
    // conserve cette methode pour roll back au cas ou
    protected SlotTarget findTargetSlot(ISlotableItem source, SlotTarget sourceSlot, SlotGroup targetSlotGroup) {
        // ---------------------
        // cherche les slot de la cible qui portent la source
        List<Integer> inFrontHolding = targetSlotGroup.findTargetSlotHolding(source);
        
        // ATTENTION ON PEUT AVOIR PLUSIEURS FOIS LE MEME ID DE SLOT PORTANT DES LIENS DIFFERENDS
        
        
        Integer selection = null; // id du slot final
        
        
        // if only one found target slot, then no ambiguity
        if(inFrontHolding.size()==1)
            selection = inFrontHolding.get(0);
        
        // if several found, we check the one holding the same ILink than the source
        else if(inFrontHolding.size()>1){
            for(Integer in: inFrontHolding){ // pour chaque slot graphique en face portant la source
                List<SlotTarget> allSlots = targetSlotGroup.getSlotTarget(in);
                
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                SlotTarget t = allSlots.get(0); //POURQUOI 0???
                
                if(t.getLink()==sourceSlot.getLink()){ // si le slot source et slot cible portent le m�me lien
                    if(selection==null)
                        selection = in;
                    else{
                        String message = "found several targets holding a shared link with source target\n";
                        // en d'autre terme il y a plusieurs liens entre A et B via les m�mes interfaces?
                        StringBuffer sb = new StringBuffer();
                        sb.append(message);
                        //sb.append(" graphical slot " + selection + " holds link " + sourceSlot.getLink()+"\n");
                        for(Integer inDebug: inFrontHolding){
                            sb.append(" graphical slot " + inDebug + " holds: \n");
                            
                            List<SlotTarget> sts = targetSlotGroup.getSlotTarget(inDebug);
                            for(SlotTarget s: sts)
                                sb.append("   " + s);
                        }
                        //sb.append(targetSlotGroup.toString());
                        //sb.append(link);
                        //sb.append("All slots:" + allSlots);
                        
                        targetSlotGroup.findTargetSlotHolding(source);
                        
                        throw new RuntimeException(sb.toString());
                    }
                }
            }
            if(selection==null)
                throw new RuntimeException("found no target slot sharing the same link object");
                
            //throw new RuntimeException("Many targets hold this item: " + inFrontHolding + "\nfor link " + link);
        }
        
        // if no slot found holding this source
        else if(inFrontHolding.size()==0)
            throw new RuntimeException("No targets hold this item: " + source);
        
        // recup�re le slot targetSlot portant le m�me lien
        List<SlotTarget> targets = targetSlotGroup.getSlotTarget(selection);
        SlotTarget targetSlot = targets.get(0); // POURQUOI 0????
        
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        
        return targetSlot;
    }

    
    
    /* CREATE / EDIT / REGISTER PATH */
     
    protected IPath createAndRegisterPath(ISlotableItem source, ISlotableItem target, Object link, SlotGroup sourceSlotGroup, SlotGroup targetSlotGroup, Object inf1, Object inf2){
        int sourceSlotId = sourceSlotGroup.getTargetSlot(new SlotTarget(target, inf1, link));
        int targetSlotId = targetSlotGroup.getTargetSlot(new SlotTarget(source, inf2, link));
        
        if(sourceSlotId == -1)
            throw new RuntimeException("error source slot group does not contain target");
        if(targetSlotId == -1)
            throw new RuntimeException("error target slot group does not contain source");
        
        Point2D sourcePoint = sourceSlotGroup.getSlotPathPoint(sourceSlotId);
        Point2D targetPoint = targetSlotGroup.getSlotPathPoint(targetSlotId);
        
        if(sourcePoint == null)
            throw new RuntimeException("error sourcePoint is null for source slot id " + sourceSlotId);
        if(targetPoint == null)
            throw new RuntimeException("error targetPoint is null for target slot id " + targetSlotId);
        
        IPath path = pathFactory.newPath(sourcePoint, targetPoint, DEFAULT_PATH_LOCK, DEFAULT_PATH_LOCK);
        sourceSlotGroup.attach(sourceSlotId, path);
        targetSlotGroup.attach(targetSlotId, path);
        return path;
    }
    
    protected IPath editExistingPath(ISlotableItem source, ISlotableItem target, Object link, SlotGroup sourceSlotGroup, SlotGroup targetSlotGroup, IPath path, Object inf1, Object inf2){
        int sourceSlotId = sourceSlotGroup.getTargetSlot(new SlotTarget(target, inf1, link)); // id of slot on source device to reach target
        int targetSlotId = targetSlotGroup.getTargetSlot(new SlotTarget(source, inf2, link));
        
        if(sourceSlotId == -1)
            throw new RuntimeException("error source slot group does not contain target");
        if(targetSlotId == -1)
            throw new RuntimeException("error target slot group does not contain source");
        
        Point2D sourcePoint = sourceSlotGroup.getSlotPathPoint(sourceSlotId);
        Point2D targetPoint = targetSlotGroup.getSlotPathPoint(targetSlotId);
        
        if(sourcePoint == null)
            throw new RuntimeException("error sourcePoint is null for " + source + " on source slot id " + sourceSlotId + " of " + sourceSlotGroup.getSide());
        if(targetPoint == null)
            throw new RuntimeException("error targetPoint is null for " + target + " on source slot id " + targetSlotId + " of " + targetSlotGroup.getSide());
        
        
        synchronized(path.getPoints()){
            path.clear(); //  LA DIFFERENCE AVEC createAndRegisterPath EST LA!!
            path.add(sourcePoint, DEFAULT_PATH_LOCK);
            path.add(targetPoint, DEFAULT_PATH_LOCK);
            sourceSlotGroup.attach(sourceSlotId, path);
            targetSlotGroup.attach(targetSlotId, path);
        }
        
        return path;
    }
    
    /* */

    // retourne le slot group qui contient le slot permettant 
    // de pointer la cible avec cette instance de lien donn�e
    public SlotGroup findSlotGroup(ISlotableItem container, ISlotableItem target){
        for(SlotGroup group: container.getSlotGroups()){
            
            List<Integer> slots = group.findTargetSlotHolding(target);
            
            if(slots.size()>0){
                List<SlotTarget> stl = group.getSlotTarget(slots.get(0));
                if(stl.size()>0)
                return group;
            }
        }
        return null;
    }
    
    public SlotGroup findSlotGroupWithLink(ISlotableItem container, ISlotableItem target, Object link){
        for(SlotGroup group: container.getSlotGroups()){
            List<Integer> slots = group.findTargetSlotHoldingWithLink(target, link);
            if(slots.size()>0)
                return group;
        }
        return null;
    }
    
    /*public SlotGroup findSlotGroupHoldingLink(ISlotableItem container, ISlotableItem target, Object link){
        for(SlotGroup group: container.getSlotGroups()){
            
            //group.findTargetSlotHolding(target, null);
            
            if(group.contains(new SlotTarget(target, null, link)))
                return group;
        }
        return null;
    }*/
    
    public SlotGroup findSlotGroupHolding(ISlotableItem container, ISlotableItem target, Object intrface){
        for(SlotGroup group: container.getSlotGroups()){
            
            List<Integer> slots = group.findTargetSlotHolding(target, intrface);
            
            if(slots.size()>0){
                List<SlotTarget> stl = group.getSlotTarget(slots.get(0));
                if(stl.size()>0)
                return group;
            }
        }
        return null;
    }
    

    public SlotLayoutConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SlotLayoutConfiguration configuration) {
        this.configuration = configuration;
    }

    /* UTILS */
    
    /** Format slot informations */
    protected String slotsInfo(ISlotableItem source, ISlotableItem target, SlotTarget sourceSlot, SlotTarget targetSlot) {
        StringBuffer sb = new StringBuffer();
        sb.append(" source: " + source);
        if(sourceSlot.getInterface() instanceof String)
            sb.append("(str: "+sourceSlot.getInterface()+ ") \n");
        else
            sb.append("(???: "+sourceSlot.getInterface()+ ") \n");
        
        sb.append(" target: " + target);
        
        if(targetSlot.getInterface() instanceof String)
            sb.append("(str: "+targetSlot.getInterface()+ ")");
        else
            sb.append("(???: "+targetSlot.getInterface()+ ")");
        
        return sb.toString();
    }
}

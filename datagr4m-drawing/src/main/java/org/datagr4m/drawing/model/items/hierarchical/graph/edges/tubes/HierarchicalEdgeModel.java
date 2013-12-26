package org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.ListUtils;
import org.apache.log4j.Logger;
import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.ItemState;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.Edge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.DefaultEdgeInfo;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.infos.IEdgeInfo;
import org.datagr4m.drawing.model.links.DirectedLink;
import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.links.ILinkFactory;
import org.datagr4m.drawing.model.links.LinkPoolFactory;
import org.datagr4m.drawing.model.pathfinder.obstacle.IPathObstacle;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.maths.geometry.PointUtils;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.graph.IPropertyEdge;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import edu.uci.ics.jung.graph.Graph;

public class HierarchicalEdgeModel implements IHierarchicalEdgeModel, Serializable {
    private static final String DEFAULT_INTERFACE_OBJECT = "interface";

    private static final long serialVersionUID = 3206985350656775071L;

    public static boolean DEBUG = false;    
    public static boolean ASYMETRIC_HIERARCHY_SUPPORT = true;

    /**************/

    public HierarchicalEdgeModel() {
        this.pathFactory = new PathFactory();
        
        this.rootTubes = new ArrayList<Tube>();
        this.itemToObstacle = HashBiMap.create();
        this.tubeMap = new HashMap<CommutativePair<IBoundedItem>, Tube>();
        this.internalEdges = new ArrayList<IEdge>();
        this.rawEdges = new ArrayList<IEdge>();
        this.flattenList = new ArrayList<IEdge>();
        this.itemsPathes = ArrayListMultimap.create();
        this.noTubes = new ArrayList<CommutativePair<String>>();
    }
    
    /* GENERAL TUBE MODEL BUILD METHODS */

    /**
     * Supports a list of pairs as input to setup an actual list of tubeable
     * edges.
     */
    @Override
    public void build(Collection<Pair<IBoundedItem, IBoundedItem>> links) {
        // create a tube for each link
        for (Pair<IBoundedItem, IBoundedItem> link : links) {
            IEdge tree = build(link, null, null, null);
            if (DEBUG)
                Logger.getLogger(HierarchicalEdgeModel.class).info("\nBUILDED:" + tree);
        }
        flatten();
    }

    /**
     * Read the list of edges found in the topology global graph, in order to
     * extract actual network informations
     */
    @Override
    public <V,E> void build(Topology<V, E> topology, IHierarchicalModel model) {
        Graph<V, E> graph = topology.getGraph();

        for (E edge : graph.getEdges()) {
            V source = graph.getSource(edge);
            V target = graph.getDest(edge);

            if (source == null || target == null) {
                throw new IllegalArgumentException("source or dest are null. Maybe jung graph is not oriented");
            } else {
                IBoundedItem is = model.getItem(source);
                IBoundedItem it = model.getItem(target);

                if (is == null) {
                    model.toConsole();
                    throw new IllegalArgumentException("Could not find IBoundedItem representation for source in the hierarchical model\nsource:" + source + "\ntarget:" + target + "\nMost probably referenced by an edge, but not available in the group hierarchy");

                }
                if (it == null) {
                    model.toConsole();
                    throw new IllegalArgumentException("Could not find IBoundedItem representation for dest in the hierarchical model\nsource:" + source + "\ntarget:" + target + "\nMost probably referenced by an edge, but not available in the group hierarchy");

                }
                handleEdge(edge, is, it);
            }
        }
        flatten();
    }
    
    protected <E> void handleEdge(E edge, IBoundedItem is, IBoundedItem it){
        CommutativePair<IBoundedItem> link = new CommutativePair<IBoundedItem>(is, it);
        
        // Define interface if any or default
        String srcInterface = DEFAULT_INTERFACE_OBJECT;
        String trgInterface = DEFAULT_INTERFACE_OBJECT;
        if(edge instanceof IPropertyEdge){
            IPropertyEdge pe = (IPropertyEdge)edge;
            if(pe.getProperties().containsKey(IPropertyEdge.PROPERTY_SOURCE_INTERFACE))
                srcInterface = pe.getProperties().get(IPropertyEdge.PROPERTY_SOURCE_INTERFACE).toString();
            if(pe.getProperties().containsKey(IPropertyEdge.PROPERTY_TARGET_INTERFACE))
                trgInterface = pe.getProperties().get(IPropertyEdge.PROPERTY_TARGET_INTERFACE).toString();
        }
        
        build(link, srcInterface, trgInterface, new DefaultEdgeInfo(){});
    }
    
    /* BUILDING TUBE MODEL */

    /**
     * Verify the existence of a parent tube, and create one if it does not
     * exists yet. Recursively creates parent tubes for each created tube.
     */
    @Override
    public IEdge build(Pair<IBoundedItem, IBoundedItem> link, Object srcInterface, Object trgInterface, IEdgeInfo info) {
        if (link.a == null || link.b == null)
            throw new IllegalArgumentException("one or two of the link items are null.");
        if (DEBUG)
            Logger.getLogger(HierarchicalEdgeModel.class).info("\n----- BUILDING LINK: " + link.a.getObject() + " / " + link.b.getObject());

        // Create the raw edge for this device link
        IEdge edge = createEdge(link, srcInterface, trgInterface, info); // orient�
        rawEdges.add(edge);

        // If parents of each edge side are different,
        // we need to create a tube for the edge
        CommutativePair<IBoundedItem> parentKey = createSmartParentTubeKey(link);
        return buildParent(parentKey, edge);
    }

    protected CommutativePair<IBoundedItem> createSmartParentTubeKey(Pair<IBoundedItem, IBoundedItem> link) {
        IHierarchicalModel p1 = link.a.getParent();
        IHierarchicalModel p2 = link.b.getParent();

        if (p1.hasDescendant(p2))
            return createTubeKey(link.a, p2);
        else if (p2.hasDescendant(p1))
            return createTubeKey(p1, link.b);
        else
            return createTubeKey(p1, p2);
    }

    protected IEdge buildParent(CommutativePair<IBoundedItem> parentKey, IEdge edge) {
        if (parentKey.a == null || parentKey.b == null)
            throw new IllegalArgumentException("Only the root can have a null parent, and the root can't have a tube to somewhere else");
        
        /*if(parentKey.a.intersects(parentKey.b))
            return edge;*/
        
        // ------------
        // cas normal: on continue la r�cursion vers le haut
        if (areInDifferentAndNonNestedGroups(parentKey)) {
            Tube tube = getOrCreateTube(parentKey); // non orient�
            tube.addChild(edge); // register edge as child
            edge.setParent(tube); // register tube as parent
            return recursiveParentTubeAdd(tube);
        } 
        // ------------
        // test groupe similaire, doit �tre fait avant nested hierarchy 
        // qui retourne vrai pour a.getDescendants(true).contains(a)
        else if(areInSimilarGroups(parentKey)){
            if (!internalEdges.contains(edge)) {
                internalEdges.add(edge);
            }
            return edge;
        }
        // -------------
        // pour les tubes asym�trique (un parent contient l'autre)
        else if (ASYMETRIC_HIERARCHY_SUPPORT && isNestedHierarchyLeftHigher(parentKey)) {
            CommutativePair<IBoundedItem> pseudoParentKey = new CommutativePair<IBoundedItem>(edge.getSourceItem(), parentKey.b);
            Tube tube = getOrCreateTube(pseudoParentKey);
            if (!tube.hasChildren(edge)){
                tube.addChild(edge);
                edge.setParent(tube); // register tube as parent
            }
            return recursiveParentTubeAdd(tube);
        } 
        else if (ASYMETRIC_HIERARCHY_SUPPORT && isNestedHierarchyRightHigher(parentKey)) {
            CommutativePair<IBoundedItem> pseudoParentKey = new CommutativePair<IBoundedItem>(parentKey.a, edge.getTargetItem());
            Tube tube = getOrCreateTube(pseudoParentKey);
            if (!tube.hasChildren(edge)){
                tube.addChild(edge);
                edge.setParent(tube); // register tube as parent
            }
            return recursiveParentTubeAdd(tube);
        } 
        // -------------
        // fin de la recursion
        else {
            if (!internalEdges.contains(edge)) 
                internalEdges.add(edge);
            return edge;
        }
    }

    /**
     * If parents are different, retrieve or create a parent tube according to
     * they key, and attach the input tube to its parent if not done yet.
     * 
     * If parents are identical, only stores the input tube into the
     * internalEdge list.
     */
    protected IEdge buildParent(CommutativePair<IBoundedItem> parentKey, Tube tube) {
        if (parentKey.a == null || parentKey.b == null)
            throw new IllegalArgumentException("Only the root can have a null parent, and the root can't have a tube to somewhere else");

        
        // -------------
        // cas classique
        if (areInDifferentAndNonNestedGroups(parentKey)) {
            Tube parentTube = getOrCreateTube(parentKey);

            // add the input tube to its parent if not done yet
            if (!parentTube.hasChildren(tube))
                parentTube.addChild(tube);

            // do the same for the parent
            return recursiveParentTubeAdd(parentTube);
        } 
        // ------------
        // test groupe similaire, doit �tre fait avant nested hierarchy 
        // qui retourne vrai pour a.getDescendants(true).contains(a)
        else if(areInSimilarGroups(parentKey)){
            boolean didRoot = false;
            if(parentKey.a instanceof IHierarchicalModel){
                IHierarchicalModel hm = (IHierarchicalModel)parentKey.a;
                if(hm.isRoot()){
                    didRoot = true;                    
                    if (!rootTubes.contains(tube)) 
                        rootTubes.add(tube);
                }
            }
            if(!didRoot)
                if (!internalEdges.contains(tube)) 
                    internalEdges.add(tube);
            return tube;
        }
        // -------------
        // pour les tubes asym�trique (un parent contient l'autre)
        else if (ASYMETRIC_HIERARCHY_SUPPORT && isNestedHierarchyLeftHigher(parentKey)) {
            CommutativePair<IBoundedItem> pseudoParentKey = new CommutativePair<IBoundedItem>(tube.getSourceItem(), parentKey.b);
            Tube parentTube = getOrCreateTube(pseudoParentKey);
            if (!parentTube.hasChildren(tube))
                parentTube.addChild(tube);
            return recursiveParentTubeAdd(parentTube);
        } 
        else if (ASYMETRIC_HIERARCHY_SUPPORT && isNestedHierarchyRightHigher(parentKey)) {
            CommutativePair<IBoundedItem> pseudoParentKey = new CommutativePair<IBoundedItem>(parentKey.a, tube.getTargetItem());
            Tube parentTube = getOrCreateTube(pseudoParentKey);
            if (!parentTube.hasChildren(tube))
                parentTube.addChild(tube);
            return recursiveParentTubeAdd(parentTube);
        } 
        // -------------
        // fin de la recursion
        else {
            // Logger.getLogger(HierarchicalEdgeModel.class).info("added root tube: " + tube);
            if (!internalEdges.contains(tube)) {
                internalEdges.add(tube);
            }
            return tube;
        }
    }

    /**
     * Return true if:
     * <ul>
     * <li>both parent keys are differents (if they are similar, it means we
     * reach an internal tube)
     * <li>parent A is not a parent of B and vice verca
     * </ul>
     * 
     * @param parentKey
     * @return
     */
    protected boolean areInDifferentAndNonNestedGroups(CommutativePair<IBoundedItem> parentKey) {
        boolean areInDifferentGroups = parentKey.a != parentKey.b;
        return areInDifferentGroups && !isNestedHierarchy(parentKey);
    }
    
    protected boolean areInSimilarGroups(CommutativePair<IBoundedItem> parentKey) {
        boolean areInSmilarGroups = parentKey.a == parentKey.b;
        return areInSmilarGroups;
    }

    protected boolean isNestedHierarchy(CommutativePair<IBoundedItem> parentKey) {
        return isNestedHierarchyLeftHigher(parentKey) || isNestedHierarchyRightHigher(parentKey);
    }

    /** Left parent (a in key) has b as child.*/
    protected boolean isNestedHierarchyLeftHigher(CommutativePair<IBoundedItem> parentKey) {
        boolean nestedB = false;
        if (parentKey.a instanceof IHierarchicalModel) {
            List<IBoundedItem> descendants = ((IHierarchicalModel) parentKey.a).getDescendants(true);
            nestedB = descendants.contains(parentKey.b);
        }
        return nestedB;
    }

    /** Right parent (b in key) has a as child.*/
    protected boolean isNestedHierarchyRightHigher(CommutativePair<IBoundedItem> parentKey) {
        boolean nestedA = false;
        if (parentKey.b instanceof IHierarchicalModel) {
            List<IBoundedItem> descendants = ((IHierarchicalModel) parentKey.b).getDescendants(true);
            nestedA = descendants.contains(parentKey.a);
        }
        return nestedA;
    }

    /**
     * Analyse tube, and recursively call the creation of a parent tube if the
     * input tube is not a ROOT tube, i.e. a tube for which both parent are
     * roots in the hierarchical model (i.e. groups)
     */
    protected IEdge recursiveParentTubeAdd(Tube tube) {
        // retourne tout de suite si ce tube est interdit
        if(isForbidden(tube.getSourceItem().getLabel(), tube.getTargetItem().getLabel())){
            
            for(IEdge edge: tube.getChildren()){
                if (!internalEdges.contains(edge)) 
                    internalEdges.add(edge);
            }
            //Logger.getLogger(HierarchicalEdgeModel.class).info("FORBIDDEN!!");
            //if (!internalEdges.contains(edge)) 
            //    internalEdges.add(edge);
            //return edge;
        }
        
        
        // it is a root tube. Check if it referenced yet in the root tube list
        if (isFullRoot(tube)) {
            if (!rootTubes.contains(tube)) {
                rootTubes.add(tube);
            }
            return tube;
        } 
        else {
            CommutativePair<IBoundedItem> parentKey = createSmartParentTubeKey(tube);
            
            if(parentKey.a==parentKey.b){
                
                if (!internalEdges.contains(tube)) {
                    internalEdges.add(tube);
                }
                return tube;
            }
            else
                return buildParent(parentKey, tube);
        }
    }

    /**
     * S'arrange pour avoir une paire � la racine si seulement un des deux
     * �l�ments est racine
     * 
     * @param tube
     * @return
     */
    protected CommutativePair<IBoundedItem> createSmartParentTubeKey(Tube tube) {
        if (isNoRoot(tube))
            return createParentTubeKey(tube.getSourceItem(), tube.getTargetItem());

        else if (isSourceRoot(tube) && !isTargetRoot(tube))
            return createTubeKey(tube.getSourceItem(), tube.getTargetItem().getParent());

        else if (isTargetRoot(tube) && !isSourceRoot(tube))
            return createTubeKey(tube.getSourceItem().getParent(), tube.getTargetItem());

        else
            throw new RuntimeException("error: source=" + isSourceRoot(tube) + " target=" + isTargetRoot(tube));
    }

    /* QUALIFICATION DU TUBE */

    @Override
    public boolean isFullRoot(IHierarchicalEdge tube) {
        return isSourceRoot(tube) && isTargetRoot(tube);
    }

    @Override
    public boolean isNoRoot(IHierarchicalEdge tube) {
        return (!isSourceRoot(tube)) && (!isTargetRoot(tube));
    }

    @Override
    public boolean isPartialRoot(IHierarchicalEdge tube) {
        return isSourceRoot(tube) ^ isTargetRoot(tube);
    }

    @Override
    public boolean isSourceRoot(IHierarchicalEdge tube) {
        if (tube.getSourceItem().getParent() == null)
            return true;
        if (tube.getSourceItem().getParent().getParent() == null) // otherwise
                                                                  // we reach
                                                                  // root
                                                                  // topology!
            return true;
        return false;
    }

    @Override
    public boolean isTargetRoot(IHierarchicalEdge tube) {
        if (tube.getTargetItem().getParent() == null)
            return true;
        if (tube.getTargetItem().getParent().getParent() == null)
            return true;
        return false;
    }

    /* INSTANCES CREATION OR CACHE */

    protected IPathObstacle getOrCreateObstacle(IBoundedItem item) {
        IPathObstacle obs = itemToObstacle.get(item);
        if (obs == null) {
            obs = TubeUtils.buildObstacle(item);
            itemToObstacle.put(item, obs);
        }
        return obs;
    }

    protected Tube getOrCreateTube(CommutativePair<IBoundedItem> key) {
        Tube tube = tubeMap.get(key);
        if (tube == null) {
            tube = new Tube(key.a, key.b, pathFactory);
            if (DEBUG)
                Logger.getLogger(HierarchicalEdgeModel.class).info("CREATE TUBE: " + tube);
            tubeMap.put(key, tube);
        } else {
            if (DEBUG)
                Logger.getLogger(HierarchicalEdgeModel.class).info("REUSE  TUBE: " + tube);
        }
        return tube;
    }

    protected IEdge createEdge(Pair<IBoundedItem, IBoundedItem> link, Object srcInterface, Object trgInterface, IEdgeInfo info) {
        return createEdge(link.a, srcInterface, link.b, trgInterface, info);
    }

    protected IEdge createEdge(IBoundedItem source, Object srcInterface, IBoundedItem target, Object trgInterface, IEdgeInfo info) {
        IPathObstacle srcObstacle = getOrCreateObstacle(source); // create at
                                                             // current absolute
                                                             // position
        IPathObstacle trgObstacle = getOrCreateObstacle(target);

        IPath path = pathFactory.newPath();
        path.setForbidDuplicates(false);
        return new Edge(source, srcInterface, target, trgInterface, srcObstacle, trgObstacle, path, info);
    }

    protected CommutativePair<IBoundedItem> createParentTubeKey(Pair<IBoundedItem, IBoundedItem> link) {
        return new CommutativePair<IBoundedItem>(link.a.getParent(), link.b.getParent());
    }

    protected CommutativePair<IBoundedItem> createParentTubeKey(IBoundedItem source, IBoundedItem target) {
        return new CommutativePair<IBoundedItem>(source.getParent(), target.getParent());
    }

    protected CommutativePair<IBoundedItem> createTubeKey(IBoundedItem source, IBoundedItem target) {
        return new CommutativePair<IBoundedItem>(source, target);
    }

    /* ACCESSORS */

    @Override
    public List<IBoundedItem> findItemsBetween(IBoundedItem in, IBoundedItem out){
        // these list will contain the item referenced by edges also containing IN or OUT item
        List<IBoundedItem> ins = new ArrayList<IBoundedItem>();
        List<IBoundedItem> outs = new ArrayList<IBoundedItem>();
        
        for(Tube tube: rootTubes){
            retrieveInOutItems(tube, in, out, ins, outs);
        }
        for(IEdge edge: internalEdges){
            retrieveInOutItems(edge, in, out, ins, outs);
        }
        
        return ListUtils.intersection(ins, outs);
    }

    public void retrieveInOutItems(IEdge edge, IBoundedItem in, IBoundedItem out, List<IBoundedItem> ins, List<IBoundedItem> outs) {
        // find tube referencing IN item
        if(edge.getSourceItem()==in)
            ins.add(edge.getTargetItem());
        else if(edge.getTargetItem()==in)
            ins.add(edge.getSourceItem());
        
        // find tube referencing OUT item
        if(edge.getSourceItem()==out)
            outs.add(edge.getTargetItem());
        else if(edge.getTargetItem()==out)
            outs.add(edge.getSourceItem());
        
        // go recursive!
        if(edge instanceof Tube){
            Tube tube = (Tube)edge;
            for(IEdge child: tube.getChildren())
                retrieveInOutItems(child, in, out, ins, outs);
        }
    }

    @Override
    public Tube findRoot(IEdge tube) {
        Tube root = findRootIn(rootTubes, tube);
        if (root != null)
            return root;
        else
            return findRootTubeIn(internalEdges, tube);
        // TODO: c'est la merde cette division root/internal, car internal
        // contient
        // aussi des edges sur lesquels on itere pour trouver des tubes. Et on
        // s'en
        // fout d'avoir cette distinction root/internal (pour le moment)
    }

    protected Tube findRootIn(List<Tube> roots, IEdge edge) {
        for (Tube root : roots) {
            if (root == edge)
                return root;
            else if (root.hasDescendant(edge))
                return root;
        }
        return null;
    }

    protected Tube findRootTubeIn(List<IEdge> roots, IEdge tube) {
        for (IEdge root : roots) {
            if (root instanceof Tube) {
                Tube rootTube = (Tube) root;
                if (rootTube == tube)
                    return (Tube) tube;
                else if (rootTube.hasDescendant(tube))
                    return rootTube;
            }
        }
        return null;
    }
    
    @Override
    public boolean hasLink(IBoundedItem in, IBoundedItem out){
        return hasLinkNoInterface(in, out) || hasLinkWithInterface(in, out);
    }
    
    protected boolean hasLinkNoInterface(IBoundedItem in, IBoundedItem out){
        for(ILink<ISlotableItem> link: links){
            if( (link.getSource()==in && link.getDestination()==out)
             || (link.getSource()==out && link.getDestination()==in) )
                return true;
        }
        return false;
    }

    protected boolean hasLinkWithInterface(IBoundedItem in, IBoundedItem out){
        for(ILink<Pair<ISlotableItem,Object>> link: linksWithInterface){
            if( (link.getSource()==in && link.getDestination()==out)
             || (link.getSource()==out && link.getDestination()==in) )
                return true;
        }
        return false;
    }
    

    /**
     * Return tubes that link a group just below the root model, to another root
     * group just below the root model.
     */
    @Override
    public List<Tube> getRootTubes() {
        return rootTubes;
    }

    /**
     * Tubes and edges that are not held by a root tube, i.e. some tube and edge
     * that are internal to a group.
     */
    @Override
    public List<IEdge> getInternalTubesAndEdges() {
        return internalEdges;
    }

    /** The list of all actual input edge.*/
    @Override
    public List<IEdge> getRawEdges() {
        return rawEdges;
    }

    @Override
    public void changeLayoutModels(Map<IBoundedItem, IBoundedItem> todo) {
        for (Tube t : rootTubes)
            changeLayoutModelRecursive(t, todo);
        for (IEdge e : internalEdges)
            changeLayoutModelRecursive(e, todo);
        for (IEdge e : rawEdges)
            changeLayoutModelRecursive(e, todo);
    }

    protected void changeLayoutModelRecursive(IEdge edge, Map<IBoundedItem, IBoundedItem> todo) {
        changeLayoutModel(edge, todo);

        if (edge instanceof IHierarchicalEdge) {
            IHierarchicalEdge t = (IHierarchicalEdge) edge;
            for (IEdge e : t.getChildren())
                changeLayoutModelRecursive(e, todo);
        }
    }

    protected void changeLayoutModel(IEdge edge, Map<IBoundedItem, IBoundedItem> todo) {
        IBoundedItem src = edge.getSourceItem();
        if (todo.containsKey(src)) {
            IBoundedItem newSrc = todo.get(src);
            IPathObstacle newSrcObstacle = getOrCreateObstacle(newSrc); // create
                                                                        // at
                                                                        // current
                                                                        // absolute
                                                                        // position
            edge.editTarget(newSrc, newSrcObstacle);
        }

        IBoundedItem trg = edge.getTargetItem();
        if (todo.containsKey(trg)) {
            IBoundedItem newTrg = todo.get(trg);
            IPathObstacle newTrgObstacle = getOrCreateObstacle(newTrg); // create
                                                                        // at
                                                                        // current
                                                                        // absolute
                                                                        // position
            edge.editTarget(newTrg, newTrgObstacle);
        }
    }

    
    
    /* FORBIDDEN TUBES */
    
    @Override
	public void addNoTube(String from, String to){
        noTubes.add(new CommutativePair<String>(from, to));
    }
    
    protected boolean isForbidden(String from, String to){
        CommutativePair<String> key = new CommutativePair<String>(from, to);
        //Logger.getLogger(HierarchicalEdgeModel.class).info("try forbidden:"+key);
        if(noTubes.contains(key))
            return true;
        return false;
    }
    
    protected boolean isForbidden(CommutativePair<IBoundedItem> pair){
        return isForbidden(pair.a.getLabel(), pair.b.getLabel());
    }
    
    /********** FLATTEN ALL TUBE HIERARCHY **********/

    @Override
    public List<IEdge> getFlattenList() {
        return flattenList;
    }

    protected void flatten() {
        for (Tube t : rootTubes)
            flatten(t);
        for (IEdge t : internalEdges) {
            if (t instanceof Tube) {
                flatten((Tube) t);
            } else
                flattenList.add(t);
        }
    }

    protected void flatten(Tube t) {
        for (IEdge e : t.children) {
            if (e instanceof Tube) {
                flatten((Tube) e);
            } else
                flattenList.add(e);
        }
        flattenList.add(t);
    }

    /**********************/

    @Override
    public void toConsole() {
        for (Tube t : getRootTubes()) {
            Logger.getLogger(HierarchicalEdgeModel.class).info("-----ROOT TUBE-----");
            Logger.getLogger(HierarchicalEdgeModel.class).info(t);
        }
        for (IEdge p : getInternalTubesAndEdges()) {
            Logger.getLogger(HierarchicalEdgeModel.class).info("-----INTERNAL TUBE OR EDGE-----");
            Logger.getLogger(HierarchicalEdgeModel.class).info(p);
        }
    }

    public static boolean isChelou(IEdge path) {
        IPath p = path.getPathGeometry();

        for (int i = 0; i < p.getPoints().size() - 2; i++) {
            Point2D p1 = p.getPoint(i);
            Point2D p2 = p.getPoint(i + 1);
            Point2D p3 = p.getPoint(i + 2);

            double d1 = PointUtils.distance(p1, p2);
            double d2 = PointUtils.distance(p1, p3);

            if (d1 > d2) // the nearest point seems far
                return true;
        }
        return false;
    }

    /****** BACKUP ********/

    boolean backed = false;

    @Override
	public boolean backupedOnce() {
        return backed;
    }

    @Override
	public void backup() {
        for (Tube t : rootTubes)
            recursiveBackup(t);
        for (IEdge t : internalEdges)
            recursiveBackup(t);
        backed = true;
    }

    @Override
    public void restore() {
        for (Tube t : rootTubes)
            recursiveRestore(t);
        for (IEdge t : internalEdges)
            recursiveRestore(t);
    }

    protected void recursiveBackup(IEdge edge) {
        if (edge instanceof Tube) {
            Tube t = (Tube) edge;
            t.backupPath();
            for (IEdge e : t.getChildren()) {
                if (e instanceof Tube) {
                    recursiveBackup(e);
                } else if (e instanceof Edge) {
                    ((Edge) e).backupPath();
                }
            }
        } else if (edge instanceof Edge) {
            ((Edge) edge).backupPath();
        }
    }

    protected void recursiveRestore(IEdge edge) {
        if (edge instanceof Tube) {
            Tube t = (Tube) edge;
            t.restorePath();
            for (IEdge e : t.getChildren()) {
                if (e instanceof Tube) {
                    recursiveRestore(e);
                } else if (e instanceof Edge) {
                    ((Edge) e).restorePath();
                }
            }
        } else if (edge instanceof Edge) {
            ((Edge) edge).restorePath();
        }
    }

    /*************/

    @Override
	public void unselectAll(){
        for (Tube t : rootTubes)
            recursiveUnselect(t);
        for (IEdge t : internalEdges)
            recursiveUnselect(t);
    }
    
    protected void recursiveUnselect(IEdge e){
        e.setState(ItemState.STATE_NONE);
        if(e instanceof IHierarchicalEdge){
            IHierarchicalEdge he = (IHierarchicalEdge)e;
            for(IEdge child: he.getChildren())
                recursiveUnselect(child);
        }   
    }
    
    /*************/

    /* SLOTABLE GROUP */

    @Override
    public void addItem(ISlotableItem o) {
        slotables.add(o);
    }

    @Override
    public boolean has(ISlotableItem o) {
        return slotables.contains(o);
    }

    @Override
    public List<ISlotableItem> getItems() {
        return slotables;
    }

    @Override
    public ILink<ISlotableItem> addPath(ISlotableItem source, IPath p, ISlotableItem target) {
        if(DEBUG)
            Logger.getLogger(this.getClass()).info("addPath:"+source+" > " + target+" ");
        ILink<ISlotableItem> link = getLinkFactory().getLink(source, target);
        itemsPathes.put(link, p); // 1->N
        links.add(link);

        return link;
    }
    
    @Override
    public ILink<Pair<ISlotableItem,Object>> addPath(ISlotableItem source, Object srcInterface, IPath p, ISlotableItem target, Object trgInterface) {
        if(DEBUG)
            Logger.getLogger(this.getClass()).info("addPath:"+source+" "+interfaceLog(srcInterface) + " > " + target+" "+interfaceLog(trgInterface));
        ILink<Pair<ISlotableItem,Object>> link = getLinkFactory().getLink(source, srcInterface, target, trgInterface);
        itemsPathesWithInterface.put(link, p); // 1->N
        linksWithInterface.add(link);

        return link;
    }

    @Override
    public synchronized List<IPath> getPathes(ISlotableItem source, ISlotableItem target) {
        final DirectedLink link = new DirectedLink(source, target);
        return itemsPathes.get(link);
    }
    
    @Override
    public List<IPath> getPathes(ISlotableItem source, Object srcInterface, ISlotableItem target, Object trgInterface) {
        return itemsPathesWithInterface.get(getLinkFactory().getLink(source, srcInterface, target, trgInterface));
    }
    

    @Override
    public ILink<ISlotableItem> getExtremities(IPath p) {
        return null;
    }

    /*@Override
    public ILink<Pair<ISlotableItem,Object>> getExtremitiesWithInterface(IPath p) {
        return pathExtremitiesWithInterface.get(p);
    }*/

    
    @Override
    public boolean has(IPath p) {
        return itemsPathesWithInterface.values().contains(p) || itemsPathes.values().contains(p);
    }

    @Override
    public List<IPath> getPathes() {
        return null;
    }

    @Override
    public List<ILink<ISlotableItem>> getLinks() {
        return null;
    }

    @Override
    public synchronized void clearPathRegistry() {
        itemsPathes.clear();
        itemsPathesWithInterface.clear();
    }

    @Override
    public void fillMissingItems(List<ILink<ISlotableItem>> links) {
        for (ILink<ISlotableItem> link : links) {
            if (!has(link.getSource()))
                addItem(link.getSource());
            if (!has(link.getDestination()))
                addItem(link.getDestination());
        }
    }
    
    @Override 
    public ILinkFactory getLinkFactory(){
        return linkFactory;
    }
    
    @Override
	public void logLinks() {
        for(ILink<Pair<ISlotableItem,Object>> infSlot: itemsPathesWithInterface.keySet()){
            Logger.getLogger(this.getClass()).info("Source item:"+infSlot.getSource().a+" "+interfaceLog(infSlot.getSource().b));
            Logger.getLogger(this.getClass()).info("Target item:"+infSlot.getDestination().a+" "+interfaceLog(infSlot.getDestination().b));
        }
        //return itemsPathesWithInterface.get(getLinkFactory().getLink(source, srcInterface, target, trgInterface));
    }
    
    protected String interfaceLog(Object inf) {
        return ("(interface: "+inf+ ")");
    }
    
    /*public void pairsListing(){
        final List<String[]> lines = new ArrayList<String[]>();
        String[] entry = {"owner","source","weight","parent","type"};
        lines.add(entry);
        
        for(Pair<ISlotableItem,Object> pairs: linksWithInterface.keySet()){
            
        }
        
        try {
            SimpleCsv.write(lines, file, ';');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    /* */
    
    @Override
    public LabelMode getLabelMode() {
        return labelMode;
    }

    @Override
    public void setLabelMode(LabelMode mode) {
        this.labelMode = mode;
    }
    
    
    /* MEMBERS */
    
    protected List<CommutativePair<String>> noTubes;

    protected List<ISlotableItem> slotables = new ArrayList<ISlotableItem>();

    protected ArrayListMultimap<ILink<ISlotableItem>, IPath> itemsPathes = ArrayListMultimap.create();
    protected ArrayListMultimap<ILink<Pair<ISlotableItem,Object>>, IPath> itemsPathesWithInterface = ArrayListMultimap.create();

    protected List<ILink<ISlotableItem>> links = new ArrayList<ILink<ISlotableItem>>();
    protected List<ILink<Pair<ISlotableItem,Object>>> linksWithInterface = new ArrayList<ILink<Pair<ISlotableItem,Object>>>();

    
    protected List<Tube> rootTubes;
    protected Map<CommutativePair<IBoundedItem>, Tube> tubeMap;
    protected BiMap<IBoundedItem, IPathObstacle> itemToObstacle;

    protected List<IEdge> internalEdges;
    protected List<IEdge> rawEdges;
    protected List<IEdge> flattenList;
    
    protected IPathFactory pathFactory;    
    protected ILinkFactory linkFactory = new LinkPoolFactory();
    
    protected LabelMode labelMode = LabelMode.NONE;
}

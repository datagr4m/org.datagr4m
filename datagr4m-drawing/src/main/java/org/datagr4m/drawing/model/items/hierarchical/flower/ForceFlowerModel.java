package org.datagr4m.drawing.model.items.hierarchical.flower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.FARepulsionLimited;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone.polar.PolarAngleAttractionForce2;
import org.datagr4m.drawing.layout.algorithms.forceAtlas.forces.standalone.polar.PolarDistanceRangeForce2;
import org.datagr4m.drawing.layout.algorithms.forces.IForce;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.DefaultBoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItem;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Un modele de fleur supportant des forces pour etre anime.
 * 
 * 3 forces:
 * <ul>
 * <li>force angulaire, qui maintient l'objet e un angle donne par rapport au centre
 * <li>force distance range, qui maintient l'objet e une dist in [min,max]
 * <li>force internode, qui maintient un espace de 5 entre deux objets
 * </ul>
 * Complexite
 * N neighbours
 * 
 * Peut etre egalement mis en page par un algo autre que force.
 * 
 * Supporte des reseaux avec hydre (i.e. objet de type reseau) et
 * sans hydre.
 * 
 * @author Martin Pernollet
 *
 */
public class ForceFlowerModel<E> extends AbstractFlowerModel<E> implements IFlowerModel<E> {
    public ForceFlowerModel(IBoundedItem center, List<IBoundedItem> items, double minDist, double maxDist){
        this(center, items, null, minDist, maxDist);
    }
    
    /**
     * Instance hold by Hydre object will be duplicated by {@link createExtremities}.
     */
    public ForceFlowerModel(IBoundedItem center, List<IBoundedItem> neighbours, List<HyperEdgeStructure> hydres, double minDist, double maxDist){
        this.center = center;
        this.neighbours = neighbours;
        this.networkStructures = hydres;
        this.minDist = minDist;
        this.maxDist = maxDist;
        
        // ------------------
        // cas1: sans hydre
        if(hydres==null){
            addChild(center, false);
            setNodeDegree(center, neighbours.size());
            for(IBoundedItem i: neighbours){
                addChild(i, false);
                setNodeDegree(i, 1);
            }
        }
        
        // ------------------
        // cas2: avec hydres
        else {
            int kH = 0;
            
            // item au centre
            addChild(center, false);
            setNodeDegree(center, neighbours.size());
            
            // creee chaque hydre, avec toutes les cibles
            for(HyperEdgeStructure hydre: hydres){
                if(!hydre.containsNeighbour(center)){
                    Logger.getLogger(this.getClass()).warn("ignored an hydre without center: " + hydre + "\ncenter:" + center);
                    continue;
                }
                else{
                    // the network item
                    IBoundedItem hydreNetItem = hydre.getEdge();
                    addChild(hydreNetItem, false);
                    setNodeDegree(hydreNetItem, hydre.size());
                    markAsHydre(hydreNetItem);
                    
                    // hydre extremities (on border)
                    // make copy of shared references to each neighbour instance
                    List<IBoundedItem> actualExtremities = new ArrayList<IBoundedItem>();
                    List<IBoundedItem> sourceExtremities = new ArrayList<IBoundedItem>(hydre.getNeighbours());
                    sourceExtremities.remove(center);
                    
                    /*for(IBoundedItem extr: sourceExtremities){ // remember parent
                        extr.setParent(findParent(extr, neighbours));
                    }*/
                    
                    
                    
                    if(!FARM_COLLAPSE){
                        for(IBoundedItem item: sourceExtremities)
                            createExtremities(kH, hydre, actualExtremities, item);
                    }
                    else{
                        throw new RuntimeException("not implemented");
                        /*CommonParentExtractor cpe = new CommonParentExtractor(sourceExtremities);
                        List<IBoundedItem> doneFarm = new ArrayList<IBoundedItem>();
                        
                        for(IBoundedItem child: cpe.getChildren()){
                            IHierarchicalModel parent = (IHierarchicalModel)cpe.getParent(child);
                            // add farm to model instead of item
                            if(isFarm(parent)){
                                if(!doneFarm.contains(parent)){
                                    createExtremities(kH, hydre, actualExtremities, parent.getCollapsedModel());
                                    doneFarm.add(parent);
                                }
                            }
                            else{
                                createExtremities(kH, hydre, actualExtremities, child);
                            }
                        }*/
                    }
                    
                    kH++;
                    registerHydreNetworkMap(hydreNetItem, actualExtremities);
                }
            }
            
            // ajoute tous les item hors hydre
            for(IBoundedItem i: neighbours){
                if(!inStructure(hydres, i)){
                    addChild(i);
                    setNodeDegree(i, 1);
                    markAsNonHydre(i);
                }
            }
        }
        
        refreshBounds(false);
        buildForces();
    }
    

    protected void createExtremities(int kH, HyperEdgeStructure hydre, List<IBoundedItem> actualExtremities, IBoundedItem item) {
        DefaultBoundedItem hydreExtremity = null;
        if(item instanceof DefaultBoundedItemIcon)
            hydreExtremity = (DefaultBoundedItemIcon)((DefaultBoundedItemIcon)item).clone();
        else
            hydreExtremity = (DefaultBoundedItem)((DefaultBoundedItem)item).clone();
            
        hydreExtremity.setLabel(item.getLabel()+"#" + kH);
        
        addChild(hydreExtremity);
        setNodeDegree(hydreExtremity, 1);
        markAsNonHydre(hydreExtremity);
        registerHydreRepresentation(item, hydreExtremity, hydre);
        
        actualExtremities.add(hydreExtremity);
    }
    
    /***********/
    
    public boolean isHydreNetwork(IBoundedItem i){
        return isHydre.contains(i);
    }

    /** Retourne les item "device", sauf le centre. */
    public List<IBoundedItem> getNonHydreNetworkItems(){
        return nonHydres;
    }
    
    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getInternalCircleItems()
	 */
    @Override
	public List<IBoundedItem> getInternalCircleItems(){
        return isHydre;
    }
    
    @Override
	public List<IBoundedItem> getExternalCircleItems() {
		return getNonHydreNetworkItems();
	}
    
    /* (non-Javadoc)
	 * @see com.netlight.layout.model.items.hierarchical.flower.IFlowerModel#getExternalCircleItemsFromNetwork(com.netlight.layout.model.items.IBoundedItem)
	 */
    @Override
	public List<IBoundedItem> getExternalCircleItems(IBoundedItem network){
        return hydreNetworkToExtremitiesMap.get(network);
    }
    
    public Collection<Pair<IBoundedItem,HyperEdgeStructure>> getRepresentationInHydres(IBoundedItem item){
        return hydreExtremityRepresentations.get(item);
    }

    public IBoundedItem getRepresentationInHydres(IBoundedItem item, HyperEdgeStructure hydre){
        Collection<Pair<IBoundedItem,HyperEdgeStructure>> reps = getRepresentationInHydres(item);
        
        for(Pair<IBoundedItem,HyperEdgeStructure> pair: reps){
            if(pair.b == hydre)
                return pair.a;
        }
        return null;
    }
    
    protected void markAsHydre(IBoundedItem i){
        isHydre.add(i);
    }
    
    protected void markAsNonHydre(IBoundedItem i){
        nonHydres.add(i);
    }
    
    protected void registerHydreNetworkMap(IBoundedItem network, List<IBoundedItem> extremities){
        hydreNetworkToExtremitiesMap.put(network, extremities);
    }
    
    protected void registerHydreRepresentation(IBoundedItem item, IBoundedItem copy, HyperEdgeStructure source){
        hydreExtremityRepresentations.put(item, new Pair<IBoundedItem,HyperEdgeStructure>(copy,source));
    }
    
    /***********/
    
    public void buildForces(){
        // --------------------------
        // Cas 1: force sans hydre
        if(networkStructures==null){
            for(IBoundedItem neighbour: neighbours){
                // attraction item sur son radius initial
                double alpha = neighbour.getPosition().sub(center.getPosition()).fullPolar().x;
                
                PolarAngleAttractionForce2 angleForce = new PolarAngleAttractionForce2(neighbour, center, alpha);
                angleForce.setFactor(0.001);
                addRepulsor(neighbour, angleForce);
                
                // maintient l'item dans un range
                PolarDistanceRangeForce2 distForce = new PolarDistanceRangeForce2(neighbour, center, minDist, maxDist);
                //distForce.setFactor(0.01);
                addRepulsor(neighbour, distForce);
                
                
                // attire jusqu'e une certaine distance
                /*double distanceThreshold = minDist;
                FAAttractionLimited attractLimitForce = new FAAttractionLimited(neighbour, center, distanceThreshold);
                addAttractor(neighbour, attractLimitForce);*/
                
                
                //model.addRepulsor(neighbour, new FARepulsionLimited(neighbour, center, -1, 300));
            }
           
            
            // force une interdistance minimum
            for(IBoundedItem neighbour: neighbours){
                //addRepulsor(center, new FARepulsionLimited(center, neighbour, 10, 0));
                for(IBoundedItem neighbour2: neighbours){
                    if(neighbour!=neighbour2){
                        FARepulsionLimited fa = new FARepulsionLimited(neighbour, neighbour2, minInterNodeDist);
                        //FARepulsion fa = new FARepulsion(neighbour, neighbour2);
                        //fa.setFactor(100);
                        addRepulsor(neighbour, fa);
                    }
                }
            }
        }
        
        // --------------------------
        // Cas 2: force avec hydre
        else if(networkStructures!=null){
            //throw new RuntimeException("not implemented");
        }
    }
    
    /**********************/
    
    public void increaseOverlap(int factor){
        List<IBoundedItem> increased = new ArrayList<IBoundedItem>();
        for(IBoundedItem item: neighbours){
            Collection<IForce> forces = getRepulsors(item);
            for(IForce f: forces){
                if(f instanceof FARepulsionLimited){
                    FARepulsionLimited far = (FARepulsionLimited)f;
                    
                    IBoundedItem owner = far.getOwner();
                    IBoundedItem source = far.getSource();
                    
                    double d = owner.getPosition().distance(source.getPosition())-owner.getRadialBounds()-source.getRadialBounds();
                    if(d<=0){
                        double newF = far.getFactor()*factor;
                        far.setFactor(newF);
                        Logger.getLogger(ForceFlowerModel.class).info("factor to " + newF + "  " + owner + " > " + source);
                        Logger.getLogger(ForceFlowerModel.class).info("owner vector:" + far.getOwnerForce());
                        Logger.getLogger(ForceFlowerModel.class).info("source vector:" + far.getSourceForce());
                        
                        increased.add(owner);
                    }
                }
            }
        }
        
        // augmente aussi le centre
        if(increased.size()>0){
            Collection<IForce> forces = getRepulsors(center);
            for(IForce f: forces){
                if(f instanceof FARepulsionLimited){
                    FARepulsionLimited far = (FARepulsionLimited)f;
                    IBoundedItem owner = far.getOwner();
                    IBoundedItem source = far.getSource();
                    
                    if(increased.contains(source)){
                        Logger.getLogger(ForceFlowerModel.class).info("increased center repulsion!!");
                        double newF = far.getFactor()*factor;
                        far.setFactor(newF);
                    }
                }
            }
        }
    }
        
    /***********/

    private static final long serialVersionUID = -8149369998595849028L;
    
    public static boolean FARM_COLLAPSE = false;
    
    protected Map<IBoundedItem,List<IBoundedItem>> hydreNetworkToExtremitiesMap = new HashMap<IBoundedItem,List<IBoundedItem>>();
    protected List<IBoundedItem> isHydre = new ArrayList<IBoundedItem>();
    protected List<IBoundedItem> nonHydres = new ArrayList<IBoundedItem>();
    protected Multimap<IBoundedItem,Pair<IBoundedItem,HyperEdgeStructure>> hydreExtremityRepresentations = ArrayListMultimap.create();
    
}

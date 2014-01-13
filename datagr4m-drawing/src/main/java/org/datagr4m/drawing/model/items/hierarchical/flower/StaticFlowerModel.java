package org.datagr4m.drawing.model.items.hierarchical.flower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.datagr4m.drawing.model.items.BoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;

import com.google.common.collect.ArrayListMultimap;

/**
 * 
 * @author martin
 *
 * @param <E>
 */
public class StaticFlowerModel<E> extends AbstractFlowerModel<E>{
	private static final long serialVersionUID = -2616377991528154701L;

	/**
	 * Input list of neighbours is supposed to make sense with the provided network item structures.
	 * 
	 * @param center
	 * @param neighbours
	 * @param structures
	 * @param minDist
	 * @param maxDist
	 */
	public StaticFlowerModel(IBoundedItem center, List<IBoundedItem> neighbours, List<HyperEdgeStructure> structures, double minDist, double maxDist){
		this.minDist = minDist;
        this.maxDist = maxDist;
        
        this.center = center;
        this.networks = new ArrayList<IBoundedItem>();
        this.networkToNeighbour = new HashMap<IBoundedItem,List<IBoundedItem>>();
        this.neighbours = new ArrayList<IBoundedItem>();
        this.networkStructures = structures;

        if(networkStructures!=null)
            addHyperEdges(networkStructures);
        addChild(center, false);
	}

    public void addHyperEdges(List<HyperEdgeStructure> structures) {
        for(HyperEdgeStructure structure: structures){
        	addHyperEdge(structure);
        }
    }
	
	/** Register and mutually index all items held by a {@link HyperEdgeStructure}*/
	public void addHyperEdge(HyperEdgeStructure structure){
		this.networks.add(structure.getEdge());
    	this.networkToNeighbour.put(structure.getEdge(), structure.getNeighbours());
    	this.neighbours.addAll(structure.getNeighbours());
    	
    	addChildren(structure.getNeighbours(), false);
    	addChild(structure.getEdge(), false);
	}
	
	/* FLOWER MODEL DATA STRUCTURE */
	
	@Override
	public List<IBoundedItem> getInternalCircleItems() {
		return networks;
	}
	
	@Override
	public List<IBoundedItem> getExternalCircleItems() {
		return neighbours;
	}

	@Override
	public List<IBoundedItem> getExternalCircleItems(IBoundedItem network) {
		return networkToNeighbour.get(network);
	}
	
	/* UTILITY METHODS TO WORK WITH THE FLOWER MODEL */
	
	/**
	 * A repeated item (having the same label) will have several occurence
	 * under its name entry in the returned multimap
	 */
	public ArrayListMultimap<String,IBoundedItem> getNameToOccurenceMapping(){
		ArrayListMultimap<String,IBoundedItem> mmap = ArrayListMultimap.create();
		
		for(IBoundedItem network: networkToNeighbour.keySet()){
			List<IBoundedItem> devices = getExternalCircleItems(network);
			for(IBoundedItem device: devices){
				mmap.put(device.getLabel(), device);
			}
		}
				
		return mmap;
	}
	
	/* COMPACTING FLOWER MODEL */
	
	/**
     * Compaction: les internes ayant des extremités communes sont mergés
     * si Center->network(a)->{extremity(a), extremity(b)}
     * et Center->network(b)->{extremity(a), extremity(b)}
     * alors Center->{network(a), network(b)}->{extremity(a), extremity(b)}
     */
	public void compactFlower() {
		// construit des groupes de réseau s'ils ont un ensemble d'extremité commun
        List<IBoundedItem> internals = getInternalCircleItems();
        ArrayListMultimap<Set<String>, IBoundedItem> extremityGroups= ArrayListMultimap.create();
        for(IBoundedItem i: internals){
        	List<IBoundedItem> extremities = getExternalCircleItems(i);
        	Set<String> set = new HashSet<String>(extremities.size());
        	for(IBoundedItem e: extremities)
        		set.add(e.getLabel());// on prend les noms car 
        	extremityGroups.put(set, i);
			//System.out.println("register:" + set + " FOR NET: " + i);
        }
        
        // remplace les intérieurs regroupés
    	for(Set<String> extremityGroup: extremityGroups.keySet()){
    		List<IBoundedItem> sharedInternals = extremityGroups.get(extremityGroup);
    		if(sharedInternals.size()>1){
    			mergeInternals(sharedInternals);
    			//System.out.println("compacting:" + sharedInternals + " FOR GROUP: " + extremityGroup);
    		}
    		else{
    			;//System.out.println("compacting:" + sharedInternals + " FOR GROUP: " + extremityGroup);
    			
    		}
    	}
	}
	
	/**
	 * fusionne les item a b c dans la structure suivante:
	 * 
	 * center -> a -> {m,n,o}   premier ensemble d'extremité est recopié
	 * center -> b -> {m,n,o}
	 * center -> c -> {p,n,o}
	 * 
	 * donne:
	 * center -> a,b,c -> m,n,o
	 * 
	 */
	public void mergeInternals(Collection<IBoundedItem> internals){
		List<IBoundedItem> mergedExtremities = new ArrayList<IBoundedItem>();
		
		// merge labels of each internal, and cleanup flower model at the same time
		List<String> labels = new ArrayList<String>();
		boolean done = false;
		for(IBoundedItem internal: internals){
			List<IBoundedItem> extremities = getExternalCircleItems(internal);
			if(!done){
				mergedExtremities.addAll(extremities);
				done = true;
			}
			
			// add label to the list
			labels.add(internal.getLabel());
			
			//remove networks and dependant extremities
			removeChild(internal, false);
			networks.remove(internal);
			networkToNeighbour.remove(internal);
			
			removeChildren(extremities, false);
			neighbours.removeAll(extremities);
			// remove all edges toward this item
			List<E> edges = getEdgesHolding(internal);
			for(E e: edges)
				removeEdge(e);
		}
		
		IBoundedItem mergedInternals = new BoundedItem(labels);
		
		HyperEdgeStructure s = new HyperEdgeStructure(mergedInternals, mergedExtremities);
		addHyperEdge(s);
		
		// make all new edges
		// center->networks
		setEdge(edgeFactory.newEdge(), center, mergedInternals);
		// networks->extremities for each extremity
		for(IBoundedItem extremity: s.getNeighbours()){
			setEdge(edgeFactory.newEdge(extremity, mergedInternals), extremity, mergedInternals);
		}
		
		// garder une seule instance de chaque groupe
	}
	public IEdgeFactory<E> getEdgeFactory() {
        return edgeFactory;
    }

    public void setEdgeFactory(IEdgeFactory<E> edgeFactory) {
        this.edgeFactory = edgeFactory;
    }
	
	/* */
	
	@Override
	public String toString(){
		return "center: " + getCenter() + "\n" 
			 + "neighbours: " + getNeighbours().size() + "\n" 
			 + "internals:" + getInternalCircleItems().size() + " " + getInternalCircleItems() + "\n" 
			 + "externals:" + getExternalCircleItems().size() + "\n" 
			 + "networkToNeighbour.keySet: " + networkToNeighbour.keySet().size();
	}
	
	protected IEdgeFactory<E> edgeFactory;
	

    protected List<IBoundedItem> networks;
	protected Map<IBoundedItem,List<IBoundedItem>> networkToNeighbour;
}

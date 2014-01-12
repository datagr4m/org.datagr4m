package org.datagr4m.drawing.navigation.plugin.bringandgo.flower.staticf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.model.items.AvatarManager;
import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.DefaultBoundedItemIcon;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.CommonParentExtractor;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.flower.FlowerEdge;
import org.datagr4m.drawing.model.items.hierarchical.flower.HyperEdgeStructure;
import org.datagr4m.drawing.model.items.hierarchical.flower.IEdgeFactory;
import org.datagr4m.drawing.model.items.hierarchical.flower.StaticFlowerModel;
import org.datagr4m.drawing.navigation.plugin.bringandgo.flower.FlowerBuilderConfiguration;
import org.datagr4m.topology.Topology;

import edu.uci.ics.jung.graph.Graph;

/**
 * A tool able to build a {@link StaticFlowerModel} 
 * from a source {@link IHierarchicalNodeModel} and {@link Graph<DeviceKey,NetworkEdge}.
 * 
 * Any entity is cloned using an {@link AvatarManager} that ensure the flower model
 * will not reuse existing items (which is required to keep the global view static).
 * Each avatar will have its own label, that is [devicename]#[cloneid]
 * 
 * @author Martin Pernollet
 */
public abstract class AbstractStaticFlowerBuilder<V,E> implements IStaticFlowerBuilder<V, E> {
    protected FlowerBuilderConfiguration configuration;

    // source
    protected IHierarchicalNodeModel model;
    protected Topology<V,E> data;
    
    // target
    protected StaticFlowerModel<E> flowerModel; // a model for these clones
    
    // tools
    protected AvatarManager avatarManager;
    protected IEdgeFactory<E> edgeFactory;
    
    public AbstractStaticFlowerBuilder(IHierarchicalNodeModel model, Topology<V,E> data, AvatarManager manager, IEdgeFactory<E> edgeFactory){
        this(model, data, manager, edgeFactory, new FlowerBuilderConfiguration());
    }    
    
    public AbstractStaticFlowerBuilder(IHierarchicalNodeModel model, Topology<V,E> data, AvatarManager manager, IEdgeFactory<E> edgeFactory, FlowerBuilderConfiguration configuration){
        this.model = model;
        this.data=data;
        this.avatarManager = manager;
        this.configuration = configuration;
    }
    
    @Override
    public StaticFlowerModel<E> getFlowerModel(){
        return flowerModel;
    }
        
    @Override
    public void build(IBoundedItem item){
        if(item instanceof IHierarchicalNodeModel){
        	Logger.getLogger(AbstractStaticFlowerBuilder.class).info("no b&g on a model");
        	return;
        }
        else{
	        // Lookup the model with appropriate data
	        flowerModel = buildFlower(item, model, data, edgeFactory);
        }
    }

    @SuppressWarnings("unchecked")
    public StaticFlowerModel<E> buildFlower(IBoundedItem center, IHierarchicalNodeModel model, Topology<V, E> data, IEdgeFactory<E> edgeFactory){
    	IBoundedItem centerAvatar = ((DefaultBoundedItemIcon) center).clone();
        centerAvatar.changePosition(center.getAbsolutePosition());

    	// cherche le voisin de l'objet cliqué
    	V objCenter = (V)center.getObject();
    	Graph<V,E> graph = data.getGraph();
    	
    	// Build 2 datastructures: individual edges, and hyperedges
    	Map<String,HyperEdgeStructure> hyperEdgeByName = new HashMap<String, HyperEdgeStructure>();
        Map<String,IBoundedItem> edgeItemByName = new HashMap<String, IBoundedItem>();
        List<FlowerEdge<E>> flowerIndividualEdges = new ArrayList<FlowerEdge<E>>();
    	
    	for(E edge: graph.getIncidentEdges(objCenter)){
    	    V neighbour = graph.getOpposite(objCenter, edge);
    	    String elabel = getEdgelabel(edge);
    	    
    	    HyperEdgeStructure hyperEdge = hyperEdgeByName.get(elabel);
    	    
    	    // init hyperedge
    	    if(hyperEdge==null){
    	        IBoundedItem edgeItem = new DefaultBoundedItem(elabel);
    	        hyperEdge = new HyperEdgeStructure(center, edgeItem);
    	        edgeItem.setVisible(false); // TODO: FlowerRenderer is not able to render "hyperedge edges"
    	        //bbbbbbbbbbbbSystem.out.println(edgeItem.getLabel() + " invisible");
    	        hyperEdgeByName.put(elabel, hyperEdge);
    	        edgeItemByName.put(elabel, edgeItem);
    	    }
    	    
    	    // store neighbour as a BI avatar
    	    IBoundedItem originalNeighbourItem = model.getItem(neighbour);
    	    IBoundedItem neighbourAvatar = avatarManager.createAvatar(originalNeighbourItem);
    	    hyperEdge.getNeighbours().add(neighbourAvatar);
    	    
    	    flowerIndividualEdges.add(new FlowerEdge<E>(edge, elabel, centerAvatar, (String)null, neighbourAvatar, (String)null));
    	}       
    	
    	// build final flower
    	List<HyperEdgeStructure> hE = new ArrayList<HyperEdgeStructure>(hyperEdgeByName.values());
    	StaticFlowerModel<E> flowerModel = new StaticFlowerModel<E>(centerAvatar, null, hE, configuration.getMinRadius(), configuration.getMaxRadius());
    	flowerModel.setEdgeFactory(edgeFactory);
    	for(FlowerEdge<E> flowerEdge: flowerIndividualEdges)
            flowerModel.setEdge(flowerEdge);
        flowerModel.compactFlower();
        return flowerModel;
    }
    
    public abstract String getEdgelabel(E edge);

	
    
    /**
     * Return an avatar of the item, or an avatar of the parent if:
     * <ul>
     * <li>parent is a collapsable item
     * <li>option returnParentIfCollapsable is true
     * </ul>
     * If parent is collapsable, and option is to true, but this parent is
     * already registered as a "doneFarm", then this method will return null
     * to indicate caller that no item should be added.
     */
    protected IBoundedItem getItemOrParentAvatar(IBoundedItem item, boolean returnParentIfCollapsable, CommonParentExtractor parentIndex, List<IBoundedItem> doneFarms){
    	// enregistre l'item et le lien avec son parent
    	if(!parentIndex.contains(item)){
    		parentIndex.add(item);
        }
        
        IHierarchicalNodeModel parent = parentIndex.getParent(item);
        
        // if can collapse and option to true
        if(returnParentIfCollapsable && parentIndex.canCollapse(parent)){ 
            if(!doneFarms.contains(parent)){ // and not added yet
                IBoundedItem farmAvatar = avatarManager.createAvatar(parent.getCollapsedModel());
                doneFarms.add(parent);
                return farmAvatar;
            }
            else
            	return null;
        }
        // sinon
        else{
            // add this avatar to neighbour list AND network content
            return avatarManager.createAvatar(item);
        }
    }
        
    /* A HANDY STRUCTURE */
        /* */
    
    public Topology<V, E> getData() {
		return data;
	}

	public AvatarManager getAvatarManager() {
		return avatarManager;
	}
}

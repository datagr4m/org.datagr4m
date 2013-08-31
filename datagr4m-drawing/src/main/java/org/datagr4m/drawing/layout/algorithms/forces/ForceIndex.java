package org.datagr4m.drawing.layout.algorithms.forces;

import java.util.Collection;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.IHierarchicalGraphModel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ForceIndex {
    public void register(IHierarchicalModel model){
        if(model instanceof IHierarchicalGraphModel){
            IHierarchicalGraphModel g = (IHierarchicalGraphModel)model;
            
            // register this model forces
            registerAttraction(g.getAttractionEdges());
            for(IBoundedItem i: g.getChildren())
                registerRepulsion(i, g.getRepulsors(i));
        }
        
        // register all sub model forces
        for(IBoundedItem i: model.getChildren()){
            if(i instanceof IHierarchicalModel)
                register((IHierarchicalModel)i);
        }
    }
    
    public void registerRepulsion(IBoundedItem target, Collection<IForce> forces){
        for(IForce f: forces)
            registerRepulsion(target, f);
    }
    
    public void registerRepulsion(IBoundedItem target, IForce force){
        repulsion.put(target, force.getSource());
    }
    
    public void registerAttraction(Collection<Pair<IBoundedItem,IBoundedItem>> edges){
        for(Pair<IBoundedItem,IBoundedItem> e: edges)
            registerAttraction(e);
    }
    
    public void registerAttraction(Pair<IBoundedItem,IBoundedItem> edge){
        attraction.put(edge.a, edge.b);
        attraction.put(edge.b, edge.a);
    }
    
    /**************/

    public Collection<IBoundedItem> getAttractors(IBoundedItem item){
        return attraction.get(item);
    }

    public Collection<IBoundedItem> getRepulsors(IBoundedItem item){
        return repulsion.get(item);
    }

    
    /**************/
    
    protected Multimap<IBoundedItem,IBoundedItem> attraction = ArrayListMultimap.create();
    protected Multimap<IBoundedItem,IBoundedItem> repulsion  = ArrayListMultimap.create();
    //public Collection<IForce> getRepulsors
}

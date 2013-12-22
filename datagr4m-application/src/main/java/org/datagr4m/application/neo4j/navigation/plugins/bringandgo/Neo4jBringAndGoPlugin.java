package org.datagr4m.application.neo4j.navigation.plugins.bringandgo;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.flower.IEdgeFactory;
import org.datagr4m.drawing.model.items.hierarchical.graph.HierarchicalGraphModel;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.bringandgo.AbstractBringAndGoPlugin;
import org.datagr4m.drawing.navigation.plugin.bringandgo.flower.staticf.AbstractStaticFlowerBuilder;
import org.datagr4m.topology.Topology;
import org.datagr4m.topology.graph.IPropertyEdge;
import org.datagr4m.topology.graph.IPropertyNode;
import org.datagr4m.topology.graph.PropertyEdge;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.mouse.ILocalizedMouse;

import com.google.common.collect.Multimap;

import edu.uci.ics.jung.graph.Graph;

public class Neo4jBringAndGoPlugin extends AbstractBringAndGoPlugin<IPropertyNode,IPropertyEdge>{
    public Neo4jBringAndGoPlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalModel model) {
        super(controller, display, layered, animator, mouse, model);
        init();
    }
    
    public void init() {
        Topology<IPropertyNode,IPropertyEdge> t = getTopology();
        //if(t==null)
        //    throw new RuntimeException("null topology");
        flowerBuilder = new AbstractStaticFlowerBuilder<IPropertyNode,IPropertyEdge>(model, t, avatarManager, edgeFactory){
            @Override
            public String getEdgelabel(IPropertyEdge edge) {
                return edge.getTypeName();
            }
        };
    }
    
    protected <V,E> Topology<V,E> getTopology(){
        if (model instanceof HierarchicalGraphModel) {
            HierarchicalGraphModel graphModel = (HierarchicalGraphModel) model;
            if(graphModel.getObject() instanceof Topology<?,?>){
                return (Topology<V, E>) graphModel.getObject();
            }
        }
        return null;
    }

    protected static IEdgeFactory<IPropertyEdge> edgeFactory = new IEdgeFactory<IPropertyEdge>(){

        @Override
        public IPropertyEdge newEdge() {
            return new PropertyEdge();
        }

        @Override
        public IPropertyEdge newEdge(IBoundedItem i1, IBoundedItem i2) {
            return new PropertyEdge();
        }
    };
    
    @Override
	protected Multimap<IPropertyNode,IPropertyEdge> lookupNetworks(
			Graph<IPropertyNode,IPropertyEdge> graph) {
		// TODO Auto-generated method stub
		return null;
	}
}

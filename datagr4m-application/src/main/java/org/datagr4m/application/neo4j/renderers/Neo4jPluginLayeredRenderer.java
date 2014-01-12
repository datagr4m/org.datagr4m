package org.datagr4m.application.neo4j.renderers;

import org.datagr4m.application.neo4j.navigation.plugins.louposcope.Neo4jLouposcopeLayer;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.model.annotations.AnnotationModel;

public class Neo4jPluginLayeredRenderer extends PluginLayeredRenderer{

    public Neo4jPluginLayeredRenderer(IDisplay display, IHierarchicalNodeModel model, IHierarchicalEdgeModel tubeModel, AnnotationModel amodel) {
        super(display, model, tubeModel, amodel);
    }

    @Override
    public void initLayerLouposcope(IDisplay display, IHierarchicalNodeModel model) {
        louposcopeLayer = new Neo4jLouposcopeLayer(model, display);
        addLayer(louposcopeLayer);
    }
}

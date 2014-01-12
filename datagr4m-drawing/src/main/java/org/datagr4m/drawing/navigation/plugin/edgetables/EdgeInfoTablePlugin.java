package org.datagr4m.drawing.navigation.plugin.edgetables;

import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.Tube;
import org.datagr4m.drawing.navigation.INavigationController;
import org.datagr4m.drawing.navigation.PluginLayeredRenderer;
import org.datagr4m.drawing.navigation.plugin.AbstractNavigationPlugin;
import org.datagr4m.drawing.viewer.mouse.edges.ClickedEdge;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.animation.IAnimationStack;
import org.datagr4m.viewer.layered.IPopupLayer;
import org.datagr4m.viewer.mouse.ILocalizedMouse;


@SuppressWarnings("rawtypes")
public class EdgeInfoTablePlugin extends AbstractNavigationPlugin {
    protected IPopupLayer layeredDisplay;
    
    public EdgeInfoTablePlugin(INavigationController controller, IDisplay display, PluginLayeredRenderer layered, IAnimationStack animator, ILocalizedMouse mouse, IHierarchicalNodeModel model, IPopupLayer layeredDisplay) {
        super(controller, display, layered, animator, mouse, model);
        this.layeredDisplay = layeredDisplay;
    }

    public void click(ClickedEdge edgeOrTube, Point2D screen){
        IEdge edge = edgeOrTube.getEdge();
        if (edge instanceof Tube) {
            Tube tube = (Tube) edge;
            JPanel table = getTable(tube.flatten());
            layeredDisplay.addPopupLayer(table, "Tube", (int) screen.getX(), (int) screen.getY(), 400, 200);
        } else {
            Logger.getLogger(EdgeInfoTablePlugin.class).info("not a tube instance");
            //List<String> infos = edge.getEdgeInfo().flattenInfoAsString();
            //layered.addLayer(infos, "Edge", 50,50, 100, 200);
            //JPanel table = getTable(tube.flatten());
            // List<String> infos =
            // NetworkEdgeInfo.flattenInfoAsString(tube.flattenInfos());
            // layered.addLayer(table, "Tube", (int)screen.getX(),
            // (int)screen.getY(), 100, 200);
        }   
    }
    
    protected JPanel getTable(List<IEdge> edges) {
        JEdgeTable table = new JEdgeTable("");
        table.setEdges(edges);
        return table;
    }
}

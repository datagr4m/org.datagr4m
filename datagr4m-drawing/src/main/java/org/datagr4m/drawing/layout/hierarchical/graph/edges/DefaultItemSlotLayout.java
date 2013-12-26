package org.datagr4m.drawing.layout.hierarchical.graph.edges;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.drawing.layout.slots.ISlotGroupLayout;
import org.datagr4m.drawing.layout.slots.ISlotLayout;
import org.datagr4m.drawing.layout.slots.SlotLayout;
import org.datagr4m.drawing.layout.slots.SlotLayoutConfiguration;
import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryPostProcessor;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.links.DirectedLink;
import org.datagr4m.drawing.model.links.DirectedLinkWithInterface;
import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.pathfinder.path.PathFactory;
import org.datagr4m.drawing.model.slots.ISlotableItem;


/**
 * 1) 
 * 2) Build a list of {@link ILink} and initialize a {@link SlotLayout}
 * 
 * 
 * @author martin
 *
 */
public class DefaultItemSlotLayout implements IItemSlotLayout {
    private static final long serialVersionUID = 3100881323358933508L;
    protected ISlotLayout slotLayout;
    protected ISlotGroupLayout slotGroupLayout;
    protected ISlotGeometryPostProcessor slotGeometryGeomPostProcessor;
    protected IPathFactory pathFactory = new PathFactory();
    protected SlotLayoutConfiguration configuration = new SlotLayoutConfiguration();

    public DefaultItemSlotLayout(IPathFactory pathFactory) {
        this(null, null, pathFactory);
    }

    public DefaultItemSlotLayout(ISlotGroupLayout slotGroupLayout, ISlotGeometryPostProcessor slotGroupGeomPostProcessor, IPathFactory pathFactory) {
        this.slotGroupLayout = slotGroupLayout;
        this.slotGeometryGeomPostProcessor = slotGroupGeomPostProcessor;
        this.pathFactory = pathFactory;
        this.slotLayout = new SlotLayout(false, slotGroupLayout, slotGeometryGeomPostProcessor, pathFactory);
    }
    
    public SlotLayoutConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SlotLayoutConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public void build(IHierarchicalEdgeModel model) {
        // erase any existing path from tube model
        model.clearPathRegistry();

        // create link list for slot init
        List<ILink<ISlotableItem>> links = new ArrayList<ILink<ISlotableItem>>();
        for (IEdge e : model.getRawEdges()) {
            DirectedLink link = newLink(e);
            link.setModelEdge(e.getEdgeInfo());
            links.add(link);
            // TODO: donner les links retourn� par addPath au lieu d'en cr�er
            // d'autres!

            // also make preliminary registration: we have a tube renderer
            // that will use this path instance, so we want the slot initializer
            // to edit it instead of adding a new one
            IPath path = e.getPathGeometry();

            if (!configuration.isConsiderInterfaceForPath())
                model.addPath(e.getSourceItem(), path, e.getTargetItem());
            else {
                model.addPath(e.getSourceItem(), e.getSourceInterface(), path, e.getTargetItem(), e.getTargetInterface());
            }
        }
        slotLayout.initialize(model, links);
    }

    @Override
	public DirectedLink newLink(IEdge e) {
        DirectedLink link = new DirectedLink(e.getSourceItem(), e.getTargetItem());
        link.setSourceInterface(e.getSourceInterface());
        link.setTargetInterface(e.getTargetInterface());
        return link;
    }

    /***********************/

    @Override
    public ISlotGroupLayout getSlotGroupLayout() {
        return slotGroupLayout;
    }

    @Override
    public void setSlotGroupLayout(ISlotGroupLayout slotGroupLayout) {
        this.slotGroupLayout = slotGroupLayout;
    }

    @Override
    public ISlotGeometryPostProcessor getSlotGeometryGeomPostProcessor() {
        return slotGeometryGeomPostProcessor;
    }

    @Override
    public void setSlotGeometryGeomPostProcessor(ISlotGeometryPostProcessor slotGeometryGeomPostProcessor) {
        this.slotGeometryGeomPostProcessor = slotGeometryGeomPostProcessor;
    }

    @Override
    public void setSlotLayout(ISlotLayout slotLayout) {
        this.slotLayout = slotLayout;
    }
    
    @Override
    public ISlotLayout getSlotLayout() {
        return slotLayout;
    }
}

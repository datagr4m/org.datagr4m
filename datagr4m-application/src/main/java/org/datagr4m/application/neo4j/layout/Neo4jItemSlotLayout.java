package org.datagr4m.application.neo4j.layout;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.application.neo4j.model.Neo4jEdgeInfo;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.hierarchical.graph.edges.DefaultItemSlotLayout;
import org.datagr4m.drawing.layout.slots.SlotLayout;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.IEdge;
import org.datagr4m.drawing.model.items.hierarchical.graph.edges.tubes.IHierarchicalEdgeModel;
import org.datagr4m.drawing.model.links.DirectedLink;
import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.pathfinder.path.IPath;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.datagr4m.neo4j.topology.graph.Neo4jGraphModel;

/**
 * A la construction d'un slot target, si on ne donne pas un objet unique pour l'interface,
 * ca fout les maps en l'air.
 * 
 * 
 * 
 * 
 * @author Martin
 *
 */
public class Neo4jItemSlotLayout extends DefaultItemSlotLayout {
    protected Neo4jGraphModel graphModel;

    public Neo4jItemSlotLayout(IPathFactory pathFactory, Neo4jGraphModel model) {
        super(pathFactory);
        this.graphModel = model;
    }

    public Object relationTarget(Relationship r, Object left) {
        if (left instanceof Node) {
            Node node = (Node) left;
            return r.getType() + " " + graphModel.getNodeLabel(node);
        } else
            return r.getType() + " " + left.toString();
    }

    @Override
    public void build(IHierarchicalEdgeModel model) {
        // erase any existing path from tube model
        model.clearPathRegistry();

        // create link list for slot init
        List<ILink<ISlotableItem>> links = new ArrayList<ILink<ISlotableItem>>();
        for (IEdge e : model.getRawEdges()) {
            DirectedLink link = new DirectedLink(e.getSourceItem(), e.getTargetItem());
            link.setModelEdge(e.getEdgeInfo());
            links.add(link);
            // TODO: donner les links retourne par addPath au lieu d'en
            // creer
            // d'autres!

            // also make preliminary registration: we have a tube
            // renderer
            // that will use this path instance, so we want the slot
            // initializer
            // to edit it instead of adding a new one
            IPath path = e.getPathGeometry();
            if (!configuration.isConsiderInterfaceForPath())
                model.addPath(e.getSourceItem(), path, e.getTargetItem());
            else {
                if (e.getEdgeInfo() instanceof Neo4jEdgeInfo) {
                    Neo4jEdgeInfo ni = (Neo4jEdgeInfo) e.getEdgeInfo();
                    Relationship ri = ni.getRelationship();

                    Object leftInt = null;
                    Object rightInt = null;
                    
                    if (ri != null) {
                        leftInt = relationTarget(ri, ri.getEndNode());
                        rightInt = relationTarget(ri, ri.getStartNode());
                        model.addPath(e.getSourceItem(), leftInt, path, e.getTargetItem(), rightInt);
                    } 
                    else{
                        leftInt = generateInterfaceFor(ni, e.getTargetItem().getObject());//link;
                        rightInt = generateInterfaceFor(ni, e.getSourceItem().getObject());;//link;
                        //model.addPath(e.getSourceItem(), path, e.getTargetItem());
                        model.addPath(e.getSourceItem(), leftInt, path, e.getTargetItem(), rightInt);
                    }

                    //else
                    //    throw new RuntimeException("no relation");//Logger.getLogger(this.getClass()).error("no relationship");
                } else
                    model.addPath(e.getSourceItem(), e.getSourceInterface(), path, e.getTargetItem(), e.getTargetInterface());
            }
        }

        // run slot computation for each device of the tube model
        SlotLayout si = createSlotLayout();
        // si.
        si.initialize(model, links);
    }

    protected Object generateInterfaceFor(Object object, Object object2) {
        return new Pair<Object,Object>(object, object2);
        //throw new RuntimeException("not implemented");
    }

    protected SlotLayout createSlotLayout() {
        Neo4jSlotLayout layout = new Neo4jSlotLayout(false, slotGroupLayout, slotGeometryGeomPostProcessor, pathFactory) ;
        layout.setParent(this);
        return layout;
    }
    
    private static final long serialVersionUID = 1708890198199910801L;
}

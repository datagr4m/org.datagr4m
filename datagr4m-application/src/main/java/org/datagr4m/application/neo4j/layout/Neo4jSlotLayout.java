package org.datagr4m.application.neo4j.layout;

import java.util.List;

import org.datagr4m.application.neo4j.model.Neo4jEdgeInfo;
import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.layout.slots.ISlotGroupLayout;
import org.datagr4m.drawing.layout.slots.SlotLayout;
import org.datagr4m.drawing.layout.slots.geometry.ISlotGeometryPostProcessor;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.links.ILink;
import org.datagr4m.drawing.model.pathfinder.path.IPathFactory;
import org.datagr4m.drawing.model.slots.ISlotableItem;
import org.datagr4m.drawing.model.slots.SlotSide;
import org.datagr4m.drawing.model.slots.SlotTarget;
import org.datagr4m.drawing.model.slots.SlotTarget.Direction;
import org.neo4j.graphdb.Relationship;

public class Neo4jSlotLayout extends SlotLayout{
    protected Neo4jItemSlotLayout parent;
    
    public Neo4jSlotLayout() {
        super();
    }

    public Neo4jSlotLayout(boolean isAddMode, IPathFactory factory) {
        super(isAddMode, factory);
    }

    public Neo4jSlotLayout(boolean isAddMode, ISlotGroupLayout sgLayout, ISlotGeometryPostProcessor postProcessor, IPathFactory factory) {
        super(isAddMode, sgLayout, postProcessor, factory);
    }

    public Neo4jSlotLayout(IPathFactory factory) {
        super(factory);
    }
    
    public Neo4jItemSlotLayout getParent() {
        return parent;
    }

    public void setParent(Neo4jItemSlotLayout parent) {
        this.parent = parent;
    }

    /** Select target side for each slot */
    @Override
    protected void initializeSlotTargets(List<ILink<ISlotableItem>> links) {
        slotNorthList.clear();
        slotSouthList.clear();
        slotWestList.clear();
        slotEastList.clear();

        for (ILink<ISlotableItem> link : links) {
            ISlotableItem o1 = link.getSource();
            ISlotableItem o2 = link.getDestination();
            SlotSide o1Slot = slotGroupLayout.getTargetBestSlotSide(o1, o2);
            SlotSide o2Slot = slotGroupLayout.getTargetBestSlotSide(o2, o1);

            if (link.getModelEdge() == null) {
                incrementSlotList(o1, o1Slot, new SlotTarget(o2, null, link, Direction.OUTGOING));
                incrementSlotList(o2, o2Slot, new SlotTarget(o1, null, link, Direction.INCOMING));
            } 
            else if (link.getModelEdge() instanceof Neo4jEdgeInfo) {
                Neo4jEdgeInfo nei = (Neo4jEdgeInfo) link.getModelEdge();
                Object leftInt = null;
                Object rightInt = null;

                Relationship ri = nei.getRelationship();
                if (ri != null) {
                    leftInt = parent.relationTarget(ri, ri.getEndNode());
                    rightInt = parent.relationTarget(ri, ri.getStartNode());
                } 
                else{
                    leftInt = generateInterfaceFor(nei, ((IBoundedItem)o2).getObject());
                    rightInt = generateInterfaceFor(nei, ((IBoundedItem)o1).getObject());
                }
                //else
                  //  throw new RuntimeException("no relation");
                    //Logger.getLogger(this.getClass()).error("no relationship");
                
                // Node n1 =
                // (Node)((IBoundedItem)o1).getObject();
                // Node n2 =
                // (Node)((IBoundedItem)o2).getObject();
                incrementSlotList(o1, o1Slot, new SlotTarget(o2, leftInt, link, Direction.OUTGOING));
                incrementSlotList(o2, o2Slot, new SlotTarget(o1, rightInt, link, Direction.INCOMING));
            }
        }
    }

    protected Object generateInterfaceFor(Object object, Object object2) {
        return new Pair<Object,Object>(object, object2);
        //throw new RuntimeException("not implemented");
    }

}

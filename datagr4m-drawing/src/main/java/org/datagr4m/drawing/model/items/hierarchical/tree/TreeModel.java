package org.datagr4m.drawing.model.items.hierarchical.tree;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.List;

import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.AbstractHierarchicalModel;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.maths.geometry.Pt;
import org.datagr4m.viewer.IDisplay;
import org.datagr4m.viewer.renderer.annotations.items.IClickableAnnotation;
import org.jzy3d.maths.Coord2d;


public class TreeModel extends AbstractHierarchicalModel implements IHierarchicalNodeModel{
    public TreeModel() {
        super();
    }
    public TreeModel(IHierarchicalNodeModel parent) {
        super(parent);
    }
    public TreeModel(IHierarchicalNodeModel parent, List<IBoundedItem> children) {
        super(parent, children);
    }
    public TreeModel(IHierarchicalNodeModel parent, List<IBoundedItem> children, Collection<IBoundedItem> neighbours) {
        super(parent, children, neighbours);
    }
    
    public Coord2d getPivotPoint() {
        return pivotPoint;
    }
    public void setPivotPoint(Coord2d pivotPoint) {
        this.pivotPoint = pivotPoint;
    }

    public IClickableAnnotation getNodeRepresentations(){
        return node;
    }

    public void setNodeRepresentations(IClickableAnnotation annotation){
        node = annotation;
    }

    protected IClickableAnnotation node;

    /** Tree is considered displayed if at least one node stand in the viewport.*/
    @Override
    public boolean isDisplayed(IDisplay display){
        if(!isVisible())
            return false;
        
        Rectangle2D viewBounds = display.getView().getViewBounds();
        
        List<IBoundedItem> nodes = getDescendants();
        for(IBoundedItem node: nodes){
            Point2D nodePosition = Pt.cloneAsDoublePoint(node.getPosition());
            if(viewBounds.contains(nodePosition))
                return true;
        }
        return false;
    }


    protected Coord2d pivotPoint;
    
    private static final long serialVersionUID = 3376684014379191198L;
}

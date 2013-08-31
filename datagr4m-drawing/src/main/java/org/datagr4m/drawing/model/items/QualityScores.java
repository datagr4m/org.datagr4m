package org.datagr4m.drawing.model.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.datagr4m.datastructures.pairs.CommutativePair;
import org.datagr4m.drawing.model.bounds.RectangleBounds;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalModel;


public class QualityScores {
    /**
     * Return a set of item pairs that overlap considering their raw bounds (i.e. the minimum
     * possible bounds they can return).
     */
    public static Set<CommutativePair<IBoundedItem>> countOverlappingItems(List<IBoundedItem> candidates) {
        Set<CommutativePair<IBoundedItem>> overlapping = new HashSet<CommutativePair<IBoundedItem>>();
        for(IBoundedItem i1: candidates){
            for(IBoundedItem i2: candidates){
                if(i1!=i2){
                    RectangleBounds ri1 = i1.getRawRectangleBounds();
                    ri1.shiftSelfCenterTo(i1.getPosition());
                    RectangleBounds ri2 = i2.getRawRectangleBounds();
                    ri2.shiftSelfCenterTo(i2.getPosition());
                    
                    if(ri1.intersects(ri2.cloneAsRectangle2D()))
                        overlapping.add(new CommutativePair<IBoundedItem>(i1, i2));
                }
            }
        }
        return overlapping;
    }
    
    /**
     * Compute overlapping items in a hierarchical structure
     */
    public static Set<CommutativePair<IBoundedItem>> countOverlappingItems(IHierarchicalModel model) {
        Set<CommutativePair<IBoundedItem>> overlapping = new HashSet<CommutativePair<IBoundedItem>>();
        List<IBoundedItem> candidates = model.getChildren();
        overlapping.addAll(QualityScores.countOverlappingItems(candidates));
        for(IBoundedItem candidate: candidates){
            if(candidate instanceof IHierarchicalModel){
                overlapping.addAll(countOverlappingItems((IHierarchicalModel)candidate));
            }
        }
        return overlapping;
    }
}

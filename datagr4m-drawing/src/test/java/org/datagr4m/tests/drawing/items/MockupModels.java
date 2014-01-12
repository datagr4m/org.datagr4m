package org.datagr4m.tests.drawing.items;

import org.datagr4m.drawing.model.items.DefaultBoundedItem;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.drawing.model.items.hierarchical.IHierarchicalNodeModel;
import org.datagr4m.drawing.model.items.hierarchical.pair.HierarchicalPairModel;


public class MockupModels {
    public static IHierarchicalNodeModel buildPairTree(){
        IBoundedItem left1 = new DefaultBoundedItem("left", 30);
        IBoundedItem right1 = new DefaultBoundedItem("right", 30);
        HierarchicalPairModel child1 = new HierarchicalPairModel(left1, right1);
        
        IBoundedItem left2 = new DefaultBoundedItem("left", 50);
        IBoundedItem right2 = new DefaultBoundedItem("right", 50);
        HierarchicalPairModel child2 = new HierarchicalPairModel(left2, right2);
        
        HierarchicalPairModel root = new HierarchicalPairModel(child1, child2);
        
        return root;
    }
    
    public static IHierarchicalNodeModel buildPair(){
        IBoundedItem left1 = new DefaultBoundedItem("left", 30);
        IBoundedItem right1 = new DefaultBoundedItem("right", 30);
        return new HierarchicalPairModel(left1, right1);
    }
}

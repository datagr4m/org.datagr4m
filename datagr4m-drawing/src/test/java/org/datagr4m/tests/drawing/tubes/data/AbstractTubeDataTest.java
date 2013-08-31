package org.datagr4m.tests.drawing.tubes.data;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;


public class AbstractTubeDataTest {
    public Pair<IBoundedItem,IBoundedItem> link(IBoundedItem src, IBoundedItem trg){
        return new Pair<IBoundedItem,IBoundedItem>(src, trg);
    }
}

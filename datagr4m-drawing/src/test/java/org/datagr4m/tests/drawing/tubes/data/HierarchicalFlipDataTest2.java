package org.datagr4m.tests.drawing.tubes.data;

import java.util.ArrayList;
import java.util.List;

import org.datagr4m.datastructures.pairs.Pair;
import org.datagr4m.drawing.model.items.IBoundedItem;
import org.datagr4m.tests.drawing.tubes.ITubeDataTest;


//construit model hierarchique
// root
//  group
//   pair1 i11 i12
//   pair2 i21 i22
public class HierarchicalFlipDataTest2 extends HierarchicalFlipDataTest implements ITubeDataTest{
        @Override
		public List<Pair<IBoundedItem,IBoundedItem>> makeLinks(){
        List<Pair<IBoundedItem,IBoundedItem>> links = new ArrayList<Pair<IBoundedItem,IBoundedItem>>();

        links.add(link(i11, j11));
        links.add(link(i12, j11));
        links.add(link(j11, i21));
        links.add(link(j22, i22));
        return links;
    }
}

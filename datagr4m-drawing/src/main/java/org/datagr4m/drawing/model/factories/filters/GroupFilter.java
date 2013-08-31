package org.datagr4m.drawing.model.factories.filters;

import org.datagr4m.topology.Group;


public class GroupFilter<V> {
    public boolean accepts(Group<V> group){
        return true;
    }
}

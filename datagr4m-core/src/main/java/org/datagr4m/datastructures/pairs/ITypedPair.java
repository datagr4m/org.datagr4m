package org.datagr4m.datastructures.pairs;

import java.io.Serializable;

public interface ITypedPair<T> extends Serializable{
    public T getSource();
    public T getDestination();
}

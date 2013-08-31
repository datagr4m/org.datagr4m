package org.datagr4m.datastructures.pairs;

import java.io.Serializable;

public class UniquePair<T> implements ITypedPair<T>, Serializable{
    private static final long serialVersionUID = 4805656993916441696L;
    
    public UniquePair(T source, T destination) {
        this.source = source;
        this.destination = destination;
    }
    
    @Override
	public T getSource() {
        return source;
    }
    @Override
	public T getDestination() {
        return destination;
    }
    
    /************/
    
    protected T source;
    protected T destination;
}

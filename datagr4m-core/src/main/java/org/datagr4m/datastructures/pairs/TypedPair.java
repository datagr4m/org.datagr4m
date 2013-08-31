package org.datagr4m.datastructures.pairs;

public class TypedPair<T> extends Pair<T,T> implements ITypedPair<T>{
    private static final long serialVersionUID = -594708395070268755L;

    public TypedPair(T a, T b) {
        super(a, b);
    }

    @Override
    public T getSource() {
        return a;
    }

    @Override
    public T getDestination() {
        return b;
    }
}

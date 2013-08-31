package org.datagr4m.datastructures.pairs;


/**
 * Commutativity implemented in hashCode() and equals().
 * 
 * equality returns true if:
 * <ul>
 * <li>pair1.a = pair2.a and pair1.b = pair2.b, or
 * <li>pair1.a = pair2.b and pair1.b = pair2.a
 * </ul>
 */
public class CommutativePair<T> extends TypedPair<T> implements ITypedPair<T>{
    private static final long serialVersionUID = 4246776946032440512L;

    public CommutativePair(T a, T b) {
        super(a,b);
    }

    @Override
	public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    @Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes")
		CommutativePair other = (CommutativePair) obj;
        if (a == null) {
            if (other.a != null)
                return false;
        }
        else if (!a.equals(other.a) && !a.equals(other.b))
            return false;
        if (b == null) {
            if (other.b != null)
                return false;
        }
        else if (!b.equals(other.b) && !b.equals(other.a))
            return false;
        return true;
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

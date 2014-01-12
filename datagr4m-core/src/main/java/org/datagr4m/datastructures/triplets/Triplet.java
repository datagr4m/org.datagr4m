package org.datagr4m.datastructures.triplets;

import java.io.Serializable;

public class Triplet<X, Y, Z> implements Serializable{
    private static final long serialVersionUID = 4246736946032440512L;

    public final X a;
    public final Y b;
    public final Z c;

    public Triplet(X a, Y b, Z c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        result = prime * result + ((c == null) ? 0 : c.hashCode());
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
        Triplet other = (Triplet) obj;
        if (a == null) {
            if (other.a != null)
                return false;
        } else if (!a.equals(other.a))
            return false;
        if (b == null) {
            if (other.b != null)
                return false;
        } else if (!b.equals(other.b))
            return false;
        if (c == null) {
            if (other.c != null)
                return false;
        } else if (!c.equals(other.c))
            return false;
        return true;
    }



    @Override
	public String toString(){
        return "a:"+a+" b:"+b+" c:"+c;
    }
}

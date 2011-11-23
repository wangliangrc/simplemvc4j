package com.clark.lang;

import static com.clark.lang.Functions.objectEquals;

import java.io.Serializable;

public final class Pair<L, R> implements Serializable {
    /** Serialization version */
    private static final long serialVersionUID = 4954918890077093841L;

    /** Left object */
    public final L left;

    /** Right object */
    public final R right;

    /**
     * Create a new Pair instance.
     * 
     * @param left
     * @param right
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Pair<?, ?> == false) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return objectEquals(left, other.left)
                && objectEquals(right, other.right);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(left).append(right).toHashCode();
    }

    /**
     * Returns a String representation of the Pair in the form: (L,R)
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(left);
        builder.append(",");
        builder.append(right);
        builder.append(")");
        return builder.toString();
    }

    /**
     * Static creation method for a Pair<L, R>.
     * 
     * @param <L>
     * @param <R>
     * @param left
     * @param right
     * @return Pair<L, R>(left, right)
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<L, R>(left, right);
    }
}

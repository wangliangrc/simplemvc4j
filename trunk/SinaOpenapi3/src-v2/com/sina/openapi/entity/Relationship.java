package com.sina.openapi.entity;

import java.io.Serializable;

public class Relationship implements Serializable, Cloneable,
        Comparable<Relationship> {
    private static final long serialVersionUID = 477432043245697478L;

    /**
     * source
     */
    private Relation source;

    /**
     * target
     */
    private Relation target;

    public Relation getSource() {
        return source;
    }

    public void setSource(Relation source) {
        this.source = source;
    }

    public Relation getTarget() {
        return target;
    }

    public void setTarget(Relation target) {
        this.target = target;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Relationship [source=").append(source)
                .append(", target=").append(target).append("]");
        return builder.toString();
    }

    @Override
    public Object clone() {
        Relationship o = null;
        try {
            o = (Relationship) super.clone();
            o.source = source == null ? null : (Relation) source.clone();
            o.target = target == null ? null : (Relation) target.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Relationship other = (Relationship) obj;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        if (target == null) {
            if (other.target != null)
                return false;
        } else if (!target.equals(other.target))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public int compareTo(Relationship another) {
        // TODO Auto-generated method stub
        return 0;
    }

}

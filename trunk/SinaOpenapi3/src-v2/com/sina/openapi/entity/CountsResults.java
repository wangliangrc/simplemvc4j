package com.sina.openapi.entity;

import java.io.Serializable;

public final class CountsResults implements Serializable, Cloneable,
        Comparable<CountsResults> {

    private static final long serialVersionUID = 8791939538993053415L;
    private String id;
    private int comments;
    private int rt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getRt() {
        return rt;
    }

    public void setRt(int rt) {
        this.rt = rt;
    }

    @Override
    public int compareTo(CountsResults arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CountsResults [id=").append(id).append(", comments=")
                .append(comments).append(", rt=").append(rt).append("]");
        return builder.toString();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        CountsResults other = (CountsResults) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}

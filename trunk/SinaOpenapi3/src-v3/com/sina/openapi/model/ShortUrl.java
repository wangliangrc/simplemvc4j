package com.sina.openapi.model;

import java.io.Serializable;

public class ShortUrl implements Serializable {

    private static final long serialVersionUID = -1829682999620061070L;

    private String url_short;
    private String url_long;
    private int type;

    public String getUrl_short() {
        return url_short;
    }

    public void setUrl_short(String url_short) {
        this.url_short = url_short;
    }

    public String getUrl_long() {
        return url_long;
    }

    public void setUrl_long(String url_long) {
        this.url_long = url_long;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + type;
        result = prime * result
                + ((url_long == null) ? 0 : url_long.hashCode());
        result = prime * result
                + ((url_short == null) ? 0 : url_short.hashCode());
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
        ShortUrl other = (ShortUrl) obj;
        if (type != other.type)
            return false;
        if (url_long == null) {
            if (other.url_long != null)
                return false;
        } else if (!url_long.equals(other.url_long))
            return false;
        if (url_short == null) {
            if (other.url_short != null)
                return false;
        } else if (!url_short.equals(other.url_short))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ShortUrl [url_short=").append(url_short)
                .append(", url_long=").append(url_long).append(", type=")
                .append(type).append("]");
        return builder.toString();
    }

}

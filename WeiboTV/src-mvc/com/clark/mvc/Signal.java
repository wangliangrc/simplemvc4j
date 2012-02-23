package com.clark.mvc;

/**
 * 不可变类。
 * 
 * @author clark
 *
 */
public class Signal {

    public final String name;
    public final Object body;
    public final String type;

    Signal(String name, Object body, String type) {
        super();
        this.name = name;
        this.body = body;
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Signal other = (Signal) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Signal [name=" + name + ", body=" + body + ", type=" + type
                + "]";
    }

}

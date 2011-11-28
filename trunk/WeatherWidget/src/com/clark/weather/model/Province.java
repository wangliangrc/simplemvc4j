package com.clark.weather.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class Province implements Iterable<City> {
    private String name;
    private List<City> cities = new ArrayList<City>();

    public Province(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Iterator<City> iterator() {
        return cities.iterator();
    }

    public void add(int location, City object) {
        cities.add(location, object);
    }

    public boolean add(City object) {
        return cities.add(object);
    }

    public void clear() {
        cities.clear();
    }

    public boolean contains(Object object) {
        return cities.contains(object);
    }

    public City get(int location) {
        return cities.get(location);
    }

    public int indexOf(Object object) {
        return cities.indexOf(object);
    }

    public City set(int location, City object) {
        return cities.set(location, object);
    }

    public int size() {
        return cities.size();
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
        Province other = (Province) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Province [name=").append(name).append("]");
        return builder.toString();
    }

}

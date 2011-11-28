package com.clark.weather.model;

import javax.annotation.concurrent.Immutable;

@Immutable
public class City {
    private final String code;
    private final String name;
    private final Province province;

    public City(String code, String name, Province province) {
        super();
        this.code = code;
        this.name = name;
        this.province = province;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Province getProvince() {
        return province;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
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
        City other = (City) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("City [code=").append(code).append(", name=")
                .append(name).append(", province=").append(province)
                .append("]");
        return builder.toString();
    }

}

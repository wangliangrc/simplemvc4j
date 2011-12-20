package com.sina.openapi.entity;

import java.io.Serializable;
import java.util.Arrays;

public class Trends implements Serializable, Cloneable, Comparable<Trends> {
    private static final long serialVersionUID = -237827260388746777L;

    private TrendInTrends[] trend;
    private int as_of;

    public TrendInTrends[] getTrend() {
        return trend;
    }

    public void setTrend(TrendInTrends[] trend) {
        this.trend = trend;
    }

    public int getAs_of() {
        return as_of;
    }

    public void setAs_of(int as_of) {
        this.as_of = as_of;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Trends [trend=").append(Arrays.toString(trend))
                .append(", as_of=").append(as_of).append("]");
        return builder.toString();
    }

    @Override
    public Object clone() {

        Trends o = null;
        try {
            o = (Trends) super.clone();
            for (int i = 0; i < trend.length; i++)
                o.trend[i] = trend[i] == null ? null : (TrendInTrends) trend[i]
                        .clone();
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
        Trends other = (Trends) obj;
        if (as_of != other.as_of)
            return false;
        if (!Arrays.equals(trend, other.trend))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + as_of;
        result = prime * result + Arrays.hashCode(trend);
        return result;
    }

    @Override
    public int compareTo(Trends another) {
        // TODO Auto-generated method stub
        return 0;
    }
}

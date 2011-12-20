package com.sina.openapi.model;

import java.io.Serializable;

public class Topic implements Serializable, Cloneable, Comparable<Topic> {

    private static final long serialVersionUID = 1411786023492563133L;

    private String trend_id;
    private String hotword;
    private String num;

    public String getTrend_id() {
        return trend_id;
    }

    public void setTrend_id(String trend_id) {
        this.trend_id = trend_id;
    }

    public String getHotword() {
        return hotword;
    }

    public void setHotword(String hotword) {
        this.hotword = hotword;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Topic [trend_id=").append(trend_id)
                .append(", hotword=").append(hotword).append(", num=")
                .append(num).append("]");
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
        result = prime * result + ((hotword == null) ? 0 : hotword.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result
                + ((trend_id == null) ? 0 : trend_id.hashCode());
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
        Topic other = (Topic) obj;
        if (hotword == null) {
            if (other.hotword != null)
                return false;
        } else if (!hotword.equals(other.hotword))
            return false;
        if (num == null) {
            if (other.num != null)
                return false;
        } else if (!num.equals(other.num))
            return false;
        if (trend_id == null) {
            if (other.trend_id != null)
                return false;
        } else if (!trend_id.equals(other.trend_id))
            return false;
        return true;
    }

    @Override
    public int compareTo(Topic another) {
        // TODO Auto-generated method stub
        return 0;
    }
}

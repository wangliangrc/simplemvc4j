package com.sina.openapi.entity;

import java.io.Serializable;

public class AtSuggestion implements Serializable, Cloneable, Comparable<AtSuggestion>{
    
    private static final long serialVersionUID = -8953499341632068731L;
    
    String uid;
   
    String nickname;

    String remark;
    
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AtSuggestion [uid=").append(uid)
                .append(", nickname=").append(nickname).append(", remark=").append(remark)
                .append("]");
        return builder.toString();
    }

    @Override
    public Object clone() {
        AtSuggestion o = null;
        try {
            o = (AtSuggestion) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return o;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
        AtSuggestion other = (AtSuggestion) obj;
        if (uid == null) {
            if (other.uid != null)
                return false;
        } else if (!uid.equals(other.uid))
            return false;
        return true;
    }

    @Override
    public int compareTo(AtSuggestion another) {
        if (!equals(another)) {
            try {
                return getUid().compareTo(another.getUid()) > 0 ? -1 : 1;
            } catch (Exception e) {
            }
        }
        return 0;
    }
}
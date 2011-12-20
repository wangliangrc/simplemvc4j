package com.sina.openapi.model;

import java.io.Serializable;

public class UserUnread implements Serializable {

    private static final long serialVersionUID = 3020825104242923881L;

    private int followers;
    private int dm;
    private int mentions;
    private int comments;

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getDm() {
        return dm;
    }

    public void setDm(int dm) {
        this.dm = dm;
    }

    public int getMentions() {
        return mentions;
    }

    public void setMentions(int mentions) {
        this.mentions = mentions;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getTotalNewMessages() {
        return followers + dm + mentions + comments;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserUnread [followers=").append(followers)
                .append(", dm=").append(dm).append(", mentions=")
                .append(mentions).append(", comments=").append(comments)
                .append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + comments;
        result = prime * result + dm;
        result = prime * result + followers;
        result = prime * result + mentions;
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
        UserUnread other = (UserUnread) obj;
        if (comments != other.comments)
            return false;
        if (dm != other.dm)
            return false;
        if (followers != other.followers)
            return false;
        if (mentions != other.mentions)
            return false;
        return true;
    }

}

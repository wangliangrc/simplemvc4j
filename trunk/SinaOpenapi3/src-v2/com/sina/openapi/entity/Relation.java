package com.sina.openapi.entity;

import java.io.Serializable;

public class Relation implements Serializable, Cloneable, Comparable<Relation> {
    private static final long serialVersionUID = -737862103112263979L;

    private int id;
    private String screen_name;
    private boolean following;
    private boolean followed_by;
    private boolean notifications_enabled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isFollowed_by() {
        return followed_by;
    }

    public void setFollowed_by(boolean followed_by) {
        this.followed_by = followed_by;
    }

    public boolean isNotifications_enabled() {
        return notifications_enabled;
    }

    public void setNotifications_enabled(boolean notifications_enabled) {
        this.notifications_enabled = notifications_enabled;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Relation [id=").append(id).append(", screen_name=")
                .append(screen_name).append(", following=").append(following)
                .append(", followed_by=").append(followed_by)
                .append(", notifications_enabled=")
                .append(notifications_enabled).append("]");
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Relation other = (Relation) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public int compareTo(Relation another) {
        // TODO Auto-generated method stub
        return 0;
    }
}

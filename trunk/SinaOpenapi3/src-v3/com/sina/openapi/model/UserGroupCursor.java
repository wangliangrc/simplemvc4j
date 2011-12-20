package com.sina.openapi.model;

import java.io.Serializable;
import java.util.Arrays;

public class UserGroupCursor extends Cursoring implements Serializable {
    private static final long serialVersionUID = -4039572487304085628L;
    private UserGroup[] lists;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(lists);
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
        UserGroupCursor other = (UserGroupCursor) obj;
        if (!Arrays.equals(lists, other.lists))
            return false;
        return true;
    }

    public UserGroup[] getLists() {
        return lists;
    }

    public void setLists(UserGroup[] lists) {
        this.lists = lists;
    }

}

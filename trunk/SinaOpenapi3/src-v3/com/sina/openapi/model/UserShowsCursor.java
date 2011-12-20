package com.sina.openapi.model;

import java.io.Serializable;
import java.util.Arrays;

public class UserShowsCursor extends Cursoring implements Serializable {

    private static final long serialVersionUID = 8307709889545217262L;
    private UserShow[] users;

    public UserShow[] getUsers() {
        return users;
    }

    public void setUsers(UserShow[] users) {
        this.users = users;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserShowsCursor [users=")
                .append(Arrays.toString(users)).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(users);
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
        UserShowsCursor other = (UserShowsCursor) obj;
        if (!Arrays.equals(users, other.users))
            return false;
        return true;
    }

}

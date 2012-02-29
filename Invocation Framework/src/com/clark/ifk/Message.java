package com.clark.ifk;

import java.util.Arrays;

public class Message {
    public final String name;
    public final Type[] args;
    public final Type first;
    public final boolean empty;
    public final boolean notEmpty;

    Message(String name, Object[] args) {
        super();
        this.name = name.intern();
        if (args == null) {
            this.args = new Type[0];
            first = Type.NULL;
        } else {
            this.args = new Type[args.length];
            for (int i = 0, len = args.length; i < len; i++) {
                if (args[i] == null) {
                    this.args[i] = Type.NULL;
                } else {
                    this.args[i] = new Type(args[i]);
                }
            }
            if (args != null && args.length > 0) {
                first = this.args[0];
            } else {
                first = Type.NULL;
            }
        }
        empty = Type.NULL == first;
        notEmpty = !empty;
    }

    @Override
    public String toString() {
        return "Message [name=" + name + ", args=" + Arrays.toString(args)
                + ", first=" + first + "]";
    }

}

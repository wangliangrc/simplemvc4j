package com.clark.ifk;

import java.util.Arrays;
import java.util.Date;

public class Message {
    public final String name;
    public final Type[] args;
    public final Type first;
    public final boolean empty;
    public final boolean notEmpty;
    private final Date invocationTime;
    private final Thread invocationThread;

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
        invocationTime = new Date();
        invocationThread = Thread.currentThread();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Message [name=").append(name).append(", args=")
                .append(Arrays.toString(args)).append(", first=").append(first)
                .append(", empty=").append(empty).append(", notEmpty=")
                .append(notEmpty).append(", invocationTime=")
                .append(invocationTime).append(", invocationThread=")
                .append(invocationThread).append("]");
        return builder.toString();
    }

}

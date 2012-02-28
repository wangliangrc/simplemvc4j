package com.clark.ifk;

public class Message {
    public final String name;
    public final Type[] args;
    public final Type first;

    Message(String name, Type[] args) {
        super();
        this.name = name;
        this.args = args;
        if (args != null && args.length > 0) {
            first = args[0];
        } else {
            first = null;
        }
    }

}

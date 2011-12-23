package com.clark.mvc;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Invocation {
    public final Object proxy;
    public final Method method;
    public final Object[] args;

    Invocation(Object proxy, Method method, Object[] args) {
        super();
        this.proxy = proxy;
        this.method = method;
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Invocation [proxy=").append(proxy).append(", method=")
                .append(method).append(", args=").append(Arrays.toString(args))
                .append("]");
        return builder.toString();
    }

}

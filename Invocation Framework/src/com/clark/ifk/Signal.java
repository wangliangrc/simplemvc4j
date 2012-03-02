package com.clark.ifk;

import java.util.Arrays;

public class Signal {
    public final String name;
    public final Type[] args;
    public final Type first;
    public final boolean empty;
    public final boolean notEmpty;
    public final boolean exceptionOccur;
    public final Throwable throwable;
    private final int invokeLevel;
    private final long invocationTime;
    private final Thread invocationThread;

    Signal(SignalProducer producer) {
        super();
        this.name = producer.signal.intern();
        if (producer.extra == null) {
            this.args = new Type[0];
            first = Type.NULL;
        } else {
            this.args = new Type[producer.extra.length];
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
        exceptionOccur = first.toObject() instanceof Throwable;
        throwable = (Throwable) (exceptionOccur ? first.toObject() : null);
        invokeLevel = producer.level;
        invocationTime = System.currentTimeMillis();
        invocationThread = Thread.currentThread();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Signal [name=\"").append(name).append("\", args=")
                .append(Arrays.toString(args)).append(", empty=").append(empty)
                .append(", throwable=").append(throwable)
                .append(", invokeLevel=").append(invokeLevel)
                .append(", invocationTime=").append(invocationTime)
                .append(", invocationThread=").append(invocationThread)
                .append("]");
        return builder.toString();
    }

}

class SignalProducer {
    public String signal;
    public Object[] extra;
    public int level = IFK.DEFAULT_LEVEL;
}

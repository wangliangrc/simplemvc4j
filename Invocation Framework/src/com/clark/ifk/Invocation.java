package com.clark.ifk;

public class Invocation {
    private Object receiver;
    private String rcvSignal;
    private ThreadStrategy rcvStrategy = ThreadStrategy.DEFAULT;
    private Object callbackRcv;
    private String callbackSgl;
    private ThreadStrategy callbackStrategy = ThreadStrategy.DEFAULT;
    private Object[] args;
    private IFK ifk;

    Invocation(IFK ifk, String rcvSignal) {
        assert ifk != null && rcvSignal != null && rcvSignal.length() > 0;
        this.ifk = ifk;
        this.rcvSignal = rcvSignal;
    }

    public Invocation receiver(Object receiver) {
        this.receiver = receiver;
        return this;
    }

    public Invocation sync() {
        rcvStrategy = ThreadStrategy.SYNCHRONOUS;
        return this;
    }

    public Invocation async() {
        rcvStrategy = ThreadStrategy.ASYNCHRONOUS;
        return this;
    }

    public Invocation arguments(Object... args) {
        if (args == null) {
            this.args = new Object[] {};
        } else {
            this.args = args;
        }
        return this;
    }

    public Invocation callbackReceiver(Object receiver) {
        callbackRcv = receiver;
        return this;
    }

    public Invocation callbackSignal(String name) {
        callbackSgl = name;
        return this;
    }

    public Invocation callbackSync() {
        callbackStrategy = ThreadStrategy.SYNCHRONOUS;
        return this;
    }

    public Invocation callbackAsync() {
        callbackStrategy = ThreadStrategy.ASYNCHRONOUS;
        return this;
    }

    public void invoke() {
        ifk.invokeExecutor(receiver, rcvSignal, rcvStrategy, callbackRcv, callbackSgl,
                callbackStrategy, args);
    }
}

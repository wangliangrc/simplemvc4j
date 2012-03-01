package com.clark.ifk;

public class Invocation {
    private Object receiver;
    private String rcvMsg;
    private boolean rcvRunOnUi = true;
    private Object callbackRcv;
    private String callbackMsg;
    private boolean callbackRunOnUi = true;
    private Object[] args;
    private IFK ifk;

    Invocation(IFK ifk, String rcvMsg) {
        assert ifk != null && rcvMsg != null && rcvMsg.length() > 0;
        this.ifk = ifk;
        this.rcvMsg = rcvMsg;
    }

    public Invocation receiver(Object receiver) {
        this.receiver = receiver;
        return this;
    }

    public Invocation sync() {
        rcvRunOnUi = true;
        return this;
    }

    public Invocation async() {
        rcvRunOnUi = false;
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

    public Invocation callbackMessage(String name) {
        callbackMsg = name;
        return this;
    }

    public Invocation callbackSync() {
        callbackRunOnUi = true;
        return this;
    }

    public Invocation callbackAsync() {
        callbackRunOnUi = false;
        return this;
    }

    public void invoke() {
        ifk.invokeExecutor(receiver, rcvMsg, rcvRunOnUi, callbackRcv,
                callbackMsg, callbackRunOnUi, args);
    }
}

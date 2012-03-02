package com.clark.ifk;

public class Invocation {
    private IFK ifk;
    private InvocationInteraction interaction = new InvocationInteraction();

    Invocation(IFK ifk, String rcvSignal) {
        assert ifk != null && rcvSignal != null && rcvSignal.length() > 0;
        this.ifk = ifk;
        interaction.requestMsg.signal = rcvSignal;
    }

    public Invocation receiver(Object receiver) {
        interaction.requestMsg.receiver = receiver;
        return this;
    }

    public Invocation level(int level) {
        if (level < IFK.MIN_LEVEL || level > IFK.MAX_LEVEL) {
            throw new IndexOutOfBoundsException("超出Level设置范围");
        }

        interaction.requestMsg.signalLevel = level;
        return this;
    }

    public Invocation sync() {
        interaction.requestMsg.threadStrategy = ThreadStrategy.SYNCHRONOUS;
        return this;
    }

    public Invocation async() {
        interaction.requestMsg.threadStrategy = ThreadStrategy.ASYNCHRONOUS;
        return this;
    }

    public Invocation arguments(Object... args) {
        if (args == null) {
            interaction.extra = new Object[] {};
        } else {
            interaction.extra = args;
        }
        return this;
    }

    public Invocation callbackReceiver(Object receiver) {
        interaction.responseMsg.receiver = receiver;
        return this;
    }

    public Invocation callbackSignal(String name) {
        interaction.responseMsg.signal = name;
        return this;
    }

    public Invocation callbackLevel(int level) {
        if (level < IFK.MIN_LEVEL || level > IFK.MAX_LEVEL) {
            throw new IndexOutOfBoundsException("超出Level设置范围");
        }

        interaction.responseMsg.signalLevel = level;
        return this;
    }

    public Invocation callbackSync() {
        interaction.responseMsg.threadStrategy = ThreadStrategy.SYNCHRONOUS;
        return this;
    }

    public Invocation callbackAsync() {
        interaction.responseMsg.threadStrategy = ThreadStrategy.ASYNCHRONOUS;
        return this;
    }

    public void invoke() {
        ifk.invokeExecutor(interaction);
    }
}

class InvocationInteraction {
    public Messenger requestMsg = new Messenger();
    public Messenger responseMsg = new Messenger();
    public Object[] extra;
}

class Messenger {
    public Object receiver;
    public String signal;
    public ThreadStrategy threadStrategy = ThreadStrategy.DEFAULT;
    public int signalLevel = IFK.DEFAULT_LEVEL;
}

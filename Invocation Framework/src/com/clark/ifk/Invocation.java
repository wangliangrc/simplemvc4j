package com.clark.ifk;

public class Invocation {
    private IFK ifk;
    private InvocationInteraction interaction = new InvocationInteraction();

    Invocation(IFK ifk, String rcvSignal) {
        assert ifk != null && rcvSignal != null && rcvSignal.length() > 0;
        this.ifk = ifk;
        interaction.requestArg.signal = rcvSignal;
    }

    public Invocation receiver(Object receiver) {
        interaction.requestArg.receiver = receiver;
        return this;
    }

    public Invocation level(int level) {
        if (level < IFK.MIN_LEVEL || level > IFK.MAX_LEVEL) {
            throw new IndexOutOfBoundsException("超出Level设置范围");
        }

        interaction.requestArg.level = level;
        return this;
    }

    public Invocation sync() {
        interaction.requestArg.strategy = ThreadStrategy.SYNCHRONOUS;
        return this;
    }

    public Invocation async() {
        interaction.requestArg.strategy = ThreadStrategy.ASYNCHRONOUS;
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
        interaction.responseArg.receiver = receiver;
        return this;
    }

    public Invocation callbackSignal(String name) {
        interaction.responseArg.signal = name;
        return this;
    }

    public Invocation callbackLevel(int level) {
        if (level < IFK.MIN_LEVEL || level > IFK.MAX_LEVEL) {
            throw new IndexOutOfBoundsException("超出Level设置范围");
        }

        interaction.responseArg.level = level;
        return this;
    }

    public Invocation callbackSync() {
        interaction.responseArg.strategy = ThreadStrategy.SYNCHRONOUS;
        return this;
    }

    public Invocation callbackAsync() {
        interaction.responseArg.strategy = ThreadStrategy.ASYNCHRONOUS;
        return this;
    }

    public void invoke() {
        ifk.invokeExecutor(interaction);
    }
}

class InvocationInteraction {
    public Messenger requestArg = new Messenger();
    public Messenger responseArg = new Messenger();
    public Object[] extra;
}

class Messenger {
    public Object receiver;
    public String signal;
    public ThreadStrategy strategy = ThreadStrategy.DEFAULT;
    public int level = IFK.DEFAULT_LEVEL;
}

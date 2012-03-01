package com.clark.ifk;

import java.util.concurrent.Executor;

public abstract class IFK {
    private static class Holder {
        static IFK ifk = new IFKJavaImpl();
    }

    public static IFK getInstance() {
        return Holder.ifk;
    }

    protected IFK() {
    }

    protected Executor syncExecutor, asyncExecutor;

    public final void setSyncExecutor(Executor sync) {
        this.syncExecutor = sync;
    }

    public final void setAsyncExecutor(Executor async) {
        this.asyncExecutor = async;
    }

    public abstract void register(Object receiver);

    public abstract void unregister(Object receiver);

    public final Invocation invoker(String msgname) {
        return new Invocation(this, msgname);
    }

    protected abstract void invokeExecutor(final Object receiver,
            final String message, boolean runOnUi, final Object callbackRcv,
            final String callbackMsg, final boolean callbackRunOnUi,
            final Object... args);
}

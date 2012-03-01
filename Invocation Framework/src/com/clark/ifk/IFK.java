package com.clark.ifk;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

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
        if (async == null)
            return;
        this.asyncExecutor = async;
    }

    public abstract void register(Object receiver);

    public abstract void unregister(Object receiver);

    public final Invocation invoker(String msgname) {
        return new Invocation(this, msgname);
    }

    protected abstract void invokeExecutor(Object receiver, String message,
            ThreadStrategy strategy, Object callbackRcv, String callbackMsg,
            ThreadStrategy callbackStrategy, Object... args);

    public final void close() {
        if (syncExecutor instanceof ExecutorService) {
            ((ExecutorService) syncExecutor).shutdown();
        }
        if (asyncExecutor instanceof ExecutorService) {
            ((ExecutorService) asyncExecutor).shutdown();
        }
    }
}

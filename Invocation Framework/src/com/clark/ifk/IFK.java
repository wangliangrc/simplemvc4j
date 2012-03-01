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

    protected Executor uiExecutor, poolExecutor;

    public final void setUiExecutor(Executor uiExecutor) {
        this.uiExecutor = uiExecutor;
    }

    public final void setPoolExecutor(Executor poolExecutor) {
        this.poolExecutor = poolExecutor;
    }

    public abstract void register(Object receiver);

    public abstract void unregister(Object receiver);

    public final Invocation invoker(String msgname) {
        return new Invocation(this, msgname);
    }

    protected abstract void invokeRunnable(final Object receiver,
            final String message, boolean runOnUi, final Object callbackRcv,
            final String callbackMsg, final boolean callbackRunOnUi,
            final Object... args);
}

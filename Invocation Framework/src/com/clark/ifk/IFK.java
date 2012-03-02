package com.clark.ifk;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public abstract class IFK {
    public static final int MAX_LEVEL = 1000;
    public static final int DEFAULT_LEVEL = 500;
    public static final int MIN_LEVEL = 0;

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

    public abstract void register(Object receiver, int level);

    public final void register(Object receiver) {
        register(receiver, DEFAULT_LEVEL);
    }

    public abstract void setLevel(Object receiver, int level);

    /**
     * 注意：如果你在执行异步任务时注销，则运行过程中可能出现任务注册方法移除的情况？！
     * 
     * @param receiver
     */
    public abstract void unregister(Object receiver);

    public final Invocation invoker(String msgname) {
        return new Invocation(this, msgname);
    }

    protected abstract void invokeExecutor(InvocationInteraction argument);

    public final void close() {
        if (syncExecutor instanceof ExecutorService) {
            ((ExecutorService) syncExecutor).shutdown();
        }
        if (asyncExecutor instanceof ExecutorService) {
            ((ExecutorService) asyncExecutor).shutdown();
        }
    }
}

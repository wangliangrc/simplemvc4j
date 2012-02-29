package com.clark.ifk;

public interface IFK {

    public abstract void register(Object receiver);

    public abstract void unregister(Object receiver);

    public abstract void rm(Object receiver, String message, Object... args);

    public abstract void m(String message, Object... args);

    /**
     * 
     * @param receiver
     *            如果是 Class 的实例则调用 static 方法；如果是 instance 则调用 instance 方法；如果为
     *            null 则调用 static 和 instance 方法。
     * @param msgname
     *            表示 Message 的名字
     * @param callbackRcv
     *            表示回调 Message 的 receiver
     * @param callbackMsg
     *            表示回调 Message 的名字
     * @param args
     *            表示 Message 的参数
     */
    public abstract void rmrm(final Object receiver, final String msgname,
            final Object callbackRcv, final String callbackMsg,
            final Object... args);

    public abstract void rmm(Object receiver, String message,
            String callbackMsg, Object... args);

    public abstract void mrm(String message, Object callbackRcv,
            String callbackMsg, Object... args);

    public abstract void mm(String message, String callbackMsg, Object... args);

}
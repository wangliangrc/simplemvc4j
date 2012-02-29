package com.clark.ifk;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

class IFKCImpl implements IFK {
    Executor executor;
    Object temp;

    IFKCImpl(Executor executor) {
        super();
        this.executor = executor;
        native_init();
    }

    private native void native_init();

    @Override
    public native void register(Object receiver);

    @Override
    public native void unregister(Object receiver);

    @Override
    public native void rm(Object receiver, String message, Object... args);

    @Override
    public native void m(String message, Object... args);

    @Override
    public native void rmrm(Object receiver, String msgname,
            Object callbackRcv, String callbackMsg, Object... args);

    @Override
    public native void rmm(Object receiver, String message, String callbackMsg,
            Object... args);

    @Override
    public native void mrm(String message, Object callbackRcv,
            String callbackMsg, Object... args);

    @Override
    public native void mm(String message, String callbackMsg, Object... args);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((executor == null) ? 0 : executor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IFKCImpl other = (IFKCImpl) obj;
        if (executor == null) {
            if (other.executor != null)
                return false;
        } else if (!executor.equals(other.executor))
            return false;
        return true;
    }

    @SuppressWarnings("unused")
    private static class MethodStateHolder {
        /**
         * receiver 为 null 表示 static 方法，否则是 instance 方法
         */
        Object receiver;
        Method method;
        String[] operations;

        /**
         * receiver 和 method 唯一确定一个 MethodStateHolder 实例
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((method == null) ? 0 : method.hashCode());
            result = prime * result
                    + ((receiver == null) ? 0 : receiver.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MethodStateHolder other = (MethodStateHolder) obj;
            if (method == null) {
                if (other.method != null)
                    return false;
            } else if (!method.equals(other.method))
                return false;
            if (receiver == null) {
                if (other.receiver != null)
                    return false;
            } else if (!receiver.equals(other.receiver))
                return false;
            return true;
        }

    }

    static {
        System.loadLibrary("ifk");
    }
}

package com.clark.mvc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class SignalHandler implements InvocationHandler {
    private Facade facade;
    private String signal;

    SignalHandler(Facade facade, String signal) {
        super();
        this.facade = facade;
        this.signal = signal;
    }

    @SuppressWarnings("unchecked")
    static <T> T newSignal(Facade facade, Class<T> clazz, String signal) {
        return (T) Proxy.newProxyInstance(SignalHandler.class.getClassLoader(),
                new Class<?>[] { clazz }, new SignalHandler(facade, signal));
    }

    /**
     * 动态代理实现。<br />
     * 实际上就是将调用使用 signal 的形式封装。
     * 
     * @param proxy
     * @param method
     * @param args
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        facade.sendSignal(signal, new Invocation(proxy, method, args));
        return null;
    }

}

package com.clark.mvc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        System.out.println("***********************************");
        System.out.println("            SignalHandler          ");
        System.out.println("方法签名： " + methodToString(method));
        System.out.println("方法参数列表：");
        if (args != null && args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                System.out.println("参数" + (i + 1) + ": " + args[i]);
            }
        } else {
            System.out.println("null");
        }
        System.out.println("***********************************");
        facade.sendSignal(signal, new Invocation(proxy, method, args));
        return null;
    }

    private String methodToString(Method method) {
        try {
            StringBuffer sb = new StringBuffer();
            int mod = method.getModifiers();
            if (mod != 0) {
                sb.append(Modifier.toString(mod) + " ");
            }
            sb.append(getTypeName(method.getReturnType()) + " ");
            sb.append(method.getName() + "(");
            Class<?>[] params = method.getParameterTypes(); // avoid clone
            for (int j = 0; j < params.length; j++) {
                sb.append(getTypeName(params[j]));
                if (j < (params.length - 1))
                    sb.append(",");
            }
            sb.append(")");
            Class<?>[] exceptions = method.getExceptionTypes(); // avoid clone
            if (exceptions.length > 0) {
                sb.append(" throws ");
                for (int k = 0; k < exceptions.length; k++) {
                    sb.append(exceptions[k].getName());
                    if (k < (exceptions.length - 1))
                        sb.append(",");
                }
            }

            sb.append("  ——  ").append(getTypeName(method.getDeclaringClass()));
            return sb.toString();
        } catch (Exception e) {
            return "<" + e + ">";
        }
    }

    private String getTypeName(Class<?> type) {
        if (type.isArray()) {
            try {
                Class<?> cl = type;
                int dimensions = 0;
                while (cl.isArray()) {
                    dimensions++;
                    cl = cl.getComponentType();
                }
                StringBuffer sb = new StringBuffer();
                sb.append(cl.getSimpleName());
                for (int i = 0; i < dimensions; i++) {
                    sb.append("[]");
                }
                return sb.toString();
            } catch (Throwable e) { /* FALLTHRU */
            }
        }
        return type.getSimpleName();
    }
}

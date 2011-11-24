package com.clark.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Controller {

    private static class Holder {
        static Controller controller = new Controller();
    }

    private Controller() {
    }

    static Controller getInstance() {
        return Holder.controller;
    }

    @SuppressWarnings("rawtypes")
    private HashMap<Class, FunctionHolder> hashMap = new HashMap<Class, FunctionHolder>();

    /**
     * 注册包含 {@link Command} 注解方法的 {@link Class} 对象到 Controller 注册表中。
     * 
     * @param clazz
     *            需要注册的 {@link Class} 对象。不能为 null。
     */
    @SuppressWarnings("rawtypes")
    public synchronized void register(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }

        if (hashMap.containsKey(clazz)) {
            System.err.println("Already registered command: "
                    + clazz.getCanonicalName());
            return;
        }

        Method[] declaredMethods = clazz.getDeclaredMethods();
        String errorString = "Not found command method in ["
                + clazz.getCanonicalName() + "]";
        if (declaredMethods != null) {

            Command cmd = null;
            String name = null;
            Function function = null;
            boolean found = false;
            for (final Method method : declaredMethods) {
                cmd = method.getAnnotation(Command.class);
                if (cmd != null) {

                    name = cmd.value();
                    if (name != null && name.trim().length() > 0) {
                        function = new Function() {

                            @Override
                            public void onNotification(Notification notification) {
                                try {
                                    method.invoke(null, notification);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        Facade.getInstance().register(name, function);
                        hashMap.put(clazz, new FunctionHolder(name, function));
                        found = true;
                    }

                }
            }

            if (found) {
                System.out.println("register command: ["
                        + clazz.getCanonicalName() + "]");
            } else {
                System.err.println(errorString);
            }
        } else {
            System.err.println(errorString);
        }
    }

    /**
     * 从 Controller 注册表中移除包含 {@link Command} 注解方法的 {@link Class} 对象。
     * 
     * @param clazz
     *            将要移除的包含 {@link Command} 注解方法的 {@link Class} 对象。不能为 null。
     */
    @SuppressWarnings("rawtypes")
    public synchronized void remove(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }

        if (hashMap.containsKey(clazz)) {
            FunctionHolder functionHolder = hashMap.get(clazz);
            Facade.getInstance().remove(functionHolder.name,
                    functionHolder.function);
            hashMap.remove(clazz);
            System.out.println("remove command: [" + clazz.getCanonicalName()
                    + "]");
        } else {
            System.err.println("Not found registered command: ["
                    + clazz.getCanonicalName() + "]");
        }
    }
}

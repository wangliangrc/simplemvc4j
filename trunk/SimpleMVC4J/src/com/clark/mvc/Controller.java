package com.clark.mvc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class Controller {

    Controller(Facade facade) {
        this.facade = facade;
    }

    private HashMap<Class<?>, SignalReceiverHolder> functionHolderMap = new HashMap<Class<?>, SignalReceiverHolder>();

    private Facade facade;

    /**
     * 注册包含 {@link Command} 注解方法的 {@link Class} 对象到 Controller 注册表中。<br />
     * 注意：{@link Class} 对象 clazz 中不能含有 static 非 final 域。
     * 
     * @param clazz
     *            需要注册的 {@link Class} 对象。不能为 null。
     */
    synchronized void register(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }

        if (functionHolderMap.containsKey(clazz)) {
            System.err.println("Already registered command: "
                    + clazz.getCanonicalName());
            return;
        }

        // 检验 clazz 的无状态性
        Set<Field> fields = new HashSet<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields != null && declaredFields.length > 0) {
            fields.addAll(Arrays.asList(declaredFields));
        }

        // 将父类的 field 也全部加入
        // Class<?> superClass = clazz.getSuperclass();
        // while (superClass != null) {
        // declaredFields = superClass.getDeclaredFields();
        // if (declaredFields != null && declaredFields.length > 0) {
        // fields.addAll(Arrays.asList(declaredFields));
        // }
        // superClass = superClass.getSuperclass();
        // }

        int modifier = 0;
        for (Field field : fields) {
            modifier = field.getModifiers();
            if (Modifier.isStatic(modifier)) {
                if (!Modifier.isFinal(modifier)) {
                    throw new IllegalStateException(
                            "Controller can't have any static-non-final field!");
                }
            } else {
                continue;
            }
        }

        String errorString = "Not found command method in ["
                + clazz.getCanonicalName() + "]";

        boolean found = findCommandMethods(clazz);

        if (found) {
            System.out.println(facade + " register command: ["
                    + clazz.getCanonicalName() + "]");
        } else {
            System.err.println(errorString);
        }
    }

    private boolean findCommandMethods(Class<?> clazz) {
        Command cmd = null;
        String name = null;
        SignalReceiver function = null;
        boolean found = false;
        final Method[] declaredMethods = clazz.getDeclaredMethods();

        if (declaredMethods != null && declaredMethods.length > 0) {
            for (final Method method : declaredMethods) {
                // 只能处理 static method
                if (!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                cmd = method.getAnnotation(Command.class);
                if (cmd != null) {

                    name = cmd.value();
                    if (name != null && name.trim().length() > 0) {
                        function = new SignalReceiver() {

                            @Override
                            public void onReceive(Signal signal) {
                                try {
                                    // 保证任何修饰符都可以被调用
                                    method.setAccessible(true);
                                    method.invoke(null, signal);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        facade.registerFunction(name, function);
                        functionHolderMap.put(clazz, new SignalReceiverHolder(
                                name, function));
                        found = true;
                    }

                }
            }
        }

        // final Class<?> superClass = clazz.getSuperclass();
        // if (superClass != null) {
        // found = found | findCommandMethods(superClass);
        // }

        return found;
    }

    /**
     * 从 Controller 注册表中移除包含 {@link Command} 注解方法的 {@link Class} 对象。
     * 
     * @param clazz
     *            将要移除的包含 {@link Command} 注解方法的 {@link Class} 对象。不能为 null。
     */
    synchronized void remove(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }

        if (functionHolderMap.containsKey(clazz)) {
            SignalReceiverHolder functionHolder = functionHolderMap.get(clazz);
            facade.removeFunction(functionHolder.name, functionHolder.function);
            functionHolderMap.remove(clazz);
            System.out.println(facade + " remove command: ["
                    + clazz.getCanonicalName() + "]");
        } else {
            System.err.println("Not found registered command: ["
                    + clazz.getCanonicalName() + "]");
        }
    }

    synchronized boolean contains(Class<?> key) {
        return functionHolderMap.containsKey(key);
    }
}

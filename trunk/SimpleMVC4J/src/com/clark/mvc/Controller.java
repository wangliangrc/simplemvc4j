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

    private HashMap<Class<?>, FunctionHolder> functionHolderMap = new HashMap<Class<?>, FunctionHolder>();

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
        fields.addAll(Arrays.asList(clazz.getFields()));
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
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

        Method[] declaredMethods = clazz.getDeclaredMethods();
        String errorString = "Not found command method in ["
                + clazz.getCanonicalName() + "]";
        if (declaredMethods != null) {
            Command cmd = null;
            String name = null;
            Function function = null;
            boolean found = false;
            for (final Method method : declaredMethods) {
                // 只能处理 static method
                if (!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                cmd = method.getAnnotation(Command.class);
                if (cmd != null) {

                    name = cmd.value();
                    if (name != null && name.trim().length() > 0) {
                        function = new Function() {

                            @Override
                            public void onSignal(Signal notification) {
                                try {
                                    method.invoke(null, notification);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        facade.registerFunction(name, function);
                        functionHolderMap.put(clazz, new FunctionHolder(name,
                                function));
                        found = true;
                    }

                }
            }

            if (found) {
                System.out.println(facade + " register command: ["
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
    synchronized void remove(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }

        if (functionHolderMap.containsKey(clazz)) {
            FunctionHolder functionHolder = functionHolderMap.get(clazz);
            facade.removeFunction(functionHolder.name, functionHolder.function);
            functionHolderMap.remove(clazz);
            System.out.println(facade + " remove command: [" + clazz.getCanonicalName()
                    + "]");
        } else {
            System.err.println("Not found registered command: ["
                    + clazz.getCanonicalName() + "]");
        }
    }

    synchronized boolean contains(Class<?> key) {
        return functionHolderMap.containsKey(key);
    }
}

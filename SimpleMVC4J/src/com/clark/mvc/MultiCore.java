package com.clark.mvc;

import java.util.HashMap;

public final class MultiCore implements Constants {
    private static HashMap<String, Facade> facades = new HashMap<String, Facade>();

    public static final Facade GLOBAL = new Facade("", null);

    private static synchronized Facade facade(String name, Facade parent) {
        if (name == null || name.length() == 0)
            throw new NullPointerException("Argument 'name' mustn't be empty!");

        if (parent == null) {
            parent = GLOBAL;
        }

        // 如果之前没有 name 对应的 Facade 对象则创建一个并注册到 Facade 注册表上。
        Facade facade = facades.get(name);
        if (facade == null) {
            facade = new Facade(name, parent);
            facades.put(name, facade);
            if (DEBUG) {
                System.out.println("register facade:" + facade);
            }
            return facade;
        }

        // Facade 对象的 parent 属性不能使自身
        if (facade.equals(parent)) {
            throw new IllegalArgumentException("Self dependent!");
        }

        // 如果 parent 与之前默认的 Facade 的 parent 值不相同则修改
        if (!facade.parent.equals(parent)) {
            facade.parent = parent;
        }
        return facade;
    }

    /**
     * 根据指定 object 对象找到对应的 Facade 对象。如果找不到就创建一个。
     * 
     * @param object
     * @return
     */
    public static Facade newCore(Object object) {
        if (object instanceof CharSequence) {
            return facade(object.toString(), null);
        } else {
            return facade(identityObject(object), null);
        }
    }

    /**
     * 根据指定 object 对象找到对应的 Facade 对象，并设置其 parent 属性的值。如果找不到就创建一个。
     * 
     * @param object
     * @param parent
     * @return
     */
    public static Facade newCore(Object object, Facade parent) {
        if (object instanceof CharSequence) {
            return facade(object.toString(), parent);
        } else {
            return facade(identityObject(object), parent);
        }
    }

    /**
     * 返回一般对象的 String 表达式。
     * 
     * @param object
     * @return
     */
    private static String identityObject(Object object) {
        return object.getClass().getCanonicalName() + "@"
                + System.identityHashCode(object);
    }

    /**
     * 从 Facade 注册表中移除 name 指定的 Facade。
     * 
     * @param name
     */
    private static synchronized void remove(String name) {
        if (name != null) {
            final Facade facade = facades.remove(name);
            if (facade != null) {
                if (DEBUG) {
                    System.out.println("remove facade:" + facade);
                }
                // 将 parent 中为 name 的 Facade 删除
                for (Facade facade2 : facades.values()) {
                    if (facade2.parent != null
                            && facade2.parent.getName().equals(name)) {
                        facade2.resetParent();
                    }
                }
            } else {
                if (DEBUG) {
                    System.err.println("Can't find facade named \"" + name
                            + "\"!");
                }
            }
        }
    }

    /**
     * 从 Facade 注册表中移除 object 指定的 Facade。
     * 
     * @param object
     */
    public static void removeCore(Object object) {
        if (object instanceof CharSequence) {
            remove(object.toString());
        } else {
            remove(identityObject(object));
        }
    }

    public static void setRunner(UIRunnable runner) {
        GLOBAL.setRunner(runner);
    }

    public static <T> T sendSignal(Class<T> clazz, String signal) {
        return GLOBAL.sendSignal(clazz, signal);
    }

    public static <T> void sendSignal(String signalName, String type, T body) {
        GLOBAL.sendSignal(signalName, type, body);
    }

    public static <T> void sendSignal(String signalName, T body) {
        GLOBAL.sendSignal(signalName, body);
    }

    public static void sendSignal(String signalName) {
        GLOBAL.sendSignal(signalName);
    }

    public static void registerView(Object object) {
        GLOBAL.registerView(object);
    }

    public static void removeView(Object object) {
        GLOBAL.removeView(object);
    }

    public static boolean containsView(Object key) {
        return GLOBAL.containsView(key);
    }

    public static void registerController(Class<?> clazz) {
        GLOBAL.registerController(clazz);
    }

    public static void removeController(Class<?> clazz) {
        GLOBAL.removeController(clazz);
    }

    public static boolean containsController(Class<?> key) {
        return GLOBAL.containsController(key);
    }

    public static void registerProxy(Proxy proxy) {
        GLOBAL.registerProxy(proxy);
    }

    public static void removeProxy(Proxy proxy) {
        GLOBAL.removeProxy(proxy);
    }

    public static Proxy getProxy(String proxyName) {
        return GLOBAL.getProxy(proxyName);
    }

    public static boolean containsProxy(String proxyName) {
        return GLOBAL.containsProxy(proxyName);
    }

}

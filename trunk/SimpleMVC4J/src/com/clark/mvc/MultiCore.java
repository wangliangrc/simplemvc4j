package com.clark.mvc;

import java.util.HashMap;

public final class MultiCore {
    private static HashMap<String, Facade> facades = new HashMap<String, Facade>();

    public static final Facade MAIN_CORE = new Facade("", null);

    private static synchronized Facade facade(String name, Facade parent) {
        if (name == null || name.length() == 0)
            throw new NullPointerException("Argument 'name' mustn't be empty!");

        if (parent == null) {
            parent = MAIN_CORE;
        }

        // 如果之前没有 name 对应的 Facade 对象则创建一个并注册到 Facade 注册表上。
        Facade facade = facades.get(name);
        if (facade == null) {
            facade = new Facade(name, parent);
            facades.put(name, facade);
            System.out.println("register facade:" + facade);
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
                System.out.println("remove facade:" + facade);
            } else {
                System.err.println("Can't find facade named \"" + name + "\"!");
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

    public void setUIWorker(UIWorker worker) {
        MAIN_CORE.setUIWorker(worker);
    }

    public void sendNotification(String notificationName, Object body,
            String type) {
        MAIN_CORE.sendNotification(notificationName, body, type);
    }

    public void sendNotification(String notificationName, Object body) {
        MAIN_CORE.sendNotification(notificationName, body);
    }

    public void sendNotification(String notificationName) {
        MAIN_CORE.sendNotification(notificationName);
    }

    public void registerView(Object object) {
        MAIN_CORE.registerView(object);
    }

    public void removeView(Object object) {
        MAIN_CORE.removeView(object);
    }

    public boolean containsView(Object key) {
        return MAIN_CORE.containsView(key);
    }

    public void registerController(Class<?> clazz) {
        MAIN_CORE.registerController(clazz);
    }

    public void removeController(Class<?> clazz) {
        MAIN_CORE.removeController(clazz);
    }

    public boolean containsController(Class<?> key) {
        return MAIN_CORE.containsController(key);
    }

    public void registerProxy(Proxy proxy) {
        MAIN_CORE.registerProxy(proxy);
    }

    public void removeProxy(Proxy proxy) {
        MAIN_CORE.removeProxy(proxy);
    }

    public Proxy getProxy(String proxyName) {
        return MAIN_CORE.getProxy(proxyName);
    }

    public boolean containsProxy(String proxyName) {
        return MAIN_CORE.containsProxy(proxyName);
    }

}

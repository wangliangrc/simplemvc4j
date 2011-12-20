package com.clark.mvc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Facade {
    private static HashMap<String, Facade> facades = new HashMap<String, Facade>();

    public static final Facade MAIN_FACADE = new Facade("", null);

    static {
        facades.put(MAIN_FACADE.getName(), MAIN_FACADE);
    }

    private static synchronized Facade facade(String name, Facade parent) {
        if (name == null || name.length() == 0)
            throw new NullPointerException("Argument 'name' mustn't be empty!");

        if (parent == null) {
            parent = MAIN_FACADE;
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
    public static Facade facade(Object object) {
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
    public static Facade facade(Object object, Facade parent) {
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
    public static void removeFacade(Object object) {
        if (object instanceof CharSequence) {
            remove(object.toString());
        } else {
            remove(identityObject(object));
        }
    }

    /**
     * private 构造函数，拒绝继承。
     * 
     * @param name
     *            指定 Facade 的 name。
     * @param parent
     *            指定 parent Facade。
     */
    private Facade(String name, Facade parent) {
        this.name = name;
        this.parent = parent;
    }

    private final String name;
    private volatile Facade parent;

    private final View view = new View(this);
    private final Controller controller = new Controller(this);
    private final Model model = new Model();

    private final HashMap<String, Set<Function>> functionMap = new HashMap<String, Set<Function>>();
    private volatile UIWorker uiWorker;

    /**
     * 注册一个 {@link Function} 对象到 name 指定的 key 上。
     * 
     * @param name
     * @param function
     */
    synchronized void registerFunction(String name, Function function) {
        if (name != null && function != null) {
            Set<Function> functions = getFunctions(name);
            functions.add(function);
        }
    }

    /**
     * 移除 name 对应的所有 {@link Set}<{@link Function}> 集合中的某一个 {@link Function} 对象。
     * 
     * @param name
     * @param function
     */
    synchronized void removeFunction(String name, Function function) {
        if (name != null && function != null && functionMap.containsKey(name)) {
            Set<Function> set = functionMap.get(name);
            set.remove(function);
        }
    }

    /**
     * 移除 name 对应的所有 {@link Set}<{@link Function}> 集合。
     * 
     * @param name
     */
    synchronized void removeFunction(String name) {
        functionMap.remove(name);
    }

    /**
     * 该方法确定是否运行在 {@link UIWorker} 所在线程上。<br />
     * 注意：该方法会继承 parent 的 {@link UIWorker} 属性，即如果自身没有设置 {@link UIWorker}
     * 属性还会自动查看 parent 的 {@link UIWorker} 属性，直到 MAIN Facade 为止.
     * 
     * @param notification
     */
    synchronized void notify(final Notification notification) {
        if (notification != null) {
            if (uiWorker != null) {
                uiWorker.postTask(new Runnable() {

                    @Override
                    public void run() {
                        notifyInternal(notification);
                    }
                });
            } else if (parent != null) {
                parent.notify(notification);
            } else {
                notifyInternal(notification);
            }
        }
    }

    /**
     * 该方法会遍历 Function Map 查找处相关的 Function 对象并依次 调用它们的
     * {@link Function#onNotification(Notification)} 方法。<br />
     * 注意：本方法还会调用 parent 属性的 {@link #notifyInternal(Notification)} 方法，在 parent
     * 不为 null 的时候。
     * 
     * @param notification
     */
    private void notifyInternal(Notification notification) {
        Set<Function> set = functionMap.get(notification.name);
        if (set != null && set.size() > 0) {
            System.out.println(toString() + " notified at "
                    + Thread.currentThread().toString());

            for (Function function : set) {
                function.onNotification(notification);
            }
        }

        // call parent's functions
        if (parent != null) {
            parent.notifyInternal(notification);
        }
    }

    private Set<Function> getFunctions(String name) {
        Set<Function> set = functionMap.get(name);
        if (set == null) {
            set = new HashSet<Function>();
            functionMap.put(name.intern(), set);
        }
        return set;
    }

    public String getName() {
        return name;
    }

    public Facade getParent() {
        return parent;
    }

    /**
     * 设置 parent Facade 对象。<br />
     * 注意：parent 不能为 null 或者自身。
     * 
     * @param parent
     */
    public void setParent(Facade parent) {
        if (parent == null) {
            throw new NullPointerException();
        }
        if (equals(parent)) {
            throw new IllegalArgumentException("Self dependent!");
        }
        this.parent = parent;
    }

    /**
     * 重置 parent Facade 对象，该方法会将其置为 MAIN Facade。<br />
     * 注意：MAIN Facade 对象不能调用该方法！
     */
    public void resetParent() {
        if (equals(MAIN_FACADE)) {
            throw new IllegalArgumentException("Self dependent!");
        }
        parent = MAIN_FACADE;
    }

    /**
     * 设置UI线程执行者。
     * 
     * @param worker
     *            可以为 null。如果为 null 则表示 sendNotification 方法的执行线程与调用者线程一致。
     */
    public void setUIWorker(UIWorker worker) {
        uiWorker = worker;
    }

    /**
     * 发送 {@link Notification} 通知实例。
     * <p>
     * 注意：回调方法运行线程由该方法所在线程决定。
     * 
     * @param notificationName
     *            通知名称。不能为 null。
     * @param body
     *            通知包含消息体。
     * @param type
     *            通知类型。
     */
    public void sendNotification(String notificationName, Object body,
            String type) {
        if (notificationName == null) {
            throw new NullPointerException();
        }

        if (notificationName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "notificationName mustn't be empty!");
        }
        notify(new Notification(notificationName, body, type));
    }

    /**
     * 发送 {@link Notification} 通知实例。
     * <p>
     * 注意：回调方法运行线程由该方法所在线程决定。
     * 
     * @param notificationName
     *            通知名称。不能为 null。
     * @param body
     *            通知包含消息体。
     */
    public void sendNotification(String notificationName, Object body) {
        sendNotification(notificationName, body, null);
    }

    /**
     * 发送 {@link Notification} 通知实例。
     * <p>
     * 注意：回调方法运行线程由该方法所在线程决定。
     * 
     * @param notificationName
     *            通知名称。不能为 null。
     */
    public void sendNotification(String notificationName) {
        sendNotification(notificationName, null);
    }

    public void registerView(Object object) {
        view.register(object);
    }

    public void removeView(Object object) {
        view.remove(object);
    }

    public boolean containsView(Object key) {
        return view.contains(key);
    }

    public void registerController(Class<?> clazz) {
        controller.register(clazz);
    }

    public void removeController(Class<?> clazz) {
        controller.remove(clazz);
    }

    public boolean containsController(Class<?> key) {
        return controller.contains(key);
    }

    public void registerProxy(Proxy proxy) {
        model.register(proxy);
    }

    public void removeProxy(Proxy proxy) {
        model.remove(proxy);
    }

    public Proxy getProxy(String proxyName) {
        return model.get(proxyName);
    }

    public boolean containsProxy(String proxyName) {
        return model.contains(proxyName);
    }

    /**
     * 依据 name 属性生成 hash code。
     * 
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * 比较两个 Facade 的 name 是否相同。
     * 
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Facade other = (Facade) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /**
     * 输出形如:"Facade [name=xxx]"的字符串。
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (name.equals("")) {
            builder.append("Facade [Main Facade]");
        } else {
            builder.append("Facade [name=").append(name).append("]");
        }
        return builder.toString();
    }

}

package com.clark.mvc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Facade {
    private static HashMap<String, Facade> facades = new HashMap<String, Facade>();

    public static final Facade MAIN = new Facade("", null);

    static {
        facades.put(MAIN.getName(), MAIN);
    }

    private static synchronized Facade facade(String name, Facade parent) {
        if (name == null || name.length() == 0)
            throw new NullPointerException("Argument 'name' mustn't be empty!");

        Facade facade = facades.get(name);
        if (facade == null) {
            facade = new Facade(name, parent);
            facades.put(name, facade);
        }
        if (!facade.parent.equals(parent)) {
            facade.parent = parent;
        }
        return facade;
    }

    public static Facade facade(Object object) {
        if (object instanceof String) {
            return facade((String) object, null);
        } else {
            return facade(identityObject(object), null);
        }
    }

    public static Facade facade(Object object, Facade parent) {
        if (object instanceof String) {
            return facade((String) object, parent);
        } else {
            return facade(identityObject(object), parent);
        }
    }

    private static String identityObject(Object object) {
        return object.getClass().getCanonicalName() + "@"
                + System.identityHashCode(object);
    }

    private static synchronized void remove(String name) {
        if (name != null) {
            facades.remove(name);
        }
    }

    public static void remove(Object object) {
        if (object instanceof String) {
            remove((String) object);
        } else {
            remove(identityObject(object));
        }
    }

    private Facade(String name, Facade parent) {
        this.name = name;
        if (parent == null) {
            this.parent = MAIN;
        } else {
            this.parent = parent;
        }
    }

    private final String name;
    private volatile Facade parent;

    private final View view = new View(this);
    private final Controller controller = new Controller(this);
    private final Model model = new Model();

    private final HashMap<String, Set<Function>> hashMap = new HashMap<String, Set<Function>>();
    private volatile UIWorker worker;

    synchronized void registerFunction(String name, Function function) {
        if (name != null && function != null) {
            Set<Function> functions = getFunctions(name);
            functions.add(function);
        }
    }

    synchronized void removeFunction(String name, Function function) {
        if (name != null && function != null && hashMap.containsKey(name)) {
            Set<Function> set = hashMap.get(name);
            set.remove(function);
        }
    }

    synchronized void removeFunction(String name) {
        hashMap.remove(name);
    }

    void setWorker(UIWorker worker) {
        this.worker = worker;
    }

    synchronized void notify(final Notification notification) {
        if (notification != null) {
            if (worker != null) {
                worker.postTask(new Runnable() {

                    @Override
                    public void run() {
                        notifyInternal(notification);
                    }
                });
            } else {
                notifyInternal(notification);
            }
        }
    }

    private void notifyInternal(Notification notification) {
        Set<Function> set = hashMap.get(notification.name);
        if (set != null && set.size() > 0) {
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
        Set<Function> set = hashMap.get(name);
        if (set == null) {
            set = new HashSet<Function>();
            hashMap.put(name.intern(), set);
        }
        return set;
    }

    public String getName() {
        return name;
    }

    public Facade getParent() {
        return parent;
    }

    public void setParent(Facade parent) {
        if (parent == null) {
            throw new NullPointerException();
        }
        this.parent = parent;
    }

    public void resetParent() {
        parent = MAIN;
    }

    /**
     * 设置UI线程执行者。
     * 
     * @param worker
     *            可以为 null。如果为 null 则表示 sendNotification 方法的执行线程与调用者线程一致。
     */
    public void setUIWorker(UIWorker worker) {
        setWorker(worker);
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

    /**
     * 获取 {@link View} 单例。
     * 
     * @return {@link View} 单例。
     */
    public View view() {
        return view;
    }

    /**
     * 获取 {@link Controller} 单例。
     * 
     * @return {@link Controller} 单例。
     */
    public Controller controller() {
        return controller;
    }

    /**
     * 获取 {@link Model} 单例。
     * 
     * @return {@link Model} 单例。
     */
    public Model model() {
        return model;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Facade other = (Facade) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

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

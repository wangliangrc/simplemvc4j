package com.clark.mvc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Facade {

    private static class Holder {
        static Facade instance = new Facade();
    }

    private Facade() {
    }

    /**
     * 获取 Facade 自身的单例。
     * 
     * @return Facade 自身的单例。
     */
    static Facade mvc() {
        return Holder.instance;
    }

    private HashMap<String, Set<Function>> hashMap = new HashMap<String, Set<Function>>();

    synchronized void register(String name, Function function) {
        if (name != null && function != null) {
            Set<Function> functions = getFunctions(name);
            functions.add(function);
        }
    }

    synchronized void remove(String name, Function function) {
        if (name != null && function != null && hashMap.containsKey(name)) {
            Set<Function> set = hashMap.get(name);
            set.remove(function);
        }
    }

    synchronized void remove(String name) {
        hashMap.remove(name);
    }

    synchronized void notify(Notification notification) {
        if (notification != null) {
            Set<Function> set = hashMap.get(notification.name);
            if (set != null && set.size() > 0) {
                for (Function function : set) {
                    function.onNotification(notification);
                }
            }
        }
    }

    private Set<Function> getFunctions(String name) {
        Set<Function> set = hashMap.get(name);
        if (set == null) {
            set = new HashSet<Function>();
            hashMap.put(name, set);
        }
        return set;
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
    public static void sendNotification(String notificationName, Object body,
            String type) {
        if (notificationName == null) {
            throw new NullPointerException();
        }

        if (notificationName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "notificationName mustn't be empty!");
        }
        mvc().notify(new Notification(notificationName, body, type));
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
    public static void sendNotification(String notificationName, Object body) {
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
    public static void sendNotification(String notificationName) {
        sendNotification(notificationName, null);
    }

    /**
     * 获取 {@link View} 单例。
     * 
     * @return {@link View} 单例。
     */
    public static View view() {
        return View.getInstance();
    }

    /**
     * 获取 {@link Controller} 单例。
     * 
     * @return {@link Controller} 单例。
     */
    public static Controller controller() {
        return Controller.getInstance();
    }

    /**
     * 获取 {@link Model} 单例。
     * 
     * @return {@link Model} 单例。
     */
    public static Model model() {
        return Model.getInstance();
    }
}

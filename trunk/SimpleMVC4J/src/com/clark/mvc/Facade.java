package com.clark.mvc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Facade {

    /**
     * 构造函数，拒绝包外继承。
     * 
     * @param name
     *            指定 Facade 的 name。
     * @param parent
     *            指定 parent Facade。
     */
    Facade(String name, Facade parent) {
        this.name = name;
        this.parent = parent;
    }

    private final String name;
    volatile Facade parent;

    private final View view = new View(this);
    private final Controller controller = new Controller(this);
    private final Model model = new Model(this);

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
     * @param signal
     */
    synchronized void notify(final Signal signal) {
        if (signal != null) {
            UIWorker worker = findUIWorker(this);
            if (worker != null) {
                worker.postTask(new Runnable() {

                    @Override
                    public void run() {
                        signalInternal(signal);
                    }
                });
            } else {
                signalInternal(signal);
            }
        }
    }

    private static UIWorker findUIWorker(Facade facade) {
        if (facade == null) {
            return null;
        } else if (facade.uiWorker != null) {
            return facade.uiWorker;
        } else {
            return findUIWorker(facade.parent);
        }
    }

    /**
     * 该方法会遍历 Function Map 查找处相关的 Function 对象并依次 调用它们的
     * {@link Function#onSignal(Signal)} 方法。<br />
     * 注意：本方法还会调用 parent 属性的 {@link #signalInternal(Signal)} 方法，在 parent 不为 null
     * 的时候。
     * 
     * @param signal
     */
    private void signalInternal(Signal signal) {
        Set<Function> set = functionMap.get(signal.name);
        if (set != null && set.size() > 0) {
            System.out.println(toString() + " notified at "
                    + Thread.currentThread().toString());

            for (Function function : set) {
                function.onSignal(signal);
            }
        }

        // call parent's functions
        if (parent != null) {
            parent.signalInternal(signal);
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
        if (equals(MultiCore.MAIN_CORE)) {
            throw new IllegalArgumentException("Self dependent!");
        }
        parent = MultiCore.MAIN_CORE;
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
     * 使用动态代理完成回调逻辑
     * 
     * @param clazz
     *            必须为 Interface 类型
     * @param signal
     * @return
     */
    public <T> T callback(Class<T> clazz, String signal) {
        return SignalHandler.newSignal(this, clazz, signal);
    }

    /**
     * 发送 {@link Signal} 通知实例。
     * <p>
     * 注意：回调方法运行线程由该方法所在线程决定。
     * 
     * @param signalName
     *            通知名称。不能为 null。
     * @param body
     *            通知包含消息体。
     * @param type
     *            通知类型。
     */
    public void sendSignal(String signalName, Object body, String type) {
        if (signalName == null) {
            throw new NullPointerException();
        }

        if (signalName.trim().length() == 0) {
            throw new IllegalArgumentException("signalName mustn't be empty!");
        }
        notify(new Signal(signalName, body, type));
    }

    /**
     * 发送 {@link Signal} 通知实例。
     * <p>
     * 注意：回调方法运行线程由该方法所在线程决定。
     * 
     * @param signalName
     *            通知名称。不能为 null。
     * @param body
     *            通知包含消息体。
     */
    public void sendSignal(String signalName, Object body) {
        sendSignal(signalName, body, null);
    }

    /**
     * 发送 {@link Signal} 通知实例。
     * <p>
     * 注意：回调方法运行线程由该方法所在线程决定。
     * 
     * @param signalName
     *            通知名称。不能为 null。
     */
    public void sendSignal(String signalName) {
        sendSignal(signalName, null);
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

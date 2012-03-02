package com.clark.ifk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class IFKJavaImpl extends IFK {
    private Map<Object, List<MethodStateHolder>> receiverHolders = new HashMap<Object, List<MethodStateHolder>>();
    private Map<String, List<MethodStateHolder>> signalHolders = new HashMap<String, List<MethodStateHolder>>();
    private Map<Object, Integer> receiversLevel = new HashMap<Object, Integer>();

    private final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
            10);
    private final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "IFK Async #"
                    + mCount.getAndIncrement());
            thread.setDaemon(false);
            return thread;
        }
    };
    private final Executor DEFAULT_SYNC_EXECUTOR = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "IFK Sync [DEFAULT]");
                    thread.setDaemon(false);
                    return thread;
                }
            });
    private static final boolean DEBUG_WITHOUT_THREAD = false;

    IFKJavaImpl() {
        asyncExecutor = new ThreadPoolExecutor(5, 64, 1, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory,
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    @Override
    public final void register(Object receiver, int level) {
        if (receiver == null) {
            System.err.println("IFK.register() can't accept null param");
            return;
        }

        if (receiverHolders.containsKey(receiver)) {
            System.err.println("You have already register receiver: "
                    + receiver);
            return;
        }

        Class<?> clazz = null;
        if (receiver instanceof Class) {
            clazz = (Class<?>) receiver;
        } else {
            clazz = receiver.getClass();
        }
        Method[] methods = clazz.getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return;
        }

        List<MethodStateHolder> holders = new ArrayList<MethodStateHolder>();
        SignalReceiver invoker = null;
        int modifier = 0;
        for (int i = 0, len = methods.length; i < len; i++) {
            modifier = methods[i].getModifiers();
            if ((receiver instanceof Class && !Modifier.isStatic(modifier))
                    || (!(receiver instanceof Class) && Modifier
                            .isStatic(modifier))) {
                continue;
            }
            invoker = methods[i].getAnnotation(SignalReceiver.class);
            if (invoker == null || invoker.value() == null
                    || invoker.value().length == 0) {
                continue;
            }
            if (verifyMethodFail(methods[i])) {
                continue;
            }
            MethodStateHolder holder = new MethodStateHolder();
            String[] signalNames = invoker.value();
            holder.signalNames = signalNames;
            holder.method = methods[i];
            holder.receiver = receiver;
            holder.threadStrategy = invoker.threadStrategy();
            holder.signalLevel = invoker.signalLevel();
            holders.add(holder);
            List<MethodStateHolder> oplist = null;
            for (int j = 0, size = signalNames.length; j < size; j++) {
                synchronized (signalHolders) {
                    if (signalHolders.containsKey(signalNames[j])) {
                        oplist = signalHolders.get(signalNames[j]);
                    } else {
                        oplist = new ArrayList<MethodStateHolder>();
                    }
                    // 另一个线程可以在这个时候修改 oplist ？
                    synchronized (oplist) {
                        oplist.add(holder);
                        signalHolders.put(signalNames[j].intern(), oplist);
                    }
                }
            }
        }
        if (holders.size() > 0) {
            synchronized (receiverHolders) {
                receiverHolders.put(receiver, holders);
            }
            synchronized (receiversLevel) {
                receiversLevel.put(receiver, level);
            }
        }
    }

    private boolean verifyMethodFail(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String methodName = null;
        if (Modifier.isStatic(method.getModifiers())) {
            methodName = method.getDeclaringClass().getCanonicalName() + "."
                    + method.getName();
        } else {
            methodName = method.getDeclaringClass().getCanonicalName() + "#"
                    + method.getName();
        }
        if (parameterTypes == null || parameterTypes.length == 0) {
            System.err.println(methodName + " 方法没有参数，不符合格式");
            return true;
        }

        if (parameterTypes.length != 1) {
            System.err.println(methodName + " 方法参数个数多于 1 个，不符合格式");
            return true;
        }

        if (parameterTypes[0] != Signal.class) {
            System.err.println(methodName + " 方法第一个参数不是 "
                    + Signal.class.getCanonicalName() + " 类型");
            return true;
        }

        return false;
    }

    @Override
    public final void unregister(Object receiver) {
        if (receiver == null) {
            System.err.println("IFK.unregister() can't accept null param");
            return;
        }

        List<MethodStateHolder> methodlist = null;
        synchronized (receiverHolders) {
            methodlist = receiverHolders.get(receiver);
            receiverHolders.remove(receiver);
        }
        synchronized (receiversLevel) {
            receiversLevel.remove(receiver);
        }

        List<MethodStateHolder> holdlist = null;
        if (methodlist != null) {
            for (MethodStateHolder holder : methodlist) {
                synchronized (signalHolders) {
                    for (String op : holder.signalNames) {
                        holdlist = signalHolders.get(op);
                        if (holdlist == null) {
                            continue;
                        }
                        synchronized (holdlist) {
                            holdlist.remove(holder);
                            if (holdlist.isEmpty()) {
                                signalHolders.remove(op);
                            }
                        }
                    }
                }
            }
        } else {
            synchronized (signalHolders) {
                Iterator<List<MethodStateHolder>> holdsIterator = signalHolders
                        .values().iterator();
                List<MethodStateHolder> holders = null;
                Iterator<MethodStateHolder> holdIterator = null;
                MethodStateHolder holder = null;
                while (holdsIterator.hasNext()) {
                    holders = holdsIterator.next();
                    synchronized (holders) {
                        holdIterator = holders.iterator();
                        while (holdIterator.hasNext()) {
                            holder = holdIterator.next();
                            if (holder.receiver == receiver) {
                                holdIterator.remove();
                            }
                        }
                        if (holders.size() == 0) {
                            holdsIterator.remove();
                        }
                    }
                }
            }
        }

    }

    @Override
    public void setLevel(Object receiver, int level) {
        if (level < MIN_LEVEL || level > MAX_LEVEL) {
            throw new IndexOutOfBoundsException("超出Level设置范围");
        }

        synchronized (receiversLevel) {
            if (!receiversLevel.containsKey(receiver)) {
                return;
            }
            receiversLevel.put(receiver, level);
        }
    }

    /**
     * 
     * @param receiver
     *            如果是 Class 的实例则调用 static 方法；如果是 instance 则调用 instance 方法；如果为
     *            null 则调用 static 和 instance 方法。
     * @param message
     *            表示 Message 的名字
     * @param isSync
     * @param callbackRcv
     *            表示回调 Message 的 receiver
     * @param callbackMsg
     *            表示回调 Message 的名字
     * @param isCallbackSync
     * @param extra
     *            表示 Message 的参数
     */
    @Override
    protected void invokeExecutor(InvocationInteraction interaction) {
        assert interaction.requestMsg.signal != null
                && interaction.requestMsg.signal.length() > 0;
        List<MethodStateHolder> holders = null;
        List<MethodStateHolder> preparelist = null;

        synchronized (signalHolders) {
            if (!signalHolders.containsKey(interaction.requestMsg.signal)) {
                return;
            }
            holders = signalHolders.get(interaction.requestMsg.signal);
            if (holders == null || holders.size() == 0) {
                return;
            }
        }

        synchronized (holders) {
            preparelist = filter(interaction, holders);
        }

        // fillterReceivers(interaction, holders);
        // 调用每一个过滤后省下来的方法
        if (preparelist != null && preparelist.size() > 0) {
            for (MethodStateHolder holder : preparelist) {
                invokeInternal(interaction, holder);
            }
        }
    }

    /**
     * 过滤 receiver 和 level
     * 
     * @param interaction
     * @param holders
     * @return
     */
    private List<MethodStateHolder> filter(InvocationInteraction interaction,
            List<MethodStateHolder> holders) {
        List<MethodStateHolder> result = new ArrayList<MethodStateHolder>();
        if (interaction.requestMsg.receiver != null) {
            int receiverLevel = receiversLevel
                    .get(interaction.requestMsg.receiver);
            if (receiverLevel > interaction.requestMsg.signalLevel) {
                // receiver 的 Level 高于请求 level，请求被拒绝
                return result;
            }

            // 过滤 receiver
            // static 方法
            if (interaction.requestMsg.receiver instanceof Class) {
                for (MethodStateHolder holder : holders) {
                    // Class 实例是单例的，所以可以直接使用 == 符号
                    if (holder.receiver != interaction.requestMsg.receiver) {
                        continue;
                    }

                    filterMethodLevels(interaction, holder, result);
                }
            }
            // instance 方法
            else {
                for (MethodStateHolder holder : holders) {
                    if (!holder.receiver
                            .equals(interaction.requestMsg.receiver)) {
                        continue;
                    }

                    filterMethodLevels(interaction, holder, result);
                }
            }
        } else {
            for (MethodStateHolder holder : holders) {
                filterMethodLevels(interaction, holder, result);
            }
        }
        return result;
    }

    /**
     * 依据单个方法的 level 过滤
     * 
     * @param interaction
     * @param holder
     * @param result
     */
    private void filterMethodLevels(InvocationInteraction interaction,
            MethodStateHolder holder, List<MethodStateHolder> result) {
        if (holder.signalLevel > interaction.requestMsg.signalLevel) {
            return;
        }

        result.add(holder);
        // invokeInternal(interaction, holder);
    }

    /**
     * 真正调用一个方法，期间决定运行线程
     * 
     * @param interaction
     * @param holder
     */
    private void invokeInternal(final InvocationInteraction interaction,
            final MethodStateHolder holder) {
        Runnable runnable = new Runnable() {
            public void run() {
                Object returnVal = null;
                try {
                    holder.method.setAccessible(true);
                    SignalProducer producer = new SignalProducer();
                    producer.signal = interaction.requestMsg.signal;
                    producer.extra = interaction.extra;
                    producer.level = interaction.requestMsg.signalLevel;

                    final Signal msg = new Signal(producer);
                    returnVal = holder.method.invoke(
                            holder.receiver instanceof Class ? null
                                    : holder.receiver, msg);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    returnVal = e.getCause();
                    if (returnVal instanceof RuntimeException) {
                        throw (RuntimeException) returnVal;
                    }
                } catch (RuntimeException e) {
                    throw e;
                }
                invokeResponse(interaction, returnVal);
            }
        };

        if (DEBUG_WITHOUT_THREAD) {
            runnable.run();
            return;
        }

        // 传入的参数优先级比较大
        boolean sync = false;
        if (interaction.requestMsg.threadStrategy == ThreadStrategy.DEFAULT) {
            sync = holder.threadStrategy == ThreadStrategy.DEFAULT
                    || holder.threadStrategy == ThreadStrategy.SYNCHRONOUS;
        } else {
            sync = interaction.requestMsg.threadStrategy == ThreadStrategy.SYNCHRONOUS;
        }

        if (sync) {
            if (syncExecutor == null) {
                DEFAULT_SYNC_EXECUTOR.execute(runnable);
            } else {
                syncExecutor.execute(runnable);
            }
        } else {
            asyncExecutor.execute(runnable);
        }
    }

    private void invokeResponse(InvocationInteraction interaction,
            Object returnVal) {
        if (interaction.responseMsg.signal == null
                || interaction.responseMsg.signal.length() == 0) {
            return;
        }

        InvocationInteraction responseInteraction = new InvocationInteraction();
        // 原来的 response 此时为新的 interaction 的 request
        responseInteraction.requestMsg = interaction.responseMsg;

        // 回调部分为空，不会循环处理
        if (returnVal == null || returnVal instanceof Void) {
            invokeExecutor(responseInteraction);
        } else {
            if (returnVal instanceof Type[]) {
                // Type 只用于参数转换，不作为参数传递
                final Type[] src = (Type[]) returnVal;
                int len = src.length;
                if (len == 0) {
                    invokeExecutor(responseInteraction);
                }
                final Object[] dest = new Object[src.length];
                for (int i = 0; i < len; i++) {
                    dest[i] = src[i].toObject();
                }
                responseInteraction.extra = dest;
                invokeExecutor(responseInteraction);
            } else if (returnVal instanceof Object[]) {
                final Object[] dest = (Object[]) returnVal;
                for (int i = 0, len = dest.length; i < len; i++) {
                    if (dest[i] instanceof Type) {
                        dest[i] = ((Type) dest[i]).toObject();
                    }
                }
                responseInteraction.extra = dest;
                invokeExecutor(responseInteraction);
            } else if (returnVal instanceof Type) {
                responseInteraction.extra = new Object[] { ((Type) returnVal)
                        .toObject() };
                invokeExecutor(responseInteraction);
            } else {
                responseInteraction.extra = new Object[] { returnVal };
                invokeExecutor(responseInteraction);
            }
        }
    }

    private static class MethodStateHolder {
        /**
         * receiver 为 Class 实例表示 static 方法，否则是 instance 方法
         */
        Object receiver;
        Method method;
        String[] signalNames;
        ThreadStrategy threadStrategy = ThreadStrategy.DEFAULT;
        int signalLevel = IFK.DEFAULT_LEVEL;

        /**
         * receiver 和 method 唯一确定一个 MethodStateHolder 实例
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((method == null) ? 0 : method.hashCode());
            result = prime * result
                    + ((receiver == null) ? 0 : receiver.hashCode());
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
            MethodStateHolder other = (MethodStateHolder) obj;
            if (method == null) {
                if (other.method != null)
                    return false;
            } else if (!method.equals(other.method))
                return false;
            if (receiver == null) {
                if (other.receiver != null)
                    return false;
            } else if (!receiver.equals(other.receiver))
                return false;
            return true;
        }

    }
}

package com.clark.ifk;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class IFKJavaImpl extends IFK {
    private Map<Object, List<MethodStateHolder>> receiverTable = new HashMap<Object, List<MethodStateHolder>>();
    private Map<String, List<MethodStateHolder>> operatorTable = new HashMap<String, List<MethodStateHolder>>();

    private final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
            10);
    private final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "IFK Async #" + mCount.getAndIncrement());
        }
    };
    private final Executor DEFAULT_SYNC_EXECUTOR = Executors
            .newSingleThreadExecutor(new ThreadFactory() {

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "IFK default Sync Executor");
                }
            });

    IFKJavaImpl() {
        asyncExecutor = new ThreadPoolExecutor(5, 64, 1, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory,
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    @Override
    public final void register(Object receiver) {
        if (receiver == null) {
            System.err.println("IFK.register() can't accept null param");
            return;
        }

        if (receiverTable.containsKey(receiver)) {
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

        List<MethodStateHolder> ms = new ArrayList<MethodStateHolder>();
        Messenger operator = null;
        int modifier = 0;
        for (int i = 0, len = methods.length; i < len; i++) {
            modifier = methods[i].getModifiers();
            if ((receiver instanceof Class && !Modifier.isStatic(modifier))
                    || (!(receiver instanceof Class) && Modifier
                            .isStatic(modifier))) {
                continue;
            }
            operator = methods[i].getAnnotation(Messenger.class);
            if (operator == null || operator.value() == null
                    || operator.value().length == 0) {
                continue;
            }
            if (verifyMethodFail(methods[i])) {
                StringBuilder error = new StringBuilder();
                error.append(methods[i].getDeclaringClass().getCanonicalName());
                error.append(".");
                error.append(methods[i].getName());
                error.append(" 不符合格式！");
                System.err.println(error);
                continue;
            }
            MethodStateHolder holder = new MethodStateHolder();
            String[] ops = operator.value();
            holder.operations = ops;
            holder.method = methods[i];
            holder.receiver = receiver;
            holder.strategy = operator.strategy();
            ms.add(holder);
            List<MethodStateHolder> oplist = null;
            for (int j = 0, size = ops.length; j < size; j++) {
                synchronized (operatorTable) {
                    if (operatorTable.containsKey(ops[j])) {
                        oplist = operatorTable.get(ops[j]);
                    } else {
                        oplist = new ArrayList<MethodStateHolder>();
                    }
                    oplist.add(holder);
                    operatorTable.put(ops[j].intern(), oplist);
                }
            }
        }
        if (ms.size() > 0) {
            synchronized (receiverTable) {
                receiverTable.put(receiver, ms);
            }
        }
    }

    private boolean verifyMethodFail(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes == null || parameterTypes.length != 1
                || parameterTypes[0] != Message.class;
    }

    @Override
    public final void unregister(Object receiver) {
        if (receiver == null) {
            System.err.println("IFK.unregister() can't accept null param");
            return;
        }

        List<MethodStateHolder> methodlist = null;
        synchronized (receiverTable) {
            methodlist = receiverTable.remove(receiver);
        }

        List<MethodStateHolder> oplist = null;
        for (MethodStateHolder holder : methodlist) {
            synchronized (operatorTable) {
                for (String op : holder.operations) {
                    oplist = operatorTable.get(op);
                    oplist.remove(holder);
                    if (oplist.isEmpty()) {
                        operatorTable.remove(op);
                    }
                }
            }
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
     * @param args
     *            表示 Message 的参数
     */
    @Override
    protected void invokeExecutor(Object receiver, String message,
            ThreadStrategy strategy, Object callbackRcv, String callbackMsg,
            ThreadStrategy callbackStrategy, Object... args) {
        assert message != null && message.length() > 0;
        List<MethodStateHolder> temp = null;
        synchronized (operatorTable) {
            if (!operatorTable.containsKey(message)) {
                return;
            }
            temp = operatorTable.get(message);
            if (temp == null || temp.size() == 0) {
                return;
            }
        }
        fillterReceivers(receiver, message, strategy, callbackRcv, callbackMsg,
                callbackStrategy, temp, args);
    }

    private void fillterReceivers(Object receiver, String message,
            ThreadStrategy strategy, Object callbackRcv, String callbackMsg,
            ThreadStrategy callbackStrategy, List<MethodStateHolder> holders,
            Object... args) {
        synchronized (holders) {
            if (receiver == null) {
                for (MethodStateHolder holder : holders) {
                    invokeInternal(message, callbackRcv, strategy, callbackMsg,
                            callbackStrategy, holder, args);
                }
            } else {
                if (receiver instanceof Class) {
                    for (MethodStateHolder holder : holders) {
                        // Class 实例是单例的，所以可以直接使用 == 符号
                        if (holder.receiver != receiver) {
                            continue;
                        }

                        invokeInternal(message, callbackRcv, strategy,
                                callbackMsg, callbackStrategy, holder, args);
                    }
                } else {
                    for (MethodStateHolder holder : holders) {
                        if (!holder.receiver.equals(receiver)) {
                            continue;
                        }

                        invokeInternal(message, callbackRcv, strategy,
                                callbackMsg, callbackStrategy, holder, args);
                    }
                }
            }
        }
    }

    private void invokeInternal(final String message, final Object callbackRcv,
            ThreadStrategy strategy, final String callbackMsg,
            final ThreadStrategy callbackStrategy,
            final MethodStateHolder holder, final Object... args) {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    holder.method.setAccessible(true);
                    final Message msg = new Message(message, args);
                    final Object returnVal = holder.method.invoke(
                            holder.receiver instanceof Class ? null
                                    : holder.receiver, msg);
                    invokeCallback(callbackRcv, callbackMsg, returnVal,
                            callbackStrategy);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // 传入的参数优先级比较大
        boolean sync = false;
        if (strategy == ThreadStrategy.DEFAULT) {
            sync = holder.strategy == ThreadStrategy.DEFAULT
                    || holder.strategy == ThreadStrategy.SYNCHRONOUS;
        } else {
            sync = strategy == ThreadStrategy.SYNCHRONOUS;
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

    // 处理回调逻辑
    private void invokeCallback(Object callbackRcv, String callbackMsg,
            Object returnVal, ThreadStrategy callbackStrategy) {
        if (callbackMsg == null || callbackMsg.length() == 0) {
            return;
        }

        // 回调部分为空，不会循环处理
        if (returnVal == null || returnVal instanceof Void) {
            invokeExecutor(callbackRcv, callbackMsg, callbackStrategy, null,
                    null, ThreadStrategy.DEFAULT);
        } else {
            if (returnVal instanceof Type[]) {
                // Type 只用于参数转换，不作为参数传递
                final Type[] src = (Type[]) returnVal;
                int len = src.length;
                if (len == 0) {
                    invokeExecutor(callbackRcv, callbackMsg, callbackStrategy,
                            null, null, ThreadStrategy.DEFAULT);
                }
                final Object[] dest = new Object[src.length];
                for (int i = 0; i < len; i++) {
                    dest[i] = src[i].toObject();
                }
                invokeExecutor(callbackRcv, callbackMsg, callbackStrategy,
                        null, null, ThreadStrategy.DEFAULT, dest);
            } else if (returnVal instanceof Object[]) {
                final Object[] dest = (Object[]) returnVal;
                for (int i = 0, len = dest.length; i < len; i++) {
                    if (dest[i] instanceof Type) {
                        dest[i] = ((Type) dest[i]).toObject();
                    }
                }
                invokeExecutor(callbackRcv, callbackMsg, callbackStrategy,
                        null, null, ThreadStrategy.DEFAULT, dest);
            } else {
                invokeExecutor(callbackRcv, callbackMsg, callbackStrategy,
                        null, null, ThreadStrategy.DEFAULT, returnVal);
            }
        }
    }

    private static class MethodStateHolder {
        /**
         * receiver 为 null 表示 static 方法，否则是 instance 方法
         */
        Object receiver;
        Method method;
        String[] operations;
        ThreadStrategy strategy;

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

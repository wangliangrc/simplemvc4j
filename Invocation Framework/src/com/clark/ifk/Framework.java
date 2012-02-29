package com.clark.ifk;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class Framework {
    private Map<Object, List<MethodStateHolder>> receiverTable = new HashMap<Object, List<MethodStateHolder>>();
    private Map<String, List<MethodStateHolder>> operatorTable = new HashMap<String, List<MethodStateHolder>>();
    private Executor executor;

    public final void register(Object receiver) {
        if (receiver == null) {
            return;
        }

        if (receiverTable.containsKey(receiver)) {
            return;
        }

        List<MethodStateHolder> ms = new ArrayList<MethodStateHolder>();
        Messenger operator = null;
        if (receiver instanceof Class) {
            Class<?> clazz = (Class<?>) receiver;
            Method[] methods = clazz.getDeclaredMethods();
            if (methods == null) {
                return;
            }

            int modifier = 0;
            for (int i = 0, len = methods.length; i < len; i++) {
                modifier = methods[i].getModifiers();
                if (!Modifier.isStatic(modifier)) {
                    continue;
                }
                operator = methods[i].getAnnotation(Messenger.class);
                if (operator == null || operator.value() == null
                        || operator.value().length == 0) {
                    continue;
                }
                verifyMethod(methods[i]);
                MethodStateHolder holder = new MethodStateHolder();
                String[] ops = operator.value();
                holder.operations = ops;
                holder.method = methods[i];
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
                        operatorTable.put(ops[j], oplist);
                    }
                }
            }
        } else {
            Class<?> clazz = receiver.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            if (methods == null) {
                return;
            }

            int modifier = 0;
            for (int i = 0, len = methods.length; i < len; i++) {
                modifier = methods[i].getModifiers();
                if (Modifier.isStatic(modifier)) {
                    continue;
                }
                operator = methods[i].getAnnotation(Messenger.class);
                if (operator == null || operator.value() == null
                        || operator.value().length == 0) {
                    continue;
                }
                verifyMethod(methods[i]);
                MethodStateHolder holder = new MethodStateHolder();
                String[] ops = operator.value();
                holder.operations = ops;
                holder.method = methods[i];
                holder.receiver = receiver;
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
                        operatorTable.put(ops[j], oplist);
                    }
                }
            }
        }
        if (ms.size() > 0) {
            synchronized (receiverTable) {
                receiverTable.put(receiver, ms);
            }
        }
    }

    private void verifyMethod(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length != 1
                || parameterTypes[0] != Message.class) {
            throw new RuntimeException(
                    "Callback function can only have one Message param.");
        }
    }

    public final void unregister(Object receiver) {
        if (receiver == null) {
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

    public final void setAsyncExecutor(Executor executor) {
        this.executor = executor;
    }

    public final void callAsync(Object receiver, String message, Object... args) {
        callAsync(receiver, message, (Object) null, (String) null, args);
    }

    public final void callAsync(String message, Object... args) {
        callAsync((Object) null, message, (Object) null, (String) null, args);
    }

    public final void callAsync(final Object receiver,
            final String message, final Object callbackRcv,
            final String callbackMsg, final Object... args) {
        if (executor == null) {
            callInternal(receiver, message, callbackRcv, callbackMsg, args);
        } else {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    callInternal(receiver, message, callbackRcv, callbackMsg,
                            args);
                }
            });
        }
    }

    private synchronized void callInternal(Object receiver, String message,
            Object callbackRcv, String callbackMsg, Object... args) {
        assert message != null && message.length() > 0;
        if (!operatorTable.containsKey(message)) {
            return;
        }

        List<MethodStateHolder> holders = operatorTable.get(message);
        Object returnVal = null;
        try {
            if (receiver == null) {
                for (MethodStateHolder holder : holders) {
                    holder.method.setAccessible(true);
                    returnVal = holder.method.invoke(holder.receiver, args);
                    if (callbackMsg != null && callbackMsg.length() > 0) {
                        if (returnVal == null || returnVal instanceof Void) {
                            callAsync(callbackRcv, callbackMsg, (Object) null,
                                    (String) null);
                        } else {
                            callAsync(callbackRcv, callbackMsg, (Object) null,
                                    (String) null, returnVal);
                        }
                    }
                }
            } else {
                for (MethodStateHolder holder : holders) {
                    if (!holder.receiver.equals(receiver)) {
                        continue;
                    }

                    holder.method.setAccessible(true);
                    returnVal = holder.method.invoke(holder.receiver, args);
                    if (callbackMsg != null && callbackMsg.length() > 0) {
                        if (returnVal == null || returnVal instanceof Void) {
                            callAsync(callbackRcv, callbackMsg, (Object) null,
                                    (String) null);
                        } else {
                            callAsync(callbackRcv, callbackMsg, (Object) null,
                                    (String) null, returnVal);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void callAsync(Object receiver, String message,
            String callbackMsg, Object... args) {
        callAsync(receiver, message, (Object) null, callbackMsg, args);
    }

    public final void callAsync(String message, Object callbackRcv,
            String callbackMsg, Object... args) {
        callAsync((Object) null, message, callbackRcv, callbackMsg, args);
    }

    public final void callAsync(String message, String callbackMsg,
            Object... args) {
        callAsync((Object) null, message, (Object) null, callbackMsg, args);
    }

    private static class MethodStateHolder {
        /**
         * receiver 为 null 表示 static 方法，否则是 instance 方法
         */
        Object receiver;
        Method method;
        String[] operations;

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

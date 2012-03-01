package com.clark.ifk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class IFKJavaImpl extends IFK {
    private Map<Object, List<MethodStateHolder>> receiverTable = new HashMap<Object, List<MethodStateHolder>>();
    private Map<String, List<MethodStateHolder>> operatorTable = new HashMap<String, List<MethodStateHolder>>();

    IFKJavaImpl() {
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

    // public class Invocation {
    // private Object receiver;
    // private String rcvMsg;
    // private boolean rcvRunOnUi = true;
    // private Object callbackRcv;
    // private String callbackMsg;
    // private boolean callbackRunOnUi = true;
    // private Object[] args;
    //
    // Invocation(String rcvMsg) {
    // assert rcvMsg != null && rcvMsg.length() > 0;
    // this.rcvMsg = rcvMsg;
    // }
    //
    // public Invocation receiver(Object receiver) {
    // this.receiver = receiver;
    // return this;
    // }
    //
    // public Invocation runOnUi(boolean runOnUi) {
    // rcvRunOnUi = runOnUi;
    // return this;
    // }
    //
    // public Invocation arguments(Object... args) {
    // if (args == null) {
    // this.args = new Object[] {};
    // } else {
    // this.args = args;
    // }
    // return this;
    // }
    //
    // public Invocation callbackReceiver(Object receiver) {
    // callbackRcv = receiver;
    // return this;
    // }
    //
    // public Invocation callbackMessage(String name) {
    // callbackMsg = name;
    // return this;
    // }
    //
    // public Invocation callbackRunOnUi(boolean runOnUi) {
    // callbackRunOnUi = runOnUi;
    // return this;
    // }
    //
    // public void invoke() {
    // IFKJavaImpl.this.invokeRunnable(receiver, rcvMsg, rcvRunOnUi,
    // callbackRcv, callbackMsg, callbackRunOnUi, args);
    // }
    // }

    /**
     * 
     * @param receiver
     *            如果是 Class 的实例则调用 static 方法；如果是 instance 则调用 instance 方法；如果为
     *            null 则调用 static 和 instance 方法。
     * @param message
     *            表示 Message 的名字
     * @param runOnUi
     * @param callbackRcv
     *            表示回调 Message 的 receiver
     * @param callbackMsg
     *            表示回调 Message 的名字
     * @param callbackRunOnUi
     * @param args
     *            表示 Message 的参数
     */
    @Override
    protected void invokeRunnable(final Object receiver, final String message,
            boolean runOnUi, final Object callbackRcv,
            final String callbackMsg, final boolean callbackRunOnUi,
            final Object... args) {
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

        final List<MethodStateHolder> holders = temp;
        synchronized (holders) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    invokeInternal(receiver, message, callbackRcv, callbackMsg,
                            callbackRunOnUi, holders, args);
                }
            };

            if (runOnUi) {
                if (uiExecutor == null) {
                    runnable.run();
                } else {
                    uiExecutor.execute(runnable);
                }
            } else {
                if (poolExecutor == null) {
                    runnable.run();
                } else {
                    poolExecutor.execute(runnable);
                }
            }
        }
    }

    // 处理直接调用逻辑
    private void invokeInternal(Object receiver, String message,
            Object callbackRcv, String callbackMsg, boolean callbackRunOnUi,
            List<MethodStateHolder> holders, Object... args) {
        try {
            if (receiver == null) {
                for (MethodStateHolder holder : holders) {
                    invokeInternal0(message, callbackRcv, callbackMsg,
                            callbackRunOnUi, holder, args);
                }
            } else {
                if (receiver instanceof Class) {
                    for (MethodStateHolder holder : holders) {
                        // Class 实例是单例的，所以可以直接使用 == 符号
                        if (holder.receiver != receiver) {
                            continue;
                        }

                        invokeInternal0(message, callbackRcv, callbackMsg,
                                callbackRunOnUi, holder, args);
                    }
                } else {
                    for (MethodStateHolder holder : holders) {
                        if (!holder.receiver.equals(receiver)) {
                            continue;
                        }

                        invokeInternal0(message, callbackRcv, callbackMsg,
                                callbackRunOnUi, holder, args);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeInternal0(String message, Object callbackRcv,
            String callbackMsg, boolean callbackRunOnUi,
            MethodStateHolder holder, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        holder.method.setAccessible(true);
        final Message msg = new Message(message, args);
        final Object returnVal = holder.method.invoke(
                holder.receiver instanceof Class ? null : holder.receiver, msg);
        callbackInternal(callbackRcv, callbackMsg, returnVal, callbackRunOnUi);
    }

    // 处理回调逻辑
    private void callbackInternal(Object callbackRcv, String callbackMsg,
            Object returnVal, boolean callbackRunOnUi) {
        if (callbackMsg == null || callbackMsg.length() == 0) {
            return;
        }

        // 回调部分为空，不会循环处理
        if (returnVal == null || returnVal instanceof Void) {
            invokeRunnable(callbackRcv, callbackMsg, callbackRunOnUi, null,
                    null, false);
        } else {
            if (returnVal instanceof Type[]) {
                // Type 只用于参数转换，不作为参数传递
                final Type[] src = (Type[]) returnVal;
                int len = src.length;
                if (len == 0) {
                    invokeRunnable(callbackRcv, callbackMsg, callbackRunOnUi,
                            null, null, false);
                }
                final Object[] dest = new Object[src.length];
                for (int i = 0; i < len; i++) {
                    dest[i] = src[i].toObject();
                }
                invokeRunnable(callbackRcv, callbackMsg, callbackRunOnUi, null,
                        null, false, dest);
            } else if (returnVal instanceof Object[]) {
                final Object[] dest = (Object[]) returnVal;
                for (int i = 0, len = dest.length; i < len; i++) {
                    if (dest[i] instanceof Type) {
                        dest[i] = ((Type) dest[i]).toObject();
                    }
                }
                invokeRunnable(callbackRcv, callbackMsg, callbackRunOnUi, null,
                        null, false, dest);
            } else {
                invokeRunnable(callbackRcv, callbackMsg, callbackRunOnUi, null,
                        null, false, returnVal);
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

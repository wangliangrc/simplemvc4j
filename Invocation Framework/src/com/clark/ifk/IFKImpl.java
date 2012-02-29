package com.clark.ifk;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

class IFKImpl implements IFK {
    private Map<Object, List<MethodStateHolder>> receiverTable = new HashMap<Object, List<MethodStateHolder>>();
    private Map<String, List<MethodStateHolder>> operatorTable = new HashMap<String, List<MethodStateHolder>>();
    private Executor executor;

    IFKImpl(Executor executor) {
        super();
        this.executor = executor;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.clark.ifk.IFK#register(java.lang.Object)
     */
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

        List<MethodStateHolder> ms = new ArrayList<MethodStateHolder>();
        Messenger operator = null;
        Class<?> clazz = null;
        // if (receiver instanceof Class) {
        if (receiver instanceof Class) {
            clazz = (Class<?>) receiver;
        } else {
            clazz = receiver.getClass();
        }
        Method[] methods = clazz.getDeclaredMethods();
        if (methods == null) {
            return;
        }

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
                    operatorTable.put(ops[j], oplist);
                }
            }
        }
        // } else {
        // Class<?> clazz = receiver.getClass();
        // Method[] methods = clazz.getDeclaredMethods();
        // if (methods == null) {
        // return;
        // }
        //
        // int modifier = 0;
        // for (int i = 0, len = methods.length; i < len; i++) {
        // modifier = methods[i].getModifiers();
        // if (Modifier.isStatic(modifier)) {
        // continue;
        // }
        // operator = methods[i].getAnnotation(Messenger.class);
        // if (operator == null || operator.value() == null
        // || operator.value().length == 0) {
        // continue;
        // }
        // if (verifyMethodFail(methods[i])) {
        // StringBuilder error = new StringBuilder();
        // error.append(methods[i].getDeclaringClass()
        // .getCanonicalName());
        // error.append(".");
        // error.append(methods[i].getName());
        // error.append(" 不符合格式！");
        // System.err.println(error);
        // continue;
        // }
        // MethodStateHolder holder = new MethodStateHolder();
        // String[] ops = operator.value();
        // holder.operations = ops;
        // holder.method = methods[i];
        // holder.receiver = receiver;
        // ms.add(holder);
        // List<MethodStateHolder> oplist = null;
        // for (int j = 0, size = ops.length; j < size; j++) {
        // synchronized (operatorTable) {
        // if (operatorTable.containsKey(ops[j])) {
        // oplist = operatorTable.get(ops[j]);
        // } else {
        // oplist = new ArrayList<MethodStateHolder>();
        // }
        // oplist.add(holder);
        // operatorTable.put(ops[j], oplist);
        // }
        // }
        // }
        // }
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

    /**
     * (non-Javadoc)
     * 
     * @see com.clark.ifk.IFK#unregister(java.lang.Object)
     */
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
     * (non-Javadoc)
     * 
     * @see com.clark.ifk.IFK#rm(java.lang.Object, java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public final void rm(Object receiver, String message, Object... args) {
        rmrm(receiver, message, (Object) null, (String) null, args);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.clark.ifk.IFK#m(java.lang.String, java.lang.Object)
     */
    @Override
    public final void m(String message, Object... args) {
        rmrm((Object) null, message, (Object) null, (String) null, args);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.clark.ifk.IFK#rmrm(java.lang.Object, java.lang.String,
     *      java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public final void rmrm(final Object receiver, final String msgname,
            final Object callbackRcv, final String callbackMsg,
            final Object... args) {
        if (executor == null) {
            callInternal(receiver, msgname, callbackRcv, callbackMsg, args);
        } else {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    callInternal(receiver, msgname, callbackRcv, callbackMsg,
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
        Message msg = null;
        try {
            if (receiver == null) {
                for (MethodStateHolder holder : holders) {
                    holder.method.setAccessible(true);
                    msg = new Message(message, args);
                    returnVal = holder.method.invoke(holder.receiver, msg);
                    if (callbackMsg != null && callbackMsg.length() > 0) {
                        if (returnVal == null || returnVal instanceof Void) {
                            rmrm(callbackRcv, callbackMsg, (Object) null,
                                    (String) null);
                        } else {
                            rmrm(callbackRcv, callbackMsg, (Object) null,
                                    (String) null, returnVal);
                        }
                    }
                }
            } else {
                if (receiver instanceof Class) {
                    for (MethodStateHolder holder : holders) {
                        // Class 实例是单例的，所以可以直接使用 == 符号
                        if (holder.receiver != receiver) {
                            continue;
                        }

                        holder.method.setAccessible(true);
                        msg = new Message(message, args);
                        // 调用 static 方法第一个参数为 null
                        returnVal = holder.method.invoke(null, msg);
                        if (callbackMsg != null && callbackMsg.length() > 0) {
                            if (returnVal == null || returnVal instanceof Void) {
                                rmrm(callbackRcv, callbackMsg, (Object) null,
                                        (String) null);
                            } else {
                                rmrm(callbackRcv, callbackMsg, (Object) null,
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
                        msg = new Message(message, args);
                        returnVal = holder.method.invoke(holder.receiver, msg);
                        if (callbackMsg != null && callbackMsg.length() > 0) {
                            if (returnVal == null || returnVal instanceof Void) {
                                rmrm(callbackRcv, callbackMsg, (Object) null,
                                        (String) null);
                            } else {
                                rmrm(callbackRcv, callbackMsg, (Object) null,
                                        (String) null, returnVal);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clark.ifk.IFK#rmm(java.lang.Object, java.lang.String,
     * java.lang.String, java.lang.Object)
     */
    @Override
    public final void rmm(Object receiver, String message, String callbackMsg,
            Object... args) {
        rmrm(receiver, message, (Object) null, callbackMsg, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clark.ifk.IFK#mrm(java.lang.String, java.lang.Object,
     * java.lang.String, java.lang.Object)
     */
    @Override
    public final void mrm(String message, Object callbackRcv,
            String callbackMsg, Object... args) {
        rmrm((Object) null, message, callbackRcv, callbackMsg, args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.clark.ifk.IFK#mm(java.lang.String, java.lang.String,
     * java.lang.Object)
     */
    @Override
    public final void mm(String message, String callbackMsg, Object... args) {
        rmrm((Object) null, message, (Object) null, callbackMsg, args);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((executor == null) ? 0 : executor.hashCode());
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
        IFKImpl other = (IFKImpl) obj;
        if (executor == null) {
            if (other.executor != null)
                return false;
        } else if (!executor.equals(other.executor))
            return false;
        return true;
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

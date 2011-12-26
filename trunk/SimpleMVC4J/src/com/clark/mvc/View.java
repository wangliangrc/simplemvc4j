package com.clark.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class View {

    private HashMap<Object, Set<SignalReceiverHolder>> views = new HashMap<Object, Set<SignalReceiverHolder>>();
    private Facade facade;

    View(Facade facade) {
        this.facade = facade;
    }

    /**
     * 注册一个包含 {@link Mediator} 注解方法的类的实例到 View 注册表中。
     * <p>
     * 所谓 Mediator 对象，实际上就是具有 {@link Mediator} 注解修饰的方法的类实例而已。 注册时是基于实例的区别于
     * {@link Controller} 是基于 {@link Class} 对象的。 也就是说如果一个包含 {@link Mediator}
     * 注解方法的类的两个实例同时注册，那么触发回调的时候两个实例都会接到通知：
     * 
     * <pre>
     * public class MediatorTest {
     * 
     *     public static void main(String[] args) {
     *         SomeMediator mediator1 = new SomeMediator();
     *         SomeMediator mediator2 = new SomeMediator();
     * 
     *         // 注册 Mediator
     *         mvc().view().register(mediator1);
     *         mvc().view().register(mediator2);
     * 
     *         // 发送 &quot;doSomdthing&quot; 通知
     *         mvc().sendSignal(&quot;doSomdthing&quot;);
     * 
     *         // 移除 Mediator
     *         mvc().view().remove(mediator1);
     *         mvc().view().remove(mediator2);
     *     }
     * 
     *     public static class SomeMediator {
     * 
     *         &#064;Mediator(&quot;doSomdthing&quot;)
     *         void doSomdthing(Signal signal) {
     *             System.out.println(&quot;SomeView.doSomdthing()\n&quot; + toString());
     *             System.out.println(signal);
     *         }
     * 
     *     }
     * }
     * </pre>
     * 
     * 会打印两次。
     * <p>
     * 注意：注册一个实例的时候，实际上也会关联其父类型中可以访问的方法（protected、包）等。
     * 
     * @param object
     *            一个包含 {@link Mediator} 注解方法的类的实例。不能为 null。
     */
    synchronized void register(final Object object) {
        if (object == null) {
            throw new NullPointerException("object can't be null");
        }

        if (views.containsKey(object)) {
            System.err.println("Already registered mediator: " + object);
            return;
        }

        Class<?> clazz = object.getClass();
        String errorString = "There is no mediator method in ["
                + clazz.getCanonicalName() + "] or it's super class";

        boolean found = findMediatorMethods(object, clazz);

        if (found) {
            System.out.println(facade + " register mediator: [" + object + "]");
        } else {
            System.err.println(errorString);
        }
    }

    private boolean findMediatorMethods(final Object object, Class<?> clazz) {
        // final boolean isInSamePackage = object.getClass().getPackage()
        // .equals(clazz.getPackage());
        // final boolean isSelf = object.getClass() == clazz;

        Mediator annotation = null;
        String[] names;
        Set<SignalReceiverHolder> functions = null;
        SignalReceiver function;
        boolean found = false;

        final Method[] declaredMethods = clazz.getDeclaredMethods();
        // int modifiers = 0;
        if (declaredMethods != null && declaredMethods.length > 0) {
            for (final Method method : declaredMethods) {

                // modifiers = method.getModifiers();
                // // Mediator 只能作用于实例方法
                // if (Modifier.isStatic(modifiers)) {
                // continue;
                // }
                //
                // if (isInSamePackage) {
                // // 相同包中的父类
                // if (!isSelf) {
                // if (Modifier.isPrivate(modifiers)) {
                // continue;
                // }
                // }
                // } else {
                // // 不同包中的父类
                // if (!Modifier.isPublic(modifiers)
                // && !Modifier.isProtected(modifiers)) {
                // continue;
                // }
                // }

                annotation = method.getAnnotation(Mediator.class);
                if (annotation != null) {
                    names = annotation.value();
                    if (names != null && names.length > 0) {

                        for (String name : names) {
                            function = new SignalReceiver() {

                                @Override
                                public void onReceive(Signal signal) {
                                    try {
                                        // 可以调用 private、protected 方法等
                                        method.setAccessible(true);
                                        method.invoke(object, signal);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            };
                            facade.registerFunction(name, function);
                            functions = getObservers(object);
                            functions.add(new SignalReceiverHolder(name,
                                    function));
                            found = true;
                        }

                    }
                }

            }
        }

        // final Class<?> superclass = clazz.getSuperclass();
        // // 查找父类中的 mediator 实例方法
        // if (superclass != null) {
        // found = found | findMediatorMethods(object, superclass);
        // }

        return found;
    }

    private Set<SignalReceiverHolder> getObservers(Object object) {
        Set<SignalReceiverHolder> set = views.get(object);
        if (set == null) {
            set = new HashSet<SignalReceiverHolder>();
            views.put(object, set);
        }
        return set;
    }

    /**
     * 从 View 注册表中移除一个包含 {@link Mediator} 注解方法的类的实例。
     * 
     * @param object
     *            一个已注册过的包含 {@link Mediator} 注解方法的类的实例。不能为 null。
     */
    synchronized void remove(Object object) {
        if (object == null) {
            throw new NullPointerException("object can't be null");
        }

        if (views.containsKey(object)) {
            Set<SignalReceiverHolder> set = views.get(object);
            if (set != null && set.size() > 0) {
                for (SignalReceiverHolder holder : set) {
                    facade.removeFunction(holder.name, holder.function);
                }
            }
            views.remove(object);
            System.out.println(facade + " remove mediator: [" + object + "]");
        } else {
            String errorString = "There is no mediator which is already registered: ["
                    + object + "]";
            System.err.println(errorString);
        }
    }

    synchronized boolean contains(Object key) {
        return views.containsKey(key);
    }
}

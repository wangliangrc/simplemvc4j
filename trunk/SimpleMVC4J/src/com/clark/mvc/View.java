package com.clark.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class View {

    private HashMap<Object, Set<FunctionHolder>> views = new HashMap<Object, Set<FunctionHolder>>();
    private Facade facade;

    View(Facade facade) {
        this.facade = facade;
    }

    /**
     * 注册一个包含 {@link Mediator} 注解方法的类的实例到 View 注册表中。
     * <p>
     * 所谓 Mediator 对象，实际上就是具有 {@link Mediator} 注解修饰的 public 方法的类实例而已。
     * 注册时是基于实例的区别于 {@link Controller} 是基于 {@link Class} 对象的。 也就是说如果一个包含
     * {@link Mediator} 注解方法的类的两个实例同时注册，那么触发回调的时候两个实例都会接到通知：
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
     *         mvc().sendNotification(&quot;doSomdthing&quot;);
     * 
     *         // 移除 Mediator
     *         mvc().view().remove(mediator1);
     *         mvc().view().remove(mediator2);
     *     }
     * 
     *     public static class SomeMediator {
     * 
     *         &#064;Mediator(&quot;doSomdthing&quot;)
     *         public void doSomdthing(Notification notification) {
     *             System.out.println(&quot;SomeView.doSomdthing()\n&quot; + toString());
     *             System.out.println(notification);
     *         }
     * 
     *     }
     * }
     * </pre>
     * 
     * 会打印两次。
     * <p>
     * 
     * @param object
     *            一个包含 {@link Mediator} 注解方法的类的实例。不能为 null。
     */
    @SuppressWarnings("rawtypes")
    public synchronized void register(final Object object) {
        if (object == null) {
            throw new NullPointerException("object can't be null");
        }

        if (views.containsKey(object)) {
            System.err.println("Already registered mediator: " + object);
            return;
        }

        Class clazz = object.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        String errorString = "There is no mediator method in ["
                + clazz.getCanonicalName() + "]";
        if (declaredMethods != null) {

            Mediator annotation = null;
            String[] names;
            Set<FunctionHolder> functions = null;
            Function function;
            boolean found = false;

            for (final Method method : declaredMethods) {

                annotation = method.getAnnotation(Mediator.class);
                if (annotation != null) {
                    names = annotation.value();
                    if (names != null && names.length > 0) {

                        for (String name : names) {
                            function = new Function() {

                                @Override
                                public void onNotification(
                                        Notification notification) {
                                    try {
                                        method.invoke(object, notification);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            };
                            facade.register(name, function);
                            functions = getObservers(object);
                            functions.add(new FunctionHolder(name, function));
                            found = true;
                        }

                    }
                }

            }

            if (found) {
                System.out.println("register mediator: [" + object + "]");
            } else {
                System.err.println(errorString);
            }
        } else {
            System.err.println(errorString);
        }
    }

    private Set<FunctionHolder> getObservers(Object object) {
        Set<FunctionHolder> set = views.get(object);
        if (set == null) {
            set = new HashSet<FunctionHolder>();
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
    public synchronized void remove(Object object) {
        if (object == null) {
            throw new NullPointerException("object can't be null");
        }

        if (views.containsKey(object)) {
            Set<FunctionHolder> set = views.get(object);
            if (set != null && set.size() > 0) {
                for (FunctionHolder holder : set) {
                    facade.remove(holder.name, holder.function);
                }
            }
            views.remove(object);
            System.out.println("remove mediator: [" + object + "]");
        } else {
            String errorString = "There is no mediator which is already registered: ["
                    + object + "]";
            System.err.println(errorString);
        }
    }

}

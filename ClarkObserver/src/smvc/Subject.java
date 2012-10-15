package smvc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 该类表示观察者模式中的被观察对象。 可以表示一个属性或者一组属性的变化。 可以发出一种或者多种消息（根据 id 的不同做以区别）。
 * 
 * @author clark
 * 
 */
public abstract class Subject {
    private Map<Observer, String[]> mObserversMap = new WeakHashMap<Observer, String[]>();

    /**
     * 注册观察者。 <br />
     * 注意：内部使用 {@link WeakHashMap} 保存观察者对象，所以 即使我们忘记注销他们，理论上也不会造成内存负担。
     * 
     * @param observer
     */
    public final void attach(Observer observer) {
        try {
            final Class<?> clazz = observer.getClass();
            final Method method = clazz.getDeclaredMethod("onUpdate",
                    Subject.class, Event.class);
            final EventMetaDatas set = method
                    .getAnnotation(EventMetaDatas.class);
            String[] idSet = null;
            if (set == null) {
                idSet = new String[] { Event.DEFAULT_ID };
            } else {
                idSet = set.value();
            }
            if (idSet == null) {
                throw new IllegalArgumentException(
                        "no value in @EventSet before " + observer
                                + "'s onUpdate method.");
            }
            synchronized (mObserversMap) {
                mObserversMap.put(observer, idSet);
            }
        } catch (NoSuchMethodException e) {
            // ignore
        } catch (SecurityException e) {
            // ignore
        }
    }

    /**
     * 注销观察者对象。
     * 
     * @param observer
     */
    public final void detach(Observer observer) {
        synchronized (mObserversMap) {
            mObserversMap.remove(observer);
        }
    }

    /**
     * 发送默认 id 的消息。
     */
    public final void update() {
        update(null);
    }

    /**
     * 发送默认 id 的消息。
     * 
     * @param message
     */
    public final void update(Object message) {
        update(Event.DEFAULT_ID, message);
    }

    /**
     * 发送指定 id 的消息。
     * 
     * @param id
     * @param message
     */
    public final void update(String id, Object message) {
        final Event event = new Event(id, message);
        final String targetId = event.getId();
        boolean needBreak = false;
        final Map<Observer, String[]> tmp;
        synchronized (mObserversMap) {
            tmp = new HashMap<Observer, String[]>(mObserversMap);
        }
        for (Map.Entry<Observer, String[]> entry : tmp.entrySet()) {
            if (Arrays.asList(entry.getValue()).contains(targetId)) {
                needBreak = entry.getKey().onUpdate(this, event);
                if (needBreak) {
                    break;
                }
            }
        }
    }
}

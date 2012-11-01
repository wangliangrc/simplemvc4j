package smvc;

/**
 * 观察者。
 * 
 * @author clark
 * 
 */
public interface Observer {

    /**
     * 当 {@link Subject} 对象发生变化的时候，会发出消息；如果我们关注的了这个消息就会触发该回调方法。<br />
     * 注意：如果我们对多种消息都感兴趣，可以有两种实现方法：
     * <p>
     * <ul>
     * <li>在多个 {@link Subject} 对象中注册</li>
     * <li>在一个 {@link Subject} 对象中注册，但是使用{@link EventMetaDatas} 注册多个关注 id</li>
     * </ul>
     * 
     * @param subject
     * @param event
     * @return
     */
    public boolean onUpdate(Subject subject, Event event);

}

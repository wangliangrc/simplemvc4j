package com.clark.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个运行时注解，主要用于标注属于 {@link Controller} 回调的方法。<br>
 * 修饰方法形式如下：
 * 
 * <pre>
 * &#064;Command(&quot;doXXX&quot;)
 * public static void someMethod(Signal signal) {
 *     ...
 * }
 * </pre>
 * 
 * 注意：
 * <ol>
 * <li>public 不是必须的，理论上可以接受任意修饰类型。</li>
 * <li>static 是必须的，需要保证实现 Command 机制的类不依赖于任何实例状态。</li>
 * <li>返回值并没有做出限制，但是有返回值的话也不会被利用，一般为 void。</li>
 * <li>方法名称没有限定。</li>
 * <li>参数列表要求只能接受一个 {@link Signal} 类型参数，参数个数类型不对会抛出非受检异常。</li>
 * </ol>
 * 
 * @author clark
 * @since 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String value() default "";
}

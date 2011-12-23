package com.clark.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个运行时注解，主要用于标注属于 {@link View} 回调的方法。<br>
 * 修饰方法形式如下：
 * 
 * <pre>
 * &#064;Mediator(&quot;doXXX&quot;)
 * public void someMethod(Signal signal) {
 *     ...
 * }
 * </pre>
 * 
 * 注意：
 * <ol>
 * <li>public 是必须的，需要保证 Java 反射机制可以找到该方法。</li>
 * <li>返回值并没有做出限制，但是有返回值的话也不会被利用，一般为 void。</li>
 * <li>方法名称没有限定。</li>
 * <li>参数列表要求只能接受一个 {@link Signal} 类型参数，参数个数类型不对会抛出非受检异常。</li>
 * </ol>
 * 
 * @author Clark
 * @since 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mediator {
    String[] value() default {};
}

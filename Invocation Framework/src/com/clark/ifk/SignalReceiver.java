package com.clark.ifk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SignalReceiver {
    String[] value() default {};

    ThreadStrategy threadStrategy() default ThreadStrategy.DEFAULT;

    int signalLevel() default IFK.DEFAULT_LEVEL;
}

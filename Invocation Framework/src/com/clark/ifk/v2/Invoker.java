package com.clark.ifk.v2;

interface Invoker {
    Invoker invoker(String id, Object receiver);

    Invoker invoke();

    Invoker arguments(Object... args);
}

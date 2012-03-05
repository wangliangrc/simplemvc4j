package com.clark.ifk;

public interface Receiver {
    String id(); // 不为空
    int level();
    Object receiver(); // 不为 null
}

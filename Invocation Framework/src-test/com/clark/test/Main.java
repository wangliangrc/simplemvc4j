package com.clark.test;

import com.clark.ifk.IFK;

public class Main {

    public static void main(String[] args) {
        IFK ifk = IFK.getInstance();
        ifk.register(StaticMethod.class);
        ifk.m("1", "我靠", 2, 1.f, 4.);
        ifk.m("2");

        StaticMethod instance = new StaticMethod();
        ifk.register(instance);
        ifk.m("3");

        ifk.unregister(StaticMethod.class);
        ifk.m("1", "我靠", 2, 1.f, 4.);
        ifk.m("2");
        ifk.unregister(instance);
        ifk.m("3");
    }

}

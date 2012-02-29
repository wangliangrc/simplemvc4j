package com.clark.test;

import com.clark.ifk.Framework;

public class Main {

    public static void main(String[] args) {
        Framework framework = Framework.getInstance();
        framework.register(StaticMethod.class);
        framework.m("1", "我靠", 2, 1.f, 4.);
        framework.m("2");
    }

}

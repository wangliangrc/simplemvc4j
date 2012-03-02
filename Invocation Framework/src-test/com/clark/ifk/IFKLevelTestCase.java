package com.clark.ifk;

import junit.framework.TestCase;

public class IFKLevelTestCase extends TestCase {
    private IFK ifk = IFK.getInstance();

    static class A {
        @SignalReceiver(value = { "测试 Level" }, signalLevel = 600)
        static void levelTest(Signal signal) {
            System.out.println("IFKLevelTestCase.A.levelTest()");
            System.out.println(signal);
        }
    }

    public void testLevelControl() throws Exception {
        ifk.register(A.class);

        System.out.println("使用默认 level 发送消息，没有任何反馈");
        ifk.invoker("测试 Level").invoke();

        System.out.println("使用指定高 level 发送消息 " + 600);
        ifk.invoker("测试 Level").level(600).invoke();
        System.out.println("使用指定高 level 发送消息 " + 700);
        ifk.invoker("测试 Level").level(700).invoke();

        Thread.sleep(2000);
        ifk.unregister(A.class);
    }
}

package com.clark.ifk;

import junit.framework.TestCase;

public class IFKInstanceMethodTestCase extends TestCase {
    private IFK ifk = IFK.getInstance();
    private A a = new A();

    static class A {

        @SignalReceiver("single")
        void single(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKInstanceMethodTestCase.A.single()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver("single")
        static void staticSingle(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKInstanceMethodTestCase.A.staticSingle()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver("multiple")
        void multiple1(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKInstanceMethodTestCase.A.multiple1()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver("multiple")
        void multiple2(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKInstanceMethodTestCase.A.multiple2()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver({ "single", "multiple" })
        void supportAll(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKInstanceMethodTestCase.A.supportAll()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver("测试参数传递和回调")
        Object callback(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKInstanceMethodTestCase.A.callback()");
            System.out.println("\t" + signal);
            return signal.args;
        }
    }

    public void testSingleInvocation() throws Exception {
        System.out.println("没有注册的话调用没有任何响应");
        ifk.register(a);
        System.out.println("只注册 a 的话不会触发 A 的 static 方法");
        ifk.invoker("single").invoke();

        ifk.register(A.class);
        System.out.println("注册 A 后 static 和 instance 都可以被触发");
        ifk.invoker("single").invoke();

        Thread.sleep(2000);
        ifk.unregister(A.class);
        ifk.unregister(a);
        System.out.println("注销之后什么也不会触发");
        ifk.invoker("single").invoke();
    }

    public void testMultipleInvocation() throws Exception {
        ifk.register(a);
        System.out.println("两个响应");
        ifk.invoker("multiple").invoke();
        Thread.sleep(2000);
        ifk.unregister(a);
    }

    public void testCallback() throws Exception {
        ifk.register(a);
        ifk.register(A.class);
        ifk.invoker("测试参数传递和回调")
                .arguments(false, (byte) 1, (short) 1, '好', 1, 1L, 1.f, 1.,
                        "^_^").callbackSignal("single").invoke();
        Thread.sleep(2000);
        ifk.unregister(a);
        ifk.unregister(A.class);
    }
}

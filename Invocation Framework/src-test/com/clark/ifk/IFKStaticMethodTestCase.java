package com.clark.ifk;

import junit.framework.TestCase;

public class IFKStaticMethodTestCase extends TestCase {
    private IFK ifk = IFK.getInstance();

    static class A {

        @SignalReceiver("single")
        static void single(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.single()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver("single")
        void instanceSingle(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.instanceSingle()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver("multiple")
        static void multiple1(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.multiple1()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver("multiple")
        static void multiple2(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.multiple2()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver({ "supportMulti1", "supportMulti2" })
        static void supportMulti(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.supportMulti()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver({ "single", "multiple", "supportMulti1", "supportMulti2" })
        static void supportAll(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.supportAll()");
            System.out.println("\t" + signal);
        }

        @SignalReceiver("测试参数传递和回调")
        static Object callback(Signal signal) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.callback()");
            System.out.println("\t" + signal);
            return signal.args;
        }
    }

    public void testSingleInvocation() throws Exception {
        System.out.println("没有注册时调用没有反应");
        ifk.invoker("single").invoke();
        System.out.println("**********************************");

        ifk.register(A.class);
        System.out.println("一个响应");
        ifk.invoker("single").invoke();

        A a = new A();
        ifk.register(a);
        System.out.println("两个响应");
        ifk.invoker("single").invoke();

        System.out.println("一个响应，static 的");
        ifk.invoker("single").receiver(A.class).invoke();

        System.out.println("一个响应，instance 的");
        ifk.invoker("single").receiver(a).invoke();

        Thread.sleep(2000);
        ifk.unregister(a);
        ifk.unregister(A.class);

        System.out.println("注销之后调用是没有反应的");
        ifk.invoker("single").invoke();
        System.out.println("**********************************");
    }

    public void testMultipleInvocation() throws Exception {
        ifk.register(A.class);
        System.out.println("两个响应，static 的");
        ifk.invoker("multiple").invoke();
        Thread.sleep(2000);
        ifk.unregister(A.class);
    }

    public void testSupportMlti() throws Exception {
        ifk.register(A.class);
        System.out.println("两种响应");
        ifk.invoker("supportMulti1").invoke();
        ifk.invoker("supportMulti2").invoke();
        Thread.sleep(2000);
        ifk.unregister(A.class);
    }

    public void testCallback() throws Exception {
        ifk.register(A.class);
        A a = new A();
        ifk.register(a);
        ifk.invoker("测试参数传递和回调")
                .arguments(false, (byte) 1, (short) 1, '好', 1, 1L, 1.f, 1.,
                        "^_^").callbackSignal("single").invoke();
        Thread.sleep(2000);
        ifk.unregister(a);
        ifk.unregister(A.class);
    }
}

package com.clark.ifk;

import junit.framework.TestCase;

public class IFKStaticMethodTestCase extends TestCase {
    private IFK ifk = IFK.getInstance();

    static class A {

        @Messenger("single")
        static void single(Message message) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.single()");
            System.out.println("\t" + message);
        }

        @Messenger("single")
        void instanceSingle(Message message) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.instanceSingle()");
            System.out.println("\t" + message);
        }

        @Messenger("multiple")
        static void multiple1(Message message) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.multiple1()");
            System.out.println("\t" + message);
        }

        @Messenger("multiple")
        static void multiple2(Message message) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.multiple2()");
            System.out.println("\t" + message);
        }

        @Messenger({ "supportMulti1", "supportMulti2" })
        static void supportMulti(Message message) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.supportMulti()");
            System.out.println("\t" + message);
        }

        @Messenger({ "single", "multiple", "supportMulti1", "supportMulti2" })
        static void supportAll(Message message) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.supportAll()");
            System.out.println("\t" + message);
        }

        @Messenger("测试参数传递和回调")
        static Object callback(Message message) {
            System.out.println("..................................");
            System.out.println("\tIFKStaticMethodTestCase.A.callback()");
            System.out.println("\t" + message);
            return message.args;
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
        ifk.unregister(A.class);
    }

    public void testSupportMlti() throws Exception {
        ifk.register(A.class);
        System.out.println("两种响应");
        ifk.invoker("supportMulti1").invoke();
        ifk.invoker("supportMulti2").invoke();
        ifk.unregister(A.class);
    }

    public void testCallback() throws Exception {
        ifk.register(A.class);
        A a = new A();
        ifk.register(a);
        ifk.invoker("测试参数传递和回调")
                .arguments(false, (byte) 1, (short) 1, '好', 1, 1L, 1.f, 1.,
                        "^_^").callbackMessage("single").invoke();
        ifk.unregister(a);
        ifk.unregister(A.class);
    }
}

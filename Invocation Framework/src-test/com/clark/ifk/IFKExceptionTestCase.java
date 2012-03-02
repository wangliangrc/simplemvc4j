package com.clark.ifk;

import junit.framework.TestCase;

public class IFKExceptionTestCase extends TestCase {
    private IFK ifk = IFK.getInstance();

    static class A {

        @SignalReceiver("测试异常")
        static void exceptionTest(Signal signal) throws Exception {
            System.out.println("IFKExceptionTestCase.A.exceptionTest()");
            System.out.println(signal);
            throw new Exception();
        }

        @SignalReceiver("异常处理")
        static void exceptionHandler(Signal signal) {
            System.out.println("IFKExceptionTestCase.A.exceptionHandler()");
            System.out.println(signal);
        }
    }

    public void testException() throws Exception {
        ifk.register(A.class);
        ifk.invoker("测试异常").callbackSignal("异常处理").invoke();
        Thread.sleep(2000);
        ifk.unregister(A.class);
    }
}

package com.clark.ifk;

import junit.framework.TestCase;

public class IFKThreadTestCase extends TestCase {
    private IFK ifk = IFK.getInstance();

    static class A {

        @Invoker("执行任务")
        static void task1(Signal signal) {
            System.out.println("IFKThreadTestCase.A.task1()");
        }

        @Invoker("执行任务")
        static void task2(Signal signal) {
            System.out.println("IFKThreadTestCase.A.task2()");
        }

        @Invoker("执行任务")
        static void task3(Signal signal) {
            System.out.println("IFKThreadTestCase.A.task3()");
        }

        @Invoker("执行任务")
        static void task4(Signal signal) {
            System.out.println("IFKThreadTestCase.A.task4()");
        }
    }

    public void testTask() throws Exception {
        ifk.register(A.class);
        ifk.invoker("执行任务").invoke();
        Thread.sleep(2000);
        ifk.unregister(A.class);
    }
}

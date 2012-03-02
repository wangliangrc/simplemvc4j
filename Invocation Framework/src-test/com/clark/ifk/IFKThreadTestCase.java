package com.clark.ifk;

import junit.framework.TestCase;

public class IFKThreadTestCase extends TestCase {
    private IFK ifk = IFK.getInstance();

    static class A {

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.ASYNCHRONOUS)
        static void task1(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.A.task1()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.ASYNCHRONOUS)
        static void task2(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.A.task2()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.ASYNCHRONOUS)
        static void task3(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.A.task3()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.ASYNCHRONOUS)
        static void task4(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.A.task4()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.ASYNCHRONOUS)
        static void task5(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.A.task5()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.ASYNCHRONOUS)
        static void task6(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.A.task6()");
                System.out.println(signal);
            }
        }
    }

    static class B {
        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.SYNCHRONOUS)
        static void task1(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.B.task1()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.SYNCHRONOUS)
        static void task2(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.B.task2()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.SYNCHRONOUS)
        static void task3(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.B.task3()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.SYNCHRONOUS)
        static void task4(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.B.task4()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.SYNCHRONOUS)
        static void task5(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.B.task5()");
                System.out.println(signal);
            }
        }

        @SignalReceiver(value = { "执行任务" }, threadStrategy = ThreadStrategy.SYNCHRONOUS)
        static void task6(Signal signal) {
            synchronized (System.out) {
                System.out.println("IFKThreadTestCase.B.task6()");
                System.out.println(signal);
            }
        }
    }

    public void testTask() throws Exception {
        ifk.register(A.class);
        ifk.invoker("执行任务").sync().invoke();
        Thread.sleep(2000);
        ifk.unregister(A.class);
        System.out.println();

        ifk.register(B.class);
        ifk.invoker("执行任务").async().invoke();
        Thread.sleep(2000);
        ifk.unregister(B.class);
        System.out.println();
    }

    public void testAsyncTask() throws Exception {
        ifk.register(A.class);
        ifk.invoker("执行任务").invoke();
        Thread.sleep(2000);
        ifk.unregister(A.class);
        System.out.println();

        ifk.register(B.class);
        ifk.invoker("执行任务").invoke();
        Thread.sleep(2000);
        ifk.unregister(B.class);
        System.out.println();
    }
}

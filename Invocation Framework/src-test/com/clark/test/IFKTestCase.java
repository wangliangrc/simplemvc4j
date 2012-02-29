package com.clark.test;

import java.util.Arrays;

import junit.framework.TestCase;

import com.clark.ifk.IFK;
import com.clark.ifk.IFKFactory;
import com.clark.ifk.Message;
import com.clark.ifk.Messenger;

public class IFKTestCase extends TestCase {

    static class TestClass {

        static boolean[] callTable = new boolean[8];

        @Messenger("staticMethod1")
        static Object staticMethod1(Message message) {
            System.out.println("IFKTestCase.TestClass.staticMethod1()");
            System.out.println(message);
            callTable[0] = true;
            return "测试成功";
        }

        @Messenger("staticMethod2")
        static void staticMethod2(Message message) {
            System.out.println("IFKTestCase.TestClass.staticMethod2()");
            System.out.println(message);
            callTable[1] = true;
        }

        @Messenger("staticMethod3")
        static void staticMethod3() {
            System.out.println("IFKTestCase.TestClass.staticMethod3()");
            callTable[2] = true;
        }

        @Messenger("instanceMethod1")
        Object instanceMethod1(Message message) {
            System.out.println("IFKTestCase.TestClass.instanceMethod1()");
            System.out.println(message);
            callTable[3] = true;
            return "测试成功";
        }

        @Messenger("instanceMethod2")
        void instanceMethod2(Message message) {
            System.out.println("IFKTestCase.TestClass.instanceMethod2()");
            System.out.println(message);
            callTable[4] = true;
        }

        @Messenger("instanceMethod3")
        void instanceMethod3() {
            System.out.println("IFKTestCase.TestClass.instanceMethod3()");
            callTable[5] = true;
        }

        @Messenger({ "staticMethod1", "staticMethod2" })
        static void staticMethod12(Message message) {
            System.out.println("IFKTestCase.TestClass.staticMethod11()");
            System.out.println(message);
            callTable[6] = true;
        }

        @Messenger({ "instanceMethod1", "instanceMethod2" })
        void instanceMethod12(Message message) {
            System.out.println("IFKTestCase.TestClass.instanceMethod12()");
            System.out.println(message);
            callTable[7] = true;
        }

        static void clear() {
            Arrays.fill(callTable, false);
        }
    }

    static class TestClass2 {

        static boolean[] callTable = new boolean[8];

        @Messenger("staticMethod1")
        static Object staticMethod1(Message message) {
            System.out.println("IFKTestCase.TestClass.staticMethod1()");
            System.out.println(message);
            callTable[0] = true;
            return "测试成功";
        }

        @Messenger("staticMethod2")
        static void staticMethod2(Message message) {
            System.out.println("IFKTestCase.TestClass.staticMethod2()");
            System.out.println(message);
            callTable[1] = true;
        }

        @Messenger("staticMethod3")
        static void staticMethod3() {
            System.out.println("IFKTestCase.TestClass.staticMethod3()");
            callTable[2] = true;
        }

        @Messenger("instanceMethod1")
        Object instanceMethod1(Message message) {
            System.out.println("IFKTestCase.TestClass.instanceMethod1()");
            System.out.println(message);
            callTable[3] = true;
            return "测试成功";
        }

        @Messenger("instanceMethod2")
        void instanceMethod2(Message message) {
            System.out.println("IFKTestCase.TestClass.instanceMethod2()");
            System.out.println(message);
            callTable[4] = true;
        }

        @Messenger("instanceMethod3")
        void instanceMethod3() {
            System.out.println("IFKTestCase.TestClass.instanceMethod3()");
            callTable[5] = true;
        }

        @Messenger({ "staticMethod1", "staticMethod2" })
        static void staticMethod12(Message message) {
            System.out.println("IFKTestCase.TestClass.staticMethod11()");
            System.out.println(message);
            callTable[6] = true;
        }

        @Messenger({ "instanceMethod1", "instanceMethod2" })
        void instanceMethod12(Message message) {
            System.out.println("IFKTestCase.TestClass.instanceMethod12()");
            System.out.println(message);
            callTable[7] = true;
        }

        static void clear() {
            Arrays.fill(callTable, false);
        }
    }

    private IFK ifk = IFKFactory.getInstance();

    @Override
    protected void tearDown() throws Exception {
        TestClass.clear();
        TestClass2.clear();
    }

    public void testStaticRegister() throws Exception {
        ifk.m("staticMethod1");
        assertFalse(TestClass.callTable[0]);

        ifk.register(TestClass.class);
        ifk.m("staticMethod1");
        assertTrue(TestClass.callTable[0]);
        ifk.unregister(TestClass.class);
    }

    public void testStaticMultiMethods() throws Exception {
        ifk.register(TestClass.class);

        ifk.m("staticMethod1");
        assertTrue(TestClass.callTable[0]);
        assertTrue(TestClass.callTable[6]);

        ifk.unregister(TestClass.class);
    }

    public void testStaticUni() throws Exception {
        ifk.register(TestClass.class);

        ifk.mm("staticMethod2", "staticMethod1");
        assertTrue(TestClass.callTable[1]);
        assertTrue(TestClass.callTable[0]);
        assertTrue(TestClass.callTable[6]);

        ifk.unregister(TestClass.class);
    }

    public void testStaticMultiMethods2() throws Exception {
        ifk.register(TestClass.class);
        ifk.register(TestClass2.class);

        ifk.rm(TestClass.class, "staticMethod1");
        assertTrue(TestClass.callTable[0]);
        assertTrue(TestClass.callTable[6]);
        assertFalse(TestClass2.callTable[0]);
        assertFalse(TestClass2.callTable[6]);

        ifk.unregister(TestClass.class);
        ifk.unregister(TestClass2.class);
    }

    public void testStaticMultiMethods3() throws Exception {
        ifk.register(TestClass.class);
        ifk.register(TestClass2.class);

        ifk.rm(TestClass2.class, "staticMethod1");
        assertTrue(TestClass2.callTable[0]);
        assertTrue(TestClass2.callTable[6]);
        assertFalse(TestClass.callTable[0]);
        assertFalse(TestClass.callTable[6]);

        ifk.unregister(TestClass.class);
        ifk.unregister(TestClass2.class);
    }

    public void testStaticMultiMethods4() throws Exception {
        ifk.register(TestClass.class);
        ifk.register(TestClass2.class);

        ifk.m("staticMethod1");
        assertTrue(TestClass2.callTable[0]);
        assertTrue(TestClass2.callTable[6]);
        assertTrue(TestClass.callTable[0]);
        assertTrue(TestClass.callTable[6]);

        ifk.unregister(TestClass.class);
        ifk.unregister(TestClass2.class);
    }

    public void testInstanceRegister() throws Exception {
        ifk.m("instanceMethod1");
        assertFalse(TestClass.callTable[3]);

        TestClass testClass = new TestClass();
        ifk.register(testClass);

        ifk.m("instanceMethod1");
        assertTrue(TestClass.callTable[3]);

        ifk.unregister(testClass);
    }

    public void testInstanceMultiMethods() throws Exception {
        TestClass testClass = new TestClass();
        ifk.register(testClass);

        ifk.m("instanceMethod1");
        assertTrue(TestClass.callTable[3]);
        assertTrue(TestClass.callTable[7]);

        ifk.unregister(testClass);
    }

    public void testInstanceMultiMethods2() throws Exception {
        TestClass testClass1 = new TestClass();
        TestClass2 testClass2 = new TestClass2();
        ifk.register(testClass1);
        ifk.register(testClass2);

        ifk.m("instanceMethod1");
        assertTrue(TestClass.callTable[3]);
        assertTrue(TestClass.callTable[7]);
        assertTrue(TestClass2.callTable[3]);
        assertTrue(TestClass2.callTable[7]);

        ifk.unregister(testClass1);
        ifk.unregister(testClass2);
    }

    public void testInstanceMultiMethods3() throws Exception {
        TestClass testClass1 = new TestClass();
        TestClass2 testClass2 = new TestClass2();
        ifk.register(testClass1);
        ifk.register(testClass2);

        ifk.rm(testClass1, "instanceMethod1");
        assertTrue(TestClass.callTable[3]);
        assertTrue(TestClass.callTable[7]);
        assertFalse(TestClass2.callTable[3]);
        assertFalse(TestClass2.callTable[7]);

        ifk.unregister(testClass1);
        ifk.unregister(testClass2);
    }

    public void testInstanceMultiMethods4() throws Exception {
        TestClass testClass1 = new TestClass();
        TestClass2 testClass2 = new TestClass2();
        ifk.register(testClass1);
        ifk.register(testClass2);

        ifk.rm(testClass2, "instanceMethod1");
        assertFalse(TestClass.callTable[3]);
        assertFalse(TestClass.callTable[7]);
        assertTrue(TestClass2.callTable[3]);
        assertTrue(TestClass2.callTable[7]);

        ifk.unregister(testClass1);
        ifk.unregister(testClass2);
    }

    public void testInstanceMethodCallback() throws Exception {
        TestClass testClass = new TestClass();
        ifk.register(testClass);

        ifk.mm("instanceMethod2", "instanceMethod1");
        assertTrue(TestClass.callTable[3]);
        assertTrue(TestClass.callTable[7]);
        assertTrue(TestClass.callTable[4]);

        ifk.unregister(testClass);
    }

    public void testStaticMethodSupportMulti() throws Exception {
        ifk.register(TestClass.class);

        ifk.m("staticMethod1");
        assertTrue(TestClass.callTable[6]);
        TestClass.clear();
        ifk.m("staticMethod2");
        assertTrue(TestClass.callTable[6]);

        ifk.unregister(TestClass.class);
    }

    public void testInstanceMethodSupportMulti() throws Exception {
        TestClass testClass = new TestClass();
        ifk.register(testClass);

        ifk.m("instanceMethod1");
        assertTrue(TestClass.callTable[7]);
        TestClass.clear();
        ifk.m("instanceMethod1");
        assertTrue(TestClass.callTable[7]);

        ifk.unregister(testClass);
    }
}

package com.clark.test;

import java.util.Arrays;

import junit.framework.TestCase;

import com.clark.ifk.IFK;
import com.clark.ifk.Message;
import com.clark.ifk.Messenger;

public class IFKTestCase extends TestCase {

    static class TestClass {

        static boolean[] callTable = new boolean[7];

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

        static void clear() {
            Arrays.fill(callTable, false);
        }
    }

    private IFK ifk = IFK.getInstance();

    @Override
    protected void tearDown() throws Exception {
        TestClass.clear();
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
        
//        ifk.mM(message, callbackMsg, args)
        
        ifk.unregister(TestClass.class);
    }
}

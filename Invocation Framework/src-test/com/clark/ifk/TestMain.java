package com.clark.ifk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestMain {

    public static void main(String[] args) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        printMethods(A.class);
        printMethods(B.class);

        Method methodBInA = A.class.getDeclaredMethod("methodB");
        Method methodBInB = B.class.getMethod("methodB");
        System.out.println(methodBInA + "'s id "
                + System.identityHashCode(methodBInA));
        System.out.println(methodBInB + "'s id "
                + System.identityHashCode(methodBInB));
        System.out.println(methodBInA.equals(methodBInB));

        printMethodsAnnotations(methodBInA);
        printMethodsAnnotations(methodBInB);

        Method methodA = A.class.getDeclaredMethod("methodA");
        methodA.setAccessible(true);
        methodA.invoke(new B());

        Method methodC1 = A.class.getDeclaredMethod("methodC");
        Method methodC2 = B.class.getDeclaredMethod("methodC");
        methodC1.setAccessible(true);
        methodC1.invoke(new B());
        methodC2.setAccessible(true);
        methodC2.invoke(new B());

        try {
            methodC1.invoke(new TestMain());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        printMethods(IA.class);
        printMethods(C.class);
        Method methodIA1 = IA.class.getDeclaredMethod("methodIA");
        Method methodIA2 = C.class.getDeclaredMethod("methodIA");
        System.out.println(methodIA1 + "'s id "
                + System.identityHashCode(methodIA1));
        System.out.println(methodIA2 + "'s id "
                + System.identityHashCode(methodIA2));
        System.out.println(methodIA1.equals(methodIA2));
        printMethodsAnnotations(methodIA1);
        printMethodsAnnotations(methodIA2);

        printMethods(D.class);
        printMethods(E.class);
        Method methodDInD = D.class.getDeclaredMethod("methodD");
        Method methodDInE = E.class.getMethod("methodD");
        System.out.println(methodDInD + "'s id "
                + System.identityHashCode(methodDInD));
        System.out.println(methodDInE + "'s id "
                + System.identityHashCode(methodDInE));
        System.out.println(methodDInD.equals(methodDInE));
        printMethodsAnnotations(methodDInD);
        printMethodsAnnotations(methodDInE);
    }

    static void printMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println(clazz.getCanonicalName() + "'s declared methods: ");
        if (methods != null) {
            System.out.println(Arrays.toString(methods));
        } else {
            System.out.println("null");
        }

        methods = clazz.getMethods();
        System.out.println(clazz.getCanonicalName() + "'s methods: ");
        if (methods != null) {
            System.out.println(Arrays.toString(methods));
        } else {
            System.out.println("null");
        }
    }

    static void printMethodsAnnotations(Method method) {
        System.out.println(method + " declared annotations: ");
        System.out.println(Arrays.toString(method.getDeclaredAnnotations()));
        System.out.println(method + " annotations: ");
        System.out.println(Arrays.toString(method.getAnnotations()));
    }

}

class A {

    protected void methodA() {
        System.out.println("A.methodA()");
    }

    @SignalReceiver("")
    public void methodB() {
        System.out.println("A.methodB()");
    }

    private void methodC() {
        System.out.println("A.methodC()");
    }

}

class B extends A {
    private void methodC() {
        System.out.println("B.methodC()");
    }

    @Override
    public void methodB() {
        System.out.println("B.methodB()");
    }

}

interface IA {
    @SignalReceiver("")
    void methodIA();
}

class C implements IA {

    @Override
    public void methodIA() {
    }

}

abstract class D {
    @SignalReceiver("")
    public abstract void methodD();
}

abstract class E extends D {
}
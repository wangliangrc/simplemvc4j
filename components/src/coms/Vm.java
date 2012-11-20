package coms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Vm {
    public static native void fatalError(String mag);

    public static native int getVersion();

    public static native long fromReflectedField(Field field);

    public static native long fromReflectedMethod(Method method);

    public static native Field toReflectedField(Class<?> clazz, long fieldId,
            boolean isStatic);

    public static native Method toReflectedMethod(Class<?> clazz,
            long methodId, boolean isStatic);

    public static native long getMethodId(Class<?> clazz, String name,
            String sig);

    public static native long getFieldId(Class<?> clazz, String name, String sig);

    public static native long getStaticMethodId(Class<?> clazz, String name,
            String sig);

    public static native long getStaticFieldId(Class<?> clazz, String name,
            String sig);
}

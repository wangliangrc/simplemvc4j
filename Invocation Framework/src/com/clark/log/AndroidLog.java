package com.clark.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class AndroidLog extends Log {
    private static Class<?> androidLogClass;
    private static Method[][] methods;
    static {
        try {
            androidLogClass = Class.forName("android.util.Log");
            methods = new Method[5][2];
            methods[0][0] = androidLogClass.getDeclaredMethod("v",
                    String.class, String.class);
            methods[0][1] = androidLogClass.getDeclaredMethod("v",
                    String.class, String.class, Throwable.class);

            methods[1][0] = androidLogClass.getDeclaredMethod("d",
                    String.class, String.class);
            methods[1][1] = androidLogClass.getDeclaredMethod("d",
                    String.class, String.class, Throwable.class);

            methods[2][0] = androidLogClass.getDeclaredMethod("i",
                    String.class, String.class);
            methods[2][1] = androidLogClass.getDeclaredMethod("i",
                    String.class, String.class, Throwable.class);

            methods[3][0] = androidLogClass.getDeclaredMethod("w",
                    String.class, String.class);
            methods[3][1] = androidLogClass.getDeclaredMethod("w",
                    String.class, String.class, Throwable.class);

            methods[4][0] = androidLogClass.getDeclaredMethod("e",
                    String.class, String.class);
            methods[4][1] = androidLogClass.getDeclaredMethod("e",
                    String.class, String.class, Throwable.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    AndroidLog(String tag) {
        super(tag);
    }

    @Override
    void v(boolean debug, CharSequence text) {
        if (!debug) {
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[0][0].invoke(null, getTag(), text.toString());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void v(boolean debug, CharSequence text, Throwable throwable) {
        if (!debug) {
            return;
        }
        if (throwable == null) {
            v(text);
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[0][1].invoke(null, getTag(), text.toString(), throwable);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void d(boolean debug, CharSequence text) {
        if (!debug) {
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[1][0].invoke(null, getTag(), text.toString());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void d(boolean debug, CharSequence text, Throwable throwable) {
        if (!debug) {
            return;
        }
        if (throwable == null) {
            d(text);
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[1][1].invoke(null, getTag(), text.toString(), throwable);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void i(boolean debug, CharSequence text) {
        if (!debug) {
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[2][0].invoke(null, getTag(), text.toString());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void i(boolean debug, CharSequence text, Throwable throwable) {
        if (!debug) {
            return;
        }
        if (throwable == null) {
            i(text);
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[2][1].invoke(null, getTag(), text.toString(), throwable);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void w(boolean debug, CharSequence text) {
        if (!debug) {
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[3][0].invoke(null, getTag(), text.toString());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void w(boolean debug, CharSequence text, Throwable throwable) {
        if (!debug) {
            return;
        }
        if (throwable == null) {
            w(text);
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[3][1].invoke(null, getTag(), text.toString(), throwable);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void e(boolean debug, CharSequence text) {
        if (!debug) {
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[4][0].invoke(null, getTag(), text.toString());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void e(boolean debug, CharSequence text, Throwable throwable) {
        if (!debug) {
            return;
        }
        if (throwable == null) {
            e(text);
            return;
        }
        text = text == null ? "" : text;
        try {
            methods[4][1].invoke(null, getTag(), text.toString(), throwable);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}

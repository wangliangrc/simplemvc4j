package com.clark.log;

public abstract class Log {
    public static Log getLog(Object object) {
        if (object == null) {
            return getLog(null);
        } else if (object instanceof Class) {
            return getLog(((Class<?>) object).getSimpleName());
        } else {
            return getLog(object.getClass().getSimpleName());
        }
    }

    public static Log getLog(String tag) {
        if (isAndroid) {
            return new AndroidLog(tag);
        } else {
            return new SimpleLog(tag);
        }
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Log.debug = debug;
    }

    public void d(CharSequence text) {
        d(debug, text);
    }

    public void d(CharSequence text, Throwable throwable) {
        d(debug, text, throwable);
    }

    public String getTag() {
        return tag;
    }

    public void i(CharSequence text) {
        i(debug, text);
    }

    public void v(CharSequence text) {
        v(debug, text);
    }

    public void v(CharSequence text, Throwable throwable) {
        v(debug, text, throwable);
    }

    private String tag;

    private static volatile boolean debug = false;

    private static boolean isAndroid;

    static {
        try {
            Class.forName("android.util.Log");
            isAndroid = true;
        } catch (ClassNotFoundException e) {
            isAndroid = false;
        }
    }

    Log(String tag) {
        this.tag = tag == null ? "" : tag;
    }

    public void e(CharSequence text) {
        e(debug, text);
    }

    public void e(CharSequence text, Throwable throwable) {
        e(debug, text, throwable);
    }

    public void i(CharSequence text, Throwable throwable) {
        i(debug, text, throwable);
    }

    public void w(CharSequence text) {
        w(debug, text);
    }

    public void w(CharSequence text, Throwable throwable) {
        w(debug, text, throwable);
    }

    abstract void d(boolean debug, CharSequence text);

    abstract void d(boolean debug, CharSequence text, Throwable throwable);

    abstract void e(boolean debug, CharSequence text);

    abstract void e(boolean debug, CharSequence text, Throwable throwable);

    abstract void i(boolean debug, CharSequence text);

    abstract void i(boolean debug, CharSequence text, Throwable throwable);

    abstract void v(boolean debug, CharSequence text);

    abstract void v(boolean debug, CharSequence text, Throwable throwable);

    abstract void w(boolean debug, CharSequence text);

    abstract void w(boolean debug, CharSequence text, Throwable throwable);
}

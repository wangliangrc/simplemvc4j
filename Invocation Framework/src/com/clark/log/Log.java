package com.clark.log;

public abstract class Log {
    private String tag;

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

    public static Log getLog(String tag) {
        if (isAndroid) {
            return new AndroidLog(tag);
        } else {
            return new SimpleLog(tag);
        }
    }

    public static Log getLog(Object object) {
        if (object == null) {
            return getLog(null);
        } else {
            return getLog(object.getClass().getCanonicalName());
        }
    }

    public String getTag() {
        return tag;
    }

    public abstract void v(CharSequence text);

    public abstract void v(CharSequence text, Throwable throwable);

    public abstract void d(CharSequence text);

    public abstract void d(CharSequence text, Throwable throwable);

    public abstract void i(CharSequence text);

    public abstract void i(CharSequence text, Throwable throwable);

    public abstract void w(CharSequence text);

    public abstract void w(CharSequence text, Throwable throwable);

    public abstract void e(CharSequence text);

    public abstract void e(CharSequence text, Throwable throwable);
}

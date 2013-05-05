package com.baidu.aiu;

import android.os.Build;

/**
 * Created with IntelliJ IDEA. User: baidu Date: 3/31/13 Time: 9:37 PM To change
 * this template use File | Settings | File Templates.
 */
class FixedClassLoaderFactory {

    public static FixedClassLoader getFixedClassLoader() {
        if (Build.VERSION.SDK_INT < 14) {
            return ClassLoaderImpl4.INSTANCE;
        } else {
            return ClassLoaderImpl14.INSTANCE;
        }
    }
}

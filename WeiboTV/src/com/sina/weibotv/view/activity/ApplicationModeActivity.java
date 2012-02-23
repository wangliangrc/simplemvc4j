package com.sina.weibotv.view.activity;

import java.lang.reflect.Method;

import android.os.Bundle;

public class ApplicationModeActivity extends MediatorActivity {
    private Object remoteController;
    private Method setRcGestureOnlyMethod;
    private Method setDefaultModeMethod;
    private Method displayCursorMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class<?> clazz = Class.forName("android.os.RemoteController");
            remoteController = getSystemService("remotecontroller");
            if (remoteController == null) {
                throw new RuntimeException(
                        "无法获取 android.os.RemoteController 实例！");
            }
            setRcGestureOnlyMethod = clazz
                    .getDeclaredMethod("setRcGestureOnly");
            setDefaultModeMethod = clazz.getDeclaredMethod("setDefaultMode");
            displayCursorMethod = clazz.getDeclaredMethod("displayCursor",
                    boolean.class);
        } catch (ClassNotFoundException e) {
            System.err.println("Not Lenovo TV System.");
        } catch (NoSuchMethodException e) {
            // 理论上应该到运行不到这里
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (setDefaultModeMethod != null) {
            try {
                setDefaultModeMethod.invoke(remoteController);
            } catch (Exception e) {
            }
        }
        if (displayCursorMethod != null) {
            try {
                displayCursorMethod.invoke(remoteController, true);
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (setRcGestureOnlyMethod != null) {
            try {
                setRcGestureOnlyMethod.invoke(remoteController);
            } catch (Exception e) {
            }
        }
        if (displayCursorMethod != null) {
            try {
                displayCursorMethod.invoke(remoteController, false);
            } catch (Exception e) {
            }
        }
    }

}

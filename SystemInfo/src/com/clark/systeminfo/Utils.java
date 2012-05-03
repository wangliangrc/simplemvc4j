package com.clark.systeminfo;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;

class Utils {

    public static abstract class BackgroundTask extends
            AsyncTask<Void, Void, Void> {
        public BackgroundTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            activity = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog
                    .show(activity, "注意", "加载数据……", true);
        }

        private Activity activity;
        private ProgressDialog progressDialog;

    }

    public static int getApiLevel(String osVersion) {
        if (osVersion.contains(".")) {
            int num = 0;
            if (osVersion.startsWith("Android")) {
                num = Integer.valueOf(osVersion.substring(8).replace(".", ""));
            } else {
                num = Integer.valueOf(osVersion.replace(".", ""));
            }
            num = num < 100 ? num * 10 : num;
            for (int i = 0; i < API_NUMs.length; i++) {
                if (num < API_NUMs[i]) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < APIs.length; i++) {
                if (osVersion.equalsIgnoreCase(APIs[i][2])) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    public static Object getFieldValue(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        try {
            return field.get(null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(Object obj, String fieldName)
            throws NoSuchFieldException {
        Class<?> clazz = obj.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(Class<?> clazz, String fieldName,
            Object value) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        try {
            field.set(null, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(Object obj, String fieldName, Object value)
            throws NoSuchFieldException {
        Class<?> clazz = obj.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean supportOsVersion(int apiLevel) {
        if (apiLevel <= 0) {
            throw new IllegalArgumentException(
                    "API Level must be greater than 0!");
        }
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    public static boolean supportOsVersion(String osVersion) {
        return supportOsVersion(getApiLevel(osVersion));
    }

    private static final int[] API_NUMs = { 100, 110, 150, 160, 200, 201, 210,
            220, 230, 233, 300, 310, 320, 400, 403 };

    private static final String[][] APIs = {
            new String[] { "Android 1.0", "1.0", "BASE" },
            new String[] { "Android 1.1", "1.1", "BASE_1_1" },
            new String[] { "Android 1.5", "1.5", "CUPCAKE" },
            new String[] { "Android 1.6", "1.6", "DONUT" },
            new String[] { "Android 2.0", "2.0", "ECLAIR" },
            new String[] { "Android 2.0.1", "2.0.1", "ECLAIR_0_1" },
            new String[] { "Android 2.1", "2.1", "ECLAIR_MR1" },
            new String[] { "Android 2.2", "2.2", "FROYO" },
            new String[] { "Android 2.3", "2.3", "GINGERBREAD" },
            new String[] { "Android 2.3.3", "2.3.3", "GINGERBREAD_MR1" },
            new String[] { "Android 3.0", "3.0", "HONEYCOMB" },
            new String[] { "Android 3.1", "3.1", "HONEYCOMB_MR1" },
            new String[] { "Android 3.2", "3.2", "HONEYCOMB_MR2" },
            new String[] { "Android 4.0", "4.0", "ICE_CREAM_SANDWICH" },
            new String[] { "Android 4.0.3", "4.0.3", "ICE_CREAM_SANDWICH_MR1" } };
}

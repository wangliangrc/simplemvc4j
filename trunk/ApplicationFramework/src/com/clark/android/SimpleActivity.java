package com.clark.android;

import static com.clark.func.Functions.println;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.app.LoaderManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.clark.android.annotation.AfterAttachedWindow;
import com.clark.android.annotation.AfterInit;
import com.clark.android.annotation.SaveInstance;
import com.clark.android.annotation.SystemManager;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public abstract class SimpleActivity extends android.app.Activity {
    private static final String TAG = SimpleActivity.class.getSimpleName();
    private Class<?> thisClass;
    private volatile boolean isAttachedToWindow;

    private final Field[] fields;
    private final Method[] methods;

    private final HashSet<Field> allDelegateFields = new HashSet<Field>();

    private final HashSet<Method> attachedToWindowMethods = new HashSet<Method>();
    private final HashSet<Field> saveInstances = new HashSet<Field>();

    protected SimpleActivity() {
        thisClass = getClass();
        fields = thisClass.getFields();
        methods = thisClass.getMethods();
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(layoutResId());
        processFields();
        processMethods();
        onInitialize();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (saveInstances != null && saveInstances.size() > 0) {
            String fieldName = null;
            Class<?> fieldType = null;
            for (Field field : saveInstances) {
                fieldName = field.getName();
                if (savedInstanceState.containsKey(fieldName)) {
                    fieldType = field.getType();
                    getAndSetField(fieldName, fieldType, field,
                            savedInstanceState);
                }
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void getAndPutValue(String fieldName, Class<?> fieldType,
            Field field, Bundle savedInstanceState) {
        try {
            // int
            if (fieldType == int.class) {
                int value = field.getInt(this);
                savedInstanceState.putInt(fieldName, value);
            } else if (fieldType == Integer.class) {
                Integer value = (Integer) field.get(this);
                if (value != null) {
                    savedInstanceState.putInt(fieldName, value);
                }
            } else if (fieldType == int[].class) {
                int[] value = (int[]) field.get(this);
                savedInstanceState.putIntArray(fieldName, value);
            }

            // long
            else if (fieldType == long.class) {
                long value = field.getLong(this);
                savedInstanceState.putLong(fieldName, value);
            } else if (fieldType == Long.class) {
                Long value = (Long) field.get(this);
                if (value != null) {
                    savedInstanceState.putLong(fieldName, value);
                }
            } else if (fieldType == long[].class) {
                long[] value = (long[]) field.get(this);
                savedInstanceState.putLongArray(fieldName, value);
            }

            // boolean
            else if (fieldType == boolean.class) {
                boolean value = field.getBoolean(this);
                savedInstanceState.putBoolean(fieldName, value);
            } else if (fieldType == Boolean.class) {
                Boolean value = (Boolean) field.get(this);
                if (value != null) {
                    savedInstanceState.putBoolean(fieldName, value);
                }
            } else if (fieldType == boolean[].class) {
                boolean[] value = (boolean[]) field.get(this);
                savedInstanceState.putBooleanArray(fieldName, value);
            }

            // short
            else if (fieldType == short.class) {
                short value = field.getShort(this);
                savedInstanceState.putShort(fieldName, value);
            } else if (fieldType == Short.class) {
                Short value = (Short) field.get(this);
                if (value != null) {
                    savedInstanceState.putShort(fieldName, value);
                }
            } else if (fieldType == short[].class) {
                short[] value = (short[]) field.get(this);
                savedInstanceState.putShortArray(fieldName, value);
            }

            // char
            else if (fieldType == char.class) {
                char value = field.getChar(this);
                savedInstanceState.putChar(fieldName, value);
            } else if (fieldType == Character.class) {
                Character value = (Character) field.get(this);
                if (value != null) {
                    savedInstanceState.putChar(fieldName, value);
                }
            } else if (fieldType == char[].class) {
                char[] value = (char[]) field.get(this);
                savedInstanceState.putCharArray(fieldName, value);
            }

            // byte
            else if (fieldType == byte.class) {
                byte value = field.getByte(this);
                savedInstanceState.putByte(fieldName, value);
            } else if (fieldType == Byte.class) {
                Byte value = (Byte) field.get(this);
                if (value != null) {
                    savedInstanceState.putByte(fieldName, value);
                }
            } else if (fieldType == byte[].class) {
                byte[] value = (byte[]) field.get(this);
                savedInstanceState.putByteArray(fieldName, value);
            }

            // float
            else if (fieldType == float.class) {
                float value = field.getFloat(this);
                savedInstanceState.putFloat(fieldName, value);
            } else if (fieldType == Float.class) {
                Float value = (Float) field.get(this);
                if (value != null) {
                    savedInstanceState.putFloat(fieldName, value);
                }
            } else if (fieldType == float[].class) {
                float[] value = (float[]) field.get(this);
                savedInstanceState.putFloatArray(fieldName, value);
            }

            // double
            else if (fieldType == double.class) {
                double value = field.getDouble(this);
                savedInstanceState.putDouble(fieldName, value);
            } else if (fieldType == Double.class) {
                Double value = (Double) field.get(this);
                if (value != null) {
                    savedInstanceState.putDouble(fieldName, value);
                }
            } else if (fieldType == double[].class) {
                double[] value = (double[]) field.get(this);
                savedInstanceState.putDoubleArray(fieldName, value);
            }

            // String
            else if (fieldType == String.class) {
                String value = (String) field.get(this);
                savedInstanceState.putString(fieldName, value);
            } else if (fieldType == String[].class) {
                String[] value = (String[]) field.get(this);
                savedInstanceState.putStringArray(fieldName, value);
            }

            // Bundle
            else if (fieldType == Bundle.class) {
                Bundle value = (Bundle) field.get(this);
                savedInstanceState.putBundle(fieldName, value);
            }

            // others
            else {
                Object value = field.get(this);

                // CharSequence
                if (value instanceof CharSequence) {
                    savedInstanceState.putCharSequence(fieldName,
                            (CharSequence) value);
                } else if (value instanceof CharSequence[]) {
                    savedInstanceState.putCharSequenceArray(fieldName,
                            (CharSequence[]) value);
                }

                // Parcelable
                else if (value instanceof Parcelable) {
                    savedInstanceState.putParcelable(fieldName,
                            (Parcelable) value);
                } else if (value instanceof Parcelable[]) {
                    savedInstanceState.putParcelableArray(fieldName,
                            (Parcelable[]) value);
                }

                // Serializable
                else if (value instanceof Serializable) {
                    savedInstanceState.putSerializable(fieldName,
                            (Serializable) value);
                }

                // List
                else if (value instanceof ArrayList) {
                    ArrayList list = (ArrayList) value;
                    if (list.size() > 0) {
                        Object e = list.get(0);
                        if (e instanceof String) {
                            savedInstanceState.putStringArrayList(fieldName,
                                    (ArrayList<String>) value);
                        } else if (e instanceof CharSequence) {
                            savedInstanceState.putCharSequenceArrayList(
                                    fieldName, (ArrayList<CharSequence>) value);
                        } else if (e instanceof Parcelable) {
                            savedInstanceState.putParcelableArrayList(
                                    fieldName,
                                    (ArrayList<? extends Parcelable>) value);
                        } else if (e instanceof Integer) {
                            savedInstanceState.putIntegerArrayList(fieldName,
                                    (ArrayList<Integer>) value);
                        } else {
                            Log.w(TAG,
                                    "save instances忽略"
                                            + thisClass.getSimpleName() + "."
                                            + fieldName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null && outState.size() > 0 && saveInstances != null
                && saveInstances.size() > 0) {
            String name = null;
            Class<?> type = null;
            for (Field field : saveInstances) {
                name = field.getName();
                type = field.getType();
                getAndPutValue(name, type, field, outState);
            }
        }

        println("onSaveInstanceState\n");
        println(outState);
    }

    private void getAndSetField(String name, Class<?> type, Field field,
            Bundle outState) {

        try {
            // int
            if (type == int.class) {
                field.setInt(this, outState.getInt(name));
            } else if (type == Integer.class) {
                field.set(this, outState.getInt(name));
            } else if (type == int[].class) {
                field.set(this, outState.getIntArray(name));
            }

            // long
            else if (type == long.class) {
                field.setLong(this, outState.getLong(name));
            } else if (type == Long.class) {
                field.set(this, outState.getLong(name));
            } else if (type == long[].class) {
                field.set(this, outState.getLongArray(name));
            }

            // boolean
            else if (type == boolean.class) {
                field.setBoolean(this, outState.getBoolean(name));
            } else if (type == Boolean.class) {
                field.set(this, outState.getBoolean(name));
            } else if (type == boolean[].class) {
                field.set(this, outState.getBooleanArray(name));
            }

            // short
            else if (type == short.class) {
                field.setShort(this, outState.getShort(name));
            } else if (type == Short.class) {
                field.set(this, outState.getShort(name));
            } else if (type == short[].class) {
                field.set(this, outState.getShortArray(name));
            }

            // char
            else if (type == char.class) {
                field.setChar(this, outState.getChar(name));
            } else if (type == Character.class) {
                field.set(this, outState.getChar(name));
            } else if (type == char[].class) {
                field.set(this, outState.getCharArray(name));
            }

            // byte
            else if (type == byte.class) {
                field.setByte(this, outState.getByte(name));
            } else if (type == Byte.class) {
                field.set(this, outState.getByte(name));
            } else if (type == byte[].class) {
                field.set(this, outState.getByteArray(name));
            }

            // float
            else if (type == float.class) {
                field.setFloat(this, outState.getFloat(name));
            } else if (type == Float.class) {
                field.set(this, outState.getFloat(name));
            } else if (type == float[].class) {
                field.set(this, outState.getFloatArray(name));
            }

            // double
            else if (type == double.class) {
                field.setDouble(this, outState.getDouble(name));
            } else if (type == Double.class) {
                field.set(this, outState.getDouble(name));
            } else if (type == double[].class) {
                field.set(this, outState.getDoubleArray(name));
            }

            // String
            else if (type == String.class) {
                field.set(this, outState.getString(name));
            } else if (type == String[].class) {
                field.set(this, outState.getStringArray(name));
            }

            // Bundle
            else if (type == Bundle.class) {
                field.set(this, outState.getBundle(name));
            }

            // CharSequence
            else if (CharSequence.class.isAssignableFrom(type)) {
                field.set(this, outState.getCharSequence(name));
            } else if (CharSequence[].class.isAssignableFrom(type)) {
                field.set(this, outState.getCharSequenceArray(name));
            }

            // Parcelable
            else if (Parcelable.class.isAssignableFrom(type)) {
                field.set(this, outState.getParcelable(name));
            } else if (Parcelable[].class.isAssignableFrom(type)) {
                field.set(this, outState.getParcelableArray(name));
            }

            // Serializable
            else if (Serializable.class.isAssignableFrom(type)) {
                field.set(this, outState.getSerializable(name));
            }

            // ArrayList
            else if (ArrayList.class.isAssignableFrom(type)) {
                field.set(this, outState.get(name));
            }

            // Log
            else {
                Log.w(TAG, "restore instances忽略" + thisClass.getSimpleName()
                        + "." + name);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void processMethods() {
        if (methods != null && methods.length > 0) {
            AfterAttachedWindow afterAttachedWindow = null;
            AfterInit afterInit = null;
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                afterAttachedWindow = method
                        .getAnnotation(AfterAttachedWindow.class);
                if (afterAttachedWindow != null) {
                    attachedToWindowMethods.add(method);
                }

                afterInit = method.getAnnotation(AfterInit.class);
                if (afterInit != null) {
                    try {
                        method.invoke(this);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
    }

    private void processFields() {
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())
                        || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                findViewAndListeners(getListenerAdapter(), field);
                findSystemManager(field);
                findSavedInstances(field);
            }
        }
    }

    private void findSavedInstances(Field field) {
        final SaveInstance saveInstance = field
                .getAnnotation(SaveInstance.class);
        if (saveInstance == null) {
            return;
        }

        saveInstances.add(field);

        allDelegateFields.add(field);
    }

    private void findViewAndListeners(ListenerAdapter listenerAdapter,
            Field field) {
        final ViewProperty viewProperty = field
                .getAnnotation(ViewProperty.class);
        if (viewProperty != null) {
            final ViewListener[] viewListeners = viewProperty.listeners();
            final View view = findViewById(viewProperty.value());
            if (view == null) {
                throw new NullPointerException();
            }
            try {
                field.set(this, view);

                allDelegateFields.add(field);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }

            if (listenerAdapter != null) {
                findListeners(listenerAdapter, viewListeners, view);
            }
        }
    }

    private void findSystemManager(Field field) {
        final SystemManager systemManager = field
                .getAnnotation(SystemManager.class);
        if (systemManager != null) {
            Class<?> type = field.getType();
            try {
                if (type == PackageManager.class) {
                    field.set(this, getPackageManager());
                } else if (type == WindowManager.class) {
                    field.set(this, getWindowManager());
                } else if (type == LayoutInflater.class) {
                    field.set(this, getLayoutInflater());
                } else if (type == ActivityManager.class) {
                    field.set(this, getSystemService(ACTIVITY_SERVICE));
                } else if (type == PowerManager.class) {
                    field.set(this, getSystemService(POWER_SERVICE));
                } else if (type == AlarmManager.class) {
                    field.set(this, getSystemService(ALARM_SERVICE));
                } else if (type == NotificationManager.class) {
                    field.set(this, getSystemService(NOTIFICATION_SERVICE));
                } else if (type == KeyguardManager.class) {
                    field.set(this, getSystemService(KEYGUARD_SERVICE));
                } else if (type == LocationManager.class) {
                    field.set(this, LOCATION_SERVICE);
                } else if (type == SearchManager.class) {
                    field.set(this, getSystemService(SEARCH_SERVICE));
                } else if (type == Vibrator.class) {
                    field.set(this, getSystemService(VIBRATOR_SERVICE));
                } else if (type == ConnectivityManager.class) {
                    field.set(this, getSystemService(CONNECTIVITY_SERVICE));
                } else if (type == WifiManager.class) {
                    field.set(this, getSystemService(WIFI_SERVICE));
                } else if (type == InputMethodManager.class) {
                    field.set(this, getSystemService(INPUT_METHOD_SERVICE));
                } else if (type == UiModeManager.class) {
                    field.set(this, getSystemService(UI_MODE_SERVICE));
                } else if (Build.VERSION.SDK_INT >= 8
                        && type == DownloadManager.class) {
                    field.set(this, getSystemService(DOWNLOAD_SERVICE));
                } else if (type == AssetManager.class) {
                    field.set(this, getAssets());
                } else if (Build.VERSION.SDK_INT >= 11
                        && type == FragmentManager.class) {
                    field.set(this, getFragmentManager());
                } else if (Build.VERSION.SDK_INT >= 11
                        && type == LoaderManager.class) {
                    field.set(this, getLoaderManager());
                }

                allDelegateFields.add(field);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void findListeners(final ListenerAdapter viewAdapter,
            ViewListener[] listeners, View view) {
        for (ViewListener listener : listeners) {
            switch (listener) {
                case ON_CLICK:
                    view.setOnClickListener(viewAdapter);
                    break;
                case ON_FOCUS_CHANGE:
                    view.setOnFocusChangeListener(viewAdapter);
                    break;
                case ON_KEY:
                    view.setOnKeyListener(viewAdapter);
                    break;
                case ON_LONG_CLICK:
                    view.setOnLongClickListener(viewAdapter);
                    break;
                case ON_TOUCH:
                    view.setOnTouchListener(viewAdapter);
                    break;
                case ON_ITEM_CLICK:
                    ((AdapterView<?>) view).setOnItemClickListener(viewAdapter);
                    break;
                case ON_ITEM_LONG_CLICK:
                    ((AdapterView<?>) view)
                            .setOnItemLongClickListener(viewAdapter);
                    break;
                case ON_ITEM_SELECTED:
                    ((AdapterView<?>) view)
                            .setOnItemSelectedListener(viewAdapter);
                    break;
                case ON_SCROLL:
                    ((AbsListView) view).setOnScrollListener(viewAdapter);
                    break;
                case RECYCLER:
                    ((AbsListView) view).setRecyclerListener(viewAdapter);
                    break;
            }
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        afterAttachedWindow();
    }

    private void afterAttachedWindow() {
        if (attachedToWindowMethods == null
                || attachedToWindowMethods.size() == 0) {
            return;
        }

        for (Method method : attachedToWindowMethods) {
            try {
                method.invoke(this);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
    }

    public final boolean isAttachedToWindow() {
        return isAttachedToWindow;
    }

    protected void onInitialize() {
    }

    protected abstract int layoutResId();

    protected abstract ListenerAdapter getListenerAdapter();

    @Override
    public void setContentView(View view) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContentView(int layoutResID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void onDestroy() {
        if (allDelegateFields != null && allDelegateFields.size() > 0) {
            Class<?> type = null;
            for (Field field : allDelegateFields) {
                type = field.getType();
                if (!type.isPrimitive()) {
                    try {
                        field.set(this, null);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        super.onDestroy();
    }
}

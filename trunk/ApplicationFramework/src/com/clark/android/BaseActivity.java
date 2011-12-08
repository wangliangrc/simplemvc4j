package com.clark.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.clark.android.annotation.AfterAttachedWindow;
import com.clark.android.annotation.AfterInit;
import com.clark.android.annotation.SaveInstance;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public abstract class BaseActivity extends android.app.Activity implements FieldHolder {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private volatile boolean isAttachedToWindow;

    /**
     * 用于标识 Activity 是否处于重生阶段
     */
    private boolean isActivityReborn;

    private Class<?> thisClass;
    private final Field[] fields;
    private final Method[] methods;

    private final HashSet<Field> allDelegateFields = new HashSet<Field>();

    private final HashSet<Method> attachedToWindowMethods = new HashSet<Method>();
    private final HashSet<Field> saveInstances = new HashSet<Field>();

    protected BaseActivity() {
        thisClass = getClass();
        fields = thisClass.getFields();
        methods = thisClass.getMethods();
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(layoutResId());
        isActivityReborn = savedInstanceState != null
                && savedInstanceState.size() != 0;

        processFields();
        processMethods();
        onInitialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityReborn) {
            onRebirth();
        } else {
            onComeIntoBeing();
        }
    }

    /**
     * 第一次初始化时没有保存数据的话，应该在这个回调中初始化数据（网络、IO、数据库……）
     */
    protected void onComeIntoBeing() {
    }

    /**
     * 如果定义了 @ SaveInstance 属性的话，可以在本回调中进行赋值操作
     */
    protected void onRebirth() {
    }

    @Override
    protected final void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (saveInstances != null && saveInstances.size() > 0) {
            String fieldName = null;
            Class<?> fieldType = null;
            for (Field field : saveInstances) {
                fieldName = field.getName();
                if (savedInstanceState.containsKey(fieldName)) {
                    fieldType = field.getType();
                    Utils.setFieldFromBundle(this, fieldName, fieldType, field,
                            savedInstanceState);
                }
            }
        }
    }

    @Override
    protected final void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null && outState.size() > 0 && saveInstances != null
                && saveInstances.size() > 0) {
            String name = null;
            Class<?> type = null;
            for (Field field : saveInstances) {
                name = field.getName();
                type = field.getType();
                Utils.getAndPutValue(this, name, type, field, outState);
            }
        }
    }

    @Override
    public final Set<Field> getHoldFields() {
        return allDelegateFields;
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
                Utils.findSystemManager(this, field);
                findSavedInstances(field);
                Utils.findIntentExtra(this, field, getIntent());
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
        Utils.fieldHolderOnDestroy(this);
        super.onDestroy();
    }
}

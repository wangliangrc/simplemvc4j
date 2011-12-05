package com.clark.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.clark.android.annotation.AfterAttachedWindow;
import com.clark.android.annotation.AfterInit;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public abstract class SimpleActivity extends android.app.Activity {
    private Class<?> thisClass;
    private volatile boolean isAttachedToWindow;

    private final Field[] fields;
    private final Method[] methods;

    private final HashSet<Method> attachedToWindowMethods = new HashSet<Method>();

    protected SimpleActivity() {
        thisClass = getClass();
        fields = thisClass.getFields();
        methods = thisClass.getMethods();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId());
        findViewAndSetListeners();
        execOrPrepareMethods();
    }

    private void execOrPrepareMethods() {
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

    private void findViewAndSetListeners() {
        final ListenerAdapter viewAdapter = getListenerAdapter();
        ViewProperty property = null;
        ViewListener[] listeners = null;
        View view = null;
        if (fields != null) {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                property = field.getAnnotation(ViewProperty.class);
                if (property != null) {
                    listeners = property.listeners();
                    try {
                        view = findViewById(property.value());
                        if (view == null) {
                            throw new NullPointerException();
                        }
                        field.set(this, view);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }

                    if (viewAdapter != null) {
                        findListeners(viewAdapter, listeners, view);
                    }
                }
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
}

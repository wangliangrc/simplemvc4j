package com.clark.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.clark.android.annotation.AfterAttachedWindow;
import com.clark.android.annotation.BeforeDetachedWindow;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public abstract class SimpleActivity extends android.app.Activity{
    private Class<?> thisClass;
    private volatile boolean isAttachedToWindow;

    private Field[] fields;
    private Method[] methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thisClass = getClass();
        fields = thisClass.getFields();
        methods = thisClass.getMethods();
        super.onCreate(savedInstanceState);
        setContentView(layoutResId());
        findViewAndSetListeners();
    }

    private void findViewAndSetListeners() {
        final ListenerAdapter viewAdapter = getViewAdapter();
        if(viewAdapter == null) {
            throw new NullPointerException("ViewAdapter can't be null!");
        }
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
                                ((AdapterView<?>) view)
                                        .setOnItemClickListener(viewAdapter);
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
        AfterAttachedWindow afterAttachedWindow = null;
        if (methods != null) {
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                afterAttachedWindow = method
                        .getAnnotation(AfterAttachedWindow.class);
                if (afterAttachedWindow != null) {
                    try {
                        method.invoke(this);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        BeforeDetachedWindow beforeDetachedWindow = null;
        if (methods != null) {
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                beforeDetachedWindow = method
                        .getAnnotation(BeforeDetachedWindow.class);
                if (beforeDetachedWindow != null) {
                    try {
                        method.invoke(this);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
    }

    public final boolean isAttachedToWindow() {
        return isAttachedToWindow;
    }

    protected abstract int layoutResId();
    protected abstract ListenerAdapter getViewAdapter();

    @Override
    public void setContentView(View view) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        throw new UnsupportedOperationException();
    }
}

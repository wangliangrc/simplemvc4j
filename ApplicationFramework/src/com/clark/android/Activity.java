package com.clark.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.clark.android.annotation.AfterAttachedWindow;
import com.clark.android.annotation.BeforeDetachedWindow;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public abstract class Activity extends android.app.Activity implements
        OnClickListener, OnFocusChangeListener, OnKeyListener,
        OnLongClickListener, OnTouchListener, OnItemClickListener,
        OnItemLongClickListener, OnItemSelectedListener, OnScrollListener,
        RecyclerListener {
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
                                view.setOnClickListener(this);
                                break;
                            case ON_FOCUS_CHANGE:
                                view.setOnFocusChangeListener(this);
                                break;
                            case ON_KEY:
                                view.setOnKeyListener(this);
                                break;
                            case ON_LONG_CLICK:
                                view.setOnLongClickListener(this);
                                break;
                            case ON_TOUCH:
                                view.setOnTouchListener(this);
                                break;
                            case ON_ITEM_CLICK:
                                ((AdapterView<?>) view)
                                        .setOnItemClickListener(this);
                                break;
                            case ON_ITEM_LONG_CLICK:
                                ((AdapterView<?>) view)
                                        .setOnItemLongClickListener(this);
                                break;
                            case ON_ITEM_SELECTED:
                                ((AdapterView<?>) view)
                                        .setOnItemSelectedListener(this);
                                break;
                            case ON_SCROLL:
                                ((AbsListView) view).setOnScrollListener(this);
                                break;
                            case RECYCLER:
                                ((AbsListView) view).setRecyclerListener(this);
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

    @Override
    public void setContentView(View view) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        throw new UnsupportedOperationException();
    }
}

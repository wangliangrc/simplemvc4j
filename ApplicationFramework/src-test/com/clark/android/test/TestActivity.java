package com.clark.android.test;

import static com.clark.func.Functions.println;
import android.view.View;
import android.widget.Button;

import com.clark.android.R;
import com.clark.android.SimpleActivity;
import com.clark.android.ListenerAdapter;
import com.clark.android.annotation.AfterAttachedWindow;
import com.clark.android.annotation.BeforeDetachedWindow;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public class TestActivity extends SimpleActivity {
    @Override
    protected ListenerAdapter getViewAdapter() {
        return new ListenerAdapter() {
            @Override
            public void onClick(View v) {
                println(v);
            }

            @Override
            public boolean onLongClick(View v) {
                println(v);
                return false;
            }
        };
    }

    @Override
    protected int layoutResId() {
        return R.layout.main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        println(button);
    }

    @ViewProperty(value = R.id.test, listeners = { ViewListener.ON_CLICK,
            ViewListener.ON_LONG_CLICK })
    public Button button;

    @AfterAttachedWindow
    @BeforeDetachedWindow
    public void testAttachedWindow() {
        println("testAttachedWindow");
    }

}
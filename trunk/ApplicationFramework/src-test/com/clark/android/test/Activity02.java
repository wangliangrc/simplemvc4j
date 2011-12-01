package com.clark.android.test;

import static com.clark.func.Functions.println;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.clark.android.ListenerAdapter;
import com.clark.android.R;
import com.clark.android.SimpleActivity;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public class Activity02 extends SimpleActivity {

    @Override
    protected int layoutResId() {
        return R.layout.main;
    }

    @Override
    protected ListenerAdapter getViewAdapter() {
        return new ListenerAdapter() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        Activity03.class));
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        println("Activity02: " + getTaskId() + " [" + this + "]");
        println("Activity02: " + isTaskRoot() + " [" + this + "]");
    }

    @ViewProperty(value = R.id.test, listeners = { ViewListener.ON_CLICK })
    public Button button;
}

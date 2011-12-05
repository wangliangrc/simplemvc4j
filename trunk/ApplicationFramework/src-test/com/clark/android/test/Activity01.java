package com.clark.android.test;

import static com.clark.func.Functions.println;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.clark.android.ListenerAdapter;
import com.clark.android.R;
import com.clark.android.SimpleActivity;
import com.clark.android.annotation.SystemManager;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public class Activity01 extends SimpleActivity {
    @Override
    protected ListenerAdapter getListenerAdapter() {
        return new ListenerAdapter() {
            @Override
            public void onClick(View v) {
                println(packageManager);
            }
        };
    }

    @Override
    protected int layoutResId() {
        return R.layout.main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        println("Activity01: " + getTaskId() + " [" + this + "]");
        println("Activity01: " + isTaskRoot() + " [" + this + "]");
    }

    @ViewProperty(value = R.id.test, listeners = { ViewListener.ON_CLICK })
    public Button button;

    @SystemManager
    public PackageManager packageManager;
}
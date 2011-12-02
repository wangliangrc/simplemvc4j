package com.clark.android.test;

import static com.clark.func.Functions.println;

import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.clark.android.ListenerAdapter;
import com.clark.android.R;
import com.clark.android.SimpleActivity;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public class Activity01 extends SimpleActivity {
    @Override
    protected ListenerAdapter getListenerAdapter() {
        return new ListenerAdapter() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
                List<ResolveInfo> queryIntentActivities = packageManager
                        .queryIntentActivities(intent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                println(packageManager);
                println(queryIntentActivities);

                IntentFilter filter = new IntentFilter();
                try {
                    filter.addDataType("*");
                } catch (MalformedMimeTypeException e) {
                    e.printStackTrace();
                }
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

        packageManager = getPackageManager();
    }

    @ViewProperty(value = R.id.test, listeners = { ViewListener.ON_CLICK })
    public Button button;

    public PackageManager packageManager;
}
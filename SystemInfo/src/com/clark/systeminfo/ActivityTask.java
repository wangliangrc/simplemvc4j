package com.clark.systeminfo;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ActivityTask extends ListActivity implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(getApplicationContext(),
                        ActivityRunningApp.class));
                break;

            case 1:
                startActivity(new Intent(getApplicationContext(),
                        ActivityRunningService.class));
                break;

            case 2:
                startActivity(new Intent(getApplicationContext(),
                        ActivityRunningTask.class));
                break;

            case 3:
                startActivity(new Intent(getApplicationContext(),
                        ActivityRecentTask.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        textView = (TextView) findViewById(R.id.text);
        StringBuilder builder = new StringBuilder();
        builder.append("MemoryClass:").append(activityManager.getMemoryClass())
                .append("MB\n");
        if (SUPPORT_APILEVEL_11) {
            // XXX API Level 11
            builder.append("large memery(android:largeHeap=\"true\"):")
                    .append(activityManager.getLargeMemoryClass())
                    .append("MB\n");
            builder.append("launcher large icon size:")
                    .append(activityManager.getLauncherLargeIconSize())
                    .append("px\n");
            builder.append("launcher large icon density:")
                    .append(activityManager.getLauncherLargeIconDensity())
                    .append("\n");
        }

        Runtime runtime = Runtime.getRuntime();
        final int cpus = runtime.availableProcessors();
        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();
        builder.append("cpus: ").append(cpus).append("\n");
        builder.append("当前 Java heap free space: ")
                .append(formatSize(freeMemory)).append("\n");
        builder.append("当前 Java heap total space: ")
                .append(formatSize(totalMemory)).append("\n");
        builder.append("当前 Java heap max space: ")
                .append(formatSize(maxMemory)).append("\n");
        textView.setText(builder.toString());

        setListAdapter(new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1,
                getResources().getStringArray(R.array.task_list_items)));
        getListView().setOnItemClickListener(this);
    }

    private static String formatSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return size / 1024 + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            return size / (1024 * 1024) + "MB";
        } else {
            return size / (1024 * 1024 * 1024) + "GB";
        }
    }

    private ActivityManager activityManager;

    private TextView textView;

    private static final boolean SUPPORT_APILEVEL_11 = Utils
            .supportOsVersion(11);
}

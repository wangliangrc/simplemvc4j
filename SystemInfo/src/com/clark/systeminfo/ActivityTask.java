package com.clark.systeminfo;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityTask extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        textView = (TextView) findViewById(R.id.text);
        StringBuilder builder = new StringBuilder();
        builder.append("memery:").append(activityManager.getMemoryClass()).append("MB\n");
        builder.append("large memery:").append(activityManager.getLargeMemoryClass()).append("MB\n");
        textView.setText(builder.toString());
    }

    private ActivityManager activityManager;
    private TextView textView;
}

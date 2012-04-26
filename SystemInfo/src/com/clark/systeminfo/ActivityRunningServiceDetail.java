package com.clark.systeminfo;

import android.app.ActivityManager;
import android.os.Bundle;

public class ActivityRunningServiceDetail extends AbstractTextActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runningServiceInfo = getIntent().getParcelableExtra("serviceinfo");
        if (runningServiceInfo == null) {
            throw new NullPointerException();
        }

        StringBuilder builder = new StringBuilder();
        builder.append("process name:").append(runningServiceInfo.process);
        builder.append("\nservice name:").append(runningServiceInfo.service);
        builder.append("\npid:").append(runningServiceInfo.pid);
        builder.append("\nuid:").append(runningServiceInfo.uid);
        builder.append("\nflags:").append(printFlags(runningServiceInfo.flags));
        builder.append("\nstarted:").append(runningServiceInfo.started);
        builder.append("\ncrash count:").append(runningServiceInfo.crashCount);
        builder.append("\nis foreground service:").append(runningServiceInfo.foreground);
        builder.append("\nclient count:").append(runningServiceInfo.clientCount);
        builder.append("\nclient package:").append(runningServiceInfo.clientPackage);
        builder.append("\nclient label:").append(runningServiceInfo.clientLabel);
        mTextView.setText(builder.toString());
    }

    private String printFlags(int flags) {
        StringBuilder builder = new StringBuilder();
        if ((flags & ActivityManager.RunningServiceInfo.FLAG_FOREGROUND) != 0) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append("FLAG_FOREGROUND");
        }
        if ((flags & ActivityManager.RunningServiceInfo.FLAG_PERSISTENT_PROCESS) != 0) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append("FLAG_PERSISTENT_PROCESS");
        }
        if ((flags & ActivityManager.RunningServiceInfo.FLAG_STARTED) != 0) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append("FLAG_STARTED");
        }
        if ((flags & ActivityManager.RunningServiceInfo.FLAG_SYSTEM_PROCESS) != 0) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append("FLAG_SYSTEM_PROCESS");
        }
        builder.append(" }");
        return "{ " + builder.toString();
    }

    private ActivityManager.RunningServiceInfo runningServiceInfo;
}

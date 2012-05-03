package com.clark.systeminfo;

import java.util.Arrays;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Bundle;

public class ActivityRunningAppDetail extends AbstractTextActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appProcessInfo = getIntent().getParcelableExtra("processinfo");
        if (appProcessInfo == null) {
            throw new IllegalArgumentException();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("process name:").append(appProcessInfo.processName)
                .append("\n");
        builder.append("pid:").append(appProcessInfo.pid).append("\n");
        builder.append("uid:").append(appProcessInfo.uid).append("\n");
        builder.append("importance:")
                .append(printImportance(appProcessInfo.importance))
                .append("\n");
        builder.append("lru(只有在 IMPORTANCE_BACKGROUND 下有用):")
                .append(appProcessInfo.lru).append("\n");
        builder.append("importanceReasonCode:")
                .append(printImportanceReason(appProcessInfo.importanceReasonCode))
                .append("\n");
        builder.append("pid of other process which is our client:")
                .append(appProcessInfo.importanceReasonPid).append("\n");
        builder.append("importanceReasonComponent:")
                .append(appProcessInfo.importanceReasonComponent).append("\n");
        builder.append("pkgList:")
                .append(Arrays.toString(appProcessInfo.pkgList)).append("\n");
        mTextView.setText(builder.toString());
    }

    private String printImportance(int importance) {
        if (SUPPORT_APILEVEL_9) {
            // XXX API Level 9
            if (importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                return "IMPORTANCE_PERCEPTIBLE";
            }
        }
        switch (importance) {
            case RunningAppProcessInfo.IMPORTANCE_BACKGROUND:
                return "IMPORTANCE_BACKGROUND";

            case RunningAppProcessInfo.IMPORTANCE_EMPTY:
                return "IMPORTANCE_EMPTY";

            case RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
                return "IMPORTANCE_FOREGROUND";

            case RunningAppProcessInfo.IMPORTANCE_SERVICE:
                return "IMPORTANCE_SERVICE";

            case RunningAppProcessInfo.IMPORTANCE_VISIBLE:
                return "IMPORTANCE_VISIBLE";

            default:
                throw new IllegalArgumentException();
        }
    }

    private String printImportanceReason(int reason) {
        switch (reason) {
            case RunningAppProcessInfo.REASON_PROVIDER_IN_USE:
                return "REASON_PROVIDER_IN_USE";

            case RunningAppProcessInfo.REASON_SERVICE_IN_USE:
                return "REASON_SERVICE_IN_USE";

            case RunningAppProcessInfo.REASON_UNKNOWN:
                return "REASON_UNKNOWN";

            default:
                throw new IllegalArgumentException();
        }
    }

    private ActivityManager.RunningAppProcessInfo appProcessInfo;
    private static final boolean SUPPORT_APILEVEL_9 = Utils.supportOsVersion(9);
}

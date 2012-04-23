package com.clark.systeminfo;

import java.util.Date;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityApplicationDetail extends Activity implements
        OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity:
                break;

            case R.id.service:
                break;

            case R.id.broadcast_receiver:
                break;

            case R.id.content_provider:
                break;

            case R.id.permission:
                break;

            case R.id.instrumentation:
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        packageManager = getPackageManager();
        packageInfo = getIntent().getParcelableExtra("info");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_app_detail);

        basicInfoView = (TextView) findViewById(R.id.basic_info);
        buttons[0] = (Button) findViewById(R.id.activity);
        buttons[1] = (Button) findViewById(R.id.service);
        buttons[2] = (Button) findViewById(R.id.broadcast_receiver);
        buttons[3] = (Button) findViewById(R.id.content_provider);
        buttons[4] = (Button) findViewById(R.id.permission);
        buttons[5] = (Button) findViewById(R.id.instrumentation);
        for (int i = 0; i < 6; i++) {
            buttons[i].setOnClickListener(this);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("应用程序：")
                .append(packageManager
                        .getApplicationLabel(packageInfo.applicationInfo))
                .append("\n");
        builder.append("packageName：").append(packageInfo.packageName)
                .append("\n");
        builder.append("sharedUserId：").append(packageInfo.sharedUserId)
                .append("\n");
        builder.append("sharedUserLabel：").append(packageInfo.sharedUserLabel)
                .append("\n");
        builder.append("versionName：").append(packageInfo.versionName)
                .append("\n");
        builder.append("versionCode：").append(packageInfo.versionCode)
                .append("\n");
        builder.append("第一次安装时间：")
                .append(new Date(packageInfo.firstInstallTime).toLocaleString())
                .append("\n");
        if (packageInfo.lastUpdateTime > 0) {
            builder.append("最近一次更新时间：")
                    .append(new Date(packageInfo.lastUpdateTime).toLocaleString()).append("\n");
        }
        basicInfoView.setText(builder.toString());
    }

    private PackageInfo packageInfo;
    private PackageManager packageManager;
    private TextView basicInfoView;

    private Button[] buttons = new Button[6];
}

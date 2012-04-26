package com.clark.systeminfo;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clark.func.MapArray;

public class ActivityRunningApp extends ListActivity implements
        OnItemClickListener {

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mapArray.size();
        }

        @Override
        public Object getItem(int position) {
            return mapArray.keyOfIndex(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.layout_app_item,
                        parent, false);
                ViewHolder holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.appName = (TextView) convertView
                        .findViewById(R.id.app_name);
                holder.appPackageName = (TextView) convertView
                        .findViewById(R.id.app_package);
                convertView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            PackageInfo info = (PackageInfo) getItem(position);
            holder.icon.setImageDrawable(packageManager
                    .getApplicationIcon(info.applicationInfo));
            holder.appName.setText(packageManager
                    .getApplicationLabel(info.applicationInfo));
            holder.appPackageName.setText(info.packageName);

            return convertView;
        }

    }

    private static class ViewHolder {
        private ImageView icon;
        private TextView appName;
        private TextView appPackageName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Intent intent = new Intent(getApplicationContext(),
                ActivityRunningAppDetail.class);
        intent.putExtra("processinfo", mapArray.valueOfIndex(position));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutInflater = getLayoutInflater();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        packageManager = getPackageManager();
        super.onCreate(savedInstanceState);
        backgroundTask = new Utils.BackgroundTask(this) {

            @Override
            protected Void doInBackground(Void... params) {
                ArrayList<ActivityManager.RunningAppProcessInfo> processesInfo = new ArrayList<ActivityManager.RunningAppProcessInfo>(
                        activityManager.getRunningAppProcesses());
                ArrayList<PackageInfo> pInfos = new ArrayList<PackageInfo>(
                        packageManager.getInstalledPackages(0));

                PackageInfo packageInfo = null;
                RunningAppProcessInfo appProcessInfo = null;
                int pid = -1;
                START: for (int i = 0, len = processesInfo.size(); i < len; i++) {
                    appProcessInfo = processesInfo.get(i);
                    pid = appProcessInfo.uid;
                    for (int j = 0, jlen = pInfos.size(); j < jlen; j++) {
                        packageInfo = pInfos.get(j);
                        if (packageInfo.applicationInfo.uid == pid) {
                            mapArray.append(packageInfo, appProcessInfo);
                            continue START;
                        }
                    }

                    System.out.println("没有找到" + appProcessInfo.processName
                            + "的packageInfo");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                init();
            }
        };
        backgroundTask.execute();
    }

    private void init() {
        setListAdapter(new MyAdapter());
        getListView().setOnItemClickListener(this);
    }

    private ActivityManager activityManager;

    private Utils.BackgroundTask backgroundTask;

    private LayoutInflater layoutInflater;

    private PackageManager packageManager;

    private MapArray<PackageInfo, ActivityManager.RunningAppProcessInfo> mapArray = new MapArray<PackageInfo, ActivityManager.RunningAppProcessInfo>();
}

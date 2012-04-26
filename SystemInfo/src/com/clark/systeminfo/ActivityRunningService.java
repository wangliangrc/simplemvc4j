package com.clark.systeminfo;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
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

public class ActivityRunningService extends ListActivity implements
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
                ActivityRunningServiceDetail.class);
        intent.putExtra("serviceinfo", mapArray.valueOfIndex(position));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        packageManager = getPackageManager();
        layoutInflater = getLayoutInflater();
        super.onCreate(savedInstanceState);

        backgroundTask = new Utils.BackgroundTask(this) {

            @Override
            protected Void doInBackground(Void... params) {
                List<ActivityManager.RunningServiceInfo> runningServices = activityManager
                        .getRunningServices(1000);
                List<PackageInfo> installedPackages = packageManager
                        .getInstalledPackages(0);
                START: for (int i = 0, ilen = runningServices.size(); i < ilen; i++) {
                    for (int j = 0, jlen = installedPackages.size(); j < jlen; j++) {
                        final RunningServiceInfo runningServiceInfo = runningServices
                                .get(i);
                        final PackageInfo packageInfo = installedPackages
                                .get(j);
                        if (runningServiceInfo.uid == packageInfo.applicationInfo.uid) {
                            mapArray.append(packageInfo, runningServiceInfo);
                            continue START;
                        }
                    }
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
    private PackageManager packageManager;
    private Utils.BackgroundTask backgroundTask;
    private MapArray<PackageInfo, ActivityManager.RunningServiceInfo> mapArray = new MapArray<PackageInfo, ActivityManager.RunningServiceInfo>();

    private LayoutInflater layoutInflater;
}

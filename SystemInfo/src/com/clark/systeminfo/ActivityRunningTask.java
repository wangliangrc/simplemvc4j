package com.clark.systeminfo;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ActivityRunningTask extends ListActivity {

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return runningTasks.size();
        }

        @Override
        public Object getItem(int position) {
            return runningTasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        super.onCreate(savedInstanceState);
        runningTasks = activityManager.getRunningTasks(1000);
        setListAdapter(new MyAdapter());
    }

    private ActivityManager activityManager;
    private List<RunningTaskInfo> runningTasks;
}

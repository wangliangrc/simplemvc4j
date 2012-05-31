package com.clark.systeminfo;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.clark.func.OverscrollUtils;

public class ActivityApplications extends ListActivity implements
        OnItemClickListener {

    private class GetAllApplications extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            packageInfos.clear();
            packageInfos.addAll(packageManager
                    .getInstalledPackages(PackageManager.GET_ACTIVITIES
                            | PackageManager.GET_CONFIGURATIONS
                            | PackageManager.GET_GIDS
                            | PackageManager.GET_INSTRUMENTATION
                            | PackageManager.GET_INTENT_FILTERS
                            | PackageManager.GET_META_DATA
                            | PackageManager.GET_PERMISSIONS
                            | PackageManager.GET_PROVIDERS
                            | PackageManager.GET_RECEIVERS
                            | PackageManager.GET_RESOLVED_FILTER
                            | PackageManager.GET_SERVICES
                            | PackageManager.GET_SHARED_LIBRARY_FILES
                            | PackageManager.GET_SIGNATURES));
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((BaseAdapter) getListAdapter()).notifyDataSetInvalidated();
            progressDialog = ProgressDialog.show(ActivityApplications.this,
                    "注意", "正在加载……", false);
        }

        private ProgressDialog progressDialog;
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return packageInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return packageInfos.get(position);
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
        PackageInfo info = packageInfos.get(position);
        Intent intent = new Intent(getApplicationContext(),
                ActivityApplicationDetail.class);
        intent.putExtra("info", info);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        packageManager = getPackageManager();
        layoutInflater = getLayoutInflater();
        super.onCreate(savedInstanceState);
        ListView listView = getListView();
        OverscrollUtils.initAbsListView(listView);
        setListAdapter(new MyAdapter());
        listView.setOnItemClickListener(this);
        if (savedInstanceState == null) {
            new GetAllApplications().execute();
        } else {
            ((BaseAdapter) getListAdapter()).notifyDataSetInvalidated();
            packageInfos = savedInstanceState.getParcelableArrayList("infos");
            ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("infos", packageInfos);
    }

    private PackageManager packageManager;

    private ArrayList<PackageInfo> packageInfos = new ArrayList<PackageInfo>();

    private LayoutInflater layoutInflater;
}

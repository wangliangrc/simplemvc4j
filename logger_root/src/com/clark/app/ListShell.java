package com.clark.app;

import java.io.File;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clark.logger.R;
import coms.Shell;

public class ListShell extends Shell implements OnMenuItemClickListener,
        OnClickListener {
    private Set<String> paths;
    private Adapter adapter;
    private String[] names;
    private File[] files;

    @SuppressWarnings("unchecked")
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        final Intent intent = getIntent();
        paths = (Set<String>) intent.getSerializableExtra("list");
        if (paths == null) {
            Toast.makeText(getApplicationContext(), "无任何数据!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final int size = paths.size();
        if (size == 0) {
            Toast.makeText(getApplicationContext(), "没有可用的 Log 文件!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        files = new File[size];
        names = new String[size];
        int index = 0;
        for (String s : paths) {
            files[index] = new File(s);
            names[index] = files[index].getName();
            index++;
        }
        adapter = new Adapter();
        final ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(files[position]),
                        "text/plain");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);
        final MenuItem item = menu.findItem(R.id.delete_all);
        item.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.delete_all:
            deleteAll();
            return true;

        default:
            return false;
        }
    }

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSelf());
        builder.setTitle("主意");
        final StringBuilder stringBuilder = new StringBuilder("您将要删除:\n");
        for (int i = 0, len = names.length; i < len; ++i) {
            stringBuilder.append("\t").append(names[i]).append("\n");
        }
        stringBuilder.append("是否继续？");
        builder.setMessage(stringBuilder.toString());
        builder.setPositiveButton(android.R.string.ok, this);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    public void onClick(DialogInterface dialog, int which) {
        adapter.notifyDataSetInvalidated();
        for (File file : files) {
            file.delete();
        }
        files = new File[0];
        names = new String[0];
        paths.clear();
        adapter.notifyDataSetChanged();
    }

    private class Adapter extends BaseAdapter {

        public int getCount() {
            return paths.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.simple_list_item_1, parent, false);
            }
            final TextView view = (TextView) convertView
                    .findViewById(R.id.text1);
            view.setText(names[position]);
            return convertView;
        }
    }

}

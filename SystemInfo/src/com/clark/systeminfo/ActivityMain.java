package com.clark.systeminfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ActivityMain extends Activity implements OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
        mItems = getResources().getStringArray(R.array.main_list_items);
        mListAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mItems);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(getApplicationContext(),
                        ActivityDisplay.class));
                break;

            case 1:
                startActivity(new Intent(getApplicationContext(),
                        ActivityFileSystem.class));
                break;

            case 2:
                startActivity(new Intent(getApplicationContext(),
                        ActivityApplications.class));
                break;

            case 3:
                startActivity(new Intent(getApplicationContext(),
                        ActivityTask.class));
                break;

            case 4:
                startActivity(new Intent(getApplicationContext(),
                        ActivityBuild.class));
                break;

            case 5:
                startActivity(new Intent(getApplicationContext(),
                        ActivitySetting.class));
                break;
        }
    }

    private ListView mListView;
    private BaseAdapter mListAdapter;
    private String[] mItems;
}
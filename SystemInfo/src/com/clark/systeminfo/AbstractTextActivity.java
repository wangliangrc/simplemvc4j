package com.clark.systeminfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clark.func.OverscrollUtils;

public abstract class AbstractTextActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        mTextView = (TextView) findViewById(R.id.text);
        OverscrollUtils.initScrollView((ScrollView) mTextView.getParent());
    }

    protected TextView mTextView;
}

package com.clark.android.test;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clark.android.BaseActivity;
import com.clark.android.ListenerAdapter;
import com.clark.android.R;
import com.clark.android.annotation.IntentExtra;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public class Activity01 extends BaseActivity {
    @Override
    protected ListenerAdapter getListenerAdapter() {
        return new ListenerAdapter() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button:
                        startActivity(new Intent().setClass(
                                getApplicationContext(), Activity01.class)
                                .putExtra("xxxx",
                                        (int) (Math.random() * 1000)));
                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected int layoutResId() {
        return R.layout.main;
    }

    @ViewProperty(R.id.text)
    public TextView textView;

    @IntentExtra("xxxx")
    public int testInteger;

    @ViewProperty(value = R.id.button, listeners = { ViewListener.ON_CLICK })
    public Button button;

    @Override
    protected void onComeIntoBeing() {
        textView.setText("" + testInteger);
    }

}

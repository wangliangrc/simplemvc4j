package com.clark.android.test;

import static com.clark.func.Functions.println;

import java.util.Arrays;

import android.widget.TextView;

import com.clark.android.ListenerAdapter;
import com.clark.android.R;
import com.clark.android.SimpleActivity;
import com.clark.android.annotation.SaveInstance;
import com.clark.android.annotation.ViewProperty;

public class Activity01 extends SimpleActivity {
    @Override
    protected ListenerAdapter getListenerAdapter() {
        return null;
    }

    @Override
    protected int layoutResId() {
        return R.layout.main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        print();

        i = 100;
        iArray = new int[] { 100, 10 };
        iInteger = new Integer(100);

        l = 10000;
        lLong = new Long(10001);
        lArray = new long[] { 10000, 10001 };
    }

    @Override
    protected void onStop() {
        super.onStop();
        StringBuilder builder = new StringBuilder();
        builder.append("i = ").append(i).append("\n");
        builder.append("iArray = ").append(Arrays.toString(iArray))
                .append("\n");
        builder.append("iInteger = ").append(iInteger).append("\n");

        builder.append("l = ").append(l).append("\n");
        builder.append("lLong = ").append(lLong).append("\n");
        builder.append("lArray = ").append(Arrays.toString(lArray))
                .append("\n");
        println(builder.toString());
    }

    private void print() {
        StringBuilder builder = new StringBuilder();
        builder.append("i = ").append(i).append("\n");
        builder.append("iArray = ").append(Arrays.toString(iArray))
                .append("\n");
        builder.append("iInteger = ").append(iInteger).append("\n");

        builder.append("l = ").append(l).append("\n");
        builder.append("lLong = ").append(lLong).append("\n");
        builder.append("lArray = ").append(Arrays.toString(lArray))
                .append("\n");

        textView.setText(builder.toString());
    }

    @SaveInstance
    public int i;

    @SaveInstance
    public int[] iArray;

    @SaveInstance
    public Integer iInteger;

    @SaveInstance
    public long l;

    @SaveInstance
    public Long lLong;

    @SaveInstance
    public long[] lArray;

    @ViewProperty(R.id.text)
    public TextView textView;

    @Override
    protected void onInitialize() {
        
    }
}
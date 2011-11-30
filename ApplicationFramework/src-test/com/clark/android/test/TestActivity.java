package com.clark.android.test;

import static com.clark.func.Functions.println;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;

import com.clark.android.Activity;
import com.clark.android.R;
import com.clark.android.annotation.AfterAttachedWindow;
import com.clark.android.annotation.BeforeDetachedWindow;
import com.clark.android.annotation.ViewListener;
import com.clark.android.annotation.ViewProperty;

public class TestActivity extends Activity {

    @Override
    public void onClick(View v) {
        if (v == button) {
            println("你点击了Button！");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        println("触发长按事件！");
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected int layoutResId() {
        return R.layout.main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        println(button);
    }

    @ViewProperty(value = R.id.test, listeners = { ViewListener.ON_CLICK,
            ViewListener.ON_LONG_CLICK })
    public Button button;

    @AfterAttachedWindow
    @BeforeDetachedWindow
    public void testAttachedWindow() {
        println("testAttachedWindow");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onMovedToScrapHeap(View view) {
        // TODO Auto-generated method stub
        
    }
}
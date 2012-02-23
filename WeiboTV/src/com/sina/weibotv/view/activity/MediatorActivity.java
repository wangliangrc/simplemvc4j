package com.sina.weibotv.view.activity;

import static com.clark.mvc.MultiCore.registerView;
import static com.clark.mvc.MultiCore.removeView;
import android.app.Activity;
import android.view.KeyEvent;

public class MediatorActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        registerView(this);
    }

    @Override
    protected void onPause() {
        removeView(this);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            default:
                return super.onKeyDown(keyCode, event);
        }
    }
}

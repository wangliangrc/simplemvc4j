
package com.baidu.aiu.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.aiu.AIU;
import com.baidu.aiu.log.LoggerFactory;

public class FloatWindowManager implements OnClickListener {
    private static FloatWindowManager instance;

    public synchronized static FloatWindowManager getInstance() {
        if (instance == null) {
            instance = new FloatWindowManager();
        }
        return instance;
    }

    public Context mContext;

    private WindowManager wm = null;
    private WindowManager.LayoutParams wmLittleParams = null;
    private WindowManager.LayoutParams wmBigParams = null;

    private MoveTextView mLogView = null;
    private LinearLayout mOutContainer = null;
    private ScrollView mScrollView = null;
    private TextView mBigLogView = null;
    private LinearLayout mContainer = null;

    private Button mUpdateButton = null;
    private Button mResetButton = null;

    private CharSequence mLogs = null;

    private static boolean isAdded = false;

    private View showView = null;

    @SuppressWarnings("deprecation")
    public FloatWindowManager() {
        this.mContext = AIU.getsApplicationContext();
        mContainer = new LinearLayout(mContext);
        mOutContainer = new LinearLayout(mContext);
        mOutContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        mOutContainer.setOrientation(LinearLayout.VERTICAL);

        mLogView = new MoveTextView(mContext);
        LayoutParams pv = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mLogView.setLayoutParams(pv);
        mLogView.setGravity(Gravity.CENTER);
        mLogView.setOnClickListener(this);
        mLogView.setTextColor(mContext.getResources().getColor(android.R.color.white));
        mLogView.setBackgroundResource(android.R.color.black);
        wm = (WindowManager) mContext.getSystemService("window");
        wmLittleParams = new WindowManager.LayoutParams();
        wmLittleParams.gravity = Gravity.TOP | Gravity.LEFT;
        wmLittleParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmLittleParams.format = PixelFormat.RGBA_8888;
        wmLittleParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmLittleParams.x = 0;
        wmLittleParams.y = 0;
        wmLittleParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmLittleParams.height = 60;
        mLogView.setWindowLayoutParams(wmLittleParams);

        wmBigParams = new WindowManager.LayoutParams();
        wmBigParams.gravity = Gravity.CENTER | Gravity.CENTER;
        wmBigParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmBigParams.format = PixelFormat.RGBA_8888;
        wmBigParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmBigParams.width = wm.getDefaultDisplay().getWidth() - 50;
        wmBigParams.height = wm.getDefaultDisplay().getHeight() / 2;

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.FILL_PARENT);
        params.weight = 1;
        mUpdateButton = new Button(mContext);
        mUpdateButton.setLayoutParams(params);
        mUpdateButton.setText("升级");
        mResetButton = new Button(mContext);
        mResetButton.setLayoutParams(params);
        mResetButton.setText("重置");

        mContainer.setOrientation(LinearLayout.HORIZONTAL);

        mBigLogView = new TextView(mContext);
        mBigLogView.setTextColor(mContext.getResources().getColor(android.R.color.white));
        mBigLogView.setBackgroundResource(android.R.color.black);
        mBigLogView.setOnClickListener(this);
        mBigLogView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        mContainer.addView(mUpdateButton);
        mContainer.addView(mResetButton);

        mScrollView = new ScrollView(mContext);
        mScrollView.setFillViewport(true);
        mBigLogView.setOnClickListener(this);
        mScrollView.setBackgroundResource(android.R.color.black);
        LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        p.weight = 1;
        mScrollView.setLayoutParams(p);
        mScrollView.addView(mBigLogView);

        mOutContainer.addView(mScrollView);
        mOutContainer.addView(mContainer);

        initButtonListener();

    }

    public void setText(String text) {
        mLogs = (mLogs == null) ? Html
                .fromHtml("<font color=\"#ff0000\">$</font> " + text)
                : Html.fromHtml(Html.toHtml((Spanned) mLogs) + "<font color=\"#ff0000\">$</font> "
                        + text);
        mScrollView.postDelayed((new Runnable() {
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }), 20);
        mLogView.setText("   " + text + "   ");
        mBigLogView.setText(mLogs);
    }

    public void show() {
        if (!isAdded) {
            wm.addView(mLogView, wmLittleParams);
            showView = mLogView;
            isAdded = true;
        }
    }

    public void dismiss() {
        if (showView != null) {
            if (showView == mLogView) {
                wm.removeView(mLogView);
            } else {
                wm.removeView(mOutContainer);
            }
        }
        isAdded = false;
    }
    
    public void disableButton(){
        AIU.getUIHandler().post(new Runnable(){

            @Override
            public void run() {
                mUpdateButton.setEnabled(false);
                mResetButton.setEnabled(false);
            }
            
        });
        
    }

    private void initButtonListener() {
        mUpdateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Baidu", "" + v.toString());
                LoggerFactory.getLogger().d("发起升级");
                Intent intent = new Intent();
                intent.setAction("com.baidu.aiu.action.UPDATE");
                mContext.startService(intent);
            }
        });

        mResetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Baidu", "" + v.toString());
                LoggerFactory.getLogger().d("重置应用");
                AIU.getCachedApkFile().delete();
                AIU.getLoadedApkFile().delete();
                UpdateUtils.setAppName(mContext, "app0");
                disableButton();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.d("Baidu", "" + v.toString());
        if (v == mLogView) {
            wm.removeView(mLogView);
            wm.addView(mOutContainer, wmBigParams);
            showView = mOutContainer;
        } else {
            wm.removeView(mOutContainer);
            wm.addView(mLogView, wmLittleParams);
            showView = mLogView;
        }
    }

}

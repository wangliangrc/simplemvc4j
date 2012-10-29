package coms;

import java.util.Set;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;

public final class Activity extends android.app.Activity {
    static final String TAG = Activity.class.getSimpleName();
    static final String KEY_BUNDLE_SHELL = "KEY.COMS.SHELL";

    private Application mApplication;
    private Shell mShell;

    /**
     * 处理 Intent
     */
    private void handleIntent() {
        final Intent intent = getIntent();
        if (mShell == null) {
            try {
                final String className;
                final Bundle extras = intent.getExtras();
                final String action = intent.getAction();
                final Set<String> categories = intent.getCategories();

                if (action != null
                        && action.equals(Intent.ACTION_MAIN)
                        && categories != null
                        && categories.contains(Intent.CATEGORY_LAUNCHER)
                        && (extras == null || !extras
                                .containsKey(KEY_BUNDLE_SHELL))) {
                    final ActivityInfo activityInfo = getPackageManager()
                            .getActivityInfo(getComponentName(),
                                    PackageManager.GET_META_DATA);
                    className = activityInfo.metaData.getString("main");
                } else {
                    className = intent.getStringExtra(KEY_BUNDLE_SHELL);
                }
                mShell = (Shell) Class.forName(className).newInstance();
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
        if (mShell == null) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mShell != null) {
            mShell.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onAttachedToWindow() {
        if (mShell != null) {
            mShell.onAttachedToWindow();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mShell != null) {
            mShell.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mShell != null) {
            mShell.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onContentChanged() {
        if (mShell != null) {
            mShell.onContentChanged();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean onContextItemSelected = false;
        if (mShell != null) {
            onContextItemSelected = mShell.onContextItemSelected(item);
        }
        return !onContextItemSelected ? super.onContextItemSelected(item)
                : onContextItemSelected;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        if (mShell != null) {
            mShell.onContextMenuClosed(menu);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handleIntent();
        if (mShell != null) {
            mShell.bind(this);
        }
        super.onCreate(savedInstanceState);
        mApplication = (Application) getApplicationContext();
        mApplication.attach(this);
        if (mShell != null) {
            mShell.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (mShell != null) {
            mShell.onCreateContextMenu(menu, v, menuInfo);
        }
    }

    @Override
    public CharSequence onCreateDescription() {
        CharSequence res = null;
        if (mShell != null) {
            res = mShell.onCreateDescription();
        }
        return res == null ? super.onCreateDescription() : res;
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Dialog res = null;
        if (mShell != null) {
            res = mShell.onCreateDialog(id, args);
        }
        return res == null ? super.onCreateDialog(id, args) : res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean onCreateOptionsMenu = false;
        if (mShell != null) {
            onCreateOptionsMenu = mShell.onCreateOptionsMenu(menu);
        }
        return onCreateOptionsMenu ? onCreateOptionsMenu : super
                .onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        final boolean onCreatePanelMenu = super.onCreatePanelMenu(featureId,
                menu);
        return onCreatePanelMenu ? onCreatePanelMenu : mShell
                .onCreatePanelMenu(featureId, menu);
    }

    @Override
    public View onCreatePanelView(int featureId) {
        View res = null;
        if (mShell != null) {
            res = mShell.onCreatePanelView(featureId);
        }
        return res == null ? super.onCreatePanelView(featureId) : res;
    }

    @Override
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        boolean res = false;
        if (mShell != null) {
            res = mShell.onCreateThumbnail(outBitmap, canvas);
        }
        return res ? res : super.onCreateThumbnail(outBitmap, canvas);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View res = null;
        if (mShell != null) {
            res = mShell.onCreateView(name, context, attrs);
        }
        return res == null ? super.onCreateView(name, context, attrs) : res;
    }

    @Override
    protected void onDestroy() {
        if (mShell != null) {
            mShell.onDestroy();
        }
        super.onDestroy();
        if (mShell != null) {
            mShell.unbind();
        }
        mApplication.detach(this);
    }

    @Override
    public void onDetachedFromWindow() {
        if (mShell != null) {
            mShell.onDetachedFromWindow();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean onKeyDown = false;
        if (mShell != null) {
            onKeyDown = mShell.onKeyDown(keyCode, event);
        }
        return onKeyDown ? onKeyDown : super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        boolean res = false;
        if (mShell != null) {
            res = mShell.onKeyLongPress(keyCode, event);
        }
        return res ? res : super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        boolean res = false;
        if (mShell != null) {
            res = mShell.onKeyMultiple(keyCode, repeatCount, event);
        }
        return res ? res : super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean onKeyUp = false;
        if (mShell != null) {
            onKeyUp = mShell.onKeyUp(keyCode, event);
        }
        return onKeyUp ? onKeyUp : super.onKeyUp(keyCode, event);
    }

    @Override
    public void onLowMemory() {
        if (mShell != null) {
            mShell.onLowMemory();
        }
        super.onLowMemory();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean onMenuItemSelected = false;
        if (mShell != null) {
            onMenuItemSelected = mShell.onMenuItemSelected(featureId, item);
        }
        return onMenuItemSelected ? onMenuItemSelected : super
                .onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        boolean onMenuOpened = false;
        if (mShell != null) {
            onMenuOpened = mShell.onMenuOpened(featureId, menu);
        }
        return onMenuOpened ? onMenuOpened : super
                .onMenuOpened(featureId, menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (mShell != null) {
            mShell.onNewIntent(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean onOptionsItemSelected = false;
        if (mShell != null) {
            onOptionsItemSelected = mShell.onOptionsItemSelected(item);
        }
        return onOptionsItemSelected ? onOptionsItemSelected : super
                .onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        if (mShell != null) {
            mShell.onOptionsMenuClosed(menu);
        }
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
        if (mShell != null) {
            mShell.onPanelClosed(featureId, menu);
        }
    }

    @Override
    protected void onPause() {
        if (mShell != null) {
            mShell.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mShell != null) {
            mShell.onPostCreate(savedInstanceState);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mShell != null) {
            mShell.onPostResume();
        }
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        super.onPrepareDialog(id, dialog, args);
        if (mShell != null) {
            mShell.onPrepareDialog(id, dialog, args);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean onPrepareOptionsMenu = false;
        if (mShell != null) {
            onPrepareOptionsMenu = mShell.onPrepareOptionsMenu(menu);
        }
        return onPrepareOptionsMenu ? onPrepareOptionsMenu : super
                .onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        boolean onPreparePanel = false;
        if (mShell != null) {
            onPreparePanel = mShell.onPreparePanel(featureId, view, menu);
        }
        return onPreparePanel ? onPreparePanel : super.onPreparePanel(
                featureId, view, menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mShell != null) {
            mShell.onRestart();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mShell != null) {
            mShell.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mShell != null) {
            mShell.onResume();
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        Object res = null;
        if (mShell != null) {
            res = mShell.onRetainNonConfigurationInstance();
        }
        return res == null ? super.onRetainNonConfigurationInstance() : res;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mShell != null) {
            mShell.onSaveInstanceState(outState);
        }
    }

    @Override
    public boolean onSearchRequested() {
        boolean onSearchRequested = false;
        if (mShell != null) {
            onSearchRequested = mShell.onSearchRequested();
        }
        return onSearchRequested ? onSearchRequested : super
                .onSearchRequested();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mShell != null) {
            mShell.onStart();
        }
    }

    @Override
    protected void onStop() {
        if (mShell != null) {
            mShell.onStop();
        }
        super.onStop();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mShell != null) {
            mShell.onTitleChanged(title, color);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = false;
        if (mShell != null) {
            onTouchEvent = mShell.onTouchEvent(event);
        }
        return onTouchEvent ? onTouchEvent : super.onTouchEvent(event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        boolean res = false;
        if (mShell != null) {
            res = mShell.onTrackballEvent(event);
        }
        return res ? res : super.onTrackballEvent(event);
    }

    @Override
    public void onUserInteraction() {
        if (mShell != null) {
            mShell.onUserInteraction();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        if (mShell != null) {
            mShell.onUserLeaveHint();
        }
    }

    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
        super.onWindowAttributesChanged(params);
        if (mShell != null) {
            mShell.onWindowAttributesChanged(params);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (mShell != null) {
            mShell.onWindowFocusChanged(hasFocus);
        }
    }

    /**
     * 退出整个进程
     */
    public void exit() {
        mApplication.exit();
    }
}

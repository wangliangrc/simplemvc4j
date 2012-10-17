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
    private AbstractComponent mShell;

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
                        && categories.contains(
                                Intent.CATEGORY_LAUNCHER)
                        && (extras == null || !extras
                                .containsKey(KEY_BUNDLE_SHELL))) {
                    final ActivityInfo activityInfo = getPackageManager()
                            .getActivityInfo(getComponentName(),
                                    PackageManager.GET_META_DATA);
                    className = activityInfo.metaData.getString("main");
                } else {
                    className = intent.getStringExtra(KEY_BUNDLE_SHELL);
                }
                mShell = (AbstractComponent) Class.forName(className)
                        .newInstance();
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
        final boolean onContextItemSelected = super.onContextItemSelected(item);
        return !onContextItemSelected ? mShell.onContextItemSelected(item)
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
        return mShell.onCreateDescription();
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        return mShell.onCreateDialog(id, args);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final boolean onCreateOptionsMenu = super.onCreateOptionsMenu(menu);
        return onCreateOptionsMenu ? onCreateOptionsMenu : mShell
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
        return mShell.onCreatePanelView(featureId);
    }

    @Override
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return mShell.onCreateThumbnail(outBitmap, canvas);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return mShell.onCreateView(name, context, attrs);
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
        final boolean onKeyDown = super.onKeyDown(keyCode, event);
        return onKeyDown ? onKeyDown : mShell.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return mShell.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return mShell.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        final boolean onKeyUp = super.onKeyUp(keyCode, event);
        return onKeyUp ? onKeyUp : mShell.onKeyUp(keyCode, event);
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
        final boolean onMenuItemSelected = super.onMenuItemSelected(featureId,
                item);
        return onMenuItemSelected ? onMenuItemSelected : mShell
                .onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        final boolean onMenuOpened = super.onMenuOpened(featureId, menu);
        return onMenuOpened ? onMenuOpened : mShell.onMenuOpened(featureId,
                menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (mShell != null) {
            mShell.onNewIntent(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final boolean onOptionsItemSelected = super.onOptionsItemSelected(item);
        return onOptionsItemSelected ? onOptionsItemSelected : mShell
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
        final boolean onPrepareOptionsMenu = super.onPrepareOptionsMenu(menu);
        return onPrepareOptionsMenu ? onPrepareOptionsMenu : mShell
                .onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        final boolean onPreparePanel = super.onPreparePanel(featureId, view,
                menu);
        return onPreparePanel ? onPreparePanel : mShell.onPreparePanel(
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
        return mShell.onRetainNonConfigurationInstance();
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
        final boolean onSearchRequested = mShell.onSearchRequested();
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
        final boolean onTouchEvent = super.onTouchEvent(event);
        return onTouchEvent ? onTouchEvent : mShell.onTouchEvent(event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return mShell.onTrackballEvent(event);
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

    public void exit() {
        mApplication.exit();
    }
}

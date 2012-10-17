package coms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
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
            final String className = intent.getStringExtra(KEY_BUNDLE_SHELL);
            try {
                mShell = (AbstractComponent) Class.forName(className).newInstance();
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
        mShell.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onApplyThemeResource(Theme theme, int resid, boolean first) {
        super.onApplyThemeResource(theme, resid, first);
        mShell.onApplyThemeResource(theme, resid, first);
    }

    @Override
    public void onAttachedToWindow() {
        mShell.onAttachedToWindow();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mShell.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShell.onConfigurationChanged(newConfig);
    }

    @Override
    public void onContentChanged() {
        mShell.onContentChanged();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final boolean onContextItemSelected = super.onContextItemSelected(item);
        return !onContextItemSelected ? mShell.onContextItemSelected(item) : onContextItemSelected;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        mShell.onContextMenuClosed(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (Application) getApplicationContext();
        mApplication.attach(this);
        handleIntent();
        mShell.bind(this);
        mShell.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        mShell.onCreateContextMenu(menu, v, menuInfo);
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
        return onCreateOptionsMenu ? onCreateOptionsMenu : mShell.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        final boolean onCreatePanelMenu = super.onCreatePanelMenu(featureId, menu);
        return onCreatePanelMenu ? onCreatePanelMenu : mShell.onCreatePanelMenu(featureId, menu);
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
        mShell.onDestroy();
        super.onDestroy();
        mShell.unbind();
        mApplication.detach(this);
    }

    @Override
    public void onDetachedFromWindow() {
        mShell.onDetachedFromWindow();
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
        mShell.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        final boolean onMenuItemSelected = super.onMenuItemSelected(featureId, item);
        return onMenuItemSelected ? onMenuItemSelected : mShell.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        final boolean onMenuOpened = super.onMenuOpened(featureId, menu);
        return onMenuOpened ? onMenuOpened : mShell.onMenuOpened(featureId, menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mShell.onNewIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final boolean onOptionsItemSelected = super.onOptionsItemSelected(item);
        return onOptionsItemSelected ? onOptionsItemSelected : mShell.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        mShell.onOptionsMenuClosed(menu);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
        mShell.onPanelClosed(featureId, menu);
    }

    @Override
    protected void onPause() {
        mShell.onPause();
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mShell.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mShell.onPostResume();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        super.onPrepareDialog(id, dialog, args);
        mShell.onPrepareDialog(id, dialog, args);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final boolean onPrepareOptionsMenu = super.onPrepareOptionsMenu(menu);
        return onPrepareOptionsMenu ? onPrepareOptionsMenu : mShell.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        final boolean onPreparePanel = super.onPreparePanel(featureId, view, menu);
        return onPreparePanel ? onPreparePanel : mShell.onPreparePanel(featureId, view, menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mShell.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mShell.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShell.onResume();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mShell.onRetainNonConfigurationInstance();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mShell.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSearchRequested() {
        final boolean onSearchRequested = mShell.onSearchRequested();
        return onSearchRequested ? onSearchRequested : super.onSearchRequested();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mShell.onStart();
    }

    @Override
    protected void onStop() {
        mShell.onStop();
        super.onStop();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        mShell.onTitleChanged(title, color);
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
        mShell.onUserInteraction();
    }

    @Override
    protected void onUserLeaveHint() {
        mShell.onUserLeaveHint();
    }

    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
        super.onWindowAttributesChanged(params);
        mShell.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mShell.onWindowFocusChanged(hasFocus);
    }

    public void exit() {
        mApplication.exit();
    }
}

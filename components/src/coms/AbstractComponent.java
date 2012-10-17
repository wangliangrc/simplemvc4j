package coms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

public abstract class AbstractComponent {

    private Activity mActivity;

    void bind(Activity activity) {
        mActivity = activity;
    }

    void unbind() {
        mActivity = null;
    }

    public final void addContentView(View view, LayoutParams params) {
        mActivity.addContentView(view, params);
    }

    public final boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return mActivity.bindService(service, conn, flags);
    }

    public final int checkCallingOrSelfPermission(String permission) {
        return mActivity.checkCallingOrSelfPermission(permission);
    }

    public final int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return mActivity.checkCallingOrSelfUriPermission(uri, modeFlags);
    }

    public final int checkCallingPermission(String permission) {
        return mActivity.checkCallingPermission(permission);
    }

    public final int checkCallingUriPermission(Uri uri, int modeFlags) {
        return mActivity.checkCallingUriPermission(uri, modeFlags);
    }

    public final int checkPermission(String permission, int pid, int uid) {
        return mActivity.checkPermission(permission, pid, uid);
    }

    public final int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        return mActivity.checkUriPermission(uri, pid, uid, modeFlags);
    }

    public final int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
        return mActivity.checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags);
    }

    public final void clearWallpaper() throws IOException {
        mActivity.clearWallpaper();
    }

    public final void closeContextMenu() {
        mActivity.closeContextMenu();
    }

    public final void closeOptionsMenu() {
        mActivity.closeOptionsMenu();
    }

    public final Context createPackageContext(String packageName, int flags) throws NameNotFoundException {
        return mActivity.createPackageContext(packageName, flags);
    }

    public final PendingIntent createPendingResult(int requestCode, Intent data, int flags) {
        return mActivity.createPendingResult(requestCode, data, flags);
    }

    public final String[] databaseList() {
        return mActivity.databaseList();
    }

    public final boolean deleteDatabase(String name) {
        return mActivity.deleteDatabase(name);
    }

    public final boolean deleteFile(String name) {
        return mActivity.deleteFile(name);
    }

    public final void dismissDialog(int id) {
        mActivity.dismissDialog(id);
    }

    public final boolean dispatchKeyEvent(KeyEvent event) {
        return mActivity.dispatchKeyEvent(event);
    }

    public final boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return mActivity.dispatchPopulateAccessibilityEvent(event);
    }

    public final boolean dispatchTouchEvent(MotionEvent ev) {
        return mActivity.dispatchTouchEvent(ev);
    }

    public final boolean dispatchTrackballEvent(MotionEvent ev) {
        return mActivity.dispatchTrackballEvent(ev);
    }

    public final void enforceCallingOrSelfPermission(String permission, String message) {
        mActivity.enforceCallingOrSelfPermission(permission, message);
    }

    public final void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {
        mActivity.enforceCallingOrSelfUriPermission(uri, modeFlags, message);
    }

    public final void enforceCallingPermission(String permission, String message) {
        mActivity.enforceCallingPermission(permission, message);
    }

    public final void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {
        mActivity.enforceCallingUriPermission(uri, modeFlags, message);
    }

    public final void enforcePermission(String permission, int pid, int uid, String message) {
        mActivity.enforcePermission(permission, pid, uid, message);
    }

    public final void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {
        mActivity.enforceUriPermission(uri, pid, uid, modeFlags, message);
    }

    public final void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {
        mActivity.enforceUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags, message);
    }

    public final String[] fileList() {
        return mActivity.fileList();
    }

    public final View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    public final void finish() {
        mActivity.finish();
    }

    public final void finishActivity(int requestCode) {
        mActivity.finishActivity(requestCode);
    }

    public final void finishActivityFromChild(android.app.Activity child, int requestCode) {
        mActivity.finishActivityFromChild(child, requestCode);
    }

    public final void finishFromChild(android.app.Activity child) {
        mActivity.finishFromChild(child);
    }

    public final Application getApplication() {
        return mActivity.getApplication();
    }

    public final Context getApplicationContext() {
        return mActivity.getApplicationContext();
    }

    public final ApplicationInfo getApplicationInfo() {
        return mActivity.getApplicationInfo();
    }

    public final AssetManager getAssets() {
        return mActivity.getAssets();
    }

    public final Context getBaseContext() {
        return mActivity.getBaseContext();
    }

    public final File getCacheDir() {
        return mActivity.getCacheDir();
    }

    public final ComponentName getCallingActivity() {
        return mActivity.getCallingActivity();
    }

    public final String getCallingPackage() {
        return mActivity.getCallingPackage();
    }

    public final int getChangingConfigurations() {
        return mActivity.getChangingConfigurations();
    }

    public final ClassLoader getClassLoader() {
        return mActivity.getClassLoader();
    }

    public final ComponentName getComponentName() {
        return mActivity.getComponentName();
    }

    public final ContentResolver getContentResolver() {
        return mActivity.getContentResolver();
    }

    public final View getCurrentFocus() {
        return mActivity.getCurrentFocus();
    }

    public final File getDatabasePath(String name) {
        return mActivity.getDatabasePath(name);
    }

    public final File getDir(String name, int mode) {
        return mActivity.getDir(name, mode);
    }

    public final File getExternalCacheDir() {
        return mActivity.getExternalCacheDir();
    }

    public final File getExternalFilesDir(String type) {
        return mActivity.getExternalFilesDir(type);
    }

    public final File getFileStreamPath(String name) {
        return mActivity.getFileStreamPath(name);
    }

    public final File getFilesDir() {
        return mActivity.getFilesDir();
    }

    public final Intent getIntent() {
        return mActivity.getIntent();
    }

    public final Object getLastNonConfigurationInstance() {
        return mActivity.getLastNonConfigurationInstance();
    }

    public final LayoutInflater getLayoutInflater() {
        return mActivity.getLayoutInflater();
    }

    public final String getLocalClassName() {
        return mActivity.getLocalClassName();
    }

    public final Looper getMainLooper() {
        return mActivity.getMainLooper();
    }

    public final MenuInflater getMenuInflater() {
        return mActivity.getMenuInflater();
    }

    public final String getPackageCodePath() {
        return mActivity.getPackageCodePath();
    }

    public final PackageManager getPackageManager() {
        return mActivity.getPackageManager();
    }

    public final String getPackageName() {
        return mActivity.getPackageName();
    }

    public final String getPackageResourcePath() {
        return mActivity.getPackageResourcePath();
    }

    public final android.app.Activity getParent() {
        return mActivity.getParent();
    }

    public final SharedPreferences getPreferences(int mode) {
        return mActivity.getPreferences(mode);
    }

    public final int getRequestedOrientation() {
        return mActivity.getRequestedOrientation();
    }

    public final Resources getResources() {
        return mActivity.getResources();
    }

    public final SharedPreferences getSharedPreferences(String name, int mode) {
        return mActivity.getSharedPreferences(name, mode);
    }

    public final String getString(int resId, Object... formatArgs) {
        return mActivity.getString(resId, formatArgs);
    }

    public final String getString(int resId) {
        return mActivity.getString(resId);
    }

    public final Object getSystemService(String name) {
        return mActivity.getSystemService(name);
    }

    public final int getTaskId() {
        return mActivity.getTaskId();
    }

    public final CharSequence getText(int resId) {
        return mActivity.getText(resId);
    }

    public final Theme getTheme() {
        return mActivity.getTheme();
    }

    public final CharSequence getTitle() {
        return mActivity.getTitle();
    }

    public final int getTitleColor() {
        return mActivity.getTitleColor();
    }

    public final int getVolumeControlStream() {
        return mActivity.getVolumeControlStream();
    }

    public final Drawable getWallpaper() {
        return mActivity.getWallpaper();
    }

    public final int getWallpaperDesiredMinimumHeight() {
        return mActivity.getWallpaperDesiredMinimumHeight();
    }

    public final int getWallpaperDesiredMinimumWidth() {
        return mActivity.getWallpaperDesiredMinimumWidth();
    }

    public final Window getWindow() {
        return mActivity.getWindow();
    }

    public final WindowManager getWindowManager() {
        return mActivity.getWindowManager();
    }

    public final void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        mActivity.grantUriPermission(toPackage, uri, modeFlags);
    }

    public final boolean hasWindowFocus() {
        return mActivity.hasWindowFocus();
    }

    public final boolean isChild() {
        return mActivity.isChild();
    }

    public final boolean isFinishing() {
        return mActivity.isFinishing();
    }

    public final boolean isRestricted() {
        return mActivity.isRestricted();
    }

    public final boolean isTaskRoot() {
        return mActivity.isTaskRoot();
    }

    public final Cursor managedQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mActivity.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

    public final boolean moveTaskToBack(boolean nonRoot) {
        return mActivity.moveTaskToBack(nonRoot);
    }

    public final TypedArray obtainStyledAttributes(AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
        return mActivity.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
    }

    public final TypedArray obtainStyledAttributes(AttributeSet set, int[] attrs) {
        return mActivity.obtainStyledAttributes(set, attrs);
    }

    public final TypedArray obtainStyledAttributes(int resid, int[] attrs) throws NotFoundException {
        return mActivity.obtainStyledAttributes(resid, attrs);
    }

    public final TypedArray obtainStyledAttributes(int[] attrs) {
        return mActivity.obtainStyledAttributes(attrs);
    }

    public void onAttachedToWindow() {
    }

    public void onBackPressed() {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onContentChanged() {
    }

    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    public void onContextMenuClosed(Menu menu) {
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    }

    public CharSequence onCreateDescription() {
        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return false;
    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return false;
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    public void onDetachedFromWindow() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public void onLowMemory() {
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onOptionsMenuClosed(Menu menu) {
    }

    public void onPanelClosed(int featureId, Menu menu) {
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return false;
    }

    public Object onRetainNonConfigurationInstance() {
        return null;
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public void onUserInteraction() {
    }

    public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onApplyThemeResource(Theme theme, int resid, boolean first) {
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public Dialog onCreateDialog(int id, Bundle args) {
        return null;
    }

    public void onDestroy() {
    }

    public void onNewIntent(Intent intent) {
    }

    public void onPause() {
    }

    public void onPostCreate(Bundle savedInstanceState) {
    }

    public void onPostResume() {
    }

    public void onPrepareDialog(int id, Dialog dialog, Bundle args) {
    }

    public void onRestart() {
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    public void onResume() {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onTitleChanged(CharSequence title, int color) {
    }

    public void onUserLeaveHint() {
    }

    public final void openContextMenu(View view) {
        mActivity.openContextMenu(view);
    }

    public final FileInputStream openFileInput(String name) throws FileNotFoundException {
        return mActivity.openFileInput(name);
    }

    public final FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return mActivity.openFileOutput(name, mode);
    }

    public final void openOptionsMenu() {
        mActivity.openOptionsMenu();
    }

    public final SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
        return mActivity.openOrCreateDatabase(name, mode, factory);
    }

    public final void overridePendingTransition(int enterAnim, int exitAnim) {
        mActivity.overridePendingTransition(enterAnim, exitAnim);
    }

    public final Drawable peekWallpaper() {
        return mActivity.peekWallpaper();
    }

    public final void registerForContextMenu(View view) {
        mActivity.registerForContextMenu(view);
    }

    public final Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return mActivity.registerReceiver(receiver, filter, broadcastPermission, scheduler);
    }

    public final Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return mActivity.registerReceiver(receiver, filter);
    }

    public final void removeDialog(int id) {
        mActivity.removeDialog(id);
    }

    public final void removeStickyBroadcast(Intent intent) {
        mActivity.removeStickyBroadcast(intent);
    }

    public final boolean requestWindowFeature(int featureId) {
        return mActivity.requestWindowFeature(featureId);
    }

    public final void revokeUriPermission(Uri uri, int modeFlags) {
        mActivity.revokeUriPermission(uri, modeFlags);
    }

    public final void runOnUiThread(Runnable action) {
        mActivity.runOnUiThread(action);
    }

    public final void sendBroadcast(Intent intent, String receiverPermission) {
        mActivity.sendBroadcast(intent, receiverPermission);
    }

    public final void sendBroadcast(Intent intent) {
        mActivity.sendBroadcast(intent);
    }

    public final void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData,
            Bundle initialExtras) {
        mActivity.sendOrderedBroadcast(intent, receiverPermission, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public final void sendOrderedBroadcast(Intent intent, String receiverPermission) {
        mActivity.sendOrderedBroadcast(intent, receiverPermission);
    }

    public final void sendStickyBroadcast(Intent intent) {
        mActivity.sendStickyBroadcast(intent);
    }

    public final void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        mActivity.sendStickyOrderedBroadcast(intent, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public final void setContentView(int layoutResID) {
        mActivity.setContentView(layoutResID);
    }

    public final void setContentView(View view, LayoutParams params) {
        mActivity.setContentView(view, params);
    }

    public final void setContentView(View view) {
        mActivity.setContentView(view);
    }

    public final void setDefaultKeyMode(int mode) {
        mActivity.setDefaultKeyMode(mode);
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        mActivity.setFeatureDrawable(featureId, drawable);
    }

    public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        mActivity.setFeatureDrawableAlpha(featureId, alpha);
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        mActivity.setFeatureDrawableResource(featureId, resId);
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        mActivity.setFeatureDrawableUri(featureId, uri);
    }

    public final void setIntent(Intent newIntent) {
        mActivity.setIntent(newIntent);
    }

    public final void setPersistent(boolean isPersistent) {
        mActivity.setPersistent(isPersistent);
    }

    public final void setProgress(int progress) {
        mActivity.setProgress(progress);
    }

    public final void setProgressBarIndeterminate(boolean indeterminate) {
        mActivity.setProgressBarIndeterminate(indeterminate);
    }

    public final void setProgressBarIndeterminateVisibility(boolean visible) {
        mActivity.setProgressBarIndeterminateVisibility(visible);
    }

    public final void setProgressBarVisibility(boolean visible) {
        mActivity.setProgressBarVisibility(visible);
    }

    public void setRequestedOrientation(int requestedOrientation) {
        mActivity.setRequestedOrientation(requestedOrientation);
    }

    public final void setResult(int resultCode, Intent data) {
        mActivity.setResult(resultCode, data);
    }

    public final void setResult(int resultCode) {
        mActivity.setResult(resultCode);
    }

    public final void setSecondaryProgress(int secondaryProgress) {
        mActivity.setSecondaryProgress(secondaryProgress);
    }

    public void setTheme(int resid) {
        mActivity.setTheme(resid);
    }

    public void setTitle(CharSequence title) {
        mActivity.setTitle(title);
    }

    public void setTitle(int titleId) {
        mActivity.setTitle(titleId);
    }

    public void setTitleColor(int textColor) {
        mActivity.setTitleColor(textColor);
    }

    public void setVisible(boolean visible) {
        mActivity.setVisible(visible);
    }

    public final void setVolumeControlStream(int streamType) {
        mActivity.setVolumeControlStream(streamType);
    }

    public final void setWallpaper(Bitmap bitmap) throws IOException {
        mActivity.setWallpaper(bitmap);
    }

    public final void setWallpaper(InputStream data) throws IOException {
        mActivity.setWallpaper(data);
    }

    public final boolean showDialog(int id, Bundle args) {
        return mActivity.showDialog(id, args);
    }

    public final void showDialog(int id) {
        mActivity.showDialog(id);
    }

    public final void startActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    public final void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    public final void startComponent(Intent intent) {
        final Intent newIntent = fixIntent(intent);
        startActivity(newIntent);
    }

    public final void startComponentForResult(Intent intent, int requestCode) {
        final Intent newIntent = fixIntent(intent);
        startActivityForResult(newIntent, requestCode);
    }

    private Intent fixIntent(Intent intent) {
        final ComponentName component = intent.getComponent();
        final Intent newIntent = new Intent();
        newIntent.setClassName(getApplication(), component.getClassName());
        newIntent.setAction(intent.getAction());
        newIntent.setFlags(intent.getFlags());
        newIntent.setDataAndType(intent.getData(), intent.getType());
        final Set<String> categories = intent.getCategories();
        if (categories != null) {
            for (String string : categories) {
                newIntent.addCategory(string);
            }
        }
        newIntent.putExtras(intent.getExtras());
        return newIntent;
    }

    public final boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        return mActivity.startInstrumentation(className, profileFile, arguments);
    }

    public final void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        mActivity.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags);
    }

    public final void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags)
            throws SendIntentException {
        mActivity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
    }

    public final void startManagingCursor(Cursor c) {
        mActivity.startManagingCursor(c);
    }

    public final void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData, boolean globalSearch) {
        mActivity.startSearch(initialQuery, selectInitialQuery, appSearchData, globalSearch);
    }

    public final ComponentName startService(Intent service) {
        return mActivity.startService(service);
    }

    public final void stopManagingCursor(Cursor c) {
        mActivity.stopManagingCursor(c);
    }

    public final boolean stopService(Intent name) {
        return mActivity.stopService(name);
    }

    public final void takeKeyEvents(boolean get) {
        mActivity.takeKeyEvents(get);
    }

    public final void triggerSearch(String query, Bundle appSearchData) {
        mActivity.triggerSearch(query, appSearchData);
    }

    public final void unbindService(ServiceConnection conn) {
        mActivity.unbindService(conn);
    }

    public final void unregisterForContextMenu(View view) {
        mActivity.unregisterForContextMenu(view);
    }

    public final void unregisterReceiver(BroadcastReceiver receiver) {
        mActivity.unregisterReceiver(receiver);
    }

    public final void exit() {
        mActivity.exit();
    }

}

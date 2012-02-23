package com.sina.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.exception.WeiboApiException;
import com.sina.weibo.exception.WeiboIOException;
import com.sina.weibo.exception.WeiboLocationException;
import com.sina.weibo.exception.WeiboParseException;
import com.sina.weibo.models.Comment;
import com.sina.weibo.models.CommentMessage;
import com.sina.weibo.models.FavHotWord;
import com.sina.weibo.models.Group;
import com.sina.weibo.models.MBlog;
import com.sina.weibo.models.MBlogList;
import com.sina.weibo.models.Message;
import com.sina.weibo.models.MessageList;
import com.sina.weibo.models.User;
import com.sina.weibo.models.UserInfo;
import com.sina.weibo.net.DefaultDownloadCallback;
import com.sina.weibo.net.DownloadCallbackManager;
import com.sina.weibo.net.IDownloadState;
import com.sina.weibo.net.NetEngineFactory;
import com.sina.weibo.net.RPCHelper.ApiException;
import com.sina.weibo.net.RPCHelper.NoSignalException;

/**
 * @author nieyu
 *
 */
/**
 * @author nieyu
 * 
 */
public final class Utils {

    public static final String USER_FILE = "/user.dat";
    public static final String MODE_FILE = "_mode";
    public static final String HOME_FILE = "_home";
    public static final String AT_FILE = "_at";
    public static final String CM_FILE = "_commentmessage";
    public static final String MSG_FILE = "_message";
    public static final String UID_FILE = "_uid.dat";
    public static final String TOTAL_FILE = "_total";
    public static final String MYBLOG_FILE = "_myblog";
    public static final String FAVHOTWORD_FILE = "favhotword";
    public static final String FAVORITE_FILE = "_favorite";
    public static final String BLOG_FILE = "/blog.dat";
    public static final String LOOKAROUND_FILE = "/lookaround";
    public static final String HOTFORWARD_FILE = "/hotforward";
    public static final String TOPUSER_FILE = "/topuser";
    public static final String USERINFO_FILE = "_myuserinfo";
    public static final String PREFS_NAME = "weibo";
    public static final String PREFS_USERNAME = "username";
    public static final String PREFS_PASSWORD = "password";
    public static final String PREFS_BIND_UC = "bind_uc";
    public static final String PREFS_CAN_PROMPT = "can_prompt";
    public static final String PREFS_GSID = "gsid";
    public static final String PREFS_UID = "uid";
    public static final String PREFS_ISFIRSTLOGIN = "isfirst";
    public static final String PREFS_CLEANPOR = "cleanportrait";
    public static final String PREFS_CLEANPOR_STATUS = "cleanportraitstatus";
    private static Pattern webpattern;
    private static Pattern webpattern2;
    private static Pattern sharppattern;
    private static Pattern filenamepattern;
    private static SimpleDateFormat sdf;
    static int CACHE_LIMITATION = 50;
    // delete weibo
    public static final int DELETE_MBLOG = 0;

    public static final int DELETE_FAV = 1;

    // LOG IN STATUS
    private static boolean isLogin = true;

    // Des encode key
    private static String DES_KEY = "sinachina";

    public static final String CAMERA_FILE_CACHE_PREX = "tmp_bmp_";

    private static Pattern atpattern;

    private static Pattern allPattern;

    // 不要每次都创建一个对象
    private static RectF rrbRectf = new RectF();

    private static Path rrbRath = new Path();

    private static final int RRB_DEFAULT_SIZE = 6;

    private static Paint nbPaint = new Paint();

    // Format data , time is millisecond
    // public static long millisecond8hours = 28800000;
    public static long millisecond8hours = 0;

    private static File externalCacheDir;

    public static final String DIR_LARGE_IMAGE = "/large_imgs";

    private static SinaWeiboDB mDB;

    private static Boolean mIsEnPlatform;

    private static Boolean mIsSupportGroup;

    private static int mWindowsWidth = 0;
    // //头像or预览图，图片内存缓存map
    // public static Map<String, Bitmap> mPortraitCacheMap = new HashMap<String,
    // Bitmap>();
    // public static Map<String, Bitmap> mPreviewPicMap = new HashMap<String,
    // Bitmap>();
    // private static Queue<String> mPortraitQueue = new LinkedList<String>();
    // private static Queue<String> mPreviewPicQueue = new LinkedList<String>();
    //
    // private static final int MaxCacheMapSize = 5;
    // private static final int MAX_PREVIEW_BITMAP_CACHE_SIZE = 5;
    // 存储模式
    public static final int MODE_SAVE_PORTRAIT = 0;
    public static final int MODE_SAVE_PREVIEW_BMP = 1;
    public static final int MODE_SAVE_ORIGNAL_BMP = 2;

    public static Object lock = new Object();
    public static boolean mIsChangeLanguage = true;
    private static Boolean mIsEnLanguage = null;

    private static final String VERSION_PATTERN = "(\\d+)\\.(\\d)\\.(\\d)";
    private static final long MAX_CACHE_LIMITION = 4 * 1024 * 1024;
    public static final long MAX_SD_FOLDER_LIMITION = 2000;

    // private static Boolean sCanPromptUc = null;
    private static boolean mFistOpenUrl = true;

    private static Activity mCurrentActivity = null;

    private static abstract class MyClicker extends ClickableSpan {

        MyClicker(Context context) {
            mContext = context;
        }

        private Context mContext;

        /**
         * Makes the text underlined and in the link color.
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            if (highLightTextColor == -1) {
                highLightTextColor = Theme.getInstance(mContext)
                        .getColorFromIdentifier(
                                R.color.blog_item_content_light_text);
            }
            ds.setColor(highLightTextColor);
            ds.setUnderlineText(true);
        }
    }

    private static class AtClicker extends MyClicker {
        private Context mContext;
        private String nickName;

        public void onClick(View widget) {
            Intent i = new Intent(mContext, UserInfoActivity2.class);
            /*
             * boolean isMe = nickName.equals(mContext.getString(R.string.me));
             * if (isMe && StaticInfo.mUser != null) { nickName =
             * StaticInfo.mUser.nick; }
             */
            i.putExtra("nick", nickName);
            i.putExtra("myuid", StaticInfo.mUser.uid);
            i.putExtra(UserInfoActivity2.KEY_USER_VIP, true);
            mContext.startActivity(i);
        }

        public AtClicker(Context cntx, String nickName) {
            super(cntx);
            mContext = cntx;
            this.nickName = nickName;
        }
    }

    private static class TopicClicker extends MyClicker {
        private String topic;
        private Context mContext;

        public void onClick(View arg0) {
            Intent i = new Intent(mContext, UserTopicAttentionList.class);
            i.putExtra("query", topic);
            mContext.startActivity(i);
        }

        public TopicClicker(Context context, String topic) {
            super(context);
            mContext = context;
            this.topic = topic;
        }
    }

    private static class UrlClicker extends MyClicker {
        private String urlPath;
        private Context mContext;
        private String gsid;
        private static final String URL_PREX = "http://weibo.cn/dpool/ttt/sinaurl.php?vt=3&u=%s&gsid=%s";

        public void onClick(View widget) {
            /**
             * Intent i = new Intent(Intent.ACTION_VIEW); Uri uri =
             * Uri.parse(String.format(URL_PREX, URLEncoder .encode(urlPath),
             * gsid)); i.setData(uri); mContext.startActivity(i);
             */
            String url = String.format(URL_PREX, URLEncoder.encode(urlPath),
                    gsid);
            openUrl(mContext, url);
        }

        public UrlClicker(Context context, String urlPath, String gsid) {
            super(context);
            mContext = context;
            this.urlPath = urlPath;
            this.gsid = gsid;
        }
    }

    public synchronized static boolean canPromptUc(Context ctx) {
        if (mFistOpenUrl) { // 第一次打开url链接
            SharedPreferences pref = ctx.getSharedPreferences(PREFS_BIND_UC, 0);
            boolean can = pref.getBoolean(PREFS_CAN_PROMPT, true);
            if (can) { // 用户未对uc进行设置 返回true
                Editor editor = pref.edit();
                editor.putBoolean(Utils.PREFS_CAN_PROMPT, false);
                editor.commit();
            }
            mFistOpenUrl = false;
            return can;
        } else {
            return false;
        }
    }

    public static void promptUc(final Context ctx, final String url) {
        SharedPreferences pref = SettingsPref.getDefaultSharedPreference(ctx);
        final Editor editor = pref.edit();
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.bind_uc_confirm)
                .setIcon(R.drawable.ic_help)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editor.putBoolean(SettingsPref.KEY_BIND_UC,
                                        true);
                                editor.commit();
                                openUrlAfter(ctx, url);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editor.putBoolean(SettingsPref.KEY_BIND_UC,
                                        false);
                                editor.commit();
                                openUrlByDefaultBrowser(ctx, url);
                            }
                        });
        builder.show();
    }

    public static void openUrl(Context ctx, String url) {
        if (canPromptUc(ctx)) { // 第一次打开url，并且用户未对uc设置进行操作，弹出提示框
            if (isUcInstalled(ctx)) { // 已安装uc
                promptUc(ctx, url);
            } else {
                openUrlByDefaultBrowser(ctx, url);
            }
        } else {
            if (SettingsPref.isBindUc(ctx)) { // 设置里绑定uc，使用uc打开
                openUrlAfter(ctx, url);
            } else { // 设置里未绑定uc， 使用默认浏览器打开
                openUrlByDefaultBrowser(ctx, url);
            }
        }
    }

    private static boolean isUcInstalled(Context ctx) {
        return (getUcPackageInfo(ctx, true) != null)
                || (getUcPackageInfo(ctx, false) != null);
    }

    private static PackageInfo getUcPackageInfo(Context ctx, boolean oldVersion) {
        String packageName;
        if (oldVersion) {
            packageName = "com.uc.browser";
        } else {
            packageName = "com.UCMobile";
        }

        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_CONFIGURATIONS);
            return info;
        } catch (NameNotFoundException e) {
            loge(e);
        } catch (Exception e) {
            loge(e);
        }
        return null;
    }

    private static void openUrlAfter(Context ctx, String url) {
        /**
         * 默认使用UC打开URL
         */
        if (Constants.UCMOBILE) {
            if (getUcPackageInfo(ctx, false) != null) {// new uc version
                openUrlByUcMobile(ctx, url);
                return;
            }
        }

        PackageInfo info = getUcPackageInfo(ctx, true);// old uc version
        if (info != null) {
            String versionName = info.versionName.trim();

            StringTokenizer token = new StringTokenizer(versionName, ".");
            StringBuilder sb = new StringBuilder();
            int count = 2;
            while (token.hasMoreTokens() && count > 0) {
                if (count == 1) {
                    sb.append(token.nextToken());
                } else {
                    sb.append(token.nextToken()).append(".");
                }
                count--;
            }
            sb.append("0");
            double versionCode = Double.parseDouble(sb.toString());

            /**
             * UC 7.9 版本以后，支持公有接口的调用方式
             */
            if (url.startsWith("http://t.cn/")) {
                String gsid = StaticInfo.mUser == null ? ""
                        : StaticInfo.mUser.gsid;
                url = "http://weibo.cn/dpool/ttt/sinaurl.php?u=" + url
                        + "&gsid=" + gsid;
            }

            if (versionCode > 7.8) {
                openUrlByUcBrowser(ctx, url);
                return;
            } else {
                openUrlByDefaultBrowser(ctx, url);
                return;
            }
        }

        openUrlByDefaultBrowser(ctx, url);
    }

    private static void openUrlByDefaultBrowser(Context ctx, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            if (url != null && !url.startsWith("http://")
                    && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Uri uri = Uri.parse(url);
            i.setData(uri);
            ctx.startActivity(i);
        } catch (ActivityNotFoundException e) {
            loge(e);
        }
    }

    private static void openUrlByUcBrowser(Context ctx, String url) {
        try {
            Intent i = new Intent("com.uc.browser.intent.action.LOADURL");
            i.putExtra("recall_action", "com.test.openintenttouc");
            i.putExtra("UC_LOADURL", "ext:webkit:" + url);
            i.putExtra("time_stamp", System.currentTimeMillis());
            ctx.startActivity(i);
        } catch (ActivityNotFoundException e) {
            loge(e);
        }
    }

    private static void openUrlByUcMobile(Context ctx, String url) {
        Intent i = new Intent("com.UCMobile.intent.action.LOADURL");
        i.putExtra("pd", "sinaweibo");
        i.putExtra("UC_LOADURL", url);
        ctx.startActivity(i);
    }

    public static int[] Byte2Int(byte[] bs) {
        int count = (bs.length / 3);
        int mod = bs.length % 3;
        int[] resultArray;
        if (mod == 0) {
            resultArray = new int[count];
        } else {
            resultArray = new int[count + 1];
        }
        int i = 0;
        int tmp = 0xff;
        int offset = 8;
        for (int j = 0; j < count; j++) {
            for (int n = 0; n < 3; i++, n++) {
                resultArray[j] |= ((bs[i] << offset * n) & (tmp << offset * n));
            }
        }
        if (mod > 0) {
            for (int m = 0; m < mod; m++) {
                resultArray[count] |= ((bs[i + m] << offset * m) & (tmp << offset
                        * m));
            }
        }
        return resultArray;
    }

    public static boolean checkBlogFile(String cachedir) {
        return !TextUtils.isEmpty(checkCache(cachedir, BLOG_FILE));
    }

    public static String checkCache(String cachedir, String url) {
        String filepath;
        String slash = "/";
        if (cachedir.endsWith(slash)) {
            slash = "";
        }
        if (url.contains("woriginal") && isEndWithGif(url)) {
            filepath = cachedir + slash + MD5.hexdigest(url) + ".gif";
        } else {
            filepath = cachedir + slash + MD5.hexdigest(url);
        }
        Utils.logd("——：" + filepath);

        return new File(filepath).exists() ? filepath : null;
    }

    public static void cleanAtListFile(String cachedir, User user) {
        new File(cachedir + '/' + user.uid + AT_FILE).delete();
    }

    public static void cleanBlogFile(String cachedir) {
        new File(cachedir + BLOG_FILE).delete();
    }

    public static void cleanCameraBmpCache() {
        File sdFile = new File(FileUtil.printSDCardRoot());
        File[] cacheFiles = sdFile.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.startsWith(CAMERA_FILE_CACHE_PREX);
            }

        });
        if (cacheFiles != null) {
            for (File f : cacheFiles) {
                FileUtil.deleteDependon(f);
            }
        }
    }

    public static void cleanCommentMessageListFile(String cachedir, User user) {
        new File(cachedir + '/' + user.uid + CM_FILE).delete();
    }

    public static void cleanFavoriteListFile(String cachedir, User user) {
        new File(cachedir + '/' + user.uid + FAVORITE_FILE).delete();
    }

    public static void cleanHomeListFile(Context context, String cachedir,
            User user) {
        // new File(cachedir + '/' + user.uid + HOME_FILE).delete();
        deleteAll(context, SinaWeiboDB.HOME, Group.GROUP_ID_ALL);
    }

    public static void cleanHotForwardListFile(String cachedir) {
        new File(cachedir + HOTFORWARD_FILE).delete();
    }

    public static void cleanHotWordListFile(String cachedir, User user) {
        new File(cachedir + '/' + user.uid + FAVHOTWORD_FILE).delete();
    }

    public static void cleanLookAroundListFile(String cachedir) {
        new File(cachedir + LOOKAROUND_FILE).delete();
    }

    public static void cleanMessageListFile(String cachedir, User user) {
        new File(cachedir + '/' + user.uid + MSG_FILE).delete();
    }

    public static void cleanMyBlogListFile(String cachedir, User user) {
        new File(cachedir + '/' + user.uid + MYBLOG_FILE).delete();
    }

    public static boolean cleanSearchWordList(String cachedir) {
        return new File(cachedir).delete();
    }

    public static void cleanSubMessageListFile(String uid, String cachedir,
            User user) {
        new File(cachedir + '/' + user.uid + '_' + uid).delete();
    }

    public static void cleanTopUserListFile(String cachedir) {
        new File(cachedir + TOPUSER_FILE).delete();
    }

    public static boolean cleanUserFile() {
        /**
         * new File(cachedir + USER_FILE).delete();
         */
        return mDB.deleteAll(SinaWeiboDB.USERINFO, null);
    }

    public static void clearAllLargeImages(Context ctx_) {
        FileUtil.deleteFiles(ctx_.getCacheDir().getAbsolutePath()
                + DIR_LARGE_IMAGE);
    }

    public static void cleanCurUser(Context ctx) {
        mDB.deleteAll(SinaWeiboDB.ACCOUNT | SinaWeiboDB.USERINFO, null);
    }

    // 清除缓存
    public static void clearCache(File file) {
        if (file == null)
            return;
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.exists() && f.canWrite()) {
                    f.delete();
                }
            }
        }

        if (file.isFile()) {
            if (file.exists() && file.canWrite())
                file.delete();
        }
    }

    // public static native String nativeS(String srcArray);
    public static String calculateS(String srcArray) {
        if (WeiboApplication.instance != null) {
            return WeiboApplication.instance.calculateS(srcArray);
        } else {
            return "";
        }
    }

    public static String getDecryptionString(String src) {
        if (WeiboApplication.instance != null) {
            // return new
            // String(WeiboApplication.instance.getDecryptionString(src),"utf-8");
            return WeiboApplication.instance.getDecryptionString(src);
        }
        return "";
    }

    // public static String calculateS(String srcArray){
    // char[] tmp = MD5.hexdigest(nativeS(srcArray)).toCharArray();
    // StringBuffer m = new StringBuffer();
    // m =
    // m.append(tmp[1]).append(tmp[5]).append(tmp[2]).append(tmp[10]).append(tmp[17]).append(tmp[9]).append(tmp[25]).append(tmp[27]);
    // return m.toString();
    // }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        if (sdDir != null)
            return sdDir.toString();
        else
            return null;

    }

    public static Intent commentComment(Context context, CommentMessage cmtMsg) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.COMMENT_COMMENT);
        i.putExtra(EditActivity.EXTRA_COMMENT_ID, cmtMsg.commentid);
        i.putExtra(EditActivity.EXTRA_NICKNAME, cmtMsg.commentnick);
        i.putExtra(EditActivity.EXTRA_COMMENT_AUTHOR_UID, cmtMsg.commentuid);
        i.putExtra(EditActivity.EXTRA_MBLOG_ID, cmtMsg.mblogid);
        i.putExtra(EditActivity.EXTRA_MBLOG_AUTHOR_UID, cmtMsg.mbloguid);
        return i;
    }

    /**
     * 对某条微博的某天评论进行评论
     * 
     * @param context
     * @param mBlogId
     * @param mBlogUid
     * @param cmt
     */
    public static Intent commentComment(Context context, String mBlogId,
            String mBlogUid, Comment cmt) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.COMMENT_COMMENT);
        i.putExtra(EditActivity.EXTRA_COMMENT_ID, cmt.cmtid);
        i.putExtra(EditActivity.EXTRA_NICKNAME, cmt.nick);
        i.putExtra(EditActivity.EXTRA_COMMENT_AUTHOR_UID, cmt.cmtuid);
        i.putExtra(EditActivity.EXTRA_MBLOG_ID, mBlogId);
        i.putExtra(EditActivity.EXTRA_MBLOG_AUTHOR_UID, mBlogUid);
        return i;
    }

    /**
     * 比较版本，判断是否需要升级
     * 
     * @param context
     * @param internetLatestVersion
     * @return
     */
    public static boolean compareVersion(Context context,
            String internetLatestVersion) {
        try {
            String localVersion = PackageManagerUtil.getVersion(context);
            final Pattern p = Pattern.compile(VERSION_PATTERN);
            final Matcher m = p.matcher(localVersion);
            if (m.matches()) {
                localVersion = m.group(1) + "00" + m.group(2) + m.group(3);
                Utils.logd("localVersion: " + localVersion
                        + "\tinternetLatestVersion:" + internetLatestVersion);
                return localVersion.compareTo(internetLatestVersion) < 0;
            }
        } catch (Exception e) {
            Utils.loge(e);
        }
        return false;
    }

    public static CustomToast createProgressCustomToast(int res, Context a) {
        CustomToast ct = new CustomToast(a.getApplicationContext(), res, true);
        return ct;
    }

    public static CustomToast createCustomToast(int res, Context a) {
        CustomToast ct = new CustomToast(a.getApplicationContext(), res, false);
        return ct;
    }

    public static Dialog createProgressDialog(int res, Activity a, int style) {
        Dialog mPgDialog = null;
        LinearLayout pgLayout = new LinearLayout(a);
        ProgressBar mProgressBar = null;
        mPgDialog = new Dialog(a, R.style.TransparentDialog);
        TextView tv = Utils.setTextView(res, a);
        if (a.getClass() == ImageViewer.class) {
            tv.setTextColor(Color.WHITE);
            mProgressBar = new ProgressBar(a, null,
                    android.R.attr.progressBarStyleSmall);
        } else {
            tv.setTextColor(Theme.getInstance(a).getColorFromIdentifier(
                    R.color.toast_text));
            mProgressBar = new ProgressBar(a, null,
                    android.R.attr.progressBarStyleSmallInverse);
        }
        pgLayout.addView(mProgressBar);
        pgLayout.addView(tv);
        mPgDialog.setContentView(pgLayout);
        mPgDialog.setCancelable(true);
        return mPgDialog;
    }

    public static Toast createProgressToast(int res, Context a) {
        Toast toast = null;
        toast = new Toast(a);
        LinearLayout pgLayout = new LinearLayout(a);
        ProgressBar mProgressBar = null;
        TextView tv = Utils.setTextView(res, a);
        if (a.getClass() == ImageViewer.class) {
            tv.setTextColor(Color.WHITE);
            mProgressBar = new ProgressBar(a, null,
                    android.R.attr.progressBarStyleSmall);
        } else {
            tv.setTextColor(Theme.getInstance(a).getColorFromIdentifier(
                    R.color.toast_text));
            mProgressBar = new ProgressBar(a, null,
                    android.R.attr.progressBarStyleSmallInverse);
        }
        pgLayout.addView(mProgressBar);
        pgLayout.addView(tv);

        toast.setView(pgLayout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    public static Toast createToast(int res, Context a) {
        Toast toast = null;
        toast = new Toast(a);
        LinearLayout pgLayout = new LinearLayout(a);
        TextView v = new TextView(a);
        ((TextView) v).setText(res);
        ((TextView) v).setGravity(Gravity.CENTER);
        ((TextView) v).setTextSize(13);
        ((TextView) v).setPadding(15, 0, 15, 0);
        if (a.getClass() == ImageViewer.class) {
            v.setTextColor(Color.WHITE);
        } else {
            v.setTextColor(Theme.getInstance(a).getColorFromIdentifier(
                    R.color.toast_text));
        }
        pgLayout.addView(v);

        toast.setView(pgLayout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    public static String dateFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("M-dd HH:mm");
        StringBuffer sb = new StringBuffer();
        time = time + millisecond8hours;
        Date date = new Date(time);
        sdf.format(date, sb, new FieldPosition(1));
        return sb.toString();
    }

    public static boolean deleteCacheItem(Context context, Object item,
            int mode, String cachedir) {
        switch (mode) {
            case DELETE_MBLOG: {
                String mblogid = (String) item;
                delete(context, SinaWeiboDB.HOME, mblogid,
                        Group.GROUP_ID_MY_MBLOG);
                return true;
            }

            case DELETE_FAV: {
                String mblogid = (String) item;
                if (StaticInfo.mUser != null) {
                    List<MBlog> lst = null;
                    // List<MBlog> lst = Utils.loadFavoriteListFile(cachedir,
                    // StaticInfo.mUser);
                    // ParamSinaWeiboDB mparam = new ParamSinaWeiboDB(context,
                    // StaticInfo.mUser, true, 0,
                    // SinaWeiboDBdataSource.GROUP_MYSTORE,
                    // Group.GROUP_ID_MY_FAV,
                    // 0, 0, 0, SinaWeiboDB.HOME);
                    ParamPackage mparam = new ParamPackage();
                    mparam.put(SinaWeiboDBdataSource.SINAWEIBODBCONTEXT,
                            context);
                    mparam.put(SinaWeiboDBdataSource.SINAWEIBODBUSER,
                            StaticInfo.mUser);
                    mparam.put(SinaWeiboDBdataSource.SINAWEIBODBGETMODE, true);
                    mparam.put(SinaWeiboDBdataSource.SINAWEIBODBTABLEID,
                            SinaWeiboDB.HOME);
                    mparam.put(SinaWeiboDBdataSource.SINAWEIBODBGROUPID,
                            SinaWeiboDBdataSource.GROUP_MYSTORE);
                    mparam.put(SinaWeiboDBdataSource.SINAWEIBODBGID,
                            Group.GROUP_ID_MY_FAV);
                    MBlogList mbList = null;
                    try {
                        MBlogList mBlogList = (MBlogList) new SinaWeiboDBdataSource()
                                .get(mparam);
                        if (mBlogList != null) {
                            lst = mBlogList.mblogList;
                        } else {
                            lst = null;
                        }
                    } catch (WeiboIOException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                        lst = null;
                    } catch (WeiboParseException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                        lst = null;
                    } catch (WeiboApiException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                        lst = null;
                    }
                    if (lst != null) {
                        for (int i = 0, size = lst.size(); i < size; i++) {
                            if (lst.get(i).mblogid.equals(mblogid)) {
                                lst.remove(i);
                                // Utils.saveFavoriteListFile(lst, cachedir,
                                // StaticInfo.mUser);
                                deleteAll(context, SinaWeiboDB.HOME,
                                        Group.GROUP_ID_MY_FAV);
                                // ParamSinaWeiboDB mparam1 = new
                                // ParamSinaWeiboDB(context,lst,SinaWeiboDB.HOME,
                                // Group.GROUP_ID_MY_FAV,StaticInfo.mUser);
                                ParamPackage mparam1 = new ParamPackage();
                                mparam1.put(
                                        SinaWeiboDBdataSource.SINAWEIBODBCONTEXT,
                                        context);
                                mparam1.put(
                                        SinaWeiboDBdataSource.SINAWEIBODBLIST,
                                        lst);
                                mparam1.put(
                                        SinaWeiboDBdataSource.SINAWEIBODBTABLEID,
                                        SinaWeiboDB.HOME);
                                mparam1.put(
                                        SinaWeiboDBdataSource.SINAWEIBODBGID,
                                        Group.GROUP_ID_MY_FAV);
                                mparam1.put(
                                        SinaWeiboDBdataSource.SINAWEIBODBUSER,
                                        StaticInfo.mUser);
                                new SinaWeiboDBdataSource().add(mparam1);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * at 某人发微博s
     * 
     * @param context
     * @param content
     * @return
     */
    public static Intent editAtBlog(Context context, String content) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE, EditActivity.LauchMode.AT);
        i.putExtra(EditActivity.EXTRA_INIT_TEXT, content);
        return i;
    }

    public static Intent editNewBlog(Context context) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.NEW_BLOG);
        return i;
    }

    /**
     * 发送新微博
     * 
     * @param context
     * @return
     */
    public static Intent editNewBlog(Context context, String content) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.NEW_BLOG);
        i.putExtra(EditActivity.EXTRA_INIT_TEXT, content);
        return i;
    }

    /**
     * 签到
     * 
     * @param context
     * @return
     */
    public static Intent editLocationBlog(Context context, LocationHolder holder) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.SIGNLOCATION);
        i.putExtra(EditActivity.EXTRA_LOCATION, holder);
        return i;
    }

    /**
     * 发送私信
     * 
     * @param context
     * @param nick
     * @return
     */
    public static Intent editPrivateMessage(Context context, String nick,
            String uid) {
        Intent i = new Intent(context.getApplicationContext(),
                MessageList.class);
        i.putExtra(MessageList.INTENT_PARAM_KEY_NICK, nick);
        i.putExtra(MessageList.INTENT_PARAM_KEY_UID, uid);
        return i;
    }

    /**
     * 就某一话题发送微博
     * 
     * @param context
     * @param content
     * @return
     */
    public static Intent editTopicBlog(Context context, String content) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE, EditActivity.LauchMode.TOPIC);
        i.putExtra(EditActivity.EXTRA_INIT_TEXT, content);
        return i;
    }

    /*
     * public static String Decode(String str){
     * 
     * return null; }
     */
    public static int[] Encode(String str) {
        try {
            DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey sk = skf.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, sk);
            byte[] bs = cipher.doFinal(str.getBytes());
            int[] resultArray = Byte2Int(bs);
            return resultArray;
        } catch (Exception e) {
            loge(e);
        }
        return null;
    }

    public static String formatDate(Context ctx, Date date) {
        if (date == null) {
            return "";
        }
        long delta = new Date().getTime() - date.getTime();
        int value = 0;
        String f = "%d%s";
        if (Utils.isEnLanguage(ctx)) {
            f = "%d %s";
        }
        if (delta > 86400000) {
            return getDateFormat().format(date);
        } else {
            if (delta > 3600000) {
                value = (int) (delta / 3600000);
                if (value > 1) {
                    return String.format(f, value,
                            ctx.getString(R.string.hour_label_plural));
                } else {
                    return String.format(f, value,
                            ctx.getString(R.string.hour_label));
                }
            } else if (delta > 60000) {
                value = (int) (delta / 60000);
                if (value > 1) {
                    return String.format(f, value,
                            ctx.getString(R.string.minute_label_plural));
                } else {
                    return String.format(f, value,
                            ctx.getString(R.string.minute_label));
                }
            } else {
                return String
                        .format(f, 1, ctx.getString(R.string.minute_label));
            }
        }
    }

    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;
    public static final int WEEK = 7 * DAY;

    public static String simpleFormatDate(Context ctx, Date date) {
        if (date == null) {
            return "";
        }
        long delta = new Date().getTime() - date.getTime();
        int value = 0;
        String f = "%d%s";
        if (Utils.isEnLanguage(ctx)) {
            f = "%d %s";
        }

        if (delta > WEEK) {
            value = (int) (delta / WEEK);
            return String.format(f, "1",
                    ctx.getString(R.string.day_label_plural));
        } else if (delta > DAY) {
            value = (int) (delta / DAY);
            if (value > 1) {
                return String.format(f, value,
                        ctx.getString(R.string.day_label_plural));
            } else {
                return String.format(f, value,
                        ctx.getString(R.string.day_label));
            }
        } else if (delta > HOUR) {
            value = (int) (delta / HOUR);
            if (value > 1) {
                return String.format(f, value,
                        ctx.getString(R.string.hour_label_plural));
            } else {
                return String.format(f, value,
                        ctx.getString(R.string.hour_label));
            }
        } else if (delta > MINUTE) {
            value = (int) (delta / MINUTE);
            if (value > 1) {
                return String.format(f, value,
                        ctx.getString(R.string.minute_label_plural));
            } else {
                return String.format(f, value,
                        ctx.getString(R.string.minute_label));
            }
        } else {
            return String.format(f, 1, ctx.getString(R.string.minute_label));
        }
    }

    /**
     * 转发微博的工具方法
     * 
     * @param context
     * @param mBlog
     */
    public static Intent forwardMBlog(Context context, MBlog mBlog) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.FORWARD);
        i.putExtra(EditActivity.EXTRA_MBLOG_ID, mBlog.mblogid);
        i.putExtra(EditActivity.EXTRA_MBLOG_AUTHOR_UID, mBlog.uid);
        if (!TextUtils.isEmpty(mBlog.rtreason)) {
            i.putExtra(EditActivity.EXTRA_FORWARD_REASON, mBlog.rtreason);
        }
        i.putExtra(EditActivity.EXTRA_NICKNAME, mBlog.nick);
        return i;
    }

    /**
     * 转发原始微博
     * 
     * @param context
     * @param mBlog
     * @return
     */
    public static Intent forwardSrcMBlog(Context context, MBlog mBlog) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.FORWARD);
        i.putExtra(EditActivity.EXTRA_MBLOG_ID, mBlog.rtrootid); // 原微博mblogid
        i.putExtra(EditActivity.EXTRA_MBLOG_AUTHOR_UID, mBlog.rtrootuid); // 原发表人uid
        i.putExtra(EditActivity.EXTRA_NICKNAME, mBlog.rtrootnick); // 原发表人nick
        return i;
    }

    public static Pattern getAllPattern() {
        if (allPattern == null) {
            allPattern = Pattern.compile(String.format(
                    "(@[[^@\\s%s]0-9]{1,})|(", getPunctuation())
                    + RegexUtil.PATTERN_HTTP
                    + ")|("
                    + "#[^#]+?#"
                    + "|"
                    + "\\[(\\S+?)\\])");
        }
        return allPattern;
    }

    public static Pattern getAtPattern() {
        if (atpattern == null) {
            atpattern = Pattern.compile(String.format("@[[^@\\s%s]0-9]{1,}",
                    getPunctuation()));
        }

        return atpattern;
    }

    public static File getExternalCacheDir() {
        if (FileUtil.hasSDCardMounted()) {
            if (externalCacheDir == null) {
                externalCacheDir = new File(FileUtil.printSDCardRoot()
                        + "/sina/weibo");
            }
            if (!externalCacheDir.exists()) {
                externalCacheDir.mkdirs();
            }
            return externalCacheDir;
        }
        return null;
    }

    public static Pattern getFilenamePattern() {
        if (filenamepattern == null) {
            filenamepattern = Pattern.compile("[^/\\\\<>*?:\"| ]+");
        }

        return filenamepattern;
    }

    public static long getLargeImagesTotalSize(Context ctx_) {
        return FileUtil.getFileSize(ctx_.getCacheDir().getAbsolutePath()
                + DIR_LARGE_IMAGE);
    }

    /*
     * For widget
     */
    public static boolean getLoginStatus() {
        return isLogin;
    }

    // ListView
    public static Bitmap getPortraitBitmap(String portraitUrl, String cacheDir,
            Context ctx, boolean isVip, boolean bRound, String suffix) {
        // synchronized (lock) {
        final String sdDir = Utils.getSDPath();
        final String saveDir = (TextUtils.isEmpty(sdDir) || !haveFreeSpace()) ? cacheDir
                : sdDir + suffix;
        // Log.d("lgz","getPortraitBitmap saveDir:  " + saveDir);
        Context context = ctx;
        String file = null;
        if (isInCacheMap(portraitUrl, suffix)) {
            // Log.e("weibo", "=========Load Image From Map!=========");
            Bitmap bm = BmpCache.getInstance().get(portraitUrl);
            if (bm == null || bm.isRecycled()) {
                bm = null;
                // 首先判断sdcard中是否有相应缓存图片
                if (!TextUtils.isEmpty(sdDir)) {
                    file = Utils.checkCache(sdDir + suffix, portraitUrl);
                }
                if (file == null) {// 如果sdcard中无缓存图片，则去saveDir判断是否有缓存图片
                    file = Utils.checkCache(saveDir, portraitUrl);
                }
                file = Utils.checkCache(saveDir, portraitUrl);
                Log.d("cachedir>>>>>>", "dir:  " + saveDir);
                return loadFromFile(context, file, isVip, portraitUrl, bRound,
                        suffix);
            }
            return bm;
        } else {
            if (TextUtils.isEmpty(file))
                ;
            // 首先判断sdcard中是否有相应缓存图片
            if (!TextUtils.isEmpty(sdDir)) {
                file = Utils.checkCache(sdDir + suffix, portraitUrl);
            }
            if (file == null) {// 如果sdcard中无缓存图片，则去saveDir判断是否有缓存图片
                file = Utils.checkCache(saveDir, portraitUrl);
            }
            if (file == null) {
                Bitmap bm = loadFromNet(context, portraitUrl, saveDir, isVip,
                        bRound, suffix);
                if (bm == null || bm.isRecycled())
                    return loadFromNet(context, portraitUrl, saveDir, isVip,
                            bRound, suffix);
                return bm;
            } else {
                return loadFromFile(context, file, isVip, portraitUrl, bRound,
                        suffix);
            }
        }
        // }

    }

    public static Bitmap getPreviewBitmap(String previewUrl, String cacheDir,
            Context ctx, boolean isVip, boolean bRound, String suffix) {
        return getPreviewBitmap(previewUrl, cacheDir, ctx, isVip, bRound,
                suffix, new DefaultDownloadCallback());
    }

    public static Bitmap getPreviewBitmap(String previewUrl, String cacheDir,
            Context ctx, boolean isVip, boolean bRound, String suffix,
            IDownloadState callback) {
        // synchronized (lock) {
        final String sdDir = Utils.getSDPath();
        final String saveDir = (TextUtils.isEmpty(sdDir) || !haveFreeSpace()) ? cacheDir
                : sdDir + suffix;
        Context context = ctx;
        String file = null;
        if (isInCacheMap(previewUrl, suffix) && !cacheDir.equals("")) {
            // Log.e("weibo", "=========Load Image From Map!=========");
            Bitmap bm = BmpCache.getInstance().get(previewUrl);
            if (bm == null || bm.isRecycled()) {
                bm = null;
                // 首先判断sdcard中是否有相应缓存图片
                if (!TextUtils.isEmpty(sdDir)) {
                    file = Utils.checkCache(sdDir + suffix, previewUrl);
                }
                if (file == null) {// 如果sdcard中无缓存图片，则去saveDir判断是否有缓存图片
                    file = Utils.checkCache(saveDir, previewUrl);
                }
                Bitmap result = loadFromFile(context, file, isVip, previewUrl,
                        bRound, suffix);
                callback.onComplete(file);
                DownloadCallbackManager.getInstance().remove(
                        getFileNameFromUrl(previewUrl));
                return result;
            }
            return bm;

        } else {
            if (file == null || "".equals(file))
                ;
            // 首先判断sdcard中是否有相应缓存图片
            if (!TextUtils.isEmpty(sdDir)) {
                file = Utils.checkCache(sdDir + suffix, previewUrl);
            }
            if (file == null) {// 如果sdcard中无缓存图片，则去saveDir判断是否有缓存图片
                file = Utils.checkCache(saveDir, previewUrl);
            }

            if (file == null || (new File(file)).length() == 0) {
                Bitmap bm = loadFromNet(context, previewUrl, cacheDir, isVip,
                        bRound, suffix, callback);
                if (bm == null || bm.isRecycled()) {
                    return loadFromNet(context, previewUrl, cacheDir, isVip,
                            bRound, suffix, callback);
                }
                return bm;
            } else {
                Bitmap result = loadFromFile(context, file, isVip, previewUrl,
                        bRound, suffix);
                callback.onComplete(file);
                return result;
            }
        }
        // }
    }

    private static ReentrantLock sReadCacheLock = new ReentrantLock(true);

    /**
     * 用于广告条获取缓存图片是使用
     * 
     * @param previewUrl
     * @param cacheDir
     * @param ctx
     * @param isVip
     * @param bRound
     * @return
     */
    public static Bitmap getCacheBitmap(String previewUrl, String cacheDir,
            Context ctx, boolean isVip, boolean bRound) {
        try {
            if (!sReadCacheLock.tryLock()) {
                return null;
            }
            final String saveDir = cacheDir;
            Context context = ctx;
            String file = null;
            if (isInCacheMap(previewUrl) && !cacheDir.equals("")) {
                Bitmap bm = BmpCache.getInstance().get(previewUrl);
                if (bm == null || bm.isRecycled()) {
                    bm = null;
                    file = Utils.checkCache(saveDir, previewUrl);
                    return loadFileCache(file);
                }
                return bm;

            } else {
                if (file == null || "".equals(file))
                    ;
                file = Utils.checkCache(saveDir, previewUrl);

                if (file == null) {
                    return null;
                } else {
                    return loadFileCache(file);
                }
            }
        } catch (Exception e) {
            loge(e);
            return null;
        } finally {
            sReadCacheLock.unlock();
        }
    }

    private static boolean isInCacheMap(String picUrl) {
        Bitmap bm = null;
        if (picUrl == null) {
            return false;
        } else {
            bm = BmpCache.getInstance().get(picUrl);
            if (bm == null || bm.isRecycled()) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static Bitmap loadFileCache(String file) {
        // 从文件加载
        // Log.e("weibo", "=========Load Image From File!=========");
        Bitmap bm = BitmapFactory.decodeFile(file);
        return bm;
    }

    /**
     * 取得异常或者错误的根本原因
     * 
     * @param tr
     * @return
     */
    public static Throwable getRootCause(Throwable tr) {
        if (tr == null)
            return null;
        Throwable error = null;
        Throwable lastCause, currentCause;
        lastCause = currentCause = tr.getCause();
        while (currentCause != null) {
            lastCause = currentCause;
            currentCause = currentCause.getCause();
        }

        if (lastCause == null) {
            error = tr;
        } else {
            error = lastCause;
        }
        return error;
    }

    private static int highLightTextColor = -1;

    public static void highlightContent(Context ctx, Spannable str) {
        Matcher matcher;
        matcher = getAllPattern().matcher((CharSequence) str);
        int start = 0;
        int end = 0;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            // Utils.logd("start:" + start + " end:" + end);
            // Utils.logd("str:" + str.subSequence(start, end));
            if ((end - start == 2 && WeiboApplication.ME.equals(str
                    .charAt(start + 1))) || end - start > 2) {
                if (highLightTextColor == -1) {
                    highLightTextColor = Theme.getInstance(ctx)
                            .getColorFromIdentifier(
                                    R.color.blog_item_content_light_text);
                }
                str.setSpan(new ForegroundColorSpan(highLightTextColor), start,
                        end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        // 表情
        Pattern pattern = null;
        pattern = Pattern.compile("\\[(\\S+?)\\]");
        matcher = pattern.matcher(str);
        Integer drawableSrc = null;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            drawableSrc = EmotionView.emotionsKeyString.get(matcher.group(1));
            if (drawableSrc != null && drawableSrc > 0) {
                str.setSpan(new ImageSpan(ctx, drawableSrc), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static void highlightContentClickable(Context ctx, Spannable str,
            String gsid) {
        if (highLightTextColor == -1) {
            highLightTextColor = Theme.getInstance(ctx).getColorFromIdentifier(
                    R.color.blog_item_content_light_text);
        }
        Matcher matcher;
        // @别人
        matcher = getAtPattern().matcher((CharSequence) str);
        int start;
        int end;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            if ((end - start == 2 && WeiboApplication.ME.equals(str
                    .charAt(start + 1))) || end - start > 2) {
                str.setSpan(new ForegroundColorSpan(highLightTextColor), start,
                        end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new AtClicker(ctx, str.subSequence(start + 1, end)
                        .toString()), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        // 话题
        matcher = getSharpPattern().matcher((CharSequence) str);
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            str.setSpan(new ForegroundColorSpan(highLightTextColor), start,
                    end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(
                    new TopicClicker(ctx, str.subSequence(start + 1, end - 1)
                            .toString().trim()), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // 链接
        matcher = getWebPattern().matcher((CharSequence) str);
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            str.setSpan(new ForegroundColorSpan(highLightTextColor), start,
                    end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new UrlClicker(ctx, str.subSequence(start, end)
                    .toString().trim(), gsid), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // 表情
        Pattern pattern = null;
        pattern = Pattern.compile("\\[(\\S+?)\\]");
        matcher = pattern.matcher(str);
        Integer drawableSrc = null;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            drawableSrc = EmotionView.emotionsKeyString.get(matcher.group(1));
            if (drawableSrc != null && drawableSrc > 0) {
                str.setSpan(new ImageSpan(ctx, drawableSrc), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static List<MBlog> loadAtListFile(String cachedir, User user) {
        String userUid;
        if (user == null || TextUtils.isEmpty(user.uid)) {
            userUid = "";
        } else {
            userUid = user.uid;
        }
        return (List<MBlog>) load(cachedir + '/' + userUid + AT_FILE);
    }

    public static String loadBlogFile(String cachedir) {
        return (String) load(cachedir + BLOG_FILE);
    }

    public static List<CommentMessage> loadCommentMessageListFile(
            String cachedir, User user) {
        String userUid;
        if (user == null || TextUtils.isEmpty(user.uid)) {
            userUid = "";
        } else {
            userUid = user.uid;
        }
        return (List<CommentMessage>) load(cachedir + '/' + userUid + CM_FILE);
    }

    public static Bitmap loadFromFile(Context context, String file,
            boolean isVip, String portraitUrl, boolean bRound, String suffix) {
        // synchronized (lock) {
        // 从文件加载
        // Log.e("weibo", "=========Load Image From File!=========");
        Bitmap bm = BitmapFactory.decodeFile(file.replace("//", "/"));

        if (!bRound || true) {
            File cacheFile = new File(file);
            if (cacheFile.exists() && bm == null) {
                cacheFile.delete();
            }
            return bm;
        } else {
            if (bm != null && !bm.isRecycled()) {
                // Bitmap round = Utils.outputBmp(context, bm, isVip);
                Bitmap round = getDefaultPortrait(context);
                putCacheMap(portraitUrl, round, suffix);
                return round;
            } else {
                File f = new File(file);
                if (f.canWrite())
                    f.delete();
            }
            return null;
        }

        // }
    }

    public static Bitmap loadFromNet(Context context, String portraitUrl,
            String mCacheDir, boolean isVip, boolean bRound, String suffix) {
        return loadFromNet(context, portraitUrl, mCacheDir, isVip, bRound,
                suffix, new DefaultDownloadCallback());
    }

    public static Bitmap loadFromNet(Context context, String portraitUrl,
            String mCacheDir, boolean isVip, boolean bRound, String suffix,
            IDownloadState callback) {

        synchronized (lock) {
            // Log.e("weibo", "=========Load Image From Internet!=========");
            // 到网络加载
            Bitmap round = null;
            Bitmap result = null;
            try {
                // final String IMAGE_CACHE_DIR =
                // context.getCacheDir().getAbsolutePath()
                // + Utils.DIR_LARGE_IMAGE;
                final String sdDir = Utils.getSDPath();
                final String saveDir = (TextUtils.isEmpty(sdDir) || !haveFreeSpace()) ? mCacheDir
                        : sdDir + suffix;
                // Log.d("lgz","loadFromNet saveDir:  " + saveDir);
                String filePath = NetEngineFactory
                        .getNetInstance(context)
                        .getPicture(portraitUrl, saveDir, false, callback, null);
                Bitmap bm = BitmapFactory.decodeFile(filePath);
                //
                // byte[] imageByte = RPCHelper.getInstance(context)
                // .getPictureByte(portraitUrl);
                // Bitmap bm = BitmapFactory.decodeByteArray(imageByte, 0,
                // imageByte.length);

                if (!bRound || true) {
                    // result = bm;
                    // if(!mCacheDir.equals("")){
                    // savePicFile2Memory(imageByte, portraitUrl, mCacheDir,
                    // suffix);
                    // }
                    return bm;
                } else {
                    // if (bm == null || bm.isRecycled()) {
                    // return null;
                    // }
                    // putCacheMap(portraitUrl, round, suffix);
                    // //round = Utils.outputBmp(context, bm, isVip);
                    // round = getDefaultPortrait(context);
                    //
                    // /*int first = listView.getFirstVisiblePosition();
                    // int count = listView.getChildCount();
                    // for (int i = 0; i < count; i++) {
                    // if (!(listView.getChildAt(i) instanceof
                    // MBlogListItemView))
                    // continue;
                    // MBlogListItemView v = (MBlogListItemView) listView
                    // .getChildAt(i);
                    // if (v.mBlog.portrait.equals(portraitUrl)) {
                    //
                    // }
                    // }*/
                    // result = round;
                    // // if(!mCacheDir.equals("")){
                    // // savePicFile2Memory(imageByte, portraitUrl, mCacheDir,
                    // suffix);
                    // // }
                    // // return round;
                    //
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utils.loge(e);
            }
            return result;
        }
    }

    // hotforward_file
    public static List<MBlog> loadHotForwardListFile(String cachedir) {
        return (List<MBlog>) load(cachedir + HOTFORWARD_FILE);
    }

    // 关注话题列表
    public static List<FavHotWord> loadHotWordListFile(String cachedir,
            User user) {
        return (List<FavHotWord>) load(cachedir + '/' + user.uid
                + FAVHOTWORD_FILE);
    }

    public static List<MBlog> loadLookAroundListFile(String cachedir) {
        return (List<MBlog>) load(cachedir + LOOKAROUND_FILE);
    }

    public static List<Message> loadMessageListFile(String cachedir, User user) {
        String userUid;
        if (user == null || TextUtils.isEmpty(user.uid)) {
            userUid = "";
        } else {
            userUid = user.uid;
        }
        return (List<Message>) load(cachedir + '/' + userUid + MSG_FILE);
    }

    public static int loadModeFile(String cachedir, User user) {
        int mode = 0;
        if (user != null) {
            Integer m = (Integer) load(cachedir + '/' + user.uid + MODE_FILE);
            if (m != null)
                mode = m.intValue();
            if (mode > 8)
                mode = 0;
        }
        return mode;
    }

    public static List<MBlog> loadMyBlogListFile(String cachedir, User user) {
        String userUid;
        if (user == null || TextUtils.isEmpty(user.uid)) {
            userUid = "";
        } else {
            userUid = user.uid;
        }
        return (List<MBlog>) load(cachedir + '/' + userUid + MYBLOG_FILE);
    }

    /*
     * Delete item from cache item : Key for delete implemention mode : Delete
     * mode cachedir : App cache dir
     */

    // save user history search key word list
    public static Set<String> loadSearchWordList(String cachedir) {
        return (Set<String>) load(cachedir);
    }

    public static Object[] loadSubMessageListFile(String uid, String cachedir,
            User user) {
        Object obj = load(cachedir + '/' + user.uid + '_' + uid);
        if (obj instanceof Object[])
            return (Object[]) obj;
        else
            return null;
    }

    public static List<UserInfo> loadTopUserListFile(String cachedir) {
        return (List<UserInfo>) load(cachedir + TOPUSER_FILE);
    }

    public static int[] loadTotalFile(String cachedir, User user) {
        return (int[]) load(cachedir + '/' + user.uid + TOTAL_FILE);
    }

    public static User loadUserFileDes2(String cachedir) {
        User user = (User) load(cachedir + USER_FILE);
        if (user != null) {
            DesEncrypt2 des = new DesEncrypt2();
            user.gsid = des.getDesString(user.gsid);
        }
        return user;
    }

    public static User loadUserFile(String cachedir) {
        /**
         * 从数据中获得当前登录的用户
         */
        List<User> list;
        list = (List<User>) mDB.select(SinaWeiboDB.USERINFO, null, null);
        if (list == null || list.size() < 1) {
            return null;
        }
        User user = list.get(0);
        if (user != null) {
            DesEncrypt2 des = new DesEncrypt2();
            user.gsid = des.getDesString(user.gsid);
            user.setOauth_token(des.getDesString(user.getOauth_token()));
            user.setOauth_token_secret(des.getDesString(user
                    .getOauth_token_secret()));
        }
        return user;
    }

    public static User loadUserFileOauth(String cachedir) {
        /**
         * 从数据中获得当前登录的用户
         */
        List<User> list;
        list = (List<User>) mDB.select(SinaWeiboDB.USERINFO, null, null);
        if (list == null || list.size() < 1) {
            return null;
        }
        User user = list.get(0);
        if (user != null) {
            DesEncrypt2 des = new DesEncrypt2();
            user.gsid = des.getDesString(user.gsid);
        }
        return user;
    }

    public static User loadUserFileDes1(String cachedir) {
        User user = (User) load(cachedir + USER_FILE);
        if (user != null) {
            DesEncrypt des = new DesEncrypt();
            user.gsid = des.getDesString(user.gsid);
        }
        return user;
    }

    public static List<User> loadUsrnameList() {
        List<User> list;
        list = (List<User>) mDB.select(SinaWeiboDB.USERLIST, null, null);
        if (list == null || list.size() < 1) {
            return null;
        }
        return list;
    }

    public static Set<String> loadKeyWordList(String cachedir) {
        return (Set<String>) load(cachedir);
    }

    public static void logd(CharSequence msg) {
        if (!TextUtils.isEmpty(msg) && Constants.DEBUG) {
            Log.d(Constants.TAG, msg.toString());
        }
    }

    public static void loge(CharSequence msg) {
        if (!TextUtils.isEmpty(msg) && Constants.DEBUG) {
            Log.e(Constants.TAG, msg.toString());
        }
    }

    public static void loge(Throwable e) {
        if (e != null && Constants.DEBUG) {
            Log.e(Constants.TAG, "", e);
        }
    }

    /**
     * isCover为true则输出加V图片；否则输出一般圆角图片
     * 
     * @param ctx
     * @param src
     * @param isCover
     * @return
     */
    public static Bitmap outputBmp(Context ctx, Bitmap src, boolean isCover) {
        Bitmap v = null;
        if (isCover) {
            v = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.v);
        } else {
            v = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.tv);
        }
        Bitmap bmp = null;
        int arcLength = RRB_DEFAULT_SIZE;
        if (src != null && arcLength > 0) {
            int width = src.getWidth();
            int height = src.getHeight();
            // Utils.loge("width:" + width + "height:" + height);
            Bitmap newBitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(newBitmap);
            rrbRath.reset();
            rrbRectf.set(0, 0, width, height);
            rrbRath.addRoundRect(rrbRectf, arcLength, arcLength,
                    Path.Direction.CW);
            canvas.clipPath(rrbRath);
            canvas.drawBitmap(src, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
            if (newBitmap != null && v != null) {
                int width1 = newBitmap.getWidth();
                int height1 = newBitmap.getHeight();
                int width2 = v.getWidth();
                int height2 = v.getHeight();
                bmp = Bitmap.createBitmap(width1 + (width2 / 2), height1
                        + (height2 / 2), Bitmap.Config.ARGB_4444);
                bmp.eraseColor(Color.TRANSPARENT);
                Canvas canvas2 = new Canvas(bmp);
                canvas2.drawBitmap(newBitmap, 0, 0, new Paint(
                        Paint.ANTI_ALIAS_FLAG));
                canvas2.drawBitmap(v, width1 - (width2 / 2), height1
                        - (height2 / 2), new Paint(Paint.ANTI_ALIAS_FLAG));
                newBitmap.recycle();
                v.recycle();
                return bmp;
            }
            src.recycle();
        }
        return bmp;

        /*
         * else { return outputRoundRectBitmap(src); }
         */
    }

    /**
     * isCover为true则输出加V图片；否则输出一般圆角图片
     * 
     * @param ctx
     * @param src
     * @param isCover
     * @param isRound
     *            是否是圆角图片
     * @return
     */
    public static Bitmap outputBmp(Context ctx, Bitmap src, boolean isCover,
            boolean isRound) {
        Bitmap v = null;
        if (isCover) {
            v = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.v);
        } else {
            v = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.tv);
        }
        Bitmap bmp = null;
        int arcLength = RRB_DEFAULT_SIZE;
        if (src != null && arcLength > 0) {
            int width = src.getWidth();
            int height = src.getHeight();
            // Utils.loge("width:" + width + "height:" + height);
            Bitmap newBitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(newBitmap);
            if (isRound) {
                rrbRath.reset();
                rrbRectf.set(0, 0, width, height);
                rrbRath.addRoundRect(rrbRectf, arcLength, arcLength,
                        Path.Direction.CW);
                canvas.clipPath(rrbRath);
            }
            canvas.drawBitmap(src, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
            // 暂时不画V图标
            if (true) {
                v.recycle();
                src.recycle();
                return newBitmap;
            }
            if (newBitmap != null && v != null) {
                int width1 = newBitmap.getWidth();
                int height1 = newBitmap.getHeight();
                int width2 = v.getWidth();
                int height2 = v.getHeight();
                bmp = Bitmap.createBitmap(width1 + (width2 / 2), height1
                        + (height2 / 2), Bitmap.Config.ARGB_4444);
                bmp.eraseColor(Color.TRANSPARENT);
                Canvas canvas2 = new Canvas(bmp);
                canvas2.drawBitmap(newBitmap, 0, 0, new Paint(
                        Paint.ANTI_ALIAS_FLAG));
                canvas2.drawBitmap(v, width1 - (width2 / 2), height1
                        - (height2 / 2), new Paint(Paint.ANTI_ALIAS_FLAG));
                newBitmap.recycle();
                v.recycle();
                return bmp;
            }
            src.recycle();
        }
        return bmp;

        /*
         * else { return outputRoundRectBitmap(src); }
         */
    }

    /**
     * 缩放bmp到指定的大小
     * 
     * @param bmp
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bmp, float width, float height) {
        if (bmp == null) {
            return null;
        }
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(width / bmpWidth, height / bmpHeight);
        Bitmap temp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
                matrix, true);
        return temp;
    }

    // 返回加V图片
    public static Bitmap outputCoverBmp(Bitmap src1, Bitmap src2) {
        if (src1 != null && src2 != null) {
            int width1 = src1.getWidth();
            int height1 = src1.getHeight();
            int width2 = src2.getWidth();
            int height2 = src2.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width1 + (width2 / 2), height1
                    + (height2 / 2), Bitmap.Config.ARGB_4444);
            bmp.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bmp);
            canvas.drawBitmap(src1, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
            canvas.drawBitmap(src2, width1 - (width2 / 2), height1
                    - (height2 / 2), new Paint(Paint.ANTI_ALIAS_FLAG));
            src1.recycle();
            src2.recycle();
            return bmp;
        }
        return null;
    }

    /**
     * 返回加V图片
     * 
     * @param ctx
     * @param src
     * @return
     */
    public static Bitmap outputCoverBmp(Context ctx, Bitmap src, int vip) {
        Bitmap v;
        if (vip == 1) {
            v = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.v);
        } else {
            v = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.v_def);
        }

        return outputCoverBmp(outputRoundRectBitmap(src), v);
    }

    public static void outputLogger(String s) {
        if (FileUtil.hasSDCardMounted()) {
            return;
        }
        File fb = new File(Utils.getExternalCacheDir(), "logger.txt");
        if (!fb.getParentFile().exists()) {
            fb.mkdirs();
        }
        try {
            byte[] buffer = s.getBytes();
            FileOutputStream os = new FileOutputStream(fb, true);
            os.write(buffer);
            os.close();
        } catch (IOException e) {
            Utils.loge(e);
        }

    }

    // 返回数字显示图片
    public static Bitmap outputNumberBitmap(Bitmap src, int num) {
        if (src != null && num > 0) {
            int height = src.getHeight();
            int width = src.getWidth();
            Bitmap res = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_4444);
            nbPaint.setTextSize(10.f);
            int descent = (int) nbPaint.descent();
            int radius = conputingRadius(nbPaint);
            Canvas canvas = new Canvas(res);
            canvas.drawBitmap(src, 0, 0, nbPaint);
            nbPaint.setColor(Color.RED);
            canvas.drawCircle(width - radius, height - radius, radius, nbPaint);
            nbPaint.setTextAlign(Paint.Align.CENTER);
            String text = num > 999 ? "999+" : String.valueOf(num);
            nbPaint.setColor(Color.WHITE);
            canvas.drawText(text, width - radius, height - radius + descent,
                    nbPaint);
            return res;
        }
        return null;
    }

    public static Bitmap outputRoundRectBitmap(Bitmap src) {
        return outputRoundRectBitmap(src, RRB_DEFAULT_SIZE);
    }

    public static void saveAtListFile(List<MBlog> lst, String cachedir,
            User user) {
        if (user == null)
            return;
        if (lst.size() > CACHE_LIMITATION) {
            List<MBlog> tmp = new ArrayList<MBlog>();
            tmp.addAll((Collection) lst.subList(0, CACHE_LIMITATION - 1));
            save(tmp, cachedir + '/' + user.uid + AT_FILE);
        } else
            save(lst, cachedir + '/' + user.uid + AT_FILE);
    }

    public static void saveBlogFile(String content, String cachedir) {
        save(content, cachedir + BLOG_FILE);
    }

    public static void saveCommentMessageListFile(List<CommentMessage> lst,
            String cachedir, User user) {
        if (lst.size() > CACHE_LIMITATION) {
            List<CommentMessage> tmp = new ArrayList<CommentMessage>();
            tmp.addAll((Collection) lst.subList(0, CACHE_LIMITATION - 1));
            save(tmp, cachedir + '/' + user.uid + CM_FILE);
        } else
            save(lst, cachedir + '/' + user.uid + CM_FILE);
    }

    public static void saveHomeListFile(Context context, List<MBlog> lst,
            String cachedir, User user) {
        deleteAll(context, SinaWeiboDB.HOME, Group.GROUP_ID_ALL);
        if (lst.size() > CACHE_LIMITATION) {
            List<MBlog> tmp = new ArrayList<MBlog>();
            tmp.addAll((Collection) lst.subList(0, CACHE_LIMITATION - 1));
            insert(context, tmp, SinaWeiboDB.HOME, Group.GROUP_ID_ALL,
                    StaticInfo.mUser.uid);
        } else {
            insert(context, lst, SinaWeiboDB.HOME, Group.GROUP_ID_ALL,
                    StaticInfo.mUser.uid);
        }
    }

    public static void saveHotForwardListFile(List<MBlog> lst, String cachedir) {
        save(lst, cachedir + HOTFORWARD_FILE);
    }

    public static void saveHotWordListFile(List<FavHotWord> lst,
            String cachedir, User user) {
        if (lst.size() > CACHE_LIMITATION) {
            List<FavHotWord> tmp = new ArrayList<FavHotWord>();
            tmp.addAll((Collection) lst.subList(0, CACHE_LIMITATION - 1));
            save(tmp, cachedir + '/' + user.uid + FAVHOTWORD_FILE);
        } else
            save(lst, cachedir + '/' + user.uid + FAVHOTWORD_FILE);
    }

    public static void saveLookAroundListFile(List<MBlog> lst, String cachedir) {
        save(lst, cachedir + LOOKAROUND_FILE);
    }

    public static void saveMessageListFile(List<Message> lst, String cachedir,
            User user) {
        if (lst.size() > CACHE_LIMITATION) {
            List<Message> tmp = new ArrayList<Message>();
            tmp.addAll((Collection) lst.subList(0, CACHE_LIMITATION - 1));
            save(tmp, cachedir + '/' + user.uid + MSG_FILE);
        } else
            save(lst, cachedir + '/' + user.uid + MSG_FILE);
    }

    public static void saveModeFile(int mode, String cachedir, User user) {
        save(new Integer(mode), cachedir + '/' + user.uid + MODE_FILE);
    }

    public static void saveMyBlogListFile(List<MBlog> lst, String cachedir,
            User user) {
        if (lst.size() > CACHE_LIMITATION) {
            List<MBlog> tmp = new ArrayList<MBlog>();
            tmp.addAll((Collection) lst.subList(0, CACHE_LIMITATION - 1));
            save(tmp, cachedir + '/' + user.uid + MYBLOG_FILE);
        } else
            save(lst, cachedir + '/' + user.uid + MYBLOG_FILE);
    }

    public static void saveSearchWordList(String cachedir,
            Set<String> searchWords) {
        cleanSearchWordList(cachedir);
        save(searchWords, cachedir);
    }

    public static void saveSubMessageListFile(List<Message> lst, int total,
            String uid, String cachedir, User user) {
        Object[] objs = new Object[2];
        if (lst.size() > CACHE_LIMITATION) {
            List<Message> tmp = new ArrayList<Message>();
            tmp.addAll((Collection) lst.subList(0, CACHE_LIMITATION - 1));
            objs[0] = total;
            objs[1] = tmp;
        } else {
            objs[0] = total;
            objs[1] = lst;
        }
        save(objs, cachedir + '/' + user.uid + '_' + uid);
    }

    public static void saveTopUserListFile(List<UserInfo> lst, String cachedir) {
        save(lst, cachedir + TOPUSER_FILE);
    }

    public static void saveTotalFile(int[] total, String cachedir, User user) {
        save(total, cachedir + '/' + user.uid + TOTAL_FILE);
    }

    /* save session key to UserFile, share with others activity */
    public static void saveUserFile(User user, String cachedir,
            final Context context) {
        cleanUserFile();
        if (user == null) {
            return;
        }
        DesEncrypt2 des = new DesEncrypt2();
        ContentValues[] values = new ContentValues[1];
        /**
         * 保存到数据库
         */
        ContentValues c = new ContentValues();
        String uid = user.uid == null ? "" : user.uid;
        c.put(SinaWeiboDB.USER_UID, uid);
        String gsid = user.gsid == null ? "" : user.gsid;
        c.put(SinaWeiboDB.USER_GSID, des.getEncString(gsid));
        String name = user.name == null ? "" : user.name;
        c.put(SinaWeiboDB.USER_NAME, name);
        String nick = user.nick == null ? "" : user.nick;
        c.put(SinaWeiboDB.USER_NICK, nick);
        c.put(SinaWeiboDB.USER_STATUS, user.status);
        String url = user.url == null ? "" : user.url;
        c.put(SinaWeiboDB.USER_URL, url);
        String msgurl = user.msgurl == null ? "" : user.msgurl;
        c.put(SinaWeiboDB.USER_MSG_URL, msgurl);
        String oauth_token = user.getOauth_token() == null ? "" : user
                .getOauth_token();
        c.put(SinaWeiboDB.USER_OAUTHTOKEN, des.getEncString(oauth_token));
        String oauth_token_secret = user.getOauth_token_secret() == null ? ""
                : user.getOauth_token_secret();
        c.put(SinaWeiboDB.USER_OAUTHTOKENSECRET,
                des.getEncString(oauth_token_secret));
        values[0] = c;
        mDB.insert(values, SinaWeiboDB.USERINFO, null, null);
    }

    // 存储登录用户的用户信息
    public static UserInfo loadMyInfo(String cachedir, String uid) {
        UserInfo u = (UserInfo) load(cachedir + '/' + uid + Utils.USERINFO_FILE);
        return u;
    }

    public static void saveMyInfo(String cachedir, UserInfo user) {
        if (user == null)
            return;
        save(user, cachedir + '/' + user.uid + Utils.USERINFO_FILE);
    }

    public static void cleanMyInfo(String cachedir, String uid) {
        new File(cachedir + '/' + uid + Utils.USERINFO_FILE).delete();
    }

    // save user name list
    public static void saveUsrnameList(List<User> usrList) {
        List<User> list = new ArrayList<User>(usrList);
        int size = list.size();
        ContentValues[] values = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            ContentValues c = new ContentValues();
            c.put(SinaWeiboDB.USERLIST_NAME, list.get(i).name);
            values[i] = c;
        }
        mDB.insert(values, SinaWeiboDB.USERLIST, null, null);
    }

    public static void saveKeyWordList(String cachedir, Set<String> usrnames) {
        cleanKeyWordList(cachedir);
        save(usrnames, cachedir);
    }

    public static Intent sendFeedback(Context context) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.FEEDBACK);
        return i;
    }

    public static void setLoginStatus(boolean b) {
        isLogin = b;
    }

    public static TextView setTextView(int res, Context context) {
        TextView v = new TextView(context);
        ((TextView) v).setText(res);
        ((TextView) v).setGravity(Gravity.CENTER);
        ((TextView) v).setTextSize(13);
        ((TextView) v).setPadding(15, 0, 15, 0);
        ((TextView) v).setTextColor(Color.BLACK);
        return v;
    }

    public static boolean SetWeiboDB(SinaWeiboDB sb) {
        mDB = sb;
        if (mDB != null) {
            return true;
        }
        return false;
    }

    /**
     * 对某微博进行评论
     * 
     * @param context
     * @param mBlog
     */
    public static Intent simpleComment(Context context, String mBlogId,
            String mBlogUid) {
        Intent i = new Intent(context.getApplicationContext(),
                EditActivity.class);
        i.putExtra(EditActivity.EXTRA_LAUCH_MODE,
                EditActivity.LauchMode.SIMPLE_COMMENT);
        i.putExtra(EditActivity.EXTRA_MBLOG_ID, mBlogId);
        i.putExtra(EditActivity.EXTRA_MBLOG_AUTHOR_UID, mBlogUid);
        return i;
    }

    public static String translationThrowable(Context ctx, Throwable tr) {
        tr = getRootCause(tr);
        if (tr instanceof ApiException) {
            String msg = tr.getMessage();
            if (msg.contains(":")) {
                msg.substring(msg.indexOf(":"));
            }
            return msg;
        } else if (tr instanceof WeiboApiException) {
            String msg = tr.getMessage();
            if (msg.contains(":")) {
                msg = msg.substring(msg.lastIndexOf(":") + 1);
            }
            return msg;
        } else if (tr instanceof WeiboIOException) {
            return ctx.getString(R.string.WeiboIOException);
        } else if (tr instanceof WeiboParseException) {
            return ctx.getString(R.string.NoSignalException);
        } else if (tr instanceof WeiboLocationException) {
            return ctx.getString(R.string.can_not_located);
        } else if (tr instanceof NoSignalException) {
            return ctx.getString(R.string.NoSignalException);
        } else if (tr instanceof NoRouteToHostException) {
            return ctx.getString(R.string.NoRouteToHostException);
        } else if (tr instanceof SocketTimeoutException) {
            return ctx.getString(R.string.SocketTimeoutException);
        } else if (tr instanceof UnknownHostException) {
            return ctx.getString(R.string.UnknownHostException);
        } else if (tr instanceof IOException) {
            return ctx.getString(R.string.IOException);
        } else if (tr instanceof XmlPullParserException) {
            return ctx.getString(R.string.NoSignalException);
        } else if (tr instanceof NumberFormatException) {
            return ctx.getString(R.string.list_no_item);
        } else {
            return (tr == null || tr.getMessage() == null) ? ctx
                    .getString(R.string.OthersException) : tr.getMessage();
        }
    }

    public static String trim(String src) {
        if (src != null && src.length() != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < src.length(); i++) {
                if (src.charAt(i) != ' ') {
                    sb.append(src.charAt(i));
                }
            }
            return String.valueOf(sb.toString());
        }
        return src;
    }

    public static Pattern getWebPattern() {
        if (webpattern == null)
            webpattern = Pattern.compile(RegexUtil.PATTERN_HTTP);
        return webpattern;
    }

    private static boolean cleanKeyWordList(String cachedir) {
        return new File(cachedir).delete();
    }

    private static int conputingRadius(Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds("999+", 0, "999+".length(), rect);
        return (int) Math.hypot(rect.right, rect.bottom);
    }

    private static SimpleDateFormat getDateFormat() {
        if (sdf == null)
            sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        return sdf;
    }

    private static String getPunctuation() {
        return "`~!@#\\$%\\^&*()=+\\[\\]{}\\|/\\?<>,\\.:"
                + "\\u00D7\\u00B7\\u2014-\\u2026\\u3001-\\u3011\\uFE30-\\uFFE5";
    }

    public static Pattern getWebPattern2() {
        if (webpattern2 == null) {
            webpattern2 = Pattern
                    .compile("((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
                            + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                            + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                            + "((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+" // named
                                                                            // host
                            + "(?:" // plus top level domain
                            + "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
                            + "|(?:biz|b[abdefghijmnorstvwyz])"
                            + "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])"
                            + "|d[ejkmoz]"
                            + "|(?:edu|e[cegrstu])"
                            + "|f[ijkmor]"
                            + "|(?:gov|g[abdefghilmnpqrstuwy])"
                            + "|h[kmnrtu]"
                            + "|(?:info|int|i[delmnoqrst])"
                            + "|(?:jobs|j[emop])"
                            + "|k[eghimnrwyz]"
                            + "|l[abcikrstuvy]"
                            + "|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])"
                            + "|(?:name|net|n[acefgilopruz])"
                            + "|(?:org|om)"
                            + "|(?:pro|p[aefghklmnrstwy])"
                            + "|qa"
                            + "|r[eouw]"
                            + "|s[abcdeghijklmnortuvyz]"
                            + "|(?:tel|travel|t[cdfghjklmnoprtvwz])"
                            + "|u[agkmsyz]"
                            + "|v[aceginu]"
                            + "|w[fs]"
                            + "|y[etu]"
                            + "|z[amw]))"
                            + "|(?:(?:25[0-5]|2[0-4]" // or ip address
                            + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
                            + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
                            + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                            + "|[1-9][0-9]|[0-9])))"
                            + "(?:\\:\\d{1,5})?)" // plus option port number
                            + "(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~" // plus
                                                                                // option
                                                                                // query
                                                                                // params
                            + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
                            + "(?:\\b|$)"); // and finally, a word boundary or
                                            // end of
                                            // input. This is to stop foo.sure
                                            // from
                                            // matching as foo.su
        }
        return webpattern2;

    }

    private static Pattern getSharpPattern() {
        if (sharppattern == null)
            sharppattern = Pattern.compile("#[^#]+?#");
        return sharppattern;
    }

    private static boolean isInCacheMap(String picUrl, final String suffix) {
        // synchronized (lock) {
        Bitmap bm = null;
        if (picUrl == null) {
            return false;
        } else {
            bm = BmpCache.getInstance().get(picUrl);
            if (bm == null || bm.isRecycled()) {
                return false;
            } else {
                return true;
            }
        }
        // }
    }

    private static Object load(String path) {
        Object obj = null;
        File file = new File(path);
        try {
            /*
             * if(file != null){ file.mkdirs(); }
             */
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                try {
                    obj = ois.readObject();
                } catch (ClassNotFoundException e) {
                    loge(e);
                }
                ois.close();
            }
        } catch (IOException e) {
            loge(e);
        }
        return obj;
    }

    /*
     * Return Round Bitmap
     * 
     * Notice! the src has been recycled when return
     */
    private static Bitmap outputRoundRectBitmap(Bitmap src, int arcLength) {
        if (src != null && arcLength > 0) {
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap newBitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(newBitmap);
            rrbRath.reset();
            rrbRectf.set(0, 0, width, height);
            rrbRath.addRoundRect(rrbRectf, arcLength, arcLength,
                    Path.Direction.CW);
            canvas.clipPath(rrbRath);
            canvas.drawBitmap(src, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
            src.recycle();
            return newBitmap;
        }
        return null;
    }

    private static void putCacheMap(String url, Bitmap bm, String suffix) {
        synchronized (lock) {
            if (isInCacheMap(url, suffix)) {
                return;
            }
            BmpCache.getInstance().save(url, bm);
        }
    }

    private static void save(Object obj, String path) {
        try {
            File f = new File(path);
            /*
             * if(f != null){ f.mkdirs(); f.createNewFile(); }
             */
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            loge(e);
        }
    }

    // 获取cache缓存文件夹大小
    public static long getFileLength(String fileName) {
        long length = 0;
        File file = new File(fileName);
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    if (f != null && f.exists() && f.canRead()) {
                        length += f.length();
                    }
                }
            } else if (file.isFile() && file.canRead()) {
                length = file.length();
            }
            return length;
        }
        return -1;
    }

    /**
     * 限制文件夹中的文件数量
     */
    public static void sharkFolderSize(String dir, long maxFileNum) {
        File file = new File(dir);
        if (file != null && file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length > maxFileNum) {
                clearDirectory(file);
                // long deleteCount = files.length - maxFileNum;
                // for (int i = 0; i < deleteCount && i < files.length; i++) {
                // File f = files[i];
                // if (f != null && f.exists() && f.canWrite() && f.isFile()) {
                // f.delete();
                // } else {
                // deleteCount++;
                // }
                // }
            }
        }
    }

    // 清除除用户文件之外所有缓存文件
    public static void clearCacheButUser(File file) {
        if (file == null)
            return;
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.exists()
                        && f.canWrite()
                        && !f.getName().equals(USER_FILE.substring(1))
                        && !f.getName().equals(
                                AdCenter.INVALID_AD_MAP.substring(1))) {
                    Log.d(Constants.TAG, "delete file:" + f.getName());
                    f.delete();
                }
            }
        }

        if (file.isFile()) {
            if (file.exists()
                    && file.canWrite()
                    && !file.getName().equals(USER_FILE.substring(1))
                    && !file.getName().equals(
                            AdCenter.INVALID_AD_MAP.substring(1)))
                Log.d(Constants.TAG, "delete file:" + file.getName());
            file.delete();
        }

    }

    // 清除除用户文件之外所有缓存文件
    public static void clearSDFolder(File file) {
        if (file == null)
            return;
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.exists()
                        && f.canWrite()
                        && !f.getName().equals(USER_FILE.substring(1))
                        && !f.getName().equals(
                                AdCenter.INVALID_AD_MAP.substring(1))) {
                    Log.d(Constants.TAG, "delete file:" + f.getName());
                    f.delete();
                }
            }
        }

        if (file.isFile()) {
            if (file.exists()
                    && file.canWrite()
                    && !file.getName().equals(USER_FILE.substring(1))
                    && !file.getName().equals(
                            AdCenter.INVALID_AD_MAP.substring(1)))
                Log.d(Constants.TAG, "delete file:" + file.getName());
            file.delete();
        }

    }

    // 删除文件或者文件夹
    public static void clearDirectory(File file) {
        if (file == null)
            return;
        if (!file.exists())
            return;
        boolean skip = false;
        try {
            if (file.getCanonicalPath().endsWith(
                    Constants.SAVE_FORLDER_PATH.substring(0,
                            Constants.SAVE_FORLDER_PATH.length() - 1))) {
                skip = true;
            }
            if (file.getCanonicalPath().endsWith(
                    DownloadManager.DIR_DOWNLOAD.substring(0,
                            DownloadManager.DIR_DOWNLOAD.length() - 1))) {
                skip = true;
            }
        } catch (IOException e) {
            Utils.loge(e);
        }
        if (file.isDirectory() && skip == false) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.exists() && f.canWrite()) {
                    if (f.isDirectory()) {
                        clearDirectory(f);
                    } else if (f.isFile()) {
                        f.delete();
                    }
                }
            }
        } else if (file.isFile()) {
            if (file.exists() && file.canWrite()) {
                file.delete();
            }
        }
    }

    /*
     * 存储到本地内存中，优先存储到sd卡中，如果无sd卡，存储到cache目录中
     */
    public static void savePicFile2Memory(byte[] imageByte, String url,
            String cacheDir, String dirSuffix) throws ApiException, IOException {
        synchronized (lock) {
            final String sdDir = Utils.getSDPath();
            final String saveDir = TextUtils.isEmpty(sdDir) ? cacheDir : sdDir
                    + dirSuffix;
            if (saveDir.equalsIgnoreCase(cacheDir)) {
                long cacheLength = getFileLength(saveDir);
                // 如果内存大于4MB，则清空内存
                if (cacheLength > Utils.MAX_CACHE_LIMITION) {
                    File f = new File(saveDir);
                    clearCacheButUser(f);
                }
            }
            savePictureLocal(imageByte, url, saveDir);
        }
    }

    // 当cache目录下内存满时清空头像缓存文件夹
    private static void savePictureLocal(byte[] imageByte, String url,
            String savedir) throws ApiException, IOException {
        synchronized (lock) {
            if (imageByte == null || imageByte.length <= 0)
                return;
            File tempFile = new File(savedir);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            if (url == null || "".equals(url) || savedir == null
                    || "".equals(savedir))
                return;

            String filename = MD5.hexdigest(url);
            String filepath = savedir + "/" + filename;
            File f = new File(filepath);
            FileOutputStream out = null;

            try {
                if (f.exists()) {
                    if (f.canWrite())
                        f.delete();
                }
                out = new FileOutputStream(f);
                out.write(imageByte);
                out.flush();
                out.close();
            } catch (IOException e) {
                if (out != null)
                    out.close();
                throw new ApiException("save Bitmap to Loacl File Error", e);
            }
        }
    }

    public static boolean isListEmpty(List list) {
        if (list == null)
            return true;
        if (list.size() == 0)
            return true;
        return false;
    }

    /**
     * 是否为英文版，暂时用配置文件，之后考虑加预编译
     * 
     * @param context
     * @return
     */
    public static boolean isEnPlatform(Context context) {
        if (mIsEnPlatform != null) {// 这个值为配置文件里的，不会改变，加载一次就可以了
            return mIsEnPlatform.booleanValue();
        }
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String en = properties.getProperty("en_platform", "false");
            if ("true".equals(en)) {
                mIsEnPlatform = true;
                return true;
            } else {
                mIsEnPlatform = false;
                return false;
            }
        } catch (IOException e) {
            mIsEnPlatform = false;
            return false;// 找不到配置文件按主版本处理,这里不做特殊处理
        }
    }

    /**
     * 是否是英文环境
     * 
     * @param context
     * @return
     */
    public static boolean isEnLanguage(Context context) {
        if (mIsEnLanguage != null && !mIsChangeLanguage) {
            return mIsEnLanguage;
        }
        SharedPreferences preferences = SettingsPref
                .getDefaultSharedPreference(context);
        String lg = preferences.getString(
                context.getString(R.string.setting_key_switch_language), "");
        if (lg.equals(context.getString(R.string.language_value_en))) {
            mIsEnLanguage = true;
            mIsChangeLanguage = false;
            return true;
        } else {
            mIsChangeLanguage = false;
            mIsEnLanguage = false;
            return false;
        }
    }

    /**
     * 是否支持首页分组功能，暂时用配置文件，之后考虑加预编译
     * 
     * @param context
     * @return
     */
    public static boolean isSupportGroup(Context context) {
        if (mIsSupportGroup != null) {// 这个值为配置文件里的，不会改变，加载一次就可以了
            return mIsSupportGroup.booleanValue();
        }
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String group = properties.getProperty("group_support", "true");
            if ("true".equals(group)) {
                mIsSupportGroup = true;
                return true;
            } else {
                mIsSupportGroup = false;
                return false;
            }
        } catch (IOException e) {
            mIsSupportGroup = false;
            return false;// 找不到配置文件按主版本处理,这里不做特殊处理
        }
    }

    /**
     * 获取广告时使用，用于匹配屏幕
     * 
     * @param context
     * @return
     */
    public static String getWindowsWidthParam(Context context) {
        if (mWindowsWidth == 0) {
            Display display = ((WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            mWindowsWidth = display.getWidth() < display.getHeight() ? display
                    .getWidth() : display.getHeight();
        }
        if (mWindowsWidth <= 240) {
            return "240";
        } else if (mWindowsWidth > 240 && mWindowsWidth <= 320) {
            return "320";
        } else {
            return "480";
        }
    }

    private static Bitmap portrait;

    /**
     * 获得默认头像
     */
    public static Bitmap getDefaultPortrait(Context context) {
        if (portrait == null || portrait.isRecycled()) {
            portrait = ((BitmapDrawable) Theme.getInstance(context)
                    .getDrawableFromIdentifier(R.drawable.portrait))
                    .getBitmap();
        }
        return portrait;
    }

    /**
     * 切换皮肤后清空已经加载的资源
     */
    public static void clearOldRes() {
        portrait = null;
        highLightTextColor = -1;
    }

    // public static Bitmap getDefaultPortrait2(Context context) {
    // if (portrait == null || portrait.isRecycled()) {
    // portrait = Utils.outputBmp(context, Utils
    // .outputRoundRectBitmap(BitmapFactory.decodeResource(context
    // .getResources(), R.drawable.portrait)), false);
    // }
    // return portrait;
    // }

    /**
     * 设置用户信息的头像
     */
    public static void setUserInfoPortrait(ImageView iv, Bitmap bmp,
            float width, float height) {
        Bitmap bitmap = Utils.zoomBitmap(bmp, width, height);
        iv.setImageBitmap(bitmap);
    }

    /**
     * 设置是否显示V头像
     */
    public static void setPortraitMask(ImageView iv, boolean isVip) {
        if (isVip) {
            iv.setImageResource(R.drawable.portrait_v);
        } else {
            iv.setImageResource(R.drawable.portrait_normal);
        }
    }

    /**
     * 根据vip，vipsubtype和level设置V头像
     */
    public static void setPortraitMask(ImageView iv, boolean isVip,
            boolean isVipsubtype, boolean isLevel) {
        if (isVip) {
            if (isVipsubtype) {
                iv.setImageResource(R.drawable.portrait_v_blue);
            } else {
                iv.setImageResource(R.drawable.portrait_v_yellow);
            }
        } else {
            if (isLevel) {
                iv.setImageResource(R.drawable.portrait_v_red);
            } else {
                iv.setImageResource(R.drawable.portrait_normal);
            }
        }
    }

    /**
     * 隐藏软键盘
     * 
     */
    public static void hideKeyBoard(Context ctx, EditText et) {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 读取数据库
     * 
     * @param context
     * @param tableId
     * @param gid
     * @param mgsid
     * @return
     */
    public static Object getMBlogFromDB(Context context, int tableId,
            String gid, String mgsid) {
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL;
        if (tableId == SinaWeiboDB.HOME) {
            uriString += "/query/home";
        } else {
            uriString += "/query/comment";
        }
        String whereClause = "GID" + "=" + gid + " and" + " GSID" + "=" + "'"
                + mgsid + "'";
        Cursor c = context.getContentResolver().query(Uri.parse(uriString),
                null, whereClause, null, null);
        switch (tableId) {
            case SinaWeiboDB.HOME: {
                List<MBlog> lst = new ArrayList<MBlog>();
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    // Log.e(Constants.TAG, String.valueOf(c.getCount()));
                    MBlog b = new MBlog();
                    int i = 0;
                    while (true) {
                        if (c.getColumnName(i).equals("uid")) {
                            b.uid = c.getString(i);
                            if (b.uid.equals("")) {
                                if (c != null) {
                                    c.close();
                                }
                                return null;
                            }
                        } else if (c.getColumnName(i).equals("favid")) {
                            b.favid = c.getString(i);
                        } else if (c.getColumnName(i).equals("mblogid")) {
                            b.mblogid = c.getString(i);
                        } else if (c.getColumnName(i).equals("nick")) {
                            b.nick = c.getString(i);
                        } else if (c.getColumnName(i).equals("portrait")) {
                            b.portrait = c.getString(i);
                        } else if (c.getColumnName(i).equals("vip")) {
                            if (c.getString(i).equals("1")) {// yellow v
                                b.vip = true;
                                b.vipsubtype = false;
                                b.level = false;
                            } else if (c.getString(i).equals("2")) {// blue v
                                b.vip = true;
                                b.vipsubtype = true;
                                b.level = false;
                            } else if (c.getString(i).equals("3")) {// red v
                                b.vip = false;
                                b.vipsubtype = false;
                                b.level = true;
                            } else {
                                b.vip = false;
                                b.vipsubtype = false;
                                b.level = false;
                            }
                        } else if (c.getColumnName(i).equals("content")) {
                            b.content = c.getString(i);
                        } else if (c.getColumnName(i).equals("rtrootuid")) {
                            b.rtrootuid = c.getString(i);
                        } else if (c.getColumnName(i).equals("rtrootnick")) {
                            b.rtrootnick = c.getString(i);
                        } else if (c.getColumnName(i).equals("rtrootvip")) {
                            b.rtrootvip = (c.getString(i).equals("1")) ? true
                                    : false;
                        } else if (c.getColumnName(i).equals("rtreason")) {
                            b.rtreason = c.getString(i);
                        } else if (c.getColumnName(i).equals("rtnum")) {
                            b.rtnum = c.getInt(i);
                        } else if (c.getColumnName(i).equals("commentnum")) {
                            b.commentnum = c.getInt(i);
                        } else if (c.getColumnName(i).equals("time")) {
                            b.time = new Date(Long.parseLong((c.getString(i))));
                        } else if (c.getColumnName(i).equals("pic")) {
                            b.pic = c.getString(i);
                        } else if (c.getColumnName(i).equals("src")) {
                            b.src = c.getString(i);
                        } else if (c.getColumnName(i).equals("rtrootid")) {
                            b.rtrootid = c.getString(i);
                            // break;
                        } else if (c.getColumnName(i).equals("longitude")) {
                            b.longitude = c.getString(i);
                            // break;
                        } else if (c.getColumnName(i).equals("remark")) {
                            b.remark = c.getString(i);
                        } else if (c.getColumnName(i).equals("latitude")) {
                            b.latitude = c.getString(i);
                            // break;
                        } else if (c.getColumnName(i).equals("distance")) {
                            b.distance = c.getString(i);
                            break;
                        }
                        i++;

                    }
                    // Utils.loge("select_id___" + b.mblogid +"time__" +
                    // String.valueOf(b.time.getTime()));
                    lst.add(b);
                    c.moveToNext();
                }

                if (c != null) {
                    c.close();
                }
                return lst;
            }
            case SinaWeiboDB.COMMENT: {
                List<CommentMessage> lst = new ArrayList<CommentMessage>();
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    // Log.e(Constants.TAG, String.valueOf(c.getCount()));
                    CommentMessage b = new CommentMessage();
                    int i = 0;
                    while (true) {
                        // Log.e(Constants.TAG, String.valueOf(i));
                        String name = c.getColumnName(i);
                        if (name.equals(SinaWeiboDB.COMMENT_ID)) {
                            b.commentid = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_UID)) {
                            b.commentuid = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_NICK)) {
                            b.commentnick = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_PORTRAIT)) {
                            b.commentportrait = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_CONTENT)) {
                            b.commentcontent = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_TIME)) {
                            b.commenttime = new Date(c.getInt(i) * 1000);
                        } else if (name.equals(SinaWeiboDB.COMMENT_MBLOG_ID)) {
                            b.mblogid = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_MBLOG_UID)) {
                            b.mbloguid = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_MBLOG_NICK)) {
                            b.mblognick = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_SRC_ID)) {
                            b.srcid = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_SRC_UID)) {
                            b.srcuid = c.getString(i);
                        } else if (name.equals(SinaWeiboDB.COMMENT_SRC_CONTENT)) {
                            b.srccontent = c.getString(i);
                            break;
                        }
                        i++;
                    }
                    lst.add(b);
                    c.moveToNext();
                }

                if (c != null) {
                    c.close();
                }
                return lst;
            }
        }
        // Log.e(Constants.TAG, "select out");
        return null;
        // return c;
    }

    /**
     * 从数据库读取私信
     * 
     * @param context
     * @param gsid
     * @param uid
     * @param fromtime
     * @param endtime
     * @param onlyFailedMsg
     *            ： 是否只是获取发送失败的私信
     * @return
     */
    public static List<Message> getIMFromDB(Context context, String gsid,
            String uid, long fromtime, long endtime, boolean onlyFailedMsg) {
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL
                + "/query/im";
        String whereClause;
        if (endtime > 0) {
            whereClause = "gsid='" + gsid + "' and uid=" + "'" + uid
                    + "' and localTime<" + endtime + " and localTime>"
                    + fromtime;
        } else {
            whereClause = "gsid='" + gsid + "' and uid=" + "'" + uid
                    + "' and localTime>" + fromtime;
        }
        if (onlyFailedMsg) {
            whereClause += " and state=" + IMMessageEvent.IM_STATE_FAILED;
        }
        Cursor c = context.getContentResolver().query(Uri.parse(uriString),
                null, whereClause, null, null);
        List<Message> lst = new ArrayList<Message>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Message message = new Message();
            int i = 0;
            while (true) {
                // Log.e(Constants.TAG, String.valueOf(i));
                String name = c.getColumnName(i);
                if (name.equals(SinaWeiboDB.IM_LOCAL_MSGID)) {
                    message.localMsgID = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_NUM)) {
                    message.num = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_TIME)) {
                    message.time = new Date(c.getLong(i));
                } else if (name.equals(SinaWeiboDB.IM_TYPE)) {
                    message.type = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_UID)) {
                    message.uid = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_GSID)) {
                    message.gsid = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_NICK)) {
                    message.nick = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_REMARK)) {
                    message.remark = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_PORTRAIT)) {
                    message.portrait = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_VIP)) {
                    message.vip = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_VIPSUBTYPE)) {
                    message.vipsubtype = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_LEVEL)) {
                    message.level = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_CONTENT)) {
                    message.content = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_MSGID)) {
                    message.msgid = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_FID)) {
                    message.attachment_fid = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_SHA1)) {
                    message.attachment_sha1 = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_NAME)) {
                    message.attachment_name = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_CTIME)) {
                    message.attachment_ctime = c.getLong(i);
                } else if (name.equals(SinaWeiboDB.IM_A_LTIME)) {
                    message.attachment_ltime = c.getLong(i);
                } else if (name.equals(SinaWeiboDB.IM_A_DIR_ID)) {
                    message.attachment_dir_id = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_SIZE)) {
                    message.attachment_size = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_A_TYPE)) {
                    message.attachment_type = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_W)) {
                    message.attachment_w = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_A_H)) {
                    message.attachment_h = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_A_URL)) {
                    message.attachment_url = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_THUMBNAIL)) {
                    message.attachment_thumbnail = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_VIRUS_SCAN)) {
                    message.attachment_virus_scan = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_IS_SAFE)) {
                    message.attachment_is_safe = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_S3_URL)) {
                    message.attachment_s3_url = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_LOCAL_FILEPATH)) {
                    message.attachment_localFilePath = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_ID)) {
                    message.id = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_LOCALTIME)) {
                    message.localTime = c.getLong(i);
                } else if (name.equals(SinaWeiboDB.IM_STATE)) {
                    message.state = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_SERVERCONFIRMED)) {
                    message.serverConfirmed = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_MESSAGE_TYPE)) {
                    message.setMessageType(c.getInt(i));
                    break;
                }
                i++;

            }
            lst.add(message);
            c.moveToNext();
        }

        if (c != null) {
            c.close();
        }
        // closeDb(db);
        return lst;
    }

    /**
     * 从数据库读取私信
     * 
     * @param context
     * @param tableId
     * @param gsid
     * @param uid
     * @return
     */
    public static List<Message> getIMFromDB(Context context, String localMsgid) {
        if (localMsgid == null) {
            return null;
        }
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL
                + "/query/im";
        String whereClause = SinaWeiboDB.IM_LOCAL_MSGID + " ='" + localMsgid
                + "'";
        Cursor c = context.getContentResolver().query(Uri.parse(uriString),
                null, whereClause, null, null);
        List<Message> lst = new ArrayList<Message>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Message message = new Message();
            int i = 0;
            while (true) {
                // Log.e(Constants.TAG, String.valueOf(i));
                String name = c.getColumnName(i);
                if (name.equals(SinaWeiboDB.IM_LOCAL_MSGID)) {
                    message.localMsgID = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_NUM)) {
                    message.num = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_TIME)) {
                    message.time = new Date(c.getLong(i));
                } else if (name.equals(SinaWeiboDB.IM_TYPE)) {
                    message.type = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_UID)) {
                    message.uid = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_GSID)) {
                    message.gsid = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_NICK)) {
                    message.nick = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_REMARK)) {
                    message.remark = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_PORTRAIT)) {
                    message.portrait = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_VIP)) {
                    message.vip = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_VIPSUBTYPE)) {
                    message.vipsubtype = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_LEVEL)) {
                    message.level = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_CONTENT)) {
                    message.content = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_MSGID)) {
                    message.msgid = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_FID)) {
                    message.attachment_fid = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_SHA1)) {
                    message.attachment_sha1 = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_NAME)) {
                    message.attachment_name = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_CTIME)) {
                    message.attachment_ctime = c.getLong(i);
                } else if (name.equals(SinaWeiboDB.IM_A_LTIME)) {
                    message.attachment_ltime = c.getLong(i);
                } else if (name.equals(SinaWeiboDB.IM_A_DIR_ID)) {
                    message.attachment_dir_id = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_SIZE)) {
                    message.attachment_size = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_A_TYPE)) {
                    message.attachment_type = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_W)) {
                    message.attachment_w = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_A_H)) {
                    message.attachment_h = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_A_URL)) {
                    message.attachment_url = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_THUMBNAIL)) {
                    message.attachment_thumbnail = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_VIRUS_SCAN)) {
                    message.attachment_virus_scan = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_IS_SAFE)) {
                    message.attachment_is_safe = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_S3_URL)) {
                    message.attachment_s3_url = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_A_LOCAL_FILEPATH)) {
                    message.attachment_localFilePath = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_ID)) {
                    message.id = c.getString(i);
                } else if (name.equals(SinaWeiboDB.IM_LOCALTIME)) {
                    message.localTime = c.getLong(i);
                } else if (name.equals(SinaWeiboDB.IM_STATE)) {
                    message.state = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_SERVERCONFIRMED)) {
                    message.serverConfirmed = c.getInt(i);
                } else if (name.equals(SinaWeiboDB.IM_MESSAGE_TYPE)) {
                    message.setMessageType(c.getInt(i));
                    break;
                }
                i++;

            }
            lst.add(message);
            c.moveToNext();
        }

        if (c != null) {
            c.close();
        }
        // closeDb(db);
        return lst;
    }

    private static void wrapBlog(ContentValues c, MBlog mb) {
        c.put(SinaWeiboDB.MBLOG_ID, mb.mblogid);
        c.put(SinaWeiboDB.UID, mb.uid);
        c.put(SinaWeiboDB.FAV_ID, mb.favid);
        c.put(SinaWeiboDB.NICK_NAME, mb.nick);
        c.put(SinaWeiboDB.PORTRAIT_URL, mb.portrait);
        if (mb.vip) {
            if (mb.vipsubtype) {// blue v
                c.put(SinaWeiboDB.IS_VIP, "2");
            } else {// yellow v
                c.put(SinaWeiboDB.IS_VIP, "1");
            }
        } else {
            if (mb.level) {// daren
                c.put(SinaWeiboDB.IS_VIP, "3");
            } else {
                c.put(SinaWeiboDB.IS_VIP, "0");
            }
        }
        c.put(SinaWeiboDB.CONTENT, mb.content);
        c.put(SinaWeiboDB.RTROOT_UID, mb.rtrootuid);
        c.put(SinaWeiboDB.RTROOT_NICK, mb.rtrootnick);
        c.put(SinaWeiboDB.REMARK, mb.remark);
        c.put(SinaWeiboDB.RTROOT_VIP, mb.rtrootvip ? "1" : "0");
        c.put(SinaWeiboDB.RT_REASON, mb.rtreason);
        c.put(SinaWeiboDB.RT_NUM, mb.rtnum);
        c.put(SinaWeiboDB.COMMENT_NUM, mb.commentnum);
        c.put(SinaWeiboDB.DATE, String.valueOf(mb.time.getTime()));
        c.put(SinaWeiboDB.PIC_URL, mb.pic);
        c.put(SinaWeiboDB.MBLOG_FROM, mb.src);
        c.put(SinaWeiboDB.RTROOT_ID, mb.rtrootid);
        c.put(SinaWeiboDB.LONGITUDE, mb.longitude);
        c.put(SinaWeiboDB.LATITUDE, mb.latitude);
    }

    private static void wrapComment(ContentValues c, CommentMessage cm) {
        c.put(SinaWeiboDB.COMMENT_ID, cm.commentid);
        c.put(SinaWeiboDB.COMMENT_UID, cm.commentuid);
        c.put(SinaWeiboDB.COMMENT_NICK, cm.commentnick);
        c.put(SinaWeiboDB.COMMENT_PORTRAIT, cm.commentportrait);
        c.put(SinaWeiboDB.COMMENT_CONTENT, cm.commentcontent);
        c.put(SinaWeiboDB.COMMENT_TIME, cm.commenttime.getTime());
        c.put(SinaWeiboDB.COMMENT_MBLOG_ID, cm.mblogid);
        c.put(SinaWeiboDB.COMMENT_MBLOG_UID, cm.mbloguid);
        c.put(SinaWeiboDB.COMMENT_MBLOG_NICK, cm.mblognick);
        c.put(SinaWeiboDB.COMMENT_MBLOG_CONTENT, cm.mblogcontent);
        c.put(SinaWeiboDB.COMMENT_SRC_ID, cm.srcid);
        c.put(SinaWeiboDB.COMMENT_SRC_UID, cm.srcuid);
        c.put(SinaWeiboDB.COMMENT_SRC_CONTENT, cm.srccontent);
    }

    private static void wrapIM(ContentValues c, Message m) {
        c.put(SinaWeiboDB.IM_NUM, m.num);
        c.put(SinaWeiboDB.IM_TIME, m.time.getTime());
        c.put(SinaWeiboDB.IM_TYPE, m.type);
        c.put(SinaWeiboDB.IM_UID, m.uid);
        c.put(SinaWeiboDB.IM_GSID, m.gsid);
        c.put(SinaWeiboDB.IM_NICK, m.nick);
        c.put(SinaWeiboDB.IM_REMARK, m.remark);
        c.put(SinaWeiboDB.IM_PORTRAIT, m.portrait);
        c.put(SinaWeiboDB.IM_VIP, m.vip);
        c.put(SinaWeiboDB.IM_VIPSUBTYPE, m.vipsubtype);
        c.put(SinaWeiboDB.IM_LEVEL, m.level);
        c.put(SinaWeiboDB.IM_CONTENT, m.content);
        c.put(SinaWeiboDB.IM_MSGID, m.msgid);
        c.put(SinaWeiboDB.IM_A_FID, m.attachment_fid);
        c.put(SinaWeiboDB.IM_A_SHA1, m.attachment_sha1);
        c.put(SinaWeiboDB.IM_A_NAME, m.attachment_name);
        c.put(SinaWeiboDB.IM_A_CTIME, m.attachment_ctime);
        c.put(SinaWeiboDB.IM_A_LTIME, m.attachment_ltime);
        c.put(SinaWeiboDB.IM_A_DIR_ID, m.attachment_dir_id);
        c.put(SinaWeiboDB.IM_A_SIZE, m.attachment_size);
        c.put(SinaWeiboDB.IM_A_TYPE, m.attachment_type);
        c.put(SinaWeiboDB.IM_A_W, m.attachment_w);
        c.put(SinaWeiboDB.IM_A_H, m.attachment_h);
        c.put(SinaWeiboDB.IM_A_URL, m.attachment_url);
        c.put(SinaWeiboDB.IM_A_THUMBNAIL, m.attachment_thumbnail);
        c.put(SinaWeiboDB.IM_A_VIRUS_SCAN, m.attachment_virus_scan);
        c.put(SinaWeiboDB.IM_A_IS_SAFE, m.attachment_is_safe);
        c.put(SinaWeiboDB.IM_A_S3_URL, m.attachment_s3_url);
        c.put(SinaWeiboDB.IM_A_LOCAL_FILEPATH, m.attachment_localFilePath);
        c.put(SinaWeiboDB.IM_ID, m.id);
        c.put(SinaWeiboDB.IM_LOCAL_MSGID, m.localMsgID);
        c.put(SinaWeiboDB.IM_LOCALTIME, m.localTime);
        c.put(SinaWeiboDB.IM_STATE, m.state);
        c.put(SinaWeiboDB.IM_SERVERCONFIRMED, m.serverConfirmed);
        c.put(SinaWeiboDB.IM_MESSAGE_TYPE, m.getMessageType());
    }

    public static Message parseIM(ContentValues values) {
        Message message = new Message();
        message.num = values.getAsInteger(SinaWeiboDB.IM_NUM);
        message.time = new Date(values.getAsLong(SinaWeiboDB.IM_TIME));
        message.type = values.getAsInteger(SinaWeiboDB.IM_TYPE);
        message.uid = values.getAsString(SinaWeiboDB.IM_UID);
        message.gsid = values.getAsString(SinaWeiboDB.IM_GSID);
        message.nick = values.getAsString(SinaWeiboDB.IM_NICK);
        message.remark = values.getAsString(SinaWeiboDB.IM_REMARK);
        message.portrait = values.getAsString(SinaWeiboDB.IM_PORTRAIT);
        message.vip = values.getAsInteger(SinaWeiboDB.IM_VIP);
        message.vipsubtype = values.getAsInteger(SinaWeiboDB.IM_VIPSUBTYPE);
        message.level = values.getAsInteger(SinaWeiboDB.IM_LEVEL);
        message.content = values.getAsString(SinaWeiboDB.IM_CONTENT);
        message.msgid = values.getAsString(SinaWeiboDB.IM_MSGID);
        message.attachment_fid = values.getAsString(SinaWeiboDB.IM_A_FID);
        message.attachment_sha1 = values.getAsString(SinaWeiboDB.IM_A_SHA1);
        message.attachment_name = values.getAsString(SinaWeiboDB.IM_A_NAME);
        message.attachment_ctime = values.getAsLong(SinaWeiboDB.IM_A_CTIME);
        message.attachment_ltime = values.getAsLong(SinaWeiboDB.IM_A_LTIME);
        message.attachment_dir_id = values.getAsString(SinaWeiboDB.IM_A_DIR_ID);
        message.attachment_size = values.getAsLong(SinaWeiboDB.IM_A_SIZE);
        message.attachment_type = values.getAsString(SinaWeiboDB.IM_A_TYPE);
        message.attachment_w = values.getAsInteger(SinaWeiboDB.IM_A_W);
        message.attachment_h = values.getAsInteger(SinaWeiboDB.IM_A_H);
        message.attachment_url = values.getAsString(SinaWeiboDB.IM_A_URL);
        message.attachment_thumbnail = values
                .getAsString(SinaWeiboDB.IM_A_THUMBNAIL);
        message.attachment_virus_scan = values
                .getAsString(SinaWeiboDB.IM_A_VIRUS_SCAN);
        message.attachment_is_safe = values
                .getAsString(SinaWeiboDB.IM_A_IS_SAFE);
        message.attachment_s3_url = values.getAsString(SinaWeiboDB.IM_A_S3_URL);
        message.attachment_localFilePath = values
                .getAsString(SinaWeiboDB.IM_A_LOCAL_FILEPATH);
        message.id = values.getAsString(SinaWeiboDB.IM_ID);
        message.localMsgID = values.getAsString(SinaWeiboDB.IM_LOCAL_MSGID);
        message.localTime = values.getAsLong(SinaWeiboDB.IM_LOCALTIME);
        message.state = values.getAsInteger(SinaWeiboDB.IM_STATE);
        message.serverConfirmed = values
                .getAsInteger(SinaWeiboDB.IM_SERVERCONFIRMED);
        message.setMessageType(values.getAsInteger(SinaWeiboDB.IM_MESSAGE_TYPE));
        return message;
    }

    public static boolean update(Context context, Object ob, int rt_num,
            int comment_num, int tableId) {
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL;
        ContentValues values = new ContentValues();
        if (tableId == SinaWeiboDB.HOME) {
            uriString += "/update/home";
            wrapBlog(values, (MBlog) ob);
            values.put("rt_num", rt_num);
            values.put("comment_num", comment_num);

        } else if (tableId == SinaWeiboDB.COMMENT) {
            uriString += "/update/comment";
            wrapComment(values, (CommentMessage) ob);
            values.put("rt_num", rt_num);
            values.put("comment_num", comment_num);
        } else if (tableId == SinaWeiboDB.IM) {
            uriString += "/update/im";
            wrapIM(values, (Message) ob);
        }

        context.getContentResolver().update(Uri.parse(uriString), values, null,
                null);
        return true;
    }

    public static boolean delete(Context context, int tableId, String gid,
            String mgsid) {
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL;
        if (tableId == SinaWeiboDB.HOME) {
            uriString += "/delete/home";
        }
        context.getContentResolver().delete(Uri.parse(uriString), null,
                new String[] { gid, mgsid });
        return true;
    }

    public static boolean deleteAll(Context context, int tableId, String gid) {
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL;
        if (tableId == SinaWeiboDB.HOME) {
            uriString += "/delete/allhome";
        } else if (tableId == SinaWeiboDB.COMMENT) {
            uriString += "/delete/allcomment";
        }
        context.getContentResolver().delete(Uri.parse(uriString), null,
                new String[] { gid });
        return true;
    }

    public static boolean deleteAllIM(Context context, String gsid, String uid,
            boolean isSaveFailed) {
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL
                + "/delete/allim";
        context.getContentResolver().delete(Uri.parse(uriString), null,
                new String[] { gsid, uid, Boolean.toString(isSaveFailed) });
        return true;
    }

    public static boolean deleteIM(Context context, String localMsgid) {
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL
                + "/delete/im";
        context.getContentResolver().delete(Uri.parse(uriString), null,
                new String[] { localMsgid });
        return true;
    }

    public static boolean insert(Context context, List<?> lst, int tableId,
            String gid, String mgsid) {
        if (lst == null || lst.size() == 0) {
            return false;
        }
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL;
        if (tableId == SinaWeiboDB.HOME) {
            uriString += "/insert/home";
            int size = lst.size();
            ContentValues[] contentValues = new ContentValues[size];
            for (int j = 0; j < size; j++) {
                contentValues[j] = new ContentValues();
                wrapBlog(contentValues[j], (MBlog) lst.get(j));
            }
            contentValues[0].put("gid", gid);
            contentValues[0].put("gsid", mgsid);
            context.getContentResolver().bulkInsert(Uri.parse(uriString),
                    contentValues);
        } else if (tableId == SinaWeiboDB.COMMENT) {
            uriString += "/insert/comment";
            int size = lst.size();
            ContentValues[] contentValues = new ContentValues[size];
            for (int j = 0; j < size; j++) {
                contentValues[j] = new ContentValues();
                wrapComment(contentValues[j], (CommentMessage) lst.get(j));
            }
            contentValues[0].put("gid", gid);
            contentValues[0].put("gsid", mgsid);
            context.getContentResolver().bulkInsert(Uri.parse(uriString),
                    contentValues);
        } else if (tableId == SinaWeiboDB.IM) {
            uriString += "/insert/im";
            int size = lst.size();
            ContentValues[] contentValues = new ContentValues[size];
            for (int j = 0; j < size; j++) {
                contentValues[j] = new ContentValues();
                wrapIM(contentValues[j], (Message) lst.get(j));
            }
            contentValues[0].put("gsid", gid);
            contentValues[0].put("uid", mgsid);
            context.getContentResolver().bulkInsert(Uri.parse(uriString),
                    contentValues);
        }

        return true;
    }

    public static boolean insertIM(Context context, Message message,
            String gid, String mgsid) {
        if (message == null) {
            return false;
        }
        String uriString = "content://" + SinaWeiboDBProvider.CONTENT_URL
                + "/insert/im";
        ContentValues[] contentValues = new ContentValues[1];
        contentValues[0] = new ContentValues();
        wrapIM(contentValues[0], message);
        contentValues[0].put("gsid", gid);
        contentValues[0].put("uid", mgsid);
        context.getContentResolver().bulkInsert(Uri.parse(uriString),
                contentValues);

        return true;
    }

    @SuppressWarnings("unused")
    public static MBlog parseMBlog(ContentValues values) {
        MBlog blog = new MBlog();
        blog.mblogid = values.getAsString(SinaWeiboDB.MBLOG_ID);
        blog.uid = values.getAsString(SinaWeiboDB.UID);
        blog.favid = values.getAsString(SinaWeiboDB.FAV_ID);
        blog.nick = values.getAsString(SinaWeiboDB.NICK_NAME);
        blog.portrait = values.getAsString(SinaWeiboDB.PORTRAIT_URL);
        String vip = values.getAsString(SinaWeiboDB.IS_VIP);
        if (vip.equals("1")) {// yellow v
            blog.vip = true;
            blog.vipsubtype = false;
            blog.level = false;
        } else if (vip.equals("2")) {// blue v
            blog.vip = true;
            blog.vipsubtype = true;
            blog.level = false;
        } else if (vip.equals("3")) {// red v
            blog.vip = false;
            blog.vipsubtype = false;
            blog.level = true;
        } else {
            blog.vip = false;
            blog.vipsubtype = false;
            blog.level = false;
        }
        blog.content = values.getAsString(SinaWeiboDB.CONTENT);
        blog.rtrootuid = values.getAsString(SinaWeiboDB.RTROOT_UID);
        blog.rtrootnick = values.getAsString(SinaWeiboDB.RTROOT_NICK);
        vip = values.getAsString(SinaWeiboDB.RTROOT_VIP);
        if (vip.equals("1")) {
            blog.rtrootvip = true;
        } else {
            blog.rtrootvip = false;
        }
        blog.rtreason = values.getAsString(SinaWeiboDB.RT_REASON);
        blog.rtnum = values.getAsInteger(SinaWeiboDB.RT_NUM);
        blog.commentnum = values.getAsInteger(SinaWeiboDB.COMMENT_NUM);
        blog.time = new Date(values.getAsString(SinaWeiboDB.DATE));
        blog.pic = values.getAsString(SinaWeiboDB.PIC_URL);
        blog.src = values.getAsString(SinaWeiboDB.MBLOG_FROM);
        blog.rtrootid = values.getAsString(SinaWeiboDB.RTROOT_ID);
        blog.longitude = values.getAsString(SinaWeiboDB.LONGITUDE);
        blog.latitude = values.getAsString(SinaWeiboDB.LATITUDE);
        return blog;
    }

    @SuppressWarnings("unused")
    public static CommentMessage parseComment(ContentValues values) {
        CommentMessage blog = new CommentMessage();
        blog.commentid = values.getAsString(SinaWeiboDB.COMMENT_ID);
        blog.commentuid = values.getAsString(SinaWeiboDB.COMMENT_UID);
        blog.commentnick = values.getAsString(SinaWeiboDB.COMMENT_NICK);
        blog.commentportrait = values.getAsString(SinaWeiboDB.COMMENT_PORTRAIT);
        blog.commentcontent = values.getAsString(SinaWeiboDB.COMMENT_CONTENT);
        blog.mblogid = values.getAsString(SinaWeiboDB.COMMENT_MBLOG_ID);
        blog.mbloguid = values.getAsString(SinaWeiboDB.COMMENT_MBLOG_UID);
        blog.mblognick = values.getAsString(SinaWeiboDB.COMMENT_MBLOG_NICK);
        blog.mblogcontent = values
                .getAsString(SinaWeiboDB.COMMENT_MBLOG_CONTENT);
        blog.srcid = values.getAsString(SinaWeiboDB.COMMENT_SRC_ID);
        blog.srcuid = values.getAsString(SinaWeiboDB.COMMENT_SRC_UID);
        blog.srccontent = values.getAsString(SinaWeiboDB.COMMENT_SRC_CONTENT);
        blog.commenttime = new Date(values.getAsLong(SinaWeiboDB.DATE));
        return blog;
    }

    public static String makeUpRemark(Context ctx, String remark) {
        String brace1 = ctx.getString(R.string.bracel);
        String brace2 = ctx.getString(R.string.brace2);
        String ret = "";
        ret += brace1;
        ret += remark;
        ret += brace2;
        return ret;
    }

    public static Drawable getGlobalBackground(Context context) {
        Theme theme = Theme.getInstance(context);
        Drawable drawableRepeat = theme
                .getDrawableFromIdentifier(R.drawable.global_background);
        if (drawableRepeat instanceof BitmapDrawable) {
            ((BitmapDrawable) drawableRepeat).setTileModeXY(TileMode.REPEAT,
                    TileMode.REPEAT);
        }
        return drawableRepeat;
    }

    public static Drawable getListBackground(Context context) {
        Theme theme = Theme.getInstance(context);
        Drawable drawable = theme
                .getDrawableFromIdentifier(R.drawable.list_item_bg);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            Drawable drawableRepeat = theme
                    .getDrawableFromIdentifier(R.drawable.list_background);
            if (drawable instanceof StateListDrawable) {
                if (drawableRepeat instanceof BitmapDrawable) {
                    Rect rect = drawable.getBounds();
                    ((BitmapDrawable) drawableRepeat).setTileModeXY(
                            TileMode.REPEAT, TileMode.REPEAT);
                    drawableRepeat.setBounds(rect);
                }
                ((StateListDrawable) drawable).addState(new int[] {},
                        drawableRepeat);
            }
        }
        return drawable;
    }

    /**
     * 获取可用的夜间模式版本号
     */
    public static String getNightSkinVersion(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String version = properties
                    .getProperty("night_skin_version", "1.0");
            return version;
        } catch (IOException e) {
            return "1.0";
        }
    }

    /**
     * 关闭夜间模式后，获取就得皮肤
     * 
     * @param context
     * @return
     */
    public static String loadOldSkin(Context context) {
        SharedPreferences localSharedPreferences = context
                .getSharedPreferences("Theme", Context.MODE_PRIVATE);
        return localSharedPreferences.getString("oldSkin", "");
    }

    /**
     * 跳转到查看个人资料页面
     * 
     * @param context
     * @param uid
     * @param nick
     * @param isVip
     */
    public static void viewProfile(Context context, String uid, String nick,
            boolean isVip) {
        Intent i = new Intent(context, UserInfoActivity2.class);
        i.putExtra("uid", uid);
        i.putExtra("nick", nick);
        i.putExtra(UserInfoActivity2.KEY_USER_VIP, isVip);
        context.startActivity(i);
    }

    /**
     * 判断是否为当前activity
     * 
     * @param context
     * @param activityName
     *            for example： com.sina.weibo.SwitcherUser
     */
    public static boolean isCurrentActivity(Context context, String activityName) {
        if (context == null || TextUtils.isEmpty(activityName)) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> activityList = activityManager
                .getRunningTasks(1);
        int length = activityList.size();
        String activityListName = null;
        for (int i = 0; i < length; i++) {
            activityListName = activityList.get(i).topActivity.getClassName();
            if (activityName.equals(activityListName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * return Current activityName for example： com.sina.weibo.SwitcherUser
     */

    public static String getCurrentActivityName(Context context) {
        String activityName = null;
        if (context == null) {
            return null;
        }
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> activityList = activityManager
                .getRunningTasks(1);
        activityName = activityList.get(0).topActivity.getClassName();
        return activityName;
    }

    /**
     * get the location info from media/external/image provider
     * 
     */
    public static double[] queryExif(Context context, Uri uri) {
        Cursor c = null;
        if (uri.getScheme().equals("content")) {
            c = context.getContentResolver().query(
                    uri,
                    new String[] { MediaStore.Images.ImageColumns.LATITUDE,
                            MediaStore.Images.ImageColumns.LONGITUDE, }, null,
                    null, null);
        }

        double latitude = 0;
        double longitude = 0;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            latitude = c.getDouble(c
                    .getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
            longitude = c.getDouble(c
                    .getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
        }
        return new double[] { latitude, longitude };
    }

    public static int getContentLength(String text) {
        /*
         * 其实这里有一个很大的缺陷， 我们将所有非 ASCII 码中的字符全部按照 1 的中国汉字大小计算了
         */
        int totle = 0;
        char ch = 0;
        for (int i = 0, len = text.length(); i < len; i++) {
            ch = (char) text.codePointAt(i);
            totle += ch > 255 ? 2 : 1;
        }
        return (int) Math.ceil(totle / 2.);
    }

    public static String getTargetLengthString(String text, int length) {
        final char[] chs = text.toCharArray();
        for (int i = 0, total = 0, len = chs.length; i < len; i++) {
            total += chs[i] > 255 ? 2 : 1;
            if (total > length * 2) {
                return text.substring(0, i);
            }
        }
        return text;
    }

    public static File getUriFile(String filePath, Context context) {
        Uri uri = Uri.parse(filePath);
        File file = null;
        if (uri != null && uri.getScheme() != null
                && uri.getScheme().equals("content")) {
            try {

                String[] proj = { MediaStore.Images.Media.DATA };

                Cursor actualimagecursor = context.getContentResolver().query(
                        uri, proj, null, null, null);

                int actual_image_column_index = actualimagecursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                if (actualimagecursor.moveToFirst()) {

                    String img_path = actualimagecursor
                            .getString(actual_image_column_index);

                    file = new File(img_path);

                }
            } catch (Exception e) {

            }
        } else if (uri != null && uri.getScheme() != null
                && uri.getScheme().equals("file")) {
            file = new File(uri.getPath());
        } else {
            file = new File(filePath);
        }
        return file;
    }

    /**
     * 确保指定文件或者文件夹存在
     * 
     * @param file_
     * @return
     */
    public static boolean makesureFileExist(File file_) {
        AssertUtil.checkNull(file_);

        if (makesureParentDirExist(file_)) {
            if (file_.isFile()) {
                try {
                    return file_.createNewFile();
                } catch (IOException e) {
                }
            } else if (file_.isDirectory()) {
                return file_.mkdir();
            }
        }

        return false;
    }

    /**
     * 确保指定文件或者文件夹存在
     * 
     * @param filePath_
     * @return
     */
    public static boolean makesureFileExist(String filePath_) {
        AssertUtil.checkStringNullOrEmpty(filePath_);
        return makesureFileExist(new File(filePath_));
    }

    /**
     * 确保某文件或文件夹的父文件夹存在
     * 
     * @param file_
     */
    public static boolean makesureParentDirExist(File file_) {
        AssertUtil.checkNull(file_);
        final File parent = file_.getParentFile();
        if (parent == null || parent.exists())
            return true;
        return mkdirs(parent);
    }

    /**
     * 确保某文件的父文件夹存在
     * 
     * @param filepath_
     */
    public static boolean makesureParentDirExist(String filepath_) {
        AssertUtil.checkStringNullOrEmpty(filepath_);
        return makesureParentDirExist(new File(filepath_));
    }

    /**
     * 验证文件夹创建操作成功有否
     * 
     * @param dir_
     */
    public static boolean mkdirs(File dir) {
        if (dir == null)
            return false;
        return dir.mkdirs();
    }

    /**
     * 获取科大讯飞RecognizerDialog参数
     */
    public static boolean getNetParam(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String netParam = properties.getProperty("xunfei_netparam", "true");
            if ("true".equals(netParam)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取周边微博是否是本地优先
     * 
     * @param context
     */
    public static boolean nearByBlogfromLocal(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String netParam = properties.getProperty("nearbyblog_from_local",
                    "false");
            if ("true".equals(netParam)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

    }

    /**
     * 获取NetEngine的加载方式
     */
    public static boolean isLoadNetEngineFromNative(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String loadType = properties.getProperty(
                    "load_net_engine_from_native", "true");
            if ("true".equals(loadType)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否以".gif"结尾
     * 
     * @param str
     * @return
     */
    public static boolean isEndWithGif(String str) {
        if (str != null) {
            return str.toLowerCase().endsWith(".gif");
        }
        return false;
    }

    /**
     * 判断(lat1,lon1)到(lat2,lon2)的距离是否小于distanceMeters
     * 
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param distanceMeters
     * @return
     */
    public static boolean confirmDistance(double lat1, double lon1,
            double lat2, double lon2, int distanceMeters) {
        final double EARTH_RADIUS = 6378137;

        int deltaX = (int) (rad(lon2 - lon1) * EARTH_RADIUS * Math
                .cos(rad(lat1)));
        int deltaY = (int) (rad(lat2 - lat1) * EARTH_RADIUS);
        return Math.pow(deltaX, 2) + Math.pow(deltaY, 2) < distanceMeters
                * distanceMeters;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private static MediaPlayer gMediaPlayer = null;
    private static final Uri soundUri = Uri
            .parse("android.resource://com.sina.weibo/raw/newblogtoast");

    public static void playNewBlogAudio(Context context) {
        if (!SettingsPref.isAudioOn(context)) {
            return;
        }

        AudioManager mAudioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        if (current == 0) {// silence mode
            return;
        }
        if (gMediaPlayer == null) {
            gMediaPlayer = new MediaPlayer();
        }
        if (gMediaPlayer.isLooping() || gMediaPlayer.isPlaying()) {
            gMediaPlayer.stop();
        }
        try {
            gMediaPlayer.reset();
            gMediaPlayer.setDataSource(context, soundUri);
            gMediaPlayer.prepare();
            gMediaPlayer.start();
        } catch (Exception e) {
            Utils.loge(e);
            gMediaPlayer.release();
            gMediaPlayer = null;
            gMediaPlayer = new MediaPlayer();
        }
    }

    /**
     * 是否是新年版
     */
    public static boolean isNewNearVersion(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String loadType = properties.getProperty("new_year_splash", "true");
            if ("true".equals(loadType)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static String getFileNameFromUrl(String url) {
        return MD5.hexdigest(url);
    }

    public static String getTempFilePath(String filepath) {
        if (isEndWithGif(filepath)) {
            return filepath.substring(0, filepath.lastIndexOf(".")) + "_temp";
        } else {
            return filepath + "_temp";
        }
    }

    public static String getFileNameFromPath(String name) {
        int startIdx;
        int endIdx;
        if (name.contains("/")) {
            startIdx = name.lastIndexOf("/") + 1;
        } else {
            startIdx = 0;
        }
        if (isEndWithGif(name)) {
            endIdx = name.lastIndexOf(".");
        } else {
            endIdx = name.length();
        }
        return name.substring(startIdx, endIdx);
    }

    public static InputStream skipRead(InputStream is, int offset)
            throws IOException {
        long remainning = offset;
        long num = 0;
        while (remainning > 0) {
            num = is.skip(remainning);
            remainning -= num;
        }
        return is;
    }

    public static void stopDownload(String picName) {
        DownloadCallbackManager.getInstance().setDownloadStop(picName);
    }

    public static Bitmap getBitmap(String url, String cacheDir,
            Context context, boolean vip, boolean round, boolean reload,
            String suffix, IDownloadState callback) {
        Bitmap bmp = null;

        final String NOMAL_SIZE = context
                .getString(R.string.download_image_size_3); // wap240
        final String LARGE_SIZE = context
                .getString(R.string.download_image_size_5); // wap690
        final String ORIGINAL_SIZE = context
                .getString(R.string.download_image_size_6); // woriginal
        try {
            /*
             * if (url.contains(NOMAL_SIZE)) { // "wap240" bmp =
             * Utils.getPreviewBitmap(url, cacheDir, context, vip, round,
             * suffix, callback); } else
             */
            if (url.contains(LARGE_SIZE) || url.contains(ORIGINAL_SIZE)) { // "wap690"
                                                                           // "woriginal"
                String path = getLargeImage(context, url, reload, callback);

                if (!TextUtils.isEmpty(path)) {
                    Uri fileUri = Uri.parse("file://" + path);

                    File bmpFile = null;
                    if (!FileUtil.doesExisted(bmpFile)) {
                        bmpFile = new File(fileUri.getPath());
                    }

                    int rate = 1;
                    if (!Utils.isEndWithGif(url)) {
                        Rect size = new Rect();
                        for (int i = 0; i < 1024; i++) {
                            BitmapUtils.getZoomOutBitmapBound(bmpFile,
                                    rate = (int) Math.pow(2, i), size);
                            if (makesureSizeNotTooLarge(size)) {
                                break;
                            }
                        }
                    }

                    bmp = BitmapUtils.createZoomOutBitmap(bmpFile, rate);
                    bmp = checkBitMapHeight(context, bmp, true);
                    if (bmp == null && bmpFile != null && bmpFile.exists()) {
                        bmpFile.delete();
                    }
                }
            } else { // "wap240" &
                     // "http://s1.sina.cn/tv/soft/weibo/nightmod3.png"(主题图预览)
                bmp = Utils.getPreviewBitmap(url, cacheDir, context, vip,
                        round, suffix, callback);
            }
        } catch (Exception e) {
            // Utils.loge(e);
            return null;
        }
        return bmp;
    }

    /*
     * 开启wifi优化时，在正文页预览图片，如果图片的高度过大，显示图片时会按自适应AlertDialog的最大适应高度进行显示。
     * 此时可以先对图片进行缩放到屏幕高度，然后再显示出来，这样显示既能解决4.0无法显示的bug，又能通过使用较小的bitmap避免OOM
     */
    private static Bitmap checkBitMapHeight(Context context, Bitmap bitmap,
            boolean needCheck) {
        if (needCheck) {
            int maxHeight = context.getResources().getDimensionPixelSize(
                    R.dimen.image_dialog_max_height);

            if (bitmap != null && bitmap.getHeight() > maxHeight) {
                Bitmap bmpScaledOut = BitmapUtils.createScaledBitmap(bitmap,
                        bitmap.getWidth() * maxHeight / bitmap.getHeight(),
                        maxHeight, Bitmap.Config.ARGB_4444); // 获得等屏幕高度的位图
                if (bmpScaledOut != null && !bmpScaledOut.isRecycled()) {
                    bitmap.recycle();
                    bitmap = bmpScaledOut;
                }
            }
        }

        return bitmap;
    }

    private static String getLargeImage(Context context, String url,
            boolean reload, IDownloadState callback) {
        try {
            final String IMAGE_CACHE_DIR = context.getCacheDir()
                    .getAbsolutePath() + Utils.DIR_LARGE_IMAGE;
            final String sdDir = Utils.getSDPath();
            final String saveDir = (TextUtils.isEmpty(sdDir) || !Utils
                    .haveFreeSpace()) ? IMAGE_CACHE_DIR : sdDir
                    + Constants.ORI_PIC_DIR_SUFFIX;
            if (!FileUtil.doesExisted(saveDir)) {
                new File(saveDir).mkdirs();
            }

            return NetEngineFactory.getNetInstance(context).getPicture(url,
                    saveDir, reload, callback, null);
        } catch (WeiboIOException e) {
            // Utils.loge(e);
        } catch (WeiboApiException e) {
            // Utils.loge(e);
        }

        return null;
    }

    private static boolean makesureSizeNotTooLarge(Rect rect) {
        if (rect.width() * rect.height() * 2 > 5 * 1024 * 1024) {
            // 不能超过5M
            return false;
        }

        return true;
    }

    /**
     * 获取当前的版本号
     */
    private static String currentVersionCode;

    public static String getVersionCode(Context context) {
        if (!TextUtils.isEmpty(currentVersionCode)) {
            return currentVersionCode;
        }
        try {
            currentVersionCode = context.getPackageManager().getPackageInfo(
                    Constants.PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS).versionName;
        } catch (NameNotFoundException e) {
            currentVersionCode = "";
        }
        return currentVersionCode;
    }

    public static void setCurrentActivity(Activity act) {
        mCurrentActivity = act;
    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public static final String VERSION_FLAG = "version_flag";
    private static Boolean isNewVersion = null;

    /**
     * 用于判断更多tab上是否显示“new”图标
     * 
     * @param context
     * @return
     */
    public static boolean isNewVersion(Context context) {
        if (isNewVersion == null) {
            SharedPreferences preferences = context.getSharedPreferences(
                    MainTabActivity.PREF_NAME, Context.MODE_PRIVATE);
            boolean old = preferences.getBoolean(VERSION_FLAG, false);
            isNewVersion = !old;
        }
        return isNewVersion;
    }

    /**
     * 添加新版本标识，用于判断更多tab上是否显示“new”图标
     * 
     * @param context
     * @return
     */
    public static void saveNewVersionFlag(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                MainTabActivity.PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(VERSION_FLAG, true);
        editor.commit();
        isNewVersion = false;
    }

    /*
     * 获取是否显示引导页
     */
    public static boolean isShowNavigaterActivity(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String loadType = properties.getProperty("show_navigateractivity",
                    "true");
            if ("true".equals(loadType)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isShowSelectableInNavigater(Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("platform.properties"));
            String loadType = properties.getProperty(
                    "show_selectable_in_navigater", "true");
            if ("true".equals(loadType)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    // @author:lgz
    // for calculate available space of sdcard
    public static boolean haveFreeSpace() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            StatFs st = new StatFs(Environment.getExternalStorageDirectory()
                    .getPath());
            int blockSize = st.getBlockSize();
            long available = st.getAvailableBlocks();
            long availableSize = (blockSize * available);
            if (availableSize < Constants.MIN_SDCARD_SPACE) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static long getFreeSpace() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            StatFs st = new StatFs(Environment.getExternalStorageDirectory()
                    .getPath());
            int blockSize = st.getBlockSize();
            long available = st.getAvailableBlocks();
            long availableSize = (blockSize * available);
            // Log.v("lgz","availablesize:)"+availableSize);
            return availableSize;
        }
        return 0;
    }

    public static boolean isHardwareAccelerated(View view) {
        boolean accelerated = false;
        try {
            Class viewClass = View.class;
            Method isHardwareAccelerated = viewClass.getMethod(
                    "isHardwareAccelerated", null);
            accelerated = (Boolean) isHardwareAccelerated.invoke(view, null);
        } catch (Exception e) {
            loge(e);
        }

        return accelerated;
    }
}

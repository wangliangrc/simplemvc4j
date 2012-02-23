package com.sina.weibo;

import android.app.Application;

public class WeiboApplication extends Application {
    // public static final String CURRENT_PREFS_VERSION = "CURRENTVERSION";
    // public static final String CURRENT_PREFS_VERSION_NUM =
    // "currentverisonnum";
    // public static final String LOGIN_VERSION = "loginverion";
    // public static final String UPLOAD_LOG = "UPLOAD_LOG";
    // public static final String HAS_UPLOAD_LOG = "has_upload_log";
    // public static final String HAS_IMPORT_USER_INFO_TO_DB =
    // "has_import_user_info_to_db";
    // public static final String PREF_HAS_IMPORT = "has_import";
    // public static final int LOGINOLD = 1;
    // public static final int LOGINNEW = 2;
    // public static final int LOGINDES1 = 3;
    // public static String HARDWARE_VERSION = "No hardware version";
    public static String DEVICE_NAME = "No device name";
    // public static String TELEPHONE_NUM = "";
    // public static String IMEI_NUM = "";
    // public static String CACHE_DIR = "";
    public static String ME = "";
    // public static Context globleContext;
    // public static String UA = "";
    public static String VERSION = "";
    //
    // public static String MAIN_WM = "3333_1001";
    // public static String MARKET_WM = "3335";
    // public static String SHARE_WM_KEY = "wm";
    // public static String SHARE_WM = "share_wm";
    //
    // public static boolean isFastScroll = true;
    // TITLE BAR
    // public final static int HIGH_MODE = 0;
    // public final static int MIDDLE_MODE = 1;
    // public final static int LOW_MODE = 2;
    // app width and height
    // private static int HEIGHT = 0;
    // private static int WIDTH = 0;
    // private JSONObject wmObj;
    //
    // public static String perPass;
    // public static String perName;
    // private static String mGsid;
    // private static String mUid;
    // private static boolean cleanPor=false;
    // private List<User> listAccount ;
    //
    // private static int DENSITY_DPI = DisplayMetrics.DENSITY_MEDIUM;
    // public static HashMap<String, String> mSmsRegNumMap;

    // public static int getWeiboDensityDpi() {
    // return WeiboApplication.DENSITY_DPI;
    // }
    //
    // public static int getTitleDisplayMode() {
    // int height;
    // int width;
    // // 保证 height >= width
    // if (HEIGHT >= WIDTH) {
    // height = HEIGHT;
    // width = WIDTH;
    // } else {
    // height = WIDTH;
    // width = HEIGHT;
    // }
    //
    // if (800 <= height || 800 <= width) {
    // return HIGH_MODE;
    // } else if (320 >= height || 240 >= width) {
    // return LOW_MODE;
    // } else {
    // return MIDDLE_MODE;
    // // Utils.loge("45");
    // }
    // }
    //
    // public static void setDisplay(int width, int height, int dpi) {
    // HEIGHT = height;
    // WIDTH = width;
    // DENSITY_DPI = dpi;
    // }
    //
    // public static WeiboApplication instance = null;

    // private BroadcastReceiver mRestartReceiver = new BroadcastReceiver() {
    // public void onReceive(Context context, Intent intent) {
    // Log.d("duncanRestart", "restart");
    // // Intent i = getBaseContext().getPackageManager()
    // // .getLaunchIntentForPackage(
    // // getBaseContext().getPackageName());
    // // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    // // startActivity(i);
    // // android.os.Process.killProcess(android.os.Process.myPid());
    // }
    // };

    // /**
    // * 版本兼容：
    // * 2.6.0版本添加了在User中添加了oauth_toker 和 oauth_token_secret
    // * 但是没有进行加密
    // * 2.8.1版本将这两个字段进行加密，并对以前版本进行兼容。
    // * 2.4.5 beta2之前的版本（不包含2.4.5 beta2）没有添加版本号，
    // * 在此之前的版本中含有PREFS_VERSION sharedPreferen的版本采用了des1加密
    // * 没有改SharedPreference的版本没有加密
    // * 从2.4.5 beta2 开始添加版本号
    // * 2.4.5 beta2、2.4.5采用的是des1加密，之后的版本采用的是des2加密
    // * 从2.5.0版本开始，在内存中不再保存用户密码、用户信息中添加gsid、uid
    // * 在weiboApplication中添加调用LoginPreUserService的代码,该service
    // * 主要是讲2.5.0之前的版本中保留的密码清除，并获取gsid和uid重新保存
    // * 从2.5.1起，账号信息保存数据库中
    // *
    // * 另外，在某些适配版本的2.0.4版本采用了des1加密、增加了版本号，在做版本兼容的测试中
    // * 只需要兼容添加版本号和采用des1加密的2.0.4版本即可。
    // */
    // public void onCreate() {
    // BmpCache.getInstance().clear();
    // setWM();
    // SettingsPref.changeLocale(this);
    // Utils.SetWeiboDB(SinaWeiboDB.getInstance(this, null));
    //
    // PackageInfo info;
    // try {
    // info = getPackageManager().getPackageInfo(getPackageName(), 0);
    // VERSION = info.versionName;
    // } catch (NameNotFoundException e) {
    // Utils.loge(e);
    // }
    //
    //
    // DesEncrypt2 des2 = new DesEncrypt2();
    // SharedPreferences prefs = getSharedPreferences(Utils.PREFS_NAME, 0);
    //
    // if(prefs != null) {
    // perName = prefs.getString(Utils.PREFS_USERNAME, "");
    // perPass = prefs.getString(Utils.PREFS_PASSWORD, "");
    // // MainTabActivity.mGsid =
    // des.getDesString(prefs.getString(Utils.PREFS_GSID, ""));
    // String miPass = prefs.getString(Utils.PREFS_GSID, "");
    // if(TextUtils.isEmpty(miPass)){
    // mGsid = null;
    // }else{
    // mGsid = des2.getDesString(miPass);
    // }
    // mUid = prefs.getString(Utils.PREFS_UID, "");
    // }
    //
    // // MainTabActivity.mGsid = mGsid;
    // // MainTabActivity.mUid = mUid;
    // SharedPreferences cleanPorPrefs =
    // getSharedPreferences(Utils.PREFS_CLEANPOR, 0);
    // cleanPor = cleanPorPrefs.getBoolean(Utils.PREFS_CLEANPOR_STATUS, false);
    //
    // SharedPreferences versionPrefs =
    // getSharedPreferences(CURRENT_PREFS_VERSION,0);
    // String versionName = versionPrefs.getString(CURRENT_PREFS_VERSION_NUM,
    // null);
    // if (versionName == null || !versionName.equals(VERSION)) {
    // //第一次覆盖安装，清除显示过引导页的标志位
    // SharedPreferences preferences =
    // getSharedPreferences(MainTabActivity.PREF_NAME,
    // MODE_PRIVATE);
    // preferences.edit().putBoolean(MainTabActivity.KEY_SHOWN, false).commit();
    //
    // getSharedPreferences(UPLOAD_LOG, MODE_PRIVATE).edit()
    // .putBoolean(HAS_UPLOAD_LOG, false).commit();
    // }
    //
    // SharedPreferences verPrefs =
    // getSharedPreferences(VersionCenter.PREFS_VERSION, 0);
    // long lastCheck = verPrefs.getLong(VersionCenter.PREFS_LAST_CHECK_TIME,
    // -1);
    //
    // boolean importDataSucc = false;
    // updateVersionName();
    // /**
    // if(versionName != null && versionName.startsWith("2.5.1") &&
    // versionName.startsWith("2.5.2")){
    //
    // User user =
    // Utils.loadUserFile(getApplicationContext().getCacheDir().getPath());
    // if(user != null) {
    // StaticInfo.mUser = user;
    // StaticInfo.mUsername = user.name;
    // }
    // if(!versionName.equals(VERSION)) {
    // SharedPreferences.Editor editorVersion = versionPrefs.edit();
    // editorVersion.putString(CURRENT_PREFS_VERSION_NUM, VERSION);
    // editorVersion.commit();
    // }
    // importDataSucc = true;
    //
    // }else */
    //
    // SharedPreferences importPrefs =
    // getSharedPreferences(HAS_IMPORT_USER_INFO_TO_DB, 0);
    // boolean isImport = importPrefs.getBoolean(PREF_HAS_IMPORT, false);
    // /**
    // * 新的逻辑进行版本兼容，根据是否把账户信息导入到数据库中进行判断
    // * 从2.5.1起，账号信息保存数据库中
    // */
    // if(!isImport) {
    // /**
    // * 如果没有导入到数据库中，启用老的覆盖逻辑
    // * 几个特殊版本使用了Des1加密
    // */
    // if(versionName != null &&(versionName.equals("2.0.4")
    // ||versionName.equals("2.4.5 beta2") || versionName.equals("2.4.5"))){
    //
    // /**
    // * 读取当前用户信息，将用户信息采用新的加密方式
    // */
    // User user =
    // Utils.loadUserFileDes1(getApplicationContext().getCacheDir().getPath());
    // Utils.saveUserFile(user, getApplicationContext().getCacheDir().getPath(),
    // getApplicationContext());
    //
    // /**
    // * 读取accoutlist信息，将信息采用新的加密方式
    // */
    // listAccount = AccountHelper.loadAccountsDes1(getApplicationContext());
    // AccountHelper.saveAccounts(getApplicationContext(), listAccount);
    //
    // /**
    // * 在网络状态下，启动service，将accountlist中用户获得gsid，去掉密码后再保存
    // */
    // if(isNetworkAvailable() && !TextUtils.isEmpty(perPass)){
    // Intent intent = new
    // Intent(getApplicationContext(),LoginPreUserService.class);
    // intent.putExtra(LOGIN_VERSION, LOGINDES1);
    // this.startService(intent);
    // }
    //
    // if(user != null && !TextUtils.isEmpty(perPass)){
    // if(!TextUtils.isEmpty(perName)) {
    // user.name = perName;
    // }
    // if(!TextUtils.isEmpty(mGsid)) {
    // user.gsid = mGsid;
    // }
    // if(!TextUtils.isEmpty(mUid)) {
    // user.uid = mUid;
    // }
    // Utils.saveUserFile(user, getApplicationContext().getCacheDir().getPath(),
    // getApplicationContext());
    // }
    //
    // /**
    // SharedPreferences.Editor editorVersion = versionPrefs.edit();
    // editorVersion.putString(CURRENT_PREFS_VERSION_NUM, VERSION);
    // editorVersion.commit();
    // */
    // importDataSucc = true;
    //
    // }else if(versionName != null && versionName.startsWith("2.5.0")) {
    //
    // User user =
    // Utils.loadUserFileDes2(getApplicationContext().getCacheDir().getPath());
    // listAccount = AccountHelper.loadAccountsDes2(getApplicationContext());
    // AccountHelper.saveAccounts(getApplicationContext(), listAccount);
    //
    // if(user != null) {
    // if(listAccount != null && listAccount.size() > 0) {
    // for(User u : listAccount) {
    // if(u.uid != null && u.uid.equals(user.uid)) {
    // user.name = u.name;
    // }
    // }
    // }
    // Utils.saveUserFile(user, getApplicationContext().getCacheDir().getPath(),
    // getApplicationContext());
    // }
    //
    // /**
    // SharedPreferences.Editor editorVersion = versionPrefs.edit();
    // editorVersion.putString(CURRENT_PREFS_VERSION_NUM, VERSION);
    // editorVersion.commit();
    // */
    // importDataSucc = true;
    //
    // }else if(versionName != null && (versionName.startsWith("2.6.0")
    // || versionName.startsWith("2.7.0") || versionName.startsWith("2.8.0"))){
    // //将2.6.0、2.7.0、2.8.0版本中的oauth两个字段进行加密
    // User user =
    // Utils.loadUserFileOauth(getApplicationContext().getCacheDir().getPath());
    // listAccount =
    // AccountHelper.loadAccountsFromDBOauth(getApplicationContext());
    // AccountHelper.saveAccounts(getApplicationContext(), listAccount);
    // Utils.saveUserFile(user, getApplicationContext().getCacheDir().getPath(),
    // getApplicationContext());
    // }else if(versionName == null){
    // User user;
    // if(lastCheck == -1){
    // /**
    // * 未加密版本，读取User采用不加密的方式
    // */
    // user = (User) load(getApplicationContext().getCacheDir().getPath() +
    // Utils.USER_FILE);
    // listAccount = loadAccounts();
    // AccountHelper.saveAccounts(getApplicationContext(), listAccount);
    // if(isNetworkAvailable()){
    // Intent intent = new
    // Intent(getApplicationContext(),LoginPreUserService.class);
    // intent.putExtra(LOGIN_VERSION, LOGINOLD);
    // this.startService(intent);
    // }
    //
    // if(user != null) {
    // if(!TextUtils.isEmpty(perName)) {
    // user.name = perName;
    // }
    // if(!TextUtils.isEmpty(mGsid)) {
    // user.gsid = mGsid;
    // }
    // if(!TextUtils.isEmpty(mUid)) {
    // user.uid = mUid;
    // }
    // Utils.saveUserFile(user, getApplicationContext().getCacheDir().getPath(),
    // getApplicationContext());
    // }
    //
    // }else{
    // // StaticInfo.mPassword = des.getDesString(perPass);
    // /**
    // * 其他没有在以上方式中包含的版本，这些版本没有保存版本号
    // */
    // user =
    // Utils.loadUserFileDes1(getApplicationContext().getCacheDir().getPath());
    //
    // listAccount = AccountHelper.loadAccountsDes1(getApplicationContext());
    // AccountHelper.saveAccounts(getApplicationContext(), listAccount);
    // if(isNetworkAvailable() && !TextUtils.isEmpty(perPass)){
    // Intent intent = new
    // Intent(getApplicationContext(),LoginPreUserService.class);
    // intent.putExtra(LOGIN_VERSION, LOGINNEW);
    // this.startService(intent);
    // }
    //
    // if(user != null && !TextUtils.isEmpty(perPass)) {
    // if(!TextUtils.isEmpty(perName)) {
    // user.name = perName;
    // }
    // if(!TextUtils.isEmpty(mGsid)) {
    // user.gsid = mGsid;
    // }
    // if(!TextUtils.isEmpty(mUid)) {
    // user.uid = mUid;
    // }
    // Utils.saveUserFile(user, getApplicationContext().getCacheDir().getPath(),
    // getApplicationContext());
    // }
    //
    // }
    //
    // /**
    // SharedPreferences.Editor editorVersion = versionPrefs.edit();
    // editorVersion.putString(CURRENT_PREFS_VERSION_NUM, VERSION);
    // editorVersion.commit();
    // */
    // importDataSucc = true;
    //
    // }else if(versionName != null && !importDataSucc){
    //
    // User user =
    // Utils.loadUserFileDes2(getApplicationContext().getCacheDir().getPath());
    //
    // if(user != null && !TextUtils.isEmpty(perPass)){
    // if(!TextUtils.isEmpty(perName)) {
    // user.name = perName;
    // }
    // if(!TextUtils.isEmpty(mGsid)) {
    // user.gsid = mGsid;
    // }
    // if(!TextUtils.isEmpty(mUid)) {
    // user.uid = mUid;
    // }
    // Utils.saveUserFile(user, getApplicationContext().getCacheDir().getPath(),
    // getApplicationContext());
    // }
    //
    // /**
    // SharedPreferences.Editor editorVersion = versionPrefs.edit();
    // editorVersion.putString(CURRENT_PREFS_VERSION_NUM, VERSION);
    // editorVersion.commit();
    // */
    // importDataSucc = true;
    //
    // }
    //
    // }
    //
    // if(importDataSucc) {
    // /**
    // * 导入数据库成功
    // */
    // updateImportDbTag();
    // clearNotUsefulFile();
    // }
    //
    // instance = this;
    // // IntentFilter myIntentFilter2 = new IntentFilter();
    // // myIntentFilter2.addAction("com.sina.weibo.restart");
    // // registerReceiver(mRestartReceiver, myIntentFilter2);
    // super.onCreate();
    // setCid();
    // setPin();
    // setAppKey();
    // setAppSectet();
    // ME = this.getString(R.string.me);
    // HEIGHT = 0;
    // WIDTH = 0;
    // globleContext = this.getBaseContext();
    // CACHE_DIR = this.getCacheDir().getAbsolutePath();
    //
    // TelephonyManager tm = (TelephonyManager)
    // getSystemService(TELEPHONY_SERVICE);
    // TELEPHONE_NUM = tm.getLine1Number();
    // IMEI_NUM = tm.getDeviceId();
    // if(IMEI_NUM == null){
    // IMEI_NUM = "";
    // }
    // HARDWARE_VERSION = Build.VERSION.RELEASE;
    // DEVICE_NAME = Build.MODEL;
    // UA = DEVICE_NAME + "_" + HARDWARE_VERSION + "_" + Constants.TAG + "_"
    // + getVersionName() + "_" + Constants.CID;
    // // Utils.loge(UA);
    // try{
    // UploadUserLogTask task = new UploadUserLogTask();
    // task.execute();
    // }catch(RejectedExecutionException e){
    // Utils.loge(e);
    // }
    //
    // //删除sina/weibo/pre/、sina/weibo/portrait/
    // if(!cleanPor){
    // new Thread(){
    // public void run(){
    // cleanSdDir();
    // }
    // }.start();
    // }
    //
    //
    // if(StaticInfo.mUser == null){
    // StaticInfo.mUser = Utils.loadUserFile(getCacheDir().getAbsolutePath());
    // }
    // // if (TextUtils.isEmpty(StaticInfo.mUsername)
    // // || TextUtils.isEmpty(StaticInfo.mPassword)
    // // || StaticInfo.mUser == null) {
    // // Utils.logd("warning...");
    // // }
    // if (TextUtils.isEmpty(StaticInfo.mUsername)
    // || StaticInfo.mUser == null) {
    // Utils.logd("warning...");
    // }
    // if (WeiboApplication.mSmsRegNumMap == null
    // || WeiboApplication.mSmsRegNumMap.isEmpty()
    // || WeiboApplication.mSmsRegNumMap.size() != 3) {
    // new Thread() {
    // public void run() {
    // try {
    // WeiboApplication.mSmsRegNumMap = RPCHelper.getInstance(
    // WeiboApplication.this).getRegSmsNum();
    // } catch (Exception e) {
    // Utils.loge(e);
    // }
    // }
    // }.start();
    // }
    // }
    //
    //
    // private void updateVersionName() {
    // SharedPreferences versionPrefs =
    // getSharedPreferences(CURRENT_PREFS_VERSION,0);
    // SharedPreferences.Editor editorVersion = versionPrefs.edit();
    // editorVersion.putString(CURRENT_PREFS_VERSION_NUM, VERSION);
    // editorVersion.commit();
    // }
    //
    // private void updateImportDbTag() {
    // SharedPreferences importPrefs =
    // getSharedPreferences(HAS_IMPORT_USER_INFO_TO_DB, 0);
    // SharedPreferences.Editor editor = importPrefs.edit();
    // editor.putBoolean(PREF_HAS_IMPORT, true);
    // editor.commit();
    // }
    //
    // /**
    // * 对无用的无用的文件进行清理
    // */
    // private void clearNotUsefulFile() {
    //
    // SharedPreferences pref1 = getSharedPreferences(Utils.PREFS_NAME, 0);
    // Editor editor1 = pref1.edit();
    // editor1.clear();
    // editor1.commit();
    //
    // SharedPreferences pref2 =
    // getSharedPreferences(AccountHelper.PREFS_USER_FILE, 0);
    // Editor editor2 = pref2.edit();
    // editor2.clear();
    // editor2.commit();
    //
    // File f1 = new File(getCacheDir() + Utils.USER_FILE);
    // if(f1.exists()) {
    // f1.delete();
    // }
    //
    // File f2 = new File(getCacheDir() + SwitchUser.USRNAMELISTPATH);
    // if(f2.exists()) {
    // f2.delete();
    // }
    //
    // }
    //
    //
    // public void onLowMemory() {
    // super.onLowMemory();
    // }
    //
    // public void onTerminate() {
    // super.onTerminate();
    // }
    //
    // private String getVersionName() {
    // PackageManager pm = getPackageManager();
    // String versionName;
    // PackageInfo pi;
    // try {
    // pi = pm.getPackageInfo(getPackageName(),
    // PackageManager.GET_SIGNATURES);
    // versionName = pi.versionName;
    // } catch (NameNotFoundException e) {
    // Utils.loge(e);
    // versionName = "";
    // }
    // return versionName;
    // }
    //
    // public Object load(String path) {
    // Object obj = null;
    // File file = new File(path);
    // try {
    // /*if(file != null){
    // file.mkdirs();
    // }*/
    // if (file.exists()) {
    // FileInputStream fis = new FileInputStream(file);
    // ObjectInputStream ois = new ObjectInputStream(fis);
    // try {
    // obj = ois.readObject();
    // }
    // catch (ClassNotFoundException e) {
    // Utils.loge(e);
    // }
    // ois.close();
    // }
    // }catch (IOException e) {
    // Utils.loge(e);
    // }
    // return obj;
    // }
    //
    // private List<User> loadAccounts() {
    // String user;
    // String pass;
    // String nick;
    // String gsid;
    // String uid;
    // SharedPreferences settings =
    // getSharedPreferences(AccountHelper.PREFS_USER_FILE, 0);
    // List<User> accounts = new ArrayList<User>();
    // // only load five accouts
    // for (int i = 0; i < 5; i++) {
    // user = settings.getString(AccountHelper.PREFS_USER_NAME + i, "");
    // pass = settings.getString(AccountHelper.PREFS_PASS_WORD + i, "");
    // nick = settings.getString(AccountHelper.PREFS_NICK_NAME + i, "");
    // gsid = settings.getString(AccountHelper.PREFS_USER_GSID + i, "");
    // uid = settings.getString(AccountHelper.PREFS_USER_UID + i, "");
    // if (user.length() > 0) {
    // User account = new User();
    // account.name = user;
    // account.pass = pass;
    // account.nick = nick;
    // account.gsid = gsid;
    // account.uid = uid;
    // accounts.add(account);
    // }
    // }
    //
    // return accounts;
    // }
    //
    // private void setWM() {
    // AssetManager assetManager = getAssets();
    // try {
    // InputStream is = assetManager.open("cfg.json");
    // byte[] buffer = new byte[is.available()];
    // while (is.read(buffer) != -1)
    // ;
    // String json = new String(buffer);
    // wmObj = new JSONObject(json);
    // String wm = (String) wmObj.get("WM");
    //
    //
    // SharedPreferences preferences = getSharedPreferences(SHARE_WM,
    // Context.MODE_PRIVATE);
    // String oldWM = preferences.getString(SHARE_WM_KEY, "");
    // if(oldWM.length() == 0){
    // Editor editor = preferences.edit();
    // editor.putString(SHARE_WM_KEY, wm);
    // editor.commit();
    // Constants.WM = wm;
    // }else if(oldWM.equals(wm)){
    // Constants.WM = wm;
    // }else{
    // if(wm.equals(MAIN_WM) || wm.equals(MARKET_WM)){
    // Constants.WM = oldWM;
    // }else{
    // Editor editor = preferences.edit();
    // editor.putString(SHARE_WM_KEY, wm);
    // editor.commit();
    // Constants.WM = wm;
    // }
    // }
    // } catch (IOException e) {
    // Utils.loge(e);
    // } catch (JSONException e) {
    // Utils.loge(e);
    // }
    // }
    //
    // private void setCid(){
    // String cid = null;
    // try {
    // cid = (String)wmObj.getString("CID");
    // if(cid == null || cid.equals("")){
    // cid = Constants.CID;
    // }
    // } catch (JSONException e) {
    // cid = Constants.CID;
    // Utils.loge(e);
    // }
    // if(!TextUtils.isEmpty(cid)){
    // Constants.CID = Utils.getDecryptionString(cid);
    // }
    // }
    //
    // private void setPin(){
    // String pin = null;
    // try {
    // pin = (String)wmObj.getString("KEY");
    // if(pin == null || pin.equals("")){
    // pin = Constants.KEY;
    // }
    // } catch (JSONException e) {
    // pin = Constants.KEY;
    // Utils.loge(e);
    // }
    // if(!TextUtils.isEmpty(pin)){
    // Constants.KEY = Utils.getDecryptionString(pin);
    // }
    // }
    //
    // private void setAppKey(){
    // String appKey = null;
    // try {
    // appKey = (String)wmObj.getString("APPKEY");
    // if(appKey == null || appKey.equals("")){
    // appKey = Constants.APP_KEY;
    // }
    // } catch (JSONException e) {
    // appKey = Constants.APP_KEY;
    // Utils.loge(e);
    // }
    // if(!TextUtils.isEmpty(appKey)){
    // Constants.APP_KEY = Utils.getDecryptionString(appKey);
    // }
    // }
    //
    // private void setAppSectet(){
    // String appSecret = null;
    // try {
    // appSecret = (String)wmObj.getString("APPSECRET");
    // if(appSecret == null || appSecret.equals("")){
    // appSecret = Constants.APP_SECRET;
    // }
    // } catch (JSONException e) {
    // appSecret = Constants.APP_SECRET;
    // Utils.loge(e);
    // }
    // if(!TextUtils.isEmpty(appSecret)){
    // Constants.APP_SECRET = Utils.getDecryptionString(appSecret);
    // }
    // }
    //
    // /**
    // * 用户数据收集
    // * @author nieyu
    // *
    // */
    // private class UploadUserLogTask extends AsyncTask<Void, Void, Void> {
    // protected Void doInBackground(Void... args) {
    //
    //
    //
    // SharedPreferences preferences = getSharedPreferences(UPLOAD_LOG,
    // MODE_PRIVATE);
    // boolean old = preferences.getBoolean(HAS_UPLOAD_LOG, false);
    // if(old){
    // return null;
    // }
    // boolean b = false;
    // try {
    // NetResult result =
    // NetEngineFactory.getNetInstance(WeiboApplication.this).uploadUserLog(WeiboApplication.this);
    // b= result.isSuccessful();
    // }
    // catch (WeiboApiException e) {
    // Utils.loge(e);
    // }
    // catch (WeiboIOException e) {
    // Utils.loge(e);
    // }
    // catch (WeiboParseException e) {
    // Utils.loge(e);
    // }
    // if(b){
    // Editor editor = preferences.edit();
    // editor.putBoolean(HAS_UPLOAD_LOG, b);
    // editor.commit();
    // }
    // return null;
    // }
    // protected void onPostExecute() {
    //
    // }
    // }
    //
    // public boolean isNetworkAvailable() {
    // Context context = getApplicationContext();
    // ConnectivityManager connectivity = (ConnectivityManager) context
    // .getSystemService(context.CONNECTIVITY_SERVICE);
    // if (connectivity == null) {
    // return false;
    // }else{
    // NetworkInfo[] info = connectivity.getAllNetworkInfo();
    // if (info != null) {
    // for (int i = 0; i < info.length; i++) {
    // if (info[i].getState() == NetworkInfo.State.CONNECTED) {
    // return true;
    // }
    // }
    // }
    // }
    // return false;
    // }
    //
    // static {
    // System.loadLibrary("utility");
    // }
    // public native String calculateS(String srcArray);
    // public native String getDecryptionString(String input);
    // public native INetEngine getNetInstance( Context context, String
    // versionCode );
    //
    // private void cleanSdDir() {
    // String preDir = TextUtils.isEmpty(Utils.getSDPath()) ? "" :
    // Utils.getSDPath() + Constants.OLD_PREVIEW_BMP_DIR_SUFFIX;
    // String portraitDir = TextUtils.isEmpty(Utils.getSDPath()) ? "" :
    // Utils.getSDPath() + Constants.OLD_PORTRAIT_DIR_SUFFIX;
    // Utils.clearDirectory(new File(preDir));
    // Utils.clearDirectory(new File(portraitDir));
    // getSharedPreferences(Utils.PREFS_CLEANPOR, 0).edit().
    // putBoolean(Utils.PREFS_CLEANPOR_STATUS, true).commit();
    // }
    //
}

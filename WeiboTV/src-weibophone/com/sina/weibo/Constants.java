package com.sina.weibo;

public class Constants {
public static final String PACKAGE_NAME ="com.sina.weibo";
	
	public static String USER_AGENT = "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.1.4) Gecko/20091111 Gentoo Firefox/3.5.4";
	public static int HTTP_STATUS_OK = 200;
	public static int HTTP_STATUS_DOWNLOAD_OK = 206;
	public static String SERVER = "http://api.weibo.cn/interface/f/ttt/v3/";
	public static String SERVER_V4 = "http://202.108.37.212/interface/f/ttt/v3/";
	// String SERVER = "http://3g.sina.com.cn/interface/f/ttt/v3/";
	//String SERVER = "http://218.30.115.93/interface/f/ttt/v3/";
	public static String SERVER212 = "http://202.108.37.212/interface/f/ttt/v3/";
	
	//预览头像、缩略图等的缓存文件夹
	public static String SDCARD_WEIBO_CACHE_DIR = "/" + "sina/weibo/";
	public static String PREVIEW_BMP_DIR_SUFFIX = "/" + "sina/weibo/.pre/";
	public static String PORTRAIT_DIR_SUFFIX = "/" + "sina/weibo/.portrait/";
	public static String ORI_PIC_DIR_SUFFIX = "/" + "sina/weibo/.weibo_pic/";
	public static String ORI_CHAT_DIR_SUFFIX = "/" + "sina/weibo/.weibo_chat/";
	public static final String SAVE_FORLDER_PATH = "/sina/weibo/save/";
	
	//之前的缓存文件夹，内容需要被删除
	public static String OLD_PREVIEW_BMP_DIR_SUFFIX = "/" + "sina/weibo/pre/";
    public static String OLD_PORTRAIT_DIR_SUFFIX = "/" + "sina/weibo/portrait/";
	
	//张小小杰：官方账户uid
	public static String ANDROID_OFFICAL_ACCOUNT_UID = "1781379945";
	
	public static String HTC_WILD_FIRE = "HTC Wildfire";
	public static int PIC_TINY = 128;
	public static int PIC_SMALL = 176;
	public static int PIC_NORMAL = 240;
	public static int PIC_LARGE = 320;
	public static int PIC_HUGE = 690;

	public static int MALE = 0;
	public static int FEMALE = 1;

	public static String TAG = "weibo";
	public static int INBOX = 1;
	public static int OUTBOX = 2;

	public static String DATE_FORMAT = "MM-dd HH:mm";

	public static int DIRECT_MESSAGE_LENGTH = 300;
	public static int MAX_LENGTH = 140;
	public static int PAGE_NUM = 25;
	
	public static String PUBLIC_UID = "1641537045";
	public static String FROM = "1028105010";
	public static String WM = "3333_1001";

	public static String CID = "AA3989C3F6DD0C15";
	public static String KEY = "285ED7543B6CCBA6EE029FE9773D9C91119D3536DF0BBD2BF8C8A911F02CAF54CF586811AA0433C9";
	
	public static String APP_KEY = "670B887544906C6F99066F573D767F43";
	public static String APP_SECRET = "DA5E64DA7AAFA539D70B8DFFD8A1C4C230CBD40CA1B5F8A2424457BA1E7C234BCF586811AA0433C9";

	public static String LOOK_AROUND = "1";
	public static String HOT_FORWARD = "2";
	public static String HOT_COMMENT = "8";

	public static int TIMEOUT = 30000;
	public static int UPLOAD_TIMEOUT = 60000;

	public static String CELLPHONE_NUMBER_0 = "1069009088";
	public static String CELLPHONE_NUMBER_1 = "1066888859";
	public static String CELLPHONE_NUMBER_2 = "13717612347";
	public static String CELLPHONE_NUMBER_3 = "13717612347";
	// add for notify reload
	public static String UPDATE_HOME_LIST = "sina.weibo.action.UPDATEHOME";
	public static String UPDATE_COMMENT_LIST = "sina.weibo.action.UPDATECOMMENT";
	public static String UPDATE_MESSAGE_LIST = "sina.weibo.action.UPDATEMESSAGE";
	public static String UPDATE_AT_LIST = "sina.weibo.action.UPDATEAT";
	public static String UPDATE_FANS_LIST = "sina.weibo.action.UPDATEFANS";
	// add for notify service
	public static String WEIBO_UPDATED_ACTION = "sina.weibo.action.UPDATED";
	public static String WEIBO_BACKGROUND_ACTION = "sina.weibo.action.BACKGROUND";
	public static String WEIBO_SETTING = "sina.weibo.action.SETTING";
	// Add for notify each page new msg to change the reload item
	public static String WEIBO_HOME_NEW_MSG = "com.sina.weibo.action.HOMENEW";
	public static String WEIBO_COMMENT_NEW_MSG = "com.sina.weibo.action.COMMENTNEW";
	public static String WEIBO_AT_NEW_MSG = "com.sina.weibo.action.ATNEW";
	public static String WEIBO_MESSAGE_NEW_MSG = "com.sina.weibo.action.MESSAGENEW";
	public static String WEIBO_MESSAGE_NEW_FANS = "com.sina.weibo.action.FANSNEW";
	// add for switch user
	public static String WEIBO_SWITCH_USER = "com.sina.weibo.action.SWITCHUSER";
	public static String WEIBO_SWITCH_USER_DONE = "sina.weibo.action.SWITCHUSERDONE";
	public static String WEIBO_NO_USER = "sina.weibo.action.NOUSER";
	// Main tab activity switch mode
	public static String WEIBO_SWITCH_MODE = "com.sina.weibo.action.SWITCHMODE";
	// Add for ChildrenActivity start SwitchUser , which should start maintab
	// first
	public static String WEIBO_SWITCH_TO_SWITCH_USER = "com.sina.weibo.action.switchuser_activity_on_stack_top";
	// ChildrenActivity click home button back to home
	public static String WEIBO_GO_BACK_HOME = "com.sina.weibo.action.go_back_to_home";
	// notify user nick name has been changed
	public static String WEIBO_BR_NICKNAME_CHANGED = "com.sina.weibo.action.NICKNAMECHANGED";
	public static String WEIBO_SETTING_REMARK_CHANGED = "com.sina.weibo.action.REMARKCHANGED";
	
	public static String WEIBO_SETTING_SKIN_CHANGED = "com.sina.weibo.action.SKINCHANGED";
	
	public static boolean DEBUG = false;

	// 是否使用UC_MOBILE打开链接
	public static boolean UCMOBILE = true;
	
	public static final String ACTION_BLOG_DELETE = "com.sina.weibo.intent.action.BLOG_DELETE";
	public static final String EXTRA_BLOG_ID = "com.sina.weibo.intent.extra.BLOG_ID";
	
	public static final String ACTION_RESTART = "com.sina.weibo.intent.action.restart";
	
	public static final String IMSI_CHINA_PREFIX = "460";
	
	public static final String NIGHT_MODE_SKIN_PACKAGENAME = "com.sina.weibo.nightdream";
	
	public static final int MIN_SDCARD_SPACE = 1024*1024*10; //sd卡空间如果小于10M，就认为sd卡空间不足
}
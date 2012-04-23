package com.sina.weibosdk;

import java.util.Properties;

import android.text.TextUtils;
import android.util.Log;

public final class WeiboSDKConfig {
	
    /**
     * from值
     */
    public static final String KEY_FROM = "key.weibosdk.from";
    /**
     * WM值
     */
    public static final String KEY_WM = "key.weibosdk.wm";
    /**
     * c值
     */
    public static final String KEY_C = "key.weibosdk.c";
    /**
     * s值.注意：在微博中，login时s值的计算方式为calculateS(username +
     * password)。其他时候为calculateS(u.uid)。两个s值不一样
     */
    public static final String KEY_S = "key.weibosdk.s";
    /**
     * 用户gsid。凡是需要登录的接口，都需要传递gsid值
     */
    public static final String KEY_GSID = "key.weibosdk.gsid";
    /**
     * UA信息
     */
    public static final String KEY_UA = "key.weibosdk.ua";
    /**
     * 语言 可选值 zh_CN（简体） zh_HK（繁体） en_US（英语） ms_MY（马来西亚语），ja_JP（日语）
     */
    public static final String KEY_LANG = "key.weibosdk.lang";
    
    
    
    
    /**
     * URLConnection读超时
     */
    public static final String KEY_READ_TIMEOUT = "key.weibosdk.readtimeout";
    /**
     * HttpURLConnection建立连接超时
     */
    public static final String KEY_HTTP_CONNECTION_TIMEOUT = "key.weibosdk.http.connectiontimeout";
    /**
     * HttpsURLConnection建立连接超时
     */
    public static final String KEY_HTTPS_CONNECTION_TIMEOUT = "key.weibosdk.https.connectiontimeout";
    /**
     * http请求io异常后重试次数，默认为0，不重试
     */
    public static final String KEY_RETRY_COUNT = "key.taskpool.retrycount";
    public static final String KEY_API_SERVER = "key.http.api.server";
    public static final String KEY_HTTPS_API_SERVER = "key.https.api.server";
    /**
     * 下载时，如果不知道下载文件大小时的默认值(单位:Byte)
     */
    public static final String KEY_DEFAULT_DOWNLOAD_FILE_SIZE = "key.download.file.size";
    /**
     * 在当前网络环境下
     * https连接是否正常
     * 取值范围0,1,2
     */
    public static final String KEY_HTTPSLINK_FLAG = "key.https.link.flag";
    /**
     * https 连续两次失败后，以后的 https 请求都直接用 http
     */
    public static final int HTTPS_RETRIED = 2;
    
    
    /**
     * 定义API线程池的大小
     */
    public static final String KEY_API_THREAD_POOL_SIZE = "key.api.thread.pool.size";
    /**
     * 定义Download线程池大小
     */
    public static final String KEY_DOWNLOAD_THREAD_POOL_SIZE = "key.download.thread.pool.size";
    /**
     * 定义内存Cache中图片的个数
     */
    public static final String KEY_MEM_CACHE_SIZE = "key.memcache.size";
    
    
    
    /**
     * log开关
     */
    public static final String KEY_IS_DEBUG = "key.weibosdk.isdebug";
    /**
     * loglevel
     */
    public static final String KEY_LOG_LEVEL = "key.weibosdk.log.level";

    
    
	//////////////////////////////服务器返回的Error Code /////////////////////////////////////
	public static final String ERROR_PASSWORD_WRONG = "-100";
	public static final String ERROR_VERIFICATION_CODE_WRONG = "-1005";
	// //////////////////////////////////////////////////////////////////////////////////////
    
    
    private static WeiboSDKConfig config;
    private Properties props;

    private ThreadLocal<UserAuthenRelated> mUserAuthenRelated;
    
    
    private WeiboSDKConfig() {
        props = new Properties();
        mUserAuthenRelated = 
        		new ThreadLocal<WeiboSDKConfig.UserAuthenRelated>();
        init();
    }

    private void init() {
        props.put(KEY_READ_TIMEOUT, 120000);
        props.put(KEY_HTTP_CONNECTION_TIMEOUT, 20000);
        props.put(KEY_HTTPS_CONNECTION_TIMEOUT, 20000);
        props.put(KEY_LANG, "zh_CN");
        props.put(KEY_API_THREAD_POOL_SIZE, 100);
        props.put(KEY_DOWNLOAD_THREAD_POOL_SIZE, 100);
        props.put(KEY_DEFAULT_DOWNLOAD_FILE_SIZE, 100 * 1024);
        props.put(KEY_MEM_CACHE_SIZE, 200);
        props.put(KEY_RETRY_COUNT, 0);
//        props.put(KEY_API_SERVER, "http://202.108.37.212:8000/2/");
        props.put(KEY_API_SERVER, "https://api.weibo.cn/2/");
        props.put(KEY_IS_DEBUG, false);
        props.put(KEY_LOG_LEVEL, Log.VERBOSE);
        props.put(KEY_HTTPSLINK_FLAG, 0);
    }

    public synchronized static WeiboSDKConfig getInstance() {
        if (null == config) {
            config = new WeiboSDKConfig();
        }
        return config;
    }

    public String getString(String key) {
    	String value = null;
		UserAuthenRelated auth = mUserAuthenRelated.get();
		if (auth == null) {
			value = props.getProperty(key);
		} else {
			value = auth.getProperty(key);
			if (value == null) {
				value = props.getProperty(key);
			}
		}
        return value;
    }

    public int getInt(String key) {
        return (Integer) this.props.get(key);
    }

    public boolean getBoolean(String key) {
        return (Boolean) this.props.get(key);
    }

    public void setProperty(String key, String value) {
    	if (TextUtils.isEmpty(value)) {
			return;
		}
    	UserAuthenRelated auth = mUserAuthenRelated.get();
    	if (auth == null) {
    		mUserAuthenRelated.set(new UserAuthenRelated());
    	}
    	auth = mUserAuthenRelated.get();
    	auth.setProperty(key, value);
        this.props.setProperty(key, value);
    }

    public void setProperty(String key, Object value) {
    	if (value == null) {
    		return;
    	}
        this.props.put(key, value);
    }

    /**
     * 与用户认证相关 
     */
    private class UserAuthenRelated {
    	private String gsid;
    	private String s;
    	
    	public void setProperty (String key, String value) {
    		if (KEY_S.equals(key)) {
    			s = value;
    		} else if (KEY_GSID.equals(key)) {
    			gsid = value;
    		}
    	}
    	
    	public String getProperty (String key) {
    		if (KEY_S.equals(key)) {
    			return s;
    		} else if (KEY_GSID.equals(key)) {
    			return gsid; 
    		}
    		return null;
    	}
    }
    
    /**
     * 清理线程变量
     */
    public void cleanThreadVars () {
    	mUserAuthenRelated.set(null);
    }
    
    
    public void clear (String key) {
    	UserAuthenRelated auth = mUserAuthenRelated.get();
    	if (auth != null) {
    		auth.setProperty(key, null);
    	}
    	if (props.containsKey(key)) {
    		props.remove(key);
    	}
    }
}

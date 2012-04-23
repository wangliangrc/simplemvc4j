package com.sina.weibosdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibosdk.entity.ErrorMessage;
import com.sina.weibosdk.exception.WeiboApiException;
import com.sina.weibosdk.exception.WeiboParseException;
import com.sina.weibosdk.log.LogUtils;

public class Util {

	private static LogUtils log = LogUtils.creatLog(); 
	
	private Util() {}
	
    /**
     * 检查参数是否为空
     * 
     * @param params
     * @throws WeiboApiException
     */
    public static void checkNullParams(String... params) throws WeiboApiException {
        for (String param : params) {
            if (TextUtils.isEmpty(param)) {
                throw new WeiboApiException("some request param is null");
            }
        }
    }
    
    /**
     * 把null转为""
     * @return
     */
    public static String convertNullToBlank(String s) {
    	if (s == null) {
    		return "";
    	}
    	return s;
    }

    /**
     * 检查返回的消息是否为错误信息
     * 
     * @param response
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public static void checkResponse(String response) throws WeiboParseException, WeiboApiException {
        // 不是錯誤信息，直接返回
        if (response.indexOf("errno") < 0) {
            return;
        }

        ErrorMessage err = new ErrorMessage(response);
        if (!TextUtils.isEmpty(err.getErrmsg()) || !TextUtils.isEmpty(err.getErrno())) {
            throw new WeiboApiException(err);
        }
    }

    /**
     * 只包含非二进制参数
     * 
     * @param parameters
     * @param boundary
     * @return
     */
    public static String encodePostBody(Bundle parameters, String boundary) {
        if (parameters == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (String key : parameters.keySet()) {
        	String value = parameters.getString(key);
        	if(!TextUtils.isEmpty(value)) {
	            sb.append("Content-Disposition: form-data; name=\"" + URLEncoder.encode(key)
	                    + "\"\r\n\r\n" + URLEncoder.encode(parameters.getString(key)));
	            sb.append("\r\n" + "--" + boundary + "\r\n");
        	}
        }

        return sb.toString();
    }


    public static String encodePostUrlParams(Bundle parameters) {
    	if (parameters == null) {
            return "";
        }
    	StringBuilder url = new StringBuilder();
    	url.append("?");
    	String from = convertNullToBlank(
    			parameters.getString("from"));
		url.append("from").append("=").append(from);

		String wm = convertNullToBlank(
				parameters.getString("wm"));
		url.append("&");
		url.append("wm").append("=").append(wm);

		String c = convertNullToBlank(
				parameters.getString("c"));
		url.append("&");
		url.append("c").append("=").append(c);

		String s = convertNullToBlank(
				parameters.getString("s"));
		url.append("&");
		url.append("s").append("=").append(s);

		String ua = convertNullToBlank(
				parameters.getString("ua"));
		url.append("&");
		url.append("ua").append("=").append(ua);

		String lang = convertNullToBlank(
				parameters.getString("lang"));
		url.append("&");
		url.append("lang").append("=").append(lang);

		String gsid = convertNullToBlank(
				parameters.getString("gsid"));
		url.append("&");
		url.append("gsid").append("=").append(gsid);

    	return url.toString();
    }
    
    public static String encodeGetUrlParams(Bundle parameters) {
        if (parameters == null) {
            return "";
        }
        boolean first = true;
        StringBuilder url = new StringBuilder();
        for (String key : parameters.keySet()) {
            String value = parameters.getString(key);
            if(TextUtils.isEmpty(value)) {
            	continue;
            }
            if (first) {
            	url.append("?");
            	first = false;
            } else {
            	url.append("&");
            }
        	url.append(URLEncoder.encode(key));
        	url.append("=");
        	url.append(URLEncoder.encode(value));
        }
        return url.toString();
    }
    
    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                params.putString(URLDecoder.decode(v[0]), URLDecoder.decode(v[1]));
            }
        }
        return params;
    }

    public static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    /**
     * 获得图片的ContentType。目前接口仅支持JPEG,GIF,PNG图片
     * 
     * @param picName
     * @return
     */
    public static String getImageContentType(String picName) {
        String filetype = "image/png";
        if (picName.endsWith(".jpg") || picName.endsWith(".jpeg")) {
            filetype = "image/jpeg";
        } else if (picName.endsWith(".gif")) {
            filetype = "image/gif";
        }
        return filetype;
    }

    private static Pattern srcPattern = Pattern.compile("<[^>]+>");

    public static String getFormatSourceDesc(String src) {
        if (!TextUtils.isEmpty(src)) {
            Matcher matcher = srcPattern.matcher(src);
            return matcher.replaceAll("");
        }
        return src;
    }

    /**
     * 获得当前网络信息
     * @param ctx
     * @return NetworkInfo
     */
    public static NetworkInfo getNetwrokInfo(Context ctx) {
    	ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }
    
    public static Proxy getProxy(Context ctx, NetworkInfo info) throws IOException {
        if (info == null || !info.isAvailable()) {
            throw new IOException("NoSignalException");
        }
        Proxy proxy = null;
        final int type = getNetWorkType(info);
        if (type == ConnectivityManager.TYPE_WIFI) {
            proxy = null;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            // 获取默认代理主机ip
            String host = android.net.Proxy.getDefaultHost();
            // 获取端口
            int port = android.net.Proxy.getDefaultPort();
            if (host != null && port != -1) {
                // 封装代理連接主机IP与端口号。
                InetSocketAddress inetAddress = new InetSocketAddress(host, port);
                // 根据URL链接获取代理类型，本链接适用于TYPE.HTTP
                proxy = new java.net.Proxy(Type.HTTP, inetAddress);
            } else {
                proxy = null;
            }
        }

        return proxy;

    }
    
    /**
     * 获得当前网络状态
     * @return ConnectivityManager.TYPE_MOBILE
     * 		   ConnectivityManager.TYPE_WIFI 
     */
    public static int getNetWorkType(NetworkInfo info) {
    	return info.getType();
    }
    
    public static boolean checkFileExist(String path) {
    	if(path == null) {
    		return false;
    	}
		File f = new File(path);
		return checkFileExist(f);
    }
    
    public static boolean checkFileExist(File f) {
    	if(f == null) {
    		return false;
    	}
    	return f.exists();
    }
    
    public static boolean mkfile(String path) {
    	File f = new File(path);
    	return mkfile(f);
    }

    public static boolean mkfile(File f) {
    	if(f == null) {
    		return false;
    	}
    	try {
	    	File parFile = f.getParentFile();
	    	if(parFile != null) {
	    		if(parFile.mkdirs()) {
	    			return f.createNewFile();
	    		}
	    		else {
	    			return false;
	    		}
	    	}else {
	    		return f.createNewFile();
	    	}
    	} catch (IOException e) {
    		loge(e.getMessage(), e);
    		return false;
    	}
    	
    }
    
    public static void delFile(File... files) {
    	if (files == null) {
    		return;
    	} else {
    		for(File f : files) {
    			f.delete();
    		}
    	}
    }
    
    public static void loge(String msg, Throwable t) {
    	log.error(msg, t);
    }
    
    public static void loge(String msg) {
    	log.error(msg);
    }
    
    public static void logd(String msg) {
    	log.debug(msg);
    }
}

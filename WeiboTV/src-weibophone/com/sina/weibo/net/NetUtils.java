package com.sina.weibo.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.sina.weibo.WeiboApplication;
import com.sina.weibo.exception.WeiboApiException;
import com.sina.weibo.exception.WeiboIOException;
import com.sina.weibo.exception.WeiboParseException;
import com.sina.weibo.models.ErrorMessage;
import com.sina.weibo.models.SpeedLog;
import com.sina.weibo.net.RPCHelper.NoSignalException;

public final class NetUtils {
    public static final String TYPE_FILE_NAME = "TYPE_FILE_NAME";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    private static String BOUNDARY = "---------7d4a6d158c9";
    private static String MULTIPART_FORM_DATA = "multipart/form-data";
    private static int TIMEOUT = 30000;
    private static int UPLOAD_TIMEOUT = 60000;
    private static final int HTTP_STATUS_OK = 200;
    private static int KB = 1024;

    // private static byte[] sBuffer = new byte[512];

    public static class APNWrapper {
        public String name;
        public String apn;
        public String proxy;
        public int port;

        public String getApn() {
            return apn;
        }

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }

        public String getProxy() {
            return proxy;
        }

        APNWrapper() {
        }
    }

    public enum NetworkState {
        NOTHING, MOBILE, WIFI
    }

    static final Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");

    // static APNWrapper wrapper;
    // for ophone system
    private static NetworkConnectivityListener mConnectivityListener;
    private static ServiceHandler mServiceHandler;

    public static APNWrapper getAPN(Context ctx) {
        TelephonyManager telManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();
        String phoneSystem = getPhoneSystem();
        APNWrapper wrapper = new APNWrapper();
        // 如果是ophone系统
        if ((!TextUtils.isEmpty(phoneSystem) && (phoneSystem
                .equals("Ophone OS 2.0") || phoneSystem.equals("OMS2.5")))
                && (operator.equals("46000") || operator.equals("46002"))) {
            mConnectivityListener = new NetworkConnectivityListener();
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            mServiceHandler = new ServiceHandler(Looper.myLooper());
            mConnectivityListener.registerHandler(mServiceHandler, 1);
            mConnectivityListener.startListening(ctx);
            String feature[] = queryApn(ctx, true);
            if (feature != null) {
                int fan = beginConnect(feature[0], ctx);
                if ((phoneSystem.equals("Ophone OS 2.0") && (fan == -1 || fan == 0))
                        || (phoneSystem.equals("OMS2.5") && (fan == 0))) {
                    feature = queryApn(ctx, false);// get the cmwap apntype
                }
                Object[] apnsetting = null;
                if (reflection == null) {
                    reflection = new Reflection();
                }
                String feature2 = null;
                String V[] = null;
                try {
                    apnsetting = (Object[]) reflection.invokeStaticMethod(
                            "oms.dcm.DataConnectivityHelper", "getApnSettings",
                            new Object[] { ctx, feature });
                    String S = apnsetting[0].toString();
                    V = S.split(",");
                    feature2 = (String) reflection.invokeStaticMethod(
                            "oms.dcm.DataConnectivityHelper",
                            "getProxyAndPort",
                            new Object[] { ctx, V[2].trim() });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (feature2 == null) {
                    wrapper.name = feature[0] == null ? "" : feature[0].trim();
                    wrapper.apn = feature[0] == null ? "" : feature[1].trim();
                    wrapper.proxy = android.net.Proxy.getDefaultHost();
                    wrapper.proxy = TextUtils.isEmpty(wrapper.proxy) ? ""
                            : wrapper.proxy;
                    wrapper.port = android.net.Proxy.getDefaultPort();
                    wrapper.port = wrapper.port > 0 ? wrapper.port : 80;
                    endConnectivity(feature[0]);
                    return wrapper;
                } else {
                    String[] address = feature2.split(":");
                    String IpAddress = null;
                    String PortAddress = null;

                    if (address != null && address.length >= 2) {
                        IpAddress = address[0];
                        PortAddress = address[1];
                    }
                    wrapper.name = V[1].substring(1);
                    wrapper.name = V[2].substring(1);
                    wrapper.proxy = IpAddress;
                    wrapper.port = Integer.parseInt(PortAddress);
                    endConnectivity(feature[0]);
                    return wrapper;
                }
            }
            return null;
        }
        final Cursor cursor = ctx.getContentResolver().query(PREFERRED_APN_URI,
                new String[] { "name", "apn", "proxy", "port" }, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.isAfterLast()) {
                wrapper.name = "N/A";
                wrapper.apn = "N/A";
            } else {
                wrapper.name = cursor.getString(0) == null ? "" : cursor
                        .getString(0).trim();
                wrapper.apn = cursor.getString(1) == null ? "" : cursor
                        .getString(1).trim();
            }
            cursor.close();
        } else {
            wrapper.name = "N/A";
            wrapper.apn = "N/A";
        }
        wrapper.proxy = android.net.Proxy.getDefaultHost();
        wrapper.proxy = TextUtils.isEmpty(wrapper.proxy) ? "" : wrapper.proxy;
        wrapper.port = android.net.Proxy.getDefaultPort();
        wrapper.port = wrapper.port > 0 ? wrapper.port : 80;
        return wrapper;
    }

    public static NetworkState getNetworkState(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        String phoneSystem = getPhoneSystem();
        if (!TextUtils.isEmpty(phoneSystem)
                && (phoneSystem.equals("Ophone OS 2.0") || phoneSystem
                        .equals("OMS2.5"))) {
            if (info == null || !info.isAvailable()) {
                return NetworkState.MOBILE;
            } else {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return NetworkState.MOBILE;
                } else {
                    return NetworkState.WIFI;
                }
            }
        }
        if (info == null || !info.isAvailable()) {
            return NetworkState.NOTHING;
        } else {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NetworkState.MOBILE;
            } else {
                return NetworkState.WIFI;
            }
        }
    }

    public static String encodePostBody(Bundle parameters, String boundary) {
        StringBuilder sb = new StringBuilder();
        if (parameters == null) {
            return "";
        }
        for (String key : parameters.keySet()) {
            if (parameters.getByteArray(key) != null) {
                continue;
            }
            sb.append("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n\r\n" + parameters.getString(key));
            sb.append("\r\n" + "--" + boundary + "\r\n");
        }

        return sb.toString();
    }

    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(key) + "="
                    + URLEncoder.encode(parameters.getString(key)));
        }
        return sb.toString();
    }

    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                params.putString(URLDecoder.decode(v[0]),
                        URLDecoder.decode(v[1]));
            }
        }
        return params;
    }

    public static Bundle parseUrl(String url) {
        URL u;
        try {
            u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            return new Bundle();
        }
    }

    public static HttpClient getHttpClient(Context context)
            throws WeiboIOException {
        NetUtils.NetworkState state = NetUtils.getNetworkState(context);
        HttpClient client = new DefaultHttpClient();
        // String product = Build.PRODUCT;
        if (state == NetUtils.NetworkState.NOTHING) {
            throw new WeiboIOException("NoSignalException");
        } else if (state == NetUtils.NetworkState.MOBILE) {
            NetUtils.APNWrapper wrapper = null;
            wrapper = NetUtils.getAPN(context);
            if (!TextUtils.isEmpty(wrapper.proxy)) {
                client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
                        new HttpHost(wrapper.proxy, wrapper.port));
            }
        }

        HttpConnectionParamBean paramHelper = new HttpConnectionParamBean(
                client.getParams());
        paramHelper.setSoTimeout(Constants.TIMEOUT);
        paramHelper.setConnectionTimeout(Constants.TIMEOUT);
        return client;
    }

    private static String execute(HttpClient client, HttpUriRequest request)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        String result = executeWithoutParse(client, request);
        ErrorMessage err = new ErrorMessage(result);
        /**
         * 没有错误
         */
        if (err == null || err.errno == null || "".equals(err.errno)
                || "1".equals(err.errno)) {
            return result;
        } else {
            throw new WeiboApiException(err);
        }
    }

    private static String executeWithoutParse(HttpClient client,
            HttpUriRequest request) throws WeiboIOException, WeiboApiException {
        request.setHeader("User-Agent",
                WeiboApplication.UA == null ? Constants.USER_AGENT
                        : WeiboApplication.UA);
        request.setHeader("Accept-Encoding", "gzip,deflate");
        try {
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (NullPointerException e) {
                // google issue, doing this to work around
                try {
                    response = client.execute(request);
                } catch (NullPointerException e1) {
                    throw new WeiboIOException(e1);
                }
            }
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            if (statusCode != Constants.HTTP_STATUS_OK) {
                throw new WeiboIOException(String.format(
                        "Invalid response from server: %s", status.toString()));
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null
                    && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }

            // Read response into a buffered stream
            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            // Return result from buffered stream
            String result = new String(content.toByteArray());
            return result;
        } catch (IOException e) {
            throw new WeiboIOException(e);
        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * @throws WeiboApiException
     * @throws WeiboParseException
     *             Implement a http request and return a response
     * 
     * @param url
     *            : a request url
     * @param method
     *            : get or post
     * @param params
     *            : request params
     * @param context
     *            :
     * @return response: reponse from http server
     * @throws NetException
     * @throws
     */
    public static String openUrl(String url, String method, Bundle params,
            Context context) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        String lang = getLang(context);
        HttpClient client = null;
        client = getHttpClient(context);
        StringBuilder newUrl = new StringBuilder();
        String response = "";
        if (NetUtils.METHOD_GET.equals(method)) {
            newUrl.append(getCompleteUrl(url, params));
            // String[] items = url.split("\\?");
            // if (items.length == 2) {
            // newUrl.append(items[0]).append("?");
            // String array[] = items[1].split("&");
            // boolean first = true;
            // for (String parameter : array) {
            // String v[] = parameter.split("=");
            // if (first) {
            // first = false;
            // } else {
            // newUrl.append("&");
            // }
            // if (v.length == 2) {
            // newUrl.append(URLEncoder.encode(v[0])).append("=")
            // .append(URLEncoder.encode(v[1]));
            // } else {
            // newUrl.append(parameter);
            // }
            //
            // }
            // if(params != null){
            // newUrl.append(encodeUrl(params));
            // }
            // }else{
            // newUrl.append(url).append("?").append(encodeUrl(params));
            // }

            newUrl.append("&lang=" + lang);
            HttpGet request = new HttpGet(newUrl.toString());
            response = execute(client, request);
            return response;
        } else if (NetUtils.METHOD_POST.equals(method)) {
            MultipartEntity multipartContent = buildMultipartEntity(params);
            String[] items = url.split("\\?");
            if (items.length == 2) {
                newUrl.append(items[0]).append("?");
                String array[] = items[1].split("&");
                boolean first = true;
                for (String parameter : array) {
                    String v[] = parameter.split("=");
                    if (first) {
                        first = false;
                    } else {
                        newUrl.append("&");
                    }
                    if (v.length == 2) {
                        newUrl.append(URLEncoder.encode(v[0])).append("=")
                                .append(URLEncoder.encode(v[1]));
                    } else {
                        newUrl.append(parameter);
                    }

                }
            } else {
                newUrl.append(url);
            }
            newUrl.append("&lang=" + lang);
            HttpPost request = new HttpPost(newUrl.toString());
            request.setEntity(multipartContent);
            response = execute(client, request);
            return response;
        } else {
            throw new WeiboIOException(WeiboIOException.REASON_HTTP_METHOD);
        }

    }

    /**
     * 
     * @param url
     *            : a request ad url
     * @param getParams
     *            : some parameters appended to the ad url
     * @param postParams
     *            : the post parameters
     * @param context
     * @return a reponse text from http server
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public static String openUrlWithParms(String url, Bundle getParams,
            Bundle postParams, Context context) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        String lang = getLang(context);
        HttpClient client = null;
        client = getHttpClient(context);
        StringBuilder newUrl = new StringBuilder(getCompleteUrl(url, getParams));
        String response = "";
        // String[] getItems = url.split("\\?");
        // if (getItems.length == 2) {
        // newUrl.append(getItems[0]).append("?");
        // String array[] = getItems[1].split("&");
        // boolean first = true;
        // for (String parameter : array) {
        // String v[] = parameter.split("=");
        // if (first) {
        // first = false;
        // } else {
        // newUrl.append("&");
        // }
        // if (v.length == 2) {
        // newUrl.append(URLEncoder.encode(v[0])).append("=")
        // .append(URLEncoder.encode(v[1]));
        // } else {
        // newUrl.append(parameter);
        // }
        //
        // }
        // if(getParams != null){
        // newUrl.append(encodeUrl(getParams));
        // }
        // }else{
        // newUrl.append(url).append("?").append(encodeUrl(getParams));
        // }
        newUrl.append("&lang=" + lang);
        if (postParams != null && !postParams.isEmpty()) { // 以post方式请求url
            MultipartEntity multipartContent = buildMultipartEntity(postParams);
            HttpPost request = new HttpPost(newUrl.toString());
            request.setEntity(multipartContent);
            response = execute(client, request);
        } else { // 以get方式请求url
            HttpGet request = new HttpGet(newUrl.toString());
            response = execute(client, request);
        }
        return response;
    }

    public static String openJsonPostUrl(String url, String method,
            String json, Context context) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        HttpClient client = getHttpClient(context);
        StringBuilder newUrl = new StringBuilder();
        String response = "";
        if (NetUtils.METHOD_POST.equals(method)) {
            // MultipartEntity multipartContent = buildMultipartEntity(params);
            String[] items = url.split("\\?");
            if (items.length == 2) {
                newUrl.append(items[0]).append("?");
                String array[] = items[1].split("&");
                boolean first = true;
                for (String parameter : array) {
                    String v[] = parameter.split("=");
                    if (first) {
                        first = false;
                    } else {
                        newUrl.append("&");
                    }
                    if (v.length == 2) {
                        newUrl.append(URLEncoder.encode(v[0])).append("=")
                                .append(URLEncoder.encode(v[1]));
                    } else {
                        newUrl.append(parameter);
                    }
                }
            } else {
                newUrl.append(url);
            }
            try {
                StringEntity stringEntity = new StringEntity(json);
                HttpPost request = new HttpPost(newUrl.toString());
                request.setEntity(stringEntity);
                response = executeWithoutParse(client, request);
                return response;
            } catch (UnsupportedEncodingException e) {
                throw new WeiboIOException(e);
            }
        } else {
            throw new WeiboIOException(WeiboIOException.REASON_HTTP_METHOD);
        }
    }

    public static String openXAuthGetUrl(String url, String method,
            Bundle params, String oauth_token, String oauth_token_secret,
            Context context) throws WeiboIOException, WeiboParseException,
            WeiboApiException {

        String lang = getLang(context);
        HttpClient client = null;
        client = getHttpClient(context);
        StringBuilder newUrl = new StringBuilder();
        String response = "";
        if (NetUtils.METHOD_GET.equals(method)) {
            newUrl.append(getCompleteUrl(url, params));
            // String[] items = url.split("\\?");
            // if (items.length == 2) {
            // newUrl.append(items[0]).append("?");
            // String array[] = items[1].split("&");
            // boolean first = true;
            // for (String parameter : array) {
            // String v[] = parameter.split("=");
            // if (first) {
            // first = false;
            // } else {
            // newUrl.append("&");
            // }
            // if (v.length == 2) {
            // newUrl.append(URLEncoder.encode(v[0])).append("=")
            // .append(URLEncoder.encode(v[1]));
            // } else {
            // newUrl.append(parameter);
            // }
            //
            // }
            // if(params != null){
            // newUrl.append(encodeUrl(params));
            // }
            // }else{
            // newUrl.append(url).append("?").append(encodeUrl(params));
            // }

            XAuth xAuth = new XAuth(oauth_token, oauth_token_secret,
                    Constants.APP_KEY, Constants.APP_SECRET);
            String authHeader = xAuth.generateAuthorizationHeader(
                    NetUtils.METHOD_GET, newUrl.toString(), null);

            HttpGet request = new HttpGet(newUrl.toString());
            request.setHeader("Authorization", authHeader);
            response = executeWithoutParse(client, request);
            return response;
        } else {
            throw new WeiboIOException(WeiboIOException.REASON_HTTP_METHOD);
        }

    }

    public static String getCompleteUrl(String url, Bundle getParams) {
        StringBuilder newUrl = new StringBuilder();
        String[] items = url.split("\\?");
        if (items.length == 2) {
            newUrl.append(items[0]).append("?");
            String array[] = items[1].split("&");
            boolean first = true;
            for (String parameter : array) {
                String v[] = parameter.split("=");
                if (first) {
                    first = false;
                } else {
                    newUrl.append("&");
                }
                if (v.length == 2) {
                    newUrl.append(URLEncoder.encode(v[0])).append("=")
                            .append(URLEncoder.encode(v[1]));
                } else {
                    newUrl.append(parameter);
                }

            }
            if (getParams != null) {
                newUrl.append(encodeUrl(getParams));
            }
        } else {
            newUrl.append(url).append("?").append(encodeUrl(getParams));
        }

        return newUrl.toString();
    }

    public static void rawOpenUrl(String url, String method, Bundle params,
            Context context, File outputFile) throws WeiboIOException,
            WeiboParseException, WeiboApiException, IOException {
        FileOutputStream fos = null;
        fos = new FileOutputStream(outputFile);
        String lang = getLang(context);
        HttpClient client = null;
        client = getHttpClient(context);
        StringBuilder newUrl = new StringBuilder();
        InputStream response;
        if (NetUtils.METHOD_GET.equals(method)) {
            url = url + "?" + encodeUrl(params) + "&lang=" + lang;
            HttpGet request = new HttpGet(url);
            try {
                HttpResponse httpResponse = null;
                try {
                    httpResponse = client.execute(request);
                } catch (NullPointerException e) {
                    // google issue, doing this to work around
                    httpResponse = client.execute(request);
                }
                StatusLine status = httpResponse.getStatusLine();
                int statusCode = status.getStatusCode();
                if (statusCode != Constants.HTTP_STATUS_OK) {
                    throw new WeiboIOException(String.format(
                            "Invalid response from server: %s",
                            status.toString()));
                }
                // Pull content stream from response
                HttpEntity entity = httpResponse.getEntity();
                entity.writeTo(fos);
                // response = entity.getContent();
                return;
            } catch (IOException e) {
                throw new WeiboIOException(e);
            } finally {
                if (client != null) {
                    client.getConnectionManager().shutdown();
                }
            }
        } else {
            throw new WeiboIOException(WeiboIOException.REASON_HTTP_METHOD);
        }

    }

    /**
     * 获取url对应的页面大小
     * 
     * @param url
     * @param method
     * @param params
     * @param context
     * @return 页面的字节数
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     * @throws IOException
     */
    public static long getPageSize(String url, String method, Bundle params,
            Context context) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        HttpClient client = null;
        client = getHttpClient(context);
        if (NetUtils.METHOD_GET.equals(method)) {
            HttpGet request = new HttpGet(url);
            request.setHeader("User-Agent",
                    WeiboApplication.UA == null ? Constants.USER_AGENT
                            : WeiboApplication.UA);
            request.setHeader("Accept-Encoding", "gzip,deflate");
            try {
                HttpResponse httpResponse = null;
                try {
                    httpResponse = client.execute(request);
                } catch (NullPointerException e) {
                    // google issue, doing this to work around
                    httpResponse = client.execute(request);
                }
                StatusLine status = httpResponse.getStatusLine();
                int statusCode = status.getStatusCode();
                if (statusCode != Constants.HTTP_STATUS_OK) {
                    throw new WeiboIOException(String.format(
                            "Invalid response from server: %s",
                            status.toString()));
                }
                // Pull content stream from response
                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();

                Header header = httpResponse.getFirstHeader("Content-Encoding");
                if (header != null
                        && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                    is = new GZIPInputStream(is);
                }

                byte[] contents = new byte[KB];
                long size = 0;
                while (true) {
                    int len = is.read(contents);
                    if (len == -1) {
                        break;
                    } else {
                        size += len;
                    }
                }

                // long len = entity.getContentLength();
                return size;
            } catch (IOException e) {
                throw new WeiboIOException(e);
            } finally {
                if (client != null) {
                    client.getConnectionManager().shutdown();
                }
            }
        } else if (NetUtils.METHOD_POST.equals(method)) {
            // TODO: not complete
            return 0;
        } else {
            throw new WeiboIOException(WeiboIOException.REASON_HTTP_METHOD);
        }

    }

    private static InputStream rawExecute(HttpClient client,
            HttpUriRequest request) throws WeiboIOException {
        request.setHeader("User-Agent",
                WeiboApplication.UA == null ? Constants.USER_AGENT
                        : WeiboApplication.UA);
        try {
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            if (statusCode != Constants.HTTP_STATUS_OK) {
                throw new WeiboIOException(String.format(
                        "Invalid response from server: %s", status.toString()));
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            return inputStream;
        } catch (IOException e) {
            throw new WeiboIOException(e);
        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * 返回Url中所需的lang参数
     * 
     * @return
     */
    public static String getLang(Context ctx) {
        SettingsPref.changeLocale(ctx);
        String lang = ctx.getString(R.string.language_param);
        return lang;
    }

    /**
     * 把Bundle转化为MultipartEntity
     * 
     * @param params
     * @return
     * @throws WeiboIOException
     */
    private static MultipartEntity buildMultipartEntity(Bundle params)
            throws WeiboIOException {
        MultipartEntity multipartContent = new MultipartEntity();
        List<NameValuePair> form = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            if (TYPE_FILE_NAME.equals(key)) {
                Object fileNames = params.get(key);
                if (fileNames != null && fileNames instanceof Bundle) {
                    Bundle pathBundle = (Bundle) fileNames;
                    StringBuffer data = new StringBuffer();
                    for (String uploadFileKey : pathBundle.keySet()) {
                        File file = new File(
                                pathBundle.getString(uploadFileKey));
                        FileBody bin = new FileBody(file, "image/jpeg");
                        multipartContent.addPart(uploadFileKey, bin);
                        form.add(new BasicNameValuePair(uploadFileKey, data
                                .toString()));
                    }
                }
            } else {
                StringBody sb1;
                try {
                    // String value = TextUtils.isEmpty(params.getString(key)) ?
                    // "" : params.getString(key);
                    sb1 = new StringBody(params.getString(key),
                            Charset.forName(HTTP.UTF_8));
                    multipartContent.addPart(URLEncoder.encode(key), sb1);
                    form.add(new BasicNameValuePair(key, params.getString(key)));
                } catch (UnsupportedEncodingException e) {
                    throw new WeiboIOException(e);
                }
            }
        }
        return multipartContent;
    }

    private static String read(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    public static HttpURLConnection getURLConnetion(Context context, String url)
            throws NetException {
        HttpURLConnection conn = null;
        // SET PROXY
        try {
            NetUtils.NetworkState state = NetUtils.getNetworkState(context);
            if (state == NetUtils.NetworkState.NOTHING) {
                throw new NetException(new NoSignalException());
            } else if (state == NetUtils.NetworkState.MOBILE) {
                NetUtils.APNWrapper wrapper;
                wrapper = NetUtils.getAPN(context);
                if (!TextUtils.isEmpty(wrapper.proxy)) {
                    Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,
                            new InetSocketAddress(wrapper.proxy, wrapper.port));
                    conn = (HttpURLConnection) new URL(url)
                            .openConnection(proxy);

                } else {
                    conn = (HttpURLConnection) new URL(url).openConnection();
                }

            } else {
                conn = (HttpURLConnection) new URL(url).openConnection();
            }
            return conn;
        } catch (Exception e) {
            throw new NetException(e);
        }
    }

    public static class NetException extends Exception {
        /**
		 * 
		 */
        private static final long serialVersionUID = -6236202638839756763L;

        public NetException() {
            super();
        }

        public NetException(String detailMessage) {
            super(detailMessage);
        }

        public NetException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public NetException(Throwable throwable) {
            super(throwable);
        }

    }

    /**
     * 在url后面添加参数串
     * 
     * @param url
     * @param parameters
     * @return
     */
    public static String appendUrlParams(String url, Bundle parameters) {
        String params = encodeUrl(parameters);
        if (TextUtils.isEmpty(params)) {
            return url;
        }
        if (url.indexOf('?') != -1 && url.indexOf('?') != url.length() - 1) {
            url = url + "&" + params;
        } else {
            url = url + "?" + params;
        }
        return url;
    }

    // for ophone system
    private static Reflection reflection;

    private static String getPhoneSystem() {
        if (reflection == null) {
            reflection = new Reflection();
        }
        try {
            Object opp = reflection.newInstance("android.os.SystemProperties",
                    new Object[] {});
            String system = (String) reflection.invokeMethod(opp, "get",
                    new Object[] { "apps.setting.platformversion", "" });
            return system;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ConnectivityManager mConnMgr;

    private static int beginConnect(String apType, Context ctx) {
        int result = -1;
        Integer result1 = 0;
        if (mConnMgr == null) {
            mConnMgr = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        result = mConnMgr.startUsingNetworkFeature(
                ConnectivityManager.TYPE_MOBILE, apType);
        Integer result0 = 0;
        try {
            result0 = (Integer) reflection.getStaticProperty(
                    "oms.dcm.DataConnectivityConstants",
                    "FEATURE_ALREADY_ACTIVE");
            result1 = (Integer) reflection.getStaticProperty(
                    "oms.dcm.DataConnectivityConstants",
                    "FEATURE_REQUEST_STARTED");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (result == result0.intValue()) {
            return -1;
        } else if (result == result1.intValue()) {
            return 1;
        } else {
            return -1;
        }
    }

    private static String[] queryApn(Context ctx, boolean isFirst) {
        Cursor c = ctx.getContentResolver().query(
                Uri.parse("content://telephony/apgroups"),
                new String[] { "type", "name" }, null, null, null);
        String feature[] = new String[2];
        if (c != null) {
            try {
                if (isFirst) {
                    c.moveToFirst();
                } else {
                    c.moveToFirst();
                    c.moveToNext();
                    c.moveToNext();
                }
                feature[0] = c.getString(0);
                feature[1] = c.getString(1);
                return feature;
            } finally {
                c.close();
            }
        }
        return null;
    }

    public static class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    protected static void endConnectivity(String apType) {
        if (mConnMgr != null) {
            mConnMgr.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE,
                    apType);
        }
    }

    // 判断当前网络是否为wifi
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static SpeedLog getSpeedParams(String url, String method,
            Bundle params, Context context) throws WeiboIOException,
            WeiboParseException, WeiboApiException {

        HttpClient client = null;
        client = getHttpClient(context);

        SpeedLog speedlog = new SpeedLog();
        long responseTime = 0;
        long linkTime = 0;
        if (NetUtils.METHOD_GET.equals(method)) {
            HttpGet request = new HttpGet(url);
            request.setHeader("User-Agent",
                    WeiboApplication.UA == null ? Constants.USER_AGENT
                            : WeiboApplication.UA);
            request.setHeader("Accept-Encoding", "gzip,deflate");

            try {
                long responseBegin = System.currentTimeMillis();
                HttpResponse httpResponse = null;
                try {
                    httpResponse = client.execute(request);
                } catch (NullPointerException e) {
                    // google issue, doing this to work around
                    httpResponse = client.execute(request);
                }

                // httpResponse-Time
                responseTime = System.currentTimeMillis() - responseBegin;
                Log.i(Constants.TAG, httpResponse.getProtocolVersion() + "");

                StatusLine status = httpResponse.getStatusLine();
                int statusCode = status.getStatusCode();
                if (statusCode != Constants.HTTP_STATUS_OK) {
                    throw new WeiboIOException(String.format(
                            "Invalid response from server: %s",
                            status.toString()));
                }
                // Pull content stream from response
                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();

                Header header = httpResponse.getFirstHeader("Content-Encoding");
                if (header != null
                        && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                    is = new GZIPInputStream(is);
                }

                byte[] contents = new byte[KB];
                long size = 0;
                while (true) {
                    int len = is.read(contents);
                    if (len == -1) {
                        break;
                    } else {
                        size += len;
                    }
                }
                linkTime = System.currentTimeMillis() - responseBegin;
                // long len = entity.getContentLength();
                float pageSize = (size / (float) KB);

                speedlog.responseTime = String.valueOf(responseTime);
                speedlog.linkTime = String.valueOf(linkTime);
                speedlog.httpcode = String.valueOf(statusCode);
                speedlog.pageSize = String.valueOf(pageSize);
                return speedlog;

            } catch (IOException e) {
                throw new WeiboIOException(e);
            } finally {
                if (client != null) {
                    client.getConnectionManager().shutdown();
                }
            }
        } else if (NetUtils.METHOD_POST.equals(method)) {
            // TODO: not complete
            return null;
        } else {
            throw new WeiboIOException(WeiboIOException.REASON_HTTP_METHOD);
        }
    }

}

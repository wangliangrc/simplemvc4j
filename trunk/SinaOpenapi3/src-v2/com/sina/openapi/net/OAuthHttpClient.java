package com.sina.openapi.net;

import static com.clark.func.Functions.isNotBlank;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.http.HttpParameters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.google.gdata.util.common.base.Preconditions;
import com.sina.openapi.WeiboHttpClient;
import com.sina.openapi.WeiboOpenAPIException;

public class OAuthHttpClient extends WeiboHttpClient {
    private static final int DEFAULT_TIME_OUT = 60000;
    private static final Properties oauthProperties = new Properties();
    private static final Log log = LogFactory.getLog(OAuthHttpClient.class);

    public static final String PROPERTY_CONSUMER_KEY = "com.sina.openapi.net.OAuthHttpClient.PROPERTY_CONSUMER_KEY";
    public static final String PROPERTY_CONSUMER_SECRET = "com.sina.openapi.net.OAuthHttpClient.PROPERTY_CONSUMER_SECRET";
    public static final String PROPERTY_REQUEST_TOKEN_URL = "com.sina.openapi.net.OAuthHttpClient.PROPERTY_REQUEST_TOKEN_URL";
    public static final String PROPERTY_ACCESS_TOKEN_URL = "com.sina.openapi.net.OAuthHttpClient.PROPERTY_ACCESS_TOKEN_URL";
    public static final String PROPERTY_AUTHORIZATION_URL = "com.sina.openapi.net.OAuthHttpClient.PROPERTY_AUTHORIZATION_URL";

    /*
     * 加载默认值
     */
    static {
        oauthProperties.setProperty(PROPERTY_CONSUMER_KEY,
                System.getProperty(PROPERTY_CONSUMER_KEY, "2540340328"));
        oauthProperties.setProperty(PROPERTY_CONSUMER_SECRET, System
                .getProperty(PROPERTY_CONSUMER_SECRET,
                        "886cfb4e61fad4e4e9ba9dee625284dd"));
        oauthProperties.setProperty(PROPERTY_REQUEST_TOKEN_URL, System
                .getProperty(PROPERTY_REQUEST_TOKEN_URL,
                        "http://api.t.sina.com.cn/oauth/request_token"));
        oauthProperties.setProperty(PROPERTY_ACCESS_TOKEN_URL, System
                .getProperty(PROPERTY_ACCESS_TOKEN_URL,
                        "http://api.t.sina.com.cn/oauth/access_token"));
        oauthProperties.setProperty(PROPERTY_AUTHORIZATION_URL, System
                .getProperty(PROPERTY_AUTHORIZATION_URL,
                        "http://api.t.sina.com.cn/oauth/authorize"));
    }

    private String token;
    private String tokenSecret;

    public OAuthHttpClient(String token, String tokenSecret) {
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public static void login(String userId, String password,
            String[] tokenHolder) throws WeiboOpenAPIException {
        Preconditions
                .checkArgument(isNotBlank(userId), "userId can't be empty");
        Preconditions.checkArgument(isNotBlank(password),
                "password can't be empty");
        Preconditions.checkArgument(tokenHolder != null
                && tokenHolder.length >= 2, "tokenHolder can't be empty");

        HttpClient client = null;
        try {
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
                    getOauthProperties(PROPERTY_CONSUMER_KEY),
                    getOauthProperties(PROPERTY_CONSUMER_SECRET));
            OAuthProvider provider = new CommonsHttpOAuthProvider(
                    getOauthProperties(PROPERTY_REQUEST_TOKEN_URL),
                    getOauthProperties(PROPERTY_ACCESS_TOKEN_URL),
                    getOauthProperties(PROPERTY_AUTHORIZATION_URL));
            String url = provider.retrieveRequestToken(consumer,
                    OAuth.OUT_OF_BAND);
            client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIME_OUT);
            HttpConnectionParams.setSoTimeout(params, DEFAULT_TIME_OUT);
            HttpPost post = new HttpPost(
                    oauthProperties.getProperty(PROPERTY_AUTHORIZATION_URL));
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("action", "submit"));
            pairs.add(new BasicNameValuePair("regCallback", OAuth
                    .percentEncode(OAuth.addQueryParameters(url, "from", ""))));
            pairs.add(new BasicNameValuePair("oauth_token", consumer.getToken()));
            pairs.add(new BasicNameValuePair("oauth_callback",
                    OAuth.OUT_OF_BAND));
            pairs.add(new BasicNameValuePair("from", ""));
            pairs.add(new BasicNameValuePair("userId", userId));
            pairs.add(new BasicNameValuePair("passwd", password));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,
                    "UTF-8");
            post.setEntity(entity);
            debugHttpRequestBase(post);
            String html = client.execute(post, new ResponseHandler<String>() {

                @Override
                public String handleResponse(HttpResponse arg0)
                        throws ClientProtocolException, IOException {

                    return getResponseBody(arg0);
                }
            });
            String oauthVerifier = fetchOAuthVerifier(html);
            provider.setOAuth10a(true);
            provider.retrieveAccessToken(consumer, oauthVerifier);
            tokenHolder[0] = consumer.getToken();
            tokenHolder[1] = consumer.getTokenSecret();

        } catch (Exception e) {
            throw new WeiboOpenAPIException(e);
        } finally {
            clientShutDown(client);
        }
    }

    @Override
    public String getConsumerKey() {
        return getOauthProperties(PROPERTY_CONSUMER_KEY);
    }

    @Override
    public String httpDelete(String url) throws WeiboOpenAPIException {
        HttpClient httpClient = null;
        try {
            url = removeSourceParam(url);
            HttpDelete httpDelete = new HttpDelete(url);
            getOAuthConsumer().sign(httpDelete);
            debugHttpRequestBase(httpDelete);
            httpClient = getDefaultHttpClient();
            return httpClient.execute(httpDelete,
                    new ResponseHandler<String>() {

                        @Override
                        public String handleResponse(HttpResponse arg0)
                                throws ClientProtocolException, IOException {

                            return getResponseBody(arg0);
                        }
                    });
        } catch (Exception e) {
            throw new WeiboOpenAPIException(e);
        } finally {
            clientShutDown(httpClient);
        }
    }

    @Override
    public String httpGet(String url) throws WeiboOpenAPIException {
        HttpClient httpClient = null;
        try {
            /*
             * 去掉 source param 并 encode
             */
            url = removeSourceParam(url);
            HttpGet httpGet = new HttpGet(url);
            getOAuthConsumer().sign(httpGet);
            debugHttpRequestBase(httpGet);

            httpClient = getDefaultHttpClient();
            return httpClient.execute(httpGet, new ResponseHandler<String>() {

                @Override
                public String handleResponse(HttpResponse arg0)
                        throws ClientProtocolException, IOException {

                    return getResponseBody(arg0);
                }
            });
        } catch (Exception e) {
            throw new WeiboOpenAPIException(e);
        } finally {
            clientShutDown(httpClient);
        }
    }

    @Override
    public String httpPost(String url,
            Iterable<Entry<String, Object>> httpParams)
            throws WeiboOpenAPIException {
        HttpClient httpClient = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (httpParams != null) {
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                for (final Entry<String, Object> temp : httpParams) {
                    // OAuth 验证不需要 "source"
                    if (temp.getKey().equals("source"))
                        continue;
                    params.add(new BasicNameValuePair(temp.getKey(), String
                            .valueOf(temp.getValue())));
                }
                if (params.size() > 0) {
                    httpPost.setEntity(OauthUrlEncodedFormEntity
                            .newInstance(params));
                }
            }
            getOAuthConsumer().sign(httpPost);
            debugHttpRequestBase(httpPost);
            httpClient = getDefaultHttpClient();
            return httpClient.execute(httpPost, new ResponseHandler<String>() {

                @Override
                public String handleResponse(HttpResponse arg0)
                        throws ClientProtocolException, IOException {

                    return getResponseBody(arg0);
                }
            });
        } catch (Exception e) {
            throw new WeiboOpenAPIException(e);
        } finally {
            clientShutDown(httpClient);
        }
    }

    @Override
    public String httpUpload(String url,
            Iterable<Entry<String, Object>> httpParams)
            throws WeiboOpenAPIException {

        return apacheUpload(url, httpParams);
    }

    private String apacheUpload(String url,
            Iterable<Entry<String, Object>> httpParams)
            throws WeiboOpenAPIException {
        HttpClient httpClient = null;
        try {
            OAuthConsumer consumer = getOAuthConsumer();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity();
            Object object = null;
            for (Entry<String, Object> entry : httpParams) {
                object = entry.getValue();

                if (object instanceof File) {
                    entity.addPart(entry.getKey(), new FileBody((File) object));
                } else if (object instanceof InputStream) {
                    entity.addPart(entry.getKey(), new InputStreamBody(
                            (InputStream) object, "image/*", "image.png"));
                } else {
                    try {
                        entity.addPart(
                                entry.getKey(),
                                new StringBody(object.toString(), Charset
                                        .forName("UTF-8")));
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }
            httpPost.setEntity(entity);
            consumer.sign(httpPost);
            debugHttpRequestBase(httpPost);
            httpClient = getDefaultHttpClient();
            return httpClient.execute(httpPost, new ResponseHandler<String>() {

                @Override
                public String handleResponse(HttpResponse arg0)
                        throws ClientProtocolException, IOException {

                    return getResponseBody(arg0);
                }
            });
        } catch (Exception e) {
            throw new WeiboOpenAPIException(e);
        } finally {
            clientShutDown(httpClient);
        }
    }

    // private String apacheUpload2(String url,
    // Iterable<Entry<String, Object>> httpParams)
    // throws WeiboOpenAPIException {
    // HttpClient httpClient = null;
    // OAuthConsumer consumer = getOAuthConsumer();
    // try {
    // HttpPost httpPost = new HttpPost(url);
    // if (httpParams != null) {
    // List<Part> multiparts = new ArrayList<Part>();
    // Object value = null;
    // HttpParameters params = new HttpParameters();
    // for (final Entry<String, Object> temp : httpParams) {
    // // OAuth 验证不需要 "source"
    // if (temp.getKey().equals("source"))
    // continue;
    // value = temp.getValue();
    // if (value instanceof File) {
    // try {
    // // multiparts.add(new FilePart(OAuth
    // // .percentDecode(temp.getKey()),
    // // new MyFilePart((File) value)));
    // multiparts.add(new FilePart(temp.getKey(),
    // new MyFilePart((File) value)));
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // }
    // }
    // if (value instanceof InputStream) {
    // // multiparts.add(new FilePart(OAuth.percentDecode(temp
    // // .getKey()), new MyFilePart(
    // // (InputStream) value)));
    // multiparts.add(new FilePart(temp.getKey(),
    // new MyFilePart((InputStream) value)));
    // } else {
    // // final String key =
    // // OAuth.percentDecode(temp.getKey());
    // final String key = temp.getKey();
    // final String string = temp.getValue().toString();
    // // final String value2 = OAuth.percentDecode(string);
    // final String value2 = string;
    // params.put(OAuth.percentEncode(key),
    // OAuth.percentEncode(value2));
    // multiparts.add(new StringPart(temp.getKey(), value
    // .toString(), "UTF-8"));
    // }
    // }
    // consumer.setAdditionalParameters(params);
    // httpPost.setEntity(new MultipartEntity(multiparts
    // .toArray(new Part[] {})));
    // }
    // consumer.sign(httpPost);
    // debugHttpRequestBase(httpPost);
    // httpClient = getDefaultHttpClient();
    // return httpClient.execute(httpPost, new ResponseHandler<String>() {
    //
    // @Override
    // public String handleResponse(HttpResponse arg0)
    // throws ClientProtocolException, IOException {
    //
    // return getResponseBody(arg0);
    // }
    // });
    // } catch (Exception e) {
    // throw new WeiboOpenAPIException(e);
    // } finally {
    // clientShutDown(httpClient);
    // }
    // }

    private static String getOauthProperties(String key) {
        if (System.getProperties().contains(key)) {
            return System.getProperty(key);
        }

        return oauthProperties.getProperty(key);
    }

    private static String getResponseBody(HttpResponse response)
            throws IOException {
        StringBuilder buffer = new StringBuilder();
        buffer.append("********** HTTP response BEGIN **********").append("\n");

        StatusLine statusLine = response.getStatusLine();
        String content = EntityUtils.toString(response.getEntity(), "UTF-8");

        buffer.append("Status line:").append("\n");
        buffer.append(statusLine).append("\n");

        buffer.append("Headers:").append("\n");
        Header[] allHeaders = response.getAllHeaders();
        if (allHeaders != null) {
            for (Header header : allHeaders) {
                buffer.append("\t").append(header).append("\n");
            }
        } else {
            buffer.append("empty").append("\n");
        }

        buffer.append("Response body:").append("\n");
        buffer.append(content).append("\n");
        buffer.append("********** HTTP response END **********");
        log.debug(buffer);

        if (response != null && statusLine != null) {
            if (statusLine.getStatusCode() == HttpStatus.SC_OK
                    && response.getEntity() != null) {
                return content;
            }
        }

        WeiboOpenAPIException apiException = new WeiboOpenAPIException(content,
                statusLine.getStatusCode());
        throw new IOException(apiException.getMessage());
    }

    public static void setTimeout(HttpClient client, int timeout) {
        setConnectionTimeout(client, timeout);
        setSoTimeout(client, timeout);
    }

    public static void setConnectionTimeout(HttpClient client, int timeout) {
        HttpConnectionParams.setConnectionTimeout(client.getParams(), timeout);
    }

    public static void setSoTimeout(HttpClient client, int timeout) {
        HttpConnectionParams.setSoTimeout(client.getParams(), timeout);
    }

    /**
     * 获取 OAuthConsumer 对象
     * 
     * @return
     */
    private OAuthConsumer getOAuthConsumer() {
        Preconditions.checkArgument(isNotBlank(token)
                && isNotBlank(tokenSecret),
                "token or tokenSecret mustn't be empty!");

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
                getOauthProperties(PROPERTY_CONSUMER_KEY),
                getOauthProperties(PROPERTY_CONSUMER_SECRET));
        consumer.setTokenWithSecret(token, tokenSecret);
        return consumer;
    }

    /**
     * 从 html 文档中获取授权码
     * 
     * @param html
     * @return
     * @throws WeiboOpenAPIException
     */
    private static String fetchOAuthVerifier(String html)
            throws WeiboOpenAPIException {
        String regex = "获取到的授权码：<span class=\"fb\">(\\S+)</span>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        throw new WeiboOpenAPIException("html:" + html
                + "\n\nThere is no \"授权码\" in page!");
    }

    /**
     * 将Get中的query section中去掉“SOURCE”参数
     * 
     * @param url
     * @return
     */
    private static String removeSourceParam(String url) {
        int queryIndex = url.indexOf("?");
        if (queryIndex > 0 && queryIndex != url.length() - 1) {
            String preUrl = url.substring(0, queryIndex);
            HttpParameters params = OAuth.decodeForm(url
                    .substring(queryIndex + 1));
            String source = params.getFirst("source", true);
            if (source != null
                    && source.equals(getOauthProperties(PROPERTY_CONSUMER_KEY))) {
                // OAuth 验证不需要 "source"
                params.remove("source");
            }
            HashMap<String, String> paramsMap = new HashMap<String, String>();
            for (String key : params.keySet()) {
                paramsMap.put(key, params.getFirst(key));
            }
            url = OAuth.addQueryParameters(preUrl, paramsMap);
        }
        return url;
    }

    /**
     * 获取 HttpClient 对象(某一次都是 new 的对象)
     * 
     * @return
     */
    private static HttpClient getDefaultHttpClient() {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        setTimeout(defaultHttpClient, DEFAULT_TIME_OUT);
        return defaultHttpClient;
    }

    /**
     * 关闭 HttpClient 相关链接资源
     * 
     * @param httpClient
     */
    private static void clientShutDown(HttpClient httpClient) {
        if (httpClient != null) {
            ClientConnectionManager connectionManager = httpClient
                    .getConnectionManager();
            if (connectionManager != null) {
                connectionManager.shutdown();
            }
        }
    }

    /**
     * 调试 HttpRequestBase 对象
     * 
     * @param base
     */
    private static void debugHttpRequestBase(HttpRequestBase base) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("********** HTTP request BEGIN **********").append("\n");
        buffer.append("uri:").append("\n");
        buffer.append(base.getURI()).append("\n");
        buffer.append("request line:").append("\n");
        buffer.append(base.getRequestLine()).append("\n");

        Header[] headers = base.getAllHeaders();
        if (headers != null && headers.length > 0) {
            buffer.append("headers:").append("\n");
            for (Header header : headers) {
                buffer.append("\t").append(header).append("\n");
            }
        }
        buffer.append("********** HTTP request END **********");
        log.debug(buffer);
    }

    // private class MyFilePart implements PartSource {
    //
    // private byte[] content;
    //
    // public MyFilePart(InputStream input) {
    // try {
    // content = toByteArray(input);
    // } catch (IOException e) {
    // } finally {
    // closeQuietly(input);
    // }
    // }
    //
    // public MyFilePart(File f) throws FileNotFoundException {
    // this(new FileInputStream(f));
    // }
    //
    // @Override
    // public InputStream createInputStream() throws IOException {
    // return new ByteArrayInputStream(content);
    // }
    //
    // @Override
    // public String getFileName() {
    // return "noname";
    // }
    //
    // @Override
    // public long getLength() {
    // return content.length;
    // }
    //
    // }

    /**
     * 使用 OAuth.percentEncode 进行 URLEncode
     * 
     * @author Administrator
     * 
     */
    private static class OauthUrlEncodedFormEntity extends StringEntity {
        public static OauthUrlEncodedFormEntity newInstance(
                List<? extends NameValuePair> parameters) {
            try {
                return new OauthUrlEncodedFormEntity(parameters);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException(e);
            }
        }

        private OauthUrlEncodedFormEntity(
                final List<? extends NameValuePair> parameters)
                throws UnsupportedEncodingException {
            super(formBuilder(parameters), "UTF-8");
            setContentType(URLEncodedUtils.CONTENT_TYPE);
        }

        private static String formBuilder(
                List<? extends NameValuePair> parameters) {
            StringBuilder buffer = new StringBuilder();
            for (NameValuePair pair : parameters) {
                final String name = OAuth.percentDecode(pair.getName());
                final String value = OAuth.percentDecode(pair.getValue());
                if (buffer.length() > 0) {
                    buffer.append("&");
                }
                buffer.append(OAuth.percentEncode(name)).append("=")
                        .append(OAuth.percentEncode(value));
            }
            return buffer.toString();
        }
    }

}

package com.sina.openapi;

import static com.clark.func.Functions.isBlank;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.http.HttpParameters;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class ApiClient {

    private static final String DEFAULT_APP_SECRET = "886cfb4e61fad4e4e9ba9dee625284dd";
    private static final String DEFAULT_APP_KEY = "2540340328";
    protected static DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

    static {
        final HttpParams httpParams = defaultHttpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
    }

    public static String PARAM_APP_KEY = "PARAM_APP_KEY";
    public static String PARAM_APP_SECRET = "PARAM_APP_SECRET";

    private String token;
    private String tokenSecret;

    public ApiClient(String token, String tokenSecret) {
        super();
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    private OAuthConsumer getOAuthConsumer() {
        final String appKey = System
                .getProperty(PARAM_APP_KEY, DEFAULT_APP_KEY);
        final String appSecret = System.getProperty(PARAM_APP_SECRET,
                DEFAULT_APP_SECRET);
        OAuthConsumer authConsumer = new CommonsHttpOAuthConsumer(appKey,
                appSecret);
        authConsumer.setTokenWithSecret(token, tokenSecret);
        return authConsumer;
    }

    public static ApiClient clientLogin(String userId, String password)
            throws OAuthException, IOException {
        String[] tokens = login(userId, password);
        return new ApiClient(tokens[0], tokens[1]);
    }

    public static String[] login(String userId, String password)
            throws OAuthException, IOException {
        final String appKey = System
                .getProperty(PARAM_APP_KEY, DEFAULT_APP_KEY);
        final String appSecret = System.getProperty(PARAM_APP_SECRET,
                DEFAULT_APP_SECRET);
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(appKey, appSecret);
        OAuthProvider provider = getOAuthProvider();
        String url = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
        HttpPost httpPost = new HttpPost(
                "http://api.t.sina.com.cn/oauth/authorize");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "submit"));
        pairs.add(new BasicNameValuePair("regCallback", OAuth
                .percentEncode(OAuth.addQueryParameters(url, "from", ""))));
        pairs.add(new BasicNameValuePair("oauth_token", consumer.getToken()));
        pairs.add(new BasicNameValuePair("oauth_callback", OAuth.OUT_OF_BAND));
        pairs.add(new BasicNameValuePair("from", ""));
        pairs.add(new BasicNameValuePair("userId", userId));
        pairs.add(new BasicNameValuePair("passwd", password));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(pairs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        httpPost.setEntity(entity);
        String html = null;
        try {
            html = defaultHttpClient.execute(httpPost,
                    new ResponseHandler<String>() {

                        @Override
                        public String handleResponse(HttpResponse arg0)
                                throws ClientProtocolException, IOException {
                            return EntityUtils.toString(arg0.getEntity(),
                                    "UTF-8");
                        }
                    });
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        }
        String oauthVerifier = fetchOAuthVerifier(html);
        if (oauthVerifier == null) {
            throw new IOException("There is no \"授权码\" in html!\n" + html);
        }
        provider.setOAuth10a(true);
        provider.retrieveAccessToken(consumer, oauthVerifier);
        return new String[] { consumer.getToken(), consumer.getTokenSecret() };
    }

    private static OAuthProvider getOAuthProvider() {
        return new CommonsHttpOAuthProvider(
                "http://api.t.sina.com.cn/oauth/request_token",
                "http://api.t.sina.com.cn/oauth/access_token",
                "http://api.t.sina.com.cn/oauth/authorize");
    }

    private static String fetchOAuthVerifier(String html) {
        String regex = "获取到的授权码：<span class=\"fb\">\\s*(\\S+)\\s*</span>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }

    public String execute(HttpUriRequest request) throws IOException,
            ClientProtocolException, OAuthException {
        signture(getOAuthConsumer(), request);
        return defaultHttpClient.execute(request,
                new ResponseHandler<String>() {
                    public String handleResponse(final HttpResponse response)
                            throws HttpResponseException, IOException {
                        StatusLine statusLine = response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        String mes = entity == null ? null : EntityUtils
                                .toString(entity);

                        if (statusLine.getStatusCode() >= 300) {
                            HttpResponseException throwable = null;
                            if (mes != null) {
                                throwable = new HttpResponseException(
                                        statusLine.getStatusCode(), mes);
                            } else {
                                throwable = new HttpResponseException(
                                        statusLine.getStatusCode(), statusLine
                                                .getReasonPhrase());
                            }
                            throw throwable;
                        }
                        return mes;
                    }
                });
    }

    private void signture(OAuthConsumer oAuthConsumer, HttpUriRequest request)
            throws OAuthException {
        if (request instanceof HttpEntityEnclosingRequestBase) {
            HttpEntityEnclosingRequestBase enclosingRequestBase = (HttpEntityEnclosingRequestBase) request;
            HttpEntity entity = enclosingRequestBase.getEntity();
            if (entity != null) {
                String mimeType = getContentMimeType(entity);
                if (mimeType == null) {
                    mimeType = "application/x-www-form-urlencoded";
                }

                if ("multipart/form-data".equals(mimeType)) {
                    // HTTP UPLOAD
                    signtureMultipartData(oAuthConsumer, enclosingRequestBase);
                } else if ("application/x-www-form-urlencoded".equals(mimeType)) {
                    // ignore HTTP POST
                } else {
                    throw new RuntimeException("Unknowen mime type: "
                            + mimeType);
                }
            }
        }
        HttpRequestBase httpRequestBase = (HttpRequestBase) request;
        oAuthConsumer.sign(httpRequestBase);
    }

    private String getContentMimeType(HttpEntity entity) {
        if (entity == null)
            return null;
        Header contentType = entity.getContentType();
        HeaderElement[] elements = contentType.getElements();
        if (elements == null || elements.length == 0) {
            return null;
        }

        return elements[0].getName();
    }

    private void signtureMultipartData(OAuthConsumer oAuthConsumer,
            HttpEntityEnclosingRequestBase enclosingRequestBase) {
        try {
            HttpEntity entity = enclosingRequestBase.getEntity();
            String content = EntityUtils.toString(entity, "UTF-8");
            String boundary = findBoundary(content);
            String[] parts = content.split(boundary);
            String name = null;
            String value = null;
            Matcher matcher = null;
            HttpParameters additionalParameters = new HttpParameters();
            for (String part : parts) {
                if (part.contains("Content-Transfer-Encoding: binary")) {
                    continue;
                }

                if (part.endsWith("\r\n")) {
                    matcher = PATTERN_NAME.matcher(part);
                    if (matcher.find()) {
                        name = matcher.group(1);
                        value = part.substring(0, part.length() - 2);
                        value = value.substring(value.lastIndexOf("\r\n") + 2);
                        System.out.println("name=" + name + "\tvalue:" + value);
                        additionalParameters.put(OAuth.percentEncode(name),
                                OAuth.percentEncode(value));
                    }
                }
            }
            oAuthConsumer.setAdditionalParameters(additionalParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Pattern PATTERN_NAME = Pattern
            .compile("Content\\-Disposition: form\\-data; name=\"(.+)\"");

    private static String findBoundary(String content) {
        if (isBlank(content) || !content.endsWith("--\r\n")) {
            return null;
        }

        int index = content.indexOf("\r\n");
        return content.substring(0, index);
    }

}

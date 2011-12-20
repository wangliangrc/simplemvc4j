package com.sina.openapi;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

public abstract class WeiboHttpClient {
    public abstract String httpGet(String url) throws WeiboOpenAPIException;

    final String post(String url, Map<String, Object> params)
            throws WeiboOpenAPIException {
        Object value = null;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            value = entry.getValue();
            if (value instanceof InputStream || value instanceof File) {
                return httpUpload(url, params.entrySet());
            }
        }
        return httpPost(url, params.entrySet());
    }

    public abstract String httpPost(String url,
            Iterable<Entry<String, Object>> httpParams)
            throws WeiboOpenAPIException;

    public abstract String httpUpload(String url,
            Iterable<Map.Entry<String, Object>> httpParams)
            throws WeiboOpenAPIException;

    public abstract String httpDelete(String url) throws WeiboOpenAPIException;

    public abstract String getConsumerKey();
}

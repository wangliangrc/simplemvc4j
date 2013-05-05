package com.baidu.aiu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	public static String get(String url) {
		HttpGet get = new HttpGet(url);
		return _doRequest(get);
	}

	public static String post(String url, HashMap<String, String> params) {
		HttpPost put = new HttpPost(url);
		put.setEntity(generateFormParam(params));
		return _doRequest(put);
	}

	private static String _doRequest(HttpUriRequest request) {
		// 取得HttpClient对象
		HttpClient httpclient = new DefaultHttpClient();
		// 请求HttpClient，取得HttpResponse
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// 请求成功
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 取得返回
			try {
				return EntityUtils.toString(httpResponse.getEntity());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}

		return null;
	}

	private static UrlEncodedFormEntity generateFormParam(
			HashMap<String, String> params) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		Iterator<String> it = params.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			String value = params.get(key);
			formparams.add(new BasicNameValuePair(key, value));
		}

		try {
			return new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean download(String httpUrl, String path, String fileName) {
		boolean rs = true;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			fileName = fileName.startsWith("/") ? fileName : "/" + fileName;
			File file = new File(path + fileName);
			final InputStream input = conn.getInputStream();

			IOUtils.copy(input, new FileOutputStream(file));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			rs = false;
		} catch (IOException e) {
			e.printStackTrace();
			rs = false;
		}
		return rs;
	}
}

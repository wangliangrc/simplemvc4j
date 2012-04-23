package com.sina.weibosdk.net;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.cache.CacheManager;
import com.sina.weibosdk.cache.CacheStrategy;
import com.sina.weibosdk.entity.FormFile;
import com.sina.weibosdk.exception.WeiboApiException;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.exception.WeiboIOException;
import com.sina.weibosdk.exception.WeiboInterruptException;
import com.sina.weibosdk.exception.WeiboParseException;
import com.sina.weibosdk.task.WeiboAssert;

/**
 * @author zhangqi
 */
public class HttpUrlConnectionEngine extends HttpEngine {

    private final int httpConnTimeout = WeiboSDKConfig.getInstance().getInt(
            WeiboSDKConfig.KEY_HTTP_CONNECTION_TIMEOUT);
    
    private final int httpsConnTimeout = WeiboSDKConfig.getInstance().getInt(
            WeiboSDKConfig.KEY_HTTPS_CONNECTION_TIMEOUT);
    
	private final int readTimeout = WeiboSDKConfig.getInstance().getInt(
			WeiboSDKConfig.KEY_READ_TIMEOUT);
	
	private final int defaultDownloadFileSize = WeiboSDKConfig.getInstance().getInt(
			WeiboSDKConfig.KEY_DEFAULT_DOWNLOAD_FILE_SIZE);
	
    private Context mContext;

    /**
     * http请求io异常后重试次数，默认为0，不重试
     */
    private final int retryCount = WeiboSDKConfig.getInstance(
    		).getInt(WeiboSDKConfig.KEY_RETRY_COUNT);

    private static final String BOUNDARY = "7cd4a6d158c";

    private static final String ENDLINE = "\r\n";

    public HttpUrlConnectionEngine(Context context) {
        mContext = context;
    }

    @Override
    public String get(String url, Bundle params, WeiboAssert wassert) throws WeiboException {
        HttpURLConnection conn = null;
        String response = "";
        int retried;
        url += Util.encodeGetUrlParams(params);
        for (retried = 0; retried < retryCount + 1; retried++) {
            try {
                Util.logd("url : " + url);
                conn = getHttpURLConnection(url, wassert);
                response = getResponse(conn);
                Util.logd("get response:" + response);
                Util.checkResponse(response);
                return response;
            } catch (IOException e) {
            	Util.loge(e.getMessage(), e);
            	// 如果是cancle导致的io异常，抛出WeiboInterruptException，跳出循环
                if (null != wassert) {
                    wassert.assertContinueRunning(conn);
                }
                if (retried >= retryCount) {
                    throw new WeiboIOException(e);
                }
            } catch (WeiboIOException e) {
            	Util.loge(e.getMessage(), e);
            	// 如果是cancle导致的io异常，抛出WeiboInterruptException，跳出循环
                if (null != wassert) {
                    wassert.assertContinueRunning(conn);
                }
                if (retried >= retryCount) {
                    throw e;
                }
            } catch (WeiboApiException e) {
            	// 如果是cancle导致的io异常，抛出WeiboInterruptException，跳出循环
                if (null != wassert) {
                    wassert.assertContinueRunning(conn);
                }
            	Util.loge(e.getMessage(), e);
                throw e;
            } catch (WeiboParseException e) {
            	Util.loge(e.getMessage(), e);
                throw e;
            } catch (WeiboInterruptException e) {
            	Util.logd(WeiboInterruptException.INTERRUPT_ERROR);
                throw e;
            } catch (Exception e) {
            	Util.loge(e.getMessage(), e);
                // 如果是cancle导致的异常，抛出WeiboInterruptException，跳出循环
                if (null != wassert) {
                    wassert.assertContinueRunning(conn);
                }
                throw new WeiboException(e);
            } finally {
                if (null != conn) {
                    conn.disconnect();
                }
            }
        }
        return response;

    }

    public String post(String url, Bundle params, List<FormFile> files, WeiboAssert wassert)
            throws WeiboException {
        HttpURLConnection conn = null;
        OutputStream os = null;
        String response = "";
        int retried;
        try {
        	url += Util.encodePostUrlParams(params);
        	for (retried = 0; retried < retryCount + 1; retried++) {
                try {
                    conn = getHttpURLConnection(url, wassert);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                            + BOUNDARY);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    
                    
                    StringBuilder entity = new StringBuilder();
                    entity.append("--" + BOUNDARY + ENDLINE);
                    String postBody = Util.encodePostBody(params, BOUNDARY);
                    entity.append(postBody);
                    Util.logd("body : " + postBody);
                    entity.append(ENDLINE + "--" + BOUNDARY + ENDLINE);
                    
                    long totalFileSize = 0;
                    if (null != files) {
                        for (FormFile formFile : files) {
                        	
                        	entity.append("Content-Disposition: form-data; name=\""
                                    + formFile.getFormName() + "\";filename=\""
                                    + formFile.getFilePath() + "\"" + ENDLINE);
                        	entity.append("Content-Type: " + formFile.getContentType() 
                        			+ ENDLINE + ENDLINE);
                        	
                        	File f = new File(formFile.getFilePath());
                        	totalFileSize += f.length();
                        }
                    }
                    
                    conn.setRequestProperty("Content-Length", String.valueOf(entity.length() 
                    		+ totalFileSize + (ENDLINE + "--" + BOUNDARY + ENDLINE).getBytes().length));
                    conn.connect();
                    
                    os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(entity.toString().getBytes());
                    
                    if (null != files) {
                        for (FormFile formFile : files) {
                        	byte b[] = new byte[4096];
                            int len = 0;
                            FileInputStream in = null;
                            try {
                            	in = new FileInputStream(formFile.getFilePath());
	                            while ((len = in.read(b)) != -1) {
	                                os.write(b, 0, len);
	                            }
                            } finally {
                            	if (in != null) {
                            		try {
                            			in.close();
                            		} catch (Exception e) {}
                            	}
                            }
                            // in.close();

                            os.write((ENDLINE + "--" + BOUNDARY + ENDLINE).getBytes());
                        }
                    }
                    os.flush();
                    Util.logd("url : " + url);
                    response = getResponse(conn);
                    Util.logd("post response:" + response);
                    Util.checkResponse(response);
                    return response;
                } catch (IOException e) {
                	Util.loge(e.getMessage(), e);
                	// 如果是cancle导致的io异常，抛出WeiboInterruptException，跳出循环
                    if (null != wassert) {
                        wassert.assertContinueRunning(conn);
                    }
                    if (retried >= retryCount) {
                        throw new WeiboIOException(e);
                    }
                } catch (WeiboIOException e) {
                	Util.loge(e.getMessage(), e);
                	// 如果是cancle导致的io异常，抛出WeiboInterruptException，跳出循环
                    if (null != wassert) {
                        wassert.assertContinueRunning(conn);
                    }
                    if (retried >= retryCount) {
                        throw e;
                    }
                } catch (WeiboApiException e) {
                	// 如果是cancle导致的io异常，抛出WeiboInterruptException，跳出循环
                    if (null != wassert) {
                        wassert.assertContinueRunning(conn);
                    }
                	Util.loge(e.getMessage(), e);
                    throw e;
                } catch (WeiboParseException e) {
                	Util.loge(e.getMessage(), e);
                    throw e;
                } catch (WeiboInterruptException e) {
                    throw e;
                } catch (Exception e) {
                	Util.loge(e.getMessage(), e);
                    // 如果是cancle导致的异常，抛出WeiboInterruptException，跳出循环
                    if (null != wassert) {
                        wassert.assertContinueRunning(conn);
                    }
                    throw new WeiboException(e);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Exception e) {
                        	Util.loge(e.getMessage(), e);
                        }
                    }

                    if (null != conn) {
                    	try {
                    		conn.disconnect();
                    	} catch (Exception e) {
                    		Util.loge(e.getMessage(), e);
                    	}
                    }
                }
            }
        } finally {
            
        }
        return response;
    }

    public void download(String url, CacheStrategy cs, IDownloadCallback callback, 
    		WeiboAssert wassert) throws WeiboException {
    	
    	HttpURLConnection conn = null;
    	InputStream in = null;
    	FileOutputStream fos = null;
    	
    	int fileSize = defaultDownloadFileSize;
        int retried;
        boolean isCacheMem = false;	// 是否内存缓存
        boolean isCacheFile = false; // 是否文件缓存
        String memKey = null;
        
        File cacheFile = null;	// 缓存文件
        File cacheTempFile = null; // 缓存临时文件
        
        for (retried = 0; retried < retryCount + 1; retried++) {
        	
            try {
            	if(cs == null || (TextUtils.isEmpty(cs.getCacheFilePath()) 
            			&& TextUtils.isEmpty(cs.getCacheMemKey()))) {
            		return;
            	}
            	
            	if(!TextUtils.isEmpty(cs.getCacheFilePath())) {
            		isCacheFile = true;
            		cacheFile = new File(cs.getCacheFilePath());
            		cacheTempFile = new File(cs.getCacheFilePath() + "_tmp");
            		
            		if(!Util.checkFileExist(cacheFile)) {
            			Util.mkfile(cacheFile);
            		}else {
            			isCacheFile = cs.isForceCover() ? true : false; 
            		}
            		
            		if(Util.checkFileExist(cacheTempFile)) {
            			Util.delFile(cacheTempFile);
            		}
            		
            	}
            	if(!TextUtils.isEmpty(cs.getCacheMemKey())) {
            		isCacheMem = true;
            		memKey = cs.getCacheMemKey();
            	}
            	
            	conn = getHttpURLConnection(url, wassert);
            	int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                	in = getInputStream(conn);
                	String headField = conn.getHeaderField("Content-Length");
                    if(!TextUtils.isEmpty(headField)) {
                    	try {
                    		fileSize = Integer.valueOf(headField);
                    	} catch(NumberFormatException e) {
                    		Util.loge(e.getMessage(), e);
                    	}
                    }
                } else {
                    throw new WeiboIOException(String.format("Invalid response from server: %s",
                            responseCode));
                }
                
            	int downloadLen = 0;
                int readLen = (int)(fileSize / cs.getPieceNum() + 0.5);
                byte[] buffer = new byte[readLen];
                fos = new FileOutputStream(cacheTempFile);
                
                if(callback != null) {
                	callback.onStart(cs);
                }
                while ((readLen = in.read(buffer)) != -1) {
                	downloadLen += readLen;
                	fos.write(buffer, 0, readLen);
                	if (downloadLen < fileSize) {
                		int progress = (int)((float)downloadLen / fileSize * 100 + 0.5);
                		progress = progress > 100 ? 100 : progress;
                		if(callback != null) {
                			callback.onProgress(cs, progress);
                		}
                	}
                }
                
                if(isCacheMem) {
                	Bitmap b = BitmapFactory.decodeFile(cacheTempFile.getAbsolutePath());
                	if(b != null && !b.isRecycled()) {
                		CacheManager.getInstance(mContext).addBitmapToCache(memKey, b);
                	}
                }
                
                if(isCacheFile) {
                	cacheTempFile.renameTo(cacheFile);
                }
                
                if(callback != null) {
                	callback.onFinish(cs);
                }
                
            } catch (IOException e) {
            	Util.loge(e.getMessage(), e);
            	Util.delFile(cacheFile, cacheTempFile);
            	// 如果是cancle导致的io异常，抛出WeiboInterruptException，跳出循环
                if(null != wassert) {
                    wassert.assertContinueRunning(conn);
                }
                if(retried >= retryCount) {
                    callback.onError(cs, new WeiboIOException(e));
                }
                
            } catch (WeiboIOException e) {
            	Util.loge(e.getMessage(), e);
            	Util.delFile(cacheFile, cacheTempFile);
            	// 如果是cancle导致的io异常，抛出WeiboInterruptException，跳出循环
                if(null != wassert) {
                    wassert.assertContinueRunning(conn);
                }
            	if(retried >= retryCount) {
                    callback.onError(cs, e);
                }
        	} catch (WeiboInterruptException e) {
        		Util.loge(e.getMessage());
        		Util.delFile(cacheFile, cacheTempFile);
                throw e;
            } catch (Exception e) {
            	Util.loge(e.getMessage(), e);
            	Util.delFile(cacheFile, cacheTempFile);
                // 如果是cancle导致的异常，抛出WeiboInterruptException，跳出循环
                if (null != wassert) {
                    wassert.assertContinueRunning(conn);
                }
                callback.onError(cs, new WeiboException(e));
            } finally {
                if (null != conn) {
                	try {
                		conn.disconnect();
                	} catch (Exception e) {
                		Util.loge(e.getMessage(), e);
                	}
                }
                if(null != in) {
                	try {
                		in.close();
                	} catch (Exception e) {
                		Util.loge(e.getMessage(), e);
                	}
                }
                if(null != fos) {
                	try {
                		fos.close();
                	} catch (Exception e) {
                		Util.loge(e.getMessage(), e);
                	}
                }
                
            }
        }
    	
    }
    
    
    /**
     * 从URLConnection中获取响应
     * 
     * @param conn
     * @return
     * @throws IOException
     * @throws WeiboIOException
     */
    private String getResponse(HttpURLConnection conn) throws IOException, WeiboIOException {
        InputStream inputStream = null;
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
            	inputStream = getInputStream(conn);
                return Util.read(inputStream);
            } else {
                throw new WeiboIOException(String.format("Invalid response from server: %s",
                        responseCode));
            }
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                	Util.loge(e.getMessage(), e);
                }
            }
        }

    }
    
    private InputStream getInputStream(HttpURLConnection conn) throws IOException {
    	InputStream inputStream = null;
    	inputStream = conn.getInputStream();
        String headField = conn.getHeaderField("Content-Encoding");
        if (!TextUtils.isEmpty(headField) && headField.toLowerCase().indexOf("gzip") > -1) {
            inputStream = new GZIPInputStream(inputStream);
        }
        return inputStream;
    }

    /**
     * 构造一个HttpURLConnection连接
     * 
     * @param url
     * @return
     * @throws IOException
     */
    protected HttpURLConnection getHttpURLConnection(String url, WeiboAssert wassert)
            throws IOException, WeiboInterruptException {
        HttpURLConnection conn = null;
        NetworkInfo netInfo = Util.getNetwrokInfo(mContext);
        Proxy proxy = Util.getProxy(mContext, netInfo);
        Util.logd("url : " + url);

        if (proxy != null) {
            Util.logd("proxy: " + proxy.toString());
            if (url.startsWith("https")) {
                conn = getSSLConnection(url, proxy);
                conn.setConnectTimeout(httpsConnTimeout);
            } else {
                conn = (HttpURLConnection) new URL(url).openConnection(proxy);
                conn.setConnectTimeout(httpConnTimeout);
            }
            // 检查Task是否cancel
            if (null != wassert) {
                wassert.assertContinueRunning(conn);
            }
        } else {
            if (url.startsWith("https")) {
                conn = getSSLConnection(url, null);
                conn.setConnectTimeout(httpsConnTimeout);
            } else {
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setConnectTimeout(httpConnTimeout);
            }
            // 检查Task是否cancel
            if (null != wassert) {
                wassert.assertContinueRunning(conn);
            }
        }

        conn.setReadTimeout(readTimeout);
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
        return conn;
    }

    protected HttpsURLConnection getSSLConnection(String url, Proxy proxy)
            throws MalformedURLException, IOException {
        HttpsURLConnection sslConnection = null;
        if (proxy != null) {
            sslConnection = (HttpsURLConnection) new URL(url).openConnection(proxy);
        } else {
            sslConnection = (HttpsURLConnection) new URL(url).openConnection();
        }
        trustAllHosts(sslConnection);
        return sslConnection;

    }

    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private final static TrustManager[] TRUST_ALL_CERTS = new TrustManager[] {
    	new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
			}
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
			}
		}
    };
    
    private void trustAllHosts(HttpsURLConnection conn) {
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(new KeyManager[0], TRUST_ALL_CERTS, new java.security.SecureRandom());
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(DO_NOT_VERIFY);
        } catch (Exception e) {
        	Util.loge(e.getMessage(), e);
        }
    }
    
    

}

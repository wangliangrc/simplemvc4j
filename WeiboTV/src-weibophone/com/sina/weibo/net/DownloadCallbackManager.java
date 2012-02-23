package com.sina.weibo.net;

import java.util.HashMap;

public class DownloadCallbackManager {
    private static DownloadCallbackManager mInstance = null;
    private HashMap<String, ImageDownloadCallBack> downloadMap = new HashMap<String, ImageDownloadCallBack>();

    // private HashMap<String, Object> locksMap = new HashMap<String, Object>();

    private DownloadCallbackManager() {
    }

    public synchronized static DownloadCallbackManager getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadCallbackManager();
        }

        return mInstance;
    }

    public synchronized void put(String picName, IDownloadState callback) {
        if (callback != null) {
            if (downloadMap.containsKey(picName)) {
                downloadMap.get(picName).setCallback(callback);
            } else {
                // ImageDownloadCallBack imageCallback = new
                // ImageDownloadCallBack(callback, false, new Object());
                ImageDownloadCallBack imageCallback = new ImageDownloadCallBack();
                imageCallback.setCallback(callback);
                downloadMap.put(picName, imageCallback);
            }
        }
    }

    public synchronized void setCallbackStart(String picName, boolean start) {
        if (downloadMap.containsKey(picName)) {
            downloadMap.get(picName).setStart(start);
        }
    }

    public synchronized boolean isCallbackStart(String picName) {
        if (downloadMap.containsKey(picName)) {
            return downloadMap.get(picName).isStart();
        }

        return false;
    }

    public synchronized void remove(String picName) {
        downloadMap.remove(picName);
        // locksMap.remove(picName);
    }

    public synchronized boolean contains(String picPath) {
        return downloadMap.containsKey(picPath);
    }

    public synchronized IDownloadState getCallback(String picName,
            IDownloadState defaultCallback) {
        if (downloadMap.containsKey(picName)) {
            IDownloadState callback = downloadMap.get(picName).getCallback();
            if (callback == null) {
                downloadMap.get(picName).setCallback(defaultCallback);
                return defaultCallback;
            } else {
                return callback;
            }
        }

        return defaultCallback;
    }

    public Object getLock(String picName) {
        if (downloadMap.containsKey(picName)) {
            return downloadMap.get(picName).getLock();
        }

        return new Object();
    }

    public boolean isDownloadStop(String picName) {
        if (downloadMap.containsKey(picName)) {
            return downloadMap.get(picName).isDownloadStop();
        }

        return false;
    }

    public void setDownloadStop(String picName) {
        if (downloadMap.containsKey(picName)) {
            downloadMap.get(picName).setDownloadStop(true);
        }
    }

    public void setDownloadStart(String picName) {
        if (downloadMap.containsKey(picName)) {
            downloadMap.get(picName).setDownloadStop(false);
        }
    }
}
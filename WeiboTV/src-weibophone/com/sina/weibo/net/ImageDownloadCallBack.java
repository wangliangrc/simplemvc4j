package com.sina.weibo.net;

public class ImageDownloadCallBack {
    private IDownloadState callback;
    private boolean start;
    private final Object lock = new Object();;
    private boolean downloadStop;

    public ImageDownloadCallBack() {
        super();
    }

    // public ImageDownloadCallBack(IDownloadState callback, boolean start,
    // Object lock) {
    // super();
    // this.callback = callback;
    // this.start = start;
    // this.lock = lock;
    // }

    public IDownloadState getCallback() {
        return callback;
    }

    public void setCallback(IDownloadState callback) {
        this.callback = callback;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public Object getLock() {
        return lock;
    }

    // public void setLock(Object lock) {
    // this.lock = lock;
    // }
    public boolean isDownloadStop() {
        return downloadStop;
    }

    public void setDownloadStop(boolean downloadStop) {
        this.downloadStop = downloadStop;
    }
}
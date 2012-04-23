package com.sina.weibosdk.cache;

public class CacheStrategy {

	private String mCacheFilePath;
	private String mCacheMemKey;
	private boolean isCover = true;
	private int mPieceNum = 10;
	
	public void setCacheFilePath(String path) {
		this.mCacheFilePath = path;
	}
	
	public String getCacheFilePath() {
		return this.mCacheFilePath;
	}
	
	public void setCacheMemKey(String memKey) {
		this.mCacheMemKey = memKey;
	}
	
	public String getCacheMemKey() {
		return this.mCacheMemKey;
	}

	/**
	 * 如果存在本地文件缓存，或内存缓存
	 * 是否进行覆盖
	 * @param cover
	 */
	public void setForceCover(boolean cover) {
		this.isCover = cover;
	}
	
	public boolean isForceCover() {
		return this.isCover;
	}
	
	/**
	 * 下载进度，分几片进行通知
	 * @param percent 默认是 10，
	 */
	public void setPieceNum(int piecenum) {
		mPieceNum = piecenum;
	}
	
	/**
	 * 下载进度，分几片进行通知
	 * @param percent 默认是 10，
	 */
	public int getPieceNum() {
		return mPieceNum;
	}
	
}

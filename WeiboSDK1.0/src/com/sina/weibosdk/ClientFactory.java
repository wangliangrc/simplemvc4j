package com.sina.weibosdk;

import com.sina.weibosdk.net.DefaultHttpsStrategy;

import android.content.Context;

public final class ClientFactory {

	private static ClientFactory mSelf;
	
	private Context mContext;
	
	private ClientFactory(Context ctx) {
		mContext = ctx;
	}
	
	public static synchronized ClientFactory getInstance(Context ctx) {
		if(mSelf == null) {
			mSelf = new ClientFactory(ctx);
		}
		return mSelf;
	}
	
	public WeiboClient createWeiboClient() {
		WeiboClient client = new WeiboClient(mContext);
		client.setNetRequestStrategy(new DefaultHttpsStrategy(mContext));
		return client;
	}
	
	public AsyncWeiboClient createAsyncWeiboClient() {
		AsyncWeiboClient client = new AsyncWeiboClient(mContext);
		client.setNetRequestStrategy(new DefaultHttpsStrategy(mContext));
		return client;
	}
	
}

package com.sina.weibosdk.parser;

import com.sina.weibosdk.entity.Geo;
import com.sina.weibosdk.entity.Message;
import com.sina.weibosdk.entity.MessageList;
import com.sina.weibosdk.entity.Status;
import com.sina.weibosdk.entity.StatusList;
import com.sina.weibosdk.entity.UserInfo;
import com.sina.weibosdk.exception.WeiboParseException;

public class JsonParser {

	static {
		System.loadLibrary("cjson");
	}
	
	private JsonParser() {}
	
    public static native Geo parserGeo(String json) throws WeiboParseException;
    
    public static native Message parserMessage(String json) throws WeiboParseException;
    
    public static native MessageList parserMessageList(String json) throws WeiboParseException;
    
    public static native Status parserStatus(String json) throws WeiboParseException;
    
    public static native StatusList parserStatusList(String json) throws WeiboParseException;
    
    public static native UserInfo parserUserInfo(String json) throws WeiboParseException;
    
}

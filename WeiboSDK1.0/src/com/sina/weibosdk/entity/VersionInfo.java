package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class VersionInfo extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8139180831537929345L;

	private String version;
	
	private String download;
	
	private String wapurl;
	
	private String md5;
	
	private String desc;
	
	private String changeDate;
	
	private String prompt;
	
	private String poptime;
	
	
	public VersionInfo() {
	}

	public VersionInfo(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            this.version = obj.optString("version");
            this.download = obj.optString("download");
            this.wapurl = obj.optString("wapurl");
            this.md5 = obj.optString("md5");
            this.desc = obj.optString("desc");
            this.changeDate = obj.optString("changedate");
            this.prompt = obj.optString("prompt");
            this.poptime = obj.optString("poptime");

		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getWapurl() {
		return wapurl;
	}

	public void setWapurl(String wapurl) {
		this.wapurl = wapurl;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getPoptime() {
		return poptime;
	}

	public void setPoptime(String poptime) {
		this.poptime = poptime;
	}

}

package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Square extends ResponseBean {
	private static final long serialVersionUID = 1895636316445847135L;
	private String id;
	private String title;
	private String icon;
	private String link;
	private String platform_params;
	private String unreadname;
	private String download;
	private String groupid;
	private String grouptitle;
	private String digestcatid;
	private String filesize;
	private String description;

	public Square() {}
	
	public Square(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected Square parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			this.id = obj.optString("id");
			this.title = obj.optString("title");
			this.icon = obj.optString("icon");
			this.link = obj.optString("link");
			this.platform_params = obj.optString("platform_params");
			this.unreadname = obj.optString("unreadname");
			this.download = obj.optString("download");
			this.groupid = obj.optString("groupid");
			this.grouptitle = obj.optString("grouptitle");
			this.digestcatid = obj.optString("digestcatid");
			this.filesize = obj.optString("filesize");
			this.description = obj.optString("description");
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
			throw new WeiboParseException(PARSE_ERROR, e);
		}
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Square other = (Square) obj;
		if (!id.equals(other.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id.hashCode();
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPlatformParams() {
		return platform_params;
	}

	public void setPlatformParams(String platform_params) {
		this.platform_params = platform_params;
	}

	public String getUnreadname() {
		return unreadname;
	}

	public void setUnreadname(String unreadname) {
		this.unreadname = unreadname;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGrouptitle() {
		return grouptitle;
	}

	public void setGrouptitle(String grouptitle) {
		this.grouptitle = grouptitle;
	}

	public String getDigestcatid() {
		return digestcatid;
	}

	public void setDigestcatid(String digestcatid) {
		this.digestcatid = digestcatid;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

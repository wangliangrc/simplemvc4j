package com.baidu.aiu.model;

public class UpdateVersionModel {
	private String url = null;
	private String version = null;
	private boolean needUpdate = false;

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public UpdateVersionModel() {

	}

	public UpdateVersionModel(String url, String version) {
		this.url = url;
		this.version = version;
	}
}

package com.baidu.aiu.api.model;

public class UpdateVersionModel {
	private String urlPatch = null;
	private String urlConf = null;
	private String version = null;
	private String target = null;

	private boolean needUpdate = false;

	public String getUrlPatch() {
		return urlPatch;
	}

	public void setUrlPatch(String urlPatch) {
		this.urlPatch = urlPatch;
	}

	public String getUrlConf() {
		return urlConf;
	}

	public void setUrlConf(String urlConf) {
		this.urlConf = urlConf;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public UpdateVersionModel() {

	}

}

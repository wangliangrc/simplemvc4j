package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class MapImage extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9029883373133727331L;

	private String mapImageUrl = "";
	
	public MapImage() {}
	
	public MapImage(String json) throws WeiboParseException {
		parse(json);
	}
	
	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
            JSONObject obj = new JSONObject(json);
            this.mapImageUrl = obj.optString("map_image_url");
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
	}

	public String getMapImageUrl() {
		return mapImageUrl;
	}

	public void setMapImageUrl(String mapImageUrl) {
		this.mapImageUrl = mapImageUrl;
	}

}

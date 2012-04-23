package com.sina.weibosdk.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Geo extends ResponseBean {

    private static final long serialVersionUID = -1488289272954669528L;

    private String type;

    private double[] coordinates;

    public Geo() {
	}

	public Geo(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected Geo parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            type = obj.optString("type");

            JSONArray coorArray = obj.optJSONArray("coordinates");

            if (null != coorArray) {
                int size = coorArray.length();
                coordinates = new double[size];
                for (int i = 0; i < size; i++) {
                    coordinates[i] = coorArray.getDouble(i);
                }
            }
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

}

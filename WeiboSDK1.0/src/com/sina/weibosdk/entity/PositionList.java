package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class PositionList extends ResponseBean {

    private static final long serialVersionUID = -1149428096346549503L;

    /**
     * 附近地点列表
     */
    private List<Position> pois = new ArrayList<Position>(10);

    /**
     * 中心点
     */
    private Position centerPoi;
    
    /**
     * 附近地点总数
     */
    private int total_number;

    public PositionList() {}
    
    public PositionList(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected PositionList parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray stausArray = obj.optJSONArray("pois");
            int size = stausArray.length();
            for (int i = 0; i < size; i++) {
                pois.add(new Position(stausArray.getString(i)));
            }
            String ctrPoi = obj.optString("center_poi");
            if (!TextUtils.isEmpty(ctrPoi)) {
            	this.centerPoi = new Position(ctrPoi);
            }
            this.total_number = obj.optInt("total_number");
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }

    public List<Position> getPois() {
        return pois;
    }

    public void setPois(ArrayList<Position> pois) {
        this.pois = pois;
    }

    public Position getCenterPoi() {
		return centerPoi;
	}

	public void setCenterPoi(Position centerPoi) {
		this.centerPoi = centerPoi;
	}

	public int getTotalNumber() {
        return total_number;
    }

    public void setTotalNumber(int total_number) {
        this.total_number = total_number;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		PositionList other = (PositionList) obj;

		if (!pois.equals(other.getPois())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pois.hashCode();

		return result;
	}

}

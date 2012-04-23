package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class SquareList extends ResponseBean {

	private static final long serialVersionUID = -6029490294420650510L;
	private List<Square> lists = new ArrayList<Square>(10);
	private int total_number;

	public SquareList() {}
	
	public SquareList(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray stausArray = obj.optJSONArray("list");
			int size = stausArray.length();
			for (int i = 0; i < size; i++) {
				lists.add(new Square(stausArray.getString(i)));
			}
			this.total_number = obj.optInt("total_number");
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

		SquareList other = (SquareList) obj;
		if (!lists.equals(other.getLists())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lists.hashCode();
		return result;
	}

	public List<Square> getLists() {
		return lists;
	}

	public void setLists(ArrayList<Square> lists) {
		this.lists = lists;
	}

	public int getTotalNumber() {
		return total_number;
	}

	public void setTotalNumber(int total_number) {
		this.total_number = total_number;
	}

}

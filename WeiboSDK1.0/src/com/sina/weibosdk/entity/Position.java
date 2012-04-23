package com.sina.weibosdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

public class Position extends ResponseBean {

	private static final long serialVersionUID = -2901810119424113985L;

	/**
	 * 位置ID
	 */
	private String poiid;

	/**
	 * 位置名称
	 */
	private String title;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 经度
	 */
	private String lon;

	/**
	 * 纬度
	 */
	private String lat;

	/**
	 * 类别
	 */
	private String category;

	/**
	 * 类别
	 */
	private String categorys;
	
	/**
	 * 城市
	 */
	private String city;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 未知？？？
	 */
	private String url;

	/**
	 * 电话号码
	 */
	private String phone;

	/**
	 * 邮编
	 */
	private String postcode;

	/**
	 * 微博ID
	 */
	private String weibo_id;

	/**
	 * 未知？？？
	 */
	private int checkin_num;

	/**
	 * 未知？？？
	 */
	private String checkin_user_num;

	/**
	 * 未知？？？
	 */
	private int tip_num;

	/**
	 * 图片个数
	 */
	private int photo_num;

	/**
	 * 图标URL
	 */
	private String icon;
	
	/**
	 * 未知？？？
	 */
	private int todo_num;

	/**
	 * 距离
	 */
	private int distance;

	public Position() {}
	
	public Position(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected Position parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			this.poiid = obj.optString("poiid");
			this.title = obj.optString("title");
			this.address = obj.optString("address");
			this.lon = obj.optString("lon");
			this.lat = obj.optString("lat");
			this.category = obj.optString("category");
			this.categorys = obj.optString("categorys");
			this.city = obj.optString("city");
			this.province = obj.optString("province");
			this.country = obj.optString("country");
			this.url = obj.optString("url");
			this.icon = obj.optString("icon");
			this.phone = obj.optString("phone");
			this.postcode = obj.optString("postcode");
			this.weibo_id = obj.optString("weibo_id");
			this.checkin_num = obj.optInt("checkin_num");
			this.checkin_user_num = obj.optString("checkin_user_num");
			this.tip_num = obj.optInt("tip_num");
			this.photo_num = obj.optInt("photo_num");
			this.todo_num = obj.optInt("todo_num");
			this.distance = obj.optInt("distance");
		} catch (JSONException e) {
			Util.loge(e.getMessage(), e);
			throw new WeiboParseException(PARSE_ERROR, e);
		}
		return this;
	}

	public String getPoiid() {
		return poiid;
	}

	public void setPoiid(String poiid) {
		this.poiid = poiid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getWeiboId() {
		return weibo_id;
	}

	public void setWeiboId(String weibo_id) {
		this.weibo_id = weibo_id;
	}

	public int getCheckInNum() {
		return checkin_num;
	}

	public void setCheckInNum(int checkin_num) {
		this.checkin_num = checkin_num;
	}

	public String getCheckInUserNum() {
		return checkin_user_num;
	}

	public void setCheckinUserNum(String checkin_user_num) {
		this.checkin_user_num = checkin_user_num;
	}

	public int getTipNum() {
		return tip_num;
	}

	public void setTipNum(int tip_num) {
		this.tip_num = tip_num;
	}

	public int getPhotoNum() {
		return photo_num;
	}

	public void setPhotoNum(int photo_num) {
		this.photo_num = photo_num;
	}

	public int getTodoNum() {
		return todo_num;
	}

	public void setTodoNum(int todo_num) {
		this.todo_num = todo_num;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public String getCategorys() {
		return categorys;
	}

	public void setCategorys(String categorys) {
		this.categorys = categorys;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Position other = (Position) obj;

		if (!poiid.equals(other.getPoiid())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + poiid.hashCode();

		return result;
	}

}

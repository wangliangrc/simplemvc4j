package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

/**
 * 收藏列表
 * 
 * @author zhangqi
 * 
 */
public class FavoritesList extends ResponseBean {

    private static final long serialVersionUID = -3004338633565026206L;

    private List<Favorite> favList = new ArrayList<Favorite>();

    private int total_number;

    public FavoritesList() {}
    
    public FavoritesList(String json) throws WeiboParseException {
        parse(json);
    }

    @Override
    protected FavoritesList parse(String json) throws WeiboParseException {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray favoriteArray = obj.optJSONArray("favorites");
            if (null != favoriteArray) {
                int size = favoriteArray.length();
                for (int i = 0; i < size; i++) {
                    favList.add(new Favorite(favoriteArray.getString(i)));
                }
            }

            total_number = obj.optInt("total_number");
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }

    public List<Favorite> getFavList() {
        return favList;
    }

    public void setFavList(List<Favorite> favList) {
        this.favList = favList;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
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

        FavoritesList other = (FavoritesList) obj;

        if (!favList.equals(other.getFavList())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + favList.hashCode();

        return result;
    }

}

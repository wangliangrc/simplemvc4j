package com.sina.weibo.models;

import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class NearByUserInfoList extends DataObject implements Serializable {

    private static final long serialVersionUID = 7678521592901029483L;

    public UserInfoList userInfoList;

    public int count;

    private double lat;

    private double lon;

    private long updateTime;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public NearByUserInfoList() {
        userInfoList = new UserInfoList();
        parser = userInfoList.parser;
    }

    public NearByUserInfoList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public NearByUserInfoList initFromString(String xmlStr)
            throws WeiboParseException {
        userInfoList = new UserInfoList();
        parser = userInfoList.parser;
        return parseUserInfoList(xmlStr);
    }

    private NearByUserInfoList parseUserInfoList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }

        userInfoList = userInfoList.parse();
        count = userInfoList.count;
        return this;
    }

    @Override
    public NearByUserInfoList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        return null;
    }

    @Override
    protected NearByUserInfoList parse() throws WeiboParseException {
        return this;
    }

}

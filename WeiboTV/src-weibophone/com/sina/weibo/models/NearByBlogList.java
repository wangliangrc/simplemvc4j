package com.sina.weibo.models;

import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class NearByBlogList extends DataObject implements Serializable {
    private static final long serialVersionUID = -5809030253143661178L;

    public MBlogList mblogList;

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

    public NearByBlogList() {
        mblogList = new MBlogList();
        parser = mblogList.parser;
    }

    public NearByBlogList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public NearByBlogList initFromString(String xmlStr)
            throws WeiboParseException {
        mblogList = new MBlogList();
        parser = mblogList.parser;
        return parseUserInfoList(xmlStr);
    }

    private NearByBlogList parseUserInfoList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }

        mblogList = mblogList.parse();
        count = mblogList.count;
        return this;
    }

    @Override
    public NearByBlogList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        return null;
    }

    @Override
    protected NearByBlogList parse() throws WeiboParseException {
        return this;
    }

}

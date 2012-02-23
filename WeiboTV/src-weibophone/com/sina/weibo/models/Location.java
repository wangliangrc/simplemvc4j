package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class Location extends DataObject implements Serializable {
    private static final long serialVersionUID = 3296305331008643869L;
    public String longitude;// 经度
    public String latitude;// 纬度
    public String name;
    public String poiid;
    public String xid;
    public String address;
    public String distance;
    public String category;
    public int checkinUserNum;
    public boolean offset = true;

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        Class<?> cla = o.getClass();
        if (cla == getClass()) {
            Location other = (Location) o;
            if (other.poiid.equals(poiid))
                return true;
        }
        return false;
    }

    public int hashCode() {
        return poiid.hashCode() * 101 >> 12;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Location [longitude=").append(longitude)
                .append(", latitude=").append(latitude).append(", name=")
                .append(name).append(", poiid=").append(poiid).append(", xid=")
                .append(xid).append(", address=").append(address)
                .append(", distance=").append(distance).append(", category=")
                .append(category).append(", checkinUserNum=")
                .append(checkinUserNum).append(", offset=").append(offset)
                .append("]");
        return builder.toString();
    }

    public Location() {
    }

    public Location(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public Location(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    public Location initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public Location initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected Location parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("poiid")) {
                            this.poiid = parseText(parser);
                            if (this.poiid.equals(""))
                                return null;
                        } else if (parser.getName().equals("xid")) {
                            this.xid = parseText(parser);
                        } else if (parser.getName().equals("latitude")) {
                            this.latitude = parseText(parser);
                        } else if (parser.getName().equals("longitude")) {
                            this.longitude = parseText(parser);
                        } else if (parser.getName().equals("address")) {
                            this.address = parseText(parser);
                        } else if (parser.getName().equals("distance")) {
                            this.distance = parseText(parser);
                        } else if (parser.getName().equals("name")) {
                            this.name = parseText(parser);
                        } else if (parser.getName().equals("categorys")) {
                            this.category = parseText(parser);
                        } else if (parser.getName().equals("checkin_user_num")) {
                            this.checkinUserNum = Integer
                                    .parseInt(parseText(parser));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("info")) {
                            return this;
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (NumberFormatException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (IOException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }

}

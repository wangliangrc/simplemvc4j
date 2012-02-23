package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class FanList extends DataObject implements Serializable {

    private static final long serialVersionUID = 7707977960038215514L;

    public List<Fan> fanList;

    public int count;

    public FanList() {
        fanList = new ArrayList<Fan>();
    }

    public FanList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public FanList initFromString(String xmlStr) throws WeiboParseException {
        fanList = new ArrayList<Fan>();
        return parseFanList(xmlStr);
    }

    private FanList parseFanList(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public FanList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected FanList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            String c = parseText(parser);
                            if (c == null || c.equals("")) {
                                count = 0;
                            } else {
                                count = new Integer(c);
                            }
                        } else if (parser.getName().equals("info")) {
                            Fan fan = new Fan(parser);
                            if (fan != null) {
                                this.fanList.add(fan);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (NumberFormatException e) {
            throw new WeiboParseException(e);
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(e);
        } catch (IOException e) {
            throw new WeiboParseException(e);
        }

        return this;
    }

}

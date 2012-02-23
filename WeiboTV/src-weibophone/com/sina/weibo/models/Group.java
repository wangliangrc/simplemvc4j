package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 分组信息
 * 
 * @author duncan
 * 
 */
public class Group extends DataObject implements Serializable {

    public static final String GROUP_ID_ALL = "1";
    public static final String GROUP_ID_MY_MBLOG = "2";
    public static final String GROUP_ID_MY_FAV = "3";
    public static final String GROUP_ID_NEARBY_WEIBO = "4";

    private static final long serialVersionUID = -1696695294971358566L;
    /**
     * 分组id
     */
    public String gid;
    /**
     * 分组名称
     */
    public String title;
    /**
     * 分组内成员数
     */
    public int count;
    /**
     * 是否在首页显示
     */
    public boolean disp;

    public Group() {
    }

    public Group(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public Group(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    public Group initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public Group initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected Group parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("gid")) {
                            this.gid = parseText(parser);
                        } else if (parser.getName().equals("title")) {
                            this.title = parseText(parser);
                        } else if (parser.getName().equals("count")) {
                            String c = parseText(parser);
                            if (c == null || c.equals("")) {
                                count = 0;
                            } else {
                                count = new Integer(c);
                            }
                        } else if (parser.getName().equals("disp")) {
                            String d = parseText(parser);
                            if (d == null || d.equals("")) {
                                this.disp = false;
                            } else {
                                this.disp = Integer.parseInt(d) == 1 ? true
                                        : false;

                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
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
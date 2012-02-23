package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class Fan extends DataObject implements Serializable {
    private static final long serialVersionUID = 7059316225724805091L;
    public String uid;
    public String nick;
    public String remark;
    public int gender;
    public String portrait;
    public int num;
    public int relation;
    public String mblogcontent;
    public Date mblogtime;
    public boolean isVip;
    public boolean isVipsubtype;
    public boolean isLevel;

    public Fan() {
    }

    public Fan(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public Fan(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    public Fan initFromParser(XmlPullParser _parser) throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public Fan initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected Fan parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("uid")) {
                            this.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            this.nick = parseText(parser);
                        } else if (parser.getName().equals("remark")) {
                            this.remark = parseText(parser);
                        } else if (parser.getName().equals("gender")) {
                            this.gender = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("portrait")) {
                            this.portrait = parseText(parser);
                        } else if (parser.getName().equals("num")) {
                            this.num = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("relation")) {
                            this.relation = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("content")) {
                            this.mblogcontent = parseText(parser);
                        } else if (parser.getName().equals("time")) {
                            this.mblogtime = new Date(
                                    Long.parseLong(parseText(parser)) * 1000);
                        } else if (parser.getName().equals("vip")) {// 1为用户认证
                            this.isVip = (parseText(parser).equals("1")) ? true
                                    : false;
                        } else if (parser.getName().equals("vipsubtype")) {// 大于0为企业认证
                            this.isVipsubtype = (Integer
                                    .parseInt(parseText(parser))) > 0 ? true
                                    : false;
                        } else if (parser.getName().equals("level")) {// 7为达人
                            this.isLevel = (parseText(parser).equals("7")) ? true
                                    : false;
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
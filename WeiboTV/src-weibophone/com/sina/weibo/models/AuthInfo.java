package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class AuthInfo extends DataObject implements Serializable {
    private static final long serialVersionUID = 3398411029247311346L;
    /**
     * 验证码加密串
     */
    public String cpt;
    /**
     * 验证码图片地址
     */
    public String pic;
    /**
     * 验证码问题
     */
    public String q;

    public AuthInfo() {
    }

    public AuthInfo(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public AuthInfo(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    public AuthInfo initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public AuthInfo initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected AuthInfo parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG: {
                        if (parser.getName().equalsIgnoreCase("cpt")) {
                            this.cpt = parseText(parser);
                        } else if (parser.getName().equalsIgnoreCase("pic")) {
                            this.pic = parseText(parser);
                        } else if (parser.getName().equalsIgnoreCase("q")) {
                            this.q = parseText(parser);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(e);
        } catch (IOException e) {
            throw new WeiboParseException(e);
        }
        return this;
    }
}
package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class NetResult extends DataObject implements Serializable {

    private static final long serialVersionUID = -8518640564536696788L;

    private String result;
    public String mblogid;
    public String favid;

    public boolean isSuccessful() {
        return "1".equals(result) || result == null;
    }

    public NetResult() {
    }

    public NetResult(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public NetResult(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    public NetResult initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public NetResult initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected NetResult parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equalsIgnoreCase("result")) {
                            this.result = parseText(parser);
                        } else if (parser.getName().equalsIgnoreCase("mblogid")) {
                            this.mblogid = parseText(parser);
                        } else if (parser.getName().equalsIgnoreCase("favid")) {
                            this.favid = parseText(parser);
                        }
                        break;
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
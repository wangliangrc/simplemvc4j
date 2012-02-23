package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 未读消息
 * 
 * @author duncan
 * 
 */
public class UnreadNum extends DataObject implements Serializable {
    private static final long serialVersionUID = 4323756510315597242L;
    public int comment;
    public int newfans;
    public int message;
    public int atmsg;
    public boolean newmblog;
    public int mblog;

    public UnreadNum() {
    }

    public UnreadNum(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public UnreadNum(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public DataObject initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public DataObject initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected DataObject parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("comment")) {
                            this.comment = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("newfans")) {
                            this.newfans = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("message")) {
                            this.message = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("atmsg")) {
                            this.atmsg = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("mblog")) {
                            this.newmblog = (parseText(parser).equals("1") ? true
                                    : false);
                        } else if (parser.getName().equals("newmblog")) {
                            this.mblog = Integer.parseInt(parseText(parser));
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
package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class ErrorMessage extends DataObject implements Serializable {

    private static final long serialVersionUID = -2861166030329864848L;

    private static final String ERROR_USER_OR_PASSWORD_WRONG = "-100";

    @SuppressWarnings("unused")
    private static final String ERROR_USER_NOT_EXSIT = "-101";

    @SuppressWarnings("unused")
    private static final String ERROR_PASSWORD_WRONG = "-102";

    /**
     * 错误码
     */
    public String errno;
    /**
     * 错误信息
     */
    public String errmsg;

    /**
     * 错误的url
     */
    public String errurl;

    public ErrorMessage() {
    }

    public ErrorMessage(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public ErrorMessage(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    public ErrorMessage initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public ErrorMessage initFromString(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected ErrorMessage parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("errno")) {
                            this.errno = parseText(parser);
                        } else if (parser.getName().equals("errmsg")) {
                            this.errmsg = parseText(parser);
                        } else if (parser.getName().equals("errurl")) {
                            this.errurl = parseText(parser);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (IOException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }

    public boolean isWrongPassword() {
        if (ERROR_USER_OR_PASSWORD_WRONG.equals(errno)) {
            return true;
        } else {
            return false;
        }
    }
}
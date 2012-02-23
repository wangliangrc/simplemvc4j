package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 用于测速的Url
 * 
 * @author duncan
 * 
 */
public class UrlItem extends DataObject implements Serializable {

    private static final long serialVersionUID = 9066720590236803053L;

    /**
     * url id
     */
    public String urlId;

    /**
     * url地址
     */
    public String url;

    public UrlItem() {
    }

    public UrlItem(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public UrlItem(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    public UrlItem initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public UrlItem initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected UrlItem parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("id")) {
                            this.urlId = parseText(parser);
                        } else if (parser.getName().equals("url")) {
                            this.url = parseText(parser);
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
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (IOException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }
}
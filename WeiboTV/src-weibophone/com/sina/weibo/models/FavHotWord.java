package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 话题
 * 
 * @author duncan
 * 
 */
public class FavHotWord extends DataObject implements Serializable {
    private static final long serialVersionUID = 679047293074788450L;
    public String favid;
    public String favword;

    public FavHotWord() {
    }

    public FavHotWord(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public FavHotWord(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public FavHotWord initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public FavHotWord initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected FavHotWord parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("favid")) {
                            this.favid = parseText(parser);
                        } else if (parser.getName().equals("favword")) {
                            this.favword = parseText(parser);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("favhotword")) {
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
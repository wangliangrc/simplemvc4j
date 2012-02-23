package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 热门话题
 * 
 * @author duncan
 * 
 */
public class HotWord extends DataObject implements Serializable {

    private static final long serialVersionUID = -1355827404973967112L;
    public String word;
    public String hot;
    public int num;

    public HotWord() {
    }

    public HotWord(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public HotWord(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public HotWord initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public HotWord initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected HotWord parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG: {
                        if (parser.getName().equals("word")) {
                            this.word = parseText(parser);
                        } else if (parser.getName().equals("hot")) {
                            this.hot = parseText(parser);
                        } else if (parser.getName().equals("num")) {
                            // this.num = Integer.parseInt(parseText(parser));
                            String c = parseText(parser);
                            if (c == null || c.equals("")) {
                                this.num = 0;
                            } else {
                                this.num = Integer.parseInt(c);
                            }
                        }
                        break;
                    }
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
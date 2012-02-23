package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 微博广场上的item
 * 
 * @author duncan
 * 
 */
public class SquareItem extends DataObject implements Serializable,
        IPlatformParam {

    private static final long serialVersionUID = -4264316369685825648L;

    public String title;
    public String icon;
    public String link;
    // 记录扩展参数
    private String platformParams;

    public SquareItem() {
    }

    public SquareItem(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public SquareItem(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public SquareItem initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public SquareItem initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected SquareItem parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("title")) {
                            this.title = parseText(parser);
                        } else if (parser.getName().equals("icon")) {
                            this.icon = parseText(parser);
                        } else if (parser.getName().equals("link")) {
                            this.link = parseText(parser);
                        } else if (parser.getName().equals("platform_params")) {
                            this.platformParams = parseText(parser);
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
            throw new WeiboParseException(e);
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(e);
        } catch (IOException e) {
            throw new WeiboParseException(e);
        }
        return this;
    }

    @Override
    public boolean containsParam(String key) {
        if (TextUtils.isEmpty(platformParams)) {
            return false;
        } else {
            return platformParams.contains(key);
        }
    }
}
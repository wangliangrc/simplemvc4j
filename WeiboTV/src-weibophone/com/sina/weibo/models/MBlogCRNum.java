package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 微博评论和转发数量
 * 
 * @author duncan
 * 
 */
public class MBlogCRNum extends DataObject implements Serializable {

    private static final long serialVersionUID = 3676318834396630646L;

    /**
     * 对应微博id
     */
    public String mblogId;
    /**
     * 转发数
     */
    public int rtnum;
    /**
     * 评论数
     */
    public int commentnum;

    public MBlogCRNum() {
    }

    public MBlogCRNum(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public MBlogCRNum(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public MBlogCRNum initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public MBlogCRNum initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected MBlogCRNum parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equalsIgnoreCase("mblogid")) {
                            this.mblogId = parseText(parser);
                        } else if (parser.getName().equalsIgnoreCase("rtnum")) {
                            // this.rtnum = Integer.parseInt(parseText(parser));
                            String c = parseText(parser);
                            if (c == null || c.equals("")) {
                                rtnum = 0;
                            } else {
                                rtnum = Integer.parseInt(c);
                            }
                        } else if (parser.getName().equalsIgnoreCase(
                                "commentnum")) {
                            this.commentnum = Integer
                                    .parseInt(parseText(parser));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("crnum")) {
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
            throw new WeiboParseException(e);
        } catch (IOException e) {
            throw new WeiboParseException(e);
        }
        return this;
    }
}
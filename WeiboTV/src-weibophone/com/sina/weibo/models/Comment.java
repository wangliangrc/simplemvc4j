package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 某Mblog的评论
 * 
 * @author duncan
 * 
 */
public class Comment extends DataObject implements Serializable {

    private static final long serialVersionUID = 2817870949348565300L;

    public String uid;
    public String nick;
    public String remark;
    public String couid;
    public String conick;
    public int commentrt;
    public String content;
    public String cmtuid;
    public String cmtid;
    public Date time;

    public Comment() {
    }

    public Comment(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public Comment(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public Comment initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public Comment initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected Comment parse() throws WeiboParseException {
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
                        } else if (parser.getName().equals("couid")) {
                            this.couid = parseText(parser);
                        } else if (parser.getName().equals("conick")) {
                            this.conick = parseText(parser);
                        } else if (parser.getName().equals("commentrt")) {
                            this.commentrt = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("content")) {
                            this.content = parseText(parser);
                        } else if (parser.getName().equals("cmtid")) {
                            this.cmtid = parseText(parser);
                        } else if (parser.getName().equals("cmtuid")) {
                            this.cmtuid = parseText(parser);
                        } else if (parser.getName().equals("time")) {
                            this.time = new Date(
                                    Long.parseLong(parseText(parser)) * 1000);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("comment")) {
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
}
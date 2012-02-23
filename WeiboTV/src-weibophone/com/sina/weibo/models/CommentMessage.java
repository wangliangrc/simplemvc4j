package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class CommentMessage extends DataObject implements Serializable {
    private static final long serialVersionUID = -5855526608691258180L;
    public String mblogid;
    public String mbloguid;
    public String mblognick;
    public String mblogcontent;
    public String srcid;
    public String srcuid;
    public String srcnick;
    public String srccontent;
    public String commentid;
    public String commentuid;
    public String commentnick;
    public String remark;
    public String commentportrait;
    public int vip;
    public int vipsubtype;
    public int level;
    public String commentcontent;
    public Date commenttime;

    // public boolean newItem;

    public CommentMessage() {
    }

    public CommentMessage(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public CommentMessage(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    @Override
    public CommentMessage initFromString(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public CommentMessage initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected CommentMessage parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("mblogid")) {
                            this.mblogid = parseText(parser);
                        } else if (parser.getName().equals("mbloguid")) {
                            this.mbloguid = parseText(parser);
                        } else if (parser.getName().equals("mblognick")) {
                            this.mblognick = parseText(parser);
                        } else if (parser.getName().equals("mblogcontent")) {
                            this.mblogcontent = parseText(parser);
                        } else if (parser.getName().equals("srcid")) {
                            this.srcid = parseText(parser);
                        } else if (parser.getName().equals("srcuid")) {
                            this.srcuid = parseText(parser);
                        } else if (parser.getName().equals("srcnick")) {
                            this.srcnick = parseText(parser);
                        } else if (parser.getName().equals("srccontent")) {
                            this.srccontent = parseText(parser);
                        } else if (parser.getName().equals("commentid")) {
                            this.commentid = parseText(parser);
                        } else if (parser.getName().equals("commentuid")) {
                            this.commentuid = parseText(parser);
                        } else if (parser.getName().equals("commentnick")) {
                            this.commentnick = parseText(parser);
                        } else if (parser.getName().equals("remark")) {
                            this.remark = parseText(parser);
                        } else if (parser.getName().equals("commentportrait")) {
                            this.commentportrait = parseText(parser);
                        } else if (parser.getName().equals("vip")) {
                            this.vip = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("vipsubtype")) {
                            this.vipsubtype = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("level")) {
                            this.level = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("commentcontent")) {
                            this.commentcontent = parseText(parser);
                        } else if (parser.getName().equals("commenttime")) {
                            this.commenttime = new Date(
                                    Long.parseLong(parseText(parser)) * 1000);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("msg")) {
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
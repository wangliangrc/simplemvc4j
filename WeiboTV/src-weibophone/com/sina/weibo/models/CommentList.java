package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 某Mblog的评论列表
 * 
 * @author duncan
 * 
 */
public class CommentList extends DataObject implements Serializable {

    private static final long serialVersionUID = -2383368162481419758L;

    public List<Comment> commentList;

    public int count;

    /**
     * 此博文uid
     */
    public String srcuid;

    /**
     * 此博文id
     */
    public String srcid;

    public CommentList() {
        commentList = new ArrayList<Comment>();
    }

    public CommentList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public CommentList initFromString(String xmlStr) throws WeiboParseException {
        commentList = new ArrayList<Comment>();
        return parseFanList(xmlStr);
    }

    private CommentList parseFanList(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public CommentList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        commentList = new ArrayList<Comment>();
        parser = _parser;
        return parse();
    }

    @Override
    protected CommentList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("srcuid")) {
                            srcuid = parseText(parser);
                        } else if (parser.getName().equals("srcid")) {
                            srcid = parseText(parser);
                        } else if (parser.getName().equals("count")) {
                            String c = parseText(parser);
                            if (c == null || c.equals("")) {
                                count = 0;
                            } else {
                                count = new Integer(c);
                            }
                        } else if (parser.getName().equals("comment")) {
                            Comment cmt = new Comment(parser);
                            if (cmt != null) {
                                this.commentList.add(cmt);
                            }
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

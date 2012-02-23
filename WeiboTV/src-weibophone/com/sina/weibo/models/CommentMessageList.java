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
 * 评论箱信息
 * 
 * @author duncan
 * 
 */
public class CommentMessageList extends DataObject implements Serializable {

    private static final long serialVersionUID = -2383368162481419758L;

    public List<CommentMessage> commentMessageList;

    public int count;

    public CommentMessageList() {
        commentMessageList = new ArrayList<CommentMessage>();
    }

    public CommentMessageList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public CommentMessageList initFromString(String xmlStr)
            throws WeiboParseException {
        commentMessageList = new ArrayList<CommentMessage>();
        return parseFanList(xmlStr);
    }

    private CommentMessageList parseFanList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public CommentMessageList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected CommentMessageList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            String c = parseText(parser);
                            if (c == null || c.equals("")) {
                                count = 0;
                            } else {
                                count = new Integer(c);
                            }
                        } else if (parser.getName().equals("msg")) {
                            CommentMessage cmsg = new CommentMessage(parser);
                            if (cmsg != null) {
                                this.commentMessageList.add(cmsg);
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

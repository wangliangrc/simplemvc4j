package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class MessageList extends DataObject implements Serializable {

    private static final long serialVersionUID = 7707977960038215514L;

    public List<Message> msgList;

    public int count;// 返回的私信条数
    public String sid;// 更新的sid

    public MessageList() {
        msgList = new ArrayList<Message>();
    }

    public MessageList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public MessageList initFromString(String xmlStr) throws WeiboParseException {
        msgList = new ArrayList<Message>();
        return parseMessageList(xmlStr);
    }

    private MessageList parseMessageList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public MessageList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        return null;
    }

    @Override
    protected MessageList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("sid")) {
                            sid = parseText(parser);
                        } else if (parser.getName().equals("count")) {
                            count = new Integer(parseText(parser));
                        } else if (parser.getName().equals("msg")) {
                            Message msg = new Message(parser);
                            if (msg != null) {
                                this.msgList.add(msg);
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

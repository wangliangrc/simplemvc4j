package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class BlackList extends DataObject implements Serializable {

    private static final long serialVersionUID = -2413537590815171516L;

    public static final BlackList NULL = new BlackList();

    public String sid;

    public int count;

    public List<BlackListItem> itemList;

    public boolean isNULL() {
        return itemList == null || itemList.size() == 0;
    }

    public BlackList() {
        itemList = new ArrayList<BlackListItem>();
    }

    public BlackList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public BlackList(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public BlackList initFromString(String xmlStr) throws WeiboParseException {
        itemList = new ArrayList<BlackListItem>();
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public BlackList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        itemList = new ArrayList<BlackListItem>();
        parser = _parser;
        return parse();
    }

    @Override
    protected BlackList parse() throws WeiboParseException {
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
                        } else if (parser.getName().equals("info")) {
                            BlackListItem item = new BlackListItem(parser);
                            if (item != null) {
                                this.itemList.add(item);
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

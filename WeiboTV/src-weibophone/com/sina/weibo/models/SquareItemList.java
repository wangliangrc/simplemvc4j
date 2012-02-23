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
 * 微博广场列表
 * 
 * @author duncan
 * 
 */
public class SquareItemList extends DataObject implements Serializable {

    private static final long serialVersionUID = -366206402913147459L;

    public List<SquareItem> squareItemList;

    public int count;

    public transient boolean mIsNew;

    public String mLang;
    /**
     * 当前使用的sid
     */
    public String sid;

    public SquareItemList() {
        squareItemList = new ArrayList<SquareItem>();
    }

    public SquareItemList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
        mIsNew = true;
    }

    @Override
    public SquareItemList initFromString(String xmlStr)
            throws WeiboParseException {
        squareItemList = new ArrayList<SquareItem>();
        mIsNew = true;
        return parseFanList(xmlStr);
    }

    private SquareItemList parseFanList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        mIsNew = true;
        return parse();
    }

    @Override
    public SquareItemList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        squareItemList = new ArrayList<SquareItem>();
        parser = _parser;
        mIsNew = true;
        return parse();
    }

    @Override
    protected SquareItemList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("sid")) {
                            sid = parseText(parser);
                        } else if (parser.getName().equals("count")) {
                            String c = parseText(parser);
                            if (c == null || c.equals("")) {
                                count = 0;
                            } else {
                                count = new Integer(c);
                            }
                        } else if (parser.getName().equals("item")) {
                            SquareItem item = new SquareItem(parser);
                            if (item != null) {
                                this.squareItemList.add(item);
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
        mIsNew = true;
        return this;
    }

}

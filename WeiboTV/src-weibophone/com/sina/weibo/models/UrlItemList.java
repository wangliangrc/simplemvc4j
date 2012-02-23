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
 * 测速用Url列表
 * 
 * @author duncan
 * 
 */
public class UrlItemList extends DataObject implements Serializable {

    private static final long serialVersionUID = 5587637260980299895L;

    public List<UrlItem> urlItemList;

    public UrlItemList() {
        urlItemList = new ArrayList<UrlItem>();
    }

    public UrlItemList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public UrlItemList initFromString(String xmlStr) throws WeiboParseException {
        urlItemList = new ArrayList<UrlItem>();
        return parseFanList(xmlStr);
    }

    private UrlItemList parseFanList(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public UrlItemList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        urlItemList = new ArrayList<UrlItem>();
        parser = _parser;
        return parse();
    }

    @Override
    protected UrlItemList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            UrlItem item = new UrlItem(parser);
                            if (item != null) {
                                this.urlItemList.add(item);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(e);
        } catch (IOException e) {
            throw new WeiboParseException(e);
        }

        return this;
    }

}

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
 * 广告信息列表
 * 
 * @author duncan
 * 
 */
public class AdList extends DataObject implements Serializable {

    private static final long serialVersionUID = -8381779334448025232L;

    public List<Ad> adList;

    public AdList() {
        adList = new ArrayList<Ad>();
    }

    public AdList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public AdList initFromString(String xmlStr) throws WeiboParseException {
        adList = new ArrayList<Ad>();
        return parseFanList(xmlStr);
    }

    private AdList parseFanList(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public AdList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        adList = new ArrayList<Ad>();
        parser = _parser;
        return parse();
    }

    @Override
    protected AdList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("info")) {
                            Ad item = new Ad(parser);
                            if (item != null) {
                                this.adList.add(item);
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

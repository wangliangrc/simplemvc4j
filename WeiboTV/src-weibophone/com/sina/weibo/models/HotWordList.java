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
 * 热门话题列表
 * 
 * @author duncan
 * 
 */
public class HotWordList extends DataObject implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1463040249292240961L;

    public List<HotWord> hotWordList;

    public int count;

    public HotWordList() {
        hotWordList = new ArrayList<HotWord>();
    }

    public HotWordList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public HotWordList initFromString(String xmlStr) throws WeiboParseException {
        hotWordList = new ArrayList<HotWord>();
        return parseFanList(xmlStr);
    }

    private HotWordList parseFanList(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public HotWordList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        hotWordList = new ArrayList<HotWord>();
        parser = _parser;
        return parse();
    }

    @Override
    protected HotWordList parse() throws WeiboParseException {
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
                        } else if (parser.getName().equals("item")) {
                            HotWord hotWord = new HotWord(parser);
                            if (hotWord != null) {
                                this.hotWordList.add(hotWord);
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

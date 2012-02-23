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
 * 话题列表
 * 
 * @author duncan
 * 
 */
public class FavHotWordList extends DataObject implements Serializable {

    private static final long serialVersionUID = -2468560858696354081L;

    public List<FavHotWord> favHotWordList;

    public int count;

    public FavHotWordList() {
        favHotWordList = new ArrayList<FavHotWord>();
    }

    public FavHotWordList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public FavHotWordList initFromString(String xmlStr)
            throws WeiboParseException {
        favHotWordList = new ArrayList<FavHotWord>();
        return parseFanList(xmlStr);
    }

    private FavHotWordList parseFanList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public FavHotWordList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        favHotWordList = new ArrayList<FavHotWord>();
        parser = _parser;
        return parse();
    }

    @Override
    protected FavHotWordList parse() throws WeiboParseException {
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
                        } else if (parser.getName().equals("favhotword")) {
                            FavHotWord fhWord = new FavHotWord(parser);
                            if (fhWord != null) {
                                this.favHotWordList.add(fhWord);
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

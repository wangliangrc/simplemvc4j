package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

@SuppressWarnings("serial")
public class SkinList extends DataObject implements Serializable {

    public List<ThemeEntity> mSkinList;

    public transient boolean mIsNew;

    public String version = "";

    public SkinList() {
    }

    public SkinList(String content) {
        try {
            parseSkinList(content);
        } catch (WeiboParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SkinList initFromString(String xmlStr) throws WeiboParseException {
        mSkinList = new ArrayList<ThemeEntity>(5);
        return parseSkinList(xmlStr);
    }

    private SkinList parseSkinList(String xmlStr) throws WeiboParseException {
        mSkinList = new ArrayList<ThemeEntity>(5);
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public SkinList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        mSkinList = new ArrayList<ThemeEntity>(5);
        parser = _parser;
        return parse();
    }

    @Override
    protected SkinList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            ThemeEntity item = new ThemeEntity(parser);
                            if (item != null) {
                                this.mSkinList.add(item);
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

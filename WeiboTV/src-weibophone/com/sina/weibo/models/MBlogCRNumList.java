package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class MBlogCRNumList extends DataObject implements Serializable {

    private static final long serialVersionUID = 7323849982357711262L;

    public List<MBlogCRNum> mblogCRNumList;

    public MBlogCRNumList() {
        mblogCRNumList = new ArrayList<MBlogCRNum>();
    }

    public MBlogCRNumList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public MBlogCRNumList initFromString(String xmlStr)
            throws WeiboParseException {
        mblogCRNumList = new ArrayList<MBlogCRNum>();
        return parseUserInfoList(xmlStr);
    }

    private MBlogCRNumList parseUserInfoList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public MBlogCRNumList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        return null;
    }

    @Override
    protected MBlogCRNumList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("crnum")) {
                            MBlogCRNum crNum = new MBlogCRNum(parser);
                            if (crNum != null) {
                                this.mblogCRNumList.add(crNum);
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

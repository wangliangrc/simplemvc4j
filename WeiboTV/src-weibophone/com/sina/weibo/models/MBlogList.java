package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class MBlogList extends DataObject implements Serializable {

    private static final long serialVersionUID = 7678521592901029483L;

    public List<MBlog> mblogList;

    public int count;

    public MBlogList() {
        mblogList = new ArrayList<MBlog>();
    }

    public MBlogList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public MBlogList initFromString(String xmlStr) throws WeiboParseException {
        mblogList = new ArrayList<MBlog>();
        return parseUserInfoList(xmlStr);
    }

    private MBlogList parseUserInfoList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public MBlogList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        return null;
    }

    @Override
    protected MBlogList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            String c = parseText(parser);
                            if (c.equals("") || c == null) {
                                count = 0;
                            } else {
                                count = new Integer(c);
                            }
                        } else if (parser.getName().equals("mblog")) {
                            MBlog mblog = new MBlog(parser);
                            if (mblog != null) {
                                this.mblogList.add(mblog);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (NumberFormatException e) {
            count = 0;
            throw new WeiboParseException(e);
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(e);
        } catch (IOException e) {
            throw new WeiboParseException(e);
        }

        return this;
    }

}

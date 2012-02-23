package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class UserInfoList extends DataObject implements Serializable {

    private static final long serialVersionUID = 7678521592901029483L;

    public List<UserInfo> userInfoList;

    public int count;

    public UserInfoList() {
        userInfoList = new ArrayList<UserInfo>();
    }

    public UserInfoList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public UserInfoList initFromString(String xmlStr)
            throws WeiboParseException {
        userInfoList = new ArrayList<UserInfo>();
        return parseUserInfoList(xmlStr);
    }

    private UserInfoList parseUserInfoList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }

        return parse();
    }

    @Override
    public UserInfoList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        return null;
    }

    @Override
    protected UserInfoList parse() throws WeiboParseException {
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
                            UserInfo userInfo = new UserInfo(parser);
                            if (userInfo != null) {
                                this.userInfoList.add(userInfo);
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

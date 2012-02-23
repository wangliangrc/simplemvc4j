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
 * 分组信息列表
 * 
 * @author duncan
 * 
 */
public class GroupList extends DataObject implements Serializable {

    private static final long serialVersionUID = -1771290867483316727L;

    public List<Group> groupList;

    public int count;

    public GroupList() {
        groupList = new ArrayList<Group>();
    }

    public GroupList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public GroupList initFromString(String xmlStr) throws WeiboParseException {
        groupList = new ArrayList<Group>();
        return parseFanList(xmlStr);
    }

    private GroupList parseFanList(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public GroupList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        groupList = new ArrayList<Group>();
        parser = _parser;
        return parse();
    }

    @Override
    protected GroupList parse() throws WeiboParseException {
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
                            Group group = new Group(parser);
                            if (group != null) {
                                this.groupList.add(group);
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

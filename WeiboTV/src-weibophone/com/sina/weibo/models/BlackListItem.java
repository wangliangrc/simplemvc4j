package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class BlackListItem extends DataObject implements Serializable {

    private static final long serialVersionUID = 4275554212110038754L;

    public static final SimpleDateFormat ADDTIME_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public String portraitURL;
    public String uid;
    public String nick;
    public Date addTime;
    public boolean isBlack = true;

    public BlackListItem() {
    }

    public BlackListItem(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public BlackListItem(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    public BlackListItem initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public BlackListItem initFromString(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected BlackListItem parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG: {
                        if (parser.getName().equals("portrait")) {
                            this.portraitURL = parseText(parser);
                        } else if (parser.getName().equals("uid")) {
                            this.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            this.nick = parseText(parser);
                        } else if (parser.getName().equals("addtime")) {
                            this.addTime = ADDTIME_FORMAT
                                    .parse((parseText(parser)).trim());
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if (parser.getName().equals("info")) {
                            return this;
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (IOException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (java.text.ParseException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }
}
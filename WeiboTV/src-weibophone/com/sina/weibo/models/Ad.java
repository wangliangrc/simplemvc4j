package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 广告信息
 * 
 * @author duncan
 * 
 */
public class Ad extends DataObject implements Serializable, IPlatformParam {

    public static final String CATEGORY_INDEX = "index";
    public static final String CATEGORY_MSG = "msg";

    private static final long serialVersionUID = 2988449420966720944L;

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public String id;
    public String content;
    public String title;
    public String link;
    public String imgUrl;
    public Date startDate;
    public Date endDate;
    public int displayTime;
    public boolean userClose;
    public boolean clickClose;

    public String icon;
    public String category = CATEGORY_INDEX;
    // 记录扩展参数
    private String platformParams;
    /**
     * 1=>内部打开 0=>外部打开
     */
    public int openInApp;

    public Ad() {
    }

    public boolean isAvild() {
        Date date = new Date();
        return date.after(startDate) && date.before(endDate);
    }

    public Ad(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public Ad(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    public Ad initFromParser(XmlPullParser _parser) throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public Ad initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected Ad parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("id")) {
                            this.id = parseText(parser);
                        } else if (parser.getName().equals("content")) {
                            this.content = parseText(parser);
                        } else if (parser.getName().equals("title")) {
                            this.title = parseText(parser);
                        } else if (parser.getName().equals("link")) {
                            this.link = parseText(parser);
                        } else if (parser.getName().equals("icon")) {
                            this.icon = parseText(parser);
                        } else if (parser.getName().equals("img")) {
                            this.imgUrl = parseText(parser);
                        } else if (parser.getName().equals("startdate")) {
                            this.startDate = TIME_FORMAT
                                    .parse((parseText(parser)).trim());
                        } else if (parser.getName().equals("enddate")) {
                            this.endDate = TIME_FORMAT
                                    .parse((parseText(parser)).trim());
                        } else if (parser.getName().equals("userclose")) {
                            this.userClose = Integer
                                    .parseInt(parseText(parser)) == 1 ? true
                                    : false;
                        } else if (parser.getName().equals("clickclose")) {
                            this.clickClose = Integer
                                    .parseInt(parseText(parser)) == 1 ? true
                                    : false;
                        } else if (parser.getName().equals("openinapp")) {
                            this.openInApp = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("display_time")) {
                            this.displayTime = Integer
                                    .parseInt(parseText(parser)) * 1000;
                        } else if (parser.getName().equals("platform_params")) {
                            this.platformParams = parseText(parser);
                        } else if (parser.getName().equals("category_names")) {
                            this.category = parseText(parser);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("info")) {
                            return this;
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (NumberFormatException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (IOException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        } catch (ParseException e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        }
        return this;
    }

    @Override
    public boolean containsParam(String key) {
        if (TextUtils.isEmpty(platformParams)) {
            return false;
        } else {
            return platformParams.contains(key);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ad other = (Ad) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
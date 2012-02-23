package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;

import com.sina.weibo.exception.WeiboParseException;

public class VersionInfo extends DataObject implements Serializable {
    private static final long serialVersionUID = -4328416593660466982L;
    public static final VersionInfo NULL = new VersionInfo();
    private static final SimpleDateFormat CHANGE_DATE_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd");
    public String version;
    public String downloadURL;
    public String wapURL;
    public String md5;
    public String description;
    public Date changeDate;
    public String prompt;
    private boolean isNull = true;

    public boolean isNull() {
        return isNull;
    }

    public VersionInfo() {
    }

    public VersionInfo(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public VersionInfo(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public VersionInfo initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public VersionInfo initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected VersionInfo parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG: {
                        if (parser.getName().equals("version")) {
                            this.version = parseText(parser);
                        } else if (parser.getName().equals("download")) {
                            this.downloadURL = parseText(parser);
                        } else if (parser.getName().equals("wapurl")) {
                            this.wapURL = parseText(parser);
                        } else if (parser.getName().equals("md5")) {
                            this.md5 = parseText(parser);
                        } else if (parser.getName().equals("desc")) {
                            this.description = parseText(parser);
                        } else if (parser.getName().equals("changedate")) {
                            String dateStr = parseText(parser);
                            if (android.text.TextUtils.isEmpty(dateStr)) {
                                this.changeDate = new Date(0);
                            } else {
                                this.changeDate = CHANGE_DATE_FORMATTER
                                        .parse(dateStr);
                            }

                        } else if (parser.getName().equals("prompt")) {
                            this.prompt = parseMultiLineText(parser, "prompt");
                        }
                        break;
                    }
                }
            }
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(e);
        } catch (IOException e) {
            throw new WeiboParseException(e);
        } catch (WeiboParseException e) {
            throw new WeiboParseException(e);
        } catch (ParseException e) {
            throw new WeiboParseException(e);
        }
        return this;
    }

    public VersionInfo(HashMap<String, String> itemData) {
        if (DataStructureUtil.isEmptyOrNull(itemData)) {
            isNull = true;
        } else {
            version = TextUtils.trimIgnoreNull(itemData.get("version"));
            downloadURL = TextUtils.trimIgnoreNull(itemData.get("download"));
            wapURL = TextUtils.trimIgnoreNull(itemData.get("wapurl"));
            md5 = TextUtils.trimIgnoreNull(itemData.get("md5"));
            description = TextUtils.trimIgnoreNull(itemData.get("desc"));
            try {
                changeDate = CHANGE_DATE_FORMATTER.parse(TextUtils
                        .trimIgnoreNull(itemData.get("changedate")));
            } catch (Exception e) {
                // Utils.loge(e);
                // 默认1970-1-1
                changeDate = new Date(0);
            }
        }
    }
}

package com.sina.weibo.models;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.sina.weibo.exception.WeiboParseException;

public abstract class DataObject {

    private static final Pattern entryPattern = Pattern.compile("&\\w+;");

    private static final HashMap<String, String> ENTRY_MAP = new HashMap<String, String>();

    protected static String PARSE_ERROR = "Problem parsing API response";

    protected static String UNKNOWN_ERROR = "Unknown error";

    protected XmlPullParser parser = Xml.newPullParser();

    static {
        ENTRY_MAP.put("&lt;", "<");
        ENTRY_MAP.put("&gt;", ">");
        ENTRY_MAP.put("&amp;", "&");
        ENTRY_MAP.put("&apos;", "'");
        ENTRY_MAP.put("&quot;", "\"");
    }

    public DataObject() {
    }

    public DataObject(String xmlStr) throws WeiboParseException {
        initFromString(xmlStr);
    }

    public DataObject(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    public abstract DataObject initFromString(String xmlStr)
            throws WeiboParseException;

    public abstract DataObject initFromParser(XmlPullParser _parser)
            throws WeiboParseException;

    protected abstract DataObject parse() throws WeiboParseException;

    protected String parseText(XmlPullParser parser) throws WeiboParseException {
        try {
            int type = parser.next();
            if (type == XmlPullParser.TEXT) {
                return replaceEntityRef(parser.getText().trim());
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        }
    }

    /**
     * 标签的文本中含有&lt;br /&gt;时，在解析字符串时替换为\n
     */
    protected String parseMultiLineText(XmlPullParser parser, String tag)
            throws WeiboParseException {
        StringBuilder builder = new StringBuilder();
        int type;

        try {
            while ((type = parser.next()) != XmlPullParser.END_TAG
                    || !parser.getName().equalsIgnoreCase(tag)) {
                if (type == XmlPullParser.TEXT) {
                    builder.append(replaceEntityRef(parser.getText().trim()));
                } else if (type == XmlPullParser.START_TAG
                        && parser.getName().equals("br")) {
                    builder.append("\n");
                }
            }
        } catch (Exception e) {
            throw new WeiboParseException(PARSE_ERROR, e);
        }

        return builder.toString();
    }

    private String replaceEntityRef(String src) {
        Matcher m = entryPattern.matcher(src);
        StringBuilder buffer = new StringBuilder();
        int start = -1, end = -1, lastEnd = -1;
        String val = null;
        while (m.find()) {
            start = m.start();
            end = m.end();
            val = ENTRY_MAP.get(m.group());
            if (val != null && val.length() > 0) {
                if (lastEnd != -1) {
                    buffer.append(src.substring(lastEnd, start));
                    buffer.append(val);
                    lastEnd = end;
                    start = -1;
                    end = -1;
                }
            }
        }
        if (lastEnd == -1) {
            return src;
        } else if (lastEnd != src.length()) {
            buffer.append(src.substring(lastEnd));
        }

        return buffer.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DataObject [parser=").append(parser).append("]");
        return builder.toString();
    }
}

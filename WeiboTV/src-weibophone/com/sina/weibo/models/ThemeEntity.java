package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 主题信息
 * 
 * @author nieyu
 * 
 */
public class ThemeEntity extends DataObject implements Serializable {

    public static final int STATE_NEW = 0;
    public static final int STATE_DOWNLOAD = 1;
    public static final int STATE_DOWNLOADING = 6;
    public static final int STATE_DOWNLOAD_CANCEL = 7;
    public static final int STATE_DOWNLOAD_FIALED = 5;
    public static final int STATE_INSTALLED = 2;
    public static final int STATE_USING = 3;
    public static final int STATE_UPDATE = 4;

    public String description;
    public String fileName;
    public String fileUrl;
    public String packageName = "";
    public String scanImage;
    public String themeName;
    public String version;
    public String iconUrl;
    public String fileSize;
    public String md5;
    public int state;
    public int step;

    public ThemeEntity(XmlPullParser parser) {
        try {
            initFromParser(parser);
        } catch (WeiboParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof ThemeEntity)) {
            return false;
        } else {
            return ((ThemeEntity) o).packageName.equals(packageName);
        }
    }

    public ThemeEntity(String xmlStr) {
        try {
            initFromString(xmlStr);
        } catch (WeiboParseException e) {
            e.printStackTrace();
        }
    }

    public ThemeEntity() {
    }

    @Override
    public ThemeEntity initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public ThemeEntity initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected ThemeEntity parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("skinname")) {
                            this.themeName = parseText(parser);
                        } else if (parser.getName().equals("packagename")) {
                            this.packageName = parseText(parser);
                        } else if (parser.getName().equals("downloadlink")) {
                            this.fileUrl = parseText(parser);
                        } else if (parser.getName().equals("iconurl")) {
                            this.iconUrl = parseText(parser);
                        } else if (parser.getName().equals("previewimgurl")) {
                            this.scanImage = parseText(parser);
                        } else if (parser.getName().equals("version")) {
                            this.version = parseText(parser);
                        } else if (parser.getName().equals("filesize")) {
                            this.fileSize = parseText(parser);
                        } else if (parser.getName().equals("md5")) {
                            this.md5 = parseText(parser);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            fileName = SkinListActivity.getFileName(this);
                            return this;
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

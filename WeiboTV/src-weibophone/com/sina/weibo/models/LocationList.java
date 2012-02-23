package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class LocationList extends DataObject implements Serializable {

    private static final long serialVersionUID = 7707977960038215514L;

    public List<Location> locations;

    public int total;
    public double lat;
    public double lon;
    public String location;
    public transient boolean isNewLocation = false;

    public boolean havenextpage;

    public LocationList() {
        locations = new ArrayList<Location>();
    }

    public LocationList(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    @Override
    public LocationList initFromString(String xmlStr)
            throws WeiboParseException {
        locations = new ArrayList<Location>();
        return parseMessageList(xmlStr);
    }

    private LocationList parseMessageList(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public LocationList initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        return null;
    }

    @Override
    protected LocationList parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("total")) {
                            total = new Integer(parseText(parser));
                        } else if (parser.getName().equals("location")) {
                            location = parseText(parser);
                        } else if (parser.getName().equals("havenextpage")) {
                            havenextpage = Boolean
                                    .parseBoolean(parseText(parser));
                        } else if (parser.getName().equals("info")) {
                            Location msg = new Location(parser);
                            if (msg != null) {
                                this.locations.add(msg);
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

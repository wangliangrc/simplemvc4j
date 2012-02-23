package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

public class User extends DataObject implements Serializable {
    private static final long serialVersionUID = -4108673652430439788L;
    public String name;
    public String pass;
    public String gsid;
    // public String sid;
    public String uid;
    public int status;
    public String nick;
    public String url;
    public String msgurl;
    private String oauth_token;
    private String oauth_token_secret;

    public User() {
    }

    public User(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public User(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public User initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    public User initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected User parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("gsid")) {
                            this.gsid = parseText(parser);
                        } else if (parser.getName().equals("status")) {
                            this.status = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("uid")) {
                            this.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            this.nick = parseText(parser);
                        } else if (parser.getName().equals("url")) {
                            this.url = parseText(parser);
                        } else if (parser.getName().equals("msgurl")) {
                            this.msgurl = parseText(parser);
                        } else if (parser.getName().equals("oauth_token")) {
                            this.setOauth_token(parseText(parser));
                        } else if (parser.getName()
                                .equals("oauth_token_secret")) {
                            this.setOauth_token_secret(parseText(parser));
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
        }
        return this;
    }

    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public void setOauth_token_secret(String oauth_token_secret) {
        this.oauth_token_secret = oauth_token_secret;
    }

    public String getOauth_token_secret() {
        return oauth_token_secret;
    }
}

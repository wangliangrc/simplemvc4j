package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 用户信息、猜你喜欢用户信息、名人榜用户信息、人气草根榜用户信息
 * 
 * @author duncan
 * 
 */
public class UserInfo extends DataObject implements Serializable {
    private static final long serialVersionUID = -817536279590147369L;

    public static final int RELATION_NONE = 0;
    public static final int RELATION_MY_FANS = 2;
    public static final int RELATION_I_FOLLOW = 1;
    public static final int RELATION_FRIENDS = 3;
    public static final int RELATION_IN_MY_BACKLIST = 4;

    public String sid;
    public String uid;
    public String nick;
    public String remark;
    public String portrait;
    public int gender;
    public boolean vip;
    public boolean vipsubtype;
    public boolean level;
    public String intro;
    public String domain;
    public String province;
    public String city;
    public int relation;
    public int mblognum;
    public int meattnum;
    public int attmenum;// 粉丝数量
    public String viptitle;
    public int favBlogNum;
    public int favHotwordNum;
    public int allowmsg;
    public int blackusernum;// 黑名单数目

    // 昵称的拼音
    private String nickPinyin;

    // only for GuessUser
    public String content;
    public Date time;
    public String type;

    // only for UserRankItem
    public int newaddnum;

    // 和当前位置间的距离
    public String distance;

    public String getNickPinyin() {
        if (nickPinyin == null || nickPinyin.length() == 0) {
            nickPinyin = PinyinUtils.getPinyin(nick);
        }
        return nickPinyin;
    }

    public UserInfo() {
    }

    public UserInfo(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public UserInfo(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    @Override
    public UserInfo initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    public UserInfo initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected UserInfo parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("sid")) {
                            this.sid = parseText(parser);
                        } else if (parser.getName().equals("uid")) {
                            this.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            this.nick = parseText(parser);
                        } else if (parser.getName().equals("portrait")) {
                            this.portrait = parseText(parser);
                        } else if (parser.getName().equals("gender")) {
                            this.gender = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("vip")) {
                            if ("1".equals(parseText(parser)))
                                this.vip = true;
                            else
                                this.vip = false;
                        } else if (parser.getName().equals("vipsubtype")) {
                            if (Integer.parseInt(parseText(parser)) > 0)
                                this.vipsubtype = true;
                            else
                                this.vipsubtype = false;
                        } else if (parser.getName().equals("level")) {
                            if ("7".equals(parseText(parser)))
                                this.level = true;
                            else
                                this.level = false;
                        } else if (parser.getName().equals("intro")) {
                            this.intro = parseText(parser);
                        } else if (parser.getName().equals("domain")) {
                            this.domain = parseText(parser);
                        } else if (parser.getName().equals("province")) {
                            this.province = parseText(parser);
                        } else if (parser.getName().equals("city")) {
                            this.city = parseText(parser);
                        } else if (parser.getName().equals("relation")) {
                            this.relation = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("mblognum")) {
                            this.mblognum = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("meattnum")) {
                            this.meattnum = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("attmenum")) {
                            this.attmenum = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("num")) {
                            // only for searched users or top users or GuessUser
                            this.attmenum = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("remark")) {
                            this.remark = parseText(parser);
                        } else if (parser.getName().equals("viptitle")) {
                            this.viptitle = parseText(parser);
                        } else if (parser.getName().equals("favblognum")) {
                            this.favBlogNum = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("favhotwordnum")) {
                            this.favHotwordNum = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("allow_msg")) {
                            this.allowmsg = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("content")) {
                            // only for GuessUser
                            this.content = parseText(parser);
                        } else if (parser.getName().equals("time")) {
                            // only for GuessUser
                            this.time = new Date(
                                    Long.parseLong(parseText(parser)) * 1000);
                        } else if (parser.getName().equals("type")) {
                            // only for GuessUser
                            this.type = parseText(parser);
                        } else if (parser.getName().equals("newaddnum")) {
                            this.newaddnum = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("distance")) {
                            this.distance = parseText(parser);
                        } else if (parser.getName().equals("blackusernum")) {
                            this.blackusernum = Integer
                                    .parseInt(parseText(parser));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("info")) {
                            // only for searched users or top users
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

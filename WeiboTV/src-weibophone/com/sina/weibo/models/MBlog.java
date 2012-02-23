package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.WeiboApplication;
import com.sina.weibo.exception.WeiboParseException;

public class MBlog extends DataObject implements Serializable {

    private static final long serialVersionUID = -2896764582108375087L;
    public String uid;
    public String favid;
    public String mblogid;
    public String nick;
    public String remark;
    /** 备注信息 **/
    public String portrait;
    public boolean vip;
    public boolean vipsubtype;
    public boolean level;
    public String content;
    public String rtrootuid;
    public String rtrootid;
    public String rtrootnick;
    public boolean rtrootvip;
    public String rtreason;
    public int rtnum;
    public int commentnum;
    public Date time;
    public String pic;
    public String src;
    public String longitude;// 经度
    public String latitude;// 纬度

    // 和当前位置间的距离
    public String distance;

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        Class<?> cla = o.getClass();
        if (cla == getClass()) {
            MBlog other = (MBlog) o;
            if (other.mblogid.equals(mblogid))
                return true;
        }
        return false;
    }

    public int hashCode() {
        return mblogid.hashCode() * 101 >> 12;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MBlog [uid=").append(uid).append(", favid=")
                .append(favid).append(", mblogid=").append(mblogid)
                .append(", nick=").append(nick).append(", remark=")
                .append(remark).append(", portrait=").append(portrait)
                .append(", vip=").append(vip).append(", vipsubtype=")
                .append(vipsubtype).append(", level=").append(level)
                .append(", content=").append(content).append(", rtrootuid=")
                .append(rtrootuid).append(", rtrootid=").append(rtrootid)
                .append(", rtrootnick=").append(rtrootnick)
                .append(", rtrootvip=").append(rtrootvip).append(", rtreason=")
                .append(rtreason).append(", rtnum=").append(rtnum)
                .append(", commentnum=").append(commentnum).append(", time=")
                .append(time).append(", pic=").append(pic).append(", src=")
                .append(src).append(", longitude=").append(longitude)
                .append(", latitude=").append(latitude).append(", distance=")
                .append(distance).append("]");
        return builder.toString();
    }

    public MBlog() {
    }

    public MBlog(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public MBlog(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    public MBlog initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public MBlog initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected MBlog parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("uid")) {
                            this.uid = parseText(parser);
                            if (this.uid.equals(""))
                                return null;
                        } else if (parser.getName().equals("favid")) {
                            this.favid = parseText(parser);
                        } else if (parser.getName().equals("mblogid")) {
                            this.mblogid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            String s = parseText(parser);
                            if (WeiboApplication.ME.equals(s)) {
                                this.nick = StaticInfo.mUser.nick == null ? WeiboApplication.ME
                                        : StaticInfo.mUser.nick;
                            } else {
                                this.nick = s;
                            }
                        } else if (parser.getName().equals("remark")) {
                            this.remark = parseText(parser);
                        } else if (parser.getName().equals("portrait")) {
                            this.portrait = parseText(parser);
                        } else if (parser.getName().equals("vip")) {
                            this.vip = (parseText(parser).equals("1")) ? true
                                    : false;
                        } else if (parser.getName().equals("vipsubtype")) {
                            this.vipsubtype = (Integer
                                    .parseInt(parseText(parser))) > 0 ? true
                                    : false;
                        } else if (parser.getName().equals("level")) {
                            this.level = (parseText(parser).equals("7")) ? true
                                    : false;
                        } else if (parser.getName().equals("content")) {
                            this.content = parseText(parser);
                        } else if (parser.getName().equals("rtrootuid")) {
                            this.rtrootuid = parseText(parser);
                        } else if (parser.getName().equals("rtrootid")) {
                            this.rtrootid = parseText(parser);
                        } else if (parser.getName().equals("rtrootnick")) {

                            String s = parseText(parser);
                            if (WeiboApplication.ME.equals(s)) {
                                this.rtrootnick = StaticInfo.mUser.nick == null ? WeiboApplication.ME
                                        : StaticInfo.mUser.nick;
                            } else {
                                this.rtrootnick = s;
                            }
                        } else if (parser.getName().equals("rtrootvip")) {
                            this.rtrootvip = (parseText(parser).equals("1")) ? true
                                    : false;
                        } else if (parser.getName().equals("rtreason")) {
                            this.rtreason = parseText(parser);
                        } else if (parser.getName().equals("rtnum")) {
                            this.rtnum = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("commentnum")) {
                            this.commentnum = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("time")) {
                            this.time = new Date(
                                    Long.parseLong(parseText(parser)) * 1000);
                        } else if (parser.getName().equals("pic")) {
                            this.pic = parseText(parser);
                        } else if (parser.getName().equals("source")) {
                            this.src = parseText(parser);
                        } else if (parser.getName().equals("longitude")) {
                            this.longitude = parseText(parser);
                        } else if (parser.getName().equals("latitude")) {
                            this.latitude = parseText(parser);
                        } else if (parser.getName().equals("distance")) {
                            this.distance = parseText(parser);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("mblog")) {
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
        }
        return this;
    }

}

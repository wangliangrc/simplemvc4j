package com.sina.weibo.net;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Xml;

import com.sina.weibo.WeiboApplication;
import com.sina.weibo.models.BlackList;
import com.sina.weibo.models.Comment;
import com.sina.weibo.models.CommentMessage;
import com.sina.weibo.models.Fan;
import com.sina.weibo.models.FavHotWord;
import com.sina.weibo.models.HotWord;
import com.sina.weibo.models.MBlog;
import com.sina.weibo.models.Message;
import com.sina.weibo.models.UnreadNum;
import com.sina.weibo.models.User;
import com.sina.weibo.models.UserInfo;
import com.sina.weibo.models.UserStatus;
import com.sina.weibo.models.VersionInfo;

@SuppressWarnings({ "rawtypes" })
public final class RPCHelper {
    public static final String ERROR_PASSWORD_WRONG = "-100";

    public static class ApiException extends Exception {

        private static final long serialVersionUID = 4851415466864471511L;

        public ApiException() {
            super();
        }

        public ApiException(String detailMessage) {
            super(detailMessage);
        }

        public ApiException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ApiException(Throwable throwable) {
            super(throwable);
        }

    }

    /**
     * Thrown when there were problems contacting the remote API server, either
     * because of a network error, or the server returned a bad status code.
     */
    public static class HttpException extends Exception {
        private static final long serialVersionUID = 5672654600892372211L;

        private int statusCode = -1;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public HttpException() {
            super();
        }

        public HttpException(int statusCode) {
            super();
            this.statusCode = statusCode;
        }

        public HttpException(String detailMessage) {
            super(detailMessage);
        }

        public HttpException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public HttpException(Throwable throwable) {
            super(throwable);
        }

    }

    public static class NoSignalException extends Exception {

        private static final long serialVersionUID = 4854482225687754615L;

        public NoSignalException() {
            super();
        }

        public NoSignalException(String detailMessage) {
            super(detailMessage);
        }

        public NoSignalException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public NoSignalException(Throwable throwable) {
            super(throwable);
        }

    }

    /**
     * Thrown when there were problems parsing the response to an API call,
     * either because the response was empty, or it was malformed.
     */
    public static class ParseException extends Exception {

        private static final long serialVersionUID = 3132128578218204998L;

        public ParseException() {
            super();
        }

        public ParseException(String detailMessage) {
            super(detailMessage);
        }

        public ParseException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ParseException(Throwable throwable) {
            super(throwable);
        }

    }

    private static byte[] sBuffer = new byte[1024];
    private static String PARSE_ERROR = "Problem parsing API response";

    private static String UNKNOWN_ERROR = "Unknown error";

    private static int BUFFER_SIZE = 4096;

    private static RPCHelper itself;

    private static String BOUNDARY = "---------7d4a6d158c9";

    private static String MULTIPART_FORM_DATA = "multipart/form-data";

    private static final Pattern entryPattern = Pattern.compile("&\\w+;");

    private static final HashMap<String, String> ENTRY_MAP = new HashMap<String, String>();

    static {
        ENTRY_MAP.put("&lt;", "<");
        ENTRY_MAP.put("&gt;", ">");
        ENTRY_MAP.put("&amp;", "&");
        ENTRY_MAP.put("&apos;", "'");
        ENTRY_MAP.put("&quot;", "\"");
    }

    public static synchronized RPCHelper getInstance(Context ctx) {
        if (itself == null) {
            itself = new RPCHelper(ctx.getApplicationContext());
        }
        return itself;
    }

    public static Comment parseComment(XmlPullParser parser)
            throws ParseException {
        Comment c = new Comment();
        try {
            int type;
            LOOP: {
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("uid")) {
                                c.uid = parseText(parser);
                            } else if (parser.getName().equals("nick")) {
                                c.nick = parseText(parser);
                            } else if (parser.getName().equals("remark")) {
                                c.remark = parseText(parser);
                            } else if (parser.getName().equals("couid")) {
                                c.couid = parseText(parser);
                            } else if (parser.getName().equals("conick")) {
                                c.conick = parseText(parser);
                            } else if (parser.getName().equals("commentrt")) {
                                c.commentrt = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("content")) {
                                c.content = parseText(parser);
                            } else if (parser.getName().equals("cmtid")) {
                                c.cmtid = parseText(parser);
                            } else if (parser.getName().equals("cmtuid")) {
                                c.cmtuid = parseText(parser);
                            } else if (parser.getName().equals("time")) {
                                c.time = new Date(
                                        Long.parseLong(parseText(parser)) * 1000);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("comment")) {
                                break LOOP;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return c;
        } catch (Exception e) {
            throw new ParseException(PARSE_ERROR, e);
        }
    }

    public static CommentMessage parseCommentMessage(XmlPullParser parser)
            throws ParseException {
        CommentMessage cm = new CommentMessage();
        try {
            int type;
            LOOP: {
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("mblogid")) {
                                cm.mblogid = parseText(parser);
                            } else if (parser.getName().equals("mbloguid")) {
                                cm.mbloguid = parseText(parser);
                            } else if (parser.getName().equals("mblognick")) {
                                cm.mblognick = parseText(parser);
                            } else if (parser.getName().equals("mblogcontent")) {
                                cm.mblogcontent = parseText(parser);
                            } else if (parser.getName().equals("srcid")) {
                                cm.srcid = parseText(parser);
                            } else if (parser.getName().equals("srcuid")) {
                                cm.srcuid = parseText(parser);
                            } else if (parser.getName().equals("srcnick")) {
                                cm.srcnick = parseText(parser);
                            } else if (parser.getName().equals("srccontent")) {
                                cm.srccontent = parseText(parser);
                            } else if (parser.getName().equals("commentid")) {
                                cm.commentid = parseText(parser);
                            } else if (parser.getName().equals("commentuid")) {
                                cm.commentuid = parseText(parser);
                            } else if (parser.getName().equals("commentnick")) {
                                cm.commentnick = parseText(parser);
                            } else if (parser.getName().equals("remark")) {
                                cm.remark = parseText(parser);
                            } else if (parser.getName().equals(
                                    "commentportrait")) {
                                cm.commentportrait = parseText(parser);
                            } else if (parser.getName().equals("vip")) {
                                cm.vip = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("vipsubtype")) {
                                cm.vipsubtype = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("level")) {
                                cm.level = Integer.parseInt(parseText(parser));
                            } else if (parser.getName()
                                    .equals("commentcontent")) {
                                cm.commentcontent = parseText(parser);
                            } else if (parser.getName().equals("commenttime")) {
                                cm.commenttime = new Date(
                                        Long.parseLong(parseText(parser)) * 1000);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("msg")) {
                                break LOOP;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return cm;
        } catch (Exception e) {
            throw new ParseException(PARSE_ERROR, e);
        }
    }

    public static Fan parseFan(XmlPullParser parser) throws ParseException {
        Fan f = new Fan();
        try {
            int type;
            LOOP: {
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("uid")) {
                                f.uid = parseText(parser);
                            } else if (parser.getName().equals("nick")) {
                                f.nick = parseText(parser);
                            } else if (parser.getName().equals("remark")) {
                                f.remark = parseText(parser);
                            } else if (parser.getName().equals("gender")) {
                                f.gender = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("portrait")) {
                                f.portrait = parseText(parser);
                            } else if (parser.getName().equals("num")) {
                                f.num = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("relation")) {
                                f.relation = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("content")) {
                                f.mblogcontent = parseText(parser);
                            } else if (parser.getName().equals("time")) {
                                f.mblogtime = new Date(
                                        Long.parseLong(parseText(parser)) * 1000);
                            } else if (parser.getName().equals("vip")) {
                                f.isVip = (parseText(parser).equals("1")) ? true
                                        : false;
                            } else if (parser.getName().equals("vipsubtype")) {
                                f.isVipsubtype = (Integer
                                        .parseInt(parseText(parser))) > 0 ? true
                                        : false;
                            } else if (parser.getName().equals("level")) {
                                f.isLevel = (parseText(parser).equals("7")) ? true
                                        : false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("info")) {
                                break LOOP;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return f;
        } catch (Exception e) {
            throw new ParseException(PARSE_ERROR, e);
        }
    }

    public static FavHotWord parseFavHotWord(XmlPullParser parser)
            throws ParseException {
        FavHotWord f = new FavHotWord();
        try {
            int type;
            LOOP: {
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("favid")) {
                                f.favid = parseText(parser);
                            } else if (parser.getName().equals("favword")) {
                                f.favword = parseText(parser);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("favhotword")) {
                                break LOOP;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return f;
        } catch (Exception e) {
            throw new ParseException(PARSE_ERROR, e);
        }
    }

    // public static GuessUser parseGuessUser(XmlPullParser parser) throws
    // ParseException {
    // GuessUser gu = new GuessUser();
    // LOOP: {
    // int type;
    // try {
    // while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
    // switch (type) {
    // case XmlPullParser.START_TAG: {
    // String s = parser.getName();
    // if (parser.getName().equals("uid")) {
    // gu.uid = parseText(parser);
    // } else if (parser.getName().equals("nick")) {
    // gu.nick = parseText(parser);
    // } else if (parser.getName().equals("gender")) {
    // gu.gender = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("portrait")) {
    // gu.portrait = parseText(parser);
    // } else if (parser.getName().equals("num")) {
    // gu.num = Float.parseFloat(parseText(parser));
    // } else if (parser.getName().equals("vip")) {
    // gu.vip = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("vipsubtype")) {
    // gu.vipsubtype = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("level")) {
    // gu.level = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("relation")) {
    // gu.relation = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("content")) {
    // gu.content = parseText(parser);
    // } else if (parser.getName().equals("time")) {
    // gu.time = new Date(Long.parseLong(parseText(parser)) * 1000);
    // ;
    // }
    // break;
    // }
    // case XmlPullParser.END_TAG:
    // if (parser.getName().equals("info")) {
    // break LOOP;
    // }
    // break;
    // default:
    // break;
    // }
    // }
    // } catch (Exception e) {
    // throw new ParseException(PARSE_ERROR, e);
    // }
    // }
    // return gu;
    // }

    public static HotWord parseHotWord(XmlPullParser parser)
            throws ParseException {
        HotWord hw = new HotWord();
        LOOP: {
            int type;
            try {
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG: {
                            String s = parser.getName();
                            if (parser.getName().equals("word")) {
                                hw.word = parseText(parser);
                            } else if (parser.getName().equals("hot")) {
                                hw.hot = parseText(parser);
                            } else if (parser.getName().equals("num")) {
                                hw.num = Integer.parseInt(parseText(parser));
                            }
                            break;
                        }
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {
                                break LOOP;
                            }
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                throw new ParseException(PARSE_ERROR, e);
            }
        }
        return hw;
    }

    public static MBlog parseMBlog(XmlPullParser parser) throws ParseException {
        MBlog b = new MBlog();
        try {
            int type;
            LOOP: {
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("uid")) {
                                b.uid = parseText(parser);
                                if (b.uid.equals(""))
                                    return null;
                            } else if (parser.getName().equals("favid")) {
                                b.favid = parseText(parser);
                            } else if (parser.getName().equals("mblogid")) {
                                b.mblogid = parseText(parser);
                            } else if (parser.getName().equals("nick")) {
                                String s = parseText(parser);
                                if (WeiboApplication.ME.equals(s)) {
                                    b.nick = StaticInfo.mUser.nick == null ? WeiboApplication.ME
                                            : StaticInfo.mUser.nick;
                                } else {
                                    b.nick = s;
                                }
                            } else if (parser.getName().equals("remark")) {
                                b.remark = parseText(parser);
                            } else if (parser.getName().equals("portrait")) {
                                b.portrait = parseText(parser);
                            } else if (parser.getName().equals("vip")) {
                                b.vip = (parseText(parser).equals("1")) ? true
                                        : false;
                            } else if (parser.getName().equals("vipsubtype")) {
                                b.vipsubtype = (Integer
                                        .parseInt(parseText(parser))) > 0 ? true
                                        : false;
                            } else if (parser.getName().equals("level")) {
                                b.level = (parseText(parser).equals("7")) ? true
                                        : false;
                            } else if (parser.getName().equals("content")) {
                                b.content = parseText(parser);
                            } else if (parser.getName().equals("rtrootuid")) {
                                b.rtrootuid = parseText(parser);
                            } else if (parser.getName().equals("rtrootid")) {
                                b.rtrootid = parseText(parser);
                            } else if (parser.getName().equals("rtrootnick")) {

                                String s = parseText(parser);
                                if (WeiboApplication.ME.equals(s)) {
                                    b.rtrootnick = StaticInfo.mUser.nick == null ? WeiboApplication.ME
                                            : StaticInfo.mUser.nick;
                                } else {
                                    b.rtrootnick = s;
                                }
                            } else if (parser.getName().equals("rtrootvip")) {
                                b.rtrootvip = (parseText(parser).equals("1")) ? true
                                        : false;
                            } else if (parser.getName().equals("rtreason")) {
                                b.rtreason = parseText(parser);
                            } else if (parser.getName().equals("rtnum")) {
                                b.rtnum = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("commentnum")) {
                                b.commentnum = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("time")) {
                                b.time = new Date(
                                        Long.parseLong(parseText(parser)) * 1000);
                            } else if (parser.getName().equals("pic")) {
                                b.pic = parseText(parser);
                            } else if (parser.getName().equals("source")) {
                                b.src = parseText(parser);
                            } else if (parser.getName().equals("longitude")) {
                                b.longitude = parseText(parser);
                            } else if (parser.getName().equals("latitude")) {
                                b.latitude = parseText(parser);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("mblog")) {
                                break LOOP;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return b;
        } catch (Exception e) {
            throw new ParseException(PARSE_ERROR, e);
        }
    }

    public static Message parseMessage(XmlPullParser parser)
            throws ParseException {
        Message m = new Message();
        try {
            int type;
            LOOP: {
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("num")) {
                                m.num = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("time")) {
                                m.time = new Date(
                                        Long.parseLong(parseText(parser)) * 1000);
                            } else if (parser.getName().equals("type")) {
                                m.type = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("msgid")) {
                                m.msgid = parseText(parser);
                            } else if (parser.getName().equals("uid")) {
                                m.uid = parseText(parser);
                            } else if (parser.getName().equals("nick")) {
                                m.nick = parseText(parser);
                            } else if (parser.getName().equals("remark")) {
                                m.remark = parseText(parser);
                            } else if (parser.getName().equals("portrait")) {
                                m.portrait = parseText(parser);
                            } else if (parser.getName().equals("vip")) {
                                m.vip = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("vipsubtype")) {
                                m.vipsubtype = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("level")) {
                                m.level = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("content")) {
                                m.content = parseText(parser);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("msg")) {
                                break LOOP;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return m;
        } catch (Exception e) {
            throw new ParseException(PARSE_ERROR, e);
        }
    }

    // public static UserRankItem parseUserRank(XmlPullParser parser) throws
    // ParseException {
    // UserRankItem uri = new UserRankItem();
    // LOOP: {
    // int type;
    // try {
    // while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
    // switch (type) {
    // case XmlPullParser.START_TAG: {
    // String s = parser.getName();
    // if (parser.getName().equals("uid")) {
    // uri.uid = parseText(parser);
    // } else if (parser.getName().equals("nick")) {
    // uri.nick = parseText(parser);
    // } else if (parser.getName().equals("portrait")) {
    // uri.portrait = parseText(parser);
    // } else if (parser.getName().equals("num")) {
    // uri.num = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("vip")) {
    // uri.vip = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("vipsubtype")) {
    // uri.vipsubtype = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("level")) {
    // uri.level = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("relation")) {
    // uri.relation = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("type")) {
    // uri.type = parseText(parser);
    // } else if (parser.getName().equals("time")) {
    // uri.time = new Date(Long.parseLong(parseText(parser)) * 1000);
    // } else if (parser.getName().equals("newaddnum")) {
    // uri.newAddUser = Integer.parseInt(parseText(parser));
    // }
    // break;
    // }
    // case XmlPullParser.END_TAG:
    // if (parser.getName().equals("info")) {
    // break LOOP;
    // }
    // break;
    // default:
    // break;
    // }
    // }
    // } catch (Exception e) {
    // throw new ParseException(PARSE_ERROR, e);
    // }
    // }
    // return uri;
    // }

    private static String calculateS(String preStr) {
        return Utils.calculateS(preStr);
    }

    /**
     * 按照规则生成 UA
     * 
     * @param ctx
     * @return
     */
    private static String generateUA(Context ctx) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(Build.MANUFACTURER).append("-").append(Build.MODEL);
        buffer.append("__");
        buffer.append("weibo");
        buffer.append("__");
        try {
            String versionCode = PackageManagerUtil.getVersion(ctx);
            buffer.append(versionCode.replaceAll("\\s+", "_"));
        } catch (final Exception localE) {
            LogUtils.e(localE);
            buffer.append("unknown");
        }
        buffer.append("__").append("android").append("__android")
                .append(android.os.Build.VERSION.RELEASE);
        return buffer.toString();
    }

    private static boolean isError(String content) {
        try {
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            String errorNo = null;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("errno")) {
                            errorNo = parseText(parser);
                        }
                        break;
                }
            }
            if (!sudroid.TextUtils.isEmptyOrBlank(errorNo)) {
                if (Integer.parseInt(errorNo.trim()) < 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            Utils.loge(e);
        }
        return false;
    }

    private static Object[] parseAddfavResult(String content)
            throws HttpException, ParseException {
        Object[] result = new Object[2];

        final XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("result")) {
                            result[0] = (Boolean) ((parseText(parser)
                                    .equals("1")) ? true : false);
                        } else if (parser.getName().equals("favid")) {
                            result[1] = parseText(parser);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            throw new ParseException(PARSE_ERROR, e);
        } catch (IOException e) {
            throw new HttpException(UNKNOWN_ERROR, e);
        }

        return result;
    }

    private static String[] parseError(String content) throws ParseException {
        final String[] a = new String[2];
        try {
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("errno")) {
                            type = parser.next();
                            if (type == XmlPullParser.TEXT) {
                                a[0] = parser.getText().trim();
                            }
                        } else if (parser.getName().equals("errmsg")) {
                            type = parser.next();
                            if (type == XmlPullParser.TEXT) {
                                a[1] = parser.getText().trim();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            throw new ParseException(PARSE_ERROR, e);
        }
        return a;
    }

    private static boolean parseResult(String content) throws HttpException,
            ParseException {
        boolean ret = false;
        final XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("result")) {
                            ret = (parseText(parser).equals("1")) ? true
                                    : false;
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            throw new ParseException(PARSE_ERROR, e);
        } catch (IOException e) {
            throw new HttpException(UNKNOWN_ERROR, e);
        }

        return ret;
    }

    private static String parseText(XmlPullParser parser) throws ParseException {
        try {
            int type = parser.next();
            if (type == XmlPullParser.TEXT) {
                return replaceEntityRef(parser.getText().trim());
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new ParseException(PARSE_ERROR, e);
        }
    }

    private static UserInfo parseUserInfo(XmlPullParser parser)
            throws HttpException, ParseException {

        UserInfo ui = new UserInfo();
        try {
            int type;
            LOOP: {
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("sid")) {
                                ui.sid = parseText(parser);
                            } else if (parser.getName().equals("uid")) {
                                ui.uid = parseText(parser);
                            } else if (parser.getName().equals("nick")) {
                                ui.nick = parseText(parser);
                            } else if (parser.getName().equals("portrait")) {
                                ui.portrait = parseText(parser);
                            } else if (parser.getName().equals("gender")) {
                                ui.gender = Integer.parseInt(parseText(parser));
                            } else if (parser.getName().equals("vip")) {
                                if ("1".equals(parseText(parser)))
                                    ui.vip = true;
                                else
                                    ui.vip = false;
                            } else if (parser.getName().equals("vipsubtype")) {
                                if (Integer.parseInt(parseText(parser)) > 0)
                                    ui.vipsubtype = true;
                                else
                                    ui.vipsubtype = false;
                            } else if (parser.getName().equals("level")) {
                                if ("7".equals(parseText(parser)))
                                    ui.level = true;
                                else
                                    ui.level = false;
                            } else if (parser.getName().equals("intro")) {
                                ui.intro = parseText(parser);
                            } else if (parser.getName().equals("domain")) {
                                ui.domain = parseText(parser);
                            } else if (parser.getName().equals("province")) {
                                ui.province = parseText(parser);
                            } else if (parser.getName().equals("city")) {
                                ui.city = parseText(parser);
                            } else if (parser.getName().equals("relation")) {
                                ui.relation = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("mblognum")) {
                                ui.mblognum = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("meattnum")) {
                                ui.meattnum = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("attmenum")) {
                                ui.attmenum = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("num")) {
                                // only for searched users or top users
                                ui.attmenum = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("remark")) {
                                ui.remark = parseText(parser);
                            } else if (parser.getName().equals("viptitle")) {
                                ui.viptitle = parseText(parser);
                            } else if (parser.getName().equals("favblognum")) {
                                ui.favBlogNum = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("favhotwordnum")) {
                                ui.favHotwordNum = Integer
                                        .parseInt(parseText(parser));
                            } else if (parser.getName().equals("allow_msg")) {
                                ui.allowmsg = Integer
                                        .parseInt(parseText(parser));
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("info")) {
                                // only for searched users or top users
                                break LOOP;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return ui;
        } catch (XmlPullParserException e) {
            throw new ParseException(PARSE_ERROR, e);
        } catch (IOException e) {
            throw new HttpException(UNKNOWN_ERROR, e);
        }
    }

    private static String replaceEntityRef(String src) {
        // String out = src.replaceAll("&lt;", "<");
        // out = out.replaceAll("&gt;", ">");
        // out = out.replaceAll("&amp;", "&");
        // out = out.replaceAll("&apos;", "'");
        // out = out.replaceAll("&quot;", "\"");
        // return out;
        return replaceEntityRef2(src);
    }

    private static String replaceEntityRef2(String src) {
        Matcher m = entryPattern.matcher(src);
        StringBuilder buffer = new StringBuilder();
        int start = -1, end = -1, lastEnd = -1;
        String val = null;
        while (m.find()) {
            start = m.start();
            end = m.end();
            val = ENTRY_MAP.get(m.group());
            if (!sudroid.TextUtils.isEmptyOrBlank(val)) {
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

    private Context mContext;

    /**
     * Add an attention.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return success or not
     */
    // public boolean addAttention(User u, String uid) throws HttpException {
    // try {
    // HttpGet request = new
    // HttpGet(String.format("%sdealatt.php?gsid=%s&act=1&uid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
    // Constants.SERVER, u.gsid, uid, Constants.CID, calculateS(u.uid),
    // Constants.FROM, getWMAndLang(),
    // URLEncoder.encode(generateUA(mContext))));
    // return parseResult(execute(request));
    // } catch (ParseException e) {
    // throw new HttpException(e);
    // }
    // }

    /**
     * Add a comment for a mblog. In this case, cmtuid and cmtid in the comment
     * is useless.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param content
     *            comment
     * @return success or not
     */
    public boolean addComment(User u, String srcuid, String srcid,
            String content, boolean rt) throws HttpException {
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("c", Constants.CID));
            form.add(new BasicNameValuePair("s", calculateS(u.uid)));
            form.add(new BasicNameValuePair("act", "add"));
            form.add(new BasicNameValuePair("srcuid", srcuid));
            form.add(new BasicNameValuePair("srcid", srcid));
            form.add(new BasicNameValuePair("content", content));
            form.add(new BasicNameValuePair("from", Constants.FROM));
            form.add(new BasicNameValuePair("wm", getWM()));
            form.add(new BasicNameValuePair("ua", URLEncoder
                    .encode(generateUA(mContext))));
            if (rt) {
                form.add(new BasicNameValuePair("rt", "1"));
            }
            UrlEncodedFormEntity entity;
            entity = new UrlEncodedFormEntity(form, "UTF-8");

            HttpPost request = new HttpPost(String.format(
                    "%sdealcomment.php?gsid=%s&from=%s&wm=%s&c=%s",
                    Constants.SERVER, u.gsid, Constants.FROM, getWMAndLang(),
                    Constants.CID));

            request.setEntity(entity);
            return parseResult(execute(request));
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Add a favorite mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of the mblog
     * @return success or not
     */
    public Object[] addFavMBlog(User u, String id) throws HttpException {
        try {
            String requestString = String
                    .format("%sdealfavmblog.php?gsid=%s&act=0&mid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, id, Constants.CID,
                            calculateS(u.uid), Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext)));
            HttpGet request = new HttpGet(requestString);
            String resutlt = execute(request);
            return parseAddfavResult(resutlt);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 将 uid 表示的用户添加到黑名单中
     * 
     * @param u
     * @param uid
     * @return
     * @throws HttpException
     */
    public boolean addToBlackList(User u, String uid) {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sblackuser.php?gsid=%s&uid=%s&act=add&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, uid, Constants.CID,
                            calculateS(u.uid), Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            Utils.logd(request.getURI().toString());
            return execute(request).contains("<errno>1</errno>");
        } catch (Exception e) {
            Utils.loge(e);
        }

        return false;
    }

    /**
     * Delete a Mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of mblog.
     * @param act
     *            act=0->add act=1->remove
     * @return success or not
     */
    public boolean attendTopic(User u, int act, String fav)
            throws HttpException {
        try {
            HttpGet request = null;
            if (act == 0) {
                request = new HttpGet(
                        String.format(
                                "%sdealhotword.php?gsid=%s&act=0&value=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                                Constants.SERVER, u.gsid, fav, Constants.CID,
                                calculateS(u.uid), Constants.FROM,
                                getWMAndLang(),
                                URLEncoder.encode(generateUA(mContext))));
            } else if (act == 1) {
                request = new HttpGet(
                        String.format(
                                "%sdealhotword.php?gsid=%s&act=1&value=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                                Constants.SERVER, u.gsid, fav, Constants.CID,
                                calculateS(u.uid), Constants.FROM,
                                getWMAndLang(),
                                URLEncoder.encode(generateUA(mContext))));
            }
            return parseResult(execute(request));
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Cancel an attention.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return success or not
     */
    // public boolean cancelAttention(User u, String uid) throws HttpException {
    // try {
    // HttpGet request = new
    // HttpGet(String.format("%sdealatt.php?gsid=%s&act=2&uid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
    // Constants.SERVER, u.gsid, uid, Constants.CID, calculateS(u.uid),
    // Constants.FROM, getWMAndLang(),
    // URLEncoder.encode(generateUA(mContext))));
    //
    // return parseResult(execute(request));
    // } catch (ParseException e) {
    // throw new HttpException(e);
    // }
    // }

    /**
     * Check user's status.
     * 
     * @param u
     *            user.
     * @return an UserStatus object.
     */
    public UserStatus checkUserStatus(User u) throws HttpException {
        HttpGet request = new HttpGet(String.format(
                "%scheckuserstatus.php?gsid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                Constants.SERVER, u.gsid, Constants.CID, calculateS(u.uid),
                Constants.FROM, getWMAndLang(),
                URLEncoder.encode(generateUA(mContext))));
        String content = execute(request);

        UserStatus us = new UserStatus();

        final XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("sid")) {
                            us.sid = parseText(parser);
                        } else if (parser.getName().equals("status")) {
                            us.status = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("url")) {
                            us.url = parseText(parser);
                        } else if (parser.getName().equals("msgurl")) {
                            us.msgurl = parseText(parser);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            throw new HttpException(new ParseException());
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }

        return us;
    }

    /**
     * Deleta a @ message.
     * 
     * @param u
     *            user.
     * @param msgid
     *            \@id
     * @return success or not
     */
    public boolean deleteAtMessage(User u, String msgid) throws HttpException {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sdealmsg.php?gsid=%s&act=delatc&msgid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, msgid, Constants.CID,
                            calculateS(u.uid), Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            return parseResult(execute(request));
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Delete a comment. In this case, cmtuid and cmtid in the comment must be
     * set.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param cmtuid
     *            comment author's uid
     * @param cmtid
     *            the id of the comment
     * @return success or not
     */
    public boolean deleteComment(User u, String srcuid, String srcid,
            String cmtuid, String cmtid) throws HttpException {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sdealcomment.php?gsid=%s&act=delc&srcuid=%s&srcid=%s&cmtuid=%s&cmtid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, srcuid, srcid, cmtuid,
                            cmtid, Constants.CID, calculateS(u.uid),
                            Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            return parseResult(execute(request));
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Delete a favorite mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of the mblog
     * @return success or not
     */
    public boolean deleteFavMBlog(User u, String id, String mid)
            throws HttpException {
        try {
            String deleteRequest = String
                    .format("%sdealfavmblog.php?gsid=%s&act=1&id=%s&c=%s&s=%s&from=%s&wm=%s&mid=%s&ua=%s",
                            Constants.SERVER, u.gsid, id, Constants.CID,
                            calculateS(u.uid), Constants.FROM, getWMAndLang(),
                            mid, URLEncoder.encode(generateUA(mContext)));
            HttpGet request = new HttpGet(deleteRequest);
            String deleteResult = execute(request);
            return parseResult(deleteResult);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Delete a Mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of mblog.
     * @param act
     *            do delete
     * @return success or not
     */
    public boolean deleteMBlog(User u, String mblogid) throws HttpException {
        try {
            String uri = String
                    .format("%sdealmblog.php?gsid=%s&act=dodel&id=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, mblogid, Constants.CID,
                            calculateS(u.uid), Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext)));
            HttpGet request = new HttpGet(uri);
            return parseResult(execute(request));
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Deleta all private messages come from or sent to someone.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return success or not
     */
    public boolean deleteMessages(User u, String uid) throws HttpException {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sdealmsg.php?gsid=%s&act=delchatc&uid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, uid, Constants.CID,
                            calculateS(u.uid), Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            return parseResult(execute(request));
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 删除某条私信
     * 
     * @param u
     *            当前账户信息
     * @param uid
     *            对方的UID
     * @param msgId
     *            私信ID
     * @return 删除是否成功
     * @throws HttpException
     */
    public boolean deleteOneMessage(User u, String uid, String msgId)
            throws HttpException {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sdealmsg.php?gsid=%s&act=delmsg&uid=%s&msgid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, uid, msgId,
                            Constants.CID, calculateS(u.uid), Constants.FROM,
                            getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            return parseResult(execute(request));
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 将 uid 表示的用户从黑名单中删除
     * 
     * @param u
     * @param uid
     * @return
     * @throws HttpException
     */
    public boolean delFromBlackList(User u, String uid) {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sblackuser.php?gsid=%s&uid=%s&act=del&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, uid, Constants.CID,
                            calculateS(u.uid), Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            return execute(request).contains("<errno>1</errno>");
        } catch (Exception e) {
            Utils.loge(e);
        }

        return false;
    }

    /**
     * Edit the user information.
     * 
     * @param u
     *            : User
     * @param gender
     *            : boy or girl? @see com.sina.weibo.Constants
     * @param rename
     *            : whether user auto modify by server if duplicate
     * @param nick
     *            : user nick name
     * @param intro
     *            : a introduce for a special suer
     * @return success or not
     */
    public boolean editUserInfo(User u, int gender, String nick, String intro,
            String portraitFile, int rename) throws HttpException {
        byte buf[] = new byte[BUFFER_SIZE];
        int r = 0;
        URL url;
        try {
            url = new URL(String.format(
                    "%sedituserinfo.php?gsid=%s&wm=%s&from=%s&c=%s&s=%s&ua=%s",
                    Constants.SERVER, u.gsid, getWMAndLang(), Constants.FROM,
                    Constants.CID, calculateS(u.uid),
                    URLEncoder.encode(generateUA(mContext))));
            HttpURLConnection conn = getURLConnection(url);
            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(RPCHelper.BOUNDARY);
            sb.append("\r\n");
            if (!TextUtils.isEmpty(nick)) {
                sb.append("Content-Disposition: form-data; name=\"nick\"\r\n\r\n");
                sb.append(nick);
                sb.append("\r\n");
                sb.append("--");
                sb.append(RPCHelper.BOUNDARY);
                sb.append("\r\n");
            }
            if (intro != null) {
                sb.append("Content-Disposition: form-data; name=\"intro\"\r\n\r\n");
                sb.append(intro);
                sb.append("\r\n");
                sb.append("--");
                sb.append(RPCHelper.BOUNDARY);
                sb.append("\r\n");
            }
            sb.append("Content-Disposition: form-data; name=\"gender\"\r\n\r\n");
            sb.append(String.valueOf(gender));
            sb.append("\r\n");
            sb.append("--");
            sb.append(RPCHelper.BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"s\"\r\n\r\n");
            sb.append(calculateS(u.uid));
            sb.append("\r\n");
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"rename\"\r\n\r\n");
            sb.append(String.valueOf(rename));
            sb.append("\r\n");
            sb.append("--");
            sb.append(RPCHelper.BOUNDARY);
            sb.append("\r\n");
            // 添加UA
            sb.append("Content-Disposition: form-data; name=\"ua\"\r\n\r\n");
            sb.append(DeviceUtil.getUserAgent());
            sb.append("\r\n");
            sb.append("--");
            sb.append(RPCHelper.BOUNDARY);
            sb.append("\r\n");
            BufferedOutputStream bos = new BufferedOutputStream(
                    conn.getOutputStream());
            bos.write(sb.toString().getBytes());
            if (!TextUtils.isEmpty(portraitFile)) {
                StringBuilder fsb = new StringBuilder();
                File f = new File(portraitFile);
                FileInputStream fis = new FileInputStream(f);
                fsb.append("--");
                fsb.append(RPCHelper.BOUNDARY);
                fsb.append("\r\n");
                fsb.append("Content-Disposition: form-data;name=\"portrait\";filename=\""
                        + f.getName() + "\"\r\n");
                fsb.append("Content-Type: image/jpeg\r\n\r\n");
                bos.write(fsb.toString().getBytes());
                while ((r = fis.read(buf, 0, BUFFER_SIZE)) > 0) {
                    bos.write(buf, 0, r);
                    bos.flush();
                }
            }
            byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();
            bos.write(end_data);
            bos.flush();
            bos.write("\r\n".getBytes());
            bos.write(("--" + RPCHelper.BOUNDARY + "\r\n").getBytes());
            bos.flush();
            int status = conn.getResponseCode();
            if (status != Constants.HTTP_STATUS_OK) {
                bos.close();
                conn.disconnect();
                throw new HttpException(
                        String.format(
                                "-------invalid reponse from server ----- : %d",
                                status));
            }
            InputStream is = conn.getInputStream();
            StringBuilder rsb = new StringBuilder();
            while ((r = is.read(buf, 0, 512)) > 0) {
                rsb.append(new String(buf, 0, r));
            }
            String result = rsb.toString();
            if (isError(result)) {
                final String[] tmp = parseError(result);
                if (tmp != null
                        && tmp[0]
                                .equalsIgnoreCase(RPCHelper.ERROR_PASSWORD_WRONG)) {
                    tmp[1] = RPCHelper.ERROR_PASSWORD_WRONG;
                }
                throw new HttpException(new ApiException(tmp[1]));
            }
            return parseResult(result);
        } catch (MalformedURLException e) {
            throw new HttpException(e);
        } catch (ProtocolException e) {
            throw new HttpException(e);
        } catch (FileNotFoundException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 取得黑名单列表
     * 
     * @param u
     * @param page
     * @param pageSize
     * @return
     */
    public BlackList fetchBlackList(User u, int page, int pageSize)
            throws HttpException {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sblackuser.php?gsid=%s&uid=&act=list&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, page, pageSize,
                            Constants.CID, calculateS(u.uid), Constants.FROM,
                            getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            return parseBlackList(execute(request));
        } catch (Exception e) {
            Utils.loge(e);
            throw new HttpException(e);
        }
    }

    /**
     * Forward a mblog.
     * 
     * @param u
     *            user.
     * @param mblogId
     *            the id of mblog.
     * @param mblogUid
     *            the uid of the mblog author.
     * @param reason
     *            why forward it? (can be null)
     * @param content
     *            the content
     * @return success or not
     */
    public boolean forwardMBlog(User u, String mblogId, String mblogUid,
            String reason, String content, boolean isComment)
            throws HttpException {
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("c", Constants.CID));
            form.add(new BasicNameValuePair("s", calculateS(u.uid)));
            form.add(new BasicNameValuePair("act", "dort"));
            form.add(new BasicNameValuePair("id", mblogId));
            form.add(new BasicNameValuePair("mbloguid", mblogUid));
            if (reason == null) {
                form.add(new BasicNameValuePair("rtkeepreason", "0"));
            } else {
                form.add(new BasicNameValuePair("rtkeepreason", "1"));
                form.add(new BasicNameValuePair("rtreason", reason));
            }
            form.add(new BasicNameValuePair("content", content));
            form.add(new BasicNameValuePair("from", Constants.FROM));
            form.add(new BasicNameValuePair("wm", getWM()));
            form.add(new BasicNameValuePair("ua", URLEncoder
                    .encode(generateUA(mContext))));
            if (isComment) {
                form.add(new BasicNameValuePair("cmt", "3"));
            }
            UrlEncodedFormEntity entity;
            try {
                entity = new UrlEncodedFormEntity(form, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new HttpException(UNKNOWN_ERROR, e);
            }

            HttpPost request = new HttpPost(String.format(
                    "%sdealmblog.php?gsid=%s&from=%s&wm=%s&c=%s&ua=%s",
                    Constants.SERVER, u.gsid, Constants.FROM, getWMAndLang(),
                    Constants.CID, URLEncoder.encode(generateUA(mContext))));
            request.setEntity(entity);
            return parseResult(execute(request));
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get @who blog list.
     * 
     * @param u
     *            user.
     * @param uid
     *            user uid. null means it's me.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of \@who mblogs,
     *         and array[1] is ArrayList of mblogs.
     */
    public Object[] getAtMsgList(User u, String uid, int picsize, int page,
            int pagesize) throws HttpException {
        HttpGet request;
        if (uid == null) {
            request = new HttpGet(
                    String.format(
                            "%smsg.php?gsid=%s&picsize=%d&page=%d&pagesize=%d&act=0&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, picsize, page, pagesize,
                            Constants.CID, calculateS(u.uid), Constants.FROM,
                            getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
        } else {
            request = new HttpGet(
                    String.format(
                            "%smsg.php?gsid=%s&uid=%s&picsize=%d&page=%d&pagesize=%d&act=0&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, uid, picsize, page,
                            pagesize, Constants.CID, calculateS(u.uid),
                            Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
        }
        String content = execute(request);
        return getMBlogList(content);
    }

    /**
     * Get att comment list.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's id.
     * @param srcid
     *            the id of mblog.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of comments in the page, default is 10.
     * @return a two element array. array[0] is the total count of comments, and
     *         array[1] is ArrayList of comments.
     */
    public Object[] getAttCommentList(User u, String srcuid, String srcid,
            int page, int pagesize) throws HttpException {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sgetattcommentlist.php?gsid=%s&page=%d&pagesize=%d&srcuid=%s&srcid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, page, pagesize, srcuid,
                            srcid, Constants.CID, calculateS(u.uid),
                            Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            String content = execute(request);
            Object[] result = new Object[2];
            List<Comment> lst = new ArrayList<Comment>();
            result[1] = lst;

            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            result[0] = new Integer(parseText(parser));
                        } else if (parser.getName().equals("comment")) {
                            lst.add(parseComment(parser));
                        }
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get attention/fans list.
     * 
     * @param u
     *            user.
     * @param cat
     *            list type, 0 for fans' list, 1 for attention list.
     * @param uid
     *            someone's id
     * @param sort
     *            sort method, 1 for sorting by attention time, 2 for sorting by
     *            capital, 3 for sorting by update time. Only available when cat
     *            is 1.
     * @param keyword
     *            nickname filter, can be null.
     * @param lastmblog
     *            whether contains the last mblog. 0 for no, 1 for yes.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of attentions or fans.
     */
    public Object[] getAttentionList(User u, int cat, String uid, int sort,
            String keyword, int lastmblog, int page, int pagesize)
            throws HttpException {
        try {
            StringBuilder url = new StringBuilder(Constants.SERVER);
            // url.append("attention.php?sid=").append(u.sid);
            url.append("attention.php?gsid=").append(u.gsid);
            url.append("&cat=").append(cat);
            if (uid != null) {
                url.append("&uid=").append(uid);
            }
            url.append("&sort=").append(sort);
            if (keyword != null) {
                url.append("&keyword=").append(
                        URLEncoder.encode(keyword, "UTF-8"));
            }
            url.append("&lastmblog=").append(lastmblog);
            url.append("&page=").append(page);
            url.append("&pagesize=").append(pagesize);
            url.append("&c=").append(Constants.CID);
            url.append("&s=").append(calculateS(u.uid));
            url.append("&from=").append(Constants.FROM);
            url.append("&wm=").append(getWMAndLang());
            url.append("&ua=").append(URLEncoder.encode(generateUA(mContext)));
            HttpGet request = new HttpGet(url.toString());

            String content = execute(request);
            Object[] result = new Object[2];
            List<Fan> lst = new ArrayList<Fan>();
            result[1] = lst;

            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            result[0] = new Integer(parseText(parser));
                        } else if (parser.getName().equals("info")) {
                            lst.add(parseFan(parser));
                        }
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 获取验证码接口
     * 
     * @return return the auth information which is needed by regist new accout
     */

    public Object[] getAuthInfo() throws HttpException {
        try {
            Object[] result = new Object[3];
            HttpGet request = new HttpGet(String.format(
                    "%sget.php?c=%s&o=%s&from=%s&wm=%s&ua=%s",
                    "http://3g.sina.com.cn/interface/f/captcha/",
                    Constants.CID, "xml", Constants.FROM, getWMAndLang(),
                    URLEncoder.encode(generateUA(mContext))));

            String content = execute(request);
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));

            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG: {
                        if (parser.getName().equalsIgnoreCase("cpt")) {
                            result[0] = parseText(parser);
                        } else if (parser.getName().equalsIgnoreCase("pic")) {
                            result[1] = parseText(parser);
                        } else if (parser.getName().equalsIgnoreCase("q")) {
                            result[2] = parseText(parser);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
            return result;

        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get favorite hot word list.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid, null for current user.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of words in the page, default is 10.
     * @return a two element array. array[0] is the total count of words, and
     *         array[1] is ArrayList of words.
     */
    public Object[] getFavHotWordList(User u, String uid, int page, int pagesize)
            throws HttpException {
        try {
            HttpGet request;
            if (uid != null) {
                request = new HttpGet(
                        String.format(
                                "%sgetfavhotword.php?gsid=%s&uid=%s&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                                Constants.SERVER, u.gsid, uid, page, pagesize,
                                Constants.CID, calculateS(u.uid),
                                Constants.FROM, getWMAndLang(),
                                URLEncoder.encode(generateUA(mContext))));
            } else {
                request = new HttpGet(
                        String.format(
                                "%sgetfavhotword.php?gsid=%s&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                                Constants.SERVER, u.gsid, page, pagesize,
                                Constants.CID, calculateS(u.uid),
                                Constants.FROM, getWMAndLang(),
                                URLEncoder.encode(generateUA(mContext))));
            }
            String content = execute(request);
            Object[] result = new Object[2];
            List<FavHotWord> lst = new ArrayList<FavHotWord>();
            result[1] = lst;

            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            result[0] = new Integer(parseText(parser));
                        } else if (parser.getName().equals("favhotword")) {
                            FavHotWord w = parseFavHotWord(parser);
                            if (w != null)
                                lst.add(w);
                        }
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get my favorite mblogs.
     * 
     * @param u
     *            user.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of mblogs.
     */
    public Object[] getFavMBlogList(User u, int picsize, int page, int pagesize)
            throws HttpException {
        HttpGet request = new HttpGet(
                String.format(
                        "%sgetfavmblog.php?gsid=%s&picsize=%d&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                        Constants.SERVER, u.gsid, picsize, page, pagesize,
                        Constants.CID, calculateS(u.uid), Constants.FROM,
                        getWMAndLang(), URLEncoder.encode(generateUA(mContext))));
        return getMBlogList(execute(request));
    }

    // public Object[] getGuessUserList(User u) throws HttpException {
    // try {
    // Object[] result = new Object[3];
    // List<GuessUser> lst = new ArrayList<GuessUser>();
    // result[1] = lst;
    // HttpGet request = new
    // HttpGet(String.format("%sguess.php?gsid=%s&c=%s&s=%s&from=%s&wm=%s&uid=%s&lastmblog=1&ua=%s",
    // Constants.SERVER, u.gsid, Constants.CID, calculateS(u.uid),
    // Constants.FROM, getWMAndLang(), u.uid,
    // URLEncoder.encode(generateUA(mContext))));
    // String content = execute(request);
    // final XmlPullParser parser = Xml.newPullParser();
    // parser.setInput(new StringReader(content));
    // int type;
    // while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
    // switch (type) {
    // case XmlPullParser.START_TAG:
    // if (parser.getName().equals("sid")) {
    // result[2] = parseText(parser);
    // } else if (parser.getName().equals("count")) {
    // result[0] = Integer.parseInt(parseText(parser));
    // } else if (parser.getName().equals("info")) {
    // lst.add(parseGuessUser(parser));
    // }
    // break;
    // default:
    // break;
    // }
    // }
    // return result;
    // } catch (NumberFormatException e) {
    // throw new HttpException(e);
    // } catch (XmlPullParserException e) {
    // throw new HttpException(e);
    // } catch (IOException e) {
    // throw new HttpException(e);
    // } catch (ParseException e) {
    // throw new HttpException(e);
    // }
    // }

    /**
     * 
     * @param gsid
     * @param uid
     * @param picsize
     * @param pagesize
     * @return
     * @throws HttpException
     * @throws ParseException
     */
    public String getHomeBlogList(String gsid, String uid, int picsize,
            int pagesize) throws HttpException {
        HttpGet request = new HttpGet(
                String.format(
                        "%sgettimeline.php?gsid=%s&picsize=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                        Constants.SERVER, gsid, picsize, pagesize,
                        Constants.CID, calculateS(uid), Constants.FROM,
                        getWMAndLang(), URLEncoder.encode(generateUA(mContext))));

        return execute(request);
    }

    /**
     * Get home blog list.
     * 
     * @param u
     *            user.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of mblogs.
     */
    public Object[] getHomeBlogList(User u, int picsize, String maxId,
            int pagesize) throws HttpException {
        HttpGet request;
        if (maxId != null) {
            request = new HttpGet(
                    String.format(
                            "%sgettimeline.php?gsid=%s&picsize=%d&maxid=%s&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, picsize, maxId, pagesize,
                            Constants.CID, calculateS(u.uid), Constants.FROM,
                            getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
        } else {
            request = new HttpGet(
                    String.format(
                            "%sgettimeline.php?gsid=%s&picsize=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, picsize, pagesize,
                            Constants.CID, calculateS(u.uid), Constants.FROM,
                            getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
        }

        return getMBlogList(execute(request));
    }

    public Object[] getHotWordList(User u, int type, int page, int pagesize)
            throws HttpException {
        try {
            Object[] result = new Object[3];
            List<HotWord> lst = new ArrayList<HotWord>();
            result[1] = lst;
            HttpGet request = new HttpGet(
                    String.format(
                            "%sgethotword.php?gsid=%s&c=%s&s=%s&from=%s&page=%d&pagesize=%d&type=%d&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, Constants.CID,
                            calculateS(u.uid), Constants.FROM, page, pagesize,
                            type, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            String content = execute(request);
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int i;
            while ((i = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (i) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("sid")) {
                            result[2] = parseText(parser);
                        } else if (parser.getName().equals("count")) {
                            result[0] = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("item")) {
                            lst.add(parseHotWord(parser));
                        }
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 取得最新的版本信息
     * 
     * @return
     * @throws HttpException
     */
    public VersionInfo getLatestVersion() throws HttpException {
        HttpGet request = new HttpGet(String.format(
                "%sgetlatestversion.php?from=%s&wm=%s&ua=%s", Constants.SERVER,
                Constants.FROM, getWMAndLang(),
                URLEncoder.encode(generateUA(mContext))));
        return parseVersionInfo(execute(request));
    }

    public int[] getMblogCRNum(User usr, String... mblogId)
            throws HttpException {
        try {
            char[] tmp = MD5.hexdigest(usr.uid + Constants.KEY).toCharArray();
            StringBuffer m = new StringBuffer();
            m = m.append(tmp[1]).append(tmp[5]).append(tmp[2]).append(tmp[10])
                    .append(tmp[17]).append(tmp[9]).append(tmp[25])
                    .append(tmp[27]);
            String midStr = "";
            if (mblogId.length == 1) {
                midStr = mblogId[0];
            } else if (mblogId.length == 2) {

                midStr = mblogId[0] + "," + mblogId[1];
            }
            HttpGet get = new HttpGet(
                    String.format(
                            "%sgetmblogcrnum.php?gsid=%s&id=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, usr.gsid, midStr, Constants.CID,
                            m, Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));

            int[] result = new int[4];
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(execute(get)));

            int position = 0;

            if (mblogId.length == 1) {
                int type;
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("rtnum")) {
                                try {
                                    result[0] = Integer.parseInt(parser
                                            .nextText());
                                } catch (Exception e) {
                                    result[0] = -1;
                                }
                            } else if (parser.getName().equals("commentnum")) {
                                try {
                                    result[1] = Integer.parseInt(parser
                                            .nextText());
                                } catch (Exception e) {
                                    result[1] = -1;
                                }
                            }
                            break;
                    }
                }
            } else if (mblogId.length == 2) {
                int type;
                String mid = "";
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            if ("mblogid".equals(parser.getName())) {
                                mid = parser.nextText();
                            } else {
                                if (("rtnum".equals(parser.getName()))) {
                                    if (mid.equals(mblogId[0])) {
                                        result[0] = Integer.parseInt(parser
                                                .nextText());
                                    } else if (mid.equals(mblogId[1])) {
                                        result[2] = Integer.parseInt(parser
                                                .nextText());
                                    }
                                } else if ("commentnum"
                                        .equals(parser.getName())) {
                                    if (mid.equals(mblogId[0])) {
                                        result[1] = Integer.parseInt(parser
                                                .nextText());
                                    } else if (mid.equals(mblogId[1])) {
                                        result[3] = Integer.parseInt(parser
                                                .nextText());
                                    }
                                }
                            }
                            break;
                    }
                }
            }

            return result;
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get private messages list. If the uid is null, this function will get all
     * messages come from all other users, each user has only one record to tell
     * you how many messages who send to the user. If the uid is not null, it
     * will get messages only come from who you specified.
     * 
     * @param u
     *            user.
     * @param uid
     *            the user's uid.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of mblogs.
     */
    public Object[] getMessageList(User u, String uid, int page, int pagesize)
            throws HttpException {
        try {
            HttpGet request;
            if (uid == null) {
                request = new HttpGet(
                        String.format(
                                "%smsg.php?gsid=%s&act=1&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                                Constants.SERVER, u.gsid, page, pagesize,
                                Constants.CID, calculateS(u.uid),
                                Constants.FROM, getWMAndLang(),
                                URLEncoder.encode(generateUA(mContext))));
            } else {
                request = new HttpGet(
                        String.format(
                                "%smsg.php?gsid=%s&act=1&uid=%s&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                                Constants.SERVER, u.gsid, uid, page, pagesize,
                                Constants.CID, calculateS(u.uid),
                                Constants.FROM, getWMAndLang(),
                                URLEncoder.encode(generateUA(mContext))));
            }
            String content = execute(request);
            Utils.logd(content);

            Object[] result = new Object[2];
            List<Message> lst = new ArrayList<Message>();
            result[1] = lst;

            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            result[0] = new Integer(parseText(parser));
                        } else if (parser.getName().equals("msg")) {
                            lst.add(parseMessage(parser));
                        }
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (Exception e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get my comment blog list.
     * 
     * @param u
     *            user.
     * @param boxtype
     *            1 is inbox, 2 is outbox
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of comment message in the page, default is 10.
     * @return a two element array. array[0] is the total count of comments, and
     *         array[1] is ArrayList of comments.
     */
    public Object[] getMyCommentList(User u, int boxtype, int page, int pagesize)
            throws HttpException {
        try {
            HttpGet request = new HttpGet(
                    String.format(
                            "%sgetmycomment.php?gsid=%s&type=%d&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid, boxtype, page, pagesize,
                            Constants.CID, calculateS(u.uid), Constants.FROM,
                            getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            String content = execute(request);
            Object[] result = new Object[2];
            List<CommentMessage> lst = new ArrayList<CommentMessage>();
            result[1] = lst;

            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            result[0] = new Integer(parseText(parser));
                        } else if (parser.getName().equals("msg")) {
                            lst.add(parseCommentMessage(parser));
                        }
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get "look around" / "hot foward" list.
     * 
     * @param uid
     *            public uid.
     * @param cat
     *            1 for "look around", 2 for "hot forward"
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of \@me mblogs,
     *         and array[1] is ArrayList of mblogs.
     */
    public Object[] getNewsList(String uid, String cat, int picsize, int page,
            int pagesize) throws HttpException {
        HttpGet request = new HttpGet(
                String.format(
                        "%sgetnewslist.php?cat=%s&picsize=%d&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                        Constants.SERVER, cat, picsize, page, pagesize,
                        Constants.CID, calculateS(uid), Constants.FROM,
                        getWMAndLang(), URLEncoder.encode(generateUA(mContext))));
        return getMBlogList(execute(request));
    }

    /**
     * Download and save the picture to the local storage.
     * 
     * @param url
     *            the url of picture
     * @param savedir
     *            the directory to save picture, you can get it by calling
     *            context.getCacheDir().
     * @return the path of picture in the local storage.
     */
    public String getPicture(String url, String savedir) throws HttpException {
        InputStream inputStream;
        String filename = MD5.hexdigest(url);
        String filepath = savedir + "/" + filename;
        if (new File(filepath).exists()) {
            return filepath;
        }
        HttpGet request = new HttpGet(url);
        HttpClient client = getHttpClient();
        request.setHeader("User-Agent",
                WeiboApplication.UA == null ? Constants.USER_AGENT
                        : WeiboApplication.UA);
        try {
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (NullPointerException e) {
                // google issue, doing this to work around
                response = client.execute(request);
            }
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != Constants.HTTP_STATUS_OK) {
                throw new HttpException(String.format(
                        "Invalid response from server: %s", status.toString()));
            }
            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();

        } catch (IOException e) {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            throw new HttpException("Problem communicating with API", e);
        }

        try {
            FileOutputStream content = new FileOutputStream(filepath);
            // Read response into a buffered stream
            int readBytes = 0;
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
                content.flush();
            }
            content.close();
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            return filepath;
        } catch (IOException e) {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            if (filepath != null) {
                File file2 = new File(filepath);
                file2.deleteOnExit();
            }
            throw new HttpException(UNKNOWN_ERROR, e);
        }
    }

    public String getPicture(String url, String savedir, boolean needReload)
            throws HttpException {
        InputStream inputStream;
        String filename = MD5.hexdigest(url);
        // String filepath = savedir + "/" + filename;
        String filepath;
        if (url.contains("woriginal") && Utils.isEndWithGif(url)) {
            filepath = savedir + "/" + filename + ".gif";
        } else {
            filepath = savedir + "/" + filename;
        }

        File file = new File(filepath);
        if (!needReload && file.exists() && file.length() > 0) {
            return filepath;
        }

        HttpGet request = new HttpGet(url);
        HttpClient client = getHttpClient();
        request.setHeader("User-Agent",
                WeiboApplication.UA == null ? Constants.USER_AGENT
                        : WeiboApplication.UA);

        try {
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (NullPointerException e) {
                // google issue, doing this to work around
                response = client.execute(request);
            }
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != Constants.HTTP_STATUS_OK) {
                throw new HttpException(String.format(
                        "Invalid response from server: %s", status.toString()));
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();

        } catch (IOException e) {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            throw new HttpException("Problem communicating with API", e);
        }

        try {
            OutputStream content = new BufferedOutputStream(
                    new FileOutputStream(filepath));

            // Read response into a buffered stream
            int readBytes = 0;
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
                content.flush();
            }
            content.close();
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            return filepath;
        } catch (IOException e) {
            file.delete();
            throw new HttpException(UNKNOWN_ERROR, e);
        }
    }

    public byte[] getPictureByte(String url) throws HttpException {

        InputStream inputStream;

        HttpGet request = new HttpGet(url);
        HttpClient client = getHttpClient();
        request.setHeader("User-Agent",
                WeiboApplication.UA == null ? Constants.USER_AGENT
                        : WeiboApplication.UA);

        try {
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (NullPointerException e) {
                // google issue, doing this to work around
                response = client.execute(request);
            }
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != Constants.HTTP_STATUS_OK) {
                throw new HttpException(String.format(
                        "Invalid response from server: %s", status.toString()));
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();

        } catch (IOException e) {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            throw new HttpException("Problem communicating with API", e);
        }

        try {

            ByteArrayOutputStream content = new ByteArrayOutputStream();
            byte[] imageByte;
            int readBytes = 0;

            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
                content.flush();
            }
            imageByte = content.toByteArray();
            content.close();

            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            return imageByte;
        } catch (IOException e) {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            throw new HttpException(UNKNOWN_ERROR, e);
        }
    }

    /**
     * Get a mblog.
     * 
     * @param u
     *            user.
     * @param mid
     *            the blog's id
     * @param picsize
     *            the size of picture which the user prefer.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is the mblog.
     */
    public Object[] getSingleMBlog(User u, String mid, int picsize)
            throws HttpException {
        HttpGet request = new HttpGet(
                String.format(
                        "%sgetsinglemblog.php?gsid=%s&mid=%s&picsize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                        Constants.SERVER, u.gsid, mid, picsize, Constants.CID,
                        calculateS(u.uid), Constants.FROM, getWMAndLang(),
                        URLEncoder.encode(generateUA(mContext))));
        Object[] tmp = getMBlogList(execute(request));
        if (((List) tmp[1]).size() > 0)
            tmp[1] = ((List) tmp[1]).get(0);
        return tmp;
    }

    /**
     * Get my home mblog list.
     * 
     * @param u
     *            user.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of mblogs.
     */
    public Object[] getTimelineList(String gsid, String uid, String maxid,
            String minid, int picsize, int page, int pagesize)
            throws HttpException {
        StringBuilder url = new StringBuilder(Constants.SERVER);
        // url.append("gettimeline.php?sid=").append(u.sid);
        url.append("gettimeline.php?gsid=").append(gsid);
        if (maxid != null) {
            url.append("&maxid=").append(maxid);
        }
        if (minid != null) {
            url.append("&minid=").append(minid);
        }
        url.append("&picsize=").append(picsize);
        url.append("&page=").append(page);
        url.append("&pagesize=").append(pagesize);
        url.append("&c=").append(Constants.CID);
        url.append("&s=").append(calculateS(uid));
        url.append("&from=").append(Constants.FROM);
        url.append("&wm=").append(getWMAndLang());
        url.append("&ua=").append(URLEncoder.encode(generateUA(mContext)));
        HttpGet request = new HttpGet(url.toString());
        return getMBlogList(execute(request));
    }

    /**
     * Top users
     * 
     * @param uid
     *            the public user id.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of users.
     */
    public Object[] getTopUserList(String uid, int pagesize)
            throws HttpException {
        HttpGet request = null;
        if (Utils.isEnPlatform(mContext)) {
            request = new HttpGet(
                    String.format(
                            "%sgettopuser.php?pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, pagesize, Constants.CID,
                            calculateS(uid), Constants.FROM, Constants.WM + "&"
                                    + "lang=en",
                            URLEncoder.encode(generateUA(mContext))));
        } else {
            request = new HttpGet(
                    String.format(
                            "%sgettopuser.php?pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, pagesize, Constants.CID,
                            calculateS(uid), Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
        }

        return getUserInfoList(request);
    }

    /**
     * Get unread numbers.
     * 
     * @param u
     *            user
     * @return UnreadNum object.
     */
    public UnreadNum getUnreadNum(User u) throws HttpException {
        try {
            HttpGet request = new HttpGet(String.format(
                    "%sgetunreadnum.php?gsid=%s&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                    Constants.SERVER, u.gsid, Constants.CID, calculateS(u.uid),
                    Constants.FROM, getWMAndLang(),
                    URLEncoder.encode(generateUA(mContext))));
            String content = execute(request);
            UnreadNum un = new UnreadNum();
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("comment")) {
                            un.comment = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("newfans")) {
                            un.newfans = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("message")) {
                            un.message = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("atmsg")) {
                            un.atmsg = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("mblog")) {
                            un.newmblog = (parseText(parser).equals("1") ? true
                                    : false);
                        } else if (parser.getName().equals("newmblog")) {
                            un.mblog = Integer.parseInt(parseText(parser));
                        }
                        break;
                    default:
                        break;
                }
            }
            return un;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get someone's information.
     * 
     * @param u
     *            User
     * @param uid
     *            someone's id, can be null
     * @param nick
     *            someone's nickname, can be null
     * @return UserInfo
     */
    public UserInfo getUserInfo(User u, String uid, String nick)
            throws HttpException {
        try {
            StringBuilder url = new StringBuilder(Constants.SERVER);
            // url.append("getuserinfo.php?sid=").append(u.sid);
            url.append("getuserinfo.php?gsid=").append(u.gsid);
            if (uid != null) {
                url.append("&uid=").append(uid);
            } else if (!TextUtils.isEmpty(nick)) {
                url.append("&nick=").append(nick);
            }
            url.append("&c=").append(Constants.CID);
            url.append("&s=").append(calculateS(u.uid));
            url.append("&from=").append(Constants.FROM);
            url.append("&wm=").append(getWMAndLang());
            // url.append("&wm=").append(Constants.WM);
            url.append("&ua=").append(URLEncoder.encode(generateUA(mContext)));

            Utils.logd(url.toString());

            HttpGet request = new HttpGet(url.toString());
            String content = execute(request);
            Utils.logd(content);

            UserInfo ui;
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            ui = parseUserInfo(parser);

            return ui;
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Get someone's blog list.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's id
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of mblogs.
     */
    public Object[] getUserMBlogList(User u, String uid, int picsize, int page,
            int pagesize) throws HttpException {
        HttpGet request = new HttpGet(
                String.format(
                        "%sgetusermbloglist.php?gsid=%s&uid=%s&picsize=%d&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                        Constants.SERVER, u.gsid, uid, picsize, page, pagesize,
                        Constants.CID, calculateS(u.uid), Constants.FROM,
                        getWMAndLang(), URLEncoder.encode(generateUA(mContext))));
        return getMBlogList(execute(request));
    }

    // public Object[] getUserRankList(User u, int cat, int page, int pagesize)
    // throws HttpException {
    // try {
    // Object[] result = new Object[2];
    // List<UserRankItem> lst = new ArrayList<UserRankItem>();
    // result[1] = lst;
    // HttpGet request = null;
    // if(Utils.isEnPlatform(mContext) && cat == 1){
    // request = new
    // HttpGet(String.format("%slist.php?uid=%s&gsid=%s&c=%s&s=%s&from=%s&page=%d&pagesize=%d&listid=%s&wm=%s&ua=%s",
    // Constants.SERVER, "1185497733" ,u.gsid, Constants.CID,
    // calculateS(u.uid), Constants.FROM, page, pagesize, "225337219",
    // getWMAndLang(), URLEncoder.encode(generateUA(mContext))));
    // }else{
    // request = new
    // HttpGet(String.format("%sgetuserrank.php?gsid=%s&c=%s&s=%s&from=%s&page=%d&pagesize=%d&cat=%d&wm=%s&ua=%s",
    // Constants.SERVER, u.gsid, Constants.CID,
    // calculateS(u.uid), Constants.FROM, page, pagesize, cat, getWMAndLang(),
    // URLEncoder.encode(generateUA(mContext))));
    // }
    // String content = execute(request);
    // final XmlPullParser parser = Xml.newPullParser();
    // parser.setInput(new StringReader(content));
    // int type;
    // while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
    // switch (type) {
    // case XmlPullParser.START_TAG:
    // if (parser.getName().equals("count")) {
    // result[0] = new Integer(parseText(parser));
    // } else if (parser.getName().equals("info")) {
    // lst.add(parseUserRank(parser));
    // }
    // break;
    // default:
    // break;
    // }
    // }
    // return result;
    // } catch (NumberFormatException e) {
    // throw new HttpException(e);
    // } catch (XmlPullParserException e) {
    // throw new HttpException(e);
    // } catch (IOException e) {
    // throw new HttpException(e);
    // } catch (ParseException e) {
    // throw new HttpException(e);
    // }
    // }

    public boolean log() {
        HttpGet request;
        request = new HttpGet(String.format(
                "%slog.php?imei=%s&from=%s&wm=%s&ua=%s", Constants.SERVER,
                WeiboApplication.IMEI_NUM, Constants.FROM, getWMAndLang(),
                URLEncoder.encode(generateUA(mContext))));
        try {
            execute(request);
        } catch (HttpException e) {
            // Log.e(Constants.TAG, "api error", e);
            return false;
        }
        return true;
    }

    /**
     * Login with account and password.
     * 
     * @param account
     * @param passwd
     * @return a User object.
     */
    public User login(String account, String passwd) throws HttpException {
        try {
            String content = getLoginResponseContent(account, passwd);
            User u = new User();
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("gsid")) {
                            u.gsid = parseText(parser);
                        } else if (parser.getName().equals("status")) {
                            u.status = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("uid")) {
                            u.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            u.nick = parseText(parser);
                        } else if (parser.getName().equals("url")) {
                            u.url = parseText(parser);
                        } else if (parser.getName().equals("msgurl")) {
                            u.msgurl = parseText(parser);
                        } else if (parser.getName().equals("oauth_token")) {
                            u.setOauth_token(parseText(parser));
                        } else if (parser.getName()
                                .equals("oauth_token_secret")) {
                            u.setOauth_token_secret(parseText(parser));
                        }
                        break;
                    default:
                        break;
                }
            }
            return u;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Post a new mblog with or without a picture.
     * 
     * @param u
     *            user.
     * @param content
     *            the content of the mblog.
     * @param picpath
     *            the path of picture. If it is a null, then no picture will be
     *            uploaded.
     */

    public boolean postNewMBlog(String gsid, String uid, String content,
            double lat, double lon, String picpath, boolean condition)
            throws HttpException {
        try {
            byte buf[] = new byte[4096];
            int r = 0;

            HttpURLConnection conn = null;
            URL url = new URL(String.format(
                    "%sdealmblog.php?gsid=%s&wm=%s&from=%s&c=%s&ua=%s",
                    Constants.SERVER, gsid, getWMAndLang(), Constants.FROM,
                    Constants.CID, URLEncoder.encode(generateUA(mContext))));
            conn = getURLConnection(url);
            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"content\"\r\n\r\n");
            sb.append(content);
            sb.append("\r\n");
            if (condition) {
                Utils.loge("condition in");
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"long\"\r\n\r\n");
                sb.append(lon);
                sb.append("\r\n");
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"lat\"\r\n\r\n");
                sb.append(lat);
                sb.append("\r\n");
            }
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; name=\"act\"\r\n\r\n");
            sb.append("add");
            sb.append("\r\n");
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; name=\"c\"\r\n\r\n");
            sb.append(Constants.CID);
            sb.append("\r\n");
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; name=\"s\"\r\n\r\n");
            sb.append(calculateS(uid));
            sb.append("\r\n");
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; name=\"from\"\r\n\r\n");
            sb.append(Constants.FROM);
            sb.append("\r\n");
            sb.append("--");
            sb.append(RPCHelper.BOUNDARY);
            sb.append("\r\n");

            sb.append("Content-Disposition: form-data; name=\"wm\"\r\n\r\n");
            sb.append(getWMAndLang());
            sb.append("\r\n");
            sb.append("--");
            sb.append(RPCHelper.BOUNDARY);
            sb.append("\r\n");

            // 添加UA
            sb.append("Content-Disposition: form-data; name=\"ua\"\r\n\r\n");
            sb.append(URLEncoder.encode(generateUA(mContext)));
            sb.append("\r\n");
            sb.append("--");
            sb.append(RPCHelper.BOUNDARY);
            sb.append("\r\n");

            // DataOutputStream dos = new
            // DataOutputStream(conn.getOutputStream());
            BufferedOutputStream dos = new BufferedOutputStream(
                    conn.getOutputStream());
            dos.write(sb.toString().getBytes());
            if (picpath != null) {
                StringBuilder split = new StringBuilder();
                File f = new File(picpath);
                FileInputStream fis = new FileInputStream(f);
                split.append("--");
                split.append(BOUNDARY);
                split.append("\r\n");
                split.append("Content-Disposition: form-data;name=\"pic\";filename=\""
                        + f.getName() + "\"\r\n");
                split.append("Content-Type: image/jpeg\r\n\r\n");
                dos.write(split.toString().getBytes());
                while ((r = fis.read(buf, 0, 4096)) > 0) {
                    dos.write(buf, 0, r);
                    dos.flush();
                }
                dos.write("\r\n".getBytes());
            }
            byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();
            dos.write(end_data);
            dos.flush();
            int status = conn.getResponseCode();
            if (status != Constants.HTTP_STATUS_OK) {
                dos.close();
                conn.disconnect();
                throw new HttpException(String.format(
                        "Invalid response from server —— code: %d", status));
            }
            InputStream is = conn.getInputStream();
            sb = new StringBuilder();
            while ((r = is.read(buf, 0, 4096)) > 0) {
                sb.append(new String(buf, 0, r));
            }
            is.close();
            dos.close();
            conn.disconnect();

            String result = sb.toString();
            if (isError(result)) {
                final String[] tmp = parseError(result);
                throw new HttpException(new ApiException(tmp[1]));
            }
            return parseResult(result);
        } catch (MalformedURLException e) {
            throw new HttpException(e);
        } catch (ProtocolException e) {
            throw new HttpException(e);
        } catch (FileNotFoundException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 
     * @param gsid
     * @param uid
     * @param content
     * @return
     * @throws HttpException
     * @throws ParseException
     */
    public boolean postSimpleMBlog(String gsid, String uid, String content)
            throws HttpException {
        return postNewMBlog(gsid, uid, content, 0., 0., null, false);
    }

    /**
     * 
     * @param gsid
     * @param uid
     * @param content
     * @param picpath
     * @return
     * @throws HttpException
     * @throws ParseException
     */
    public boolean postSimpleMBlog(String gsid, String uid, String content,
            String picpath) throws HttpException {
        return postNewMBlog(gsid, uid, content, 0., 0., picpath, false);
    }

    /**
     * 白名单注册接口
     * 
     * @return 如果返回null，说明出现错误
     */
    public Object[] registerByWhiteList() throws HttpException {
        try {
            Object[] result = new Object[2];
            User user = new User();
            HttpGet request = new HttpGet(String.format(
                    "%swlreg.php?c=%s&s=%s&from=%s&wm=%s&ua=%s",
                    Constants.SERVER, Constants.CID, "whitelist",
                    Constants.FROM, getWMAndLang(),
                    URLEncoder.encode(generateUA(mContext))));
            String content = execute(request);
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equalsIgnoreCase("gsid")) {
                            user.gsid = parseText(parser);
                        } else if (parser.getName().equals("uid")) {
                            user.nick = user.uid = parseText(parser);
                        } else if (parser.getName().equalsIgnoreCase("pwd")) {
                            result[1] = parseText(parser);
                        }
                        break;
                    default:
                        break;
                }
            }
            user.status = 0;
            user.url = user.msgurl = "";
            result[0] = user;
            return result;
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 邮箱注册接口
     * 
     * @param usrname
     *            用户名
     * @param password
     *            密码
     * @param regtype
     *            注册类型 m：第三方邮件；resend：重新发送
     * @param cpt
     *            验证码加密串
     * @param code
     *            验证码
     * @return 如果返回false，说明出现错误
     * @throws HttpException
     */
    public User registerEmailUsr(String email, String password, String regtype,
            String cpt, String code) throws HttpException {
        char[] tmp = MD5.hexdigest(email + password + Constants.KEY)
                .toCharArray();
        char[] atemp = MD5.hexdigest(email + WeiboApplication.IMEI_NUM)
                .toCharArray();

        StringBuilder am = new StringBuilder();
        am = am.append(atemp[1]).append(atemp[2]).append(atemp[3])
                .append(atemp[4]).append(atemp[5]).append(atemp[6]);
        StringBuilder m = new StringBuilder();
        m = m.append(tmp[1]).append(tmp[5]).append(tmp[2]).append(tmp[10])
                .append(tmp[17]).append(tmp[9]).append(tmp[25]).append(tmp[27]);

        HttpPost post = new HttpPost(String.format(
                "%sreg.php?s=%s&c=%s&from=%s&wm=%s", Constants.SERVER, m,
                Constants.CID, Constants.FROM, getWMAndLang()));
        LinkedList<BasicNameValuePair> pairList = new LinkedList<BasicNameValuePair>();
        pairList.add(new BasicNameValuePair("u", email));
        pairList.add(new BasicNameValuePair("regtype", regtype));
        if (regtype.equals("resend")) {
            pairList.add(new BasicNameValuePair("cpt", cpt));
            pairList.add(new BasicNameValuePair("code", code));
        }
        pairList.add(new BasicNameValuePair("p", password));
        pairList.add(new BasicNameValuePair("wm", getWM()));
        pairList.add(new BasicNameValuePair("ua", URLEncoder
                .encode(generateUA(mContext))));
        try {
            HttpEntity entity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);
            post.setEntity(entity);
            String as = entity.toString();

            final XmlPullParser parser = Xml.newPullParser();
            User ausr = new User();
            String content = execute(post);
            // Log.e(Constants.TAG, content);
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("gsid")) {
                            ausr.gsid = parseText(parser);
                        }/*
                          * else if (parser.getName().equals("sid")) { ausr.sid
                          * = parseText(parser); }
                          */
                        else if (parser.getName().equals("status")) {
                            ausr.status = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("uid")) {
                            ausr.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            ausr.nick = parseText(parser);
                        } else if (parser.getName().equals("url")) {
                            ausr.url = parseText(parser);
                        } else if (parser.getName().equals("msgurl")) {
                            ausr.msgurl = parseText(parser);
                        }
                        break;
                }
            }
            return ausr;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 快速注册接口
     * 
     * @param usrname
     *            用户名
     * @param password
     *            密码
     * @param nick
     *            nickname
     * @param gender
     *            1 or 0
     * @return 如果返回null，说明出现错误
     * @throws HttpException
     */
    public User registerUsr(String usrname, String password, String nick,
            String gender, String cpt, String authCode) throws HttpException {
        char[] tmp = MD5.hexdigest(usrname + password + Constants.KEY)
                .toCharArray();
        char[] atemp = MD5.hexdigest(usrname + WeiboApplication.IMEI_NUM)
                .toCharArray();

        StringBuilder am = new StringBuilder();
        am = am.append(atemp[1]).append(atemp[2]).append(atemp[3])
                .append(atemp[4]).append(atemp[5]).append(atemp[6]);
        StringBuilder m = new StringBuilder();
        m = m.append(tmp[1]).append(tmp[5]).append(tmp[2]).append(tmp[10])
                .append(tmp[17]).append(tmp[9]).append(tmp[25]).append(tmp[27]);
        HttpPost post = new HttpPost(String.format(
                "%sreg.php?s=%s&c=%s&from=%s&wm=%s", Constants.SERVER, m,
                Constants.CID, Constants.FROM, getWMAndLang()));
        LinkedList<BasicNameValuePair> pairList = new LinkedList<BasicNameValuePair>();
        pairList.add(new BasicNameValuePair("u", usrname));
        pairList.add(new BasicNameValuePair("p", password));
        pairList.add(new BasicNameValuePair("q", mContext
                .getString(R.string.regist_password_key)));
        pairList.add(new BasicNameValuePair("a", am.toString()));
        pairList.add(new BasicNameValuePair("nick", nick));
        pairList.add(new BasicNameValuePair("gender", gender));
        pairList.add(new BasicNameValuePair("cpt", cpt));
        pairList.add(new BasicNameValuePair("code", authCode));
        pairList.add(new BasicNameValuePair("wm", getWM()));
        pairList.add(new BasicNameValuePair("ua", URLEncoder
                .encode(generateUA(mContext))));
        try {
            HttpEntity entity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);
            post.setEntity(entity);

            final XmlPullParser parser = Xml.newPullParser();
            User ausr = new User();
            String content = execute(post);
            // Log.e(Constants.TAG, content);
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("gsid")) {
                            ausr.gsid = parseText(parser);
                        }/*
                          * else if (parser.getName().equals("sid")) { ausr.sid
                          * = parseText(parser); }
                          */
                        else if (parser.getName().equals("status")) {
                            ausr.status = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("uid")) {
                            ausr.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            ausr.nick = parseText(parser);
                        } else if (parser.getName().equals("url")) {
                            ausr.url = parseText(parser);
                        } else if (parser.getName().equals("msgurl")) {
                            ausr.msgurl = parseText(parser);
                        }
                        break;
                }
            }
            return ausr;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Reply a comment for a comment. In this case, cmtuid and cmtid in the
     * comment must be set.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param cmtuid
     *            comment author's uid
     * @param cmtid
     *            the id of the comment
     * @param content
     *            comment
     * @param rt
     *            是否评论后在转发
     * @return success or not
     */
    public boolean replyComment(User u, String srcuid, String srcid,
            String cmtuid, String cmtid, String content, boolean rt)
            throws HttpException {

        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("c", Constants.CID));
            form.add(new BasicNameValuePair("s", calculateS(u.uid)));
            form.add(new BasicNameValuePair("act", "addReply"));
            form.add(new BasicNameValuePair("srcuid", srcuid));
            form.add(new BasicNameValuePair("srcid", srcid));
            form.add(new BasicNameValuePair("cmtuid", cmtuid));
            form.add(new BasicNameValuePair("cmtid", cmtid));
            form.add(new BasicNameValuePair("content", content));
            form.add(new BasicNameValuePair("from", Constants.FROM));
            form.add(new BasicNameValuePair("wm", getWM()));
            form.add(new BasicNameValuePair("ua", URLEncoder
                    .encode(generateUA(mContext))));
            if (rt) {
                form.add(new BasicNameValuePair("rt", "1"));
            }
            UrlEncodedFormEntity entity;
            entity = new UrlEncodedFormEntity(form, "UTF-8");
            HttpPost request = new HttpPost(String.format(
                    "%sdealcomment.php?gsid=%s&from=%s&wm=%s&c=%s",
                    Constants.SERVER, u.gsid, Constants.FROM, getWMAndLang(),
                    Constants.CID));
            request.setEntity(entity);
            return parseResult(execute(request));
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    public boolean report(User usr, String srcuid, String mblogId,
            String reason_) throws HttpException {
        return report(usr, srcuid, mblogId, reason_, 1, 1);
    }

    public boolean reportBlog(User usr, String mblogId, String reason_)
            throws HttpException {
        return report(usr, null, mblogId, reason_, 1, 1);
    }

    public boolean reportUser(User usr, String srcuid, String reason_)
            throws HttpException {
        return report(usr, srcuid, null, reason_, 1, 1);
    }

    /**
     * Search mblogs
     * 
     * @param u
     *            user.
     * @param keyword
     *            what you want to search.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of mblogs.
     */
    public Object[] searchMBlog(User u, String keyword, int picsize, int page,
            int pagesize) throws HttpException {
        try {
            HttpGet request;
            request = new HttpGet(
                    String.format(
                            "%ssearchmblog.php?gsid=%s&keyword=%s&picsize=%d&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid,
                            URLEncoder.encode(keyword, "UTF-8"), picsize, page,
                            pagesize, Constants.CID, calculateS(u.uid),
                            Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            return getMBlogList(execute(request));

        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
    }

    /**
     * Search users
     * 
     * @param u
     *            user.
     * @param keyword
     *            what you want to search.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of users.
     */
    public Object[] searchUser(User u, String keyword, int page, int pagesize)
            throws HttpException {
        try {
            HttpGet request;
            request = new HttpGet(
                    String.format(
                            "%ssearchuser.php?gsid=%s&keyword=%s&page=%d&pagesize=%d&c=%s&s=%s&from=%s&wm=%s&ua=%s",
                            Constants.SERVER, u.gsid,
                            URLEncoder.encode(keyword, "UTF-8"), page,
                            pagesize, Constants.CID, calculateS(u.uid),
                            Constants.FROM, getWMAndLang(),
                            URLEncoder.encode(generateUA(mContext))));
            return getUserInfoList(request);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 获取短信注册服务器手机号码
     * 
     * @return： HashMap hm，包含手机号和对应运营商的哈希表
     */
    public HashMap<String, String> getRegSmsNum() throws HttpException,
            XmlPullParserException, IOException, ParseException {
        HashMap<String, String> hm = new HashMap<String, String>();
        HttpGet request;
        request = new HttpGet(String.format(
                "%sgetregsmsnum.php?from=%s&wm=%s&ua=%s&c=%s",
                Constants.SERVER, Constants.FROM, getWMAndLang(),
                URLEncoder.encode(generateUA(mContext)), Constants.CID));
        String rlt = execute(request);

        final XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new StringReader(rlt));
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("cmcc")) {
                        hm.put(mContext.getString(R.string.reg_sms_cm),
                                parseText(parser));
                    } else if (parser.getName().equals("unicom")) {
                        hm.put(mContext.getString(R.string.reg_sms_uni),
                                parseText(parser));
                    } else if (parser.getName().equals("telecom")) {
                        hm.put(mContext.getString(R.string.reg_sms_ct),
                                parseText(parser));
                    } else if (parser.getName().equals("fallback")) {

                    }
                    break;
                default:
                    break;
            }
        }
        return hm;
    }

    /**
     * Send a private message.
     * 
     * @param u
     *            user.
     * @param nick
     *            someone's nick
     * @param content
     *            the content of the message.
     * @return success or not
     */
    public boolean sendMessage(User u, String nick, String content)
            throws HttpException {
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("c", Constants.CID));
            form.add(new BasicNameValuePair("s", calculateS(u.uid)));
            form.add(new BasicNameValuePair("act", "send"));
            form.add(new BasicNameValuePair("nick", nick));
            form.add(new BasicNameValuePair("content", content));
            form.add(new BasicNameValuePair("from", Constants.FROM));
            form.add(new BasicNameValuePair("wm", getWM()));
            form.add(new BasicNameValuePair("ua", URLEncoder
                    .encode(generateUA(mContext))));

            UrlEncodedFormEntity entity;
            try {
                entity = new UrlEncodedFormEntity(form, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new HttpException(UNKNOWN_ERROR, e);
            }

            HttpPost request = new HttpPost(String.format(
                    "%sdealmsg.php?gsid=%s&from=%s&wm=%s&c=%s",
                    Constants.SERVER, u.gsid, Constants.FROM, getWMAndLang(),
                    Constants.CID));
            request.setEntity(entity);
            return parseResult(execute(request));
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    private String execute(HttpUriRequest request) throws HttpException {
        HttpClient client = getHttpClient();
        request.setHeader("User-Agent",
                WeiboApplication.UA == null ? Constants.USER_AGENT
                        : WeiboApplication.UA);
        request.setHeader("Accept-Encoding", "gzip,deflate");
        try {
            if (client == null)
                throw new IOException();
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (NullPointerException e) {
                // google issue, doing this to work around
                response = client.execute(request);
            }
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            if (statusCode != Constants.HTTP_STATUS_OK) {
                throw new HttpException(String.format(
                        "Invalid response from server: %s", status.toString()));
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null
                    && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }

            // Read response into a buffered stream
            int readBytes = 0;
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            // Return result from buffered stream
            String result = new String(content.toByteArray());
            if (isError(result)) {
                final String[] tmp = parseError(result);
                Utils.loge(result);
                Utils.loge(Arrays.toString(tmp));
                HttpException he = new HttpException(new ApiException(tmp[1]));
                he.setStatusCode(Integer.valueOf(tmp[0]));
                throw he;
            }
            return result;
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }
    }

    private HttpClient getHttpClient() throws HttpException {
        NetUtils.NetworkState state = NetUtils.getNetworkState(mContext);
        HttpClient client = new DefaultHttpClient();
        // String product = Build.PRODUCT;
        if (state == NetUtils.NetworkState.NOTHING) {
            throw new HttpException(new NoSignalException());
        } else if (state == NetUtils.NetworkState.MOBILE) {
            NetUtils.APNWrapper wrapper = null;
            wrapper = NetUtils.getAPN(mContext);
            if (!TextUtils.isEmpty(wrapper.proxy)) {
                client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
                        new HttpHost(wrapper.proxy, wrapper.port));
            }

        }

        HttpConnectionParamBean paramHelper = new HttpConnectionParamBean(
                client.getParams());
        paramHelper.setSoTimeout(Constants.TIMEOUT);
        paramHelper.setConnectionTimeout(Constants.TIMEOUT);
        return client;
    }

    private String getLoginResponseContent(String account, String passwd)
            throws HttpException {
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("u", account));
            form.add(new BasicNameValuePair("p", passwd));
            form.add(new BasicNameValuePair("c", Constants.CID));
            form.add(new BasicNameValuePair("s", calculateS(account + passwd)));
            form.add(new BasicNameValuePair("ua", URLEncoder
                    .encode(generateUA(mContext))));
            UrlEncodedFormEntity entity;
            entity = new UrlEncodedFormEntity(form, "UTF-8");
            HttpPost request = new HttpPost(String.format(
                    "%slogin.php?from=%s&wm=%s&c=%s", Constants.SERVER,
                    Constants.FROM, getWMAndLang(), Constants.CID));
            request.setEntity(entity);
            String content = execute(request);
            return content;
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
    }

    private Object[] getMBlogList(String content) throws HttpException {
        try {
            Object[] result = new Object[3];
            List<MBlog> lst = new ArrayList<MBlog>();
            result[1] = lst;

            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            try {
                                result[0] = new Integer(parseText(parser));
                            } catch (Exception e) {
                                result[0] = 0;
                            }
                        } else if (parser.getName().equals("mblog")) {
                            MBlog mb = parseMBlog(parser);
                            if (mb != null)
                                lst.add(mb);
                        } else if (parser.getName().equals("relation")) {
                            result[2] = new Integer(parseText(parser));
                        }
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    private HttpURLConnection getURLConnection(URL url) throws HttpException {
        HttpURLConnection conn = null;
        try {
            NetUtils.NetworkState state = NetUtils.getNetworkState(mContext);
            if (state == NetUtils.NetworkState.NOTHING) {
                throw new HttpException(new NoSignalException());
            } else if (state == NetUtils.NetworkState.MOBILE) {
                NetUtils.APNWrapper wrapper;
                wrapper = NetUtils.getAPN(mContext);
                if (!TextUtils.isEmpty(wrapper.proxy)) {
                    Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,
                            new InetSocketAddress(wrapper.proxy, wrapper.port));
                    conn = (HttpURLConnection) url.openConnection(proxy);

                } else {
                    conn = (HttpURLConnection) url.openConnection();
                }

            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(Constants.TIMEOUT);
            conn.setReadTimeout(Constants.UPLOAD_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
                    + "; boundary=" + BOUNDARY);
        } catch (IOException e) {
            throw new HttpException(e);
        }
        return conn;
    }

    private Object[] getUserInfoList(HttpGet request) throws HttpException {
        try {
            String content = execute(request);
            Object[] result = new Object[2];
            List<UserInfo> lst = new ArrayList<UserInfo>();
            result[1] = lst;

            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("count")) {
                            try {
                                result[0] = new Integer(parseText(parser));
                            } catch (Exception e) {
                                result[0] = 0;
                            }
                        } else if (parser.getName().equals("info")) {
                            UserInfo ui = parseUserInfo(parser);
                            if (ui != null)
                                lst.add(ui);
                        }
                        break;
                    default:
                        break;
                }
            }
            return result;
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    /**
     * 解析黑名单列表
     * 
     * @param content
     * @return
     */
    private BlackList parseBlackList(String content) {
        System.out.println("RPCHelper.parseBlackList()");
        final HashMap<String, Object> datas = new HashMap<String, Object>();
        final Set<HashMap<String, Object>> items = new LinkedHashSet<HashMap<String, Object>>();
        final XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG: {
                        if (parser.getName().equals("sid")) {
                            datas.put("sid", parseText(parser));
                        } else if (parser.getName().equals("count")) {
                            datas.put("count", parseText(parser));
                        } else if (parser.getName().equals("info")) {
                            parseBlackListItem(parser, items);
                        }
                        break;
                    }
                }
            }
            datas.put("infos", items);
            return new BlackList(content);
        } catch (Exception e) {
            Utils.loge(e);
        }
        return BlackList.NULL;
    }

    /**
     * 解析单个 info 结构
     * 
     * @param parser
     * @param datas
     * @throws XmlPullParserException
     * @throws IOException
     * @throws ParseException
     */
    private void parseBlackListItem(XmlPullParser parser,
            Set<HashMap<String, Object>> datas) throws XmlPullParserException,
            IOException, ParseException {
        final HashMap<String, Object> itemData = new HashMap<String, Object>();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG: {
                    if (parser.getName().equals("portrait")) {
                        itemData.put("portrait", parseText(parser));
                    } else if (parser.getName().equals("uid")) {
                        itemData.put("uid", parseText(parser));
                    } else if (parser.getName().equals("nick")) {
                        itemData.put("nick", parseText(parser));
                    } else if (parser.getName().equals("addtime")) {
                        itemData.put("addtime", parseText(parser));
                    }
                    break;

                }
                case XmlPullParser.END_TAG: {
                    if (parser.getName().equals("info")) {
                        datas.add(itemData);
                        return;
                    }
                    break;
                }
            }
        }
    }

    /**
     * 解析最新版本信息
     * 
     * @param content
     * @return
     */
    private VersionInfo parseVersionInfo(String content) {
        final HashMap<String, String> itemData = new HashMap<String, String>();
        final XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG: {
                        if (parser.getName().equals("version")) {
                            itemData.put("version", parseText(parser));
                        } else if (parser.getName().equals("download")) {
                            itemData.put("download", parseText(parser));
                        } else if (parser.getName().equals("wapurl")) {
                            itemData.put("wapurl", parseText(parser));
                        } else if (parser.getName().equals("md5")) {
                            itemData.put("md5", parseText(parser));
                        } else if (parser.getName().equals("desc")) {
                            itemData.put("desc", parseText(parser));
                        } else if (parser.getName().equals("changedate")) {
                            itemData.put("changedate", parseText(parser));
                        }
                        break;
                    }
                }
            }
            return new VersionInfo(itemData);
        } catch (Exception e) {
            Utils.loge(e);
        }
        return VersionInfo.NULL;
    }

    /**
     * 以上为正确返回格式，接口错误直接返回统一错误格式<br />
     * 1. URL我们会转换为WEB的URL格式 http://t.sina.com.cn/uid/mblogid
     * 这种，所以要举报某条微博，需要将此微博所属的uid也传过来，否则无效 <br />
     * 2. type和互联网一致(1.内容涉及色情或暴力;2.政治反动举报;3.内容可能侵权;4.内容涉及其他违规事项)，现在wap这边都是1，
     * 可以不传此参数 <br />
     * 3. group和互联网一致(1.用户；2微博；3评论)，wap这边默认也都是1，可以不传此参数
     * 
     * @param usr
     *            当前用户gsid POST/GET方式，必选，通过登录接口获取得到
     * @param srcuid
     *            举报的uid POST/GET方式，可选 要举报某个用户，请填写此uid
     * @param mblogId
     *            举报的微博id POST/GET方式，可选 要举报某个用户的某条微博，请填写此id
     * @param reason_
     *            举报原因 POST/GET方式，必选
     * @param type
     *            举报类型 POST/GET方式，可选，默认为1
     * @param group
     *            举报对象 POST/GET方式，可选，默认为1
     * @return
     * @throws HttpException
     */
    private boolean report(User usr, String srcuid, String mblogId,
            String reason_, int type, int group) throws HttpException {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.SERVER).append("dealcomplaint.php?")
                .append("gsid=").append(usr.gsid).append("&c=")
                .append(Constants.CID).append("&s=")
                .append(calculateS(usr.uid)).append("&from=")
                .append(Constants.FROM).append("&wm=").append(getWMAndLang())
                .append("&ua=").append(URLEncoder.encode(generateUA(mContext)));

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        if (type >= 1 && type <= 4) {
            params.add(new BasicNameValuePair("type", String.valueOf(type)));
        }
        if (group >= 1 && group <= 3) {
            params.add(new BasicNameValuePair("group", String.valueOf(group)));
        }
        if (!TextUtils.isEmpty(srcuid)) {
            params.add(new BasicNameValuePair("srcuid", srcuid));
        }
        if (!TextUtils.isEmpty(mblogId)) {
            params.add(new BasicNameValuePair("id", mblogId));
        }
        params.add(new BasicNameValuePair("reason", reason_));
        params.add(new BasicNameValuePair("ua", URLEncoder.encode(URLEncoder
                .encode(generateUA(mContext)))));
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
                    "UTF-8");
            HttpPost post = new HttpPost(sb.toString());
            post.setEntity(entity);
            String res = execute(post);
            if (!TextUtils.isEmpty(res)) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(new StringReader(res));
                int resType = 1;
                while ((resType = parser.next()) != XmlPullParser.END_DOCUMENT) {
                    if (resType == XmlPullParser.START_TAG
                            && parser.getName().equalsIgnoreCase("result")) {
                        if (Integer.parseInt(parseText(parser)) == 1) {
                            return true;
                        } else {
                            Utils.logd("result code is not 1.");
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }

        return false;
    }

    /**
     * 返回Url中所需的wm和Lang参数
     * 
     * @return
     */
    private String getWMAndLang() {
        SettingsPref.changeLocale(mContext);
        String lang = mContext.getString(R.string.language_param);
        return Constants.WM + "&" + "lang=" + lang;
    }

    /**
     * 返回Url中所需的wm参数
     * 
     * @return
     */
    private String getWM() {
        return Constants.WM;
    }

    private RPCHelper(Context ctx) {
        mContext = ctx;
    }

    public User login(User user) throws HttpException {
        // TODO Auto-generated method stub
        try {
            String content = getLoginResponseContent(user);
            User u = new User();
            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("gsid")) {
                            u.gsid = parseText(parser);
                        } else if (parser.getName().equals("status")) {
                            u.status = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("uid")) {
                            u.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            u.nick = parseText(parser);
                        } else if (parser.getName().equals("url")) {
                            u.url = parseText(parser);
                        } else if (parser.getName().equals("msgurl")) {
                            u.msgurl = parseText(parser);
                        }
                        break;
                    default:
                        break;
                }
            }
            return u;
        } catch (NumberFormatException e) {
            throw new HttpException(e);
        } catch (XmlPullParserException e) {
            throw new HttpException(e);
        } catch (IOException e) {
            throw new HttpException(e);
        } catch (ParseException e) {
            throw new HttpException(e);
        }
    }

    private String getLoginResponseContent(User u) throws HttpException {

        StringBuilder url = new StringBuilder(Constants.SERVER);
        url.append("login.php?gsid=").append(u.gsid);
        url.append("&c=").append(Constants.CID);
        url.append("&s=").append(calculateS(u.uid));
        url.append("&from=").append(Constants.FROM);
        url.append("&wm=").append(getWMAndLang());
        // url.append("&wm=").append(Constants.WM);
        url.append("&ua=").append(URLEncoder.encode(generateUA(mContext)));
        String str = url.toString();
        HttpGet request = new HttpGet(str);
        String content;
        content = execute(request);
        return content;

    }
}
package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;

import com.sina.weibo.exception.WeiboParseException;

public class MessageSendResult extends DataObject implements Serializable {

    private static final long serialVersionUID = 3240417424496836458L;

    public String result = null;
    public long sendTime;
    public int send_type;
    public String msgid;
    public String recipient_id;
    public String recipient_nick;
    public String recipient_profile_image;
    public String sender_id;
    public String sender_nick;
    public String sender_profile_image;
    public String content;

    public String attachment_fid;
    public String attachment_sha1;
    public String attachment_name;
    public long attachment_ctime;
    public long attachment_ltime;
    public String attachment_dir_id;
    public long attachment_size;
    public String attachment_type;
    public int attachment_w;
    public int attachment_h;
    public String attachment_url;
    public String attachment_thumbnail;
    public String attachment_virus_scan;
    public String attachment_is_safe;
    public String attachment_s3_url;

    public String err_code = "";
    public String err_msg = "";

    public boolean isSuccessful() {
        return "1".equals(result) || result == null;
        // boolean flag = false;
        // if(result != null && "1".equals(result)){
        // flag = true;
        // }else{
        // if(err_code != null && !"".equals(err_code.trim())){
        // result = err_code;
        // flag = false;
        // }
        // }
        // return flag;
    }

    public String getErrMsg() {
        return err_msg;
    }

    public MessageSendResult() {
    }

    public MessageSendResult(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public MessageSendResult(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    public MessageSendResult initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public MessageSendResult initFromString(String xmlStr)
            throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected MessageSendResult parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equalsIgnoreCase("result")) {
                            this.result = parseText(parser);
                        } else if (parser.getName().equals("errno")) {
                            this.err_code = parseText(parser);
                        } else if (parser.getName().equals("errmsg")) {
                            this.err_msg = parseText(parser);
                        } else if (parser.getName().equals("time")) {
                            this.sendTime = Long.parseLong(parseText(parser));
                        }

                        else if (parser.getName().equals("time")) {
                            this.sendTime = Long.parseLong(parseText(parser));
                        } else if (parser.getName().equals("send_type")) {
                            String sendType = parseText(parser);
                            if (!TextUtils.isEmpty(sendType)) {
                                this.send_type = Integer.parseInt(sendType);
                            } else {
                                this.send_type = -1;
                            }
                        } else if (parser.getName().equals("msgid")) {
                            this.msgid = parseText(parser);
                        } else if (parser.getName().equals("recipient_id")) {
                            this.recipient_id = parseText(parser);
                        } else if (parser.getName().equals("recipient_nick")) {
                            this.recipient_nick = parseText(parser);
                        } else if (parser.getName().equals(
                                "recipient_profile_image")) {
                            this.recipient_profile_image = parseText(parser);
                        } else if (parser.getName().equals("sender_id")) {
                            this.sender_id = parseText(parser);
                        } else if (parser.getName().equals("sender_nick")) {
                            this.sender_nick = parseText(parser);
                        } else if (parser.getName().equals(
                                "sender_profile_image")) {
                            this.sender_profile_image = parseText(parser);
                        } else if (parser.getName().equals("content")) {
                            this.content = parseText(parser);
                        }

                        // 附件
                        else if (parser.getName().equals("fid")) {
                            this.attachment_fid = parseText(parser);
                        } else if (parser.getName().equals("sha1")) {
                            this.attachment_sha1 = parseText(parser);
                        } else if (parser.getName().equals("name")) {
                            this.attachment_name = parseText(parser);
                        } else if (parser.getName().equals("ctime")) {
                            this.attachment_ctime = Long
                                    .parseLong(parseText(parser));
                        } else if (parser.getName().equals("ltime")) {
                            this.attachment_ltime = Long
                                    .parseLong(parseText(parser));
                        } else if (parser.getName().equals("dir_id")) {
                            this.attachment_dir_id = parseText(parser);
                        } else if (parser.getName().equals("size")) {
                            this.attachment_size = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("type")) {
                            this.attachment_type = parseText(parser);
                        } else if (parser.getName().equals("w")) {
                            this.attachment_w = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("h")) {
                            this.attachment_h = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("url")) {
                            this.attachment_url = parseText(parser);
                        } else if (parser.getName().equals("thumbnail")) {
                            this.attachment_thumbnail = parseText(parser);
                        } else if (parser.getName().equals("virus_scan")) {
                            this.attachment_virus_scan = parseText(parser);
                        } else if (parser.getName().equals("is_safe")) {
                            this.attachment_is_safe = parseText(parser);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            throw new WeiboParseException(e);
        } catch (IOException e) {
            throw new WeiboParseException(e);
        }
        return this;
    }
}
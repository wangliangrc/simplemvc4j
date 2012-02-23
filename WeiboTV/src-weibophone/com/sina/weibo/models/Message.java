package com.sina.weibo.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 私信
 * 
 * @author duncan
 * 
 */
public class Message extends DataObject implements Serializable {
    private static final long serialVersionUID = 7758101094155298971L;
    // 消息类型
    public static final String MIME_AUDIO = "audio/x-wav";
    public static final String MIME_IMAGE = "image/jpeg";
    public static final String MIME_TEXT = "text/plain";
    public static final String MIME_VIDEO = "video/x-msvideo";

    public static final int TYPE_AUDIO = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_LOCATION = 4;
    public static final int TYPE_UNKNOW = -1;

    public static final int MESSAGE_SENDER = 1;
    public static final int MESSAGE_RECEIVER = 0;
    public static final int MESSAGE_REPOST = 2;
    public static final int SERVER_COMFIRMED = 1;
    public static final int SERVER_UNCOMFIRMED = 0;
    // 是否显示时间
    public boolean isShowTime;

    // 私信主要内容
    public int num;// 和此uid的所有私信条数，只在uid为空时有
    public Date time;// 此条私信的时间
    public int type;// 1为对别人说，0为别人对我说,2转发私信
    public String uid;// 对方
    public String nick;// 昵称
    public String remark;// 备注名称
    public String portrait;// 发言人头像
    public int vip;// vip用户类型
    public int vipsubtype;// vip用户子类型
    public int level;// 级别
    public String content;// 消息内容
    public String msgid = null;// 服务器返回的message id

    // 私信附件
    public String attachment_fid = ""; // 文件在微盘上的id
    public String attachment_sha1;// 文件的验证码
    public String attachment_name;// 文件名称
    public long attachment_ctime;// 创建文件的时间
    public long attachment_ltime;// 上次修改的时间
    public String attachment_dir_id;// 目录id
    public long attachment_size;// 附件（文件）大小
    public String attachment_type;// 文件类型：是图片，是音频等等
    public int attachment_w;// 图片宽
    public int attachment_h;// 图片高
    public String attachment_url;// 文件的下载地址
    public String attachment_thumbnail;// 缩略图的下载地址
    public String attachment_virus_scan;// 是否经过病毒扫描，经过返回true，未经过为false
    public String attachment_is_safe;// 如果经过病毒扫描，是安全的为true。
    public String attachment_s3_url;
    // 本地额外信息
    public String attachment_localFilePath;// 附件在本地文件系统中的路径
    public String upload_key;// 当传输附件到微盘时，微盘给每个文件创建的一个key，有效时间2天

    public String id;// 数据库里主键；
    public String localMsgID = "";// 本地为这条消息（message）创建的一个id
    public long localTime = -1;// 本条消息对应的本地时间，需要把服务器的时间映射成本地时间，用于对消息进行排序；
    public int state = IMMessageEvent.IM_STATE_SUCCESS;// 本条消息的状态。如果是用户本身发送的消息，则状态有：正在发送中，发送失败，发送成功，发送暂停等等。如果是接收到对方的消息，则状态只有：成功
    public int serverConfirmed = SERVER_UNCOMFIRMED;// 表示本条消息是从服务器上获取到的。值为0，1（或是yes，no）因为服务器上记录了所有成功接收到的消息，并会给每个message分配一个新的id，而不用客户端发送上来时携带的id。
    public String gsid;// 自己的gsid
    private IIMMessageListener mMessageListener;
    private int mMessageType;
    public boolean isResend = false;

    public Message() {

    }

    public Message(IIMMessageListener listener) {

    }

    public Message(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public Message(XmlPullParser _parser) throws WeiboParseException {
        initFromParser(_parser);
    }

    @Override
    public Message initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return null;
    }

    public Message initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    protected Message parse() throws WeiboParseException {
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("num")) {
                            this.num = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("time")) {
                            this.time = new Date(
                                    Long.parseLong(parseText(parser)) * 1000);
                        } else if (parser.getName().equals("type")) {
                            this.type = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("msgid")) {
                            this.msgid = parseText(parser);
                        } else if (parser.getName().equals("uid")) {
                            this.uid = parseText(parser);
                        } else if (parser.getName().equals("nick")) {
                            this.nick = parseText(parser);
                        } else if (parser.getName().equals("remark")) {
                            this.remark = parseText(parser);
                        } else if (parser.getName().equals("portrait")) {
                            this.portrait = parseText(parser);
                        } else if (parser.getName().equals("vip")) {
                            this.vip = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("vipsubtype")) {
                            this.vipsubtype = Integer
                                    .parseInt(parseText(parser));
                        } else if (parser.getName().equals("level")) {
                            this.level = Integer.parseInt(parseText(parser));
                        } else if (parser.getName().equals("content")) {
                            this.content = parseText(parser);
                        } else if (parser.getName().equals("attachment")) {
                            boolean isEnd = false;
                            while (!isEnd
                                    && (type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                                switch (type) {
                                    case XmlPullParser.START_TAG:
                                        // 附件
                                        if (parser.getName().equals("fid")) {
                                            this.attachment_fid = parseText(parser);
                                        } else if (parser.getName().equals(
                                                "sha1")) {
                                            this.attachment_sha1 = parseText(parser);
                                        } else if (parser.getName().equals(
                                                "name")) {
                                            this.attachment_name = parseText(parser);
                                        } else if (parser.getName().equals(
                                                "ctime")) {
                                            this.attachment_ctime = Long
                                                    .parseLong(parseText(parser));
                                        } else if (parser.getName().equals(
                                                "ltime")) {
                                            this.attachment_ltime = Long
                                                    .parseLong(parseText(parser));
                                        } else if (parser.getName().equals(
                                                "dir_id")) {
                                            this.attachment_dir_id = parseText(parser);
                                        } else if (parser.getName().equals(
                                                "size")) {
                                            this.attachment_size = Long
                                                    .parseLong(parseText(parser));
                                        } else if (parser.getName().equals(
                                                "type")) {
                                            this.attachment_type = parseText(parser);
                                        } else if (parser.getName().equals("w")) {
                                            this.attachment_w = Integer
                                                    .parseInt(parseText(parser));
                                        } else if (parser.getName().equals("h")) {
                                            this.attachment_h = Integer
                                                    .parseInt(parseText(parser));
                                        } else if (parser.getName().equals(
                                                "url")) {
                                            this.attachment_url = parseText(parser);
                                        } else if (parser.getName().equals(
                                                "thumbnail")) {
                                            this.attachment_thumbnail = parseText(parser);
                                        } else if (parser.getName().equals(
                                                "virus_scan")) {
                                            this.attachment_virus_scan = parseText(parser);
                                        } else if (parser.getName().equals(
                                                "is_safe")) {
                                            this.attachment_is_safe = parseText(parser);
                                        } else if (parser.getName().equals(
                                                "s3_url")) {
                                            this.attachment_s3_url = parseText(parser);
                                        }
                                        break;
                                    case XmlPullParser.END_TAG:
                                        if (parser.getName().equals(
                                                "attachment")) {
                                            isEnd = true;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("msg")) {
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

    public IIMMessageListener getMessageListener() {
        return mMessageListener;
    }

    public void setMessageListener(IIMMessageListener mMessageListener) {
        this.mMessageListener = mMessageListener;
    }

    public int getMessageType() {
        return mMessageType;
    }

    /**
     * 
     * @param fileName
     * @return
     */
    public static int getMessageType(String fileName) {
        int type = TYPE_UNKNOW;
        if (fileName == null || "".equals(fileName.trim())) {
            type = TYPE_TEXT;
        } else {
            fileName = fileName.toLowerCase();

            if (fileName.endsWith(".amr") || fileName.endsWith(".wav")) {
                type = TYPE_AUDIO;
            } else if (fileName.contains(".jpg") || fileName.contains(".png")
                    || fileName.contains(".tif") || fileName.contains(".jpeg")
                    || fileName.contains(".gif") || fileName.contains(".ico")
                    || fileName.contains(".cur") || fileName.contains(".xbm")
                    || fileName.contains(".bmp")) {
                type = TYPE_IMAGE;
            } else {
                type = TYPE_UNKNOW;
            }
        }
        return type;
    }

    public void setMessageType(int aType) {
        this.mMessageType = aType;
    }

    public void confirm(Message result) {
        // if(!this.equals(result)){
        // return;
        // }
        this.num = result.num;
        this.time = result.time;
        this.type = result.type;
        this.uid = result.uid;
        this.nick = result.nick;
        this.remark = result.remark;
        this.portrait = result.portrait;
        this.vip = result.vip;
        this.vipsubtype = result.vipsubtype;
        this.level = result.level;
        this.content = result.content;
        this.msgid = result.msgid;

        // 私信附件
        this.attachment_fid = result.attachment_fid;
        this.attachment_sha1 = result.attachment_sha1;
        this.attachment_name = result.attachment_name;
        this.attachment_ctime = result.attachment_ctime;
        this.attachment_ltime = result.attachment_ltime;
        this.attachment_dir_id = result.attachment_dir_id;
        this.attachment_size = result.attachment_size;
        this.attachment_type = result.attachment_type;
        this.attachment_w = result.attachment_w;
        this.attachment_h = result.attachment_h;
        this.attachment_url = result.attachment_url;
        this.attachment_thumbnail = result.attachment_thumbnail;
        this.attachment_virus_scan = result.attachment_virus_scan;
        this.attachment_is_safe = result.attachment_is_safe;
        this.attachment_s3_url = result.attachment_s3_url;

        // 本地额外信息
        // this.attachment_localFilePath;//附件在本地文件系统中的路径

        // this.id;//数据库里主键；
        // this.localMsgID;//本地为这条消息（message）创建的一个id
        if (this.localTime < 0) {
            this.localTime = result.localTime;
        }
        // this.localTime = result.localTime;
        // this.localTime;//本条消息对应的本地时间，需要把服务器的时间映射成本地时间，用于对消息进行排序；
        // this.state;//本条消息的状态。如果是用户本身发送的消息，则状态有：正在发送中，发送失败，发送成功，发送暂停等等。如果是接收到对方的消息，则状态只有：成功
        this.serverConfirmed = Message.SERVER_COMFIRMED;

    }

    /**
     * 用于判断私信是否已经在私信列表中。 规则： 1：接收到的私信，肯定都有msgid，根据msgid判断是不是同一条私信；
     * 2：发送和接收的私信，肯定不是同一条私信； 3：发送的私信对比：
     * 1）如果服务器已经返回发送的结果是成功的，肯定会有msgid，所有发送成功的私信都根据msgid判断是不是同一条私信；
     * 2）如果是发送失败的，肯定没有msgid，所以直接根据localmsgid判断是不是同一条私信。
     * 3）所有发送失败的和发送成功的私信，肯定不是同一条私信；
     * 4）所有正在发送中的私信和发送成功的私信，只能根据内容判断是不是同一条私信，这样不能判断发出去和服务器返回的内容不一致的私信（比如地理位置）
     */
    @Override
    public int hashCode() {
        if (this.content == null) {
            return "".hashCode();
        }
        return this.content.trim().hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Message m = (Message) o;
        if (this.content == null) {
            this.content = "";
        }
        if (m.content == null) {
            m.content = "";
        }
        boolean result = this.content.trim().equals(m.content.trim());
        if (this.type == Message.MESSAGE_RECEIVER
                && m.type == Message.MESSAGE_RECEIVER) {
            return this.msgid.equals(m.msgid);
        } else if (this.type == Message.MESSAGE_RECEIVER
                || m.type == Message.MESSAGE_RECEIVER) {
            return false;
        } else {
            if (!isEmpty(this.msgid) && !isEmpty(m.msgid)) {
                return this.msgid.equals(m.msgid);
            } else if (isEmpty(this.msgid) && isEmpty(m.msgid)) {
                return this.localMsgID.equals(m.localMsgID);
            } else {
                if (this.state == IMMessageEvent.IM_STATE_FAILED
                        || m.state == IMMessageEvent.IM_STATE_FAILED) {
                    return false;
                } else {
                    return result;
                }
            }
        }
    }

    private boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }
}
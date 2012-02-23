package com.sina.weibo.models;

import java.io.Serializable;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sina.weibo.exception.WeiboParseException;

/**
 * 测速日志
 * 
 * @author duncan
 * 
 */
public class SpeedLog extends DataObject implements Serializable {

    private static final long serialVersionUID = 4799817919908922183L;

    /**
     * 开始时间
     */
    public String startTime;

    /**
     * 结束时间
     */
    public String endTime;

    /**
     * 页面大小
     */
    public String pageSize;

    /**
     * url Id
     */
    public String urlId;

    /**
     * 联网方式
     */
    public String lineType;
    /**
     * dns解析时间
     */
    public String dnsTime;
    /**
     * http响应时间
     */
    public String responseTime;
    /**
     * dns解析出来的ip
     */
    public String vip;
    /**
     * 网关ip
     */
    public String gwip;
    /**
     * http状态码
     */
    public String httpcode;
    /**
     * 网络连接时间
     */
    public String linkTime;

    public SpeedLog() {
    }

    public SpeedLog(String xmlStr) throws WeiboParseException {
        super(xmlStr);
    }

    public SpeedLog(XmlPullParser _parser) throws WeiboParseException {
        super(_parser);
    }

    public SpeedLog initFromParser(XmlPullParser _parser)
            throws WeiboParseException {
        parser = _parser;
        return parse();
    }

    @Override
    public SpeedLog initFromString(String xmlStr) throws WeiboParseException {
        try {
            parser.setInput(new StringReader(xmlStr));
        } catch (XmlPullParserException ex) {
            throw new WeiboParseException(ex);
        }
        return parse();
    }

    @Override
    protected SpeedLog parse() throws WeiboParseException {
        // int type;
        // try {
        // while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
        // switch (type) {
        // case XmlPullParser.START_TAG:
        // if (parser.getName().equals("id")) {
        // this.urlId = parseText(parser);
        // } else if (parser.getName().equals("url")) {
        // // this.url = parseText(parser);
        // }
        // break;
        // case XmlPullParser.END_TAG:
        // if (parser.getName().equals("item")) {
        // return this;
        // }
        // break;
        // default:
        // break;
        // }
        // }
        // } catch (XmlPullParserException e) {
        // throw new WeiboParseException(PARSE_ERROR, e);
        // } catch (IOException e) {
        // throw new WeiboParseException(PARSE_ERROR, e);
        // }
        return this;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return new StringBuilder().append("starttime:").append(startTime)
                .append(" endtime:").append(endTime).append(" pagesize:")
                .append(pageSize).append(" lineType:").append(lineType)
                .append(" dnstime:").append(dnsTime).append(" responsetime:")
                .append(responseTime).append(" ip:").append(vip)
                .append(" gwip:").append(gwip).append(" httpcode:")
                .append(httpcode).append(" linktime:").append(linkTime)
                .toString();
    }
}
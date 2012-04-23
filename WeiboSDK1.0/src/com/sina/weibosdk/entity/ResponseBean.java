package com.sina.weibosdk.entity;

import java.io.Serializable;

import com.sina.weibosdk.exception.WeiboParseException;

/**
 * 调用平台api接口后返回一个实体类
 * 
 * @author zhangqi
 * 
 */
public abstract class ResponseBean implements Serializable {

    private static final long serialVersionUID = 1569187328120321525L;

    protected static String PARSE_ERROR = "Problem parsing API response";

    protected static String UNKNOWN_ERROR = "Unknown error";

    public ResponseBean() {
    }

    public ResponseBean(String json) throws WeiboParseException {

    }

    protected abstract ResponseBean parse(String json) throws WeiboParseException;

}

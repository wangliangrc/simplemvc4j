package com.sina.weibosdk.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboIOException;

/**
 * 
 * @author zhangqi
 * 
 */
@SuppressWarnings( "unused" )
public class FormFile {

    /**
     * 输入流形式的文件数据
     */
    private InputStream inputStream;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件字段在form表单里面的名称
     */
    private String formName;

    /**
     * 文件的数据类型
     */
    private String contentType = "application/octet-stream";

    public FormFile(String filePath, String formName) throws WeiboIOException {
        this(filePath, formName, null);
    }

    public FormFile(String filePath, String formName, String contentType) throws WeiboIOException {
//        try {
            this.filePath = filePath;
            this.formName = formName;
            if (!TextUtils.isEmpty(contentType)) {
                this.contentType = contentType;
            }
//            inputStream = new FileInputStream(filePath);
//        } catch (FileNotFoundException e) {
//        	Util.loge(e.getMessage(), e);
//            throw new WeiboIOException(e);
//        }

    }

//    public InputStream getInputStream() {
//        return inputStream;
//    }

//    public void setInputStream(InputStream inputStream) {
//        this.inputStream = inputStream;
//    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}

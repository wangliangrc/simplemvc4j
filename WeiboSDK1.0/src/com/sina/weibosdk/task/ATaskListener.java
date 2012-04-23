package com.sina.weibosdk.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.util.Log;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.exception.WeiboParseException;
import com.sina.weibosdk.parser.JsonParser;

/**
 * Api异步任务回调lisenter
 * 
 * @author zhangqi
 * 
 * @param <T>
 */
public abstract class ATaskListener<T> {

    public abstract void onComplete(ATask task, T bean);

    public abstract void onWeiboException(ATask task, WeiboException exception);

    @SuppressWarnings("unchecked")
    public T parse(String response) throws WeiboParseException {
        Class<?> c = getGenericType();
        String className = c.getName();
        if ("com.sina.weibosdk.entity.StatusList".equals(className)) {
            Log.e("v4", "StatusList native parser");
            return (T) JsonParser.parserStatusList(response);
        } else if ("com.sina.weibosdk.entity.Status".equals(className)) {
            return (T) JsonParser.parserStatus(response);
        } else if ("com.sina.weibosdk.entity.Message".equals(className)) {
            return (T) JsonParser.parserMessage(response);
        } else if ("com.sina.weibosdk.entity.MessageList".equals(className)) {
            return (T) JsonParser.parserMessageList(response);
        } else if ("com.sina.weibosdk.entity.Geo".equals(className)) {
            return (T) JsonParser.parserGeo(response);
        } else if ("com.sina.weibosdk.entity.UserInfo".equals(className)) {
            return (T) JsonParser.parserUserInfo(response);
        } else {
            try {
                Constructor<T> constructor = (Constructor<T>) c
                        .getDeclaredConstructor(String.class);
                T result = constructor.newInstance(response);
                return result;
            } catch (Exception e) {
            	Util.loge(e.getMessage(), e);
                throw new WeiboParseException(e.getMessage());
            }
        }
    }

    /**
     * 获取T的类型
     * 
     * @param index
     * @return
     */
    private Class<?> getGenericType() {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params.length < 1) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[0] instanceof Class)) {
            return Object.class;
        }
        return (Class<?>) params[0];
    }
}

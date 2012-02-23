package com.sina.weibo.models;

public interface IPlatformParam {
    // 返回的xml中platform_params字段中可能出现的参数
    public static final String PARAM_GSID = "gsid";
    public static final String PARAM_FROM = "from";
    public static final String PARAM_WM = "wm";
    public static final String PARAM_UA = "ua";
    public static final String PARAM_C = "c";

    /**
     * 参数串中（逗号分隔的字符串 e.g. "<platform_params>from,wm,gsid,ua</platform_params>"）
     * 是否包含了 key
     * 
     * @param key
     * @return
     */
    public boolean containsParam(String key);
}

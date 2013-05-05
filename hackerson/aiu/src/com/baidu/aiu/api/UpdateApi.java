package com.baidu.aiu.api;

import android.util.Log;

import com.baidu.aiu.api.model.UpdateVersionModel;
import com.baidu.aiu.util.HttpUtils;
import com.baidu.aiu.util.UpdateUtils;

public class UpdateApi {

    public static UpdateVersionModel update(String version, String appName) {
        final String url = "http://" + UpdateUtils.getIp()
                        + ":8001/ht/update.php?ver=" + version + "&ap=" + appName
                        + "&aver=1.0.0";
        Log.d("baidu", "url: \n" + url);

        String cfg = HttpUtils.get(url);

        Log.d("baidu", ">>>>>>>>>>>>>>>>>>>\n" + cfg);

        if ("".equals(cfg) || cfg == null) {
            return null;
        } else {
            return JsonParser.parser(cfg);
        }
    }

}
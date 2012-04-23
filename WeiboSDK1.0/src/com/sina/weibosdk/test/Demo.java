package com.sina.weibosdk.test;

import android.content.Context;
import android.util.Log;

import com.sina.weibosdk.ClientFactory;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.entity.StatusList;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.requestparam.APIRequestParam;
import com.sina.weibosdk.requestparam.RequestParam;
import com.sina.weibosdk.task.ATask;
import com.sina.weibosdk.task.ATaskListener;
import com.sina.weibosdk.task.AsyncRequestTask;

@SuppressWarnings("unused")
public class Demo {

    /**
     * 设置应用中使用的常量
     */
    private void initConfig() {
        WeiboSDKConfig config = WeiboSDKConfig.getInstance();
        config.setProperty(WeiboSDKConfig.KEY_READ_TIMEOUT, 120000);
        config.setProperty(WeiboSDKConfig.KEY_LANG, "zh_cn");
        config.setProperty(WeiboSDKConfig.KEY_API_THREAD_POOL_SIZE, 100);
        config.setProperty(WeiboSDKConfig.KEY_RETRY_COUNT, 0);
        config.setProperty(WeiboSDKConfig.KEY_GSID, "3_5bc09fb156053a89ec1e9172ff505b82b851a9");
        config.setProperty(WeiboSDKConfig.KEY_S, "1d2e51f4");
        config.setProperty(WeiboSDKConfig.KEY_C, "android");
        config.setProperty(WeiboSDKConfig.KEY_UA,
                "HUAWEI-C8500__weibo__2.8.0_beta1__android__android2.1-update1");
        config.setProperty(WeiboSDKConfig.KEY_API_SERVER, "http://202.108.37.212:8000/2/");
        config.setProperty(WeiboSDKConfig.KEY_IS_DEBUG, false);
        config.setProperty(WeiboSDKConfig.KEY_LOG_LEVEL, Log.VERBOSE);
    }

    /**
     * 使用同步进行请求
     */
    public void synRequest(Context ctx) {
        initConfig();
        try {
            StatusList statusList = ClientFactory.getInstance(ctx).createWeiboClient(
            		).getFriendsTimeLine(null, 1, 1, 25, null, null);
        } catch (WeiboException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 使用异步进行请求
     */
    public void asynRequest(Context ctx) {
        initConfig();
        AsyncRequestTask aTask = new AsyncRequestTask(ctx);
        aTask.setMethod("get"); // or aTask.setMethod("post")
        RequestParam param = new APIRequestParam();
        param.addParam("page", "1");
        param.addParam("count", "25");
        aTask.execute("statuses/friends_timeline", param, new StatusListener());

        /**
         * 若不需要，可以中断Task, 从而中断I/O操作
         */
        aTask.cancel();
    }

    private class StatusListener extends ATaskListener<StatusList> {

        @Override
        public void onComplete(ATask task, StatusList bean) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onWeiboException(ATask task, WeiboException exception) {
            // TODO Auto-generated method stub

        }

    }

}

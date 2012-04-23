package com.sina.weibosdk.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sina.weibosdk.AsyncWeiboClient;
import com.sina.weibosdk.ClientFactory;
import com.sina.weibosdk.R;
import com.sina.weibosdk.Util;
import com.sina.weibosdk.WeiboClient;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.entity.Comment;
import com.sina.weibosdk.entity.CommentList;
import com.sina.weibosdk.entity.MessageUserList;
import com.sina.weibosdk.entity.StatusList;
import com.sina.weibosdk.entity.User;
import com.sina.weibosdk.entity.MessageUserList.UserMessage;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.parser.JsonParser;
import com.sina.weibosdk.task.ATask;
import com.sina.weibosdk.task.ATaskListener;

public class WeiboSDKActivity extends Activity implements OnClickListener {

    Button sendBtn;
    TextView request;
    TextView response;
    WeiboClient client;
    AsyncWeiboClient asyncClient;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sendBtn = (Button) findViewById(R.id.send);
        request = (TextView) findViewById(R.id.request);
        response = (TextView) findViewById(R.id.response);
        sendBtn.setOnClickListener(this);

        WeiboSDKConfig config = WeiboSDKConfig.getInstance();
        config.setProperty(WeiboSDKConfig.KEY_C, "android");
        config.setProperty(WeiboSDKConfig.KEY_S, "40fa4056");// login时"8f63c64b"
        config.setProperty(WeiboSDKConfig.KEY_UA, "zq");
        config.setProperty(WeiboSDKConfig.KEY_GSID, "3_5bc65b3b002f3db249d0286183b15fc7b145be6858fc09");
        config.setProperty(WeiboSDKConfig.KEY_IS_DEBUG, true);
        client = ClientFactory.getInstance(this).createWeiboClient();
        asyncClient = ClientFactory.getInstance(this).createAsyncWeiboClient();
    }

    @Override
    public void onClick(View v) {
        if (v == sendBtn) {
            // try {
            // // User user = client
            // // .Login("251400691@qq.com",
            // //
            // "SXzwoWp8YKCAYhNAKTPPRKH8l+P1OUKzpSMqVQ0qsRYIkvP4EIbGGGPWjXAcEdY1Dm07rc4pRke2ZnWOYkDhbwIMW3ihrKk4FHGyY1PzrMW5MpcgSOtu7vM+USe/XtCbjzkIdExrwe68d0c6UN8afqFrCttKRLIF8t+gm6JELqI=",
            // // true);
            //
            // // response.setText("Response: " + user.getScreen_name());
            //
            // // String resp = client.StatusUpload("test5", 0, 0,
            // // "sdcard/abc.jpg");
            // //
            // // response.setText(resp);
            //
            // StatusReqParam param = new StatusReqParam();
            // param.setStatus("test12");
            // param.setLat(0);
            // param.setLon(0);
            // param.addFile("sdcard/abc.jpg");
            // StringBuilder sb = new StringBuilder();
            // sb.append("http://api.new.weibo.cn/").append("statuses/upload?").append("gsid=")
            // .append(param.getGsid());
            // RequestTask<User> task = new RequestTask<User>(this,
            // sb.toString(),
            // param.getParams(), param.getFiles(), new uploadListener());
            // task.setRequestMethod("POST");
            // // task.excute();
            //
            // Bundle b = new Bundle();
            // b.putString("client_id", "1646212960");
            // b.putString("response_type", "token");
            // b.putString("redirect_uri", "http://www.sina.com");
            // b.putString("display", "mobile");
            // String ret =
            // client.httpGet("https://api.weibo.com/oauth2/authorize", b);
            // response.setText(ret);
            // } catch (WeiboIOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // } catch (WeiboApiException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // } catch (WeiboParseException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

            // RequestParam param2 = new APIRequestParam();
            // try {
            // param2.addFile("sdcard/abc.jpg");
            // param2.addParam("status", "goodday");
            //
            // } catch (WeiboIOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            //
            // final RequestTask<User> task2 = new RequestTask<User>(this,
            // "http://202.108.37.212:8000/2/statuses/upload", param2, new
            // UserListener());
            // task2.setMethod("POST");
            // ThreadPool.getInstance().addTask(task2);

            // RequestParam param2 = new APIRequestParam();
            // param2.addParam("status", "空气质量差zzz56");
            // try {
            // param2.addFile("sdcard/abc.jpg");
            // } catch (WeiboIOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // AsyncRequestTask task = new AsyncRequestTask(this, 1000);
            // task.setMethod("POST");
            // task.execute("http://202.108.37.212:8000/2/statuses/upload",
            // param2, new UserListener());
            // ATask task = asyncClient.getFriendsTimeLineTask(null, 1, 50,
            // null, null,
            // new StatusListener());

        	try {
//				client.uploadStatus("234324https121xcx12333testffffff" + System.nanoTime(), "sdcard/icon.png", 0, 0, 0, null, null);
//				client.updateStatus("test33" + System.nanoTime(), 0, 0, 0, null, 1, null, null);
//        		double lon = 116.304034;
//        		double lat = 39.9831892;
////        		client.getNearByUsers(lat, lon, 0, 0, 25, 0, 0, 0, 0);
//        		client.getNearByPosition(lat, lon, 0, null, 0, 25, 0, 0);
//        		client.getUsersShow(null, "来去之间");
//        		client.getFriendsTimeLine(null, 1, 25, null, null);
//        		client.getCommentsToMe(1, 25, null, null, 0);
//        		client.createMessage("test" + System.nanoTime(), 
//        				"1819954987", null, "66950332", null, 0, null, null);
//        		client.getFavorites(1, 50, 240);
//        		client.getNearByUsers(lat, lon, 0, 0, 0, 0, 0, 0, 0);
//        		MessageUserList mul = client.getMessageUserList(0, 0, 0, 0);
//        		Log.e("weibosdk", "total : " + mul.getTotal_number());
//        		for (UserMessage um : mul.getUserMessageList()) {
//        			Log.e("weibosdk", "unread : " + um.getUnreadNum());
//        		}
//        		client.getMessageList(null, 0, 25, null, null, 0);
//        		client.getNewestMessage(0, 25, null, null, 0);
//        		client.getHourlyTopicList(true, 25);
        		try {
        		File f = new File("/sdcard/weibosdk_log.txt");
        		FileInputStream fis = new FileInputStream(f);
        		byte[] buf = new byte[(int)f.length()]; 
        		fis.read(buf);
        		
        		new CommentList(new String(buf));
        		} catch (IOException e) {
        			Util.loge(e.getMessage(), e);
        		}
        		
        	} catch (WeiboException e) {
        		Util.loge(e.getMessage(), e);
			}
        	
        }
    }

    class UserListener extends ATaskListener<User> {

        @Override
        public void onComplete(ATask task, final User bean) {
            runOnUiThread(new Runnable() {
                public void run() {
                    response.setText(bean.getScreen_name() + " test");
                }
            });

        }

        @Override
        public void onWeiboException(ATask task, WeiboException exception) {
            // TODO Auto-generated method stub

        }

    }

    class uploadListener extends ATaskListener<User> {

        @Override
        public void onComplete(ATask task, User bean) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onWeiboException(ATask task, WeiboException exception) {
            // TODO Auto-generated method stub

        }

    }

    class StatusListener extends ATaskListener<StatusList> {

        @Override
        public void onComplete(ATask task, final StatusList bean) {
            runOnUiThread(new Runnable() {
                public void run() {
                    response.setText(bean.getStatusList().get(0).getText());
                }
            });

        }

        @Override
        public void onWeiboException(ATask task, WeiboException exception) {
            // TODO Auto-generated method stub

        }

    }

}
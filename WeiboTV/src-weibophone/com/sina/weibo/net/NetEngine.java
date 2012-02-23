package com.sina.weibo.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.sina.weibo.Constants;
import com.sina.weibo.StaticInfo;
import com.sina.weibo.Utils;
import com.sina.weibo.WeiboApplication;
import com.sina.weibo.exception.WeiboApiException;
import com.sina.weibo.exception.WeiboIOException;
import com.sina.weibo.exception.WeiboParseException;
import com.sina.weibo.models.AdList;
import com.sina.weibo.models.BlackList;
import com.sina.weibo.models.CommentList;
import com.sina.weibo.models.CommentMessageList;
import com.sina.weibo.models.FanList;
import com.sina.weibo.models.FavHotWordList;
import com.sina.weibo.models.GroupList;
import com.sina.weibo.models.HotWordList;
import com.sina.weibo.models.LocationList;
import com.sina.weibo.models.MBlogCRNum;
import com.sina.weibo.models.MBlogCRNumList;
import com.sina.weibo.models.MBlogList;
import com.sina.weibo.models.MessageList;
import com.sina.weibo.models.MessageSendResult;
import com.sina.weibo.models.NearByBlogList;
import com.sina.weibo.models.NearByUserInfoList;
import com.sina.weibo.models.NetResult;
import com.sina.weibo.models.SkinList;
import com.sina.weibo.models.SpeedLog;
import com.sina.weibo.models.SquareItemList;
import com.sina.weibo.models.UnreadNum;
import com.sina.weibo.models.UrlItemList;
import com.sina.weibo.models.User;
import com.sina.weibo.models.UserInfo;
import com.sina.weibo.models.UserInfoList;
import com.sina.weibo.models.VersionInfo;
import com.sina.weibo.net.NetUtils.APNWrapper;
import com.sina.weibo.net.RPCHelper.HttpException;

class NetEngine implements INetEngine {

    private Context mContext;

    /**
     * 微博api地址
     */
    private static final String SERVER = "http://api.weibo.cn/interface/f/ttt/v3/";
    private static final String SERVER_V4 = "http://api.weibo.cn/interface/f/ttt/v4/";

    /**
     * 内部测试api地址
     */
    private static final String INTERNAL_SERVER = "http://202.108.37.212/interface/f/ttt/v3/";
    private static final String INTERNAL_SERVER_V4 = "http://202.108.37.212/interface/f/ttt/v4/";

    /**
     * 内部测试api地址2
     */
    private static final String INTERNAL_SERVER_2 = "http://202.108.5.62/interface/f/ttt/v3/";

    private static final String SERVER_AD = "http://wbapp.mobile.sina.cn/interface/f/ttt/v3/";

    private static int BUFFER_SIZE = 4096;

    private NetEngine(Context context) {
        mContext = context;
    }

    private static NetEngine mNetInstance;

    // private static byte[] sBuffer = new byte[512];

    public static synchronized NetEngine getNetInstance(Context context) {
        if (mNetInstance == null) {
            mNetInstance = new NetEngine(context);
        }
        return mNetInstance;
    }

    private APNWrapper getApnWrapper() {
        return NetUtils.getAPN(mContext);
    }

    public User login(String username, String password)
            throws WeiboParseException, WeiboIOException, WeiboApiException {
        String pwd = "";
        try {
            pwd = new RsaKey().encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User u = null;
        Bundle b = new Bundle();
        b.putString("flag", "1");// encode password by using rsa
        b.putString("u", username);
        b.putString("p", pwd);
        b.putString("c", Constants.CID);
        b.putString("s", calculateS(username + password));
        b.putString("ua", URLEncoder.encode(generateUA(mContext)));
        String url = String.format("%slogin.php?&wm=%s&from=%s&c=%s&ua=%s",
                NetEngine.SERVER, Constants.WM, Constants.FROM, Constants.CID,
                generateUA(mContext));
        String response = NetUtils.openUrl(url, NetUtils.METHOD_POST, b,
                mContext);
        u = new User(response);
        return u;
    }

    public User login(User user) throws WeiboParseException, WeiboIOException,
            WeiboApiException {
        User u = null;
        Bundle b = new Bundle();
        b.putString("gsid", user.gsid);
        b.putString("c", Constants.CID);
        b.putString("s", calculateS(user.uid));
        b.putString("from", Constants.FROM);
        b.putString("wm", Constants.WM);
        b.putString("ua", URLEncoder.encode(generateUA(mContext)));
        String url = String.format("%slogin.php", NetEngine.SERVER);
        String response = NetUtils.openUrl(url, NetUtils.METHOD_GET, b,
                mContext);
        u = new User(response);
        return u;
    }

    // public String[] getAuthCode() throws NetException, WeiboIOException{
    // String[] rlt = null;
    // String url = "http://3g.sina.com.cn/interface/f/captcha/get.php";
    // // String response = NetUtils.openUrl(url, NetUtils.METHOD_GET, null,
    // mContext);
    // // rlt = Parser.parseAuthInfo(response);
    // return rlt;
    // }

    public User regist(String username, String password, String question,
            String answer, String cpt, String code, String nick, String gender,
            String uuid) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        User u = null;
        Bundle b = new Bundle();
        b.putString("u", username);
        b.putString("p", password);
        b.putString("q", question);
        b.putString("a", answer);
        b.putString("cpt", cpt);
        b.putString("code", code);
        b.putString("nick", nick);
        b.putString("gender", gender);
        if (!TextUtils.isEmpty(uuid)) {
            b.putString("uuid", uuid);
        }
        String s = getAppKey(username, password);
        String url = String.format("%sreg.php?s=%s&c=%s&from=%s&wm=%s",
                NetEngine.SERVER, s, Constants.CID, Constants.FROM,
                Constants.WM);

        String response = NetUtils.openUrl(url, NetUtils.METHOD_GET, b,
                mContext);
        u = new User(response);
        return u;
    }

    // public WhiteListUser registByWhiteList(){
    // WhiteListUser u = null;
    //
    // return u;
    // }

    private String getAppKey(String usrname, String password) {
        char[] tmp = MD5.hexdigest(usrname + password + Constants.KEY)
                .toCharArray();
        char[] atemp = MD5.hexdigest(usrname + WeiboApplication.IMEI_NUM)
                .toCharArray();
        StringBuilder am = new StringBuilder();
        am = am.append(atemp[1]).append(atemp[2]).append(atemp[3])
                .append(atemp[4]).append(atemp[5]).append(atemp[6]);
        StringBuilder m = new StringBuilder();
        m = m.append(tmp[1]).append(tmp[5]).append(tmp[2]).append(tmp[10])
                .append(tmp[17]).append(tmp[9]).append(tmp[25]).append(tmp[27]);
        return m.toString();
    }

    /**
     * Get someone's information.
     * 
     * @param u
     *            User 当前用户
     * @param uid
     *            someone's id, can be null
     * @param nick
     *            someone's nickname, can be null
     * @return UserInfo
     * @throws WeiboIOException
     * @throws WeiboApiException
     */
    public UserInfo getUserInfo(User u, String uid, String nick)
            throws WeiboParseException, WeiboIOException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getuserinfo.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        if (uid != null) {
            params.putString("uid", uid);
        } else if (nick != null) {
            params.putString("nick", nick);
        }
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);

        UserInfo ui = new UserInfo(content);
        return ui;
    }

    /**
     * Search users
     * 
     * @param u
     *            user.
     * @param keyword
     *            what you want to search.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of items in the page, default is 10.
     * @return UserInfoList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public UserInfoList searchUser(User u, String keyword, int page,
            int pagesize) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("searchuser.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("keyword", keyword);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pagesize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        UserInfoList uList = new UserInfoList(content);
        return uList;
    }

    /**
     * get at suggestion users
     * 
     * @param u
     *            user.
     * @param keyword
     *            what you want to search.
     * @return UserInfoList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public UserInfoList getAtSuggestion(User u, String keyword)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("atuser.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("q", keyword);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        UserInfoList uList = new UserInfoList(content);
        return uList;
    }

    /**
     * 获取推荐用户 Top users
     * 
     * @param uid
     *            the public user id.
     * @param pagesize
     *            the number of items in the page, default is 10.
     * @return UserInfoList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public UserInfoList getTopUserList(String uid, int pagesize)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("gettopuser.php");

        Bundle params = new Bundle();
        params.putString("pagesize", "" + pagesize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        UserInfoList uList = new UserInfoList(content);
        return uList;
    }

    /**
     * get users you may like
     * 
     * @param User
     * @return UserInfoList
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public UserInfoList getGuessUserList(User u) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("guess.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("lastmblog", "1");
        params.putString("uid", u.uid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        UserInfoList uList = new UserInfoList(content);
        return uList;
    }

    /**
     * 获取 名人榜，人气草根榜
     * 
     * @param User
     *            u
     * @param cat
     *            1为名人榜,2为人气草根榜
     * @param page
     * @param pageSize
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public UserInfoList getUserRankList(User u, int cat, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        Bundle params = new Bundle();
        if (Utils.isEnPlatform(mContext)) {
            url.append(NetEngine.SERVER);
            url.append("list.php");
            params.putString("listid", "174855182");
        } else {
            url.append(NetEngine.SERVER);
            url.append("getuserrank.php");
            params.putString("cat", "" + cat);
        }
        params.putString("gsid", u.gsid);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pageSize);
        params.putString("uid", u.uid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        UserInfoList uList = new UserInfoList(content);
        return uList;
    }

    /**
     * Edit the user information.
     * 
     * @param u
     *            : User
     * @param gender
     *            : boy or girl? @see com.sina.weibo.Constants
     * @param rename
     *            : whether user auto modify by server if duplicate
     * @param nick
     *            : user nick name
     * @param intro
     *            : a introduce for a special suer
     * @return NetResult: success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult editUserInfo(User u, int gender, String nick,
            String intro, String portraitFile, int rename)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        url.append(String.format(
                "%sedituserinfo.php?gsid=%s&wm=%s&from=%s&c=%s&s=%s&ua=%s",
                NetEngine.SERVER, u.gsid, Constants.WM, Constants.FROM,
                Constants.CID, calculateS(u.uid),
                URLEncoder.encode(generateUA(mContext))));
        Bundle params = new Bundle();
        if (!TextUtils.isEmpty(nick)) {
            params.putString("nick", nick);
        }
        if (intro != null) {
            params.putString("intro", intro);
        }
        params.putString("gender", String.valueOf(gender));
        params.putString("s", calculateS(u.uid));
        params.putString("rename", String.valueOf(rename));
        params.putString("ua", generateUA(mContext));
        if (!TextUtils.isEmpty(portraitFile)) {
            File f = new File(portraitFile);
            if (f.exists()) {
                Bundle filesPath = new Bundle();
                filesPath.putString("portrait", portraitFile);
                params.putParcelable(NetUtils.TYPE_FILE_NAME, filesPath);
            }
        }
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_POST,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * 取得黑名单列表
     * 
     * @param u
     * @param page
     * @param pageSize
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public BlackList fetchBlackList(User u, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("blackuser.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("uid", "");
        params.putString("act", "list");
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pageSize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        return new BlackList(content);
    }

    /**
     * 将 uid 表示的用户从黑名单中删除
     * 
     * @param u
     * @param uid
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     * @throws HttpException
     */
    public NetResult delFromBlackList(User u, String uid)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("blackuser.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("uid", uid);
        params.putString("act", "del");
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * 将 uid 表示的用户添加到黑名单中
     * 
     * @param u
     * @param uid
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     * @throws HttpException
     */
    public NetResult addToBlackList(User u, String uid)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("blackuser.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("uid", uid);
        params.putString("act", "add");
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * Post a new mblog with or without a picture.
     * 
     * @param u
     *            user.
     * @param content
     *            the content of the mblog.
     * @param picpath
     *            the path of picture. If it is a null, then no picture will be
     *            uploaded.
     * @param latitude
     *            经度
     * @param longitude
     *            纬度
     * @param isLocated
     *            是否有定位信息
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult postNewMBlog(User u, String content, double latitude,
            double longitude, String picpath, String poiid, String poititle,
            String xid, boolean offset, boolean isLocated)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        url.append(String.format(
                "%sdealmblog.php?gsid=%s&wm=%s&from=%s&c=%s&ua=%s",
                NetEngine.SERVER, u.gsid, Constants.WM, Constants.FROM,
                Constants.CID, generateUA(mContext)));

        Bundle params = new Bundle();
        params.putString("content", content);
        if (isLocated) {
            params.putString("long", String.valueOf(longitude));
            params.putString("lat", String.valueOf(latitude));
            params.putString("offset", offset ? "1" : "0");
            if (poiid != null) {
                params.putString("poiid", poiid);
                params.putString("poititle", poititle);
                params.putString("xid", xid);
            }
        }
        params.putString("act", "add");
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        if (!TextUtils.isEmpty(picpath)) {
            File f = new File(picpath);
            if (f.exists()) {
                Bundle filesPath = new Bundle();
                filesPath.putString("pic", picpath);
                params.putParcelable(NetUtils.TYPE_FILE_NAME, filesPath);
            }
        }
        String xmlResult = NetUtils.openUrl(url.toString(),
                NetUtils.METHOD_POST, params, mContext);
        NetResult result = new NetResult(xmlResult);
        return result;
    }

    /**
     * Forward a mblog.
     * 
     * @param u
     *            user.
     * @param mblogId
     *            the id of mblog.
     * @param mblogUid
     *            the uid of the mblog author.
     * @param reason
     *            why forward it? (can be null)
     * @param content
     *            the content
     * @return NetResult
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult forwardMBlog(User u, String mblogId, String mblogUid,
            String reason, String content, boolean isComment)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        url.append(String.format(
                "%sdealmblog.php?gsid=%s&wm=%s&from=%s&c=%s&ua=%s",
                NetEngine.SERVER, u.gsid, Constants.WM, Constants.FROM,
                Constants.CID, generateUA(mContext)));

        Bundle params = new Bundle();
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("act", "dort");
        params.putString("id", mblogId);
        params.putString("mbloguid", mblogUid);
        params.putString("content", content);
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        params.putString("id", mblogId);
        params.putString("mbloguid", mblogUid);
        if (reason == null) {
            params.putString("rtkeepreason", "0");
        } else {
            params.putString("rtkeepreason", "1");
            params.putString("rtreason", reason);
        }
        if (isComment) {
            params.putString("cmt", "1");
        }

        String xmlResult = NetUtils.openUrl(url.toString(),
                NetUtils.METHOD_POST, params, mContext);
        NetResult result = new NetResult(xmlResult);
        return result;
    }

    /**
     * Delete a Mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of mblog.
     * @param act
     *            do delete
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteMBlog(User u, String mblogid)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealmblog.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("id", mblogid);
        params.putString("act", "dodel");
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String xmlResult = NetUtils.openUrl(url.toString(),
                NetUtils.METHOD_GET, params, mContext);
        NetResult result = new NetResult(xmlResult);
        return result;
    }

    /**
     * Get home blog list.
     * 
     * @param u
     *            current user.
     * @param gid
     *            group id.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param maxid
     *            the max mblog id in the list you will get
     * @param pageSize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList.
     * 
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getHomeBlogList(User u, String gid, int picsize,
            String maxId, int pageSize) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("gettimeline.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("picsize", "" + picsize);
        params.putString("pagesize", "" + pageSize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("thm", generateThm(mContext));
        params.putString("ua", generateUA(mContext));
        if (maxId != null) {
            params.putString("maxid", maxId);
        }
        if (gid != null && !gid.trim().equals("")) {
            params.putString("gid", gid);
        }

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MBlogList mbList = new MBlogList(content);
        return mbList;
    }

    private String generateThm(Context mContext) {
        StringBuilder builder = new StringBuilder();
        Theme theme = Theme.getInstance(mContext);
        String readMode;
        int mode = mContext.getSharedPreferences(StaticInfo.READMODE, 0)
                .getInt(StaticInfo.READMODE, StaticInfo.READ_DEF);
        switch (mode) {
            case StaticInfo.READ_MODE_0:
                readMode = "thumbnail";
                break;
            case StaticInfo.READ_MODE_1:
                readMode = "classic";
                break;
            case StaticInfo.READ_MODE_2:
                readMode = "textonly";
                break;
            default:
                readMode = "thumbnail";
                break;
        }
        if (!android.text.TextUtils.isEmpty(theme.getName())) {
            builder.append(theme.getName()).append("_")
                    .append(theme.getVersion());
        } else {
            builder.append("default").append("_")
                    .append(Utils.getVersionCode(mContext));
        }
        builder.append("_");
        builder.append(readMode);
        return builder.toString();

    }

    /**
     * Get someone's blog list.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's id
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList.
     * 
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getUserMBlogList(User u, String uid, int picsize,
            int page, int pageSize) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getusermbloglist.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("uid", uid);
        params.putString("picsize", "" + picsize);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pageSize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MBlogList mbList = new MBlogList(content);
        return mbList;
    }

    /**
     * Get a mblog.
     * 
     * @param u
     *            user.
     * @param mid
     *            the blog's id
     * @param picsize
     *            the size of picture which the user prefer.
     * @return MBlogList this List contains only one element.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getSingleMBlog(User u, String mid, int picsize)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getsinglemblog.php");
        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("mid", mid);
        params.putString("picsize", "" + picsize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MBlogList mbList = new MBlogList(content);
        return mbList;
    }

    /**
     * Search mblogs 根据关键词搜索微博
     * 
     * @param u
     *            user.
     * @param keyword
     *            what you want to search.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList searchMBlog(User u, String keyword, int picsize, int page,
            int pagesize) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("searchmblog.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("keyword", keyword);
        params.putString("picsize", "" + picsize);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pagesize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MBlogList mbList = new MBlogList(content);
        return mbList;
    }

    /**
     * Get @who blog list.
     * 
     * @param u
     *            user.
     * @param uid
     *            user uid. null means it's me.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     * 
     */
    public MBlogList getAtMsgList(User u, String uid, int picsize, int page,
            int pagesize) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("msg.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "0");
        params.putString("picsize", "" + picsize);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pagesize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        if (uid != null) {
            params.putString("uid", uid);
        }

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MBlogList mbList = new MBlogList(content);
        return mbList;
    }

    /**
     * Get "look around" / "hot foward" list.
     * 
     * @param uid
     *            public uid.
     * @param cat
     *            1 for "look around", 2 for "hot forward"
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of \@me mblogs,
     *         and array[1] is ArrayList of mblogs.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getNewsList(String uid, String cat, int picsize, int page,
            int pagesize) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getnewslist.php");

        Bundle params = new Bundle();
        params.putString("cat", cat);
        params.putString("picsize", "" + picsize);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pagesize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MBlogList mbList = new MBlogList(content);
        return mbList;
    }

    /**
     * Get my favorite mblogs.
     * 
     * @param u
     *            user.
     * @param picsize
     *            the size of picture which the user prefer.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of mblogs in the page, default is 10.
     * @return MBlogList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MBlogList getFavMBlogList(User u, int picsize, int page, int pagesize)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getfavmblog.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("picsize", "" + picsize);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pagesize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MBlogList mbList = new MBlogList(content);
        return mbList;
    }

    /**
     * Add a favorite mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of the mblog
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult addFavMBlog(User u, String id) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealfavmblog.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "0");
        params.putString("mid", id);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * Delete a favorite mblog.
     * 
     * @param u
     *            user.
     * @param mid
     *            the id of the mblog
     * @return NetResult success or not
     * 
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteFavMBlog(User u, String id, String mid)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealfavmblog.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "1");
        params.putString("id", id);
        params.putString("mid", mid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * Get comments list about a mblog.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's id.
     * @param srcid
     *            the id of mblog.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pagesize
     *            the number of comments in the page, default is 10.
     * @return CommentList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public CommentList getAttCommentList(User u, String srcuid, String srcid,
            int page, int pagesize) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getattcommentlist.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("srcuid", srcuid);
        params.putString("srcid", srcid);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pagesize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        CommentList cmtList = new CommentList(content);
        return cmtList;
    }

    /**
     * Add a comment for a mblog. In this case, cmtuid and cmtid in the comment
     * is useless.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param content
     *            comment
     * @param rt
     *            post a new MBlog or not
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult addComment(User u, String srcuid, String srcid,
            String content, boolean rt) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        url.append(String.format(
                "%sdealcomment.php?gsid=%s&from=%s&wm=%s&c=%s&ua=%s",
                Constants.SERVER, u.gsid, Constants.FROM, Constants.WM,
                Constants.CID, generateUA(mContext)));

        Bundle params = new Bundle();
        params.putString("act", "add");
        params.putString("content", content);
        params.putString("srcid", srcid);
        params.putString("srcuid", srcuid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        if (rt) {
            params.putString("rt", "1");
        }

        String xmlStr = NetUtils.openUrl(url.toString(), NetUtils.METHOD_POST,
                params, mContext);
        NetResult result = new NetResult(xmlStr);
        return result;
    }

    /**
     * Reply a comment for a comment. In this case, cmtuid and cmtid in the
     * comment must be set.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param cmtuid
     *            comment author's uid
     * @param cmtid
     *            the id of the comment
     * @param content
     *            comment
     * @param rt
     *            post a new MBlog or not
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult replyComment(User u, String srcuid, String srcid,
            String cmtuid, String cmtid, String content, boolean rt)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        url.append(String.format(
                "%sdealcomment.php?gsid=%s&from=%s&wm=%s&c=%s&ua=%s",
                Constants.SERVER, u.gsid, Constants.FROM, Constants.WM,
                Constants.CID, generateUA(mContext)));

        Bundle params = new Bundle();
        params.putString("act", "addReply");
        params.putString("content", content);
        params.putString("srcid", srcid);
        params.putString("srcuid", srcuid);
        params.putString("cmtid", cmtid);
        params.putString("cmtuid", cmtuid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        if (rt) {
            params.putString("rt", "1");
        }

        String xmlStr = NetUtils.openUrl(url.toString(), NetUtils.METHOD_POST,
                params, mContext);
        NetResult result = new NetResult(xmlStr);
        return result;
    }

    /**
     * Delete a comment. In this case, cmtuid and cmtid in the comment must be
     * set.
     * 
     * @param u
     *            user.
     * @param srcuid
     *            mblog author's uid
     * @param srcid
     *            the id of the mblog
     * @param cmtuid
     *            comment author's uid
     * @param cmtid
     *            the id of the comment
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteComment(User u, String srcuid, String srcid,
            String cmtuid, String cmtid) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealcomment.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "delc");
        params.putString("srcuid", srcuid);
        params.putString("srcid", srcid);
        params.putString("cmtid", cmtid);
        params.putString("cmtuid", cmtuid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * Get my comment blog list.
     * 
     * @param u
     *            user.
     * @param boxtype
     *            1 is inbox, 2 is outbox
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of comment message in the page, default is 10.
     * @return CommentMessageList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public CommentMessageList getMyCommentList(User u, int boxtype, int page,
            int pageSize) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getmycomment.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("type", "" + boxtype);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pageSize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        CommentMessageList result = new CommentMessageList(content);
        return result;
    }

    /**
     * Send a private message.
     * 
     * @param u
     *            user.
     * @param nick
     *            someone's nick
     * @param content
     *            the content of the message.
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult sendMessage(User u, String nick, String content)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        url.append(String.format(
                "%sdealmsg.php?gsid=%s&from=%s&wm=%s&c=%s&ua=%s",
                Constants.SERVER, u.gsid, Constants.FROM, Constants.WM,
                Constants.CID, generateUA(mContext)));

        Bundle params = new Bundle();
        params.putString("content", content);
        params.putString("act", "send");
        params.putString("nick", nick);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String xmlStr = NetUtils.openUrl(url.toString(), NetUtils.METHOD_POST,
                params, mContext);
        NetResult result = new NetResult(xmlStr);
        return result;
    }

    /**
     * Send a private message with an attachment
     * 
     * @param u
     *            user.
     * @param uid
     *            recipient's uid
     * @param nick
     *            recipient's nick
     * @param content
     *            the content of the message.
     * @param fid
     *            attachment file id (sender's fid,recipient's fid in
     *            VDisk),format => afid,bfid
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MessageSendResult sendMessage(User u, String uid, String nick,
            String content, String fid) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        url.append(String.format(
                "%ssendmessage.php?gsid=%s&from=%s&wm=%s&c=%s&ua=%s",
                NetEngine.SERVER, u.gsid, Constants.FROM, Constants.WM,
                Constants.CID, generateUA(mContext)));

        Bundle params = new Bundle();
        params.putString("fuid", uid);
        if (nick != null) {
            params.putString("fnick", nick);
        }
        params.putString("uid", u.uid);
        params.putString("content", content);

        if (fid != null && !"".equals(fid.trim())) {
            params.putString("fid", fid);
        }

        params.putString("gsid", u.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String xmlStr = NetUtils.openUrl(url.toString(), NetUtils.METHOD_POST,
                params, mContext);
        MessageSendResult result = new MessageSendResult(xmlStr);
        return result;
    }

    /**
     * Get private messages list. If the uid is null, this function will get all
     * messages come from all other users, each user has only one record to tell
     * you how many messages who send to the user. If the uid is not null, it
     * will get messages only come from who you specified.
     * 
     * @param u
     *            user.
     * @param uid
     *            the user's uid.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of message in the page, default is 10.
     * @return MessageList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MessageList getMessageList(User u, String uid, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("msg.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "1");
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pageSize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        if (uid != null && !"".equals(uid.trim()) && !uid.trim().equals(u.uid)) {
            params.putString("uid", uid);
        }

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MessageList result = new MessageList(content);
        return result;
    }

    /**
     * Get private messages list. If the uid is null, this function will get all
     * messages come from all other users, each user has only one record to tell
     * you how many messages who send to the user. If the uid is not null, it
     * will get messages only come from who you specified.
     * 
     * @param u
     *            user.
     * @param uid
     *            the user's uid.
     * @param since_id
     *            the msgid, get messages whose msgid is bigger than since_id
     * @param max_id
     *            the msgid, get messages whose msgid is smaller than max_id
     * @param count
     *            the number of message , default is 20.
     * @return MessageList.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public MessageList getMessageList(User u, String uid, String since_id,
            String max_id, int count) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("msg.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "2");
        if (since_id != null && !"".equals(since_id.trim())) {
            params.putString("since_id", since_id);
        }
        if (max_id != null && !"".equals(max_id.trim())) {
            params.putString("max_id", max_id);
        }
        if (count > 0) {
            params.putString("count", count + "");
        }
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        if (uid != null && !"".equals(uid.trim()) && !uid.trim().equals(u.uid)) {
            params.putString("uid", uid);
        }

        StringBuilder url1 = new StringBuilder();
        url1.append(String
                .format("%smsg.php?gsid=%s&from=%s&wm=%s&c=%s&s=%s&ua=%s&act=2&since_id=%s&max_id=%s&count=%s&uid=%s",
                        NetEngine.SERVER, u.gsid, Constants.FROM, Constants.WM,
                        Constants.CID, calculateS(u.uid), generateUA(mContext),
                        since_id, max_id, count + "", uid));
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);

        MessageList result = new MessageList(content);
        return result;
    }

    /**
     * 删除某条私信
     * 
     * @param u
     *            当前账户信息
     * @param uid
     *            对方的UID
     * @param msgId
     *            私信ID
     * @return NetResult 删除是否成功
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteOneMessage(User u, String uid, String msgId)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealmsg.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "delmsg");
        params.putString("uid", uid);
        params.putString("msgid", msgId);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * Deleta all private messages come from or sent to someone.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult deleteMessages(User u, String uid)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealmsg.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "delchatc");
        params.putString("uid", uid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * Get attention/fans list.
     * 
     * @param u
     *            user.
     * @param cat
     *            list type, 1 for fans' list, 0 for attention list.
     * @param uid
     *            someone's id
     * @param sort
     *            sort method, 1 for sorting by attention time, 2 for sorting by
     *            capital, 3 for sorting by update time. Only available when cat
     *            is 1.
     * @param keyword
     *            nickname filter, can be null.
     * @param lastmblog
     *            whether contains the last mblog. 0 for no, 1 for yes.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of mblogs in the page, default is 10.
     * @return a two element array. array[0] is the total count of mblogs, and
     *         array[1] is ArrayList of attentions or fans.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public FanList getAttentionList(User u, int cat, String uid, int sort,
            String keyword, int lastmblog, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("attention.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("cat", "" + cat);
        params.putString("sort", "" + sort);
        params.putString("lastmblog", "" + lastmblog);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pageSize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        if (uid != null) {
            params.putString("uid", uid);
        }
        if (keyword != null) {
            params.putString("keyword", keyword);
        }

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        FanList result = new FanList(content);
        return result;
    }

    /**
     * Add an attention.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult addAttention(User u, String uid) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealatt.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "1");
        params.putString("uid", uid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * Cancel an attention.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult cancelAttention(User u, String uid)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealatt.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "2");
        params.putString("uid", uid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * Get favorite hot word list.
     * 
     * @param u
     *            user.
     * @param uid
     *            someone's uid, null for current user.
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of words in the page, default is 10.
     * @return FavHotWordList
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public FavHotWordList getFavHotWordList(User u, String uid, int page,
            int pageSize) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getfavhotword.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pageSize);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        if (uid != null) {
            params.putString("uid", uid);
        }
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        FavHotWordList result = new FavHotWordList(content);
        return result;
    }

    /**
     * Add/Delete a Mblog.
     * 
     * @param u
     *            user.
     * @param id
     *            the id of mblog.
     * @param act
     *            act=0->add act=1->remove
     * @return NetResult success or not
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult dealTopic(User u, int act, String fav)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealhotword.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("act", "" + act);
        params.putString("value", fav);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * get billboard of the hot word
     * 
     * @param u
     *            current user
     * @param type
     *            0=> recent 1 hour 2=> today 3=> this week
     * @param page
     *            the index of current page in pages, 1 is the first page.
     * @param pageSize
     *            the number of words in the page, default is 10.
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public HotWordList getHotWordList(User u, int type, int page, int pageSize)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("gethotword.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("page", "" + page);
        params.putString("pagesize", "" + pageSize);
        params.putString("type", "" + type);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        HotWordList result = new HotWordList(content);
        return result;
    }

    /**
     * Get unread numbers.
     * 
     * @param u
     *            user
     * @return UnreadNum.
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public UnreadNum getUnreadNum(User u) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getunreadnum.php");

        Bundle params = new Bundle();
        params.putString("gsid", u.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(u.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        UnreadNum result = new UnreadNum(content);
        return result;
    }

    /**
     * 举报
     * 
     * @see NetEngine#report(User, String, String, String, int, int)
     * @param usr
     * @param srcuid
     * @param mblogId
     * @param reason_
     * @return NetResult
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult report(User usr, String srcuid, String mblogId,
            String reason_) throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        return report(usr, srcuid, mblogId, reason_, 1, 1);
    }

    /**
     * 举报某篇微博
     * 
     * @param usr
     * @param mblogId
     * @param reason_
     * @return NetResult
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult reportBlog(User usr, String mblogId, String reason_)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        return report(usr, null, mblogId, reason_, 1, 1);
    }

    /**
     * 举报某用户
     * 
     * @param usr
     * @param srcuid
     * @param reason_
     * @return NetResult
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public NetResult reportUser(User usr, String srcuid, String reason_)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        return report(usr, srcuid, null, reason_, 1, 1);
    }

    /**
     * 以上为正确返回格式，接口错误直接返回统一错误格式<br />
     * 1. URL我们会转换为WEB的URL格式 http://t.sina.com.cn/uid/mblogid
     * 这种，所以要举报某条微博，需要将此微博所属的uid也传过来，否则无效 <br />
     * 2. type和互联网一致(1.内容涉及色情或暴力;2.政治反动举报;3.内容可能侵权;4.内容涉及其他违规事项)，现在wap这边都是1，
     * 可以不传此参数 <br />
     * 3. group和互联网一致(1.用户；2微博；3评论)，wap这边默认也都是1，可以不传此参数
     * 
     * @param usr
     *            当前用户gsid POST/GET方式，必选，通过登录接口获取得到
     * @param srcuid
     *            举报的uid POST/GET方式，可选 要举报某个用户，请填写此uid
     * @param mblogId
     *            举报的微博id POST/GET方式，可选 要举报某个用户的某条微博，请填写此id
     * @param reason_
     *            举报原因 POST/GET方式，必选
     * @param type
     *            举报类型 POST/GET方式，可选，默认为1
     * @param group
     *            举报对象 POST/GET方式，可选，默认为1
     * @return
     * @throws HttpException
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    private NetResult report(User usr, String srcuid, String mblogId,
            String reason_, int type, int group) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder();
        url.append(NetEngine.SERVER).append("dealcomplaint.php?")
                .append("gsid=").append(usr.gsid).append("&c=")
                .append(Constants.CID).append("&s=")
                .append(calculateS(usr.uid)).append("&from=")
                .append(Constants.FROM).append("&wm=").append(Constants.WM)
                .append("&ua=").append(generateUA(mContext));

        Bundle params = new Bundle();
        if (type >= 1 && type <= 4) {
            params.putString("type", String.valueOf(type));
        }
        if (group >= 1 && group <= 3) {
            params.putString("group", String.valueOf(group));
        }
        if (!TextUtils.isEmpty(srcuid)) {
            params.putString("srcuid", srcuid);
        }
        if (!TextUtils.isEmpty(mblogId)) {
            params.putString("id", mblogId);
        }
        params.putString("reason", reason_);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_POST,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;

    }

    /**
     * 取得最新的版本信息
     * 
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     * @throws HttpException
     */
    public VersionInfo getLatestVersion(boolean force) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getlatestversion.php");

        String from = Constants.FROM;// .substring(Constants.FROM.length()-4,
                                     // Constants.FROM.length());
        if (WeiboApplication.VERSION.contains("beta")) {
            from = Constants.FROM;
        }
        Bundle params = new Bundle();
        params.putString("c", Constants.CID);
        params.putString("from", from);
        if (force) {
            params.putString("manual", "1");
        }
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        VersionInfo result = new VersionInfo(content);
        return result;
    }

    /**
     * Get group list for current user
     * 
     * @param usr
     *            current
     * @return GroupList
     * 
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public GroupList getGroupList(User usr) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("attgroup.php");

        Bundle params = new Bundle();
        params.putString("gsid", usr.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        GroupList result = new GroupList(content);
        return result;
    }

    /**
     * 获取微博评论和转发数量
     * 
     * @param usr
     *            current user
     * @param mblogId
     *            微博id,支持多个
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public MBlogCRNumList getMblogCRNum(User usr, String... mblogId)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getmblogcrnum.php");

        StringBuilder midStr = new StringBuilder();
        boolean first = true;
        for (String id : mblogId) {
            if (first) {
                midStr.append(id);
                first = false;
            } else {
                midStr.append(",").append(id);
            }
        }
        Bundle params = new Bundle();
        params.putString("gsid", usr.gsid);
        params.putString("id", midStr.toString());
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        MBlogCRNumList result = new MBlogCRNumList();
        if (mblogId.length > 1) {
            result.initFromString(content);
        } else if (mblogId.length == 1) {
            MBlogCRNum crNum = new MBlogCRNum(content);
            result.mblogCRNumList.add(crNum);
        }

        return result;
    }

    /**
     * Download and save the picture to the local storage.
     * 
     * @param url
     *            the url of picture
     * @param savedir
     *            the directory to save picture, you can get it by calling
     *            context.getCacheDir().
     * @return the path of picture in the local storage.
     * @throws WeiboIOException
     * @throws WeiboApiException
     */
    public String getPicture(String url, String savedir)
            throws WeiboIOException, WeiboApiException {
        return getPicture(url, savedir, false, new DefaultDownloadCallback(),
                null);
    }

    /**
     * Download and save the picture to the local storage.
     * 
     * @param url
     *            the url of picture
     * @param savedir
     *            the directory to save picture, you can get it by calling
     *            context.getCacheDir().
     * @param needReload
     *            if true,reload picture from net
     * @param callback
     *            reporting download state by using this callback object
     * @param name
     *            assign this file name
     * @return the path of picture in the local storage.
     * @throws WeiboIOException
     * @throws WeiboApiException
     */
    public String getPicture(String url, String savedir, boolean needReload,
            IDownloadState callback, String name) throws WeiboIOException,
            WeiboApiException {
        InputStream inputStream;
        // String filename = MD5.hexdigest(url);
        String filepath;
        String picName;
        if (name != null) { // 得到filepath和picName
            filepath = name;
            picName = Utils.getFileNameFromPath(name);
        } else {
            String filename = Utils.getFileNameFromUrl(url);
            if (url.contains("woriginal") && Utils.isEndWithGif(url)) {
                filepath = savedir + "/" + filename + ".gif";
            } else {
                filepath = savedir + "/" + filename;
            }
            picName = filename;
        }

        DownloadCallbackManager.getInstance().put(picName, callback);
        DownloadCallbackManager.getInstance().setDownloadStart(picName);

        String tempFilepath = Utils.getTempFilePath(filepath);
        Utils.sharkFolderSize(savedir, Utils.MAX_SD_FOLDER_LIMITION);
        Utils.makesureFileExist(filepath);
        File file = new File(filepath);
        File tempFile = new File(tempFilepath);

        Object lock = DownloadCallbackManager.getInstance().getLock(picName);
        synchronized (lock) {// 根据文件名进行同步下载
            if (!needReload) {
                if (file.exists()) {
                    if (file.length() > 0) {// 文件已存在并且长度不为0则返回
                        DownloadCallbackManager.getInstance()
                                .getCallback(picName, callback)
                                .onComplete(filepath);
                        DownloadCallbackManager.getInstance().remove(picName);
                        return filepath;
                    } else {
                        file.delete();// /
                    }
                }
            } else {
                if (file.exists()) {
                    file.delete();
                }
            }

            HttpGet request = new HttpGet(url);
            HttpClient client = NetUtils.getHttpClient(mContext);
            request.setHeader("User-Agent",
                    WeiboApplication.UA == null ? Constants.USER_AGENT
                            : WeiboApplication.UA);
            int length = 100 * 1024;// 100KB-default vaule
            // int offset = 0;
            try {
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                } catch (NullPointerException e) {
                    // google issue, doing this to work around
                    response = client.execute(request);
                }

                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != Constants.HTTP_STATUS_OK) {
                    throw new WeiboApiException(String.format(
                            "Invalid response from server: %s",
                            status.toString()));
                }
                Header lengthHeader = response.getFirstHeader("Content-Length");
                if (lengthHeader != null) {// 获得下载文件长度
                    length = Integer.valueOf(lengthHeader.getValue());
                }

                if (tempFile.exists()) {
                    int tempLength = (int) tempFile.length();
                    if (tempLength < length) {// temp文件不完整 删除temp文件
                        tempFile.delete();
                    } else {// temp文件完整 重命名并返回
                        tempFile.renameTo(file);
                        DownloadCallbackManager.getInstance()
                                .getCallback(picName, callback)
                                .onComplete(filepath);
                        DownloadCallbackManager.getInstance().remove(picName);
                        if (client != null) {
                            client.getConnectionManager().shutdown();
                        }
                        return filepath;
                    }
                }

                // Pull content stream from response
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();

            } catch (IOException e) {
                DownloadCallbackManager.getInstance()
                        .getCallback(picName, callback).onFail(null);
                if (client != null) {
                    client.getConnectionManager().shutdown();
                }
                throw new WeiboApiException("Problem communicating with API", e);
            }

            try { // downlod temp
                FileOutputStream content = new FileOutputStream(tempFilepath,
                        true);
                // Read response into a buffered stream
                int readBytes = 0;
                float lastProgress = 0;
                // set the callback start to true
                DownloadCallbackManager.getInstance().setCallbackStart(picName,
                        true);
                DownloadCallbackManager.getInstance()
                        .getCallback(picName, callback).onStart(null);
                int receivedLength = 0;
                byte[] sBuffer = new byte[512];
                // if (offset != 0) {
                // inputStream = Utils.skipRead(inputStream, offset);
                // receivedLength += offset;
                // float percent = (float)receivedLength/(float)length;
                // if( percent - lastProgress >= 0.1){
                // lastProgress = percent;
                // DownloadCallbackManager.getInstance().getCallback(picName,
                // callback).onProgressChanged(percent*100);
                // }
                // }
                while (!DownloadCallbackManager.getInstance().isDownloadStop(
                        picName)
                        && (readBytes = inputStream.read(sBuffer)) != -1) {// 下载temp文件并通知回调
                    content.write(sBuffer, 0, readBytes);
                    // report progress every 10 percents
                    receivedLength += readBytes;
                    float percent = (float) receivedLength / (float) length;
                    if (percent - lastProgress >= 0.1) {
                        lastProgress = percent;
                        DownloadCallbackManager.getInstance()
                                .getCallback(picName, callback)
                                .onProgressChanged(percent * 100);
                    }
                    content.flush();
                }
                content.close();
                if (client != null) {
                    client.getConnectionManager().shutdown();
                }

                if (tempFile.length() < length) {// temp文件未完整下载抛出异常
                    // tempFile.delete();
                    // throw exception
                    throw new IOException("未完整下载temp文件");
                } else {// temp文件完整下载
                    tempFile.renameTo(file);
                    DownloadCallbackManager.getInstance()
                            .getCallback(picName, callback)
                            .onComplete(filepath);
                    DownloadCallbackManager.getInstance().remove(picName);
                }

                return filepath;
            } catch (IOException e) {
                e.printStackTrace();
                DownloadCallbackManager.getInstance()
                        .getCallback(picName, callback).onFail(null);
                if (client != null) {
                    client.getConnectionManager().shutdown();
                }
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                if (file.exists()) {
                    file.delete();
                }
                throw new WeiboApiException("Unknown error", e);
            }
        }
    }

    /**
     * 获取广场所需信息
     * 
     * @param usr
     *            用户信息
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public SquareItemList getSquare(User usr) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        if (Utils.isEnPlatform(mContext)) {
            url.append("getsearchiconworld.php");
        } else {
            url.append("getsearchicon.php");
        }
        Bundle params = new Bundle();
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        SquareItemList result = new SquareItemList(content);
        return result;

    }

    /**
     * 获取测速用的Url列表
     * 
     * @param usr
     *            用户信息
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public UrlItemList getSpeedUrlItemList(User usr) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("admob.php");
        Bundle params = new Bundle();
        params.putString("gsid", usr.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        UrlItemList result = new UrlItemList(content);
        return result;
    }

    /**
     * 提交测速结果
     * 
     * @param usr
     * @param log
     * @return NetResult 是否提交成功
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public NetResult postSpeedLog(User usr, SpeedLog log)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        String url = String.format(
                "%sadmob.php?act=log&wm=%s&from=%s&c=%s&ua=%s&gsid=%s",
                NetEngine.SERVER, Constants.WM, Constants.FROM, Constants.CID,
                generateUA(mContext), usr.gsid);
        Bundle params = new Bundle();
        params.putString("gsid", usr.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        params.putString("starttime", log.startTime);
        params.putString("endtime", log.endTime);
        params.putString("pagesize", log.pageSize);
        params.putString("linetype", log.lineType);
        params.putString("urlid", log.urlId);
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_POST,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    /**
     * 提交测速结果
     * 
     * @param usr
     * @param logs
     * @return NetResult 是否提交成功
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public NetResult postSpeedLog(User usr, List<SpeedLog> logs)
            throws WeiboIOException, WeiboParseException, WeiboApiException {
        String url = String.format(
                "%sadmob.php?act=log&wm=%s&from=%s&c=%s&ua=%s&gsid=%s",
                NetEngine.SERVER, Constants.WM, Constants.FROM, Constants.CID,
                generateUA(mContext), usr.gsid);
        StringBuffer starttime = new StringBuffer();
        StringBuffer endtime = new StringBuffer();
        StringBuffer pagesize = new StringBuffer();
        StringBuffer linetype = new StringBuffer();
        StringBuffer urlid = new StringBuffer();
        StringBuffer dnstime = new StringBuffer();
        StringBuffer responsetime = new StringBuffer();
        StringBuffer vip = new StringBuffer();
        StringBuffer gwip = new StringBuffer();
        StringBuffer httpcode = new StringBuffer();
        StringBuffer linktime = new StringBuffer();

        boolean first = true;
        for (SpeedLog log : logs) {
            if (!first) {
                starttime.append(",");
                endtime.append(",");
                pagesize.append(",");
                linetype.append(",");
                urlid.append(",");
                dnstime.append(",");
                responsetime.append(",");
                vip.append(",");
                gwip.append(",");
                httpcode.append(",");
                linktime.append(",");
            } else {
                first = false;
            }
            starttime.append(log.startTime);
            endtime.append(log.endTime);
            pagesize.append(log.pageSize);
            linetype.append(log.lineType);
            urlid.append(log.urlId);
            dnstime.append(log.dnsTime);
            responsetime.append(log.responseTime);
            vip.append(log.vip);
            gwip.append(log.gwip);
            httpcode.append(log.httpcode);
            linktime.append(log.linkTime);

        }
        Bundle params = new Bundle();
        params.putString("gsid", usr.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        params.putString("starttime", starttime.toString());
        params.putString("endtime", endtime.toString());
        params.putString("pagesize", pagesize.toString());
        params.putString("linetype", linetype.toString());
        params.putString("urlid", urlid.toString());
        params.putString("dnstime", dnstime.toString());
        params.putString("response", responsetime.toString());
        params.putString("vip", vip.toString());
        params.putString("gwip", gwip.toString());
        params.putString("httpcode", httpcode.toString());
        params.putString("linktime", linktime.toString());
        Log.i(Constants.TAG,
                "starttime---" + starttime.toString() + "endtime---"
                        + endtime.toString() + "pagesize---"
                        + pagesize.toString());
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_POST,
                params, mContext);
        NetResult result = new NetResult(content);
        return result;
    }

    // /**
    // * 获取广告信息列表
    // * @return
    // * @throws WeiboIOException
    // * @throws WeiboParseException
    // * @throws WeiboApiException
    // */
    // public AdList getAdList() throws WeiboIOException, WeiboParseException,
    // WeiboApiException {
    // StringBuilder url = new StringBuilder(NetEngine.SERVER);
    // url.append("clientad.php");
    // Bundle params = new Bundle();
    // params.putString("from", Constants.FROM);
    // params.putString("wm", Constants.WM);
    // params.putString("ua", generateUA(mContext));
    // params.putString("platform", "android");
    // params.putString("size", Utils.getWindowsWidthParam(mContext));
    // String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
    // params, mContext);
    // AdList result = new AdList(content);
    // return result;
    // }

    /**
     * 获取广告信息列表
     * 
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public AdList getAdList() throws WeiboIOException, WeiboParseException,
            WeiboApiException {
        if (StaticInfo.mUser == null || TextUtils.isEmpty(StaticInfo.mUser.uid)) {
            AdList resultAd = new AdList();
            return resultAd;
        }
        String uid = StaticInfo.mUser.uid;
        AdHelper helper = new AdHelper(mContext);
        StringBuilder url = new StringBuilder(NetEngine.SERVER_AD);
        url.append("clientad.php");
        Bundle getParams = new Bundle();
        Bundle postParams = new Bundle();
        getParams.putString("from", Constants.FROM);
        getParams.putString("wm", Constants.WM);
        getParams.putString("ua", generateUA(mContext));
        getParams.putString("platform", "android");
        getParams.putString("category", "all");
        getParams.putString("size", Utils.getWindowsWidthParam(mContext));
        if (TextUtils.isEmpty(uid) == false) {
            getParams.putString("gsid", StaticInfo.mUser.gsid);
            getParams.putString("c", Constants.CID);
            getParams.putString("s", calculateS(StaticInfo.mUser.uid));

            postParams.putString("uid", uid);
            postParams.putString("posid", "pos4ea7cda9892d7");
            // postParams.putString("udid", "");
            postParams.putString("info", helper.getTelephonyText() + "+"
                    + helper.getWifiText());
            postParams.putString("dinfo",
                    helper.getImei() + "_" + helper.getImsi());
        }
        String content = NetUtils.openUrlWithParms(url.toString(), getParams,
                postParams, mContext);
        AdList result = new AdList(content);
        return result;
    }

    /**
     * 计算s值
     * 
     * @param preStr
     * @return
     */
    private static String calculateS(String preStr) {
        return Utils.calculateS(preStr);
    }

    /**
     * 按照规则生成 UA
     * 
     * @param ctx
     * @return
     */
    public String generateUA(Context ctx) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(Build.MANUFACTURER).append("-").append(Build.MODEL);
        buffer.append("__");
        buffer.append("weibo");
        buffer.append("__");
        try {
            String versionCode = PackageManagerUtil.getVersion(ctx);
            buffer.append(versionCode.replaceAll("\\s+", "_"));
        } catch (final Exception e) {
            e.printStackTrace();
            buffer.append("unknown");
        }
        buffer.append("__").append("android").append("__android")
                .append(android.os.Build.VERSION.RELEASE);
        return buffer.toString();
    }

    // /**
    // * 用户数据收集
    // * @return
    // */
    // public static NetResult uploadUserLog(Context context) throws
    // WeiboApiException , WeiboParseException , WeiboIOException{
    // StringBuilder url = new StringBuilder(NetEngine.SERVER);
    // url.append("log.php");
    // Bundle params = new Bundle();
    // params.putString("from", Constants.FROM);
    // params.putString("wm", Constants.WM);
    // TelephonyManager telephonyManager =
    // (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    // String imei = telephonyManager.getDeviceId();
    // params.putString("imei", imei);
    // String imsi = telephonyManager.getSubscriberId();
    // if (imsi != null && imsi.length() > 0) {
    // params.putString("imsi", imsi);
    // }
    // params.putString("mobile_type", WeiboApplication.DEVICE_NAME);
    // String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
    // params, context);
    // NetResult result = new NetResult(content);
    // return result;
    // }
    /**
     * 用户数据收集
     * 
     * @return
     */
    public NetResult uploadUserLog(Context context) throws WeiboApiException,
            WeiboParseException, WeiboIOException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("deallog.php");
        Bundle params = new Bundle();
        params.putString("act", "install");
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(context));
        String desc = "";
        final NetUtils.NetworkState state = NetUtils.getNetworkState(context
                .getApplicationContext());
        if (state == NetUtils.NetworkState.NOTHING) {
            // desc = "";
        } else if (state == NetUtils.NetworkState.WIFI) {
            desc = "wifi";
        } else {
            final NetUtils.APNWrapper apnWrapper = NetUtils.getAPN(context
                    .getApplicationContext());
            if (apnWrapper != null) {
                desc = apnWrapper.name;
            }
        }
        if (!TextUtils.isEmpty(desc)) {
            params.putString("agency", desc.toLowerCase());
        }
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        if (!TextUtils.isEmpty(imei)) {
            params.putString("imei", imei);
        }
        String imsi = telephonyManager.getSubscriberId();
        if (imsi != null && imsi.length() > 0) {
            params.putString("imsi", imsi);
        }
        params.putString("mobile_type", WeiboApplication.DEVICE_NAME);
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, context);
        NetResult result = new NetResult(content);
        return result;
    }

    public SkinList getSkinList(Context context, User usr)
            throws WeiboApiException, WeiboParseException, WeiboIOException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("getskin.php");
        Bundle params = new Bundle();
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(context));
        params.putString("platform", "android");
        params.putString("gsid", usr.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, context);
        SkinList result = new SkinList(content);
        result.mIsNew = true;
        result.version = Utils.getVersionCode(context);
        return result;
    }

    /**
     * 获取周边位置信息
     * 
     * @param context
     * @param usr
     * @param lat
     * @param lon
     * @param query
     * @param num
     * @param start
     * @return
     * @throws WeiboApiException
     * @throws WeiboParseException
     * @throws WeiboIOException
     */
    public LocationList getDistanceByRange(Context context, User usr,
            double lat, double lon, String query, int num, int start)
            throws WeiboApiException, WeiboParseException, WeiboIOException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        if (!android.text.TextUtils.isEmpty(query)) {
            url.append("poi_distancebyrange.php");
        } else {
            url.append("poi_distance.php");
        }
        Bundle params = new Bundle();
        if (usr != null) {
            params.putString("gsid", usr.gsid);
            params.putString("c", Constants.CID);
            params.putString("s", calculateS(usr.uid));
        }
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(context));
        params.putString("platform", "android");
        params.putString("lat", String.valueOf(lat));
        params.putString("lon", String.valueOf(lon));
        if (!android.text.TextUtils.isEmpty(query)) {
            params.putString("query", query);
        }
        params.putString("num", String.valueOf(num));
        params.putString("start", String.valueOf(start));

        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, context);
        LocationList result = new LocationList(content);
        result.isNewLocation = true;
        return result;
    }

    /**
     * 获取周边的人
     * 
     * @param usr
     *            用户信息
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public NearByUserInfoList getNearByPeople(User usr, String lat, String lon,
            int page, int pageSize) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("lbsnearbyuser.php");
        Bundle params = new Bundle();
        params.putString("gsid", usr.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        params.putString("lat", lat);
        params.putString("long", lon);
        params.putString("page", String.valueOf(page));
        params.putString("pagesize", String.valueOf(pageSize));
        params.putString("lastmblog", "1");
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NearByUserInfoList result = new NearByUserInfoList(content);
        if (result != null) {
            result.setLat(Double.parseDouble(lat));
            result.setLon(Double.parseDouble(lon));
            result.setUpdateTime(new Date().getTime());
        }
        return result;
    }

    /**
     * 获取周边的微博
     * 
     * @param usr
     *            用户信息
     * @return
     * @throws WeiboIOException
     * @throws WeiboParseException
     * @throws WeiboApiException
     */
    public NearByBlogList getNearByBlog(User usr, String lat, String lon,
            int page, int pageSize) throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("lbsnearbytimeline.php");
        Bundle params = new Bundle();
        params.putString("gsid", usr.gsid);
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(usr.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        params.putString("lat", lat);
        params.putString("long", lon);
        params.putString("page", String.valueOf(page));
        params.putString("pagesize", String.valueOf(pageSize));
        String content = NetUtils.openUrl(url.toString(), NetUtils.METHOD_GET,
                params, mContext);
        NearByBlogList result = new NearByBlogList(content);
        if (result != null) {
            result.setLat(Double.parseDouble(lat));
            result.setLon(Double.parseDouble(lon));
            result.setUpdateTime(new Date().getTime());
        }
        return result;
    }

    /**
     * 确保当前User对象中包含有效的oauth_token
     * 
     * @param context
     * @param oauth_token
     */
    public boolean prepareVaildUser(Context context, String oauth_token) {
        // 判断是否有合法的token
        if (TextUtils.isEmpty(oauth_token)) {
            List<User> accounts = AccountHelper.loadAccountsFromDB(context
                    .getApplicationContext());
            try {
                User mUser = NetEngine.getNetInstance(
                        context.getApplicationContext())
                        .login(StaticInfo.mUser);
                if (accounts != null) {
                    int size = accounts.size();
                    for (int i = 0; i < size; i++) {
                        if (accounts.get(i).uid.equals(mUser.uid)) {
                            accounts.remove(i);
                            mUser.name = StaticInfo.mUser.name;
                            accounts.add(i, mUser);
                            break;
                        }
                    }
                }
                StaticInfo.mUser.setOauth_token(mUser.getOauth_token());
                StaticInfo.mUser.setOauth_token_secret(mUser
                        .getOauth_token_secret());
                AccountHelper.saveAccounts(context.getApplicationContext(),
                        accounts);
                return true;
            } catch (WeiboParseException e) {
                e.printStackTrace();
                return false;
            } catch (WeiboIOException e) {
                e.printStackTrace();
                return false;
            } catch (WeiboApiException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 分享导航图微博
     */
    @Override
    public NetResult postNavigaterBlog() throws WeiboIOException,
            WeiboParseException, WeiboApiException {
        StringBuilder url = new StringBuilder(NetEngine.SERVER);
        url.append("dealmblog.php");
        Bundle params = new Bundle();
        params.putString("gsid", StaticInfo.mUser.gsid);
        params.putString("act", "event");
        params.putString("c", Constants.CID);
        params.putString("s", calculateS(StaticInfo.mUser.uid));
        params.putString("from", Constants.FROM);
        params.putString("wm", Constants.WM);
        params.putString("ua", generateUA(mContext));
        String xmlResult = NetUtils.openUrl(url.toString(),
                NetUtils.METHOD_GET, params, mContext);
        NetResult result = new NetResult(xmlResult);
        return result;
    }
}

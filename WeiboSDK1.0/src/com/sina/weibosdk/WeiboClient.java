package com.sina.weibosdk;

import android.content.Context;
import com.sina.weibosdk.cache.CacheStrategy;
import com.sina.weibosdk.entity.AtUsers;
import com.sina.weibosdk.entity.BlackList;
import com.sina.weibosdk.entity.Comment;
import com.sina.weibosdk.entity.CommentList;
import com.sina.weibosdk.entity.Favorite;
import com.sina.weibosdk.entity.FavoritesList;
import com.sina.weibosdk.entity.Group;
import com.sina.weibosdk.entity.GroupArray;
import com.sina.weibosdk.entity.GroupInfo;
import com.sina.weibosdk.entity.GroupList;
import com.sina.weibosdk.entity.GroupUserList;
import com.sina.weibosdk.entity.HotStatusList;
import com.sina.weibosdk.entity.HotTopicList;
import com.sina.weibosdk.entity.MapImage;
import com.sina.weibosdk.entity.Message;
import com.sina.weibosdk.entity.MessageList;
import com.sina.weibosdk.entity.MessageUserList;
import com.sina.weibosdk.entity.PositionList;
import com.sina.weibosdk.entity.Remind;
import com.sina.weibosdk.entity.Result;
import com.sina.weibosdk.entity.SquareList;
import com.sina.weibosdk.entity.Status;
import com.sina.weibosdk.entity.StatusCRNumList;
import com.sina.weibosdk.entity.StatusList;
import com.sina.weibosdk.entity.Topic;
import com.sina.weibosdk.entity.TopicList;
import com.sina.weibosdk.entity.User;
import com.sina.weibosdk.entity.UserInfo;
import com.sina.weibosdk.entity.UserInfoList;
import com.sina.weibosdk.entity.VerifyCode;
import com.sina.weibosdk.entity.VersionInfo;
import com.sina.weibosdk.exception.WeiboApiException;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.net.HttpEngine;
import com.sina.weibosdk.net.HttpUrlConnectionEngine;
import com.sina.weibosdk.net.IDownloadCallback;
import com.sina.weibosdk.net.NetRequestProxy;
import com.sina.weibosdk.parser.JsonParser;
import com.sina.weibosdk.requestparam.APIRequestParam;
import com.sina.weibosdk.requestparam.RequestParam;

/**
 * 封装网络API请求
 * 
 * @author zhangqi
 * 
 */
public final class WeiboClient {

    private static HttpEngine mDefaultStrategy;
    private HttpEngine mRequestStrategy;
    private NetRequestProxy mProxy;
    private Context mContext;
    private String SERVER;

    public WeiboClient(Context context) {
        mContext = context;
        if (mDefaultStrategy == null) {
            mDefaultStrategy = new HttpUrlConnectionEngine(mContext);
        }
        mRequestStrategy = mDefaultStrategy;
        mProxy = NetRequestProxy.getInstance(context);
        SERVER = WeiboSDKConfig.getInstance().getString(WeiboSDKConfig.KEY_API_SERVER);
    }

    /**
     * 设置网络请求的策略，如果不设置，用默认的策略
     */
    public void setNetRequestStrategy(HttpEngine strategy) {
        mRequestStrategy = strategy;
    }

    public HttpEngine getNetRequestStrategy() {
        return mRequestStrategy;
    }

    /**
     * 
     * @param username
     *            用户名
     * @param password
     *            用户密码 。请输入明文，函数中会用RSA加密
     * @param s
     *            由用户名和密码计算出的s值
     * @param cpt
     *            验证码加密串
     * @param cptcode
     *            验证码
     * @return
     * @throws Exception
     */
    public User login(String username, String password, boolean flag, String s, String cpt,
            String cptcode) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().loginParam(username, password, s, cpt,
                cptcode);
        String resp = mProxy.get(SERVER + "account/login", param.getParams(), null,
                mRequestStrategy);
        return new User(resp);
    }

    /**
     * 返回验证码
     * 
     * @return
     * @throws WeiboException
     */
    public VerifyCode getCaptcha() throws WeiboException {
        RequestParam param = new APIRequestParam();
        String resp = mProxy.get(SERVER + "captcha/get", param.getParams(), null, mRequestStrategy);
        return new VerifyCode(resp);
    }

    /**
     * 
     * @param page
     *            页码。注意：最多返回200条分页内容。默认值1。
     * @param count
     *            指定每页返回的记录条数。默认值50，最大值200。
     * @return
     * @throws WeiboException
     */
    public StatusList getPublicTimeLine(int page, int count) throws WeiboException {
        String resp = mProxy.get(SERVER + "statuses/public_timeline", WeiboClientHelper
                .getInstance().getPublicTimeLineParam(page, count).getParams(), null,
                mRequestStrategy);
        return JsonParser.parserStatusList(resp);
    }

    /**
     * 
     * @param gid
     *            分组id值，如果不为空，则返回指定好友分组的timeline
     * @param page
     *            页码。注意：最多返回200条分页内容。默认值1。
     * @param count
     *            指定每页返回的记录条数。默认值50，最大值200。
     * @param since_id
     *            若指定此参数，则只返回ID比since_id大的微博消息（即比since_id发表时间晚的微博消息）。默认为0
     * @param feature
     * 			  过滤类型ID （0：全部、1：原创、2：图片、3：视频、4：音乐、5：商品） 默认为 0           
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的微博消息。默认为0
     * @return
     * @throws WeiboException
     */
    public StatusList getFriendsTimeLine(String gid, int feature, int page, int count, String since_id,
            String max_id) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().getFriendsTimeLineParam(gid, feature, page,
                count, since_id, max_id);
        String resp = mProxy.get(SERVER + "statuses/friends_timeline", param.getParams(), null,
                mRequestStrategy);
        return JsonParser.parserStatusList(resp);
    }

    /**
     * 
     * @param uid
     *            用户id值，默认为当前登录用户
     * @param page
     *            页码。注意：最多返回200条分页内容。默认值1。
     * @param count
     *            指定每页返回的记录条数。默认值50，最大值200。
     * @param filter
     *            微博类型，0全部，1原创，2图片，3视频，4音乐,5商品. 返回指定类型的微博信息内容。
     * @return
     * @throws WeiboException
     */
    public StatusList getUserTimeLine(String uid, int page, int count, int filter)
            throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().getUserTimeLineParam(uid, page, count,
                filter);
        String resp = mProxy.get(SERVER + "statuses/user_timeline", param.getParams(), null,
                mRequestStrategy);
        // return new StatusList(resp);
        return JsonParser.parserStatusList(resp);
    }

    /**
     * 
     * @param id
     *            要获取的微博消息ID
     * @return
     * @throws WeiboException
     */
    public Status getSingleStatus(String id) throws WeiboException {
        Util.checkNullParams(id);
        String resp = mProxy.get(SERVER + "statuses/show", WeiboClientHelper.getInstance()
                .getSingleStatusParam(id).getParams(), null, mRequestStrategy);
        // return new Status(resp);
        return JsonParser.parserStatus(resp);
    }

    /**
     * 
     * @param statusId
     *            微博ID，英文半角逗号分隔，最大100
     * @return
     * @throws WeiboException
     */
    public StatusCRNumList getStatusCRNum(String... statusId) throws WeiboException {
        String resp = mProxy.get(SERVER + "statuses/count", WeiboClientHelper.getInstance()
                .getStatusCRNumParam(statusId).getParams(), null, mRequestStrategy);
        return new StatusCRNumList(resp);
    }

    /**
     * 
     * @param page
     *            页码。注意：最多返回200条分页内容。默认值1。
     * @param count
     *            指定每页返回的记录条数。默认值50，最大值200。
     * @param since_id
     *            若指定此参数，则只返回ID比since_id大的微博消息（即比since_id发表时间晚的微博消息）。默认为0
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的微博消息。默认为0
     * @param filter_by_author
     *            过滤类型ID （0：所有用户、1：关注的人）默认为0。
     * @param filter_by_type
     *            过滤类型ID （0：所有微博、1：原创的微博）默认为0
     * @return
     * @throws WeiboException
     */
    public StatusList getMentionsTimeline(int page, int count, String since_id, String max_id,
            int filter_by_author, int filter_by_type) throws WeiboException {
        String resp = mProxy.get(
                SERVER + "statuses/mentions",
                WeiboClientHelper
                        .getInstance()
                        .getMentionsTimelineParam(page, count, since_id, max_id, filter_by_author,
                                filter_by_type).getParams(), null, mRequestStrategy);
        // return new StatusList(resp);
        return JsonParser.parserStatusList(resp);

    }

    /**
     * 
     * @param app_base
     *            是否按照当前应用信息对搜索结果进行过滤。当值为1时，仅返回通过该应用发送的微博消息。当值为0时，不过滤。
     * @param count
     *            返回结果条数数量，默认20，最大50
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。
     * @param picsize
     *            带图片微博返回的bmiddle_pic字段图片的大小，可选值 240,320,960
     * @return
     * @throws WeiboException
     */
    public HotStatusList getHotStatusDaily(int app_base, int count, int is_encoded, int picsize)
            throws WeiboException {
        String resp = mProxy.get(SERVER + "hot/repost_daily", WeiboClientHelper.getInstance()
                .getHotStatusDailyParam(app_base, count, is_encoded, picsize).getParams(), null,
                mRequestStrategy);
        return new HotStatusList(resp);
    }

    /**
     * 
     * @param app_base
     *            是否按照当前应用信息对搜索结果进行过滤。当值为1时，仅返回通过该应用发送的微博消息。当值为0时，不过滤。
     * @param count
     *            返回结果条数数量，默认20，最大50
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。
     * @param picsize
     *            带图片微博返回的bmiddle_pic字段图片的大小，可选值 240,320,960
     * @return
     * @throws WeiboException
     */
    public HotStatusList getHotCommentDaily(int app_base, int count, int is_encoded, int picsize)
            throws WeiboException {
        String resp = mProxy.get(SERVER + "hot/comments_daily", WeiboClientHelper.getInstance()
                .getHotCommentDailyParam(app_base, count, is_encoded, picsize).getParams(), null,
                mRequestStrategy);
        return new HotStatusList(resp);
    }

    /**
     * 
     * @param status
     *            要发布的微博消息文本内容，必须做URLEncode,信息内容不超过140个汉字。
     * @param visible
     *            微博的可见性，0：所有人能看，1：仅自己可见，默认为0。
     * @param lat
     *            纬度。有效范围：-90.0到+90.0，+表示北纬。默认为0.0
     * @param lon
     *            经度。有效范围：-180.0到+180.0，+表示东经。默认为0.0
     * @param annotations
     *            元数据，主要是为了方便第三方应用记录一些适合于自己使用的信息。每条微博可以包含一个或者多个元数据。请以json字串的形式提交
     *            ，字串长度不超过512个字符，具体内容可以自定。
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。默认0.
     * @return
     * @throws WeiboException
     */
    public Status updateStatus(String status, int visible, double lat, double lon,
            String annotations, int is_encoded, String cpt, String cptcode) throws WeiboException {
        String resp = mProxy.get(
                SERVER + "statuses/update",
                WeiboClientHelper
                        .getInstance()
                        .updateStatusParam(status, visible, lat, lon, annotations, is_encoded, cpt,
                                cptcode).getParams(), null, mRequestStrategy);
        // return new Status(resp);
        return JsonParser.parserStatus(resp);
    }

    /**
     * 
     * @param status
     *            要发布的微博消息文本内容，必须做URLEncode,信息内容不超过140个汉字。
     * @param picPath
     *            要上传的图片。仅支持JPEG,GIF,PNG图片,为空返回400错误。目前上传图片大小限制为<5M。
     * @param lat
     *            纬度。有效范围：-90.0到+90.0，+表示北纬。默认为0.0
     * @param lon
     *            经度。有效范围：-180.0到+180.0，+表示东经。默认为0.0
     * @param visible
     * @return
     * @throws WeiboException
     */
    public Status uploadStatus(String status, String picPath, double lat, double lon, int visible,
            String cpt, String cptcode) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().uploadStatusParam(status, picPath,
                lat, lon, visible, cpt, cptcode);
        String resp = mProxy.post(SERVER + "statuses/upload", param.getParams(), param.getFiles(),
                null, mRequestStrategy);
        // return new Status(resp);
        return JsonParser.parserStatus(resp);
    }

    /**
     * 
     * @param status
     *            添加的转发文本。必须做URLEncode,信息内容不超过140个汉字。如不填则默认为“转发微博”。
     * @param id
     *            要转发的微博ID
     * @param is_comment
     *            是否在转发的同时发表评论。0表示不发表评论，1表示发表评论给当前微博，2表示发表评论给原微博，3是1、2都发表。默认为0。
     * @param annotations
     *            元数据
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。默认0.
     * @return
     * @throws WeiboException
     */
    public Status repostStatus(String status, String id, int is_comment, String annotations,
            int is_encoded, String cpt, String cptcode) throws WeiboException {
        Util.checkNullParams(id);
        String resp = mProxy.get(SERVER + "statuses/repost", WeiboClientHelper.getInstance()
                .repostStatusParam(status, id, is_comment, annotations, is_encoded, cpt, cptcode)
                .getParams(), null, mRequestStrategy);
        // return new Status(resp);
        return JsonParser.parserStatus(resp);

    }

    /**
     * 
     * @param id
     *            要删除的微博消息ID
     * @return
     * @throws WeiboException
     */
    public Status destroyStatus(String id, String cpt, String cptcode) throws WeiboException {
        String resp = mProxy.get(SERVER + "statuses/destroy", WeiboClientHelper.getInstance()
                .destroyStatusParam(id, cpt, cptcode).getParams(), null, mRequestStrategy);
        // return new Status(resp);
        return JsonParser.parserStatus(resp);
    }

    /**
     * 
     * @param page
     *            页码。
     * @param count
     *            每页返回结果数。
     * @param picsize
     *            带图片微博返回的bmiddle_pic字段图片的大小，可选值 240,320,960
     * @return
     * @throws WeiboException
     */
    public FavoritesList getFavorites(int page, int count, int picsize) throws WeiboException {
        String resp = mProxy.get(SERVER + "favorites", WeiboClientHelper.getInstance()
                .getFavoritesParam(page, count, picsize).getParams(), null, mRequestStrategy);
        return new FavoritesList(resp);
    }

    /**
     * 
     * @param id
     *            要收藏的微博ID
     * 
     * @return
     * @throws WeiboException
     */
    public Favorite createFavorite(String id) throws WeiboException {
        Util.checkNullParams(id);
        String resp = mProxy.get(SERVER + "favorites/create", WeiboClientHelper.getInstance()
                .createFavoriteParam(id).getParams(), null, mRequestStrategy);
        return new Favorite(resp);
    }

    /**
     * 
     * @param id
     *            要删除的收藏ID
     * 
     * @return
     * @throws WeiboException
     */
    public Favorite deleteFavorite(String id) throws WeiboException {
        Util.checkNullParams(id);
        String resp = mProxy.get(SERVER + "favorites/destroy", WeiboClientHelper.getInstance()
                .deleteFavoriteParam(id).getParams(), null, mRequestStrategy);
        return new Favorite(resp);
    }

    /**
     * 根据用户ID获取用户资料
     * 
     * @param uid
     *            如果为空，返回当前用户的分组列表
     */
    public UserInfo getUsersShow(String uid, String screen_name) throws WeiboException {
        String resp = mProxy.get(SERVER + "users/show", WeiboClientHelper.getInstance()
                .getUsersShowParam(uid, screen_name).getParams(), null, mRequestStrategy);
        return JsonParser.parserUserInfo(resp);
    }

    /**
     * 更新用户的基本信息，仅更新提交的字段
     */
    public UserInfo updateUserInfo(String screen_name, String gender, String description)
            throws WeiboException {
        String resp = mProxy.post(
                SERVER + "users/update",
                WeiboClientHelper.getInstance()
                        .updateUserInfoParam(screen_name, gender, description).getParams(),
                WeiboClientHelper.getInstance()
                        .updateUserInfoParam(screen_name, gender, description).getFiles(), null,
                mRequestStrategy);
        return JsonParser.parserUserInfo(resp);
    }

    /**
     * 根据用户ID获取用户资料
     * 
     * @param image
     *            FILE方式，可选，非PNG格式
     * @param trim_user
     *            是否返回用户的信息，0返回，1不返回，默认为0
     */
    public Object updateUserPortrait(String image, int trim_user) throws WeiboException {
        Util.checkNullParams(image);
        String resp = mProxy.post(SERVER + "account/avatar_upload", WeiboClientHelper.getInstance()
                .updateUserPortraitParam(image, trim_user).getParams(), WeiboClientHelper
                .getInstance().updateUserPortraitParam(image, trim_user).getFiles(), null,
                mRequestStrategy);
        if (0 == trim_user) {
            return JsonParser.parserUserInfo(resp);
        } else if (1 == trim_user) {
            return new Result(resp);
        } else {
            return null;
        }
    }

    /**
     * 返回给定用户所关注人的排序，排序依据昵称首字母或更新时间或关注时间 。
     * 
     * @param uid
     *            返回此uid所关注人的排序结果,默认当前用户
     * @param page
     *            返回结果的页码,默认1
     * @param count
     *            返回结果条数数量,默认50
     * @param sourt
     *            1为按关注时间排序，2为按昵称首字母排序，3为按更新时间排序
     * @param trim_status
     *            是否返回最后一条微博信息，0返回，1不返回，默认为0.
     */
    public UserInfoList getFriends(String uid, int page, int count, int sourt, int trim_status)
            throws WeiboException {
        String resp = mProxy.get(SERVER + "friendships/friends", WeiboClientHelper.getInstance()
                .getFriendsParam(uid, page, count, sourt, trim_status).getParams(), null,
                mRequestStrategy);
        return new UserInfoList(resp);
    }

    /**
     * 获取用户粉丝列表及每个粉丝的最新一条微博
     * 
     * @param uid
     *            返回此uid所关注人的排序结果,默认当前用户
     * @param page
     *            返回结果的页码,默认1
     * @param count
     *            返回结果条数数量 ,默认50
     * @param sourt
     *            1默认排序 2根据粉丝的粉丝数排序 3依据当前用户与其粉丝之间的互动频率进行排序，默认为1
     * @param trim_status
     *            是否返回最后一条微博信息 ，0返回，1不返回，默认为0
     */
    public UserInfoList getFollowers(String uid, int page, int count, int sourt, int trim_status)
            throws WeiboException {
        String resp = mProxy.get(SERVER + "friendships/followers", WeiboClientHelper.getInstance()
                .getFollowersParam(uid, page, count, sourt, trim_status).getParams(), null,
                mRequestStrategy);
        return new UserInfoList(resp);
    }

    /**
     * 当前登录用户批量关注指定ID的用户
     * 
     * @param uid
     *            要关注的用户ID，用逗号分隔的uid 最多20个
     */
    public UserInfo createFriendship(String uid, String cpt, String cptcode) throws WeiboException {
        Util.checkNullParams(uid);// uid是必选参数
        String resp = mProxy.get(SERVER + "friendships/create", WeiboClientHelper.getInstance()
                .createFriendshipParam(uid, cpt, cptcode).getParams(), null, mRequestStrategy);
        // return new UserInfo(resp);
        return JsonParser.parserUserInfo(resp);
    }

    /**
     * 取消对某用户的关注
     * 
     * @param uid
     *            要取消关注的用户ID
     */
    public UserInfo destroyFriendship(String uid, String cpt, String cptcode) throws WeiboException {
        Util.checkNullParams(uid);// uid是必选参数
        String resp = mProxy.get(SERVER + "friendships/destroy", WeiboClientHelper.getInstance()
                .destroyFriendshipParam(uid, cpt, cptcode).getParams(), null, mRequestStrategy);
        return JsonParser.parserUserInfo(resp);
    }

    /**
     * 移除粉丝
     * 
     * @param uid
     *            需要移除的用户ID
     */
    public UserInfo destroyFollowers(String uid, String cpt, String cptcode) throws WeiboException {
        Util.checkNullParams(uid);// uid是必选参数
        String resp = mProxy.get(SERVER + "friendships/followers_destroy", WeiboClientHelper
                .getInstance().destroyFollowersParam(uid, cpt, cptcode).getParams(), null,
                mRequestStrategy);
        // return new UserInfo(resp);
        return JsonParser.parserUserInfo(resp);
    }

    /**
     * 更新当前登录用户所关注的某个好友的备注信息
     * 
     * @param uid
     *            需要修改备注信息的用户ID
     * @param remark
     *            备注信息。需要URL Encode
     */
    public UserInfo updateRemark(String uid, String remark) throws WeiboException {
        Util.checkNullParams(uid, remark);// uid,remark是必选参数
        String resp = mProxy.get(SERVER + "friendships/remark_update", WeiboClientHelper
                .getInstance().updateRemarkParam(uid, remark).getParams(), null, mRequestStrategy);
        // return new UserInfo(resp);
        return JsonParser.parserUserInfo(resp);
    }

    /**
     * 返回与关键字相匹配的微博
     * 
     * @param q
     *            搜索的关键字
     */
    public StatusList searchStatuses(String q, int page, int count) throws WeiboException {
        String resp = mProxy.get(SERVER + "search/statuses", WeiboClientHelper.getInstance()
                .searchStatusesParam(q, page, count).getParams(), null, mRequestStrategy);
        // return new StatusList(resp);
        return JsonParser.parserStatusList(resp);
    }

    /**
     * 返回与关键字相匹配的微博用户
     * 
     * @param q
     *            搜索的关键字
     */
    public UserInfoList searchUsers(String q, int page, int count) throws WeiboException {
        String resp = mProxy.get(SERVER + "search/users", WeiboClientHelper.getInstance()
                .searchUsersParam(q, page, count).getParams(), null, mRequestStrategy);
        return new UserInfoList(resp);
    }

    /**
     * 返回与关键字相匹配的微博用户
     * 
     * @param q
     *            搜索的关键字
     * @param type
     *            搜索建议类型，0：关注人、1：最近联系的1000个粉丝、2：关注人关注的用户（已废弃）、3：互相关注用户，默认为0。
     * @param range
     *            搜索范围，0：只查用户昵称、1：只搜索当前用户对关注人的备注、2：都查，默认为2。
     */
    public AtUsers searchAtUsers(String q, int count, int type, int range) throws WeiboException {
        String resp = mProxy.get(SERVER + "search/at_users", WeiboClientHelper.getInstance()
                .searchAtUsersParam(q, count, type, range).getParams(), null, mRequestStrategy);
        return new AtUsers(resp);
    }

    /**
     * 获取指定用户的好友分组列表
     * 
     * @param uid
     *            如果为空，返回当前用户的分组列表
     */
    public GroupList getGroup(String uid, String mode, int is_encoded) throws WeiboException {
        String resp = mProxy.get(SERVER + "groups",
                WeiboClientHelper.getInstance().getGroupParam(uid, mode, is_encoded).getParams(),
                null, mRequestStrategy);
        return new GroupList(resp);
    }

    /**
     * 获取某一分组的timeline
     * 
     * @param uid
     *            分组的拥有者id，不传时，默认为当前用户的uid
     * @param list_id
     *            指定list的ID。当list为private时，当前登录用户必须为list的创建者
     * @param since_id
     *            若指定此参数，则只返回ID比since_id大的微博消息（即比since_id发表时间晚的微博消息）。默认为0
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的微博消息。默认为0
     * @param count
     *            返回结果条数数量，默认50,最大值为200
     * @param page
     *            指定返回结果的页码。默认值1
     * @param base_app
     *            是否基于当前应用来获取数据，0为不限制，1为限制本应用，默认值0
     * @param feature
     *            返回微博的过滤类型，0全部，1原创，2图片，3视频，4音乐，默认值0
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。默认0
     */
    public StatusList getGroupTimeLine(String uid, String list_id, String since_id, String max_id,
            int count, int page, int base_app, int feature, int is_encoded)
            throws WeiboApiException, WeiboException {
        String resp = mRequestStrategy.get(
                SERVER + "groups/timeline",
                WeiboClientHelper
                        .getInstance()
                        .getGroupTimeLineParam(uid, list_id, since_id, max_id, count, page,
                                base_app, feature, is_encoded).getParams(), null);
        return JsonParser.parserStatusList(resp);
    }

    /**
     * 获取某一好友分组下的成员列表，带成员备注信息
     * 
     * @param uid
     *            分组的拥有者id，不传时，默认为当前用户的uid
     * @param list_id
     *            好友分组的ID
     * @param count
     *            单页返回的记录条数，默认值20，最大值200
     * @param cursor
     *            将结果分页，每页包含20个list。由-1开始分页，定位一个id地址，
     *            通过比较id大小实现next_cursor和previous_cursor向前或向后翻页
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。默认0
     */
    public UserInfoList getGroupMembers(String uid, long list_id, int count, int cursor,
            int is_encoded) throws WeiboApiException, WeiboException {
        String resp = mRequestStrategy.get(SERVER + "groups/members", WeiboClientHelper
                .getInstance().getGroupMembersParam(uid, list_id, count, cursor, is_encoded)
                .getParams(), null);
        return new UserInfoList(resp);
    }

    /**
     * 批量获取指定用户作为成员的指定用户的好友分组信息
     * 
     * @param uids
     *            指定用户ID，用逗号隔开，每次不超过50个
     * @param owner_uid
     *            分组所属的用户ID，不传默认为当前用户的uid
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。默认0
     */
    public GroupArray getGroupListed(String uids, String owner_uid, int is_encoded)
            throws WeiboApiException, WeiboException {
        String resp = mRequestStrategy.get(SERVER + "groups/listed", WeiboClientHelper
                .getInstance().getGroupListedParam(uids, owner_uid, is_encoded).getParams(), null);
        return new GroupArray(resp);
    }

    /**
     * 获取某一好友分组下的所有成员列表
     * 
     * @param uid
     *            分组的拥有者id，不传时，默认为当前用户的uid
     * @param list_id
     *            好友分组的ID
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。默认0
     */
    public UserInfoList getGroupsAllMembers(String uid, long list_id, int is_encoded)
            throws WeiboApiException, WeiboException {
        String resp = mRequestStrategy.get(SERVER + "groups/allmembers", WeiboClientHelper
                .getInstance().getGroupAllMembersParam(uid, list_id, is_encoded).getParams(), null);
        return new UserInfoList(resp);
    }

    /**
     * 返回登录用户的所有分组及关注人
     * 
     * @param uid
     *            分组的拥有者id，不传时，默认为当前用户的uid
     */
    public GroupUserList getGroupUserAll(String uid) throws WeiboApiException, WeiboException {
        String resp = mRequestStrategy.get(SERVER + "groups/groupuserall", WeiboClientHelper
                .getInstance().getGroupUserAllParam(uid).getParams(), null);
        return new GroupUserList(resp);
    }

    /**
     * 添加用户到分组
     * 
     * @param uid
     *            分组的拥有者id，不传时，默认为当前用户的uid。可批量：加入多个用户到一个分组时，uid用逗号隔开
     * @param list_id
     *            好友分组的ID
     */
    public Result getGroupMembersAdd(String uid, long list_id) throws WeiboApiException,
            WeiboException {
        String resp = mRequestStrategy.get(SERVER + "groups/members_add", WeiboClientHelper
                .getInstance().getGroupMembersAddParam(uid, list_id).getParams(), null);
        return new Result(resp);
    }

    /**
     * 删除分组内的用户
     * 
     * @param uid
     *            分组的拥有者id，不传时，默认为当前用户的uid
     * @param list_id
     *            好友分组的ID
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义。默认0
     */
    public Group getGroupMembersDestroy(String uid, int list_id, int is_encoded)
            throws WeiboApiException, WeiboException {
        String resp = mRequestStrategy.get(SERVER + "groups/members_destroy", WeiboClientHelper
                .getInstance().getGroupMembersDestroyParam(uid, list_id, is_encoded).getParams(),
                null);
        return new Group(resp);
    }

    /**
     * 创建一个新的list，每个用户最多能够创建20个
     * 
     * @param name
     *            要创建的List的名称，不超过10个汉字，20个半角字符
     * @param mode
     *            创建的list是公共的(public)还是私有的(private)，默认为private。属性创建后不可更改
     */
    public GroupInfo groupCreate(String name, String mode, String description, int is_encoded)
            throws WeiboApiException, WeiboException {
        String resp = mRequestStrategy.post(
                SERVER + "groups/create",
                WeiboClientHelper.getInstance()
                        .getGroupCreateParam(name, mode, description, is_encoded).getParams(),
                WeiboClientHelper.getInstance()
                        .getGroupCreateParam(name, mode, description, is_encoded).getFiles(), null);
        return new GroupInfo(resp);
    }

    /**
     * 创建一个新的list，每个用户最多能够创建20个
     * 
     * @param name
     *            要创建的List的名称，不超过10个汉字，20个半角字符
     * @param list_id
     *            需要更新的list的ID。只能更新用户自己创建的LIST
     */
    public GroupInfo groupUpdate(String name, long list_id, String description, int is_encoded)
            throws WeiboApiException, WeiboException {
        Util.checkNullParams(name, String.valueOf(list_id));
        String resp = mRequestStrategy.post(
                SERVER + "groups/update",
                WeiboClientHelper.getInstance()
                        .getGroupUpdateParam(name, list_id, description, is_encoded).getParams(),
                WeiboClientHelper.getInstance()
                        .getGroupUpdateParam(name, list_id, description, is_encoded).getFiles(),
                null);
        return new GroupInfo(resp);
    }

    /**
     * 当前登录用户删除其所创建的指定LIST
     * 
     * @param list_id
     *            需要更新的list的ID。只能更新用户自己创建的LIST
     * @param is_encoded
     *            返回结果是否转义。0：不转义，1：转义
     */
    public GroupInfo groupDestroy(long list_id, int is_encoded) throws WeiboApiException,
            WeiboException {
        String resp = mRequestStrategy.post(SERVER + "groups/destroy", WeiboClientHelper
                .getInstance().getGroupDestroyParam(list_id, is_encoded).getParams(),
                WeiboClientHelper.getInstance().getGroupDestroyParam(list_id, is_encoded)
                        .getFiles(), null);
        return new GroupInfo(resp);
    }

    /**
     * 分组批量操作（add, edit, del）。（无对应底层接口）
     * 
     * @param deals
     *            deals为json字符串经过encoded，必须为数组，数组成员对象参见测试链接。批量操作即数组内可以有多个操作对象
     */
    public UserInfoList groupDeal(int deals) throws WeiboApiException, WeiboException {
        String resp = mRequestStrategy.get(SERVER + "groups/groupdeal", WeiboClientHelper
                .getInstance().getGroupDealsParam(deals).getParams(), null);
        return new UserInfoList(resp);
    }

    /**
     * 获取当前用户收到的最新私信列表。返回私信按最新时间排序。
     * 
     * @param page
     *            页码。注意：最多返回200条分页内容。默认值1。
     * @param count
     *            指定每页返回的记录条数。默认值50，最大值200。
     * @param since_id
     *            若指定此参数，则只返回ID比since_id大的私信（即比since_id发表时间晚的私信）。默认为0
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的微博消息。默认为0
     * @param is_encoded
     * 
     * @return
     * @throws WeiboException
     */
    public MessageList getNewestMessage(int page, int count, String since_id, String max_id,
            int is_encoded) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().getNewestMessageParam(page, count,
                since_id, max_id, is_encoded);
        String resp = mProxy.get(SERVER + "direct_messages", param.getParams(), null,
                mRequestStrategy);
        // return new MessageList(resp);
        return JsonParser.parserMessageList(resp);
    }

    /**
     * 获取与指定用户的往来私信列表
     * 
     * @param uid
     *            指定用户的ID
     * @param page
     * @param count
     * @param since_id
     * @param max_id
     * @param is_encoded
     * @return
     * @throws WeiboException
     */
    public MessageList getMessageList(String uid, int page, int count, String since_id,
            String max_id, int is_encoded) throws WeiboException {
        Util.checkNullParams(uid);
        RequestParam param = WeiboClientHelper.getInstance().getMessageListParam(uid, page, count,
                since_id, max_id, is_encoded);
        String resp = mProxy.get(SERVER + "direct_messages/conversation", param.getParams(), null,
                mRequestStrategy);
        // return new MessageList(resp);
        return JsonParser.parserMessageList(resp);
    }

    /**
     * 获取与当前登录用户有私信往来的用户列表，与该用户往来的最新私信。返回内容按照往来的最新时间排序
     * 
     * @param page
     * @param cursor
     *            返回结果的游标。默认为0
     * @param refresh_user_count
     *            是否更新用户计数，0：不更新，1：更新，默认为0
     * @param is_encoded
     * @return
     * @throws WeiboException
     */
    public MessageUserList getMessageUserList(int count, int cursor, int refresh_user_count,
            int is_encoded) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().getMessageUserListParam(count, cursor,
                refresh_user_count, is_encoded);
        String resp = mProxy.get(SERVER + "direct_messages/user_list", param.getParams(), null,
                mRequestStrategy);
        return new MessageUserList(resp);
    }

    /**
     * 发送一条私信
     * 
     * @param text
     *            要发生的消息内容
     * @param uid
     *            私信接收方的用户ID
     * @param screen_name
     *            私信接收方的微博昵称。和uid必须选填一个，建议使用uid
     * @param fids
     *            需要发送的附件ID。多个ID时以逗号分隔。上限为10个
     * @param id
     *            需要发送的微博ID
     * @param is_encoded
     * @return
     * @throws WeiboException
     */
    public Message createMessage(String text, String uid, String screen_name, String fids,
            String id, int is_encoded, String cpt, String cptcode) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().createMessageParam(text, uid,
                screen_name, fids, id, is_encoded, cpt, cptcode);
        String resp = mProxy.get(SERVER + "direct_messages/create", param.getParams(), null,
                mRequestStrategy);
        // return new Message(resp);
        return JsonParser.parserMessage(resp);
    }

    /**
     * 删除一条私信
     * 
     * @param id
     *            私信ID
     * @param is_encoded
     * @return
     * @throws WeiboException
     */
    public Message destroyMessage(String id, int is_encoded, String cpt, String cptcode)
            throws WeiboException {
        Util.checkNullParams(id);
        RequestParam param = WeiboClientHelper.getInstance().destroyMessageParam(id, is_encoded,
                cpt, cptcode);
        String resp = mProxy.get(SERVER + "direct_messages/destroy", param.getParams(), null,
                mRequestStrategy);
        // return new Message(resp);
        return JsonParser.parserMessage(resp);
    }

    /**
     * 批量删除私信，通过私信的ID
     * 
     * @param ids
     *            需要删除的私信ID
     * @param is_encoded
     * @return
     * @throws WeiboException
     */
    public MessageList destroyBatchMessageById(String ids, int is_encoded, String cpt,
            String cptcode) throws WeiboException {
        Util.checkNullParams(ids);
        RequestParam param = WeiboClientHelper.getInstance().destroyBatchMessageByIdParam(ids,
                is_encoded, cpt, cptcode);
        String resp = mProxy.get(SERVER + "direct_messages/destroy_batch", param.getParams(), null,
                mRequestStrategy);
        // return new MessageList(resp);
        return JsonParser.parserMessageList(resp);
    }

    /**
     * 批量删除私信，通过用户ID
     * 
     * @param uid
     *            用户UID。当传入当前登录用户UID时，表示删除当前登录用户的全部私信
     * @param is_encoded
     * @return
     * @throws WeiboException
     */
    public Result destroyBatchMessageByUid(String uid, int is_encoded, String cpt, String cptcode)
            throws WeiboException {
        Util.checkNullParams(uid);
        RequestParam param = WeiboClientHelper.getInstance().destroyBatchMessageByUidParam(uid,
                is_encoded, cpt, cptcode);
        String resp = mProxy.get(SERVER + "direct_messages/destroy_batch", param.getParams(), null,
                mRequestStrategy);
        return new Result(resp);
    }

    /**
     * 根据微博消息ID返回某条微博消息的评论列表的
     * 
     * @param id
     *            微博id值
     * @param page
     * @param count
     * @param since_id
     * @param max_id
     * @param filter_by_author
     *            筛选类型ID（0：全部，1：我关注的人，2：陌生人，3：v用户）默认为0
     * @param is_encoded
     * @return
     * @throws WeiboException
     */
    public CommentList getComments(String id, int page, int count, String since_id, String max_id,
            int filter_by_author, int is_encoded) throws WeiboException {
        String resp = mProxy.get(SERVER + "comments/show", WeiboClientHelper.getInstance()
                .getCommentsParam(id, page, count, since_id, max_id, filter_by_author, is_encoded)
                .getParams(), null, mRequestStrategy);
        return new CommentList(resp);
    }

    /**
     * 我所发出的评论列表
     * 
     * @param page
     * @param count
     * @param since_id
     * @param max_id
     * @param filter_by_source
     *            返回结果过滤。默认为0：返回全部。1：返回来自微博的评论；2：返回来自微群的评论
     * @return
     * @throws WeiboException
     */
    public CommentList getCommentsByMe(int page, int count, String since_id, String max_id,
            int filter_by_source) throws WeiboException {
        String resp = mProxy.get(SERVER + "comments/by_me", WeiboClientHelper.getInstance()
                .getCommentsByMeParam(page, count, since_id, max_id, filter_by_source).getParams(),
                null, mRequestStrategy);
        return new CommentList(resp);
    }

    /**
     * 用户接收到的评论列表的
     * 
     * @param page
     * @param count
     * @param since_id
     * @param max_id
     * @param filter_by_source
     *            筛选类型ID（0：全部，1：我关注的人，2：陌生人）默认为0
     * @return
     * @throws WeiboException
     */
    public CommentList getCommentsToMe(int page, int count, String since_id, String max_id,
            int filter_by_source) throws WeiboException {
        String resp = mProxy.get(SERVER + "comments/to_me", WeiboClientHelper.getInstance()
                .getCommentsToMeParam(page, count, since_id, max_id, filter_by_source).getParams(),
                null, mRequestStrategy);
        return new CommentList(resp);
    }

    /**
     * 返回最新n条提到登录用户的评论（即包含@username的微博评论）
     * 
     * @param page
     * @param count
     * @param since_id
     * @param max_id
     * @param filter_by_author
     * @param filter_by_source
     * @return
     * @throws WeiboException
     */
    public CommentList getCommentsMentionMe(int page, int count, String since_id, String max_id,
            int filter_by_author, int filter_by_source) throws WeiboException {
        String resp = mProxy.get(
                SERVER + "comments/mentions",
                WeiboClientHelper
                        .getInstance()
                        .getCommentsMentionMeParam(page, count, since_id, max_id, filter_by_author,
                                filter_by_source).getParams(), null, mRequestStrategy);
        return new CommentList(resp);
    }

    /**
     * 对一条微博信息进行评论
     * 
     * @param id
     * @param comment
     * @param comment_ori
     *            当回复一条转发微博的评论时，是否评论给原微博,需要进行URLEncode
     * @return
     * @throws WeiboException
     */
    public Comment createComment(String id, String comment, boolean comment_ori, String cpt,
            String cptcode) throws WeiboException {
        Util.checkNullParams(id, comment);
        String resp = mProxy.get(SERVER + "comments/create", WeiboClientHelper.getInstance()
                .createCommentParam(id, comment, comment_ori, cpt, cptcode).getParams(), null,
                mRequestStrategy);
        return new Comment(resp);
    }

    /**
     * 回复评论
     * 
     * @param cid
     *            要回复的评论ID
     * @param id
     *            要评论的微博消息ID
     * @param comment
     * @param without_mention
     *            1：回复中不自动加入“回复@用户名”，0：回复中自动加入“回复@用户名”.默认为0.
     * @param comment_ori
     * @return
     * @throws WeiboException
     */
    public Comment replyComment(String cid, String id, String comment, int without_mention,
            boolean comment_ori, String cpt, String cptcode) throws WeiboException {
        Util.checkNullParams(cid, id, comment);
        String resp = mProxy.get(SERVER + "comments/reply", WeiboClientHelper.getInstance()
                .replyCommentParam(cid, id, comment, without_mention, comment_ori, cpt, cptcode)
                .getParams(), null, mRequestStrategy);
        return new Comment(resp);
    }

    /**
     * 删除评论
     * 
     * @param cid
     *            要删除的评论ID。只能删除登录用户自己发布的评论
     * @return
     * @throws WeiboException
     */
    public Comment destroyComment(String cid, String cpt, String cptcode) throws WeiboException {
        Util.checkNullParams(cid);
        String resp = mProxy.get(SERVER + "comments/destroy", WeiboClientHelper.getInstance()
                .destroyCommentParam(cid, cpt, cptcode).getParams(), null, mRequestStrategy);
        return new Comment(resp);
    }

    /**
     * @param lat
     *            纬度，有效范围：-90.0到+90.0，+表示北纬。
     * @param lon
     *            经度，有效范围：-180.0到+180.0，+表示东经。
     * @param rang
     *            查询范围半径，默认为500，最大为11132，单位米。
     * @param q
     *            查询的关键词，必须进行URLencode。
     * @param page
     *            返回结果的页码.默认为1
     * @param count
     *            单页返回的记录条数，最大为50。
     * @param category
     *            查询的分类代码，取值范围见：分类代码对应表。
     * @param sort
     *            排序方式，0：按权重，默认为0。
     */
    public PositionList getNearByPosition(double lat, double lon, int rang, String q, int page,
            int count, int category, int sort) throws WeiboException {
        String resp = mProxy.get(
                SERVER + "place/nearby_pois",
                WeiboClientHelper.getInstance()
                        .getNearByPositionParam(lat, lon, rang, q, page, count, category, sort)
                        .getParams(), null, mRequestStrategy);
        return new PositionList(resp);
    }
    
    
    public StatusList getNearByTimeLine(double lat, double lon, int rang, 
    		int page, int count, long starttime, long endtime, int sort) throws WeiboException {
    	String resp = mProxy.get(
                SERVER + "place/nearby_timeline",
                WeiboClientHelper.getInstance()
                        .getNearByTimeLineParam(lat, lon, rang, page, count, starttime, endtime, sort)
                        .getParams(), null, mRequestStrategy);
        return JsonParser.parserStatusList(resp);
    }

    /**
     * 根据关键词按坐标点范围获取POI点的信息
     * @param q
     * 			查询的关键词，必须进行URLencode，与category参数必选其一。
     * @param category
     * 			查询的分类代码，与q参数必选其一，取值范围见：分类代码对应表。
     * @param coordinate
     * 			查询的中心点坐标，经度纬度用逗号分隔，与pid参数必选其一，pid优先。 
     * @param pid
     * 			查询的中心点POI的ID，与coordinate参数必选其一，pid优先。 
     * @param city
     * 			城市代码，默认为全国搜索。 
     * @param range
     * 			查询范围半径，默认为500，最大为500。 
     * @param page
     * 			返回结果的页码，默认为1，最大为40。 
     * @param count
     * 			单页返回的记录条数，默认为10，最大为20。 
     * @return
     * @throws WeiboException
     */
    public PositionList searchByGeo(String q, String category, String coordinate, 
    		String pid, String city, int range, int page, int count) throws WeiboException {
    	String resp = mProxy.get(
                SERVER + "location/pois/search/by_geo",
                WeiboClientHelper.getInstance()
                        .searchByGeoParam(q, category, coordinate, pid, city, range, page, count)
                        .getParams(), null, mRequestStrategy);
        return new PositionList(resp);
    }
     
    /**
     * @param lat
     *            纬度，有效范围：-90.0到+90.0，+表示北纬。
     * @param lon
     *            经度，有效范围：-180.0到+180.0，+表示东经。
     * @param rang
     *            查询范围半径，默认为500，最大为11132，单位米。
     * @param page
     *            返回结果的页码.默认为1
     * @param count
     *            返回结果条数数量，默认50
     * @param starttime
     *            开始时间，Unix时间戳。
     * @param endtime
     *            结束时间，Unix时间戳。
     * @param sort
     *            排序方式，0：按时间、1：按距离，默认为0。
     * @param trim_status
     *            是否返回最后一条微博信息，0返回，1不返回，默认为0.
     */
    public UserInfoList getNearByUsers(double lat, double lon, int rang, int page, int count,
            long starttime, long endtime, int sort, int trim_status) throws WeiboException {
        String resp = mProxy.get(
                SERVER + "place/nearby_users",
                WeiboClientHelper
                        .getInstance()
                        .getNearByUsersParam(lat, lon, rang, page, count, starttime, endtime, sort,
                                trim_status).getParams(), null, mRequestStrategy);
        return new UserInfoList(resp);
    }

    /**
     * 获取某人话题
     * 
     * @param uid
     * @param page
     * @param has_num
     *            是否返回话题数
     * @param count
     * @return
     * @throws WeiboException
     */
    public TopicList getTopicList(String uid, int page, boolean has_num, int count)
            throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().getTopicListParam(uid, page, has_num,
                count);
        String resp = mProxy.get(SERVER + "trends", param.getParams(), null, mRequestStrategy);
        return new TopicList(resp);
    }

    /**
     * 返回最近一小时内的热门话题
     * 
     * @param base_app
     *            是否基于当前应用来获取数据
     * @param count
     * @return
     * @throws WeiboException
     */
    public HotTopicList getHourlyTopicList(boolean base_app, int count) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().getHourlyTopicListParam(base_app,
                count);
        String resp = mProxy.get(SERVER + "trends/hourly", param.getParams(), null,
                mRequestStrategy);
        return new HotTopicList(resp);
    }

    /**
     * 返回最近一天内的热门话题
     * 
     * @param base_app
     *            是否基于当前应用来获取数据
     * @param count
     * @return
     * @throws WeiboException
     */
    public HotTopicList getDailyTopicList(boolean base_app, int count) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance()
                .getDailyTopicListParam(base_app, count);
        String resp = mProxy
                .get(SERVER + "trends/daily", param.getParams(), null, mRequestStrategy);
        return new HotTopicList(resp);
    }

    /**
     * 返回最近一周内的热门话题
     * 
     * @param base_app
     *            是否基于当前应用来获取数据
     * @param count
     * @return
     * @throws WeiboException
     */
    public HotTopicList getWeeklyTopicList(boolean base_app, int count) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().getWeeklyTopicListParam(base_app,
                count);
        String resp = mProxy.get(SERVER + "trends/weekly", param.getParams(), null,
                mRequestStrategy);
        return new HotTopicList(resp);
    }

    /**
     * 关注某话题
     * 
     * @param trend_name
     *            要关注的话题关键词
     * @return
     * @throws WeiboException
     */
    public Topic followTopic(String trend_name, String cpt, String cptcode) throws WeiboException {
        Util.checkNullParams(trend_name);
        RequestParam param = WeiboClientHelper.getInstance().followTopicParam(trend_name, cpt,
                cptcode);
        String resp = mProxy.get(SERVER + "trends/follow", param.getParams(), null,
                mRequestStrategy);
        return new Topic(resp);
    }

    /**
     * 取消关注某话题
     * 
     * @param trend_id
     *            要取消关注的话题ID
     * @return
     * @throws WeiboException
     */
    public Result destroyTopic(String trend_id, String cpt, String cptcode) throws WeiboException {
        Util.checkNullParams(trend_id);
        RequestParam param = WeiboClientHelper.getInstance().destroyTopicParam(trend_id, cpt,
                cptcode);
        String resp = mProxy.get(SERVER + "trends/destroy", param.getParams(), null,
                mRequestStrategy);
        return new Result(resp);
    }

    /**
     * 分页输出当前登录用户的黑名单列表，包括黑名单内的用户详细信息
     * 
     * @param page
     * @param count
     * @return
     * @throws WeiboException
     */
    public BlackList getBlackList(int page, int count) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().getBlackListParam(page, count);
        String resp = mProxy.get(SERVER + "blocks/blocking", param.getParams(), null,
                mRequestStrategy);
        return new BlackList(resp);
    }

    /**
     * 将某用户加入登录用户的黑名单, 返回黑名单用户信息
     * 
     * @param uid
     * @return
     * @throws WeiboException
     */
    public UserInfo createBlack(String uid) throws WeiboException {
        Util.checkNullParams(uid);
        RequestParam param = WeiboClientHelper.getInstance().createBlackParam(uid);
        String resp = mProxy.get(SERVER + "blocks/create", param.getParams(), null,
                mRequestStrategy);
        // return new UserInfo(resp);
        return JsonParser.parserUserInfo(resp);
    }

    /**
     * 将某用户加入登录用户的黑名单,返回用户信息
     * 
     * @param uid
     * @return
     * @throws WeiboException
     */
    public UserInfo destoryBlack(String uid) throws WeiboException {
        Util.checkNullParams(uid);
        RequestParam param = WeiboClientHelper.getInstance().destoryBlackParam(uid);
        String resp = mProxy.get(SERVER + "blocks/destroy", param.getParams(), null,
                mRequestStrategy);
        // return new UserInfo(resp);
        return JsonParser.parserUserInfo(resp);
    }

    /**
     * 举报某条信息
     * 
     * @param url
     *            举报url
     * @param content
     *            举报内容，必须做URLEncode，采用UTF-8编码
     * @param type
     *            默认4.取值为：1:博客，2：评论，3：用户，4：其他，5：群组，6：群微博，7：群评论，
     *            8：相册，9：相片，10：相册/相片的评论，11：私信，12：群长文本，13：精选集
     * @param rid
     *            对应举报类型的ID。当type不为4时，rid必须有值。例：当type为1时 rid需要传微博的id， type为3
     *            时，rid传用户的uid，以此类推
     * @param class_id
     *            举报内容类型， 值为：1.垃圾广告 2. 色情 3.其他， 4.身份造假，5 不实信息，6 人身攻击， 7
     *            泄露个人隐私，默认为3。
     * @return
     * @throws WeiboException
     */
    public Result report(String url, String content, int type, int rid, int class_id)
            throws WeiboException {
        Util.checkNullParams(url, content);
        RequestParam param = WeiboClientHelper.getInstance().reportParam(url, content, type, rid,
                class_id);
        String resp = mProxy.post(SERVER + "report/spam", param.getParams(), null, null,
                mRequestStrategy);
        return new Result(resp);
    }

    /**
     * 获取九宫格图标（含国际版接口）
     * 
     * @param from
     *            from值
     * @param lang
     *            语言参数统一使用lang，可选值 zh_CN（简体） zh_HK（繁体） en_US（英语）
     *            ms_MY（马来西亚语），ja_JP（日语）
     * @param version
     *            设置为world时表示国际版；不传或其它值为通用版
     * @param filter
     *            筛选参数，可选值1：全部 2：应用 3：精选，默认为1
     * @param ua
     *            客户端UA，格式参考UA统一格式，用来判断最低版本号
     */
    public SquareList getSquared(String from, String lang, String version, int filter, String ua)
            throws WeiboException {
        String resp = mProxy.get(SERVER + "client/squared", WeiboClientHelper.getInstance()
                .getSquaredParam(from, lang, version, filter, ua).getParams(), null,
                mRequestStrategy);
        return new SquareList(resp);
    }
    
    /**
     * 客户端新版本检测接口
     * @param from 
     * 			客户端from值，长度为十位
     * @param wm
     * 			网盟参数，决定该版本是否使用与该渠道，默认为 3333
     * @param manual
     * 			是否手动升级，1 为用户手动检测，空为客户端自动检测
     * @return
     * @throws WeiboException
     */
    public VersionInfo getLatestVersion(String from, String wm, String manual)
		    throws WeiboException {
		String resp = mProxy.get(SERVER + "client/version", WeiboClientHelper.getInstance()
		        .getLatestVersionParam(from, wm, manual).getParams(), null,
		        mRequestStrategy);
		return new VersionInfo(resp);
	}

    /**
     * 获取登录用户的各种消息未读数
     * 
     * @param callback
     *            JSONP回调函数，用于前端调用返回JS格式的信息
     */
    public Remind getUnreadRemind(String callback) throws WeiboException {
        APIRequestParam param = new APIRequestParam();
        param.addParam("callback", callback);
        String resp = mProxy.get(SERVER + "remind/unread_count", param.getParams(), null,
                mRequestStrategy);
        return new Remind(resp);
    }
    
    /**
     * 生成一张静态的地图图片 
     * @param center_coordinate 
     * 				中心点坐标，经度纬度用逗号分隔，与城市代码两者必选其一，中心点坐标优先。 
     * @param coordinates
     * 				地图上标点的坐标串，经度纬度用逗号分隔，多个坐标间用“|”分隔，最多不超过10个。
     * 				示例：coordinates=120.0358,23.1014|116.0358,38.1014。
     * @param size
     * 			生成的地图大小，格式为宽×高，最大值为800，默认为240，示例：size=480×360。 
     * @param format
     * 			生成的地图的图片格式，支持png、jpg等格式，参数全部为小写，默认为png
     * @param zoom
     * 			地图焦距等级，取值范围为1-17，默认为自适应大小。 
     * @param offset_x
     * 			x轴偏移方向，东移为正，西移为负，偏移单位为1/4图片宽度，
     * 			示例：offset_x=1，地图向右移动1/4。 
     * @param offset_y
     * 			y轴偏移方向，北移为正，南移为负，偏移单位为1/4图片高度，
     * 			示例：offset_y=1，地图向上移动1/4。 
     * @param font
     * 			字体格式，参数形式为：”字体,字体风格,字号,字体颜色,背景颜色,是否有背景“，
     * 			其中是否有背景（0：无，1：有），示例：font=宋体,1,20,0XFF0C0C,0XFFFF00,1，
     * 			默认值为“宋体,1,20,0XFF0CC0,0XFFFFE0,1”，字号最大不超过72号字，
     * 			如果coordinates参数不存在则font参数无效。 
     * @param lines
     * 			在地图中画一条线，参数形式为：“线的颜色,线的宽度,线的拐点坐标”，
     * 			拐点坐标经度纬度用逗号分隔，多个坐标间用“|”分隔，最多不超过10个，
     * 			示例：lines=0XFF0000,2,116.3256,39.9668|116.1256,39.9671，取值范围为：线的宽度0-50。 
     * @param polygons
     * 			在地图中画一个多边形，参数形式为：“边框颜色,边框宽度,填充颜色,填充透明度,
     * 			多边形的拐点坐标”，拐点坐标经度纬度用逗号分隔，多个坐标间用“|”分隔，最多不超过10个，
     * 			示例：polygons=0XFF0000,1,0XFF0000,50,116.3256,39.9668|116.1256,39.9671|116.3256,39.8671，
     * 			取值范围：边框宽度0-50，默认为1、填充透明度0（透明）-100（不透明），默认为50。 
     * @param scale
     * 			是否显示比例尺，true：是，false：否。 
     * @param traffic
     * 			是否需要叠加实际交通地图，true：是，false：否。 
     * @return
     */
    public MapImage getMapImage(String center_coordinate, String coordinates, String size, 
    		String format, int zoom, String offset_x, String offset_y, String font,
    		String lines, String polygons, boolean scale, boolean traffic) throws WeiboException {
        APIRequestParam param = new APIRequestParam();
        String resp = mProxy.get(SERVER + "location/base/get_map_image", param.getParams(), null,
                mRequestStrategy);
        return new MapImage(resp);
    }

    /**
     * 下载文件
     * 
     * @param url
     * @param cs
     * @param callback
     * @throws WeiboException
     */
    public void download(String url, CacheStrategy cs, IDownloadCallback callback)
            throws WeiboException {
        mProxy.download(url, cs, callback, null, mRequestStrategy);
    }

}

package com.sina.openapi;

import static com.clark.func.Functions.isNotBlank;
import static com.clark.func.Functions.isNotEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import oauth.signpost.OAuth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gdata.util.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sina.openapi.entity.AtSuggestion;
import com.sina.openapi.entity.CommentOpen;
import com.sina.openapi.entity.CountsResults;
import com.sina.openapi.entity.MessageOpen;
import com.sina.openapi.entity.Relationship;
import com.sina.openapi.entity.ShortUrl;
import com.sina.openapi.entity.Status;
import com.sina.openapi.entity.Topic;
import com.sina.openapi.entity.TrendInTrends;
import com.sina.openapi.entity.Trends;
import com.sina.openapi.entity.UserGroupCursor;
import com.sina.openapi.entity.UserShow;
import com.sina.openapi.entity.UserShowsCursor;
import com.sina.openapi.entity.UserUnread;

public final class RPCOpenAPI {
    public static final String HOST = "http://api.t.sina.com.cn/";
    public static final String HOST2 = "http://api.weibo.com/2/";
    public static final NumberFormat DECIMAL_FORMAT = new DecimalFormat(
            "#.#;-#.#");
    private static Log log = LogFactory.getLog(RPCOpenAPI.class);

    private WeiboHttpClient httpClient;
    private String APP_KEY;
    private Gson gson;

    public RPCOpenAPI(WeiboHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
        APP_KEY = httpClient.getConsumerKey();
        gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
                .create();
    }

    /**
     * 返回最新更新的20条微博消息。 <br>
     * 为避免资源浪费及提高效率，对消息缓存60秒。 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:false <br>
     * 请求数限制:true
     * 
     * @param base_app
     *            是否仅获取当前应用发布的信息。0为所有，1为仅本应用
     * @return XML示例: 注意geo地理位置字段仅对有地理位置的微博才有返回
     * @throws WeiboOpenAPIException
     */
    public Status[] getPublicTimeline(int count, int base_app)
            throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/public_timeline.json?source=")
                .append(APP_KEY);
        if (count > 0)
            sb.append("&count=").append(count);
        if (base_app == 0 || base_app == 1)
            sb.append("&base_app=").append(base_app);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public Status[] getPublicTimeline(int count) throws WeiboOpenAPIException {
        return getPublicTimeline(count, 0);
    }

    /**
     * 返回用户所有关注用户最新n条微博信息。 和用户“我的首页”返回内容相同。 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param since_id
     *            可选参数（微博信息ID）. 只返回ID比since_id大（比since_id时间晚的）的微博信息内容。
     * @param max_id
     *            可选参数（微博信息ID）. 返回ID不大于max_id的微博信息内容。
     * @param count
     *            可选参数. 每次返回的最大记录数，不能超过200，默认20.
     * @param page
     *            可选参数. 指定返回结果的页码。根据当前登录用户所关注的用户数及这些被关注用户发表的微博数，
     *            翻页功能最多能查看的总记录数会有所不同，通常最多能查看1000条左右。
     * @param baseApp
     *            可选参数. 是否基于当前应用来获取数据。1为限制本应用微博，0为不做限制。
     * @param feature
     *            可选参数. 微博类型，0全部，1原创，2图片，3视频，4音乐. 返回指定类型的微博信息内容。
     * @return
     * @throws WeiboOpenAPIException
     */
    public Status[] getFriendsTimeline(String since_id, String max_id,
            int count, int page, int baseApp, int feature)
            throws WeiboOpenAPIException {

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/friends_timeline.json?source=")
                .append(APP_KEY);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        if (baseApp >= 0 && baseApp <= 1) {
            sb.append("&baseApp=").append(baseApp);
        }
        if (feature >= 0 && feature <= 4) {
            sb.append("&feature=").append(feature);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public Status[] getFriendsTimeline(String since_id, String max_id,
            int count, int page) throws WeiboOpenAPIException {
        return getFriendsTimeline(since_id, max_id, count, page, 0, 0);
    }

    public Status[] initFriendsTimeline(int count) throws WeiboOpenAPIException {
        return getFriendsTimeline("", "", count, 1);
    }

    public Status[] getMoreFriendsTimeline(String max_id, int count)
            throws WeiboOpenAPIException {
        return getFriendsTimeline("", max_id, count, 1);
    }

    public Status[] refreshFriendsTimeline(String since_id, int count)
            throws WeiboOpenAPIException {
        return getFriendsTimeline(since_id, "", count, 1);
    }

    /**
     * 返回用户的发布的最近n条信息，和用户微博页面返回内容是一致的。 此接口也可以请求其他用户的最新发表微博。 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param id
     *            可选参数. 根据指定用户UID或微博昵称来返回微博信息。
     * @param user_id
     *            可选参数.
     *            用户UID，主要是用来区分用户UID跟微博昵称一样，产生歧义的时候，特别是在微博昵称为数字导致和用户Uid发生歧义。
     * @param screen_name
     *            可选参数.微博昵称，主要是用来区分用户UID跟微博昵称一样，产生歧义的时候。
     * @param since_id
     *            可选参数（微博信息ID）. 只返回ID比since_id大（比since_id时间晚的）的微博信息内容
     * @param max_id
     *            可选参数（微博信息ID）. 返回ID不大于max_id的微博信息内容。
     * @param count
     *            可选参数. 每次返回的最大记录数，最多返回200条，默认20。
     * @param page
     *            可选参数. 分页返回。注意：最多返回200条分页内容。
     * @param baseApp
     *            可选参数. 是否基于当前应用来获取数据。1为限制本应用微博，0为不做限制。
     * @param feature
     *            可选参数. 微博类型，0全部，1原创，2图片，3视频，4音乐. 返回指定类型的微博信息内容。
     * @return 默认返回最近15天以内的微博信息 由于分页限制，暂时最多只能返回用户最新的200条微博信息 用户最多只能请求到最近200条记录
     * @throws WeiboOpenAPIException
     */
    public Status[] getUserTimeline(String id, String user_id,
            String screen_name, String since_id, String max_id, int count,
            int page, int baseApp, int feature) throws WeiboOpenAPIException {

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/user_timeline.json?source=")
                .append(APP_KEY);
        if (isNotBlank(id)) {
            sb.append("&id=").append(id);
        }
        if (isNotBlank(user_id)) {
            sb.append("&user_id=").append(user_id);
        }
        if (isNotBlank(screen_name)) {
            sb.append("&screen_name=").append(screen_name);
        }
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        if (baseApp >= 0 && baseApp <= 1) {
            sb.append("&baseApp=").append(baseApp);
        }
        if (feature >= 0 && feature <= 4) {
            sb.append("&feature=").append(feature);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            return gson.fromJson(str, Status[].class);
        }
    }

    public Status[] getUserTimeline(String id, String since_id, String max_id,
            int count, int page) throws WeiboOpenAPIException {
        return getUserTimeline(id, null, null, since_id, max_id, count, page,
                0, 0);
    }

    public Status[] initUserTimeline(String id, int count)
            throws WeiboOpenAPIException {
        return refreshUserTimeline(id, null, count);
    }

    public Status[] refreshUserTimeline(String id, String since_id, int count)
            throws WeiboOpenAPIException {
        return getUserTimeline(id, since_id, "", count, 1);
    }

    public Status[] getMoreUserTimeline(String id, String max_id, int count)
            throws WeiboOpenAPIException {
        return getUserTimeline(id, null, max_id, count, 1);
    }

    /**
     * 返回最新n条@我的微博 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param since_id
     *            可选参数. 返回ID比数值since_id大（比since_id时间晚的）的提到。
     * @param max_id
     *            可选参数. 返回ID不大于max_id(时间不晚于max_id)的提到。
     * @param count
     *            可选参数. 每次返回的最大记录数（即页面大小），不大于200，默认为20。
     * @param page
     *            可选参数. 返回结果的页序号。注意：有分页限制。
     * @return
     * @throws WeiboOpenAPIException
     */
    public Status[] getMentionsTimeline(String since_id, String max_id,
            int count, int page) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/mentions.json?source=")
                .append(APP_KEY);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public Status[] initMentionsTimeline(int count)
            throws WeiboOpenAPIException {
        return getMentionsTimeline("", "", count, 1);
    }

    public Status[] refreshMentionsTimeline(String since_id, int count)
            throws WeiboOpenAPIException {
        return getMentionsTimeline(since_id, "", count, 1);
    }

    public Status[] getMoreMentionsTimeline(String max_id, int count)
            throws WeiboOpenAPIException {
        return getMentionsTimeline("", max_id, count, 1);
    }

    /**
     * 按时间顺序返回最新n条发送及收到的评论。 类似微博的friends_timeline接口。 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param since_id
     *            可选参数（评论ID）. 只返回ID比since_id大（比since_id时间晚的）的评论。
     * @param max_id
     *            可选参数（评论ID）. 返回ID不大于max_id的评论。
     * @param count
     *            可选参数. 每次返回的最大记录数，不大于200，默认20。
     * @param page
     *            可选参数. 返回结果的页序号。注意：有分页限制。
     * @return
     * @throws WeiboOpenAPIException
     */
    public CommentOpen[] getCommentsTimeline(String since_id, String max_id,
            int count, int page) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/comments_timeline.json?source=")
                .append(APP_KEY);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, CommentOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public CommentOpen[] initCommentsTimeline(int count)
            throws WeiboOpenAPIException {
        return getCommentsTimeline("", "", count, 1);
    }

    public CommentOpen[] refreshCommentsTimeline(String since_id, int count)
            throws WeiboOpenAPIException {
        return getCommentsTimeline(since_id, "", count, 1);
    }

    public CommentOpen[] getMoreCommentsTimeline(String max_id, int count)
            throws WeiboOpenAPIException {
        return getCommentsTimeline("", max_id, count, 1);
    }

    /**
     * 发出的评论 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param since_id
     *            可选参数（评论ID）. 只返回比since_id大（比since_id时间晚的）的评论
     * @param max_id
     *            可选参数（评论ID）. 返回ID不大于max_id的评论。
     * @param count
     *            可选参数. 每次返回的最大记录数，最多返回200条，默认为20。
     * @param page
     *            可选参数. 分页返回。注意：最多返回200条分页内容。
     * @return
     * @throws WeiboOpenAPIException
     */
    public CommentOpen[] getCommentsByMe(String since_id, String max_id,
            int count, int page) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/comments_by_me.json?source=")
                .append(APP_KEY);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, CommentOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public CommentOpen[] initCommentsByMe(int count)
            throws WeiboOpenAPIException {
        return getCommentsByMe("", "", count, 1);
    }

    public CommentOpen[] refreshCommentsByMe(String since_id, int count)
            throws WeiboOpenAPIException {
        return getCommentsByMe(since_id, "", count, 1);
    }

    public CommentOpen[] getMoreCommentsByMe(String max_id, int count)
            throws WeiboOpenAPIException {
        return getCommentsByMe("", max_id, count, 1);
    }

    /**
     * 返回当前登录用户收到的评论。 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param since_id
     *            若指定此参数，则只返回ID比since_id大的评论（比since_id发表时间晚）。
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的评论
     * @param count
     *            单页返回的记录条数。
     * @param page
     *            返回结果的页码。
     * @return rlt 评论列表
     * @throws WeiboOpenAPIException
     */
    public CommentOpen[] getCommentToMe(String since_id, String max_id,
            int count, int page) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/comments_to_me.json?source=")
                .append(APP_KEY);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, CommentOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public CommentOpen[] initCommentToMe(int count)
            throws WeiboOpenAPIException {
        return getCommentToMe("", "", count, 1);
    }

    public CommentOpen[] refreshCommentToMe(String since_id, int count)
            throws WeiboOpenAPIException {
        return getCommentToMe(since_id, "", count, 1);
    }

    public CommentOpen[] getMoreCommentToMe(String max_id, int count)
            throws WeiboOpenAPIException {
        return getCommentToMe("", max_id, count, 1);
    }

    /**
     * 返回指定微博的最新n条评论 <br>
     * 格式:xml, json, rss, atom <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param id_
     *            必选参数. 返回指定的微博ID
     * @param count
     *            可选参数. 每次返回的最大记录数（即页面大小），不大于200，默认为20。
     * @param page
     *            可选参数. 返回结果的页序号。注意：有分页限制。
     * @return 缺少参数，将返回400错误 微博ID不存在，将返回500错误
     * @throws WeiboOpenAPIException
     */
    public CommentOpen[] getCommentsOfTheStatus(String id_, int count, int page)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(id_));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/comments.json?source=")
                .append(APP_KEY).append("&id=").append(id_);
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, CommentOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 批量统计微博的评论数，转发数，一次请求最多获取100个。 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param ids_
     *            必填参数. 微博ID号列表，用逗号隔开
     * @return 缺少参数返回403 不存在的id(或者已删除微博的id)在结果集中会直接忽略，而不会返回其他提示。
     * @throws WeiboOpenAPIException
     */
    public CountsResults[] getFsCsCountsOfStatuses(String... ids_)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotEmpty(ids_));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/counts.json?source=").append(APP_KEY)
                .append("&ids=");
        for (int i = 0, length = ids_.length; i < length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(ids_[i]);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, CountsResults[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 获取当前用户Web未读消息数，包括@我的, 新评论，新私信，新粉丝数。 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserUnread getUnreadNumOfWeiboUser() throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/unread.json?source=").append(APP_KEY);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserUnread.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 获取单条ID的微博信息，作者信息将同时返回。 <br>
     * 格式:xml, json <br>
     * HTTP请求方式:GET <br>
     * 是否需要身份验证:true <br>
     * 请求数限制:true
     * 
     * @param blogId_
     *            必须参数(微博信息ID)，要获取已发表的微博ID,如ID不存在返回空
     * @return
     * @throws WeiboOpenAPIException
     */
    public Status showOneStatus(String blogId_) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(blogId_));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/show/").append(blogId_)
                .append(".json?source=").append(APP_KEY);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 如果用户新浪通行证身份验证成功且用户已经开通微博则返回 http状态为 200； 如果是不则返回401的状态和错误信息。
     * 此方法用了判断用户身份是否合法且已经开通微博。 <br>
     * 返回数据格式: xml, json <br>
     * 请求方式: GET <br>
     * 是否需要登录: true <br>
     * 请求数限制: false
     * 
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShow verifyAccountCredentials() throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("account/verify_credentials.json?source=")
                .append(APP_KEY);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShow.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 更新用户头像。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param image
     *            必须参数 必须为小于700K的有效的GIF, JPG图片. 如果图片大于500像素将按比例缩放。
     * @author
     * @throws WeiboOpenAPIException
     */
    public UserShow updateAccountProfileImage(File image)
            throws WeiboOpenAPIException {
        Preconditions.checkNotNull(image);

        if (!image.exists()) {
            throw new WeiboOpenAPIException("Can't find file:"
                    + image.getAbsolutePath());
        }

        try {
            return updateAccountProfileImage(new FileInputStream(image));
        } catch (FileNotFoundException e) {
            throw new WeiboOpenAPIException("Can't find file:"
                    + image.getAbsolutePath());
        }
    }

    /**
     * 更新用户头像。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param input
     * @param filename
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShow updateAccountProfileImage(InputStream input)
            throws WeiboOpenAPIException {
        Preconditions.checkNotNull(input);

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("account/update_profile_image.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("image", input);
        params.put("source", APP_KEY);
        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShow.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 更新当前登录用户在新浪微博上的基本信息。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true <br>
     * 下列参数必选其一，多选不限。
     * 
     * @param name
     *            可选参数 昵称，不超过20个汉字
     * @param gender
     *            可选参数 性别， m 表示男性，f 表示女性。
     * @param province
     *            可选参数 省份代码，参考省份城市编码表
     * @param city
     *            可选参数 城市代码，1000表示不指定具体城市。参考省份城市编码表
     * @param description
     *            可选参数 个人描述。不超过160个汉字
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public UserShow updateAccountProfile(String name, String gender,
            int province, int city, String description)
            throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("account/update_profile.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        if (isNotBlank(name))
            params.put("name", name);
        if ("m".equals(gender) || "f".equals(gender))
            params.put("gender", gender);
        if (province > 0)
            params.put("province", province);
        if (city > 0)
            params.put("city", city);
            params.put("description", description);
        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShow.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 删除微博。注意：只能删除自己发布的信息。 <br>
     * 格式:xml, json <br>
     * HTTP请求方式:DELETE <br>
     * 是否需要身份验证:true <br>
     * 请求数限制:true
     * 
     * @param blogId_
     *            必须参数. 要删除的微博ID.
     * @return 如果参数错误，将返回400错误
     * @throws WeiboOpenAPIException
     */
    public Status destroyOneStatus(String blogId_) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(blogId_));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/destroy/").append(blogId_)
                .append(".json?source=").append(APP_KEY);
        String str = httpClient.httpDelete(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 发布一条微博信息。请求必须用POST方式提交。 为防止重复，发布的信息与当前最新信息一样话，将会被忽略。 <br>
     * 格式:xml, json <br>
     * HTTP请求方式:POST <br>
     * 是否需要身份验证:true <br>
     * 请求数限制:true
     * 
     * @param text_
     *            必填参数， 要更新的微博信息。必须做URLEncode,信息内容部超过140个汉字,为空返回400错误。
     * @param in_reply_to_status_id
     *            可选参数，@ 需要回复的微博信息ID, 这个参数只有在微博内容以 @username 开头才有意义。（即将推出）。
     * @param lat
     *            可选参数，纬度，发表当前微博所在的地理位置，有效范围 -90.0到+90.0,
     *            +表示北纬。只有用户设置中geo_enabled=true时候地理位置才有效。(仅对受邀请的合作开发者开放)
     * @param lon
     *            可选参数，经度。有效范围-180.0到+180.0, +表示东经。(仅对受邀请的合作开发者开放)
     * @param annotations
     *            可选参数，元数据，主要是为了方便第三方应用记录一些适合于自己使用的信息。每条微博可以包含一个或者多个元数据。
     *            请以json字串的形式提交，字串长度不超过512个字符，具体内容可以自定
     * @return 如果没有登录或超过发布上限，将返回403错误 如果in_reply_to_status_id不存在，将返回500错误
     *         系统将忽略重复发布的信息。每次发布将比较最后一条发布消息，如果一样将被忽略。因此用户不能连续提交相同信息。
     * @throws WeiboOpenAPIException
     */
    public Status updateStatus(String text_, String in_reply_to_status_id,
            double lat, double lon) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(text_));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/update.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        // URLEncode两次
        params.put("status", OAuth.percentEncode(OAuth.percentEncode(text_)));
        if (!(in_reply_to_status_id == null || in_reply_to_status_id.toString()
                .length() == 0)) {
            params.put("in_reply_to_status_id", in_reply_to_status_id);
        }
        if (Math.abs(lat - 0.) > 0.0001) {
            params.put("lat", DECIMAL_FORMAT.format(lat));
        }
        if (Math.abs(lon - 0.) > 0.0001) {
            params.put("long", DECIMAL_FORMAT.format(lon));
        }

        // if (annotations != null) {
        // StringBuilder jsonSb = new StringBuilder();
        // jsonSb.append("[");
        // for (int i = 0; i < annotations.length; i++) {
        // if (annotations[0] != null) {
        // jsonSb.append(annotations[0].toJSONString());
        // if (i < annotations.length - 1)
        // jsonSb.append(",");
        // }
        // jsonSb.append("]");
        // }
        // if (jsonSb != null)
        // params.put("annotations", jsonSb.toString());
        // }

        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public Status updateStatus(String text_, String in_reply_to_status_id)
            throws WeiboOpenAPIException {
        return updateStatus(text_, in_reply_to_status_id, 0.f, 0.f);
    }

    public Status updateStatus(String text_) throws WeiboOpenAPIException {
        return updateStatus(text_, null, 0.f, 0.f);
    }

    /**
     * 上传图片及发布微博信息。请求必须用POST方式提交。 为防止重复，发布的信息与当前最新信息一样话，将会被忽略。 目前上传图片大小限制为<1M。 <br>
     * 格式:xml, json <br>
     * HTTP请求方式:POST, 必须用Multipart/form-data方式 <br>
     * 是否需要身份验证:true <br>
     * 请求数限制:true
     * 
     * @param text_
     *            必填参数， 要更新的微博信息。必须做URLEncode,信息内容不超过140个汉字。支持全角、半角字符。
     * @param file_
     *            必填参数。仅支持JPEG,GIF,PNG图片,为空返回400错误。目前上传图片大小限制为<1M。
     * @param lat
     *            可选参数，纬度，发表当前微博所在的地理位置，有效范围 -90.0到+90.0,
     *            +表示北纬。只有用户设置中geo_enabled=true时候地理位置才有效。(保留字段，暂不支持)
     * @param lon
     *            可选参数，经度。有效范围-180.0到+180.0, +表示东经。(保留字段，暂不支持)
     * @return
     * @throws WeiboOpenAPIException
     *             如果没有登录或超过发布上限，将返回403错误
     *             系统将忽略重复发布的信息。每次发布将比较最后一条发布消息，如果一样将被忽略。因此用户不能连续提交相同信息
     *             。发布成功返回发布的信息ID,否则返回为空。 如果使用的Oauth认证，图片参数pic不参与签名。
     */
    public Status uploadStatus(String text_, File file_, double lat, double lon)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(text_));
        Preconditions.checkNotNull(file_);

        if (!file_.exists()) {
            throw new WeiboOpenAPIException("Can't find file:"
                    + file_.getAbsolutePath());
        }

        try {
            return uploadStatus(text_, new FileInputStream(file_), lat, lon);
        } catch (FileNotFoundException e) {
            throw new WeiboOpenAPIException("Can't find file:"
                    + file_.getAbsolutePath());
        }
    }

    public Status uploadStatus(String text_, InputStream input_, double lat,
            double lon) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(text_));
        Preconditions.checkNotNull(input_);

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/upload.json");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pic", input_);
        params.put("source", APP_KEY);
        // // 替换 +、% 号
        // text_ = text_.replace("+", "");
        // text_ = text_.replace("%", "");
        params.put("status", text_);

        if (Math.abs(lat - 0.) > 0.0001) {
            params.put("lat", DECIMAL_FORMAT.format(lat));
        }
        if (Math.abs(lon - 0.) > 0.0001) {
            params.put("long", DECIMAL_FORMAT.format(lon));
        }

        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public Status uploadStatus(String text_, InputStream input_)
            throws WeiboOpenAPIException {
        return uploadStatus(text_, input_, 0.f, 0.f);
    }

    public Status uploadStatus(String text_, File file_)
            throws FileNotFoundException, WeiboOpenAPIException {
        return uploadStatus(text_, file_, 0.f, 0.f);
    }

    /**
     * 转发一条微博消息。请求必须用POST方式提交。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param id
     *            必须参数 要转发的微博ID
     * @param text_
     *            可选参数 添加的转发文本。必须做URLEncode,信息内容不超过140个汉字。如不填则默认为“转发微博”。
     * @param is_comment
     *            可选参数
     *            是否在转发的同时发表评论。0表示不发表评论，1表示发表评论给当前微博，2表示发表评论给原微博，3是1、2都发表。默认为0。
     * @return rlt 微博列表
     * @throws WeiboOpenAPIException
     * 
     * @author
     */
    public Status repostStatus(String id, String text_, int is_comment)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(id));
        Preconditions.checkArgument(isNotBlank(text_));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/repost.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        params.put("id", id);
        if (isNotBlank(text_)) {
            params.put("status", text_);
        }
        if (is_comment >= 0 && is_comment <= 3) {
            params.put("is_comment", is_comment);
        }

        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 对一条微博信息进行评论。请求必须用POST方式提交。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param id
     *            必选参数 要评论的微博消息ID
     * @param comment
     *            必选参数 评论内容。必须做URLEncode,信息内容不超过140个汉字。
     * @param cid
     *            可选参数 要回复的评论ID。
     *            (如果提供了正确的cid参数，则该接口的表现为回复指定的评论。此时id参数将被忽略。即使cid参数代表的评论不属于id参数代表的微博消息
     *            ，通过该接口发表的评论信息直接回复cid代表的评论。)
     * @param without_mention
     *            1：回复中不自动加入“回复@用户名”，0：回复中自动加入“回复@用户名”.默认为0.
     * @return rlt 评论列表
     * @throws UnsupportedEncodingException
     * @throws
     * 
     * @author
     */
    public CommentOpen commentStatus(String id, String comment, String cid,
            int without_mention, int comment_ori, int is_repost)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(id));
        Preconditions.checkArgument(isNotBlank(comment));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/comment.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        params.put("id", id);
        params.put("comment", comment);
        if (isNotBlank(cid)) {
            params.put("cid", cid);
        }
        if (without_mention == 0 || without_mention == 1) {
            params.put("without_mention", without_mention);
        }
        if (comment_ori == 0 || comment_ori == 1) {
            params.put("comment_ori", comment_ori);
        }
        if (is_repost == 0 || is_repost == 1) {
            params.put("is_repost", is_repost);
        }
        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, CommentOpen.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 删除评论。注意：只能删除登录用户自己发布的评论， 不可以删除其他人的评论。 <br>
     * 格式：xml， json <br>
     * POST/DELETE <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param :id 必选参数 欲删除的评论ID，该参数为一个REST风格参数
     * 
     * @author
     */
    public CommentOpen destroyMyCommentOfOneStatus(String _id)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(_id));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST)
                .append("statuses/comment_destroy/" + _id + ".json?source=")
                .append(APP_KEY);

        String str = httpClient.httpDelete(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, CommentOpen.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 批量删除评论。注意：只能删除登录用户自己发布的评论， 不可以删除其他人的评论。 <br>
     * 格式：xml， json <br>
     * POST/DELETE <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param ids
     *            必选参数 欲删除的一组评论ID，用半角逗号隔开，最多20个
     * 
     * @author
     */
    public CommentOpen[] destroyMyCommentsOfOneStatus(String... ids)
            throws WeiboOpenAPIException {
        Preconditions.checkNotNull(ids);

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/comment/destroy_batch.json?source=")
                .append(APP_KEY);
        StringBuilder str_ids = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (i != 0) {
                str_ids.append(",");
            }
            str_ids.append(ids[i]);
        }
        sb.append("&ids=").append(str_ids);
        String str = httpClient.httpDelete(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, CommentOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 回复评论。请求必须用POST方式提交。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param cid
     *            必选参数 要回复的评论ID。
     * @param comment
     *            必选参数 要回复的评论内容。必须做URLEncode,信息内容不超过140个汉字。
     * @param blogId
     *            必选参数 要评论的微博消息ID
     * @param without_mention
     *            1：回复中不自动加入“回复@用户名”，0：回复中自动加入“回复@用户名”.默认为0.
     * @return rlt 评论列表
     * 
     * @author
     */
    public CommentOpen replyCommentOfOneStatus(String cid, String comment,
            String blogId, int without_mention) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(cid));
        Preconditions.checkArgument(isNotBlank(comment));
        Preconditions.checkArgument(isNotBlank(blogId));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/reply.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        params.put("cid", cid);
        params.put("id", blogId);
        params.put("comment", comment);
        if (without_mention == 0 || without_mention == 1)
            params.put("without_mention", without_mention);
        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, CommentOpen.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 按用户ID或昵称返回用户资料以及用户的最新发布的一条微博消息。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param _id
     *            可选参数 用户ID(int64)或者昵称(string)。该参数为一个REST风格参数。
     * @param user_id
     *            可选参数
     *            用户ID，主要是用来区分用户ID跟微博昵称。当微博昵称为数字导致和用户ID产生歧义，特别是当微博昵称和用户ID一样的时候
     *            ，建议使用该参数
     * @param screen_name
     *            可选参数 微博昵称，主要是用来区分用户UID跟微博昵称，当二者一样而产生歧义的时候，建议使用该参数
     * @return rlt
     * 
     * @author
     */
    public UserShow showOneUser(String _id, String user_id, String screen_name)
            throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        if (!(_id == null || _id.toString().length() == 0))
            sb.append(HOST).append("users/show/" + _id + ".json?source=")
                    .append(APP_KEY);
        else
            sb.append(HOST).append("users/show.json?source=").append(APP_KEY);

        if (user_id != null && user_id.length() > 0)
            sb.append("&user_id=").append(user_id);
        if (!(screen_name == null || screen_name.toString().length() == 0))
            sb.append("&screen_name=").append(screen_name);

        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShow.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 
     * @param _id
     *            所查询用户ID
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShow showOneUserUsingId(String _id) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(_id));
        return showOneUser(null, _id, null);
    }

    /**
     * 
     * @param screenName
     *            所查询用户昵称
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShow showOneUserUsingScreenName(String screenName)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(screenName));
        return showOneUser(null, null, screenName);
    }

    /**
     * 获取用户关注列表及每个关注用户的最新一条微博， 返回结果按关注时间倒序排列，最新关注的用户排在最前面。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param _id
     *            可选参数 用户ID(int64)或者昵称(string)。该参数为一个REST风格参数。
     * @param user_id
     *            可选参数
     *            用户ID，主要是用来区分用户ID跟微博昵称。当微博昵称为数字导致和用户ID产生歧义，特别是当微博昵称和用户ID一样的时候
     *            ，建议使用该参数
     * @param screen_name
     *            可选参数 微博昵称，主要是用来区分用户UID跟微博昵称，当二者一样而产生歧义的时候，建议使用该参数
     * @param cursor
     *            可选参数
     *            用于分页请求，请求第1页cursor传-1，在返回的结果中会得到next_cursor字段，表示下一页的cursor
     *            。next_cursor为0表示已经到记录末尾。
     * @param count
     *            可选参数 每页返回的最大记录数，最大不能超过200，默认为20。
     * @author
     */
    public UserShowsCursor getFriendsOfUser(String _id, String user_id,
            String screen_name, int cursor, int count)
            throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        if (isNotBlank(user_id)) {
            sb.append(HOST)
                    .append("statuses/friends/" + user_id + ".json?source=")
                    .append(APP_KEY);
        } else {
            sb.append(HOST).append("statuses/friends.json?source=")
                    .append(APP_KEY);
        }

        if (isNotBlank(user_id))
            sb.append("&user_id=").append(user_id);
        if (isNotBlank(screen_name))
            sb.append("&screen_name=").append(screen_name);
        if (cursor >= -1) {
            sb.append("&cursor=").append(cursor);
        }
        if (count > 0 && count <= 200) {
            sb.append("&count=").append(count);
        }

        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShowsCursor.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 
     * @param uid
     *            所查询用户的 id
     * @param cursor
     * @param count
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShowsCursor getFriendsOfUserUsingId(String uid, int cursor,
            int count) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(uid));
        return getFriendsOfUser("", uid, "", cursor, count);
    }

    /**
     * 
     * @param screenName
     *            所查询用户的昵称
     * @param cursor
     * @param count
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShowsCursor getFriendsOfUserUsingScreenName(String screenName,
            int cursor, int count) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(screenName));
        return getFriendsOfUser("", "", screenName, cursor, count);
    }

    /**
     * 获取用户粉丝列表及每个粉丝的最新一条微博， 返回结果按粉丝的关注时间倒序排列， 最新关注的粉丝排在最前面。每次返回20个,
     * 通过cursor参数来取得多于20的粉丝。 注意目前接口最多只返回5000个粉丝。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：false <br>
     * 请求数限制：true
     * 
     * @param _id
     *            可选参数 用户ID(int64)或者昵称(string)。该参数为一个REST风格参数。
     * @param user_id
     *            可选参数
     *            用户ID，主要是用来区分用户ID跟微博昵称。当微博昵称为数字导致和用户ID产生歧义，特别是当微博昵称和用户ID一样的时候
     *            ，建议使用该参数
     * @param screen_name
     *            可选参数 微博昵称，主要是用来区分用户UID跟微博昵称，当二者一样而产生歧义的时候，建议使用该参数
     * @param cursor
     *            可选参数
     *            用于分页请求，请求第1页cursor传-1，在返回的结果中会得到next_cursor字段，表示下一页的cursor
     *            。next_cursor为0表示已经到记录末尾。
     * @param count
     *            可选参数 每页返回的最大记录数，最大不能超过200，默认为20。
     * @author
     */
    public UserShowsCursor getFansOfUser(String _id, String user_id,
            String screen_name, int cursor, int count)
            throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        if (isNotBlank(user_id)) {
            sb.append(HOST)
                    .append("statuses/followers/" + user_id + ".json?source=")
                    .append(APP_KEY);
        } else {
            sb.append(HOST).append("statuses/followers.json?source=")
                    .append(APP_KEY);
        }

        if (isNotBlank(user_id))
            sb.append("&user_id=").append(user_id);
        if (isNotBlank(screen_name))
            sb.append("&screen_name=").append(screen_name);
        if (cursor >= -1)
            sb.append("&cursor=").append(cursor);
        if (count > 0 && count <= 200)
            sb.append("&count=").append(count);

        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShowsCursor.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 
     * @param uid
     *            所查询用户的 id
     * @param cursor
     * @param count
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShowsCursor getFansOfUserUsingId(String uid, int cursor,
            int count) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(uid));
        return getFansOfUser("", uid, "", cursor, count);
    }

    /**
     * 
     * @param screenName
     *            所查询用户的昵称
     * @param cursor
     * @param count
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShowsCursor getFansOfUserUsingScreenName(String screenName,
            int cursor, int count) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(screenName));
        return getFansOfUser("", "", screenName, cursor, count);
    }

    // /**
    // * 更新当前登录用户所关注的某个好友的备注信息。 <br>
    // * 格式：xml， json <br>
    // * POST <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param friend_user_id
    // * 必选参数 需要修改备注信息的用户ID。
    // * @param remark
    // * 必选参数 备注信息。需要URL Encode。
    // *
    // * @author
    // */
    // public UserShow updateRemarkOfTheFriend(String friend_user_id, String
    // remark)
    // throws WeiboOpenAPIException {
    // checkNull(remark);
    //
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("user/friends/update_remark.json");
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("source", APP_KEY);
    // params.put("user_id", friend_user_id);
    // params.put("remark", remark);
    //
    // String str = httpClient.post(sb.toString(), params);
    // UserShow rlt = null;
    // try {
    // rlt = jsonParser.parseUser(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }

    /**
     * 返回登录用户的最新收到的n条私信，每条私信包含发送者和接受者的详细信息。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param since_id
     *            可选参数 若指定此参数，则只返回ID比since_id大的记录（比since_id发送时间晚）。
     * @Param max_id 可选参数 若指定此参数，则返回ID小于或等于max_id的记录
     * @param count
     *            可选参数 200。 单页返回的记录条数
     * @param page
     *            可选参数 返回结果的页码。
     * @author
     * */
    public MessageOpen[] getReceivedMessageTimeline(String since_id,
            String max_id, int count, int page) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("direct_messages.json?source=").append(APP_KEY);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }

        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, MessageOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public MessageOpen[] initReceivedMessageTimeline(int count)
            throws WeiboOpenAPIException {
        return getReceivedMessageTimeline("", "", count, 1);
    }

    public MessageOpen[] refreshReceivedMessageTimeline(String since_id,
            int count) throws WeiboOpenAPIException {
        return getReceivedMessageTimeline(since_id, "", count, 1);
    }

    public MessageOpen[] getMoreReceivedMessageTimeline(String max_id, int count)
            throws WeiboOpenAPIException {
        return getReceivedMessageTimeline("", max_id, count, 1);
    }

    /**
     * 获取与某个用户的私信对话
     * 
     * @param id
     *            那个用户的id
     */
    public MessageOpen[] getConversationWithSomebody(String id, int page,
            int count, String since_id, String max_id)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(id));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("direct_messages/with/").append(id)
                .append(".json?source=").append(APP_KEY);
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }

        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, MessageOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public MessageOpen[] initConversationWithSomebody(String id, int count)
            throws WeiboOpenAPIException {
        return getConversationWithSomebody(id, 1, count, "", "");
    }

    public MessageOpen[] refreshConversationWithSomebody(String id,
            String since_id, int count) throws WeiboOpenAPIException {
        return getConversationWithSomebody(id, 1, count, since_id, "");
    }

    public MessageOpen[] getMoreConversationWithSomebody(String id,
            String max_id, int count) throws WeiboOpenAPIException {
        return getConversationWithSomebody(id, 1, count, "", max_id);
    }

    /**
     * 返回登录用户已发送的最新n条私信，每条私信包含发送者和接受者的详细信息。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param since_id
     *            必选参数 若指定此参数，则只返回ID比since_id大的记录（比since_id发送时间晚）。
     * @param max_id
     *            可选参数 若指定此参数，则返回ID小于或等于max_id的记录
     * @param count
     *            可选参数 单页返回的记录条数。
     * @param page
     *            可选参数 返回结果的页码。
     * @return
     * @throws WeiboOpenAPIException
     * @author
     */
    public MessageOpen[] getMessageSentTimeline(String since_id, String max_id,
            int count, int page) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("direct_messages/sent.json?source=")
                .append(APP_KEY);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }

        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, MessageOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public MessageOpen[] initMessageSentTimeline(int count)
            throws WeiboOpenAPIException {
        return getMessageSentTimeline("", "", count, 1);
    }

    public MessageOpen[] refreshMessageSentTimeline(String since_id, int count)
            throws WeiboOpenAPIException {
        return getMessageSentTimeline(since_id, "", count, 1);
    }

    public MessageOpen[] getMoreMessageSentTimeline(String max_id, int count)
            throws WeiboOpenAPIException {
        return getMessageSentTimeline("", max_id, count, 1);
    }

    /**
     * 发送一条私信，请求必须使用POST方式提交。发送成功将返回完整的私信消息，包括发送者和接收者的详细信息。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param id
     *            私信接收方的用户ID(int64)或者微博昵称(string)
     * @param text
     *            要发生的消息内容，需要做URLEncode，文本大小必须小于300个汉字。
     * @param user_id
     *            私信接收方的用户ID，在用户ID与微博昵称容易混淆的时候，建议使用该参数。
     * @param screen_name
     *            私信接收方的微博昵称，在用户ID与微博昵称容易混淆的时候，建议使用该参数。
     * @throws WeiboOpenAPIException
     * 
     * @author
     */
    public MessageOpen sendNewMessage(String id, String text, String user_id,
            String screen_name) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(text));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("direct_messages/new.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        params.put("text", text);
        if (isNotBlank(id)) {
            params.put("id", id);
        }
        if (isNotBlank(user_id)) {
            params.put("user_id", user_id);
        }
        if (isNotBlank(screen_name)) {
            params.put("screen_name", screen_name);
        }
        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, MessageOpen.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 
     * @param uid
     *            目标用户的id
     * @param text
     * @return
     * @throws WeiboOpenAPIException
     */
    public MessageOpen sendNewMessageUsingUserID(String uid, String text)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(uid));
        return sendNewMessage(null, text, uid, null);
    }

    /**
     * 
     * 
     * @param screenName
     *            目标用户的昵称
     * @param text
     * @return
     * @throws WeiboOpenAPIException
     */
    public MessageOpen sendNewMessageUsingScreenName(String screenName,
            String text) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(screenName));
        return sendNewMessage(null, text, null, screenName);
    }

    /**
     * 根据ID删除登录用户收到的私信。 <br>
     * 格式：xml， json <br>
     * POST/DELETE <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param id
     * @return
     * @throws WeiboOpenAPIException
     * @throws JSONException
     * 
     * @author
     */
    public MessageOpen destroyMessage(String id_) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(id_));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("direct_messages/destroy/").append(id_)
                .append(".json?source=").append(APP_KEY);

        String str = httpClient.httpDelete(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, MessageOpen.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 批量删除当前登录用户的私信。 <br>
     * 格式：xml， json <br>
     * POST/DELETE <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param ids
     *            必选参数 想要删除私信的id，多个id之间用半角逗号分割，每次提交系统最多能接受20个私信ID。
     * @author
     */
    public MessageOpen[] destroyMessages(String... ids)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotEmpty(ids));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("direct_messages/destroy_batch.json?source=")
                .append(APP_KEY);
        StringBuilder str_ids = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (i != 0) {
                str_ids.append(",");
            }
            str_ids.append(ids[i]);
        }
        sb.append("&ids=").append(str_ids);

        String str = httpClient.httpDelete(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, MessageOpen[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 返回与当前登陆用户有私信往来的用户列表 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param count
     *            可选参数 返回用户条数，默认为50
     * @param cursor
     *            可选参数 返回结果的起始位置
     * 
     * @author
     */
    public UserShowsCursor getMessageInteractionUserList(int count, int cursor)
            throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("direct_messages/userlist.json?source=")
                .append(APP_KEY);
        if (cursor > 0) {
            sb.append("&cursor=").append(cursor);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShowsCursor.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 关注一个用户。关注成功则返回关注人的资料，目前的最多关注2000人。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param _id
     *            可选参数 要关注的用户ID(int64)或者微博昵称(string)，该参数是一个REST风格的参数。用法见注意事项
     * @param user_id
     *            可选参数 要关注的用户ID，为了避免用户的ID与微博昵称相同而产生混淆的情况，推荐使用该参数。
     * @param screen_name
     *            可选参数 要关注的用户微博昵称，为了避免用户的ID与微博昵称相同而产生混淆的情况，推荐使用该参数。
     * 
     * @author
     */
    public UserShow beFriendWithSomebody(String _id, String user_id,
            String screen_name) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        if (isNotBlank(_id)) {
            sb.append(HOST).append("friendships/create/" + _id + ".json");
        } else {
            sb.append(HOST).append("friendships/create.json");
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        if (isNotBlank(user_id)) {
            params.put("user_id", user_id);
        }
        if (isNotBlank(screen_name)) {
            params.put("screen_name", screen_name);
        }

        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShow.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public UserShow beFriendWithSomebodyUsingUserID(String uid)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(uid));
        return beFriendWithSomebody(null, uid, null);
    }

    public UserShow beFriendWithSomebodyUsingScreenName(String screenName)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(screenName));
        return beFriendWithSomebody(null, null, screenName);
    }

    /**
     * 取消对某用户的关注。 <br>
     * 格式：xml， json <br>
     * POST/DELETE <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param _id
     *            可选参数 要关注的用户ID(int64)或者微博昵称(string)，该参数是一个REST风格的参数。用法见注意事项
     * @param user_id
     *            可选参数 要关注的用户ID，为了避免用户的ID与微博昵称相同而产生混淆的情况，推荐使用该参数。
     * @param screen_name
     *            可选参数 要关注的用户微博昵称，为了避免用户的ID与微博昵称相同而产生混淆的情况，推荐使用该参数。
     * 
     * @author
     */
    public UserShow cancelFriendshipWithSomebody(String _id, String user_id,
            String screen_name) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        if (isNotBlank(_id)) {
            sb.append(HOST).append("friendships/destroy/" + _id + ".json?");
        } else {
            sb.append(HOST).append("friendships/destroy.json?");
        }
        sb.append("source=").append(APP_KEY);
        if (isNotBlank(user_id)) {
            sb.append("&user_id=").append(user_id);
        }
        if (isNotBlank(screen_name)) {
            sb.append("&screen_name=").append(screen_name);
        }

        String str = httpClient.httpDelete(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShow.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public UserShow cancelFriendshipWithSomebodyUsingUserID(String uid)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(uid));
        return cancelFriendshipWithSomebody(null, uid, null);
    }

    public UserShow cancelFriendshipWithSomebodyUsingScreenName(
            String screenName) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(screenName));
        return cancelFriendshipWithSomebody(null, null, screenName);
    }

    // /**
    // * 将某用户加入登录用户的黑名单 <br>
    // * 格式：xml， json <br>
    // * POST <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param user_id
    // * 必选参数 要加入黑名单的用户ID
    // * @param screen_name
    // * 可选参数 要加入黑名单的用户微博昵称
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public UserShow createBlacklistUser(String user_id, String screen_name)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("blocks/create.json");
    //
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("source", APP_KEY);
    // params.put("user_id", user_id);
    // if (!(screen_name == null || screen_name.toString().length() == 0))
    // params.put("screen_name", screen_name);
    // UserShow rlt = null;
    // String str = httpClient.post(sb.toString(), params);
    // try {
    // rlt = (jsonParser.parseUser(str));
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 将某用户从当前登录用户的黑名单中移除 <br>
    // * 格式：xml， json <br>
    // * POST/DELETE <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param user_id
    // * 必选参数 要移出黑名单的用户ID
    // * @param screen_name
    // * 可选参数 要移出黑名单的用户微博昵称
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public UserShow destroyBlacklistUser(String user_id, String screen_name)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("blocks/destroy.json?source=").append(APP_KEY);
    // sb.append("&user_id=").append(user_id);
    // if (!(screen_name == null || screen_name.toString().length() == 0))
    // sb.append("&screen_name=").append(screen_name);
    // String str = httpClient.httpDelete(sb.toString());
    // UserShow rlt = null;
    // try {
    // rlt = (jsonParser.parseUser(str));
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 检测指定用户是否在登录用户的黑名单内。 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param user_id
    // * 必选参数 要检测的用户ID
    // * @param screen_name
    // * 可选参数 要检测的用户微博昵称
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public boolean isBlacklistUser(String user_id, String screen_name)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("blocks/exists.json?source=").append(APP_KEY);
    // sb.append("&user_id=").append(user_id);
    // if (!(screen_name == null || screen_name.toString().length() == 0))
    // sb.append("&screen_name=").append(screen_name);
    // String str = httpClient.httpGet(sb.toString());
    // JSONObject jsonObj = null;
    // try {
    // jsonObj = new JSONObject(str);
    // } catch (JSONException e) {
    //
    // throw new WeiboOpenAPIException(e);
    // }
    //
    // boolean rlt = jsonObj.optBoolean("result");
    // return rlt;
    // }
    //
    // /**
    // * 分页输出当前登录用户的黑名单列表，包括黑名单内的用户详细信息。 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param page
    // * 可选参数 页码。
    // * @param count
    // * 可选参数 单页记录数。
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public UserShow[] getBlacklist(int page, int count)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("blocks/blocking.json?source=").append(APP_KEY);
    // if (page > 0)
    // sb.append("&page=").append(page);
    // if (count > 0)
    // sb.append("&count=").append(count);
    // String str = httpClient.httpGet(sb.toString());
    // UserShow[] rlt = null;
    // try {
    // rlt = (jsonParser.parseUserList(str));
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }

    /**
     * 返回登录用户最近收藏的20条微博消息，和用户在主站上“我的收藏”页面看到的内容是一致的。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param page
     *            可选参数 页码。
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public Status[] getFavoriteStatuseList(int page)
            throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("favorites.json?source=").append(APP_KEY);
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 收藏一条微博消息。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param id
     *            必选参数 要收藏的微博消息ID。
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public Status createFavorateStatus(String id) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(id));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("favorites/create.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        params.put("id", id);
        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 删除微博收藏。注意：只能删除自己收藏的信息。 <br>
     * 格式：xml， json <br>
     * POST/DELETE <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param _id
     *            必选参数 要删除的已收藏的微博消息ID。该参数为一个REST风格参数。
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public Status destroyFavoriteStatus(String _id)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(_id));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("favorites/destroy/").append(_id)
                .append(".json?source=").append(APP_KEY);
        String str = httpClient.httpDelete(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 删除微博收藏。注意：只能删除自己收藏的信息。 <br>
     * 格式：xml， json <br>
     * POST/DELETE <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param ids
     *            必选参数 要删除的一组已收藏的微博消息ID，用半角逗号隔开，一次提交最多提供20个ID。
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public Status[] destroyFavoriteStatuses(String... ids)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotEmpty(ids));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("favorites/destroy_batch.json?source=")
                .append(APP_KEY);
        StringBuilder strIds = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (i != 0) {
                strIds.append(",");
            }
            strIds.append(ids[i]);
        }
        sb.append("&ids=").append(strIds);
        String str = httpClient.httpDelete(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    // /**
    // * 返回系统推荐的用户列表。 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param category
    // * 可选参数 分类，返回某一类别的推荐用户，默认为default。分类，返回某一类别的推荐用户，默认为default。<br>
    // * 如果不在以下分类中，返回空列表<br>
    // * default：人气关注 <br>
    // * ent：影视名星 <br>
    // * hk_famous：港台名人 <br>
    // * model：模特<br>
    // * cooking：美食&健康 <br>
    // * sport：体育名人 <br>
    // * finance：商界名人 <br>
    // * tech：IT互联网 <br>
    // * singer：歌手<br>
    // * writer：作家 <br>
    // * moderator：主持人 <br>
    // * medium：媒体总编 <br>
    // * stockplayer：炒股高手
    // * @return rlt 用户列表
    // *
    // * @author
    // */
    // public UserShow[] getHotUsers(HotuserType category)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("users/hot.json?source=").append(APP_KEY)
    // .append("&category=").append(category);
    //
    // String str = httpClient.httpGet(sb.toString());
    // UserShow[] res = null;
    // try {
    // res = jsonParser.parseUserList(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return res;
    // }
    //
    // public enum HotuserType {
    // DEFAULT("default"), FILMSTARS("ent"), HK_STARS("hk_famous"), MODEL(
    // "model"), COOKING("cooking"), SPORT("sport"), FINANCE("finance"), IT(
    // "tech"), SINGER("singer"), WRITER("writer"), MODERATOR(
    // "moderator"), MEDIUM("medium"), STOCK_PLAYER("stockplayer");
    //
    // private String name;
    //
    // private HotuserType(String name) {
    // this.name = name;
    // }
    //
    // @Override
    // public String toString() {
    // return name;
    // }
    // }

    /**
     * 返回与指定的一个或多个条件相匹配的微博。 <br>
     * 格式：json <br>
     * GET <br>
     * 需要登录：false <br>
     * 请求数限制：true
     * 
     * @param q
     *            搜索的关键字。必须进行URL Encode
     * @param page
     *            可选参数 页码，从1开始，默认为1。
     * @param count
     *            可选参数 每页返回的微博数。（默认返回10条，最大200条）
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public Status[] searchStatuses(String q, int page, int count)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(q));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/search.json?source=").append(APP_KEY);
        sb.append("&q=").append(q);
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 返回与指定的一个或多个条件相匹配的微博。 <br>
     * 格式：xml,json <br>
     * GET <br>
     * 需要登录：false <br>
     * 请求数限制：true
     * 
     * @param q
     *            可选参数 搜索的关键字。必须进行URL_encode
     * @param filter_ori
     *            可选参数 过滤器，是否为原创，0为全部，5为原创，4为转发。默认为0。
     * @param filter_pic
     *            可选参数 过滤器。是否包含图片。0为全部，1为包含，2为不包含。
     * @param fuid
     *            可选参数 微博作者的用户ID。
     * @param province
     *            可选参数 省份ID，参考省份城市编码表
     * @param city
     *            可选参数 城市ID，参考省份城市编码表
     * @param starttime
     *            可选参数 开始时间，Unix时间戳
     * @param endtime
     *            可选参数 结束时间，Unix时间戳
     * @param page
     *            可选参数 页码
     * @param count
     *            可选参数 每页返回的微博数。（默认返回10条，最大200条。）
     * @param needcount
     *            可选参数 返回结果中是否包含返回记录数。true则返回搜索结果记录数。
     * @param base_app
     *            可选参数 是否按照当前应用信息对搜索结果进行过滤。当值为1时，仅返回通过该应用发送的微博消息。
     * @param callback
     *            可选参数 仅JSON方式支持，用于JSONP跨域数据访问。
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    private Status[] deepSearchStatuses(String q, int filter_ori,
            int filter_pic, int fuid, int province, int city, Date starttime,
            Date endtime, int page, int count, boolean needcount, int base_app)
            throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/search.json?source=").append(APP_KEY);
        if (isNotBlank(q)) {
            sb.append("&q=").append(q);
        }
        if (filter_ori == 0 || filter_ori == 5 || filter_ori == 4) {
            sb.append("&filter_ori=").append(filter_ori);
        }
        if (filter_pic == 0 || filter_pic == 1 || filter_pic == 2) {
            sb.append("&filter_pic=").append("filter_pic");
        }
        if (fuid > 0) {
            sb.append("&fuid=").append(fuid);
        }
        if (province > 0) {
            sb.append("&province=").append(province);
        }
        if (city > 0) {
            sb.append("&city=").append(city);
        }
        if (starttime != null) {
            sb.append("&starttime=").append(starttime.getTime());
        }
        if (endtime != null) {
            sb.append("&endtime=").append(endtime.getTime());
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (needcount) {
            sb.append("&needcount=").append(needcount);
        }
        if (base_app == 1) {
            sb.append("&base_app=").append(base_app);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 返回与指定的一个或多个条件相匹配的微博。 <br>
     * 格式：xml,json <br>
     * GET <br>
     * 需要登录：false <br>
     * 请求数限制：true
     * 
     * @param q
     *            可选参数 搜索的关键字。必须进行URL_encode
     * @param page
     *            可选参数 页码
     * @param count
     *            可选参数 每页返回的微博数。（默认返回10条，最大200条。）
     * @return
     * @throws WeiboOpenAPIException
     */
    public Status[] deepSearchStatuses(String q, int page, int count)
            throws WeiboOpenAPIException {
        return deepSearchStatuses(q, -1, -1, -1, -1, -1, null, null, page,
                count, false, 0);
    }

    /**
     * 获取某用户的话题。 <br>
     * 格式：xml,json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param user_id
     *            必选参数 用户id
     * @param page
     *            可选参数 页码
     * @param count
     *            可选参数 每页返回的记录数
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public Topic[] getTopicList(String user_id, int page, int count)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(user_id));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("trends.json?source=").append(APP_KEY);
        sb.append("&user_id=").append(user_id);
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Topic[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 获取某话题下的微博消息。 <br>
     * 格式：xml,json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param topic_name
     *            必选参数 话题关键词。
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public Status[] getStatusesOfTopic(String topic_name)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(topic_name));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("trends/statuses.json?source=").append(APP_KEY);
        sb.append("&trend_name=").append(topic_name);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            return gson.fromJson(str, Status[].class);
        }
    }

    /**
     * 关注某话题。 <br>
     * 格式：xml,json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param topic_name
     *            必选参数 要关注的话题关键词。
     * @return topicid 话题id
     * 
     * @author
     * @throws WeiboException
     */
    public String followTheTopic(String topic_name)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(topic_name));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("trends/follow.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        params.put("trend_name", topic_name);
        String str = httpClient.post(sb.toString(), params);

        try {
            JsonElement jsonElement = new JsonParser().parse(str);
            if (jsonElement != null && jsonElement.isJsonObject()) {
                final JsonObject jsonObject = jsonElement.getAsJsonObject();
                return jsonObject.getAsJsonPrimitive("topicid").getAsString();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        throw new WeiboOpenAPIException(str);
    }

    /**
     * 取消对某话题的关注。 <br>
     * 格式：xml,json <br>
     * DELETE <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param topic_id
     *            必选参数 要取消关注的话题ID。
     * 
     * @author
     * @throws WeiboException
     */
    public boolean cancelFollowTheTopic(String topic_id)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(topic_id));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("trends/destroy.json?source=").append(APP_KEY);
        sb.append("&trend_id=").append(topic_id);
        String str = httpClient.httpDelete(sb.toString());
        return parserBooleanResult(str);
    }

    /**
     * 解析形如 {"result":true} 的字符串
     * 
     * @param str
     * @return
     * @throws WeiboOpenAPIException
     */
    private boolean parserBooleanResult(String str)
            throws WeiboOpenAPIException {
        return parserSingleJsonBoolean(str, "result");
    }

    /**
     * 解析形如 {"friends":true} 的字符串
     * 
     * @param str
     * @return
     * @throws WeiboOpenAPIException
     */
    private boolean parserBooleanFriends(String str)
            throws WeiboOpenAPIException {
        return parserSingleJsonBoolean(str, "friends");
    }

    private boolean parserSingleJsonBoolean(String str, String key)
            throws WeiboOpenAPIException {
        try {
            JsonElement jsonElement = new JsonParser().parse(str);
            if (jsonElement != null && jsonElement.isJsonObject()) {
                final JsonObject jsonObject = jsonElement.getAsJsonObject();
                return jsonObject.getAsJsonPrimitive(key).getAsBoolean();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        throw new WeiboOpenAPIException(str);
    }

    /**
     * 返回最近一小时内的热门话题。 <br>
     * 格式：xml,json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param base_app
     *            可选参数 是否基于当前应用来获取数据。1表示基于当前应用来获取数据。
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public Trends getHotTopicsHourly(int base_app) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("trends/hourly.json?source=").append(APP_KEY);
        if (base_app == 1) {
            sb.append("&base_app=").append(base_app);
        }
        String str = httpClient.httpGet(sb.toString());
        return parserTrends(str);
    }

    /**
     * 解析形如 Trends 对象
     * 
     * @param str
     * @return
     * @throws WeiboOpenAPIException
     */
    private Trends parserTrends(String str) throws WeiboOpenAPIException {
        try {
            JsonElement jsonElement = new JsonParser().parse(str);
            if (jsonElement != null && jsonElement.isJsonObject()) {
                final JsonObject jsonObject = jsonElement.getAsJsonObject();
                Trends trends = new Trends();
                trends.setAs_of(jsonObject.getAsJsonPrimitive("as_of")
                        .getAsInt());
                final JsonObject trendsObject = jsonObject
                        .getAsJsonObject("trends");
                for (Map.Entry<String, JsonElement> entry : trendsObject
                        .entrySet()) {
                    final JsonElement jsonElement2 = entry.getValue();
                    if (jsonElement2.isJsonArray()) {
                        synchronized (gson) {
                            trends.setTrend(gson.fromJson(
                                    jsonElement2.toString(),
                                    TrendInTrends[].class));
                        }
                        break;
                    } else {
                        throw new Exception(jsonElement2 + " isn't JsonArray!");
                    }
                }

                return trends;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        throw new WeiboOpenAPIException(str);
    }

    // /**
    // * 返回最近一天内的热门话题。 <br>
    // * 格式：xml,json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param base_app
    // * 可选参数 是否基于当前应用来获取数据。1表示基于当前应用来获取数据。
    // *
    // * @author
    // */
    // public Trends getHotTopicsDaily(int base_app) throws
    // WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("trends/daily.json?source=").append(APP_KEY);
    // if (base_app == 1)
    // sb.append("&base_app=").append(base_app);
    // else
    // sb.append("&base_app=").append(0);
    // String str = httpClient.httpGet(sb.toString());
    // Trends rlt = null;
    // try {
    // rlt = jsonParser.parseThrends(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 返回最近一周内的热门话题。 <br>
    // * 格式：xml,json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param base_app
    // * 可选参数 是否基于当前应用来获取数据。1表示基于当前应用来获取数据。
    // *
    // * @author
    // */
    // public Trends getHotTopicsWeekly(int base_app) throws
    // WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("trends/weekly.json?source=").append(APP_KEY);
    // if (base_app == 1)
    // sb.append("&base_app=").append(base_app);
    // else
    // sb.append("&base_app=").append(0);
    // String str = httpClient.httpGet(sb.toString());
    // Trends rlt = null;
    // try {
    // rlt = jsonParser.parseThrends(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }

    /*
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * ********************* 以下为不常用 API *******************
     */

    /**
     * 返回一条原创微博消息(原创微博就是被转发微博)的最新n条转发微博消息。 本接口无法对非原创微博进行查询。 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param blogId
     *            要获取转发微博列表的原创微博ID
     * @param since_id
     *            若指定此参数，则只返回ID比since_id大的评论（比since_id发表时间晚）。
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的评论
     * @param count
     *            单页返回的记录条数。
     * @param page
     *            返回结果的页码。
     * @return rlt 微博列表
     * @throws WeiboOpenAPIException
     */
    public Status[] getRepostTimelineOfStatus(String blogId, String since_id,
            String max_id, int count, int page) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(blogId));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("repost_timeline.json?source=").append(APP_KEY)
                .append("&id=").append(blogId);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public Status[] initRepostTimelineOfStatus(String id, int count)
            throws WeiboOpenAPIException {
        return getRepostTimelineOfStatus(id, "", "", count, 1);
    }

    public Status[] refreshRepostTimelineOfStatus(String id, String since_id,
            int count) throws WeiboOpenAPIException {
        return getRepostTimelineOfStatus(id, since_id, "", count, 1);
    }

    public Status[] getMoreRepostTimelineOfStatus(String id, String max_id,
            int count) throws WeiboOpenAPIException {
        return getRepostTimelineOfStatus(id, "", max_id, count, 1);
    }

    /**
     * 获取用户最新转发的n条微博消息 <br>
     * 格式:xml, json <br>
     * GET <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param uid
     *            要获取转发微博列表的用户ID
     * @param since_id
     *            若指定此参数，则只返回ID比since_id大的评论（比since_id发表时间晚）。
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的评论
     * @param count
     *            单页返回的记录条数。
     * @param page
     *            返回结果的页码。
     * @return rlt 微博列表
     * @throws WeiboOpenAPIException
     */
    public Status[] getRepostTimelineOfTheUser(String uid, String since_id,
            String max_id, int count, int page) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(uid));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("repost_by_me.json?source=").append(APP_KEY)
                .append("&id=").append(uid);
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public Status[] initRepostTimelineOfTheUser(String id, int count)
            throws WeiboOpenAPIException {
        return getRepostTimelineOfTheUser(id, "", "", count, 1);
    }

    public Status[] refreshRepostTimelineOfTheUser(String id, String since_id,
            int count) throws WeiboOpenAPIException {
        return getRepostTimelineOfTheUser(id, since_id, "", count, 1);
    }

    public Status[] getMoreRepostTimelineOfTheUser(String id, String max_id,
            int count) throws WeiboOpenAPIException {
        return getRepostTimelineOfTheUser(id, "", max_id, count, 1);
    }

    /**
     * 将当前登录用户的某种新消息的未读数为0。 可以清零的计数类别有：1. 评论数，2. @me数，3. 私信数，4. 关注数 <br>
     * 格式:xml, json <br>
     * POST <br>
     * 需要登录:true <br>
     * 请求数限制:true
     * 
     * @param type
     *            可以清零的计数类别有：1. 评论数，2. @ me数，3. 私信数，4. 关注数
     * @return rlt 是否清除成功
     * @throws WeiboOpenAPIException
     */
    public boolean resetUnreadcountOfUser(int type)
            throws WeiboOpenAPIException {
        if (type < 1 || type > 4) {
            throw new IllegalArgumentException(
                    "type must be greater than 1 and less than 4!");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("statuses/reset_count.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        params.put("type", type);
        String str = httpClient.post(sb.toString(), params);
        return parserBooleanResult(str);
    }

    /**
     * 查看用户A是否关注了用户B。如果用户A关注了用户B，则返回true，否则返回false。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param user_a
     *            必选参数 用户A的用户ID
     * @param user_b
     *            必选参数 用户B的用户ID
     * @return rlt 是否被关注
     * 
     * @author
     */
    public boolean verifyFriendshipsExists(String user_a, String user_b)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(user_a)
                && isNotBlank(user_b));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("friendships/exists.json?source=")
                .append(APP_KEY).append("&user_a=").append(user_a)
                .append("&user_b=").append(user_b);

        String str = httpClient.httpGet(sb.toString());
        return parserBooleanFriends(str);
    }

    /**
     * 返回两个用户关注关系的详细情况 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @param source_id
     *            可选参数 源用户UID
     * @Param source_screen_name 可选参数 源微博昵称
     * @param target_id
     *            可选参数 要判断的目标用户ID
     * @param target_screen_name
     *            可选参数 要判断的目标用户的微博昵称 其中target_id 和 target_screen_name 必须其一
     * @author
     * @throws WeiboOpenAPIException
     */
    public Relationship showFriendshipBetween(String source_id,
            String source_screen_name, String target_id,
            String target_screen_name) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("friendships/show.json?source=").append(APP_KEY);
        if (isNotBlank(target_id)) {
            sb.append("&target_id=").append(target_id);
        }
        if (isNotBlank(target_screen_name)) {
            sb.append("&target_screen_name=").append(target_screen_name);
        }
        if (isNotBlank(source_id)) {
            sb.append("&source_id=").append(source_id);
        }
        if (isNotBlank(source_screen_name)) {
            sb.append("&source_screen_name=").append(source_screen_name);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Relationship.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    public Relationship showFriendshipUsingScreenName(
            String source_screen_name, String target_screen_name)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(source_screen_name)
                && isNotBlank(target_screen_name));
        return showFriendshipBetween(null, source_screen_name, null,
                target_screen_name);
    }

    public Relationship showFriendshipUsingUserID(String source_id,
            String target_id) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(source_id)
                && isNotBlank(target_id));
        return showFriendshipBetween(source_id, null, target_id, null);
    }

    // /**
    // * 测试接口。可以用来对系统的可访问性进行基本的冒烟测试. <br>
    // * 格式:xml, json <br>
    // * GET <br>
    // * 需要登录:false <br>
    // * 请求数限制:false
    // *
    // * @param
    // * @return rlt OK字段
    // * @throws WeiboOpenAPIException
    // *
    // * @author
    // */
    // public boolean test() throws WeiboOpenAPIException {
    // boolean rlt;
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("help/test.json?source=").append(APP_KEY);
    // String str = httpClient.httpGet(sb.toString());
    // JSONObject jsonObj;
    // try {
    // jsonObj = new JSONObject(str);
    // } catch (JSONException e) {
    // throw new WeiboOpenAPIException(e);
    // }
    // rlt = jsonObj.optBoolean("OK");
    // return rlt;
    // }
    //
    // /**
    // * 返回用户关注的一组用户的ID列表 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param _id
    // * 可选参数 用户的ID(int64)或者微博昵称(string)。该参数为REST风格的参数
    // * @param user_id
    // * 可选参数 要获取的好友列表所属的用户的ID。
    // * @param screen_name
    // * 可选参数 要获取的好友列表所属的用户微博昵称。
    // * @param count
    // * 可选参数 单页记录数。
    // * @param cursor
    // * 可选参数
    // * 游标。单页最多返回5000条记录。通过增加或减少cursor值来获取更多的关注列表。如果提供该参数，返回结果中将给出下一页的起始游标
    // * 。
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // * @throws JSONException
    // */
    // public UserIdList friends_ids(String _id, int user_id, String
    // screen_name,
    // int count, int cursor) throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // if (!(_id == null || _id.toString().length() == 0))
    // sb.append(HOST).append("friends/ids/" + _id + ".json?");
    // else
    // sb.append(HOST).append("friends/ids.json?");
    // sb.append("source=").append(APP_KEY);
    // if (user_id > 0)
    // sb.append("&user_id").append(user_id);
    // if (!(screen_name == null || screen_name.toString().length() == 0))
    // sb.append("&screen_name=").append(screen_name);
    // if (count > 0)
    // sb.append("&count=").append(count);
    // if (cursor > 0)
    // sb.append("&cursor=").append(cursor);
    //
    // String str = httpClient.httpGet(sb.toString());
    // UserIdList rlt = null;
    // try {
    // rlt = jsonParser.parseUserIdList(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 返回用户的粉丝用户ID列表 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param _id
    // * 可选参数 用户的ID(int64)或者微博昵称(string)。该参数为REST风格的参数
    // * @param user_id
    // * 可选参数 要获取的粉丝列表所属的用户的ID。
    // * @param screen_name
    // * 可选参数 要获取的粉丝列表所属的用户微博昵称
    // * @param count
    // * 可选参数 单页记录数。
    // * @param cursor
    // * 可选参数
    // * 游标。单页最多返回5000条记录。通过增加或减少cursor值来获取更多的粉丝列表。如果提供该参数，返回结果中将给出下一页的起始游标
    // * 。
    // *
    // * @author
    // */
    // public UserIdList followers_ids(String _id, int user_id,
    // String screen_name, int count, int cursor)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // if (!(_id == null || _id.toString().length() == 0))
    // sb.append(HOST).append("follower/ids/" + _id + ".json?");
    // else
    // sb.append(HOST).append("follower/ids.json?");
    // sb.append("source=").append(APP_KEY);
    // if (user_id > 0)
    // sb.append("&user_id").append(user_id);
    // if (!(screen_name == null || screen_name.toString().length() == 0))
    // sb.append("&screen_name=").append(screen_name);
    // if (count > 0)
    // sb.append("&count=").append(count);
    // if (cursor > 0)
    // sb.append("&cursor=").append(cursor);
    //
    // String str = httpClient.httpGet(sb.toString());
    // UserIdList rlt = null;
    // try {
    // rlt = jsonParser.parseUserIdList(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 设置隐私信息 <br>
    // * 格式：xml， json <br>
    // * POST <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param comment
    // * 可选参数 谁可以评论此账号的微薄。0：所有人，1：我关注的人。默认为0。
    // * @param message
    // * 可选参数 谁可以给此账号发私信。0：所有人，1：我关注的人。默认为1。
    // * @param realname
    // * 可选参数 是否允许别人通过真实姓名搜索到我， 0允许，1不允许，默认值1。
    // * @param geo
    // * 可选参数 发布微博，是否允许微博保存并显示所处的地理位置信息。 0允许，1不允许，默认值0。
    // * @param badge
    // * 可选参数 勋章展现状态，值—1私密状态，0公开状态，默认值0。
    // *
    // * @author
    // */
    // public UserShow account_updatePrivacy(int comment, int message,
    // int realname, int geo, int badge) throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("account/update_privacy.json");
    //
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("source", APP_KEY);
    // if (comment == 0 || comment == 1)
    // params.put("comment", comment);
    // if (message == 0 || message == 1)
    // params.put("message", message);
    // if (realname == 0 || realname == 1)
    // params.put("realname", realname);
    // if (geo == 0 || geo == 1)
    // params.put("geo", geo);
    // if (badge == -1 || badge == 0)
    // params.put("badge", badge);
    // String str = httpClient.post(sb.toString(), params);
    // UserShow rlt = new UserShow();
    // try {
    // rlt = (jsonParser.parseUser(str));
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 获取隐私信息设置情况 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public AccountResult account_getPrivacy() throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("account/get_privacy.json?source=").append(
    // APP_KEY);
    // String str = httpClient.httpGet(sb.toString());
    // AccountResult rlt = null;
    // try {
    // rlt = (jsonParser.parseAccountResult(str));
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 分页输出当前登录用户黑名单中的用户ID列表。 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param page
    // * 可选参数 页码。
    // * @param count
    // * 可选参数 单页记录数。
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public String[] getBlacklistUserIds(int page, int count)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("blocks/blocking.json?source=").append(APP_KEY);
    // if (page > 0)
    // sb.append("&page=").append(page);
    // if (count > 0)
    // sb.append("&count=").append(count);
    // String str = httpClient.httpGet(sb.toString());
    // String[] rlt;
    // try {
    // // JSONArray jsonArray = response.asJSONArray();
    // JSONArray jsonArray = new JSONArray(str);
    // rlt = new String[jsonArray.length()];
    // for (int i = 0; i < rlt.length; i++) {
    // rlt[i] = jsonArray.optJSONObject(i).optString("id");
    // }
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 返回指定用户的标签列表 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param user_id
    // * 必选参数 要获取的标签列表所属的用户ID
    // * @param count
    // * 可选参数 单页记录数。
    // * @param page
    // * 可选参数 页码。
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public Tag[] tags(String user_id, int count, int page)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("tags.json?source=").append(APP_KEY);
    // sb.append("&user_id=").append(user_id);
    // if (count > 0)
    // sb.append("&count=").append(count);
    // if (page > 0)
    // sb.append("&page=").append(page);
    // String str = httpClient.httpGet(sb.toString());
    // Tag[] rlt = null;
    // try {
    // rlt = (jsonParser.parseTagList(str));
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 为当前登录用户添加新的用户标签 <br>
    // * 格式：xml， json <br>
    // * POST <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param tags
    // * 必选参数 要创建的一组标签，用半角逗号隔开。
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public int[] tags_create(String tags) throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("tags/create.json");
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("source", APP_KEY);
    // params.put("tags", tags);
    // String str = httpClient.post(sb.toString(), params);
    // Tag[] tagsTemp = null;
    // try {
    // tagsTemp = (jsonParser.parseTagList(str));
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // int[] rlt = new int[tagsTemp.length];
    // for (int i = 0; i < tagsTemp.length; i++) {
    // rlt[i] = tagsTemp[i].id;
    // }
    // return rlt;
    // }
    //
    // /**
    // * 获取当前登录用户感兴趣的推荐标签列表 <br>
    // * 格式：xml， json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param count
    // * 可选参数 单页记录数。
    // * @param page
    // * 可选参数 页码。由于推荐标签是随机返回，故此特性暂不支持。
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public Tag[] tags_suggestions(int count, int page)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("tags/suggestions.json?source=").append(APP_KEY);
    // if (count > 0)
    // sb.append("&count=").append(count);
    // if (page > 0)
    // sb.append("&page=").append(page);
    // String str = httpClient.httpGet(sb.toString());
    // Tag[] rlt = null;
    // try {
    // rlt = (jsonParser.parseTagList(str));
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 删除标签 <br>
    // * 格式：xml， json <br>
    // * POST/DELETE <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param tag_id
    // * 必选参数 要删除的标签ID
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public boolean tags_destroy(int tag_id) throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("tags/destroy.json?source=").append(APP_KEY);
    // sb.append("&tag_id=").append(tag_id);
    // String str = httpClient.httpDelete(sb.toString());
    // boolean rlt;
    // try {
    // JSONObject jsonObj = new JSONObject(str);
    // rlt = jsonObj.optBoolean("result");
    // } catch (JSONException e) {
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 删除一组标签 <br>
    // * 格式：xml， json <br>
    // * POST/DELETE <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param ids
    // * 必须参数 要删除的一组标签ID，以半角逗号隔开，一次最多提交20个ID。
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public int[] tags_destroyBatch(int... ids) throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("tags/destroy_batch.json?source=").append(
    // APP_KEY);
    // StringBuilder str_ids = new StringBuilder();
    // for (int i = 0; i < ids.length; i++) {
    // if (i != 0) {
    // str_ids.append(",");
    // }
    // str_ids.append(ids[i]);
    // }
    // sb.append("&ids=").append(str_ids);
    // String str = httpClient.httpDelete(sb.toString());
    // Tag[] tagsTemp = null;
    // try {
    // tagsTemp = jsonParser.parseTagList(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // int[] rlt = new int[tagsTemp.length];
    // for (int j = 0; j < tagsTemp.length; j++)
    // rlt[j] = tagsTemp[j].id;
    // return rlt;
    // }
    //
    // /**
    // * 获取API的访问频率限制。返回当前小时内还能访问的次数。 <br>
    // * 频率限制是根据用户请求来做的限制，具体细节参见：接口访问权限说明。 <br>
    // * 格式：xml，json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public Hash account_rateLimitStatus() throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("account/rate_limit_status.json?source=")
    // .append(APP_KEY);
    // String str = httpClient.httpGet(sb.toString());
    // Hash rlt = null;
    // try {
    // rlt = jsonParser.parseHash(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }

    /**
     * 清除已验证用户的session，退出登录，并将cookie设为null。主要用于widget等web应用场合。 <br>
     * 格式：xml， json <br>
     * POST <br>
     * 需要登录：true <br>
     * 请求数限制：true
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public UserShow account_endSession() throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("account/end_session.json");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", APP_KEY);
        String str = httpClient.post(sb.toString(), params);
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShow.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    // /**
    // * 注册新浪微博用户接口，该接口为受限接口（只对受邀请的合作伙伴开放）。 <br>
    // * 格式：xml， json <br>
    // * POST <br>
    // * 需要登录：flase <br>
    // * 请求数限制：flase
    // *
    // * @param nick
    // * 必选参数 昵称，不超过20个汉字
    // * @param gender
    // * 必选参数 性别，m 表示男性，f 表示女性
    // * @param password
    // * 必选参数 密码
    // * @param email
    // * 必选参数 注册邮箱，需要保持与当前网站同域，如：在abc.com下注册的用户需要使用***@abc.com的邮箱。
    // * @param ip
    // * 必选参数 注册用户的真实IP
    // * @param province
    // * 可选参数 省份代码，参考省份城市编码表
    // * @param city
    // * 可选参数 城市代码，1000表示不指定具体城市。参考省份城市编码表
    // * @return uid 用户id
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public String account_register(String nick, String gender, String
    // password,
    // String email, String ip, int province, int city)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("account/register.json");
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("source", APP_KEY);
    // params.put("nick", nick);
    // params.put("gender", gender);
    // params.put("password", password);
    // params.put("email", email);
    // params.put("ip", ip);
    // if (province > 0)
    // params.put("province", province);
    // if (city > 0)
    // params.put("city", city);
    // String str = httpClient.post(sb.toString(), params);
    // String rlt = null;
    // try {
    // JSONObject jsonObj = new JSONObject(str);
    // rlt = jsonObj.optString("uid");
    // } catch (JSONException e) {
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 二次注册微博的接口，该接口为受限接口（只对受邀请的合作伙伴开放）。 <br>
    // * 格式：xml， json <br>
    // * POST <br>
    // * 需要登录：flase <br>
    // * 请求数限制：flase
    // *
    // * @param uid
    // * 必选参数 用户ID
    // * @param nickname
    // * 必选参数 昵称，不超过20个汉字
    // * @param gender
    // * 必选参数 性别，m 表示男性，f 表示女性
    // * @param email
    // * 必选参数 注册邮箱，需要保持与当前网站同域，如：在abc.com下注册的用户需要使用***@ abc.com的邮箱。
    // * @param ip
    // * 必选参数 注册用户的真实IP
    // * @param province
    // * 可选参数 省份代码，参考省份城市编码表
    // * @param city
    // * 可选参数 城市代码，1000表示不指定具体城市。参考省份城市编码表
    // * @return uid 用户id
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public int account_activate(int uid, String nickname, String gender,
    // String email, String ip, int province, int city)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("account/activate.json");
    // Map<String, Object> params = new HashMap<String, Object>();
    // params.put("source", APP_KEY);
    // params.put("uid", uid);
    // params.put("nickname", nickname);
    // params.put("gender", gender);
    // params.put("email", email);
    // params.put("ip", ip);
    // if (province > 0)
    // params.put("province", province);
    // if (city > 0)
    // params.put("city", city);
    //
    // String str = httpClient.post(sb.toString(), params);
    // int rlt;
    // try {
    // JSONObject jsonObj = new JSONObject(str);
    // rlt = jsonObj.optInt("uid");
    // } catch (JSONException e) {
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }

    /**
     * 返回与关键字相匹配的微博用户。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：false <br>
     * 请求数限制：true
     * 
     * @param q
     *            必选参数 搜索的关键字。必须进行URL Encode
     * @param snick
     *            可选参数 搜索范围是否包含昵称。0为不包含，1为包含。
     * @param sdomain
     *            可选参数 搜索范围包含个性域名。0为不包含，1为包含。
     * @param sintro
     *            可选参数 搜索范围包含简介。0为不包含，1为包含。
     * @param province
     *            可选参数 省份ID，参考省份城市编码表
     * @param city
     *            可选参数 城市ID，参考省份城市编码表
     * @param gender
     *            可选参数 性别 (m 为男，f 为女)
     * @param comrosch
     *            可选参数 公司学校名称。
     * @param sort
     *            可选参数 排序方式，1为按更新时间，2为按粉丝数。
     * @param page
     *            可选参数 页码
     * @param count
     *            可选参数 每页返回的微博数。（默认返回10条）
     * 
     * @author
     * @throws WeiboOpenAPIException
     */
    public UserShow[] searchUsers(String q, int snick, int sdomain, int sintro,
            int province, int city, String gender, String comrosch, int sort,
            int page, int count) throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotBlank(q));

        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("users/search.json?source=").append(APP_KEY);
        sb.append("&q=").append(q);
        if (snick == 0 || snick == 1) {
            sb.append("&snick=").append(snick);
        }
        if (sdomain == 0 || sdomain == 1) {
            sb.append("&sdomain=").append(sdomain);
        }
        if (sintro == 0 || sintro == 1) {
            sb.append("&sintro=").append(sintro);
        }
        if (province > 0) {
            sb.append("&province=").append(province);
        }
        if (city > 0) {
            sb.append("&city=").append(city);
        }
        if ("f".equalsIgnoreCase(gender) || "m".equalsIgnoreCase(gender)) {
            sb.append("&gender=").append(gender);
        }
        if (isNotBlank(comrosch)) {
            sb.append("&comrosch").append(comrosch);
        }
        if (sort == 1 || sort == 2) {
            sb.append("&sort=").append(sort);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        if (count > 0) {
            sb.append("&count=").append(count);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserShow[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 返回与关键字相匹配的微博用户。 <br>
     * 格式：xml， json <br>
     * GET <br>
     * 需要登录：false <br>
     * 请求数限制：true
     * 
     * @param q
     *            必选参数 搜索的关键字。必须进行URL Encode
     * @param page
     *            可选参数 页码
     * @param count
     *            可选参数 每页返回的微博数。（默认返回10条）
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserShow[] searchUsers(String q, int page, int count)
            throws WeiboOpenAPIException {
        return searchUsers(q, 1, 1, 0, 0, 0, null, null, 2, page, count);
    }

    /**
     * 将一个或多个长链接转换成短链接。<br />
     * 多个url参数需要使用如下方式：url_long=aaa&url_long=bbb
     * 
     * @param url_longs
     *            需要转换的长链接，需要URLencoded，最多不超过20个。
     * @return
     * @throws WeiboOpenAPIException
     * 
     *             TODO 暂时还不支持多URL转换
     */
    public ShortUrl[] urlShorten(String... url_longs)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotEmpty(url_longs));
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("short_url/shorten.json?source=")
                .append(APP_KEY);
        for (String url : url_longs) {
            sb.append("&url_long=").append(url);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, ShortUrl[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 将一个或多个短链接还原成原始的长链接<br />
     * 多个url参数需要使用如下方式：url_short=aaa&url_short=bbb
     * 
     * @param url_shorts
     *            需要还原的短链接，需要URLencoded，最多不超过20个
     * @return
     * @throws WeiboOpenAPIException
     * 
     *             TODO 暂时还不支持多URL转换
     */
    public ShortUrl[] urlExpand(String... url_shorts)
            throws WeiboOpenAPIException {
        Preconditions.checkArgument(isNotEmpty(url_shorts));
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append("short_url/expand.json?source=").append(APP_KEY);
        for (String url : url_shorts) {
            sb.append("&url_short=").append(url);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, ShortUrl[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 展示分组成员的最新微博信息
     * 
     * @param userId
     *            用户UID或昵称
     * @param list_id
     *            List的ID或者slug *
     * @param since_id
     *            可选参数。返回带有比指定list
     *            ID大的ID（比指定list的ID新的）的结果。被进入API的微博数会被限制。如果当微博的限制达到时
     *            ，since_id将被强制到最老的可用ID。
     * @param max_id
     *            可选参数。返回带有一个小于（就是比较老的）或等于指定list ID的ID的结果。
     * @param per_page
     *            可选参数。每次返回的最大记录数。
     * @param page
     *            选填参数，是否基于当前应用来获取数据。1为限制本应用微博，0为不做限制。
     * @param base_app
     *            选填参数，是否基于当前应用来获取数据。1为限制本应用微博，0为不做限制。
     * @param feature
     *            选填参数，微博类型，0：全部，1：原创，2：图片，3：视频，4：音乐.
     *            返回指定类型的微博信息内容，如feature=3则只返回视频微博。
     * @throws WeiboOpenAPIException
     */
    public Status[] getUserListStatus(String userId, String list_id,
            String since_id, String max_id, int per_page, int page,
            int base_app, int feature) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append(userId).append("/lists/").append(list_id)
                .append("/statuses.json?source=").append(APP_KEY);
   
        if (isNotBlank(max_id)) {
            sb.append("&max_id=").append(max_id);
        }
        if (isNotBlank(since_id)) {
            sb.append("&since_id=").append(since_id);
        }
        if (per_page > 0) {
            sb.append("&count=").append(per_page);
        }
        if (page > 0) {
            sb.append("&page=").append(page);
        }
        if (base_app >= 0 && base_app <= 1) {
            sb.append("&baseApp=").append(base_app);
        }
        if (feature >= 0 && feature <= 4) {
            sb.append("&feature=").append(feature);
        }
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }

    /**
     * 根据参数listType的控制，列出用户所有的list。
     * 
     * @param userId
     *            用户UID或昵称
     * @param cursor
     *            可选参数。将结果分页，每一页包含20个lists。由-1开始分页，定位一个id地址，
     *            通过比较id大小实现next_cursor 和previous_cursor向前或向后翻页。
     * @param listType
     *            可选参数。控制返回的LIST。1：返回私有列表； 0：返回公开列表
     *            ；默认0。当要求返回私有LIST时，当前用户必须为私有LIST的创建者。
     * @return
     * @throws WeiboOpenAPIException
     */
    public UserGroupCursor getUserGroupLists(String userId, int cursor,
            int listType) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        sb.append(HOST).append(userId).append("/lists.json?source=")
                .append(APP_KEY);
        if (listType == 0 || listType == 1) {
            sb.append("&listType=").append(listType);
        }
        if (cursor >= -1) {
            sb.append("&cursor=").append(cursor);
        }
        log.error(sb);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, UserGroupCursor.class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }
    
    /**
     * @用户时的联想建议
     * 
     * @param source
     *            申请应用时分配的AppKey，调用接口时候代表应用的唯一身份。（采用OAuth授权方式不需要此参数）
     * @param q
     *            搜索的关键字，必须做URLencoding
     * @param count
     *            返回的记录条数，默认为10，粉丝最多1000，关注最多2000。
     * @param type
     *           联想类型，0：关注、1：粉丝。
    * @param range
     *            联想范围，0：只联想关注人、1：只联想关注人的备注、2：全部，默认为2。
     * @return
     * @throws WeiboOpenAPIException
     */
    public AtSuggestion[] getAtSuggestions (String q, int count, int type,
            int range) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        //https://api.weibo.com/2/search/suggestions/at_users.json
        sb.append(HOST).append("search/suggestions/at_users.json");//?source=.append(APP_KEY);
        if (q != null) {
            sb.append("?q=").append(q);
        }
        if (count >= 0) {
            sb.append("&count=").append(count);
        }
        if (type == 0 || type == 1) {
            sb.append("&type=").append(type);
        }
        if (range == 0 || range == 1 || range == 2) {
            sb.append("&range=").append(range);
        }
        log.error(sb);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, AtSuggestion[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }
    
    /**
     * 按天返回热门微博转发榜的微博列表
     *    
     * @param source
     *            申请应用时分配的AppKey，调用接口时候代表应用的唯一身份。（采用OAuth授权方式不需要此参数）
     * @param count
     *            返回的记录条数，最大不超过50，默认为20。
     * @param base_app
     *            是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
     * @return
     * @throws WeiboOpenAPIException
     */
    public Status[] getHotRepostHourly (int count, int base_app) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        //https://api.weibo.com/2/statuses/hot/repost_daily.json
        sb.append(HOST).append("statuses/hot/repost_daily.json?source=")
        .append(APP_KEY);
        if (count >= 0) {
            sb.append("&count=").append(count);
        }
        if (base_app == 0 || base_app == 1) {
            sb.append("&type=").append(base_app);
        }
        log.error(sb);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }
    
    
    /**
     * 按天返回热门微博评论榜的微博列表
     *    
     * @param source
     *            申请应用时分配的AppKey，调用接口时候代表应用的唯一身份。（采用OAuth授权方式不需要此参数）
     * @param count
     *            返回的记录条数，最大不超过50，默认为20。
     * @param base_app
     *            是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
     * @return
     * @throws WeiboOpenAPIException
     */
    public Status[] getsAtSuggestions (int count, int base_app) throws WeiboOpenAPIException {
        StringBuilder sb = new StringBuilder();
        //https://api.weibo.com/2/statuses/hot/comments_daily.json
        sb.append(HOST).append("statuses/hot/comments_daily.json?source=").append(APP_KEY);
        if (count >= 0) {
            sb.append("&count=").append(count);
        }
        if (base_app == 0 || base_app == 1) {
            sb.append("&base_app=").append(base_app);
        }
        log.error(sb);
        String str = httpClient.httpGet(sb.toString());
        synchronized (gson) {
            try {
                return gson.fromJson(str, Status[].class);
            } catch (JsonParseException e) {
                throw new WeiboOpenAPIException(e);
            }
        }
    }
    // /**
    // * 返回新浪微博官方所有表情、魔法表情的相关信息。 <br>
    // * 包括短语、表情类型、表情分类，是否热门等。 <br>
    // * 格式：xml,json <br>
    // * GET <br>
    // * 需要登录：false <br>
    // * 请求数限制：false
    // *
    // * @param type
    // * @param language
    // *
    // * @author
    // * @throws WeiboOpenAPIException
    // */
    // public Emotion[] emotions(String type, String language)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("emotions.json?source=").append(APP_KEY);
    // if (!(type == null || type.toString().length() == 0))
    // sb.append("&type=").append(type);
    // if (!(language == null || language.toString().length() == 0))
    // sb.append("&language=").append(language);
    // String str = httpClient.httpGet(sb.toString());
    // Emotion[] rlt = null;
    // try {
    // rlt = jsonParser.parseEmotionList(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
    //
    // /**
    // * 跳转到单条微博的Web地址。可以通过此url跳转到微博对应的Web网页。 <br>
    // * 格式：xml,json <br>
    // * GET <br>
    // * 需要登录：false <br>
    // * 请求数限制：false
    // *
    // * @param _userid
    // * 必选参数 微博消息的发布者ID
    // * @param _id
    // * 必选参数 微博消息的ID
    // * @return url web地址
    // * @author
    // */
    // public String user_statusesId(int _userid, int _id) {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append(_userid).append("/statuses/").append(_id)
    // .append(".json?source=").append(APP_KEY);
    // return sb.toString();
    // }
    //
    // /**
    // * 返回当前用户可能感兴趣的用户。 <br>
    // * 格式：xml,json <br>
    // * GET <br>
    // * 需要登录：true <br>
    // * 请求数限制：true
    // *
    // * @param with_reason
    // * 可选参数 是否返回推荐原因，可选值1/0。当值为1，返回结果中增加推荐原因，会大幅改变返回值格式。
    // *
    // * @throws WeiboOpenAPIException
    // */
    // public UserShow[] users_suggestions(int with_reason)
    // throws WeiboOpenAPIException {
    // StringBuilder sb = new StringBuilder();
    // sb.append(HOST).append("users/suggestions.json?source=")
    // .append(APP_KEY);
    // if (with_reason == 0 || with_reason == 1)
    // sb.append("&with_reason=").append(with_reason);
    // String str = httpClient.httpGet(sb.toString());
    // UserShow[] rlt = null;
    // try {
    // rlt = jsonParser.parseUserList(str);
    // } catch (Exception e) {
    // if (e instanceof WeiboOpenAPIException)
    // throw (WeiboOpenAPIException) e;
    // else
    // throw new WeiboOpenAPIException(e);
    // }
    // return rlt;
    // }
}

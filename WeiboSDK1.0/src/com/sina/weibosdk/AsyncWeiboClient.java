package com.sina.weibosdk;

import android.content.Context;

import com.sina.weibosdk.cache.CacheStrategy;
import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.exception.WeiboIOException;
import com.sina.weibosdk.net.HttpEngine;
import com.sina.weibosdk.net.HttpUrlConnectionEngine;
import com.sina.weibosdk.net.IDownloadCallback;
import com.sina.weibosdk.requestparam.APIRequestParam;
import com.sina.weibosdk.requestparam.RequestParam;
import com.sina.weibosdk.task.ATask;
import com.sina.weibosdk.task.ATaskListener;
import com.sina.weibosdk.task.AsyncDownloadTask;
import com.sina.weibosdk.task.AsyncRequestTask;

/**
 * 对异步调用方法进行了封装
 * 
 * @author zhangqi
 * 
 */
@SuppressWarnings({ "rawtypes" })
public final class AsyncWeiboClient {

    private Context mContext;
    private HttpEngine mRequestStrategy;
    private static HttpEngine mDefaultStrategy;

    AsyncWeiboClient(Context context) {
        mContext = context;
        if (mDefaultStrategy == null) {
            mDefaultStrategy = new HttpUrlConnectionEngine(context);
        }
        mRequestStrategy = mDefaultStrategy;
    }

    private ATask createTask() {
        ATask task = new AsyncRequestTask(mContext);
        task.setNetRequestStrategy(mRequestStrategy);
        return task;
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
     * @param listener
     *            Api异步任务回调lisenter
     * 
     * 
     * @return
     * @throws Exception
     */
    public ATask loginTask(String username, String password, boolean flag, String s, String cpt,
            String cptcode, ATaskListener listener) throws WeiboException {
        RequestParam param = WeiboClientHelper.getInstance().loginParam(username, password, s, cpt,
                cptcode);
        ATask task = createTask();
        task.execute("account/login", param, listener);
        return task;
    }

    /**
     * 返回验证码
     * 
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     */
    public ATask getCaptchaTask(ATaskListener listener) {
        RequestParam param = new APIRequestParam();
        ATask task = createTask();
        task.execute("captcha/get", param, listener);
        return task;
    }

    /**
     * 
     * @param page
     *            页码。注意：最多返回200条分页内容。默认值1。
     * @param count
     *            指定每页返回的记录条数。默认值50，最大值200。
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     */
    public ATask getPublicTimeLineTask(int page, int count, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getPublicTimeLineParam(page, count);
        ATask task = createTask();
        task.execute("statuses/public_timeline", param, listener);
        return task;
    }

    /**
     * 
     * @param gid
     *            分组id值，如果不为空，则返回指定好友分组的timeline
     * @param page
     *            页码。注意：最多返回200条分页内容。默认值1。
     * @param count
     *            指定每页返回的记录条数。默认值50，最大值200。
     * @param feature
     * 			  过滤类型ID （0：全部、1：原创、2：图片、3：视频、4：音乐、5：商品） 默认为 0                  
     * @param since_id
     *            若指定此参数，则只返回ID比since_id大的微博消息（即比since_id发表时间晚的微博消息）。默认为0
     * @param max_id
     *            若指定此参数，则返回ID小于或等于max_id的微博消息。默认为0
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     */
    public ATask getFriendsTimeLineTask(String gid, int feature, int page, int count, String since_id,
            String max_id, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getFriendsTimeLineParam(gid, feature, page,
                count, since_id, max_id);
        ATask task = createTask();
        task.execute("statuses/friends_timeline", param, listener);
        return task;
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
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask getUserTimeLineTask(String uid, int page, int count, int filter,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getUserTimeLineParam(uid, page, count,
                filter);
        ATask task = createTask();
        task.execute("statuses/user_timeline", param, listener);
        return task;
    }

    /**
     * 
     * @param id
     *            要获取的微博消息ID
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask getSingleStatusTask(String id, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getSingleStatusParam(id);
        ATask task = createTask();
        task.execute("statuses/show", param, listener);
        return task;
    }

    /**
     * 
     * @param statusId
     *            微博ID，英文半角逗号分隔，最大100
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask getStatusCRNum(ATaskListener listener, String... statusId) {
        RequestParam param = WeiboClientHelper.getInstance().getStatusCRNumParam(statusId);
        ATask task = createTask();
        task.execute("statuses/count", param, listener);
        return task;
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
     * 
     * @return
     * 
     */
    public ATask getMentionsTimelineTask(int page, int count, String since_id, String max_id,
            int filter_by_author, int filter_by_type, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getMentionsTimelineParam(page, count,
                since_id, max_id, filter_by_author, filter_by_type);
        ATask task = createTask();
        task.execute("statuses/mentions", param, listener);
        return task;
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
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask getHotStatusDaily(int app_base, int count, int is_encoded, int picsize,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getHotStatusDailyParam(app_base,
                count, is_encoded, picsize);
        ATask task = createTask();
        task.execute("hot/repost_daily", param, listener);
        return task;
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
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask getHotCommentDailyTask(int app_base, int count, int is_encoded, int picsize,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getHotCommentDailyParam(app_base,
                count, is_encoded, picsize);
        ATask task = createTask();
        task.execute("hot/comments_daily", param, listener);
        return task;
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
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask updateStatusTask(String status, int visible, double lat, double lon,
            String annotations, int is_encoded, String cpt, String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().updateStatusParam(status, visible,
                lat, lon, annotations, is_encoded, cpt, cptcode);
        ATask task = createTask();
        task.execute("statuses/update", param, listener);
        return task;
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
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * @throws WeiboIOException
     * 
     */
    public ATask uploadStatusTask(String status, String picPath, double lat, double lon,
            int visible, String cpt, String cptcode, ATaskListener listener)
            throws WeiboIOException {
        RequestParam param = WeiboClientHelper.getInstance().uploadStatusParam(status, picPath,
                lat, lon, visible, cpt, cptcode);
        ATask task = createTask();
        task.execute("statuses/upload", param, listener);
        return task;
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
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask repostStatusTask(String status, String id, int is_comment, String annotations,
            int is_encoded, String cpt, String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().repostStatusParam(status, id,
                is_comment, annotations, is_encoded, cpt, cptcode);
        ATask task = createTask();
        task.execute("statuses/repost", param, listener);
        return task;
    }

    /**
     * 
     * @param id
     *            要删除的微博消息ID
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask destroyStatusTask(String id, String cpt, String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destroyStatusParam(id, cpt, cptcode);
        ATask task = createTask();
        task.execute("statuses/destroy", param, listener);
        return task;
    }

    /**
     * 
     * @param page
     *            页码。
     * @param count
     *            每页返回结果数。
     * @param picsize
     *            带图片微博返回的bmiddle_pic字段图片的大小，可选值 240,320,960
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask getFavoritesTask(int page, int count, int picsize, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance()
                .getFavoritesParam(page, count, picsize);
        ATask task = createTask();
        task.execute("favorites", param, listener);
        return task;
    }

    /**
     * 
     * @param id
     *            要收藏的微博ID
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask createFavoriteTask(String id, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().createFavoriteParam(id);
        ATask task = createTask();
        task.execute("favorites/create", param, listener);
        return task;
    }

    /**
     * 
     * @param id
     *            要删除的收藏ID
     * @param listener
     *            Api异步任务回调lisenter
     * @return
     * 
     */
    public ATask deleteFavoriteTask(String id, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().deleteFavoriteParam(id);
        ATask task = createTask();
        task.execute("favorites/destroy", param, listener);
        return task;
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
     */
    public ATask getTopicListTask(String uid, int page, boolean has_num, int count,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getTopicListParam(uid, page, has_num,
                count);
        ATask task = createTask();
        task.execute("trends", param, listener);
        return task;
    }

    /**
     * 返回最近一小时内的热门话题
     * 
     * @param base_app
     *            是否基于当前应用来获取数据
     * @param count
     * @return
     */
    public ATask getHourlyTopicListTask(boolean base_app, int count, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getHourlyTopicListParam(base_app,
                count);
        ATask task = createTask();
        task.execute("trends/hourly", param, listener);
        return task;
    }

    /**
     * 返回最近一天内的热门话题
     * 
     * @param base_app
     *            是否基于当前应用来获取数据
     * @param count
     * @return
     */
    public ATask getDailyTopicListTask(boolean base_app, int count, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance()
                .getDailyTopicListParam(base_app, count);
        ATask task = createTask();
        task.execute("trends/daily", param, listener);
        return task;
    }

    /**
     * 返回最近一周内的热门话题
     * 
     * @param base_app
     *            是否基于当前应用来获取数据
     * @param count
     * @return
     */
    public ATask getWeeklyTopicListTask(boolean base_app, int count, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getWeeklyTopicListParam(base_app,
                count);
        ATask task = createTask();
        task.execute("trends/weekly", param, listener);
        return task;
    }

    /**
     * 关注某话题
     * 
     * @param trend_name
     *            要关注的话题关键词
     * @return
     */
    public ATask followTopicTask(String trend_name, String cpt, String cptcode,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().followTopicParam(trend_name, cpt,
                cptcode);
        ATask task = createTask();
        task.execute("trends/follow", param, listener);
        return task;
    }

    /**
     * 取消关注某话题
     * 
     * @param trend_id
     *            要取消关注的话题ID
     * @return
     */
    public ATask destroyTopicTask(String trend_id, String cpt, String cptcode,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destroyTopicParam(trend_id, cpt,
                cptcode);
        ATask task = createTask();
        task.execute("trends/destroy", param, listener);
        return task;
    }

    /**
     * 分页输出当前登录用户的黑名单列表，包括黑名单内的用户详细信息
     * 
     * @param page
     * @param count
     * @return
     */
    public ATask getBlackListTask(int page, int count, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getBlackListParam(page, count);
        ATask task = createTask();
        task.execute("blocks/blocking", param, listener);
        return task;
    }

    /**
     * 将某用户加入登录用户的黑名单, 返回黑名单用户信息
     * 
     * @param uid
     * @return
     */
    public ATask createBlackTask(String uid, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().createBlackParam(uid);
        ATask task = createTask();
        task.execute("blocks/create", param, listener);
        return task;
    }

    /**
     * 将某用户加入登录用户的黑名单,返回用户信息
     * 
     * @param uid
     * @return
     */
    public ATask destoryBlackTask(String uid, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destoryBlackParam(uid);
        ATask task = createTask();
        task.execute("blocks/destroy", param, listener);
        return task;
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
     */
    public ATask reportTask(String url, String content, int type, int rid, int class_id,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().reportParam(url, content, type, rid,
                class_id);
        ATask task = createTask();
        task.execute("report/spam", param, listener);
        return task;
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
     */
    public ATask getNewestMessageTask(int page, int count, String since_id, String max_id,
            int is_encoded, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getNewestMessageParam(page, count,
                since_id, max_id, is_encoded);
        ATask task = createTask();
        task.execute("direct_messages", param, listener);
        return task;
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
     */
    public ATask getMessageListTask(String uid, int page, int count, String since_id,
            String max_id, int is_encoded, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getMessageListParam(uid, page, count,
                since_id, max_id, is_encoded);
        ATask task = createTask();
        task.execute("direct_messages/conversation", param, listener);
        return task;
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
     */
    public ATask getMessageUserListTask(int page, int cursor, int refresh_user_count,
            int is_encoded, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getMessageUserListParam(page, cursor,
                refresh_user_count, is_encoded);
        ATask task = createTask();
        task.execute("direct_messages/user_list", param, listener);
        return task;
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
     */
    public ATask createMessageTask(String text, String uid, String screen_name, String fids,
            String id, int is_encoded, String cpt, String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().createMessageParam(text, uid,
                screen_name, fids, id, is_encoded, cpt, cptcode);
        ATask task = createTask();
        task.execute("direct_messages/create", param, listener);
        return task;
    }

    /**
     * 删除一条私信
     * 
     * @param id
     *            私信ID
     * @param is_encoded
     * @return
     */
    public ATask destroyMessage(String id, int is_encoded, String cpt, String cptcode,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destroyMessageParam(id, is_encoded,
                cpt, cptcode);
        ATask task = createTask();
        task.execute("direct_messages/destroy", param, listener);
        return task;
    }

    /**
     * 批量删除私信，通过私信的ID
     * 
     * @param ids
     *            需要删除的私信ID
     * @param is_encoded
     * @return
     */
    public ATask destroyBatchMessageByIdTask(String ids, int is_encoded, String cpt,
            String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destroyBatchMessageByIdParam(ids,
                is_encoded, cpt, cptcode);
        ATask task = createTask();
        task.execute("direct_messages/destroy_batch", param, listener);
        return task;
    }

    /**
     * 批量删除私信，通过用户ID
     * 
     * @param uid
     *            用户UID。当传入当前登录用户UID时，表示删除当前登录用户的全部私信
     * @param is_encoded
     * @return
     */
    public ATask destroyBatchMessageByUidTask(String uid, int is_encoded, String cpt,
            String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destroyBatchMessageByUidParam(uid,
                is_encoded, cpt, cptcode);
        ATask task = createTask();
        task.execute("direct_messages/destroy_batch", param, listener);
        return task;
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
     */
    public ATask getCommentsTask(String id, int page, int count, String since_id, String max_id,
            int filter_by_author, int is_encoded, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getCommentsParam(id, page, count,
                since_id, max_id, filter_by_author, is_encoded);
        ATask task = createTask();
        task.execute("comments/show", param, listener);
        return task;
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
     */
    public ATask getCommentsByMeTask(int page, int count, String since_id, String max_id,
            int filter_by_source, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getCommentsByMeParam(page, count,
                since_id, max_id, filter_by_source);
        ATask task = createTask();
        task.execute("comments/by_me", param, listener);
        return task;
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
     */
    public ATask getCommentsToMeTask(int page, int count, String since_id, String max_id,
            int filter_by_source, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getCommentsToMeParam(page, count,
                since_id, max_id, filter_by_source);
        ATask task = createTask();
        task.execute("comments/to_me", param, listener);
        return task;
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
     */
    public ATask getCommentsMentionMeTask(int page, int count, String since_id, String max_id,
            int filter_by_author, int filter_by_source, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getCommentsMentionMeParam(page, count,
                since_id, max_id, filter_by_author, filter_by_source);
        ATask task = createTask();
        task.execute("comments/mentions", param, listener);
        return task;
    }

    /**
     * 对一条微博信息进行评论
     * 
     * @param id
     * @param comment
     * @param comment_ori
     *            当回复一条转发微博的评论时，是否评论给原微博,需要进行URLEncode
     * @return
     */
    public ATask createComment(String id, String comment, boolean comment_ori, String cpt,
            String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().createCommentParam(id, comment,
                comment_ori, cpt, cptcode);
        ATask task = createTask();
        task.execute("comments/create", param, listener);
        return task;
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
     */
    public ATask replyCommentTask(String cid, String id, String comment, int without_mention,
            boolean comment_ori, String cpt, String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().replyCommentParam(cid, id, comment,
                without_mention, comment_ori, cpt, cptcode);
        ATask task = createTask();
        task.execute("comments/reply", param, listener);
        return task;
    }

    /**
     * 删除评论
     * 
     * @param cid
     *            要删除的评论ID。只能删除登录用户自己发布的评论
     * @return
     */
    public ATask destroyCommentTask(String cid, String cpt, String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destroyCommentParam(cid, cpt, cptcode);
        ATask task = createTask();
        task.execute("comments/destroy", param, listener);
        return task;
    }

    /**
     * 根据用户ID获取用户资料
     * 
     * @param uid
     *            如果为空，返回当前用户的分组列表
     */
    public ATask getUsersShowTask(String uid, String screen_name, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getUsersShowParam(uid, screen_name);
        ATask task = createTask();
        task.execute("users/show", param, listener);
        return task;
    }

    /**
     * 更新用户的基本信息，仅更新提交的字段
     */
    public ATask updateUserInfoTask(String screen_name, String gender, String description,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().updateUserInfoParam(screen_name,
                gender, description);
        ATask task = createTask();
        task.execute("users/update", param, listener);
        return task;
    }

    /**
     * 根据用户ID获取用户资料
     * 
     * @param image
     *            FILE方式，可选，非PNG格式
     * @param trim_user
     *            是否返回用户的信息，0返回，1不返回，默认为0
     */
    public ATask updateUserPortraitTask(String image, int trim_user, ATaskListener listener)
            throws WeiboIOException {
        RequestParam param = WeiboClientHelper.getInstance().updateUserPortraitParam(image,
                trim_user);
        ATask task = createTask();
        task.execute("account/avatar_upload", param, listener);
        return task;
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
    public ATask getFriendsTask(String uid, int page, int count, int sourt, int trim_status,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getFriendsParam(uid, page, count,
                sourt, trim_status);
        ATask task = createTask();
        task.execute("friendships/friends", param, listener);
        return task;
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
    public ATask getFollowersTask(String uid, int page, int count, int sourt, int trim_status,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getFollowersParam(uid, page, count,
                sourt, trim_status);
        ATask task = createTask();
        task.execute("friendships/followers", param, listener);
        return task;
    }

    /**
     * 当前登录用户批量关注指定ID的用户
     * 
     * @param uid
     *            要关注的用户ID，用逗号分隔的uid 最多20个
     */
    public ATask createFriendshipTask(String uid, String cpt, String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().createFriendshipParam(uid, cpt,
                cptcode);
        ATask task = createTask();
        task.execute("friendships/create", param, listener);
        return task;
    }

    /**
     * 取消对某用户的关注
     * 
     * @param uid
     *            要取消关注的用户ID
     */
    public ATask destroyFriendshipTask(String uid, String cpt, String cptcode,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destroyFriendshipParam(uid, cpt,
                cptcode);
        ATask task = new AsyncRequestTask(mContext);
        task.execute("friendships/destroy", param, listener);
        return task;
    }

    /**
     * 移除粉丝
     * 
     * @param uid
     *            需要移除的用户ID
     */
    public ATask destroyFollowersTask(String uid, String cpt, String cptcode, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().destroyFollowersParam(uid, cpt,
                cptcode);
        ATask task = createTask();
        task.execute("friendships/followers_destroy", param, listener);
        return task;
    }

    /**
     * 更新当前登录用户所关注的某个好友的备注信息
     * 
     * @param uid
     *            需要修改备注信息的用户ID
     * @param remark
     *            备注信息。需要URL Encode
     */
    public ATask updateRemarkTask(String uid, String remark, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().updateRemarkParam(uid, remark);
        ATask task = createTask();
        task.execute("friendships/remark_update", param, listener);
        return task;
    }

    /**
     * 返回与关键字相匹配的微博
     * 
     * @param q
     *            搜索的关键字
     */
    public ATask searchStatusesTask(String q, int page, int count, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().searchStatusesParam(q, page, count);
        ATask task = createTask();
        task.execute("search/statuses", param, listener);
        return task;
    }

    /**
     * 返回与关键字相匹配的微博用户
     * 
     * @param q
     *            搜索的关键字
     */
    public ATask searchUsersTask(String q, int page, int count, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().searchUsersParam(q, page, count);
        ATask task = createTask();
        task.execute("search/users", param, listener);
        return task;
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
    public ATask searchAtUsersTask(String q, int count, int type, int range, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().searchAtUsersParam(q, count, type,
                range);
        ATask task = createTask();
        task.execute("search/at_users", param, listener);
        return task;
    }

    /**
     * 获取指定用户的好友分组列表
     * 
     * @param uid
     *            如果为空，返回当前用户的分组列表
     */
    public ATask getGroupTask(String uid, String mode, int is_encoded, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupParam(uid, mode, is_encoded);
        ATask task = createTask();
        task.execute("groups", param, listener);
        return task;
    }

    public ATask getGroupTimeLineTask(String uid, String list_id, String since_id, String max_id, int count,
            int page, int base_app, int feature, int is_encoded, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupTimeLineParam(uid, list_id,
                since_id, max_id, count, page, base_app, feature, is_encoded);
        ATask task = createTask();
        task.execute("groups/timeline", param, listener);
        return task;
    }

    public ATask getGroupMembersTask(String uid, int list_id, int count, int cursor,
            int is_encoded, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupMembersParam(uid, list_id,
                count, cursor, is_encoded);
        ATask task = createTask();
        task.execute("groups/members", param, listener);
        return task;
    }

    public ATask getGroupListedTask(String uids, String owner_uid, int is_encoded,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupListedParam(uids, owner_uid,
                is_encoded);
        ATask task = createTask();
        task.execute("groups/listed", param, listener);
        return task;
    }

    public ATask getGroupsAllMembersTask(String uid, int list_id, int is_encoded,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupAllMembersParam(uid, list_id,
                is_encoded);
        ATask task = createTask();
        task.execute("groups/allmembers", param, listener);
        return task;
    }

    public ATask getGroupUserAllTask(String uid, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupUserAllParam(uid);
        ATask task = createTask();
        task.execute("groups/groupuserall", param, listener);
        return task;
    }

    public ATask getGroupMembersAddTask(String uid, int list_id, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupMembersAddParam(uid, list_id);
        ATask task = createTask();
        task.execute("groups/members_add", param, listener);
        return task;
    }

    public ATask getGroupMembersDestroyTask(String uid, int list_id, int is_encoded,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupMembersDestroyParam(uid,
                list_id, is_encoded);
        ATask task = createTask();
        task.execute("groups/members_destroy", param, listener);
        return task;
    }

    public ATask getGroupCreateTask(String name, int list_id, String description, int is_encoded,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupUpdateParam(name, list_id,
                description, is_encoded);
        ATask task = createTask();
        task.execute("groups/update", param, listener);
        return task;
    }

    public ATask getGroupUpdateTask(String name, String mode, String description, int is_encoded,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupCreateParam(name, mode,
                description, is_encoded);
        ATask task = createTask();
        task.execute("groups/create", param, listener);
        return task;
    }

    public ATask getGroupDealTask(int deals, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getGroupDealsParam(deals);
        ATask task = createTask();
        task.execute("groups/groupdeal", param, listener);
        return task;
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
    public ATask getNearByPositionTask(double lat, double lon, int rang, String q, int page, int count,
            int category, int sort, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getNearByPositionParam(lat, lon, rang,
                q, page, count, category, sort);
        ATask task = createTask();
        task.execute("place/nearby_pois", param, listener);
        return task;
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
    public ATask getNearByUsersTask(double lat, double lon, int rang, int page, int count,
            int starttime, int endtime, int sort, int trim_status, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getNearByUsersParam(lat, lon, rang,
                page, count, starttime, endtime, sort, trim_status);
        ATask task = createTask();
        task.execute("place/nearby_users", param, listener);
        return task;
    }
    
    
    public ATask getNearByTimeLine(double lat, double lon, int rang, 
    		int page, int count, long starttime, long endtime, int sort, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance()
        		.getNearByTimeLineParam(lat, lon, rang, page, count, starttime, endtime, sort);
        ATask task = createTask();
        task.execute("place/nearby_timeline", param, listener);
        return task;
    }
    
    public ATask searchByGeo(String q, String category, String coordinate, 
    		String pid, String city, int range, int page, int count, ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance()
        		.searchByGeoParam(q, category, coordinate, pid, city, range, page, count);
        ATask task = createTask();
        task.execute("location/pois/search/by_geo", param, listener);
        return task;
    }

    /**
     * 1 获取九宫格图标（含国际版接口）
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
    public ATask getSquaredTask(String from, String lang, String version, int filter, String ua,
            ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getSquaredParam(from, lang, version,
                filter, ua);
        ATask task = createTask();
        task.execute("client/squared", param, listener);
        return task;
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
    public ATask getLatestVersionTask(String from, String wm, String manual, 
    		ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getLatestVersionParam(from, wm, manual);
        ATask task = createTask();
        task.execute("client/version", param, listener);
        return task;
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
    public ATask getMapImageTask(String center_coordinate, String coordinates, String size, 
    		String format, int zoom, String offset_x, String offset_y, String font,
    		String lines, String polygons, boolean scale, boolean traffic, 
    		ATaskListener listener) {
        RequestParam param = WeiboClientHelper.getInstance().getMapImageParam(
        		center_coordinate, coordinates, size, format, zoom, offset_x, offset_y, 
        		font, lines, polygons, scale, traffic);
        ATask task = createTask();
        task.execute("location/base/get_map_image", param, listener);
        return task;
    }

    public ATask download(String url, CacheStrategy cs, IDownloadCallback callback) {
        AsyncDownloadTask task = new AsyncDownloadTask(mContext);
        task.setNetRequestStrategy(mRequestStrategy);
        task.setCacheStrategy(cs);
        task.download(url, callback);
        return task;
    }

}

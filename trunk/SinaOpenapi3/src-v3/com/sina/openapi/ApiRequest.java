package com.sina.openapi;

import static com.clark.func.Functions.asserts;
import static com.clark.func.Functions.closeQuietly;
import static com.clark.func.Functions.isBlank;
import static com.clark.func.Functions.isNotBlank;
import static com.clark.func.Functions.isNotEmpty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuth;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

public class ApiRequest {
    private static final String host = "api.t.sina.com.cn";
    private static final String scheme = "http";
    private static final NumberFormat DECIMAL_FORMAT = new DecimalFormat(
            "#0.0;-#0.0");

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
     */
    public static HttpUriRequest getFriendsTimeline(String since_id,
            String max_id, int count, int page, int baseApp, int feature) {
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (isNotBlank(since_id)) {
            parameters.add(new BasicNameValuePair("since_id", since_id));
        }
        if (isNotBlank(max_id)) {
            parameters.add(new BasicNameValuePair("max_id", max_id));
        }
        if (count > 0) {
            parameters.add(new BasicNameValuePair("count", "" + count));
        }
        if (page > 0) {
            parameters.add(new BasicNameValuePair("page", "" + page));
        }
        if (baseApp >= 0 && baseApp <= 1) {
            parameters.add(new BasicNameValuePair("base_app", "" + baseApp));
        }
        if (feature >= 0 && feature <= 4) {
            parameters.add(new BasicNameValuePair("feature", "" + feature));
        }
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/friends_timeline.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getPublicTimeline(int count, int base_app) {
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (count > 0) {
            parameters.add(new BasicNameValuePair("count", "" + count));
        }
        if (base_app >= 0 && base_app <= 1) {
            parameters.add(new BasicNameValuePair("base_app", "" + ""
                    + base_app));
        }
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/public_timeline.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getUserTimeline(String id, String user_id,
            String screen_name, String since_id, String max_id, int count,
            int page, int baseApp, int feature) {
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (isNotBlank(id)) {
            parameters.add(new BasicNameValuePair("id", id));
        }
        if (isNotBlank(user_id)) {
            parameters.add(new BasicNameValuePair("user_id", user_id));
        }
        if (isNotBlank(screen_name)) {
            parameters.add(new BasicNameValuePair("screen_name", screen_name));
        }
        if (isNotBlank(since_id)) {
            parameters.add(new BasicNameValuePair("since_id", since_id));
        }
        if (isNotBlank(max_id)) {
            parameters.add(new BasicNameValuePair("max_id", max_id));
        }
        if (count > 0) {
            parameters.add(new BasicNameValuePair("count", "" + count));
        }
        if (page > 0) {
            parameters.add(new BasicNameValuePair("page", "" + page));
        }
        if (baseApp >= 0 && baseApp <= 1) {
            parameters.add(new BasicNameValuePair("base_app", "" + baseApp));
        }
        if (feature >= 0 && feature <= 4) {
            parameters.add(new BasicNameValuePair("feature", "" + feature));
        }
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/user_timeline.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getMentionsTimeline(String since_id,
            String max_id, int count, int page) {
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (isNotBlank(since_id)) {
            parameters.add(new BasicNameValuePair("since_id", since_id));
        }
        if (isNotBlank(max_id)) {
            parameters.add(new BasicNameValuePair("max_id", max_id));
        }
        if (count > 0) {
            parameters.add(new BasicNameValuePair("count", "" + count));
        }
        if (page > 0) {
            parameters.add(new BasicNameValuePair("page", "" + page));
        }
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/mentions.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getCommentsTimeline(String since_id,
            String max_id, int count, int page) {
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (isNotBlank(since_id)) {
            parameters.add(new BasicNameValuePair("since_id", since_id));
        }
        if (isNotBlank(max_id)) {
            parameters.add(new BasicNameValuePair("max_id", max_id));
        }
        if (count > 0) {
            parameters.add(new BasicNameValuePair("count", "" + count));
        }
        if (page > 0) {
            parameters.add(new BasicNameValuePair("page", "" + page));
        }
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/comments_timeline.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getCommentsByMe(String since_id,
            String max_id, int count, int page) {
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (isNotBlank(since_id)) {
            parameters.add(new BasicNameValuePair("since_id", since_id));
        }
        if (isNotBlank(max_id)) {
            parameters.add(new BasicNameValuePair("max_id", max_id));
        }
        if (count > 0) {
            parameters.add(new BasicNameValuePair("count", "" + count));
        }
        if (page > 0) {
            parameters.add(new BasicNameValuePair("page", "" + page));
        }
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/comments_by_me.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getCommentToMe(String since_id, String max_id,
            int count, int page) {
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (isNotBlank(since_id)) {
            parameters.add(new BasicNameValuePair("since_id", since_id));
        }
        if (isNotBlank(max_id)) {
            parameters.add(new BasicNameValuePair("max_id", max_id));
        }
        if (count > 0) {
            parameters.add(new BasicNameValuePair("count", "" + count));
        }
        if (page > 0) {
            parameters.add(new BasicNameValuePair("page", "" + page));
        }
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/comments_to_me.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getCommentsOfTheStatus(String id_, int count,
            int page) {
        asserts(isNotBlank(id_));

        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (count > 0) {
            parameters.add(new BasicNameValuePair("count", "" + count));
        }
        if (page > 0) {
            parameters.add(new BasicNameValuePair("page", "" + page));
        }
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/comments.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getFsCsCountsOfStatuses(String... ids_) {
        asserts(isNotEmpty(ids_));

        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        StringBuilder ids = new StringBuilder();
        for (String id : ids_) {
            if (ids.length() > 0) {
                ids.append(",");
            }
            ids.append(id);
        }
        parameters.add(new BasicNameValuePair("ids", ids.toString()));
        String query = URLEncodedUtils.format(parameters, "UTF-8");
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/counts.json", query, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest getUnreadNumOfWeiboUser() {
        HttpGet httpGet = new HttpGet();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/unread.json", null, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest showOneStatus(String blogId_) {
        asserts(isNotBlank(blogId_));

        HttpGet httpGet = new HttpGet();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1, "/statuses/show/"
                    + blogId_ + ".json", null, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest verifyAccountCredentials() {
        HttpGet httpGet = new HttpGet();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/account/verify_credentials.json", null, null);
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
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
    public static HttpUriRequest updateAccountProfileImage(InputStream input) {
        asserts(input != null);

        HttpPost httpPost = new HttpPost();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/account/update_profile_image.json", null, null);
            httpPost.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        MultipartEntity entity = new MultipartEntity(
                HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.addPart("image", new InputStreamBody(input, "image/*",
                "image.png"));
        httpPost.setEntity(entity);
        return httpPost;
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
    public static HttpUriRequest updateAccountProfile(String name,
            String gender, int province, int city, String description) {
        HttpPost httpPost = new HttpPost();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/account/update_profile.json", null, null);
            httpPost.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        if (isNotBlank(name)) {
            parameters.add(new BasicNameValuePair("name", name));
        }
        if ("m".equals(gender) || "f".equals(gender)) {
            parameters.add(new BasicNameValuePair("gender", gender));
        }
        if (province > 0) {
            parameters.add(new BasicNameValuePair("province", "" + province));
        }
        if (city > 0) {
            parameters.add(new BasicNameValuePair("city", "" + city));
        }
        if (isNotBlank(description)) {
            parameters.add(new BasicNameValuePair("description", description));
        }
        try {
            MyUrlEncodedFormEntity entity = new MyUrlEncodedFormEntity(
                    parameters, "UTF-8");
            httpPost.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return httpPost;
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
    public static HttpUriRequest destroyOneStatus(String blogId_) {
        asserts(isNotBlank(blogId_));

        HttpDelete httpDelete = new HttpDelete();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1, "/statuses/destroy/"
                    + blogId_ + ".json", null, null);
            httpDelete.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpDelete;
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
    public static HttpUriRequest updateStatus(String text_,
            String in_reply_to_status_id, double lat, double lon) {
        asserts(isNotBlank(text_));

        HttpPost httpPost = new HttpPost();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/update.json", null, null);
            httpPost.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("status", OAuth
                .percentEncode(text_)));
        if (isNotBlank(in_reply_to_status_id)) {
            parameters.add(new BasicNameValuePair("in_reply_to_status_id",
                    in_reply_to_status_id));
        }
        if (Math.abs(lat - 0.) > 0.0001) {
            parameters.add(new BasicNameValuePair("lat", DECIMAL_FORMAT
                    .format(lat)));
        }
        if (Math.abs(lon - 0.) > 0.0001) {
            parameters.add(new BasicNameValuePair("long", DECIMAL_FORMAT
                    .format(lon)));
        }
        try {
            MyUrlEncodedFormEntity entity = new MyUrlEncodedFormEntity(
                    parameters, "UTF-8");
            httpPost.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return httpPost;
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
     * @throws IOException
     * @throws WeiboOpenAPIException
     *             如果没有登录或超过发布上限，将返回403错误
     *             系统将忽略重复发布的信息。每次发布将比较最后一条发布消息，如果一样将被忽略。因此用户不能连续提交相同信息
     *             。发布成功返回发布的信息ID,否则返回为空。 如果使用的Oauth认证，图片参数pic不参与签名。
     */
    public static HttpUriRequest uploadStatus(String text_, InputStream input_,
            double lat, double lon) throws IOException {
        asserts(input_ != null);
        asserts(isNotBlank(text_));

        HttpPost httpPost = new HttpPost();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/upload.json", null, null);
            httpPost.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("pic", new InputStreamBody(input_, "image/*",
                "image.png"));
        try {
            entity.addPart("status",
                    new StringBody(text_, Charset.forName("UTF-8")));
            if (Math.abs(lat - 0.) > 0.0001) {
                entity.addPart("lat", new StringBody(
                        DECIMAL_FORMAT.format(lat), Charset.forName("UTF-8")));
            }
            if (Math.abs(lon - 0.) > 0.0001) {
                entity.addPart(
                        "long",
                        new StringBody(DECIMAL_FORMAT.format(lon), Charset
                                .forName("UTF-8")));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                512);
        entity.writeTo(byteArrayOutputStream);
        closeQuietly(byteArrayOutputStream);
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(
                byteArrayOutputStream.toByteArray());
        byteArrayEntity.setContentType(entity.getContentType());
        byteArrayEntity.setContentEncoding(entity.getContentEncoding());
        httpPost.setEntity(byteArrayEntity);
        return httpPost;
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
    public static HttpUriRequest repostStatus(String id_, String text,
            int is_comment) {
        asserts(isNotBlank(id_));

        HttpPost httpPost = new HttpPost();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/repost.json", null, null);
            httpPost.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("id", id_));
        if (isNotBlank(text)) {
            parameters.add(new BasicNameValuePair("status", text));
        }
        if (is_comment >= 0 && is_comment <= 3) {
            parameters
                    .add(new BasicNameValuePair("is_comment", "" + is_comment));
        }
        try {
            MyUrlEncodedFormEntity entity = new MyUrlEncodedFormEntity(
                    parameters, "UTF-8");
            httpPost.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return httpPost;
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
    public static HttpUriRequest commentStatus(String id, String comment,
            String cid, int without_mention, int comment_ori, int is_repost) {
        asserts(isNotBlank(id));
        asserts(isNotBlank(comment));

        HttpPost httpPost = new HttpPost();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/comment.json", null, null);
            httpPost.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("id", id));
        parameters.add(new BasicNameValuePair("comment", comment));
        if (isNotBlank(cid)) {
            parameters.add(new BasicNameValuePair("cid", cid));
        }
        if (without_mention >= 0 && without_mention <= 1) {
            parameters.add(new BasicNameValuePair("without_mention", ""
                    + without_mention));
        }
        if (comment_ori >= 0 && comment_ori <= 1) {
            parameters.add(new BasicNameValuePair("comment_ori", ""
                    + comment_ori));
        }
        if (is_repost >= 0 && is_repost <= 1) {
            parameters.add(new BasicNameValuePair("is_repost", "" + is_repost));
        }
        try {
            MyUrlEncodedFormEntity entity = new MyUrlEncodedFormEntity(
                    parameters, "UTF-8");
            httpPost.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return httpPost;
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
    public static HttpUriRequest destroyMyCommentOfOneStatus(String _id) {
        asserts(isNotBlank(_id));

        HttpDelete httpDelete = new HttpDelete();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/comment_destroy/" + _id + ".json", null, null);
            httpDelete.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpDelete;
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
    public static HttpUriRequest destroyMyCommentsOfOneStatus(String... ids) {
        asserts(isNotEmpty(ids));

        HttpDelete httpDelete = new HttpDelete();
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        StringBuilder builder = new StringBuilder();
        for (String id : ids) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(id);
        }
        parameters.add(new BasicNameValuePair("ids", parameters.toString()));
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/comment/destroy_batch.json",
                    URLEncodedUtils.format(parameters, "UTF-8"), null);
            httpDelete.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpDelete;
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
    public static HttpUriRequest replyCommentOfOneStatus(String cid,
            String comment, String blogId, int without_mention) {
        asserts(isNotBlank(cid));
        asserts(isNotBlank(comment));
        asserts(isNotBlank(blogId));

        HttpPost httpPost = new HttpPost();
        try {
            URI uri = URIUtils.createURI(scheme, host, -1,
                    "/statuses/reply.json", null, null);
            httpPost.setURI(uri);
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            parameters.add(new BasicNameValuePair("cid", cid));
            parameters.add(new BasicNameValuePair("id", blogId));
            parameters.add(new BasicNameValuePair("comment", comment));
            if (without_mention >= 0 && without_mention <= 1) {
                parameters.add(new BasicNameValuePair("without_mention", ""
                        + without_mention));
            }
            MyUrlEncodedFormEntity entity = new MyUrlEncodedFormEntity(
                    parameters, "UTF-8");
            httpPost.setEntity(entity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return httpPost;
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
    public static HttpUriRequest showOneUser(String _id, String user_id,
            String screen_name) {
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        if (isNotBlank(user_id)) {
            params.add(new BasicNameValuePair("user_id", user_id));
        }
        if (isNotBlank(screen_name)) {
            params.add(new BasicNameValuePair("screen_name", screen_name));
        }
        String query = URLEncodedUtils.format(params, "UTF-8");
        try {
            URI uri;
            if (isBlank(_id)) {
                uri = URIUtils.createURI(scheme, host, -1, "/users/show.json",
                        query, null);
            } else {
                uri = URIUtils.createURI(scheme, host, -1, "/users/show/" + _id
                        + ".json", query, null);
            }
            httpGet.setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return httpGet;
    }

    // -----------------------------------------------------------------
    //
    // 工具类
    //
    // -----------------------------------------------------------------
    private static class MyUrlEncodedFormEntity extends StringEntity {
        public MyUrlEncodedFormEntity(
                final List<? extends NameValuePair> parameters,
                final String encoding) throws UnsupportedEncodingException {
            super(format(parameters, encoding), encoding);
            setContentType(URLEncodedUtils.CONTENT_TYPE);
        }

        private static final String PARAMETER_SEPARATOR = "&";
        private static final String NAME_VALUE_SEPARATOR = "=";

        private static String format(
                final List<? extends NameValuePair> parameters,
                final String encoding) {
            final StringBuilder result = new StringBuilder();
            for (final NameValuePair parameter : parameters) {
                final String encodedName = encode(parameter.getName(), encoding);
                final String value = parameter.getValue();
                final String encodedValue = value != null ? encode(value,
                        encoding) : "";
                if (result.length() > 0)
                    result.append(PARAMETER_SEPARATOR);
                result.append(encodedName);
                result.append(NAME_VALUE_SEPARATOR);
                result.append(encodedValue);
            }
            return result.toString();
        }

        private static String encode(final String content, final String encoding) {
            return OAuth.percentEncode(content);
        }
    }
}

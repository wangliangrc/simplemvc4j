package com.sina.weibosdk;

import android.text.TextUtils;

import com.sina.weibosdk.exception.WeiboException;
import com.sina.weibosdk.exception.WeiboIOException;
import com.sina.weibosdk.requestparam.APIRequestParam;
import com.sina.weibosdk.requestparam.HotStatusReqParam;
import com.sina.weibosdk.requestparam.RequestParam;
import com.sina.weibosdk.requestparam.StatusReqDownParam;
import com.sina.weibosdk.requestparam.StatusReqUpParam;
import com.sina.weibosdk.security.RsaKey;

class WeiboClientHelper {

    private static WeiboClientHelper instance;

    public synchronized static WeiboClientHelper getInstance() {
        if (null == instance) {
            instance = new WeiboClientHelper();
        }
        return instance;
    }

    public RequestParam loginParam(String username, String password, String s, String cpt,
            String cptcode) throws WeiboException {
        RequestParam param = new APIRequestParam();
        param.addParam("u", username);
        try {
            param.addParam("p", new RsaKey().encrypt(password));
        } catch (Exception e) {
            throw new WeiboException(e);
        }

        param.addParam("flag", "1");
        param.addParam("s", s);
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam getPublicTimeLineParam(int page, int count) {
        StatusReqDownParam param = new StatusReqDownParam();
        param.setPage(page);
        param.setCount(count);
        // 目前接口支持不传gsid，c值，s值获取PublicTimeLine
        // String s = param.getS();
        // // 未登陆时使用public s值
        // if (TextUtils.isEmpty(s)) {
        // s =
        // WeiboSDKConfig.getInstance().getString(WeiboSDKConfig.KEY_PUBLIC_S);
        // }
        // Util.checkNullParams(s);
        // param.addParam("s", s);
        return param;
    }

    public RequestParam getFriendsTimeLineParam(String gid, int feature, int page, int count, String since_id,
            String max_id) {
        StatusReqDownParam param = new StatusReqDownParam();
        param.setGid(gid);
        if (page != 0) {
        	param.setPage(page);
        }
        if (count != 0) {
        	param.setCount(count);
        }
        if (feature != 0) {
        	param.setFeature(feature);
        }
        param.setSince_id(since_id);
        param.setMax_id(max_id);
        return param;
    }

    public RequestParam getUserTimeLineParam(String uid, int page, int count, int filter) {
        StatusReqDownParam param = new StatusReqDownParam();
        param.setUid(uid);
        if (page != 0) {
        	param.setPage(page);
        }
        if (count != 0) {
        	param.setCount(count);
        }
        param.setFilter(filter);
        return param;
    }

    public RequestParam getSingleStatusParam(String id) {
        RequestParam param = new APIRequestParam();
        param.addParam("id", id);
        return param;
    }

    public RequestParam getStatusCRNumParam(String... statusId) {
        StringBuilder idsStr = new StringBuilder();
        boolean first = true;
        for (String id : statusId) {
            if (first) {
                idsStr.append(id);
                first = false;
            } else {
                idsStr.append(",").append(id);
            }
        }
        RequestParam param = new APIRequestParam();
        param.addParam("ids", idsStr.toString());
        return param;
    }

    public RequestParam getMentionsTimelineParam(int page, int count, String since_id,
            String max_id, int filter_by_author, int filter_by_type) {
        StatusReqDownParam param = new StatusReqDownParam();
        if (page != 0) {
        	param.setPage(page);
        }
        if (count != 0) {
        	param.setCount(count);
        }
        param.setSince_id(since_id);
        param.setMax_id(max_id);
        param.setFilter_by_author(filter_by_author);
        param.setFilter_by_type(filter_by_type);
        return param;
    }

    public RequestParam getHotStatusDailyParam(int app_base, int count, int is_encoded, int picsize) {
        HotStatusReqParam param = new HotStatusReqParam();
        param.setAppBase(app_base);
        param.setCount(count);
        param.setIsEncoded(is_encoded);
        param.setPicsize(picsize);
        return param;
    }

    public RequestParam getHotCommentDailyParam(int app_base, int count, int is_encoded, int picsize) {
        HotStatusReqParam param = new HotStatusReqParam();
        param.setAppBase(app_base);
        param.setCount(count);
        param.setIsEncoded(is_encoded);
        param.setPicsize(picsize);
        return param;
    }

    public RequestParam updateStatusParam(String status, int visible, double lat, double lon,
            String annotations, int is_encoded, String cpt, String cptcode) {
        StatusReqUpParam param = new StatusReqUpParam();
        param.setStatus(status);
        param.setVisible(visible);
        if (lat > 0) {
        	param.setLat(lat);
        }
        if (lon > 0) {
        	param.setLon(lon);
        }
        param.setAnnotations(annotations);
        param.setIsEncoded(is_encoded);
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam uploadStatusParam(String status, String picPath, double lat, double lon,
            int visible, String cpt, String cptcode) throws WeiboIOException {
        StatusReqUpParam param = new StatusReqUpParam();
        param.setStatus(status);
        param.setVisible(visible);
        if (lat > 0) {
        	param.setLat(lat);
        }
        if (lon > 0) {
        	param.setLon(lon);
        }
        param.addFile(picPath);
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam repostStatusParam(String status, String id, int is_comment,
            String annotations, int is_encoded, String cpt, String cptcode) {
        StatusReqUpParam param = new StatusReqUpParam();
        param.setStatus(status);
        param.setId(id);
        param.setIsComment(is_comment);
        param.setAnnotations(annotations);
        param.setIsEncoded(is_encoded);
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam destroyStatusParam(String id, String cpt, String cptcode) {
        RequestParam param = new APIRequestParam();
        param.addParam("id", id);
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam getFavoritesParam(int page, int count, int picsize) {
        RequestParam param = new APIRequestParam();
        if (page > 0) {
            param.addParam("page", String.valueOf(page));
        }

        if (count > 0) {
            param.addParam("count", String.valueOf(count));
        }

        if (picsize == 240 || picsize == 320 || picsize == 960) {
            param.addParam("picsize", String.valueOf(picsize));
        }
        return param;
    }

    public RequestParam createFavoriteParam(String id) {
        RequestParam param = new APIRequestParam();
        param.addParam("id", id);
        return param;
    }

    public RequestParam deleteFavoriteParam(String id) {
        RequestParam param = new APIRequestParam();
        param.addParam("id", id);
        return param;
    }

    public RequestParam getTopicListParam(String uid, int page, boolean has_num, int count) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("uid", uid);
        if (page != 0) {
            param.addParam("page", "" + page);
        }
        if (has_num) {
            param.addParam("has_num", "" + 1);
        } else {
            param.addParam("has_num", "" + 0);
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        return param;
    }

    public RequestParam getHourlyTopicListParam(boolean base_app, int count) {
        APIRequestParam param = new APIRequestParam();
        if (base_app) {
            param.addParam("base_app", "1");
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        return param;
    }

    public RequestParam getDailyTopicListParam(boolean base_app, int count) {
        APIRequestParam param = new APIRequestParam();
        if (base_app) {
            param.addParam("base_app", "1");
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        return param;
    }

    public RequestParam getWeeklyTopicListParam(boolean base_app, int count) {
        APIRequestParam param = new APIRequestParam();
        if (base_app) {
            param.addParam("base_app", "1");
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        return param;
    }

    public RequestParam followTopicParam(String trend_name, String cpt, String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("trend_name", trend_name);
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam destroyTopicParam(String trend_id, String cpt, String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("trend_id", trend_id);
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam getBlackListParam(int page, int count) {
        APIRequestParam param = new APIRequestParam();
        if (page != 0) {
            param.addParam("page", "" + page);
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        return param;
    }

    public RequestParam createBlackParam(String uid) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("uid", uid);
        param.addParam("trim_user", "0");
        return param;
    }

    public RequestParam destoryBlackParam(String uid) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("uid", uid);
        param.addParam("trim_user", "0");
        return param;
    }

    public RequestParam reportParam(String url, String content, int type, int rid, int class_id) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("url", url);
        param.addParam("content", content);
        if (type != 0) {
            param.addParam("type", "" + type);
        }
        if (rid != 0) {
            param.addParam("rid", content);
        }
        if (class_id != 0) {
            param.addParam("class_id", content);
        }
        return param;
    }

    public RequestParam getNewestMessageParam(int page, int count, String since_id, String max_id,
            int is_encoded) {
        StatusReqDownParam param = new StatusReqDownParam();
        if (page != 0) {
            param.setPage(page);
        }
        if (count != 0) {
            param.setCount(count);
        }
        param.setSince_id(since_id);
        param.setMax_id(max_id);
        param.addParam("is_encoded", String.valueOf(is_encoded));
        return param;
    }

    public RequestParam getMessageListParam(String uid, int page, int count, String since_id,
            String max_id, int is_encoded) {
        StatusReqDownParam param = new StatusReqDownParam();
        param.addParam("uid", uid);
        if (page != 0) {
            param.setPage(page);
        }
        if (count != 0) {
            param.setCount(count);
        }
        param.setSince_id(since_id);
        param.setMax_id(max_id);
        param.addParam("is_encoded", String.valueOf(is_encoded));
        return param;
    }

    public RequestParam getMessageUserListParam(int count, int cursor, int refresh_user_count,
            int is_encoded) {
        APIRequestParam param = new APIRequestParam();
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        if (cursor != 0) {
            param.addParam("cursor", "" + cursor);
        }
        if (refresh_user_count != 0) {
            param.addParam("refresh_user_count", "" + refresh_user_count);
        }
        param.addParam("is_encoded", String.valueOf(is_encoded));
        return param;
    }

    public RequestParam createMessageParam(String text, String uid, String screen_name,
            String fids, String id, int is_encoded, String cpt, String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("text", text);
        param.addParam("uid", uid);
        param.addParam("screen_name", screen_name);
        param.addParam("fids", fids);
        param.addParam("id", id);
        param.addParam("is_encoded", String.valueOf(is_encoded));
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam destroyMessageParam(String id, int is_encoded, String cpt, String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("id", id);
        param.addParam("is_encoded", String.valueOf(is_encoded));
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam destroyBatchMessageByIdParam(String ids, int is_encoded, String cpt,
            String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("ids", ids);
        param.addParam("is_encoded", String.valueOf(is_encoded));
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam destroyBatchMessageByUidParam(String uid, int is_encoded, String cpt,
            String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("uid", uid);
        param.addParam("is_encoded", String.valueOf(is_encoded));
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam getCommentsParam(String id, int page, int count, String since_id,
            String max_id, int filter_by_author, int is_encoded) {
        APIRequestParam param = new APIRequestParam();
        if (!TextUtils.isEmpty(id)) {
            param.addParam("id", id);
        }
        if (page != 0) {
            param.addParam("page", "" + page);
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        param.addParam("since_id", since_id);
        param.addParam("max_id", max_id);
        param.addParam("filter_by_author", "" + filter_by_author);
        param.addParam("is_encoded", "" + is_encoded);
        return param;
    }

    public RequestParam getCommentsByMeParam(int page, int count, String since_id, String max_id,
            int filter_by_source) {
        APIRequestParam param = new APIRequestParam();
        if (page != 0) {
            param.addParam("page", "" + page);
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        param.addParam("since_id", since_id);
        param.addParam("max_id", max_id);
        param.addParam("filter_by_source", "" + filter_by_source);
        return param;
    }

    public RequestParam getCommentsToMeParam(int page, int count, String since_id, String max_id,
            int filter_by_source) {
        APIRequestParam param = new APIRequestParam();
        if (page != 0) {
            param.addParam("page", "" + page);
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        param.addParam("since_id", since_id);
        param.addParam("max_id", max_id);
        param.addParam("filter_by_source", "" + filter_by_source);
        return param;
    }

    public RequestParam getCommentsMentionMeParam(int page, int count, String since_id,
            String max_id, int filter_by_author, int filter_by_source) {
        APIRequestParam param = new APIRequestParam();
        if (page != 0) {
            param.addParam("page", "" + page);
        }
        if (count != 0) {
            param.addParam("count", "" + count);
        }
        param.addParam("since_id", since_id);
        param.addParam("max_id", max_id);
        param.addParam("filter_by_author", "" + filter_by_author);
        param.addParam("filter_by_source", "" + filter_by_source);
        return param;
    }

    public RequestParam createCommentParam(String id, String comment, boolean comment_ori,
            String cpt, String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("id", id);
        param.addParam("comment", comment);
        if (comment_ori) {
            param.addParam("comment_ori", "1");
        } else {
            param.addParam("comment_ori", "0");
        }
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam replyCommentParam(String cid, String id, String comment,
            int without_mention, boolean comment_ori, String cpt, String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("cid", cid);
        param.addParam("id", id);
        param.addParam("comment", comment);
        param.addParam("without_mention", "" + without_mention);
        if (comment_ori) {
            param.addParam("comment_ori", "1");
        } else {
            param.addParam("comment_ori", "0");
        }
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam destroyCommentParam(String cid, String cpt, String cptcode) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("cid", cid);
        param.addParam("cpt", cpt);
        param.addParam("code", cptcode);
        return param;
    }

    public RequestParam getUsersShowParam(String uid, String screen_name) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("uid", uid);
        param.addParam("screen_name", screen_name);
        return param;
    }

    public RequestParam updateUserInfoParam(String screen_name, String gender, String description) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("screen_name", screen_name);
        param.addParam("gender", gender);
        param.addParam("description", description);
        return param;
    }

    public RequestParam updateUserPortraitParam(String image, int trim_user)
            throws WeiboIOException {
        APIRequestParam params = new APIRequestParam();
        params.addParam("trim_user", String.valueOf(trim_user));
        params.addFile(image, "image");
        return params;
    }

    public RequestParam getFriendsParam(String uid, int page, int count, int sourt, int trim_status) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("uid", uid);
        param.addParam("page", String.valueOf(page));
        param.addParam("count", String.valueOf(count));
        param.addParam("sourt", String.valueOf(sourt));
        param.addParam("trim_status", String.valueOf(trim_status));
        return param;
    }

    public RequestParam getFollowersParam(String uid, int page, int count, int sourt,
            int trim_status) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("page", String.valueOf(page));
        params.addParam("count", String.valueOf(count));
        params.addParam("sourt", String.valueOf(sourt));
        params.addParam("trim_status", String.valueOf(trim_status));
        return params;
    }

    public RequestParam createFriendshipParam(String uid, String cpt, String cptcode) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("cpt", cpt);
        params.addParam("code", cptcode);
        return params;
    }

    public RequestParam destroyFriendshipParam(String uid, String cpt, String cptcode) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("cpt", cpt);
        params.addParam("code", cptcode);
        return params;
    }

    public RequestParam destroyFollowersParam(String uid, String cpt, String cptcode) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("cpt", cpt);
        params.addParam("code", cptcode);
        return params;
    }

    public RequestParam updateRemarkParam(String uid, String remark) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("remark", remark);
        return params;
    }

    public RequestParam searchStatusesParam(String q, int page, int count) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("q", q);
        if (page != 0) {
        	params.addParam("page", String.valueOf(page));
        }
        if (count != 0) {
        	params.addParam("count", String.valueOf(count));
        }
        return params;
    }

    public RequestParam searchUsersParam(String q, int page, int count) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("q", q);
        params.addParam("page", String.valueOf(page));
        params.addParam("count", String.valueOf(count));
        return params;
    }

    public RequestParam searchAtUsersParam(String q, int count, int type, int range) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("q", q);
        params.addParam("count", String.valueOf(count));
        params.addParam("type", String.valueOf(type));
        params.addParam("range", String.valueOf(range));
        return params;
    }

    public RequestParam getGroupParam(String uid, String mode, int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("mode", mode);
        params.addParam("is_encoded", String.valueOf(is_encoded));
        params.addParam("p", "feed");
        return params;
    }

    public RequestParam getGroupTimeLineParam(String uid, String list_id, String since_id, String max_id,
            int count, int page, int base_app, int feature, int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("list_id", list_id);
        params.addParam("since_id", since_id);
        params.addParam("max_id", max_id);
        if (count != 0) {
        	params.addParam("count", String.valueOf(count));
        }
        if (page != 0) {
        	params.addParam("page", String.valueOf(page));
        }
        params.addParam("base_app", String.valueOf(base_app));
        params.addParam("feature", String.valueOf(feature));
        params.addParam("is_encoded", String.valueOf(is_encoded));
        return params;
    }

    public RequestParam getGroupMembersParam(String uid, long list_id, int count, int cursor,
            int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("list_id", String.valueOf(list_id));
        params.addParam("count", String.valueOf(count));
        params.addParam("cursor", String.valueOf(cursor));
        params.addParam("is_encoded", String.valueOf(is_encoded));
        return params;
    }

    public RequestParam getGroupListedParam(String uids, String owner_uid, int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uids", uids);
        params.addParam("owner_uid", owner_uid);
        params.addParam("is_encoded", String.valueOf(is_encoded));
        return params;
    }

    public RequestParam getGroupAllMembersParam(String uid, long list_id, int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("list_id", String.valueOf(list_id));
        params.addParam("is_encoded", String.valueOf(is_encoded));
        return params;
    }

    public RequestParam getGroupUserAllParam(String uid) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        return params;
    }

    public RequestParam getGroupMembersAddParam(String uid, long list_id) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("list_id", String.valueOf(list_id));
        return params;
    }

    public RequestParam getGroupMembersDestroyParam(String uid, int list_id, int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("uid", uid);
        params.addParam("list_id", String.valueOf(list_id));
        params.addParam("is_encoded", String.valueOf(is_encoded));
        return params;
    }

    public RequestParam getGroupCreateParam(String name, String mode, String description,
            int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("name", name);
        params.addParam("mode", mode);
        params.addParam("description", description);
        params.addParam("is_encoded", String.valueOf(is_encoded));
        return params;
    }

    public RequestParam getGroupUpdateParam(String name, long list_id, String description,
            int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("name", name);
        params.addParam("list_id", String.valueOf(list_id));
        params.addParam("description", description);
        params.addParam("is_encoded", String.valueOf(is_encoded));
        return params;
    }

    public RequestParam getGroupDestroyParam(long list_id, int is_encoded) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("list_id", String.valueOf(list_id));
        params.addParam("is_encoded", String.valueOf(is_encoded));
        return params;
    }

    public RequestParam getGroupDealsParam(int deals) {
        APIRequestParam params = new APIRequestParam();
        params.addParam("deals", String.valueOf(deals));
        return params;
    }

    public RequestParam getNearByPositionParam(double lat, double lon, int rang, String q, int page,
            int count, int category, int sort) {
        APIRequestParam param = new APIRequestParam();
        if (lat > 0) {
        	param.addParam("lat", "" + lat);
        }
        if (lon > 0) {
        	param.addParam("long", "" + lon);
        }
        if (rang != 0) {
        	param.addParam("rang", "" + rang);
        }
        param.addParam("q", q);
        if (page != 0) {
        	param.addParam("page", "" + page);
        }
        if (count != 0) {
        	param.addParam("count", "" + count);
        }
        if (category != 0) {
        	param.addParam("category", "" + category);
        }
        if (sort != 0) {
        	param.addParam("sort", "" + sort);
        }
        return param;
    }
    
    public RequestParam searchByGeoParam(String q, String category, String coordinate, 
    		String pid, String city, int range, int page, int count) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("q", q);
        param.addParam("category", category);
        param.addParam("coordinate", coordinate);
        param.addParam("pid", pid);
        param.addParam("city", city);
        if (range != 0) {
        	param.addParam("range", "" + range);
        }
        if (page != 0) {
        	param.addParam("page", "" + page);
        }
        if (count != 0) {
        	param.addParam("count", "" + count);
        }
        return param;
    }

    public RequestParam getNearByTimeLineParam(double lat, double lon, 
    		int rang, int page, int count, long starttime, long endtime, int sort) {
    	APIRequestParam param = new APIRequestParam();
        if (lat > 0) {
        	param.addParam("lat", "" + lat);
        }
        if (lon > 0) {
        	param.addParam("long", "" + lon);
        }
        if (rang != 0) {
        	param.addParam("rang", "" + rang);
        }
        if (page != 0) {
        	param.addParam("page", "" + page);
        }
        if (count != 0) {
        	param.addParam("count", "" + count);
        }
        if (starttime != 0) {
        	param.addParam("starttime", "" + starttime);
        }
        if (endtime != 0) {
        	param.addParam("endtime", "" + endtime);
        }
        if (sort != 0) {
        	param.addParam("sort", "" + sort);
        }
        return param;
    	
    }
    
    public RequestParam getNearByUsersParam(double lat, double lon, int rang, int page, int count,
            long starttime, long endtime, int sort, int trim_status) {
        APIRequestParam param = new APIRequestParam();
        if (lat > 0) {
        	param.addParam("lat", "" + lat);
        }
        if (lon > 0) {
        	param.addParam("long", "" + lon);
        }
        if (rang != 0) {
        	param.addParam("rang", "" + rang);
        }
        if (page != 0) {
        	param.addParam("page", "" + page);
        }
        if (count != 0) {
        	param.addParam("count", "" + count);
        }
        if (starttime != 0) {
        	param.addParam("starttime", "" + starttime);
        }
        if (endtime != 0) {
        	param.addParam("endtime", "" + endtime);
        }
        if (sort != 0) {
        	param.addParam("sort", "" + sort);
        }
        if (trim_status != 0) {
        	param.addParam("trim_status", "" + trim_status);
        }
        return param;
    }

    public RequestParam getSquaredParam(String from, String lang, String version, int filter,
            String ua) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("from", from);
        param.addParam("lang", lang);
        param.addParam("version", version);
        param.addParam("filter", String.valueOf(filter));
        param.addParam("ua", ua);
        return param;
    }
    
    public RequestParam getLatestVersionParam(String from, String wm, String manual) {
        APIRequestParam param = new APIRequestParam();
        param.addParam("from", from);
        param.addParam("wm", wm);
        param.addParam("manual", manual);
        return param;
    }
    
    public RequestParam getMapImageParam(String center_coordinate, String coordinates, String size, 
    		String format, int zoom, String offset_x, String offset_y, String font,
    		String lines, String polygons, boolean scale, boolean traffic) {
    	APIRequestParam param = new APIRequestParam();
    	param.addParam("center_coordinate", center_coordinate);
    	param.addParam("coordinates", coordinates);
    	param.addParam("size", size);
    	param.addParam("format", format);
    	if (zoom != 0) {
    		param.addParam("zoom", String.valueOf(zoom));
    	}
    	param.addParam("offset_x", offset_x);
    	param.addParam("offset_y", offset_y);
    	param.addParam("font", font);
    	param.addParam("lines", lines);
    	param.addParam("polygons", polygons);
    	param.addParam("scale", String.valueOf(scale));
    	param.addParam("traffic", String.valueOf(traffic));
    	return param;
    }

}
